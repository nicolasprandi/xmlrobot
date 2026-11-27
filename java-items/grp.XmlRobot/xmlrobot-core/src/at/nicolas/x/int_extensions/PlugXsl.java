/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.tasks.Xslt;

public class PlugXsl implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new Exception("f xsl I wants 2 parameters: 1=file, 2=xsl-select-expression");
		
		Vcar   vfile= (Vcar)vcar_list.get(0);
		Vcar   vxsl= (Vcar)vcar_list.get(1);
			
		String xml = vfile.s;
		// xml escapen nic123
		String xsl = Commons.escapeXml(vxsl.s);
		
		
		if ( !CommonsVfs.existsFile(xml,rh.getSitzung())) 
			throw new Exception("f xsl'"+xml+"' is not a File !!!");
		
		String xsl_file_content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
				+"<xsl:stylesheet version=\"1.0\" " 
				+  "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"> "
				+"<xsl:output method=\"text\"/> "
				+"<xsl:template match=\"/\"> "
				+  "<xsl:value-of select=\""+xsl+"\"/> "
				+"</xsl:template> "	
				+"</xsl:stylesheet> ";

		
		String tempfile_xsl = rh.getTempDir() + "f_xsl.xsl";
		String tempfile_out = rh.getTempDir() + "f_xsl_out.txt";
		
		CommonsVfs.writeToFileVA(xsl_file_content, tempfile_xsl,rh.getSitzung());
		Element e = new Element("xslt");
		e.setAttribute("xml", xml);
		e.setAttribute("xsl", tempfile_xsl);
		e.setAttribute("out", tempfile_out);
		e.setAttribute("flag","fresh");
		Xslt xslt_task = new Xslt();
		
		//lc.log_ext("temporary-xsl-file is "+tempfile_xsl,this);
		try {
			xslt_task.execute(e, rh);
		} catch (Exception e1) {
			// lc.fatal("temporary-xsl-file:"+tempfile_xsl);
			//lc.addException("your xsl-request is: "+xsl);
			//lc.addException("look in the temporary-xsl-file which is: "+tempfile_xsl);
			throw e1;
		}
		
		String result = CommonsVfs.readTextfile(tempfile_out,rh.getSitzung());

		return (new Vcar(result));

	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}