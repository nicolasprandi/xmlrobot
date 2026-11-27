/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.text.DateFormat;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Excel_Form implements ITask{

	String 	excel;
	int    	sheet_index_orig;
	String	sheet_name;
	int    	sheet_index;
	int		x;
	int		y;
	int		y2;
	String  root_name;
	int 	style;
	String out;



	
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {

	

		ExcelUtilXrob.setFromTask("excel_form"); 
		boolean apply_date = !Commons.hasFlag(e, "no_dates");

		DateFormat date_format = Excel_Table.getDateFormat(e);

		// Sheet - Parameter einlesen
		excel = 	Commons.para2string(e.getAttributeValue("excel"),null,"excel");
		sheet_index_orig = Commons.para2int(e.getAttributeValue("sheet_index"),1,"sheet_index");
		sheet_name = Commons.para2string(e.getAttributeValue("sheet"),null,"sheet");

		// File excelFile = new File(excel);
		
		// Sheet - Physisch einlesen
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
			throw new Exception("excel_form: x,y depressive, use x1,y1");
		
		x = 		Commons.para2int(e.getAttributeValue("x1"),1,"x1");
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
		y2--;
		
		root_name = Commons.para2string(e.getAttributeValue("root_name"),"root","root_name");
		style =ExcelUtilXrob.style2int(e.getAttributeValue("style"));
		out = 		Commons.para2string(e.getAttributeValue("out"),null,"out");
		
		Unique unique = null;
		boolean keep_names = Commons.hasFlag(e, "keep_names");
		if ( !keep_names ) 
			unique = new Unique("xml_attribute");
		
		// root_name correction
		if ( style == ExcelUtilXrob.style_excel_update)
			root_name = "rows";
		
		// Ausgabe JDOM initialisieren
		Element  root = new Element(root_name);
		if ( style != ExcelUtilXrob.style_short)
			ExcelUtilXrob.setSheetInfo(root, sheet_name, sheet_index);

		
		// Loopen
		ExcelUtilXrob.appendFieldInit (  style );
			
		HashMap<Integer,String> aktRow;
		 
        for(int dataI=y; dataI<(y2+1); dataI++){
        	 //lc.log_ext("Next Row", this);
        	 aktRow = ExcelUtilPoi.getRow(sheet, dataI, x, x+1,apply_date,date_format);
        	 if ( aktRow != null) {
	        	 // auf aktRow rumturnen
	        	 String myheader;
	        	 try {
					myheader = (String)aktRow.get(x);
	        	 } catch (Exception e1) {
					myheader=null;
	        	 }
	        	 if ( myheader == null ) myheader=""; 
	        	 // Sp.p("Doing FormRow,"+dataI+","+myheader);
        		 if ( !keep_names ) {

    		        	 String newheader = unique.getValue(myheader);
    		        
    		        	 if ( !myheader.equals(newheader)) {
    		        		 // 
    		        		 aktRow.put(x, newheader);
    		        		 //lc.log_ext("Correcting name from "+myheader+" to "+newheader, this);
    		        	 }        			 
        		 }

	        	 
	        	ExcelUtilXrob.appendFormField(aktRow, root,x,x+1);
        	 }
        }
         
        ExcelUtilXrob.appendFieldClose();
		 
		// Dokument schreiben
		Document doc = new Document(root);


		CommonsVfs.writeToFile(doc, out,rh.getSitzung());
		
		
		 
		return null;
	}

	public String getExtensionId() {
		return ( null);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}