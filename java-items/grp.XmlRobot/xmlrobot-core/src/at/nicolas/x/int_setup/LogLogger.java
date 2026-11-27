/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jdom2.Attribute;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ExceptionNotice;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.Parameters;


public class LogLogger {

	Logger logger = null;
	public static PatternLayout standardPatternLayout = new PatternLayout( "%-5p %m%n" );
	private boolean logDetails = true;	
	private int log_node_level = 0;
	private ArrayList<String> nodeList = new ArrayList<String>();
	private int tab_size = 3;
	private String tab_text = new String (" ");
	
	private static String consoleAppId = null;
	
	private ArrayList<ExceptionNotice> exceptionNoticeList = new ArrayList<ExceptionNotice>();
	
	private LogConnector lc = new LogConnector("");
	
	public boolean isDebug_filter_tasks() {
		return debug_filter_tasks;
	}
	public boolean loggingWouldTakePlace (Level forThisLevel ) {
		if ( forThisLevel == Level.OFF )
			return false;
		if (getLevel().toInt() <=  forThisLevel.toInt() )
			return true;
		else
			return false;
	}
	public void setDebug_filter_tasks(boolean debug_filter_tasks) {
		this.debug_filter_tasks = debug_filter_tasks;
		log("Debug filter 'tasks' was set to "+debug_filter_tasks,null,Level.INFO);
	}

	public boolean isDebug_filter_expressions() {
		return debug_filter_expressions;
	}

	public void setDebug_filter_expressions(boolean debug_filter_expressions) {
		this.debug_filter_expressions = debug_filter_expressions;
		log("Debug filter 'expressions' was set to "+debug_filter_expressions,null,Level.INFO);
	}

	public boolean isDebug_filter_logic() {
		return debug_filter_logic;
	}

	public void setDebug_filter_logic(boolean debug_filter_logic) {
		this.debug_filter_logic = debug_filter_logic;
		log("Debug filter 'logic' was set to "+debug_filter_logic,null,Level.INFO);
	}

	public boolean isDebug_filter_extensions() {
		return debug_filter_extensions;
	}

	public void setDebug_filter_extensions(boolean debug_filter_extensions) {
		this.debug_filter_extensions = debug_filter_extensions;
		log("Debug filter 'extensions' was set to "+debug_filter_extensions,null,Level.INFO);
	}
	
	public boolean isDebug_filter_calls() {
		return debug_filter_calls;
	}

	public void setDebug_filter_calls(boolean debug_filter_calls) {
		this.debug_filter_calls = debug_filter_calls;
		log("Debug filter 'calls' was set to "+debug_filter_calls,null,Level.INFO);
	}	

	private boolean debug_filter_tasks = true;
	private boolean debug_filter_expressions = true;
	private boolean debug_filter_logic = false;
	private boolean debug_filter_extensions = false;
	private boolean debug_filter_calls = true;

	public LogLogger() {
	}
	
	public void clearLogNodeLevel() {
		log_node_level =0;
	}
	public ExceptionNotice getLastExceptionNotice() {
		if ( exceptionNoticeList.size()>0) {
			return exceptionNoticeList.get(exceptionNoticeList.size()-1);
		} else {
			return null;
		}
	}
	public static void createStandardLoggerIfNotExists(Level _level ) {
		
		if ( CoreBuilder.rslogger == null ) {
			createStandardLogger( _level );		
		}
	}

	public static void createStandardLoggerIfNotExists(String _levelString ) {
		createStandardLoggerIfNotExists(string2Level(_levelString) );
	}
	
	public static void createStandardLogger(Level _level ) {
		
		CoreBuilder.rslogger = new LogLogger();
		CoreBuilder.rslogger.logger = Logger.getRootLogger();
		CoreBuilder.rslogger.setLevel(_level);
	}

	public static void createStandardLogger(String _levelString ) {
		createStandardLogger(string2Level(_levelString));
	}
	
	public static Level string2Level (String _levelString ) {
		Level level = null;
		
		if ( _levelString == null )           	 level = CoreBuilder.rslogger.getLevel();
		else if ( _levelString.equals("all"))    level = Level.ALL;
		else if ( _levelString.equals("debug"))  level = Level.DEBUG;
		else if ( _levelString.equals("error"))  level = Level.ERROR;
		else if ( _levelString.equals("fatal"))  level = Level.FATAL;
		else if ( _levelString.equals("info"))   level = Level.INFO;
		else if ( _levelString.equals("off"))    level = Level.OFF;
		else if ( _levelString.equals("trace"))  level = Level.TRACE;
		else if ( _levelString.equals("warn"))   level = Level.WARN;
		else									 level = CoreBuilder.rslogger.getLevel();		
		return level;
	}
	
