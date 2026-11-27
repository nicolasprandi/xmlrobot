/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.LangException;


public class ExcelUtilXrob {
	private static String fromTask = null;
	static void setFromTask ( String _fromTask ) {
		fromTask = _fromTask;
	}	
	
	/*
	static ILogConnector lc = null;
	static ITask task = null;
	static void setLogConnector ( ILogConnector _lc, ITask _task ) {
		lc = _lc;
		task = _task;
	}*/
	/*
	 * SetSheetInfo
	 */
	public static void setSheetInfo ( Element root, String sheetName, int sheetIndex ) {
		root.setAttribute("sheet_name", sheetName);
		root.setAttribute("sheet_index",Commons.int2String(sheetIndex+1));
	}
	

	/*
	 * 	Append Row
	 */
	private static String	ar_name_rec;
	private static String	ar_name_index;
	private static HashMap<Integer,String> ar_headRow;
	private static int ar_style;
	private static String cell_element = "c";

	public static void appendTableRowInit ( 	Element root,
										String name_rec, 
										String name_index,
										HashMap<Integer,String> headRow,
										int style ) {
		ar_name_rec = name_rec;		
		ar_name_index = name_index;
		ar_headRow = headRow;
		ar_style = style;
		
		if ( ar_style == style_excel_update ) {
			Element headerElement = new Element("row");
			headerElement.setAttribute("row", "-1");
			Set<Integer> keySet = headRow.keySet();
			Iterator<Integer> i = keySet.iterator();
			Integer   aktKey = null;
			String    aktVal = null;
			while ( i.hasNext() ) {
				aktKey = (Integer)i.next();
				aktVal = (String)headRow.get(aktKey);
				Element cellElement = new Element(cell_element);
				cellElement.setText(aktVal);
				headerElement.addContent(cellElement);
			}
			root.addContent(headerElement);
			
		}

	}
	public static void appendRowClose ( ) {
		ar_name_rec = null;	
		ar_name_index = null;
		ar_headRow = null;
		ar_style = -1;

	}	
	
	public static void appendTableRow(HashMap<Integer,String> aktRow, int row_index, Element root,Unique unique ) throws Exception {
		
		if ( aktRow == null ) {
			Sp.p("appendRow received null, row_index="+row_index);
			// return;
		}
		Set<Integer> keySet = aktRow.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;
		String    aktVal = null;
		String	  headName = null;

		Element eRow = new Element(ar_name_rec);
	 	if ( ar_name_index != null )
	 		eRow.setAttribute(ar_name_index, String.valueOf(row_index));
	 	
		while ( i.hasNext() ) {
			aktKey = (Integer)i.next();
			aktVal = (String)aktRow.get(aktKey);
		
		 	headName = ar_headRow.get(aktKey);
		 	
		 	if (headName == null ) {
		 		if ( unique != null ) {
		 			headName = unique.getValue("");
		 			ar_headRow.put(aktKey, headName);
		 		} else {
		 			throw new LangException(999,"Excel-Reader encountered a problem. The column with the index '"+(aktKey+1)+"' doesnt have a column-name. The first column-value found was '"+aktVal+"'");
		 		}
		 	}
		 	
		 	if ( ar_style == style_excel_update ) {
		 		Element cellElement = new Element(cell_element);
				cellElement.setText(aktVal);
				eRow.addContent(cellElement);		 		
		 	} else if (aktVal != null) {
			 	if ( ar_style == style_short ) {
			 		// short
			 		eRow.setAttribute(headName, aktVal);
			 	} else {
			 		Element cellElement = null;
			 		if ( ar_style == style_generic ) {
			 			// generic
			 			cellElement = new Element("col");
			 			cellElement.setAttribute("name", headName );
			 		} else if ( ar_style == style_standard ) {
			 			// standard
			 			cellElement = new Element(headName);
			 		}
			 		
					cellElement.setText(aktVal);
					eRow.addContent(cellElement);		 		
			 	}		 		
		 	}
		 }
	 	
		 root.addContent(eRow);		
	}
	
