/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.io.IOException;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.StorageLimitException;


public class InterSequence {

	LogConnector lc = new LogConnector("Sequence");
	LogConnector lcd = new LogConnector("");
	
	public String id = null;
	public String filename = null;
	public InterSequenceFile sequence = null;
	public String defaul_len = null;

	public static String getFilnameOfElement(Element eE ) throws Exception {
		String filename;
		if ( eE.getAttribute("file") != null && eE.getAttribute("xml") != null) {
			throw new LangException(204,"task 'sequence' can have either attribute 'file' or attribute 'xml', not both.");
		} else if ( eE.getAttribute("file") != null ) {
			filename = eE.getAttributeValue("file");
		} else if ( eE.getAttribute("xml") != null ) {
			filename = eE.getAttributeValue("xml");
		}  else {
			throw new LangException(205,"task 'sequence' needs attribute 'file' or 'xml'");
		}		
		return filename;
	}
	public InterSequence (Element element,ISitzung sitzung) throws Exception  {
		id = element.getAttributeValue("id");
		if ( id.startsWith("sys_"))
			throw new LangException(206, "Sequences can't have an id starting with 'sys_' but found: "
					+ Entities.exportEntities(id));
		filename = getFilnameOfElement(element );

		if ( element.getAttribute("len") != null ) {
			defaul_len = element.getAttributeValue("len");
		}
		sequence = new InterSequenceFile (filename,defaul_len,sitzung);
	}
	
	public String translate ( String in ) {
				
		/*if ( in.equals("(sequence,"+id+",+)"))
			return sequence.getNext();
		else */
		if (in.equals("(sequence,"+id+")"))
			return sequence.getLast();
		else if ( in.equals("(sequence,"+id+",?)"))
			return sequence.getCandidate();
		else
			return null;

	}
	
	public void close() throws IOException, StorageLimitException {
		sequence.close();
	}
	public String getFnEscaped () {
		return ( filename);
	}
	public void debugx(int index, Level level ) {
		
		if ( index == 0 ) 
			lcd.log(Interpreter.declareDebug
					+ Commons.trailBlanks("(seq,",Interpreter.lb2) 
					+ ")"
					, level);
					
		lcd.log(Interpreter.declareDebug
				+Commons.leadBlanks(id, Interpreter.lb2)
				+Interpreter.debug_equal_nolock
				+sequence.getLast()
				+ " in "
				+ filename
				, level );				
	}	


}