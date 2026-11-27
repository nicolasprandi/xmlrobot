package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import at.nicolas.x.int_specs.Commons;

public class Step2Mig1Search {
	public static int run( IntParas iParas ,
			Mp parentMp, int theround) throws Exception {
		int a;
		try {
			a = runThrowing(iParas,parentMp,theround);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw ( e );
		}
		return a;
	}
	
	public static int runThrowing( IntParas iParas ,
			Mp parentMp, int theround) throws Exception {
		/*[requo-timer]*/
		/*[requo-customer]*/
		boolean isPostgres = iParas.isPostgres;
		Connection conn = iParas.getConn();
		int commmitSize = iParas.getCommitSize();
		int current_max_mig_obj_id =0;	 
		{
			// mig_obj_id die hoechste
		    Statement stmt = null;
		    ResultSet rs = null;
		    
		    Mp mpMax = iParas.mm.beginMeasurePoint(parentMp, "select_max");
	        
			try { 
							
				stmt = iParas.getConn().createStatement();
				rs = stmt.executeQuery(
						"select ( select max(mig_obj_id) from mig_obj) as  max_mig_obj_id, "+
						" ( select max(mig_obj_id) from mig_obj_hist) as max_mig_obj_id_hist;" );
				
				
				while (rs.next()) {
					int max_mig_obj_id = rs.getInt("max_mig_obj_id");	
					if ( max_mig_obj_id > current_max_mig_obj_id)
						current_max_mig_obj_id = max_mig_obj_id;
					
					int max_mig_obj_id_hist = rs.getInt("max_mig_obj_id_hist");
					if ( max_mig_obj_id_hist > current_max_mig_obj_id)
						current_max_mig_obj_id = max_mig_obj_id_hist;											
					
				}
			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "2Mig1Search.select max");				
  
			} finally {
			         
			    
			    try { if (rs != null) rs.close(); } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    
			    mpMax.finalizeAndPrint(); 

			}
					
		}
		
			

		// int statisticDelWert = 0;
			
		{       
			iParas.logPrint("Started to search for migration-triggers ...");
		    //Statement stmt = null;
		    ResultSet rs = null;
		    PreparedStatement insertPs = null;
		    Mp mpReq = iParas.mm.beginMeasurePoint(parentMp, "get_migrations");
			int commitCount = 0;
			int statisticCount = 0;
			int reportCount = 0;
			int reportSize = iParas.getReportSize();				        
			Statement stmt =null;
			try { 
							
				//stmt = iParas.getConn().createStatement();
				String queryTextToDo = null;
				
				if ( isPostgres ) 
					queryTextToDo = getPostgresThings ;
				else {
					if ( iParas.hasPronto1() )
						queryTextToDo = getMySqlThingsPronto1 ;						
					else
						queryTextToDo = getMySqlThings ;
						
				}
				/* System.out.println("");
				System.out.println(queryTextToDo);
				System.out.println("");*/
				stmt = iParas.getConn().createStatement();
				rs= iParas.doRunAndExplainQuery("3.1", "get_migrations","Migrations-Pruefung" , queryTextToDo,
						stmt,
						//null,
						iParas.sx_MigProduceMigs,mpReq,iParas.ix_sx_MigProduceMigs,true);				
				//if ( commitCount == statisticCount) throw new Exception("danke");

				 insertPs = conn.prepareStatement(
						" insert into mig_obj (uk, obj_typ, uk_attr_usg_id, uk_inhalt, master_obj_id, slave_obj_id, mig_obj_id, slave_wert_id ) values (?,?,?,?,?,?,?,?);");
	
				
				// gruppe uk
				String last_uk = "";
				int seq_uk = 0;
				
				// aktuelle gruppe
				int obj_id_master = -1;
				//int uk_attr_usg_id_master = -1;
				//String uk_inhalt_master;
				
				// datenfelder aus dem fetch			
				String uk = null;
				int obj_id = -1;
				// int obj_typ = -1;
				// int attr_id = -1;
				// int attr_typ = -1;
				// int wert_id = -1;
				int uk_attr_usg_id = -1;
				String uk_inhalt = null;	
				int obj_typ = -1;
				

				while (rs.next()) {
										
					// datenfelder rausholen
					uk = rs.getString("uk");
					
					// System.out.println("uk="+uk+".");
					if ( uk != null ) {
						
					
					obj_id = rs.getInt("obj_id");
					obj_typ = rs.getInt("obj_typ");
					// attr_id = rs.getInt("attr_id");
					// attr_typ = rs.getInt("attr_typ");
					// wert_id = rs.getInt("wert_id");
					uk_attr_usg_id = rs.getInt("uk_attr_usg_id");
					uk_inhalt = rs.getString("uk_inhalt");
					
					// prüfen des gruppenwechsels uk
					if ( !uk.equals(last_uk)) {
						// eine neue gruppe
						last_uk = uk;
						seq_uk = 1;
						obj_id_master = obj_id;
						//uk_attr_usg_id_master = uk_attr_usg_id;
						// uk_inhalt_master = uk_inhalt;
					} else
						seq_uk++;
					
					
					// ego-migrationen unterbinden
					if ( seq_uk > 1 && obj_id_master != obj_id ) {
						// stats
						commitCount++;
						statisticCount++;
						
						if ( commitCount > commmitSize) { 
							//iParas.loadedMigObj(statisticCount);
							conn.commit();
							commitCount = 0;							
						}	
						
						reportCount++;
						if (reportCount >= reportSize ) {
							iParas.logPrint("   Found:"+IntParas.tausenderLead(statisticCount));
							reportCount = 0;
						}						
						
						// System.out.println("migrating: "+uk+"           from "+obj_id+" to "+obj_id_master);
						insertPs.setString(1, uk);
						insertPs.setInt(2, obj_typ ); // neu juli 2023
						
						insertPs.setInt(3, uk_attr_usg_id);
						insertPs.setString(4, uk_inhalt);
						insertPs.setInt(5, obj_id_master);
						insertPs.setInt(6, obj_id);
						current_max_mig_obj_id++;
						
						insertPs.setInt(7, current_max_mig_obj_id);
						insertPs.setInt(8, rs.getInt("wert_id"));
						insertPs.execute();					
						
					
					}
						
					} else {
						// ein uk==null ??
					
						throw new Exception(
								"uk is null: uk="+rs.getString("uk")+
								" attr_usg_id="+rs.getInt("uk_attr_usg_id")+
								" inhalt="+rs.getString("uk_inhalt")+
								" obj_id="+rs.getInt("obj_id"));
					}
				}
				
				//iParas.loadedMigObj(statisticCount);
				conn.commit();
			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "2Mig1Search.searchMigrationRequests");
				throw ec;
			} finally {			    
			    try { if (rs != null) rs.close(); } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { if (insertPs != null) insertPs.close(); } catch (Exception e) {}	
			    try { 	
			    
			    mpReq.addMeasure("mig_requ_new", statisticCount);
			    iParas.mpMain.addMeasure(theround+".mig_requ_new", statisticCount);
				iParas.logPrint("Found "+ iParas.tausender( statisticCount) +" migration-triggers.");	

			    mpReq.finalizeAndPrint(); 
			    					    
			    } catch (Exception e) {}
			}
			
