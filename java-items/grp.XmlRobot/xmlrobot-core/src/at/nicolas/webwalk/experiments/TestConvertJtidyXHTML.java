// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk.experiments;

import java.io.FileOutputStream;
import java.io.InputStream;

public class TestConvertJtidyXHTML {
 //https://stackoverflow.com/questions/15063870/jtidy-java-api-toconvert-html-to-xhtml
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir = "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.WebWalker\\app.wwTest1\\pro.test\\";
		String in = dir+"in.html";
		String out = dir+"out.xml";

		InputStream fis = null;
		FileOutputStream fos = null;	
		/*
		fis = new ByteArrayInputStream(
				(Commons.readTextfileFormatLf(in, "UTF-8", "\n")).getBytes(StandardCharsets.UTF_8));
		
	    Tidy tidy = new Tidy();

	    tidy.setInputEncoding("UTF-8");
	    tidy.setXHTML(true);	        
        tidy.setDropEmptyParas(true);
        tidy.setShowWarnings(false); //to hide errors
        tidy.setQuiet(true); //to hide warning
        tidy.setUpperCaseAttrs(false);
        tidy.setUpperCaseTags(false);	    
	    tidy.setOutputEncoding("UTF-8");
	    fos = new FileOutputStream(out);
	        	
	     tidy.parse(fis, fos);
	     */
	        		
	}

}