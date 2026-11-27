package at.nicolas.x.term_core;

public class TiStylerDesktop extends TiStyler {
	private String table_border="0";
	
	public String getTable0() {
		return " ";
	}	//width='100%'
	public String getTable1(String classAdder, int desktopWidthPx ) {
		return " border='"+table_border+"'  cellpadding='1' cellspacing='1' width='"+desktopWidthPx+"px'";
	}
	
	public String getTable2() {
		return " border='"+table_border+"' width='100%' cellpadding='0' cellspacing='0'";
	}	
	
	public String getTable3() {
		return " width='100%'";
	}		
	
	public String getAnkerGREYNONE() {
		return " style='color:grey; text-decoration:none;'";
	}	
	
	public String getAnkerGREY() {
		return " style='color:grey;'";
	}	
	public String getAnkerUL(boolean bold) {
		if ( bold )
			return " style='color:black; text-decoration: underline; font-weight:bold;'";
		else
			return " style='color:black; text-decoration: underline; '";
	}
	
	public String getAnkerFile(boolean bold) {
		if ( bold )
			return " style='color:black; text-decoration: underline; font-weight:bold;'";
		else
			return " style='color:black; text-decoration: underline; '";
	}	
	
	public String getAnkerDirBLACK(boolean asPartOfChoose) {
		return " style='color:black;'";
	}		
	
	public String getAnkerJobBLACK() {
		return " style='color:black;'";
	}
	
	public String getAnkerServiceBLACK() {
		return " style='color:black;'";
	}	
	public String getAnkerWebBLACK() {
		return " style='color:black;'";
	}	
	
	public String getTdRec(String tup) {
		return " style='padding-right: 8px;' ";
	}	
	
	public String getARec(String tup) {
		return " style='color:black;'";
	}		
	
	public String getDivRec(String tup) {
		return " style='color:black;'";
	}
	
	public String getDivRecStyle(String tup) {
		return " color:black;";
	}	
	
	public String getJsTip() {
		return " style='color:light-grey; font-size:x-small;'";
	}		
	
	public TiStylerDesktop(String table_border) {
		this.table_border = table_border;
		
	}
	public void setTable_border(String table_border) {
		this.table_border = table_border;
	}
	


}
