/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import at.nicolas.x.int_interpreter.Sp;

public class Tk_Token2 {

	private volatile int value = 0;
	public synchronized int go(int bla) {
		if ( bla > 0 ) {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			Sp.pe(e);
		}
		}
		
		value++;
		value = value +bla;
		return value;		
	}
	public synchronized int leave(int bla) {
		if ( bla > 0 ) {
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			Sp.pe(e);
		}
		}
		
		value++;
		value = value + bla;
		return value;		
	}
	
}