/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.StorageLimitException;

public class InterSequenceFile {
	
	private String fiN =null;
	private Document doc =null;
	private Element root = null;
	private int int_lastSequence=0;
	private int len = -1;
	private LogConnector lc = new LogConnector("SequenceFile");
	private ISitzung sitzung;
	public InterSequenceFile ( String _fi, String default_len,ISitzung sitzung ) throws Exception {
	this.sitzung = sitzung;
		fiN=Entities.exportEntities(_fi);
		
		if ( fiN == null || fiN.equals(""))
			throw new LangException(207,"task 'sequence' needs a filename defined as elementtext. eg: <sequ id='x'>(dir,app)sequence1.xml</sequ>"); 
		
		
		if ( !CommonsVfs.existsFile(fiN,sitzung) ) {
			Element root = new Element("sequence");
			root.setAttribute("lastsequence", "0");
			if ( default_len == null || (default_len.trim().length())==0 )
				root.setAttribute("len", "10");
			else
				root.setAttribute("len", default_len);
			Document doc = new Document();
			doc.setRootElement(root);
			CommonsVfs.writeToFile(doc, fiN,sitzung);
			lc.warn("The sequencefile '"+fiN+"' does not exist, but it was created");
			
			
		} 
		
		doc = CommonsVfs.getXmldocumentByFilename(fiN,sitzung);
		root = doc.getRootElement();
		String string_lastSequence = root.getAttributeValue("lastsequence");
		if ( string_lastSequence != null && string_lastSequence.length()>0 ) 
			int_lastSequence = Integer.parseInt(string_lastSequence);
		else
			int_lastSequence = 0;
		String string_len = root.getAttributeValue("len");
		if ( string_len != null && string_len.length()>0 ) 
			len = Integer.parseInt(string_len);
			
	}
	
	public String getNext() {
		int_lastSequence++;
		return format(int_lastSequence);				
	}
	
	public String getLast() {
		return format(int_lastSequence);				
	}	
	
	public String getCandidate() {
		int y = int_lastSequence+1;
		return format(y);				
	}	
	
	private String format(int x) {

		String nummer = Integer.toString(x);
		if ( (len > -1) && ( nummer.length()< len ) ) {
			nummer = Commons.StringTimes("0",len-nummer.length()) + nummer;
		}
		return (nummer );					

	}	
	
	public void close() throws IOException, StorageLimitException {

			root.setAttribute("lastsequence",Integer.toString(int_lastSequence));
			CommonsVfs.writeToFile(doc, fiN,sitzung);
	}


}