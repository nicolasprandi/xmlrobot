// Copyright (c) 2023 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.csv2xml;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;

import com.opencsv.CSVReader;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;

//import at.nicolas.x.int_specs.Commons;

public class Csv2XmlCore {

	public void go ( String csv, String xml, 
							String root_name, String row_name,
							String f,
							String encoding,
							IRequestHandler rh) throws Exception {
		if ( f==null )
			throw new Exception("Need format");
        CSVReader reader = null;
        
		FileObject fo_csv = CoreBuilder.fsm.resolveFile(csv);
		
		if ( CommonsVfs.getTimeOfFileObject(fo_csv,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}
        
        InputStream is = CommonsVfs.getInputStreamByFilename(csv, rh.getSitzung());
        
        if ( encoding == null || encoding.length()==0)
        	reader = new CSVReader(new InputStreamReader(is));
        else	
        	reader = new CSVReader( 
        			new InputStreamReader(is, encoding));
        
        String[] firstLine = null;
        String[] nextLine;
        /*[requo-timer]*/
        Element rootElement = new Element(root_name);
        /*[requo-customer]*/
        while ((nextLine = reader.readNext()) != null) {
        	if ( firstLine == null ) {
        		firstLine = nextLine;        	
        		// printArray(firstLine, "Csv-header of "+csv);
        	} else {
        		Element rowElement = new Element(row_name);
        		for ( int i=0; i <= nextLine.length-1; i++) {
        			if ( i < firstLine.length  ) {
            			String tit = firstLine[i];
            			String con = nextLine[i];
            			if ( con != null && con.length() > 0) {
            				if ( f.equals("e")) {
        	        			Element valElement=new Element("val");
        	        			valElement.setAttribute("name", tit);
        	        			valElement.setText(con);
        	        			rowElement.addContent(valElement);            					
            				}  if ( f.equals("a")) {
            					rowElement.setAttribute(tit,con);            					
            				}

            			}        				
        			} else {
        				System.out.println("csv2xml ignored value: "+nextLine[i]);
        			}

        		}
        		/*[requo-timer]*/
        		if ( rowElement.getChildren().size() > 0 && f.equals("e"))
        			rootElement.addContent(rowElement);
        		else if ( rowElement.getAttributes().size() > 0 && f.equals("a"))
        			rootElement.addContent(rowElement);        		
        	}
        }
        reader.close();
        is.close();
        Document doc = new Document();
        doc.setRootElement(rootElement);
        /*[requo-timer]*/
        CommonsVfs.writeToFile(doc, xml,rh.getSitzung());
        //MyCommons.writeToFile(doc, xml,rh);
		
	}
	private static void printArray ( String[] p , String title) {
		System.out.println("Printing: "+title); 
		for ( int i = 0; i<p.length; i++ ) {
			System.out.println("   ["+(i+1)+"] is ["+p[i]+"]");
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir= "C:\\Users\\nicolas\\Documents\\things-to-do\\dwh-dev\\XmlRobot-v031\\xmlrobot-internals\\workspaces\\ws.MyWorkspace\\app.Couronne-DWH\\pro.Countries\\result\\";
		Csv2XmlCore reader = new Csv2XmlCore();
		/*
		reader.go(
				dir+"sql-result.txt",
				dir+"sql-result.xml",
				"rootx","rowy","a","UTF-8");
				*/
	}

}