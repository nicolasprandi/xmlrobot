/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.text.DateFormat;
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
import at.nicolas.x.pub.Parameters;


/*
 * to do:
 * - default for y is getFirstRow
 */
public class Excel_Matrix implements ITask{

	String 	excel;
	int    	sheet_index_orig;
	String	sheet_name;
	int    	sheet_index;
	int		x;
	int		y;
	int		x2;
	int		y2;
	String  root_name;
	int		count_base;
	int 	style;
	String out;


	
	HashMap<Integer,String> headRow = null;
	Element  root = null;

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {

		
		style =ExcelUtilXrob.style2int(e.getAttributeValue("style"));
		if ( style!= ExcelUtilXrob.style_standard && style!= ExcelUtilXrob.style_excel_update ) {
			throw new Exception("excel_matrix  allows only the styles: standard or excel_update; but it is: "+e.getAttributeValue("style"));
		}
		boolean apply_date = !Commons.hasFlag(e, "no_dates");
		DateFormat date_format = Excel_Table.getDateFormat(e);
		// Sheet - Parameter einlesen
		excel = 	Commons.para2string(e.getAttributeValue("excel"),null,"excel");
		sheet_index_orig = Commons.para2int(e.getAttributeValue("sheet_index"),1,"sheet_index");
		sheet_name = Commons.para2string(e.getAttributeValue("sheet"),null,"sheet");

		// File excelFile = new File(excel);
	
		// Sheet - Physisch einlesen
		// HSSFWorkbook wb = (HSSFWorkbook) ExcelUtilPoi.getWorkbook(excel);
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
			throw new Exception( "excel_matrix: x,y depressive, use x1,y1");
		
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
		
		// Root - Einlesen
		String default_root_name = "root";
		if ( style == ExcelUtilXrob.style_excel_update)
			default_root_name = "rows";
		root_name = Commons.para2string(e.getAttributeValue("root_name"),default_root_name,"root_name");
		
		// Root - Initalisieren
		root = new Element(root_name);
		ExcelUtilXrob.setSheetInfo(root, sheet_name, sheet_index);
		
		Element cells = new Element ("cells");
		Element cols = new Element ("dim_col");
		Element rows = new Element ("dim_row");
		root.addContent(cells);
		root.addContent(cols);
		root.addContent(rows);
		
		// Countbase - Einlesen
		count_base = Commons.para2int(e.getAttributeValue("count_base"),-1,"count_base");
		
		// Absolute - Initalisieren, Offset
		int offset_rows = 0;
		int offset_cols = 0;
		if ( count_base > -1 ) {
			offset_rows = count_base - y;
			offset_cols = count_base - x;
		} else {
			offset_cols++;
			offset_rows++;
		}
		
		// Out - Einlesen		
		out = 		Commons.para2string(e.getAttributeValue("out"),null,"out");
				
		// Loopen		
		HashSet<Integer> col_set = new HashSet<Integer>();
		
		ExcelUtilXrob.appendCellsInit ( offset_rows, offset_cols, style );
					
		HashMap<Integer,String> aktRow;
	
		if ( y > 0 ) {
			for ( int ir=0;ir<y;ir++) {
				Element eAdding = new Element("row");
				root.addContent(eAdding);
				
			}
		}
		for (int dataI=y; dataI<(y2+1); dataI++){
			// lc.trace("---Next Row of Matrix---", this);
			aktRow = ExcelUtilPoi.getRow(sheet, dataI, x, x2, apply_date,date_format);
			if ( aktRow != null ) {
				Element eRow = new Element("row");
				eRow.setAttribute("row", Commons.int2String(dataI+offset_rows));
				rows.addContent(eRow);
				 
				Set<Integer> keySet = aktRow.keySet();
				Iterator<Integer> i = keySet.iterator();
				Integer   aktKey = null;
				 	
				while ( i.hasNext() ) {
					aktKey = (Integer)i.next();
					if ( !col_set.contains(aktKey))
						col_set.add(aktKey);
				}
				ExcelUtilXrob.appendMatrixCells(aktRow, dataI, cells, root);				
			} else if ( style == ExcelUtilXrob.style_excel_update ) {
				Element eEmpty = new Element("row");
				root.addContent(eEmpty);
			}

		}
         
        ExcelUtilXrob.appendCellsClose();
         
        Iterator<Integer> it = col_set.iterator();

        while(it.hasNext()) {
        	 Element eCol = new Element ("col");
        	 eCol.setAttribute("col", Commons.int2String((Integer)it.next()+offset_cols));
 			 cols.addContent(eCol);
        }
         
		// Dokument schreiben
		Document doc = new Document(root);
		


	
		
		CommonsVfs.writeToFile(doc, out,rh.getSitzung());
		 
		return null;
	}
		


	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
}