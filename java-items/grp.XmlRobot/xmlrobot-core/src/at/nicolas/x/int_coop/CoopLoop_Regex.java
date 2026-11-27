/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.ITaskLogic;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class CoopLoop_Regex  implements ITask, ITaskLogic, ICoop {
	
	String remain_prop_idN = null;
	InterLoopRegex loopRegex = null;
	
	
	IRequestHandler rh;

	String tname;
	
	private static final int act_open   = 0;
	private static final int act_close   = 1;

	private static KeyWordMapper tasknames;
	
	public Parameters execute(Element eN, IRequestHandler rh) throws Exception {
		this.rh = rh;
		
		if ( eN.getAttribute("remain_prop_id") != null )
			this.remain_prop_idN=eN.getAttributeValue("remain_prop_id");
			
		if ( eN.getAttribute("act") == null ) {
			throw new Exception(tname+" needs attribute @act (open,close)");
		}
		
		if ( eN.getAttribute("id") == null ) {
			throw new Exception(tname+" needs attribute @id");
		}		
		
		//String action = eN.getAttributeValue("act");
		String idN = eN.getAttributeValue("id");
		
		Parameters retParas = null;
		
		int ac = tasknames.convert(eN.getAttributeValue("act"));

		switch ( ac ) {
		case act_open: {	
			// starting the loop
			if ( Commons.hasFlag(eN, "with_next"))
				throw new LangException(219,"Coop loop_regex has flag 'with_next' not implemented yet");
			loopRegex = new InterLoopRegex(eN,logConnectorLogic,rh.getSitzung());
			rh.putNo(idN, Wellknown.coop_type_loop_regex, this);
						
			break;
		}


		default: { 
			CoopLoop_Regex loop_regex = (CoopLoop_Regex)rh.getNo(idN,  Wellknown.coop_type_loop_regex);
			if ( loop_regex == null ) 
				throw new LangException(220,"The "+tname+" with id '"+idN+"' was not found");
			
		    switch ( ac ) {
		    case act_close: {
		    	retParas = new Parameters();
				/*
				Sp.p("setting x_remain");
				retParas.setParameter("prop_"+id+"_remain", 
							loop_regex.loopRegex.getRest());
							*/
				
				if ( loop_regex.remain_prop_idN != null ) {
				
					retParas.setParameter( "prop_"+loop_regex.remain_prop_idN, 
							Entities.exportEntities(loop_regex.loopRegex.getRest()));				
				}
			
				rh.removeNo(idN, Wellknown.coop_type_loop_regex);
			
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
		tasknames = new KeyWordMapper("CoopLoopRegex-Actions",2);
		tasknames.put("open", act_open);
		tasknames.put("close", act_close);
	}


	ILogConnectorLogic logConnectorLogic;
	@Override
	public void setLogConnectorLogic(ILogConnectorLogic logConnectorLogic) {
		this.logConnectorLogic = logConnectorLogic;
		
	}






}