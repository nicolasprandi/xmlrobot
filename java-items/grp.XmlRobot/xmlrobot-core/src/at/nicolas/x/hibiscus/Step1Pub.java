package at.nicolas.x.hibiscus;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import at.nicolas.x.int_specs.Commons;

public class Step1Pub  {
	
	private static String getWerteCopyAllSystems(int maxBox )  {
		String maxBoxThis = "";
		if ( maxBox > -1 ) 
			maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";
		return
			 "  select"+ 
			 "      wert.wert_id,"+ 
			 "      wert.attr_id,"+ 
			 "     wert.inhalt,"+ 
			 "     wert.von,"+ 
			 "     wert.bis,"+ 
			 "     wert.wann,"+ 
			 "     wert.box_id,"+ 
			 "     wert.hat_mehr,"+ 
			 "     typ_attr.final_typ,"+ 
			 "     attr.attr_typ,"+
			 "     usg.attr_usg_id,"+
			 "     usg.ident,"+
			 "     attr.obj_id"+ 
			 " from"+  
			 " 	i_wert as wert,"+ 
			 " 	i_attr as attr,"+ 
			 " 	i_box as box,"+ 
			 " 	t_a as typ_attr,"+ 
			 " 	i_obj as obj,"+           
			 " 	t_u as usg"+              
			 " where"+ 
			 "   box.zustand = 1 "+ maxBoxThis+
			 " 	and box.box_id = wert.box_id and"+ 
			 " 	wert.attr_id = attr.attr_id and"+ 
			 " 	attr.attr_typ = typ_attr.attr_typ and"+
			 "   obj.obj_id = attr.obj_id and"+
			 "   usg.attr_typ =attr.attr_typ  and"+
			 "   usg.obj_typ = obj.obj_typ"+
			 "   ;";	}
	
	private static String getWerteCopyMySql_withOuterJoin(int maxBox )  {
		String maxBoxThis = "";
		if ( maxBox > -1 ) 
			maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";	
		return
			"select "+
					"  A.wert_id, "+
					"  A.attr_id, "+
					"  A.inhalt, "+
					"  A.von, "+
					"  A.bis, "+
					"  A.wann, "+
					"  A.box_id, "+ 
					"  A.hat_mehr, "+
					"  A.obj_id, "+
					"  A.attr_typ, "+
					"  B.final_typ, "+  
					"  B.attr_usg_id, "+
					"  B.ident "+   
					"from ( "+
					"select "+
					"  wert.wert_id, "+
					"  wert.attr_id, "+
					"  wert.inhalt, "+
					"  wert.von, "+
					"  wert.bis, "+
					"  wert.wann, "+
					"  wert.box_id, "+ 
					"  wert.hat_mehr, "+
					"  attr.obj_id, "+
					"  attr.attr_typ, "+
					"  obj.obj_typ "+ 
					"from "+ 
					"	i_wert as wert, "+
					" 	i_attr as attr, "+ 
					"	i_box as box, "+
					" 	i_obj as obj "+
					"where "+   
					"  box.zustand = 1 "+maxBoxThis+
					"  and box.box_id = wert.box_id and "+
					"  wert.attr_id = attr.attr_id and "+
					"  obj.obj_id = attr.obj_id "+
					") AS A  "+
					"LEFT JOIN ( "+
					"select "+
					"  typ_attr.final_typ, "+
					"  usg.attr_usg_id, "+
					"  usg.ident, "+
					"  usg.obj_typ, "+
					"  usg.attr_typ "+ 
					"from "+ 
					"  t_a as typ_attr, "+
					"  t_u as usg "+
					"where "+  
					"  usg.attr_typ = typ_attr.attr_typ  "+
					") AS B  "+
					"ON   "+
					"A.attr_typ = B.attr_typ and "+
					"A.obj_typ = B.obj_typ; ";
	}
			
