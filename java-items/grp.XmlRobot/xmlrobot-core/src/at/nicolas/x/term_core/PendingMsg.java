/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import org.jdom2.Element;

public class PendingMsg {
	Element firstElement = null;
	Element secondElement = null;

	public Element getFirstElement() {
		return firstElement;
	}
	public void setFirstElement(Element firstElement) {
		this.firstElement = firstElement;
	}
	public Element getSecondElement() {
		return secondElement;
	}
	public void setSecondElement(Element secondElement) {
		this.secondElement = secondElement;
	}
	public boolean hasSecondElement() {
		if ( secondElement == null )
			return false;
		else
			return true;
	}
}