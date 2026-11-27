// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk.experiments;

public class TestConvertJtidyXML {
 //https://stackoverflow.com/questions/10390922/how-do-i-make-jtidy-make-html-documents-well-formed/14837004#14837004
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
	        tidy.setDropEmptyParas(true);
	        tidy.setShowWarnings(false); //to hide errors
	        tidy.setQuiet(true); //to hide warning
	        tidy.setUpperCaseAttrs(false);
	        tidy.setUpperCaseTags(false);
	  
	        tidy.setInputEncoding("UTF-8");
	        
	        StringWriter sw = new StringWriter();
	        
			tidy.parse(fis1, sw);
			
			StringBuffer sb = sw.getBuffer();
			String strClean = sb.toString();
			  
			fis1.close();
			sw.close();
			// done
			BufferedReader br = new BufferedReader(new StringReader(strClean));
			sw = new StringWriter();
	        tidy = new Tidy();
	        tidy.setXmlTags(true);
	        tidy.parse(br, sw);
	        sb = sw.getBuffer();
	        String strClean2 = sb.toString();
	        br.close();
	        sw.close();
	        
	        int a = strClean2.indexOf("<html ");
	        if ( a >= 0 )
	        	strClean2 = strClean2.substring(a);
	        
	        if ( strClean2.startsWith("<html xmlns=\"http://www.w3.org/1999/xhtml\">"))
	        	strClean2 = "<html>"+strClean2.substring(44);
	        Commons.writeToEncodedFile(strClean2, out, "UTF-8");
	        */
	        		
	}

}