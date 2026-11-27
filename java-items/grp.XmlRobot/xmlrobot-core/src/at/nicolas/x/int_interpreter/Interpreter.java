package at.nicolas.x.int_interpreter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.log4j.Level;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.XMLOutputter;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.WebVfs.ReqResDummy;
import at.nicolas.x.WebVfs.TkTicketManager;
import at.nicolas.x.WebVfs.XrRqRp;
import at.nicolas.x.WebVfs.XrRqRpEx;
import at.nicolas.x.WebVfs.XrRqRpTi;
import at.nicolas.x.WebVfs.XrRqRpTk;
import at.nicolas.x.int_call.AppJobexecution;
import at.nicolas.x.int_call.AppJobexecutionIncludeFork;
import at.nicolas.x.int_call.AppJobexecutionUsage;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_coop.CoopSpirit;
import at.nicolas.x.int_coop.IInterLoop;
import at.nicolas.x.int_coop.InterLoopRegex;
import at.nicolas.x.int_coop.InterLoopSelect;
import at.nicolas.x.int_coop.InterLoopWhile;
import at.nicolas.x.int_coop.InterLoopXml;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_db.DbPro;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LogLogger;
import at.nicolas.x.int_setup.MountConfig;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.CustomException;
import at.nicolas.x.int_specs.HaltException;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.LogConnectorLogic;
import at.nicolas.x.int_specs.StopException;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.pub.UserInfo;
import at.nicolas.x.pub.Vcar;


