// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.generator;


import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class Shuffler implements ITask {


	private IRequestHandler rh;
	
	private Element ein;
	private int wish;
	private String sheet;
	
	private boolean keep_data_original = false;
	@Override
	public Parameters execute(Element e, IRequestHandler _rh) throws Exception {
		// TODO Auto-generated method stub


		this.rh = _rh;
		
		// receive parameters: keep data original
		if ( e.getAttribute("keep_data_original") != null) {
			String p_keep_data_original = e.getAttributeValue("keep_data_original");
			if ( p_keep_data_original.equals("1"))
				keep_data_original = true;
			else if ( p_keep_data_original.equals("0"))
				keep_data_original = false;
			else
				throw new Exception("task shuffle_testdata the parameter-attriubte 'keep_data_original' is '"+
						p_keep_data_original +"' but it should be 1 or 0");						
		}
		
		// receive parameters: in
		if ( e.getAttribute("in") == null )
			throw new Exception("task shuffle_testdata the parameter-attriubte 'in' is empty "+
					" but it should contain a filename");

		ein = CommonsVfs.getElementByFilename(e.getAttributeValue("in"),rh.getSitzung());
		
		// receive parameters: wish
		boolean wishdefault = false;
		if ( e.getAttribute("wish") != null ) {
			String pwish = e.getAttributeValue("wish");
			if ( pwish.equals("")) {
				// taking the defaultvalue out of in-file
				wishdefault = true;
			} else {
				try {
					wish = Integer.parseInt(pwish);
				} catch (NumberFormatException ex ) {
					throw new Exception("task shuffle_testdata the parameter-attriubte 'wish' is empty '"+
							pwish+"' but it should contain a number or be empty at all. "+
							ex.getLocalizedMessage());
				}
			}
		} else
			wishdefault = true;
		if ( wishdefault ) {
			List<Element> list = Commons.xpathSelectNodes ( ein, "dims/dim_row/row" ); 
			wish = list.size() - 1;
			//lc.log_ext("Assuming '"+wish+"' to be your wish.", this);
					  
		}
		
		// receive parameters: out1
		if ( e.getAttribute("out1") == null )
			throw new Exception("task shuffle_testdata the parameter-attriubte 'out1' is empty "+
					" but it should contain a filename");
		
		// receive parameters: out2
		if ( e.getAttribute("out2") == null )
			throw new Exception("task shuffle_testdata the parameter-attriubte 'out2' is empty "+
					" but it should contain a filename");
		
		// receive parameters: out2
		if ( e.getAttribute("sheet") == null )
			throw new Exception("task shuffle_testdata the parameter-attriubte 'sheet' is empty "+
					" but it should contain a sheetname");
		sheet = e.getAttributeValue("sheet");
		
		// Printer out 1
		// File out1_fPrinter = new File(e.getAttributeValue("out1"));
		
	
		OutputStreamRequest os1r = CommonsVfs.getOutputStreamByFilenameP1(
				e.getAttributeValue("out1"), false,rh.getSitzung());
		OutputStream os1 = os1r.getOutputStream();
		// FileOutputStream out1_fos = new FileOutputStream(out1_fPrinter,false);
		PrintStream p1 = new PrintStream(os1,false,"UTF-8");
		
		// Printer out 2
		OutputStreamRequest os2r = CommonsVfs.getOutputStreamByFilenameP1(
				e.getAttributeValue("out2"), false,rh.getSitzung());		
		OutputStream os2 = os2r.getOutputStream();	
		// File out2_fPrinter = new File(e.getAttributeValue("out2"));
		// FileOutputStream out2_fos = new FileOutputStream(out2_fPrinter,false);
		PrintStream p2 = new PrintStream(os2,false,"UTF-8");	
		
		// preperation 1
		p1.println("<?xml version=\"1.0\" ?>");
		p1.println("<dataset>");
		
		// writing everything than the values
		Element ezwei = ein.clone();
		List<Element> del = Commons.xpathSelectNodes ( ezwei, "coltype/values" );
		
		Iterator<Element> foundIterator = del.iterator();
		while ( foundIterator.hasNext() ) {
			Element foundWhere = (Element)foundIterator.next();
			foundWhere.removeContent();
		}
		p1.println(Commons.toText( ezwei ));
		p1.println("<rows nbr_rows=\""+wish+"\">");
		
		// preperations 2 
		p2.println("<?xml version=\"1.0\" ?>");
		p2.println("<root nbr_rows=\""+wish+"\" sheet_name=\""+Commons.escapeXml(sheet)+"\">");
		p2.println("");			
		
		// preparing object per column
		int nbr_cols = Commons.string2int(ein.getAttributeValue("nbr_cols"));
		Col[] cols = new Col[nbr_cols];
		int j = 0;
		while ( j < nbr_cols ) {
			Col ncol = new Col(
					Commons.xpathSelectSingleNode(ein, "coltype[@col_index="+j+"]"),
						keep_data_original,
						p1,
						p2,
						this);
			cols[j] = ncol;
			j++;			
		}
		
		// creating data, loop per wished record
		int i = 0;
		while ( i < wish ) {
			i++;
			if ((i % 100) == 0)
				rh.terminalProgress("Creating case "+i+" of "+wish, this);
			
			// headers for printers
			p1.println("<r ri=\""+i+"\">");
			p2.println("<row ");
			
			// loop around the cols
			j = 0;
			while ( j < nbr_cols ) {
				cols[j].next(j);
				j++;			
			}
			
			// trailers for printers
			p1.println("</r>");
			p2.println(" />");			
		}
		
		
		p1.print("</rows></dataset>");
		p2.print("</root>");			
		

		p1.close();
		try { os1.close(); } catch (Exception ex) {
			rh.printTrace("failed to close outputstream os1 by shuffler:"+ex.getMessage()); }		
		CommonsVfs.finishOutputStreamP2(e.getAttributeValue("out1"), rh.getSitzung(),os1r.getOpnum());
		
		p2.close();
		try { os2.close(); } catch (Exception ex) {
			rh.printTrace("failed to close outputstream os2 by shuffler:"+ex.getMessage()); }		
		CommonsVfs.finishOutputStreamP2(e.getAttributeValue("out2"), rh.getSitzung(),os2r.getOpnum());		
		return null;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		String prodir ="E:\\_LOXODROM\\ava-internals\\workspaces\\ws.Ava\\app.Test-Data-Generator\\pro.0511-0707_[Lnv-Wn]\\"; 
		prodir ="E:\\_LOXODROM\\ava-internals\\workspaces\\ws.Ava\\app.Test-Data-Generator\\pro.TC-A-2\\";
		Element e = new Element("shuffle");
		e.setAttribute("wish", "10");
		e.setAttribute("keep_data_original", "1");
		e.setAttribute("in", prodir+"WorkingData\\excel-analysis.xml");
		e.setAttribute("out1", "C:\\Users\\Developer\\Documents\\tdg-output\\out1.xml");
		e.setAttribute("out2", "C:\\Users\\Developer\\Documents\\tdg-output\\out2.xml");
		e.setAttribute("sheet", "parlament candidates");
		
		
		ShuffleTestdata s = new ShuffleTestdata();
		try {
			s.staticTaskdef(null, "shuffle", new RequestHandlerDummy());
			s.execute(e, new RequestHandlerDummy());
			System.out.println("done");
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		*/

	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
	
		
	}

}