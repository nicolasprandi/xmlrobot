/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.Vcar;


public class CoopLx implements IFun{

	String funn;
	String pm;	
	
	private static final int act_has_next   = 0;
	private static final int act_exists   = 1;
	private static final int act_nbr_of_attributes   = 2;
	private static final int act_xml   = 3;

	private static KeyWordMapper tasknames;
	
	public static Element getCurrentElement( IRequestHandler rh, String loop_xpath_id,Object client ) throws Exception {
		CoopLoop_Xml loop_xml = (CoopLoop_Xml)rh.getNo(loop_xpath_id, Wellknown.coop_type_loop_xml);
		if ( loop_xml == null ) {
			throw new Exception("The loop_xml with id '"+loop_xpath_id+"' was not found");
		}	
		return ( loop_xml.loopXml.getCurrentElement());
	}
	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "loop_xml";
		this.pm = funn+": ";
		
		if ( vcar_list.size() < 1 ) 
			throw new Exception(pm+"I want at least 1 parameters, 1=loop-id");

		String function = null;
		if ( vcar_list.size() >= 2)
			function = ((Vcar)vcar_list.get(1)).s;
		
		// the id must exist
		String id = ((Vcar)vcar_list.get(0)).s;
		
		CoopLoop_Xml loop_xml = (CoopLoop_Xml)rh.getNo(id, Wellknown.coop_type_loop_xml);
		if ( loop_xml == null ) {
			throw new Exception(pm+"The loop_xml with id '"+id+"' was not found");
		}
		Vcar result = null;
		if ( function == null ) {
			String stringresult = loop_xml.loopXml.lxValue(vcar_list);
			if ( stringresult == null ) {
				stringresult = loop_xml.loopXml.getMissingStrategy("xGetting a value for loop_xpath cooperation id="+id);
			}
			result = (new Vcar(stringresult));			
		
		} else {
			int ac = tasknames.convertRes(function);

			switch ( ac ) {
			case act_has_next: {	
				boolean hasNext = loop_xml.loopXml.hasNext();
				if ( rh.shouldBreakLoops())
					hasNext = false;
				result = new Vcar(hasNext);
				break;
			}
			case act_exists: {	
				String stringresult = loop_xml.loopXml.lxExists(vcar_list);
				result = (new Vcar(stringresult));
				break;
			}
			case act_nbr_of_attributes: {	
				int siz = loop_xml.loopXml.getCurrentElement().getAttributes().size();
				// String stringresult = loop_xml.loopXml.lxExists(vcar_list);
				result = (new Vcar(siz));
				break;
			}
			case act_xml: {	
				Element toprint = loop_xml.loopXml.getCurrentElement();
				String result_string = Commons.toTextRaw(toprint);

				result = (new Vcar(result_string));
				break;
			}

			default: { 	
				String stringresult = loop_xml.loopXml.lxValue(vcar_list);
				if ( stringresult == null ) {
					stringresult = loop_xml.loopXml.getMissingStrategy("Getting a value for loop_xml cooperation id="+id
							+ " paras="+Vcar.getParaList(vcar_list, "", ""));
				}
				result = (new Vcar(stringresult));
			  	break;
			}
			} 
		}
		
		
		
		return result;		

	}



	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("CoopLx",4);
		tasknames.put("has_next?", act_has_next);
		tasknames.put("exists?", act_exists);
		tasknames.put("nbr_of_attributes?", act_nbr_of_attributes);
		tasknames.put("xml?", act_xml);
		tasknames.putFinished();		
	}



}