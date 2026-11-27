package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import at.nicolas.x.int_specs.Commons;

public class StepBLimit {

	
//private static IntegrateParas iiParas = null;
public static void run( IntParas iParas, Mp mpLimit,int objectsInTotal ) throws Exception {
	/*[requo-timer]*/
	/*[requo-customer]*/
	// Mp mpLimit = iParas.mm.beginMeasurePoint(parentMp, "ObjectDeletionByLimit");
	Connection conn = iParas.getConn();
	
	int reportSize = iParas.getReportSize(); 
	int commmitSize = iParas.getCommitSize();
	
    if ( iParas.getAdapt())
    	commmitSize= iParas.getCommitSizeAdapted2();	
	
	int objects_to_remove = objectsInTotal - iParas.maxObjectsReal;
	mpLimit.addMeasure("nbr_obj_to_remove", objects_to_remove);
	iParas.mpMain.addMeasure("nbr_obj_to_remove", objects_to_remove);
	int max_obj_id = -1;
	{
		Statement stmt=null;
	    ResultSet rs=null;
	    
	    try {
			stmt = iParas.getConn().createStatement();
			rs = stmt.executeQuery("select obj_id as obj_id from obj order by obj_id DESC limit "+
			(objects_to_remove+1)+";");
			while (rs.next()) {
				max_obj_id = rs.getInt("obj_id");
			}
				
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "StepBLimit.get-max-obj-id");
			throw (ec);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}	
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		}

	}
	if ( objectsInTotal == -1 ) {
		iParas.logPrint("Problem in fetching the max-obj-id.");
		throw new Exception("Problem in fetching the max-obj-id.");		
	}
	mpLimit.addMeasure("max_obj_id", max_obj_id);
	iParas.newMessageWillCome();
	iParas.addFinalMessage("Reduction of objects due to licence-limit performed !");
	iParas.addFinalMessage("Applied limitation of object-count = "+iParas.tausender(iParas.maxObjectsReal));
	iParas.addFinalMessage("Currently number of tried objects = "+iParas.tausender(objectsInTotal));
	iParas.addFinalMessage("Number of removed objects = "+iParas.tausender(objects_to_remove));
	iParas.addFinalMessage("Remaining max obj_id = "+max_obj_id);
   
	/* mehr
	 * 
	 */
	{       
	    Statement stmt = null;
	    ResultSet rs = null;

		PreparedStatement preparedDel =null;
	     
	    Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce mehr");
	    int statisticCount = 0;
	    int commitCount = 0;
	    
	    try {
			
			stmt = iParas.getConn().createStatement();
			String queText = 
					" select "+
					" mehr.wert_id "+
					" from mehr,wert,attr "+
					" where "+
					" mehr.wert_id = wert.wert_id and"+
					" wert.attr_id = attr.attr_id and "+
					" attr.obj_id > "+max_obj_id+";";

		    rs=iParas.doRunAndExplainQuery(
		    		"10.mehr", "reduce mehr","limit" , 
		    		queText,stmt,-1,mpDel,-1,false);				    
			
			preparedDel = conn.prepareStatement(
					" DELETE FROM mehr WHERE wert_id = ?;");			
			
			while (rs.next()) {
				commitCount++;
				statisticCount++;
				if ( commitCount > commmitSize) { 
					conn.commit();
					commitCount = 0;					
				}		
				//System.out.println("mehr.wert_id = "+rs.getInt("wert_id"));
				preparedDel.setInt(1, rs.getInt("wert_id"));
				preparedDel.executeUpdate();								
			}
			
			//iParas.logPrintDebug("Did delete "+ statisticCount +" records in table 'mehr'");		
			//iParas.logPrintDebug(" ");
			mpDel.addMeasure("deleted_mehr", statisticCount);
		} catch ( Exception ec ) {					
			ec.printStackTrace();
			iParas.printExceptionBy(ec, "StepBLimit.remove mehr");
			throw (ec);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}	
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			try { if ( preparedDel!= null) preparedDel.close(); } catch (Exception e) {}	

			try { conn.commit(); } catch (Exception e) {}
			try { 
				
				mpDel.finalizeAndPrint(); 
							    
				} catch (Exception e) {}
		    
	  }
	} 	
	
	// iParas.logPrintDebug(" ");
	/* wert
	 * 
	 */
	{
    Statement stmt = null;
    ResultSet rs = null;

	PreparedStatement preparedDel =null;
     
    Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce wert");
    int statisticCount = 0;
    int commitCount = 0;
    
    try {
		
		stmt = iParas.getConn().createStatement();
		String queText = 
				" select "+
				" wert.wert_id "+
				" from wert,attr "+
				" where "+
				" wert.attr_id = attr.attr_id and "+
				" attr.obj_id > "+max_obj_id+";";

	    rs=iParas.doRunAndExplainQuery(
	    		"10.wert", "reduce wert","limit" , 
	    		queText,stmt,-1,mpDel,-1,false);				    
		
		preparedDel = conn.prepareStatement(
				" DELETE FROM wert WHERE wert_id = ?;");			
		
		while (rs.next()) {
			commitCount++;
			statisticCount++;
			if ( commitCount > commmitSize) { 
				conn.commit();
				commitCount = 0;					
			}		
			//System.out.println("wert.wert_id = "+rs.getInt("wert_id"));
			preparedDel.setInt(1, rs.getInt("wert_id"));
			preparedDel.executeUpdate();								
		}
		
		//iParas.logPrintDebug("Did delete "+ statisticCount +" records in table 'wert'");		
		//iParas.logPrintDebug(" ");
		mpDel.addMeasure("deleted_wert", statisticCount);
	} catch ( Exception ec ) {					
		ec.printStackTrace();
		iParas.printExceptionBy(ec, "StepBLimit.remove wert");
		throw (ec);
	} finally {
		try { if (rs != null) rs.close(); } catch (Exception e) {}	
		try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		try { if ( preparedDel!= null) preparedDel.close(); } catch (Exception e) {}	

		try { conn.commit(); } catch (Exception e) {}
		try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
	    
	}}
 		
	// iParas.logPrintDebug(" ");
	/* attr
	 * 
	 */
    {   
    	Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce attr");
	    Statement stmt = null;
	         
	    try{
			stmt = iParas.getConn().createStatement();
			int deleted = stmt.executeUpdate( 
					"delete from attr where obj_id>"+max_obj_id+";");
			
			conn.commit();
			mpDel.addMeasure("deleted_attr", deleted);
			//iParas.logPrintDebug("Did delete "+ deleted +" records in table 'attr'");		
			//iParas.logPrintDebug(" ");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "10.attr");	
		} finally {

			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
		}
	}    
    // iParas.logPrintDebug(" ");
    
	/* frisch
	 * 
	 */
    {   
    	Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce frisch");
	    Statement stmt = null;
	         
	    try{
			stmt = iParas.getConn().createStatement();
			int deleted = stmt.executeUpdate( 
					"delete from frisch where obj_id>"+max_obj_id+";");
			
			conn.commit();
			mpDel.addMeasure("deleted_frisch", deleted);
			//iParas.logPrintDebug("Did delete "+ deleted +" records in table 'attr'");		
			//iParas.logPrintDebug(" ");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "10.frisch");	
		} finally {

			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
		}
	}    
 // iParas.logPrintDebug(" ");    
    
	/* obj
	 * 
	 */    
    {   
    	Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce obj");
	    Statement stmt = null;
	         
	    try{
			stmt = iParas.getConn().createStatement();
			int deleted = stmt.executeUpdate( 
					"delete from obj where obj_id>"+max_obj_id+";");
			
			conn.commit();
			mpDel.addMeasure("deleted_obj", deleted);
			//iParas.logPrintDebug("Did delete "+ deleted +" records in table 'attr'");		
			//iParas.logPrintDebug(" ");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "10.obj");	
		} finally {

			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
		}
	}    
 // iParas.logPrintDebug(" ");    
    
	
	/* beza
	 * 
	 */
    {   
    	Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce bez/a");
	    Statement stmt = null;
	         
	    try{
			stmt = iParas.getConn().createStatement();
			int deleted = stmt.executeUpdate( 
					"delete from bez where obj_id_a>"+max_obj_id+";");
			
			conn.commit();
			mpDel.addMeasure("deleted_bez/a", deleted);
			//iParas.logPrintDebug("Did delete "+ deleted +" records in table 'attr'");		
			//iParas.logPrintDebug(" ");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "10.bez/a");	
		} finally {

			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
		}
	}    
 // iParas.logPrintDebug(" ");	
	/* bezb
	 * 
	 */
    {   
    	Mp mpDel = iParas.mm.beginMeasurePoint(mpLimit, "Reduce bez/b");
	    Statement stmt = null;
	         
	    try{
			stmt = iParas.getConn().createStatement();
			int deleted = stmt.executeUpdate( 
					"delete from bez where obj_id_b>"+max_obj_id+";");
			
			conn.commit();
			mpDel.addMeasure("deleted_bez/b", deleted);
			//iParas.logPrintDebug("Did delete "+ deleted +" records in table 'attr'");		
			//iParas.logPrintDebug(" ");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "10.bez/b");	
		} finally {

			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { mpDel.finalizeAndPrint(); } catch (Exception e) {}
		}
	}    
    // iParas.logPrintDebug(" ");
  }				
						
	private static int tsDifferCounter = 0;

	private static boolean tsDiffer ( Timestamp a, Timestamp b) {
		if ( a== null && b == null ) return false;
		else if ( a== null && b != null ) return true;
		else if ( b== null && a != null ) return true;
		return  ( !a.equals(b));
	}	
}