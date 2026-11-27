/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.term_core.ITerminatorStyler;
import at.nicolas.x.term_core.Seitenlinks;



public interface ITerminator {
	public void parkIn();
	public void parkOut();
	public void parkForget();
	public void setFileChoose(boolean on);
	public void setDirChoose(boolean on);
	public boolean getFileChoose();
	public boolean getDirChoose();	
	public String getTiScreenBodyMember();
	public String getTiScreenBodyMemberAndConsume(String from);
	public boolean screenIsClear();
	public String getTp();
	public Seitenlinks getTerminalLinkArrayLast();
	public void setProgress( String progress, boolean doLcInfo );
	public void executeTi ( Element tl, Level loglevel, String app, String appdir, String inst, String inst_dir, ITiHandler rh) throws Exception;
	public void executeTk ( Element tl, Level loglevel,  String app, String appdir, String inst, String inst_dir, ITiHandler rh) throws Exception;
	public void executeSomething ( Element tx, Level loglevel, ITiHandler rh) throws Exception;
	public void clear();
	public void clearScreen();
	public void restart();
	public void restart(String app, String inst);

	public int getTk_width();
	public void write(String text);
	public void write(String label,String text);
	public void jobgroupstart (Interpreter interpreter);
	public void jobstart ( String job, String app, String pro, boolean isSc, int call_depth, String tip );
	public void jobend ( String job, String app, String pro, boolean isSc, int call_depth, String jobinfo );
	public void joberror ( String job, String app, String pro, boolean isSc, int call_depth, boolean red );

	public void flush();	
	public void backButtonPressed() ;
	public void foreButtonPressed() ;
	public void tiSave();
	public void top();
	public void bottom();
	public void tiBreak(Element e);	
	public void tiHr(Element e);
	
	public void tiBreakHr(Element e);	
	
	public void tiClear(Element e);
	public void clearMemory();
	public void tiSwitch ( String app, String pro );
	public boolean isWeb();
	
	ITerminatorStyler getStyler();
	
	public void print_tk ( Element eE, Level loglevel, ITiHandler tih, String key,String app, String appdir, String inst, String inst_dir
			, boolean print_with_tips, boolean print_tips_only, boolean print_all_fields, boolean print_all_tested_fields) throws Exception ;
	

}