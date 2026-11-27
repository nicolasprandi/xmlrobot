/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.term_core.ITerminatorStyler;
import at.nicolas.x.term_core.Seitenlinks;



public class CoreDummyTerminator implements ITerminator {

	@Override
	public void setProgress(String progress, boolean doLcInfo) {
		// TODO Auto-generated method stub

	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	public void gotoLastLine() {
		// TODO Auto-generated method stub
		
	}


	public void setLastLineMark(String x) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart(String app, String inst) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobstart(String job, String app, String pro, boolean isSc,
			int call_depth, String tip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobend(String job, String app, String pro, boolean isSc,
			int call_depth, String jobinfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joberror(String job, String app, String pro, boolean isSc,
			int call_depth, boolean red) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButtonPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void foreButtonPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tiSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void top() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tiBreak(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bottom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeSomething(Element tx, Level loglevel, ITiHandler rh)
			throws Exception {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void clearMemory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tiHr(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tiSwitch(String app, String pro) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void executeTi(Element tl, Level loglevel, String app,
			String appdir, String inst, String inst_dir, ITiHandler rh)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void executeTk(Element tl, Level loglevel, String app,
			String appdir, String inst, String inst_dir, ITiHandler rh)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getTk_width() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void write(String text) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void write(String label, String text) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public Seitenlinks getTerminalLinkArrayLast() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ITerminatorStyler getStyler() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTiScreenBodyMember() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTp() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void jobgroupstart(Interpreter x) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public boolean screenIsClear() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setFileChoose(boolean on) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setDirChoose(boolean on) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean getFileChoose() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean getDirChoose() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void parkIn() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void parkOut() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void parkForget() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getTiScreenBodyMemberAndConsume(String from) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void print_tk(Element eE, Level loglevel, ITiHandler tih, String key, String app, String appdir, String inst,
			String inst_dir, boolean print_with_tips, boolean print_tips_only, boolean print_all_fields, boolean print_all_tested_fields)
					throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void tiBreakHr(Element e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void tiClear(Element e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isWeb() {
		// TODO Auto-generated method stub
		return false;
	}












}