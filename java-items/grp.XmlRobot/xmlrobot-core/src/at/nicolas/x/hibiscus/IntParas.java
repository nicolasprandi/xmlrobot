package at.nicolas.x.hibiscus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_coop.CoopDbci;
import at.nicolas.x.int_interpreter.InterSequence;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.tasks.Unique;

public class IntParas {
	protected Mp mpMain = null;
	protected int cnt_mv_rule_updates = 0;
	protected int cnt_mv_rule_deletes = 0;
	protected int cnt_mv_rule_inserts = 0;
	protected int cnt_mv_rule_reads = 0;
	protected static String[] selectnames = { 
			"Sel_PubBox", 
			"Sel_PubObj", 
			"Sel_PubAttr",
			"Sel_PubWert",
			"Sel_PubMehr", 
			"Sel_PubBez",
			"Sel_MigProduceMigs",
			"Sel_MigOptiEquals",
			"Sel_MigOptiCyles", 
			"Sel_MigOptiDouble", 
			"Sel_MigSmart",
			"Sel_MigDoValueMigs", 
			"Sel_MigDoBezBMigs", 
			"Sel_MigDoBezAMigs", 
			"Sel_MigUntop", 
			"Sel_MigRemoveObjects", 
			"Sel_HealRelations", 
			"Sel_HealAttributes", 
			"Sel_HealWert", 
			"Sel_TopSolo", 
			"Sel_TopWerte",
			"Sel_Mig2FrischJdbc",
			"Sel_Mig2FrischDssDelete",
			"Sel_KopfGrouped",
			"Sel_KopfPlain"
					};
	String[] smps = new String[selectnames.length];	
	protected static final int sx_PubBox = 0;
	protected static final int sx_PubObj = 1;
	protected static final int sx_PubAttr = 2;
	protected static final int sx_PubWert = 3;
	protected static final int sx_PubMehr = 4;
	protected static final int sx_PubBez = 5;
	protected static final int sx_MigProduceMigs = 6;
	protected static final int sx_MigOptiEquals = 7;
	protected static final int sx_MigOptiCyles = 8;
	protected static final int sx_MigOptiDouble = 9;
	protected static final int sx_MigSmart = 10;
	protected static final int sx_MigCoreGetAttrMigs = 11;
	protected static final int sx_MigCoreGetBezBMigs = 12;
	protected static final int sx_MigCoreGetBezAMigs = 13;
	protected static final int sx_MigUntop = 14;
	protected static final int sx_MigRemoveObjects = 15;
	protected static final int sx_HealRelations = 16;
	protected static final int sx_HealAttributes = 17;
	protected static final int sx_HealWert = 18;
	protected static final int sx_TopSolo = 19;
	protected static final int sx_TopWerte = 20;
	protected static final int sx_Mig2FrischJdbc = 21;
	protected static final int sx_Mig2FrischDssDelete = 22;
	protected static final int sx_KopfGrouped = 23;
	protected static final int sx_KopfPlain = 24;
	

	protected static String[] pointnames = { "Start-1",
			"SearchMigrations-2",
			"AfterFirstMigrationSearch-3",
			"Heal-4",
			"Top-5",
			"Statistics-6",
			"Kopf-7",
			"End-8",
			"iMigObj-Sel-09","iMigObj-Upd-10","iMigObj-Rel-11",
			"iHealWert-Sel-12","iHealWert-Upd-13",
			"iTop-Sel-14","iTop-Upd-15",
			"RoundBusyStart-16","RoundBusyEnd-17",
			"Before-PubBox-18", 
			"Before-PubObj-19", 
			"Before-PubAttr-20",
			"Before-PubWert-21",
			"Before-PubMehr-22", 
			"Before-PubBez-23",
			"Before-MigProduceMigs-24",
			"Before-MigOptiEquals-25",
			"Before-MigOptiCyles-26", 
			"Before-MigOptiDouble-27", 
			"Before-MigSmart-28",
			"Before-MigDoValueMigs-29", 
			"Before-MigDoBezBMigs-30", 
			"Before-MigDoBezAMigs-31", 
			"Before-MigUntop-32", 
			"Before-MigRemoveObjects-33", 
			"Before-HealRelations-34", 
			"Before-HealAttributes-35", 
			"Before-HealWert-36", 
			"Before-TopSolo-37", 
			"Before-TopWerte-38",
			"Before-Mig2FrischJdbc-39",
			"Before-Mig2FrischDssDelete-40",
			"Mv-41",
			"CustMv-42",
			"CustProcess-43",
			"Before-KopfGrouped-44",
			"Before-KopfPlain-45",
			"LimitCorrection-Start-46",
			"LimitCorrection-End-47"
			};
protected static String[] pointnamesShort = { "Start",
"SearchMigrations",
"AfterFirstMigrationSearch",
"Heal",
"Top",
"Statistics",
"Kopf",
"End",
"iMigObj-Sel","iMigObj-Upd","iMigObj-Rel",
"iHealWert-Sel","iHealWert-Upd",
"iTop-Sel","iTop-Upd",
"RoundBusyStart","RoundBusyEnd",			
"Before-PubBox", 
"Before-PubObj", 
"Before-PubAttr",
"Before-PubWert",
"Before-PubMehr", 
"Before-PubBez",
"Before-MigProduceMigs",
"Before-MigOptiEquals",
"Before-MigOptiCyles", 
"Before-MigOptiDouble", 
"Before-MigSmart",
"Before-MigDoValueMigs", 
"Before-MigDoBezBMigs", 
"Before-MigDoBezAMigs", 
"Before-MigUntop", 
"Before-MigRemoveObjects", 
"Before-HealRelations", 
"Before-HealAttributes", 
"Before-HealWert", 
"Before-TopSolo", 
"Before-TopWerte",
"Before-Mig2FrischJdbc",
"Before-Mig2FrischDssDelete",
"Mv",
"CustMv",
"CustProcess",
"Before-KopfGrouped",
"Before-KopfPlain",
"LimitCorrection-Start-46",
"LimitCorrection-End-47" };
/*
protected static boolean[] hardcoded = { 
true,
true,
true,
true,
true,
false,
false,
true,
false,false,false,
false,false,
false,false,
false,false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false,
false};	
*/
Imp[] imps = new Imp[pointnames.length];	

protected static final int ix_Start = 0;
protected static final int ix_SearchMigrations = 1;
protected static final int ix_AfterFirstMigrationSearch = 2;
protected static final int ix_Heal = 3;
protected static final int ix_Top = 4;
protected static final int ix_Statistics = 5;
protected static final int ix_Kopf = 6;
protected static final int ix_End = 7;

protected static final int ix_iMigObj_Sel = 8;
protected static final int ix_iMigObj_Upd = 9;
protected static final int ix_iMigObj_Rel = 10;

protected static final int ix_iHealWert_Sel = 11;
protected static final int ix_iHealWert_Upd = 12;

protected static final int ix_iTop_Sel = 13;
protected static final int ix_iTop_Upd = 14;	
protected static final int ix_roundBusyStart = 15;
protected static final int ix_roundBusyEnd = 16;

protected static final int ix_sx_PubBox = 17;
protected static final int ix_sx_PubObj = 18;
protected static final int ix_sx_PubAttr = 19;
protected static final int ix_sx_PubWert = 20;
protected static final int ix_sx_PubMehr = 21;
protected static final int ix_sx_PubBez = 22;
protected static final int ix_sx_MigProduceMigs = 23;
protected static final int ix_sx_MigOptiEquals = 24;
protected static final int ix_sx_MigOptiCyles = 25;
protected static final int ix_sx_MigOptiDouble = 26;
protected static final int ix_sx_MigSmart = 27;
protected static final int ix_sx_MigCoreGetAttrMigs = 28;
protected static final int ix_sx_MigCoreGetBezBMigs = 29;
protected static final int ix_sx_MigCoreGetBezAMigs = 30;
protected static final int ix_sx_MigUntop = 31;
protected static final int ix_sx_MigRemoveObjects = 32;
protected static final int ix_sx_HealRelations = 33;
protected static final int ix_sx_HealAttributes = 34;
protected static final int ix_sx_HealWert = 35;
protected static final int ix_sx_TopSolo = 36;
protected static final int ix_sx_TopWerte = 37;
protected static final int ix_sx_Mig2FrischJdbc = 38;
protected static final int ix_sx_Mig2FrischDssDelete = 39;
protected static final int ix_Mv = 40;
protected static final int ix_CustMv = 41;
protected static final int ix_CustProcess = 42;
protected static final int ix_sx_KopfGrouped = 43;
protected static final int ix_sx_KopfPlain = 44;

protected static final int ix_limit_start = 45;
protected static final int ix_limit_end = 46;

private boolean threadStopped = false;
public synchronized void shouldStopThread() {
	threadStopped = true;
	System.out.println("IntParas received request to threadStop");
}	
	private TaskLogger tl = null;
	boolean isPostgres = false;
	String dbms;
	String explainFile;
	boolean doExplain = false;
	int explainCounter = 0;
	IRequestHandler rh;	
	CoopDbci dbci = null;
	
