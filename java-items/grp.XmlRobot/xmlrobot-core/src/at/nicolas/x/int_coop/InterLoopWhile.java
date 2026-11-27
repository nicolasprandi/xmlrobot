/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.IFunEvalHandler;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class InterLoopWhile  implements IInterLoop {


	ILogConnectorLogic lcl;
	LogConnector lcd = new LogConnector("");	

	private String idE = null;
	private String idN = null;
	private String wh = null;
	private int limit = -1;
	IFunEvalHandler eh;
	Interpreter interpreter;
	// private String name;

	public int pos = 0;

	
	public String getIdN () {
		return idN;
	}
	
	public InterLoopWhile (
			Element elementN, 
			IFunEvalHandler eh,
			Interpreter interpreter, 

			ILogConnectorLogic lcl ) throws Exception {
		
		this.lcl = lcl;

		idN = elementN.getAttributeValue("id");
		if ( idN == null )
			throw new Exception("must specify attribute 'id' for loop-While");
		else if ( idN.startsWith("sys_"))
			throw new LangException(231,"Loops can't have an id starting with 'sys_' but found: "
					+ idN);
		idE = Entities.importEntities(idN);
		

		wh = elementN.getAttributeValue("while");

				
		if ( wh == null )
			throw new Exception("must specify attribute 'while' for loop-While");
		
		if ( elementN.getAttributeValue("limit") != null ) {
			limit = Commons.string2int(elementN.getAttributeValue("limit"));
		}
		this.interpreter = interpreter;
		
		this.eh = eh;
		// name = "WhileLoop ["+idE+"]";
		// lc.core(name+" started ...",LogConnector.CORE_STRUCTURE);

		//lcl.setLogicHeader(idN+".loop_while");
		lcl.setLogicHeader(idN);
		lcl.logicStart("(a)","Started");

	}
	public boolean hasNext() throws Exception {
		// has next is always called for entrance
		
		if ( limit != -1 && pos>=limit) { 
			// lc.core(name+" stopped by reaching the max.",LogConnector.CORE_STRUCTURE);
			lcl.logicEnd("(z)","Finished, by reaching max of "+limit);
			return false;
		}
		
		String wh2 = interpreter.translateValue(wh);
		
		/*
		{ // Gas Nic
			Expression expr = new Expression(wh2);
			Vcar vcar = expr.evaluate(eh);
			if ( !wh2.equals(vcar.s)) {
				throw new Exception("in 'loop_while.hasNext' gibts ein problem [wh2]"+wh2
						+" [expr]"+vcar.s);
			}
		}*/
		
		
		if (wh2.equals("0")) {
			lcl.logicEnd("(z)","Finished.");
			return false;
		} else if (wh2.equals("1")) {
			lcl.logic("("+Integer.toString(pos+1)+")",null);
			return true;
		} else {
			throw new Exception("WhileLoop: while ["+wh+"] does not deliver a boolean, it delivers:"+wh2);
		}
		/*	
		if ( !vcar.isBool) throw new Exception("WhileLoop: while ["+wh2+"] does not deliver a boolean, it delivers '"+vcar.s+"'");
		
		if (!vcar.getBool) {
			lcl.reportEnd("(z)","Finished.");
		} else { 
			lcl.report("("+Integer.toString(pos+1)+")",null);
			
		}
		return (vcar.getBool);
		*/

	}		
	public void next() {		
		 pos++;
	}
	

	public String translate ( String i ) throws Exception {


		int klammerauf = i.indexOf("(loop,"+idE);
		if ( klammerauf == -1 ) {
			return null;
		}
		
		int klammerzu = Commons.getClosingBracket(i, klammerauf+6);
		if ( klammerzu == -1 )
			throw new LangException(232,"while-Loop didnt find closing bracket in: "+i);
		
		String r = Commons.stringReplace(i, "(loop,"+idE+",#i)",Commons.int2String(pos)); 
		return r;
		
	}
	
	public Vcar lwValue ( String value_type ) throws Exception {
		// #i
		String r = null;
		if ( value_type.equals("#i") ) {
			r = Commons.int2String(pos);	
		} else {
			throw new LangException(233,"The loop_while '"
						+idN
						+"' received an unknown request '"+value_type+"'");
		}
		
		return (new Vcar(r));
		
	}	
	/*
	public Vcar lwValue ( ArrayList<Vcar> vcar_list ) throws Exception {
		// 1= loop-id
		// 2= value
		String r = null;
		if ( vcar_list.size() == 3 ) {
			String x = ((Vcar)vcar_list.get(2)).s;
			
			if ( x.equals("#i") ) {
				r = Commons.int2String(pos);	
			}
			
		}
		
		return (new Vcar(r));
		
	}	
	*/	
	
	public void debugx(int index, Level level) {
		
		if ( index == 0 )
			lcd.log(Interpreter.declareDebug
					+Commons.trailBlanks("whileLoop",Interpreter.lb)
					+Commons.leadBlanks(idE, Interpreter.lb2)
					+": while=\""
					+wh
					+"\"",
					level);
		else
			lcd.log(Interpreter.declareDebug
					+Commons.trailBlanks("",Interpreter.lb)
					+Commons.leadBlanks(idE, Interpreter.lb2)
					+": while=\""
					+wh
					+"\"",
					level);				
	}	
	public static void staticStartup() {
		
	}
}