	/*
	 * 	Append Field
	 */
	private static int af_style;
	public static void appendFieldInit ( int style ) {
		af_style = style;
	}
	public static void appendFieldClose ( ) {
		af_style = -1;
	}	
	
	public static void appendFormField(HashMap<Integer,String> aktRow, Element root, int header, int data ) {
		
		Set<Integer> keySet = aktRow.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;
		String    aktVal = null;

		String feld_name = (String)aktRow.get(header);
		String feld_inhalt = (String)aktRow.get(data);
		/*
	 	int cur_pos = 0;
		while ( i.hasNext() ) {
			aktKey = (Integer)i.next();
			aktVal = (String)aktRow.get(aktKey);
			if ( cur_pos == header ) {
				feld_name = aktVal;
			} else if ( cur_pos == data ) {
				feld_inhalt = aktVal; 
			}else { 
				if ( feld_inhalt == null ) {
					feld_inhalt = aktVal; 
				}
			} 
			cur_pos++;
		}
		*/
	 	
		if ( feld_inhalt != null ) {
			/*
			if ( feld_name == null )
				feld_name = "relates_to";
			else if ( feld_inhalt == null ) {
				feld_inhalt = new String("");
				// feld_name = "relates_to";
			} 
			if ( feld_name.indexOf(" ")> 0 )
				feld_name = Commons.stringReplace(feld_name, " ", "_");
			if ( feld_name.endsWith(":"))
				feld_name = feld_name.substring(0, feld_name.length()-1);
			if ( feld_name.indexOf(":")> 0 )
				feld_name = Commons.stringReplace(feld_name, ":", "_");		
				*/	

			//--
		 	if ( af_style == style_short ) {
		 		// short
		 		root.setAttribute(feld_name, feld_inhalt);
		 	} else {
				Element ne = null;
				if ( af_style == style_standard ) {
					ne = new Element(feld_name);
					ne.setText(feld_inhalt);
				} else if ( af_style == style_generic ){
					ne = new Element("col");
		 			ne.setAttribute("name", feld_name);		
		 			ne.setText(feld_inhalt);
				} else if ( af_style == style_excel_update ){
					ne = new Element("row");
					Element e1 = new Element (cell_element);
					e1.setText(feld_name);
					ne.addContent(e1);
					
					Element e2 = new Element (cell_element);
					e2.setText(feld_inhalt);
					ne.addContent(e2);
				} 
				
				root.addContent(ne);
		 	}
			//--

		}
				
	}	
	
	/*
	 * 	Append Cells
	 */
	private static int ac_offset_rows;
	private static int ac_offset_cols;
	public static void appendCellsInit (int offset_rows, int offset_cols, int _af_style ) {
		ac_offset_rows = offset_rows;
		ac_offset_cols = offset_cols;
		af_style = _af_style;
	}
	public static void appendCellsClose ( ) {
		ac_offset_rows = 0;
		ac_offset_cols = 0;
	}	
	
	public static void appendMatrixCells(HashMap<Integer,String> aktRow, int row_index, Element cells, Element root ) {
		
		Set<Integer> keySet = aktRow.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;
		String    aktVal = null;
		
		Element eRow = new Element("row");
		
	 	int curpos = 0;
		while ( i.hasNext() ) {
			aktKey = (Integer)i.next();
			aktVal = (String)aktRow.get(aktKey);
			
			Element e = new Element(cell_element);
			e.setAttribute("row", Commons.int2String(row_index + ac_offset_rows ));
			e.setAttribute("col", Commons.int2String(aktKey + ac_offset_cols ));
			e.setText(aktVal);
			
			cells.addContent(e);
			
			if (af_style == style_excel_update) {
				Sp.p(" +++ aktKey="+aktKey+", curpos=" +curpos+"+++ VAL="+aktVal);
				//curpos++;
				if ( aktKey > curpos ) {
					int toadd = aktKey - curpos;
					for ( int ix = 0;ix<toadd;ix++) {
						Element e3 = new Element(cell_element);
						eRow.addContent(e3);	
						Sp.p("   Added one Cell");
					}
					
				}
				Element e2= new Element(cell_element);
				e2.setText(aktVal);
				eRow.addContent(e2);
				curpos = aktKey+1;
				
				
			}

		 }		
		
		if (af_style == style_excel_update) {
			root.addContent(eRow);
		}
	}	
	
