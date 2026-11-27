/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.io.File;
// f�r die pr�fung der extensions bei ausgew�hltem file
import java.io.FilenameFilter;

public class Tk_TkFilenameFilterMacLinux implements FilenameFilter {

	String[] exp;
	String extensions;
	boolean everything_ok = true;
	
	public Tk_TkFilenameFilterMacLinux ( String extensions ) {
		
		if ( extensions != null ) {
			this.extensions = extensions.toLowerCase();
			exp = this.extensions.split(" ");
			everything_ok = false;	
		}
	}
	
	public boolean accept(File dir, String file) {

		if ( everything_ok ) return true;
		
		boolean ret = false;
		for ( String ex : exp ) {
			if ( file.toLowerCase().endsWith(ex) ){
				ret = true;
				break;
			}
		}
		
		return ret;
	}


	public String getDescription() {
		if ( everything_ok)
			return "*.*";
		else
			return extensions;
	}

}