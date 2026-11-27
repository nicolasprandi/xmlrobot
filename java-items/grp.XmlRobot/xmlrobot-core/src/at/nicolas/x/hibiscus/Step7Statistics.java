package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import at.nicolas.x.int_specs.Commons;

public class Step7Statistics {
	public static void run( IntParas iParas,
			Mp parentMp ) throws Exception {
		/*[requo-timer]*/
		/*[requo-customer]*/
		Connection conn = iParas.getConn();
		int commmitSize = iParas.getCommitSize();


// 
// top perform
//
Timestamp now = null; 
{
	Date date = new Date();
	long time = date.getTime();
	now = new Timestamp(time);
}			


//
// objekt-uebersicht
//
{       
 Statement stmtTruncate = null;
 Statement stmtInsert = null;
     
 try {
	 stmtTruncate = iParas.getConn().createStatement();
	 int zurueck1 = stmtTruncate.executeUpdate(
	 		" TRUNCATE r_o;");
	 iParas.getConn().commit();	

	 stmtInsert = iParas.getConn().createStatement();
	 int zurueck2 = stmtInsert.executeUpdate(
	 		" INSERT into r_o ( obj_typ, anzahl )  "+
	     "  SELECT "+
	     "  min(obj.obj_typ) as obj_typ, "+ 
	     " count(*) as anzahl  "+
	     " from "+
	     "   obj as obj "+
	     " group by "+
	     "   obj.obj_typ "+
	     " order by "+
	     "   obj.obj_typ;"
	       );
	 iParas.getConn().commit();	
	 iParas.logPrint(" ");      
	 iParas.logPrint("Did create overview of objects");	
	 iParas.logPrint("nbr of          object-types= "+iParas.tausender(zurueck2)+".");	
	 iParas.logPrint("nbr of previous object-types= "+iParas.tausender(zurueck1)+".");
	 iParas.logPrint(" "); 
	 parentMp.addMeasure("nbr_obj_types_before", zurueck1);
	 parentMp.addMeasure("nbr_obj_types", zurueck2);
	 
	 iParas.mpMain.addMeasure("nbr_obj_types", zurueck2);
	 
				
	} catch (Exception e1) {
	  	iParas.logPrintException(e1);
		throw ( e1);
	} finally {     
		
		
		try { if (stmtTruncate != null) stmtTruncate.close(); } catch (Exception e) {}	
		try { if (stmtInsert != null) stmtInsert.close(); } catch (Exception e) {}	
	}
}
					
		
}	
		
	
}