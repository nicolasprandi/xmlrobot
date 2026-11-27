/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.Iterator;
import java.util.Set;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class ParaSplit implements ITask{


		
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		
		String value = Commons.getXValue(e);
		if ( value== null) throw new LangException(506,"Task 'para_split' needs attribute @x");
		
		String prefix = e.getAttributeValue("prefix");
		if ( prefix== null) throw new LangException(506,"Task 'para_split' needs attribute @prefix");
		
		String quote = "'";
		if ( e.getAttribute("quote") != null)
			quote=e.getAttributeValue("quote");
		Parameters p = new Parameters(value,quote);
		Set<String> propKeySet = p.getParameterNames();
		Iterator<String> propIter = propKeySet.iterator();
		while ( propIter.hasNext() ) {
			String derName = propIter.next();
			String derInhalt = p.getParameter(derName);
			rh.setVar("prop", Entities.importEntities(prefix+derName), Entities.importEntities(derInhalt));
		}
	
		 		 
		return null;
	}


	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}