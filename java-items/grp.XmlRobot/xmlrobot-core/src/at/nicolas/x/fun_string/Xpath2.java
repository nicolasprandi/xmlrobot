/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Xpath2 implements IFun {

	private static final String funn= "xpathx";
	private static final String pm= "function 'xpathx' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() < 3 ||  vcar_list.size() > 4) 
			throw new LangException(pm+"wants three or four parameters: 1=Default 2=file, 3=xpath, 4=@attributename");
		

		String grundwert = ((Vcar)vcar_list.get(0)).s;
			
		String xml = ((Vcar)vcar_list.get(1)).s;
		String xpath = ((Vcar)vcar_list.get(2)).s;

		if ( !CommonsVfs.existsFile(xml,rh.getSitzung())) 
			throw new Exception(pm+"'"+xml+"' is not a File !!!");

		String result;
		Document heyDocument = CommonsVfs.getXmldocumentByFilename(xml,rh.getSitzung());
		Element  heyRootElement = heyDocument.getRootElement();
		
		try {
			Element x = Commons.xpathSelectSingleNode(heyRootElement, xpath);
			
			if ( x == null ) {
				String m = "function 'xpath2' with xpath '"+xpath+"'found nothing in '"+xml+"'";
				Exception e = new Exception(m);
				// lc.addException(e );
				throw e;
			}
			
			
			
			if ( vcar_list.size() == 4 ) {
				result = Xpath.getValue(x, ((Vcar)vcar_list.get(3)).s,false);
			} else
				result = Xpath.getValue(x, null,false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// lc.log_ext("Catched Exception", this);
			result = grundwert;

		}
		
		
		//lc.log_ext("xpathx result is: "+result,this);
		return (new Vcar(result));
	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	



}