	/*
	 * 			getSheetIndex ( Sheet, name, index)
	 */
	public static int getSheetIndex ( Object workbook, String sheet_name, int sheet_index ) throws Exception {
		int retval = -1;
		if ( sheet_name != null && sheet_name.length()>0 ) {
			// per name
			int sheet_count = ExcelUtilPoi.getNumberOfSheets(workbook);
			
			for(int i=0; i < sheet_count; i++){
				String cur_sheet_name = ExcelUtilPoi.getSheetName((Object)workbook, i);
				if ( cur_sheet_name.equals(sheet_name)) {
					retval = i +1;
					break;
				}
			}
			if ( retval == -1 ) {
				throw new Exception("The sheet '"+sheet_name+"' was not found in excel.");
			}
		} else if ( sheet_index > 0 )  {
			retval = sheet_index;			
		} else {
			throw new Exception("No Sheet found");
		}
		
		return retval;
		
	}
	
	/*
	 * X-Achse einlesen
	 */
	public static int paraX2int ( String value, int def, String name) throws Exception {
		int ret = -1;
		if ( value == null ) {
			//lc.log_ext("value '"+name+ "' set to "+def+ " (default)",iBug);
			ret = def;
		} else {		
			if ( Commons.isInt(value) ) {
				//lc.log_ext("value '"+name+ "' set to "+value,iBug);
				ret = Commons.string2int(value);
			} else {
				if ( value.length() == 0 )
					throw new Exception("The value of '"+name+"' is undefined");
				else if ( value.length()== 1 ) {
					ret = cellChar2int(value);
					
				} else if ( value.length()== 2 ) {
					int a = cellChar2int(value.substring(0, 1));
					int b = cellChar2int(value.substring(1, 2));
					ret = ( a * 26 ) + b;
				}
				//lc.log_ext("value '"+name+ "' received "+value +" and was set to "+ret,iBug);
			}
			
		}
		return ret;
	}
	
