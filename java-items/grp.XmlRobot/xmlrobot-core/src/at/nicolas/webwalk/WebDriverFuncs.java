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

public class WebDriverFuncs implements IFun {
	// WdFuncs
	private static String fun_id ="wdriver";
	private static String fun_logo ="function 'wdriver'";
	
	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {

	}

	@Override
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		/*[requo-timer]*/
		if ( vcar_list.size() < 1 ) 
			throw new LangException(fun_logo+": I want at least one parameter: the wd-id");

		
		String id = ((Vcar)vcar_list.get(0)).s;
		if ( id.equals("?") )
			id = WebDriverTasks.wd_default_id;
		// System.out.println( "Cyclo-Classloader FUN:" + Cyclo.class.getClassLoader());
		NicsWd nicsWd = (NicsWd)rh.getNo(id, NicsWd.coop_type, this);

		if ( nicsWd == null )
			throw new Exception(fun_logo+" Didnt find nicsWd with id "+id);
		
		String request = ((Vcar)vcar_list.get(1)).s;
		String result;
		if ( request.equals("get_url")) {
			result = nicsWd.getCurrentUrl();
		} /* else if ( request.equals("page_source")) {
			result = nicsWd.getPageSource();
		}*/ else if ( request.equals("get_title")) {
			result = nicsWd.getTitle();
		} else if ( request.equals("get_tab_id")) {
			result = nicsWd.getWindowHandle();
		}  else if ( request.equals("get_tab_count")) {
			ArrayList<String> newTab = new ArrayList<String>(nicsWd.driver.getWindowHandles());
			int count = newTab.size();
			result = Commons.int2String(count);
		}else
			throw new Exception(fun_logo+" doesnt know this request "+request);
		
		return new Vcar(result);
	}



}