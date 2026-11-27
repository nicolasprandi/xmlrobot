package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import at.nicolas.x.int_specs.Commons;

public class StepALimit {

	
//private static IntegrateParas iiParas = null;
public static void run( IntParas iParas, Mp parentMp ) throws Exception {
	/*[requo-timer]*/
	/*[requo-customer]*/
	
	Connection conn = iParas.getConn();
	int commmitSize = iParas.getCommitSize();
	int reportSize = iParas.getReportSize(); 
	
	int objectsInTotal = -1;
	{
		Statement stmt=null;
	    ResultSet rs=null;
	    
	    try {
			stmt = iParas.getConn().createStatement();
			rs = stmt.executeQuery("select count(*) as anzahl from  obj  ;");
			if (rs.next())
				objectsInTotal = rs.getInt("anzahl");
	    } catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "StepALimit.count");
			throw (ec);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}	
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		}

	}
	if ( objectsInTotal == -1 ) {
		iParas.logPrint("Objects could not be counted");
		throw new Exception("Objects could not be counted");
		
	} else if ( objectsInTotal > iParas.maxObjectsReal) {
		// die effektive anzahl überschritten
		Mp mpLimit = iParas.mm.beginMeasurePoint(parentMp, "ObjectLimitReduction");
		iParas.logPrint(
				"Tolerant Object Limit [ "+iParas.tausender(iParas.maxObjectsReal)+" ] has been reached. "+
				"Number of objects is [ "+iParas.tausender(objectsInTotal)+" ]");
		iParas.logPrint("Object-Reduction will be performed!");
		
		mpLimit.addMeasure("tolerant_limit", iParas.maxObjectsReal);
		mpLimit.addMeasure("contract_limit", iParas.maxObjectsContract);
		mpLimit.addMeasure("object_count", objectsInTotal);

		if (iParas.shouldRunClassicIndexpoint(IntParas.ix_limit_start,mpLimit)) ;
				
		StepBLimit.run(iParas, mpLimit, objectsInTotal);
		
		if (iParas.shouldRunClassicIndexpoint(IntParas.ix_limit_end,mpLimit)) ;
		
		mpLimit.finalizeAndPrint();
		  
	} else if (objectsInTotal > iParas.maxObjectsContract) {
		// die effektive anzahl des vertrags
		Mp mpLimit = iParas.mm.beginMeasurePoint(parentMp, "ContractualObjectLimitReached");
		
		String t1 = "Contractual Object Limit of [ "+iParas.tausender(iParas.maxObjectsContract)+" ] has been reached. ";
		String t1b = "Current number of effectively existing objects is [ "+iParas.tausender(objectsInTotal)+" ].";		
		String t2 = "Object-Reduction will be performed as soon as ";
		String t2b = "the effective limit of [ "+
				iParas.tausender(iParas.maxObjectsReal)+" ] will be exceeded.";		
		iParas.logPrint(t1);
		iParas.logPrint(t1b);
		iParas.logPrint(t2);
		iParas.logPrint(t2b);
		
		iParas.newMessageWillCome();
		iParas.addFinalMessage(t1);
		iParas.addFinalMessage(t1b);
		iParas.addFinalMessage(t2);
		iParas.addFinalMessage(t2b);
		
		
		mpLimit.addMeasure("tolerant_limit", iParas.maxObjectsReal);
		mpLimit.addMeasure("contract_limit", iParas.maxObjectsContract);
		mpLimit.addMeasure("object_count", objectsInTotal);
		
		
				
		mpLimit.finalizeAndPrint();		
		
	}else if (objectsInTotal > iParas.maxObjectsWarning) {
		// Zeit zum warnen
		
		Mp mpLimit = iParas.mm.beginMeasurePoint(parentMp, "ObjectLimitWarning");
		
		String t1 = "Warning Object Limit [ "+iParas.tausender(iParas.maxObjectsWarning)+" ] has been reached. "+
				"Number of objects is [ "+iParas.tausender(objectsInTotal)+" ]";
		String t2 = "Object-Reduction will be performed when the effective limit of [ "+
				iParas.tausender(iParas.maxObjectsReal)+" ] is reached.";
		
		iParas.logPrint(t1);
		iParas.logPrint(t2);
		
		iParas.newMessageWillCome();
		iParas.addFinalMessage(t1);
		iParas.addFinalMessage(t2);
		
		mpLimit.addMeasure("warning_limit", iParas.maxObjectsWarning);
		mpLimit.addMeasure("tolerant_limit", iParas.maxObjectsReal);
		mpLimit.addMeasure("contract_limit", iParas.maxObjectsContract);
		mpLimit.addMeasure("object_count", objectsInTotal);
				
		mpLimit.finalizeAndPrint();				
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