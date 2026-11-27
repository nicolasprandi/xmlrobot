/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import javax.swing.JComponent;

public interface Tc_UiMap {



	//
	//  Initialisierung
	//  an das masterUiMapSet werden ver�nderungen propagiert
	//
	public void setMapSetMaster (	Tc_AppConsole _appConsole3);

	//
	//  Die Funktion setzt den aktuellen Kontext der Map.
	//  Hier d�rften eigentlich nur Segmente �bergeben werden, die dem
	//  getRootSegTypeId() entsprechen.
	//  Das RootSegment ist zu speichern, bzw. der Bildschirm darauf auszurichten
	//  Default Selektionen der Childs sind auch vorzunehmen.
	//
	public void bind ( );
	
	public JComponent getJComponent ();
	public void start ( );


} // Ende Interfacedefinition Var