	OutputStreamRequest osr = null;
	OutputStream psOutputStream = null;
				
	PrintStream psPrinter= null;
	
	boolean dss = false;
	int mv_style = 0;
	boolean solo = false;
	// int solo_starting_round = -1;
	boolean numform = false;
	boolean smart = false;
	boolean smart_in_opti = false;
	boolean simple = false;
	boolean doubleoccupation = false;
	protected boolean adapt = false;
	
	boolean isdetails= false;
	//boolean newopt= false;
	// boolean newtop= true;
	
	boolean upp_mig= false;
	boolean upp_heal= false;
	boolean upp_top= false;
	
	boolean kopfgrouped = false;
	boolean mvrefresh = false;
	boolean pso = false;
	
	long beginnMs = System.currentTimeMillis();

	protected static Duration getDurationString(long beginn, long end ) {

		long durationMs = (end - beginn) + 500;
		
		long durationS = durationMs / 1000;
		long HH = durationS / 3600;
		long MM = ( durationS % 3600) / 60;
		long SS = durationS % 60;
		
		String HH_ = Commons.leadBlanks(Long.toUnsignedString(HH),2);
		String MM_;
		String SS_;
		if ( HH > 0 ) {

			MM_= Commons.leadSymbols(Long.toUnsignedString(MM),2,"0");
			SS_ = Commons.leadSymbols(Long.toUnsignedString(SS),2,"0");
		} else {
			
			if ( MM < 10 )
				MM_= Commons.leadBlanks(Long.toUnsignedString(MM),2);
			else
				MM_= Commons.leadSymbols(Long.toUnsignedString(MM),2,"0");
			
			if ( MM > 0 )
				SS_ = Commons.leadSymbols(Long.toUnsignedString(SS),2,"0");
			else
				SS_ = Commons.leadBlanks(Long.toUnsignedString(SS),2);
		}
		
	
		
		Duration duration;
		
		if (HH>0 )         {
			duration = new Duration(HH_ + "'"+MM_+"'"+SS_,"hrs");
		} else if ( MM > 0 ) {
			duration = new Duration("   "+MM_+"'"+SS_,"min");
		} else if ( SS > 0 ) {
			duration = new Duration("      "+SS_,"sec");
		} else {
			duration = new Duration("       0","sec");
		}

		return duration; 
		
	}	
	protected MeasureSystem mm ;
	protected void setAdapt(boolean _adapt ) {
		if ( hasPronto1() )
			adapt=_adapt;
	}
	
	protected boolean getAdapt( ) {
		return adapt;
	}	
	
	private boolean dropIndex = false;
	private int pronto = 0;	
	

	String index;
	String breaker;
	String cust_flag;
	String kick;
	String mv;

	
	/*
	protected static final int ix_ = ;
	protected static final int ix_ = ;
	protected static final int ix_ = ;
	protected static final int ix_ = ;
	protected static final int ix_ = ;
	protected static final int ix_ = ; */
	

	
	String tempDir = null;

	protected String getTempfileClassicName(String filetitle ) {
		
		return Commons.fileClassic(tempDir+filetitle);
	
		
	}
	
	protected void doSmartKeyReduction (Mp parentMp) throws Exception {
		
	    Statement stmt = null;
	    ResultSet rs = null;
	    PreparedStatement prepared = null; 
	    Mp mpSmart = mm.beginMeasurePoint(parentMp, "smart-key-reduction");
	    logPrint("Going to remove wert-identifiers of migrated-attributes (SmartKeyReduction) ... ");
	      
	    int statisticCount = 0;
	    
	    /*if ( dss && !isPostgres ) {
			logPrint("Going to perform smart-key-reduction with database-specific-statement ... ");
			Statement stmt2 = null;
			try {
				stmt2 = getConn().createStatement();
				statisticCount = stmt2.executeUpdate(
						"delete from wert where wert_id in (select slave_wert_id as wert_id from mig_obj);");
				conn.commit();
				logPrint(" Did delete "+ statisticCount +" werte.");
			} finally {
				if (stmt2 != null) stmt2.close();
			}
		} else */ {
	    	// without dss
		    try {


				 stmt = getConn().createStatement();
				 String queText = 
						 	" select slave_wert_id as wert_id from mig_obj; ";
				 rs= doRunAndExplainQuery("4.2b", "remove wert-identifiers of slave-objects","SmartKeyReduction" , queText,stmt,
						 sx_MigSmart,mpSmart,ix_sx_MigSmart,true);				 

				
				prepared = conn.prepareStatement(
				" DELETE FROM wert WHERE wert_id = ?; ");
				int commitCount = 0;
				int cs = getCommitSize();				
				while (rs.next()) {
					commitCount++;
					statisticCount++;
					if ( commitCount > cs) { 
						conn.commit();
						commitCount = 0;					
					}		

					prepared.setInt(1, rs.getInt("wert_id"));

					prepared.executeUpdate();
				}

				conn.commit();
				logPrint("   Did eleminate [ "+ statisticCount +" ] wert-identifiers of slave-objects  ");		
				// logPrintDebug("");
			} finally {
			    try { if (rs != null) rs.close(); } catch (Exception e) {}	
			    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
			    try { if (prepared != null) prepared.close(); } catch (Exception e) {}	

		  }		    	
	    }

	    try { mpSmart.addMeasure("deleted_werte", statisticCount);
	    	  mpSmart.finalizeAndPrint(); 
	    	 			    
	    	} catch (Exception e) {}		
		
	}
	protected Updater getUpdater(
			String category,
			String name,
			int    nuberOfFields,
			int    bufferSize,
			String commandDoku ) throws FileNotFoundException {
		
	
		
		return new Updater(
				getTempfileClassicName(name+".daten"), 
				category,
				name,
				nuberOfFields,
				bufferSize,
				tl,
				commandDoku);
		
	}	
	protected Updater getUpdater(
			String category,
			String name,
			int    nuberOfFields,
			int    bufferSize) throws FileNotFoundException {
		
		return getUpdater(category,name,nuberOfFields,bufferSize,null);
		
	}
	protected static final int bytesPerInt = 4;

	
	protected boolean shouldRunClassicIndexpoint(int index_point, Mp masterMesurePoint ) throws Exception {
		if ( imps[index_point] !=null && dropIndex && hasPronto1()) {
			// da gibts eine definition wird ersetzt

			Mp mpIp;
			if ( masterMesurePoint != null ) {
				mpIp = mm.beginMeasurePoint(
						masterMesurePoint,
						"Idx '"+pointnames[index_point]+"'",
						"Idx '"+pointnames[index_point]+"'"
						
						);
			} else {
			  mpIp = mm.beginMeasurePoint(
					"Idx '"+pointnames[index_point]+"'","Idx Index-Point '"+pointnames[index_point]+"'");
			}
			//tl.print("");
			//tl.print(">> Idx."+pointnames[index_point]+" STARTING Custom-Index-Point ...");			
			doRunCustomIndexpoint(index_point);
			// tl.print("   CUSTOM INDEX-POINT finished.");
			//tl.print("<< Idx."+pointnames[index_point]+" FINISHED Custom-Index-Point.");
			mpIp.finalizeAndPrint();
			//tl.print(" ");
			return false;
		} else if (dropIndex && hasPronto1()) {
			// Standard-Hardcoded-Index operation should take place
			/*if ( hardcoded[index_point]) {
				tl.print(">> Running index-actions #"+(index_point+1)+" "+pointnamesShort[index_point]+" ...");
				return true;
			} else { */
				tl.print("xx Skipping index-actions #"+(index_point+1)+" "+pointnamesShort[index_point]+" due to non existent szenario.");
				return false;
			//}
			
			
		} else {
			// bleibt wie es ist
			//tl.print("xx Skipping index-actions #"+(index_point+1)+" "+pointnamesShort[index_point]+" because no paramaters have been specified");
			tl.print("xx Skipping index-actions #"+(index_point+1));
			
			return false;
			
		}
	}
	private void doRunCustomIndexpoint (int index_point ) throws Exception {
		
		Imp imp = imps[index_point];

		for ( int i = 0; i<imp.imas.size(); i++ ) {
			if ( threadStopped ) throw new Exception("Background-Integration-Process stopped due to request");
			Ima ima = imp.imas.get(i);
			doRunIma (ima,i+1);
		}
		
	}
	
