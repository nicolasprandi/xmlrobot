/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Locale_Prop implements IFun {

	private static final String funn= "locale_prop";
	private static final String pm= "function 'locale_prop' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1   ) 
			throw new LangException(pm+"wants 1 parameter: 1=propertyname");		
		
		String pn = ((Vcar)vcar_list.get(0)).s;
		
		Locale locale = null;
		if ( vcar_list.size() == 2 ) {
			locale = new Locale(((Vcar)vcar_list.get(1)).s);
		} else
			locale = Locale.getDefault();
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols( locale );
		
		String r = null;
		
		if ( pn.equals("display_name"))
			r = locale.getDisplayName();
		else if ( pn.equals("country"))
			r = locale.getCountry();
		else if ( pn.equals("language"))
			r = locale.getLanguage();
		else if ( pn.equals("currency_symbol"))
			r = dfs.getCurrencySymbol();
		else if ( pn.equals("currency_code"))
			r = dfs.getCurrency().getCurrencyCode();
		else if ( pn.equals("decimal_separator")) 
			r=Character.toString(dfs.getDecimalSeparator());
		else if ( pn.equals("grouping_separator")) 
			r=Character.toString(dfs.getGroupingSeparator());
		else if ( pn.equals("minus_sign")) 
			r=Character.toString(dfs.getMinusSign());
		else if ( pn.equals("monetary_decimal_separator")) 
			r=Character.toString(dfs.getMonetaryDecimalSeparator());	
		else
			throw new LangException(pm+"found unknown local-property: "+pn);
		
		return (new Vcar(r));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}