package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import at.nicolas.x.int_specs.Commons;

public class Step3Heal {
	private static final String n_deleteWert ="Heal.Delete.wert" ;
	private static final String t_deleteWert ="DELETE FROM wert WHERE wert_id = ?;" ;
	
//private static IntegrateParas iiParas = null;
public static void run( IntParas iParas, Mp parentMp ) throws Exception {
	/*[requo-timer]*/
	/*[requo-customer]*/
	
	Connection conn = iParas.getConn();
	int commmitSize = iParas.getCommitSize();
	int reportSize = iParas.getReportSize(); 
	
	  
	// 
	// heal_double_bez_simple
	//
	
	  {       
		  Mp mpDoubRel = iParas.mm.beginMeasurePoint(parentMp, "Heal double relations");
		  int statisticCount = 0;
		    Statement stmt = null;
		    ResultSet rs = null;
		    PreparedStatement prepared = null;
		    // iParas.logPrintDebug("");
		    iParas.logPrint("Started to heal duplicate relations ...");     
		    try {
				int commitCount = 0;
				
				stmt = iParas.getConn().createStatement();
				 
				String queText = "select "+ 
						"bez.bez_typ as bez_typ, "+
						"bez.obj_id_a as obj_id_a, "+
						"bez.obj_id_b as obj_id_b, "+
						"bez.bez_id as bez_id "+
						"from bez as bez  "+
						"order by bez_typ, obj_id_a, obj_id_b; ";
				
				//System.out.println("HealOnly: bez "+queText);
		      
			    rs=iParas.doRunAndExplainQuery("4.8/h", "heal duplicate relations","heal" , 
			    		queText,stmt,iParas.sx_HealRelations,mpDoubRel,iParas.ix_sx_HealRelations,false);

				commitCount = 0;
				statisticCount = 0;
				
				prepared = conn.prepareStatement(
				" DELETE FROM bez WHERE bez_id = ?;");
				/*
				PreparedStatement prepared = conn.prepareStatement(
				" insert into mig_obj " + 
				"(uk, uk_attr_usg_id, uk_inhalt, master_obj_id, slave_obj_id ) "+
				"values (?,?,?,?,?);");
				*/
				
				commitCount = 0;
				statisticCount = 0;		
				
				int bez_typ = -1;
				int obj_id_a = -1;
				int obj_id_b = -1;
				
				int grp_bez_typ = -1;
				int grp_obj_id_a = -1;
				int grp_obj_id_b = -1;		
				
				
				while (rs.next()) {
					
					bez_typ = rs.getInt("bez_typ");
					obj_id_a = rs.getInt("obj_id_a");
					obj_id_b = rs.getInt("obj_id_b");
					
					if ( grp_bez_typ  != bez_typ  ||
						 grp_obj_id_a != obj_id_a ||
						 grp_obj_id_b != obj_id_b ) {
						// neue gruppe
						grp_bez_typ = bez_typ;
						grp_obj_id_a = obj_id_a;
						grp_obj_id_b = obj_id_b;
						// dieser bleibt
					} else {
						// alle weiteren gruppenelemente werden gel�scht
						prepared.setInt(1, rs.getInt("bez_id"));					
						prepared.executeUpdate();		
						
						commitCount++;
						statisticCount++;					
						
						if ( commitCount > commmitSize) { 
							conn.commit();
							commitCount = 0;					
						}					
					}
				}
				
				iParas.logPrint("   Did heal "+ iParas.tausender(statisticCount) +" duplicate relations");
				//iParas.logPrintDebug(" ");
			} finally {
			    
			    try { if (rs != null) rs.close(); } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { conn.commit(); } catch (Exception e) {}	
			    try { if (prepared != null) prepared.close(); } catch (Exception e) {}	
			    
			    try { 
			    	mpDoubRel.addMeasure("healed_double_realtions", statisticCount);
			    	mpDoubRel.finalizeAndPrint(); 
			    				    
			    	} catch (Exception e) {}
		  }
		  } 		
	// 
	// heal_double_attr_types
	//	
	  {       
		    Statement stmt = null;
		    ResultSet rs = null;
			PreparedStatement preparedUp = null;
			PreparedStatement preparedDel =null;
		     
		    Mp mpDupAttr = iParas.mm.beginMeasurePoint(parentMp, "Heal duplicate attributes");
		    iParas.logPrint("Started to heal duplicate attributes ..."); 
		    int statisticCount = 0;
		    int myCommmitSize = commmitSize;
		    if ( iParas.getAdapt())
		    	myCommmitSize= iParas.getCommitSizeAdapted2(); 		    
		    try {

				int commitCount = 0;
				
				 stmt = iParas.getConn().createStatement();
				String queText = "select "+ 
						"  n1.obj_id as obj_id, "+
						"  n1.attr_typ as attr_typ, "+
						"  n1.cnt as cnt, "+
						"  n1.master_attr_id as master_attr_id, "+
						"  n2.attr_id as attr_id "+
						"from ( "+
						"  select "+
						"    attr.obj_id, "+
						"    attr.attr_typ, "+
						"    count(*) as cnt, "+
						"    min(attr.attr_id) as master_attr_id "+
						"  from  "+
						"    attr as attr, "+
						"    obj as obj "+
						"  where "+
						"    attr.obj_id = obj.obj_id  and "+
						"    attr.top_aktuell = 0 "+
						"  group by attr.obj_id, attr_typ "+
						"  ) n1,  "+
						"  ( "+
						"  select "+
						"      attr.obj_id, "+
						"      attr.attr_typ, "+
						"      attr.attr_id "+
						"   from "+
						"      attr as attr "+
						"   ) n2 "+
						"where  "+
						"  n1.cnt > 1 and "+
						"  n1.obj_id = n2.obj_id and "+
						"  n1.attr_typ = n2.attr_typ and "+
						"  n1.master_attr_id <> n2.attr_id;";
				//System.out.println("HealOnly: attr "+queText);
			    rs=iParas.doRunAndExplainQuery("4.9/h", "heal duplicate attributes","heal" , 
			    		queText,stmt,iParas.sx_HealAttributes,mpDupAttr,iParas.ix_sx_HealAttributes,false);				    
			
				commitCount = 0;
				statisticCount = 0;
					
				preparedUp = conn.prepareStatement(
								" update wert set attr_id=? where attr_id = ?;");
				
				preparedDel = conn.prepareStatement(
						" DELETE FROM attr WHERE attr_id = ?;");			
				
				commitCount = 0;
				statisticCount = 0;		

				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > myCommmitSize) { 
						conn.commit();
						commitCount = 0;					
					}		

					preparedUp.setInt(1, rs.getInt("master_attr_id"));
					preparedUp.setInt(2, rs.getInt("attr_id"));
					preparedUp.executeUpdate();

					preparedDel.setInt(1, rs.getInt("attr_id"));
					preparedDel.executeUpdate();				
					
				}
				
				iParas.logPrint("   Did heal "+ iParas.tausender(statisticCount) +" duplicate attributes");		
				// iParas.logPrintDebug(" ");
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {}	
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
				try { if ( preparedUp!= null) preparedUp.close(); } catch (Exception e) {}	
				try { if ( preparedDel!= null) preparedDel.close(); } catch (Exception e) {}	

				try { conn.commit(); } catch (Exception e) {}
				try { 
					mpDupAttr.addMeasure("healed_double_attrs", statisticCount);
					mpDupAttr.finalizeAndPrint(); 
								    
					} catch (Exception e) {}
			    
		  }
		  } 		
	// 
	// heal_double_werte
	//	
  {    
	  iParas.logPrint("Started to heal duplicate werte ...");	  


PreparedStatement deleteWert = conn.prepareStatement(
		" DELETE FROM wert WHERE wert_id = ?;");

//pronto 2
Updater u_deleteWert =null ;
int i_deleteWert =0;
    
Statement stmt = null;
ResultSet rs = null;

Mp mpNow = iParas.mm.beginMeasurePoint(parentMp, "Heal duplicate werte");
int myCommmitSize = commmitSize;
int statisticCount = 0;	
try {

	stmt = iParas.getConn().createStatement();
	String queText = "select "+ 
			"  attr.attr_id as attr_id, "+
			"  wert.wert_id as wert_id, "+
			"  wert.von as von, "+
			"  wert.bis as bis, "+
			"  wert.inhalt as inhalt "+
			"from "+
			"  attr as attr, "+
			"  wert as wert "+
			"where "+
			"  wert.attr_id = attr.attr_id and "+
			"  attr.top_aktuell = 0 "+
			" order by "+
			"  attr.attr_id, "+
			"  wert.inhalt, "+
			"  wert.von, "+
			"  wert.bis; ";	
	if (iParas.hasPronto2And(iParas.upp_heal)) {
		 u_deleteWert = iParas.getUpdater(
				 "Heal4-DoubleWert",n_deleteWert,
				 2,100*1*iParas.bytesPerInt,t_deleteWert);	
		// vor-select.mig.werte
		if (iParas.shouldRunClassicIndexpoint(iParas.ix_iHealWert_Sel,parentMp));
	}
	
	rs=iParas.doRunAndExplainQuery("4.10/h", "heal duplicate werte","heal" , 
			queText,stmt,iParas.sx_HealWert,mpNow,iParas.ix_sx_HealWert,false);

	
	int commitCount = 0;
	
	int reportCount = 0;	
	int affectedCount = 0;
	int affectedCount2 = 0;
	
	int grp_attr_id = -1;
	String grp_inhalt = new String("");
	Timestamp grp_von = null;
	Timestamp grp_bis = null;
	
 	String countText = null;
	if ( iParas.hasPronto2And(iParas.upp_heal) )
		countText ="Heal-Count:";
	else 
		countText ="Heal-Count:";			
	while (rs.next()) {
		
		statisticCount++;
		
		reportCount++;
		if (reportCount >= reportSize ) {
			iParas.logPrint("   "+countText+IntParas.tausenderLead(statisticCount));
			reportCount = 0;
		}	
		
		if ( grp_attr_id == -1 || 
			( grp_attr_id  != rs.getInt("attr_id")  ||
			 !grp_inhalt.equals(rs.getString("inhalt")) ||
			 tsDiffer(grp_von, rs.getTimestamp("von")) || 
			 tsDiffer(grp_bis, rs.getTimestamp("bis") ) )) {
			// neue gruppe
			 grp_attr_id = rs.getInt("attr_id");
			 grp_inhalt = rs.getString("inhalt");
			 grp_von = rs.getTimestamp("von");
			 grp_bis = rs.getTimestamp("bis");
			// dieser bleibt
		} else {
			// alle weiteren gruppenelemente werden gelöscht	
			affectedCount++;
			if ( iParas.hasPronto2And(iParas.upp_heal) ) {
				u_deleteWert.setInt(rs.getInt("wert_id"));
				i_deleteWert++;
				
			} else {
				commitCount++;
				if ( commitCount > myCommmitSize) { 
					conn.commit();
					commitCount = 0;					
				}	
				deleteWert.setInt(1, rs.getInt("wert_id"));					
				affectedCount2 = affectedCount2 + deleteWert.executeUpdate();	
			}
					
		}
	} // schleifenende
	
	if ( iParas.hasPronto2And(iParas.upp_heal) ) {
		iParas.logPrint("  Wert-Healing did cache [ "+ iParas.tausender(affectedCount) +" ] wert-removals ");
	} else {
		conn.commit();
		iParas.logPrint("  Wert-Healing did [ "+ iParas.tausender(affectedCount) +"] wert-removals and affected [ "+affectedCount2+"] rows");			
	}	
		
	// iParas.logPrintDebug(" ");
   
} finally {
	try { if (rs != null) rs.close(); } catch (Exception e) {}	
	try { if (stmt != null) stmt.close(); } catch (Exception e) {}	

	}
//gecachte updates verarbeiten
if ( iParas.hasPronto2And(iParas.upp_heal)) {
	Mp mpNowUpdate = iParas.mm.beginMeasurePoint(mpNow, "Performing separated updates");
	// index point fuer update ausführen
	if (iParas.shouldRunClassicIndexpoint(iParas.ix_iHealWert_Upd,parentMp));
	
	{   // deleteWert	
		String title ="HealWert.deleteWert"; /* ! */ 
		iParas.logUpdaterIntro(u_deleteWert,i_deleteWert); /* ! */ 
		u_deleteWert.switchFromWritingToReading(i_deleteWert); /* ! */ 
		
		int commitCount = 0; int reportCount = 0;  myCommmitSize = iParas.getUpdateCommitSize(); statisticCount = 0; int affected = 0;	
		// if ( iParas.getAdapt() ) myCommmitSize= iParas.getCommitSizeAdapted2(); /* ! */ 
			
		for ( int i = 0; i< i_deleteWert ; i++ ) { /* ! */		
			/* ! */ 
			deleteWert.setInt(1, u_deleteWert.getInt());
			affected = affected +deleteWert.executeUpdate();	
			
			statisticCount++;
			commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
			reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_deleteWert,statisticCount); reportCount = 0;}		
		}	
		conn.commit();
		u_deleteWert.close(statisticCount); /* ! */ 
		iParas.logUpdaterExtro(u_deleteWert, statisticCount,affected);		
		try { if ( deleteWert!= null) deleteWert.close(); } catch (Exception e) {}	
		try { mpNowUpdate.addMeasure("found", statisticCount);
			  mpNowUpdate.finalizeAndPrint(); 
			  			    
			} catch (Exception e) {}
	} // deleteWert done	
	
}

mpNow.finalizeAndPrint();


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
}