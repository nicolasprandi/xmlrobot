/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.ISitzung;

public class InterJobFile {

	String all = null;
	
	String head = null;
	
	ArrayList<String> arrayListJobId = null;
	ArrayList<String> arrayListJobText = null; 
	
	String filename = null;
	int    type = -1;
	
	public static final int JOBFILEEDITOR_HEAD = 0;
	public static final int JOBFILEEDITOR_ALL = 1;
	public static final int JOBFILEEDITOR_JOB = 2;
	
	private LogConnector lc = new LogConnector("JobFile");

	
	private InterJobFile ( 	String _filename, 
						int _type, 
						String _all, 
						String _head, 
						ArrayList<String> _arrayListJobId, 
						ArrayList<String> _arrayListJobText ) {
	
		filename = _filename;
		type = _type;
		all = _all;
		head = _head;
		arrayListJobId = _arrayListJobId;
		arrayListJobText = _arrayListJobText;
		
		
	}
	
	public InterJobFile clone ( String _filename ) {
		InterJobFile neu = null;
		
		if ( type == JOBFILEEDITOR_ALL) {
			neu = new InterJobFile (  _filename, type,
								new String(all), null, 
								null, null );
		} else {
			neu = new InterJobFile ( _filename, 
								type,
								null, 
								new String(head), 
								new ArrayList<String>(arrayListJobId), 
								new ArrayList<String>(arrayListJobText) );			
		}
		
		return (neu);
	}
	private ISitzung theInterJobFileSitzung;
	public InterJobFile ( String _filename, int _type, ISitzung interJobFileSitzung ) throws Exception  {
		theInterJobFileSitzung = interJobFileSitzung;
		filename = _filename;
		type = _type;
		LogConnector lc = new LogConnector("JobFile(String,int):");
		
		try {
			Document document = CommonsVfs.getXmldocumentByFilename(filename,interJobFileSitzung);
			
			// all erstellen
			Format prettyFormat = Format.getPrettyFormat();
			XMLOutputter outputter = new XMLOutputter(prettyFormat);
			if ( type == JOBFILEEDITOR_ALL) {
				all = outputter.outputString(document);
				head = null;
				arrayListJobId = null;
				arrayListJobText = null;
			} else {
				all = null;
				Element root = document.getRootElement();
				ArrayList<Element> headArray = new ArrayList<Element>();
				List<Element>      headList = root.getChildren();
				Iterator<Element>  headIterator = headList.iterator();
				while ( headIterator.hasNext()) {
					Element aktHead = (Element)headIterator.next();
					if ( aktHead.getName().equals("job")) 
						break;
					headArray.add(aktHead);
				}
				head = outputter.outputString(headArray);
				
				// Jobs
				arrayListJobId = new ArrayList<String>();
				arrayListJobText = new ArrayList<String>();

				List<Element> jobList = root.getChildren("job");
				Iterator<Element> jobIterator = jobList.iterator();
				while ( jobIterator.hasNext()) {
					Element aktJob = (Element)jobIterator.next();
					arrayListJobId.add(aktJob.getAttributeValue("id") );
					arrayListJobText.add( outputter.outputString(aktJob) );
					// logger.debug(outputter.outputString(aktJob));
				}
			}

		} catch (Exception e) {
			throw e;

		}
		
	}
	
	public String getAll() {
		return all;
	}
	
	public void setAll( String _all ) {
		all = _all;
	}

	public String getHead() {
		return head;
	}
	
	public void setHead( String _head ) {
		head = _head;
	}
	
	public String getJob( String id ) {
		String retval = null;
		
		for ( int i = 0; i < arrayListJobId.size(); i++ ) {
			if ( ((String)arrayListJobId.get(i)).equals(id) ) {
				retval = (String)arrayListJobText.get(i);
				i = arrayListJobId.size();
			}
		}
		return ( retval );
		
	}

	public void setJob( String id, String text ) {
		for ( int i = 0; i < arrayListJobId.size(); i++ ) {
			if ( ((String)arrayListJobId.get(i)).equals(id) ) {
				arrayListJobText.set(i,text);
				i = arrayListJobId.size();
			}
		}
		
	}
		
	public boolean save() throws Exception {
		boolean retval = true;
		String filetext = getFiletext();
		try {
			CommonsVfs.writeToUTF8File(filetext,filename,theInterJobFileSitzung);
			// Commons.writeToFile(filetext,filename);
			// logger.info(filename + " saved!");
		} catch (Exception e) {
			throw e;
			// lc.fatalException("Problem in writing the file "+filename+", "+ e.getLocalizedMessage(),e);
			//retval = false;
		}
		return ( retval );

	}
	
	public String getFiletext() {
		String retval = null;
		if ( type == JOBFILEEDITOR_ALL) {
			retval = all;
		} else {
			retval = "<?xml version=\"1.0\" ?>"
					+"<jobliste>"
					+ head;
			for ( int i = 0; i < arrayListJobId.size(); i++ ) {
				retval = retval + (String)arrayListJobText.get(i);
			}
			retval = retval + "</jobliste>";

		}
		return ( retval );
		
	}
	

}