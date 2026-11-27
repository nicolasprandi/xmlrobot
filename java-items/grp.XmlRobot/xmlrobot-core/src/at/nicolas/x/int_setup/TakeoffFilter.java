/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.io.File;
// fuer die manuelle selektion des takeoff-files

import javax.swing.filechooser.FileFilter;

public class TakeoffFilter extends FileFilter {

	String[] exp;
	String extensions;
	boolean everything_ok = true;
	
	public TakeoffFilter ( String extensions ) {
		
		if ( extensions != null ) {
			this.extensions = extensions.toLowerCase();
			exp = this.extensions.split(" ");
			everything_ok = false;	
		}
		
	}
	
	@Override
	public boolean accept(File f) {
		if ( everything_ok ) return true;
		
		boolean ret = false;
		for ( String ex : exp ) {
			if ( f.getName().toLowerCase().endsWith(ex) || f.isDirectory()){
				ret = true;
				break;
			}
		}
		
		return ret;
	}

	@Override
	public String getDescription() {
		if ( everything_ok)
			return "*.*";
		else
			return extensions;
	}

}