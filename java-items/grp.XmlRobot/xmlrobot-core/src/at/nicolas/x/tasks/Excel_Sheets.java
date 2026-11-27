/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Excel_Sheets implements ITask{

	String 	excel;
	String out;

	String tname;
	private static String says = new String("task 'excel_sheets' says: ");
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "excel_sheets";	

				
		excel = 	Commons.para2string(e.getAttributeValue("excel"),null,"excel");
		out = 		Commons.para2string(e.getAttributeValue("out"),null,"out");		

		if ( excel == null )
			throw new Exception(says+"attribute 'excel' is required."
							+" It has the filename of the excel to be processed. Found: "+Commons.toText(e));
		if ( out == null )
			throw new Exception(says+"attribute 'out' is required."
							+" It has the filename of the xml-file to be created. Found: "+Commons.toText(e));
		
		// File excelFile = new File(excel);
	
		// Ausgabe JDOM initialisieren
		Element  rootElement = new Element("sheets");
		
		// Excel Input initialisieren
		Object wb = ExcelUtilPoi.getWorkbook(excel,rh.getSitzung());
		int sheet_count = ExcelUtilPoi.getNumberOfSheets(wb);
		
		for(int i=0; i < sheet_count; i++){
			Element sheet = new Element("sheet");
			sheet.setAttribute("index",Integer.toString(i+1));
			sheet.setAttribute("name",ExcelUtilPoi.getSheetName((Object)wb, i));
			sheet.setText(ExcelUtilPoi.getSheetName((Object)wb, i));
			rootElement.addContent(sheet);
		}
		
		// Dokument schreiben
		Document doc = new Document(rootElement);

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