	private void doRunIma (Ima ima, int counter ) throws Exception {
		
		
		
		if ( ima.catcher) {
			try {
				doRunImaInner (ima,counter);
			} catch ( Exception ec ) {
				if ( ec.getMessage().equals("stop"))
					throw new Exception("Integration by breakpoint stopped.");
				else {
				tl.print("      [ Catched ] "+ec.getMessage());
				
				tl.print("                  "+ima.id);
				}
			}
		} else
			doRunImaInner (ima,counter);
		

		
	}	
	
	private void doRunImaInner (Ima ima, int counter  ) throws Exception {
		String nachlauf=Commons.leadBlanks("", (counter+"").length());	
		
		
		
		
		if ( ima.type == Ima.type_cmd) {
			// cmd
			tl.print("   "+counter+") "+(ima.cmd).replaceAll("\n", " "));
			Statement stmt = null;
			try { 
				stmt = getConn().createStatement();
				int back = stmt.executeUpdate(ima.cmd);	
				tl.print("   "+nachlauf+"  = "+back);
			} finally {
				if ( stmt != null) stmt.close();
			}
				
		} else if ( ima.type == Ima.type_sel) {
			//sel
			tl.print("   "+counter+") "+(ima.cmd).replaceAll("\n", " "));

			Statement stmt = null;
			ResultSet resultSet = null;
			
			try { 
				stmt = getConn().createStatement();
				resultSet = stmt.executeQuery(ima.cmd);
				
				
				ResultSetMetaData rsmd = resultSet.getMetaData();
				
				int columnsNumber = rsmd.getColumnCount();
				while (resultSet.next()) {
					String sammler="";
				    for (int i = 1; i <= columnsNumber; i++) {
				    	String columnValue = resultSet.getString(i);
				    	if ( columnValue != null)
				         columnValue = Commons.stringReplace(columnValue.trim(),"'","''");
				        sammler = sammler.concat(rsmd.getColumnName(i) +"='"+columnValue + "' " );
				    }
				    tl.print("   "+nachlauf+"  = "+sammler);
				
				}				
			} finally {
				if ( stmt != null) stmt.close();
				if ( resultSet != null) resultSet.close();
				
			}
				
		} else if ( ima.type == Ima.type_break) {
			// break
			int sizeBefore = ("   "+counter+") ").length();
			tl.print("   "+counter+") "+"WAITING for breakpoint-action: remove file: '"+breaker+"' or let it begin with 'stop' to cancel the entire integration.");
			CommonsVfs.writeToUTF8File("-stop remove this file", breaker, rh.getSitzung());
			boolean inHere = true;
			setState("Waiting for Breakpoint");
			
			
			while ( inHere ) {
				if ( threadStopped)
					throw new Exception("Background-Integration-Process stopped due to request");
				if ( CommonsVfs.existsFile(breaker, rh.getSitzung())) {
					// file existiert
					String content = CommonsVfs.readTextfile(breaker, rh.getSitzung());
					if ( content.startsWith("stop"))
						throw new Exception("stop");
				} else {
					// file existiert nicht mehr
					tl.print(Commons.leadBlanks(" ", sizeBefore)+"CONTINUED after breakpoint.");
					inHere = false;
				}
				if ( inHere ) 
					Thread.sleep(5000);
				
			}

		} else if ( ima.type == Ima.type_commit) {
			// break
			tl.print("   "+counter+") "+"commit");
			getConn().commit();
		}				
	}		

