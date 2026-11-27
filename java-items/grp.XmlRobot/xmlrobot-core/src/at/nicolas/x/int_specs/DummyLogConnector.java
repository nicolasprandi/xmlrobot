/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.ITask;

public class DummyLogConnector implements ILogConnector {

	@Override
	public void info(String msg, ITask from) {
		Sp.p(msg);

	}

	@Override
	public void warn(String msg, ITask from) {
		Sp.p(msg);

	}

	@Override
	public void trace(String msg, ITask from) {
		Sp.p(msg);

	}
	
	@Override
	public void info(String msg, IFun from) {
		Sp.p(msg);

	}

	@Override
	public void warn(String msg, IFun from) {
		Sp.p(msg);

	}

	@Override
	public void trace(String msg, IFun from) {
		Sp.p(msg);

	}	

	@Override
	public void debug(String msg, IFun from) {
		Sp.p(msg);

	}

	@Override
	public void debug(String msg, ITask from) {
		Sp.p(msg);

	}
	// override
	public void addException(String msg) {
		Sp.p(msg);

	}

	// @Override
	public void addException(Exception e) {
		Sp.p(e.getLocalizedMessage());

	}

	@Override
	public void printExceptionStack(Exception e) {
		Sp.p(e.getLocalizedMessage());

	}

	@Override
	public void fatal(String message) {
		Sp.p(message);

	}

	// @Override
	public void addException2(String msg, String job, String jobfile) {
		Sp.p(msg);
		
	}

	// @Override
	public void addException2(Exception e, String job, String jobfile) {
		Sp.p(e.getLocalizedMessage());
		
	}

	@Override
	public void log_ext(String msg, ITask from) {
		// TODO Auto-generated method stub
		Sp.p(msg);
	}

	@Override
	public void log_ext(String msg, IFun from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean debugIsOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean traceIsOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void error(String message) {
		Sp.p(message);
		
	}



}