/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Vcar extends Object {
	
	private boolean dont_import = false;
	public void dontImport() {
		dont_import = true;
	}
	public boolean shouldBeImporte() {
		return (!dont_import);
	}
	/*
	private String getStringUnhide() {
		// return Entities.replaceEntitiesUnHide(s1);
		// return Entities.exportEntities(s1);
		return s1;
	}
	*/
	
	private String s1;
	public String s;
	public boolean isNum = false;
	public BigDecimal getNum ;
	
	public boolean isBool = false;
	private boolean getBool;
	
	public Vcar ( String _s ) {
		s1 = _s;
		calculate();
		// verify();
	}
	private static String myZERO = new String("0");
	private static String myONE = new String("1");
	
	private static BigDecimal myZERO_bD = new BigDecimal(0);
	private static BigDecimal myONE_bD = new BigDecimal(1);
	
	public Vcar ( BigDecimal _d ) {
		s1 = _d.toPlainString();
		s=s1;
		isNum = true;
		getNum = _d.add(BigDecimal.ZERO);
		if ( getNum.compareTo(BigDecimal.ONE)==0 ) {
			isBool = true;
			getBool = true;
			s=myONE;
		} else if ( getNum.compareTo(BigDecimal.ZERO)==0  ) {
			isBool = true;
			getBool = false;
			s=myZERO;
		} else {
			isBool = false;
		}
		// verify();

	}	
	
	public Vcar ( int _i ) {
		
		s1 = Integer.toString(_i);
		s=s1;
		isNum = true;
		getNum = new BigDecimal(_i);
		if ( getNum.compareTo(BigDecimal.ONE)==0  ) {
			isBool = true;
			getBool = true;
			s=myONE;
		} else if ( getNum.compareTo(BigDecimal.ZERO)==0  ) {
			isBool = true;
			getBool = false;
			s=myZERO;
		} else {
			isBool = false;
		}
		// verify();
		
	}		
	
	public Vcar ( boolean _b ) {

		if ( _b ) {
			s1 = myONE;
			getNum = myONE_bD;
			
		} else {
			s1 = myZERO;
			getNum = myZERO_bD;
		}
		s=s1;
		isNum = true;
		isBool = true;
		getBool = _b;
		// verify();
	}		
	
	public Vcar setString( String _s ) {
		s1 = _s;

		calculate();
		return this;
	}
	public Vcar ( ) {
		s1 = new String();
		calculate();
	}	


	
	public int getInt () throws Exception {
		if (!isNum)
			throw new Exception("Integer value expected, but found this: ["+s+"]");
		return getNum.intValue();
	}
	public BigDecimal getNumber () throws Exception {
		if (!isNum)
			throw new Exception("Numeric value expected, but found this: ["+s+"]");
		return getNum;
	}	
	public boolean getBool () throws Exception {
		if (!isBool) {
			throw new Exception("Boolean value expected, but found this: ["+s+"]");
		}
		return getBool;
	}	
	

	public void mustNumber() throws Exception {
		if (!isNum)
			throw new Exception("Numeric value expected, but found this: ["+s+"]");
	}
	public void mustBool() throws Exception {
		if (!isBool)
			throw new Exception("Boolean value expected, but found this: ["+s+"]");
	}	
	public void calculate() {

		s = s1;	
		getNum = getBigDecimalIfNumeric(s1);
		if ( getNum !=null ) {
			isNum = true;
			if ( getNum.compareTo(BigDecimal.ZERO)==0) {
				isBool = true;
				getBool = false;
			} else if ( getNum.compareTo(BigDecimal.ONE)==0) {
				isBool = true;
				getBool = true;
			} else
				isBool = false;
		} else {
			isNum = false;
			isBool = false;
		}
		
	}
	private void verifyReal() {
		
	}
	private void verifyxx() {
		s1 = s;

		boolean isNum_x = isNum;
		BigDecimal getNum_x = null;
		if ( getNum != null)
			getNum_x = getNum.add(BigDecimal.ZERO) ;
		else
			getNum_x = null;
		
		boolean isBool_x = isBool;
		boolean getBool_x = getBool;
		s1 = s;
		calculate();
		if ( isNum != isNum_x )
			JOptionPane.showMessageDialog(null, "verify-problem, isNum");
		if ( isNum ) {
			if ( getNum_x.compareTo(getNum) != 0 )
				JOptionPane.showMessageDialog(null, "verify-problem, getNum");
		}
		if ( isBool != isBool_x )
			JOptionPane.showMessageDialog(null, "verify-problem, isBool");
		if ( isBool ) {
			if ( getBool != getBool_x )
				JOptionPane.showMessageDialog(null, "verify-problem, getBool");		
		}
	}
	/*
	private static boolean isNumeric ( String check ) {
		try {
			double x = Double.parseDouble(check);
			// log_debug(check+ " is double");
			return true;
		} catch (NumberFormatException e ) {
			// log_debug(check+ " is NOT double");
			return false;
		}
	}
	*/
	
	private static BigDecimal getBigDecimalIfNumeric ( String check ) {
		try {
			return ( new BigDecimal (check));

		} catch (NumberFormatException e ) {
			// log_debug(check+ " is NOT double");
			return null;
		}
	}	
	
	public static String getParaList ( ArrayList<Vcar> vcar_list, String prolog, String poster ) {
		String result = "";
		Iterator<Vcar> i = vcar_list.iterator();
		while ( i.hasNext()) {
			Vcar x = (Vcar)i.next();
			result = result+x.s+",";
		}
		if ( result.endsWith(","))
			result = result.substring(0, result.length()-1);
		if ( prolog != null)
			result = prolog+result;
		if ( poster != null)
			result = result+poster;
		return result;
	}


}