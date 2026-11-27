/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import at.nicolas.x.int_interpreter.Sp;

public class Tk_Token {

	static volatile int value = 0;
	
	public synchronized static int go(int bla) {
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
	public synchronized static int leave(int bla) {
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