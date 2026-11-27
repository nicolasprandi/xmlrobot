package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import at.nicolas.x.int_specs.Commons;

public class Step2Mig2Optimize {
	
		
//private static IntegrateParas iiParas = null;
public static void run( IntParas iParas,
		Mp parentMp ) throws Exception {
	/*[requo-timer]*/
	/*[requo-customer]*/
	//iiParas = iParas;
	Connection conn = iParas.getConn();
	int commmitSize = iParas.getCommitSize();
	// 
	// mig_obj_remove_duplicates
	//	
	{       
	    Statement stmt = null;
	    ResultSet rs = null;
	    PreparedStatement prepared = null;   
	    Mp mpDupl = iParas.mm.beginMeasurePoint(parentMp, "find-duplicates");
	    int statisticCount = 0;
	    try {
	    	// iParas.logPrintDebug(" ");
	    	iParas.logPrint("Started to identify redundant migration-requests ...");

			 stmt = iParas.getConn().createStatement();
			 String queText = 
						"	select "+ 
								"	mig_obj.master_obj_id as master_object_id, "+ 
								"	mig_obj.slave_obj_id as slave_obj_id, "+ 
								"	count(*) as cnt, "+
								"	min(mig_obj_id) as keep_mig_obj_id "+
								"	from "+ 
								"	  mig_obj as mig_obj "+
								"	group by mig_obj.master_obj_id, mig_obj.slave_obj_id "+
								"	HAVING cnt > 1; " ;
			 rs = iParas.doRunAndExplainQuery("4.1", "identify redundant migration-requests","optimize" , queText,stmt,
					 iParas.sx_MigOptiEquals,mpDupl,iParas.ix_sx_MigOptiEquals,true);			 
			
			 prepared = conn.prepareStatement(
			" DELETE FROM mig_obj WHERE  master_obj_id = ? and slave_obj_id = ? and mig_obj_id <> ?; ");
			/*
			PreparedStatement prepared = conn.prepareStatement(
			" insert into mig_obj " + 
			"(uk, uk_attr_usg_id, uk_inhalt, master_obj_id, slave_obj_id ) "+
			"values (?,?,?,?,?);");
			*/
			
			int commitCount = 0;
			
			while (rs.next()) {
				commitCount++;
				statisticCount++;
				if ( commitCount > commmitSize) { 
					conn.commit();
					commitCount = 0;					
				}		

				prepared.setInt(1, rs.getInt("master_object_id"));
				prepared.setInt(2, rs.getInt("slave_obj_id"));
				prepared.setInt(3, rs.getInt("keep_mig_obj_id"));
				prepared.executeUpdate();
			}

			conn.commit();
			iParas.logPrint("   Did eleminate [ "+ iParas.tausender(statisticCount) +" redundant migration-requests ]. ");		
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "2Mig2Optimize.reduce redundant migration-requests");					
		} finally {
	        
	    
	    try { if (rs != null) rs.close();  } catch (Exception e) {}
	    try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	    try { 	mpDupl.addMeasure("found_redundancies", statisticCount);
	    		mpDupl.finalizeAndPrint(); 
	    					    
	    	} catch (Exception e) {}	    
	  }
	 } 	

	// 
	// mig_obj_remove_cycles
	//
	
	  {       
		    Statement stmt = null;
		    ResultSet rs = null;
		    PreparedStatement prepared = null; 
		    
		    Mp mpCyc = iParas.mm.beginMeasurePoint(parentMp, "find mig-cycles");
		    iParas.logPrint("Started to identify migration-cycles ...");
		    int statisticCount = 0;
		    try {


				 stmt = iParas.getConn().createStatement();
				 String queText = 
						 	" select "+
							"  slave_obj_id, "+
							"  mig_obj_id as slave_mig_obj_id "+
							"from "+
							"  mig_obj "+
							"where "+
							"  slave_obj_id in ( "+
							"    select "+
							"      master_obj_id "+
							"    from "+
							"      mig_obj   "+
							"  ); ";
				 rs= iParas.doRunAndExplainQuery("4.2", "remove migration-cycles","optimize" , queText,stmt,
						 iParas.sx_MigOptiCyles,mpCyc,iParas.ix_sx_MigOptiCyles,true);				 
				
				 prepared = conn.prepareStatement(
				" DELETE FROM mig_obj WHERE mig_obj_id = ?; ");

				int commitCount = 0;
									
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						conn.commit();
						commitCount = 0;					
					}		

					prepared.setInt(1, rs.getInt("slave_mig_obj_id"));

					prepared.executeUpdate();
				}