	protected int setupSs() throws JDOMException, IOException, Exception {
		tl.is ( "kick=["+kick+"]",0);
		Element xmlRoot = CommonsVfs.getElementByFilename(kick,rh.getSitzung());
		
		List<Element> smpList = xmlRoot.getChildren("sp");
		Iterator<Element> iImp = smpList.iterator();
		int smpPos = 0;
		while ( iImp.hasNext() ) {
			
			
			// System.out.println("impPos="+impPos);
			Element spElement = (Element)iImp.next();	
			boolean isOk = false;
			if ( spElement.getAttribute("sn") != null && 
					spElement.getAttributeValue("sn").trim().length() > 0 &&
					spElement.getAttribute("sql") != null &&
					spElement.getAttributeValue("sql").trim().length() > 0 ) {
				int thisSn = Commons.string2int(spElement.getAttributeValue("sn"));
				// tl.print("received sn="+thisSn);
				int thisIndex = thisSn - 1;
				if ( smps[thisIndex]==null ) {
					tl.print("Select #"+thisSn+" "+selectnames[thisIndex]+" wird uebernommen ");
					smps[thisIndex] = new String(spElement.getAttributeValue("sql"));
					smpPos++;
				} else {
					// schon belegt
					throw new Exception("Die Position #"+thisSn+" ist schon belegt.");
				}
			} else {
				throw new Exception("felder sn und sql muessen vorhanden sein");
			}
		}	
		return smpPos;
	}
	protected boolean setupIs() throws JDOMException, IOException, Exception {
		tl.is ( "index=["+index+"]",0);
		tl.is ( "breaker=["+breaker+"]",0);
		tl.is ( "cust_flag=["+cust_flag+"]",0);
		tl.is("----------------------------------------------------------------------------------------------",0);
		
		ValiResult allRes = new ValiResult();
		ValiResult[] pointDefinitions = new ValiResult[pointnames.length];
		allRes.setTaskLogger(tl);
		
		Element xmlRoot = CommonsVfs.getElementByFilename(index,rh.getSitzung());
		/*
		 * 		List<Element> list = e.getChildren("sel");
		Iterator<Element> it = list.iterator();
		while ( it.hasNext()) {
			Element ce = (Element)it.next();
		 */
		List<Element> impList = xmlRoot.getChildren("imp");
		Iterator<Element> iImp = impList.iterator();
		int impPos = 0;
		while ( iImp.hasNext() ) {
			
			impPos++;
			// System.out.println("impPos="+impPos);
			Element impElement = (Element)iImp.next();
			
			String impIdCore="pos="+impPos;
			if ( impElement.getAttribute("row")!=null && impElement.getAttributeValue("row").length()>0)
				impIdCore=impIdCore+ " row="+impElement.getAttributeValue("row");
			
				
			String impIdFull = "IMP ("+impIdCore+")";
			ValiResult impValiRes = new ValiResult(allRes,impIdFull);
			impValiRes.setTaskLogger(tl);			
			impValiRes.validateImp(impElement);			
			impValiRes.printErrorsAndWarnings();
			
			List<Element> imaList = impElement.getChildren("ima");
			Iterator<Element> iIma = imaList.iterator();
			int imaPos = 0;
			
			if (!( impElement.getAttribute("on")!= null &&
					 impElement.getAttributeValue("on").equals("1"))) {			
					tl.is("           IMP is OFF", 0);	
				
			}
						
			while ( iIma.hasNext() ) {
				
				imaPos++;
				Element imaElement = (Element)iIma.next();
				
				String imaIdCore="pos="+imaPos;
				if ( imaElement.getAttribute("row")!=null && imaElement.getAttributeValue("row").length()>0)
					imaIdCore=imaIdCore+" row="+imaElement.getAttributeValue("row");

				// imaIdCore = impIdCore+","+imaIdCore;
				String imaIdFull = "IMA("+imaIdCore+") IMP("+impIdCore+")";	
				String imaIdShort = "IMA("+imaIdCore+")";	
				
				String imaName="";
				if ( imaElement.getAttribute("name")!=null && 
					 imaElement.getAttributeValue("name").length()>0)
					imaName=imaElement.getAttributeValue("name")+" ";
				
				if ( imaElement.getAttribute("type")!=null && 
						 imaElement.getAttributeValue("type").length()>0){
					if ( imaElement.getAttributeValue("type").equals("cmd"))
						imaName = imaName +"= cmd = "+Commons.cutNice(imaElement.getAttributeValue("cmd"),100).replaceAll("\n", " ")+" ";
					else if ( imaElement.getAttributeValue("type").equals("sel"))
						imaName = imaName +"= query = "+Commons.cutNice(imaElement.getAttributeValue("cmd"),100).replaceAll("\n", " ")+" ";
					else if ( imaElement.getAttributeValue("type").equals("break"))
						imaName = imaName +"= breakpoint - ";
					else if ( imaElement.getAttributeValue("type").equals("commit"))
						imaName = imaName +"= commit - ";
					else
						imaName = imaName +"= Unknown ActionType '"+imaElement.getAttributeValue("type")+"' ";					
				}else
					imaName = imaName +"= No ActionType specfied at all";
				
				ValiResult imaValiRes = new ValiResult(impValiRes,imaIdFull,imaIdShort,imaName);
				imaValiRes.setTaskLogger(tl);			
				imaValiRes.validateIma(imaElement);			
				imaValiRes.printErrorsAndWarnings();	
				
				if ( imaElement.getAttribute("on")!= null &&
					 imaElement.getAttributeValue("on").equals("1")) {
					// die action kommt in die suppe
					imaValiRes.propagateErrors();
					// tl.is("will not be candidate", 1);
				} else
					tl.is("           IMA is OFF", 0);
				
			}		
			// System.out.println("FiNAL - 1");
			if ( impElement.getAttribute("on")!= null &&
				 impElement.getAttributeValue("on").equals("1")) {
				// die action kommt womöglich in die suppe
				//
				// System.out.println("FiNAL - 2");
				int pointindex =-1;
				if ( impElement.getAttribute("point")!= null &&
					 Commons.isInt(impElement.getAttributeValue("point"))) {
					int myPointNumer = Commons.string2int(impElement.getAttributeValue("point"));
					//if ( myPointNumer >=0 && myPointNumer <=6)
						pointindex = myPointNumer-1;
				}
				// System.out.println("FiNAL - 3");
				
				if ( pointindex == -1 );
					//impValiRes.appendError("@point is incorrect ["+impElement.getAttributeValue("point")+"]");
				else if ( pointDefinitions[pointindex] == null ) {
					// point noch nicht besetzt
					pointDefinitions[pointindex] = impValiRes;
				} else {
					// schon besetzt
					impValiRes.appendError("point "+pointnames[pointindex]+" is already occupied");
				}
				// System.out.println("FiNAL - 4");
				
				impValiRes.propagateErrors();
					// tl.is("will not be candidate", 1);
			} else	{	
				tl.is("           ----------", 0);
				tl.is("           IMP is OFF", 0);	
				
		}
			// System.out.println("FiNAL - 9");
			tl.is("----------------------------------------------------------------------------------------------",0);
		}
		
		tl.is("================================================================================================",0);
		if (allRes.hasErrors()) {
			tl.is("Index-Szenario is NOT VALID",0);
			allRes.printErrors();
		} else {
			tl.is("VALID Index-Szenario",0);
			if (allRes.hasWarnings()) {
				allRes.printWarnings();
			}			
		}
		
		tl.is("================================================================================================",0);
		
		if (allRes.hasErrors()) {
			return false;
		} else  {
			tl.is("Builing Internal Representation of Index-Scenraio",0);
			
			// schleife um die	Imp[] imps = new Imp[6];
			// ValiResult[] pointDefinitions = new ValiResult[6];
			ImParasFilter imParasFilter = new ImParasFilter(
					breaker,
					cust_flag,
					runstats,
					dbms,
					pronto,
					tl
					);
			
			int i = 0;
			while ( i < pointnames.length ) {
				
				if ( pointDefinitions[i] != null ) {
					// für den punkt gibt es eine definition
					imps[i] = pointDefinitions[i].buildImp(imParasFilter,i);
					
				}
				i++;
				
			}
			tl.is(" ",0);
			if ( doExplain) {
			
			tl.is("============== THE SUMMARY of Index-Realtime-Szenario ==================",0);
			
			i = 0;
			tl.is(" ",0);
			while ( i < pointnames.length ) {
				
				if ( imps[i] != null ) {
					// für den punkt gibt es eine definition
					
					imps[i].isSum(tl);
					tl.isSum(" ",0);
				} else {
					String doing;
					/* if ( hardcoded[i]) doing="Built-in actions will be performed.";
					else 
						*/ doing="No actions will be performed.";
					tl.isSum(Commons.trailBlanks("IMP", 22)+"x  "+IntParas.pointnames[i]+
							" << --- "+doing
							+" ",0);
					tl.isSum(" ",0); // Not declared in index-szenario. 
				}
				i++;
				
			}	
			
			// ab hier tests
			/*
			tl.is("============== Test nummernwechsel begin ==================",0);
			
			i = 0;
			tl.printBlank("");
			while ( i < pointnames.length ) {				
				String pri = 
						Commons.leadSymbols(""+i, 2, "0") + "/" +
						Commons.leadSymbols(""+(i+1), 2, "0") +" ";							
				pri = pri + "      "+pointnames[i];				
				tl.printBlank(pri);
				i++;
				
			}		
			tl.printBlank("");
			tl.is("============== Test nummernwechsel ende ==================",0);
			
			*/
			// bis hier tests
			
			tl.is("============== END of SUMMARY Index-Realtime-Scenario =====================================",0);
			tl.is("",0);
			
		}
			return true;
		}
		
			
		
		
		
	}
	/*
	private void doRunAndExplainIndexCore (String level, String statementName, String aktion, String cmd ) throws Exception {
	    Statement stmt = getConn().createStatement();
	    int updateValue = stmt.executeUpdate(cmd);
	}*/
	
	

