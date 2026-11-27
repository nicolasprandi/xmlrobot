/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.Vcar;

public class Tester {
	
	public static boolean testOrTestdynAttributeIs0 ( Element element,ITiHandler tih ) {
		//Sp.p("Test.testAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));
		if ( testAttributeIs0Internal(element,tih) ||
			 testDynAttributeIs0Internal(element,tih))
			return true;
		else
			return false;

	}
	public static boolean testOrTestdynAttributeIs0 ( Element element,Interpreter interpreter ) {
		//Sp.p("Test.testAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));
		if ( testAttributeIs0Internal(element,interpreter) ||
			 testDynAttributeIs0Internal(element,interpreter))
			return true;
		else
			return false;

	}	
	public static boolean testDynAttributeIs0 ( Element element,ITiHandler tih ) {
		//System.out.println("Test.testAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));
		if ( 
			 testDynAttributeIs0Internal(element,tih))
			return true;
		else
			return false;

	}		
	
	
	
	public static boolean testAttributeIs0Internal ( Element element,ITiHandler tihxx ) {
		//Sp.p("Test.testAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));		
		if ( element.getAttribute("test") == null ) return false;
		Vcar x = new Vcar(element.getAttributeValue("test"));
		try {
			if ( x.isBool && !x.getBool())
				return true;
			else
				return false;
		} catch (Exception e) {
			Sp.p("Failed");
			return false;
		}

	}	
	
	public static boolean testAttributeIs0Internal ( Element element,Interpreter interpreter ) {
		//Sp.p("Test.testAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));		
		if ( element.getAttribute("test") == null ) return false;
		Vcar x = new Vcar(element.getAttributeValue("test"));
		try {
			if ( x.isBool && !x.getBool())
				return true;
			else
				return false;
		} catch (Exception e) {
			Sp.p("Failed");
			return false;
		}

	}	
	
	private static boolean testDynAttributeIs0Internal ( Element element,ITiHandler tih ) {
		if ( element.getAttribute("test_dyn") == null ) return false;
		String value = element.getAttributeValue("test_dyn");
		
		try {
			value = Entities.exportEntities(value);		
			value = tih.translateString(value);
			Vcar x = new Vcar(value);
			try {
				if ( x.isBool && !x.getBool())
					return true;
				else
					return false;
			} catch (Exception e) {
				// should never happen
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
	}	
	public static boolean testFirstOrTestfirstDynAttributeIs0 ( Element element,ITiHandler tih ) {
		// Sp.p("Test.testFirstAttributeIs0: "+element.getName() 
		// 		+ ", id="+element.getAttributeValue("id"));
		if ( testFirstAttributeIs0Internal(element,tih) ||
			 testFirstDynAttributeIs0Internal(element,tih))
			return true;
		else
			return false;

	}
	
	public static boolean testFirstAttributeIs0Internal ( Element element,ITiHandler tih ) {
		//Sp.p("Test.testFirstAttributeIs0: "+element.getName() 
		//		+ ", id="+element.getAttributeValue("id"));
		if ( element.getAttribute("test_first") == null ) return false;
		
		Vcar x = new Vcar(element.getAttributeValue("test_first"));
		try {
			if ( x.isBool && !x.getBool())
				return true;
			else
				return false;
		} catch (Exception e) {
			// should never happen
			return false;
		}

	}	
	
	private static boolean testFirstDynAttributeIs0Internal ( Element element,ITiHandler tih ) {
		if ( element.getAttribute("test_first_dyn") == null ) return false;
		
		String value = element.getAttributeValue("test_first_dyn");
		
		try {
			value = Entities.exportEntities(value);		
			value = tih.translateString(value);
			Vcar x = new Vcar(value);
			try {
				if ( x.isBool && !x.getBool())
					return true;
				else
					return false;
			} catch (Exception e) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
	}	
		
}