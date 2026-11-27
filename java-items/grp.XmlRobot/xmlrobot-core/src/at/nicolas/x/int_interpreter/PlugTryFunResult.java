/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import at.nicolas.x.pub.Vcar;

public class PlugTryFunResult {

	public boolean executed;
	public Vcar vcar;
	
	public PlugTryFunResult (  boolean executed, Vcar vcar ) {
		this.executed = executed;
		this.vcar = vcar;
		
		
	}
}