	int dbRunnerCounter = 0;
	
	protected void doDbRunnerIntro (String level, String statementName, String aktion, String cmd,boolean withExplainPlease) {
		psPrinter.print("<dbRunner ");
		
		dbRunnerCounter++;

		psPrinter.print(" pos=\""+
				Commons.int2String(dbRunnerCounter)+
				"\" ");			
		
		psPrinter.print(" level=\""+
				Commons.escapeXml(level)+
				"\" ");		
		psPrinter.print(" statementName=\""+
				Commons.escapeXml(statementName)+
				"\" ");				
		psPrinter.print(" aktion=\""+
				Commons.escapeXml(aktion)+
				"\" ");			
		
		psPrinter.print(" ts=\""+
				(new Timestamp( System.currentTimeMillis()).toString() )+
				"\" ");
		if ( withExplainPlease ) {
			psPrinter.print(" addedExplain=\""+Commons.boolToString(withExplainPlease)+"\"");
		};			
		psPrinter.print(" statement=\""+
				Commons.escapeXml(cmd)+
				"\" ");				
		psPrinter.println(" >");		
		
	}
	protected void doDbRunnerExtro () {
		psPrinter.println("</dbRunner>");
		
	}
	protected ResultSet doRunAndExplainQuery (
			String level, 
			String statementName, 
			String aktion, 
			String queText,
			Statement stmt,
			int sx_index,
			Mp mp,
			int intro_ip_index,
			boolean isPartOfMigration ) throws Exception {
		if ( threadStopped )
			throw new Exception("Background-Integration-Process stopped due to request");
		
		ResultSet returner = null;
		
		if ( intro_ip_index > -1) {
			if (shouldRunClassicIndexpoint(intro_ip_index,mp));
		}
		
		long startingMs = System.currentTimeMillis();
		
		int buffer_sx_index = -1;
		if ( isPartOfMigration )
			buffer_sx_index = sx_index;
		
		if ( sx_index == -1  ) {
			// im ersetzter kein select gefunden			
			returner = doRunAndExplainQueryXXX (
					 level, 
					 statementName, 
					 aktion, 
					 queText,
					 stmt,
					 buffer_sx_index);			
		} else if ( smps[sx_index] == null ) {
			// im ersetzter kein select gefunden
			tl.print("do "+selectnames[sx_index]+" #"+(sx_index+1));
			
			returner = doRunAndExplainQueryXXX (
					 level, 
					 statementName, 
					 aktion, 
					 queText,
					 stmt,
					 buffer_sx_index);			
		} else {
			mp.addMeasure("your_select", sx_index+1);
			tl.print("GO #"+(sx_index+1)+" "+selectnames[sx_index]+" UPDATED Major-Selection");
			tl.print("!!!  !!!");
			tl.print("!!!  !!! Running selection #"+(sx_index+1)+" "+" from OVERWRiTTEN select: "+selectnames[sx_index]+" = "+smps[sx_index]);
			tl.print("!!!  !!!");
			try {
				returner = doRunAndExplainQueryXXX (
						 level, 
						 selectnames[sx_index]+" = "+statementName, 
						 aktion, 
						 smps[sx_index],
						 stmt,
						 buffer_sx_index);
			} catch (Exception e) {
				tl.logExceptionBy(e, "Running the individual select");
				mp.addMeasure("your_select_failed", 1);
				tl.print("your query failed:");
				tl.print(smps[sx_index]);
			throw ( e );						
				
				
			}				
		}
		long endingMs = System.currentTimeMillis();
		int timeMs = (int)(endingMs - startingMs);
		mp.addMeasure("prep_time_ms", timeMs);
		return returner;
	}

	HashMap<Integer,PreparedStatement> psMap = new HashMap<Integer,PreparedStatement>();
	protected ResultSet doRunAndExplainQueryXXX (
			String level, 
			String statementName, 
			String aktion, 
			String queText,
			Statement stmt,
			int buffer_sx_index) throws Exception {
	
			ResultSet rsToReturn = null;
			if ( doExplain ) {				
				doDbRunnerIntro( 
						level,
						statementName,
						aktion,
						queText, 
						true);		
				
					rsToReturn=doRunAndExplainQueryCore ( 
							level,  statementName,  aktion, queText,stmt,buffer_sx_index );

				doDbRunnerExtro();				
			} else {
				
				if ( buffer_sx_index > -1 && pso) {
					if ( psMap.containsKey(buffer_sx_index)) {
						// gebuffertes ps und vorhanden
						PreparedStatement prepared = psMap.get(buffer_sx_index);
						rsToReturn = prepared.executeQuery();
					} else {
						// grundsätzlich gebuffert aber noch nicht vorhanden
						PreparedStatement prepared = conn.prepareStatement(queText);
						psMap.put(buffer_sx_index, prepared);
						rsToReturn = prepared.executeQuery();					
					}					
				} else {
					// nicht gebuffert
					rsToReturn = stmt.executeQuery(queText);
				}
				
				
			}
			return rsToReturn;

	}

	private ResultSet  doRunAndExplainQueryCore (
			String level, String statementName, 
			String aktion, String cmd,Statement stmtReal,
			int buffer_sx_index ) throws Exception {
	ResultSet rsToReturn = null;
	// prüfen ob nur ausführen:
	// - wenn buffer_sx_index > -1 und 
	//   in map vorhanden
		
	if ( pso && buffer_sx_index > -1 && psMap.containsKey(buffer_sx_index)) {
		// gebuffertes ps und vorhanden
		PreparedStatement prepared = psMap.get(buffer_sx_index);
		rsToReturn = prepared.executeQuery();
	} else {
		// nicht gebuffert oder
		// gebuffert und noch nicht vorhanden
		Statement stmt = getConn().createStatement();
	    ResultSet rs =  stmt.executeQuery(
		"EXPLAIN "+cmd );
		   		    
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		//psPrinter.println("<rows>");
		// hier die schleife um die meta-daten
		int countRecords = 0;
		psPrinter.println("<rows>");
	    while (rs.next()) {
	    	countRecords++;
	    	psPrinter.print("<row ");
	    	Unique unique = new Unique("xml_attribute");
	    	for (int i = 1; i <= columnCount; i++ ) {
	    		// if ( (rs.getString(i)).trim().length() > 0 )
	    		String wert = rs.getString(i);
	    		// System.out.println("wert="+wert);
	    		if ( wert!=null &&  wert.trim().length() > 0 )
	    		psPrinter.print(unique.getValue(rsmd.getColumnName(i))+"=\""+Commons.escapeXml(rs.getString(i))+"\" "); 
	    	}	    	
	    	psPrinter.println("/>");	    	
	    }
	    psPrinter.println("</rows>");
	    //psPrinter.println("</rows>");
	    psPrinter.println("<meta>"); 
		psPrinter.println("<coltypes size=\""+columnCount+"\" >");
		Unique unique = new Unique("xml_attribute");
		for (int i = 1; i <= columnCount; i++ ) {
			
			psPrinter.print("<coltype ");
			psPrinter.print(" an=\""+ ""+unique.getValue(rsmd.getColumnName(i)) +"\" ");
			psPrinter.print(" idx=\""+ ""+i +"\" ");			
			psPrinter.print(" label=\""+ Commons.escapeXml(rsmd.getColumnLabel(i))+"\" ");
			psPrinter.print(" name=\""+ Commons.escapeXml(rsmd.getColumnName(i))+"\" ");
			psPrinter.print(" type_name=\""+ Commons.escapeXml(rsmd.getColumnTypeName(i))+"\" ");
			psPrinter.print(" type=\""+  Commons.escapeXml(Commons.int2String(rsmd.getColumnType(i)))+"\" ");
			psPrinter.print(" nullable=\""+ Commons.escapeXml(""+rsmd.isNullable(i))+"\" ");
				
			psPrinter.println(" />");
		}
		psPrinter.println("</coltypes>");	
		if ( buffer_sx_index > -1 ) {
			// gebuffert als neues ps anlegen
			PreparedStatement prepared = conn.prepareStatement(cmd);
			psMap.put(buffer_sx_index, prepared);
			rsToReturn = prepared.executeQuery();
			
		} else {
			// nicht gebuffert, standard statement
			rsToReturn = stmtReal.executeQuery(cmd);
			
		}
		 
		psPrinter.print("<end  ");
		psPrinter.print("ts=\""+Commons.escapeXml((new Timestamp( System.currentTimeMillis()).toString() ))+"\" ");
		psPrinter.print("rowCount=\""+countRecords+"\" ");
		psPrinter.println(" />");
		psPrinter.println("</meta>");		
	}
	
 	 
    
					
    return rsToReturn;

	
}	
	
