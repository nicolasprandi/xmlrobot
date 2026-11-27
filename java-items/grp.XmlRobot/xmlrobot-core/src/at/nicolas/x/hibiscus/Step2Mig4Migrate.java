package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class Step2Mig4Migrate {
	
private static final String n_updateMoveSlaveAttrToMaster ="Mig4.MoveSlaveAttrToMaster.wert";
private static final String n_updateMoveSlaveAttrToMasterPronto1 ="Mig4.MoveSlaveAttrToMasterPronto1.wert";
private static final String n_updateDelete ="Mig4.Delete.attr";
private static final String n_updateMoveWertToMasterAttributPronto1 ="Mig4.MoveToMasterAttributPronto1.wert";

private static final String t_updateMoveSlaveAttrToMaster ="update wert set attr_id=? where attr_id = ?;";
private static final String t_updateMoveSlaveAttrToMasterPronto1 ="update wert set obj_id=? where attr_id = ?;";
private static final String t_updateDelete ="delete from attr where attr_id=?;";
private static final String t_updateMoveWertToMasterAttributPronto1 ="update wert set attr_id=? , obj_id=? where attr_id = ?;";

public static void run( IntParas iParas, boolean doPronto2Really, boolean  doPronto2FinalClose,
		Mp parentMp, int theround) throws Exception {
	
/*[requo-timer]*/
/*[requo-customer]*/
//iiParas = iParas;
boolean isPostgres = iParas.isPostgres;
Connection conn = iParas.getConn();
int commmitSize = iParas.getCommitSize();
int reportSize = iParas.getReportSize(); 


// 
// mig_perform_obj_migration
//	
if ( 1> 0 ){

int statisticCountObj = 0;

Mp mpObjMig = null;
PreparedStatement updMoveAttrToObj = conn.prepareStatement(
	" update attr set obj_id=? where attr_id = ?;");	

PreparedStatement updMoveAttrToObjP1 = conn.prepareStatement(
	" update wert set obj_id=? where attr_id = ?;");				

PreparedStatement updDeleteAttribute = conn.prepareStatement(
	" delete from attr where attr_id=?;");	

PreparedStatement updMoveWertToAttr = conn.prepareStatement(
	" update wert set attr_id=? where attr_id = ?;");			

PreparedStatement updMoveWertToAttrP1 = conn.prepareStatement(
	" update wert set attr_id=? , obj_id=? where attr_id = ?;");	

// pronto 2


Updater u_updateMoveSlaveAttrToMaster =null ;
Updater u_updateMoveSlaveAttrToMasterPronto1 =null ;
Updater u_updateDelete =null ;
Updater u_updateMoveWertToMasterAttributPronto1 =null ;

try {
int i_updateMoveSlaveAttrToMaster =0;
int i_updateMoveSlaveAttrToMasterPronto1  =0;
int i_updateDelete  =0;
int i_updateMoveWertToMasterAttributPronto1  =0;		
if ( 2>0){       
Statement stmt = null;
ResultSet rs = null;

int myCommmitSize = commmitSize;
if ( iParas.getAdapt() )
	myCommmitSize= iParas.getCommitSizeAdapted2();

try {
	
	// jezt wirds ernst
	 stmt = iParas.getConn().createStatement();
	 
	 String queText = "";
	 if ( isPostgres)
		queText = postgresOuterJoin;
	 else
		queText = mySqlOuterJoin;		
	 
	 if ( doPronto2Really ) {
		 u_updateMoveSlaveAttrToMaster = iParas.getUpdater(
				 "Mig3-ContentMig",n_updateMoveSlaveAttrToMaster,
				 2,100*2*iParas.bytesPerInt,
				 t_updateMoveSlaveAttrToMaster);
		 u_updateMoveSlaveAttrToMasterPronto1 = iParas.getUpdater(
				 "Mig3-ContentMig",n_updateMoveSlaveAttrToMasterPronto1,
				 2,100*2*iParas.bytesPerInt,
				 t_updateMoveSlaveAttrToMasterPronto1);
		 u_updateDelete = iParas.getUpdater(
				 "Mig3-ContentMig",n_updateDelete,
				 1,100*1*iParas.bytesPerInt,
				 t_updateDelete);

		 u_updateMoveWertToMasterAttributPronto1 = iParas.getUpdater(
				 "Mig3-ContentMig",n_updateMoveWertToMasterAttributPronto1,
				 3,100*3*iParas.bytesPerInt,
				 t_updateMoveWertToMasterAttributPronto1);		 
		 
		 
	 }
	 
	  if ( iParas.smart && !iParas.smart_in_opti ) {       
		  iParas.doSmartKeyReduction(parentMp);
	  }		 
	  if ( doPronto2Really ) {
		  // vor-select.mig.werte
		  if (iParas.shouldRunClassicIndexpoint(iParas.ix_iMigObj_Sel,parentMp));
		  
	  }
	  
	  mpObjMig = iParas.mm.beginMeasurePoint(parentMp, "Attribute-Migration");
	  iParas.logPrint("Started to perform attribute-migrations ...");
	  rs= iParas.doRunAndExplainQuery("4.3", 
			"attribute-migrations","migration" , 
			queText,stmt,iParas.sx_MigCoreGetAttrMigs,mpObjMig,
			iParas.ix_sx_MigCoreGetAttrMigs,true);

				
	int commitCount = 0;
	
	int reportCount = 0;
	
	
	int updateMoveAttr = 0;
	//int updateDeleteCount = 0;
	int updateMoveWert = 0;
	String countText = null;
	if ( doPronto2Really )
		countText ="Cache:";
	else 
		countText ="Update:";
	while (rs.next()) {
		
		statisticCountObj++;
		
		
		// commit ausführen nur wenn nicht pronto 2
		if ( !doPronto2Really ) {
			commitCount++;
			if ( commitCount > myCommmitSize) { 
				conn.commit();
				commitCount = 0;					
			}				
		}
		
		reportCount++;
		if (reportCount >= reportSize ) {
			iParas.logPrint("   "+countText+IntParas.tausenderLead(statisticCountObj));
			reportCount = 0;
		}					
		
	      int situation = 0;
	      if ( rs.getObject("attr_id_m") == null ) {
	    	  situation = 1;
	    	  updateMoveAttr++;	
	      } else if ( rs.getObject("attr_id_s") == null );
	      else { 
	    	  situation = 2;
	    	  updateMoveWert++;
	      }

		if ( doPronto2Really ) {			
			if ( situation == 1 ) {
				// move attribute				
				u_updateMoveSlaveAttrToMasterPronto1.setInt(rs.getInt("obj_id_m4s"));
				u_updateMoveSlaveAttrToMasterPronto1.setInt(rs.getInt("attr_id_s"));
				i_updateMoveSlaveAttrToMasterPronto1++;										
				// das geht auf attribute
				u_updateMoveSlaveAttrToMaster.setInt(rs.getInt("obj_id_m4s"));
				u_updateMoveSlaveAttrToMaster.setInt(rs.getInt("attr_id_s"));
				i_updateMoveSlaveAttrToMaster++;												
			} else if (situation == 2 ) {				
				// move wert
				u_updateMoveWertToMasterAttributPronto1.setInt(rs.getInt("attr_id_m"));
				u_updateMoveWertToMasterAttributPronto1.setInt(rs.getInt("obj_id_m4s"));
				u_updateMoveWertToMasterAttributPronto1.setInt(rs.getInt("attr_id_s"));
				i_updateMoveWertToMasterAttributPronto1++;					
				// delete attribute
				u_updateDelete.setInt(rs.getInt("attr_id_s"));
				i_updateDelete ++;	
			}	
		} else {				
			if ( situation == 1 ) {
				if ( iParas.hasPronto1()) {
					updMoveAttrToObjP1.setInt(1, rs.getInt("obj_id_m4s"));
					updMoveAttrToObjP1.setInt(2, rs.getInt("attr_id_s"));
					updMoveAttrToObjP1.executeUpdate();							
				}						
				updMoveAttrToObj.setInt(1, rs.getInt("obj_id_m4s"));
				updMoveAttrToObj.setInt(2, rs.getInt("attr_id_s"));
				updMoveAttrToObj.executeUpdate();				
			} else if (situation == 2 ) {
				if ( iParas.hasPronto1()) { // nic123
					updMoveWertToAttrP1.setInt(1, rs.getInt("attr_id_m"));
					updMoveWertToAttrP1.setInt(2, rs.getInt("obj_id_m4s"));
					updMoveWertToAttrP1.setInt(3, rs.getInt("attr_id_s"));
					updMoveWertToAttrP1.executeUpdate();										
				} else {							
					updMoveWertToAttr.setInt(1, rs.getInt("attr_id_m"));
					updMoveWertToAttr.setInt(2, rs.getInt("attr_id_s"));
					updMoveWertToAttr.executeUpdate();
				}				
				// und auch löschen
				updDeleteAttribute.setInt(1, rs.getInt("attr_id_s"));
				updDeleteAttribute.executeUpdate();				
			}			
		 } // ende IF pronto really ELSE

	} // ende WHILE

	if ( doPronto2Really ) {
		iParas.logPrint("  Did cache [ "+ iParas.tausender(statisticCountObj) +" ] value-migration-requests");
	} else {
		conn.commit();
		iParas.logPrint("  Did [ "+ iParas.tausender(statisticCountObj) +" ] value-migrations-requests  ");			
	}

	iParas.logPrint("  From this "+ iParas.tausender(updateMoveAttr) +" solo values");	
	//iParas.logDebug2("  davon "+ updateDeleteCount +" = Beides/Varianz=1, Delete Sl");	
	iParas.logPrint("  From this "+ iParas.tausender(updateMoveWert) +" parallel values for one attribute");
} catch ( Exception ec ) {					
	iParas.printExceptionBy(ec, "Mig3Migrate.migrate obj,attr,wert");				
	} finally {
		try { if (rs != null) rs.close(); } catch (Exception e) {}	
		try { if (stmt != null) stmt.close(); } catch (Exception e) {}	         
    
  } // ende TRY
 } // ende 2
// gecachte updates verarbeiten
if ( doPronto2Really) {
	// updater ausführen
	
	Mp mpFinalUpdate = iParas.mm.beginMeasurePoint(mpObjMig, "consolidated-update");
	
	// index point fuer update ausführen
	if (iParas.shouldRunClassicIndexpoint(iParas.ix_iMigObj_Upd,parentMp));
	

	{   
		// updateDelete	
		String title ="MigObj.Upp-Delete"; /* ! */ 
		Mp mpBulk = iParas.mm.beginMeasurePoint(mpFinalUpdate, title);
		iParas.logUpdaterIntro(u_updateDelete,i_updateDelete); /* ! */ 
		u_updateDelete.switchFromWritingToReading(i_updateDelete); /* ! */ 
		
		int commitCount = 0; int reportCount = 0; int myCommmitSize = iParas.getUpdateCommitSize(); int statisticCount = 0; int affected = 0;	
		// if ( iParas.getAdapt() ) myCommmitSize= iParas.getCommitSizeAdapted2(); /* ! */ 
			
		for ( int i = 0; i< i_updateDelete ; i++ ) { /* ! */		
			/* ! */ 
			updDeleteAttribute.setInt(1, u_updateDelete.getInt());
			//updateDelete.setInt(2, u_updateDelete.getInt());
			affected = affected +updDeleteAttribute.executeUpdate();	
			
			statisticCount++;
			commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
			reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_updateDelete,statisticCount); reportCount = 0;}		
		}	
		conn.commit();
		u_updateDelete.close(statisticCount); /* ! */ 
		iParas.logUpdaterExtro(u_updateDelete, statisticCount,affected);	
		mpBulk.addMeasure("delete_actions", statisticCount);
		mpBulk.addMeasure("deleted", affected);
		mpBulk.addMeasure("table 'attr'", 1);
		mpBulk.addMeasure("commit_size", myCommmitSize);
		mpBulk.finalizeAndPrint();
	} // updateDelete done	
	
{   // updateMoveSlaveAttrToMaster	
	String title ="MigObj.Upp-MoveSlaveAttrToMaster"; /* ! */ 
	Mp mpBulk = iParas.mm.beginMeasurePoint(mpFinalUpdate, title);
	
	iParas.logUpdaterIntro(u_updateMoveSlaveAttrToMaster,i_updateMoveSlaveAttrToMaster); /* ! */ 
	u_updateMoveSlaveAttrToMaster.switchFromWritingToReading(i_updateMoveSlaveAttrToMaster); /* ! */ 
	
	int commitCount = 0; int reportCount = 0; int myCommmitSize = iParas.getUpdateCommitSize(); int statisticCount = 0; int affected = 0;	
	//if ( iParas.getAdapt() ) myCommmitSize= iParas.getCommitSizeAdapted2(); /* ! */ 
		
	for ( int i = 0; i< i_updateMoveSlaveAttrToMaster ; i++ ) { /* ! */		
		/* ! */ 
		updMoveAttrToObj.setInt(1, u_updateMoveSlaveAttrToMaster.getInt());
		updMoveAttrToObj.setInt(2, u_updateMoveSlaveAttrToMaster.getInt());
		affected = affected +updMoveAttrToObj.executeUpdate();	
		
		statisticCount++;
		commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
		reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_updateMoveSlaveAttrToMaster,statisticCount); reportCount = 0;}		
	}	
	conn.commit();
	u_updateMoveSlaveAttrToMaster.close(statisticCount); /* ! */ 
	iParas.logUpdaterExtro(u_updateMoveSlaveAttrToMaster, statisticCount,affected);	
	mpBulk.addMeasure("update_actions", statisticCount);
	mpBulk.addMeasure("updated", affected);	
	mpBulk.addMeasure("table 'attr'", 1);
	mpBulk.addMeasure("commit_size", myCommmitSize);
	mpBulk.finalizeAndPrint();
} // updateMoveSlaveAttrToMaster done

{   // updateMoveSlaveAttrToMasterPronto1	
	String title ="MigObj.Upp-MoveSlaveAttrToMasterPronto1"; /* ! */
	Mp mpBulk = iParas.mm.beginMeasurePoint(mpFinalUpdate, title);
	 
	iParas.logUpdaterIntro(u_updateMoveSlaveAttrToMasterPronto1,i_updateMoveSlaveAttrToMasterPronto1); /* ! */ 
	u_updateMoveSlaveAttrToMasterPronto1.switchFromWritingToReading(i_updateMoveSlaveAttrToMasterPronto1); /* ! */ 
	
	int commitCount = 0; int reportCount = 0; int myCommmitSize = iParas.getUpdateCommitSize(); int statisticCount = 0; int affected = 0;	
	//if ( iParas.getAdapt() ) myCommmitSize= iParas.getCommitSizeAdapted2(); /* ! */ 
		
	for ( int i = 0; i< i_updateMoveSlaveAttrToMasterPronto1 ; i++ ) { /* ! */		
		/* ! */ 
		updMoveAttrToObjP1.setInt(1, u_updateMoveSlaveAttrToMasterPronto1.getInt());
		updMoveAttrToObjP1.setInt(2, u_updateMoveSlaveAttrToMasterPronto1.getInt());
		affected = affected +updMoveAttrToObjP1.executeUpdate();	
		
		statisticCount++;
		commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
		reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_updateMoveSlaveAttrToMasterPronto1,statisticCount); reportCount = 0;}		
	}	
	conn.commit();
	u_updateMoveSlaveAttrToMasterPronto1.close(statisticCount); /* ! */ 
	iParas.logUpdaterExtro(u_updateMoveSlaveAttrToMasterPronto1, statisticCount,affected);	
	mpBulk.addMeasure("update_actions", statisticCount);
	mpBulk.addMeasure("updated", affected);	
	mpBulk.addMeasure("table 'wert'", 1);
	mpBulk.addMeasure("commit_size", myCommmitSize);
	mpBulk.finalizeAndPrint();
} // updateMoveSlaveAttrToMasterPronto1 done



