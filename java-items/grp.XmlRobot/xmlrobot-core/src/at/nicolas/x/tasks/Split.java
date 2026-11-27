/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Split implements ITask{

		
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		
		String txt = e.getAttributeValue("text");
		if ( txt== null) throw new LangException(506,"Task 'split' needs attribute @text");
		
		String regex;
		if ( e.getAttribute("regex")!=null)
			regex = e.getAttributeValue("regex");
		else
			regex = "\\s+";
		
		boolean flag_empty = Commons.hasFlag(e, "empty");
		
		
		String out_file = e.getAttributeValue("out");
		if ( out_file== null) throw new LangException(507,"Task 'split' needs attribute @out");
		
		Element eSplits = new Element("splits");
		String founds[];
		if ( flag_empty )
			founds = txt.split(regex,-1);
		else
			founds = txt.split(regex);

		for ( String found: founds ) {
			
			if ( found.length()> 0 || flag_empty) {
				Element eSplit = new Element("split");
				eSplit.setAttribute("x", found);
				eSplits.addContent(eSplit);
			}
		}
		
		Document document = new Document(eSplits);
		
		CommonsVfs.writeToFile(document, out_file,rh.getSitzung());
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}