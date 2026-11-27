/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

public interface ILogConnector {
	
	public void log_ext(String msg, ITask from);
	public void log_ext(String msg, IFun from);
	
	public void info(String msg, ITask from);
	public void warn(String msg, ITask from);
	public void trace(String msg, ITask from);
	public void debug(String msg, ITask from);
	
	public void info(String msg, IFun from);
	public void warn(String msg, IFun from);
	public void trace(String msg, IFun from);	
	public void debug(String msg, IFun from);		
		
	public void fatal ( String message );	
	public void error ( String message );
		
	public void printExceptionStack ( Exception e);
	
	public boolean debugIsOn();
	public boolean traceIsOn();

	
}