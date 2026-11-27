// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk.experiments;

public class TestConvertJtidy2 {
 //https://stackoverflow.com/questions/15063870/jtidy-java-api-toconvert-html-to-xhtml
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir = "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.WebWalker\\app.wwTest1\\pro.test\\";
		String in = dir+"in.html";
		String out = dir+"out.xml";
		String tmp = dir+"tmp.xhtml";
		
		/*
		InputStream fis1 = 
				new ByteArrayInputStream(
						(Commons.readTextfileFormatLf(in, "UTF-8", "\n")).getBytes(StandardCharsets.UTF_8));
		

	        Tidy tidy = new Tidy();
	        tidy.setShowWarnings(true);
	        tidy.setInputEncoding("UTF-8");
	        tidy.setOutputEncoding("UTF-8");
	        tidy.setXHTML(true);
	        tidy.setMakeClean(true);	        
	        
			FileOutputStream fos1 = new FileOutputStream(tmp);
			tidy.parse(fis1, fos1);
			fis1.close();
			fos1.close();
			// done
			
	        tidy = new Tidy();
	        tidy.setShowWarnings(true);
	        tidy.setXmlTags(true);
	        tidy.setInputEncoding("UTF-8");
	        tidy.setOutputEncoding("UTF-8");
	        tidy.setXHTML(true);
	        tidy.setMakeClean(true);	        
			InputStream fis2 = 
					new ByteArrayInputStream(
							(Commons.readTextfileFormatLf(tmp, "UTF-8", "\n")).getBytes(StandardCharsets.UTF_8));

			
			FileOutputStream fos2 = new FileOutputStream(out);
			tidy.parse(fis2, fos2);
			
			fis2.close();
			fos2.close();			
			*/
	        /*
	        tidy.setWraplen(Integer.MAX_VALUE);
	        tidy.setPrintBodyOnly(true);
	        tidy.setXmlOut(true);
	        tidy.setSmartIndent(true);
	        */

	        	
	        	
	        		
	}

}