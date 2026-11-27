package at.nicolas.webwalk;

import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;

public class ConvJSoup implements ITask{

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String home="C:\\Users\\nicolas\\Desktop\\Requ\\xmlrobot-internals\\workspaces\\ws.WebWalk\\app.Sugarbabe2\\pro.0325_[Lnn10]\\results\\aussenministerium\\";

		// convertHtmlfile2Xml(home+"Albanien-BMEIA.html",home+"Albanien-BMEIA.xml");
		
		

	}
	
	public static void convertHtmlfile2Xml(String inputFile,
            String outputFile, IRequestHandler rh) throws Exception {
		String html = CommonsVfs.readTextfile(inputFile,rh.getSitzung());
		convertHtmlstring2Xml (html,outputFile,rh);
	}
	
	public static org.jdom2.Element convertHtmlstring2Element(String inputHtmlString) throws Exception {

		Document doc = Jsoup.parse(inputHtmlString);
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);  
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		// doc.outputSettings().charset("ISO-8859-1");
		org.jdom2.Element root = new org.jdom2.Element(doc.children().get(0).tagName());
		appendElements(root, doc.children().get(0).children());
		
		return root;
  


    }		
	
	public static void convertHtmlstring2Xml(String inputHtmlString,
            String outputFile, IRequestHandler rh) throws Exception {

		org.jdom2.Element jElement = convertHtmlstring2Element( inputHtmlString);
		
		org.jdom2.Document odoc = new org.jdom2.Document(jElement);
		CommonsVfs.writeToFilePretty(odoc, outputFile,rh.getSitzung());
		
  


    }	
	private static void appendElements( org.jdom2.Element root, org.jsoup.select.Elements  adds) {
		
		for (org.jsoup.nodes.Element e : adds) {
			org.jdom2.Element neu = new org.jdom2.Element(e.tagName());
			neu.setText(e.ownText());
			Attributes as = e.attributes();
			for ( Attribute a : as ) {
				String attrname = Commons.stringReplace(a.getKey(), ":", "_");
				if ( attrname.equals("xmlns"))
					attrname ="_xmlns";
				
				
				neu.setAttribute(attrname, a.getValue());
			}
			root.addContent(neu);
			appendElements(neu,e.children());
		}		
	}






	@Override
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		if ( e.getAttribute("html")==null)
			throw new LangException(515,"jsoup-html-conversion wants an attribute 'html'");
		if ( e.getAttribute("to_file")==null)
			throw new LangException(516,"jsoup-html-conversion  wants an attribute 'to_file'");
		
		convertHtmlfile2Xml(e.getAttributeValue("html"),e.getAttributeValue("to_file"),rh);
		return null;
	}

	@Override
	public void staticTaskdef(Element arg0, String arg1, IRequestHandler arg2) {
		// TODO Auto-generated method stub
		
	}




}
