/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Vcar;

public abstract class Tk_TkBoolBase extends Tk_Tk implements Tk_IKeysOn{

	Element tkN;
	String idN;
    String curValN = null;
	ITiHandler tih;
	String mainLabelN;
	String tipN;
	boolean nono;
	boolean no_is_0;
	
	public void built(Element tkN,ITiHandler tih,String mainLabelN, String tipN) throws Exception {
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabelN = mainLabelN;
		this.tipN = tipN;
		this.nono = Commons.hasFlag(tkN, "nono");
		this.no_is_0 = Commons.hasFlag(tkN, "no_is_0");
		idN = tkN.getAttributeValue("id");
		curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);

	}

	public String getVarType() {
		return ("prop");
	}

	public String getVarIdN() {
		return idN;
	}

	public String getValue4RegexN() {
		return getValueN();
	}		
	public String getDefaultValue() throws Exception {
		// TODO Auto-generated method stub
		return Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);
	}
	

	@Override
	public String getValueN() {
		return curValN;
	}
	public String getValuePN() {
		String ret = curValN;
		Vcar x = new Vcar(curValN);
		try {
			if ( x.isBool && x.getBool()) ret=tih.getSitzung().lang().getYes();
			else if ( x.isBool && !x.getBool()) ret=tih.getSitzung().lang().getNo();
			return ret;
		} catch (Exception e) {
			return curValN;
		}
	}	

	public void clearValue() {
		// TODO Auto-generated method stub
		if ( no_is_0)
			curValN = "0";
		else
			curValN = "";
		
	}

	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return nono;
	}

	@Override
	public  synchronized void setValueN(String value) {
		curValN = value;	
	}

}