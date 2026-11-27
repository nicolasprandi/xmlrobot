/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Strategy {
	
	HashMap<String, String>  strats;
	public Strategy() {
		strats = new HashMap<String, String>();
		
	}
	
	public void addStrategy( String in, String errortext ) throws Exception {
		
		if ( in != null ) {
			String[] temp = in.split("\\s+");
			for(int i =0; i < temp.length ; i++) {
				String now = temp[i];
				
				String[] ab = now.split("=");
				if ( ab.length != 2 )
					throw new Exception(errortext+" This does not fit a Stategy, should be 'XX=YY', but its "+now);
				String name = ab[0];
				String val = ab[1];
				if ( strats.containsKey(name)) {
					// existiert schon, �berschreiben
					strats.put(name, val);
				} else
					strats.put(name, val);				
			}		
		}		
	}
	
	public void report( CommStartMsg sm ) {
		
		sm.out.print("Strategies = ");
		
		Set<String> keyset = strats.keySet();
		Iterator<String> i = keyset.iterator();

		while ( i.hasNext() ) {
			String name  = (String)i.next();
			String val = (String)strats.get(name);
			sm.out.print(name+"='"+val+"' ");
		}		
		sm.out.println("");
	}
	
	public boolean hasStrategy ( String in ) {
		return ( strats.containsKey(in)) ;
	}
	
	public String getStrategy ( String in ) {
		if ( strats.containsKey(in)) {
			return (String)strats.get(in);
		} else
			return null;
	}	
	
	public String getStrategy ( String in, String onDefault ) {
		if ( strats.containsKey(in)) {
			return (String)strats.get(in);
		} else
			return onDefault;
	}		
	
	public boolean isStrategy ( String strategy, String val ) {
		if ( strats.containsKey(strategy)) {
			String x = (String)strats.get(strategy);
			if ( x.equals(val))
				return true;
			else 
				return false;
		} else
			return false;
	}		
	


}