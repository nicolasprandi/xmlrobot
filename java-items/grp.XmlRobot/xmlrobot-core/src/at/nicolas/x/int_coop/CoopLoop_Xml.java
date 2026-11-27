/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.ITaskLogic;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.Parameters;


public class CoopLoop_Xml  implements ITask, ITaskLogic, ICoop {
	
	Element e = null;
	InterLoopXml loopXml = null;
	IRequestHandler rh;	
	String tname;
	ILogConnectorLogic logConnectorLogic;
	private static final int act_open   = 0;
	private static final int act_next   = 1;
	private static final int act_close   = 2;
	private static final int act_get_attribute_list   = 3;

	private static KeyWordMapper tasknames;	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.rh = rh;	
		/*
		if ( e.getAttribute("act") == null ) {
			throw new Exception(tname+" needs attribute @act (open,next,close)");
		}*/
		
		if ( e.getAttribute("id") == null ) {
			throw new Exception(tname+" needs attribute @id");
		}		
		
		//String action = e.getAttributeValue("act");
		String id = e.getAttributeValue("id");
		
		int ac = tasknames.convert(e.getAttributeValue("act"));

		switch ( ac ) {
		case act_open: {	
			loopXml = new InterLoopXml(e,logConnectorLogic,rh);
			rh.putNo(id, Wellknown.coop_type_loop_xml,  this);
			
			break;
		}


		default: { 	
			CoopLoop_Xml loop_xml = (CoopLoop_Xml)rh.getNo(id, Wellknown.coop_type_loop_xml);
			if ( loop_xml == null ) 
				throw new Exception("The "+tname+" with id '"+id+"' was not found");

		    switch ( ac ) {
		    case act_next: {	
		    	loop_xml.loopXml.next();
		        break;
		    }
		    case act_close: {	
		    	rh.removeNo(id, Wellknown.coop_type_loop_xml);
		        break;
		    }    
		    
		    case act_get_attribute_list: {	
		    	Element thee = loop_xml.loopXml.getCurrentElement();
				List<Attribute> alist = thee.getAttributes();
				Element r = new Element("attributes");
				Iterator<Attribute> i = alist.iterator();
				while ( i.hasNext()) {
					Attribute ca = (Attribute)i.next();
					Element ne = new Element("attribute");
					ne.setAttribute("name",ca.getName());
					ne.setAttribute("value",ca.getValue());
					r.addContent(ne);
				}
				String spirit_id = e.getAttributeValue("to_spirit");
				CoopSpirit.createSpirit(spirit_id, r, rh);
		    	break;
		    }     
		    
		    } 
		  	break;
		}
		}  

		

		 
		return null;
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
		tasknames = new KeyWordMapper("CoopLoopXml-Actions",4);
		tasknames.put("open", act_open);
		tasknames.put("next", act_next);
		tasknames.put("close", act_close);
		tasknames.put("get_attribute_list", act_get_attribute_list);		
	}
	
	@Override
	public void setLogConnectorLogic(ILogConnectorLogic logConnectorLogic) {
		this.logConnectorLogic = logConnectorLogic;
		
	}

}