	protected void printWelcomeInfos() throws FileSystemException, StorageLimitException, UnsupportedEncodingException {
		this.tl.print("maxObjectsContract="+maxObjectsContract);
		// this.tl.print("maxObjectsReal="+maxObjectsReal);
		this.tl.print("maxObjectsWarning="+maxObjectsWarning);
		
			this.tl.print("pronto="+pronto);
			this.tl.print("dropIndex="+dropIndex);
			this.tl.print("temp="+tempDir);
			
			this.tl.print("upp_mig="+upp_mig);
			this.tl.print("upp_heal="+upp_heal);
			this.tl.print("upp_top="+upp_top);
			
			this.tl.print("kopfgrouped="+kopfgrouped);
			// this.tl.print("mvrefresh="+mvrefresh);
			this.tl.print("pso="+pso);
		
		this.tl.print("isMySqlorMaria="+!isPostgres);	
		this.tl.print("commitSize="+getCommitSize());
		
		if ( intensiveIntegrationRounds != -1 )
			this.tl.print("intensiveIntegrationRounds="+intensiveIntegrationRounds);
			
		
		if ( doExplain ) {
			 osr = CommonsVfs.getOutputStreamByFilenameP1(explainFile, false,rh.getSitzung());
			 psOutputStream = osr.getOutputStream();
						
			 psPrinter= new PrintStream(psOutputStream,true,"UTF-8");	
			 
				psPrinter.println("<?xml version=\"1.0\" encoding=\"Utf-8\"?>");
				psPrinter.print("<dbRunners ");
				psPrinter.println(" dbms=\""+
						dbms+
						"\" ");	
				/*
				psPrinter.println(" url=\""+
						Commons.escapeXml(url)+
						"\" ");	*/
				psPrinter.println(" ts=\""+
								(new Timestamp( System.currentTimeMillis()).toString() )+
								"\" ");
	
				psPrinter.println(" recreate_index=\""+
						Commons.boolToString(dropIndex)+
						"\" ");	
				
				
				psPrinter.println(" explain=\""+
	
						Commons.boolToString(doExplain)+
						"\" ");	
				if ( doExplain ) {
					psPrinter.println(" explainFile=\""+							
					Commons.escapeXml(explainFile)+
						"\" ");		
					psPrinter.println(" runstats=\""+							
					Commons.escapeXml(Commons.boolToString(runstats))+
						"\" ");						
				}
				psPrinter.println(" >");				
				

				
		}
	}	
	
	protected void closeExplainer() throws FileSystemException, StorageLimitException {
		if ( doExplain ) {
			psPrinter.print("<end written=\""+Commons.escapeXml(explainFile)+"\" ");
			psPrinter.print("ts=\""+Commons.escapeXml((new Timestamp( System.currentTimeMillis()).toString() ))+"\" ");
			psPrinter.println(" />");
			psPrinter.println("</dbRunners>");
			psPrinter.close();
			 try {
				psOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				tl.print("failed to close outputstream:"+e.getMessage());
				// e.printStackTrace();
			}
			 
			 CommonsVfs.finishOutputStreamP2(explainFile, rh.getSitzung(),0);
		}
		
	}
	private Connection conn = null;
	private int commitSize = 1000;
	private int reportSize = 20000;


	
	// achtung alles unter arcadia-update
	
	//private int maxMig = 62070777;/*[mig]*/	
	
		
	/*[objects-contract]*/ protected int maxObjectsContract = 70;
	/*[objects-real]*/ protected int maxObjectsReal = 100;
	/*[objects-warning]*/ protected int maxObjectsWarning = 50;
	
	//protected int maxObjects = 23790777;	
	//protected int maxObjectsContract = 23790777;
	
	
	// private int maxBez = 19580777;/*[bez]*/	
	// private int maxAttr = 111610777;/*[attr]*/	
	// private int maxWert = 111610777;/*[wert]*/	
	
	// private int maxMehr = 720777;/*[mehr]*/	
	// private int maxTop = 300000777;/*[top]*/	
	
	
	
	private int loadCounter = 0;
	protected int getLoadCount() {
		return loadCounter;
	}
	

	
	protected boolean getDropIndex() {
		return dropIndex;
	}	

	
	
	private boolean has_pronto1;
	protected boolean hasPronto1() {
		return has_pronto1;
	}
	private boolean has_pronto2;
	protected boolean hasPronto2() {
		return has_pronto2;
	}	
	protected boolean hasPronto2And(boolean auch_testen) {
		return ( has_pronto2 && auch_testen) ;
	}	
	private void setPronto ( int new_pronto ) {
		pronto = new_pronto;
		if ( pronto >= 1 ) 
			has_pronto1 = true;
		else
			has_pronto1 = false;
		
		if ( pronto >= 2 ) 
			has_pronto2 = true;
		else
			has_pronto2 = false;		
			
	}
		
	

	/*
	protected void loadedObjects( int counter ) throws Exception {
		if ( counter > maxObjects) throw new Exception("Cancelled due to volume-limit. Max Objects: "+maxObjects);		
		loadCounter=loadCounter+counter;
	}	
	
	protected void loadedMigObj( int counter ) throws Exception {

		if ( counter > maxMig) throw new Exception("Cancelled due to volume-limit. Max Mig Obj: "+maxMig);		
	}		

	protected void loadedBez( int counter ) throws Exception {
		if ( counter > maxBez) throw new Exception("Cancelled due to volume-limit. Max Bez: "+maxBez);	
		loadCounter=loadCounter+counter;
	}		
	
	protected void loadedAttr( int counter ) throws Exception {
		if ( counter > maxAttr) throw new Exception("Cancelled due to volume-limit. Max Attr: "+maxAttr);	
		loadCounter=loadCounter+counter;
	}		
	
	protected void loadedWert( int counter ) throws Exception {
		if ( counter > maxWert) throw new Exception("Cancelled due to volume-limit. Max Wert: "+maxWert);		
		loadCounter=loadCounter+counter;
	}	
	

	protected void loadedTop( int counter ) throws Exception {
		if ( counter > maxTop) throw new Exception("Cancelled due to volume-limit. Max Top: "+maxTop);		
	}	
		
	protected void loadedMehr( int counter ) throws Exception {
		if ( counter > maxMehr) throw new Exception("Cancelled due to volume-limit. Max Mehr: "+maxMehr);	
		loadCounter=loadCounter+counter;
	}		
	*/
			
	
	protected void logPrint(String msg ) throws Exception {
		//lc.info(msg, itask);

		tl.print("   "+msg.trim());
		if ( threadStopped )
			throw new Exception("Background-Integration-Process stopped due to request");
		//System.out.println(msg);
	}
	
