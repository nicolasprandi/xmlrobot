/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.HashMap;

import at.nicolas.x.int_interpreter.Sp;

public class Unique {

	final int STYLE_NONE = 0;
	final int STYLE_ATTRIBUTE = 1;
	private int style = 0;
	int runner = 0;
	private HashMap<String, String> register = new HashMap<String, String>();
	public Unique (String style) throws Exception {

		if ( style == null || style.length()==0)
			this.style = STYLE_NONE;
		else if ( style.equals("xml_attribute"))
			this.style = STYLE_ATTRIBUTE;
		else
			throw new Exception("Unique style '"+style+"' is not known. use 'xml_attribute' or keep it empty");		
	}
	
	public String getValue( String in_orig ) {
		
		String in = in_orig;
		if ( in_orig == null)
			in = "";
		if ( style == STYLE_ATTRIBUTE ) {
			in = in.trim();
			in = in.replaceAll("[^A-Za-z0-9]", "_");
			if (!in.matches("^[A-Za-z_].*$")) { 
				in = '_' +in;
			}
		}
		
		
		if ( register.get(in) != null ) {
			// Sp.p("Unique.getValue in="+in+" -- EXISTIERT BEREITS");
			runner++;
			String toadd = "_"+runner;
			while ( register.get(in+toadd) != null) {
				// we do this until in is unique
				runner++;
				toadd = "_"+runner;
			}	
			in = in+toadd;
		}
		// Sp.p("Unique.getValue adding to map:="+in+"");
		register.put(in, "");
		
		return in;

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Unique u = new Unique("xml_attribute");
			Sp.p(u.getValue(" (9) "));
			Sp.p(u.getValue("(9)"));
			Sp.p(u.getValue("__3"));
			Sp.p(u.getValue("&b9"));
			Sp.p(u.getValue("c_1"));
			Sp.p(u.getValue("c"));
			Sp.p(u.getValue("c"));
			Sp.p(u.getValue(""));
			Sp.p(u.getValue(""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}

	}

}