{   // updateMoveWertToMasterAttributPronto1	
	String title ="MigObj.Upp-MoveWertToMasterAttributPronto1"; /* ! */
	Mp mpBulk = iParas.mm.beginMeasurePoint(mpFinalUpdate, title);
	iParas.logUpdaterIntro(u_updateMoveWertToMasterAttributPronto1,i_updateMoveWertToMasterAttributPronto1); /* ! */ 
	u_updateMoveWertToMasterAttributPronto1.switchFromWritingToReading(i_updateMoveWertToMasterAttributPronto1); /* ! */ 
	
	int commitCount = 0; int reportCount = 0; int myCommmitSize = iParas.getUpdateCommitSize(); int statisticCount = 0; int affected = 0;	
	// if ( iParas.getAdapt() ) myCommmitSize= iParas.getCommitSizeAdapted2(); /* ! */ 
		
	for ( int i = 0; i< i_updateMoveWertToMasterAttributPronto1 ; i++ ) { /* ! */		
		/* ! */ 

		updMoveWertToAttrP1.setInt(1, u_updateMoveWertToMasterAttributPronto1.getInt());

		updMoveWertToAttrP1.setInt(2, u_updateMoveWertToMasterAttributPronto1.getInt());

		updMoveWertToAttrP1.setInt(3, u_updateMoveWertToMasterAttributPronto1.getInt());

		affected = affected +updMoveWertToAttrP1.executeUpdate();	

		
		statisticCount++;

		commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	 

		reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_updateMoveWertToMasterAttributPronto1,statisticCount); reportCount = 0;}	

	}	

	conn.commit();

	u_updateMoveWertToMasterAttributPronto1.close(statisticCount); /* ! */ 

	iParas.logUpdaterExtro(u_updateMoveWertToMasterAttributPronto1, statisticCount,affected);		
	mpBulk.addMeasure("update_actions", statisticCount);
	mpBulk.addMeasure("updated", affected);	
	mpBulk.addMeasure("table 'wert'", 1);
	mpBulk.addMeasure("commit_size", myCommmitSize);
	mpBulk.finalizeAndPrint();
} // updateMoveWertToMasterAttributPronto1 done


	
if ( doPronto2FinalClose) {
	// index point fuer release ausführen
	if (iParas.shouldRunClassicIndexpoint(iParas.ix_iMigObj_Rel,mpFinalUpdate)) ;
	
}	

