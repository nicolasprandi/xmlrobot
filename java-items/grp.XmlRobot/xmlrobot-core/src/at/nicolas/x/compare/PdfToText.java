// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.compare;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.pub.HtmlEscape;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class PdfToText implements ITask {
	Element e;
	IRequestHandler rh;


	@Override
	public Parameters execute(
			Element e, 
			IRequestHandler rh) throws Exception {
		this.e = e;
		this.rh = rh;
	

		
		// Check parameter
		if ( e.getAttribute("in") == null)
			throw new Exception("pdftotext: needs attribute 'in' with a filename to read!");
		if ( e.getAttribute("out") == null)
			throw new Exception("pdftotext: needs attribute 'out' with a filename to write!");		
		
		// main
		InputStream is = MyCommons.getInputStreamByFilename(e.getAttributeValue("in"),this.rh);
	
		if ( is == null )
			throw new Exception("pdftotext: parameter 'in' is not a file: "+e.getAttributeValue("in"));
		
		PDFTextStripper textStripper = new PDFTextStripper();
		PDDocument doc = PDDocument.load(is);
		String text = textStripper.getText(doc);
		doc.close();
		String frm = "text";
		if ( e.getAttribute("format")!=null) 
			frm = e.getAttributeValue("format");
		if ( frm.equals("text")) {
			MyCommons.writeToUTF8File(text.trim(), e.getAttributeValue("out"),this.rh);
		} else if ( frm.equals("xml")) {
			Element x = new Element("x");
			x.setText(text.trim());
			Document d = new Document();
			d.setRootElement(x);
			MyCommons.writeToFile(d, e.getAttributeValue("out"),this.rh);
		} else if ( frm.equals("html")) {
			String html = HtmlEscape.escape2(text.trim());
			MyCommons.writeToUTF8File(html, e.getAttributeValue("out"),this.rh);
			/*
			JEditorPane jEditorPaneTi = null;
			jEditorPaneTi = new JEditorPane();
			jEditorPaneTi.setContentType("text/html");
			jEditorPaneTi.setEditable(false);
			JScrollPane jScrollPaneTi = new JScrollPane(jEditorPaneTi);
			jEditorPaneTi.setText("<html><body>"+html+"</body</html>");
		
			JFrame jFrame= new JFrame();	
			jFrame.setTitle("Html Test" );
			jFrame.addWindowListener(new WindowAdapter() {
			      public void windowClosing(WindowEvent e) {
			    	  try {
			    		  System.exit(0);
			    	  } catch (Exception e1) {
						e1.printStackTrace();
			    	  }
			      	}
				});		
			
			jFrame.setContentPane(jScrollPaneTi);
			jFrame.setSize(new Dimension(700,500));
			jFrame.setVisible(true);
			*/
		}else {
			throw new Exception("pdftotext: parameter 'format' is not valid: "+e.getAttributeValue("format")+"; should be 'text' or 'html'");
		}

		// 
		return null;
	}


	
	/**
	 * @param args
	 */

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		
		
	}

}