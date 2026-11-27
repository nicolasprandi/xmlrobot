// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.util.List;

import org.jdom2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
//import org.w3c.tidy.Tidy;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;

public class Tools {
	
	private static int weCounter = 0;
	
	public static String getNextCounter () {
		weCounter++;
		return Integer.toString(weCounter);
	}
	
	private static int dynSpiritCounter = 0;
	
	public static String getDynSpiritCounter () {
		dynSpiritCounter++;
		return "DynSpiritWebWalt-"+ Integer.toString(dynSpiritCounter);
	}
	
		


		
	/*
	public static String convertStringXhtml2XmlXXX(String xhtml_content) {
		
		InputStream  fis1 = 
				new ByteArrayInputStream(xhtml_content.getBytes(StandardCharsets.UTF_8));

	    Tidy tidy = new Tidy();
	    tidy.setXmlOut(true);    
	    tidy.setInputEncoding("UTF-8");
	    
	    StringWriter sw = new StringWriter();
		System.out.println("** - ** 2b- before round 1");        
		tidy.parse(fis1, sw);			
		System.out.println("** - ** 3b- parse round 1 done");		
		StringBuffer sb = sw.getBuffer();
		String strClean = sb.toString();	
		

		try {
			fis1.close();
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
		return strClean;

	}
	*/
	/*
public static String convertStringHtml2Xhtml(String html_content) {
	
	InputStream  fis1 = 
			new ByteArrayInputStream(html_content.getBytes(StandardCharsets.UTF_8));
	System.out.println("** - ** 1-start xml");	
    Tidy tidy = new Tidy();
    tidy.setXHTML(true);
    tidy.setInputEncoding("UTF-8");
    
    StringWriter sw = new StringWriter();
	System.out.println("** - ** 2- before round 1");        
	tidy.parse(fis1, sw);			
	System.out.println("** - ** 3- parse round 1 done");		
	StringBuffer sb = sw.getBuffer();
	String strClean = sb.toString();			  
	try {
		fis1.close();
	} catch (IOException e) {

		e.printStackTrace();
	}
	try {
		sw.close();
	} catch (IOException e) {

		e.printStackTrace();
	}		
	return strClean;

}
*/

public static void saveHtml (
		String html,
		Element e,
		IRequestHandler rh) throws Exception {

	String encoding = "UTF-8";
	if ( e.getAttribute("encoding") != null )
		encoding = e.getAttributeValue("encoding");
	
	String to_file = null;
	if ( e.getAttribute("to_file") != null )
		to_file = e.getAttributeValue("to_file");	
	else
		throw new Exception("webdriver: save needs to_file");
			
	
	System.out.println("encoding:"+encoding+" to_file:"+to_file);
	CommonsVfs.writeToEncodedFile(
			html, 
			e.getAttributeValue("to_file"), 
			encoding,
			rh.getSitzung());	        
	
}
	
	public static Element preRegisterWebElements (
			NicsWd nicsWd,
			List <WebElement> list,
			IRequestHandler rh, 
			ILogConnector lc, 
			boolean keep) throws Exception {

		Element root;
		root = new Element("sel");
		

		for (WebElement we : list) {

				String nextCounter = Tools.getNextCounter();
				if (keep)
					nextCounter=nextCounter.concat("k__");
				
				if ( nicsWd.register.containsKey(nextCounter) ) {
					throw new Exception("an element with the id "+nextCounter+" is already registered.");
				} else {
					nicsWd.register.put(nextCounter, we);
				}
				
				Element wex;
				wex = new Element("we");
				wex.setAttribute("id",nextCounter);
				if ( we.getTagName() != null )
					wex.setAttribute("tag_name",we.getTagName());
				
				if ( we.getText() != null )
					wex.setAttribute("text",we.getText());
	
				wex.setAttribute("is_displayed",Commons.boolToString(we.isDisplayed()));
				wex.setAttribute("is_enabled",Commons.boolToString(we.isEnabled()));
				wex.setAttribute("is_selected",Commons.boolToString(we.isSelected()));
				root.addContent(wex);				
		
			
		}
			
		return root;
		
	}	
	
	public static Element registerWebElements (
			List <WebElement> list,
			IRequestHandler rh, 
			ILogConnector lc,
			boolean keep) throws Exception {
		// System.out.println("** begin Tools.registerWebElements");
		Element root;
		root = new Element("sel");
		
		for (WebElement we : list) {
			
			String nextCounter = putWebElement(we,rh,keep); 
			Element wex;
			wex = new Element("we");

			wex.setAttribute("id",nextCounter);
			if ( we.getTagName() != null )
				wex.setAttribute("tag_name",we.getTagName());
			
			if ( we.getText() != null )
				wex.setAttribute("text",we.getText());

			wex.setAttribute("is_displayed",Commons.boolToString(we.isDisplayed()));
			wex.setAttribute("is_enabled",Commons.boolToString(we.isEnabled()));
			wex.setAttribute("is_selected",Commons.boolToString(we.isSelected()));
			root.addContent(wex);			
			
		}

		return root;
		
	}
	
	public static String registerWebElement ( 
			WebElement we,
			IRequestHandler rh, 
			ILogConnector lc,
			boolean keep ) throws Exception {
		//System.out.println("** begin Tools.registerWebElement");
		
		String nextCounter = putWebElement(we,rh,keep);
		
		//System.out.println("** end Tools.registerWebElement");
		return nextCounter;
		
	}
	
	public static String putWebElement(WebElement we, IRequestHandler rh, boolean keep ) throws Exception {
		NicsWe nicsWe = new NicsWe(we);
		String nextCounter = Tools.getNextCounter();
		if (keep)
			nextCounter=nextCounter.concat("k__");
		
		rh.putNo(	nextCounter, 
				NicsWe.coop_type, 
				"WebElement", 
				nicsWe, 
				null);
		
		return nextCounter;		
	}
		
	public static By xmlBy ( Element e ) throws Exception {
		By ret = null;
		if ( e.getAttribute("by_class_name") != null )
			ret = new By.ByClassName(e.getAttributeValue("by_class_name"));
		else if ( e.getAttribute("by_css") != null )
			ret = new By.ByCssSelector(e.getAttributeValue("by_css"));		
		else if ( e.getAttribute("by_id") != null )
			ret = new By.ById(e.getAttributeValue("by_id"));
		else if ( e.getAttribute("by_link_text") != null )
			ret = new By.ByLinkText(e.getAttributeValue("by_link_text"));		
		else if ( e.getAttribute("by_name") != null )
			ret = new By.ByName(e.getAttributeValue("by_name"));	
		else if ( e.getAttribute("by_partial_link_text") != null )
			ret = new By.ByPartialLinkText(e.getAttributeValue("by_partial_link_text"));			
		else if ( e.getAttribute("by_tag_name") != null )
			ret = new By.ByTagName(e.getAttributeValue("by_tag_name"));	
		else if ( e.getAttribute("by_xpath") != null ) {
			// System.out.println("Applying by xpath: "+e.getAttributeValue("by_xpath"));
			ret = new By.ByXPath(e.getAttributeValue("by_xpath"));	
		} else
			throw new Exception("Unbekannter by-type");
		
		// System.out.println("** byXsl +ret");
			
		return ret;
			
	}

}