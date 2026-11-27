/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Tk_Sel_Translate implements IFun {

	private static final String funn= "tk_sel_translate";
	private static final String pm= "tk_sel_translate: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(pm+"I want 2 parameters: 1=value 2=filename-sel-xml, but received "+vcar_list.size()+" Parameters");
		
		String value = ((Vcar)vcar_list.get(0)).s;
		String filename = ((Vcar)vcar_list.get(1)).s;
		
		Element e = CommonsVfs.getElementByFilename(filename,rh.getSitzung());
		String result = "";
		List<Element> list = e.getChildren("sel");
		Iterator<Element> it = list.iterator();
		while ( it.hasNext()) {
			Element ce = (Element)it.next();
			if ( Commons.hasFlag(value, ce.getText())) {
				String show = ce.getText();
				if ( ce.getAttribute("label") != null )
					show = ce.getAttributeValue("label");
				result = result + show + ", ";
			}
			
		}
		if ( result.endsWith(", "))
			result = result.substring(0, result.length()-2);
				
		return (new Vcar(result));
	}
	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}