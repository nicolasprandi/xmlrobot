/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.util.ArrayList;

import javax.swing.JComponent;

import at.nicolas.x.int_specs.ITerminator;

public interface Persp_IZP {

	public void build( );
	public ArrayList getMaps();
	public JComponent getJComponent ();
	public String getName();
	
	
	public ITerminator getTerminator();
}