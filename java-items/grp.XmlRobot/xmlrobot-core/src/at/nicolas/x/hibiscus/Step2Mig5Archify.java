package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import at.nicolas.x.int_specs.Commons;

public class Step2Mig5Archify {
	
public static void run( IntParas iParas,
		Mp parentMp) throws Exception {
	/*[requo-timer]*/
	/*[requo-customer]*/
	boolean isPostgres = iParas.isPostgres;
	Connection conn = iParas.getConn();
	int commmitSize = iParas.getCommitSize();

	Mp mpArch = parentMp;	
	//
	// mig_table_copy
	//
	  {       
		    Statement stmt = null;
		    ResultSet rs = null;
		        
			if ( iParas.dss && !isPostgres ) {
				iParas.logPrint("Going to remember object-migrations with database-specific-statement ... ");
				Statement stmt2 = null;
				try {
					stmt2 = iParas.getConn().createStatement();
					int returner = stmt2.executeUpdate(
							"insert into mig_obj_hist "+
									"select "+ 
									"  mig_obj_id as mig_obj_id, "+
									"  obj_typ as obj_typ, "+
									"  ison as ison, "+
									"  datum as datum, "+
									"  uk as uk, "+
									"  uk_attr_usg_id as uk_attr_usg_id, "+
									"  uk_inhalt as uk_inhalt, "+
									"  master_obj_id as master_obj_id, "+
									"  slave_obj_id as slave_obj_id, "+
									"  slave_wert_id as slave_wert_id "+
									"from   mig_obj ; ");
					conn.commit();
					iParas.logPrint(" Did copy "+ iParas.tausender(returner) +" migrations.");
				} catch ( Exception ec ) {					
					iParas.printExceptionBy(ec, "Mig4Archify.insert into mig_obj_hist/dss");	
				} finally {
					if (stmt2 != null) stmt2.close();
				}
			} else {
				PreparedStatement objInsert = null;
		    try {
		    	iParas.logPrint("Going to remember object-migrations with jdbc-interface ... ");
				int commitCount = 0;
				int statisticCount = 0;
				 stmt = iParas.getConn().createStatement();
				 rs = stmt.executeQuery(
						"select "+
						" mig_obj_id, "+
						" obj_typ, "+
						" ison, "+
						" datum, "+
						" uk,  "+
						" uk_attr_usg_id, "+
						" uk_inhalt, "+
						" master_obj_id, "+
						" slave_obj_id, slave_wert_id "+
						"from mig_obj; "					
						);

				 objInsert = conn.prepareStatement(
						" insert into mig_obj_hist (mig_obj_id, obj_typ, ison, datum, uk,uk_attr_usg_id,uk_inhalt,master_obj_id,slave_obj_id, slave_wert_id  ) "+
						"values (?,?,?,?,?,?,?,?,?,?);");
				
				commitCount = 0;
				statisticCount = 0;		
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						conn.commit();
						commitCount = 0;
						
					}		
					// System.out.println(rs.getInt("obj_id")+" "+rs.getInt("obj_typ")+" "+rs.getInt("box_id"));
					objInsert.setInt(1, rs.getInt("mig_obj_id"));
					objInsert.setInt(2, rs.getInt("obj_typ"));
					objInsert.setInt(3, rs.getInt("ison"));
					objInsert.setTimestamp(4, rs.getTimestamp("datum"));
					objInsert.setString(5, rs.getString("uk"));
					objInsert.setInt(6, rs.getInt("uk_attr_usg_id"));
					objInsert.setString(7, rs.getString("uk_inhalt"));
					objInsert.setInt(8, rs.getInt("master_obj_id"));
					objInsert.setInt(9, rs.getInt("slave_obj_id"));
					objInsert.setInt(10, rs.getInt("slave_wert_id"));
					objInsert.execute();
				}
				
				conn.commit();
				iParas.logPrint("   Did file "+ iParas.tausender(statisticCount) +" object-migrations to mig_obj_hist.");	
		    } catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "Mig4Archify.insert into mig_obj_hist/jdbc");							
			} finally {
		    
		    try { if (rs != null) rs.close();   } catch (Exception e) {}	
		    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		    try { if (objInsert != null) objInsert.close(); } catch (Exception e) {}	
		  }
		    }
		  } 	
	
	//
	// mig_table_trunc
	//
	  {       
		    Statement stmt = null;

		    iParas.logPrint("Started to trunc table mig_obj ...");     
		    try{
				 stmt = iParas.getConn().createStatement();
				int trunc = stmt.executeUpdate(
					"delete from mig_obj;"					
						);
				
				conn.commit();
				iParas.logPrint("   Did trunc table mig_obj, result is "+trunc);
		    } catch ( Exception ec ) {					
				iParas.printExceptionBy(ec, "Mig4Archify.trung table 'mig_obj'");	
			} finally {

				if (stmt != null) stmt.close();
			}
		  } 
	  
	  try { 
		  
		  //mpArch.finalizeAndPrint(); 
					    
		} catch (Exception e) {}
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