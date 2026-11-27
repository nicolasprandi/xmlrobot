// Copyright (c) 2023 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.csv2xml;

import org.jdom2.Element;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class Csv2Xml implements ITask {

	



	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		// TODO Auto-generated method stub
		Csv2XmlCore reader = new Csv2XmlCore();
		/*[requo-timer]*/
		
		String root_name = "root";
		if ( e.getAttribute("root_name") != null )
			root_name = e.getAttributeValue("root_name");
		String row_name = "row";
		/*[requo-timer]*/
		if ( e.getAttribute("row_name") != null )
			row_name = e.getAttributeValue("row_name");		
		reader.go(
				e.getAttributeValue("csv"),
				e.getAttributeValue("xml"),
				root_name,row_name,
				e.getAttributeValue("format"),
				e.getAttributeValue("csv_encoding"),
				rh);
		/*[requo-customer]*/
		return null;
	}	

}