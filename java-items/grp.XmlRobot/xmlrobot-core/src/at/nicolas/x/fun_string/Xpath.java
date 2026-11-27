/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Xpath implements IFun {

	private static final String funn= "xpath";
	private static final String pm= "function 'xpath' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() < 2 ||  vcar_list.size() > 3) 
			throw new LangException(pm+"wants two or three parameters: 1=file, 2=xpath, 3=@attributename");
		
		Vcar   vfile= (Vcar)vcar_list.get(0);
		Vcar   vxpath= (Vcar)vcar_list.get(1);
	
			
		String xml = vfile.s;
		String xpath = vxpath.s;

		if ( !CommonsVfs.existsFile(xml,rh.getSitzung())) 
			throw new Exception(pm+"says: '"+xml+"' is not a File !!!");
		
		Document heyDocument = CommonsVfs.getXmldocumentByFilename(xml,rh.getSitzung());
		Element  heyRootElement = heyDocument.getRootElement();
		Element x = Commons.xpathSelectSingleNode(heyRootElement, xpath);
		
		if ( x == null ) {
			String m = "*008* xpath didnt find anything in xml. xpath=\""+xpath+"\" xml=\""+xml+"\"";
			Exception e = new Exception(m);
			// lc.addException(e );
			throw e;
		}
		
		String result;
		
		if ( vcar_list.size() == 3 ) {
			result = getValue(x, ((Vcar)vcar_list.get(2)).s,true);
		} else
			result = getValue(x, null,true);
		
		
		//lc.log_ext("result is: "+result,this);
		return (new Vcar(result));
	}


	public String getExtensionId() {
		return funn;
	}
	
	public static String getValue( Element x, String attribute, boolean fatalstack ) throws Exception {
		String result = null;
		if ( attribute != null  ) {
			if ( attribute.startsWith("@")) {
				// attribute
				result = x.getAttributeValue(attribute.substring(1));
				if ( result == null ) {
					String m = "xpath:result '"+Commons.cutNice(Commons.toTextCompact(x),100)+
							"' did not find requested attribute '"+attribute.substring(1)+"'";
					Exception e = new Exception(m);
					//if ( fatalstack)
					//	lc.addException( e);
					throw e;
				}
				
			} else {
				//lc.addException("f,xpath has uncorrect attribute-parameter '"+attribute+"'");
				throw new Exception("function 'xpath', Unknown attribute-parameter: "+attribute);
			}
		} else {
			result = x.getText();
		}
		
		return result;
	}
	
	public static String getValue( Element from, String xpath, String attribute ) throws Exception {
		String result = null;
		Element x;
		if ( xpath == null )
			x = from;
		else 
			x = Commons.xpathSelectSingleNode(from,xpath);
		
		if ( x == null )
			return null;
		
		if ( attribute != null  ) {
			if ( attribute.startsWith("@")) {
				// attribute
				result = x.getAttributeValue(attribute.substring(1));
			} else {
				throw new LangException("Unknown attribute-parameter: "+attribute);
			}
		} else {
			result = x.getText();
		}
		
		return result;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}	


}