	public static void run( IntParas iParas, boolean isPostgres,Mp parentMp ) throws Exception {
		/*[requo-timer]*/
		/*[requo-customer]*/
		Connection conn = iParas.getConn();
		int commmitSize = iParas.getCommitSize();
		
		// tabelle frisch löschen
		{   
			Mp mpTruncFrisch = null;
		    Statement stmt = null;

		         
		    try{
		    	mpTruncFrisch = iParas.mm.beginMeasurePoint(parentMp, "trunc frisch");
		    	iParas.logPrint("Started to trunc table frisch ...");
		    	stmt = iParas.getConn().createStatement();
				
				int trunc = stmt.executeUpdate( "delete from frisch;");
				
				conn.commit();
				iParas.logPrint("   Did trunc table frisch, result is "+trunc);
				mpTruncFrisch.addMeasure("deleted_frisch", trunc);
		    } catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "1Pub.trunc table 'frisch'");	
			} finally {

				if (stmt != null) stmt.close();
				mpTruncFrisch.finalizeAndPrint();
			}
		} 		
		
		// die hoechste box ermitteln
		int maxBox = -1;
		int anzahl = -1;
		Mp mpMB = iParas.mm.beginMeasurePoint(parentMp, "get max box id");
    	
		{
			Statement stmt=null;
		    ResultSet rs=null;
		    
		    try {
				stmt = iParas.getConn().createStatement();
				rs = stmt.executeQuery("select max(box_id) as max_box_id, count(*) as anzahl from  i_box where zustand=1 ;");
				if (rs.next()) {
					maxBox = rs.getInt("max_box_id");
					anzahl = rs.getInt("anzahl");
				}
					
				if ( anzahl == 0 )
					maxBox= -1;
		    } catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "Step1Pub.GetMaxBoxId");
				throw (ec);
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {}	
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			}

		}		
		if ( maxBox > -1) {
			iParas.mpMain.addMeasure("max_box_id", maxBox);
			mpMB.addMeasure("max_box_id", maxBox);			
		}
		iParas.mpMain.addMeasure("count_box", anzahl);
		mpMB.addMeasure("count_box", anzahl);
		iParas.logPrint("Max-Box-Id to integrate was detected as [ "+maxBox+" ]");
		mpMB.finalizeAndPrint();		
		
		// weiter mit dem kopieren
		Mp mpBox = iParas.mm.beginMeasurePoint(parentMp, "copy boxes");
		
		//
		// move boxes		
		//
		if ( iParas.dss && !isPostgres ) {
			iParas.logPrint("Going to copy [ BOXES ] with database-specific-statement ... ");
			Statement stmt = null;
			try {
				stmt = iParas.getConn().createStatement();
				String maxBoxThis = "";
				if ( maxBox > -1 ) 
					maxBoxThis = " and box_id < "+(maxBox+1) +" ";
				
				int returner = stmt.executeUpdate(
						"insert into box (box_id,zustand,kurzname,titel,beschreibung,uri,werte,qualitaet,zeit_import,zeit_qk,zeit_export) "+
								"select "+ 
								"  box_id as box_id, "+
								"  zustand as zustand, "+								
								"  kurzname as kurzname, "+
								"  titel as titel, "+
								"  beschreibung as beschreibung, "+
								"  uri as uri, "+
								"  werte as werte, "+			
								"  qualitaet as qualitaet, "+								
								"  zeit_import as zeit_import, "+
								"  zeit_qk as zeit_qk, "+
								"  CURRENT_TIMESTAMP as zeit_export "+
								"from  i_box "+ 
								"where zustand=1 "+maxBoxThis+"; ");
				conn.commit();
				
				iParas.logPrint("Did copy [ "+ iParas.tausender(returner) +" ] boxes");
				mpBox.addMeasure("count_box", returner);
				// iParas.logPrint(" ");
			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "1Pub.insert into table 'box'/dss");
				throw ( ec);
			} finally {
				try {if (stmt != null) stmt.close();  } catch (Exception e) {}	
				
			}
		} else {
			iParas.logPrint("Going to copy [ BOXES ] with jdbc ... ");
			  
			    Statement stmt = null;
			    ResultSet rs = null;
			    PreparedStatement insert = null;
			        
			    try {
						int commitCount = 0;
						int statisticCount = 0;			
						stmt = iParas.getConn().createStatement();
						String maxBoxThis = "";
						if ( maxBox > -1 ) 
							maxBoxThis = " and box_id < "+(maxBox+1) +" ";
						String queText = "	select box_id, zeit_import, zeit_qk, qualitaet, uri, beschreibung, kurzname, titel, werte "+
								"	from i_box "+
								"	WHERE "+
								"	zustand = 1"+maxBoxThis+";"	;
						rs= iParas.doRunAndExplainQuery("1.5", "select i-boxen im zustand 1","boxen publizieren lls" , queText,stmt,
								iParas.sx_PubBox,mpBox,iParas.ix_sx_PubBox,false);
						
						
						 insert = conn.prepareStatement(
								" insert into box (box_id, zeit_import, zeit_qk, qualitaet, uri, beschreibung,zeit_export,zustand,kurzname,titel,werte ) values (?,?,?,?,?,?,CURRENT_TIMESTAMP,2,?,?,?);");
						
						commitCount = 0;
						statisticCount = 0;		
						while (rs.next()) {
							commitCount++;
							statisticCount++;
							if ( commitCount > commmitSize) { 
								conn.commit();
								commitCount = 0;
							}		
							insert.setInt(1, rs.getInt("box_id"));
							insert.setTimestamp(2, rs.getTimestamp("zeit_import"));
							insert.setTimestamp(3, rs.getTimestamp("zeit_qk"));
							insert.setInt(4, rs.getInt("qualitaet"));
							insert.setString(5, rs.getString("uri"));
							insert.setString(6, rs.getString("beschreibung"));
							insert.setString(7, rs.getString("kurzname"));
							insert.setString(8, rs.getString("titel"));
							insert.setString(9, rs.getString("werte"));
							insert.execute();
						}
						
						conn.commit();
						iParas.logPrint("Did copy ["+ iParas.tausender(statisticCount) +"] boxes");
						mpBox.addMeasure("count_box", statisticCount);
				} catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "1Pub.insert into table 'box'/jdbc");
					throw ( ec);
					} finally {
						try { if (rs != null) rs.close();   } catch (Exception e) {}	
						try { if (stmt != null) stmt.close(); } catch (Exception e) {}
						try { if (insert != null) insert.close(); } catch (Exception e) {}
						
			       
			    
			  }			
		}
		mpBox.finalizeAndPrint();   		
		  
		//
		// obj
		//
		// if ( 1==1 ) throw new Exception("soll so sein ");
		

		if ( iParas.dss && !isPostgres ) {
			{ // copy objects from int-to-central
			Mp mpObj = iParas.mm.beginMeasurePoint(parentMp, "copy objects");	
			iParas.logPrint("Going to copy [ OBJECTS ] with database-specific-statement ... ");
			Statement stmt = null;
			try {
				stmt = iParas.getConn().createStatement();
				String maxBoxThis = "";
				if ( maxBox > -1 ) 
					maxBoxThis = " and i_box.box_id < "+(maxBox+1) +" ";
				// "+maxBoxThis+"
				
				int returner = stmt.executeUpdate(
						"insert into obj (obj_id,obj_typ,box_id) "+   
								"select "+ 
								"  i_obj.obj_id, "+
								"  i_obj.obj_typ, "+
								"  i_obj.box_id "+  
								"from "+ 
								"  i_obj as i_obj, "+
								"  i_box as i_box "+ 
								"where "+
								"  i_box.zustand = 1 and "+ 
								"  i_box.box_id = i_obj.box_id "+maxBoxThis+" ; ");
				conn.commit();
				iParas.logPrint("Did copy "+ iParas.tausender(returner) +" objects");
				mpObj.addMeasure("count_obj", returner);
				iParas.mpMain.addMeasure("count_obj", returner);
			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "1Pub.insert table 'obj'/dss");
				throw ( ec);
			} finally {
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
				mpObj.finalizeAndPrint();
			}
			
			}
			
			{ // copy objects to frisch
				Mp mpObjFrisch = iParas.mm.beginMeasurePoint(parentMp, "copy objects to frisch");	
				iParas.logPrint("Going to copy [ OBJECTS to FRISCH ] with database-specific-statement ... ");
				Statement stmt = null;
				try {
					String maxBoxThis = "";
					if ( maxBox > -1 ) 
						maxBoxThis = " and i_box.box_id < "+(maxBox+1) +" ";
					// "+maxBoxThis+"
					
					stmt = iParas.getConn().createStatement();
					
					int returner = stmt.executeUpdate(
							"insert into frisch (obj_id,obj_typ) "+   
									"select "+ 
									"  i_obj.obj_id, "+
									"  i_obj.obj_typ "+
									"from "+ 
									"  i_obj as i_obj, "+
									"  i_box as i_box "+ 
									"where "+
									"  i_box.zustand = 1 and "+ 
									"  i_box.box_id = i_obj.box_id "+maxBoxThis+" ; ");
					conn.commit();
					iParas.logPrint("Did copy "+ iParas.tausender(returner) +" objects to frisch");
					mpObjFrisch.addMeasure("count_obj", returner);
				} catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "1Pub.insert table 'frisch'/dss");
					throw ( ec);
				} finally {
					try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
					mpObjFrisch.finalizeAndPrint();
				}
				
				}
		} else {
			iParas.logPrint("Going to copy [ OBJECTS ] with jdbc. Including also table frisch ... ");

			Statement stmt = null;
		    ResultSet rs = null;
		    PreparedStatement objInsert = null;
		    PreparedStatement frischInsert = null;
		    Mp mpObj = iParas.mm.beginMeasurePoint(parentMp, "copy objects (&frisch)");
			try {
				//iParas.logDebug("Going to:  Copy objects... ");
				int commitCount = 0;
				int statisticCount = 0;
				 stmt = iParas.getConn().createStatement();
					String maxBoxThis = "";
					if ( maxBox > -1 ) 
						maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";
					// "+maxBoxThis+"				 
				 String queText = " select "+
			                "  obj.obj_id,"+
			                "  obj.obj_typ,"+
			                "  obj.kopf,"+
			                "  obj.box_id  "+
							" from "+
							"  i_obj as obj,"+
							"  i_box as box "+
							" where"+
							"  box.zustand = 1 and "+
							"  box.box_id = obj.box_id "+maxBoxThis+" ;";
				 rs =  iParas.doRunAndExplainQuery("1.1", "objekte im zustand 1","objekte publizieren lls" , queText,stmt,
							iParas.sx_PubObj,mpObj,iParas.ix_sx_PubObj,false);


				commitCount = 0;
				statisticCount = 0;

				objInsert = conn.prepareStatement(
						" insert into obj (obj_id, obj_typ, kopf, box_id ) values (?,?,?,?);"); 

				frischInsert = conn.prepareStatement(
						" insert into frisch (obj_id, obj_typ) values (?,?);"); 
				
				commitCount = 0;
				statisticCount = 0;		
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						//iParas.loadedObjects(statisticCount);
						conn.commit();
						commitCount = 0;
						
					}		
					// System.out.println(rs.getInt("obj_id")+" "+rs.getInt("obj_typ")+" "+rs.getInt("box_id"));
					objInsert.setInt(1, rs.getInt("obj_id"));
					objInsert.setInt(2, rs.getInt("obj_typ"));
					objInsert.setString(3, rs.getString("kopf"));
					objInsert.setInt(4, rs.getInt("box_id"));
					objInsert.execute();
					
					frischInsert.setInt(1, rs.getInt("obj_id"));
					frischInsert.setInt(2, rs.getInt("obj_typ"));

					frischInsert.execute();					
				}
				//iParas.loadedObjects(statisticCount);
				conn.commit();
				iParas.logPrint("Did copy "+ iParas.tausender(statisticCount) +" objects. Also to frisch.");	
				mpObj.addMeasure("count_obj", statisticCount);
				iParas.mpMain.addMeasure("count_obj", statisticCount);

			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "1Pub.insert table 'obj' & 'frisch'/jdbc");	
				throw ( ec);
				
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {}	
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
				try { if (objInsert != null) objInsert.close(); } catch (Exception e) {}	
				try { if (frischInsert != null) frischInsert.close(); } catch (Exception e) {}
				mpObj.finalizeAndPrint();
		  }
		}

		
		
		//
		// attr
		//
		// if ( 1==1 ) throw new Exception("soll so sein ");
		Mp mpAttr = iParas.mm.beginMeasurePoint(parentMp, "copy attributes");

		if ( iParas.dss && !isPostgres ) {
			iParas.logPrint("Going to copy [ ATTRIBUTES ] with database-specific-statement ... ");
			Statement stmt = null;
			try {
				String maxBoxThis = "";
				if ( maxBox > -1 ) 
					maxBoxThis = " and i_box.box_id < "+(maxBox+1) +" ";
				// "+maxBoxThis+"				
				stmt = iParas.getConn().createStatement();
				
				int returner = stmt.executeUpdate(
						"insert into attr (attr_id,obj_id,attr_typ,box_id,top_aktuell) "+    
								"select  "+
								"  i_attr.attr_id,  "+
								"  i_attr.obj_id,   "+
								"  i_attr.attr_typ,   "+
								"  i_attr.box_id, "+
								" 0 as top_aktuell "+  
								"from "+ 
								"  i_attr as i_attr, "+
								"  i_box as i_box "+ 
								"where "+
								"  i_box.zustand = 1 and "+ 
								"  i_box.box_id = i_attr.box_id"+maxBoxThis+"; ");
				conn.commit();
				iParas.logPrint("Did copy "+ iParas.tausender(returner) +" attributes.");
				mpAttr.addMeasure("count_attr", returner);
				iParas.mpMain.addMeasure("count_attr", returner);
			} catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "1Pub.insert table 'attr'/dss");	
				throw ( ec);
			} finally {
				
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}
					
				
			}
		} else   {       
			    Statement stmt = null;
			    ResultSet rs = null;
			    PreparedStatement insert = null;
			    iParas.logPrint("Going to copy [ ATTRIBUTES ] with jdbc ... ");   
			    try {
			    	//iParas.logDebug("Going to:  Copy Attributes ... ");
						int commitCount = 0;
						int statisticCount = 0;			
						stmt = iParas.getConn().createStatement();
						String maxBoxThis = "";
						if ( maxBox > -1 ) 
							maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";
						// "+maxBoxThis+"
						String queText = 
								"	select "+
								"        attr.attr_id, "+
								"        attr.obj_id , "+
								"        attr.attr_typ, "+
								"        attr.box_id "+
								"	from "+ 
								"		i_attr as attr, "+
								"		i_box as box "+
								"	where "+
								"  box.zustand = 1 and "+
								"		box.box_id = attr.box_id "+maxBoxThis+"; ";
						rs=iParas.doRunAndExplainQuery("1.2", "select i-attr im zustand 1","attribute publizieren lls" , queText, stmt,
								iParas.sx_PubAttr,mpAttr,iParas.ix_sx_PubAttr,false);
						
						 insert = conn.prepareStatement(
								" insert into attr (attr_id, obj_id, attr_typ,top_aktuell, box_id) values (?,?,?,0,?);");
						
						commitCount = 0;
						statisticCount = 0;		
						while (rs.next()) {
							commitCount++;
							statisticCount++;
							if ( commitCount > commmitSize) { 
								//iParas.loadedAttr(statisticCount);
								conn.commit();
								commitCount = 0;
							}		
							insert.setInt(1, rs.getInt("attr_id"));
							insert.setInt(2, rs.getInt("obj_id"));
							insert.setInt(3, rs.getInt("attr_typ"));
							insert.setInt(4, rs.getInt("box_id"));
							insert.execute();
						}
						//iParas.loadedAttr(statisticCount);
						conn.commit();
						iParas.logPrint("Did copy "+ iParas.tausender(statisticCount) +" attributes.");	
						mpAttr.addMeasure("count_attr", statisticCount);
						iParas.mpMain.addMeasure("count_attr", statisticCount);
			    } catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "1Pub.insert table 'attr'/jdbc");	
					throw ( ec);
			    } finally {
			    
			    try { if (rs != null) rs.close(); } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { if (insert != null) insert.close(); } catch (Exception e) {}	
			   
			  }
			  }
		
		mpAttr.finalizeAndPrint();
			
