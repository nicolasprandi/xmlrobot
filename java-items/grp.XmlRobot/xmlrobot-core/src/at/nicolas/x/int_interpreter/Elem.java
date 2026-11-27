/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;

public class Elem {
	
	private LogConnector lc = new LogConnector("");
	private String 		 n = new String("   [Element]   ");
	
	public boolean isFunction = false;
	public String function = null;
	public String function_name = null;
	private ArrayList<String> pList;
	private ArrayList<Expression> peList;
	public boolean isText = false;
	public String text = null;
	
	
	public void setFunction( String f) throws Exception {
		isFunction = true;
		function = f;
		pList = getParaParts ( f );
		peList = new ArrayList<Expression>();
		
		int curpos = 0;
		Iterator<String> j = pList.iterator();
		while ( j.hasNext()) {
			String curVal = (String)j.next();
			if ( curpos == 0) {
				if ( !curVal.equals("f")) 
					throw new Exception("Should have an f at the beginning");
			} else if ( curpos == 1 ) {
				function_name = curVal;
			} else {
				Expression pe = new Expression(curVal);
				peList.add(pe);				
			}
			
			curpos++;
		}
	
	}
	public  ArrayList<Expression> getParaExpressionList() {
		return peList;
	}
	private ArrayList<String> getParaParts (String f ) {
		
		ArrayList<Integer> posList = new ArrayList<Integer>();
		ArrayList<String> paraList = new ArrayList<String>();
		int depth = 0;
		
		int position = 1;
		String enc;
		while (position < f.length()) {
			enc = f.substring(position, position+1);
			
			if (enc.equals("(") ) depth++;
			else if (enc.equals(",")  && depth==0) posList.add(new Integer(position));
			else if (enc.equals(")") && depth>0 ) depth--;
			
			position++;
		}
		// lc.core(n+"Starting up: "+f,LogConnector.CORE_EXPRESSIONS);
		int startingPoint = 0;
		Iterator<Integer> i = posList.iterator();
		while ( i.hasNext()) {
			Integer integer = (Integer)i.next();
			if ( startingPoint == 0 )
				paraList.add(f.substring(startingPoint+1,integer));
			else
				paraList.add(f.substring(startingPoint,integer));
			startingPoint = integer+1;
		}

		paraList.add(f.substring(startingPoint,f.length()-1));
		
		return paraList;	
	}
	
	public void setText( String t) {
		isText = true;
		text = t;
	}
	
	public void trace(int reportlevel ) {
		
		String prefix = Commons.StringTimes("  ", reportlevel);
		if ( isText ) lc.core(prefix+"| "+text+" (text)",LogConnector.CORE_EXPRESSIONS);
		else if ( isFunction ) {
			lc.trace(prefix+"| "+function_name+" (function)");
			int next_reportlevel = reportlevel + 1;
			Iterator<Expression> j = peList.iterator();
			while ( j.hasNext()) {
				Expression pe = (Expression)j.next();
				pe.trace(next_reportlevel);
			}			
		}

	}
	

}