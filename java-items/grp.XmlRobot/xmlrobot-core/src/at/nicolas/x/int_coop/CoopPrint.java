/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.io.OutputStream;
import java.io.PrintStream;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.Parameters;


public class CoopPrint  implements ITask, ICoop {
	

	//Element e = null;
	
	String tname;
	String outputfilename = null;		
	IRequestHandler rh;	
	
	PrintStream psPrinter;
	OutputStream psOutputStream;
	OutputStreamRequest osr ;

	private static final int act_open   = 0;
	private static final int act_print   = 1;
	private static final int act_println   = 2;
	private static final int act_close   = 3;

	private static KeyWordMapper tasknames;
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.rh = rh;
		if ( e.getAttribute("act") == null ) {
			throw new Exception(tname+" needs attribute @act (open,println,print,close)");
		}
		
		if ( e.getAttribute("id") == null ) {
			throw new Exception(tname+" needs attribute @id");
		}		
		
		//String action = e.getAttributeValue("act");
		String id = e.getAttributeValue("id");
		
		Parameters retParas = null;
		int ac = tasknames.convert(e.getAttributeValue("act"));
		switch ( ac ) {
		case act_open: {	
			boolean autoflush = Commons.hasFlag(e, "autoflush");
			String encoding = e.getAttributeValue("encoding");
			if ( Commons.isEmpty(encoding))
				encoding ="UTF-8";
			//_lc.log_ext("Encoding will be: ["+encoding.trim()+"]", this);
			psPrinter = null;
			outputfilename = e.getAttributeValue("out");
			
			if ( Commons.hasFlag(e, "append")) {
				osr = CommonsVfs.getOutputStreamByFilenameP1(outputfilename, true,rh.getSitzung());
			} else {
				osr = CommonsVfs.getOutputStreamByFilenameP1(outputfilename, false,rh.getSitzung());
			}	
			psOutputStream = osr.getOutputStream();
			
			
			
			psPrinter = new PrintStream(psOutputStream,autoflush,encoding);
			
			rh.putNo(id, Wellknown.coop_type_print, this);

			break;
		}


		default: { 	
			CoopPrint print = (CoopPrint)rh.getNo(id, Wellknown.coop_type_print);
			if ( print == null ) 
				throw new Exception("The "+tname+" with id '"+id+"' was not found");
			
			
			String toprint = null;
			if ( ac == act_print || ac == act_println ) {
				toprint = Commons.getXValue(e);
				if ( toprint == null ) toprint="";
				if ( Commons.hasFlag(e, "dont_unescape_on_write"))
					toprint = Entities.importEntities(toprint);				
			}			
			
		    switch ( ac ) {
		    case act_print: {	
		    	print.psPrinter.print(toprint);
		    	break;
		    }
		    case act_println: {	
		    	print.psPrinter.println(toprint);
		    	break;
		    }
		    case act_close: {	
		    	rh.removeNo(id, Wellknown.coop_type_print);
				 print.psOutputStream.close();
				 print.psPrinter.close();
				 //System.out.println("-----------------------outputfilename-"+outputfilename);
				 CommonsVfs.finishOutputStreamP2(print.outputfilename, rh.getSitzung(),print.osr.getOpnum());
				
		    	break;
		    }		    
		    } 
		  	break;
		}
		}  		
		 
		return retParas;
	}
	
	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void autoClose() throws Exception {
		// TODO Auto-generated method stub
		 psOutputStream.close();
		 psPrinter.close();
		 CommonsVfs.finishOutputStreamP2(outputfilename, rh.getSitzung(),osr.getOpnum());
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("CoopPrint-Functions",4);
		tasknames.put("open", act_open);
		tasknames.put("print", act_print);
		tasknames.put("println", act_println);
		tasknames.put("close", act_close);		
	}

}