public class Interpreter  implements IFunEvalHandler, IRequestHandler, ITiHandler, Runnable {
	private static boolean devMode = false;	
private static HashMap<String, Integer> cmdm = null;
private TempFileManager tempFileManger;
private static boolean printTkThings = false;
private static long interpreterId = 0;
private long myInterpreterId = -1;
private static boolean reportCoops = false;

private boolean threadStopped = false;

private ConcurrentHashMap<String, ICoop> kMap = null;


public synchronized void shouldStopThread() {
	System.out.println("Interpreter received request to threadStop");
	
	threadStopped = true;
}
	
final  String pp= "Gast";
private String taskExplainer ( Element e ) {
	if ( Commons.hasFlag(e, "secret")) {
		if ( e.getAttribute("secret") == null ) {
			// auch kein pseudonym
			return "Secret-Task [?]";
				
		} else {
			// mit pseudonym
			return "Secret-Task ["+e.getAttributeValue("secret")+"]";					
		}			
		
	} else
		return Commons.toText(e);
}

private String taskExplainerCompact ( Element e ) {
	if ( Commons.hasFlag(e, "secret")) {
		if ( e.getAttribute("secret") == null ) {
			// auch kein pseudonym
			return "Secret-Task [?]";
				
		} else {
			// mit pseudonym
			return "Secret-Task ["+e.getAttributeValue("secret")+"]";					
		}			
		
	} else
		return Commons.toTextCompact(e);
}

private Exception performLoginInternal() throws Exception {
	

Exception eReturn = null;
if ( login_user == null || login_pwd == null ) {
	eReturn = new Exception("We need a user and a password");
} else {
	
	try {
		isitzung.performUserLogin(login_user, login_pwd, login_role, this.appid,surrounding);
		if ( login_pdb_background ) {
			isitzung.doBackgroundLoginPdB(login_user, login_pwd);
		}
	} catch (Exception e) {
		eReturn = e;
	}
	
	

}
login_pwd=null; login_user=null; login_role = null;
return eReturn;

}
						;

private PrintStream tracePrinter = null;

public boolean isInterpreterShouldGiveBackTk() {
	return interpreterShouldGiveBackTk;
}
public void setInterpreterShouldGiveBackTk(boolean interpreterShouldGiveBackTk) {
	this.interpreterShouldGiveBackTk = interpreterShouldGiveBackTk;
}

public boolean isInterpreterShouldIgnoreTk() {
	return interpreterShouldIgnoreTk;
}
// private boolean tiShouldSelectFile = false;
/*
public boolean isTiShouldSelectFile() {
	return tiShouldSelectFile;
}
public void setTiShouldSelectFile(boolean tiShouldSelectFile) {
	this.tiShouldSelectFile = tiShouldSelectFile;
	terminator.setFileChoose(tiShouldSelectFile);
}
public boolean isTiShouldSelectDir() {
	return tiShouldSelectDir;
}
public void setTiShouldSelectDir(boolean tiShouldSelectDir) {
	this.tiShouldSelectDir = tiShouldSelectDir;
	terminator.setDirChoose(tiShouldSelectDir);
}
private boolean tiShouldSelectDir = false;
*/


Surrounding surrounding = null;
/*
public boolean isIncludeHeaderAndFooter() {
	return (surrounding.header != Surrounding.INCLUDE_OFF);
}
*/

public void setSurrounder(Surrounding surrounding) {
	this.surrounding = surrounding;

}

private IGuiController guiController = null;


private static final int c_prop = 			0; // nic123 kommt noch
private static final int c_file = 			1; // nic123 kommt noch
private static final int c_dir = 			2; // nic123 kommt noch
private static final int c_when = 			3;
private static final int c_loop = 			4;
private static final int c_choose = 		5;
private static final int c_try_catch = 		6;
private static final int c_call = 			7;
private static final int c_call_job = 		8;
private static final int c_call_service = 	9;
private static final int c_call_pro = 		10;
private static final int c_call_app = 		11;
private static final int c_ti = 			12;
private static final int c_throw = 			13;
private static final int c_throw_again = 	14;
private static final int c_error = 			15;
private static final int c_warn = 			17;
private static final int c_info = 			19;
private static final int c_i = 				20;
private static final int c_debug = 			21;
private static final int c_trace = 			23;
private static final int c_fatal = 			25;
private static final int c_log = 			27;
private static final int c_lock = 			28;
private static final int c_taskdef = 		29;
private static final int c_fundef = 		30;
private static final int c_ask = 			31;
private static final int c_remove = 		32;
private static final int c_escape = 		33;
private static final int c_unescape = 		34;
private static final int c_sequence = 		35;
private static final int c_report_coops = 	36;
private static final int c_log_plus = 		38;
private static final int c_dump_all = 		39;
private static final int c_dump = 			40;
private static final int c_dump_ti = 		41;
private static final int c_dump_ti_all = 	42;
private static final int c_include = 		43;
private static final int c_global = 		44;
private static final int c_tp = 			45;
private static final int c_tk = 			46;
private static final int c_ti_break = 		47;
private static final int c_ti_hr = 			48;
private static final int c_ti_flush = 		49;
private static final int c_ti_bottom = 		51;
private static final int c_log_minus = 		53;
private static final int c_insert = 		54;
private static final int c_inline = 		55;
private static final int c_restart = 		57;
private static final int c_ti_switch = 		58;
private static final int c_interpreter = 	59;
private static final int c_parallel = 		61;
private static final int c_block = 			62;
private static final int c_begin = 			63;
private static final int c_end = 			64;
private static final int c_echo = 			65;
private static final int c_translate = 		67;
private static final int c_ti_space = 		68;
private static final int c_var = 			69;
private static final int c_tx_style = 		70; 
private static final int c_need = 			71;	
private static final int c_alt = 			72;
private static final int c_case = 			73;
// private static final int c_p = 				74;
// private static final int c_f = 				75;
// private static final int c_d = 				76;
private static final int c_coopdef = 		77;
private static final int c_stop = 			78;
private static final int c_mc = 			79;
private static final int c_vfs = 			80;
private static final int c_commit = 		81;
private static final int c_rollback = 		82;
private static final int c_exclude = 		83;

private static final int c_ti_break_hr = 	84;
private static final int c_ti_hr_break = 	85;
private static final int c_ti_clear = 	86;

private static HashMap<String, Integer> requm = null; 
private static final int c_req_file_temp_dir      = 0;
private static final int c_req_cache_system       = 1;
private static final int c_req_sys_cache          = 2;
private static final int c_req_databaselink       = 3;
private static final int c_req_libro              = 4;
private static final int c_req_libro2             = 5;		
private static final int c_req_master_project     = 6;
private static final int c_req_master_project_app = 7;
private static final int c_req_new_temp_pro       = 8;
private static final int c_req_session_home_fs    = 9;    
private static final int c_req_session_home_mem   = 10; 
private static final int c_req_mpr                = 11; 
private static final int c_req_mpe1               = 12;
private static final int c_req_mpe2               = 13;
private static final int c_req_mpe3               = 14;
/*
private static final int c_ = ;
private static final int c_ = ;
*/				

public XrRqRp getRqRp() {
	
	// XrRqRp x = new XrRqRp();
	return rr;
}
public static void startupActions(boolean report) {
	cmdm=new HashMap<String, Integer>();
	cmdm.put("prop", new Integer(c_prop));
	cmdm.put("file", new Integer(c_file));
	cmdm.put("dir", new Integer(c_dir));
	cmdm.put("when", new Integer(c_when));
	cmdm.put("loop", new Integer(c_loop));
	cmdm.put("choose", new Integer(c_choose));
	cmdm.put("try_catch", new Integer(c_try_catch));
	cmdm.put("call", new Integer(c_call));
	cmdm.put("call_job", new Integer(c_call_job));
	cmdm.put("call_service", new Integer(c_call_service));
	cmdm.put("call_pro", new Integer(c_call_pro));
	cmdm.put("call_app", new Integer(c_call_app));
	cmdm.put("ti", new Integer(c_ti));
	cmdm.put("throw", new Integer(c_throw));
	cmdm.put("stop", new Integer(c_stop));
	cmdm.put("throw_again", new Integer(c_throw_again));
	cmdm.put("error", new Integer(c_error));
	cmdm.put("warn", new Integer(c_warn));
	cmdm.put("info", new Integer(c_info));
	cmdm.put("i", new Integer(c_i));
	cmdm.put("debug", new Integer(c_debug));
	cmdm.put("trace", new Integer(c_trace));
	cmdm.put("fatal", new Integer(c_fatal));
	cmdm.put("log", new Integer(c_log));	
	
	cmdm.put("lock", new Integer(c_lock));
	cmdm.put("taskdef", new Integer(c_taskdef));
	cmdm.put("fundef", new Integer(c_fundef));
	cmdm.put("ask", new Integer(c_ask));
	cmdm.put("remove", new Integer(c_remove));
	cmdm.put("escape", new Integer(c_escape));
	cmdm.put("unescape", new Integer(c_unescape));
	cmdm.put("sequence", new Integer(c_sequence));
	cmdm.put("dump_coops", new Integer(c_report_coops));
	cmdm.put("var", new Integer(c_var));
	cmdm.put("log_plus", new Integer(c_log_plus));
	cmdm.put("dump_all", new Integer(c_dump_all));
	cmdm.put("dump", new Integer(c_dump));
	cmdm.put("dump_ti", new Integer(c_dump_ti));
	cmdm.put("dump_ti_all", new Integer(c_dump_ti_all));
	cmdm.put("include", new Integer(c_include));
	cmdm.put("exclude", new Integer(c_exclude));
	cmdm.put("global", new Integer(c_global));
	cmdm.put("tp", new Integer(c_tp));
	cmdm.put("tk", new Integer(c_tk));
	cmdm.put("ti_break", new Integer(c_ti_break));
	
	cmdm.put("ti_hr_break", new Integer(c_ti_hr_break));
	cmdm.put("ti_break_hr", new Integer(c_ti_break_hr));
	cmdm.put("ti_clear", new Integer(c_ti_clear));	


	cmdm.put("ti_hr", new Integer(c_ti_hr));
	cmdm.put("vfs", new Integer(c_vfs));
	cmdm.put("commit", new Integer(c_commit));
	cmdm.put("rollback", new Integer(c_rollback));
	cmdm.put("ti_flush", new Integer(c_ti_flush));
	cmdm.put("ti_bottom", new Integer(c_ti_bottom));
	cmdm.put("log_minus", new Integer(c_log_minus));
	cmdm.put("insert", new Integer(c_insert));
	cmdm.put("inline", new Integer(c_inline));
	
	cmdm.put("restart", new Integer(c_restart));
	cmdm.put("ti_switch", new Integer(c_ti_switch));
	cmdm.put("interpreter", new Integer(c_interpreter));
	cmdm.put("parallel", new Integer(c_parallel));
	cmdm.put("block", new Integer(c_block));
	cmdm.put("begin", new Integer(c_begin));
	cmdm.put("end", new Integer(c_end));
	cmdm.put("need", new Integer(c_need));
	cmdm.put("echo", new Integer(c_echo));
	cmdm.put("ti_space", new Integer(c_ti_space));
	cmdm.put("tx_style", new Integer(c_tx_style));
	cmdm.put("translate", new Integer(c_translate));
	cmdm.put("alt", new Integer(c_alt));
	cmdm.put("case", new Integer(c_case));
	//cmdm.put("p", new Integer(c_p));
	//cmdm.put("f", new Integer(c_f));
	//cmdm.put("d", new Integer(c_d));
	cmdm.put("coopdef", new Integer(c_coopdef));
	cmdm.put("mc", new Integer(c_mc));

	
	if ( report ) {
		Sp.p("");
		Sp.p("-) Registered SYSTEM-TASKS:");
		SortedSet<String> keys = new TreeSet<String>(cmdm.keySet());
		for (String key : keys) { 
			Sp.p(key);
		}
		Sp.p("");
	}
	requm=new HashMap<String, Integer>();
	requm.put("file_temp_dir", new Integer(c_req_file_temp_dir));  
	requm.put("cache_system", new Integer(c_req_cache_system));  
	requm.put("sys_cache", new Integer(c_req_sys_cache));  
	requm.put("databaselink", new Integer(c_req_databaselink));  
	requm.put("libro", new Integer(c_req_libro));  
	requm.put("libro2", new Integer(c_req_libro2));  
	requm.put("master_project", new Integer(c_req_master_project));  
	requm.put("master_project_app", new Integer(c_req_master_project_app));  
	requm.put("new_temp_pro", new Integer(c_req_new_temp_pro));  
	requm.put("session_home_fs", new Integer(c_req_session_home_fs));  
	requm.put("session_home_mem", new Integer(c_req_session_home_mem));  
	requm.put("mpr", new Integer(c_req_mpr));  	
	
	requm.put("mpe1", new Integer(c_req_mpe1)); 
	requm.put("mpe2", new Integer(c_req_mpe2)); 
	requm.put("mpe3", new Integer(c_req_mpe3)); 	
	// nic321
	looptype_names = new KeyWordMapper("LoopTypes",4);
	looptype_names.put("xml", looptype_xml);
	looptype_names.put("regex", looptype_regex);
	looptype_names.put("select", looptype_select);
	looptype_names.put("while", looptype_while);
	looptype_names.putFinished();
	
	etyp_names = new KeyWordMapper("VarTypes-Exclude",3);
	etyp_names.put("p", etyp_p);
	etyp_names.put("f", etyp_f);
	etyp_names.put("d", etyp_d);
	etyp_names.putFinished();
	 
	kwmc_names = new KeyWordMapper("Mc-Types",6);
	kwmc_names.put("when", kwmc_when);
	kwmc_names.put("choose", kwmc_choose);
	kwmc_names.put("include", kwmc_include);
	kwmc_names.put("try_catch", kwmc_try_catch);
	kwmc_names.put("loop", kwmc_loop);
	kwmc_names.put("call_local", kwmc_call_local);
	kwmc_names.putFinished();	
	
	kwmvtyp = new KeyWordMapper("Var-Types",3);
	kwmvtyp.put("prop", kwmvtyp_prop);
	kwmvtyp.put("file", kwmvtyp_file);
	kwmvtyp.put("dir", kwmvtyp_dir);
	kwmvtyp.putFinished();	
	
	dt_names = new KeyWordMapper("Dir-Requests",11);
	dt_names.put("file_temp_dir", dt_file_temp_dir);
	dt_names.put("cache_system", dt_cache_system);
	dt_names.put("sys_cache", dt_sys_cache);
	dt_names.put("databaselink", dt_databaselink);
	dt_names.put("libro", dt_libro);
	dt_names.put("libro2", dt_libro2);
	dt_names.put("master_project", dt_master_project);
	dt_names.put("master_project_app", dt_master_project_app);
	dt_names.put("new_temp_pro", dt_new_temp_pro);
	dt_names.put("session_home_fs", dt_session_home_fs);
	dt_names.put("session_home_mem", dt_session_home_mem);
	dt_names.putFinished();	
}	
private static final int dt_file_temp_dir   = 0;
private static final int dt_cache_system   = 1;
private static final int dt_sys_cache   = 2;
private static final int dt_databaselink   = 3;
private static final int dt_libro   = 4;
private static final int dt_libro2   = 5;
private static final int dt_master_project  = 6;
private static final int dt_master_project_app   = 7;
private static final int dt_new_temp_pro   = 8;
private static final int dt_session_home_fs   = 9;
private static final int dt_session_home_mem   = 10;
private static KeyWordMapper dt_names;

private static final int kwmvtyp_prop   = 0;
private static final int kwmvtyp_file   = 1;
private static final int kwmvtyp_dir   = 2;
private static KeyWordMapper kwmvtyp;

private static final int kwmc_when   = 0;
private static final int kwmc_choose   = 1;
private static final int kwmc_include   = 2;
private static final int kwmc_try_catch   = 3;
private static final int kwmc_loop   = 4;
private static final int kwmc_call_local   = 5;
private static KeyWordMapper kwmc_names;

private static final int looptype_xml   = 0;
private static final int looptype_regex   = 1;
private static final int looptype_select   = 2;
private static final int looptype_while   = 3;
private static KeyWordMapper looptype_names;

private static final int etyp_p   = 0;
private static final int etyp_f   = 1;
private static final int etyp_d   = 2;
private static KeyWordMapper etyp_names;

public static boolean break_one = false;
public static boolean break_loops = false;
public static final String debug_equal_lock = " == ";
public static final String debug_equal_nolock = " =  ";	

private static final String tp_start = "Go: ";
private static final String tp_error = "ERROR:  ";
private boolean ti_bottom = false;

private static final int step_type_logic = 1;
private static final int step_type_std = 2;
private static final int step_type_visual = 3;
private static final int step_type_expression = 4;
private static final int step_type_call = 5;

private static boolean stop_go = false;
public static void setStopGo ( boolean p_stop_go) {
	stop_go = p_stop_go;
}

// Logging
LogConnector lc = new LogConnector("");
LogConnector lc_ti = new LogConnector("ti");
static LogConnector lcs = new LogConnector("");

DbJob jobConstr = null;
ISitzung isitzung = null;
Eden eden = null;

String appid = null;

String appidOrAs = null;
boolean isMasterApp = false;
String instid = null;
boolean isSystemcall = false;

String appdir = null;
String appdirEscaped = null;
String instdir = null;
String instdirEscaped = null;
int call_depth = 0;
String librodirEscaped = null;

HashMap<String, InterDir> dirMap = new HashMap<String, InterDir>();
HashMap<String, InterFile> fileMap = new HashMap<String, InterFile>();
HashMap<String, InterProp> propMap = new HashMap<String, InterProp>();

ArrayList<InterSequence> sequenceList = new ArrayList<InterSequence>();

ArrayList<InterLoopXml> loopListXpath = new ArrayList<InterLoopXml>();
ArrayList<InterLoopRegex> loopListRegex = new ArrayList<InterLoopRegex>();
ArrayList<InterLoopWhile> loopListWhile = new ArrayList<InterLoopWhile>();
ArrayList<InterLoopSelect> loopListSelect = new ArrayList<InterLoopSelect>();
ArrayList<InterLogg> LogList = new ArrayList<InterLogg>();
ArrayList<InterLock> LockList = new ArrayList<InterLock>();

HashMap<String, PlugFundef>  FundefMap = new HashMap<String, PlugFundef>();
HashMap<String, PlugTaskdef> TaskdefMap = new HashMap<String, PlugTaskdef>();

// private static HashMap<String, ICoop> noMapKeep = new HashMap<String, ICoop>();
private        HashMap<String, ICoop> noMap = new HashMap<String, ICoop>();

private static HashMap<String, String> global_map = new HashMap<String, String>();

Date jobtime = null;
Date steptime = null;

public static final String declareEcho = "echo: ";
public static final String declareXmlLoop = "XmlLoop: ";
public static final String declareWhileLoop = "WhileLoop: ";	
public static final String declareChoose = "Choose: ";
public static final String declareExe = "Exe: ";
public static final String declareDebug = " ";
public static final String declareGetprofile = "getprofile: ";
public static final String declareCall = "Call: ";
public static final String declareOutercall = "outercall: ";
public static final String declareSystemcall = "systemcall: ";

ArrayList<Element> configList1 = new ArrayList<Element>();
int prologSize = 0;

public static final int lb = 10;
public static final int lb2 = 26;
 
Parameters remember_paras = new Parameters ();

// private String lastErrorStep = null;
private int silence = 0;
public static final int SILENCE_NO   = 0;
public static final int SILENCE_YES  = 1;
public static final int SILENCE_VERY = 2;	

// private ITerminator terminator = null;
boolean construction_ok = true;
// private boolean dont_clear = false;

private int logic_counter = 0;

private Level tiLogLevel = null;
private boolean ti_log_vars = false;

private boolean debugOn = false;
// private boolean isDebug = false;

private Element flexList ;
private String to_ok = "";
private ArrayList<DbJob> jobcalllist = new ArrayList<DbJob>();

private int math_scale = 10;
private int math_round = BigDecimal.ROUND_HALF_UP;


// private levelDebugLeast = true;
/*
 * INITIALS
 */

public int getNextLogicCounter() {
	logic_counter++;
	return logic_counter;
}
private String getDescriptionStringSmall() {
	String ret = 
			"Job="+jobConstr.getJobid() +
			" app="+appid +
			" inst="+instid+
			" instdir="+instdir+
			" isService="+isSystemcall+
			" flexList="+Commons.toTextCompact(flexList); 
				// das wird nur bei loglevel debug+ verwendet, 
				// das kann so bleiben, kein secret-interpretation notwendig
	

	return ret;
}
private String getDescriptionString() {
	// das wird nur bei loglevel debug+ verwendet
	String ret = 
			"Job="+jobConstr.getJobid() +
			" app="+appid +
			" inst="+instid+
			" instdir="+instdir+
			" flexList="+Commons.toTextCompact(flexList);
	
	if ( configList1 != null ) {
		Iterator i = configList1.iterator();
		if (configList1.size() >  0) {
			ret=ret+ " CONFIGs:: ";
			while ( i.hasNext()) {
				Element e = (Element)i.next();
				ret = ret + Commons.toTextCompact(e);
				
			}
		}
		
	}
	return ret;
}
DbApp myapp = null;
public Interpreter ( 
		DbJob _jobConstr,
		String _appid, 
		String _instid,
		boolean _isSystemcall,
		int _silence,
		int call_depth,
		Element flexList,
		String _instdir  ,
		IGuiController guicontroller,
		ISitzung sitzung
		) {
	
	myInterpreterId = ++interpreterId;
	if ( reportCoops )
		System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  NEW INTERPRETER job="+_jobConstr.getJobid()+" app="+_appid+" service="+_isSystemcall);
	isitzung = sitzung;
	jobConstr = _jobConstr;
	appid = _appid;
	instid = _instid;
	isSystemcall = _isSystemcall;

	silence = _silence;
	this.call_depth = call_depth;
	this.flexList = flexList;
	this.guiController = guicontroller;
	
	if ( isSystemcall ) {
		appidOrAs = "S";
		isMasterApp = false;
	} else {
		appidOrAs = appid;
		if ( appid.equals(isitzung.getMasterProjektApp()))
			isMasterApp = true;
		else
			isMasterApp = false;				
	}
	kMap = isitzung.getCoopMap_k(appidOrAs,isMasterApp);
		
	if ( kMap.size() > 0) {
		lc.info("Number of permantent Coops at job-start: "+kMap.size());		
	}	
		
	// lc.info("Doing Interpreter Initial, coop      counter = "+noMap.size());
	try {
		

		
		Sp.p("****** Interpreter.BUILD() -- "+appid+", "+jobConstr.getJobid()+", "+instid+", "+_instdir+", isservice="+isSystemcall);
		// lc.warn("DUMM: Welcome to Interpreter-Constructor");
		tempFileManger = TempFileManager.getTempFileManager(this);
		 librodirEscaped = Entities.importEntities(isitzung.getLibroDir(appid));
		/*[echnaton-customer]*/		
		if ( appid != null ) {
			myapp = CoreBuilder.db.getAppById(appid);
			if ( myapp == null )
				throw new Exception("@1 The application '"+appid+"' does not exist");
			appdir = myapp.getDir();
			appdirEscaped = myapp.getDirEscaped();
			
			
			if ( _instdir != null ) {
				instdir = _instdir;
				instdirEscaped = Entities.importEntities(instdir);
				Sp.p("inst dir is still: "+instdir);
			} else if ( instid != null ) {
				DbPro myinst = myapp.getInstById(instid);
				if ( myinst != null ) {
					instdir = myinst.getDir();
					instdirEscaped = Entities.importEntities(instdir);
					Sp.p("inst dir was fetched from db: "+instdir);
				} else
					Sp.p("db didnt deliver dir for inst : "+instid);
					// throw new Exception("The projoect '"+instid+"' does not exist in the application '"+appid+"'");
				
			}
		} else if ( instid != null ) {
			throw new Exception("@2 A project, in this case '"+instid+"' without an app is not possible");
		}
		/*[echnaton-timer]*/	
		if ( jobConstr != null )
			lc.debug("Engine:   starting job="+jobConstr.getJobid()+
					" app="+appid+" pro="+instid+" isService="+isSystemcall+
					" silence="+silence+" call_depth="+call_depth+" jobfile="+jobConstr.getFullfn());

		// lc.core_on();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		Sp.p("Printing Stack @4");
		Sp.pe(e);
		
	}
	
}

Element flexEndElement = null;
public void setFlexEndElement( Element e ) {
	flexEndElement = e;
}

// private boolean webModus = false;

private boolean interpreterShouldGiveBackTk = false;
private boolean ignoreClassloading = false;
private boolean interpreterShouldIgnoreTk = false;

public boolean isIgnoreClassloading() {
	return ignoreClassloading;
}
public void setIgnoreClassloading(boolean ignoreClassloading) {
	this.ignoreClassloading = ignoreClassloading;
}
public void setInterpreterShouldIgnoreTk(boolean interpreterShouldIgnoreTk) {
	this.interpreterShouldIgnoreTk = interpreterShouldIgnoreTk;
}


public void addConfigList( List<Element> confList ) {
	printElementList(confList, "Interpreter is adding Config-list");
	Iterator<Element> i = confList.iterator();
	while ( i.hasNext()) {
		Element conf = i.next();
		addConfig ( conf );
	}
	
}

public void addConfig( Element configElement ) {
	if ( configElement != null ) {
		configList1.add(configElement);			
		prologSize = prologSize + configElement.getContentSize() + 1;
	}
}
private XrRqRp rr = null;
private ITerminator terminator = null;
public void setTerminatorBeforeRun(ITerminator _terminator) {
	terminator = _terminator;
}

private Surrounding subSurrounding = null;
public void run(  )  {
	

	try {	
		
		
		if ( surrounding == null )
			throw new Exception("Interpreter: "+getDescriptionStringSmall()+" doesnt have a surrounding");	
		subSurrounding = surrounding.getSurroundingInterpreterSub();
		
		// sicherheitsstufe&& false&& false
		/* sicherheitsstufe */ if (isitzung.hasPrivTrace()) {			
			try {
				tracePrinter = tempFileManger.requestTracer(getDescriptionStringSmall(),jobConstr.getJobid(),call_depth);
				canPrintTrace = true ;
			} catch (IOException e) {
				canPrintTrace = false ;
				Sp.p("Could not initialize tracePrinter: "+e.getMessage());
				Sp.pe(e);
			}
		} else if (!isitzung.isOnline() &&  lc.debugIsOn() ) {
			canPrintTrace = true ;
		} else
			canPrintTrace = false ;
	
		if ( lc.debugIsOn() &&  CoreBuilder.rslogger.isDebug_filter_logic()) {
			Element tx = new Element("ti");
			tx.setAttribute("label", ">> interpreter >>");
			String tit ;
			if ( isSystemcall ) tit = "service";
			else tit = "job";
			tit = tit+"="+jobConstr.getJobidEscaped();
			if ( appid !=null)
				tit = tit+", app="+Entities.importEntities(appid);
			if ( instid !=null)
				tit = tit+", pro="+Entities.importEntities(instid);			
			tx.setAttribute("x",tit);
			try {
				terminator.executeTi(tx,tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e1) {}	
			
			if ( instdir != null ) {
				tx = new Element("ti");
				tx.setAttribute("label", "prodir");
				tx.setAttribute("flag", "label_right");
				tx.setAttribute("type", "dir");
				tx.setAttribute("x",Entities.importEntities(instdir));
				try {
				terminator.executeTi(tx,tiLogLevel,appid,appdir,instid,instdir,this);
				} catch (Exception e1) {}					
			}
			
			tx = new Element("ti");
			tx.setAttribute("label", "jobfile");
			tx.setAttribute("flag", "label_right");
			tx.setAttribute("type", "file");
			tx.setAttribute("x",jobConstr.getFullfnEscaped());
			try {
			terminator.executeTi(tx,tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e1) {}	
		}
		
		if ( kMap.size() > 0) {
			lc.info("Number of permantent Coops at job-start: "+kMap.size());		
		}	
						
		runInternal();
	} catch (Exception e) {
		
		XrRqRpEx a =  new XrRqRpEx(e,exceptionTracer,
				"", // terminator.getTiScreenBodyMemberAndConsume("INTERPRETER 3"), // helli
				terminator.getTp());
		a.set(instid, instdir, appid, appdir, tiLogLevel, lc, jobtodo.getJobid(),
				terminator, jobtodo); 		
		rr = (XrRqRp)a;

		lc.addExceptionIfNew(e,e);
		lc.warn("EXCEPTION: "+e.getLocalizedMessage());
		lc.trace("---- exception thrown by [ runInternal() ] ------------------------");
		Sp.p("---- exception thrown by [ runInternal() ] ------------------------");
		Sp.p("Printing Stack @6");
		Sp.pe(e);
			
		
		// throw new RuntimeException("@6 "+e.getLocalizedMessage());

	}
	
	
}

private boolean haltIsRequested = false;
public void halt() {
	haltIsRequested = true;		
}
private DbJob jobtodo=null;
private void runInternal ()  {
	rr = null;
	try {
		CoreBuilder.rslogger.resetCounterDebug();
		if (!construction_ok) { 
			XrRqRpEx a = new XrRqRpEx(new Exception("construction not ok"),
					null,
					"konstrktion ot op","kaputitput");
			
			
			a.set(instid, instdir, appid, appdir, tiLogLevel, lc, jobtodo.getJobid(),
					terminator, jobtodo); 		
			rr = (XrRqRp)a;	
					
		} else {
			jobtime = new Date();
			jobtodo = jobConstr;			
			rr = run2(configList1,flexList);
			/*
			if (  !dont_clear ) {
				closeAllSequences();
				closeAllLogs();
			}*/			
		}


		CoreBuilder.appManager.halted(this);
	} catch (Exception e) {
		CoreBuilder.appManager.halted(this);
		rr =  new XrRqRpEx(e,null,
				"<p> halted</p>",
				"halted");
	}
}

public XrRqRp getRqRpAfterRunning() {
	return rr;
}
private XrRqRp run2(  ArrayList<Element> configList, 
		Element flexList ) {
	jobcalllist.add(jobtodo);
	if ( call_depth == 0 ) {
		terminator.jobgroupstart(this);
		// CARLA7
		CoreBuilder.rslogger.clearLogNodeLevel();
		//JOptionPane.showMessageDialog(null,"did groupsart for job '"+
		//jobtodo.getJobid()+"' with call_depth = "+call_depth);		
	} else {
		//JOptionPane.showMessageDialog(null,"no groupsart for job '"+
		//jobtodo.getJobid()+"' with call_depth = "+call_depth);
	}
	printJobstartToTerminator();


		
	XrRqRp rr = run3NOVO(configList, flexList);

	
	// Logger: Done
	/*
	boolean doFlush = true;
	if ( jobtodo.getJobid().equals("??") || jobtodo.getJobid().equals("?")) 
		doFlush=false;*/
	


	
	return performFinalPrints(rr);
		
}
private XrRqRp performFinalPrints( XrRqRp rr ) { // helli
	if ( rr instanceof XrRqRpTi ) 
		printTerminatorWellEnded((XrRqRpTi)rr);		
	if ( rr instanceof XrRqRpEx ) {
		XrRqRpEx kk =  (XrRqRpEx)rr;
		//printTrace("Interperter-final-prints-exceptio in terminator BEGIN "+terminator.getTiScreenBodyMember());
		printTerminatorEndeBad(kk);
		//printTrace("Interperter-final-prints-exceptio in terminator   END "+terminator.getTiScreenBodyMember());
		// kk.setTiBody(terminator.getTiScreenBodyMemberAndConsume("Interperter.performFinalPrints"));
	}
	return rr;
	
}

private void closeTempFileManager(Exception ex) {
	
	try {
		tempFileManger.finishJob(ex);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		Sp.pe(e);
	}	
}
private void printTerminatorEndeBad ( XrRqRpEx x ) {
	Exception ex = x.getException();
	LogConnector lc = x.getLc();
	
	
	if ( ex instanceof at.nicolas.x.int_specs.StopException ) {
		
		String myMsg ="?";
		try {
			myMsg=ex.getLocalizedMessage();
		} catch (Exception e) {
			myMsg="?";
		}
		setProgressPlaintext("Stopped with reason: "+myMsg, true);
		lc.info("stoppe with reason: "+myMsg);	
		
		terminator.tiBreak(null);
		Element stopE = new Element("ti");
		stopE.setAttribute("label", "Job stopped");
		
		Element stopE2 = new Element("ti");
		stopE2.setAttribute("label", "Reason:");
		stopE2.setText(myMsg);			
		stopE.addContent(stopE2);
		try {
			terminator.executeTi(stopE,tiLogLevel,appid,appdir,instid,instdir,this);
		} catch (Exception e) {
			Sp.pe(e);
		}

		terminator.jobend(
				jobtodo.getJobid(), 
				appid, instid, 
				isSystemcall, call_depth, 
				"Job Stopped");	
		
	} else if ( isTkStop(ex) ) {
		
		String myMsg ="?";
		try {
			myMsg=ex.getLocalizedMessage();
		} catch (Exception e) {
			myMsg="?";
		}
		setProgressPlaintext(myMsg, true);
		lc.info(myMsg);	
		
		terminator.tiBreak(null);
		if ( lc.debugIsOn()|| CoreBuilder.ws.isRoleTestUp()) {
			debugall(false, 
					lc.getCurrentLevel()
					//lc.getLevel(etE.getAttributeValue("level"))
					,myMsg,
					true);
			
		} else {
		
			Element stopE = new Element("ti");
			stopE.setAttribute("label", "Input stopped");
			
			Element stopE2 = new Element("ti");
			
			stopE2.setText(myMsg);			
			stopE.addContent(stopE2);
			try {
				terminator.executeTi(stopE,tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}				
		}


		terminator.jobend(
				jobtodo.getJobid(), 
				appid, instid, 
				isSystemcall, call_depth, 
				"Input & Job Stopped");	
		
	}  else {
		Element e = new Element("tp");
		// e.setText("? FAIL ? "+jobText+ " "+locName);
		
		try {
			e.setText(tp_error+ex.getMessage());
		} catch (Exception e4) {
			// TODO Auto-generated catch block
			String xx ="Ein Fehler mit einem xml-problem";
			e.setText(xx);
			System.out.println("ERROR: "+tp_error+ex.getMessage());
		}
		String fortp;
		try {
			fortp = tp_error+ex.getMessage();
		} catch (Exception e3) {
			fortp = tp_error+"Some unknown message";
		}
		try {
			setProgressPlaintext(fortp, true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			try {
				setProgressPlaintext(tp_error+ex.getMessage(), true);
			} catch (Exception e2) {				}
		}	

		// Logger: Exception nic123
		if ( lc.debugIsOn() && CoreBuilder.rslogger.isDebug_filter_logic()) {
			Element tx = new Element("ti");
			tx.setAttribute("label", "<< interpreter <<");
			tx.setAttribute("x","done with ERROR.");
			try {
				terminator.executeTi(tx,tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e1) {}		
		}			
		lc.fatal("***********************");	

		lc.fatal("*         *   ");			
		lc.fatal("*  ERROR  *   "+ ex.getLocalizedMessage() + " - "+ex.getClass().getCanonicalName());
		lc.fatal("*         *   in file: "+ jobtodo.getFullfn());	
		lc.fatal("*         *   ");	
		lc.fatal("***********************");	
		
		lc.fatal("*         *   XmlRobot-ExceptionStack: ");
		lc.printFatalStack(" *  *  *  * *   ");
		lc.fatal("*         *   ");		
		Sp.p("Printing Stack @7");
		// printExceptionStackWhenTracable(ex);
		String dertext=appid;
String longStrow2 ="*****************************";

/*
        lc.fatal("app="+.appid+" "+pro="+.p");	
		lc.fatal("*         *"+zeile_job);
		if ( appid != null )
			lc.fatal(zeile_app);
		if ( instid != null )
			lc.fatal(zeile_inst);
		
		lc.fatal("*         *");
		lc.fatal(longStrow2);*/
		try {
			lc.fatal("*  ERROR  *   "+ ex.getLocalizedMessage() + " - "+ex.getClass().getCanonicalName());
		} catch (Exception e2) {
			lc.fatal("*  ERROR  *   "+ "No Message" + " - "+ex.getClass().getCanonicalName());
		}
		lc.fatal("*         *   in file: "+ jobtodo.getFullfn());			
        lc.fatal(longStrow2);	
    	if ( call_depth > 0 ) {
    		Element logoff = new Element("log_minus");
    		executeJobelementSafe(logoff,"jobender",false,c_log_minus);
    	}
								
		if ( call_depth == 0  && !isSystemcall && !jobConstr.getJm().isServiceApp() && jobConstr.getJobid().indexOf("?")==-1) {
			String mj = jobConstr.getJobid();
			if ( isSystemcall)
				mj = mj+":SERVICE";
			mj = mj+":EXCEPTION";
		}

		terminator.tiBreak(null);
		
		// musik
		try {
			ArrayList<Element> doku;
			if ( isitzung.hasPrivDump())
				doku = exceptionTracer.getTiDoku();
			else
				doku = exceptionTracer.getTiDokuNoDump();
			Iterator<Element> dokuIterator = doku.iterator();
			while ( dokuIterator.hasNext()) {
				Element toprint = (Element)dokuIterator.next();
				try {
					terminator.executeTi(toprint,tiLogLevel,appid,appdir,instid,instdir,this);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Sp.pe(e1);
				}
			}
		} catch (Exception e1) {

		}

		if ( ex instanceof at.nicolas.x.int_specs.StopException );				
		else if ( debugOn)
			debugall(false, Level.WARN, "Dump at Time of Exception",true);
		else	
			debugall(false, Level.WARN, "Dump at Time of Exception",true);
		
		if ( ex instanceof at.nicolas.x.int_specs.StopException ) {
		} else if ( isTkStop(ex))
			terminator.joberror(jobtodo.getJobid(), appid, instid, isSystemcall, call_depth,false);
		else
			terminator.joberror(jobtodo.getJobid(), appid, instid, isSystemcall, call_depth,true);
		
		terminator.top();			
		
		

		if ( traceThisException(ex)) {
			Sp.pe(ex);
			Sp.p("Printing Stack @8");
		}
	} // das ende von: IF NOTstop exception	
	closeTempFileManager(ex);
	
}
	

Stack<StackStep> stack = new Stack<StackStep>();
Stack<LogicStackMember> logicStack = new Stack<LogicStackMember>();

private ExceptionTracer exceptionTracer = null;
public XrRqRp continueTheRunAfterResponse ( ) {
	
	//devMode = true;
	XrRqRp ff = innerCircle(null);
	return performFinalPrints(ff);

}
public XrRqRp continueTheRunAfterTkCancel ( ) {
	Element et = new Element("throw");
	et.setText("Input Stopped");
	et.setAttribute("id", "tkstopped");
	//addToStackPlain(et);
	XrRqRp ff = innerCircle(et);
	return performFinalPrints(ff);

}
private XrRqRp run3NOVO(  ArrayList<Element> configList, Element flexList ) {
	break_loops = false;
	exceptionTracer = new ExceptionTracer(jobtodo);
	// addToStackDebug("Station: A");
	if ( flexEndElement != null ) {
		lc_ti.debug_logic("Preperations (z) doint end-elements");
		addToStackSimple( flexEndElement.getChildren() );
	}	

	try {
		// check privilegs
		/*
		if ( !isSystemcall && !isitzung.hasPriv("app:"+appid)) {
			throw new Exception("Access not granted for app:"+appid);
		}*/
		
		// job priv testen
		if ( jobConstr.getRoleIndex() < 29 && jobConstr.getRoleIndex() > isitzung.getRoleIndex() )
			throw new Exception("Cannot run the job ["+jobConstr.getJobid()+"] due to your privilegs."+
		    " Jobs-Role=["+Rollen.getRoleByHeight(jobConstr.getRoleIndex())+"] " +
		    "Sessions-Role=["+Rollen.getRoleByHeight(isitzung.getRoleIndex())+"] " );

		
		if ( isitzung.checkLoginSuspension() != null ) {
			throw new Exception ("Login suspended for a while ...");
		}
		if ( surrounding != null && surrounding.isPublic());
		else if ( !isitzung.hasAppPriv(this.appid))
			throw new Exception ("No privileg for app "+this.appid);
		
		
		if ( surrounding != null &&
			 surrounding.footer != Surrounding.INCLUDE_OFF  &&
			 isitzung.hasPrivAppFooter()
			 ) { // && silence == SILENCE_NO) {

			addToStackPlain(surrounding.getFooterElement());
					
		}			
	
		ArrayList<Element> list = getExecutionElements(jobtodo);			

		addToStackSimple( list);
		
		// ArrayList<Element> configList2 = configList;		
		if ( surrounding != null &&
				surrounding.header != Surrounding.INCLUDE_OFF) { // && silence == SILENCE_NO) {
			addToStackPlain(surrounding.getHeaderElement());					
		}
				
		lc_ti.debug_logic("Preperations (y) Preperations finished");
		// addToStackDebug("Station: C");
		// addToStackDebug("Before Flexlist");
		if ( flexList != null ) {
			
			addToStackSimple( flexList.getChildren());
			
			lc_ti.debug_logic("Preperations (c) Starting execution of Flex-Paras");		
		}
		// addToStackDebug("After Flexlist");
		// addToStackDebug("Station: D");
		addToStackSimple( jobtodo.getExeProlog() );
		lc_ti.debug_logic("Preperations (b) Starting execution of Job-Prolog");

		//
		// addToStackDebug("Station: E");#addToStackDebug("Before Flexlist");#
		//addToStackDebug("Before ConfigList");
		//printElementList(configList, "CONFIG-LIST");
		// 
		addToStackSimpleList( configList);
		//addToStackDebug("After ConfigList");
		/* soll alles mit request gemacht werden
		final String libo =isitzung.getLibroUri(appid); 
		if ( libo!= null && libo.length() > 0 ) {
			Element tmpEf = new Element("dir");
			tmpEf.setAttribute("id", "libro");
			tmpEf.setAttribute("flag", "lock");
			tmpEf.setAttribute("x", isitzung.getLibroUri(appid));
			addToStackPlain(tmpEf);					
		}
		*/
		
		try {
			if( surrounding != null ) {
				String dbl= getDatabaselink();
				// System.out.println("#dddddddddddddbbbbbbbbbbbbbbbbLLLLLLLL "+dbl);
				if ( dbl!= null && dbl.trim().length() > 0 ) {
					Element tmpEf = new Element("dir");
					tmpEf.setAttribute("id", "databaselink");
					tmpEf.setAttribute("flag", "lock");
					tmpEf.setAttribute("x", dbl);
					addToStackPlain(tmpEf);	
				}			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			
	

		Element tmpE = new Element("dir");
		tmpE.setAttribute("id", "temp");
		tmpE.setAttribute("x", tempFileManger.requestMemTempDir());
		addToStackPlain(tmpE);	
		
		Element tmpEl = new Element("lock");
		tmpEl.setAttribute("id", "temp");
		tmpEl.setAttribute("type", "dir");
		tmpEl.setAttribute("flag","catch");
		addToStackPlain(tmpEl);			
		
		Element tmpE2 = new Element("dir");
		tmpE2.setAttribute("id", "filetemp");
		tmpE2.setAttribute("x", tempFileManger.requestFileTempDir());
		addToStackPlain(tmpE2);		
		
		Element tmpE2l = new Element("lock");
		tmpE2l.setAttribute("id", "filetemp");
		tmpE2l.setAttribute("type", "dir");
		tmpE2l.setAttribute("flag","catch");
		addToStackPlain(tmpE2l);				
		
		lc_ti.debug_logic("Preperations (a) Starting execution of Job-ConfigList");
		
		printStack("Initial run job: [ "+jobtodo.getJobid()+" ] isService="+isSystemcall);
		XrRqRp is =innerCircle(null);
		return is;
	} catch (Exception ex ) {
		
		XrRqRpEx a =  new XrRqRpEx(ex,exceptionTracer ,
				"", // terminator.getTiScreenBodyMemberAndConsume("INTERPRETER 1"), // helli
				terminator.getTp());
		a.set(instid, instdir, appid, appdir, tiLogLevel, lc, jobtodo.getJobid(),
				terminator, jobtodo); 		
		rr = (XrRqRp)a;	
		return rr;
	}
	
}
public Interpreter subInterprterToFinish = null;
private XrRqRp innerCircle(Element throwToInsert) {
	TkTicketManager finishedByWebTk = null;
	
	Element throwElementToConsume = throwToInsert;
	
	if ( subInterprterToFinish != null ) {
		if ( throwElementToConsume != null ) {
			subInterprterToFinish.addToStackPlain(throwElementToConsume);
			throwElementToConsume = null;
		}

		if (terminator.screenIsClear()  ) {
			terminator.write("RESUME execution of SUB-JOB "+subInterprterToFinish.getDescriptionStringSmall()
			+ " REQUESTED BY: "+getDescriptionStringSmall());
	
			if ( surrounding != null && surrounding.header != Surrounding.INCLUDE_OFF) { // && silence == SILENCE_NO) {
				try {
					subInterprterToFinish.addToStackPlain(surrounding.getHeaderElement());
				} catch (Exception e) {
					Sp.pe(e);
				}						
			}			
					
		}	
		Sp.p("Going to execute subInterprterToFinisch");
		
		XrRqRp gg = subInterprterToFinish.continueTheRunAfterResponse();
		
		if ( gg instanceof XrRqRpTk || gg instanceof XrRqRpEx )
			return gg;
		subInterprterToFinish = null;
	
	}
	
	try {
		
	if ( throwElementToConsume != null ) {
		addToStackPlain(throwElementToConsume);
		throwElementToConsume = null;
	}
	
	if (terminator.screenIsClear()  ) ;
		// terminator.write("RESUME execution of JOB "+this.getDescriptionStringSmall());	
		
	while ( !stack.isEmpty() && finishedByWebTk==null) {
		TkTicketManager finishedByTk = stackRotatorCatched();
		if ( finishedByTk != null ) {
			p("### stackRotatorCatched() returned: "+taskExplainerCompact(finishedByTk.tkOriginal));
			
			boolean doTkNow = false;
			
			Element etE = finishedByTk.interpreter.translateJobelement( finishedByTk.tkOriginal,true,-1,true ); 
			if ( printTkThings )
				Sp.p("YY "+taskExplainer(finishedByTk.tkOriginal));
			String test1 = etE.getAttributeValue("test");
			boolean test_eq_0 = false;
			if ( test1 != null && test1.equals("0"))
				test_eq_0 = true;
			
			if ( Commons.hasFlag(etE, "print_only") )
				doTkNow = true;
			else if ( test_eq_0 )
				doTkNow = true;
			else if (  interpreterShouldIgnoreTk ) {}
			else if (  interpreterShouldGiveBackTk ) {
				finishedByWebTk = finishedByTk;
				doTkNow = false;
			} else 
				doTkNow = true;
			if ( printTkThings ) {
				Sp.p("YY doTkNow="+doTkNow);
				Sp.p("YY finishedByWebTk="+finishedByWebTk);
			}
			if (doTkNow) {
				// Desktop: aufruf Tk
				
				if ( etE.getAttribute("top_label") != null ) {
					Element top_level_tk = new Element("tk");
					top_level_tk.setAttribute("label", etE.getAttributeValue("top_label"));
					top_level_tk.addContent(etE);
					terminator.executeTk(top_level_tk,tiLogLevel,appid,appdir,instid,instdir,this);
				} else
					terminator.executeTk(etE,tiLogLevel,appid,appdir,instid,instdir,this);
			}
		} 
	}
	if ( finishedByWebTk != null ) {
		
		XrRqRpTk f ;
		if (subInterprterToFinish != null )
			f = new XrRqRpTk(finishedByWebTk);
		else
			f = new XrRqRpTk(finishedByWebTk);
		f.set(tiLogLevel, appid, appdir, instid, instdir);
		if ( printTkThings )
			Sp.p("YY returning the new tk f="+f);
		return f;

	} else {
		XrRqRpTi fTi = new XrRqRpTi( terminator,
				instid,
				instdir,
				appid,
				appdir,
				tiLogLevel,
				this ,
				lc);
		fTi.setZeile_job(jobtodo.getJobid());
		fTi.setJobText(jobtodo.getJobid());
		fTi.setDoFlush(true);
		fTi.setJobSimu(jobtodo.getJobid());
		fTi.setZeile_inst(instid);
		fTi.setZeile_job(jobtodo.getJobid());
		fTi.setZeile_app2(appid);
		// nic123
		closeDownInterpreter();
		return (XrRqRp)fTi;
	}
} catch (Exception e) {
	// TODO Auto-generated catch block
	Sp.pe(e);
	XrRqRpEx fEx = new XrRqRpEx(
			e,
			exceptionTracer, 
			"", // terminator.getTiScreenBodyMemberAndConsume("INTERPRETER 2"), // helli
			terminator.getTp());
	
	fEx.set(instid, instdir, appid, appdir, tiLogLevel, lc, jobtodo.getJobid(),
			terminator, jobtodo); 
	exceptionToThrowAgain = e;
	// nic123
	closeDownInterpreter();
	return (XrRqRp)fEx;
}
}

private void closeDownInterpreter()  {
	if ( reportCoops )
		System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  CLOSE DOWN INTERPRETER");

	try {
		closeAllSequences();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (StorageLimitException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		closeAllLogs();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	removeAllNo();
}

private Element translateTkResultToTransBase( Element finishedByWebTk ) throws Exception {
	
	Element stage2 = finishedByWebTk.clone();
	
	List<Element> elements = Commons.xpathSelectNodes(stage2, "tk");
	Iterator<Element> i = elements.iterator();
	while ( i.hasNext() ) {
		Element e = (Element)i.next();
		if ( e.getAttribute("flag") != null ) {
			String txt = e.getAttributeValue("flag");
			String founds[]= txt.split("\\s+");
			
			for ( String found: founds ) {
				
				if ( found.length()> 0 ) {
					e.setAttribute("flag_"+found, found);
				}
			}
		}
		String type = null;
		String type2 = null;
		if ( e.getAttribute("type") != null )
			type = e.getAttributeValue("type");
		
		if ( type== null ) ; 
		else if ( type.equals("dir") ) {
			type2 = "dir";			
		} else 	if ( type.equals("file") ) {
			type2 = "file";			
		} else 	if ( type==null || 
				type.equals("text")|| 
				type.equals("textblock")
				|| type.equals("textblock") 
				|| type.equals("bool")
				|| type.equals("sel")) {
			type2 = "prop";			
		}
		if ( type2 != null ) {
			e.setAttribute("type2",type2);
		}
		
		if ( e.getAttribute("id") != null && type2 != null ) {
			String expression = "("+type2+","+e.getAttributeValue("id")+")";
			String translated = "()"+expression+"()";
			e.setAttribute("value_expression", translated);
			e.setAttribute("value_has", "()(f,has,"+type2+","+e.getAttributeValue("id")+")()");
			e.setAttribute("value_expression2", "()(f,vob,"+type2+","+e.getAttributeValue("id")+")()");
			e.setAttribute("value_initial", "(f,vob,"+type2+","+e.getAttributeValue("id")+")");
			e.setAttribute("titulo", type2+"_"+e.getAttributeValue("id"));
			
			if ( e.getAttribute("label") == null  ) {
				e.setAttribute("label",type2+"."+e.getAttributeValue("id"));
			}
			
		}
	}
	JOptionPane.showMessageDialog(null,"Terminator: clear() "+taskExplainer(stage2));
	return stage2;
	/*
	Element etE = translateJobelement( finishedByWebTk,true,-1,true ); 
	Element gugi = null;
	if ( etE.getAttribute("top_label") != null ) {
		Element top_level_tk = new Element("tk");
		top_level_tk.setAttribute("label", etE.getAttributeValue("top_label"));
		top_level_tk.addContent(etE);
		gugi = top_level_tk;
		// terminator.executeTk(top_level_tk,tiLogLevel,appid,appdir,instid,instdir,this);
	} else
		gugi = etE;
		// terminator.executeTk(etE,tiLogLevel,appid,appdir,instid,instdir,this);

	
	//XrRqRpTk f = new XrRqRpTk(finishedByWebTk,gugi);
	// f.set(gugi, tiLogLevel, appid, appdir, instid, instdir, this);
	return gugi;
	*/
}
private Exception exceptionToThrowAgain = null;
private void p(String println) {
	if ( !devMode ) return;
	Sp.p(println);
}
private void p22(String println) {
	
	Sp.ph(println);

}
private void printLogicStackMember(String titel, LogicStackMember lsm ) {
	if ( !devMode ) return;
	Sp.p("LOGIC-STACK-Member: "+titel);
	Sp.p(lsm.getType()+" "+taskExplainer(lsm.getBaseElement()));
}
private void printLogicStack(String titel ) {
	if ( !devMode ) return;
	Sp.p("LOGIC-STACK-REPORT: "+titel);
	Iterator<LogicStackMember> i = logicStack.iterator();
	int j = 0;
	while ( i.hasNext()) {
		j++;
		LogicStackMember x = (LogicStackMember)i.next();
		Sp.p(j+" "+x.getType()+" "+taskExplainer(x.getBaseElement()));
	}
	
	
}
private void printElementList(List<Element> elementList, String titel ) {
	if ( debugOn ) {
	Sp.p("--- Print Element List: "+titel+" ---- BEGIN");
	Iterator<Element> i = elementList.iterator();
	int j = 0;
	while ( i.hasNext()) {
		j++;
		Element x = (Element)i.next();
		Sp.p(j+": "+taskExplainer(x));
	}
	Sp.p("--- Print Element List: "+titel+" ---- END");	
	}
	
}
private TkTicketManager stackRotatorCatched() throws Exception {
	p("+++ beginning stackRotatorCatched())");
	// Element finishedByTk = null;
	try {
		TkTicketManager finishedByTk = stackRotator();
		if ( finishedByTk != null ) {
			//p("### stackRotator() returned: "+taskExplainerCompact(finishedByTk));
			//TkTicketManager x = new TkTicketManager(finishedByTk, 
			//		this,lc,this);
			
			
			return finishedByTk;			
		} else
			return null;

	} catch (FollowEndException ex) {
		p22("Job Stopped with reason: "+ex.getLocalizedMessage());
		return null;
		
	} catch (Exception ex) {
		Sp.p("ex="+ex.getLocalizedMessage());
		exceptionTracer.noteException(ex, logicStack,jobcalllist);
		
		// Vorangegangen loops mal rausl�schen
		p("### stackRotatorCatched() had exception: "+ex.getLocalizedMessage());
		printLogicStack("### Beginn to remove logic to ignore");
		LogicStackMember tryStackMember = null;
		if ( logicStack.isEmpty()) 
			tryStackMember = null;
		else
			tryStackMember = logicStack.peek();
		int k = 0;
		String collected_error_id = null;
		boolean thatsCatched = false;
		LogicStackMember catchedBy = null;
		while ( !thatsCatched && tryStackMember!=null && 
				tryStackMember.getType() != LogicStackMember.TYPE_TRY ) {
			// kwm01, kwm02 aus dieser Schleife muss rausgesprungen werden
			k++;
			tryStackMember = logicStack.pop();
			switch ( tryStackMember.getType() ) {
			case LogicStackMember.TYPE_WHEN: {	
				p("Cancelling WHEN due to exception: "+ex.getLocalizedMessage());
			    break;
			}
			case LogicStackMember.TYPE_CHOOSE: {	
				p("Cancelling CHOOSE due to exception: "+ex.getLocalizedMessage());
				break;
			}
			case LogicStackMember.TYPE_INCLUDE : {	
				p("Cancelling INCLUDE due to exception: "+ex.getLocalizedMessage());
				break;
			}
			case LogicStackMember.TYPE_CALL_LOCAL: {	
				p("Cancelling CALL of "+tryStackMember.getJobid()
				+" due to exception: "+ex.getLocalizedMessage());				
				jobcalllist.remove(jobcalllist.size()-1);
				break;
			}
			case LogicStackMember.TYPE_LOOP: {	
				// printLogicStack("At HEADER of loop "+k); 
				String loop_type = tryStackMember.getLoop_type();
				IInterLoop interLoop = tryStackMember.getInterLoop();
				p("Cancelling LOOP_"+loop_type+" id="+tryStackMember.getBaseElement().getAttributeValue("id")
						+" due to exception: "+ex.getLocalizedMessage());
				
				int lt = looptype_names.convert(loop_type);

				switch ( lt ) {
				case looptype_xml: {	
					boolean removed_ok = loopListXpath.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 a::::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_regex: {	
					boolean removed_ok = loopListRegex.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 b::::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_select: {	
					InterLoopSelect sOff = (InterLoopSelect)interLoop;
					sOff.close();
					boolean removed_ok = loopListSelect.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 b::::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_while: {	
					boolean removed_ok = loopListWhile.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 c::::: Remove not sucess "+interLoop);
					}
					break;
				}
				}  
				
				break;
			}
			default: {
				throw new Exception("Senior Mendes: Tenemos la mierda aqua, falta ayuda!");
			    
			}			
			}
			
			
			if ( Commons.hasFlag(tryStackMember.getBaseElement(),"catch" )) {
				thatsCatched = true;
				catchedBy = tryStackMember;
				Sp.p("Catched by:");
			} else if ( tryStackMember.getBaseElement().getAttribute("error_id") != null) {
				collected_error_id = tryStackMember.getBaseElement().getAttributeValue("error_id");
				p("-- Added collected_error_id from "+taskExplainerCompact(tryStackMember.getBaseElement()));
				p("-- Added collected_error_id from type="+tryStackMember.getType());
			}
			
			if ( thatsCatched || logicStack.isEmpty()) 
				tryStackMember = null;
			else
				tryStackMember = logicStack.peek();
			printLogicStack("At FOOTER of loop "+k);			
			
		}	
		
		printLogicStack("### End to remove logic to ignore");
		//
		if ( thatsCatched ) {
			int status = 0;
			while ( status < 2 && !stack.isEmpty()) {
				StackStep nextStackStep = stack.peek();
				LogicStackMember lsm = nextStackStep.getLsm();
				if ( lsm == catchedBy ) {
					stack.pop();
					status = 1;
				} else if ( status == 0 ) 
					stack.pop();
				else if ( status == 1)
					status = 2;
			}
			return null;
		} else if ( logicStack.isEmpty() ) {
			p("The LogicStack is now empty");
			throw ex;
		} else {
			if ( collected_error_id != null ) {
				p("!!!!! collected_error_id="+collected_error_id);
				ex = new CustomException(ex.getLocalizedMessage(),collected_error_id);
			} else
				p("!!!!! collected_error_id="+collected_error_id);
			tryStackMember = logicStack.pop();
			// if ( tryStackMember.getHashCode() != ) ... doch nicht
			if (tryStackMember.getType() != LogicStackMember.TYPE_TRY)
				throw new Exception("Programmierproblem 2492 with");
			// debugOn = true;
			// es gibt einen exception und einen catcher
			
			Element tryCatchElement = tryStackMember.getBaseElement();
			LogConnectorLogic lcl = tryStackMember.getLcl();
			
			String customExceptionClassName = CustomException.getClassName();
			
			// Restlichen elemente aus dem stack l�schen die eigentlich teil vom try sind
			// dh. so lange suchen bis das erste mc-try_catch-close gefunden wird
			boolean foundEndOfTry = false;
			while ( !foundEndOfTry && !stack.isEmpty()) {
				StackStep nextStackStep = stack.pop();
				Element nextStepToRun = nextStackStep.getStepToRun();
				if ( 	nextStepToRun.getName().equals("mc") &&
						nextStepToRun.getAttributeValue("task").equals("try_catch") &&
						nextStepToRun.getAttributeValue("act").equals("close") ) {
						foundEndOfTry = true;
						
				}
			}
			if ( devMode) {
				p("++++++++ Going to evaluate: "+taskExplainerCompact(tryCatchElement));
				p("try_catch catched: "+ex.getLocalizedMessage());
			}
			String idText = "";
			if ( ex.getClass().getName().equals(CustomException.getClassName())) {
				CustomException zzz = (CustomException)ex;
				if (zzz.getError_type()!=null)
				idText="id="+zzz.getError_type()+" ";
			}

			List<Element> catchList = tryCatchElement.getChildren("catch");
			Iterator<Element> icl = catchList.iterator();
			boolean catchFound = false;
			int cp = 0;
			while ( icl.hasNext() ) {
				Element cCatch = icl.next();
				
				// Element cCatch = translateJobelement( cCatchUntranslated,true,0,false );
				cp++;
				//hund
				String classes = null;
				if (cCatch.getAttribute("class")!=null )
					classes = translateValue(cCatch.getAttributeValue("class"));
				
				String error_types = null;
				if (cCatch.getAttribute("id")!=null )
					error_types = translateValue(cCatch.getAttributeValue("id"));
				
				p("-- looking at catch: "+taskExplainerCompact(cCatch));
				p("--        classes-1: "+classes);
				p("--    error_types-1: "+error_types);
				
				String cpn = new String("catch-block with NO arguments");
				if ( classes != null )
					cpn = "class='"+classes+"' ";
				if ( error_types != null )
					cpn = "id='"+error_types+"' ";
				if ( cpn.equals(""));
				else
					cpn=", "+cpn;
				
				//if ( debugOn)
				//	lcl.logic("("+cp+")", "Verifying"+cpn);
				if ( classes == null && error_types == null ) {
					// nothing defined, so we take everything
					classes = "java.lang.Exception";
					error_types = "*";
				} else if ( classes == null  ) {
					// only error_types is defined
					classes =  customExceptionClassName;
				} else if ( error_types == null ) {
					// only classes are defined
					error_types = "*";
				} else {
					// classes and error_types are defined
					classes = classes.concat(" "+customExceptionClassName);
				}	

				String[] classnames = (classes.trim()).split("\\s+");
				String[] errortypes = (error_types.trim()).split("\\s+");
				
				
				for ( String cClass: classnames ) {
					
					String dClass = cClass; 	
										
					if ( Class.forName(dClass).isInstance(ex)) {
					
						
						if ( dClass.equals(customExceptionClassName)) {
							
							// check error_type
							for ( String cType: errortypes ) {
								CustomException ce2 = (CustomException)ex;
								if ( cType.endsWith("*")) {
									int endpos = cType.indexOf("*");
									String starter = cType.substring(0, endpos);
									// lc.core("+++ starter="+starter,LogConnector.CORE_STRUCTURE);
									if ( starter.length() == 0)
										catchFound = true;
									else if ( ce2.getError_type() != null 
											&& ce2.getError_type().startsWith(starter))
										catchFound = true;
									
								} else if (ce2.getError_type() != null 
										&& ce2.getError_type().equals(cType)) {
										catchFound = true;
								}
							}
							
						} else {
							
							catchFound = true;
						}
						
						if ( catchFound ) {
							p("--- found the catch!!");
							if ( debugOn)
								lcl.logic("(x)", "This catch fits, entering catch-block ...");
							/// lc.core("try_catch: found the catch",LogConnector.CORE_STRUCTURE);

							{	// remove props
								ArrayList<Element> pl = new ArrayList<Element>();

								Element ct = new Element("remove");
								ct.setAttribute("id", "catched_id");	
								ct.setAttribute("type", "prop");
								ct.setAttribute("test","(f,has,prop,catched_id)");
								pl.add(ct);

								Element cc = new Element("remove");
								cc.setAttribute("id", "catched_class");	
								cc.setAttribute("type", "prop");	
								cc.setAttribute("test","(f,has,prop,catched_class)");
								pl.add(cc);

								Element cm = new Element("remove");
								cm.setAttribute("id", "catched_msg");	
								cm.setAttribute("type", "prop");
								cm.setAttribute("test","(f,has,prop,catched_msg)");
								pl.add(cm);			
								
								addToStackSimple(pl);									
							}							
							
							// -> after_catch
							/* */
							Element eAfterCatch = tryCatchElement.getChild("after_catch");
							if ( eAfterCatch != null ) {
								lcl.logic("(x)", "Entering after-catch-block ...");
								addToStackSimple(eAfterCatch.getChildren());
								lcl.logic("(y)", "Returned from after-catch-block."); 
							}
							
							// -> catch steps
							addToStackSimple(cCatch.getChildren());
							
							{
								// -> set error variables
								ArrayList<Element> pl = new ArrayList<Element>();
								// id and class
	
								if ( ex instanceof CustomException ) {
									if ( (((CustomException)ex).getError_type()) != null ) {
										Element ct = new Element("prop");
										ct.setAttribute("id", "catched_id");
										ct.setText(Entities.importEntities(((CustomException)ex).getError_type()));
										pl.add(ct); 									
									}
									
									Element cc = new Element("prop");
									cc.setAttribute("id", "catched_class");	
									cc.setText("CustomException");
									pl.add(cc);
	
								} else {
									Element cc = new Element("prop");
									cc.setAttribute("id", "catched_class");	
									cc.setText(Entities.importEntities(ex.getClass().getName()));
									pl.add(cc);											
								}
	
								// message 
								Element cm = new Element("prop");
								cm.setAttribute("id", "catched_msg");	
								String cmt = null;
								if ( ex.getLocalizedMessage() != null )
									cmt = ex.getLocalizedMessage();
								else if( ex.getMessage() != null )
									cmt = ex.getMessage();
								if ( cmt == null ) {
									Sp.p("KEINE MESSAGE von "+ex.getLocalizedMessage());
									Sp.pe(ex);
									cmt = "NO ERROR-MESSAGE FROM " +ex.toString();
								}
								if ( cmt!=null) {
									cm.setText(Entities.importEntities(cmt));
									pl.add(cm);	
								}
								addToStackSimple(pl);
							} // end of setting error-variables	
							printStack("After catched exception");

						} // end of if catch-found
					} // end of: if ( Class.forName(dClass).isInstance(ex)) {
					if ( catchFound ) break;
				}  // end of: for ( String cClass: classnames ) {
				if ( catchFound ) break;
				
			} // end of: while ( icl.hasNext() ) {
			
			
			if ( catchFound)
				return null;
			else
				throw ex;
		} // end of: tryStack.isEmpty() )throw ex; else {
	} // end of: } catch (Exception ex) {
} // end of method

private boolean canPrintTrace = false;
public void printTrace(String msg ) {

	if ( tracePrinter!=null ) {
		tracePrinter.println(msg);
	} else if ( lc.debugIsOn() ){
		Sp.p("TRACE: "+msg);
	}
	
}
public boolean canPrintTrace() {
	return this.canPrintTrace;
}

private TkTicketManager stackRotator() throws Exception {
	printStack("+++ beginning stackRotator())");
	// debugOn = CoreBuilder.rslogger.is_debug_plus; nic123 
	TkTicketManager finishedByTk = null;
	debugOn = lc.debugIsOn();
	// debugOn = true;
	while ( !stack.isEmpty() && finishedByTk==null) {
		StackStep stackStepToRun =stack.pop();
		Element elementToRun = stackStepToRun.getStepToRun();
		// p("// SEE // "+Commons.toTextCompact(elementToRun));
		

		String en = elementToRun.getName();
		boolean performedThisStep= false;
		int cmd = -1;

		if ( tracePrinter!=null ) {
			tracePrinter.println("// SEE // "+taskExplainerCompact(elementToRun));
		}		
		//
		if ( elementToRun.getAttribute("test") != null ) {
			String ueber= translateValueMy(elementToRun.getAttributeValue("test"),false);
			boolean testResult = Commons.stringtoBoolean(ueber, "*+02 Evaluating test argument");
			if ( testResult ) {
				if ( debugOn ) lc.debug_logic("*+03 Step will be exectuted by test: "+elementToText(elementToRun));
			} else {
				if ( debugOn ) lc.debug_logic("*+04 Step omitted by test: "+elementToText(elementToRun));
				performedThisStep= true;
			}
		}
		if ( !performedThisStep ) {
			/* das muss woanders hin
			if ( tracePrinter!=null ) {
				try {
					Element xxx = null;
					if ( elementToRun.getName().equals("call_service"))
						xxx= translateJobelement( elementToRun,true,-1,false );
					else
						xxx= translateJobelement( elementToRun,true,0,false );
					tracePrinter.println("       // "+Commons.toTextCompact(xxx));
				} catch (Exception e) {
					tracePrinter.println("       // Could not translate that!");
				}
			}	
			*/		
			Integer i_cmd = cmdm.get(en);
			
			if ( i_cmd != null) {
				cmd = i_cmd.intValue();

String loglabel = "|| "+en	;			
switch ( cmd ) {
	case c_when: {						
	
		if ( elementToRun.getAttribute("test") == null )
			throw new Exception("*+05 Missing testattribute for [when]-task: "+taskExplainerCompact(elementToRun));

		
		
		LogicStackMember lsm = new LogicStackMember(
				LogicStackMember.TYPE_WHEN,
				elementToRun,
				null);
		logicStack.push( lsm );
		Element mcMyEnd = new Element("mc");
		mcMyEnd.setAttribute("task","when");
		mcMyEnd.setAttribute("act","close");
		addToStackMc(mcMyEnd,lsm);		
		
		addToStackSimple( elementToRun.getChildren() );
		performedThisStep= true;
		break;
	}
	case c_choose: {
		logic_counter++;
		LogConnectorLogic lcl = new LogConnectorLogic(logic_counter+".choose");
		boolean oneChoosen = false;

		List<Element> selector = elementToRun.getChildren();
		Iterator<Element> si = selector.iterator();
		int pos=0;
		if ( debugOn ) lcl.logicStart("(a)","Starting (Number of possiblities="+selector.size()+") ...");
		
		while ( si.hasNext()) {
			pos++;
			boolean executeThis = false;
			Element whenOtherwise = si.next();
			String checklog = declareChoose+"test: "+whenOtherwise.getName();				
			
			if ( whenOtherwise.getName().equals("when") && whenOtherwise.getAttribute("test")!=null) {
				String original_test = whenOtherwise.getAttributeValue("test");
				checklog = checklog + " " + whenOtherwise.getAttributeValue("test");

				try {
					
					if ( debugOn ) lcl.logic("("+pos+")", "Verifying test-argument: "+original_test);
					
					String test_result = translateValue(whenOtherwise.getAttributeValue("test"));
					
					if ( debugOn ) lcl.logic("("+pos+")", "            test-result: "+test_result);
					
					Vcar vcar = new Vcar(test_result);
										
					if (vcar.getBool()) {
						executeThis=true;
						if ( debugOn ) lcl.logic("(x)", "This fits, going to execute the when-block ...");
					}
				} catch (Exception ex) {
					Sp.p("Error in Evaluating the test-argument: '"+whenOtherwise.getAttributeValue("test")+"'");
					throw new LangException(608,"Error in Evaluating the test-argument: "+whenOtherwise.getAttributeValue("test")+", Reason:"+ex.getLocalizedMessage());
				}
				
			} else if ( whenOtherwise.getName().equals("otherwise") ) {
				executeThis=true;
				if ( debugOn ) lcl.logic("(x)", "Otherwise fits, going to exectute the otherwise-block ...");
				
		    } else {
				throw new LangException(609,"Syntax error in choose: "+elementToText(whenOtherwise));
			}
			if ( executeThis ) {
				LogicStackMember lsm = new LogicStackMember(
						LogicStackMember.TYPE_CHOOSE,
						elementToRun,
						null);
				logicStack.push( lsm );
				
				Element mcMyEnd = new Element("mc");
				mcMyEnd.setAttribute("task","choose");
				mcMyEnd.setAttribute("act","close");
				addToStackMc(mcMyEnd,lsm);		
				

				
				addToStackSimple( whenOtherwise.getChildren() );
				/*
				List<Element> selector2=e.getChildren();
				Element whenOtherwise2 = selector2.get(pos-1);
				executeExecutionElements(whenOtherwise2.getChildren(),job);
				if ( debugOn)
					lcl.logic("(y)", "Returned from block-execution.");
					*/
				oneChoosen=true;
				break;
			}
		}
		/*
		if ( debugOn ) {
			if (!oneChoosen)
				lcl.logicEnd("(z)","Finished, NO block executed");
			else
				lcl.logicEnd("(z)","Finished, block executed");			
		}
		*/
		//
		performedThisStep= true;
		break;
	}
	case c_loop: {
		Element etE = translateJobelement( elementToRun,true,0,true );
		Entities.exportEntitiesAttributesOnly(etE);
		if ( etE.getAttribute("xpath") != null ) {
			// et is Natural!
			LogConnectorLogic lcc = new LogConnectorLogic(logic_counter+".LoopXml");
			
			logic_counter++;
			InterLoopXml neu = new InterLoopXml ( 
					etE,
		
					lcc,
					this );
			
			LogicStackMember lsm = new LogicStackMember(
					LogicStackMember.TYPE_LOOP,
					elementToRun,
					lcc);
			lsm.setLoopOptions("xml", neu);
			lsm.setHashCode(System.identityHashCode(neu));
			logicStack.push( lsm );
			
			Element mcTryStart = new Element("mc");
			mcTryStart.setAttribute("task","loop");
			mcTryStart.setAttribute("loop_type","xml");
			mcTryStart.setAttribute("act","close");
			addToStackLoop(mcTryStart, elementToRun, lcc,neu,lsm);
			loopListXpath.add(neu);		
						
		} else if ( etE.getAttribute("select") != null ||  etE.getAttribute("select_file") != null) {
			

			LogConnectorLogic lcc = new LogConnectorLogic(logic_counter+".LoopXml");
			
			logic_counter++;
			InterLoopSelect neu = new InterLoopSelect ( 
					etE , 
					lc,
					new LogConnectorLogic(logic_counter+".LoopSelect"),
					this);
			
			LogicStackMember lsm = new LogicStackMember(
					LogicStackMember.TYPE_LOOP,
					elementToRun,
					lcc);
			
			lsm.setLoopOptions("select", neu);
			lsm.setHashCode(System.identityHashCode(neu));
			logicStack.push( lsm );
			Element mcTryStart = new Element("mc");
			mcTryStart.setAttribute("task","loop");
			mcTryStart.setAttribute("loop_type","select");
			mcTryStart.setAttribute("act","close");
			addToStackLoop(mcTryStart, elementToRun, lcc,neu,lsm);
			loopListSelect.add(neu);
				
		} else 	if ( etE.getAttribute("regex") != null ) {
			// et is Natural!
			LogConnectorLogic lcc = new LogConnectorLogic(logic_counter+".LoopRegex");
			
			logic_counter++;
			InterLoopRegex neu = new InterLoopRegex ( 
					etE,
				
					lcc,isitzung );

			LogicStackMember lsm = new LogicStackMember(
					LogicStackMember.TYPE_LOOP,
					elementToRun,
					lcc);
			lsm.setLoopOptions("regex", neu);
			lsm.setHashCode(System.identityHashCode(neu));
			logicStack.push( lsm );
			
			Element mcTryStart = new Element("mc");
			mcTryStart.setAttribute("task","loop");
			mcTryStart.setAttribute("loop_type","regex");
			mcTryStart.setAttribute("act","close");
			addToStackLoop(mcTryStart, elementToRun, lcc,neu,lsm);
			loopListRegex.add(neu);		
			

			
		} else 	if ( etE.getAttribute("while") != null ) {
			// et is Natural!
			LogConnectorLogic lcc = new LogConnectorLogic(logic_counter+".LoopWhile");
			
			logic_counter++;
			
			InterLoopWhile neu = new InterLoopWhile ( 
					etE,
					this,
					this,
				
					lcc);			
			LogicStackMember lsm = new LogicStackMember(
					LogicStackMember.TYPE_LOOP,
					elementToRun,
					lcc);
			lsm.setLoopOptions("while", neu);
			lsm.setHashCode(System.identityHashCode(neu));
			logicStack.push( lsm );
			
			Element mcTryStart = new Element("mc");
			mcTryStart.setAttribute("task","loop");
			mcTryStart.setAttribute("loop_type","while");
			mcTryStart.setAttribute("act","close");
			addToStackLoop(mcTryStart, elementToRun, lcc,neu,lsm);
			loopListWhile.add(neu);		
			

			
		}  else {
			throw new LangException(607,"Unknown loop-specification, use keywords xml, while or regex or select or select_file in: "+elementToText(etE));
		}
		
		performedThisStep= true;
		break;
	}
	case c_try_catch: {
		logic_counter++;
		int my_try_catch_counter = logic_counter;
		LogConnectorLogic lcl = new LogConnectorLogic(my_try_catch_counter+".try_catch");
		// ?whatfore? isStepDomain = true;
		Element etry = elementToRun.getChild("try");
		// ?whatfore? 
		 
		// ?whatfore? int callnumbers = jobcalllist.size();
		// ?whatfore? int fatalsize = lc.getFatalSize();
		
		// end of try-catch
		// tryStack.push(elementToRun);
		LogicStackMember lsm = new LogicStackMember(
				LogicStackMember.TYPE_TRY,
				elementToRun,
				lcl);
		lsm.setHashCode(System.identityHashCode(elementToRun));
		logicStack.push(lsm );		
		
		Element mcTryStart = new Element("mc");
		mcTryStart.setAttribute("task","try_catch");
		mcTryStart.setAttribute("act","close");
		addToStackTryCatch(mcTryStart, elementToRun, lcl,lsm);
		
		// vom try
		if ( etry.getChildren().size() > 0)
			addToStackSimple( etry.getChildren() );
		
		// begin of try-catch
		Element mcTryEnd = new Element("mc");
		mcTryEnd.setAttribute("task","try_catch");
		mcTryEnd.setAttribute("act","open");
		addToStackTryCatch(mcTryEnd, elementToRun, lcl,lsm);		
		//

		if ( debugOn)
			lcl.logicStart("(a)", "Started.");
		//
		printStack("After try-catch of ="+taskExplainer(elementToRun));
		/* to do somewhere
		 if ( debugOn ) {
			 lcl.logicEnd("(z)","Finished well, no Exception ");
		 }
		*/
		
		
		//
		 
		performedThisStep= true;
		break;
	}	
	case c_include: {
		if ( elementToRun.getAttribute("file") == null )
			throw new LangException("*+08 Missing attribute 'file' for include-task: "+taskExplainerCompact(elementToRun));
		String fn_natural = Entities.exportEntities(translateValue(elementToRun.getAttributeValue("file")));
		

		boolean ganzneu = false; /* nic123 kommt noch*/
		FileObject fo_natural = CommonsVfs.resolveFileProcess( fn_natural,isitzung);
		isitzung.checkMaxFileMemory(fo_natural);
		
		if ( CommonsVfs.getTimeOfFileObjectForProcessing(fo_natural,isitzung) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}	
		// wo steht das temp
		FileName fo_name = fo_natural.getName();			
		if ( ganzneu ) {
		
			if ( ! ((fo_name.getURI()).startsWith(tempFileManger.requestMemTempDir()))) {
				// fangt nicht mit temp an
				throw new LangException("include works only in temp-dir: file="+fo_name.getURI());			
			}
			if ( fo_name.getExtension() == null ) {
			} else if ( fo_name.getExtension().length() > 0 ) {
				throw new LangException("include works not with extension in filename, extension is: "+
						fo_name.getExtension()+", file="+fo_name.getURI());
			}		
			fo_natural.close();
		}
		
		

		LogicStackMember lsm = new LogicStackMember(
				LogicStackMember.TYPE_INCLUDE,
				elementToRun,
				null);
		logicStack.push( lsm );
		
		Element mcMyEnd = new Element("mc");
		mcMyEnd.setAttribute("task","include");
		mcMyEnd.setAttribute("act","close");
		addToStackMc(mcMyEnd,lsm);		
		
		Element root = CommonsVfs.getElementByFilename(fn_natural,isitzung);
		addToStackSimple( root.getChildren() );	
		
		
		/* nic123 kommt noch das file rausloesen */	
		if ( ganzneu ) {
			fo_natural.delete();
		} 
		performedThisStep= true;
		break;
	}
	case c_exclude: {
		//System.out.println("-ssssssssssssssssssssssss-- EXCLUDE BEGIN --- ");
		if ( elementToRun.getAttribute("file") == null )
			throw new Exception("*+08 Missing attribute 'file' for exclude-task: "+taskExplainerCompact(elementToRun));
		
		if ( elementToRun.getAttribute("regex") == null ) {
			String mymsg="wichtig Missing attribute 'regex' for exclude-task: "+taskExplainerCompact(elementToRun);
			//System.out.println(mymsg);
			Sp.pAlarmMaybeExcludeAttac(mymsg);
			throw new Exception(mymsg);
		}
		//System.out.println("-ss  1 ");
		String regex2 = elementToRun.getAttributeValue("regex");
		String regex = Entities.exportEntities(translateValue(elementToRun.getAttributeValue("regex")));
		
		String fn_natural = Entities.exportEntities(translateValue(elementToRun.getAttributeValue("file")));
		Element all = CommonsVfs.getElementByFilename(fn_natural,isitzung);
		List<Element> childs = all.getChildren();
		Iterator<Element> iChilds = childs.iterator();
		//System.out.println("-ss  2 ");
		while ( iChilds.hasNext() ) {
			Element akt = (Element)iChilds.next();
			String aktEingang = taskExplainerCompact(akt);
			String type = akt.getName();
			String id = akt.getAttributeValue("id");
			String vorText = akt.getText();
			//System.out.println("-ss    3 ");
			String derInhalt = c_translate(akt.getText(),"exclude-file=["+fn_natural+"] regex=["+regex+"]",true);
			//System.out.println("-ss    4 ");
			
			if ( akt.getAttribute("x") != null )  {
				akt.removeAttribute("x");
			}
			//System.out.println("-ss    5 ");
			akt.setText(derInhalt);
			//System.out.println("-ss    6 ");
			if ( Commons.stringMatchesRegex(id, regex)) {

				// kwm03
				int et = etyp_names.convert(type);

				switch ( et ) {
				case etyp_p: {	
					addProp(akt,false);
					break;
				}
				case etyp_f: {	
					addFile(akt,false);
					break;
				}
				case etyp_d: {	
					addDir(akt,false);
					break;
				}
				}
				
			}else {
				//elementToRun
				//System.out.println("-ss    8 ");
				String errorString = "";
				
				Sp.pAlarmExclude(
						
						"the 'id' does not match the 'regex' id=["+id +"] regex=["+regex+"] "+" type=["+type+"]"+
						" newValue=["+akt.getText()+"]" +
							
							" file=["+fn_natural+"]");
				
				/*
				Sp.pAlarmExclude(
						
					" neuer inhalt [ "+	Commons.toTextCompact(akt) + " ] of  alter inhalt [ " +aktEingang + " ] " +
						Commons.toTextCompact(all) + " ] from [ " +
						Commons.toTextCompact(elementToRun) + " ] " + 
						
						" file="+fn_natural);
				*/			
			}
		}
		//System.out.println("-ssssssssssssssssssssssss-- EXCLUDE END --- ");
		performedThisStep= true;
		break;
	}	
	case c_rollback :
	case c_commit: {
		 // calls a service out of an application
		Sp.p("commit Calling service for instdir="+instdir+", pro="+instid);		
		if ( getGuiController().isSocalledWeb() ) {
			

			int para_silent = SILENCE_YES;		
							
			String scJobE;
			if ( cmd==c_commit)
				scJobE = Entities.importEntities(surrounding.proDbCommitJob);
			else
				scJobE = Entities.importEntities(surrounding.proDbRollbackJob);
			// flex zusammenstellen
			Element flex = new Element("config");
		
			// und was sonst noch so angegeben ist
			//String theProjectDirName = guiController.prepareProjectForServices();
			addElementsForProjectDb(flex, instid, instdir);
			
			// System.out.println("theProjectDirName="+theProjectDirName);
			
			if ( elementToRun.getAttribute("path") == null)
				throw new Exception("The step '"+en+"' needs attribute @path");
			Element ec = new Element("prop");
			ec.setAttribute("id", "db_path");
			ec.setAttribute("x", Entities.exportEntities(translateValue(elementToRun.getAttributeValue("path"))));
			flex.addContent(ec);
			
			
						

			//asdf
			
			//
			XrRqRpTk dothis = performSubrunner(
					scJobE,true, appid, instid, instdir,
					flex,null,para_silent,null);
			if ( dothis != null ) {
				finishedByTk = dothis.ticketManager;					
			}
			performedThisStep= true;			
		} else {
			
		}

		break;			
	}

	case c_call_job :
	case c_call_service :
	case c_call_pro :
	case c_call_app :  {		
		boolean pm = false;
		performedThisStep = true;
		// terminator.write("bin in irgendeinem call: "+en);
		
		Element etE = translateJobelement( elementToRun,true,-1,true ); 
		if ( ( cmd==c_call_job && !isSystemcall) || (cmd==c_call_service && isSystemcall)) {
			
			if ( debugOn) {
			 terminator.write("CALL-VARIANTE-1: "+en);
			 //terminator.write("  ",Commons.toTextCompact(elementToRun));
			 terminator.write("  ",taskExplainerCompact(etE)+"  == "+taskExplainerCompact(elementToRun));
			}
			if ( elementToRun.getAttribute("job") == null)
				throw new Exception("The step '"+en+"' needs attribute job");
			
			String job_id = Entities.exportEntities(translateValue(elementToRun.getAttributeValue("job")));			
			printStack("Call job-BEGIN: '"+job_id+"' which is service '"+isSystemcall+"'",elementToRun);	
			DbJob nextJob = jobtodo.getJm().selectJob(job_id);
			
			if ( nextJob == null ) {
				throw new Exception("The job '"+job_id+"' was not found in the dir '"+jobtodo.getJm().getDir()+"'");
			}
			
			// String logid = job_id;
			
			// for ti-repoerting
			jobcalllist.add(nextJob);

			LogicStackMember lsm =new LogicStackMember(
					LogicStackMember.TYPE_CALL_LOCAL,
					elementToRun,
					null);
			
			lsm.setCallInnerOptions(job_id);
			lsm.setHashCode(System.identityHashCode(nextJob));
			logicStack.push( lsm );
			
			// close um jobcalllist remove machen zu k�nnen
			Element mcCallClose = new Element("mc");
			mcCallClose.setAttribute("task","call_local");
			mcCallClose.setAttribute("act","close");
			if ( elementToRun.getAttribute("reason")!= null )
				mcCallClose.setAttribute("reason",elementToRun.getAttributeValue("reason"));
			addToStackCallInner(mcCallClose,lsm);
			

			
			// der eigentliche job
			ArrayList<Element>	list = getExecutionElements(nextJob);
			if ( list.size() > 0)
				addToStackSimple( list );

			// job parameter
			if ( elementToRun.getChildren().size() > 0)
				addToStackSimple( elementToRun.getChildren() );			
			
			//

			
			printStack("Call job-END: '"+job_id+"' which is service '"+isSystemcall+"'");	
												
			performedThisStep= true;
			break;
		} else if ( cmd==c_call_job && isSystemcall ) {
			// terminator.write("+++ cmd==c_call_job && isSystemcall ++++");
			if ( debugOn) {
				terminator.write("CALL-VARIANTE-2: "+en+" "+taskExplainer(etE));
			}
			
			// p22("+++ cmd==c_call_job && isSystemcall ++++");
			String name = "'call_job'";
			String ap = "task "+name+" says: ";
			
			// parameter lesen und pruefen 
			String jobidE = etE.getAttributeValue("job");
			if (jobidE==null) 
				throw new LangException(611,ap+"I want an attribute 'job' which specifies the job");
			String jobidN = Entities.exportEntities(jobidE);
			
			String app_to_useE = etE.getAttributeValue("app");
			if (app_to_useE==null) 
				throw new LangException(612,ap+"I want an attribute 'app' which specifies the application");
			String app_to_useN = Entities.exportEntities(app_to_useE);
			DbApp requested_app = CoreBuilder.db.getAppById(app_to_useN);
			if ( requested_app == null )
				throw new LangException(613,ap+"The app '"+app_to_useN+"' does not extist");
						
			String inst_to_useE = etE.getAttributeValue("pro");
			String inst_to_useN = null;
			
			if ( inst_to_useE != null ) {
				inst_to_useN = Entities.exportEntities(inst_to_useE);
				DbPro requested_pro = requested_app.getInstById(inst_to_useN);
				if ( requested_pro == null )
					throw new LangException(614,ap+"The pro '"+inst_to_useN+"' of app '"+app_to_useN+"' does not extist");
				
			}

			// Job Aufrufen	

			String loglabel2 = "|| call_job ";
			String logid2 = jobidE;
			
			int flag_silence = Interpreter.SILENCE_NO;
			if ( Commons.hasFlag(etE, "silent"))
				flag_silence = Interpreter.SILENCE_YES;

			
			XrRqRpTk dothis = performSubrunner(
					jobidN, 
					false, 
					app_to_useN, 
					inst_to_useN, 
					null, 
					null,
					null,
					flag_silence,null);
			if ( dothis != null ) {
				finishedByTk = dothis.ticketManager;					
			}
			
			performedThisStep= true;
			break;		
			
		} else if ( cmd==c_call_service) {
			 // calls a service out of an application

			Sp.p("Calling service for app="+appid+", pro="+instid);
			String scInstN = instid;
			String scInstDirN = instdir;
			String scAppN  = appid;

			if ( etE.getAttributeValue("pro")!=null && etE.getAttributeValue("app")!=null ) {
				Sp.pd("Calling service A");
				scAppN  = Entities.exportEntities(etE.getAttributeValue("app"));
				scInstN = Entities.exportEntities(etE.getAttributeValue("pro"));
				scInstDirN = null;
			} else if ( etE.getAttributeValue("app")!=null ) {
				Sp.pd("Calling service B");
				scAppN  = Entities.exportEntities(etE.getAttributeValue("app"));
				scInstN = null;
				scInstDirN = null;
			} else if ( etE.getAttributeValue("pro")!=null ) {
				Sp.pd("Calling service C");
				scInstN = Entities.exportEntities(etE.getAttributeValue("pro"));
				scInstDirN = null;
			} else {
				Sp.pl("Calling service D");
			}
			//if ( debugOn) {
			Sp.pl("Calling service D "+" VARIANTE 4 bin im call_service "+
						  "app="+scAppN +
						  ", pro="+scInstN +
						  ", proDir="+scInstDirN + ", "+
						  taskExplainerCompact(etE));
			//	} 			
			int para_silent = SILENCE_NO;
			if ( Commons.hasFlag(etE, "silent"))
				para_silent = SILENCE_YES;			
							
			String scJobE  = etE.getAttributeValue("job");
			if ( scJobE == null)
				throw new LangException(615,"When using 'call_service', the attribute 'job' must be specified!");
			
			// flex zusammenstellen
			Element flex = new Element("config");
		
			// und was sonst noch so angegeben ist
			flex.addContent(etE.cloneContent());
			Sp.pl("Calling service E FLEX="+taskExplainer(flex));
			XrRqRpTk dothis = performSubrunner(
					scJobE, true, scAppN, scInstN, scInstDirN,
					flex,null,para_silent,null);
			if ( dothis != null ) {
				finishedByTk = dothis.ticketManager;					
			}
			performedThisStep= true;
			break;	
		} else if ( cmd==c_call_app ) {
			String ap = "task 'call_app' says: ";
			if ( debugOn) {
			terminator.write("CALL-VARIANTE-3: "+en+" "+taskExplainerCompact(etE));
			}
			// parameter lesen und pruefen
			String appidE = etE.getAttributeValue("app");
			String jobidE = etE.getAttributeValue("job");
			String inst_dirE = etE.getAttributeValue("pro_dir");
			String inst_idE = etE.getAttributeValue("pro_id");
			
			if (appidE==null) throw new LangException(622,ap+"I want an attribute 'app' which specifies the application");
			if (inst_dirE==null) throw new LangException(623,ap+"I want an attribute 'pro_dir' which specifies the temporary project-dir");
			if (inst_idE==null) throw new LangException(624,ap+"I want an attribute 'pro_id' which specifies the id of the temporary project");
			if (jobidE==null) throw new LangException(625,ap+"I want an attribute 'job' which specifies the job");
			
			int para_silent = SILENCE_NO;
			if ( Commons.hasFlag(etE, "silent"))
				para_silent = SILENCE_YES;
			
 
			String logid = "app="+
							appidE+
							" pro="+
							inst_idE+
							" prodir="+
							inst_dirE+
							" job="+
							jobidE;
			
			// Job Aufrufen	 

			Element flex = null;
			Element flexEnd = null;
			
			// flex zusammenstellen
			if ( etE.getChildren().size() == 2 ) {
				// vielleicht sinds begin und end
				if ( (etE.getChildren().get(0)).getName().equals("begin") &&
					 (etE.getChildren().get(1)).getName().equals("end")	) {
					// es gibt ein begin und ein ende
					flex = new Element("config");
					flex.addContent((etE.getChildren().get(0)).cloneContent());
					flexEnd = new Element("config");
					flexEnd.addContent((etE.getChildren().get(1)).cloneContent());
				} else {
					flex = new Element("config");
					flex.addContent(etE.cloneContent());
				}
				
			} else if ( etE.getChildren().size() == 1 ) {
				// vielleicht sinds begin und end
				if ( (etE.getChildren().get(0)).getName().equals("begin") ) {
					// nur ein begin
					flex = new Element("config");
					flex.addContent((etE.getChildren().get(0)).cloneContent());
				} else if ( (etE.getChildren().get(0)).getName().equals("end") ) {
					// nur ein ende
					flexEnd = new Element("config");
					flexEnd.addContent((etE.getChildren().get(0)).cloneContent());
				} else {
					// weder begin noch ende, alles begin
					flex = new Element("config");
					flex.addContent(etE.cloneContent());
				}
				
			} else {
				flex = new Element("config");
				flex.addContent(etE.cloneContent());
			}
			///
			String appId = Entities.exportEntities(appidE);
			String jobId = Entities.exportEntities(jobidE);			
			String instId = Entities.exportEntities(inst_idE);
			String instDir = Entities.exportEntities(inst_dirE);
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appId, 
					instId, 
					instDir,
					true,
					isitzung);
			Sp.p("Interpreter adds ExecutionPrologUsage app="+appId+", job="+jobId
					+", inst="+instId+", instDir="+instDir);
			printElementList(configListx,"Interpreter Adds");


			
			DbApp appx = CoreBuilder.db.getAppById(appId);
			if ( appx == null )
				throw new Exception("The app '"+appId+"' was not found.");
			
			DbJob jobx = appx.getJm().selectJob(jobId);
			if ( jobx == null )
				throw new Exception("The job '"+jobId+"' could not be selected.");		

			///
			Sp.p("insdir="+Entities.exportEntities(inst_dirE));
			
			if ( Commons.hasFlag(etE, "fork") ) {
				
				AppJobexecutionIncludeFork ajeFork = 
						new AppJobexecutionIncludeFork(
								appId,
								instId,
								jobId,
								flex,
								false,
								para_silent,
								terminator,
								call_depth+1,
								instDir,
								guiController,
								isitzung,
								flexEnd,
								false,
								ignoreClassloading,
								surrounding);
				ajeFork.run();
				
			} else {
				XrRqRpTk dothis = performSubrunner(
						jobId, 
						false,
						appId, 
						instId, 
						instDir,
						flex,flexEnd,para_silent, configListx);
				if ( dothis != null ) {
					finishedByTk = dothis.ticketManager;					
				}				
			}

			performedThisStep= true;
			break;				

		} else {
			terminator.write("??? call-variante nicht abgefangen" +en);
			throw new Exception("problem mit "+en);
			
		}
		//terminator.write("bin in irgendeinem ENDE call: "+en);
		//break;
	}
	case c_tk: {
		performedThisStep= true;
		finishedByTk = new TkTicketManager(elementToRun,this,lc,this);
		break;
	}
	case c_mc: {
		p("### performing mc: "+taskExplainerCompact(elementToRun));
		String task = elementToRun.getAttributeValue("task");
		String act = elementToRun.getAttributeValue("act");
		if ( task==null)
			throw new Exception("Some very heav problem with mistery calls: no task");
		if ( act==null)
			throw new Exception("Some very heav problem with mistery calls: no act");		
		
		// kwm4 
		int ac = kwmc_names.convert(task);

		switch ( ac ) {
		case kwmc_when: {	
			LogicStackMember lsm = logicStack.pop();
			if ( lsm.getType() != LogicStackMember.TYPE_WHEN) {
				printLogicStackMember("Programmiererproblem 1235", lsm);
				throw new Exception("Removed Stack member should be a 'when' but isnt");
			}
			break;
		}
		case kwmc_choose: {	
			LogicStackMember lsm = logicStack.pop();
			if ( lsm.getType() != LogicStackMember.TYPE_CHOOSE) {
				printLogicStackMember("Programmiererproblem 946315", lsm);
				throw new Exception("Removed Stack member should be a 'choose' but isnt");
			}
			break;
		}
		case kwmc_include: {	
			LogicStackMember lsm = logicStack.pop();
			if ( lsm.getType() != LogicStackMember.TYPE_INCLUDE) {
				printLogicStackMember("Programmiererproblem 1185", lsm);
				throw new Exception("Removed Stack member should be a 'include' but isnt");
			}
			break;
		}
		case kwmc_try_catch: {	
			 if ( act.equals("close")) {
				LogicStackMember lsm = logicStack.pop();
				if ( lsm.getType() != LogicStackMember.TYPE_TRY ) {
					printLogicStackMember("��2������ Problem", lsm);
					throw new Exception("Removed Stack member should be a 'try' but isnt");
				}				
				if ( debugOn)
					stackStepToRun.getLcl().logicEnd("(z)", "Finished.");
			} //else if ( act.equals("open")) {
				/*
				nic123
				logicStack.push( new LogicStackMember(LogicStackMember.TYPE_TRY,
														stackStepToRun.getBaseElement(),
														stackStepToRun.getLcl()));
				if ( debugOn)
					stackStepToRun.getLcl().logicStart("(a)", "Started.");	
				*/			
			//}
			break;
		}
		case kwmc_loop: {	
			String loop_type = elementToRun.getAttributeValue("loop_type");
			IInterLoop interLoop = stackStepToRun.getInterLoop();
			Element loopElement = stackStepToRun.getBaseElement();
			if ( interLoop.hasNext()) {
				// continue with next iteration
				stack.push(stackStepToRun);
				interLoop.next();
				addToStackSimple(loopElement.getChildren());
				printStack("Next iteration of loop: "+loopElement.getAttributeValue("id"));
			} else {
				LogicStackMember lsm = logicStack.pop();
				int offHashCode = System.identityHashCode(interLoop);
				if ( offHashCode != lsm.getHashCode())
					throw new Exception("Programmierfehler 38123");
				
				int lt = looptype_names.convert(loop_type);

				switch ( lt ) {
				case looptype_xml: {	
					boolean removed_ok = loopListXpath.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 :a:::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_regex: {	
					boolean removed_ok = loopListRegex.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 :b:::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_select: {	
					boolean removed_ok = loopListSelect.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 :b:::: Remove not sucess "+interLoop);
					}
					break;
				}
				case looptype_while: {	
					boolean removed_ok = loopListWhile.remove(interLoop);	
					if ( !removed_ok ) {
						throw new Exception("@18 :c:::: Remove not sucess "+interLoop);
					}
					break;
				}
				}  
				
				
				printLogicStack("Bfore ����");
				
				if ( lsm.getType() != LogicStackMember.TYPE_LOOP ) {
					printLogicStackMember("Problem", lsm);
					throw new Exception("Removed Stack member should be 'loop' but isnt");
				}
			}
			break;
		}
		case kwmc_call_local: {	
LogicStackMember lsm = logicStack.pop();
			
			DbJob off = jobcalllist.get(jobcalllist.size()-1);
			int offHash = System.identityHashCode(off);
			if ( lsm.getHashCode() != offHash )
				throw new Exception("Programmierfehler 8432");
			
			jobcalllist.remove(jobcalllist.size()-1);
			
			if ( lsm.getType() != LogicStackMember.TYPE_CALL_LOCAL) {
				printLogicStackMember("��3������ Problem", lsm);
				throw new Exception("Removed Stack member should be a 'call_local' but isnt");
			}
			if ( elementToRun.getAttribute("reason") != null) {
				throw new FollowEndException(elementToRun.getAttributeValue("reason"));
			}
			break;
		}
		}  
		
				
		performedThisStep= true;
		break;
	} // end of: case
} // end of: switch
			}	// end of: if ( i_cmd != null ) 		
		} // end of: if ( !performedThisStep) 
		//
		

		
		if ( !performedThisStep) {
			// sicherheitsstufe
			// ein normaler step, den wie bisher ausf�hren lassen
			//if ( debugOn) 
			//p22(">> RUN >> "+taskExplainerCompact(elementToRun));
			
			if ( tracePrinter!=null ) tracePrinter.println(">> RUN >> "+taskExplainerCompact(elementToRun));
			executeJobelementFinalCatched(elementToRun, cmd);			
		}
	}
	return finishedByTk;
}


private XrRqRpTk performSubrunner(
		String sjobid ,
		boolean sisServiceCall,
		String sappId,
		String sproId,
		String sproDir,
		Element flex,
		Element flexElementEnd,
		int ssilence,
		ArrayList<Element> configList
		
		) throws Exception {
	XrRqRpTk x = null;
	try {
		x = performSubrunner2(
				 sjobid ,
				 sisServiceCall,
				 sappId,
				 sproId,
				 sproDir,
				 flex,
				 flexElementEnd,
				 ssilence ,
				 configList);
		return x;
	} catch (Exception e) {
		if ( sjobid.equals("??") || sjobid.equals("?") )
			return null;
		else
			throw e;
	}
	// return null;
}
private XrRqRpTk performSubrunner2(
		String sjobid ,
		boolean sisServiceCall,
		String sappId,
		String sproId,
		String sproDir,
		Element flex,
		Element flexElementEnd,
		int ssilence,
		ArrayList<Element> xxconfigList
		) throws Exception {
	// terminator.write("Subrunner2 pro="+sproId+", prodir="+sproDir);
	if ( sproId == null && sproDir !=null ) {
		throw new Exception("performSubrunner2: sproDir="+sproDir+", but no proId!!");
	}
	DbJob jobx = null;
	if ( sisServiceCall ) 
		jobx = CoreBuilder.db.getScJm().selectJob(sjobid);
	else
		jobx = CoreBuilder.db.getAppById(sappId).getJm().selectJob(sjobid);
	if ( jobx == null )
		throw new Exception("The job '"+sjobid+"' could not be selected.");	
	int my_call_depth = call_depth+1;
	// call_depth++;
	Interpreter myinterpreter = new Interpreter ( 
			jobx,
			sappId, 
			sproId,
			sisServiceCall,
			ssilence,
			my_call_depth, // my_call_depth,
			flex,
			sproDir,
			this.guiController,
			this.isitzung);
	myinterpreter.setInterpreterShouldGiveBackTk(interpreterShouldGiveBackTk);
	myinterpreter.setInterpreterShouldIgnoreTk(interpreterShouldIgnoreTk);
	
	myinterpreter.setIgnoreClassloading(ignoreClassloading);
	myinterpreter.setGuiController(guiController);
	myinterpreter.setSurrounder(subSurrounding);
	myinterpreter.setSitzung(this.isitzung);
	
	// if ( call_depth > 1 )
	// 	myinterpreter.setIncludeHeaderAndFooter ( false );
	// myinterpreter.setWebModus(this.webModus);

	if ( flexElementEnd != null ) {
		myinterpreter.setFlexEndElement(flexElementEnd);
	}
	
	
   	
   	if ( xxconfigList != null && xxconfigList.size() > 0  ) {
   		myinterpreter.addConfigList(xxconfigList);
   	} else {
   		ArrayList<Element> sconfigList=null;
   		try {
   			Sp.p("######### bin im abrufen CoreBuilder.db.getExecutionPrologAnonym");
   			// sconfigList = CoreBuilder.db.getExecutionProlog(sappId, sproId,true,sisServiceCall);
   			sconfigList = CoreBuilder.db.getExecutionPrologAnonym(sappId, sproId, sproDir, true, sisServiceCall,
   					isitzung);

   		} catch (Exception e) {
   			
   		}
   	   	myinterpreter.addConfigList(sconfigList);  		
   	}
   	myinterpreter.setTerminatorBeforeRun(terminator);
   	// terminator.write(mycalldepth+"."+"1) Starting from subrunner2: "+myinterpreter.getDescriptionString());
   	String towrite ="Starting from SubRunner2 with depth="+call_depth
   			+", job="+jobx.getJobid()
   			+", service="+sisServiceCall
   			+", silence="+getSilenceText(ssilence)
   			+", app="+sappId
   			+", pro="+sproId
   			+", proDir="+sproDir;
   	if ( debugOn) 
   	terminator.write(towrite);
   			
   	

	myinterpreter.run();
	
	XrRqRp frommy = myinterpreter.getRqRpAfterRunning();
	if ( frommy instanceof XrRqRpEx ) {
		if ( debugOn )
		terminator.write("SubRunner2 Returned with Exception from Subinterpreter "+myinterpreter.getDescriptionString());
		XrRqRpEx frommy2 = (XrRqRpEx)frommy;
		
		Exception tothrow = new Exception(
				"From call "+myinterpreter.getDescriptionStringSmall()
		+": " +frommy2.getException().getLocalizedMessage());
		
		myinterpreter.destroy();
		throw frommy2.getException();
	} else if ( frommy instanceof XrRqRpTi ) {
		XrRqRpTi frommy2 = (XrRqRpTi)frommy;
		//frommy2.getTerminator().write(mycalldepth+"."+"2) Returned with Ti from subinterpreter "
		// +myinterpreter.getDescriptionStringSmall());

		// myinterpreter.destroy();
		return null;
		// throw tothrow;
	} else if ( frommy instanceof XrRqRpTk ) {
		XrRqRpTk ttk = (XrRqRpTk)frommy;
		if ( debugOn )
		terminator.write("depth="+call_depth+" "+"SubRunner2 Returned with Tk : "+
		taskExplainerCompact(ttk.getTranslatedForPrint())+
		".... from subinterpreter "+myinterpreter.getDescriptionStringSmall());
		terminator.flush();
		// muss vermerkt werden
		subInterprterToFinish = myinterpreter;
		return ttk;
		
	}
	
	return null;
}

private void addToStackDebug( String info ) {
	Element e = new Element("ti");
	e.setText(info);
	StackStep add = new StackStep( e,null,null,null,null);
	stack.push(add);

}

private void addToStackPlain( Element e) {

	StackStep add = new StackStep( e,null,null,null,null);
	stack.push(add);

}

private void addToStackMc( Element e, LogicStackMember lsm ) {

	StackStep add = new StackStep( e,null,null,null,lsm);
	stack.push(add);

}

private void addToStackTryCatch( Element elementToRun,Element tryCatchElement, 
		LogConnectorLogic lcl,
		LogicStackMember lsm) {

	StackStep add = new StackStep( elementToRun,tryCatchElement,lcl, null,lsm);
	stack.push(add);

}

private void addToStackCallInner( Element elementToRun ,
		LogicStackMember lsm) {

	StackStep add = new StackStep( elementToRun, null,null, null,lsm);
	stack.push(add);

}

private void addToStackLoop(Element elementToRun,
							Element tryCatchElement, 
							LogConnectorLogic lcl,
							IInterLoop interLoop,
							LogicStackMember lsm ) {

	StackStep add = new StackStep( elementToRun,tryCatchElement,lcl, interLoop,lsm);
	stack.push(add);

}

public void addToStackSimple( List<Element> list ) {
	
	for (int i = list.size() - 1; i >= 0; i--) {
	    final Element now = (Element) list.get(i);
	    StackStep add = new StackStep( now,null,null,null,null);
	    stack.push(add);
	 }
}

private void addToStackSimpleList( ArrayList<Element> list ) throws Exception {
	
	for (int i = list.size() - 1; i >= 0; i--) {
	    final Element curConfig = (Element) list.get(i);
		List<Element> executionList = curConfig.getChildren();
		addToStackSimple(executionList);
	 }
		
}


private void printStack (String header) {
	if ( !devMode ) return;
	Sp.p("");
	Sp.p("Stack Report: ");
	Sp.p("************ "+ header+" **************");
	for (int i = stack.size() - 1; i >= 0; i--) {
	    final StackStep now = (StackStep) stack.get(i);
	    
	    printStackStep (Commons.int2String(stack.size()-i), now);

	 }	
}

private void printStack (String header, Element elementToRun) {
	if ( !devMode ) return;
	Sp.p("");
	Sp.p("Stack Report: ");
	Sp.p("elementToRun: "+taskExplainerCompact(elementToRun));
	Sp.p("************ "+ header+" **************");
	for (int i = stack.size() - 1; i >= 0; i--) {
	    final StackStep now = (StackStep) stack.get(i);
	    
	    printStackStep (Commons.int2String(stack.size()-i), now);

	 }	
}


private void printStackStep (String header, StackStep stackStep) {
	if ( !devMode ) return;
	Sp.p(header +" Stack: "+taskExplainerCompact(stackStep.getStepToRun()));
}
private boolean traceThisExceptionPrintStack( Exception e ) {
	boolean trace = true;	
	try {
	
		if ( e instanceof at.nicolas.x.int_specs.CustomException )
			trace = false;
		else if ( e instanceof at.nicolas.x.int_specs.TkStopException)
			trace = false;
		else if ( e instanceof at.nicolas.x.int_specs.StopException)
			trace = false;		
		else if ( e instanceof at.nicolas.x.pub.LangException)
			trace = false;		
	} catch (Exception ex) {}
	

	return trace;

}

private boolean traceThisException( Exception e ) {
	boolean trace = true;	
	try {

		if ( e instanceof at.nicolas.x.int_specs.CustomException )
			trace = false;
		else if ( e instanceof at.nicolas.x.int_specs.TkStopException)
			trace = false;
		else if ( e instanceof at.nicolas.x.int_specs.StopException)
			trace = false;		
		else if ( e instanceof at.nicolas.x.pub.LangException)
			trace = false;		
	} catch (Exception ex) {}
	

	return trace;

}

private boolean isTkStop( Exception e ) {
	boolean trace = false;
	try {
		String classname = e.getClass().getCanonicalName();
		if ( classname.equals("at.nicolas.x.TkStopException"))
			trace = true;	
	} catch (Exception ex) {}
	return trace;

}
private void printExceptionStackWhenTracable ( Exception e ) {
	
	if ( traceThisExceptionPrintStack(e)) 
		lc.printExceptionStack(e);		
		
}


private static String ml2 ( String value, String type ) {
	return value;
}	






private static ArrayList<Element> getExecutionElements( DbJob job ) throws Exception {
	Element foundJob = job.getElement();
	ArrayList<Element> list = new ArrayList<Element>();
	
	List<Element> l_jobchilds = foundJob.getChildren();
	Iterator<Element> i_jobchilds = l_jobchilds.iterator();
	while ( i_jobchilds.hasNext()) {
		Element aktJobchild = i_jobchilds.next();
		list.add(aktJobchild);
	}

	return list;
}
private void executeJobelementSafe ( Element e,  String from, boolean fail_again, int cmd )  {
	try {
		executeJobelementFinalCatched ( e,  cmd );
	} catch (Exception e1) {
		lc.warn("@13 internal save step-execution had a problem [from:] "+from+" [step] [original] "+taskExplainer(e));
		if ( fail_again ) {
			lc.addExceptionIfNew("@13 internal save step-execution had a problem [from:] "+
					from+" [in step:] "+taskExplainer(e),
					e1);
		}
	}
}


private void trimJobcalllist(int tosize ) {
	while ( jobcalllist.size() > tosize) {
		jobcalllist.remove(jobcalllist.size()-1);
	}
}
private Element currentlyExcutingElementOriginal = null;
private void ti ( Element ti ) {
	try {
		terminator.executeTi(ti,tiLogLevel,appid,appdir,instid,instdir,this);
	} catch (Exception e) {
		// TODO Auto-generated catch block 

		lc.warn("Problem calling ti internally with "+taskExplainer(ti));
		Sp.pe(e);
	}

}
// hier spielt die musik
private void executeJobelementFinalCatched ( Element e,  int cmd ) throws Exception  {
	
	if ( e.getAttribute("error_id") != null ) {
		// there is something to be catched
		String throw_id = e.getAttributeValue("error_id");
		String throw_msg = e.getAttributeValue("error_msg");
		
		try {
			executeJobelementFinal ( e, cmd );
			
		} catch (Exception e1) {			
			// error_id & throw_msg
			if ( throw_id != null)
				throw_id = Entities.exportEntities(translateString(throw_id));
			
			if ( throw_msg != null )
				throw_msg = Entities.exportEntities(translateString(throw_msg));
			else
				throw_msg = e1.getLocalizedMessage()+"; Problem occurred in executing task '"+e.getName()+"'";			

			lc.debug("Throwing implizit CustomError [Catched:]"+e1.getLocalizedMessage()
					+", [throwed id:]"+throw_id
					+" [throwed msg:]"+throw_msg);
			
			CustomException ce = new CustomException(
					throw_msg,
					throw_id);
			
			throw ce;
		}
	} else if (!Commons.hasFlag(e, "catch")) {
		// kein catch, also fallen lassen
		
		if ( debugOn ) {
			try {
				executeJobelementFinal ( e, cmd );
			} catch (Exception e1) {
				lc.addExceptionIfNew("@14 Problem in executing step during debugging, [Message:] "+
							e1.getLocalizedMessage()+
						"  [job:] "+jobtodo.getJobid()+" [jobfile:] "+
						jobtodo.getFullfn()+" [ExceptionClass:] "+e.getClass().getName()+"  [step:] "
						+taskExplainer(e),
						e1);
				if ( traceThisExceptionPrintStack(e1)) {
					Sp.p("Printing Stack @14");
					lc.printExceptionStack(e1); 
				}
				throw e1;
			}				
		} else
			executeJobelementFinal ( e, cmd );
	} else  {
		// has catch
		//int jobcallsize = jobcalllist.size();
		//int fatalsize = lc.getFatalSize();
		try {			
			executeJobelementFinal ( e, cmd );
		} catch (Exception e1) {
			lc.trace("Catched Exception: "+ e1.getMessage()+" Step "+taskExplainer(e));
			//trimJobcalllist(jobcallsize );
			//lc.trimFatalStack(fatalsize);
		}
	}				
}

private void executeJobelementFinal ( Element e,  int cmd ) throws Exception  {
	
if ( haltIsRequested ) {
	throw new HaltException("@16 "+"Stop on HALT as requested");
} else if ( stop_go ) {
	while ( stop_go ) {
		Thread.sleep(500);	
		lc.warn(jobtodo.getJobid()+": I am sleeping and on pause, press Button 'C' to continue ...");
	}
}

break_one = false;

Element etE = null;
// lastErrorStep = null;
// boolean isStepDomain = false;
String en = e.getName();
currentlyExcutingElementOriginal = e;

// try {
	
	debugOn = CoreBuilder.rslogger.is_debug_plus;
	
	// sektion 1: date to steptime
	steptime = new Date();
	
	// Integer i_cmd = cmdm.get(en);
	
	// cmd bekommt die tasknummer aus der liste, falls nicht gefunden ist es -1
	/*
	int cmd = -1;
	if ( i_cmd != null)
		cmd = i_cmd.intValue();
		*/
	
	boolean remove_test = false;
	
	// doing the test right now
	/*
	if ( e.getAttribute("test") != null ) {
		// il ya une attribute test
		remove_test=true; 
		// String ueber ;
		// try {
			String ueber= translateValueMy(e.getAttributeValue("test"),false);
		//} catch (Exception e1) {
		//	lc.addException("Problem in translating test-attribute, Message: "+e1.getLocalizedMessage()+
		//			", job: "+job.getJobid()+", jobfile: "+job.getFullfn()+", step: "+Commons.toText(e));
		//	throw e1;					
		//}
		boolean ueber2 = Commons.stringtoBoolean(ueber, "@30 Evaluating test argument");
		if ( ueber2 ) {
			if ( debugOn ) 
				lc.debug_logic("Step will be exectuted by test: "+elementToText(e));
		} else {
			if ( debugOn ) 
				lc.debug_logic("Step omitted by test: "+elementToText(e));
			return;
		} // else
		 //	throw new LangException("#17 "+"The test-expression should deliver a boolean value (1 or 0) but it [delivers] '"+ueber+"' [step] "+elementToText(e));

	}
	*/
	
	String untranslatedElementContent = null;
	if ( e.getAttribute("sys_rec") !=null) {
		untranslatedElementContent = taskExplainer(e);		
	}
	
	// sektion 2: child-levels für loop, try_catch, choose und when festlegen, standard childlevel -1
	int childLevels = -1;
	/*
	switch ( cmd ) {
		case c_when:
		case c_choose:
		case c_loop:
		case c_try_catch: {
			childLevels = 0;
			break;
		}
		
	}
	 */

	// sektion 3: bei prop, file, dir (flag import) oder loop_while die übersetzung verwerfen
	if ( cmd == c_need ) {
		// kein translaten bei need
		etE = e;
	} else {
		
		// lc.debug_expressions("STARTED--");
		etE= translateJobelement( e,true,childLevels,remove_test );
		// sicherheitsstufe
		 //if ( debugOn) 
		// p22("       == " +Commons.toTextCompact(etE));
		if ( threadStopped)
			throw new Exception("Interpreter-Thread was stopped");
		if ( tracePrinter!=null ) tracePrinter.println("       == "+taskExplainerCompact(etE));
		// lc.debug_expressions("FiNisHED--");
		/*
		if ( CoreBuilder.rslogger.getLevel() == Level.ALL )
			CheckDirty.go(et, true, getSource4CheckDirty(), lc,childLevels);
			*/
	}
	
	if ( e.getAttribute("sys_rec") !=null) {

		try {
			String translatedElementContent = taskExplainer(etE);
				
			Sp.p("B: "+translatedElementContent);
	
			Element innerCall = null;
			if ( isSystemcall) {
				innerCall = new Element("call_service");
			} else {
				innerCall = new Element("call_job");	
			}
			innerCall.setAttribute("job", etE.getAttributeValue("sys_rec"));
			innerCall.setAttribute("reason","record");
			
			Element sp2 = new Element("prop");
			sp2.setAttribute("id", "sys_rec_original");
			sp2.setAttribute("x", Entities.importEntities(untranslatedElementContent));
			addPropSys(sp2);
			
			Element sp3 = new Element("prop");
			sp3.setAttribute("id", "sys_rec_effective");
			sp3.setAttribute("x", translatedElementContent);
			addPropSys(sp3);
			
			addToStackPlain(innerCall);
			// Element finishedByTk = stackRotatorCatched();	TEUFEL
			// if ( finishedByTk != null ) {
				// follow-job can not use tk
			//	throw new Exception("Record job '"+etE.getAttributeValue("sys_rec")+"' cant use tk!!");
			// }
		
		} catch (Exception e1) {
			Sp.p("Problem in executing a record-job: "+etE.getAttributeValue("sys_rec")+" for "+untranslatedElementContent);
			throw e1;
		}
					
	}	
	
 // ende debug on 

	// 
	/* secret ist jetzt seit 17.9.2021 in verwendung
	if ( etE.getAttribute("secret") != null)
		throw new LangException(603,"Attribute 'secret' not allowed in general. [step]"+
				Commons.toText(e));
	
	if ( Commons.hasFlag(etE, "secret"))
		throw new LangException(604,"Flag 'secret' not allowed in general. [step]"+
				Commons.toText(e));
				*/
	
	// check for attributes starting with sys_

	if ( debugOn ) {
		String checkflag = etE.getAttributeValue("flag");
		if ( checkflag != null ) {
			String a[] = checkflag.split("\\s+");;			
			for ( String thisA: a ) {
				if ( thisA.startsWith("sys_")) {
					if ( thisA.equals("sys_debug") || thisA.equals("sys_debug_content"));
					else
						throw new LangException(606,"Can't have flags starting with 'sys_', but found:"+thisA);
				}
			}
		}
	}

	
	/* sektion 7: inline
	 * inline
	 */
	List<Element> l_inline = etE.getChildren("inline");
	if ( l_inline.size() > 0 ) {
		Iterator<Element> i_inline = l_inline.iterator();
		while ( i_inline.hasNext()) {
			Element x_orig = i_inline.next();
			Element x = null;
			if (childLevels == 0)
				x = translateJobelement( x_orig,true,-1,false );
			else
				x = x_orig;
			if ( x.getAttribute("attribute") == null )
				throw new LangException(601,"inline must have the attribute 'attribute' which specifies the parameter to be sustituted!");
			String attribute_name = x.getAttributeValue("attribute");
			
			if ( attribute_name == null ) 
				throw new LangException(602,"inline must have the attribute 'attribute' which specifies the parameter to be sustituted!");
			//String tempfileN = getTempFileN("inline",".txt"); 
			
			String tempfileN = isitzung.getSessionHomeFs()+"/inline/"+Commons.randomAlphaNumeric(5);
			// System.out.println("using tempfileN "+tempfileN);
			
			String encoding = "UTF-8";
			if ( x.getAttribute("encoding")!=null)
				encoding = Entities.exportEntities(x.getAttributeValue("encoding"));

			String towrite = x.getText();

			
			if ( Commons.hasFlag(etE, "sys_debug")) {
			try {
				Element tx = new Element("ti");
				tx.setAttribute("type", "file");
				tx.setAttribute("label", "inline: "+attribute_name+":");
				tx.setAttribute("x",Entities.importEntities(tempfileN));
				tx.setAttribute("flag","label_right flow");
				
				ti(tx);
				
					if ( Commons.hasFlag(etE, "sys_debug_content")) {
						Element fc = new Element("ti");
						fc.setAttribute("label"," ");
						fc.setAttribute("flag","html");
						fc.setText(translateString("<font face=\"Courier\">(f,escape,_htmlb,"+towrite+")</font>"));
						ti(fc);
					}
					
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				lc.warn("Some problem with sys_debug_content");
			}
			} // end if 
				
			
			if ( !Commons.hasFlag(x, "dont_unescape_on_write"))
				towrite = Entities.exportEntities(towrite);

			CommonsVfs.writeToEncodedFile(towrite, 
					tempfileN, 
					encoding,isitzung);
			e.setAttribute(attribute_name, Entities.importEntities(tempfileN));
			etE.setAttribute(attribute_name, Entities.importEntities(tempfileN));
			

			
			// löschen des inline-elements aus et?
		}				
	}
	
	
boolean weiter =true;
Exception nichtWeiter = null;
if ( cmd > -1 ) {
// sektion 8:
weiter = false;




/*
 * 
 * Step Interpretion
 */
/* doing:  ********************************************************************/
switch ( cmd ) {
	
	case c_prop : {
		/* ----------------------------------------------------------------------- */
		addProp(etE,false);
		break;
		
	} // end case
	
	case c_file : {
		// ----------------------------------------------------------------------- 
		addFile(etE,false);
		break;
		
	} // end case
	
	case c_dir : {
		// ----------------------------------------------------------------------- 
		addDir(etE,false);
		break;
	} // end case	
					
	
	case c_when : {
		// -----------------------------------------------------------------------
		// GESTRICHTEN 
		break;
	} // end case
	
	case c_loop : {
		// -----------------------------------------------------------------------
		// GESTRICHEN
		break;
	} // end case	
	
	case c_choose : {
		// ----------------------------------------------------------------------- 
		// GESTRICHEN

		break;
	} // end case	
	case c_try_catch : {
		// ----------------------------------------------------------------------- 
		// GESTRICHEN
		break;
	} // end case
	
	case c_need : {
		// ----------------------------------------------------------------------- 
		if ( debugOn )
			lc.debug_tasks("need: "+elementToText(e));

		List<Element> need_childs = e.getChildren("need");
		
		boolean has_need_children = false;
		if ( need_childs.size() > 0 )
			has_need_children = true;

		if ( !has_need_children) {
			checkNeed(e);			
		} else {
			List<Element> childs = e.getChildren(); 
			Iterator<Element> iChilds = childs.iterator(); 
			while ( iChilds.hasNext()) { 
				Element aktNeed = iChilds.next();
				checkNeed ( aktNeed); 
			}						
		}		
		break;
	} // end case need 
	
	case c_call_job :
	case c_call_service :
	case c_call_pro :
	case c_call_app : {
		// ----------------------------------------------------------------------- 
		boolean pm = debugOn;
		if ( ( cmd==c_call_job && !isSystemcall) || (cmd==c_call_service && isSystemcall)) {
			// GESTRICHEN

			
		} else if ( cmd==c_call_job && isSystemcall ) {
			String name = "'call_job'";
			String ap = "task '"+name+"' says: ";
			
			// parameter lesen und pruefen 
			String jobidE = etE.getAttributeValue("job");
			if (jobidE==null) 
				throw new LangException(611,ap+"I want an attribute 'job' which specifies the job");

			String app_to_useE = etE.getAttributeValue("app");
			if (app_to_useE==null) 
				throw new LangException(612,ap+"I want an attribute 'app' which specifies the application");
			String app_to_useN = Entities.exportEntities(app_to_useE);
			DbApp requested_app = CoreBuilder.db.getAppById(app_to_useN);
			if ( requested_app == null )
				throw new LangException(613,ap+"The app '"+app_to_useN+"' does not extist");
						
			/*
				throw new LangException("#29 "+ap+"I want an attribute 'app' which specifies the application [step:original]"+
						Commons.toText(e)+" [step:translated] "+Commons.toText(et));
			*/
			String inst_to_useE = etE.getAttributeValue("pro");
			String inst_to_useN = null;
			
			if ( inst_to_useE != null ) {
				inst_to_useN = Entities.exportEntities(inst_to_useE);
				DbPro requested_pro = requested_app.getInstById(inst_to_useN);
				if ( requested_pro == null )
					throw new LangException(614,ap+"The pro '"+inst_to_useN+"' of app '"+app_to_useN+"' does not extist");
				
			}

			// Job Aufrufen	

			String loglabel = "|| call_job ";
			String logid = jobidE;
			if ( pm ) {
				Element logon = new Element("log_plus");
				logon.setAttribute("label", loglabel);
				logon.setAttribute("id", logid);
				executeJobelementSafe(logon,"call job",false,c_log_plus);
			}

			Element logoff = new Element("log_minus");
			logoff.setAttribute("label", loglabel);
			logoff.setAttribute("id", logid);
			
			int flag_silence = Interpreter.SILENCE_NO;
			if ( Commons.hasFlag(etE, "silent"))
				flag_silence = Interpreter.SILENCE_YES;
			
			try {
				AppJobexecution x = new AppJobexecution(
						app_to_useN,
						inst_to_useN,
						Entities.exportEntities(jobidE),
						etE,
						false,
						flag_silence,
						terminator,
						call_depth+1,
						null); 	
				x.run();	

			} catch (Exception ex) {
				executeJobelementSafe(logoff,"Calling AppJobexecution",false,c_log_minus);					
				throw new Exception("@23 "+"Error in executing job  '"+Entities.exportEntities(jobidE)+"' [message] "+ex.getLocalizedMessage()
						+" [step] "+taskExplainerCompact(Entities.exportEntitiesClone(etE)));
			}
			if ( pm ) {
				executeJobelementSafe(logoff,"Calling AppJobexecution",false,c_log_minus);	
			}
		}  
		
		 else if ( cmd==c_call_service) {
			 // calls a service out of an application
			Sp.p("Calling service for app="+appid+", pro="+instid);
			String scInstN = instid;
			String scInstDirN = instdir;
			String scAppN  = appid;

			if ( etE.getAttributeValue("pro")!=null && etE.getAttributeValue("app")!=null ) {
				scAppN  = Entities.exportEntities(etE.getAttributeValue("app"));
				scInstN = Entities.exportEntities(etE.getAttributeValue("pro"));
				scInstDirN = null;
			} else if ( etE.getAttributeValue("app")!=null ) {
				scAppN  = Entities.exportEntities(etE.getAttributeValue("app"));
				scInstN = null;
				scInstDirN = null;
			} else if ( etE.getAttributeValue("pro")!=null ) {
				scInstN = Entities.exportEntities(etE.getAttributeValue("pro"));
				scInstDirN = null;
			}
							
			String scJobE  = etE.getAttributeValue("job");
			if ( scJobE == null)
				throw new LangException(615,"When using 'call_service', the attribute 'job' must be specified!");
			
			// flex zusammenstellen
			Element flex = new Element("config");
		
			// und was sonst noch so angegeben ist
			flex.addContent(etE.cloneContent());
			
			// log.trace(declareSystemcall +" "+scJob+" for app("+scApp+") inst("+scInst+ ") started.");
			String loglabel= "|| call_service";
			String logid = scJobE;
			if ( pm ) {
				Element logon = new Element("log_plus");
				logon.setAttribute("label", loglabel);
				logon.setAttribute("id", logid);
				executeJobelementSafe(logon,"call service",false,c_log_plus);	
			}

			Element logoff = new Element("log_minus");
			logoff.setAttribute("label", loglabel);
			logoff.setAttribute("id", logid);
			// logoff.setAttribute("name", logid);

			int mySilence = silence;
			if ( Commons.hasFlag(etE, "silent"))
				mySilence = Interpreter.SILENCE_YES;
			try {
				AppSystemcall x = new AppSystemcall(
						Entities.exportEntities(scJobE),
						scAppN,
						scInstN,
						flex,
						mySilence,
						terminator, 
						call_depth+1,
						scInstDirN);
				x.run();
			} catch (Exception ex) {
				executeJobelementSafe(logoff,"AppSystemcall",false,c_log_minus);					
				throw new Exception("@24 "+"Error in executing call_service '"+Entities.exportEntities(scJobE)+
						"' [message] "+ex.getLocalizedMessage()
						+" [step] "+taskExplainerCompact(Entities.exportEntitiesClone(etE)));
			}
			if ( pm ) {
				executeJobelementSafe(logoff,"AppSystemcall",false,c_log_minus);
			}
			
			// log.trace(declareSystemcall +" "+scJob+" for app("+scApp+") inst("+scInst+ ") finished.");					

		}  else if ( cmd==c_call_pro) {
			//Element eu = unescapeStep(et);
			if ( true )
				throw new LangException (616,"task 'call_pro' not implemented anymore");
			String ap = "task 'call_pro' says: ";

			// parameter lesen und pruefen
			String appid = etE.getAttributeValue("app");
			String jobid = etE.getAttributeValue("job");
			String instid = etE.getAttributeValue("pro");
			String return_dir = etE.getAttributeValue("return_dir_id");
			if ( return_dir != null )
				throw new Exception("tasks 'call_job' has not attribute 'return_dir'. use function 'pro_dir'");
			
			if (appid==null) 
				throw new LangException(617,ap+"I want an attribute 'app' which specifies the application");
			if (instid==null) 
				throw new LangException(618,ap+"I want an attribute 'pro' which specifies the project");
			if (jobid==null) 
				throw new LangException(619,ap+"I want an attribute 'job' which specifies the job");
			
			// Job Aufrufen	 

			String loglabel = "|| call_pro"; 
			String logid = "app="+etE.getAttributeValue("app")+" pro="+etE.getAttributeValue("pro")+
					" job="+etE.getAttributeValue("job");
			if ( pm ) {
				Element logon = new Element("log_plus");
				logon.setAttribute("label", loglabel);
				logon.setAttribute("id", logid);
				executeJobelementSafe(logon,"call pro",false,c_log_plus);
			}

			Element logoff = new Element("log_minus");
			logoff.setAttribute("label", loglabel);
			logoff.setAttribute("id", logid);
			
			Element flex = null;
			Element flexEnd = null;
			// flex zusammenstellen
			if ( etE.getChildren().size() == 2 ) {
				// vielleicht sinds begin und end
				if ( (etE.getChildren().get(0)).getName().equals("begin") &&
					 (etE.getChildren().get(1)).getName().equals("end")	) {
					flex = new Element("config");
					flex.addContent((etE.getChildren().get(0)).cloneContent());
					flexEnd = new Element("config");
					flexEnd.addContent((etE.getChildren().get(1)).cloneContent());
				} else {
					flex = new Element("config");
					flex.addContent(etE.cloneContent());
				}
				
			} else {
				flex = new Element("config");
				flex.addContent(etE.cloneContent());
			}
			
			try {
				
				AppJobexecution x = new AppJobexecution(
						Entities.exportEntities(appid),
						Entities.exportEntities(instid),
						Entities.exportEntities(jobid),
						flex,
						false,
						Interpreter.SILENCE_NO,
						terminator, 
						call_depth+1,
						flexEnd); 	
				x.run();	

			} catch (Exception ex) {
				executeJobelementSafe(logoff,"AppJobexecution.call_pro",false,c_log_minus);					
				throw new LangException(620, "Error in executing call_pro [app] "+appid+"[pro] "+instid+" [job] "+jobid+"[message] "+ex.getLocalizedMessage()
						+" [step] "+taskExplainerCompact(etE));						
				
			}
			if ( pm ) {
				executeJobelementSafe(logoff,"AppJobexecution.call_pro",false,c_log_minus);					
			}

		}  else if ( cmd==c_call) { 
			throw new LangException (621,"task 'call' not implemented yet");
		} else if ( cmd==c_call_app ) {
			String ap = "task 'call_app' says: ";
			
			// parameter lesen und pruefen
			String appidE = etE.getAttributeValue("app");
			String jobidE = etE.getAttributeValue("job");
			String inst_dirE = etE.getAttributeValue("pro_dir");
			String inst_idE = etE.getAttributeValue("pro_id");
			
			if (appidE==null) throw new LangException(622,ap+"I want an attribute 'app' which specifies the application");
			if (inst_dirE==null) throw new LangException(623,ap+"I want an attribute 'pro_dir' which specifies the temporary project-dir");
			if (inst_idE==null) throw new LangException(624,ap+"I want an attribute 'pro_id' which specifies the id of the temporary project");
			if (jobidE==null) throw new LangException(625,ap+"I want an attribute 'job' which specifies the job");
			
			int para_silent = SILENCE_NO;
			if ( Commons.hasFlag(etE, "silent"))
				para_silent = SILENCE_YES;
			
			String loglabel = "|| call_app"; 
			String logid = "app="+
							appidE+
							" pro="+
							inst_idE+
							" prodir="+
							inst_dirE+
							" job="+
							jobidE;
			
			// Job Aufrufen	 
			if ( pm ) {
				Element logon = new Element("log_plus");
				logon.setAttribute("label", loglabel);
				logon.setAttribute("id", logid);
				executeJobelementSafe(logon,"call_app",false,c_log_plus);	
			}

			Element logoff = new Element("log_minus");
			logoff.setAttribute("label", loglabel);
			logoff.setAttribute("id", logid);				
			// logoff.setAttribute("name", name);

			Element flex = null;
			Element flexEnd = null;
			
			// flex zusammenstellen
			if ( etE.getChildren().size() == 2 ) {
				// vielleicht sinds begin und end
				if ( (etE.getChildren().get(0)).getName().equals("begin") &&
					 (etE.getChildren().get(1)).getName().equals("end")	) {
					// es gibt ein begin und ein ende
					flex = new Element("config");
					flex.addContent((etE.getChildren().get(0)).cloneContent());
					flexEnd = new Element("config");
					flexEnd.addContent((etE.getChildren().get(1)).cloneContent());
				} else {
					flex = new Element("config");
					flex.addContent(etE.cloneContent());
				}
				
			} else if ( etE.getChildren().size() == 1 ) {
				// vielleicht sinds begin und end
				if ( (etE.getChildren().get(0)).getName().equals("begin") ) {
					// nur ein begin
					flex = new Element("config");
					flex.addContent((etE.getChildren().get(0)).cloneContent());
				} else if ( (etE.getChildren().get(0)).getName().equals("end") ) {
					// nur ein ende
					flexEnd = new Element("config");
					flexEnd.addContent((etE.getChildren().get(0)).cloneContent());
				} else {
					// weder begin noch ende, alles begin
					flex = new Element("config");
					flex.addContent(etE.cloneContent());
				}
				
			} else {
				flex = new Element("config");
				flex.addContent(etE.cloneContent());
			}
			
			try {
				AppJobexecutionUsage x = new AppJobexecutionUsage(
						Entities.exportEntities(appidE),
						Entities.exportEntities(inst_idE),
						Entities.exportEntities(inst_dirE),
						Entities.exportEntities(jobidE),
						flex,
						false,
						para_silent,
						terminator, 
						call_depth+1,
						flexEnd); 	
				x.run();	

			} catch (Exception ex) {
				
				executeJobelementSafe(logoff,"AppJobexecution.call_app",false,c_log_minus);					
				throw new Exception("@25 "+"Error in executing task 'call_app' "
						+" with: app="+Entities.exportEntities(appidE)+
						", pro="+Entities.exportEntities(inst_idE)
						+", pro_dir="+Entities.exportEntities(inst_dirE)
						+", job="+Entities.exportEntities(jobidE)
						+" [message] "+ex.getLocalizedMessage()
						+" [step] "+taskExplainerCompact(Entities.exportEntitiesClone(etE)));	
			}
			if ( pm ) {
				executeJobelementSafe(logoff,"AppJobexecution.call_app",false,c_log_minus);			
			}

		}				
		
		break;
	} // end case
	
	case c_ti : {
		// ----------------------------------------------------------------------- 
		terminator.executeTi(etE,tiLogLevel,appid,appdir,instid,instdir,this);
		break;
	} // end case	
	
	case c_throw : {
		// ----------------------------------------------------------------------- 
		String throwMsg = etE.getAttributeValue("x");
		if ( throwMsg == null )
			throwMsg = etE.getText();

		String error_type = etE.getAttributeValue("id");
		// System.out.println("CE "+throwMsg+" - "+error_type+" "+Commons.toText(etE));
		CustomException ce = new CustomException(
				Entities.exportEntities(throwMsg),
				Entities.exportEntities(error_type));
		// System.out.println("CD "+throwMsg+" - "+error_type);

		throw ce;
	} // end case	
	
	case c_stop : {
		// ----------------------------------------------------------------------- 
		String throwMsg = etE.getAttributeValue("x");
		if ( throwMsg == null )
			throwMsg = etE.getText();
		//nic123
		String error_type = etE.getAttributeValue("id");
		StopException ce = new StopException(
				Entities.exportEntities(throwMsg),
				Entities.exportEntities(error_type));

		throw ce;
	} // end case		
	
	case c_throw_again : {
		// ----------------------------------------------------------------------- 
		// isStepDomain = true;
		if ( exceptionToThrowAgain == null ) {
			String catched_msg = null;
			try {
				catched_msg = getVar("prop", "catched_msg");
			} catch (Exception e1) {
				catched_msg = null;
			}
			
			String catched_class = null;
			try {
				catched_class = getVar("prop", "catched_class");
			} catch (Exception e1) {
				catched_class = null;
			}			
			if ( catched_msg != null ) {
				if ( catched_class != null )
					catched_msg = catched_msg + " [CLASS: "+catched_class+"]";
				throw new Exception(Entities.exportEntities(catched_msg));
			} else
				throw new Exception("Just Thrown again");
		} else {
			throw exceptionToThrowAgain;
		}
		/*
		Exception toThrowAgain = lc.getLastFatal().getException();
		nic123
		if ( toThrowAgain!= null ) {
			lc.fatal("<throw_again/> "+toThrowAgain.getLocalizedMessage());
			throw toThrowAgain;
		} else
			lc.warn("The task 'throw_again' was called although there is no past exception available");
		*/
		// break;
	} // end case	
	case c_debug :
	case c_log :
	case c_info :
	case c_i :
	case c_error :
	case c_fatal :
	case c_warn :
	case c_trace : {
		// -----------------------------------------------------------------------
		//if ( debugOn) {
		//	if ( !CoreBuilder.rslogger.debugOn_filter_expressions() ) {
		//		exportElement(et);
		//	}
		// } else
		// unescaping, log operiert auf natural basis
		Entities.exportEntities(etE);	
		
		// getting the right level
		Level level;

		if 		( cmd==c_info|| cmd==c_i)  level = Level.INFO;
		else if ( cmd==c_warn) level = Level.WARN;
		else if ( cmd==c_debug) level = Level.DEBUG;
		else if ( cmd==c_error) level = Level.ERROR;
		else if ( cmd==c_fatal) level = Level.FATAL;
		else if ( cmd==c_trace) level = Level.TRACE;	
		else {
			// must be c_log
			if ( etE.getAttributeValue("level") != null ) level = LogLogger.string2Level(etE.getAttributeValue("level"));
			else 	level = lc.getCurrentLevel();			
		} 

		
		String sayIt = etE.getAttributeValue("x");
		if ( sayIt == null )
			sayIt = etE.getText();
		lc.log(sayIt,level);
		
		break;
	} // end case	
	
	case c_lock : {
		// ----------------------------------------------------------------------- 
		addLock(etE);
		break;
	} // end case
	
	case c_taskdef : {
		// ----------------------------------------------------------------------- 
		Sp.p("c_taskdef: ignoreClassloading="+ignoreClassloading+", interpreterShouldGiveBackTk="+interpreterShouldGiveBackTk);
		PlugTaskdef dasneue = PlugTaskdef.newInstance(etE,this,ignoreClassloading); // TEUFEL webModus);
		if ( TaskdefMap.get(dasneue.getName()) != null )
			throw new LangException(626,"task '"+dasneue.getName()+"' exists already");
		TaskdefMap.put(dasneue.getName(),dasneue);
		break;
	} // end case
	
	case c_fundef : {
		// ----------------------------------------------------------------------- 
		PlugFundef dasneue = PlugFundef.newInstance(etE,this,ignoreClassloading); // TEUFEL webModus);
		if ( FundefMap.get(dasneue.getName()) != null )
			throw new LangException(627,"fun '"+dasneue.getName()+"' exists already");		
		FundefMap.put(dasneue.getName(),dasneue);		
		break;
	} // end case	
	case c_coopdef : {
		// ----------------------------------------------------------------------- 
		
		String jarFile;
		ClassLoader jarLoader;
		if ( etE.getAttribute("classloader") != null ) {
			// 
			String classloaderId = Entities.exportEntities(etE.getAttributeValue("classloader"));
			if (classloaderMap.containsKey(classloaderId)) {
				// exists already in the map
				// use that old one
				lc.trace("classloader '"+classloaderId+"' exists already and is going to be used");
				Loader old = classloaderMap.get(classloaderId);
				jarFile = old.jarfile;
				jarLoader = old.classloader;
			} else {
				// create the loader and put it in the map
				
				ClassloaderInfo cli = ClassloaderInfo.getClassloaderInfo(etE, lc,ignoreClassloading); // TEUFEL webModus);
				jarFile = cli.filelist;
				jarLoader = cli.classloader;
				Loader neu = new Loader (jarLoader,jarFile);
				classloaderMap.put(classloaderId, neu);
				lc.trace("classloader '"+classloaderId+"' is stored for later use");
				
			}
		} else {
			
			ClassloaderInfo cli = ClassloaderInfo.getClassloaderInfo(etE, lc,false); // TEUFEL webModus);
		
			jarFile = cli.filelist; 
			jarLoader = cli.classloader;			
		}
		
		List<Element> l_defs = etE.getChildren();
		Iterator<Element> i_defs = l_defs.iterator();
		while ( i_defs.hasNext()) {
			Element aktDef = i_defs.next();
			String defId = null;
			String defClass = null;
			Sp.p("name:::: "+aktDef.getName());
			if ( !aktDef.getName().equals("jar") ) {
				if ( aktDef.getAttribute("id") == null )
					throw new LangException(29, "task 'coopdef' defintion needs attribute 'id'");
				defId = Entities.exportEntities(aktDef.getAttributeValue("id"))	;
	
				if ( aktDef.getAttribute("class") == null )
					throw new LangException(30, "task 'coopdef' defintion needs attribute 'class'");
				defClass = Entities.exportEntities(aktDef.getAttributeValue("class"))	;
			}
			if ( aktDef.getName().equals("taskdef")) {
				PlugTaskdef td = new PlugTaskdef(
						defId,
						defClass,
						jarLoader,
						//jarFile,
						this,
						Entities.exportEntitiesClone(aktDef),
						true);
				
				if ( TaskdefMap.get(td.getName()) != null )
					throw new LangException(31, "task '"+td.getName()+"' exists already");				
				
				TaskdefMap.put(td.getName(),td);
				
			} else if ( aktDef.getName().equals("fundef")) {
				PlugFundef td = new PlugFundef(
						defId,
						defClass,
						jarLoader,
						jarFile,
						this,
						Entities.exportEntitiesClone(aktDef));
				if ( FundefMap.get(td.getName()) != null )
					throw new LangException(32,"fun '"+td.getName()+"' exists already");				
				FundefMap.put(td.getName(),td);				
			} else if ( aktDef.getName().equals("jar")) {
				
			}else {
				throw new LangException(33,"task 'coopdef' found unknow child '"+aktDef.getName()+"' but only taskdef or fundef is acceptable");
			}

		}

		
	
		break;
	} // end case		
	
	case c_ask : {
		// ----------------------------------------------------------------------- 
		String wh = etE.getAttributeValue("what");
		String out = etE.getAttributeValue("out");
		
		if ( wh == null  )
			throw new LangException(34, "task 'ask' needs an attribute 'what' with flags: apps, services, vars or coops");
		
		if ( out == null )
			throw new LangException(35, "task 'ask' needs an attribute 'out' with the output-file");
		
		boolean natural;
		if ( etE.getAttribute("style") == null ) {
			throw new LangException(36,"task 'ask' needs attribute 'style' with value 'natural' or 'escaped'");			
		} else if ( etE.getAttributeValue("style").equals("natural"))
			natural = true;
		else if ( etE.getAttributeValue("style").equals("escaped"))
			natural = false;
		else
			throw new LangException(37, "task 'ask' needs attribute 'style' with value 'natural' or 'escaped'");			

		Element askRoot = new Element("ws");
		askRoot.setAttribute("what", wh);
		if ( natural ) {
			askRoot.setAttribute("dir", CoreBuilder.ws.getWsDir());
			askRoot.setAttribute("id", CoreBuilder.ws.getWsId());
			askRoot.setAttribute("log_dir", CoreBuilder.ws.getLogsDir());
			askRoot.setAttribute("backup_dir", CoreBuilder.ws.getBackupDir());
			askRoot.setAttribute("style", "natural");
		} else {
			askRoot.setAttribute("dir", CoreBuilder.ws.getWsDirEscaped());
			askRoot.setAttribute("id", CoreBuilder.ws.getWsIdEscaped());
			askRoot.setAttribute("log_dir", CoreBuilder.ws.getLogsDirEscaped());
			askRoot.setAttribute("backup_dir", CoreBuilder.ws.getBackupDirEscaped());	
			askRoot.setAttribute("style", "escaped");
		}
		
		if ( wh.indexOf("apps") > -1 ) {
			Element askDb = CoreBuilder.db.getAskDb(
					Entities.exportEntities(etE.getAttributeValue("app")),
					natural);
			askRoot.addContent(askDb);
		}
		
		if ( wh.indexOf("services") > -1 ) {
			Element askSj = CoreBuilder.db.getAskSystemjobs(natural);
			askRoot.addContent(askSj);
		}
		
		if ( wh.indexOf("coops") > -1 ) {
	
			Element reportCoopsAsk = reportCoopsAsk();
			askRoot.addContent(reportCoopsAsk);
		}		
		
		if ( wh.indexOf("vars") > -1 ) {
								
			Element vars_element=new Element("vars");
			
			// PROPS
			Set<String> propKeySet = new TreeSet<String>(propMap.keySet());
			Iterator<String> propIter = propKeySet.iterator();
			while ( propIter.hasNext() ) {
				InterProp aktProp = propMap.get(propIter.next());
				Element dazu = new Element("var");
				
				if ( natural ) {
					dazu.setAttribute("id", Entities.exportEntities(aktProp.idE));
					dazu.setText(Entities.exportEntities(aktProp.valueE));						
				} else {
					dazu.setAttribute("id", aktProp.idE);
					dazu.setText(aktProp.valueE);
				}
				dazu.setAttribute("type", "prop");
				// dazu.setAttribute("exe", "<prop id=\""+aktProp.id+"\" x=\""+aktProp.value+"\" />");
				vars_element.addContent(dazu);

			}			
			// FILES
			Set<String> fileKeySet = new TreeSet<String>(fileMap.keySet());
			Iterator<String> fileIter = fileKeySet.iterator();
			while ( fileIter.hasNext() ) {
				InterFile aktFile = fileMap.get(fileIter.next());
				Element dazu = new Element("var");
				
				if ( natural ) {
					dazu.setAttribute("id", Entities.exportEntities(aktFile.id));
					dazu.setText(Entities.exportEntities(aktFile.future_fn));				
				} else {
					dazu.setAttribute("id", aktFile.id);
					dazu.setText(aktFile.future_fn);										
				}
				dazu.setAttribute("type", "file");
				vars_element.addContent(dazu);
						
			}		
			
			// DIRS 

			Set<String> dirKeySet = new TreeSet<String>(dirMap.keySet());
			Iterator<String> dirIter = dirKeySet.iterator();
			while ( dirIter.hasNext() ) {
				InterDir aktDir = dirMap.get(dirIter.next());
				Element dazu = new Element("var");
				
				String cont;
				if ( natural ) {
					dazu.setAttribute("id", Entities.exportEntities(aktDir.id));
					
					if (aktDir.value.equals(FileName.SEPARATOR) || aktDir.value.equals("/")) {
						cont = "";
					} else
						cont = Entities.exportEntities(aktDir.value);					
				} else {
					dazu.setAttribute("id", aktDir.id);
					
					if (aktDir.value.equals(FileName.SEPARATOR) || aktDir.value.equals("/")) {
						cont = "";
					} else
						cont = aktDir.value;					
				}
				dazu.setAttribute("type", "dir");
				dazu.setText(cont);

				vars_element.addContent(dazu);
			}
			

			
			
			askRoot.addContent(vars_element);
		}				
						
		Document v = new Document(askRoot);
		CommonsVfs.writeToFile(v, Entities.exportEntities(out),isitzung);		
		break;
	} // end case
	
	case c_remove : {
		// ----------------------------------------------------------------------- 
		String r_id = etE.getAttributeValue("id");
		String r_type = etE.getAttributeValue("type");
		if ( r_id == null || r_type == null ) {
			throw new LangException (38, "The command 'remove' demands the attributes 'id' and 'type'");
		}
		
		if ( r_type.equals("prop")) {
			InterProp togo = getPropById ( r_id );
			if ( togo != null ) {
				if ( togo.locked ) {
					String msg = "The prop '"+Entities.exportEntities(r_id)
							+"' was requested to remove, but it is LOCKED."
							+" [step:] "+taskExplainerCompact(etE);
					throw new LangException(39, msg);						
				}
				propMap.remove(r_id);
			} else {
				String msg = "The prop '"+Entities.exportEntities(r_id)
						+"' was requested to remove, but it does not exist."
						+" [step] "+taskExplainerCompact(etE);
				throw new LangException(40,msg);	
			}
			
		} else if ( r_type.equals("file")) {
			InterFile togo = getFileById ( r_id );
			if ( togo != null ) {
				if ( togo.locked ) {
					String msg = "The file '"+Entities.exportEntities(r_id)
							+"' was requested to remove, but it is LOCKED."
							+" [step:] "+taskExplainerCompact(etE);
					throw new LangException(41,msg);						
				}
				fileMap.remove(r_id);
			} else {
				String msg = "The file '"+Entities.exportEntities(r_id)
						+"' was requested to remove, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(42,msg);	
			}
			
		} else if ( r_type.equals("dir")) {
			InterDir togo = getDirById ( r_id );
			if ( togo != null ) {
				if ( togo.getIsLocked() ) {
					String msg = "The dir '"+Entities.exportEntities(r_id)
							+"' was requested to remove, but it is LOCKED."
							+" [step:]"+taskExplainerCompact(etE);
					throw new LangException(43,msg);						
				}
				dirMap.remove(r_id);
			} else {
				String msg = "The dir '"+Entities.exportEntities(r_id)
						+"' was requested to remove, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(45,msg);	
			}
		} else {
			throw new LangException (46,"The task 'remove' demands an attributes 'type' with the value prop,dir or file");
		}
		break;
	} // end case
	
	case c_translate : {
		if ( Commons.hasFlag(etE, "ok")) {
			String r_id = etE.getAttributeValue("id");
			String r_type = etE.getAttributeValue("type");

			if ( r_id == null || r_type == null ) {
				throw new LangException (47,"The task 'translate' demands the attributes 'id' and 'type'");
			}
			
			if ( r_type.equals("prop")) {
				InterProp togo = getPropById ( r_id );
				
				if ( togo != null ) {
					String propVal = togo.valueE;
					String propValTrans = translateString(propVal);
					togo.valueE = propValTrans;
					// PropList.remove(togo);
				} else {
					String msg = "The prop '"+Entities.exportEntities(r_id)
							+"' was requested to translate, but it does not exist."
							+" [step:]"+taskExplainerCompact(etE);
					throw new LangException(48, msg);				
				}
				
			} else if ( r_type.equals("file")) {
				InterFile togo = getFileById ( r_id );

				if ( togo != null ) {
					String propVal = togo.future_fn;
					String propValTrans = translateString(propVal);
					togo.future_fn = propValTrans;
				} else {
					String msg = "The file '"+Entities.exportEntities(r_id)
							+"' was requested to translate, but it does not exist."
							+" [step:]"+taskExplainerCompact(etE);
					throw new LangException(49,msg);	
				}
			} else if ( r_type.equals("dir")) {
				InterDir togo = getDirById ( r_id );

				if ( togo != null ) {
					String propVal = togo.value;
					String propValTrans = translateString(propVal);
					togo.value = propValTrans;
					
				} else {
					String msg = "The dir '"+Entities.exportEntities(r_id)
							+"' was requested to translate, but it does not exist."
							+" [step:]"+taskExplainerCompact(etE);
					throw new LangException(50,msg);	
				}
			} else {
				throw new LangException (51,"The task 'translate' demands an attributes 'type' with the value prop,dir or file");
			}			
		} else 
			c_translate( etE);
	
		break;
	} // end case	
	
	case c_escape : { 
		// ----------------------------------------------------------------------- 
		String r_id = etE.getAttributeValue("id");
		String r_type = etE.getAttributeValue("type");
		if ( r_id == null || r_type == null ) {
			throw new LangException (52,"The command 'escape' demands the attributes 'id' and 'type'");
		}
		
		if ( r_type.equals("prop")) {
			InterProp togo = getPropById ( r_id );
			
			if ( togo != null ) {
				String propVal = togo.valueE;
				String propValTrans = Entities.importEntities(propVal);
			
				// togo.setValue(elementE)
				togo.valueE = propValTrans;
				
				// PropList.remove(togo);
			} else {
				String msg = "The prop '"+Entities.exportEntities(r_id)
						+"' was requested to escape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(53,msg);
			}
			
		} else if ( r_type.equals("file")) {
			InterFile togo = getFileById ( r_id );

			if ( togo != null ) {
				String propVal = togo.future_fn;
				String propValTrans = Entities.importEntities(propVal);
				togo.future_fn = propValTrans;
			} else {
				String msg = "The file '"+Entities.exportEntities(r_id)
						+"' was requested to escape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(54,msg);
			}
		} else if ( r_type.equals("dir")) {
			InterDir togo = getDirById ( r_id );

			if ( togo != null ) {
				String propVal = togo.value;
				String propValTrans = Entities.importEntities(propVal);
				togo.value = propValTrans;
				
			} else {
				String msg = "The prop '"+Entities.exportEntities(r_id)
						+"' was requested to escape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(55,msg);
			}
		} else {
			throw new LangException (56, "The task 'escape' demands an attributes 'type' with the value prop,dir or file");
		}
		break;
	} // end case
	
	case c_unescape : {
		// ----------------------------------------------------------------------- 

		String r_id = etE.getAttributeValue("id");
		String r_type = etE.getAttributeValue("type");
		if ( r_id == null || r_type == null ) {
			throw new LangException (57,"The task 'export' demands the attributes 'id' and 'type'");
		}
		
		if ( r_type.equals("prop")) {
			InterProp togo = getPropById ( r_id );
			
			if ( togo != null ) {
				String propVal = togo.valueE;
				String propValTrans = Entities.exportEntities(propVal);
				
				togo.valueE = propValTrans;
				// PropList.remove(togo);
			} else {
				String msg = "The prop '"+Entities.exportEntities(r_id)
						+"' was requested to unescape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(58,msg);
			}
			
		} else if ( r_type.equals("file")) {
			InterFile togo = getFileById ( r_id );

			if ( togo != null ) {
				String propVal = togo.future_fn;
				String propValTrans = Entities.exportEntities(propVal);
				togo.future_fn = propValTrans;
			} else {
				String msg = "The file '"+Entities.exportEntities(r_id)
						+"' was requested to unescape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(59,msg);
			}
		} else if ( r_type.equals("dir")) {
			InterDir togo = getDirById ( r_id );

			if ( togo != null ) {
				String propVal = togo.value;
				String propValTrans = Entities.exportEntities(propVal);
				togo.value = propValTrans;
				
			} else {
				String msg = "The dir '"+Entities.exportEntities(r_id)
						+"' was requested to unescape, but it does not exist."
						+" [step:]"+taskExplainerCompact(etE);
				throw new LangException(60,msg);
			}
		} else {
			throw new LangException (61,"The task 'unescape' demands an attributes 'type' with the value prop,dir or file, but it found: "+r_type);
		}
		break;
	} // end case	
	
	case c_echo : {
		// ----------------------------------------------------------------------- 
		Level level = LogConnector.getLevel(etE.getAttributeValue("level"));
		if ( etE.getAttributeValue("id")!=null) {			
			String msg = "";
			if ( etE.getAttribute("label")!=null)
				msg = Entities.exportEntities(etE.getAttributeValue("label"))+": ";
			String thetype = etE.getAttributeValue("type");
			// lc.warn ("the type = ["+thetype+"]");
			if ( thetype == null )
				thetype="prop";
			
			try {						

				String tosay = 
						"ECHO: "
						+ msg+"("
						+ thetype+","
						+ etE.getAttributeValue("id")
						+ ") <=> "
						+ Entities.exportEntities(getVar(thetype, etE.getAttributeValue("id")))
						+ " // internal: " 
						+ getVar(thetype, etE.getAttributeValue("id"))
						;

				Sp.p(tosay);
				lc.log(tosay, level);
							
			} catch (Exception e1) {
				String tosay = "ECHO: "+msg+"("+
						thetype+","+
						etE.getAttributeValue("id")+") : The variable does not exist";
				Sp.p(tosay);
				lc.log(tosay, level);					
			}
		} else {
			String echo;
			if ( e.getAttribute("label")!=null)
				echo = "ECHO: "+Entities.exportEntities(etE.getAttributeValue("label"))+": ";
			else
				echo ="ECHO: ";
			String content = Commons.getXValue(etE);
			String put = echo+ Entities.exportEntities(content)+ " // internal: "+content;
			Sp.p(put);
			lc.log(put, level);
			
		}
		break;
	} // end case	
	
	case c_sequence : {
		// -----------------------------------------------------------------------
		String rId = etE.getAttributeValue("id");
		if ( rId == null )
			throw new LangException(62,"task 'sequence' needs attribute 'id', in:"+taskExplainer(etE));

		String act = etE.getAttributeValue("act");
		if ( act==null)
			throw new LangException(63,"taks 'sequence' needs attribute 'act' - open, next or close");
				
		if ( act.equals("next")) {
			InterSequence s = getSequenceById(rId, 
							"task 'sequence' act 'next'", true);
			s.sequence.getNext();
			
		} else if ( act.equals("open")) {
			
			String filename = InterSequence.getFilnameOfElement(etE);
			
			for ( int i = 0; i<sequenceList.size(); i++ ) {
				InterSequence sequence = sequenceList.get(i);
				if ( rId.equals(sequence.id) ) {
					String msg = "The sequence '"+Entities.exportEntities(rId)
							+"' exists already.";
					throw new LangException(64,msg);	

				}
				if ( filename.equals(sequence.getFnEscaped()) ) {
					String msg = "A sequence with the filename '"+Entities.exportEntities(filename)
							+"' is already in use. It has the id '"+
							Entities.exportEntities(sequence.id)+"'.";
					throw new LangException(65,msg);	

				}		
				
			}

			InterSequence neu = new InterSequence ( etE,isitzung );
			sequenceList.add(neu);			
			
		} else if (act.equals("close")) {
			InterSequence s = getSequenceById(rId, 
					"task 'sequence' act 'close'", true);
						
			s.close();
			sequenceList.remove(s);
			lc.debug("Sequence '"+Entities.exportEntities(etE.getAttributeValue("id"))+"' was closed.");

		} else {
			throw new LangException(66,"taks 'sequence' needs proper attribute 'act' - open, next or close - but found: "+act);
	
		}
		
		break;
	} // end case 
	
	case c_report_coops : { 
		// ----------------------------------------------------------------------- 
		Level requestLevel = LogConnector.getLevel(etE.getAttributeValue("level"));
	
		reportCoops2Log(
				Entities.exportEntities(etE.getAttributeValue("label")),
				requestLevel,
				false,
				CoreBuilder.rslogger.loggingWouldTakePlace(requestLevel));
		break;
	} // end case
	/*
	case c_sequence_close : {
		
		// ----------------------------------------------------------------------- 
		InterSequence foundSequence = null;
		for ( int i = 0; i<sequenceList.size(); i++ ) {
			InterSequence sequence = sequenceList.get(i);
			if ( sequence.id.equals(et.getAttributeValue("id"))) {
				foundSequence = sequence;
				break;
			}
		}
		if ( foundSequence != null ) {			
			
			foundSequence.close();
			sequenceList.remove(foundSequence);
			lc.debug("Sequence '"+Entities.exportEntities(et.getAttributeValue("id"))+"' was closed.");
		} else {
			String msg = "#798 Task 'sequence_close' did not find a sequence with id '"+
					Entities.exportEntities(et.getAttributeValue("id"))
					+"'";
			throw new LangException(msg);
		}
		break;
	} */ // end case	
	
	case c_log_plus : {
		// ----------------------------------------------------------------------- 

		CoreBuilder.rslogger.logNodeOpen(
				etE.getAttribute("id"), 
				etE.getAttribute("label"),
				LogConnector.getLevel(etE.getAttributeValue("level")));
	
		break;
	} // end case
	
	case c_log_minus : {
		// ----------------------------------------------------------------------- 
		CoreBuilder.rslogger.logNodeClose(
				null,
				null,
				LogConnector.getLevel(etE.getAttributeValue("level")),
				"false");
		break;
	} // end case	
	case c_ti_space : {
		// ----------------------------------------------------------------------- 
		terminator.executeSomething(etE, tiLogLevel, this); 
		break;
	} // end case	
	
	
	case c_tx_style : {
		// ----------------------------------------------------------------------- 
		terminator.executeSomething(etE, tiLogLevel, this); 
		break;
	} // end case	
	
	case c_dump_all :
	case c_dump :
	case c_dump_ti :
	case c_dump_ti_all : {

		// ----------------------------------------------------------------------- 
		boolean not_all = false;
		if (cmd==c_dump || cmd==c_dump_ti )
			not_all = true;

		boolean to_ti = false;
		if (cmd==c_dump_ti || cmd==c_dump_ti_all )
			to_ti = true;
		else
			to_ti = Commons.hasFlag(etE, "ti");
		
		// getting the right level
		debugall(not_all, 
				LogConnector.getLevel(etE.getAttributeValue("level"))
				,etE.getAttributeValue("label"),
				to_ti);
		
		break;
		
	} // end case	
	
	case c_include : {
		// -----------------------------------------------------------------------
		// GESTRICHEN
		
		break;
	} // end case	
	
	case c_global : {
		// ----------------------------------------------------------------------- 
		String global_id = etE.getAttributeValue("id");
		String global_act = etE.getAttributeValue("act");
		
		if ( global_id == null || global_act == null )
			throw new LangException(68,"task 'global' needs attributes 'id' and 'act'. 'act' can be 'set' or 'get' or 'remove'."
					);
		
		if ( global_act.equals("set")) {
			if ( etE.getAttribute("x")==null)
				throw new LangException(69,"task 'global' with 'set' needs attribute 'x' with a value to store");
			global_map.put(global_id, etE.getAttributeValue("x"));				
			Sp.p("######### SET global ["+global_id+"] to ["+etE.getAttributeValue("x")+"]");
		
		} else if ( global_act.equals("get")) {
			String neuer_wert = global_map.get(global_id);	
			Element efi = new Element("prop");
			if ( etE.getAttribute("as_id") == null )
				efi.setAttribute("id", global_id);
			else	
				efi.setAttribute("id", etE.getAttributeValue("as_id"));
			efi.setText(neuer_wert);
			Sp.p("######### GET global ["+global_id+"] with value ["+neuer_wert+"] went to propId ["+efi.getAttributeValue("id")+"]");
			
			addProp(efi,false);
			// if ( Commons.hasFlag(etE, "remove"))
				global_map.remove(global_id);
		} else if ( global_act.equals("remove")) {
			global_map.remove(global_id);
		}
		break;
	} // end case	
	
	case c_tp : {
		// ----------------------------------------------------------------------- 
		String msg = etE.getAttributeValue("x");
		if ( msg== null)
			msg = etE.getText();
		myState = msg;
		
		if ( Commons.hasFlag(etE, "ok")) {
			to_ok = to_ok + " { "+msg+" }";
		} else {
			terminator.setProgress(msg,true);					
		}
		break;
	} // end case 
	
	case c_tk : {
		// ----------------------------------------------------------------------- 
		if ( etE.getAttribute("top_label") != null ) {
			Element top_level_tk = new Element("tk");
			top_level_tk.setAttribute("label", etE.getAttributeValue("top_label"));
			top_level_tk.addContent(etE);
			terminator.executeTk(top_level_tk,tiLogLevel,appid,appdir,instid,instdir,this);
		} else
			terminator.executeTk(etE,tiLogLevel,appid,appdir,instid,instdir,this);
		break;
	} // end case
	
	case c_ti_break : {
		// ----------------------------------------------------------------------- 
		terminator.tiBreak(etE);
		break;
	} // end case	
	
	case c_ti_hr : {
		// ----------------------------------------------------------------------- 
		terminator.tiHr(etE);
		break;
	} // end case
	
	case c_ti_hr_break: 
	case c_ti_break_hr: {
		// ----------------------------------------------------------------------- 
		terminator.tiBreakHr(etE);
		break;
	} // end case
	
	case c_ti_clear : {
		// ----------------------------------------------------------------------- 
		terminator.tiClear(etE);
		break;
	} // end case		
	
	case c_vfs : {
		// ----------------------------------------------------------------------- 
		//terminator.tiHr(etE);
		if ( etE.getAttribute("act") == null )
			throw new Exception("needs act eg scan_ftp");
		if ( etE.getAttributeValue("act").equals("scan_ftp")) {
			
			Sp.p("im c_vfs");
			FileSystemOptions opts = new FileSystemOptions();
			
			FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
			FtpFileSystemConfigBuilder.getInstance( ).setUserDirIsRoot(opts,true);
			// ftp://speedtest.tele2.net
			// FileObject fo = VFS.getManager().resolveFile("ftp://demo:password@test.rebex.net", opts);
			String x = etE.getAttributeValue("dir");
			if ( x== null )
				throw new Exception("braucht dir");
			
			String oto = etE.getAttributeValue("out");
			if ( oto== null )
				throw new Exception("braucht out");
			Element r = new Element("result");
			FileObject fo = VFS.getManager().resolveFile("ftp://anonymous:password@speedtest.tele2.net", opts);
			Sp.p("im c_vfs 2");
			FileObject[] n = fo.getChildren();
			Sp.p("im c_vfs 3");
			for (int i = 0; i < n.length; i++) { 
				  
				FileObject jetzt = n[i];
				
				FileName fn = jetzt.getName();
				Element y;
				if ( jetzt.isFile() ) y=new Element("file");
				else y = new Element("dir");
				y.setAttribute("publicURI", jetzt.getPublicURIString());
				y.setAttribute("baseName", fn.getBaseName());
				y.setAttribute("URI", fn.getURI());
				// y.setAttribute("publicURI", jetzt.getPublicURIString());
				r.addContent(y);
	            Sp.p(i + "isfile="+jetzt.isFile()+" isfolder="+jetzt.isFolder() +" "+jetzt.getPublicURIString()+
	            		" "+fn.getBaseName() +" "+ fn.getExtension()+" "+fn.getFriendlyURI()
	            + " "+fn.getPathDecoded()+" "+fn.getType().getName());
	        } 
			Document doc = new Document(r);
			CommonsVfs.writeToFile(doc, oto,isitzung);
			Sp.p("im c_vfs 4");
			String directoryname = "ftp://demo:password@test.rebex.net";			
		}
		
			// TODO Auto-generated constructor stub
		/*
			FileObject fo2 = Commons.getFileObjectByFilename(directoryname);
			FileObject[] r =  fo2.getChildren();
			Sp.p("im c_vfs fertig");
			*/
		
		// FileObject listendir =  fsManager.resolveFile("ftp://"+username+":"+password+"@"+server+"/data/" + "xxxx/",opts);
		break;
	} // end case	
	
	case c_ti_flush : {
		// ----------------------------------------------------------------------- 
		terminator.flush();
		terminator.bottom();
		break;
	} // end case
	
	case c_ti_bottom : {
		// ----------------------------------------------------------------------- 
		if ( etE.getAttributeValue("on") == null )
			ti_bottom = true;
		else {
			String on =  etE.getAttributeValue("on");
			// das muss noch getestet werden
			ti_bottom = Commons.stringtoBoolean(on, "@26 Evaluating attribute 'on' of task 'ti_bottom'");
			/*
			if ( on.equals("0"))
				ti_bottom = false;
			else if ( on.equals("1"))
				ti_bottom = true;
			else
				throw new LangException("#56 "+"ti_bottom, attribute 'on' must be 1/0, but it is: "+on);
			*/
		}
		break;
	} // end case
		
	case c_insert : {
		// ----------------------------------------------------------------------- 
		throw new LangException(70,"passed an insert file="+etE.getAttributeValue("file"));
		// break;
	} // end case		
				
	case c_inline : {
		// ----------------------------------------------------------------------- 
		// seems to be leftover from a loop
		break;
	} // end case	
	
	case c_commit : {
		// ----------------------------------------------------------------------- 
		// seems to be leftover from a loop
		break;
	} // end case	
	
	case c_rollback : {
		// ----------------------------------------------------------------------- 
		// seems to be leftover from a loop
		if ( etE.getAttribute("path") == null )
			throw new Exception("rollback nees attribute @path");
		
		String dirNameNatural = Entities.exportEntities(instdir+etE.getAttributeValue("path"));
		FileSystemManager fsm = CoreBuilder.fsm;
		
		FileObject dir = fsm.resolveFile(dirNameNatural);
		if ( dir.exists() && dir.isFolder()) {
			CommonsVfs.deleteDir(dir, isitzung);
			// dir.deleteAll();asdf
			Sp.p("Deleted dir: "+Entities.exportEntities(dirNameNatural));
		} else
			throw new Exception("That is not an existing dir: "+Entities.exportEntities(dirNameNatural));

		break;
	} // end case	
		
	case c_restart : {
		// ----------------------------------------------------------------------- 
		// dont_clear = true;
		/*
		if ( Commons.hasFlag(et, "exit"))
			System.exit(0); */
		if ( !isitzung.isOnline() ){
		if ( etE.getAttribute("computer") != null ) {
			CoreBuilder.ws.updateLastComputer(Entities.exportEntities(etE.getAttributeValue("computer")));
		}
		
		if ( etE.getAttribute("app") != null && etE.getAttribute("pro") != null) {
			lc.warn("Interpreter called Restart with app="+etE.getAttributeValue("app")+", pro="+etE.getAttributeValue("pro"));
			terminator.restart(
					Entities.exportEntities(etE.getAttributeValue("app")), 
					Entities.exportEntities(etE.getAttributeValue("pro")));
		} else if (etE.getAttribute("app") != null) {
			lc.warn("Interpreter called Restart with app="+etE.getAttributeValue("app"));
			terminator.restart(
					Entities.exportEntities(etE.getAttributeValue("app")), 
					"");					
		} else {
			lc.warn("Interpreter called Restart");
			terminator.restart();
		}
		} else {
			lc.warn("Im Online kein Restart!-!");
		}
		break;
	} // end case	
	
	case c_ti_switch : {
		// ----------------------------------------------------------------------- 
		String send_app = null;
		if ( etE.getAttribute("app") != null ) 
			send_app = etE.getAttributeValue("app");
		String send_pro = null;
		if ( etE.getAttribute("pro") != null ) 
			send_pro = etE.getAttributeValue("pro");
		
		try {
			// chif: switch auf sich selber hat ein problem
			terminator.tiSwitch(
					Entities.exportEntities(send_app), 
					Entities.exportEntities(send_pro));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			lc.trace("---- exception thrown by [ interpreter ti-switch-problem ] ------------------------");
			
			Sp.p("---- exception thrown by [ interpreter ti-switch-problem ] ------------------------");
			
		}
		break;
	} // end case
	
	case c_interpreter : {
		// ----------------------------------------------------------------------- 
		// not documented!
		if ( etE.getAttribute("act") != null && etE.getAttributeValue("act").equals("check-login")) {
			
			/*
			FileObject theObject = CoreBuilder.fsm.resolveFile(
					isitzung.getSessionHomeMem() +"/temp_prox/"+Commons.getTsHourly()+".txt"
					); */

			if ( isitzung.isLoggedIn());
			else
				nichtWeiter= new Exception("No Login performed unitl now.");				

						
		} else if ( etE.getAttribute("act") != null && etE.getAttributeValue("act").equals("do-login-direct")) {
			/*
			login_user = etE.getAttributeValue("login_user");
			login_pwd = etE.getAttributeValue("login_pwd");
			login_role = etE.getAttributeValue("login_role"); */
			
			login_user = "Gast1";
			login_pwd = "schm3aus";
			login_role = "user";
			
			nichtWeiter = performLoginInternal();
			
			
		} else if ( etE.getAttribute("act") != null && etE.getAttributeValue("act").equals("do-login")) {
			// System.out.println("----------------- do-login");
			String attempted = isitzung.nextLoginAttempt();
			// System.out.println("----------------- attempted="+attempted);
			if ( attempted != null )
				nichtWeiter = new Exception(attempted);
			else
				nichtWeiter = performLoginInternal();

		} else {
			/*
			if ( etE.getAttribute("suspend") != null ) {
				
				String on = etE.getAttributeValue("talk");
				boolean on2 = Commons.stringtoBoolean(on, "@31 Evaluating attribute 'talk'");
				if ( on2 ) {
					lc.debug("interpreter was set to: talk=1");
					lc.core_on();
				} else {
					lc.core_off();
					lc.debug("interpreter was set to: talk=0");
				}
			}		*/	
			if ( etE.getAttribute("buttons") != null ) {
				String buttons = etE.getAttributeValue("buttons");
				if ( buttons.equals("button"))
					is_buttons_by_button = true;
				else if ( buttons.equals("nobutton"))
					is_buttons_by_button = false;
				else
					throw new LangException("interpreter button muss button oder nobutton sein. ");
			}
			
			if ( etE.getAttribute("talk") != null ) {
				String on = etE.getAttributeValue("talk");
				boolean on2 = Commons.stringtoBoolean(on, "@31 Evaluating attribute 'talk'");
				if ( on2 ) {
					lc.debug("interpreter was set to: talk=1");
					lc.core_on();
				} else {
					lc.core_off();
					lc.debug("interpreter was set to: talk=0");
				}
			}
			if ( etE.getAttribute("jovial") != null ) {
				String jovial = etE.getAttributeValue("jovial");
				boolean jovial2 = Commons.stringtoBoolean(jovial, "@32 Evaluating attribute 'jovial'");
				if ( jovial2) {
					is_jovial = true;
					lc.log("interpreter was set to: jovial=1",CoreBuilder.rslogger.getLevel());
				} else {
					is_jovial = false;	
					lc.log("interpreter was set to: jovial=0",CoreBuilder.rslogger.getLevel());
				}
			}
			
			if ( etE.getAttribute("turbo") != null ) {
				String turbo = etE.getAttributeValue("turbo");
				boolean turbo2 = Commons.stringtoBoolean(turbo, "@32b Evaluating attribute 'turbo'");
				if ( turbo2) {
					is_not_turbo = false;
				} else {
					is_not_turbo = true;	
				}
			}		
			
			if ( etE.getAttribute("level") != null ) {
				lc.setLevel(etE.getAttributeValue("level"));
			}
			
			if ( etE.getAttribute("math_scale") != null ) {
				math_scale = Commons.string2int(etE.getAttributeValue("math_scale"));
				lc.log("interpreter was set to: math_scale="+math_scale,CoreBuilder.rslogger.getLevel());
			}		
			
			if ( etE.getAttribute("math_round") != null ) {
				math_round = Commons.RoundMethodCode(etE.getAttributeValue("math_round"));
				lc.log("interpreter was set to: math_scale="+etE.getAttributeValue("math_round"),CoreBuilder.rslogger.getLevel());
			}		
			
			// not documented, from ti_log
			if ( etE.getAttribute("ti_level") != null ) {
				String tl = etE.getAttributeValue("ti_level");
				if ( tl.equals("off"))
					tiLogLevel=null;
				else
					tiLogLevel=LogConnector.getLevel(tl);					
			}

			if ( etE.getAttribute("watch") != null ) {
				String watch = etE.getAttributeValue("watch");
				boolean watch2 = Commons.stringtoBoolean(watch, "@33 Evaluating attribute 'watch'");
				if ( watch2 ) {
					ti_log_vars = true;
					lc.log("interpreter was set to: watch=1",CoreBuilder.rslogger.getLevel());
				} else  {
					ti_log_vars = false;	
					lc.log("interpreter was set to: watch=0",CoreBuilder.rslogger.getLevel());
				}
			}		
			
			if ( etE.getAttribute("add_file_logger") != null ) {
				//InterLogg neu = new InterLogg ( etE ); nic123 mal rausgenommen
				//LogList.add(neu);
			}
			
			if ( etE.getAttribute("new_file_logger") != null ) {
				throw new Exception("no new_file_logger, add_file_logger");
			}		

			if ( etE.getAttribute("language") != null ) {
				boolean wasUpdatedLang = isitzung.setLang(etE.getAttributeValue("language"));
				if ( !isitzung.isOnline() ) {
					if ( wasUpdatedLang ) {
						CoreBuilder.ws.updateLastLang(etE.getAttributeValue("language"));
						terminator.restart(appid, getProId());
						
					}
				}
				
			}		
			
			if ( etE.getAttribute("act") != null && etE.getAttributeValue("act").equals("reload")) {
				ArrayList<Element> configListxy = CoreBuilder.db.getExecutionPrologExtended();
				if ( configListxy.size() == 1 ) {
					Element theconf = configListxy.get(0);
					// System.out.println("... having ... "+taskExplainer(theconf));
					List<Element> childs = theconf.getChildren();
					addToStackSimple(childs);
					
				}
				/*
				System.out.println("size="+configListxy.size());
				Iterator ixx = configListxy.iterator();
				while ( ixx.hasNext()) {
					Element e1 = (Element)ixx.next();
					System.out.println("... having ... "+taskExplainer(e1));
					addToStackPlain(e1);
				} */
					 
				
			}				
			
		}
		if ( nichtWeiter != null )
			throw nichtWeiter;
		
		break;
	} // end case
		
	case c_var :	
	case c_parallel :
	case c_block :
	case c_call :
	case c_end:
	case c_begin :
	case c_case :
	case c_alt :
	/*case c_p :
	case c_f :
	case c_d :{
		// ----------------------------------------------------------------------- 
		throw new LangException(71,"Task '"+en+"' is not possible, reserved for calls out of ti");	
	} */ // end case				
	/*
	case c_ : {
		// ----------------------------------------------------------------------- 

		break;
	} // end case			
	*/
	default: { 	weiter = true; 
				depressive("Achtung: dieser tasks wird in der zentrale nicht abgefangen:"+en);
				break;
	
	}
} // ende vom gesamten case-block
} // ende vom if-then-else cmd==-1
// musik
if ( weiter ) {
	PlugTryTaskResult ttr = null;;
	// try {
			
		Entities.exportEntities(etE);

	
		PlugTryTask x = new PlugTryTask( etE, this, jobtodo,TaskdefMap);
		ttr = x.run(this);
		// lc.warn("aaaaaaaaaaaaaaaaaaaaaaa");
	
	
	if ( ttr==null || !ttr.executed ) {
		throw new LangException(73,"The task '"+etE.getName()+"' is not defined in XmlRobot-Language!");	
	}
		
	if ( ttr.retvals != null && ttr.retvals.getParameters() != null) {
		HashMap<String, String> varList = ttr.retvals.getParameters();
		// aus ttr
		Set<String> keySet = varList.keySet();
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;

		while ( i.hasNext() ) {
			aktKey = i.next();
			if ( aktKey.startsWith("prop_")) {
				Element efi = new Element("prop");
				efi.setAttribute("id", Entities.importEntities(aktKey.substring(5)));
				efi.setText(Entities.importEntities(varList.get(aktKey)));
				
				addProp(efi,false);
			} else if ( aktKey.startsWith("file_")) {
				Element efi = new Element("file");
				efi.setAttribute("id", Entities.importEntities(aktKey.substring(5)));
				efi.setText(Entities.importEntities(varList.get(aktKey)));
				addFile(efi,true);
			} else if ( aktKey.startsWith("dir_")) {
				Element efi = new Element("dir");
				efi.setAttribute("id", Entities.importEntities(aktKey.substring(4)));
				efi.setText(Entities.importEntities(varList.get(aktKey)));
				addDir(efi,true);
			} else {
				/*
				Element efi = new Element("prop");
				efi.setAttribute("id", Entities.importEntities(aktKey));
				efi.setText(Entities.importEntities(varList.get(aktKey)));
				addProp(efi);	*/						
			}
			
		}					
	}
} else if ( nichtWeiter != null )
	throw nichtWeiter;
/*
} 
// sektion 9: errorcatcher
catch (Exception ex) {

	if ( !isStepDomain) {
		Element ret = new Element("ti");
	
		Element lc1 = new Element("ti");
		lc1.setAttribute("type", "text");
		lc1.setAttribute("label", "Problem: ");
		try {
			lc1.setText(Commons.createHtmlEntities(Commons.toTextCompact(et)));
		} catch (Exception ex3) {
			// TODO Auto-generated catch block
			lc1.setText(Commons.toTextCompact(et));
			lc.fatalException("There seems to be an xml problem with translated: et="+et,ex3);
			throw ex3;
		}
		ret.addContent(lc1);	
		// nic123
	
		try {
			lc.fatalException(Commons.toTextCompact(et), null, ex, job.getJobid(), job.getFullfn());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			lc.warn("Problem in doing something");
		}
	}
	throw ex;
} */
} // hier endet die musik
/*
public static void exportElementxx ( Element retval ) throws Exception {
	
	List<Attribute> attributeList = retval.getAttributes();
	Iterator<Attribute> it1 = attributeList.iterator();
	while ( it1.hasNext()) {
		Attribute attribute = it1.next();
		attribute.setValue(Entities.exportEntities(attribute.getValue()));	
	}


	List<Element> elementeList = retval.getChildren();
	Iterator<Element> it2 = elementeList.iterator();
	while ( it2.hasNext()) {
		Element x = it2.next();
		exportElement(x);
	}	

	if (  retval.getChildren().size() == 0 ) {
		// there are no child elements
		String textvalue = retval.getText();
		if ( textvalue != null && textvalue.length()>0 ) {
			retval.setText(Entities.exportEntities(textvalue));
		}			
	}

}	*/
private String c_translate ( String valueE, String additionalErrorMsg, boolean checkSicherheitsstufe ) throws Exception {

	return (translateString(valueE));

	
}

private String c_translateXXX ( String valueE, String additionalErrorMsg, boolean checkSicherheitsstufe ) throws Exception {
	// das mit der sicherheitsstufe war ein früher ansatz um 
	// das unbefugte lesen/schreiben von verzeichnissen zu steuern
	// hat sich mit dem berechigungssystem total arledigt.
	// ist auch aus dem arcadia rausgenommen
	/* sicherheiststufe */ final int sicherheitsstufe = 4;
	
	Sp.pl("sicherheitsstufe = " + sicherheitsstufe+ " received = " + valueE );
	String lastResult = null;
	if ( !checkSicherheitsstufe ) {
		lastResult = translateString(valueE);
	} else if ( sicherheitsstufe == 1 ) {
		// nichts uebersetzen
		lastResult = valueE;
	} else if ( sicherheitsstufe == 2 ) {
		// (dir,pro) am anfang
		if ( valueE.startsWith("(dir,pro)") ) {
			lastResult = instdirEscaped + valueE.substring(9);
		} else if ( valueE.startsWith("(dir,libro)") ) {
			lastResult = librodirEscaped + valueE.substring(11);
		} else
			lastResult = valueE;
	} else if ( sicherheitsstufe == 3 ) {
		if ( valueE.startsWith("(dir,pro)") ) {
			lastResult = instdirEscaped + valueE.substring(9);
		} else if ( valueE.startsWith("(dir,app)") ) {
			lastResult = appdirEscaped + valueE.substring(9);
		} else if ( valueE.startsWith("(dir,libro)") ) {
			lastResult = librodirEscaped + valueE.substring(11);
		} else
			lastResult = valueE;
	} else if ( sicherheitsstufe == 4 ) {
		// alles uebersetzen
		lastResult = translateString(valueE);
	} else {
		// nichts uebersetzen
		lastResult = valueE;
	}
	// in dem lastResult ist nun das escapte ergebnis
	// hier duerfen keine weiteren funktionen mehr enhalten sein
	// System.out.println("s1 lastResult = ["+lastResult+"]");
	String pruefung = Entities.isValidExpression(lastResult);

	if ( pruefung != null ) {

		String info = "sicherheitsstufe '"+sicherheitsstufe+"' verletzt von: value=["+valueE+"] lastResult=["+lastResult+"] msg=["+pruefung+"]"
				+" addMsg=["+additionalErrorMsg+"]";

		
		Sp.pAlarmSicherheitsstufe(info);

		lastResult = info;
		throw new Exception(info);
	}

	return lastResult;
	
}
private void c_translate ( Element etE ) throws Exception {
	// ----------------------------------------------------------------------- 
	String r_id = etE.getAttributeValue("id");
	String r_type = etE.getAttributeValue("type");

	if ( r_id == null || r_type == null ) {
		throw new LangException (47,"The task 'translate' demands the attributes 'id' and 'type'");
	}
	
	// kwm5
	InterProp interProp = null;
	InterFile interFile = null;
	InterDir interDir = null;
	String valueE = null;
	
	int vt = kwmvtyp.convert(r_type);

	switch ( vt ) {
	case kwmvtyp_prop: {	
		interProp = getPropById ( r_id );
		
		if ( interProp != null ) {
			valueE = interProp.valueE;
		} else {
			String msg = "The prop '"+Entities.exportEntities(r_id)
					+"' was requested to translate, but it does not exist."
					+" [step:]"+taskExplainerCompact(etE);
			throw new LangException(48, msg);				
		}
		break;
	}
	case kwmvtyp_file: {	
		interFile = getFileById ( r_id );

		if ( interFile != null ) {
			valueE = interFile.future_fn;
		} else {
			String msg = "The file '"+Entities.exportEntities(r_id)
					+"' was requested to translate, but it does not exist."
					+" [step:]"+taskExplainerCompact(etE);
			throw new LangException(49,msg);	
		}
		break;
	}
	case kwmvtyp_dir: {	
		interDir = getDirById ( r_id );

		if ( interDir != null ) {
			valueE = interDir.value;			
		} else {
			String msg = "The dir '"+Entities.exportEntities(r_id)
					+"' was requested to translate, but it does not exist."
					+" [step:]"+taskExplainerCompact(etE);
			throw new LangException(50,msg);	
		}
		break;
	}}	
	
	
	// valueE wird nun uebersetzt
	String lastResult = c_translate(valueE,"SimpleTranslateAmalie",false);
	
	if ( interProp != null )
		interProp.valueE= lastResult;
	else if ( interFile != null )
		interFile.future_fn = lastResult;
	else if ( interDir != null )
		interDir.value = lastResult;
	
}


private Element translateJobelement ( 
		Element _element, 
		boolean cloneIt, 
		int childLevels, 
		boolean remove_test_attribute ) throws Exception {
	// wrapper for cloning
	Element retval = null;
	if ( cloneIt) {
		retval=(Element)_element.clone();
		if ( remove_test_attribute && retval.getAttribute("test")!=null)
			retval.removeAttribute("test");
	} else
		retval = _element;
	
	translateJobelement2 ( retval, childLevels );			
	return retval;
}

private void translateJobelement2 ( Element retval, int childLevels  ) throws Exception {
	boolean stayCool = Commons.hasFlag(retval, "cool");
	// translations will finaly be done by translateValue
	// Iteriere Attribute, alle attribute übersetzen ausser while-attribut in loop-element
	// boolean is_loop_element = retval.getName().equals("loop");
	List<Attribute> attributeList = retval.getAttributes();
	Iterator<Attribute> it1 = attributeList.iterator();
	while ( it1.hasNext()) {
		Attribute attribute = it1.next();
		
		// if ( !(is_loop_element && attribute.getName().equals("while"))) {

				attribute.setValue(translateValueMy(attribute.getValue(),stayCool));
		// }
	}

	if ( childLevels != 0 ) {
		int newChildLevels = childLevels -1;
		List<Element> elementeList = retval.getChildren();
		Iterator<Element> it2 = elementeList.iterator();
		while ( it2.hasNext()) {
			// Element x = (Element)it2.next();
			translateJobelement2(it2.next(),newChildLevels );
		}	
	}

	int child_size = retval.getChildren().size();
	if ( child_size == 0 ) {
		// there are no child elements
		String textvalue = retval.getText();
		if ( textvalue != null && textvalue.length()>0 ) {
			
			retval.setText(translateValueMy(textvalue,stayCool));
		}			
	}/* else {
		Sp.p("- - - - - -ZERO  ["+retval.getName()+"] zero ["+retval.getText()+"]");			
	} */


}	



private String translateValueMy( String workon, boolean stayCool ) throws Exception {
	// lc.core_on(LogConnector.CORE_TRANSLATE);
	// lc.core("==> "+workon,LogConnector.CORE_TRANSLATE);
	// lc.core_on();
	// lc.core("called translateValue('"+workon+"')",LogConnector.CORE_TRANSLATE);
	// debug ("zu uebersetzen            ["+workon+"]");
	
	// (0) superescape
	String first = translateSuperEscape(workon);
	// (1) Variablen
	String retval;
	if (workon.indexOf("(loop,") == -1) {
			retval = translateVariablesSpeed01_ALL_NEW(first,true,true);
		} else {
			String second = translateVariablesSpeed01_ALL_NEW(first,true,false); 
			retval = translateVariablesSpeed01_ALL_NEW(second,false,true);			
		}
 
		// (2) Expressions
	Expression expr =null;
	try {
		expr = new Expression(retval);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		lc.addExceptionIfNew(e,e);
	}
	if (expr == null ) {
		throw new LangException(1,"This expression is crap: "+retval);
	}		
	Vcar vc = expr.evaluate(this);

	
	// (3) Exporters
	String afterExporters = translateExporters1Gas(vc.s);
	if (is_not_turbo && !stayCool ) {
		// System.out.println("afterExporters="+afterExporters);
		String errorMsg = Entities.isValidExpression(afterExporters);
		if (errorMsg != null )
			throw new LangException(2,"Expression after translation not valid: "+errorMsg);		
	}

	/*
	String afterExporters = translateExporters1(vc.s);
	String afterExportersGas = translateExporters1Gas(vc.s);
	if ( !afterExporters.equals(afterExportersGas)) {
		String mymsg = "altes und neues translateExporters1/Gas liefert unterschiedliche Ergebnisse"
				+"[afterExporters]"+afterExporters+" [afterExportersGas]"+afterExportersGas; 
		Sp.p(mymsg);
		throw new Exception ("#65 "+mymsg );
	}
	*/
	//System.out.println("TRANS: "+afterExporters);
	//System.out.println("       "+StringEscapeUtils.escapeJava(afterExporters));
	if ( !debugOn ) return afterExporters; // abkuerzung
	
	// retval = vc.s;
	// lc.core("    ..> ==> "+retval,LogConnector.CORE_TRANSLATE);
	
	if ( debugOn && CoreBuilder.rslogger.isDebug_filter_expressions() && !workon.equals(afterExporters)) {

		lc.debug_expressions("= "+workon+" <=> "+afterExporters);
		
		
	}
	
	return afterExporters;
}

private String translateSuperEscape ( String w ) throws Exception {
	int pos = 0;
	StringBuffer ret = new StringBuffer();
	// Chunklist erstellen

	while ( pos < w.length()) {
		int klammerauf = w.indexOf("()",pos);
		
		
		if ( klammerauf == -1 ) {
			// no superescape found
			ret.append(w.substring(pos));
			break;
		} 
		
		int klammerzu = w.indexOf("()",klammerauf+2);
		// int klammerzu =Commons.getClosingBracket ( w, klammerauf+2);
		if ( klammerzu == -1 ) {
			throw new LangException(3,"superescape didn't find closing brackets, expected [()] in ["+w.substring(klammerauf)+
					"] complete string was ["+w+"]");
		} else {
			
			// found closer for superescape
			if ( klammerauf > pos ) {
				// und vorher gibts auch was
				ret.append(w.substring(pos,klammerauf));
			} 				
			String tosuperescape = w.substring(klammerauf+2,klammerzu);
			
			ret.append(Entities.importEntities(tosuperescape));
			pos = klammerzu+2;
			
		}

	} // ende der while schleife
		
	return ret.toString();
	
}

private String translateVariablesSpeed01_ALL_NEW( String w, boolean vars, boolean loops) throws Exception {

	
	int pos = 0;
	StringBuffer ret = new StringBuffer();
	// Chunklist erstellen

	while ( pos < w.length()) {
		int klammerauf = w.indexOf("(",pos);
		
		// keine klammer auf, fertig
		if ( klammerauf == -1 ) {
			ret.append(w.substring(pos));
			break;
		} 
		// akjsldf
		int klammerzu =Commons.getClosingBracket ( w, klammerauf+1);
		// int klammerzu = w.indexOf(")",klammerauf+1);
		// fail when keine closing braket gefunden
		if ( klammerzu == -1 ) {
			throw new LangException(4,"no closing bracket found [request to interpret:] "+w.substring(klammerauf));
		}
		// das was vor der klammer auf ist ausgeben und naechste position setzen
		if ( klammerauf > pos ) 
			ret.append(w.substring(pos,klammerauf)); 

		
		// dass muss bearbeitet werden
		String work = w.substring(klammerauf,klammerzu+1);
		
		String beg = w.substring(klammerauf+1);
		boolean found = false;
		if ( vars ) {
			if ( beg.startsWith("prop,")) {
				found = true;					 
				ret.append(translateProp(work));			
			} else if ( beg.startsWith("file,")) {
				found = true;					 
				ret.append(translateFile(work));			
			} else if ( beg.startsWith("dir,")) {
				found = true;					 
				ret.append(translateDir(work));			
			}// else if ( beg.startsWith("sequence,")) {
			 //	throw new Exception("found a sequence: "+beg);
				// found = true;					 
				// ret.append(translateSequence(work));			
			// }
		}
		
		if ( !found && loops && beg.startsWith("loop,")) {
			found = true;					 
			ret.append(translateLoop(work));			
		}
		
		if ( found ) {
			pos = klammerzu+1; 
		} else {
			// es ist zwar eine klammer auf, aber nichts zum uebersetzen
			// irgendeine anderer ausdruck wie f oder x
			// zumindest pruefen ob irgendwann auch die passende klammer zu kommt
			int klammerzu2 = Commons.getClosingBracket(w, klammerauf+1);

			if ( klammerzu2 == -1 )
				throw new LangException(5,"No closing bracket found in: ["+ w.substring(klammerauf)+"] complete string is ["+w+"]");


			ret.append(w.substring(klammerauf,klammerauf+1));

			pos = klammerauf+1;	
		}

	} // ende der while schleife

	/*
	if ( debugOn && CoreBuilder.rslogger.isDebug_filter_expressions() ) 
			Sp.p("<<<             TO: "+ret.toString());
			*/		
		
	return ret.toString();
	
}	// end translateVariablesSpeed01_ALL_NEW
private boolean is_jovial = false;
private boolean is_not_turbo = true;
private boolean is_buttons_by_button = true;
private String translateProp( String in ) throws Exception { 
	InterProp prop = propMap.get(in.substring(6,in.length()-1));
	if ( prop!= null)
		return prop.valueE;
	else if ( is_jovial )
		return new String("");
	else 
		throw new LangException(6,"We are not jovial! Unknown: (prop," + in.substring(6,in.length()-1)+") "+ getExcutingElementDocu());
	
}	
private String translateFile( String in ) throws Exception {
	
	InterFile file = fileMap.get(in.substring(6,in.length()-1));
	if ( file!= null)
		return file.future_fn;		
	else if ( is_jovial )
		return new String("");
	else
		throw new LangException(7,"We are not jovial! Unknown: (file," + in.substring(6,in.length()-1)+") "+ getExcutingElementDocu());
}
private String translateDir( String in ) throws Exception {
	InterDir dir = dirMap.get(in.substring(5,in.length()-1));
	if ( dir!= null)
		return dir.value;
	else if ( is_jovial )
		return new String("");
	else
		throw new LangException(8,"We are not jovial! Unknown: (dir," + in.substring(5,in.length()-1)+") "+ getExcutingElementDocu());
}
private String translateLoop( String in ) throws Exception{
	
	
	// XmlLoopList
	String retval = null;
	for ( int i = 0; i<loopListXpath.size() && retval == null; i++ ) {
		InterLoopXml loop = loopListXpath.get(i);
		retval = loop.translate(in);
	}
	
	// loopListRegex
	for ( int i = 0; i<loopListRegex.size() && retval == null; i++ ) {
		InterLoopRegex loop = loopListRegex.get(i);
		retval = loop.translate(in);
	}
	
	// loopListRegex
	for ( int i = 0; i<loopListSelect.size() && retval == null; i++ ) {
		InterLoopSelect loop = loopListSelect.get(i);
		retval = loop.translate(in);
	}	
	
	// WhileLoopList
	for ( int i = 0; i<loopListWhile.size() && retval == null; i++ ) {
		InterLoopWhile loop = loopListWhile.get(i);
		retval = loop.translate(in);
	}	
	
	if ( retval == null ) {
		if ( is_jovial )
			retval= new String("");
		else
			throw new LangException(9,"We are not jovial. loop didn't deliver a value. "
					+" [Request] "+Entities.exportEntities(in));			
	}
				
	return retval;
}	

@Override
public String sequenceFun(String paraN1, String paraN2) throws Exception {
	// TODO Auto-generated method stub
	
	if ( paraN1 == null )
		throw new LangException("needs sequence-id as first parameter");
	String paraE1 = Entities.importEntities(paraN1);
	InterSequence thisSequence = null;
	for ( int i = 0; i<sequenceList.size() && thisSequence == null; i++ ) {
		InterSequence sequence = sequenceList.get(i);
		if ( sequence.id.equals(paraE1))
			thisSequence = sequence;
	}	
	if ( thisSequence == null )
		throw new LangException("Did not find sequence id '"+paraN1+"'");
	
	if ( paraN2 == null ) {
		return thisSequence.sequence.getLast();
	} else if ( paraN2.equals("?")) {
		return thisSequence.sequence.getCandidate();
	} else if ( paraN2.equals("candidate?")) {
		return thisSequence.sequence.getCandidate();
	}
	throw new LangException("Unknown functionparameter: "+
			paraN2+" of sequence '"+paraN1+"'");
}

private String translateSequence( String in ) throws Exception{
	
	String retval = null;
	for ( int i = 0; i<sequenceList.size() && retval == null; i++ ) {
		InterSequence sequence = sequenceList.get(i);
		retval = sequence.translate(in);
	}
	if ( retval == null ) {
		//if ( is_jovial )
		//	retval= new String("");
		// else
			throw new LangException(10,"Unknown (sequence,"+in+",...) [in]" + in+ " [step]:"+getExcutingElementDocu());			
	};
	
	return retval;		
}
/*
private String translateValue2_speed02( String in ) throws Exception {
	// lc.core("called translateValue2('"+in+"')",LogConnector.CORE_TRANSLATE);
	
	// PropList
	if ( in.startsWith("(prop,")) {
		return translateProp( in);			
	}		
	
	// FileList
	else if ( in.startsWith("(file,")) {
		return translateFile( in);		
	}
	
	// DirList		
	else if ( in.startsWith("(dir,")) {			
		return translateDir( in);
	}
	
	else if ( in.startsWith("(loop,")){
		return translateLoop( in);
		
	} else if ( in.startsWith("(sequence,")) {
		return translateSequence( in);
	} else {
		throw new Exception("Hey Nic, there is a problem in translateValue2_speed02: "+in);
	}
}	
*/
/*
private String translateExporters1( String w ) throws Exception {
	
	int pos = 0;
	StringBuffer ret = new StringBuffer();
	// Chunklist erstellen

	while ( pos < w.length()) {
		int klammerauf = w.indexOf("(",pos);
		
		
		if ( klammerauf == -1 ) {
			// lc.core("[translateValue] detected lyrics 1: "+w+"",LogConnector.CORE_TRANSLATE);
			// es gibt keine klammer auf mehr
			//ret = ret.concat(w.substring(pos));
			ret.append(w.substring(pos));
			break;
		} 
		
		String beg = w.substring(klammerauf+1);
		if (	beg.startsWith("x,") ){
			
			// int klammerzu = w.indexOf(")",klammerauf+1);
			int klammerzu = Commons.getClosingBracket(w, klammerauf+1);
			if ( klammerzu != -1 ) {
				// es wurde auch eine klammer zu gefunden
				if ( klammerauf > pos ) {
					// und vorher gibts auch was
					ret.append(w.substring(pos,klammerauf));
				} 
		
				ret.append(translateExporters2(w.substring(klammerauf,klammerzu+1)));
				pos = klammerzu+1;

			} else {
				// es wurde keine klammerzu gefunden				
				ret.append(w.substring(pos));
				break;
			}
		} else {
			// es ist zwar eine klammer auf, aber nichts zum uebersetzen
			ret.append(w.substring(pos,klammerauf+1));
			pos = klammerauf+1;
		}
	}

	
	return ret.toString();
	
}	// end translateExporters1	
*/
private String translateExporters1Gas( String w ) throws Exception {
	
	int pos = 0;
	StringBuffer ret = new StringBuffer();
	// Chunklist erstellen

	while ( pos < w.length()) {
		int klammerauf = w.indexOf("(x,",pos);
				
		if ( klammerauf == -1 ) {
			// lc.core("[translateValue] detected lyrics 1: "+w+"",LogConnector.CORE_TRANSLATE);
			// es gibt keine klammer auf mehr
			//ret = ret.concat(w.substring(pos));
			ret.append(w.substring(pos));
			break;
		} else {

			int klammerzu = Commons.getClosingBracket(w, klammerauf+3);
			if ( klammerzu > -1 ) {
				// es wurde auch eine klammer zu gefunden
				if ( klammerauf > pos ) {
					// und vorher gibts auch was
					ret.append(w.substring(pos,klammerauf));
				} 

				
				ret.append(Commons.escapeXml(w.substring(klammerauf+3,klammerzu)));
				pos = klammerzu+1;

			} else {
				// es wurde keine klammerzu gefunden
				throw new LangException(11,"didnt find closing bracket for '(x,' happend in: "+w);
			}
		}
	}
	
	return ret.toString();
	
}	// end translateExporters1
/*
private String translateExporters2( String in ) throws Exception {
	// lc.core("called translateValue2('"+in+"')",LogConnector.CORE_TRANSLATE);
	

		String content = in.substring(3,in.length()-1);
		if ( content!= null)
			return Commons.cdata_xml(content);
		else
			return in;			
}	
*/
/*
private String translateExporters2Gas( String in ) throws Exception {
	// lc.core("called translateValue2('"+in+"')",LogConnector.CORE_TRANSLATE);
	return Commons.cdata_xml(in.substring(3,in.length()-1));
			
}*/	

private void debugall(boolean only_unlocked, 
		Level level, 
		String label, 
		boolean toTi ) {
	
	if ( !isitzung.hasPrivDump()) {
		//System.out.println("surrounding does not allow dumping !!!");
	}
	if ( isitzung.hasPrivDump() ) {
		
		
	String mylabel = label;
	boolean toTi2 = CoreBuilder.rslogger.loggingWouldTakePlace(level);

	
	if ( label == null ) {
		if ( only_unlocked )
			mylabel ="Dump";
		else
			mylabel ="Dump All";
	}
	
	String tilabel = mylabel;
	mylabel = Entities.exportEntities(mylabel);
		
	lc.log("* "+mylabel.toUpperCase()+ " " +Commons.StringTimes("-", 70-mylabel.length()),level);

	// PropList

	lc.log("* prop:",level);

	if ( toTi && toTi2) {
		Element ti_top = new Element("ti");
		ti_top.setAttribute("label",tilabel);
		ti_top.setAttribute("size","2");
		try {
			terminator.executeTi(ti_top, tiLogLevel,appid,appdir,instid,instdir,this);
		} catch (Exception e) {}
	}
	
	Element ti_vars = new Element("ti");
	try { if ( toTi && toTi2 ) {
			ti_vars.setAttribute("label","prop");
			terminator.executeTi(ti_vars, tiLogLevel,appid,appdir,instid,instdir,this);
		}
	} catch (Exception e1) {}
	
	boolean do_all = !only_unlocked;
	Element ti_var = new Element("ti");

	Set<String> propKeySet = new TreeSet<String>(propMap.keySet());
	Iterator<String> propIter = propKeySet.iterator();
	int propIndex = 0;

	while ( propIter.hasNext() ) {
		propIndex++;
		InterProp aktProp = propMap.get(propIter.next());
		
		aktProp.debugx(propIndex,only_unlocked,level,"* ");
		try {
			if ( toTi && toTi2 ) {
				if ( do_all || !aktProp.locked ) {
					ti_var.setAttribute("label","(e:o)prop(e:s)"+Entities.importEntities(aktProp.idE)+"(e:c)");
			
					ti_var.setAttribute("x",aktProp.valueE);
					ti_var.setAttribute("type","text");
					terminator.executeTi(ti_var, tiLogLevel,appid,appdir,instid,instdir,this);				
				}				
			}
		} catch (Exception e1) {}
	}	
	ti_var.setAttribute("flag","flow");
	// FileList
	try { if ( toTi && toTi2 ) {
			ti_vars.setAttribute("label","file");
			terminator.executeTi(ti_vars, tiLogLevel,appid,appdir,instid,instdir,this);
		}
	} catch (Exception e1) {}
	
	lc.log("* file:",level);	

	Set<String> fileKeySet = new TreeSet<String>(fileMap.keySet());
	Iterator<String> fileIter = fileKeySet.iterator();
	int fileIndex = 0;
	while ( fileIter.hasNext() ) {
		fileIndex++;
		InterFile aktFile = fileMap.get(fileIter.next());
		
		aktFile.debugx(fileIndex,only_unlocked, level,"* ");
		
		try {
			if ( toTi && toTi2 ) {
				if ( do_all || !aktFile.locked ) {
					ti_var.setAttribute("label","(e:o)file(e:s)"+Entities.importEntities(aktFile.id)+"(e:c)");					
					ti_var.setAttribute("x",aktFile.future_fn);
					ti_var.setAttribute("type","file");
					terminator.executeTi(ti_var, tiLogLevel,appid,appdir,instid,instdir,this);
					ti_var.setAttribute("label"," ");					
					ti_var.setAttribute("type","text");
					terminator.executeTi(ti_var, tiLogLevel,appid,appdir,instid,instdir,this);
				}
			}
		} catch (Exception e1) {}			
	}		
	
	// DirList
	try { if ( toTi  && toTi2) {
		ti_vars.setAttribute("label","dir");
		terminator.executeTi(ti_vars, tiLogLevel,appid,appdir,instid,instdir,this);
	}} catch (Exception e1) {}
	lc.log("* dir:",level);	
	
	Set<String> dirKeySet = new TreeSet<String>(dirMap.keySet());
	Iterator<String> dirIter = dirKeySet.iterator();
	int dirIndex = 0;
	while ( dirIter.hasNext() ) {
		dirIndex++;

		InterDir aktDir = dirMap.get(dirIter.next());
		aktDir.debugx(dirIndex,only_unlocked, level,"* ");
		try {
			if ( toTi && toTi2 ) {
				if ( do_all || !aktDir.getIsLocked() ) {
					ti_var.setAttribute("label","(e:o)dir(e:s)"+Entities.importEntities(aktDir.id)+"(e:c)");
					ti_var.setAttribute("x",aktDir.value);
					ti_var.setAttribute("type","dir");
					terminator.executeTi(ti_var, tiLogLevel,appid,appdir,instid,instdir,this);					
					ti_var.setAttribute("label"," ");					
					ti_var.setAttribute("type","text");
					terminator.executeTi(ti_var, tiLogLevel,appid,appdir,instid,instdir,this);					

				}}} catch (Exception e1) {}			
	}
	
	if ( !only_unlocked ) {
		// SequenceList
		for ( int i = 0; i<sequenceList.size(); i++ ) {
			sequenceList.get(i).debugx(i,level);
		}

		// LoopList-While
		for ( int i = 0; i<loopListWhile.size(); i++ ) {
			loopListWhile.get(i).debugx(i,level);
		}
		
		// LoopList-Xpath
		for ( int i = 0; i<loopListXpath.size(); i++ ) {
			loopListXpath.get(i).debugx(i,level);
		}
		
		// LoopList-Regex
		for ( int i = 0; i<loopListRegex.size(); i++ ) {
			loopListRegex.get(i).debugx(i,level,lc);
		}
		
		// LoopList-Select
		for ( int i = 0; i<loopListSelect.size(); i++ ) {
			loopListSelect.get(i).debugx(i,level,lc);
		}		

		// LogList
		lc.log("* log4j:",level);	
		for ( int i = 0; i<LogList.size(); i++ ) {
			LogList.get(i).debugx(i,level,"* ");
		}
		
		CoreBuilder.rslogger.systemdebug(level,"* ");
		
		// Taskdef
		debugxTaskdef(level);
		
		debugxFundef(level);
		
		reportCoops2Log (null,level,false,toTi  && toTi2);
									
		/*  config */			
		{			
			Set<String> keySet = (remember_paras.getParameters()).keySet();
			Iterator<String> i = keySet.iterator();
			String   aktKey = null;
			int index = 0;
			while ( i.hasNext() ) {
				aktKey = i.next();

				if ( index == 0 )
					lc.log(declareDebug
							+Commons.trailBlanks("config",lb)
							+Commons.leadBlanks(aktKey, lb2)
							+"= "
							+ remember_paras.getParameter(aktKey)
							,level);
				else
					lc.log(declareDebug
							+Commons.trailBlanks("",lb)
							+Commons.leadBlanks(aktKey, lb2)
							+"= "
							+ remember_paras.getParameter(aktKey)
							,level);			
				
				index++;
			}	
		}		
	}
	}
}
private void setProgressPlaintext ( String message, boolean do_log_info ) {
	terminator.setProgress(Entities.importEntities(message),do_log_info);
}
public static boolean shouldBeValidated ( Element e ) {
	String flags = e.getAttributeValue("flag");
	if ( flags != null ) {
		if ( flags.indexOf("validate") != -1 )
			return true;			
	}
	return false;
}


private String elementToText(Element e) {
	
      XMLOutputter serializer = new XMLOutputter();
      Format f = Format.getPrettyFormat();
      f.setIndent("");
      // f.setEncoding("UTF-8");
      f.setLineSeparator("");
      f.setTextMode(TextMode.PRESERVE);
      serializer.setFormat(f);
      
      String x = serializer.outputString(e);

      
      // String z = x.replaceAll("Â", "");
      // Sp.p("...    z="+z);
      x=x.replaceAll("\n", "");
      x=x.replaceAll("\r", "");
      return x;
	
}

//
//  Lock
//	
private void addLock ( Element eE ) throws Exception {

	InterLock neu = new InterLock(eE);
	
	// kwm6
	int vt = kwmvtyp.convert(neu.type);

	switch ( vt ) {
	case kwmvtyp_prop: {	
		InterProp alt = getPropById(neu.id);
		if ( alt != null )
			alt.locked=true;
		else {
			throw new LangException(87,"task 'lock' didnt find the prop '"+neu.id+"'");
		}
		break;
	}
	case kwmvtyp_file: {	
		InterFile alt = getFileById(neu.id);
		if ( alt != null )
			alt.locked=true;
		else {
			throw new LangException(13,"task 'lock' didnt find the file '"+neu.id+"'");
		}
		break;
	}
	case kwmvtyp_dir: {	
		InterDir alt = getDirById(neu.id);
		if ( alt != null )
			alt.setLockOn();
		else {
			throw new LangException(12,"task 'lock' didnt find the dir '"+neu.id+"'");
		}
		break;
	}}	

	LockList.add(neu);
}
			
//
//  Dir
//

private void addDir ( Element element, boolean attackreport ) throws Exception {
	// System.out.println("-- adding dir: "+Commons.toTextCompact(element));
	if ( attackreport )
		System.out.println(taskExplainerCompact(element));
	
	if ( element.getAttribute("id")==null)
		throw new Exception("missing @id in dir-specification.");
	String id = element.getAttributeValue("id");
	if ( id.startsWith("sys_")) {
		throw new LangException(88,"Directories with id starting sys_ are reserved.");
	}
	InterDir alt = getDirById(id);

	if ( alt!=null && alt.getIsLocked() ) {
		System.out.println ("**********************************************************");		
		System.out.println ("**********************************************************");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked [ dir ]");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked [ dir ] with id [ "+
				id+" ] with value: " + getX(element));
		
		System.out.println ( Commons.getTsMs()+": Attempt to set locked dir with id [ "+
				id+" ] with value: " + getX(element));
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");		
		throw new Exception("Modification of dir '"+id+"' is not possible, its LOCKED !!");
	} else if ( alt != null ) 
		alt.setValue(element);
	else {
		alt = new InterDir(element);
		dirMap.put(id, alt);			
	}
	if ( Commons.hasFlag(element, "lock")) {
		alt.setLockOn();
	}
	
	if ( ti_log_vars ) {
		InterDir x = getDirById(element.getAttributeValue("id"));
		Element ti1 = new Element("ti");
		ti1.setAttribute("label", "dir '"+element.getAttributeValue("id")+"' was set to:");
		ti1.setAttribute("x",x.value);
		ti1.setAttribute("type","dir");
		terminator.executeTi(ti1, tiLogLevel,appid,appdir,instid,instdir,this);
		Sp.p(
				"Did set dir '"+element.getAttributeValue("id")
				+"' to '"+x.value+"'");
		//terminator.flush(true);
		//terminator.bottom();
		
	}		
	
}

private InterDir getDirById ( String id ) {
	return ( dirMap.get(id));
			
}

//
//  File
//
private void addFile ( Element element, boolean attackreport ) throws Exception {
	// System.out.println("-- adding file: "+Commons.toTextCompact(element));
	if ( attackreport )
		System.out.println(taskExplainerCompact(element));
	
	String id = element.getAttributeValue("id");
	if ( id==null)
		throw new Exception("Define an attribute 'id' for the file-variable");
	if ( id.startsWith("sys_")) {
		throw new LangException(89,"Files with id starting sys_ are reserved.");
	}
	InterFile alt = getFileById(id);

	if ( alt!=null && alt.locked ) {
		System.out.println ("**********************************************************");		
		System.out.println ("**********************************************************");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked [ file ]");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked [ file ] with id [ "+
				id+" ] with value: " + getX(element));
		
		System.out.println ( Commons.getTsMs()+": Attempt to set locked file with id [ "+
				id+" ] with value: " + getX(element));
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");			
		throw new Exception("Modification of file '"+id+"' is not possible, its LOCKED !!");
	} else if ( alt != null ) 
		alt.setValue(element);
	else {
		fileMap.put(id, new InterFile(element));			
	}
	
	
	if ( ti_log_vars ) {
		InterFile x = getFileById(element.getAttributeValue("id"));
		Element ti1 = new Element("ti");
		ti1.setAttribute("label", "file '"+element.getAttributeValue("id")+"' was set to:");
		ti1.setAttribute("x",x.future_fn);
		ti1.setAttribute("type","file");
		terminator.executeTi(ti1, tiLogLevel,appid,appdir,instid,instdir,this);	
		Sp.p(
				"Did set file '"+element.getAttributeValue("id")
				+"' to '"+x.future_fn+"'");		
		//terminator.flush(true);
		//terminator.bottom();
	}				
}

private InterFile getFileById ( String id ) {
	return ( fileMap.get(id));

	
}


	


private void debugxTaskdef(Level level) {
	
	Set<String> keyset = TaskdefMap.keySet();
	Iterator<String> iterator = keyset.iterator();
	int index = 0;

	while ( iterator.hasNext() ) {
		String name  = iterator.next();
		PlugTaskdef aktValue = TaskdefMap.get(name);
		if ( index == 0)
			lc.log("* taskdef:",level);

		lc.log("* "+Interpreter.declareDebug
			+Commons.leadBlanks(aktValue.name, Interpreter.lb2)
			+" =  "
			+aktValue.classname 
			+" in "
			+ aktValue.getClassLoaderDesciption()
			,level);
		/*
		lc.log(Interpreter.declareDebug
				+Commons.leadBlanks(" ", Interpreter.lb2)
				+"    "
				+aktValue.getUrlString()
				,level);	*/
		index++;
	}
}

private void debugxFundef(Level level) {
	
	Set<String> keyset = FundefMap.keySet();
	Iterator<String> iterator = keyset.iterator();
	int index = 0;

	while ( iterator.hasNext() ) {
		String name  = iterator.next();
		PlugFundef aktValue = FundefMap.get(name);
		if ( index == 0)
			lc.log("* fundef:",level);

		lc.log("* "+Interpreter.declareDebug
			+Commons.leadBlanks(aktValue.name, Interpreter.lb2)
			+" =  "
			+aktValue.classname
			+ " in " + aktValue.getUrlString()
			,level);
		/*
		lc.log(Interpreter.declareDebug
				+Commons.leadBlanks(" ", Interpreter.lb2)
				+"    "
				+aktValue.getUrlString()
				,level);	
				*/
		index++;
	}
}
		
	
//
//  Sequence
//	
private InterSequence getSequenceById ( String idE, String whoAsks, boolean fail ) throws LangException {
	if ( idE == null )
		throw new LangException(90,whoAsks+" needs attribute 'id'");
	for ( int i = 0; i<sequenceList.size(); i++ ) {
		InterSequence sequence = sequenceList.get(i);
		if ( sequence.id.equals(idE)) {
			return sequence;
		}
	}	
	
	if ( fail ) {
		String msg = whoAsks+" did not find a sequence with id '"+
				Entities.exportEntities(idE)
				+"'";
		throw new LangException(91,msg);		
	} else
		return null;
	
}


private void addSequence ( Element element ) throws Exception  {
	String rId = element.getAttributeValue("id");
	if ( rId == null )
		throw new LangException(92,"task 'sequence' needs attribute 'id', found:"+taskExplainer(element));
	
	String filename = InterSequence.getFilnameOfElement(element);
	
	for ( int i = 0; i<sequenceList.size(); i++ ) {
		InterSequence sequence = sequenceList.get(i);
		if ( rId.equals(sequence.id) ) {
			String msg = "The sequence '"+Entities.exportEntities(rId)
					+"' exists already.";
			throw new LangException(93,msg);	

		}
		if ( filename.equals(sequence.getFnEscaped()) ) {
			String msg = "A sequence with the filename '"+Entities.exportEntities(filename)
					+"' is already in use. It has the id '"+
					Entities.exportEntities(sequence.id)+"'.";
			throw new LangException(94,msg);	

		}		
		
	}

	InterSequence neu = new InterSequence ( element,isitzung );
	sequenceList.add(neu);			


}

private void closeAllSequences() throws IOException, StorageLimitException {
	for ( int i = 0; i<sequenceList.size(); i++ ) {
		InterSequence sequence = sequenceList.get(i);
		sequence.close();
	}
}

//
//  Log
//	



private void closeAllLogs() throws IOException {
	for ( int i = 0; i<LogList.size(); i++ ) {
		InterLogg logg = LogList.get(i);
		try {
			logg.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("Could not remove all appenders "+e.getLocalizedMessage()+" Printing Stack @27");
			
		}
	}
}

//
//  Prop
//
String getX ( Element elementE) throws Exception {
	
	String returner;
	if ( elementE.getAttribute("x") != null )
		returner = elementE.getAttributeValue("x");
	else
		returner = elementE.getText();
	return returner;	

}
private void addProp ( Element elementE, boolean attackreport ) throws Exception {
	// System.out.println("-- adding property: "+Commons.toTextCompact(elementE));
	if ( attackreport )
		System.out.println(taskExplainerCompact(elementE));
	
	String id = elementE.getAttributeValue("id");
	if ( id.startsWith("sys_")) {
		throw new LangException(95,"Properties with id starting sys_ are reserved.");
	}
	InterProp alt = getPropById(id);

	if ( alt!=null && alt.locked ) {
		System.out.println ("**********************************************************");		
		System.out.println ("**********************************************************");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked property");
		System.out.println ( Commons.getTsMs()+": Attempt to set locked property with id [ "+
				id+" ] with value: " + getX(elementE));
		
		System.out.println ( Commons.getTsMs()+": Attempt to set locked property with id [ "+
				id+" ] with value: " + getX(elementE));
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");
		System.out.println ("**********************************************************");
		
		throw new Exception("Modification of prop '"+id+"' is not possible, its LOCKED !!");
	} else if ( alt != null ) 
		alt.setValue(elementE);
	else {
		propMap.put(id, new InterProp(elementE));			
	}
	
	if ( ti_log_vars ) {
		InterProp x = getPropById(elementE.getAttributeValue("id"));
		Element ti1 = new Element("ti");
		ti1.setAttribute("label", "prop '"+elementE.getAttributeValue("id")+"' was set to:");
		ti1.setAttribute("x",x.valueE);
		terminator.executeTi(ti1, tiLogLevel,appid,appdir,instid,instdir,this);
		//terminator.flush(true);
		//terminator.bottom();
		Sp.p(
				"Did set prop '"+elementE.getAttributeValue("id")
				+"' to '"+x.valueE+"'");		
		
	}
		
}
	
private void addPropSys ( Element elementE ) throws Exception {
	
	String id = elementE.getAttributeValue("id");

	InterProp alt = getPropById(id);

	if ( alt!=null && alt.locked ) 
		throw new Exception("Modification of prop '"+id+"' is not possible, its LOCKED !!");
	else if ( alt != null ) 
		alt.setValue(elementE);
	else {
		propMap.put(id, new InterProp(elementE));			
	}
	
	if ( ti_log_vars ) {
		InterProp x = getPropById(elementE.getAttributeValue("id"));
		Element ti1 = new Element("ti");
		ti1.setAttribute("label", "prop '"+elementE.getAttributeValue("id")+"' was set to:");
		ti1.setAttribute("x",x.valueE);
		terminator.executeTi(ti1, tiLogLevel,appid,appdir,instid,instdir,this);
		//terminator.flush(true);
		//terminator.bottom();
		Sp.p(
				"Did set prop '"+elementE.getAttributeValue("id")
				+"' to '"+x.valueE+"'");		
		
	}
		
}
private InterProp getPropById ( String id ) {
	return propMap.get(id);
}
/*
private void logAttention() {
	lc.warn("**");
	lc.warn("** *****************************************************************************");
	lc.warn("** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** **");
	lc.warn("** *****************************************************************************");
	lc.warn("**");		
}
*/
/*
private Element getJobelementsSetFail(boolean truefalse) {
	Element e = new Element("config");
	
	Element e1 = new Element("prop");
	e1.setAttribute("id", "fail");
	e1.setText(String.valueOf(truefalse));
	e.addContent(e1);
	return e;
}
*/


public Vcar executeFunction(String fn, ArrayList<Vcar> vcar_list) throws Exception {


	try {
		PlugTryFunResult tryFunResult = null;
		Vcar result = null;	
		PlugTryFun tryFun = new PlugTryFun(fn, vcar_list, this, FundefMap);
		tryFunResult = tryFun.run();
		
		if ( !tryFunResult.executed ) {
			throw new LangException(96,"The function '"+fn+"' was not found in XmlRobot-Language.");
		} else {
			result = tryFunResult.vcar;
			if ( result == null ) {
				lc.warn("!!! executeFunction delivers nothing !!!");
				result = new Vcar("???");
			}
		}
		return ( result );
		
	} catch (Exception e) {
		
		String y = "(f,"+fn;
		Iterator<Vcar> i = vcar_list.iterator();
		while ( i.hasNext() ) {
			Vcar value = i.next();
			y = y + "," + value.s; 
		}
		y = y + ")";
		
		String errorString = "Problem in executing a function: "+y+ ", "+e.getMessage();

		//lc.fatalException(errorString,e);
		lc.debug_expressions(errorString);
		
		throw ( e );
	}		
}

// RequestHandler



private static int sequence_temp_file = 0;

public String getTempFileN(String prefix, String postfix) {
	sequence_temp_file++;
	String a = prefix;
	if ( a == null ) a = "temp";
	String b = postfix;
	if ( b == null ) b = "";
	
	return ( Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.DIR_SYSTEMP))+a+Commons.int2String(sequence_temp_file)+b );
}	
/*
private boolean rememberIsTrue ( String parameter ) {
	String value = remember_paras.getParameter(parameter);
	if ( value == null )
		return false;
	else if ( value.equals("true"))
		return true;
	return false;
}*/


/*
private String translateExportValue(String x) throws Exception {
	return Entities.exportEntities(translateValueMy(x));
}	
*/


private final String loginPrefix="login_";
private String login_user = null;
private String login_pwd = null;
private String login_role = null;
private boolean login_pdb_background = false;

public synchronized void setVar(String varType, String varIdE, String valueE) throws Exception {
	// System.out.println("Interpreter #"+interpreterId+ " was requested to write: "+varType+"/"+varIdE+" = ["+valueE+"]");
	Element x = new Element(varType);
	x.setAttribute("id", varIdE);
	x.setText(valueE);
	// kwm7
	int vt = kwmvtyp.convert(varType);

	switch ( vt ) {
	case kwmvtyp_prop: {	
		if ( varIdE.startsWith(loginPrefix)) {
			if ( varIdE.equals(loginPrefix+"user")) {
				boolean ueberschreiben = false;
				if ( valueE.length() > 0 && login_user!=null )
					ueberschreiben = true;
				
				if ( valueE != null && valueE.length() > 0 )
					login_user = valueE.toLowerCase();
				// System.out.println("login_user: ueberschreiben= "+ueberschreiben);
				if ( ueberschreiben ) {
					x.setText("x");
					// System.out.println("ueberschriebn: "+valueE);
				}
				
			} else if ( varIdE.equals(loginPrefix+"pwd")) {
				boolean ueberschreiben = false;
				if ( valueE.length() > 0 && login_pwd!=null )
					ueberschreiben = true;
				// System.out.println("login_pwd: ueberschreiben= "+ueberschreiben);
				if ( valueE != null && valueE.length() > 0 )
					login_pwd = valueE;
				
				if ( ueberschreiben ) {
					x.setText("x");
					//System.out.println("ueberschriebn: "+valueE);
				}
			} else if ( varIdE.equals(loginPrefix+"pdbxx")) {

				if ( valueE != null && valueE.length() > 0 )
					login_pdb_background = Commons.stringToBool(varIdE);
				

			}   else if ( varIdE.equals(loginPrefix+"role")) {
				boolean ueberschreiben = false;
				if ( valueE.length() > 0 && login_role!=null )
					ueberschreiben = true;
				// System.out.println("login_role: ueberschreiben= "+ueberschreiben);
				
				if ( valueE != null && valueE.length() > 0 )
					login_role = valueE;
				/*
				if ( ueberschreiben ) {
					x.setText("x");
				} */
			}						 
		} 
		addProp(x,false);
		break;
	}
	case kwmvtyp_file: {	
		addFile(x,false);
		break;
	}
	case kwmvtyp_dir: {	
		addDir(x,false);
		break;
	}}	
		
}
@Override
public synchronized void setVarFollow(String varType, String varIdE, String valueE,
		String followJobN) throws Exception {
	/*
	Sp.p("setVarFollow varType=["+varType+"] "
			+" varIdE=["+varIdE+"] "
			+" valueE=["+valueE+"] "
			+" followJobN=["+followJobN+"] "
			);	*/
	
	String currentValueE = null;
	
	boolean has_been_updated = false;
	try {
		currentValueE = getVar(varType,varIdE);

		if ( currentValueE.equals(valueE))
			has_been_updated = false;
		else
			has_been_updated = true;
	} catch (Exception e) {
		// ok, just not defined
		has_been_updated = true;

	}
	setVar(varType,  varIdE,  valueE);
	//if ( has_been_updated ) { 1.5.23 follow_job immer ausführen
		// going to call follow_job
		// Sp.p("Interpreter.setVarFollow: is going to call FOLLOW-JOB-N ["+followJobN+"]");
		try {
			callFollowJob ( varType, varIdE, valueE, followJobN );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
	//} 
		
}
private synchronized void callFollowJob ( 
		String varType, 
		String varIdE, 
		String valueE,
		String followJobN ) throws Exception {

	/*
	Sp.p("callFollowJob varType=["+varType+"] "
			+" varIdE=["+varIdE+"] "
			+" valueE=["+valueE+"] "
			+" followJobN=["+followJobN+"] "
			);
			*/
		

		String loglabel = "|| "+" follow variable "+varType+","+varIdE;
		String logid = followJobN;
		
		// keine ahnung was das mal war
		// executeExecutionElements( getJobelementsSetFail(false).getChildren(), job);
		//boolean pm =  CoreBuilder.rslogger.is_debug_plus;
		//if ( pm ) CoreBuilder.rslogger.logNodeOpenDirect( logid, loglabel,null);	

		// this is the job to execute
		Element innerCall = null;
		if ( isSystemcall )
			innerCall = new Element("call_service");
		else
			innerCall = new Element("call_job");
		
		innerCall.setAttribute("reason","follow: "+followJobN);
		innerCall.setAttribute("job",followJobN);
		innerCall.setAttribute("flag","catch");
		
		// prop,follow_var_id
		Element set_id = new Element("prop");
		set_id.setAttribute("id", "sys_follow_id");
		set_id.setAttribute("x", varIdE);		
		//innerCall.addContent(set_id);
		addPropSys(set_id);
		
		// prop,follow_var_type
		Element set_type = new Element("prop");
		set_type.setAttribute("id", "sys_follow_type");
		set_type.setAttribute("x", varType);		
		//innerCall.addContent(set_type);
		addPropSys(set_type);
		
		// prop,follow_var_type
		Element set_value = new Element("prop");
		set_value.setAttribute("id", "sys_follow_value");
		set_value.setAttribute("x", valueE);			
		//innerCall.addContent(set_value);
		addPropSys(set_value);

		
		
		try {
			addToStackPlain(innerCall);
			Sp.p("Starting follow-job: ");
			TkTicketManager finishedByTk = stackRotatorCatched();	
			Sp.p("Ending follow-job: ");
			if ( finishedByTk != null ) {
				// follow-job can not use tk
				throw new Exception("Follow job '"+followJobN+"' cant use tk!!");
			}

		} catch (Exception e1) {
			// ein exception im follow-job
			// if ( pm ) CoreBuilder.rslogger.logNodeCloseDirect( logid, loglabel,null);					
			throw e1;
		}
		
		// if ( pm ) CoreBuilder.rslogger.logNodeCloseDirect( logid, loglabel,null);
							
}
/*
private synchronized void callFollowJobFASTALT ( 
		String varType, 
		String varIdE, 
		String valueE,
		String followJobN ) throws Exception {

	Sp.p("callFollowJob varType=["+varType+"] "
			+" varIdE=["+varIdE+"] "
			+" valueE=["+valueE+"] "
			+" followJobN=["+followJobN+"] "
			);
		
		DbJob nextJob = jobConstr.getJm().selectJob(followJobN);
		
		if ( nextJob == null ) {
			throw new Exception("The follow_job '"+followJobN+"' was not found in the dir '"+jobConstr.getJm().getDir()+"'");
		}

		String loglabel = "|| "+" follow variable "+varType+","+varIdE;
		String logid = followJobN;
		
		// keine ahnung was das mal war
		// executeExecutionElements( getJobelementsSetFail(false).getChildren(), job);
		boolean pm =  CoreBuilder.rslogger.is_debug_plus;
		if ( pm ) CoreBuilder.rslogger.logNodeOpenDirect( logid, loglabel,null);	
		
		// prop,follow_var_id
		Element set_id = new Element("prop");
		set_id.setAttribute("id", "sys_follow_id");
		set_id.setAttribute("x", varIdE);		
		addPropSys(set_id);
		
		// prop,follow_var_type
		Element set_type = new Element("prop");
		set_type.setAttribute("id", "sys_follow_type");
		set_type.setAttribute("x", varType);		
		addPropSys(set_type);
		
		// prop,follow_var_type
		Element set_value = new Element("prop");
		set_value.setAttribute("id", "sys_follow_value");
		set_value.setAttribute("x", valueE);		
		addPropSys(set_value);		


		// this is the job to execute
		Element innerCall = null;
		if ( isSystemcall )
			innerCall = new Element("call_service");
		else
			innerCall = new Element("call_job");
		
		ArrayList<Element>	list = getExecutionElements(nextJob);
		
		try {
			// executeExecutionElements(list,nextJob); // ACHTUNG

			Iterator<Element> iterator = list.iterator();
			while ( iterator.hasNext()) {
				Element dothis = iterator.next();	
				executeJobelementFinalCatched(dothis,getCmdFromStep(dothis));
				
			}			

		} catch (Exception e1) {
			if ( pm ) CoreBuilder.rslogger.logNodeCloseDirect( logid, loglabel,null);					
			throw e1;
		}
		
		if ( pm ) CoreBuilder.rslogger.logNodeCloseDirect( logid, loglabel,null);
							
}
*/

private int getCmdFromStep (Element e ) {
	
	int cmd = -1;
	Integer i_cmd = cmdm.get(e.getName());
	
	if ( i_cmd != null)
		cmd = i_cmd.intValue();
	Sp.p("FOLLOW STEP: "+e.getName()+"="+cmd);
	return cmd;
	
}

public synchronized String getVar ( String varType, String varIdE )throws Exception {
	
	// kwm8
	
	String retval = null;
	
	int vt = kwmvtyp.convert(varType);

	switch ( vt ) {
	case kwmvtyp_prop: {	
		retval = getPropById(varIdE).valueE;
		break;
	}
	case kwmvtyp_file: {	
		retval = getFileById(varIdE).future_fn;
		break;
	}
	case kwmvtyp_dir: {	
		retval = getDirById(varIdE).value;
		break;
	}}
	
	return retval;
	
}



/*
public Element translateElement(Element x) throws Exception {
	return ( this.translateJobelement(x, false,-1,false) );

}
*/




private void checkNeed ( Element e) throws Exception {
	String toptext = e.getTextTrim();
	if ( toptext != null && toptext.length()>0) {
		
		String a[] = toptext.split("\\s+");;			
		for ( String thisB: a ) {

			String thisA = null;
			// try {

				if ( thisB.startsWith("(prop,") && thisB.endsWith(")") ) {
					String prop_id = thisB.substring(6, thisB.length()-1);
					InterProp prop = propMap.get(prop_id);
					if ( prop == null )
						thisA = null;
					else
						thisA = "ok";
				} else if ( thisB.startsWith("(dir,") && thisB.endsWith(")") ) {
					String dir_id = thisB.substring(5, thisB.length()-1);
					InterDir dir = dirMap.get(dir_id);
					if ( dir == null )
						thisA = null;
					else
						thisA = "ok";					
				} else if ( thisB.startsWith("(file,") && thisB.endsWith(")") ) {
					String file_id = thisB.substring(6, thisB.length()-1);
					InterFile file = fileMap.get(file_id);
					if ( file == null )
						thisA = null;
					else
						thisA = "ok";					
				} else {
					throw new LangException(99,"task 'need' wants just a variable, but found: "+ thisB);
				}
			//} catch (Exception e1) {
			//	thisA = null;
			//}
		
			if ( thisA == null ) { // !Commons.exists(thisA)) {
				// need not fullfilled
				if ( e.getAttribute("default")!=null) {

					if ( thisB.startsWith("(prop,") && thisB.endsWith(")") ) {
						String prop_id = thisB.substring(6, thisB.length()-1);
						
						Element add = new Element("prop");
						add.setAttribute("id", prop_id);
						add.setText(translateString(e.getAttributeValue("default")));
						addProp(add,false);
						if ( debugOn )
							lc.debug_expressions("need set prop '"+prop_id+"' to '"+e.getAttributeValue("default")+"'");							
					
					} else if ( thisB.startsWith("(dir,") && thisB.endsWith(")") ) {
						String dir_id = thisB.substring(5, thisB.length()-1);
						
						Element add = new Element("dir");
						add.setAttribute("id", dir_id);
						add.setText(translateString(e.getAttributeValue("default")));
						addDir(add,false);
						if ( debugOn )
							lc.debug_expressions("need set dir '"+dir_id+"' to '"+e.getAttributeValue("default")+"'");							
					
					} else if ( thisB.startsWith("(file,") && thisB.endsWith(")") ) {
						String file_id = thisB.substring(6, thisB.length()-1);
						
						Element add = new Element("file");
						add.setAttribute("id", file_id);
						add.setText(translateString(e.getAttributeValue("default")));
						addFile(add,false);
						
						if (debugOn)
							lc.debug_expressions("need set file '"+file_id+"' to '"+e.getAttributeValue("default")+"'");							
					} else {
						throw new LangException(99,"need didn't find a variable to be needed in: "+ thisB);
					}
					
				} else {
					// es existiert kein ddefault
					String msg1 = "need: the variable "+thisB+ " is missed. happend in: "+taskExplainer(e);
					String msg = e.getAttributeValue("msg");
					if ( msg!=null ) {
						msg1 = Entities.exportEntities(translateString(msg)) +", "+msg1;
					}
					throw new LangException(100,msg1);						
				} // ende if ( e.getAttribute("default")!=null) {

			} // ende: if ( !Commons.exists(thisA)) {			
		} // ende: for ( String thisA: a ) {		
	} // ende: if ( toptext != null && toptext.length()>0) {		
} // ende: private void checkNeed ( Element e, Element tke ) throws Exception {

/*
private String transExport(String in ) throws Exception {
	String b = translateValue(in);
	return ( Entities.exportEntities(b));
}
*/


private void removeAllNo()  {
	if ( reportCoops )
		System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  removeAllNo");
	Set<String> keySet = noMap.keySet();
	Iterator<String> i = keySet.iterator();
	while ( i.hasNext() ) {
		String aktKey   = i.next();

		
		ICoop toremove = noMap.get(aktKey);
		if ( reportCoops )
		System.out.println("                    COOP   "+myInterpreterId+"||||  removeAllNo: "+aktKey);
		try {
			toremove.autoClose();
			lc.warn("Performed AutoClose for JOB-Coop: '"+aktKey+"'");
		} catch (Exception e) {
			lc.error("PROBMLEM when performing AutoClose for JOB-Coop: '"+aktKey+"'; "+e.getLocalizedMessage());
			Sp.pe(e);
		}
		// noMap.remove(aktKey);				
	}		
	noMap = new HashMap<String, ICoop>();
	// nic123 classloaderMap
	if ( noMap.size() > 0 )
		lc.warn("Could not remove all left coops !");
	
	if ( kMap.size() > 0) {
		lc.info("Number of permantent Coops at job-end: "+kMap.size());		
	}
	// lc.debug("coop      counter after remove all = "+noMap.size());
	
	/*
	Set<String> keyset = TaskdefMap.keySet();
	Iterator<String> iterator = keyset.iterator();


	while ( iterator.hasNext() ) {
		String name  = iterator.next();
		PlugTaskdef aktValue = TaskdefMap.get(name);
		aktValue.c
	}	*/
}

/*
public static void removeAllNoKEEP()  {

	Set<String> keySet = noMapKeep.keySet();
	Iterator<String> i = keySet.iterator();
	while ( i.hasNext() ) {
		String aktKey   = i.next();

		ICoop toremove = noMapKeep.get(aktKey);
		lcs.warn("AutoClose for Coop-KEEP "+aktKey);
	
		try {
			toremove.autoClose();
			lcs.warn("Performed AutoClose for KEEP-Coop: '"+aktKey+"'");
		} catch (Exception e) {
			lcs.error("PROBMLEM when performing AutoClose for KEEP-Coop: '"+aktKey+"'; "+e.getLocalizedMessage());
			Sp.pe(e);
		}		
		
		
		noMapKeep.remove(aktKey);				
	}		
	
	if ( noMapKeep.size() > 0 )
		lcs.warn("Could not remove all left coops !");
	

}	
*/

private Element reportCoopsAsk () {
	if ( reportCoops)
	System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  reportCoopsAsk");

	Element ret = new Element("coops");
	ret.setAttribute("size_keep",Commons.int2String(isitzung.getCoopMap_k(appidOrAs,isMasterApp).size() ));
	ret.setAttribute("size_std",Commons.int2String(noMap.size()));
	boolean printedLabelToTi = false;

	if ( noMap.size() > 0 ) {
		
		Set<String> keySet = noMap.keySet();
		Iterator<String> i = keySet.iterator();
		
		int j = 0;
		while ( i.hasNext() ) {
			j++;
			String aktKey   = i.next();
			Object  aktObj = noMap.get(aktKey);
			Element a = new Element("coop");
			String internal = Entities.exportEntities(aktKey);
			// a.setAttribute("internal", internal);
			int indexdp = internal.indexOf(":");
			if ( indexdp > 0 ) {
				String vorher = internal.substring(0, indexdp);
				String nachher = internal.substring(indexdp+1);
				a.setAttribute("type", vorher);
				a.setAttribute("id", nachher);
			}
			
			
			// a.setAttribute("type", Entities.exportEntities(aktKey))
			a.setAttribute("canoName", aktObj.getClass().getCanonicalName());
			a.setAttribute("memory", "std");
			ret.addContent(a);
		}		
	}
	
	if ( kMap.size() > 0 ) {
			
		Set<String> keySet = kMap.keySet();
		Iterator<String> i = keySet.iterator();
		
		int j = 0;
		while ( i.hasNext() ) {
			j++;
			String aktKey   = i.next();
			Object  aktObj = kMap.get(aktKey);
			Element a = new Element("coop");
			
			String internal = Entities.exportEntities(aktKey);
			// a.setAttribute("internal", internal);
			int indexdp = internal.indexOf(":");
			if ( indexdp > 0 ) {
				String vorher = internal.substring(0, indexdp);
				String nachher = internal.substring(indexdp+1);
				a.setAttribute("type", vorher);
				a.setAttribute("id", nachher);
				a.setAttribute("keeplessId", nachher.substring(0, nachher.length()-3));
			}			

			a.setAttribute("canoName", aktObj.getClass().getCanonicalName());
			a.setAttribute("memory", "keep");
			ret.addContent(a);
		}		
	}
	return ret;
	
}

private void reportCoops2Log (String labelNPara, Level level, 
		boolean keepOnly, boolean doti) {
	if ( reportCoops)
	System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  reportCoops2Log ");
	String labelN = labelNPara;
	
	Element ti = new Element("ti");

	boolean printedLabelToTi = false;
	
	if ( kMap.size() > 0 ) {
		if ( labelN != null )
			lc.log("* coops.keep, "+labelN+" Nbr of Background-Coops-k: "+kMap.size(),level);
		else
			lc.log("* coops.keep, Nbr of Background-Coops-k: "+kMap.size(),level);
		if ( doti ) {
			try {
				if ( labelN != null ) {
					ti.setAttribute("label",Entities.importEntities(labelN));
					terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
					printedLabelToTi = true;
				}
				
				ti.setAttribute("label","Background-Coops: "+kMap.size());
				terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e) {}			
		}
		Set<String> keySet = kMap.keySet();
		Iterator<String> i = keySet.iterator();
		
		int j = 0;
		while ( i.hasNext() ) {
			j++;
			String aktKey   = i.next();
			Object  aktObj = kMap.get(aktKey);
			lc.log("*                          "+j+". =  '"+Entities.exportEntities(aktKey)+"' of class '"+aktObj.getClass().getCanonicalName()+"' with internal name '"+aktObj+"'",level);
			
			if ( doti ) {
				try {
					ti.setAttribute("label",j+". "+aktKey);
					ti.setAttribute("x",aktObj.getClass().getCanonicalName());
					terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
				} catch (Exception e) {}			
			}			
		}		
	}
	ti.removeAttribute("x");
	if ( !keepOnly && noMap.size() > 0 ) {
		if ( labelN != null )
			lc.log("* coops.plain, "+labelN+" Nbr of Job-Coops: "+noMap.size(),level);
		else
			lc.log("* coops.plain, Nbr of Job-Coops: "+noMap.size(),level);
		if ( doti ) {
			try {
				if ( !printedLabelToTi && labelN!=null) {
				ti.setAttribute("label",Entities.importEntities(labelN));
				terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
				}
				
				ti.setAttribute("label","Job-Objects: "+noMap.size());
				terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
			} catch (Exception e) {}			
		}
		Set<String>keySet = noMap.keySet();
		Iterator<String> i = keySet.iterator();
		int j = 0;
		while ( i.hasNext() ) {
			j++;
			String aktKey   = i.next();
			Object  aktObj = noMap.get(aktKey);
			lc.log("*                          "+j+". =  '"+Entities.exportEntities(aktKey)+"' of class '"+aktObj.getClass().getCanonicalName()+"' with internal name '"+aktObj+"'",level);
			
			if ( doti ) {
				try {
					ti.setAttribute("label",j+". "+aktKey);
					ti.setAttribute("x",aktObj.getClass().getCanonicalName());
					terminator.executeTi(ti, tiLogLevel,appid,appdir,instid,instdir,this);
				} catch (Exception e) {}			
			}				
		}			
	}
	
}



@Override
public boolean isMac() {
	// TODO Auto-generated method stub
	return CoreBuilder.ws.isMac;
}

@Override
public boolean isLinux() {
	// TODO Auto-generated method stub
	return CoreBuilder.ws.isLinux;
}





@Override
public ITerminator getTerminator() throws Exception {
	// TODO Auto-generated method stub
	return terminator;
}

@Override
public String getGlobalAndRemove(String global_id) throws Exception {
	// TODO Auto-generated method stub nic
	listGlobals ("getGlobalAndRemove requested ["+global_id+"]");
	
	String neuer_wert = global_map.get(global_id);
	if ( neuer_wert != null )
		global_map.remove(global_id);
		
	return neuer_wert;

}

private void listGlobals (String titel) {
	Sp.p(titel+ " - Globals");
	Set<String> keySet = global_map.keySet();
	Iterator<String> i = keySet.iterator();
	while ( i.hasNext() ) {
		String aktKey   = i.next();

		String value = global_map.get(aktKey);
		Sp.p("["+aktKey+"]  ->  ["+value+"]");
		
			
	}	
}

@Override
public String getAppId() throws Exception {
	// TODO Auto-generated method stub
	return appid;
}

@Override
public String getProId() throws Exception {
	// TODO Auto-generated method stub
	return instid;
}

@Override
public void depressive(String message) {
	lc.warn("!!  DEPRESSION  !!  "+message);
	Element d=new Element("ti");
	d.setAttribute("label", "!!   DEPRESSION   !!");
	d.setText(Entities.importEntities(message));
	try {
		terminator.executeTi(d,tiLogLevel,appid,appdir,instid,instdir,this);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		Sp.pe(e);
	}
}	
public void critical(String message) {
	lc.warn("!!  CRITICAL  !!  "+message);
	Element d=new Element("ti");
	d.setAttribute("label", "!!   CRITICAL   !!");
	d.setText(Entities.importEntities(message));
	try {
		terminator.executeTi(d,tiLogLevel,appid,appdir,instid,instdir,this);
	} catch (Exception e) {

	}
}		

private String getExcutingElementDocu () {
	if ( currentlyExcutingElementOriginal != null ) {
		if ( Commons.hasFlag(currentlyExcutingElementOriginal, "secret")) {
			if ( currentlyExcutingElementOriginal.getAttribute("secret") == null ) {
				// auch kein pseudonym
				return " [ in ] Secret-Task [?]";
					
			} else {
				// mit pseudonym
				return " [ in ] Secret-Task ["+currentlyExcutingElementOriginal.getAttributeValue("secret")+"]";					
			}			
			
		} else
			return " [ in ] "+taskExplainer(currentlyExcutingElementOriginal);
	} else
		return "";
}

// Interface: IRequestHandler: Named Objects
@Override

public synchronized void putNoApp(String idN, String type, ICoop object, String theApp)
		throws Exception {
	// TODO Auto-generated method stub
	if ( reportCoops)
		System.out.println("##6 rh.putNoApp (idN="+idN+", type="+type+", theApp="+theApp+")");	
	String theKey = type+":"+idN;
	
	boolean myIsMasterApp = false;
	
	if ( theApp.equals(isitzung.getMasterProjektApp()))
		myIsMasterApp = true;
	else
		myIsMasterApp = false;
	
	ICoop already = getNoApp(idN,type,theApp,myIsMasterApp);
	if ( already != null )
		throw new LangException(102,"Cooperation exists already id='"+idN+"' type='"+type+"'");
	if ( type.indexOf(":") > 0 )
		throw new LangException(103,"Cooperation can't have ':' in its type");

	if ( idN.endsWith("__") ) {
		ConcurrentHashMap<String, ICoop> cooptab = isitzung.getCoopMap(theApp, idN,myIsMasterApp,true);
		cooptab.put(theKey, object);

	} else {
		noMap.put(theKey, object);
	}
	
}

@Override
public synchronized ICoop getNoApp(String idN, String type,  String theApp) throws Exception {
	if ( reportCoops)
		System.out.println("##1 rh.getNoApp (idN="+idN+", type="+type+", theApp="+theApp+")");	
	String theKey = type+":"+idN;
	if ( idN.endsWith("__") ) { 
		boolean myIsMasterApp = false;
		if ( theApp.equals(isitzung.getMasterProjektApp()))
			myIsMasterApp = true;
		else
			myIsMasterApp = false;
		
		ConcurrentHashMap<String, ICoop> mymap = isitzung.getCoopMap(theApp,idN,myIsMasterApp,false);
		/* if (mymap.containsKey(theKey))
			return (  mymap.get(theKey));
		else
			return null; */
		
		return (  mymap.get(theKey));
	} else
		return noMap.get(theKey);
}

public synchronized ICoop getNoApp(String idN, String type,  String theApp,boolean myIsMasterApp) throws Exception {
	if ( reportCoops)
		System.out.println("##2 rh.getNoApp (idN="+idN+", type="+type+", theApp="+theApp+", myIsMasterApp="+myIsMasterApp+")");	

	String theKey = type+":"+idN;
	if ( idN.endsWith("__") ) { 
		ConcurrentHashMap <String, ICoop> mymap = isitzung.getCoopMap(theApp,idN,myIsMasterApp,false);
		return (  mymap.get(theKey));
	} else
		return noMap.get(theKey);
}

@Override
public synchronized void removeNoApp(String idN, String type,  String theApp) throws Exception {
	if ( reportCoops)
		System.out.println("##5 rh.removeNoApp (idN="+idN+", type="+type+", theApp="+theApp+")");	

	String theKey = type+":"+idN;

	
	
	if ( idN.endsWith("__") ) {
		boolean myIsMasterApp = false;
		if ( theApp.equals(isitzung.getMasterProjektApp()))
			myIsMasterApp = true;
		else
			myIsMasterApp = false;
		ConcurrentHashMap <String, ICoop> mymap = isitzung.getCoopMap(theApp,idN,myIsMasterApp,true);
		if (mymap.containsKey(theKey))
			mymap.remove(theKey);
		else
			{
			System.out.println("das sollte nicht sein");
			}
	} else
		noMap.remove(theKey);
	
}
public synchronized void putNo(String idN, String type, ICoop object) throws Exception {
	if ( reportCoops)
		System.out.println("##7 rh.putNo (idN="+idN+", type="+type+")");	
	
	putNoApp(idN, type,  object, appidOrAs);
	
}
@Override
public synchronized void putNo(String idN, String type, String type_string, ICoop object, Object client) throws Exception {
	if ( reportCoops)
		System.out.println("##8 rh.putNo (idN="+idN+", type="+type+", type_string="+type_string+", client="+client+")");	

	putNoApp(idN, type,  object, appidOrAs);
	
}
@Override
public synchronized ICoop getNo(String idN, String type) throws Exception {
	// TODO Auto-generated method stub
	if ( reportCoops)
		System.out.println("##3 rh.getNo (idN="+idN+", type="+type+")");	
	
	return getNoApp(idN, type,  appidOrAs);

}

public synchronized ICoop getNo(String idN, String type,Object client) throws Exception {
	if ( reportCoops)
		System.out.println("##4 rh.getNo (idN="+idN+", type="+type+", client="+client+")");	
	
	return getNoApp(idN, type,  appidOrAs);

}


@Override
public synchronized void removeNo(String idN, String type ) throws Exception {
	if ( reportCoops)
		System.out.println("##6 rh.removeNo (idN="+idN+", type="+type+")");	
	
	removeNoApp(idN, type,  appidOrAs);
			
}	
public synchronized void removeNo(String idN, String type, Object client ) throws Exception {
	if ( reportCoops)
		System.out.println("##9 rh.putNo (idN="+idN+", type="+type+", client="+client+")");	
	
	removeNoApp(idN, type,  appidOrAs);
			
}

// INTERFACE: IRequestHandler: Variablen

public Element translate(Element e) throws Exception {
	Element e2 = this.translateJobelement(e, true,-1,false) ;
	return e2;
}

public Element translatePrimitive(Element e, 
		boolean cloneIt, 
		int childLevels, 
		boolean remove_test_attribute) throws Exception {
	Element e2 = this.translateJobelement(e, cloneIt,childLevels,remove_test_attribute) ;
	return e2;
}


/*
public Element export(Element e) throws Exception {
	return Entities.exportEntitiesClone(e);
}
*/
public String translateValue( String workon ) throws Exception {
	return translateValueMy( workon,false );
} 

public String translateValueSave( String workon ) throws Exception {
	return c_translate(workon,"translateValueSave",false);
	// return translateValueMy( workon,false );
}


@Override
public boolean doesExistVariable(String type, String id)  throws Exception {
	String val;
	try {
		val = getVar(type,Entities.importEntities(id));
		if ( val== null )
			return false;
		else
			return true;
	} catch (Exception e) {
		return false;
	}
}

@Override
public String getVariableValue(String type, String idN) throws Exception {
	String result;
	try {
		result = getVar(type,Entities.importEntities(idN));
		return result;
	} catch (Exception e) {
		if ( is_jovial )
			return new String("");
		else
			throw new LangException(104,"We are not jovial. Nof found: ("+type+","+idN+") "
					+"");
	}

}
public String getVariableValue2(String type, String idE) throws Exception {
	//System.out.println("Interpreter #"+interpreterId+" was requested for VariableValue2 ("+type+"/"+idE);
	String result;
	try {
		result = getVar(type,idE);
		//System.out.println("Interpreter #"+interpreterId+" gives back:"+result);
		return result;
	} catch (Exception e) {
		//System.out.println("Interpreter #"+interpreterId+" has exception:"+e.getLocalizedMessage());
		if ( is_jovial )
			return new String("");
		else
			throw new LangException(104,"We are not jovial. Nof found: ("+type+","+idE+") "
					+"");
	}

}

@Override
public Date getJobdate() {
	// TODO Auto-generated method stub
	return jobtime;
}

public String getTempDir() {
	
	return Entities.exportEntities(isitzung.getSessionHomeFs()+"/tiempo/");
}
@Override
public boolean isJovial() {
	// TODO Auto-generated method stub
	return is_jovial;
}

// INTERFACE: rh classloaders 
@Override
public ClassLoader getPoiClassLoader() {
	return CoreBuilder.ws.getPoiClassLoader();
}
@Override
public ClassLoader getAntClassLoader() {
	return CoreBuilder.ws.getAntClassLoader();
}

// INTERFACE: rh Please Remember me

@Override
public void pleaseRememberMe(ITask itask) throws Exception {
	lc.trace("Task still working: ?");
	if ( haltIsRequested )
		throw new HaltException("@28 "+"stopped on halt upon request.");
}



@Override
public void terminalProgress(String msg, boolean do_log_info, ITask from) {
	// TODO Auto-generated method stub
	setProgressPlaintext("[?] "+msg, do_log_info);	
}	
@Override
public void terminalProgress(String msg, ITask from) {
	// TODO Auto-generated method stub
	setProgressPlaintext("[?] "+msg, false);	
}	

@Override
public boolean shouldBreakOnNextOccations() {
	// TODO Auto-generated method stub
	return break_one;
}

@Override
public boolean shouldBreakLoops() {
	// TODO Auto-generated method stub
	return break_loops;
}
// INTERFACE: 

// INTERFACE: 

@Override
public String translateString(String x) throws Exception {
	return translateValueMy(x,false);
}

private static HashMap<String, Loader> classloaderMap = new HashMap<String, Loader>();
private class Loader  {
	ClassLoader classloader;
	String jarfile;
	Loader ( ClassLoader classloader, String jarfile ) {
		this.classloader = classloader;
		this.jarfile = jarfile;
	}
}

@Override
public int getMathScale() {
	// TODO Auto-generated method stub
	return math_scale;
}


@Override
public int getMathRoundMethod() {
	// TODO Auto-generated method stub
	return math_round;
}

@Override
public void createSpirit(String spirit_id, Element xmlElement, IRequestHandler rh, ILogConnector lc)  throws Exception{
	CoopSpirit.createSpirit(spirit_id, xmlElement, rh);
	
}

@Override
public boolean doesExistCoop(String internal_type, String id) throws Exception {
	if ( reportCoops)
	System.out.println("|||   ||||    ||||  COOP   "+myInterpreterId+"||||  doesExistsCoop"+" "+id+"/"+internal_type);
	boolean ret;
	if ( id.endsWith("__") )
		ret = isitzung.getCoopMap(appidOrAs,id,isMasterApp,false).containsKey(internal_type+":"+id);
	else
		ret = noMap.containsKey(internal_type+":"+id);
	return ret;
}
public boolean doesExistCoopApped(String internal_type, String id, String appx) throws Exception {
	boolean ret;
	if ( id.endsWith("__") ) {
		boolean myIsMasterApp = false;
		if ( appx.equals(isitzung.getMasterProjektApp()))
			myIsMasterApp = true;
		else
			myIsMasterApp = false;	
		ret = isitzung.getCoopMap(appx,id,myIsMasterApp,false).containsKey(internal_type+":"+id);
	}
		else
		ret = noMap.containsKey(internal_type+":"+id);
	return ret;
}

@Override
public FileSystemManager getFsm()  {
	// TODO Auto-generated method stub
	return CoreBuilder.fsm;
}


private void printJobstartToTerminator() {
	String locName = Commons.getDbLoc(appid, instid);	
	String jobText;
	if (isSystemcall) {
		jobText = jobtodo.getJobid(); // .toUpperCase();
		if ( jobText.endsWith("?") ) {
			jobText = jobText.substring(0, jobText.length()-1) + " [Service] [Help]";
		} else {
			jobText = jobText+ " [Service]";
		}
	} else if ( jobtodo.getJobid().equals("?"))
		jobText = "? [Application Info] ";
	else if ( jobtodo.getJobid().equals("??"))
		jobText = "?? [Project Info] ";
	else {
		if ( instid == null )
			jobText = jobtodo.getJobid(); // +" [ "+appid + " ]";
		else
			jobText = jobtodo.getJobid(); //+" [ "+instid + " ]";
	}	
	
	String longStrowC = new String("***************************************************************************************************************");
	String longStrow = new String("****************************************************************************************************************");
	String longStrow2= new String("*         ******************************************************************************************************");
	// ***************************** header 
	
	try {
		/*
		Element eJobid = new Element("prop");
		eJobid.setAttribute("id", "job");
		eJobid.setText(jobtodo.getJobidEscaped());
		executeJobelementFinalCatched(eJobid,c_prop);

		Element lock2 = new Element("lock");
		lock2.setAttribute("id", "job");
		lock2.setAttribute("type", "prop");
		executeJobelementFinalCatched(lock2, c_lock);
		*/

		Element ej = new Element("prop");
		ej.setAttribute("id", "fs" ); 
		ej.setText(FileName.SEPARATOR);
		executeJobelementFinalCatched(ej,c_prop);
		
		Element eh = new Element("lock");
		eh.setAttribute("id", "fs");
		eh.setAttribute("type", "prop");
		executeJobelementFinalCatched(eh, c_lock);
		/*
		Element eg = new Element("file");
		eg.setAttribute("id", "jobs" ); 
		eg.setText(jobtodo.getFullfnEscaped());
		executeJobelementFinalCatched(eg, c_file);		

		Element ei = new Element("lock");
		ei.setAttribute("id", "jobs");
		ei.setAttribute("type", "file");
		executeJobelementFinalCatched(ei, c_lock); */
	} catch (Exception e4) {
		// TODO Auto-generated catch block
		Sp.pe(e4);
	}	
	
	String zeile_job = " Job : "+ ml2(jobText,"job");
	
	String zeile_app = "*  START  * App : "+ml2(appid+ " - "+appdir,"app") ;
	String zeile_app2= "*   OK    * App : "+ml2(appid+ " - "+appdir,"app") ;
	String zeile_inst= "*********** Pro : "+ml2(instid + " - "+instdir,"pro");
	
	// log plus
	Element logon = new Element("log_plus");
	logon.setAttribute("label", Entities.importEntities(jobText));
	if ( call_depth > 0 ) {
		try {
			executeJobelementSafe(logon,"jobstarter",false,c_log_plus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
	}	
	
	String jobSimu = jobtodo.getJobid();

	if ( silence == SILENCE_VERY);
	else {
		
		// Logger: Start
		// lc.info(longStrowC);
		lc.info("***********"+zeile_job);
		if ( appid != null )
			lc.info(zeile_app);
		if ( instid != null )
			lc.info(zeile_inst);	
		
		// lc.info(longStrow);	
		if ( jobSimu.equals("??")) {
			if ( instid == null ) 
				jobSimu = "App '"+appid+"'";
			else
				jobSimu = "Project '"+instid+"'";
		} else if ( jobSimu.equals("???")) {
			jobSimu = "Project & App '"+appid+"'";
		} else if ( jobSimu.equals("?"))
			jobSimu = "App '"+appid+"'";
		 else if ( jobSimu.endsWith("?"))
				jobSimu = "Job '"+jobtodo.getJobid().substring(0,jobtodo.getJobid().length()-1)+"'";

		if ( silence != SILENCE_YES ) {
			terminator.jobstart(jobSimu, appid, instid, isSystemcall, call_depth,jobtodo.getToolTip());	
			Element e = new Element("tp");
			e.setText(tp_start + Entities.importEntities(jobText)+ " " + locName );
			executeJobelementSafe(e,"tp intro",true,c_tp);	
		} else if ( silence == SILENCE_NO ) {
			
			Element e = new Element("tp");
			e.setText(tp_start + Entities.importEntities(jobText)+ " " + locName );
			executeJobelementSafe(e,"tp intro",true,c_tp);				
		}
	}

	if ( call_depth == 0 && !isSystemcall && !jobConstr.getJm().isServiceApp() && jobConstr.getJobid().indexOf("?")==-1) {
		String mj = jobConstr.getJobid();
		if ( isSystemcall)
			mj = mj+":SERVICE";
	}	
}




private void printTerminatorWellEnded(XrRqRpTi x) {
	terminator.setProgress("[ OK ]" + to_ok+" " +Entities.importEntities(x.getJobText()),false);	
	LogConnector lc = x.getLc();
	
	
	lc.info("***********"+x.getZeile_job());
	if ( appid != null )
		lc.info(x.getZeile_app2());
	if ( instid != null )
		lc.info(x.getZeile_inst());	

	if ( call_depth > 0 ) {
		Element logoff = new Element("log_minus");
		executeJobelementSafe(logoff,"jobender",false,c_log_minus);
	}
	if ( lc.debugIsOn() && CoreBuilder.rslogger.isDebug_filter_logic()) {
		Element tx = new Element("ti");
		tx.setAttribute("label", "<< interpreter <<");
		tx.setAttribute("x","done ok.");
		try {
			terminator.executeTi(
					tx,
					x.getLoglevel(),
					x.getAppId(),
					x.getAppDir(),
					x.getProId(),
					x.getProDir(),
					x.getTiHandler());
		} catch (Exception e1) {}		
	}				
	// Terminallogger: Done
	if ( silence == SILENCE_NO) { 
		if ( call_depth == 0  && !isSystemcall && !jobConstr.getJobid().equals("??")) {
			String mj = jobConstr.getJobid();
			if ( isSystemcall)
				mj = mj+":SERVICE";
			terminator.flush();
		}
		terminator.jobend(x.getJobSimu(), appid, instid, isSystemcall, call_depth,jobtodo.getJobinfo());					
	}

	if ( call_depth == 0  && !isSystemcall && !jobConstr.getJm().isServiceApp() && jobConstr.getJobid().indexOf("?")==-1) {
		String mj = jobConstr.getJobid();
		if ( isSystemcall)
			mj = mj+":SERVICE";
		mj = mj+":EXCEPTION";
	}



// terminator.flush(x.isDoFlush()); gummi
	terminator.flush();

if ( x.getJobText().equals("sel_open_file [Service]") 
		|| x.getJobText().equals("open_web [Service]")
		|| x.getJobText().equals("open_dir [Service]")
		|| x.getJobText().equals("open_file [Service]")
		|| x.getJobText().equals("sel_open_file_typical [Service]")					
		)
		;
else if ( ti_bottom )
	terminator.bottom();
else
	terminator.top();	

closeTempFileManager(null);


}

public void destroy() {
	
}

// kwm9
public String getSilenceText(int sil  ) {
	switch ( sil ) {
	case 0: {	
		return "SILENCE_NO";
	    
	}
	case 1: {	
		return "SILENCE_YES";
	    
	}
	case 2: {	
		return "SILENCE_VERY";
	    
	}	
	default: {	
		return "Silence '"+sil+"' ???";
	    
	}}
		
}

@Override
public FileObject[] getChildDirs(String directoryname) throws FileSystemException {
	// TODO Auto-generated method stub
	return CommonsVfs.getNinos(directoryname,isitzung);
}

@Override
public FileObject[] getChildDirs(FileObject fileObject) throws FileSystemException {
	// TODO Auto-generated method stub
	return CommonsVfs.getNinos(fileObject,isitzung);
}
public IGuiController getGuiController() {
	return guiController;
}
public void setGuiController(IGuiController guiController) {
	this.guiController = guiController;
}
public void setSitzung(ISitzung isitzung) {
	this.isitzung = isitzung;
	
}

private String getDatabaselink() {

	String aha= surrounding.getDatabaseLink(isitzung);

	if ( aha == null )
		return "";
	else
		return aha;
	
}
/*
private String getLoggedOnFileName() {
	return isitzung.getSessionHomeMem() +"/temp_pro/"+Commons.getTsHourly()+".txt";
}
*/
@Override
// kwm10 

public String request(String type, String keyname) throws Exception {
	String ret=null;
	
	int vt = kwmvtyp.convert(type);

	switch ( vt ) {
	case kwmvtyp_dir: {
		Integer i_cmd = requm.get(keyname);
		int cmd = 99;
		
		if ( i_cmd != null) 
			cmd = i_cmd.intValue();		
			
		switch ( cmd ) {
			case c_req_file_temp_dir: {
				ret = tempFileManger.requestFileTempDir();
				break;
			}
			case c_req_cache_system: {
				ret = CoreBuilder.ws.getStatusDir();
				break;
			}
			case c_req_sys_cache: {
				ret = isitzung.getSessionHomeFs() +"sys_cache/";
				break;
			}
			case c_req_databaselink: {
				ret = getDatabaselink();
				break;
			}
			case c_req_libro : {
				ret = isitzung.getLibroDir(appid);
				break;
			}   
			case c_req_libro2 : {
				ret = isitzung.getLibroDir2(appid);
				break;
			}   
			case c_req_master_project : {
				if ( isitzung.hasMasterProjectDir())
					ret = isitzung.getMasterProjektDir();
				else
					ret = new String("");	      
				break;
			}     
			case c_req_master_project_app : {
				if ( isitzung.hasMasterAppDir())
					ret = isitzung.getMasterAppDir();
				else
					ret = new String("");		      
				break;
			}     
			case c_req_new_temp_pro : {
				ret = isitzung.getSessionHomeFs() +"temp_pro/"+
					Commons.getTsShortDate()+"_"+isitzung.getNextIndexTempPro()+"_"+Commons.randomAlphaNumeric(3)+"/";      
				break;
			}     
			case c_req_session_home_fs : {
				ret = isitzung.getSessionHomeFs() ;
				break;
			}   
			case c_req_session_home_mem : {
				ret = isitzung.getSessionHomeMem() ;
				break;
			}  
			case c_req_mpr : {
				// #arenga#1# mount point root 		
				ret = CoreBuilder.ws.getMpRootDir() ;    
				break;
			}  
			case c_req_mpe1 : {
				// #arenga#1# mount point external 1		
				ret = CoreBuilder.ws.getMpExt1Dir() ;    
				break;
			} 
			case c_req_mpe2 : {
				// #arenga#1# mount point external 1		
				ret = CoreBuilder.ws.getMpExt2Dir() ;    
				break;
			} 
			case c_req_mpe3 : {
				// #arenga#1# mount point external 1		
				ret = CoreBuilder.ws.getMpExt3Dir() ;    
				break;
			} 			
			default: {
				throw new Exception("Unknown kombination in reqeust: type,keyname,"+type+","+keyname+", we know: dir,file_temp_dir");
			}                                                              
		} 		
		
	    break;
	}
	case kwmvtyp_prop: {	
		if ( keyname.equals("master_project_app")) {
			if ( isitzung.hasMasterProjectDir()) {
				ret = isitzung.getMasterProjektApp();
				System.out.println("master_project_app="+ret);
			} else
				ret = new String("");		
		}   else {
			throw new Exception("Unknown kombination in prop-request with keyname = "+keyname);
		}
		break;
	}

	}
	
	if ( ret== null )
		throw new Exception("Unknown request of type '"+type+"' and keyname '"+keyname+"'");
	return ret;
}


public int getRoleOverall() {
	return isitzung.getRoleIndex();
	/*
	int c_role_h = surrounding.role;
	if ( c_role_h == -1) {
		c_role_h= DbJob.getRoleHeight(CoreBuilder.ws.getRole());
		
	}
	return c_role_h;*/
}
@Override
public Surrounding getSurrounding() {
	// TODO Auto-generated method stub
	return surrounding;
}
@Override
public String getAppDir() throws Exception {
	// TODO Auto-generated method stub
	return appdir;
}
@Override
public String getProDir() throws Exception {
	// TODO Auto-generated method stub
	return instdir;
}
public String getMyFileResultsDir( ) throws Exception{
	return instdir+"MyResults/FileResults/";
	
}
public String getMyFolderResultsDir( ) throws Exception{
	return instdir+"MyResults/FolderResults/";
}
@Override
public String getUriPresentation(String uri) {
	// System.out.println("interpreter #"+interpreterId+" getUriPresentationet("+uri+")");
	if ( uri == null ) return "";
	String locp0 = null;
	String locp1 = null;
	String locp2 = null;
	
	if ( uri.length() == 0 )
		return uri;
	else if ( uri.startsWith(isitzung.getMasterProjektDir())) {
		locp0 = isitzung.lang().getUriProArt();
		locp1 = isitzung.lang().getUriProMaster();
		locp2 = uri.substring(isitzung.getMasterProjektDir().length());	

	}
	else if ( uri.startsWith(instdir)) {
		locp0 = isitzung.lang().getUriProArt();
		locp1 = isitzung.lang().getUriPro();
		locp2 = uri.substring(instdir.length());	
		/*
		String may=
		"Project: "+uri.substring(instdir.length());
		if (may.equals("Project: "))
			return "Project-Folder";
		else
			return may;
			*/
	} else if ( uri.startsWith(appdir)) {
		locp0 = isitzung.lang().getUriAppArt();
		locp1 = isitzung.lang().getUriApp();
		locp2 = uri.substring(appdir.length());		
		//return "App: "+uri.substring(appdir.length());
	} else if ( getDatabaselink().length()> 0 && uri.startsWith(getDatabaselink())) {
		locp0 = isitzung.lang().getUriPdbArt();
		locp1 = isitzung.lang().getUriPdb();
		locp2 = uri.substring(getDatabaselink().length());
		
		//return "Db: "+uri.substring(getDatabaselink().length());
	
	} else if ( uri.startsWith(isitzung.getSessionHomeFs())) {
		locp0 = isitzung.lang().getUriSessionArt();
		locp1 = isitzung.lang().getUriSession();
		locp2 = uri.substring(isitzung.getSessionHomeFs().length());	
		
		// return "Session: "+uri.substring(isitzung.getSessionHomeFs().length()+1);	
	} else if ( uri.startsWith(isitzung.getSessionHomeMem())) {
		locp0 = isitzung.lang().getUriMemArt();
		locp1 = isitzung.lang().getUriMem();
		locp2 = uri.substring(isitzung.getSessionHomeMem().length());	
		
		// return "Session: "+uri.substring(isitzung.getSessionHomeFs().length()+1);	
	} else if ( isitzung.getLibroDir(appid).length()> 0 && uri.startsWith(isitzung.getLibroDir(appid))) {
		locp0 = isitzung.lang().getUriLibroArt();
		locp1 = isitzung.lang().getUriLibro();
		locp2 = uri.substring(isitzung.getLibroDir(appid).length());		
		// return "Libro: "+uri.substring(isitzung.getLibroUri().length()+1);
	} else if ( uri.startsWith(CoreBuilder.ws.getSystemDir())) {
		locp0 = isitzung.lang().getUriSysArt();
		locp1 = isitzung.lang().getUriSys();
		locp2 = uri.substring(CoreBuilder.ws.getSystemDir().length());	
		
		// return "Session: "+uri.substring(isitzung.getSessionHomeFs().length()+1);	
	} else return //"URI: "+ 
		uri;
	

	
	if ( locp2.length()> 0 ) {
		if ( locp2.startsWith("/"))
			return locp1+": "+locp2.substring(1);
		else
			return locp1+": "+locp2;
	} else
		return locp0+" "+locp1;
	
//isitzung.getSessionHomeFs() +"/";

	
}
public  void addElementsForProjectDb( Element c1, String projectId, String projectDir  ) {
	
	// 2 props �bergeben ext_ons und ext_config...
	// String eONs = ;
	// String eConf = ;
	Sp.p ("- 1 -");

	Element s1 = new Element("prop");			
	s1.setAttribute("id","db_ons");
	s1.setAttribute("x",surrounding.extOn);
	c1.addContent(s1);
	
	Sp.p ("- 2b -");
	Element s1b = new Element("prop");			
	s1b.setAttribute("id","db_bits");
	s1b.setAttribute("x",surrounding.extBits);
	c1.addContent(s1b);			
	
	Sp.p ("- 3 -");
	Element s2 = new Element("prop");
	s2.setAttribute("id","db_conf");
	UserInfo ui = isitzung.getUserInfo();
	s2.setAttribute("x",surrounding.getCurrentProDbConfStringForServices(isitzung));
	c1.addContent(s2);		
	
	
	if ( ui != null && ui.getDbext() != null ) {
		Sp.p ("- 3 -");
		Element s2r = new Element("prop");
		s2r.setAttribute("id","db_pro_extension");
		s2r.setAttribute("x",getUserInfo().getDbext());
		c1.addContent(s2r);			
	} else {
		Sp.p ("- 3b -");
		Element s2r = new Element("prop");
		s2r.setAttribute("id","db_pro_extension");
		// s2r.setAttribute("x",getUserInfo().getDatabaseTerm());
		c1.addContent(s2r);		
	}
	
	Sp.p ("- 4 -");
	Element s3 = new Element("prop");
	s3.setAttribute("id","db_pro");
	s3.setAttribute("x",projectId);
	c1.addContent(s3);		
	
	Sp.p ("- 5 -");
	Element s4 = new Element("dir");
	s4.setAttribute("id","db_pro");
	s4.setAttribute("x",projectDir);
	c1.addContent(s4);				
	
	Sp.p ("- 6 -");
	Element s5 = new Element("prop");
	s5.setAttribute("id","db_app");
	s5.setAttribute("x",appid);
	c1.addContent(s5);	
	
	Sp.p ("- 7 -");
	Element s6 = new Element("prop");
	s6.setAttribute("id","db_pro_commits");
	s6.setAttribute("x",myapp.getCommitsPath());
	c1.addContent(s6);	
	
	Element s7 = new Element("prop");
	s7.setAttribute("id","db_pro_cache");
	s7.setAttribute("x",myapp.getCachePath());
	c1.addContent(s7);		
	
}

private UserInfo getUserInfo() {
	// TODO Auto-generated method stub
	return getGuiController().getUserInfo();
}
@Override
public ILogConnector getLogConnector() {
	// TODO Auto-generated method stub
	return lc;
}
@Override
public ISitzung getSitzung() {
	// TODO Auto-generated method stub
	return isitzung;
}
@Override
public boolean hasPriv(String requestedPriv) {
	return isitzung.hasPriv(requestedPriv);
}
@Override
public String getPrivPara(String requestedPrivPara) {
	return isitzung.getPrivPara(requestedPrivPara);
}

public boolean hasPrivAnt() {
	return isitzung.hasPrivAnt();
}
@Override
public boolean triggerButtonsByButton() {
	// TODO Auto-generated method stub
	return is_buttons_by_button;
}

@Override
public void runRhJob(String job, Element parameters) throws Exception {
	// TODO Auto-generated method stub
	
	Interpreter ret = JobRunner.createInterpreter(
			job, 
			false, // is Service Call: no
			appid, 
			instid, 
			instdir, 
			parameters, 
			null, // parameters at end
			Interpreter.SILENCE_NO, 
			null, // eine configlist
			0, // depth
			interpreterShouldGiveBackTk,
			ignoreClassloading,
			terminator,
			false, // includeDefaultConfig
			guiController,
			isitzung,
			true);
	
	ArrayList<Element> configList = 
			CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					instid, 
					instdir,
					true,
					isitzung);
	ret.addConfigList(configList);
	
	JobRunner jr = new JobRunner();
	ret.setSurrounder(subSurrounding);
	jr.runNewJob(ret, new ReqResDummy());

	
}

@Override
public boolean getTurbo() {
	// TODO Auto-generated method stub
	return !is_not_turbo;
}

private String myState = null;
public synchronized String getState() {
	// TODO Auto-generated method stub
	return myState;
}


}