	private static int cellChar2int ( String x ) throws Exception {
		// lc.info("sdlf received '"+x+"'", iBug);
		int ret = -1;
		if ( x.equals("A") ) ret = 1;
		else if ( x.equals("B") ) ret = 2;		
		else if ( x.equals("C") ) ret = 3;
		else if ( x.equals("D") ) ret = 4;
		else if ( x.equals("E") ) ret = 5;
		else if ( x.equals("F") ) ret = 6;
		else if ( x.equals("G") ) ret = 7;
		else if ( x.equals("H") ) ret = 8;
		else if ( x.equals("I") ) ret = 9;
		else if ( x.equals("J") ) ret = 10;
		else if ( x.equals("K") ) ret = 11;		
		else if ( x.equals("L") ) ret = 12;
		else if ( x.equals("M") ) ret = 13;
		else if ( x.equals("N") ) ret = 14;
		else if ( x.equals("O") ) ret = 15;
		else if ( x.equals("P") ) ret = 16;
		else if ( x.equals("Q") ) ret = 17;
		else if ( x.equals("R") ) ret = 18;
		else if ( x.equals("S") ) ret = 19;
		else if ( x.equals("T") ) ret = 20;		
		else if ( x.equals("U") ) ret = 21;
		else if ( x.equals("V") ) ret = 22;
		else if ( x.equals("W") ) ret = 23;
		else if ( x.equals("X") ) ret = 24;
		else if ( x.equals("Y") ) ret = 25;
		else if ( x.equals("Z") ) ret = 26;
		else if ( x.equals("AA") ) ret = 27;
		else if ( x.equals("AB") ) ret = 28;
		else if ( x.equals("AC") ) ret = 29;
		else if ( x.equals("AD") ) ret = 30;
		else if ( x.equals("AE") ) ret = 31;
		else if ( x.equals("AF") ) ret = 32;
		else if ( x.equals("AG") ) ret = 33;
		else if ( x.equals("AH") ) ret = 34;
		else if ( x.equals("AI") ) ret = 35;
		else if ( x.equals("AJ") ) ret = 36;
		else if ( x.equals("AK") ) ret = 37;
		else if ( x.equals("AL") ) ret = 38;
		else if ( x.equals("AM") ) ret = 39;
		else if ( x.equals("AN") ) ret = 40;
		else if ( x.equals("AO") ) ret = 41;
		else if ( x.equals("AP") ) ret = 42;
		else if ( x.equals("AQ") ) ret = 43;
		else if ( x.equals("AR") ) ret = 44;
		else if ( x.equals("AS") ) ret = 45;
		else if ( x.equals("AT") ) ret = 46;
		else if ( x.equals("AU") ) ret = 47;
		else if ( x.equals("AV") ) ret = 48;
		else if ( x.equals("AW") ) ret = 49;
		else if ( x.equals("AX") ) ret = 50;
		else if ( x.equals("AY") ) ret = 51;
		else if ( x.equals("AZ") ) ret = 52;
		else if ( x.equals("BA") ) ret = 53;
		else if ( x.equals("BB") ) ret = 54;
		else if ( x.equals("BC") ) ret = 55;
		else if ( x.equals("BD") ) ret = 56;
		else if ( x.equals("BE") ) ret = 57;
		else if ( x.equals("BF") ) ret = 58;
		else if ( x.equals("BG") ) ret = 59;
		else if ( x.equals("BH") ) ret = 60;	
		else if ( x.equals("BI") ) ret = 61;
		else if ( x.equals("BJ") ) ret = 62;
		else if ( x.equals("BK") ) ret = 63;
		else if ( x.equals("BL") ) ret = 64;
		else if ( x.equals("BM") ) ret = 65;
		else if ( x.equals("BN") ) ret = 66;
		else if ( x.equals("BO") ) ret = 67;
		else if ( x.equals("BP") ) ret = 68;
		else if ( x.equals("BQ") ) ret = 69;
		else if ( x.equals("BR") ) ret = 70;
		else if ( x.equals("BS") ) ret = 71;
		else if ( x.equals("BT") ) ret = 72;
		else if ( x.equals("BU") ) ret = 73;
		else if ( x.equals("BV") ) ret = 74;
		else if ( x.equals("BW") ) ret = 75;
		else if ( x.equals("BX") ) ret = 76;		
		else if ( x.equals("BY") ) ret = 77;
		else if ( x.equals("BZ") ) ret = 78;		
		
		else {
			Exception excpetion = new Exception("task '"+fromTask+"' expected something between A and BZ, but it is '"+x+"'");
			// lc.addException(excpetion);
			throw excpetion;			
		}
		return ret;
		
	}
	
	/*
	 * style
	 */
	public static final int style_standard = 1;
	public static final int style_generic = 2;
	public static final int style_short = 3;
	public static final int style_excel_update = 4;
	
	public static int style2int ( String value) throws Exception {
		
		String style_orig =Commons.para2string(value,"standard","style");
		int style = -1;
		if ( style_orig.endsWith("standard"))
			style = style_standard;
		else if ( style_orig.endsWith("generic"))
			style = style_generic;
		else if ( style_orig.endsWith("short"))
			style = style_short;
		else if ( style_orig.endsWith("excel_update"))
			style = style_excel_update;
		else
			throw new Exception (fromTask+ " says: The attribute 'style' should be 'standard', 'generic' or 'short', but it is '"+style_orig+"'");

		return style;

	}	
}