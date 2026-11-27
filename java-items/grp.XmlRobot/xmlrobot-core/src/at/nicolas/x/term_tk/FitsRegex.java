/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

public class FitsRegex {
	static Tk_MyMessageDialog fits(
			String resultN,
			boolean hasNono,
			String regex,
			Tk_MyMessageDialog regexMsg,
			String varLabel
			) {
		Tk_MyMessageDialog ret = null;
		if ( resultN == null ) {							
			
			ret = new Tk_MyMessageDialog();
			ret.built("Input Error","There must be a value for '"+varLabel+"'");
			ret.setTopHeaderText("Value requested");
			ret.setFocusToOkButton();
		} else if ( resultN.length()==0 && !hasNono ) {
			ret = null;
		} else if ( resultN.length()==0 && hasNono ) {
			ret = new Tk_MyMessageDialog();
			ret.built("Input Error","There must be a value for '"+varLabel+"'");
			ret.setTopHeaderText("Value requested");
			ret.setFocusToOkButton();			
		} else if ( regex == null ) {
			ret = null;
		} else {
			boolean valid_regex = resultN.matches(regex);
			// Sp.p("Evaluating for regex : '"+resultN+"'");
			if ( valid_regex )
				ret = null;
			else
				ret =regexMsg;
		}		
		return ret;
		
	}

}