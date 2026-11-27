/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;



public class UniqueCoop  implements ITask, ICoop {

	public static String type="_unq";
	public Unique unique;	
	IRequestHandler rh;
	

	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
				
		if ( e.getAttribute("act") == null ) {
			throw new Exception("unique needs attribute @act (open,close)");
		}
		
		if ( e.getAttribute("id") == null ) {
			throw new Exception("unique needs attribute @id");
		}		
		
		String action = e.getAttributeValue("act");
		String id = e.getAttributeValue("id");
		
		Parameters retParas = null;

		if ( action.equals("open")) {
			// starting the loop
			unique = new Unique(e.getAttributeValue("style"));
			 rh.putNo(id, type,  this);
			//lc.log_ext("Opened "+id, this);
			
		} else {
			UniqueCoop uniqueCoop = (UniqueCoop)rh.getNo(id, type);
			if ( uniqueCoop == null ) 
				throw new Exception("The unique with id '"+id+"' was not found");
			
			if ( action.equals("something-which-doesn exist")){
				;
			} else if ( action.equals("close")){
				
				retParas = new Parameters();
				// retParas.setParameter(id+"_remain", loop_regex.loopRegex.getRest());
				
				rh.removeNo(id, type);
				// Sp.p("REMOVED unique "+id);
				//lc.log_ext("Closed "+id, this);
			} else
				throw new Exception("unique attribute @act must be start orend");
			
		}
		 
		return retParas;
	}
	


	@Override
	public void autoClose() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}