/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import at.nicolas.x.pub.Parameters;

public class PlugTryTaskResult {

	public boolean executed;
	public Parameters retvals;
	
	public PlugTryTaskResult ( boolean executed, Parameters retvals ) {
		this.executed = executed;
		this.retvals = retvals;
		
	}
}