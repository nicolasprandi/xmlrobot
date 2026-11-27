// Copyright � 2016 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;



public class WebElementFuncs implements IFun {
	private static String fun_id ="welement";	
	private static String fun_logo ="task 'welement'";
	// WeFuncs
	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {

	}

	@Override
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		/*[requo-timer]*/
		if ( vcar_list.size() < 1 ) 
			throw new LangException(fun_id+": I want at least one parameter: the we-id");

		
		String id = ((Vcar)vcar_list.get(0)).s;
		// System.out.println( "Cyclo-Classloader FUN:" + Cyclo.class.getClassLoader());
		NicsWe nicsWe = (NicsWe)rh.getNo(id, NicsWe.coop_type, this);
		String request = ((Vcar)vcar_list.get(1)).s;
		
		if ( request.equals("exists")) {
			if ( nicsWe == null )
				return new Vcar("0");
			else
				return new Vcar("1");
		} else if ( nicsWe == null )
			throw new Exception("Didnt find nicsWe with id "+id);
		
		
		String result;
		if ( request.equals("attribute")) {
			result = nicsWe.getAttribute(((Vcar)vcar_list.get(2)).s);
		} else if ( request.equals("css")) {
			result = nicsWe.getCssValue(((Vcar)vcar_list.get(2)).s);
		} else if ( request.equals("tag_name")) {
			result = nicsWe.getTagName();
		}  else if ( request.equals("text")) {
			result = nicsWe.getText();
		} else if ( request.equals("is_enabled")) {
			result = Commons.boolToString(nicsWe.isEnabled());
		} else if ( request.equals("is_displayed")) {
			result = Commons.boolToString(nicsWe.isDisplayed());
		} else if ( request.equals("is_selected")) {
			result = Commons.boolToString(nicsWe.isSelected());
		}  else if ( request.equals("location_x")) {
			result = Commons.int2String(nicsWe.getLocationX());
		}  else if ( request.equals("location_y")) {
			result = Commons.int2String(nicsWe.getLocationY());
		}  else if ( request.equals("size_x")) {
			result = Commons.int2String(nicsWe.getSizeX());
		}  else if ( request.equals("size_y")) {
			result = Commons.int2String(nicsWe.getSizeY());
		} else {
			throw new LangException(fun_logo+" doesn't know this request: "+request);
		}
		
		return new Vcar(result);
	}

}