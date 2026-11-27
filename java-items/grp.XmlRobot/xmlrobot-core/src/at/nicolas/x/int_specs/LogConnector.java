/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_specs;

import org.apache.log4j.Level;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class LogConnector  implements ILogConnector {

	public static final int CORE_EXPRESSIONS = 0;		/* Functions */
	public static final int CORE_STRUCTURE = 1;			/* loops, choose, exceptions */
	public static final int CORE_JOBMANAGER = 2;		/* loops, choose, exceptions */
	public static final int CORE_CLASSLOAD = 3;			/* Class load xrif, xrit */
	public static final int CORE_TERMINAL = 4;			/*  alles vom Terminal*/
	public static final int CORE_TRANSLATE = 5;			/*  Translations: prop, dir, file, moment*/	
	public static boolean[] core_on = {false,false,false,false,false,false};
	public static String[] core_names = {"EXPRESSIONS","STRUCTURE","JOBMANAGER","CLASSLOAD","TERMINAL","TRANSLATE"};
	
	public static final String interpreter = "Interpreter: ";
	public static final String function = "Function: ";
	public static final String task = "Task: ";

	public String from = null;
	private boolean dots_off = false;
	public LogConnector ( String _from ) {
		from = _from;
	}
	
	public boolean debugIsOn() {
		return CoreBuilder.rslogger.debugIsOn();
	}
	public boolean traceIsOn() {
		return CoreBuilder.rslogger.traceIsOn();
	}
	
	public void setFrom ( String _from ) {
		from = _from;
	}
	public void setDotsOff( boolean _dots_off ) {
		dots_off = _dots_off;
	}
	public void core_on() {
		
		for ( int i=0; i<core_on.length; i++ ) {
			core_on[i]=true;
		}
	}
	
	public void core_on(int from) {
		core_on[from]=true;
	}
	
	public void core_off() {
		for ( int i=0; i<core_on.length; i++ ) {
			core_on[i]=false;
		}
	}	
	
	public void core_off(int from) {
		core_on[from]=false;
	}	
		
	public void core ( String message, int from ) {
		
		if ( core_on[from] == true ) {
			current ( "|| "+core_names[from]+" || "+message );
		} if ( from ==CORE_STRUCTURE) {
			trace ( "|| "+core_names[from]+" || "+message );
		}
	}		
	
	public void abend_to_eleminate ( String message ) throws Exception {
		//abend ( message, null);
	}
	
	public String getAbendString ( String message, Parameters paras ) {
		if ( paras == null )
			return message;
		else {
			String p = paras.getStringForLineprint();
			return message + " " + p;
		}
	}


	public void debug ( String message, Parameters paras ) {
		log ( message, paras, Level.DEBUG);
	}
	public void debug ( String message ) {
		log ( message, null , Level.DEBUG);
	}
	
	public void debug_tasks ( String message ) {
		if ( CoreBuilder.rslogger.isDebug_filter_tasks() )
			log ( message, null , Level.DEBUG);
	}	
	
	public void debug_expressions ( String message ) {
		if ( CoreBuilder.rslogger.isDebug_filter_expressions() )
			log ( message, null , Level.DEBUG);
	}	
	
	public void debug_calls ( String message ) {
		if ( CoreBuilder.rslogger.isDebug_filter_calls() );
			log ( message, null , Level.DEBUG);
	}		
	
		
	
	public void debug_logic ( String message ) {
		if ( CoreBuilder.rslogger.isDebug_filter_logic() ) {
			log ( message, null , Level.DEBUG);
			
		}
	}	
	
	public void debug_extensions ( String message ) {
		// if ( Conf.rslogger.isDebug_filter_extensions() )
			log ( message, null , Level.TRACE);
	}	

	public void trace ( String message, Parameters paras ) {
		log ( message, paras, Level.TRACE);
	}
	public void trace ( String message ) {
		log ( message, null, Level.TRACE);
	}

	public void info ( String message, Parameters paras ) {
		log ( message, paras, Level.INFO);
	}
	
	public void info ( String message ) {
		
		log ( message, null, Level.INFO);
	}


	public void fatal ( String message, Parameters paras ) {
		log ( message, paras, Level.FATAL);
	}

	public void fatal ( String message ) {
		 log ( message, null, Level.FATAL);
	}
	
	public ExceptionNotice getLastFatal() {
		return CoreBuilder.rslogger.getLastExceptionNotice();
	}

	public void fatalException ( String message, Parameters paras, Exception e, String job, String jobfile ) {
		
		CoreBuilder.rslogger.getFatalStack().add( new ExceptionNotice (message,paras,e,job,jobfile));
		log ( message, paras, Level.TRACE);
		
	}
	public int getFatalSize() {
		return CoreBuilder.rslogger.getFatalStack().size();
	}
	public void trimFatalStack(int tosize ) {
		while ( CoreBuilder.rslogger.getFatalStack().size() > tosize) {
			(CoreBuilder.rslogger.getFatalStack()).remove
			(CoreBuilder.rslogger.getFatalStack().size()-1);
		}
	}	
	
	private void fatalException ( String message, Parameters paras, Exception e ) {
		
		CoreBuilder.rslogger.getFatalStack().add( new ExceptionNotice (message,paras,e));
		log ( message, paras, Level.TRACE);
		
		
	}

	private void fatalException ( String message, Exception e ) {
		fatalException( message, null, e );
	}
	
	private void fatalException ( Exception e ) {
		fatalException( e.getLocalizedMessage(), null, e );
	}	
	


	
	public void addExceptionIfNew ( String msg, Exception last) {
		ExceptionNotice fe = getLastFatal();
		if ( fe != null ) {
			Exception lastfatal = fe.getException();
			if ( lastfatal != last	) {
				Sp.p("** addedNewException because new lastfatal="+lastfatal.getLocalizedMessage()
						+", last="+last.getLocalizedMessage());
				Exception e = new Exception(msg);
				fatalException( e );
			} else		
				Sp.p("** same Exception");
		} else {
			Exception e = new Exception(msg);
			fatalException( e );
		}		
	}	
	
	public void addExceptionIfNew ( Exception msg, Exception last) {
		
		ExceptionNotice fe = getLastFatal();
		if ( fe != null ) {
			Exception lastfatal = fe.getException();
			if ( lastfatal != last	) {
				fatalException( msg );
			}
		} else {
			fatalException( msg );
		}


		
	}		
	
	/*
	public void addException (Exception e) {

		fatalException( e );
	}		
	*/
	
	// Override
	/*
	public void addException2(String msg, String job, String jobfile) {
		fatalException ( msg, null, null, job, jobfile );
		
	}
	*/

	// Override
	/*
	public void addException2(Exception e, String job, String jobfile) {
		fatalException ( null, null, e, job, jobfile );
		
		
	}	
	*/
	@Override
	public void printExceptionStack ( Exception e ) {
		
			fatal("*         *   Java-ExceptionStack:" );
			Sp.p("*         *   Java-ExceptionStack:");
			
			fatal("*         *   Class:"+e.getClass().getCanonicalName() );
			Sp.p("*         *   Class:"+e.getClass().getCanonicalName());			
			
			StackTraceElement[] trace = e.getStackTrace();
		    for (int i = 0; i < trace.length; i++) {
		        fatal("*         *      " + trace[i]);
		        Sp.p("*         *      " + trace[i]);
		    }

		    Throwable throwable = e.getCause();
		    if (throwable != null) {
		        fatal("*         *      Caused by:"+throwable.getMessage());	
		        Sp.p("*         *      Caused by:"+throwable.getMessage());
		    }
			
	}	
	/*
	public void printExceptionStackxxx(Exception e) {
		// TODO Auto-generated method stub
		StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
        	Sp.p(trace[i].getClassName()+" in line :"+trace[i].getLineNumber());
        	fatal(trace[i].getClassName()+" in line :"+trace[i].getLineNumber());
        }
        Sp.p("--");
    
        
        Throwable throwable = e.getCause();
        if (throwable != null) {
        	Sp.p("*         *      Caused by:"+throwable.getMessage());	
        	fatal ("*         *      Caused by:"+throwable.getMessage());	
        }
	}	
	*/
	
	public void warn ( String message, Parameters paras ) {
		log ( message, paras, Level.WARN);
	}

	public void warn ( String message ) {
		log ( message, null, Level.WARN);
	}


	public void warn ( String message, boolean doit ) {
		if (doit)
		log ( message, null, Level.WARN);
	}	
	public Level getCurrentLevel () {
		return CoreBuilder.rslogger.getLevel();
	}
	public void current ( String message, Parameters paras ) {
		Level curLevel = CoreBuilder.rslogger.getLevel();
		log ( message, paras, curLevel);
	}

	public void current ( String message ) {
		current ( message, null);
	}
	
	public void log ( String message, Level level ) {
		log ( message, null, level);
	}
	
	public void log ( String message, Parameters paras,Level level ) {
		
		if ( CoreBuilder.rslogger == null ) {
			Sp.p( "==> "+from+", "
			 		+ level.toString()+": "+message);
			if ( paras != null) {
			    String raus1 = paras.getStringForLineprint().replaceAll("(\r\n|\r|\n|\n\r)", "\n                           ");
				Sp.p("                Paras are: "+raus1);
			}
		} else {
			
			if ( !Commons.isEmpty(from) ) {
				if ( dots_off)
					CoreBuilder.rslogger.log(from+ message, paras, level); // doppelpunkt weg
				else
					CoreBuilder.rslogger.log(from+ ": "+ message, paras, level);
			} else 
				CoreBuilder.rslogger.log(message, paras, level);
		}
	}

	
	public void setLevel ( Level _level ) {
		if ( CoreBuilder.rslogger != null ) CoreBuilder.rslogger.setLevel(_level);
	}
	public void setLevel ( String _level ) {
		if ( CoreBuilder.rslogger != null ) CoreBuilder.rslogger.setLevel(Level.toLevel(_level.toUpperCase()));
	}
	
	public void clearFatalStack() {
		CoreBuilder.rslogger.clearFatalStack();
		
	}
	
	public void printFatalStack(String prefix) {

		CoreBuilder.rslogger.printFatalStack(prefix);
	}

	public void log_ext(String msg, ITask from) {
		debug_extensions( " [?] "+msg);		
	}	
	public void log_ext(String msg, IFun from) {
		debug_extensions( " [...] "+msg);		
	}		
	
	public void info(String msg, IFun from ) {
		info(" [...] " +msg);		
	}

	public void warn(String msg, IFun from) {
		warn(" [...] " +msg);		
	}
	
	public void trace(String msg, IFun from) {
		trace( " [...] "+msg);		
	}	

	public void debug(String msg, IFun from) {
		debug( " [...] "+msg);	
	}
	
	// Ixrt
	public void info(String msg, ITask from) {
		info(" [?] " +msg);		
	}

	public void warn(String msg, ITask from) {
		warn(" [?] " +msg);		
	}
	
	public void trace(String msg, ITask from) {
		trace( " [?] "+msg);		
	}	

	public void debug(String msg, ITask from) {
		debug( " [?] "+msg);	
	}	



	@Override
	public void error(String message) {
		// error( message);	
		CoreBuilder.rslogger.log(message, null, Level.ERROR);
	}
	public static Level getLevel() {
		return CoreBuilder.rslogger.getLevel();
	}
	public static Level getLevel (String _levelString ) {
		Level level = null;
		
		if ( _levelString == null )           	 level = CoreBuilder.rslogger.getLevel();
		else if ( _levelString.equals("all"))    level = Level.ALL;
		else if ( _levelString.equals("debug"))  level = Level.DEBUG;
		else if ( _levelString.equals("error"))  level = Level.ERROR;
		else if ( _levelString.equals("fatal"))  level = Level.FATAL;
		else if ( _levelString.equals("info"))   level = Level.INFO;
		else if ( _levelString.equals("i"))      level = Level.INFO;
		else if ( _levelString.equals("off"))    level = Level.OFF;
		else if ( _levelString.equals("trace"))  level = Level.TRACE;
		else if ( _levelString.equals("warn"))   level = Level.WARN;	
		else									 level = CoreBuilder.rslogger.getLevel();			
		return level;
	}	

}