				conn.commit();
				iParas.logPrint("   Did eleminate [ "+ iParas.tausender(statisticCount) +" migration-cycles ] ");		
				
		    } catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "2Mig2Optimize.reduce cylclic migration-requests");					
			} finally {
		      
		    try { if (rs != null) rs.close(); } catch (Exception e) {}	
		    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		    try { if (prepared != null) prepared.close(); } catch (Exception e) {}	
		    try { 
		    	mpCyc.addMeasure("found_cycles", statisticCount);
		    	mpCyc.finalizeAndPrint(); 
		    				    
		    	} catch (Exception e) {}
  		    
		  }
	  }	
	  
		// 
		// iParas.doubleslaves
		//
		
		  if ( iParas.doubleoccupation ){       
			    //Statement stmt = null;
			    ResultSet rs = null;
			    PreparedStatement prepared = null; 
			    int statisticCount = 0;
			    Mp mpDoub = iParas.mm.beginMeasurePoint(parentMp, "find double-occupations");
			    iParas.logPrint("Started to remove double-occupations ...");
			    
			    try {


					 //stmt = iParas.getConn().createStatement();
					 String queText = 
							 " select "+ 
									 " min(mig_obj_id) as first_mig_obj_id, "+
									 " slave_obj_id "+ 
									 " from   mig_obj "+
									 " group by slave_obj_id "+
									 " having count(*)>1  ; " ;
					 rs= iParas.doRunAndExplainQuery("4.2", "remove double-slaves","optimize" , queText,
							 //stmt,
							 null,
							 iParas.sx_MigOptiDouble,mpDoub,iParas.ix_sx_MigOptiDouble,true);				 
					
					prepared = conn.prepareStatement(
					" DELETE FROM mig_obj WHERE mig_obj_id <> ? and slave_obj_id=? ; ");

					int commitCount = 0;
									
					while (rs.next()) {
						commitCount++;
						statisticCount++;
						if ( commitCount > commmitSize) { 
							conn.commit();
							commitCount = 0;					
						}		

						prepared.setInt(1, rs.getInt("first_mig_obj_id"));
						prepared.setInt(2, rs.getInt("slave_obj_id"));

						prepared.executeUpdate();
					}

					conn.commit();
					iParas.logPrint("   Did eleminate [ "+ iParas.tausender(statisticCount) +" double-slaving migration-requests ] ");		
					
			    } catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "2Mig2Optimize.reduce double occupations double-slaving");						
				} finally {
				    try { if (rs != null) rs.close(); } catch (Exception e) {}	
				    //try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
				    try { if (prepared != null) prepared.close(); } catch (Exception e) {}	
				    
				    try { mpDoub.addMeasure("found_double_occupations", statisticCount);
				    	  mpDoub.finalizeAndPrint(); 
				    	  			    
				    	} catch (Exception e) {}
			  }
		  }		  
	  

	  
		  if ( iParas.smart && iParas.smart_in_opti ) {       
			  iParas.doSmartKeyReduction(parentMp);
			  }		 	  
		
}						
	private static int tsDifferCounter = 0;
	/*
	private static boolean tsDifferTESTER ( Timestamp a, Timestamp b) {
		boolean ret = tsDifferInt ( a, b);
		iiParas.logDebug("tsDiffers ["+tsDifferCounter+"] a="+a+" b="+b+" = "+ret);
		tsDifferCounter++;
		return ret;

	}
	*/
	private static boolean tsDiffer ( Timestamp a, Timestamp b) {
		if ( a== null && b == null ) return false;
		else if ( a== null && b != null ) return true;
		else if ( b== null && a != null ) return true;
		return  ( !a.equals(b));
	}	
	/*
	Diese beiden wurden urspruenglich gejoint (master_obj_id = slave_obj_id )
	    select
	    master_obj_id,
	    mig_obj_id as master_mig_obj_id
	    from
	    mig_obj
	    order by master_obj_id;
	    
	    select
	    slave_obj_id,
	    mig_obj_id as slave_mig_obj_id
	    from
	    mig_obj
	    order by slave_obj_id;
	
	Daraus ist dann das geworden
	    select
	      slave_obj_id,
	      mig_obj_id as slave_mig_obj_id
	    from
	      mig_obj
	    where
	      slave_obj_id in (
	        select
	          master_obj_id
	        from
	          mig_obj  
	      );			 
*/
}