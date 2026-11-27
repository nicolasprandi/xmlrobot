/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.StorageLimitException;


public class Javascript implements ITask{

	String 	javascript;
	String	export;
	String 	export_prefix;
	IRequestHandler rh;
	OutputStreamRequest osr;


	
	//Element withE;	ScriptEngineManager factory = new ScriptEngineManager();
	

	
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		this.rh = rh;
		// lc.log_ext(this, "hello javascript");
				
		javascript = 	Commons.para2string(e.getAttributeValue("javascript"),null,"javascript");
		export = Commons.para2string(e.getAttributeValue("export"),null,"export");
		export_prefix = Commons.para2string(e.getAttributeValue("export_prefix"),null,"export_prefix");		
		if ( javascript == null )
			throw new Exception("javascript : Please set the attribute 'javascript'");
		
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		List<Element> printerList = e.getChildren("printer");
		ArrayList<JavascriptPrinter> psList = new ArrayList<JavascriptPrinter>();
		Iterator<Element> iterator = printerList.iterator();
		while ( iterator.hasNext() ) {
			Element ePrinter = (Element)iterator.next();
			if ( ePrinter.getAttribute("file") == null )
				throw new LangException(505,"task 'javascript' printer needs attribute file");
			String fPrinter = ePrinter.getAttributeValue("file");
			boolean autoflush = Commons.hasFlag(ePrinter, "autoflush");
			String encoding = ePrinter.getAttributeValue("encoding");
			if ( encoding == null )
				encoding ="UTF-8";
			//lc.log_ext("Encoding will be:"+encoding, this);
			PrintStream psPrinter = null;
			OutputStream fos = null;
			if ( Commons.hasFlag(ePrinter, "append")) {
				osr = CommonsVfs.getOutputStreamByFilenameP1(fPrinter,true,rh.getSitzung());
				
			} else {
				osr = CommonsVfs.getOutputStreamByFilenameP1(fPrinter,false,rh.getSitzung());
				
				// psPrinter = new PrintStream(fPrinter);
			}
			fos = osr.getOutputStream();
			psPrinter = new PrintStream(fos,autoflush,encoding);
			
			String name = "printer";
			if ( ePrinter.getAttributeValue("id") != null)
				name = "printer"+ePrinter.getAttributeValue("id");
			engine.put(name, psPrinter);
			psList.add(new JavascriptPrinter(psPrinter,fos,fPrinter,osr));
			
		}
		
		InputStream jsIs = CommonsVfs.getInputStreamByFilename(javascript,rh.getSitzung());
		try {
			engine.eval( new InputStreamReader(jsIs));
			jsIs.close();
		} catch (Exception e1) {
			try {
				jsIs.close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Sp.pe(e2);
			}
			InputStream jsIsE = CommonsVfs.getInputStreamByFilenameForProcessing(javascript,rh.getSitzung());
			BufferedReader br = new BufferedReader(new InputStreamReader( jsIsE ));
			String ausw;
			int i = 0;
			String msg = e1.getLocalizedMessage();
			int idx1 = msg.indexOf("ReferenceError");
			if ( idx1 != -1 )
				msg = msg.substring(idx1+16);

			
			//lc.warn("**********************************************************************************************************************",this);
			//lc.warn("* "+msg,this);
			//lc.warn("**********************************************************************************************************************",this);
			// int idx2 = msg.indexOf("at line number");
			/*
			int linenumber = -1;
			if ( idx2 != -1) {
				String linensdfumber = "hase";
			}*/
			while ((ausw = br.readLine()) != null ) {
				i++;
				//lc.warn("* "+Commons.int2Prefixedstring(i, 3)+" " + ausw,this);
			}
			//lc.warn("**********************************************************************************************************************",this);
			
			try {
				br.close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Sp.pe(e2);
			}		
			try {
				jsIsE.close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Sp.pe(e2);
			}
			closeAllPrinters(psList);
			
			throw e1;
		}
		
		closeAllPrinters(psList);

		
		Parameters p = new Parameters();
		if ( export != null ) {
			String[] exports = export.split("\\s+");
			for ( String ex : exports ) {
				String v = null;
				Object o = engine.get(ex);
				if ( o == null ) {
					//lc.warn("the javascript var '"+ex+"' does not exist.",this);
				} else {
					if ( o.equals("") ) {
						//lc.log_ext("empty export var found. setting to empty.",this);
						v="";
					} else {
						String n = engine.get(ex).getClass().getName();
						
						if (n.equals("java.lang.Double")) {
							v = Double.toString((Double)o);		
							if ( v.endsWith(".0"))
								v = v.substring(0, v.length()-2);
						} else if (n.equals("java.lang.Boolean")) {
							Boolean bul = (java.lang.Boolean)o;
							if (bul.booleanValue() == true )
								v = "1";
							else
								v="0";
											
						}  else if (n.equals("java.lang.Integer")) {
							
							Integer teger1 = (java.lang.Integer)o;
							v=teger1.toString();
											
						} else if (n.equals("sun.org.mozilla.javascript.internal.Undefined")) {
							v=null;						
						} else {
							//lc.log_ext("exporting "+ex +", class.getname="+n,this);
							v = (String)o;
						}						
					}
					
					if ( v== null )
						;//lc.warn("!!!!!!!!!!!!! exporting "+ex+" is not possible !!!",this);
					else {
						if ( export_prefix != null )
							ex = export_prefix.concat(ex);
						p.setParameter(ex, v);		
						//lc.log_ext("Export set prop "+ex+"= ["+v+"]",this);
					}
				}

			}
		}

		//lc.log_ext("return values: "+p.getStringForLineprint2(),this); 		
		
		return p;
	}
	private void closeAllPrinters(ArrayList<JavascriptPrinter> psList) throws FileSystemException, StorageLimitException {
		Iterator<JavascriptPrinter> p2 = psList.iterator();
		while ( p2.hasNext() ) {
			JavascriptPrinter px = (JavascriptPrinter)p2.next();
			CommonsVfs.finishOutputStreamP2(px.getOutputFile(), rh.getSitzung(),px.getOutputStreamRequest().getOpnum());
			
			
			try {
				px.getPrintStream().close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Sp.pe(e2);
			}	
			try {
				px.getOs().close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				Sp.pe(e2);
			}
		}		
	}
	


	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}