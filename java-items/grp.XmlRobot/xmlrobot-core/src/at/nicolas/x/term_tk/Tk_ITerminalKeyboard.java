/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;



public interface Tk_ITerminalKeyboard {

	public void executeTk ( Element tk, Level loglevel, ITiHandler tih, ITerminator iTerminator) throws Exception;

}