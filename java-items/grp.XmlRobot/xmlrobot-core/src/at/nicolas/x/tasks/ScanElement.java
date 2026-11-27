/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.Namespace;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ITask;

public class ScanElement extends Element {

	static ITask itask = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, Integer> int_map = new HashMap<String, Integer>();
	// HashMap<String, Double> double_map = new HashMap<String, Double>();

	public ScanElement() {
		// TODO Auto-generated constructor stub
	}

	public void putInt ( String name, int value ) {
		int_map.put(name, value);
		this.setAttribute(name, Commons.int2String(value));
	}
	public void putInt ( String name, double value ) {
		
		putInt(name, (new Double(value)).intValue());
	}	
	/*
	public void putDouble ( String name, double value ) {
		double_map.put(name, value);
	} */
	
	public int getInt ( String name) throws Exception {
		
		try {
			return int_map.get(name) ;
			
		} catch (Exception e) {
			try {
			//lc.info("Problem with dir: "+this.getAttributeValue("ipath"),itask);
			Sp.p("A Problem occured "+name);
			Sp.p("DID NOT FIND: "+name);
			Sp.p("Element="+Commons.toText(this));
			
			
			} catch (Exception e2 ) {
				Sp.p("had a problem!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
			Sp.p("bevore return 0");
			// throw new Exception("There is some problem with the directory, some secret system directory ...");
			return 0;
		
		}
	}
	/*
	public double getDouble ( String name) {
		return double_map.get(name) ;
	}	*/
	
	public ScanElement(String name) {
		super(name);
		int_map = new HashMap<String, Integer>();
		// double_map = new HashMap<String, Double>();
		// TODO Auto-generated constructor stub
	}

	public ScanElement(String name, Namespace namespace) {
		super(name, namespace);
		// TODO Auto-generated constructor stub
	}

	public ScanElement(String name, String uri) {
		super(name, uri);
		// TODO Auto-generated constructor stub
	}

	public ScanElement(String name, String prefix, String uri) {
		super(name, prefix, uri);
		// TODO Auto-generated constructor stub
	}
	
	public static String cutDouble ( double in ) throws Exception {
		if ( in > 0 ) {
			Double x = new Double(in  + 0.5);
			int y = x.intValue();
			return Commons.int2String(y);			
		} else {
			double y = Commons.round(in, 2);
			return Commons.double2string(y);
			
		}

	}	

}