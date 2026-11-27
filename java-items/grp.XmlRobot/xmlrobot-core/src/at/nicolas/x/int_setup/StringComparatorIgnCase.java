/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.util.Comparator;

public class StringComparatorIgnCase implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		return o1.compareToIgnoreCase(o2);
	}

}