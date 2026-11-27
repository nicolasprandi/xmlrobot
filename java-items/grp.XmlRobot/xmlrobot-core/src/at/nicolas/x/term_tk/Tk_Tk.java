/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import javax.swing.JDialog;

import org.jdom2.Element;

import at.nicolas.x.int_specs.ITiHandler;


public abstract class Tk_Tk implements Tk_IKeysOn{
	
	
	public abstract void built (Element tkN,
								ITiHandler tih, String mainLabel, String tip) throws Exception;
	public abstract String getVarType();
	public abstract String getVarIdN();
	public abstract void execute(boolean first_go) throws Exception;
	public abstract String getValueN();
	public abstract String getValue4RegexN();
	public abstract String getValuePN();
	public abstract void clearValue();
	public abstract void setValueN(String value);
	public abstract boolean hasNono();
	
	volatile boolean  windowCanceled = false;
	volatile boolean  performedVfs = false;
	JDialog jDialog;
	
	volatile boolean keys_on = false;
	volatile long    keys_on_start = 0;
	
	public void keysOn()  {
		keys_on = true;		
		keys_on_start = System.currentTimeMillis()+40;
	}
	
	public synchronized void wcTrue() {
		windowCanceled =true;		
	}
	
	public synchronized void wcFalse() {
		windowCanceled =false;		
	}	
	
	public synchronized void wcFalseByVfs() {
		windowCanceled =false;		
		performedVfs = true;
	}	
			
	
	public synchronized void dispose() {
		jDialog.dispose();	
		keys_on = false;	
	}	

}