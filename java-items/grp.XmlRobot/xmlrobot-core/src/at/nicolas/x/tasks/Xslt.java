/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Xslt   implements ITask, ErrorListener, URIResolver{

	String xsl;
	String xml;
	String out;
	Parameters paras;
	IRequestHandler rh;
	/*[timerx]*/
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.rh = rh;
		
		if ( e.getAttribute("xsl")==null)
			throw new LangException(515,"xslt wants an attribute 'xsl'");
		if ( e.getAttribute("xml")==null)
			throw new LangException(516,"xslt wants an attribute 'xml'");
		if ( e.getAttribute("out")==null)
			throw new LangException(517,"xslt wants an attribute 'out'");
		
		if ( e.getAttribute("xsl_spirit")!= null ||
			 e.getAttribute("xml_spirit")!= null ||
			 e.getAttribute("out_spirit")!= null )
			throw new Exception("spirits for xslt not implemented yet.");
		
		xsl = e.getAttributeValue("xsl");
		xml = e.getAttributeValue("xml");
		out = e.getAttributeValue("out");
		if ( e.getChildText("parameters") !=null ) {
			JOptionPane.showMessageDialog(null, "xslt.parameters depreciated, use paras!!!/n"+
					Commons.toText(e));			
		}		

		paras = new Parameters(e.getChildText("params"),"'");
		
		//Sp.p("xsl  = "+xsl);
		//Sp.p("xml  = "+xml);
		//Sp.p("out  = "+out);		
		//Sp.p("paras= "+paras.getStringForLineprint());
		
		if ( !CommonsVfs.existsFileForProcess(xsl,rh.getSitzung()))
			throw new Exception("xslt: xsl file not found ("+xsl+")");
		else if ( !CommonsVfs.existsFile(xml,rh.getSitzung()))
			throw new Exception("xslt: xml file not found ("+xml+")");


		//
		FileObject fo_xsl = CoreBuilder.fsm.resolveFile(xsl);
		
		if ( CommonsVfs.getTimeOfFileObjectForProcessing(fo_xsl,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}		
		FileContent fc_xsl = fo_xsl.getContent();
		InputStream is_xsl = fc_xsl.getInputStream();
		StreamSource sc_xsl = new StreamSource(is_xsl);
		
		FileObject fo_xml = CoreBuilder.fsm.resolveFile(xml);
		CommonsVfs.grantsFileRead(fo_xml, rh.getSitzung());
		rh.getSitzung().checkMaxFileMemory(fo_xml);
		if ( CommonsVfs.getTimeOfFileObject(fo_xml,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}	
		FileContent fc_xml = fo_xml.getContent();
		InputStream is_xml = fc_xml.getInputStream();
		StreamSource sc_xml = new StreamSource(is_xml);
		
		FileObject fo_out = CommonsVfs.resolveAnySilent(out);
		long opnum = CommonsVfs.grantsUpdateFileP1(fo_out,rh.getSitzung(),"xslt");
		
		FileContent fc_out = fo_out.getContent();			
		OutputStream os_out = fc_out.getOutputStream();

	
		//
				
		/*
		StreamSource sc_xsl = null;
		FileInputStream fis_xsl =null;

		fis_xsl = new FileInputStream(new File(xsl));
		sc_xsl = new StreamSource(fis_xsl);	
		*/

		
        TransformerFactory tfactory = TransformerFactory.newInstance();
        // tfactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        tfactory.setErrorListener(this);

        MyUriResolver myUriResolver = null;
        if ( e.getAttribute("home") != null ) {
        	//Sp.p("found HOME "+e.getAttributeValue("home"));
        	myUriResolver = new MyUriResolver(e.getAttributeValue("home"),e);
        	tfactory.setURIResolver(myUriResolver);
        }        
        Transformer transformer;
		try {
			transformer = tfactory.newTransformer(sc_xsl);
			//Sp.p("BBB");
		} catch (Exception e1) {
			//Sp.p("AAAA");
			is_xsl.close();	
			is_xml.close();
			os_out.close();			
			throw e1;
		} finally {
			CommonsVfs.grantsUpdateFileP2(fo_out,rh.getSitzung(),opnum);
		}
        transformer.setErrorListener(this);
        
        if ( myUriResolver != null )
        	transformer.setURIResolver(myUriResolver);		
 
        Set<String> keySet = paras.getParameterNames();		
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;
		while ( i.hasNext() ) {
			aktKey = (String)i.next();		
			transformer.setParameter( aktKey, paras.getParameter(aktKey) );
			//Sp.p("param: "+aktKey+" = "+paras.getParameter(aktKey));	
		}        

		// Sp.p("Going into xslt ... ");
		
		
		
        try {
        	
			transformer.transform(sc_xml, new StreamResult(os_out));
		} catch (Exception e1) {
			is_xsl.close();	
			is_xml.close();
			os_out.close();
			throw e1;
		}
        
        is_xsl.close();
        
		is_xml.close();
		os_out.close();
		
		
		return null;
	}
	




	@Override
	public void error(TransformerException arg0) throws TransformerException {
		// lc.info(this, "error");
		// lc.addException(arg0.getMessageAndLocation());
		Sp.p("xslt.TransformerException Message="+arg0.getLocalizedMessage());
		throw arg0;
		
	}



	@Override
	public void fatalError(TransformerException arg0)
			throws TransformerException {
		//lc.info("fatalError",this);
		// lc.addException("fatalError "+arg0.getMessageAndLocation());
		Sp.p("xxx="+arg0.getLocalizedMessage());

		throw arg0;
	}



	@Override
	public void warning(TransformerException arg0) throws TransformerException {
		//lc.info("warning",this);
		// lc.addException("warning "+arg0.getMessageAndLocation());
		throw arg0;
		
	}



	@Override
	public Source resolve(String href, String base) throws TransformerException {
		// TODO Auto-generated method stub
		Sp.p("Calld UriResolver with href="+href+", base="+base);
		return null;
	}
	
	private class MyUriResolver  implements URIResolver{
		String home_dir;

		//Xslt mama;
		private HashMap<String, String> homeMap = null;
		MyUriResolver ( String home_dir, Element xsltStep ) {
			this.home_dir = home_dir;
	
			//this.mama = mama;
			homeMap = new HashMap<String, String>();
			List<Element> homes = xsltStep.getChildren("home");
			if ( homes.size() > 0 ) {
				Iterator<Element> iHomes = homes.iterator();
				while ( iHomes.hasNext()) {
					Element cHome = (Element)iHomes.next();
					// System.out.println("map+ "+cHome.getAttributeValue("file") +" - "+cHome.getText());
					homeMap.put(cHome.getAttributeValue("file"),cHome.getText());
				}
			}
			//lc.log_ext("Set homedir to: "+home_dir, mama);
		}
		@Override
		public Source resolve(String href, String base)
				throws TransformerException {
			// TODO Auto-generated method stub
			// lc.info("xslt resolved href=["+href+"] base=["+base+"] to ["+home_dir+href+"]", mama);
			 // File f = new File(home_dir+href);
			 InputStream is = null;
			 StreamSource ss = null;
			 try {
				// zuerst in der map suchen
				String homeExMap = homeMap.get(href);
				if ( homeExMap != null ) {
					is = CommonsVfs.getInputStreamByFilenameForProcessing(homeExMap+href,rh.getSitzung());
				} else {
					is = CommonsVfs.getInputStreamByFilenameForProcessing(home_dir+href,rh.getSitzung());					
				}
				ss = new StreamSource(is);
				//System.out.println("RESOLVED "+home_dir+href);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println("RESOLVING PROBLEM "+home_dir+href+" "+e.getLocalizedMessage());
				Sp.pe(e);
				ss = null;
			}
			 
			return ss;
		}
		
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}