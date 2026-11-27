/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Excel_Table implements ITask{

	String 	excel;
	int    	sheet_index_orig;
	String	sheet_name;
	int    	sheet_index;
	int		x;
	int		y;
	int		x2;
	int		y2;
	String  root_name;	
	String  row_name;
	String  row_index_name;	
	int		style = 0;  // 1=standard, 2=generic, 3=short
	String out;
	String filler_input;
	int		count_base;

	
	HashMap<Integer,String> headRow = null;
	Element  root = null;
	String tname;
	private static String says = new String("task 'excel_table' says: ");
	public static DateFormat getDateFormat (Element e ) {
		if ( e.getAttribute("date_format") == null ) {
			return new SimpleDateFormat("yyyy-MM-dd");
		} else if ( ((e.getAttributeValue("date_format")).trim()).equals(""))
			return new SimpleDateFormat("yyyy-MM-dd");
		else
			return new SimpleDateFormat(e.getAttributeValue("date_format"));
	}

	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		this.tname = "excel_table";	

		
		// Sheet - Parameter einlesen
		excel = 	Commons.para2string(e.getAttributeValue("excel"),null,"excel");
		if ( excel == null )
			throw new LangException(501,says+"attribute 'excel' is required."
							+" It has the filename of the excel to be processed. Found: "+Commons.toText(e));
		sheet_index_orig = Commons.para2int(e.getAttributeValue("sheet_index"),1,"sheet_index");
		sheet_name = Commons.para2string(e.getAttributeValue("sheet"),null,"sheet");

		boolean apply_date = !Commons.hasFlag(e, "no_dates");
		DateFormat date_format = Excel_Table.getDateFormat(e);
		
		// File excelFile = new File(excel);
	
		// Sheet - Physisch einlesen
		// HSSFWorkbook wb = (HSSFWorkbook)ExcelUtilPoi.getWorkbook(excel);
		Object wb = ExcelUtilPoi.getWorkbook(excel,rh.getSitzung());
		sheet_index = ExcelUtilXrob.getSheetIndex((Object)wb, sheet_name, sheet_index_orig);
		sheet_index = sheet_index -1 ;
		
		// HSSFSheet sheet = ExcelUtilPoi.getSheet(wb, sheet_index);
		Object sheet = ExcelUtilPoi.getSheet(wb, sheet_index);
		String sheet_name = ExcelUtilPoi.getSheetName(wb, sheet_index);
		
		// Koordinaten - Defaultwerte ermitteln
		int y_default = ExcelUtilPoi.getFirstRowNum(sheet) +1 ;
		int y2_default = ExcelUtilPoi.getLastRowNum(sheet) +1; 
		
		// Koordinaten - Parameter einlesen
		if ( e.getAttributeValue("x")!= null || e.getAttributeValue("y")!= null)
			throw new LangException(502, "excel_talbe: x,y depressive, use x1,y1");
		
		x = 		ExcelUtilXrob.paraX2int(e.getAttributeValue("x1"),1,"x1");
		x2 = 		ExcelUtilXrob.paraX2int(e.getAttributeValue("x2"),0,"x2");
		
		y = 		Commons.para2int(e.getAttributeValue("y1"),y_default,"y1");
		y2 = 		Commons.para2int(e.getAttributeValue("y2"),y2_default,"y2");
		
		// Koordinaten - Korrektur
		if ( y < y_default ) {
			y = y_default;
			//lc.log_ext("value 'y' was set to beginning of data: "+(y_default), this);
		}
		
		if ( y2 > y2_default ) {
			y2 = y2_default;
			//lc.log_ext("value 'y2' was set to end of data: "+(y2_default), this);
		}		
		
		// Koordinaten - Technische Korrektur
		x--;
		y--;
		x2--;
		y2--;
		
		filler_input = Commons.para2string(e.getAttributeValue("filler"),null,"filler");		
		root_name = Commons.para2string(e.getAttributeValue("root_name"),"root","root_name");
		if ( root_name.equals("") ) root_name = "root";
		row_name = Commons.para2string(e.getAttributeValue("row_name"),"row","row_name");
		if ( row_name.equals("") ) row_name = "row";
		row_index_name = Commons.para2string(e.getAttributeValue("row_index_name"),null,"row_index_name");
		if ( row_index_name!= null && row_index_name.equals("") ) row_index_name = null;
		style =ExcelUtilXrob.style2int(e.getAttributeValue("style"));
		
		out = 		Commons.para2string(e.getAttributeValue("out"),null,"out");
		if ( out == null )
			throw new LangException(503,says+"attribute 'out' is required."
							+" It has the filename of the xml-file to be created. Found: "+Commons.toText(e));		
		
		// root_name correction
		if ( style == ExcelUtilXrob.style_excel_update) {
			root_name = "rows";
			row_name = "row";
		}
		
		// kopfzeile mit spaltenueberschriften
		headRow = ExcelUtilPoi.getRow(sheet, y, x, x2,apply_date,date_format);
		if ( headRow == null )
			throw new LangException (504,"No header-Row found in "+excel+", in sheet #"+(sheet_index+1));
		
		Unique unique = null;
		boolean keep_names = Commons.hasFlag(e, "keep_names");
		if ( !keep_names ) {
			// make nice attribute names
			//lc.log_ext("Correcting columns to valid xml_attribute name ", this);
			Set<Integer> headKeySet = headRow.keySet();
			Iterator<Integer> headIt = headKeySet.iterator();
			Integer   headAktKey = null;
			String    headAktVal = null;
		 	unique = new Unique("xml_attribute");
			while ( headIt.hasNext() ) {
				headAktKey = (Integer)headIt.next();
				headAktVal = (String)headRow.get(headAktKey);
				headRow.put(headAktKey, unique.getValue(headAktVal));			 	
			}
		}

		// filler
		String[] filler;
		boolean has_filler = false;
		HashSet<Integer> fillerSet = new HashSet<Integer>();
		
		if ( filler_input != null ) {
			has_filler = true;
			filler = filler_input.split("\\s+");
			
			Set<Integer> headKeySet = headRow.keySet();
			Iterator<Integer> headIt = headKeySet.iterator();
			Integer   headAktKey = null;
			String    headAktVal = null;
		 	
			while ( headIt.hasNext() ) {
				headAktKey = (Integer)headIt.next();
				headAktVal = (String)headRow.get(headAktKey);
				for ( String cFiller:filler ) {
					if (cFiller.equals(headAktVal)) {
						// dieser wert kommt im filler vor
						fillerSet.add(headAktKey);
						// lc.log_ext("Filler identied: Pos="+(headAktKey+1)+", Header="+headAktVal, this);
					}
				}				 	

			 }
		}
		
		// count_base
		count_base = Commons.para2int(e.getAttributeValue("count_base"),-1,"count_base");
		
		// Element root initialisieren
		root = new Element(root_name);
		ExcelUtilXrob.setSheetInfo(root, sheet_name, sheet_index);
		
		 ExcelUtilXrob.appendTableRowInit ( root,row_name,row_index_name, headRow, style );
					
		 HashMap<Integer,String> aktRow;
		 HashMap<Integer,String> ultiRow = null;
		 
		 int index_offset = 0;
		 if ( count_base > -1 ) {
			 index_offset = count_base - y- 1;
			 // lc.log_ext("count_base="+count_base+", offset="+index_offset, this);
		 } else
			 index_offset = 1;
		 
		 int rounder = 0;
		 int rounder_limit = 17;
         for(int dataI=y+1; dataI<y2+1; dataI++){
        	 rounder++;
        	 if ( rounder > rounder_limit ) {
        		 rounder = 0;
        		 rh.pleaseRememberMe(this);
        	 }
        	 // lc.log_ext("Next Row #"+(dataI+1), this);
        	 aktRow = ExcelUtilPoi.getRow(sheet, dataI, x, x2, apply_date,date_format);
        	 
        	 if ( !has_filler);
        	 else if ( ultiRow == null ) {
        		 ultiRow = aktRow;
        	 } else if ( aktRow == null );
        	 else {
        		 // update
        		 Iterator<Integer> it = fillerSet.iterator();
        		 while (it.hasNext()) {
        			 Integer fillerIndex = (Integer)it.next();
        			 
        			 // pr�fen ob dieser index in aktRow nicht besetzt ist
        			 if ( !aktRow.containsKey(fillerIndex)) {
        				 // kopieren
        				 aktRow.put(fillerIndex, ultiRow.get(fillerIndex));
        			 }
        		 }
        		 ultiRow = aktRow;
        	 }
        	 if ( aktRow != null ) {
            	 int row_index = dataI+index_offset;

            	 ExcelUtilXrob.appendTableRow(aktRow, row_index, root,unique);        		 
        	 }

         }
         
         ExcelUtilXrob.appendRowClose();
         
		 // Dokument schreiben
		 Document doc = new Document(root);

		 CommonsVfs.writeToFile(doc, out,rh.getSitzung());
		 
		return null;
	}
		
	public String getExtensionId() {
		return ( tname);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
}