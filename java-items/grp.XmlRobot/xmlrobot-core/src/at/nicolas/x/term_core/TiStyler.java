package at.nicolas.x.term_core;

public class TiStyler {

	public String getTable0() {
		return " class=\"tiTable0\"";
	}
	
	public String getTable1(String classAdder, int desktopWidthPx) {
		if ( classAdder== null)
			return " class=\"tiTable1\"";
		else
			return " class=\"tiTable1 "+classAdder+"\"";
	}
	
	public String getTable2() {
		return " class=\"tiTable2\"";
	}	
	
	public String getTable3() {
		return " class=\"tiTable3\"";
	}	
	
	public String getAnkerGREYNONE() {
		return " class=\"tiGreynone\"";
	}	
	
	public String getAnkerGREY() {
		return " class=\"tiGrey\"";
	}	
	
	public String getAnkerUL(boolean bold) {
		if ( bold )
			return " class='tiUlBold'";
		else
			return " class='tiUl'";
	}	
	
	public String getAnkerFile(boolean bold) {
		if ( bold )
			return " class='tiFile tiFileBold'";
		else
			return " class='tiFile'";
	}		
	
	public String getAnkerDirBLACK(boolean asPartOfChoose) {
		if ( asPartOfChoose )
			return " class='tiDir tiDirAsPartOfChoose'";
		else
			return " class='tiDir'";
	}		
	
	public String getAnkerJobBLACK() {
		return " class='tiJob'";
	}
	
	public String getAnkerServiceBLACK() {
		return " class='tiService'";
	}	
	  
	public String getAnkerWebBLACK() {
		return " class='tiWeb'";
	}		
	
	public String getTdRec(String tup) {
		return " class='tiTabGrid_"+tup+" tiTabGrid'";
	}	
	
	public String getDivRec(String tup) {
		return " class='tiTabFloat_"+tup+" tiTabFloat'"; //_"+tup+"'";
	}	
	public String getDivRecStyle(String tup) {
		return " "; //_"+tup+"'";
	}	
	
	public String getARec(String tup) {
		return " class='tiTab_"+tup+" tiTab'";
	}		
	
	public String getJsTip() {
		return " class='tiJsTip'";
	}		
	
	public String getTable1Class() {
		return " tiTable1 ";
	}
	
	public String getTable2Class() {
		return " tiTable2 ";
	}	
	
	public String getTable3Class() {
		return " tiTable3 ";
	}	
	
	public String getAnkerGREYNONEClass() {
		return " tiGreynone ";
	}	
	
	public String getAnkerGREYClass() {
		return " tiGrey ";
	}	
	
	public String getAnkerULClass(boolean bold) {
		if ( bold )
			return " tiUlBold ";
		else
			return " tiUl ";
	}	
	
	public String getAnkerDirBLACKClass() {
		return " tiDir ";
	}		
	
	public String getAnkerJobBLACKClass() {
		return " tiJob ";
	}
	
	public String getAnkerServiceBLACKClass() {
		return " tiService ";
	}	
	
	public String getAnkerJobPlainBLACKClass() {
		return " tiJobPlain ";
	}
	
	public String getAnkerServicePlainBLACKClass() {
		return " tiServicePlain ";
	}		
	
	public String getAnkerWebBLACKClass() {
		return " tiWeb ";
	}		
	
	public String getTdRecClass(String tup) {
		return " tiTabGrid_"+tup+" tiTabGrid ";
	}	
	
	public String getDivRecClass(String tup) {
		return " tiTabFloat_"+tup+" tiTabFloat "; //_"+tup+"'";
	}	
	
	public String getARecClass(String tup) {
		return " tiTab_"+tup+" tiTab ";
	}		
	
	public String getJsTipClass() {
		return " tiJsTip ";
	}			
		
	public TiStyler() {
		// TODO Auto-generated constructor stub
	}

}