mpFinalUpdate.finalizeAndPrint(); 

	
} // ende prontoReally nachverarbeitung
} catch ( Exception ec ) {					
	iParas.printExceptionBy(ec, "Mig3Migrate.migrate obj,attr,wert-Updater");	
} finally {
	
	try { if ( updMoveAttrToObj!= null ) updMoveAttrToObj.close(); } catch (Exception e) {}	
	try { if ( updMoveAttrToObjP1!= null ) updMoveAttrToObjP1.close(); } catch (Exception e) {}
	try { if ( updDeleteAttribute!= null ) updDeleteAttribute.close(); } catch (Exception e) {}
	try { if ( updMoveWertToAttr!= null ) updMoveWertToAttr.close(); } catch (Exception e) {}
	try { if ( updMoveWertToAttrP1!= null ) updMoveWertToAttrP1.close(); } catch (Exception e) {}

	try { if ( u_updateMoveSlaveAttrToMaster!= null ) u_updateMoveSlaveAttrToMaster.close(); } catch (Exception e) {}	
	try { if ( u_updateMoveSlaveAttrToMasterPronto1!= null ) u_updateMoveSlaveAttrToMasterPronto1.close(); } catch (Exception e) {}
	try { if ( u_updateDelete!= null ) updDeleteAttribute.close(); } catch (Exception e) {}
	// try { if ( u_updateMoveWertToMasterAttribut!= null ) u_updateMoveWertToMasterAttribut.close(); } catch (Exception e) {}
	try { if ( u_updateMoveWertToMasterAttributPronto1!= null ) u_updateMoveWertToMasterAttributPronto1.close(); } catch (Exception e) {}


}
try { mpObjMig.addMeasure("attr_migs", statisticCountObj);
		iParas.mpMain.addMeasure(theround+".attr_migs", statisticCountObj);
	  mpObjMig.finalizeAndPrint(); 
	  
	  //mpObjMig.addMeasure("registered-requests", statisticCount);			    
	} catch (Exception e) {}	

} // ende der werte-migration
	
	// 
	// mig_perform_obj_bez2_migration
	//	  
	  {       
		    Statement stmt = null;
		    ResultSet rs = null;
		    // iParas.logPrintDebug("");
		    PreparedStatement prepared = null;
		    Mp mpBezB = iParas.mm.beginMeasurePoint(parentMp, "mig_bez_B");
		    iParas.logPrint("Started to migrate relations / B -> A  ..."); 
		    int statisticCount = 0;	
		    try {


				 stmt = iParas.getConn().createStatement();
				 String queText = "select "+
							" bez.bez_id as bez_id, "+
							" mig_obj.master_obj_id as master_obj_id "+
							"from  "+
							"  mig_obj as mig_obj, "+
							"  bez as bez "+
							"WHERE  "+
							"  bez.obj_id_b = mig_obj.slave_obj_id; ";
				 rs=iParas.doRunAndExplainQuery("4.4", "migrate relations / B","migration" , 
						 queText,stmt,iParas.sx_MigCoreGetBezBMigs,mpBezB,
						 iParas.ix_sx_MigCoreGetBezBMigs,true);				 
								
				prepared = conn.prepareStatement(
								" update bez set obj_id_b=? where bez_id = ?;");
				
				int commitCount = 0;
				
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						conn.commit();
						commitCount = 0;					
					}		

					prepared.setInt(1, rs.getInt("master_obj_id"));
					prepared.setInt(2, rs.getInt("bez_id"));

					prepared.executeUpdate();
				}
				conn.commit();
				iParas.logPrint("   Did "+ iParas.tausender(statisticCount) +" migrate relations / B");		
		    } catch ( Exception ec ) {					
		    	iParas.printExceptionBy(ec, "Mig3Migrate.migrate bez-B");		
			} finally {
		        		    
			    try { if (rs != null) rs.close();  } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { if (prepared != null) prepared.close(); } catch (Exception e) {}			    
			    try { mpBezB.addMeasure("bez_b_migs", statisticCount);
			    	  iParas.mpMain.addMeasure(theround+".bez_b_migs", statisticCount);
			    	  mpBezB.finalizeAndPrint(); 
			    	  	    
			    	} catch (Exception e) {}
		  }
		  }		

	// 
	// mig_perform_obj_bez_migration
	//
	  
	  {       
		    Statement stmt = null;
		    ResultSet rs = null;
		    PreparedStatement prepared =null;
		    Mp mpBezA = iParas.mm.beginMeasurePoint(parentMp, "mig_bez_A");
		    int statisticCount = 0;	
		        
		    try {
		    	iParas.logPrint("Started to migrate relations / A -> B"); 

				 stmt = iParas.getConn().createStatement();
				 String queText = "select "+
							" bez.bez_id as bez_id, "+
							" mig_obj.master_obj_id as master_obj_id "+
							"from  "+
							"  mig_obj as mig_obj, "+
							"  bez as bez "+
							"WHERE  "+
							"  bez.obj_id_a = mig_obj.slave_obj_id; ";
				 rs=iParas.doRunAndExplainQuery("4.5", "migrate relations / A","migration" , 
						 queText,stmt,iParas.sx_MigCoreGetBezAMigs,mpBezA, 
						 iParas.ix_sx_MigCoreGetBezAMigs,true);				 

					
				 prepared = conn.prepareStatement(
								" update bez set obj_id_a=? where bez_id = ?;");
				
				int commitCount = 0;
				
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						conn.commit();
						commitCount = 0;					
					}		

					prepared.setInt(1, rs.getInt("master_obj_id"));
					prepared.setInt(2, rs.getInt("bez_id"));

					prepared.executeUpdate();
				}
				
				iParas.logPrint("   Did "+ iParas.tausender(statisticCount) +" migrate relations / A ");	
				
		    } catch ( Exception ec ) {					
		    	iParas.printExceptionBy(ec, "Mig3Migrate.migrate bez-A");				
			} finally {			    
			    try { if (rs != null) rs.close();   } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try {  if (prepared != null) prepared.close(); } catch (Exception e) {}	
			    try { conn.commit(); } catch (Exception e) {}	
			    
			    try { 
			    	mpBezA.addMeasure("bez_a_migs", statisticCount);	
			    iParas.mpMain.addMeasure(theround+".bez_a_migs", statisticCount);
		    	    
			    	mpBezA.finalizeAndPrint(); 
			    	  		    
			    	} catch (Exception e) {}			    
		  }
	}  	
	// 
	// migrierte_ziele auf unaktuell
	//	
	  
	  {
		  Statement stmt = null;
		  ResultSet rs = null;
		  PreparedStatement psUpdater = null;
		  
		    Mp mpUnaktuell = iParas.mm.beginMeasurePoint(parentMp, "mig_ziel_unaktuell");
		    int statisticCount = 0;			  
		  
		  try {
		    
		    stmt = iParas.getConn().createStatement();
		    
		    String queText = "select " +
		    		" attr.attr_id as attr_id "+
		    		"from " + 
		    		" mig_obj as mig_obj, " +
		    		" attr as attr  "+
		    		"WHERE " + 
		    		" mig_obj.master_obj_id = attr.obj_id and " +
		    		" attr.top_aktuell = 1; ";
		      
		    rs=iParas.doRunAndExplainQuery("4.6", "mark attributes to re-top","migration" , 
		    		queText,stmt,iParas.sx_MigUntop,mpUnaktuell,
		    		iParas.ix_sx_MigUntop,true);
		    
		    psUpdater = conn.prepareStatement(
				"update  attr set top_aktuell = 0 where attr_id = ?;");
		    
		    int commitCount = 0;
		    
		    while (rs.next()) {
		      commitCount++;
		      statisticCount++;
		      if ( commitCount > commmitSize) { 
		        conn.commit();
		        commitCount = 0;    
		      }		
		      
		      
		      psUpdater.setInt(1, rs.getInt("attr_id"));
		      psUpdater.execute();
		    }
		      
		    iParas.logPrint("Did mark "+ iParas.tausender(statisticCount) +" attributes for future recalculation of top-value due to migration-involvement.");	
		  } catch ( Exception ec ) {					
		    	iParas.printExceptionBy(ec, "Mig3Migrate.migrierte attribute auf top-unaktuell");		  
		  } finally {
		    
		    try { conn.commit(); } catch (Exception e) {}	
		    try { if (rs != null) rs.close();  } catch (Exception e) {}	
		    try { if (stmt != null) stmt.close(); } catch (Exception e) {}		
		    try { if (psUpdater != null) psUpdater.close(); } catch (Exception e) {}	
		    
		    
		    try { mpUnaktuell.addMeasure("future top recalcs", statisticCount);
		    	mpUnaktuell.finalizeAndPrint(); 
		    				    
		    	} catch (Exception e) {}		    
		  }
		}	 
	  
		// 
		// migrierte objekte loeschen
		//	
		  
		  {
			  Statement stmt = null;
			  ResultSet rs = null;
			  PreparedStatement psUpdater =null;
			  
			  Mp mpMigDel = iParas.mm.beginMeasurePoint(parentMp, "remove migrated objects");
			  int statisticCount = 0;		
			    
			  try {
			    

			    stmt = iParas.getConn().createStatement();
		    
			    String queText = "select mig_obj.slave_obj_id as obj_id "+
								 " from  "+
								 "  mig_obj; ";
			      
			    rs=iParas.doRunAndExplainQuery("4.7", "remove migrated objects","migration" , 
			    		queText,stmt,iParas.sx_MigRemoveObjects,mpMigDel,
			    		iParas.ix_sx_MigRemoveObjects,true);
			    
			    
			     psUpdater = conn.prepareStatement(
			    				" DELETE  FROM obj WHERE obj_id = ?;");
			    
			    int commitCount = 0;
			    
			    while (rs.next()) {
			      commitCount++;
			      statisticCount++;
			      if ( commitCount > commmitSize) { 
			        conn.commit();
			        commitCount = 0;    
			      }		
			      
			      
			      psUpdater.setInt(1, rs.getInt("obj_id"));
			      psUpdater.execute();
			    }
			      
			    iParas.logPrint("Did remove "+ iParas.tausender(statisticCount) +" migrated objects.");	
			  } catch ( Exception ec ) {					
			    	iParas.printExceptionBy(ec, "Mig3Migrate.migrierte objekte löschen");			  
			  } finally {
			    
			    try { conn.commit(); } catch (Exception e) {}	
			    try { if (rs != null) rs.close();   } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { if (psUpdater != null) stmt.close(); } catch (Exception e) {}	
			    
			    
			    try { 
			    	mpMigDel.addMeasure("removed_objects", statisticCount);	
			    	mpMigDel.finalizeAndPrint(); 
			    			    
			    	} catch (Exception e) {}
			  }
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
	private static String xpostgresUpdate = 	"update "+
		"  obj "+
		"set "+
		"  pub_ist_aktuell = 0 "+
		"where obj_id in (   "+
		"select "+
		" obj.obj_id "+
		"from  "+
		"  mig_obj as mig_obj, "+
		"  obj as obj "+
		"WHERE  "+
		"  mig_obj.master_obj_id = obj.obj_id and "+
		"  obj.pub_ist_aktuell = 1 ); "	;
	private static String xmySqlUpdate = 
		"update "+
		"  obj "+ 
		"set "+ 
		"  pub_ist_aktuell = 0 "+ 
		"where obj_id in (   SELECT * FROM( "+
		"select "+ 
		" obj.obj_id "+ 
		"from "+  
		"  mig_obj as mig_obj, "+ 
		"  obj as obj "+ 
		"WHERE "+  
		"  mig_obj.master_obj_id = obj.obj_id and "+ 
		"  obj.pub_ist_aktuell = 1 )blabla); " ; 
	*/	
	
	
	private static String postgresOuterJoin ="select "+
			"  master.mig_id_m as mig_id_m, "+
			"  master.obj_id_m as obj_id_m, "+
			"  master.attr_typ_m as attr_typ_m, "+
			"  master.attr_id_m as attr_id_m, "+
			"  master.obj_typ_m as obj_typ_m, "+
			"  slave.mig_id_s as mig_id_s, "+
			"  slave.obj_id_s as obj_id_s, "+
			"  slave.obj_id_m4s as obj_id_m4s, "+
			"  slave.attr_typ_s as attr_typ_s, "+
			"  slave.attr_id_s as attr_id_s "+ 
			"from "+
			"( select "+
			"   mig_obj.mig_obj_id as mig_id_m, "+
			"   mig_obj.master_obj_id as obj_id_m, "+
			"   attr.attr_typ as attr_typ_m, "+ 
			"   attr.attr_id as attr_id_m, "+
			"   obj.obj_typ as obj_typ_m "+
			"  from  "+
			"   mig_obj as mig_obj, "+
			"   attr as attr, "+
			"   obj as obj "+
			"  WHERE  "+
			"   attr.obj_id = mig_obj.master_obj_id and "+
			"   obj.obj_id = mig_obj.master_obj_id ) master "+
			"full outer join (      "+
			"  select "+
			"   mig_obj.mig_obj_id as mig_id_s, "+
			"   mig_obj.slave_obj_id as obj_id_s, "+
			"   mig_obj.master_obj_id as obj_id_m4s,  "+
			"   attr.attr_typ as attr_typ_s,  "+
			"   attr.attr_id as attr_id_s "+
			"  from  "+
			"   mig_obj as mig_obj, "+
			"   attr as attr "+
			"  WHERE  "+
			"   attr.obj_id = mig_obj.slave_obj_id) slave "+
			"on ( master.mig_id_m = slave.mig_id_s and "+
			"     master.attr_typ_m = slave.attr_typ_s ) ; ";
	private static String mySqlOuterJoin =
	"select "+ 
	"  master.mig_id_m as mig_id_m, "+ 
	"  master.obj_id_m as obj_id_m, "+ 
	"  master.attr_typ_m as attr_typ_m, "+ 
	"  master.attr_id_m as attr_id_m, "+ 
	"  master.obj_typ_m as obj_typ_m, "+ 
	"  slave.mig_id_s as mig_id_s, "+ 
	"  slave.obj_id_s as obj_id_s, "+ 
	"  slave.obj_id_m4s as obj_id_m4s,  "+
	"  slave.attr_typ_s as attr_typ_s, "+ 
	"  slave.attr_id_s as attr_id_s "+  
	"from "+ 
	"( select "+ 
	"   mig_obj.mig_obj_id as mig_id_m, "+ 
	"   mig_obj.master_obj_id as obj_id_m, "+ 
	"   attr.attr_typ as attr_typ_m, "+  
	"   attr.attr_id as attr_id_m, "+ 
	"   obj.obj_typ as obj_typ_m  "+
	"  from "+  
	"   mig_obj as mig_obj, "+ 
	"   attr as attr, "+ 
	"   obj as obj "+ 
	"  WHERE "+  
	"   attr.obj_id = mig_obj.master_obj_id and "+ 
	"   obj.obj_id = mig_obj.master_obj_id ) master "+ 
	"left outer join ( "+      
	"  select "+ 
	"   mig_obj.mig_obj_id as mig_id_s, "+ 
	"   mig_obj.slave_obj_id as obj_id_s, "+ 
	"   mig_obj.master_obj_id as obj_id_m4s, "+  
	"   attr.attr_typ as attr_typ_s, "+  
	"   attr.attr_id as attr_id_s "+ 
	"  from "+  
	"   mig_obj as mig_obj, "+ 
	"   attr as attr "+ 
	"  WHERE "+  
	"   attr.obj_id = mig_obj.slave_obj_id) slave "+ 
	"on ( master.mig_id_m = slave.mig_id_s and "+ 
	"     master.attr_typ_m = slave.attr_typ_s ) "+
	"UNION  "+
	"select  "+
	"  master.mig_id_m as mig_id_m,  "+
	"  master.obj_id_m as obj_id_m, "+ 
	"  master.attr_typ_m as attr_typ_m,   "+
	"  master.attr_id_m as attr_id_m,  "+
	"  master.obj_typ_m as obj_typ_m, "+ 
	"  slave.mig_id_s as mig_id_s, "+ 
	"  slave.obj_id_s as obj_id_s,  "+
	"  slave.obj_id_m4s as obj_id_m4s,  "+
	"  slave.attr_typ_s as attr_typ_s, "+ 
	"  slave.attr_id_s as attr_id_s  "+ 
	"from   "+
	"( select   "+
	"   mig_obj.mig_obj_id as mig_id_m,  "+
	"   mig_obj.master_obj_id as obj_id_m,  "+
	"   attr.attr_typ as attr_typ_m, "+  
	"   attr.attr_id as attr_id_m, "+ 
	"   obj.obj_typ as obj_typ_m "+ 
	"  from "+  
	"   mig_obj as mig_obj,  "+
	"   attr as attr, "+ 
	"   obj as obj  "+
	"  WHERE  "+ 
	"   attr.obj_id = mig_obj.master_obj_id and "+ 
	"   obj.obj_id = mig_obj.master_obj_id ) master "+ 
	"right outer join ( "+      
	"  select "+ 
	"   mig_obj.mig_obj_id as mig_id_s, "+ 
	"   mig_obj.slave_obj_id as obj_id_s, "+ 
	"   mig_obj.master_obj_id as obj_id_m4s, "+  
	"   attr.attr_typ as attr_typ_s, "+  
	"   attr.attr_id as attr_id_s  "+
	"  from  "+ 
	"   mig_obj as mig_obj, "+ 
	"   attr as attr  "+
	"  WHERE "+ 
	"   attr.obj_id = mig_obj.slave_obj_id) slave "+ 
	"on ( master.mig_id_m = slave.mig_id_s and "+ 
	"     master.attr_typ_m = slave.attr_typ_s )  "+              
	"      ;  ";		
	
}