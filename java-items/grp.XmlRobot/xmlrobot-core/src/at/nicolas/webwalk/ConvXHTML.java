// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

// import org.w3c.tidy.Tidy;

import at.nicolas.x.int_specs.Commons;

public class ConvXHTML {

	public static void main(String[] args) throws Exception {
		String dir = "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.WebWalker\\app.wwTest1\\pro.test\\";
		String in = dir+"in.html";
		String outhtml = dir+"out.html";
		String outxhtml = dir+"out.xhtml";
		String outxml = dir+"out.xml";
		
		/*
		String html = Commons.readTextfileFormat(in, "UTF-8");
		Commons.writeToEncodedFile(html, outhtml, "UTF-8");
		System.out.println("in:"+html);
		
		
		String xml = html2xml(html,true);
		System.out.println("xml:"+xml);
		Commons.writeToEncodedFile(xml, outxml, "UTF-8");
		
		String xhtml = html2xml(html,false);
		System.out.println("xhtml:"+xhtml);
		Commons.writeToEncodedFile(xhtml, outxhtml, "UTF-8");		
		*/


	}
	
	/*
	 * 
	 	public static void main(String[] args) throws Exception {
		String dir = "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.WebWalker\\app.wwTest1\\pro.test\\";
		String in = dir+"in.html";
		String outhtml = dir+"out.html";
		String outxhtml = dir+"out.xhtml";
		String outxml = dir+"out.xml";
		
		String html = Commons.readTextfileFormat(in, "UTF-8");
		Commons.writeToEncodedFile(html, outhtml, "UTF-8");
		System.out.println("in:"+html);
		String xhtml = html2xhtml(html);
		System.out.println("xhtml:"+xhtml);
		String xml = xhtml2xml(xhtml);
		System.out.println("xml:"+xml);
		Commons.writeToEncodedFile(xhtml, outxhtml, "UTF-8");
		Commons.writeToEncodedFile(xml, outxml, "UTF-8");


	}
	 * 
	 */
	/*
	public static String html2xml( String in_html, boolean removeThings ) {
		
		// round 1
		
		 BufferedReader br = new BufferedReader(new StringReader(in_html));
		  StringWriter sw = new StringWriter();

		  Tidy t = new Tidy();
		  t.setDropEmptyParas(true);
		  t.setShowWarnings(false); //to hide errors
		  t.setQuiet(true); //to hide warning
		  t.setUpperCaseAttrs(false);
		  t.setUpperCaseTags(false);
		  t.parse(br,sw);
		  StringBuffer sb = sw.getBuffer();
		  String strClean = sb.toString();
		  try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			sw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		  
		  // round 2
		  //do another round of tidyness
		  br = new BufferedReader(new StringReader(strClean));
		  sw = new StringWriter();

		  t = new Tidy();
		  t.setXmlTags(true);
		  t.parse(br,sw);
		  sb = sw.getBuffer();
		  String strClean2 = sb.toString();
		  try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			sw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		  if ( removeThings)
			  strClean2 = removeThings(strClean2);
		  
		  return strClean2;
		
	}
	*/
	
	public static String removeThings ( String in_xhtml ) {
		String unescaped = in_xhtml;
	    int a = unescaped.indexOf("<html ");
	    if ( a >= 0 )
	    	unescaped = unescaped.substring(a);
	    
		int wo = unescaped.indexOf("<html xmlns=\"http://www.w3.org/1999/xhtml\"");
		if (wo > -1 && wo < 300 ) {
			unescaped = unescaped.substring(0, wo)
					+ "<html "
					+ unescaped.substring(wo+42);
			System.out.println("new: "+unescaped);
		}
		return unescaped;
	}
	/*
	public static String html2xhtml( String in_html ) throws Exception {
		
		InputStream fis1 = 
				new ByteArrayInputStream(
						(in_html).getBytes(StandardCharsets.UTF_8));
		

	        Tidy tidy = new Tidy();
	        tidy.setDropEmptyParas(true);
	        tidy.setShowWarnings(false); //to hide errors
	        tidy.setQuiet(true); //to hide warning
	        tidy.setUpperCaseAttrs(false);
	        tidy.setUpperCaseTags(false);
	        tidy.setIndentContent(true);
	        tidy.setInputEncoding("UTF-8");
	        

	        tidy.setWraplen(Integer.MAX_VALUE);
	        //tidy.setPrintBodyOnly(true);
	        tidy.setXmlOut(true);
	        tidy.setSmartIndent(true);
	        tidy.setXHTML(true);
	        StringWriter sw = new StringWriter();
	        
			tidy.parse(fis1, sw);
			
			StringBuffer sb = sw.getBuffer();
			String strClean = sb.toString();
			  
			fis1.close();
			sw.close();
			// done
			return strClean;
		
	}
	
	public static String xhtml2xml( String in_xhtml ) throws Exception {
		
		
		BufferedReader br = new BufferedReader(new StringReader(in_xhtml));
		StringWriter sw = new StringWriter();
        Tidy tidy = new Tidy();
        tidy.setXmlTags(true);
        tidy.parse(br, sw);
        StringBuffer sb = sw.getBuffer();
        String strClean = sb.toString();
        br.close();
        sw.close();
        return strClean;
		
	}	
	*/
	


}