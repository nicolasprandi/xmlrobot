/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Expression {
	private ArrayList<Elem> eList = new ArrayList<Elem>();
	
	private LogConnector lc = new LogConnector("");
	private String 		 n = new String("");
	
	
	private String myx;
	
	// ************************* Expression ********************************************
	/*
	 * 27.11.2012, core_expr_done
	 *             Expression sollte jetzt nur noch von translate2 aufgerufen werden
	 *             Die bisherigen aufrufe sind herausgenommen und mit #DONTO gekennzeichnet
	 *             Betriffene Classen
	 *             - Dir
	 *             - TryTask
	 *             - Interpreter, info
	 *             - Filee
	 *             
	 *             Ausnahme:
	 *             LoopWhile, hasNext
	 *             Elem.setFunction
	 */
	public Expression ( String x ) throws Exception {
		 myx = x;	
		 // lc.core(n + "--> Starting up: "+x,LogConnector.CORE_EXPRESSIONS);
		 int pos = 0;
		 int len = x.length();
		 
		 while ( pos < len ) {
			 int nextFun = x.indexOf("(f,",pos);

			 if ( nextFun == -1 ) {
				 /* no function found, starting with (f, */
				 if ( x.indexOf("(fx,",pos) > -1 )
					 throw new LangException(200,"fx function-variation not implemented yet.");
				 addText(x.substring(pos));
				 pos = len;
			 } else {
				 /* found a (f, starting on nextFun */
				 if (nextFun > pos ) addText(x.substring(pos, nextFun));
				 int closing = getClosingBracket(x, nextFun+1);
				 if ( closing == -1 ) 
					 throw new LangException(201,"Found no closing-bracket for:"+x.substring(nextFun));
				 addFunction(x.substring(nextFun, closing+1));
				 pos = closing+1;
			 }
		 }
	}
	
	// ************************* evaluate ********************************************	
	
	public Vcar evaluate ( IFunEvalHandler eh ) throws Exception {
		String res = new String();
		Iterator<Elem> i1 = eList.iterator();
		while ( i1.hasNext()) {
			Elem elem = (Elem)i1.next();
			if ( elem.isText) {
				// its a text
				String vorher = new String ( elem.text);

				res = res + vorher; // hier stand mal nachher
			} else if ( elem.isFunction ) {
				ArrayList<Vcar> pList = new ArrayList<Vcar>();
				
				ArrayList<Expression> peList = elem.getParaExpressionList();
				
				Iterator<Expression> i2 = peList.iterator();
				while ( i2.hasNext()) {
					Expression expr = (Expression)i2.next();
					Vcar dazu = expr.evaluate(eh);
					//dazu.s = Expression.replaceEntities(dazu.s); 
					//dazu.calculate();
					pList.add(dazu);
				}
				Vcar fun_result = eh.executeFunction(elem.function_name, pList);
				res = res + fun_result.s;
			} else {
				res = res + "[ERROR: unknow Element type in function]";
			}
		}
		
		// lc.core(n + "------> Result : "+res+" of "+myx,LogConnector.CORE_EXPRESSIONS);
		// lc.debug(n + "------> Result : "+res+" of "+myx);
		
		return new Vcar(res);
		
	}		
	
	public void trace(int reportlevel ) {
		
		int next_reportlevel = reportlevel + 1;
		Iterator<Elem> j = eList.iterator();
		while ( j.hasNext()) {
			Elem ce = (Elem)j.next();
			ce.trace(next_reportlevel);
			// lc.trace("para: "+(String)j.next());
		}
	}
	
	private void addText( String x ) {
		// lc.core("-addText('"+x+"')",LogConnector.CORE_EXPRESSIONS);
		// lc.core(n + "    addText: "+x,LogConnector.CORE_EXPRESSIONS);
		Elem e = new Elem();
		e.setText(x);
		eList.add(e);
	}
	
	private void addFunction( String x ) throws Exception {
		// lc.core("-addFunction('"+x+"')",LogConnector.CORE_EXPRESSIONS);
		// lc.core(n + "     addFun: "+x,LogConnector.CORE_EXPRESSIONS);
		Elem e = new Elem();
		e.setFunction(x);
		eList.add(e);
	}	
	
	


	
	// ************************* getClosingQuote ********************************************
	static public int getClosingQuote ( String x, int fromIndex, boolean hardEvaluation  ) throws Exception {
		//  private static LogConnector lcs = new LogConnector("xre.Expression.getClosingQuote ("+x+","+fromIndex+")");

		String y = x.substring(fromIndex,fromIndex+1);
	
		if ( hardEvaluation ) {
			if ( !y.equals("'") ) {
				throw new Exception("on position "+fromIndex+" is no ', an "+y+" was found!");					
			}
		}
		
		// search for '
		int found;
		found = x.indexOf("'", fromIndex+1);
		if ( found == -1 )
			return -1;
		else {
			return ( found );
		}
			

	}

	// ************************* getNextEnding ********************************************
	
	static public int getNextEnding(String x, int fromIndex ) {
		
		int takeOff = fromIndex;
		
		int z = getNextEndingSmall(x,takeOff);
		
		while (z > -1) {
			/* check the orrunces of quotes before */
			int ocu = getOccurences(x, "'", fromIndex, z);
			if ( ocu%2 == 0 ) {
				// alles ok
				return z;
			}
			takeOff++;
			z = getNextEndingSmall(x,takeOff);
		}		
		return -1;		
	}
	
	static private int getNextEndingSmall(String x, int fromIndex ) {
		int foundBracket = x.indexOf(")",fromIndex);
		int foundComma = x.indexOf(",",fromIndex);
		if ( foundBracket == -1 && foundComma!=-1) return foundComma;
		else if ( foundBracket != -1 && foundComma==-1) return foundBracket;
		else if ( foundBracket == -1 && foundComma==-1) return foundBracket;
		int foundSmaller = foundComma;
		if ( foundBracket < foundSmaller )
			foundSmaller = foundBracket;
		return foundSmaller;
		
	}
		
	
	static public int getOccurences ( String searchIn, String searchFor ) {
		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1){

		       lastIndex = searchIn.indexOf(searchFor,lastIndex);

		       if( lastIndex != -1){
		             count ++;
		      }
		}
		return count;
	}
	
	static public int getOccurences ( String searchIn, String searchFor, int beginIndex, int endIndex ) {
		String _searchIn = searchIn.substring(beginIndex, endIndex);
		return getOccurences(_searchIn,searchFor);
	}	


	// ************************* getClosingBracket ********************************************	
	static public int getClosingBracket ( String x, int fromIndex) {
		int depth = 0;
		
		int position = fromIndex;
		String enc;
		while (position < x.length()) {
			enc = x.substring(position, position+1);
			
			if (enc.equals("(") ) depth++;
			else if (enc.equals(")")  && depth==0) return position;
			else if (enc.equals(")") && depth>0 ) depth--;
			
			position++;
		}
		return -1;		
		
	}
	// ************************* getClosingBracket2 ********************************************
	/*
	static public int getClosingBracket2 ( String x, int fromIndex) {
		int depth = 0;
		//int found = -1;
		boolean visible = true;
		
		int position = fromIndex;
		String enc;
		while (position < x.length()) {
			enc = x.substring(position, position+1);
			
			if (enc.equals("(") && visible ) depth++;
			else if (enc.equals(")") && visible && depth==0) return position;
			else if (enc.equals("'")) visible=!visible;
			else if (enc.equals(")") && visible && depth>0 ) depth--;
			
			position++;
		}
		return -1;		
		
	}	
	*/
}