			// iParas.logPrint("  Did delete "+ statisticDelWert +" werte due to future irrelevance.");
			return ( statisticCount );	
		}
			
		
	}
	private static String getPostgresThings = " select "+
			" usg.attr_usg_id || '|' || trim(from wert.inhalt2) as uk, "+
			" obj.obj_id, "+
			" obj.obj_typ, "+
			// " attr.attr_id, "+
			// " attr.attr_typ, "+
			" wert.wert_id, "+
			" usg.attr_usg_id as uk_attr_usg_id, "+ 
			" wert.inhalt as uk_inhalt "+
			"from  "+
			"  wert as wert, "+
			"  attr as attr,   "+
			"  obj as obj, "+
			"  t_u as usg "+
			"where "+
			"  obj.obj_id = attr.obj_id and "+
			"  attr.attr_id = wert.attr_id and "+
			"  attr.attr_typ = usg.attr_typ and "+
			"  usg.obj_typ = obj.obj_typ and "+
			"  usg.ident = 1 " +
			"order by uk, obj_id; ";
	
	private static String getMySqlThings = " select "+
			" CONCAT(CONVERT(usg.attr_usg_id,char),'|',TRIM(wert.inhalt2 )) as uk, "+
			" obj.obj_id, "+
			" obj.obj_typ, "+
			// " attr.attr_id, "+
			// " attr.attr_typ, "+
			" wert.wert_id, "+
			" usg.attr_usg_id as uk_attr_usg_id, "+ 
			" wert.inhalt as uk_inhalt "+
			"from  "+
			"  wert as wert, "+
			"  attr as attr,   "+
			"  obj as obj, "+
			"  t_u as usg "+
			"where "+
			"  obj.obj_id = attr.obj_id and "+
			"  attr.attr_id = wert.attr_id and "+
			"  attr.attr_typ = usg.attr_typ and "+
			"  usg.obj_typ = obj.obj_typ and "+
			"  usg.ident = 1 " +
			"order by uk, obj_id; ";	
	
	private static String getMySqlThingsPronto1 = " select "+
			" CONCAT(CONVERT(wert.attr_usg_id,char),'|',TRIM(wert.inhalt2 )) as uk, "+
			" wert.obj_id as obj_id, "+
			" obj.obj_typ, "+
			// " attr.attr_id, "+
			// " attr.attr_typ, "+
			" wert.wert_id, "+
			" wert.attr_usg_id as uk_attr_usg_id, "+ 
			" wert.inhalt as uk_inhalt "+
			"from  "+
			"  wert as wert, "+
			"  attr as attr,   "+
			"  obj as obj, "+
			"  t_u as usg "+
			"where "+
			"  obj.obj_id = attr.obj_id and "+
			"  attr.attr_id = wert.attr_id and "+
			"  attr.attr_typ = usg.attr_typ and "+
			"  usg.obj_typ = obj.obj_typ and "+
			"  usg.ident = 1 " +
			"order by wert.attr_usg_id, wert.inhalt2, wert.obj_id; ";
}