	protected void logUpdater(String msg ) {
		//lc.info(msg, itask);
		tl.print("   "+msg.trim());
		//System.out.println(msg);
	}	
	
	protected void logUpdaterIntro(Updater updater, int expected ) {
		logUpdater("   Updater.run {{ "+ updater.name +" }} running index-friendly update, expecting [ "+tausender(expected)+" ] records.");
		
	}	
	protected void logUpdaterCount(Updater updater, int statisticCount ) {
		logUpdater("   Update:"+IntParas.tausenderLead(statisticCount) );
		
	}		
	static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
	static String tausender(int in ) {
		return numberFormat.format(in);
	}
	
	static String tausenderLead(int in ) {
		return Commons.leadBlanks(numberFormat.format(in)+"",13);
		 
	}
	
	protected void logUpdaterExtro(Updater updater, int statisticCount, int effected ) {
		logUpdater("   Updater did [ "+ tausender(statisticCount) +" ] index-friendly {{ "+
	updater.name+" }} updates and affected [ "+tausender(effected)+" ] records." );	
		// logUpdater(" ");
		
	}		
	

	

	
	protected void logPrintException(Exception e ) {
			tl.printException(e);
	}	
	
	protected void printExceptionBy(Exception e, String guiltyIndentifier ) {
		tl.logExceptionBy(e,guiltyIndentifier);
}		
	

	
	public int getCommitSize() {
		return commitSize;
	}
	protected int getUpdateCommitSize() {
		return commitSize;
	}	
	
	protected int getReportSize() {
		return reportSize;
	}
	
