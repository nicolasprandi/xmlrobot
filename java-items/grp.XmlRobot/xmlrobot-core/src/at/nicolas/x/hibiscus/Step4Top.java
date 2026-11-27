package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import at.nicolas.x.int_specs.Commons;

public class Step4Top {
	
	private static final String n_updateOnTopValue ="wertIsTop.wert" ;	
	private static final String t_updateOnTopValue ="update wert set ist_top=1 where wert_id = ?;" ;
	public static void run( IntParas iParas, Mp parentMp ) throws Exception {
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

if (1<2){ // gibt nur noch newtop
	       
	    //Statement stmt = null;
	    //ResultSet rs = null;
	    PreparedStatement prepared = null; 
	    
	    int statisticCount = 0;
	    Mp mpClear = iParas.mm.beginMeasurePoint(parentMp, "setWertIsTOP");
	    iParas.logPrint("Started to set wert.is_top to 0 ...");
	    try {

			prepared = conn.prepareStatement(
			
			" update wert set ist_top=0 where attr_id in ( select attr_id from attr where top_aktuell = 0); ");
			statisticCount = prepared.executeUpdate();

			conn.commit();
			iParas.logPrint("   Did clear [ "+ iParas.tausender(statisticCount) +" ] werte ");		
			
		} finally {
		    try { if (prepared != null) prepared.close(); } catch (Exception e) {}	
		    
		    try { 
		    	mpClear.addMeasure("cleared_werte", statisticCount);
		    	mpClear.finalizeAndPrint(); 
		    				    
		    	} catch (Exception e) {}
}}
{       
if ( iParas.solo ) {
	
	PreparedStatement updateOnTopValue = null;
	PreparedStatement updateTopaktuell = null;	
	Statement stmt = null;
	ResultSet rsAttributes = null;	
	Mp mpSolo = iParas.mm.beginMeasurePoint(parentMp, "solo");
	iParas.logPrint("Starting top-calulations solo ..."); 
	int statisticCount = 0;
try { 

int commitCount = 0;

int myCommmitSize = commmitSize;
if ( iParas.getAdapt())
	myCommmitSize= iParas.getCommitSizeAdapted2(); 
stmt = iParas.getConn().createStatement();
String queText = 
"select "+
" attr_id "+
"from ( "+
"select "+
" attr_id, "+
" ( select count(*) "+
"    from "+ 
"      wert "+
"    where "+ 
"      attr.attr_id = wert.attr_id ) as count_wert "+
"from "+ 
"  attr "+
"where "+ 
"  attr.top_aktuell = 0) as vorlauf "+
"where count_wert = 1;  ";

rsAttributes=iParas.doRunAndExplainQuery("5.1.a", "identify solo-werte in attributes","top werte" , 
		queText,stmt,
		iParas.sx_TopSolo,mpSolo, iParas.ix_sx_TopSolo,false);

updateOnTopValue = conn.prepareStatement(
	" update wert set ist_top=1 where attr_id = ?;");

updateTopaktuell = conn.prepareStatement(
		" update attr set top_aktuell=1 where attr_id = ?;");

while (rsAttributes.next()) {

	commitCount++;
	statisticCount++;

	updateOnTopValue.setInt(1, rsAttributes.getInt("attr_id"));			
	updateOnTopValue.executeUpdate();
	
	updateTopaktuell.setInt(1, rsAttributes.getInt("attr_id"));			
	updateTopaktuell.executeUpdate();	
			
	if ( commitCount > myCommmitSize) { 
		//iParas.loadedTop(statisticCount);
		conn.commit();
		commitCount = 0;				
	}						
}

// iParas.loadedTop(statisticCount);
conn.commit();
iParas.logPrint("Did identify "+ iParas.tausender(statisticCount) +" werte as solo werte in attribute, marked them all as top-value and top_aktuell");	   
//iParas.logPrintDebug(" ");    
}  catch (Exception e1) {
	iParas.printExceptionBy(e1,"solo-algorithm in top");
	throw ( e1);
} finally {
	try { if (stmt != null) stmt.close();  } catch (Exception ex ) {}
	try { if (rsAttributes != null) rsAttributes.close(); } catch (Exception ex ) {}
	try { if (updateOnTopValue != null) updateOnTopValue.close(); } catch (Exception ex ) {}
	
	try { mpSolo.addMeasure("soletti", statisticCount);
		  mpSolo.finalizeAndPrint(); 
		  			    
		} catch (Exception e) {}	
}
} 	
} // ende solo
{       // da spielt die musik
	
	Mp mpTop = iParas.mm.beginMeasurePoint(parentMp, "top-calcs");

iParas.logPrint("Started to top-calculations core ...");
PreparedStatement updateOnTopValueNew = null;


if ( !iParas.hasPronto2And(iParas.upp_top) ) {
	updateOnTopValueNew = conn.prepareStatement(
			" update wert set ist_top=1 where wert_id = ?;");	
}

/*
PreparedStatement notOnTopValue  = conn.prepareStatement(
		" update wert set ist_top=0 where wert_id <> ? and attr_id=?;");
*/

//pronto 2
Updater u_updateOnTopValue =null ;
//Updater u_notOnTopValue =null ;

int i_updateOnTopValue =0 ;
//int i_notOnTopValue =0 ;

Statement stmt = null;
ResultSet rs = null;

int myCommmitSize = commmitSize;
if ( iParas.getAdapt())
	myCommmitSize= iParas.getCommitSizeAdapted2(); 

int reportSize = iParas.getReportSize();   

try { 
	
stmt = iParas.getConn().createStatement();
String queText = 
		"select "+
		" attr.attr_id as attr_id, "+
		" wert.wert_id as wert_id, "+
		" wert.wann as wann, "+
		" wert.von as von , "+
		" wert.bis as bis "+   
		"from "+
		" attr as attr,"+
		" wert as wert "+
		"where "+
		" attr.top_aktuell = 0 and "+
		" wert.attr_id = attr.attr_id "+
		"order by "+
		" attr_id, wert.wann DESC;";	

if ( iParas.hasPronto2And(iParas.upp_top) ) {
	 u_updateOnTopValue = iParas.getUpdater(
			 "Top",n_updateOnTopValue,
			 1,100*1*iParas.bytesPerInt,t_updateOnTopValue);
	 /*
	 u_notOnTopValue = iParas.getUpdater(
			 "Top","notOnTopValue",
			 2,100*2*iParas.bytesPerInt);		 */
	 
	 // vor-select.mig.werte
	 if (iParas.shouldRunClassicIndexpoint(iParas.ix_iTop_Sel,parentMp));
	 
}

rs=iParas.doRunAndExplainQuery("5.1.b", "unaktuelle attribute/mehrfach","top werte" , 
		queText,stmt,iParas.sx_TopWerte,mpTop,iParas.ix_sx_TopWerte,false);


int commitCount = 0;
int statisticCount = 0;
int reportCount = 0;

int filteredCount = 0;


//boolean debugMe = false;

int last_attr_id = -1;

//int obj_id = -1;
int attr_id = -1;
int bester_wert = -1;

//Timestamp von_wert_bester = null;// =rsWerte.getTimestamp("von");
Timestamp bis_wert_bester = null;// =rsWerte.getTimestamp("bis");
//Timestamp wann_wert_bester = null;// =rsWerte.getTimestamp("wann");	

int affected_updateOnTopValue =0;
int affected_notOnTopValue =0;
	String countText = null;
if ( iParas.hasPronto2And(iParas.upp_top) )
	countText ="Top-Count:";
else 
	countText ="Top-Count:"; 
while (rs.next()) {
	statisticCount++;
	
	reportCount++;
	if (reportCount >= reportSize ) {
		iParas.logPrint("   "+countText+IntParas.tausenderLead(statisticCount));
		reportCount = 0;
	}				
	
	attr_id = rs.getInt("attr_id");
	
	if ( attr_id != last_attr_id ) {
		// das ist eine neue attr_id
		
		// wenn das nicht der erste ist wird das alte fertiggemacht
		if ( last_attr_id > -1 ) {
			// das alte attribut fertigmachen
			filteredCount++;
			/*if ( debugMe ) {
				System.out.println("- - best-wert="+bester_wert);
			}*/
			if ( iParas.hasPronto2And(iParas.upp_top) ) {
				
				u_updateOnTopValue.setInt(bester_wert);
				i_updateOnTopValue++;		
				/* nic333
				if ( !iParas.newtop) {
					u_notOnTopValue.setInt(bester_wert);
					u_notOnTopValue.setInt(attr_id);
					i_notOnTopValue++;		
				} */
				
			} else {
				 
				updateOnTopValueNew.setInt(1, bester_wert);			
				affected_updateOnTopValue= affected_updateOnTopValue+
						updateOnTopValueNew.executeUpdate();
				// gibt nur noch newtop
				/*
				if ( !iParas.newtop) { 
					notOnTopValue.setInt(1, bester_wert);	
					notOnTopValue.setInt(2, attr_id);	
					affected_notOnTopValue= affected_notOnTopValue+
							notOnTopValue.executeUpdate();					
				} */
		
							
				commitCount++;
				
				
				if ( commitCount > myCommmitSize) { 
					//iParas.loadedTop(statisticCount);
					conn.commit();
					commitCount = 0;				
				}					
				
			}
			
		
			
		}
		// das last_attr_id setzten
		last_attr_id = attr_id;
		
		// das neue attribut eroeffnen
		//obj_id = rsAttributes.getInt("obj_id");
		bester_wert = -1;
		/*
		if ( debugMe ) {
			System.out.println("- obj="+obj_id+" attr="+attr_id);
		}*/
		 //von_wert_bester = null;// =rsWerte.getTimestamp("von");
		 bis_wert_bester = null;// =rsWerte.getTimestamp("bis");
		 //wann_wert_bester = null;// =rsWerte.getTimestamp("wann");				
	}
	
	// nun die verarbeitung für den wert
	

	
	Integer wert_id = (Integer)rs.getObject("wert_id");
	/*if ( debugMe ) {
		System.out.println("- - wert="+wert_id);
	}	*/	
	//Timestamp von_wert =rsAttributes.getTimestamp("von");
	Timestamp bis_wert =rs.getTimestamp("bis");
	//Timestamp wann_wert =rsAttributes.getTimestamp("wann");
	boolean das_wird_bester =false;
	if ( bester_wert == -1 ) das_wird_bester = true;
	else {
		if ( bis_wert == null && bis_wert_bester != null ) {
			das_wird_bester = true;
		} else if ( bis_wert != null && bis_wert_bester != null ) {
			int compResult = bis_wert.compareTo(bis_wert_bester);
			if ( compResult > 0 ) {
				das_wird_bester = true;
			}
		}
		
	}
	if ( das_wird_bester ) {
		bester_wert = wert_id;
		//von_wert_bester= von_wert;
		bis_wert_bester = bis_wert;
		//wann_wert_bester = wann_wert;				
	}
	

				
}
// nach schleifen-ende die endverarbeitung für das letzte attribut,
if ( last_attr_id > -1 ) {
	filteredCount++;
	if ( iParas.hasPronto2And(iParas.upp_top) ) {
		
		u_updateOnTopValue.setInt(bester_wert);
		i_updateOnTopValue++;		
		/*if ( !iParas.newtop) {
		u_notOnTopValue.setInt(bester_wert);
		u_notOnTopValue.setInt(attr_id);
		i_notOnTopValue++;	
		} */
		
	} else {
		 
		updateOnTopValueNew.setInt(1, bester_wert);			
		affected_updateOnTopValue= affected_updateOnTopValue+
				updateOnTopValueNew.executeUpdate();
		// gibt nur noch newtop
		/*
		if ( !iParas.newtop) {				
		notOnTopValue.setInt(1, bester_wert);	
		notOnTopValue.setInt(2, attr_id);	
		affected_notOnTopValue= affected_notOnTopValue+
				notOnTopValue.executeUpdate();		
		} */
		
	}	
}

if ( iParas.hasPronto2And(iParas.upp_top) ) {
	iParas.logPrint("  Did cache [ "+ iParas.tausender(filteredCount) +" ] top-requests/attributes");
} else {
	conn.commit();

	iParas.logPrint("   Did work on "+ iParas.tausender(filteredCount)+" top-requests/attributes "+
	" affected_updateOnTopValue="+affected_updateOnTopValue+
	" affected_notOnTopValue="+affected_notOnTopValue);		
}

//iParas.loadedTop(statisticCount);
	
// iParas.logPrintDebug("  and marked finally "+ statisticCount +" werte as top-value ");    
    
  }  catch (Exception e1) {
	  	iParas.logPrintException(e1);
		throw ( e1);
	} finally {
       
    try { if (rs != null) rs.close(); } catch (Exception e) {}	
    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
    
       
  }

//gecachte updates verarbeiten
if ( iParas.hasPronto2And(iParas.upp_top)) {
	
	updateOnTopValueNew = conn.prepareStatement(
			" update wert set ist_top=1 where wert_id = ?;");	
	
	Mp mpUpdater = iParas.mm.beginMeasurePoint(mpTop, "Dedicated Update");
	// updater ausführen
	
	// index point fuer update ausführen
	if (iParas.shouldRunClassicIndexpoint(iParas.ix_iTop_Upd,mpUpdater)); 
	
	{   // updateOnTopValue	
		String title ="Top.updateOnTopValue"; /* ! */ 
		iParas.logUpdaterIntro(u_updateOnTopValue,i_updateOnTopValue); /* ! */ 
		u_updateOnTopValue.switchFromWritingToReading(i_updateOnTopValue); /* ! */ 
		
		int commitCount = 0; int reportCount = 0; int statisticCount = 0; int affected = 0;	
		myCommmitSize= iParas.getUpdateCommitSize();  
			
		for ( int i = 0; i< i_updateOnTopValue ; i++ ) { /* ! */		
			/* ! */ 
			updateOnTopValueNew.setInt(1, u_updateOnTopValue.getInt());
			affected = affected +updateOnTopValueNew.executeUpdate();	
			
			statisticCount++;
			commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
			reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(u_updateOnTopValue,statisticCount); reportCount = 0;}		
		}	
		conn.commit();
		u_updateOnTopValue.close(statisticCount); /* ! */ 
		iParas.logUpdaterExtro(u_updateOnTopValue, statisticCount,affected);		
	    try { if ( updateOnTopValueNew!= null) updateOnTopValueNew.close(); } catch (Exception e) {}	

	} // updateOnTopValue done
	/*
	{   
		
		u_notOnTopValue.switchFromWritingToReading(i_notOnTopValue);  
		int statisticCount = 0;
		if ( !iParas.newtop) {
		String title ="Top.notOnTopValue"; 
		iParas.logUpdaterIntro(title,i_notOnTopValue); 
		
		int commitCount = 0; int reportCount = 0;  int affected = 0;	
		myCommmitSize= iParas.getUpdateCommitSize(); 
			
		for ( int i = 0; i< i_notOnTopValue ; i++ ) { 		

			notOnTopValue.setInt(1, u_notOnTopValue.getInt());
			notOnTopValue.setInt(2, u_notOnTopValue.getInt());
			affected = affected +notOnTopValue.executeUpdate();	
			
			statisticCount++;
			commitCount++; if ( commitCount > myCommmitSize) {conn.commit();commitCount = 0;}	  		
			reportCount++; if (reportCount >= reportSize ) {iParas.logUpdaterCount(title,statisticCount); reportCount = 0;}		
		}	
		conn.commit();
		iParas.logUpdaterExtro(title, statisticCount,affected);	
		}
		u_notOnTopValue.close(statisticCount); 
		
	    try { if ( notOnTopValue!= null) notOnTopValue.close(); } catch (Exception e) {}	

	} notOnTopValue done	*/
	
	mpUpdater.finalizeAndPrint();
}
mpTop.finalizeAndPrint();
  } 	


		
//
// setze alle obj.pub_ist_akuell >> 0
//
{       
    Statement stmtUpAktuell = null;
    Mp mpAttrTopping = iParas.mm.beginMeasurePoint(parentMp, "AttributeTopping");

        
    try {
    	iParas.logPrint("  Going to mark attributes as top-aktuell");
    	stmtUpAktuell  = iParas.getConn().createStatement();
			int res = stmtUpAktuell.executeUpdate(
				"update attr set top_aktuell = 1 where top_aktuell = 0;"					
					);
			conn.commit();
			iParas.logPrint("   Did finally mark "+iParas.tausender(res)+" attributes as top_aktuell");
			mpAttrTopping.addMeasure("attributes_topped", res);
				
	} catch (Exception e1) {
	  	iParas.logPrintException(e1);
		throw ( e1);
	} finally {     
		if (stmtUpAktuell != null) 
			stmtUpAktuell.close();
	}
    mpAttrTopping.finalizeAndPrint();
} 

//if ( 1==1)
//throw new Exception("soll so sein");



					
		
}	
		
	
}