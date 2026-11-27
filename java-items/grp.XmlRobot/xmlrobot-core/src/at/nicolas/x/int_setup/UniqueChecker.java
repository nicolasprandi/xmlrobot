/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.util.HashMap;

public class UniqueChecker {
	
	private HashMap<String, String> list = new HashMap<String, String>();
	private CommStartMsg sm;
	private String name;
	public UniqueChecker ( CommStartMsg sm, String name  ) {
		this.sm = sm;
		this.name = name;
	}
	
	public boolean isNew( String key, String desc, String failMessage ) {
		
		if ( list.get(key) == null ) {
			// a new one
			list.put(key, desc);
			return true;
		} else {
			if ( failMessage != null ) {
				String message = failMessage;
				if ( name != null )
					message = message +", happend in: "+name;
				String other = (String)list.get(key);
				message = message +", the first one was: "+other;
				sm.out.println(message);
				sm.addImportant(message);
			}
			
			return false;
		}
	}

}