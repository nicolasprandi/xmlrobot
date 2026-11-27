/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.IFunEvalHandler;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.ITaskLogic;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.Parameters;


public class CoopLoop_While  implements ITask, ITaskLogic, ICoop {

	Element e = null;
	InterLoopWhile loopWhile = null;	
	IRequestHandler rh;	
	String tname;
	ILogConnectorLogic logConnectorLogic;

	
	private IFunEvalHandler eh;
	private Interpreter interpreter;
	
	private static final int act_open   = 0;
	private static final int act_next   = 1;
	private static final int act_close   = 2;

	private static KeyWordMapper tasknames;	
	
	public void setInterpreter( IFunEvalHandler eh,Interpreter interpreter ) {
		this.eh = eh;
		this.interpreter = interpreter;
	}
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.rh = rh;
		if ( e.getAttribute("act") == null ) {
			throw new Exception(tname+" needs attribute @act (open,next,close)");
		}
		
		if ( e.getAttribute("id") == null ) {
			throw new Exception(tname+" needs attribute @id");
		}		
		
		// String action = e.getAttributeValue("act");
		String id = e.getAttributeValue("id");
		
		Parameters retParas = null;
		
		int ac = tasknames.convert(e.getAttributeValue("act"));

		switch ( ac ) {
		case act_open: {	
			// starting the loop
			loopWhile = new InterLoopWhile(
							e, 
							eh,
							interpreter, 
							logConnectorLogic);
			rh.putNo(id, Wellknown.coop_type_loop_while,  this);
			//_lc.log_ext("Opened "+id, this);
			break;
		}


		default: { 	
			CoopLoop_While loop_while = (CoopLoop_While)rh.getNo(id, Wellknown.coop_type_loop_while);
			if ( loop_while == null ) 
				throw new Exception("The "+tname+" with id '"+id+"' was not found");

		    switch ( ac ) {
		    case act_next: {	
		    	loop_while.loopWhile.next();
		    	break;
		    }
		    case act_close: {	
		    	rh.removeNo(id, Wellknown.coop_type_loop_while);
		    	break;
		    }    
		    
		    } 
		  	break;
		}
		}  
		 
		return retParas;
	}
	

	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("CoopLoopWhile-Actions",3);
		tasknames.put("open", act_open);
		tasknames.put("next", act_next);
		tasknames.put("close", act_close);
	}
	@Override
	public void setLogConnectorLogic(ILogConnectorLogic logConnectorLogic) {
		this.logConnectorLogic = logConnectorLogic;
		
	}
}