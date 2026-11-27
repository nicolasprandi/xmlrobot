// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.generator;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;


public class Col {
	int position= -1;
	int max_row;
	boolean keep_data_original;
	PrintStream p1;
	PrintStream p2;
	String[] vals;
	String name;
	public Col(Element x, boolean keep_data_original,
			PrintStream p1,PrintStream p2,
			Shuffler stdg) {
		// TODO Auto-generated constructor stub
		Element y = Commons.xpathSelectSingleNode(x, "values/value[position()=last()]");
		if ( y== null ) 
			max_row = 0;
		else
			max_row = Commons.string2int(y.getAttributeValue("row_index"));
		this.keep_data_original = keep_data_original;
		this.p1 = p1;
		this.p2 = p2;
		vals = new String[max_row+1];
		List<Element> val = Commons.xpathSelectNodes ( x, "values/value" );
		name = x.getAttributeValue("name");
		//lc.log_ext(name+ ", max_row="+max_row, stdg);
		Iterator<Element> foundIterator = val.iterator();
		
		while ( foundIterator.hasNext() ) {
			Element r = (Element)foundIterator.next();
			int k = Commons.string2int(r.getAttributeValue("row_index"));
			vals[k] = new String ( r.getText());
		}
		
		/*
		if ( lc.debugIsOn() ) {
			for ( String f: vals ) {
				lc.log_ext("     val="+f, stdg);
			}
		}
		*/
		
	}
	
	public void next(int j) {
		position++;
		boolean good = true;
		if ( position > max_row) {
			if (keep_data_original) 
				good = false;
			else
				position = 0;
		}
		if ( good ) {
			String value = vals[position];
			if ( value != null ) {
				String val2 = Commons.escapeXml(value);
				p1.println("<v ci=\""+j+"\" x=\""+val2+"\"/>");
				p2.println(name+"=\""+val2+"\" ");					
			}
		
		}

	}

}