	public static String level2String (Level level) {
		String ret = null;
		
		if ( level==Level.ALL) ret="all";
		else if ( level==Level.DEBUG) ret="debug";
		else if ( level==Level.ERROR) ret="error";
		else if ( level==Level.FATAL) ret="fatal";
		else if ( level==Level.INFO) ret="info";
		else if ( level==Level.OFF) ret="off";
		else if ( level==Level.TRACE) ret="trace";
		else if ( level==Level.WARN) ret="warn";
		else { 
			ret = "?";
			LogConnector myLc = new LogConnector("RsLogger.level2String");
			myLc.warn("Received the following, unknown Level: "+level.toString()+", returned instead '?'");		
		}
		return ret;
	}	
		
	public void setLogDetails(boolean on ) {
		logDetails = on;
	}
	public boolean getLogDetails( ) {
		return (logDetails);
	}
	
	public boolean is_debug_plus = false;
	public void setLevel ( Level _logLevel ) {
		if ( _logLevel.equals(Level.DEBUG) ||
				_logLevel.equals(Level.TRACE) ||
				_logLevel.equals(Level.ALL) )
				is_debug_plus = true;
			else
				is_debug_plus = false;		
		logger.setLevel(_logLevel);
		log("LogLevel was set to "+_logLevel.toString(),null,_logLevel);

	}
	
	public Level getLevel () {
		return logger.getLevel();
	}
	public boolean debugIsOn() {
		if ( logger.getLevel().equals(Level.DEBUG) ||
			 logger.getLevel().equals(Level.TRACE) ||
			 logger.getLevel().equals(Level.ALL) )
			return true;
		else
			return false;
	}
	public boolean traceIsOn() {
		return ( logger.getLevel().equals(Level.TRACE) || logger.getLevel().equals(Level.ALL) );
	}	
	private String getPlusMinusPrefix  ( Attribute id, Attribute name ) {
		String prefix = "";
		if ( id !=null && name != null) 
			prefix = Entities.exportEntities(name.getValue()) + 
			" ["+Entities.exportEntities(id.getValue())+"]";
		else if (id != null) 
			prefix = "["+Entities.exportEntities(id.getValue())+ "]";
		else if (name != null ) 
			prefix = Entities.exportEntities(name.getValue());
		return prefix;
	}
	private String getPlusMinusPrefix  ( String id, String name ) {
		String prefix = "";
		if ( id !=null && name != null) 
			prefix = name + " ["+id+"]";
		else if (id != null) 
			prefix = "["+id+ "]";
		else if (name != null ) 
			prefix = name;
		return prefix;
	}	
	
	public void logNodeOpenDirect ( String id, String name, Level loglevel ) {
		String prefix = getPlusMinusPrefix(id,name);
		
		if ( loglevel == null)
			log ( "+  "+prefix,null,loglevel);
		else
			log ( "+  "+prefix,null,loglevel);
			
		log_node_level++;
		if ( id != null )
			nodeList.add(id);
		else
			nodeList.add(null);
	}
	
	public void logNodeOpen ( Attribute id, Attribute name, Level loglevel ) {
		String prefix = getPlusMinusPrefix(id,name);
		
		log ( "+  "+prefix,null,loglevel);
			
		log_node_level++;
		if ( id != null )
			nodeList.add(Entities.exportEntities(id.getValue()));
		else
			nodeList.add(null);
	}
	
	public void logNodeClose ( Attribute _nodeid, Attribute name, Level loglevel, String _force_nodematch ) throws Exception {
		
		log ( "-",null,loglevel);
		
		boolean force_nodematch = false;
		if ( _force_nodematch == null ) 
			force_nodematch = false;
		
		else if ( _force_nodematch.equals("true")) 
			force_nodematch = true;
		else 
			force_nodematch = false;
		
		int lastNodeIndex = nodeList.size();
		lastNodeIndex--;
		
		if ( force_nodematch ) {
			String nodeid = Entities.exportEntities(_nodeid.getValue());
			if ( lastNodeIndex >= 0 ) {
				String lastNodeid = (String)nodeList.get(lastNodeIndex);
				if ( !lastNodeid.equals(nodeid))				
					throw new Exception ("Lognodes do not match, last was '"+lastNodeid+"', want to close is '"+_nodeid.getValue()+"'");				
			} else {
				throw new Exception ("Lognodes do not match by Index");								
			}
		}
		if ( lastNodeIndex >= 0)
			nodeList.remove(lastNodeIndex);
		log_node_level--;
	}
	
