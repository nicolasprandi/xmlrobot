/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Exists implements IFun{
	
	String funn ="exists";
	String pm ="function 'exists' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		if ( vcar_list.size() < 2 ) 
			throw new LangException(310,pm+" wants at least 2 parameters: 1=type [...] 2=varid");
		
		String p1 = ((Vcar)vcar_list.get(0)).s;
		if ( p1.equals("prop") || p1.equals("dir") || p1.equals("file") ) {
			if ( vcar_list.size() != 2 ) 
				throw new LangException(310,pm+" wants 2 parameters: 1=type 2=varid");
			
			boolean result = false;
			String p2 = ((Vcar)vcar_list.get(1)).s;
			result = exists(p1,p2,rh);
			
			Vcar r = new Vcar(result);
			r.dontImport();
			return r;
		} else if ( p1.equals("coop2")) {
			if ( vcar_list.size() < 3 ) 
				throw new LangException(310,pm+" wants 3 parameters: 1=coop2 2=internal-type 3=id");
			
			String theApp = null;
				if ( vcar_list.size() == 4 ) {
					theApp =((Vcar)vcar_list.get(3)).s;
					if (theApp != null &&
						theApp.trim().length()==0 ) {
						theApp = null;
					}
				}
			boolean result;
			if ( theApp == null )
				result=rh.doesExistCoop(
					((Vcar)vcar_list.get(1)).s,
					((Vcar)vcar_list.get(2)).s);
			else
				result=rh.doesExistCoopApped(
					((Vcar)vcar_list.get(1)).s,
					((Vcar)vcar_list.get(2)).s,
					theApp);			
			Vcar r = new Vcar(result);
			r.dontImport();
			return r;
		} else {
			throw new LangException(311,pm+" found an uncorrect variable-type '"+p1+"'");
		}

	}
	
	public static boolean exists ( String type, String id, IRequestHandler rh ) throws Exception {
	
		return rh.doesExistVariable(type, id);

	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}
	
	public static boolean existsDepressive ( String x ) {
		boolean result = true;
		if ( ( x.startsWith("(prop,") || 
				   x.startsWith("(dir,") || 
				   x.startsWith("(file,") || 
				   x.startsWith("(loop,") ) &&
				   x.endsWith(")") )
				result = false;
			else
				result = true;
		return result;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}	

}