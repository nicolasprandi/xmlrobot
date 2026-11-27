package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Step2Mig3Frisch {
	
public static void run( IntParas iParas,Mp parentMp ) throws Exception {
	
/*[requo-timer]*/
/*[requo-customer]*/
	
boolean removeSlaveMigrators = true;


Connection conn = iParas.getConn();
int commmitSize = iParas.getCommitSize();
int reportSize = iParas.getReportSize(); 

//
// von mig_obj nach frisch kopieren
// tbd:
// hier könnte man noch die migrations-slaves rauslöschen
// das kann dann mit parameter gesteuert werden ob das gebraucht wird
// es muss allerdings auch so funktionieren



if ( iParas.dss && !iParas.isPostgres ) {
	// copy mig_obj to frisch by dss
	
	Mp mpMigFrischDss = iParas.mm.beginMeasurePoint(parentMp, "frisch_insert_dss");	
	iParas.logPrint("Going to handle [ MIG_OBJ by FRISCH ] with database-specific-statement ... ");
	Statement stmtInsert = null;
	try {
		stmtInsert = iParas.getConn().createStatement();
		int returner = stmtInsert.executeUpdate(
				"insert into frisch "+   
						"select "+ 
						"  mig_obj.master_obj_id, "+
						"  mig_obj.obj_typ "+
						"from "+ 
						"  mig_obj as mig_obj; " );
		conn.commit();
		iParas.logPrint("Did insert "+ iParas.tausender(returner) +" records into frisch due to master-migration-role.");
		mpMigFrischDss.addMeasure("mig_obj_count", returner);
	} catch ( Exception ec ) {			
		ec.printStackTrace();
		iParas.printExceptionBy(ec, "2Mig3FrischMigrations.insert table 'frisch'/dss");
		throw ( ec);
	} finally {
		try { if (stmtInsert != null) stmtInsert.close(); } catch (Exception e) {}	
		mpMigFrischDss.finalizeAndPrint();
	}
	
	if ( removeSlaveMigrators ) {
		
		Statement stmt = null;
	    ResultSet rs = null;

	    PreparedStatement frischDelete = null;
	    Mp mpMigFrischJdbcDelete = iParas.mm.beginMeasurePoint(parentMp, "delete frisch");
	    iParas.logPrint("Going to delete [ FRISCH ] due to slave-migratione-role with dss/jdbc.... ");
	    try {
			//iParas.logDebug("Going to:  Copy objects... ");
			int commitCount = 0;
			int statisticCountDeleted = 0;
			 stmt = iParas.getConn().createStatement();
			 String queText = " select "+
		                "  mig_obj.slave_obj_id "+
						" from "+
						"  mig_obj as mig_obj;";
			 
			 rs =  iParas.doRunAndExplainQuery("2.3A", "mig_obj unfile","mig_obj_slave remove frisch" , queText,stmt,
						iParas.sx_Mig2FrischDssDelete,
						mpMigFrischJdbcDelete,
						iParas.ix_sx_Mig2FrischDssDelete,true);


			commitCount = 0;
			frischDelete = conn.prepareStatement(
					" DELETE FROM frisch where obj_id=?; ");
			
			
			commitCount = 0;
		
			while (rs.next()) {
				commitCount++;

				if ( commitCount > commmitSize) { 
					conn.commit();
					commitCount = 0;				
				}					

				

				frischDelete.setInt(1, rs.getInt("slave_obj_id"));
				int deletedNow = frischDelete.executeUpdate();
				statisticCountDeleted = statisticCountDeleted + deletedNow;
		
			}
			conn.commit();

			iParas.logPrint("Did delete "+ iParas.tausender( statisticCountDeleted) +" frisch-entries due to slave-migration-role.");
			mpMigFrischJdbcDelete.addMeasure("frisch_deleted", statisticCountDeleted);
			

		} catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "2.2A MigFrischMigrationsDelete/dss");	
			throw ( ec);
			
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}	
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			try { if (frischDelete != null) frischDelete.close(); } catch (Exception e) {}	
			
			mpMigFrischJdbcDelete.finalizeAndPrint();
	  }		
	}
	
} else {
	iParas.logPrint("Going to copy [ MIG_OBJ to FRISCH ] with jdbc.... ");

	Statement stmt = null;
    ResultSet rs = null;
    // PreparedStatement objInsert = null;
    PreparedStatement frischInsert = null;
    PreparedStatement frischDelete = null;
    Mp mpMigFrischJdbc = iParas.mm.beginMeasurePoint(parentMp, "copy mig_obj to frisch");
	try {
		//iParas.logDebug("Going to:  Copy objects... ");
		int commitCount = 0;
		int statisticCountInsert = 0;
		int statisticCountDeleted = 0;
		 stmt = iParas.getConn().createStatement();
		 String queText = " select "+
	                "  mig_obj.master_obj_id,"+
	                "  mig_obj.obj_typ, "+
	                "  mig_obj.slave_obj_id "+
					" from "+
					"  mig_obj as mig_obj;";
		 
		 rs =  iParas.doRunAndExplainQuery("2.2A", "mig_obj file","mig_obj nach frisch" , queText,stmt,
					iParas.sx_Mig2FrischJdbc,
					mpMigFrischJdbc,
					iParas.ix_sx_Mig2FrischJdbc,true);


		commitCount = 0;
		statisticCountInsert = 0;

		frischInsert = conn.prepareStatement(
				" insert into frisch (obj_id, obj_typ) values (?,?);"); 
		
		if ( removeSlaveMigrators ) {
			frischDelete = conn.prepareStatement(
					" DELETE FROM frisch where obj_id=?; ");
		}
		
		
		commitCount = 0;
		statisticCountInsert = 0;		
		while (rs.next()) {
			commitCount++;
			statisticCountInsert++;
			if ( commitCount > commmitSize) { 
				conn.commit();
				commitCount = 0;				
			}					
			frischInsert.setInt(1, rs.getInt("master_obj_id"));
			frischInsert.setInt(2, rs.getInt("obj_typ"));
			frischInsert.executeUpdate();	
			
			if ( removeSlaveMigrators ) {
				frischDelete.setInt(1, rs.getInt("slave_obj_id"));
				int deletedNow = frischDelete.executeUpdate();
				statisticCountDeleted = statisticCountDeleted + deletedNow;
			}			
		}
		conn.commit();
		iParas.logPrint("Did copy "+ iParas.tausender(statisticCountInsert) +" mig_obj to frisch.");	
		mpMigFrischJdbc.addMeasure("frisch_insert", statisticCountInsert);
		if ( removeSlaveMigrators ) {
			iParas.logPrint("Did delete "+ iParas.tausender(statisticCountDeleted) +" frisch-entries due to slave-migration-role.");
			mpMigFrischJdbc.addMeasure("frisch_deleted", statisticCountDeleted);
		}

	} catch ( Exception ec ) {					
		iParas.printExceptionBy(ec, "2.2A MigFrischMigrations/jdbc");	
		throw ( ec);
		
	} finally {
		try { if (rs != null) rs.close(); } catch (Exception e) {}	
		try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		try { if (frischInsert != null) frischInsert.close(); } catch (Exception e) {}	
		try { if (frischDelete != null) frischDelete.close(); } catch (Exception e) {}	
		
		mpMigFrischJdbc.finalizeAndPrint();
  }
}


	
	  		
}						
	
	
	
	
	
}