	public void logNodeCloseDirect ( String _nodeid, String name, Level loglevel ) throws Exception {
			
		log ( "-",null,loglevel);
		
		
		int lastNodeIndex = nodeList.size();
		lastNodeIndex--;
		
		if ( lastNodeIndex >= 0)
			nodeList.remove(lastNodeIndex);
		log_node_level--;
	}
	
	
	private int counter_debug = 0;
	public void resetCounterDebug() {
		counter_debug=0;
	}
	

	public void log ( String _message, Parameters _paras, Level x_level ) {
		Level _level = x_level;
		if ( x_level == null )
			_level = logger.getLevel();
		String prefix1 = "   ";
		String prefix = Commons.StringTimes(tab_text, tab_size*log_node_level);

		String msg = null;
		if ( _message != null)
			msg = _message.replaceAll("(\r\n|\r|\n|\n\r)", "\n"+prefix+"  ");
		else
			msg =":-( unknown message :-(";
		if ( msg.startsWith("ti") || msg.startsWith("tp") || msg.startsWith("tk") ) {
			prefix1 = msg.substring(0, 2)+" ";
			
			msg = msg.substring(3);
			
		}
		boolean do_log_this = true;
		if ( _level == Level.DEBUG) {
			if ( !CoreBuilder.ws.isSee_debug_info())
				do_log_this = false;
		}

		if ( do_log_this )
			logger.log(_level, prefix1+prefix + msg);
		
		if ( logDetails && _paras != null ) {
			msg = _paras.getStringForLineprint().replaceAll("(\r\n|\r|\n|\n\r)", "\n"+prefix+"  ");
			logger.log(_level, prefix + "Det: "+msg);
		}
			
	}
	
	public String getCurrentPrefix() {
		return Commons.StringTimes(tab_text, tab_size*log_node_level);
	}
	
	public void clearFatalStack() {
		exceptionNoticeList = new ArrayList<ExceptionNotice>();
	}
	
	public void printFatalStack(String prefix) {
		Iterator<ExceptionNotice> i = exceptionNoticeList.iterator();
		
		String prefixLevel = Commons.StringTimes(tab_text, tab_size*log_node_level);
		String pre = prefixLevel + prefix;
		int c = exceptionNoticeList.size();
		
		while ( i.hasNext()) {
			
			ExceptionNotice cf = (ExceptionNotice)i.next();
			logger.log(Level.FATAL, c+" "+ Entities.exportEntities(pre+cf.getMsg().replaceAll("\n", "")));
			c--;
		}
	}	
	
	private HashMap<String, Appender> appenderMap = new HashMap<String, Appender>(); // key = id, object = Appender
	private int appenderIndex = 0;
	public String addAppender ( Appender _appender ) {
		String appId = "APPENDER_"+appenderIndex;
		appenderIndex++;
		appenderMap.put(appId, _appender);
		logger.addAppender(_appender);
		return appId;
	}
	
	public void removeAppender ( String _appId ) throws Exception {
		if ( appenderMap.containsKey(_appId)) {
			// diese id existiert
			Appender thisAppender = (Appender)appenderMap.get(_appId);
			appenderMap.remove(_appId);
			logger.removeAppender(thisAppender);
		} else {
			throw new Exception ( "The appender with the id '"+_appId+"' does not exist.");
		}
	}
	
	public void removeAllAppender ( ) {
		appenderMap = new HashMap<String, Appender>();
		logger.removeAllAppenders();
	}
	
	public static String appStandardConsoleAppender () throws Exception {
		if ( CoreBuilder.rslogger == null ) 
			throw new Exception ("RsLogger.appStandardConsoleAppender: no rslogger until now defined");
		if ( consoleAppId == null ) {
			ConsoleAppender consoleAppender = new ConsoleAppender(LogLogger.standardPatternLayout,ConsoleAppender.SYSTEM_OUT);
			consoleAppId = CoreBuilder.rslogger.addAppender(consoleAppender);
		} else {
			CoreBuilder.rslogger.log("RsLogger: A Standard ConsoleAppender was already created!", null, Level.WARN);
		}
		return ( consoleAppId);
		
	}
	
	public void systemdebug(Level level, String prefix ) {
		
		Set<String> keyset = appenderMap.keySet();
		Iterator<String> iterator = keyset.iterator();

		while ( iterator.hasNext() ) {
			String aktId  = (String)iterator.next();
			Appender aktAppender = (Appender)appenderMap.get(aktId);
			
			lc.log(prefix+ Interpreter.declareDebug
					+Commons.leadBlanks(aktId,Interpreter.lb2)
					+Interpreter.debug_equal_nolock
					+"class: problem aktAppender.getClass().getCanonicalName()"
					// +aktAppender.getClass().getCanonicalName()
					, level);	
		
		}				
		
		
	}


	public ArrayList<ExceptionNotice> getFatalStack() {
		return exceptionNoticeList;
	}
	
	

}