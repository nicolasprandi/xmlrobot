/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.ISitzung;


public class ExcelUtilPoi {
	
	/*
	 * 			getSheetName ( Workbook, Index)
	 
	public static String getSheetNameX ( HSSFWorkbook workbook, int sheetIndex) {
		return workbook.getSheetName(sheetIndex);
	}*/
	
	public static String getSheetName ( Object workbook, int sheetIndex) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String x = ( String )wbGetSheetName.invoke(workbook, new Object[]{sheetIndex});
		return x;
	}
	
	/*
	 * 			getWorkbook ( Filename )
	 * 	 
	public static HSSFWorkbook getWorkbookX ( String filename ) throws FileNotFoundException, IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		return wb;
	}
	*/	 
	
	public static Object getWorkbook ( String filename, ISitzung wbSitzung ) throws   Exception {

		FileObject fo_excel = CommonsVfs.resolveFileSilent(filename);

		if ( CommonsVfs.getTimeOfFileObject(fo_excel,wbSitzung) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}	
		
		Object wb;
		if ( filename.endsWith("x")) {
			// InputStream is = new FileInputStream(filename);
			// File f = new File(filename);
			InputStream fis = CommonsVfs.getInputStreamByFilename(filename,wbSitzung);
			//FileInputStream fis = new FileInputStream(filename);
			/*
			Object fs;
			try { 
				fs = fsConstruct.newInstance(new Object[]{fis});
			} catch (java.lang.reflect.InvocationTargetException e) {
				throw new IOException("Problem in reading "+filename+". Make sure it has format [ Excel 97-2003 ]. Maybe convert by saving it under this format! ");
			}				*/
			wb = wbXSSFConstruct.newInstance(new Object[]{fis});
			fis.close();
		} else {
			InputStream fis = CommonsVfs.getInputStreamByFilename(filename,wbSitzung);
			Object fs;
			try { 
				fs = fsConstruct.newInstance(new Object[]{fis});
			} catch (java.lang.reflect.InvocationTargetException e) {
				throw new IOException("Problem in reading "+filename+". Make sure it has format [ Excel 97-2003 ]. Maybe convert by saving it under this format! ");
			}			
			wb = wbHSSFConstruct.newInstance(new Object[]{fs});
			fis.close();
		}
		return wb;
	}	
	
	/*
	 * 			getNumberOfSheets ( Workbook )
	 * 
	 
	public static int getNumberOfSheetsX ( HSSFWorkbook workbook) {
		return workbook.getNumberOfSheets();
	}	*/
		
	
	public static int getNumberOfSheets ( Object workbook) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		int x = ( Integer )wbGetNumberOfSheets.invoke(workbook);
		return x;
	}	
	
	/*
	 * 			getRowX ( Sheet, RowNumber, MinCellIndex, MaxCellIndex) => HashMap ( Integer:Index, String:Value)
	 
	public static HashMap<Integer,String> getRow ( HSSFSheet sheet, int rowIndex, int minCi, int maxCi, ILogConnector lc, Ixrt ixrt ) {
		HashMap<Integer,String> ret = new HashMap<Integer, String>();
		
		
 		lc.log_ext("Row #"+(rowIndex+1),ixrt);
 		
 		HSSFRow row = sheet.getRow(rowIndex);
 		if ( row == null ) {
 			lc.warn("Row #"+(rowIndex+1)+" does not exist.", ixrt);
 			return null;
 		}
 		
	 	// ueber Spalten loopen 
	 	Iterator<?> iCell = row.cellIterator();
	 	HSSFCell aCell = null;
	 	int indexCell = -1;

	 	
	 	boolean zumindest_ein_inhalt = false;
		while ( iCell.hasNext() ) {
			
			aCell = (HSSFCell)iCell.next();			
			indexCell = aCell.getColumnIndex();
			
		 	if ( ((maxCi==-1) || (indexCell <= maxCi )) &&
		 		 ((minCi==-1) || (indexCell >= minCi ))) {
		 		String inhalt = getCellValue(aCell);
			 	if ( inhalt !=null && inhalt.length()>0 ) {				 			 		
			 		zumindest_ein_inhalt = true;
			 		ret.put(indexCell, inhalt);
			 		lc.log_ext((indexCell+1)+" : "+inhalt, ixrt );
			 	}	
		 	}
		 }
	 	
		//
		if ( !zumindest_ein_inhalt )
		  ret = null;
		
		return ret;
		
		new Object[]{sheetIndex}
	}
*/
	public static HashMap<Integer,String> getRow ( Object sheet, 
			int rowIndex, int minCi, int maxCi
			, boolean apply_date,DateFormat df ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashMap<Integer,String> ret = new HashMap<Integer, String>();
		
		
 		//lc.log_ext("Row #"+(rowIndex+1),ixrt);

 		//HSSFRow row = sheet.getRow(rowIndex);	
 		Object row = shGetRow.invoke(sheet, new Object[]{rowIndex});

 		if ( row == null ) {
 			//lc.log_ext("Row #"+(rowIndex+1)+" does not exist.", ixrt);
 			return null;
 		}
 		
	 	// ueber Spalten loopen 
 		// Iterator<?> iCell = row.cellIterator();
	 	Iterator<?> iCell = (Iterator)roCellIterator.invoke(row);
	 	// HSSFCell aCell = null;
	 	Object aCell = null;
	 	int indexCell = -1;

	 	
	 	boolean zumindest_ein_inhalt = false;
		while ( iCell.hasNext() ) {
			
			aCell = iCell.next();			
			// indexCell = aCell.getColumnIndex();
			indexCell =  (Integer)ceGetColumnIndex.invoke(aCell) ;
			
		 	if ( ((maxCi==-1) || (indexCell <= maxCi )) &&
		 		 ((minCi==-1) || (indexCell >= minCi ))) {
		 		
		 		String inhalt = getCellValue(aCell, apply_date,df );
		 		
			 	if ( inhalt !=null && inhalt.length()>0 ) {				 			 		
			 		zumindest_ein_inhalt = true;
			 		ret.put(indexCell, inhalt);
			 		//lc.log_ext((indexCell+1)+" : "+inhalt, ixrt );
			 	}
		 	}
		 }
	 	
		//
		if ( !zumindest_ein_inhalt )
		  ret = null;
		
		return ret;
	}	
	/*
	 * 			getCellValue ( Cell ) => String
	
		public static String getCellValueX ( HSSFCell cell ) {
 		
 		String inhalt = null;
 		int ct = cell.getCellType();
 		if ( ct == HSSFCell.CELL_TYPE_BLANK);
 		else if ( ct == HSSFCell.CELL_TYPE_BOOLEAN) {
 			boolean x = cell.getBooleanCellValue();
 			if ( x )
 				inhalt = "1";
 			else
 				inhalt = "0";
 		} else if ( ct == HSSFCell.CELL_TYPE_ERROR ) {
 			byte x = cell.getErrorCellValue();
 			inhalt = Byte.toString(x);
 		} else if ( ct == HSSFCell.CELL_TYPE_FORMULA ) {
 			inhalt = cell.getCellFormula();
 		} else if ( ct == HSSFCell.CELL_TYPE_NUMERIC ) {
 			inhalt = Commons.double2string(cell.getNumericCellValue());
 		} else if ( ct == HSSFCell.CELL_TYPE_STRING ) {
 			inhalt = cell.getStringCellValue();
 		}
 		return inhalt;	
	}  new Object[]{sheetIndex}
	 */
	public static String getCellValue ( Object cell, boolean apply_date,
			DateFormat df ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
 		
 		String inhalt = null;
 		String date_format = null;
 		// int ct = cell.getCellType();
 		int ct = (Integer)ceGetCellType.invoke(cell);
 		// if ( ct == HSSFCell.CELL_TYPE_BLANK);
 		
 		ceCELL_TYPE_BLANK.setAccessible(true);
 		ceCELL_TYPE_BOOLEAN.setAccessible(true);
 		ceCELL_TYPE_ERROR.setAccessible(true);
 		ceCELL_TYPE_FORMULA.setAccessible(true);
 		ceCELL_TYPE_NUMERIC.setAccessible(true);
 		ceCELL_TYPE_STRING.setAccessible(true);
 		
 		if ( ct == ceCELL_TYPE_BLANK.getInt(null) ) ;
 		// else if ( ct == HSSFCell.CELL_TYPE_BOOLEAN) {
 		else if ( ct == ceCELL_TYPE_BOOLEAN.getInt(null)) {
 			// boolean x = cell.getBooleanCellValue();
 			boolean x = (Boolean)ceGetBooleanCellValue.invoke(cell);
 			if ( x )
 				inhalt = "1";
 			else
 				inhalt = "0";
 		// } else if ( ct == HSSFCell.CELL_TYPE_ERROR ) {
 		} else if ( ct == ceCELL_TYPE_ERROR.getInt(null) ) {
 			// byte x = cell.getErrorCellValue();
 			byte x = (Byte)ceGetErrorCellValue.invoke(cell);
 			inhalt = Byte.toString(x);
 		// } else if ( ct == HSSFCell.CELL_TYPE_FORMULA ) {
 		} else if ( ct == ceCELL_TYPE_FORMULA.getInt(null) ) {
 			// inhalt = cell.getCellFormula();
 			inhalt = (String)ceGetCellFormula.invoke(cell);
 		// } else if ( ct == HSSFCell.CELL_TYPE_NUMERIC ) {
 		} else if ( ct == ceCELL_TYPE_NUMERIC.getInt(null) ) {
 			if ( apply_date ) {
 				Boolean is_cell_date_formated = (Boolean)sdIsCellDateFormatted.invoke(null,cell);
 				if (is_cell_date_formated.booleanValue()){
 					// date
 					Date date = (Date)ceGetDateCellValue.invoke(cell);
 					inhalt = df.format(date);

 				} else {
 					inhalt = Commons.double2string((Double)ceGetNumericCellValue.invoke(cell));
 				}
 			} else {
 				inhalt = Commons.double2string((Double)ceGetNumericCellValue.invoke(cell));
 			}
 			// inhalt = Commons.double2string(cell.getNumericCellValue());
 			
 		// } else if ( ct == HSSFCell.CELL_TYPE_STRING ) {
 		} else if ( ct == ceCELL_TYPE_STRING.getInt(null) ) {
 			// inhalt = cell.getStringCellValue();
 			inhalt = (String)ceGetStringCellValue.invoke(cell);
 		}
 		return inhalt;	
	}
	/*
	 * 			getSheet ( Workbook, sheetIndex )
	 * 
		public static HSSFSheet getSheet ( HSSFWorkbook workbook, int sheetIndex ) {
			HSSFSheet x = workbook.getSheetAt(sheetIndex);
			return x;
		} new Object[]{sheetIndex}
	 */
	public static Object getSheet ( Object workbook, int sheetIndex ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// HSSFSheet x =  workbook.getSheetAt(sheetIndex);
		Object x = wbGetSheetAt.invoke(workbook, new Object[]{sheetIndex});
		return x;
	}
	
	/*
	 * 			getLastRowNum ( Sheet )
	 * 
	 * 	public static int getLastRowNum ( HSSFSheet sheet ) {
		int x = sheet.getLastRowNum();
		return x;
	 */
	public static int getLastRowNum ( Object sheet ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// int x = sheet.getLastRowNum();
		int x = (Integer)shGetLastRowNum.invoke(sheet);
		return x;
	}	
	
	/*
	 * 			getFirstRowNum ( Sheet )
	 	public static int getFirstRowNum ( HSSFSheet sheet ) {
		int x = sheet.getFirstRowNum();
		return x;
	}
	 */
	public static int getFirstRowNum ( Object sheet ) throws Exception {
		// int x = sheet.getFirstRowNum();
		if ( sheet == null )
			throw new Exception("das sheet ist null");
		Object oj = shGetFirstRowNum.invoke(sheet);
		if ( oj == null ) {
			Sp.p("pppppppppppppppppppppppppppppasdfpasd pppppppppppppp");
			throw new Exception("das ding ist null");
		}
		int x = (Integer)shGetFirstRowNum.invoke(sheet);
		return x;
	}		
	

	/*
	 * 
	 */
	private static ClassLoader cl;
	
	// Workbook
	private static Class cWorkbook;
	private static Class cHSSFWorkbook;
	private static Class cXSSFWorkbook;
	private static Method wbGetSheetName;
	private static Method wbGetNumberOfSheets;
	private static Method wbGetSheetAt;
	private static Method wbCreateSheet;
	private static Method wbCreateSheetName;
	private static Method wbWrite;
	private static Method wbRemoveSheetAt;
	
	private static Constructor wbHSSFConstruct;
	private static Constructor wbXSSFConstruct;

	
	
	// filesystem
	private static Class cFileSystem;
	private static Constructor fsConstruct;	
	
	// cell
	private static Class cCell;
	private static Method ceGetColumnIndex;
	private static Method ceGetCellType;	
	private static Method ceGetBooleanCellValue ;
	private static Method ceGetErrorCellValue ;
	private static Method ceGetCellFormula ;
	private static Method ceGetNumericCellValue ;
	private static Method ceGetStringCellValue;	
	private static Method ceSetCellValueString;	
	private static Method ceGetDateCellValue;

	private static Field  ceCELL_TYPE_BLANK;
	private static Field  ceCELL_TYPE_BOOLEAN;
	private static Field  ceCELL_TYPE_ERROR ;
	private static Field  ceCELL_TYPE_FORMULA ;
	private static Field  ceCELL_TYPE_NUMERIC ;
	private static Field  ceCELL_TYPE_STRING ;	
	
	// row
	private static  Class cRow;
	private static  Method roCellIterator;
	private static  Method roCreateCell;
	
	// sheet
	private static  Class cSheet;	
	private static  Method shGetRow;
	private static  Method shGetLastRowNum ;
	private static  Method shGetFirstRowNum;
	private static  Method shCreateRow;
	
	// DateUtil
	private static  Class cDateUtil;	
	private static  Method sdIsCellDateFormatted;

	
	public static void setup ( ClassLoader _cl) throws ClassNotFoundException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		Sp.p("*** ExcelUtil.setup ****");
		cl = _cl;
		
		URL res = cl.getResource(
	             "org/apache/poi/poifs/filesystem/POIFSFileSystem.class");
	String path = res.getPath();

	res = cl.getResource("org/apache/poi/POIXMLDocument.class");
	path = res.getPath();
	
	res = cl.getResource("org/apache/commons/collections4/ListValuedMap.class");
	path = res.getPath();
	
	res = cl.getResource("org/apache/xmlbeans/XmlObject.class");
	path = res.getPath();

	res = cl.getResource("org/apache/poi/hslf/usermodel/HSLFSlideShow.class");
	path = res.getPath();	


			// Workbook
			cWorkbook = cl.loadClass("org.apache.poi.ss.usermodel.Workbook");
			cHSSFWorkbook = cl.loadClass("org.apache.poi.hssf.usermodel.HSSFWorkbook");
			cXSSFWorkbook = cl.loadClass("org.apache.poi.xssf.usermodel.XSSFWorkbook");
			
			wbGetSheetName = cWorkbook.getMethod("getSheetName", int.class);
			wbGetNumberOfSheets = cWorkbook.getMethod("getNumberOfSheets");
			wbHSSFConstruct = cHSSFWorkbook.getConstructor(cl.loadClass("org.apache.poi.poifs.filesystem.POIFSFileSystem"));

			wbXSSFConstruct = cXSSFWorkbook.getConstructor(Class.forName("java.io.InputStream")); //cXSSFWorkbook.getConstructor(cl.loadClass("org.apache.poi.poifs.filesystem.POIFSFileSystem"));
			wbGetSheetAt = cWorkbook.getMethod("getSheetAt", int.class);

			wbCreateSheet = cWorkbook.getMethod("createSheet");
			wbCreateSheetName = cWorkbook.getMethod("createSheet", String.class);
			wbWrite = cWorkbook.getMethod("write", OutputStream.class);
			wbRemoveSheetAt = cWorkbook.getMethod("removeSheetAt", int.class);
			
			// FileSystem
			cFileSystem = cl.loadClass("org.apache.poi.poifs.filesystem.POIFSFileSystem");
			fsConstruct = cFileSystem.getConstructor(java.io.InputStream.class);
			
			// cell - ce

			cCell = cl.loadClass("org.apache.poi.ss.usermodel.Cell");
			
			 ceGetColumnIndex = cCell.getMethod("getColumnIndex");
			 ceGetCellType = cCell.getMethod("getCellType");		
			 ceGetBooleanCellValue = cCell.getMethod("getBooleanCellValue");
			 ceGetErrorCellValue = cCell.getMethod("getErrorCellValue");
			 ceGetCellFormula = cCell.getMethod("getCellFormula");
			 ceGetNumericCellValue = cCell.getMethod("getNumericCellValue");
			 ceGetStringCellValue = cCell.getMethod("getStringCellValue");
			 ceSetCellValueString = cCell.getMethod("setCellValue", String.class);
			  ceCELL_TYPE_BLANK = cCell.getField("CELL_TYPE_BLANK");
			  ceCELL_TYPE_BOOLEAN = cCell.getField("CELL_TYPE_BOOLEAN");
			  ceCELL_TYPE_ERROR = cCell.getField("CELL_TYPE_ERROR");
			  ceCELL_TYPE_FORMULA = cCell.getField("CELL_TYPE_FORMULA");
			  ceCELL_TYPE_NUMERIC = cCell.getField("CELL_TYPE_NUMERIC");
			  ceCELL_TYPE_STRING = cCell.getField("CELL_TYPE_STRING");
			  ceGetDateCellValue = cCell.getMethod("getDateCellValue");
			
			// Row -ro

			cRow = cl.loadClass("org.apache.poi.ss.usermodel.Row");
			roCellIterator = cRow.getMethod("cellIterator");
			roCreateCell = cRow.getMethod("createCell", int.class);
			
			// Sheet-sh

			cSheet = cl.loadClass("org.apache.poi.ss.usermodel.Sheet");		
			shGetRow = cSheet.getMethod("getRow", int.class);
			shGetLastRowNum = cSheet.getMethod("getLastRowNum");
			shGetFirstRowNum = cSheet.getMethod("getFirstRowNum");
			shCreateRow = cSheet.getMethod("createRow", int.class);
			
			// sd dateUtil
			cDateUtil = cl.loadClass("org.apache.poi.ss.usermodel.DateUtil");	

				sdIsCellDateFormatted = cDateUtil.getMethod("isCellDateFormatted",
						cl.loadClass("org.apache.poi.ss.usermodel.Cell"));




	}

	/*
	 * 			addSheet ( Workbook, Sheetname )
	 **/ 
	/* ALT
	public static HSSFSheet addSheetX ( HSSFWorkbook workbook, String sheetname ) {
		HSSFSheet neu = null;
		if ( sheetname != null )
			neu = workbook.createSheet(sheetname);
		else
			neu = workbook.createSheet();
		
		return neu;
	}	
	NEU 
	public static Sheet  addSheetX ( Workbook workbook, String sheetname ) {
		Sheet neu = null;
		if ( sheetname != null )
			neu = workbook.createSheet(sheetname);
		else
			neu = workbook.createSheet();
		
		return neu;
	}		*/
	public static Object addSheet ( Object workbook, String sheetname ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object neu = null;
		if ( sheetname != null )
			neu = wbCreateSheetName.invoke(workbook, new Object[]{sheetname});
		else
			neu = wbCreateSheet.invoke(workbook, new Object[]{});
		
		return neu;
	}	
	
	public static boolean removeSheet ( Object workbook, String sheetname ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		boolean done = false;
		int count = getNumberOfSheets(workbook);
		for ( int i=count-1;i>=0;i-- ) {
			String curSheetName = getSheetName(workbook, i);
			if ( curSheetName.equals(sheetname)) {
				wbRemoveSheetAt.invoke(workbook, new Object[]{i});
				i = 0;
			}
		}
		
		return done;
	}		
	
	
	/*
	public static HSSFRow createRow (HSSFSheet sheet, int index ) {
		HSSFRow ret = sheet.createRow(index);
		return ret;		
	}
	*/
	
	public static Object createRow (Object sheet, int index ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object ret = shCreateRow.invoke(sheet, new Object[]{index} );
		return ret;		
	}	
	
	/*
	public static void setCells ( HSSFRow row, HashMap<Integer,String> newRow ) {
		Set<Integer> keySet = newRow.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;
		String    aktVal = null;
		while ( i.hasNext() ) {
			aktKey = (Integer)i.next();
			aktVal = (String)newRow.get(aktKey);
			
			HSSFCell cell = row.createCell(aktKey);
			cell.setCellValue(aktVal);
			
		}
	}	
	*/
	
	public static void setCells ( Object row, HashMap<Integer,String> newRow ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Set<Integer> keySet = newRow.keySet();
		Iterator<Integer> i = keySet.iterator();
		Integer   aktKey = null;
		String    aktVal = null;
		while ( i.hasNext() ) {
			aktKey = (Integer)i.next();
			aktVal = (String)newRow.get(aktKey);
			
			Object cell = roCreateCell.invoke(row, new Object[]{aktKey} );
			ceSetCellValueString.invoke(cell, aktVal);
			
		}
	}		
	
	/*
	public static void writeWorkbook (HSSFWorkbook workbook, String filename ) throws IOException {
		
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();				
	}
	*/
	
	public static void writeWorkbook (Object workbook, String filename, ISitzung writeWbSitzung) throws Exception {
		OutputStream os = null;
		OutputStreamRequest osr = null;
		try {
			 osr=CommonsVfs.getOutputStreamByFilenameP1(filename,false,writeWbSitzung);
			 os = osr.getOutputStream();
			wbWrite.invoke(workbook, new Object[]{os} );
			//fileOut.flush();
			os.close();
		} catch (Exception e) {
			/*
			try {
				os.close();
			} catch (Exception e1) {
				Sp.pe(e1);
			}*/
			Sp.p("Problem in writing "+filename);
			Sp.pe(e);
			throw new Exception("Problem in writing '"+filename+"', Error= "+e.getLocalizedMessage());
		} finally {
			if ( os != null ) os.close();
			CommonsVfs.finishOutputStreamP2(filename, writeWbSitzung, osr.getOpnum());
		}
		//
		/*
		OutputStream fileOut = null;
		
		try {
			fileOut = Commons.getOutputStreamByFilename(filename,false);
			wbWrite.invoke(workbook, new Object[]{fileOut} );
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			Thread.sleep(300);
			Sp.p("Excel Update went to sleep for 300ms 1x");
			try {
				fileOut = Commons.getOutputStreamByFilename(filename,false);
				wbWrite.invoke(workbook, new Object[]{fileOut} );
				fileOut.flush();
				fileOut.close();
			} catch (Exception e1) {
				Thread.sleep(300);
				Sp.p("Excel Update went to sleep for 300ms 2x");
				try {
					fileOut = Commons.getOutputStreamByFilename(filename,false);
					wbWrite.invoke(workbook, new Object[]{fileOut} );
					fileOut.flush();
					fileOut.close();
				} catch (Exception e2) {
					Thread.sleep(300);
					Sp.p("Excel Update went to sleep for 300ms 3x");
					fileOut = Commons.getOutputStreamByFilename(filename,false);
					wbWrite.invoke(workbook, new Object[]{fileOut} );
					fileOut.flush();
					fileOut.close();					
				}
			}
		}
		*/
		
						
	}	
	
}