	public int getCommitSizeAdapted2() {
		int adapted = commitSize/2;
		try {
			logPrint("  Using adapted commit-size of "+adapted);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return adapted;
	}	
	//protected void setCommitSize(int commitSize) {
	//	this.commitSize = commitSize;
	//}


	
	protected Connection getConn() {
		return conn;
	}
	protected CoopDbci getDbci() {
		return dbci;
	}	

	boolean runstats=false;
	

	private int intensiveIntegrationRounds = -1;
	protected int getIntensiveIntegrationRounds() {
		return intensiveIntegrationRounds;
	}
	 String mv_remember_print = null;
	protected IntParas(
			TaskLogger _taskLogger, 
			int _pronto, 
			boolean _dropIndex, 
			boolean _isPostgres,
			String _dbms, 
			String _explainFile, 
			IRequestHandler _rh, 
			//String _url, 
			boolean _runstats,
			Connection _conn,
			int _commitsize,
			int _reportsize,
			String _index,
			String _break,
			String _cust_flag,
			Element e,
			String _kick,
			String _mv,
			CoopDbci _dbci) throws Exception {
		
		//url = _url;
		this.rh = _rh;

		this.tl =_taskLogger;		
		this.dbci = _dbci;
		
		dss = Commons.hasFlag(e, "dss");
		solo = Commons.hasFlag(e, "solo");
		
		 upp_mig= Commons.hasFlag(e, "upp_mig");
		 upp_heal= Commons.hasFlag(e, "upp_heal");
		 upp_top= Commons.hasFlag(e, "upp_top");	
		 
		 if ( e.getAttribute("mv_style")!= null && 
			e.getAttributeValue("mv_style").trim().length()>0) {
			 mv_style = Commons.string2int(e.getAttributeValue("mv_style").trim());
		}
		/*
		if ( solo ) {
			if ( e.getAttribute("solo_start")!= null && 
				 e.getAttributeValue("solo_start").trim().length()>0) {
				solo_starting_round = Commons.string2int(e.getAttributeValue("solo_start").trim());
			} else
				solo_starting_round = 1;
		} */
		numform = Commons.hasFlag(e, "numform");
		smart = Commons.hasFlag(e, "smart");
		smart_in_opti = Commons.hasFlag(e, "smart_in_opti");
		simple = Commons.hasFlag(e, "simple");
		doubleoccupation = Commons.hasFlag(e, "doubleoccupation");
		setAdapt(Commons.hasFlag(e, "adapt"));	
		
		isdetails = Commons.hasFlag(e, "isdetails");
		// newopt = Commons.hasFlag(e, "newopt");
		kopfgrouped = Commons.hasFlag(e, "kopfgrouped");
		// mvrefresh = Commons.hasFlag(e, "mvrefresh");
		//newtop = Commons.hasFlag(e, "newtop");
		
		pso = Commons.hasFlag(e, "pso");
		
		this.tempDir = rh.getSitzung().getLongLivingTempDir();
		
		
		/*
		this.tempDir = e.getAttributeValue("log");
		if ( tempDir == null )
			throw new Exception("integration needs parameter '@log' ");
		
		CommonsVfs.resolveFolderDelete(tempDir, rh.getSitzung());
		CommonsVfs.mkdirCascade(tempDir, rh.getSitzung());
		*/
		



		
		if ( _kick != null && _kick.trim().length()> 0 )
			this.kick= _kick;
		else
			this.kick= null;		
		
		if ( _mv != null && _mv.trim().length()> 0 ) {
			mv_remember_print = "mv_original="+_mv;
			this.mv = tempDir+"mv.xml";
			if ( this.mv.startsWith("var/www") ) 
				this.mv = "/"+this.mv ;
				
			CommonsVfs.copy_filename_blind(
					_mv, 
					this.mv );
		} else
			this.mv= null;			
		
		
		if ( _index != null && _index.trim().length()> 0 )
			this.index= _index;
		else
			this.index= null;


		if ( _break != null && _break.trim().length()> 0 )
			this.breaker= _break;
		else
			this.breaker= null;
		
		this.cust_flag = _cust_flag;
		commitSize = _commitsize;
		reportSize = _reportsize;
		

		
		
		isPostgres = _isPostgres;
		dbms = _dbms;
		conn = _conn;
		
		// product-conf berechtigungen
		boolean explainIsAllowed = true; /*explain-is-allowed*/
		if ( explainIsAllowed) {
			if ( _explainFile!=null && _explainFile.trim().length() > 0 ) {
				doExplain = true;
				explainFile = _explainFile;			
			} else {
				doExplain = false;
				explainFile=null;
			}
		} else {
			doExplain = false;
			explainFile=null;			
		}
				
		boolean prontoIsAllowed = true; /*pronto-is-allowed*/
		if ( prontoIsAllowed) {
			runstats = _runstats;
			dropIndex = _dropIndex;
			setPronto(_pronto);		
			intensiveIntegrationRounds = 1; // intensive default
			if ( has_pronto2 ) {
				if ( e.getAttribute("intensive_rounds") !=null && 
					(e.getAttributeValue("intensive_rounds").trim()).length() > 0 ) {
					String vx = e.getAttributeValue("intensive_rounds").trim();
					if ( Commons.isInt(vx) )
						intensiveIntegrationRounds = Commons.string2int(vx);
					
				}
			}
		} else {
			dropIndex = false;
			runstats = false;			
			setPronto(0);			
		}
		
		mm = new MeasureSystem(tl,this);
		
		
		
		
		
		

	}


	/*
	protected void RunImaCmd(String command, String level,String titel,String aktion ) throws SQLException  {
		try {
			if ( doExplain) {
				//doRunAndExplainIndex (level, titel, aktion, createCommand); 
			} else {
				//tl.print(createCommand);
				//Statement stmt = getConn().createStatement();

				//	int returner = stmt.executeUpdate(	createCommand);			
				
			}
		

		} catch (Exception e) {
			tl.print("command ["+titel+"] could not be done: "); //+createCommand
			tl.print(e.getLocalizedMessage());
			throw e;
		}		
	}		
	*/
/*	
protected void DropIndexStart1() throws SQLException  {
			
	if ( !isPostgres ) {

		DropIndexSqlCatched("DROP INDEX idx_attr_attr_typ ON attr ;");
		DropIndexSqlCatched("DROP INDEX idx_wert_attr_id ON wert ;");
		DropIndexSqlCatched("DROP INDEX idx_wert_wann ON wert ;");
		DropIndexSqlCatched("DROP INDEX idx_wert_heal_double_werte ON wert ;");
		DropIndexSqlCatched("DROP INDEX idx_wert_top ON wert ;");
		DropIndexSqlCatched("DROP INDEX idx_attr_top_aktuell ON attr ;");
		DropIndexSqlCatched("DROP INDEX idx_wert_pronto1 ON wert;");
		DropIndexSqlCatched("DROP INDEX idx_mig_obj_collect ON mig_obj;");
		DropIndexSqlCatched("DROP INDEX idx_mig_obj_slaves ON mig_obj;");
		
		DropIndexSqlCatched("DROP INDEX idx_bez_obj_a ON bez;");
		DropIndexSqlCatched("DROP INDEX idx_bez_obj_b ON bez;");

		getConn().commit();
		
		if ( runstats ) {
			RunIndexSql("ANALYZE TABLE i_obj, i_attr, i_bez, i_box, i_wert, i_mehr", 
					"0.1",
					"runstats i_obj, i_attr, i_wert, i_bez, i_box, i_wert",
					"analyze import" );
			 getConn().commit();
			 postAnalyze();								 	
		}
							
	}
	tl.print("Did drop index.");
			
				
}
*/
	/*
	protected void postAnalyze()  {
		 try {
			if ( dbms.equals("mysql")) {
				 
					RunIndexSql("FLUSH OPTIMIZER_COSTS", 
							" ",
							"flush costs",
							"" );
					 getConn().commit();							 
				 
			 } else if ( dbms.equals("maria")) {
				 	/// braucht man berechtigung dazu
					//RunSql("FLUSH TABLE_STATISTICS", 
					//		false,
					//		" ",
					//		"flush costs",
					//		"" );
					// getConn().commit();	
								 
				 
			 }
		} catch (Exception e) {
			tl.print("Could not run cost-flushing");
		} 		
	}
	*/
	/*	
	protected void BuildIndexFindMigrations2() throws SQLException {

		
	
			if ( isPostgres ) {
				// postgres
			} else {

				RunIndexSqlCatched("CREATE INDEX idx_wert_attr_id ON wert (attr_id); ");

				RunIndexSqlCatched("CREATE INDEX idx_attr_top_aktuell ON attr (obj_id, top_aktuell); ");	
				
				RunIndexSqlCatched("CREATE INDEX idx_wert_pronto1 ON wert (attr_usg_id, inhalt2(60), obj_id);");
				
				RunIndexSqlCatched("CREATE INDEX idx_bez_obj_a ON bez (obj_id_a);");
				RunIndexSqlCatched("CREATE INDEX idx_bez_obj_b ON bez (obj_id_b);");

				
				getConn().commit();

				
				if ( runstats ) {
					RunIndexSql("ANALYZE TABLE obj, attr, wert,bez,mehr", 
							"2.1",
							"runstats obj, attr, wert, bez, mehr",
							"analyze central" );
					 getConn().commit();
					 postAnalyze();
							 	
				}					

			}	
			tl.print("Finished to build index #2.");						
	} */
	/*
	protected void BuildIndexHeal4() throws SQLException {
	
			if ( isPostgres ) {
			} else {

				RunIndexSqlCatched("CREATE INDEX idx_wert_heal_double_werte ON wert (attr_id, inhalt(60), von, bis); ");				
				getConn().commit();
			}	
			tl.print("Finished to build index #4/BeforeHeal.");						
	} */	
	/*
	protected void BuildIndexAfterFirstMigrationSearch3() throws SQLException {
	
		
			if ( isPostgres ) {

			} else {

				RunIndexSqlCatched("CREATE INDEX idx_mig_obj_collect ON mig_obj (master_obj_id, slave_obj_id);");
				RunIndexSqlCatched("CREATE INDEX idx_mig_obj_slaves ON mig_obj (slave_obj_id);");
				
				getConn().commit();

				
				if ( runstats ) {
					RunIndexSql("ANALYZE TABLE mig_obj", 
							"9.1",
							"runstats mig_obj",
							"analyze central" );
					 getConn().commit();
					 postAnalyze();
							 	
				}					

			}	
			tl.print("Finished to build index #3");
			tl.print("");									
	}	*/
	/*
	protected void BuildIndexTop5() throws SQLException {
				
			if ( isPostgres ) {
			} else {
				DropIndexSqlCatched("DROP INDEX idx_wert_heal_double_werte ON wert ;");
				RunIndexSqlCatched("CREATE INDEX idx_wert_top ON wert (attr_id, wann desc); ");				
				getConn().commit();				
				if ( runstats ) {
					RunIndexSql("ANALYZE TABLE wert", 
							"3.1",
							"runstats wert",
							"analyse central/wert" );
					 getConn().commit();
					 
					 postAnalyze();								 	
				}
			}	
			tl.print("Finished to build index #5.");
			tl.print("");										
	}	*/
	/*
	protected void BuildIndexEnd8() throws SQLException {
			
			if ( isPostgres ) {

			} else {
				RunIndexSqlCatched("CREATE INDEX idx_attr_attr_typ ON attr (attr_typ); ");					
				getConn().commit();
				
				if ( runstats ) {
					if ( runstats ) {
						RunIndexSql("ANALYZE TABLE wert, attr", 
								"idx3",
								"runstats wert",
								"analyse central/wert" );
						 getConn().commit();
						 
						 postAnalyze();								 	
					}
				}					

			}	
			tl.print("Finished to build index #6.");						
	}	
	*/
	/*
	protected void DropIndexSqlCatched(String dropCommand)  {
		try {
			// tl.print("  DROP INDEX: "+dropCommand);
			RunIndexSql(dropCommand,"x.2","DropIndex","index" );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			tl.print("        [ PROBLEM ] "+e.getMessage());
		}

	}	
	protected void RunIndexSqlCatched(String createCommand)  {
		try {

			RunIndexSql(createCommand,"x.1","Build Index","index" );
		} catch (Exception e) {

			tl.print("   PROBLEM: "+e.getMessage());
		}
	
	}	
	
	
	protected void RunIndexSql(String createCommand, String level,String titel,String aktion ) throws SQLException  {

		tl.print("   "+createCommand);
		Statement stmt = getConn().createStatement();
			stmt.executeUpdate(
				createCommand);		

	}		
	*/
	
	protected void closeAllPreparedStatements() {
		Set<Integer> keySet = psMap.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;

		while ( i.hasNext() ) {
			aktKey = i.next();
			PreparedStatement ps = psMap.get(aktKey);
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	ArrayList<String> msgs = new ArrayList<String>();
	protected void addFinalMessage(String finalMessage ) {
		msgs.add(finalMessage);
	}
	protected void newMessageWillCome( ) {
		if ( msgs.size() > 0 )
			msgs.add(" ");
	}	
	protected void printFinalMessage(TaskLogger taskLogger ) {
		if ( msgs.size() > 0 ) {
			taskLogger.print(" " );
			taskLogger.print("   **************************************************************************************************" );
			for ( int i = 0; i<msgs.size(); i++ ) {
				String msg = msgs.get(i);
				taskLogger.print("   **   " + msg);
			}
			taskLogger.print("   **************************************************************************************************" );
			taskLogger.print(" " );
			
		}
	}	
	
	private String state = null;
	public synchronized String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		//System.out.println("Integration.state set to: "+this.state);
	}
}
