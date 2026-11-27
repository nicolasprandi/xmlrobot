/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Excel_Update implements  ITask{


	String tname;
		
	private static String cell_element = "c"; 
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "excel_update";

		ExcelUtilXrob.setFromTask("excel_update");
		// Sheet - Parameter einlesen
		String what = 	Commons.para2string(e.getAttributeValue("what"),null,"what");
		String out = 		Commons.para2string(e.getAttributeValue("out"),null,"out");
		String how = 		Commons.para2string(e.getAttributeValue("how"),null,"how");
		String with = 		Commons.para2string(e.getAttributeValue("with"),null,"with");
		String sheet_name= Commons.para2string(e.getAttributeValue("sheet_name"),null,"sheet_name");
		String remove_sheet = Commons.para2string(e.getAttributeValue("remove_sheet"),null,"remove_sheet");
		// String encoding = 		Commons.para2string(e.getAttributeValue("encoding"),null,"encoding",lc,this);
		
		// Sheet - Physisch einlesen
		if ( out == null ) {
			// writing to the same back
			// throw new Exception(this.tname+": out-parameter must be specified !");
		}
		Object wb = ExcelUtilPoi.getWorkbook(what,rh.getSitzung());
		if ( how.equals("add")) {
			if ( remove_sheet != null ) {
				boolean done = ExcelUtilPoi.removeSheet(wb, remove_sheet);
				if ( done == false ) {
					//lc.log_ext("Could not remove sheet '"+remove_sheet+"'", this);
				}
			}
			if ( sheet_name == null )
				throw new Exception("excel_update needs attribute 'sheet_name'");
			Object sheet = ExcelUtilPoi.addSheet(wb, sheet_name);
			Element root = CommonsVfs.getElementByFilename(with,rh.getSitzung());
			// Loop for row
			int row_id = 0;
			List<Element> l_rows = root.getChildren("row");
			Iterator<Element> i_rows = l_rows.iterator();
			
			while ( i_rows.hasNext()) {
				
				Element aktRow = (Element)i_rows.next();				
				HashMap<Integer,String> newRow = row2Cells ( aktRow );				
				Object row = ExcelUtilPoi.createRow(sheet, row_id);				
				ExcelUtilPoi.setCells ( row, newRow );				
				row_id++;
				
			}
		} else if (how.equals("append")) {
			throw new Exception( tname+", how 'append' depressive, use 'add'");
		}
		
		
		// und rausschreiben
		if ( out == null )
			out = what;

		try {
			ExcelUtilPoi.writeWorkbook(wb, out,rh.getSitzung());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.pe(e1);
			Sp.p("Writing workbook: "+e1.getLocalizedMessage());
			throw e1;
		}
		 
		return null;
	}

	private HashMap<Integer,String> row2Cells ( Element row ) throws Exception {
		HashMap<Integer,String> ret = new HashMap<Integer,String>();
		int next_cell_id = 0;
		
		List<Element> l_cells = row.getChildren(cell_element);
		Iterator<Element> i_cells = l_cells.iterator();
		
		while ( i_cells.hasNext()) {
			
			Element aktCell = (Element)i_cells.next();	
			if ( aktCell.getAttribute("format") != null )
				throw new Exception("Attribute 'format' for a cell is forbidden: "+Commons.toText(aktCell));
			ret.put(next_cell_id, aktCell.getText());
			next_cell_id++;
			
		}
		
		return ret;
	}
	
	private HashMap<Integer,String> line2Cells ( String line ) {
		HashMap<Integer,String> ret = new HashMap<Integer,String>();
		int line_length = line.length();
		boolean quoted = false;
		String coll = new String("");
		int next_cell_id = 0;
		for (int i = 0; i < line_length; i++) {
			String cs = line.substring(i, i+1);
			// System.out.print(" ["+cs+"]");
			
			if ( (cs.equals(";") && !quoted) ) {
				// System.out.print("A=Field Separator ");
				ret.put(next_cell_id, coll);
				next_cell_id++;
				coll = new String("");
			} else if ( cs.equals(";") && quoted ) {
				// System.out.print("B");
				coll = coll.concat(cs);
			} else if ( cs.equals("\"") ) {
				// System.out.print("C");
				// ein hochkomma 
				if ( coll.equals("")) {
					// am anfang
					quoted = true;
					// System.out.print("D=Field Starter ");
				} else if ( quoted ) {
					// System.out.print("E");
					// was ist das naechste zeichen
					if ( line_length >= i+2 ) {
						// System.out.print("F");
						if ( line.substring(i+1,i+2).equals("\"")  ) {
							// System.out.print("G");
							// n�chstes zeichen auch ein hochkomma
							// und das ganze im quoted
							coll = coll.concat("\"");
							i++;
						} else {
							// System.out.print("H=Field Closer ");
							quoted = false;
						}
					} else {
						// System.out.print("I");
						quoted = false;
					}
				}
			} else {
				// System.out.print("J=Content Character ");
				coll = coll.concat(cs);
			}
			
			
			
		} // end for
		
		if ( !coll.equals(""))
			ret.put(next_cell_id, coll);
		
		return ret;
	}
	
	public String getExtensionId() {
		return ( tname);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}