//
// wert
//
		
  {   
	  Mp mpWert = iParas.mm.beginMeasurePoint(parentMp, "copy werte");
    Statement stmt = null;
    ResultSet rs = null;
	PreparedStatement insertNoBisPronto1 = null;						
	PreparedStatement insertNoVonPronto1 = null;
	PreparedStatement insertNoBisNoVonPronto1 = null;
	PreparedStatement insertPronto1 = null;
	PreparedStatement insert = null;				
	PreparedStatement insertNoBis = null;						
	PreparedStatement insertNoVon = null;
	PreparedStatement insertNoBisNoVon = null;
	PreparedStatement insertSchlecht = null;
	
	Timestamp now = new Timestamp( System.currentTimeMillis() ) ;
	int schlechtCount = 0;			        
    try {
    
    iParas.logPrint("Started to copy [ WERT ] with jdbc ...");
	int commitCount = 0;
	int statisticCount = 0;		
	
	int reportCount = 0;
	int reportSize = iParas.getReportSize();	
	
	stmt = iParas.getConn().createStatement();
	 
	String queText;
	String queTextExplain;
	
	if ( isPostgres) {
		queText = getWerteCopyAllSystems(maxBox);
		queTextExplain="/postgres";
	} else {
		queText = getWerteCopyMySql_withOuterJoin(maxBox);
		queTextExplain="/mysql-maria";
	}
			 
	rs= iParas.doRunAndExplainQuery(
			"1.4", 
			"select i-werte im zustand 1",
			"werte publizieren "+queTextExplain , 
			queText,
			stmt,
			iParas.sx_PubWert,mpWert,iParas.ix_sx_PubWert,false);



	if ( iParas.hasPronto1() ) {
		 insertNoBisPronto1 = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, von, wann, box_id, hat_mehr, inhalt2, attr_usg_id, obj_id, ident ) values (?,?,?,?,?,?,?,?,?,?,?);");						
		 insertNoVonPronto1 = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, bis, wann, box_id, hat_mehr, inhalt2, attr_usg_id, obj_id, ident ) values (?,?,?,?,?,?,?,?,?,?,?);");
		 insertNoBisNoVonPronto1 = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, wann, box_id, hat_mehr, inhalt2, attr_usg_id, obj_id, ident ) values (?,?,?,?,?,?,?,?,?,?);");						
		 insertPronto1 = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, von, bis, wann, box_id, hat_mehr, inhalt2, attr_usg_id, obj_id, ident ) values (?,?,?,?,?,?,?,?,?,?,?,?);");
	} else {
		 insert = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, von, bis, wann, box_id, hat_mehr, inhalt2) values (?,?,?,?,?,?,?,?,?);");				
		 insertNoBis = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, von, wann, box_id, hat_mehr, inhalt2) values (?,?,?,?,?,?,?,?);");						
		 insertNoVon = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, bis, wann, box_id, hat_mehr, inhalt2) values (?,?,?,?,?,?,?,?);");
		 insertNoBisNoVon = conn.prepareStatement(
				" insert into wert (wert_id, attr_id, inhalt, wann, box_id, hat_mehr, inhalt2) values (?,?,?,?,?,?,?);");		
	}
	 insertSchlecht = conn.prepareStatement(
			" insert into i_schlecht (wert_id, datum, grund, box_id) values (?,?,?,?);");
	
	//iParas.logPrint(" Did create prepared statements.");
	commitCount = 0;
	statisticCount = 0;	
	
	Pattern patternDate = Pattern.compile("^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$");
	Pattern patternTime = Pattern.compile("^[0-9][0-9]\\:[0-9][0-9]$");
	Pattern patternTimestamp1 = Pattern.compile("^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]\\ [0-9][0-9]\\:[0-9][0-9]:[0-9][0-9].[0-9][0-9][0-9]$");
	Pattern patternTimestamp2 = Pattern.compile("^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]\\ [0-9][0-9]\\:[0-9][0-9]:[0-9][0-9]$");

	//iParas.logPrint(" Did create regex-patterns.");
	BigDecimal maxNum = new BigDecimal("1000000000000000");
	BigDecimal minNum = new BigDecimal("-1000000000000000");
	BigDecimal zehntausend = new BigDecimal(10000);
	BigDecimal zero = new BigDecimal(0);
		
	//iParas.logPrint(" Did create Big-Decimal-Constants.");
	String int_inhalt = null;
	String int_inhalt2 = null;
	BigDecimal numNum = null;	
	String i2 = null;	
	
	
	
	if ( iParas.numform && iParas.hasPronto1()) {
		// das alternative nummernformat, zwecks zeitsparen nur für pronto implementiert
		Exception exIntSize = new Exception("Die ganze Zahl ist zu gross oder zu klein");
		Exception exNoBool =  new Exception("Das ist kein boolscher wert (0/1).");
		Exception exNoDate =   new Exception("Das ist kein Datum. Format soll sein [YYYY-MM-DD]");
		Exception exNoTime =   new Exception("Das ist keine Zeit. Format soll sein [hh:mm]");
		Exception exNotTs = new Exception("Das ist kein Zeitstempel. Format soll sein [YYYY-MM-DD HH:MM:SS.sss]");
		Exception exFinType = new Exception("final typ ist unbekannt");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		DecimalFormat intDfI = new DecimalFormat("000000000000000.0000",symbols);
		DecimalFormat intDfN = new DecimalFormat("000000000000000.0000",symbols);	
		String plus = new String("+");
		Timestamp vonRs =null;
		Timestamp bisRs =null;	
		int x = -1;
		//String nodecimals = new String(".0000");
		boolean hasProblem = false;
		while (rs.next()) {
			commitCount++;
			statisticCount++;
			if ( commitCount > commmitSize) { 
				//iParas.loadedWert(statisticCount);
				conn.commit();
				//iParas.logPrint("  Did commit, statisticCount="+statisticCount);
				commitCount = 0;				
			}	
			reportCount++;
			if (reportCount >= reportSize ) {
				iParas.logPrint("   Copy:"+IntParas.tausenderLead(statisticCount));
				reportCount = 0;
			}
			/*
			if ( statisticCount%10000 == 0 ) {
				iParas.logPrint("  Copy Werte #"+statisticCount);
			} */		

		
			int_inhalt = "";
			int_inhalt2 = "";
				 
			int final_typ = rs.getInt("final_typ");

			vonRs =null;
			bisRs =null;		
			hasProblem=false;
			try {
				
				switch ( final_typ ) {
				case 1: 
					  // varchar
					  int_inhalt = rs.getString("inhalt");
					  int_inhalt2 = int_inhalt.toUpperCase().replaceAll("\\s", "");		
					  break;
				case 2:	
					  // char
					  int_inhalt = rs.getString("inhalt");
					  int_inhalt2 = int_inhalt.toUpperCase().replaceAll("\\s", "");		
					  break;
				case 3:
					// integer
					//
					if ( iParas.simple ) {
						/* variante 2 */
						x = Integer.parseInt(rs.getString("inhalt").trim());
						
						if(x<0 ) 
							int_inhalt= String.format("%016d", x)+".0000";
						else 
							int_inhalt ="+"+String.format("%015d", x)+".0000";
						
						int_inhalt2 = int_inhalt;						
					} else {
						/* variante 1 */
						numNum = new BigDecimal (rs.getString("inhalt").trim());
						numNum = numNum.setScale(0, RoundingMode.DOWN);
						if ( numNum.compareTo(maxNum) > -1 || numNum.compareTo(minNum) < 1)
							throw exIntSize;
						
						int_inhalt = intDfI.format(numNum);
						if ( !int_inhalt.startsWith("-")) int_inhalt = plus.concat(int_inhalt);
					    int_inhalt2 = int_inhalt;							
					}



				   											

					break;
				case 4: {
					  // number
					numNum = new BigDecimal (rs.getString("inhalt").trim());
					numNum = numNum.setScale(4, RoundingMode.DOWN);
					if ( numNum.compareTo(maxNum) > -1 || numNum.compareTo(minNum) < 1)
						throw exIntSize;

					
					int_inhalt = intDfN.format(numNum);
					if ( !int_inhalt.startsWith("-")) int_inhalt = "+".concat(int_inhalt);
				    int_inhalt2 = int_inhalt;		

					break;
				} case 5: {
					  // bool
					int boolWert = rs.getInt("inhalt");
					
				    if ( boolWert == 1) {
					      int_inhalt = "1";
					      int_inhalt2 = "1";
					} else if ( boolWert==0) {
					      int_inhalt = "0";
					      int_inhalt2 = "0";
					} else
						throw exNoBool;  

					break; }
					
					
				case 6: {
					 /* date */
					i2 = rs.getString("inhalt").trim();

					if (patternDate.matcher(i2).matches() ) {
					    int_inhalt = i2;
					    int_inhalt2 = i2;							
					} else 
						throw exNoDate;
					break;}
				case 7: {
					/* ts */	
					i2 = rs.getString("inhalt").trim();
					 
					if (patternTimestamp1.matcher(i2).matches() ) {
					    int_inhalt = i2;
					    int_inhalt2 = i2;							
					} else {
						
						if ( patternTimestamp2.matcher(i2).matches()) {
						      int_inhalt = i2+".000";
						      int_inhalt2 = i2+".000";								
						} else 
							throw exNotTs;
						
					}
					break;}
				case 8: {
					 /* date */
					i2 = rs.getString("inhalt").trim();

					if (patternTime.matcher(i2).matches() ) {
					    int_inhalt = i2;
					    int_inhalt2 = i2;							
					} else 
						throw exNoTime;
					break;}
				default:	
					throw exFinType;
				}	
			} catch ( Exception problem ) {
				// kommt ins schlecht
				insertSchlecht.setInt(1, rs.getInt("wert_id"));
				// iParas.logDebug("           ... done: wert_id");
				
				insertSchlecht.setTimestamp(2, now);
				// iParas.logDebug("           ... done: now");
				
				insertSchlecht.setString(3, "["+rs.getString("inhalt")+"]"+final_typ+": "+problem.getLocalizedMessage());		
				// iParas.logDebug("           ... done: schlecht_grund");
				
				insertSchlecht.setInt(4, rs.getInt("box_id"));		
				// iParas.logDebug("           ... done: box_id");
				
				insertSchlecht.execute();
				// iParas.logDebug("           ... executed! ");
				schlechtCount++;
				hasProblem = true;
			}
				// alles ok, ab in die datenbank	
			if ( !hasProblem) {		
					
					vonRs =rs.getTimestamp("von");
					bisRs =rs.getTimestamp("bis");
					
						
					if ( vonRs == null && bisRs == null) {
						insertNoBisNoVonPronto1.setInt(1, rs.getInt("wert_id"));
						insertNoBisNoVonPronto1.setInt(2, rs.getInt("attr_id"));
						insertNoBisNoVonPronto1.setString(3, int_inhalt);															
						insertNoBisNoVonPronto1.setTimestamp(4, rs.getTimestamp("wann"));								
						insertNoBisNoVonPronto1.setInt(5, rs.getInt("box_id"));								
						insertNoBisNoVonPronto1.setInt(6, rs.getInt("hat_mehr"));								
						insertNoBisNoVonPronto1.setString(7, int_inhalt2);
						if ( rs.getInt("ident")==0 ) {
							insertNoBisNoVonPronto1.setNull(8, Types.SMALLINT);
							insertNoBisNoVonPronto1.setNull(9, Types.INTEGER);
							insertNoBisNoVonPronto1.setInt(10, 0);										
						} else {
							insertNoBisNoVonPronto1.setInt(8, rs.getInt("attr_usg_id"));
							insertNoBisNoVonPronto1.setInt(9, rs.getInt("obj_id"));
							insertNoBisNoVonPronto1.setInt(10, 1);
						}													
						insertNoBisNoVonPronto1.execute();		
						
					} else if ( vonRs == null ) {
						insertNoVonPronto1.setInt(1, rs.getInt("wert_id"));
						insertNoVonPronto1.setInt(2, rs.getInt("attr_id"));
						insertNoVonPronto1.setString(3, int_inhalt);								
						insertNoVonPronto1.setTimestamp(4, bisRs);							
						insertNoVonPronto1.setTimestamp(5, rs.getTimestamp("wann"));								
						insertNoVonPronto1.setInt(6, rs.getInt("box_id"));								
						insertNoVonPronto1.setInt(7, rs.getInt("hat_mehr"));								
						insertNoVonPronto1.setString(8, int_inhalt2);
						if ( rs.getInt("ident")==0 ) {
							insertNoVonPronto1.setNull(9, Types.SMALLINT);
							insertNoVonPronto1.setNull(10, Types.INTEGER);
							insertNoVonPronto1.setInt(11, 0);										
						} else {
							insertNoVonPronto1.setInt(9, rs.getInt("attr_usg_id"));
							insertNoVonPronto1.setInt(10, rs.getInt("obj_id"));
							insertNoVonPronto1.setInt(11, 1);
						}													
						insertNoVonPronto1.execute();		
						
					} else if ( rs.getTimestamp("bis") == null ) {
						insertNoBisPronto1.setInt(1, rs.getInt("wert_id"));
						insertNoBisPronto1.setInt(2, rs.getInt("attr_id"));
						insertNoBisPronto1.setString(3, int_inhalt);								
						insertNoBisPronto1.setTimestamp(4, vonRs);							
						insertNoBisPronto1.setTimestamp(5, rs.getTimestamp("wann"));								
						insertNoBisPronto1.setInt(6, rs.getInt("box_id"));								
						insertNoBisPronto1.setInt(7, rs.getInt("hat_mehr"));								
						insertNoBisPronto1.setString(8, int_inhalt2);
						
						if ( rs.getInt("ident")==0 ) {
							insertNoBisPronto1.setNull(9, Types.SMALLINT);
							insertNoBisPronto1.setNull(10, Types.INTEGER);
							insertNoBisPronto1.setInt(11, 0);										
						} else {
							insertNoBisPronto1.setInt(9, rs.getInt("attr_usg_id"));
							insertNoBisPronto1.setInt(10, rs.getInt("obj_id"));
							insertNoBisPronto1.setInt(11, 1);
						}													
						insertNoBisPronto1.execute();		
						
					} else {
														
						insertPronto1.setInt(1, rs.getInt("wert_id"));
						insertPronto1.setInt(2, rs.getInt("attr_id"));
						insertPronto1.setString(3, int_inhalt);								
						insertPronto1.setTimestamp(4, vonRs);
						insertPronto1.setTimestamp(5, bisRs);								
						insertPronto1.setTimestamp(6, rs.getTimestamp("wann"));								
						insertPronto1.setInt(7, rs.getInt("box_id"));								
						insertPronto1.setInt(8, rs.getInt("hat_mehr"));								
						insertPronto1.setString(9, int_inhalt2);
						
						if ( rs.getInt("ident")==0 ) {
							insertPronto1.setNull(10, Types.SMALLINT);
							insertPronto1.setNull(11, Types.INTEGER);
							insertPronto1.setInt(12, 0);										
						} else {
							insertPronto1.setInt(10, rs.getInt("attr_usg_id"));
							insertPronto1.setInt(11, rs.getInt("obj_id"));
							insertPronto1.setInt(12, 1);
						}
						
						insertPronto1.execute();	
					} 						
							
			} // ende hasProblem
			
		

		} // ende loop		
	} else {
		// alte nummernimplementierung
		boolean ok = true;
		String schlecht_grund = null;
		boolean convert_itn = false;	
		int numBool = -1;
		boolean numNeg = false;
		BigDecimal numVorkomma = null;
		String numPlainString = null;
		String numErg = null;	
		Timestamp vonRs =null;
		Timestamp bisRs =null;	
	while (rs.next()) {
		commitCount++;
		statisticCount++;
		if ( commitCount > commmitSize) { 
			//iParas.loadedWert(statisticCount);
			conn.commit();
			//iParas.logPrint("  Did commit, statisticCount="+statisticCount);
			commitCount = 0;
			
		}	
		
		reportCount++;
		if (reportCount >= reportSize ) {
			iParas.logPrint("    Copy:"+IntParas.tausenderLead(statisticCount));
			reportCount = 0;
		}		
		/*
		if ( statisticCount%10000 == 0 ) {
			iParas.logPrint("  Copy Werte #"+statisticCount);
		} */		

		ok = true;
		schlecht_grund = "";
		int_inhalt = "";
		int_inhalt2 = "";
		convert_itn = false;		 
		int final_typ = rs.getInt("final_typ");

		vonRs =null;
		bisRs =null;		

		switch ( final_typ ) {
			case 1: 
				  // varchar
				  int_inhalt = rs.getString("inhalt");
				  int_inhalt2 = int_inhalt.toUpperCase().replaceAll("\\s", "");		
				  break;
			case 2:	
				  // char
				  int_inhalt = rs.getString("inhalt");
				  int_inhalt2 = int_inhalt.toUpperCase().replaceAll("\\s", "");		
				  break;
			case 3:
				  // integer
				  // convert_itn = true;
				  convert_itn = true;
				  break;
			case 4: {
				  // number
				  convert_itn = true;			
				  break; }
			case 5: {
				  // bool
				try {
					numBool = rs.getInt("inhalt");
				    if ( numBool == 1) {
					      int_inhalt = "1";
					      int_inhalt2 = "1";
					    } else if ( numBool==0) {
					      int_inhalt = "0";
					      int_inhalt2 = "0";
					    } else {
					      ok = false;
					      schlecht_grund = "Das ist kein Bool: ["+rs.getString("inhalt")+"]";
					    }
				} catch (Exception e) {
				      ok = false;
				      schlecht_grund = "Das ist kein Bool: ["+rs.getString("inhalt")+"]";
				}
				break; }
				
				
			case 6: {
				 /* date */
				i2 = rs.getString("inhalt").trim();

				if (patternDate.matcher(i2).matches() ) {
				    int_inhalt = i2;
				    int_inhalt2 = i2;							
				} else {
				    ok = false;
				    schlecht_grund = "Das ist kein Datum: ["+i2+"] Format soll sein [YYYY-MM-DD]";							
				}
				break;}
			case 7: {
				/* ts */	
				i2 = rs.getString("inhalt").trim();
				 
				if (patternTimestamp1.matcher(i2).matches() ) {
				    int_inhalt = i2;
				    int_inhalt2 = i2;							
				} else {
					
					if ( patternTimestamp2.matcher(i2).matches()) {
					      int_inhalt = i2+".000";
					      int_inhalt2 = i2+".000";								
					} else {
					    ok = false;
					    schlecht_grund = "Das ist kein Zeitstempel: ["+i2+"] Format soll sein [YYYY-MM-DD HH:MM:SS.sss]";								
					}
					
				}
				break;} 		
			
			case 8: {
				 /* time */
				i2 = rs.getString("inhalt").trim();

				if (patternTime.matcher(i2).matches() ) {
				    int_inhalt = i2;
				    int_inhalt2 = i2;							
				} else {
				    ok = false;
				    schlecht_grund = "Das ist keine Zeit: ["+i2+"] Format soll sein [hh:mm]";							
				}
				break;}	
			default:	
				  ok = false;
				  schlecht_grund = "final typ "+final_typ+" nicht implementiert";
		}

		if ( convert_itn ) {
			  
			  numNum = null;
			  try {
			    numNum = new BigDecimal (rs.getString("inhalt").trim());  
			  } catch (NumberFormatException e ) {
			    // log_debug(check+ " is NOT double");
			    numNum = null;
			  }
			  
			  if ( numNum == null ) {
			    ok = false;
			    schlecht_grund = "Das ist keine Zahl: ["+rs.getString("inhalt")+"]";
			  } else if ( numNum.compareTo(maxNum) > -1 || numNum.compareTo(minNum) < 1) {
			    ok = false;
			    if ( numNum.compareTo(maxNum) > -1 ) 
			      schlecht_grund = "Die Zahl ist zu gross: ["+rs.getString("inhalt")+"]";
			    else 
			      schlecht_grund = "Die Zahl ist zu klein: ["+rs.getString("inhalt")+"]";
			  } else {
			  
			    numNum = numNum.multiply(zehntausend);
			  
			    numNeg = false;
			    if ( numNum.compareTo(zero) == -1) { 
			      numNeg = true;
			      numNum = numNum.abs();
			    }
			    numVorkomma = numNum.setScale(0, RoundingMode.DOWN);
			    numPlainString = 
			      Commons.leadSymbols(numVorkomma.toPlainString(), 19, "0");
			    numErg = numPlainString.substring(0,15)+'.'+numPlainString.substring(15);
			    if ( numNeg ) numErg = "-"+numErg;
			    else numErg ="+"+numErg;
			  
			    int_inhalt = numErg;
			    int_inhalt2 = numErg;  
			  }
			}
		
		
	if ( ok ) {
		vonRs =rs.getTimestamp("von");
		bisRs =rs.getTimestamp("bis");
		
		if ( iParas.hasPronto1()  ) {
			
			if ( vonRs == null && bisRs == null) {
				insertNoBisNoVonPronto1.setInt(1, rs.getInt("wert_id"));
				insertNoBisNoVonPronto1.setInt(2, rs.getInt("attr_id"));
				insertNoBisNoVonPronto1.setString(3, int_inhalt);															
				insertNoBisNoVonPronto1.setTimestamp(4, rs.getTimestamp("wann"));								
				insertNoBisNoVonPronto1.setInt(5, rs.getInt("box_id"));								
				insertNoBisNoVonPronto1.setInt(6, rs.getInt("hat_mehr"));								
				insertNoBisNoVonPronto1.setString(7, int_inhalt2);
				if ( rs.getInt("ident")==0 ) { 
					insertNoBisNoVonPronto1.setNull(8, Types.SMALLINT);
					insertNoBisNoVonPronto1.setNull(9, Types.INTEGER);
					insertNoBisNoVonPronto1.setInt(10, 0);										
				} else {
					insertNoBisNoVonPronto1.setInt(8, rs.getInt("attr_usg_id"));
					insertNoBisNoVonPronto1.setInt(9, rs.getInt("obj_id"));
					insertNoBisNoVonPronto1.setInt(10, 1);
				}													
				insertNoBisNoVonPronto1.execute();		
				
			} else if ( vonRs == null ) {
				insertNoVonPronto1.setInt(1, rs.getInt("wert_id"));
				insertNoVonPronto1.setInt(2, rs.getInt("attr_id"));
				insertNoVonPronto1.setString(3, int_inhalt);								
				insertNoVonPronto1.setTimestamp(4, bisRs);							
				insertNoVonPronto1.setTimestamp(5, rs.getTimestamp("wann"));								
				insertNoVonPronto1.setInt(6, rs.getInt("box_id"));								
				insertNoVonPronto1.setInt(7, rs.getInt("hat_mehr"));								
				insertNoVonPronto1.setString(8, int_inhalt2);
				if ( rs.getInt("ident")==0 ) {
					insertNoVonPronto1.setNull(9, Types.SMALLINT);
					insertNoVonPronto1.setNull(10, Types.INTEGER);
					insertNoVonPronto1.setInt(11, 0);										
				} else {
					insertNoVonPronto1.setInt(9, rs.getInt("attr_usg_id"));
					insertNoVonPronto1.setInt(10, rs.getInt("obj_id"));
					insertNoVonPronto1.setInt(11, 1);
				}													
				insertNoVonPronto1.execute();		
				
			} else if ( rs.getTimestamp("bis") == null ) {
				insertNoBisPronto1.setInt(1, rs.getInt("wert_id"));
				insertNoBisPronto1.setInt(2, rs.getInt("attr_id"));
				insertNoBisPronto1.setString(3, int_inhalt);								
				insertNoBisPronto1.setTimestamp(4, vonRs);							
				insertNoBisPronto1.setTimestamp(5, rs.getTimestamp("wann"));								
				insertNoBisPronto1.setInt(6, rs.getInt("box_id"));								
				insertNoBisPronto1.setInt(7, rs.getInt("hat_mehr"));								
				insertNoBisPronto1.setString(8, int_inhalt2);
				
				if ( rs.getInt("ident")==0 ) {
					insertNoBisPronto1.setNull(9, Types.SMALLINT);
					insertNoBisPronto1.setNull(10, Types.INTEGER);
					insertNoBisPronto1.setInt(11, 0);										
				} else {
					insertNoBisPronto1.setInt(9, rs.getInt("attr_usg_id"));
					insertNoBisPronto1.setInt(10, rs.getInt("obj_id"));
					insertNoBisPronto1.setInt(11, 1);
				}													
				insertNoBisPronto1.execute();		
				
			} else {
												
				insertPronto1.setInt(1, rs.getInt("wert_id"));
				insertPronto1.setInt(2, rs.getInt("attr_id"));
				insertPronto1.setString(3, int_inhalt);								
				insertPronto1.setTimestamp(4, vonRs);
				insertPronto1.setTimestamp(5, bisRs);								
				insertPronto1.setTimestamp(6, rs.getTimestamp("wann"));								
				insertPronto1.setInt(7, rs.getInt("box_id"));								
				insertPronto1.setInt(8, rs.getInt("hat_mehr"));								
				insertPronto1.setString(9, int_inhalt2);
				
				if ( rs.getInt("ident")==0 ) {
					insertPronto1.setNull(10, Types.SMALLINT);
					insertPronto1.setNull(11, Types.INTEGER);
					insertPronto1.setInt(12, 0);										
				} else {
					insertPronto1.setInt(10, rs.getInt("attr_usg_id"));
					insertPronto1.setInt(11, rs.getInt("obj_id"));
					insertPronto1.setInt(12, 1);
				}
				
				insertPronto1.execute();	
			} 						
		} else  {
			
			if ( vonRs == null && bisRs == null) {
				insertNoBisNoVon.setInt(1, rs.getInt("wert_id"));
				insertNoBisNoVon.setInt(2, rs.getInt("attr_id"));
				insertNoBisNoVon.setString(3, int_inhalt);															
				insertNoBisNoVon.setTimestamp(4, rs.getTimestamp("wann"));								
				insertNoBisNoVon.setInt(5, rs.getInt("box_id"));								
				insertNoBisNoVon.setInt(6, rs.getInt("hat_mehr"));								
				insertNoBisNoVon.setString(7, int_inhalt2);				
				insertNoBisNoVon.execute();									
			} else if ( vonRs == null ) {
				insertNoVon.setInt(1, rs.getInt("wert_id"));
				insertNoVon.setInt(2, rs.getInt("attr_id"));
				insertNoVon.setString(3, int_inhalt);								
				insertNoVon.setTimestamp(4, bisRs);							
				insertNoVon.setTimestamp(5, rs.getTimestamp("wann"));								
				insertNoVon.setInt(6, rs.getInt("box_id"));								
				insertNoVon.setInt(7, rs.getInt("hat_mehr"));								
				insertNoVon.setString(8, int_inhalt2);				
				insertNoVon.execute();									
			} else if ( rs.getTimestamp("bis") == null ) {
				insertNoBis.setInt(1, rs.getInt("wert_id"));
				insertNoBis.setInt(2, rs.getInt("attr_id"));
				insertNoBis.setString(3, int_inhalt);								
				insertNoBis.setTimestamp(4, vonRs);							
				insertNoBis.setTimestamp(5, rs.getTimestamp("wann"));								
				insertNoBis.setInt(6, rs.getInt("box_id"));								
				insertNoBis.setInt(7, rs.getInt("hat_mehr"));								
				insertNoBis.setString(8, int_inhalt2);				
				insertNoBis.execute();									
			} else {												
				insert.setInt(1, rs.getInt("wert_id"));
				insert.setInt(2, rs.getInt("attr_id"));
				insert.setString(3, int_inhalt);								
				insert.setTimestamp(4, vonRs);
				insert.setTimestamp(5, bisRs);								
				insert.setTimestamp(6, rs.getTimestamp("wann"));								
				insert.setInt(7, rs.getInt("box_id"));								
				insert.setInt(8, rs.getInt("hat_mehr"));								
				insert.setString(9, int_inhalt2);				
				insert.execute();	
			} 						
		}

				
			
	} else {
															
			insertSchlecht.setInt(1, rs.getInt("wert_id"));
			// iParas.logDebug("           ... done: wert_id");
			
			insertSchlecht.setTimestamp(2, now);
			// iParas.logDebug("           ... done: now");
			
			insertSchlecht.setString(3, schlecht_grund);		
			// iParas.logDebug("           ... done: schlecht_grund");
			
			insertSchlecht.setInt(4, rs.getInt("box_id"));		
			// iParas.logDebug("           ... done: box_id");
			
			insertSchlecht.execute();
			// iParas.logDebug("           ... executed! ");
			schlechtCount++;
	}

	} // ende loop
    } // ende if numform
	//iParas.logPrint("  Did finish werte-loop"); 
	//iParas.loadedWert(statisticCount);
	conn.commit();
	iParas.logPrint("Did copy  "+ iParas.tausender(statisticCount) +
			" werte. Davon "+iParas.tausender(schlechtCount)+" schlecht.");	
	mpWert.addMeasure("count_wert", statisticCount);
	iParas.mpMain.addMeasure("count_wert", statisticCount);
	mpWert.addMeasure("wert_invalid", schlechtCount);
    } catch ( Exception ec ) {					
		iParas.printExceptionBy(ec, "1Pub.insert into table 'wert'/jdbc");	
		throw ( ec);
} finally {
     


try { if (rs != null) rs.close(); } catch (Exception e) {}	
try { if (stmt != null) stmt.close(); } catch (Exception e) {}	

try { if ( insertNoBisPronto1!= null) insertNoBisPronto1.close(); } catch (Exception e) {}	
try { if ( insertNoVonPronto1!= null) insertNoVonPronto1.close(); } catch (Exception e) {}	
try { if ( insertNoBisNoVonPronto1!= null) insertNoBisNoVonPronto1.close(); } catch (Exception e) {}	
try { if ( insertPronto1!= null) insertPronto1.close(); } catch (Exception e) {}	
try { if ( insert!= null) insert.close(); } catch (Exception e) {}	
try { if ( insertNoBis!= null) insertNoBis.close(); } catch (Exception e) {}	
try { if ( insertNoVon!= null) insertNoVon.close(); } catch (Exception e) {}	
try { if ( insertNoBisNoVon!= null) insertNoBisNoVon.close(); } catch (Exception e) {}	
try { if ( insertSchlecht!= null) insertSchlecht.close(); } catch (Exception e) {}	

if ( schlechtCount > 0)
iParas.logPrint("used "+now.toString()+" as schlecht.datum");
//iParas.logPrint(" ");
  }
    
    mpWert.finalizeAndPrint();
  }					
		
			//
			// mehr
			//
			// if ( 1==1 ) throw new Exception("soll so sein ");
  			Mp mpMehr = iParas.mm.beginMeasurePoint(parentMp, "copy mehr");
			if ( iParas.dss && !isPostgres ) {
				iParas.logPrint("Going to copy [ MEHR ] with database-specific-statement ... ");
				Statement stmt = null;
				try {
					String maxBoxThis="";
					if ( maxBox > -1 ) 
						maxBoxThis = " and i_box.box_id < "+(maxBox+1) +" ";
					// "+maxBoxThis+"	
					stmt = iParas.getConn().createStatement();
					
					int returner = stmt.executeUpdate(
							"insert into mehr (wert_id,inhalt,box_id) "+   
									"select "+ 
									"  i_mehr.wert_id, "+
									"  i_mehr.inhalt, "+
									"  i_mehr.box_id "+  
									"from "+ 
									"  i_mehr as i_mehr, "+
									"  i_box as i_box "+ 
									"where "+
									"  i_box.zustand = 1 and "+ 
									"  i_box.box_id = i_mehr.box_id "+maxBoxThis+"; ");
					conn.commit();
					iParas.logPrint("Did copy "+ iParas.tausender(returner) +" mehr.");
					mpMehr.addMeasure("count_mehr", returner);
					iParas.mpMain.addMeasure("count_mehr", returner);
				 } catch ( Exception ec ) {					
						iParas.printExceptionBy(ec, "1Pub.insert into table 'mehr'/dss");
						throw ( ec);
				} finally {
					
					try { if (stmt != null) stmt.close(); } catch (Exception e) {}	

				}
			} else   			  {       
				    Statement stmt = null;
				    ResultSet rs = null;
				    PreparedStatement insert = null;
					iParas.logPrint("Going to copy [ MEHR ] with jdbc ... ");
				        
				    try {
				    	//iParas.logDebug("Going to:  Copy mehr ... ");
							int commitCount = 0;
							int statisticCount = 0;			
							stmt = iParas.getConn().createStatement();
							String maxBoxThis = "";
							if ( maxBox > -1 ) 
								maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";
							// "+maxBoxThis+"
							String queText = "	select "+
									"	    mehr.wert_id, "+
									"	    mehr.inhalt, "+
									"	    mehr.box_id  "+
									"	from  "+
									"		i_mehr as mehr, "+
									"		i_box as box "+
									"	where "+
									"  box.zustand = 1 and "+
									"		box.box_id = mehr.box_id "+maxBoxThis+"; "	;
							rs=iParas.doRunAndExplainQuery("1.3", "select i-mehr im zustand 1","mehr publizieren" , queText,stmt,
									iParas.sx_PubMehr,mpMehr,iParas.ix_sx_PubMehr,false);


							 insert = conn.prepareStatement(
									" insert into mehr (wert_id, inhalt, box_id) values (?,?,?);");
							
							commitCount = 0;
							statisticCount = 0;		
							while (rs.next()) {
								commitCount++;
								statisticCount++;
								if ( commitCount > commmitSize) { 
									//iParas.loadedMehr(statisticCount);
									conn.commit();
									commitCount = 0;
								}		
								insert.setInt(1, rs.getInt("wert_id"));
								insert.setString(2, rs.getString("inhalt"));
								insert.setInt(3, rs.getInt("box_id"));
								insert.execute();
							}
							//iParas.loadedMehr(statisticCount);
							conn.commit();
							// 
							iParas.logPrint("Did copy "+ iParas.tausender(statisticCount) +" mehr.");		
							mpMehr.addMeasure("count_mehr", statisticCount);
							iParas.mpMain.addMeasure("count_mehr", statisticCount);
				    } catch ( Exception ec ) {					
						iParas.printExceptionBy(ec, "1Pub.insert into table 'mehr'/jdbc");	
						throw ( ec);
					} finally {
				    
				    try { if (rs != null) rs.close(); } catch (Exception e) {}	
				    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
				    try { if (insert != null) insert.close(); } catch (Exception e) {}	

				  }
				  }	
			mpMehr.finalizeAndPrint(); 
			  
				//
				// bez
				//
			// if ( 1==1 ) throw new Exception("soll so sein ");
			Mp mpBez = iParas.mm.beginMeasurePoint(parentMp, "copy bez");
						if ( iParas.dss && !isPostgres ) {
							iParas.logPrint("Going to copy [ BEZ ] with database-specific-statement ... ");
							Statement stmt = null;
							
							try {
								String maxBoxThis="";
								if ( maxBox > -1 ) 
									maxBoxThis = " and i_box.box_id < "+(maxBox+1) +" ";
								// "+maxBoxThis+"	
								stmt = iParas.getConn().createStatement();
								
								int returner = stmt.executeUpdate(
										"insert into bez (bez_id,bez_typ,obj_id_a,obj_id_b,box_id) "+    
												"select "+ 
												"  i_bez.bez_id, "+
												"  i_bez.bez_typ, "+
												"  i_bez.obj_id_a,  "+
												"  i_bez.obj_id_b, "+
												"  i_bez.box_id "+ 
												"from "+ 
												"  i_bez as i_bez, "+
												"  i_box as i_box "+ 
												"where "+
												"  i_box.zustand = 1 and "+ 
												"  i_box.box_id = i_bez.box_id "+maxBoxThis+"; ");
								conn.commit();
								iParas.logPrint("Did copy "+ iParas.tausender(returner) +" bez.");
								mpBez.addMeasure("count_bez", returner);
								iParas.mpMain.addMeasure("count_bez", returner);
							 } catch ( Exception ec ) {					
									iParas.printExceptionBy(ec, "1Pub.insert into table 'bez'/dss");	
									throw ( ec);
							} finally {
								
								try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
								
							}
						} else{       
				    Statement stmt = null;
				    ResultSet rs = null;
				    PreparedStatement insert = null;
				    iParas.logPrint("Going to copy [ BEZ ] with jdbc ... ");
									    
				    //iParas.logDebug("Going to: Copy Beziehungen  ... ");    
				    try {
							int commitCount = 0;
							int statisticCount = 0;			
							stmt = iParas.getConn().createStatement();
							String maxBoxThis = "";
							if ( maxBox > -1 ) 
								maxBoxThis = " and box.box_id < "+(maxBox+1) +" ";
							// "+maxBoxThis+"
							String queText = " select "+
									" 	 bez.bez_id, "+
									" 	 bez.bez_typ, "+
									"	 bez.obj_id_a, "+
									"	 bez.obj_id_b, "+
									"	 bez.box_id "+
									" from  "+
									"	i_bez as bez, "+
									"	i_box as box "+
									" where "+
									"  box.zustand = 1  "+maxBoxThis+
									" and box.box_id = bez.box_id; "	;
							rs=iParas.doRunAndExplainQuery("1.1", "select i-bez im zustand 1","beziehungen publizieren" , queText,stmt,
									iParas.sx_PubBez,mpBez,iParas.ix_sx_PubBez,false);
							

							 insert = conn.prepareStatement(
									" insert into bez (bez_id, bez_typ, obj_id_a, obj_id_b, box_id) values (?,?,?,?,?);");
							
							commitCount = 0;
							statisticCount = 0;		
							while (rs.next()) {
								commitCount++;
								statisticCount++;
								if ( commitCount > commmitSize) { 
									//iParas.loadedBez(statisticCount);
									conn.commit();
									commitCount = 0;
									
								}		
								// System.out.println(rs.getInt("obj_id")+" "+rs.getInt("obj_typ")+" "+rs.getInt("box_id"));
								insert.setInt(1, rs.getInt("bez_id"));
								insert.setInt(2, rs.getInt("bez_typ"));
								insert.setInt(3, rs.getInt("obj_id_a"));
								insert.setInt(4, rs.getInt("obj_id_b"));
								insert.setInt(5, rs.getInt("box_id"));
								insert.execute();
							}
							//iParas.loadedBez(statisticCount);
							conn.commit();
							iParas.logPrint("Did copy "+ iParas.tausender(statisticCount) +" bez.");	
							mpBez.addMeasure("count_bez", statisticCount);
							iParas.mpMain.addMeasure("count_bez", statisticCount);
				    } catch ( Exception ec ) {					
						iParas.printExceptionBy(ec, "1Pub.insert into table 'bez'/jdbc");	
						throw ( ec);
						} finally {
							try { if (rs != null) rs.close(); } catch (Exception e) {}	
							try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
							try { if (insert != null) insert.close(); } catch (Exception e) {}				          
					    }
					  }			
					mpBez.finalizeAndPrint(); 

		//
		// update boxes
		//
		  {       
			  
			    Statement stmt = null;
			    Mp mpUBox = iParas.mm.beginMeasurePoint(parentMp, "update box zustand"); 
			    iParas.logPrint("Going to update boxes ... ");
			    try {
						stmt = iParas.getConn().createStatement();
						String maxBoxThis="";
						if ( maxBox > -1 ) 
							maxBoxThis = " and box_id < "+(maxBox+1) +" ";
						int updateValue = stmt.executeUpdate(
							"	UPDATE i_box "+
							"	SET zeit_export = CURRENT_TIMESTAMP , zustand = 2"+
							"	WHERE "+
							"	zustand = 1 "+maxBoxThis+";"					
								);
						iParas.logPrint("Did update "+updateValue+" Boxes");
						conn.commit();
			    } catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "1Pub.update table 'boxes'/dss");	
					throw ( ec);
					} finally {   
			    
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { mpUBox.finalizeAndPrint(); } catch (Exception e) {}
			  }
			  }
	}

}