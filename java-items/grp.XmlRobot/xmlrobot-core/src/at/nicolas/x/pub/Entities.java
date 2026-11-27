/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;


public class Entities {
	/*
	private static final String entity_gen = new String("(e:");
	private static final String entity_ob  = new String("(e:o)");
	private static final String entity_cb  = new String("(e:c)");
	private static final String entity_co  = new String("(e:s)");
	// private static final String entity_pa  = new String("(entity:pa)");	
	private static final String entity_ob_val  = new String("(");
	private static final String entity_cb_val  = new String(")");
	private static final String entity_co_val  = new String(",");
	*/
	
	/*	
	public static String exportEntitiesOld ( String escapedString ) {
		String result = new String ( escapedString );
		
		try {
			
		} catch (Exception e) {
			Sp.pe(e);
		}
		if (  (result).indexOf(entity_gen) > -1 ) {

			result = stringReplace(result, entity_ob, "#werjuew#");
			result = stringReplace(result, entity_cb,"#wrijs#aid#");
			result = stringReplace(result, entity_co, "rifjr+ske#sad");
			
			result = stringReplace(result, "#werjuew#", entity_ob_val);
			result = stringReplace(result, "#wrijs#aid#", entity_cb_val);
			result = stringReplace(result, "rifjr+ske#sad", entity_co_val);
			

		}	

		return result;

	}
	
	public static String importEntitiesOld ( String usualString ) {
		String result = new String ( usualString );

		result = stringReplace(result, entity_ob_val, "#765372#");
		result = stringReplace(result, entity_cb_val, "#783292746#");
		result = stringReplace(result, entity_co_val, "#8947436#");
		
		result = stringReplace(result, "#765372#", entity_ob);
		result = stringReplace(result, "#783292746#", entity_cb);
		result = stringReplace(result, "#8947436#", entity_co);		

		return result;
	}
	*/
	
	public static boolean stringContainsEntityCharacter (String tocheck ) {
		
		if (    tocheck.indexOf("(") > -1 
			|| 	tocheck.indexOf(",") > -1
			|| 	tocheck.indexOf(")") > -1
				)
			return true;
		else
			return false;
	}	
	public static boolean stringContainsEntity (String tocheck ) {
		
		if (    tocheck.indexOf("(e:o)") > -1 
			|| 	tocheck.indexOf("(e:s)") > -1
			|| 	tocheck.indexOf("(e:c)") > -1
				)
			return true;
		else
			return false;
	}
	public static String importEntities ( String natural ) {
		if ( natural == null ) return null;
	    StringBuffer out=new StringBuffer("");
	    char[] chars=natural.toCharArray();
	    for(int i=0;i<chars.length;i++)
	    {
	      switch(chars[i]) { 
	        case 40:out.append("(e:o)"); break; 
	        case 41:out.append("(e:c)"); break; 
	        case 44:out.append("(e:s)"); break; 
	        default: out.append(chars[i]); break;
	      }
	    }

	    return out.toString();
	}	
	
	private final static int KLAMMER_AUF  = 40;
	private final static int KLAMMER_ZU   = 41;
	private final static int KOMMA        = 44;
	private final static int E            = 101;
	private final static int DOPPELPUNKT  = 58;
	private final static int O 		   = 111;
	private final static int C  		   = 99;
	private final static int S  		   = 115;	
	
	public static String exportEntities ( String escaped ) {
		if ( escaped == null ) return null;
	    StringBuffer out=new StringBuffer("");
	    char[] chars=escaped.toCharArray();
	    
	    int len = chars.length;
	   
	    for(int i=0;i<chars.length;i++)
	    {
	      switch(chars[i]) { 
	        case 40:{
	        		if ( len> i+4 ) {
	        			// e:
	        			if (   chars[i+1] == E
	        				&& chars[i+2] == DOPPELPUNKT ) {
	        				
	        				if (   chars[i+3] == O
	    	        			&& chars[i+4] == KLAMMER_ZU ) {
	        					// (e:o)
	        					out.append("(");
	        					i=i+4;
	        				} else if (   chars[i+3] == C
	    	        			&& chars[i+4] == KLAMMER_ZU ) {
	        					// (e:c)
	        					out.append(")");
	        					i=i+4;
		        			} else if (   chars[i+3] == S
	    	        			&& chars[i+4] == KLAMMER_ZU ) {
	        					// (e:c)
	        					out.append(",");
	        					i=i+4;
		        			} else
		        				out.append(chars[i]);
	        				
	        			} else {
	        				// no entity
	        				out.append(chars[i]);
	        			}
	        		} else {
	        			// not enought
	        			out.append(chars[i]);
	        		}
	        		break; } 

	        default: out.append(chars[i]); break;
	      }
	    }

	    return out.toString();
	}		
	
	
	public static Element exportEntitiesClone(Element escapedElement) throws Exception {
		Element e2 = escapedElement.clone();
		Entities.exportEntities(e2);
		return e2;
	}
	public static void exportEntities ( Element escapedElement )  {
		
		List<Attribute> attributeList = escapedElement.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(Entities.exportEntities(attribute.getValue()));	
		}


		List<Element> elementeList = escapedElement.getChildren();
		Iterator<Element> it2 = elementeList.iterator();
		while ( it2.hasNext()) {
			Element x = (Element)it2.next();
			exportEntities(x);
		}	

		if (  escapedElement.getChildren().size() == 0 ) {
			// there are no child elements
			String textvalue = escapedElement.getText();
			if ( textvalue != null && textvalue.length()>0 ) {
				escapedElement.setText(Entities.exportEntities(textvalue));
			}			
		}
	
	}	
	public static void exportEntitiesAttributesOnly ( Element escapedElement )  {
		
		List<Attribute> attributeList = escapedElement.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(Entities.exportEntities(attribute.getValue()));	
		}
	
	}	
	
	public static void importEntities ( Element usualElement )  {
		
		List<Attribute> attributeList = usualElement.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(Entities.importEntities(attribute.getValue()));	
		}


		List<Element> elementeList = usualElement.getChildren();
		Iterator<Element> it2 = elementeList.iterator();
		while ( it2.hasNext()) {
			Element x = (Element)it2.next();
			importEntities(x);
		}	

		if (  usualElement.getChildren().size() == 0 ) {
			// there are no child elements
			String textvalue = usualElement.getText();
			if ( textvalue != null && textvalue.length()>0 ) {
				usualElement.setText(Entities.importEntities(textvalue));
			}			
		}
		
	
	}		
	/*
	private static String stringReplace ( String source, String search, String replace ) {
		
	    StringBuffer result = new StringBuffer();
	    int len = search.length();
	    if(len == 0) {
	        return source; //verhindert Endlosschleife bei search.equals("");
	    }

	    int pos = 0; //position
	    int nPos;    //next position
	    do {
	        nPos = source.indexOf(search, pos);
	        if(nPos != -1) { //gefunden
	            result.append(source.substring(pos, nPos));
	            result.append(replace);
	            pos = nPos+len;
	        } else { //nicht gefunden
	            result.append(source.substring(pos)); //letzter abschnitt
	        }
	    } while(nPos!= -1);
		

	    return result.toString();
	}	
	*/
	/*
	public static String isValidExpression ( String w )  {
		String res1 = isValidExpressionV1(w);
		String res2 = isValidExpressionV2(w);
		if ( res1 == null && res2 == null)
			return null;
		else if ( res1 != null && res2 != null ) {
			if ( res1 == res2 )
				return res1;
			else
				return "Syntax Error, Different Message, V-old {"+res1+"} V-new {"+res2+"}";
		}
		else {
			return "Syntax Error BUT DIFFERENT!!!, V-old {"+res1+"} V-new {"+res2+"}";
		}
		
	}
	*/
	// public static String isValidExpressionV2 ( String w )  {
	public static String isValidExpression ( String w )  {
		//System.out.println("---- w="+w);
		char[] chars=w.toCharArray();
		int w_length = w.length();
		String ret = null;
	    for(int i=0;i<chars.length;i++)
	    {
	      switch(chars[i])
	      {
	        case 40: {
	        	// klammer auf, expecting an entity
	        	if ( w_length < i+4 ) 
	        		// not enough characters for entity
					return ("found '(' at position '"+i+"' message 'no corresponding closing bracket' [ in ] "+w+ " [ ...... at ..... ] "+w.substring(i));
      	
	        	String check = w.substring(i,i+5);
	        	
				if ( !check.equals("(e:o)") &&
					 !check.equals("(e:c)") &&
					 !check.equals("(e:s)")) 
				 return("found '(' at position '"+i+"' message 'not followed up by entity' [ in ] "+w+ " [ ...... at ......] "+w.substring(i));
							
				i = i + 4;
				
				break;
	        }
	        case 41: {
	        	// klammer zu
				ret = "found ')' at position '"+i+"' message='no corresponding opening bracket' [ in ] "+w + " [ ...... at .... ] "+w.substring(i);
				break;

	        }
	        case 44: {
	        	// komma
				ret = "found ',' [ in ] "+w +" [ ...... at ...... ] "+w.substring(i);
				break;
	        }

	      }
	      
	    }
	    return ret;
	}	
	/*
	public static String isValidExpressionV1 ( String w )  {
		
		if ( w.indexOf(",") >-1 )
			return ("found ',' [ in ] "+w +" [ ...... at ...... ] "+w.substring(w.indexOf(",")));

		int pos = 0;

		while ( pos < w.length()) {
			int klammerauf = w.indexOf("(",pos);
						
			if ( klammerauf == -1 ) {
				int klammerzu = w.indexOf(")",pos);
				if (klammerzu == -1)
					break;
				else
					return "found ')' [ in ] "+w + " [ ...... at .... ] "+w.substring(pos);
			} 
			
			String check=null;
			try {
				check = w.substring(klammerauf,klammerauf+5);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return ("found '(' without closing bracket [ in ] "+w+ " [ ...... at ..... ] "+w.substring(klammerauf));
			}
		
			if ( check.equals("(e:o)") ||
				 check.equals("(e:c)") ||
				 check.equals("(e:s)")) {
				// ok				
			} else {
				return("found '(' not followed up by entity [ in ] "+w+ " [ ...... at ......] "+w.substring(klammerauf));
			}	
			pos = klammerauf + 5;
		} // ende der while schleife	
		
		return null;
	}
	public static void main(String[] args) throws Exception {
		String test;
		test="(e:o)"; Sp.p(test + " <=> "+isValidExpression(test));
		test="(e:o),"; Sp.p(test + " <=> "+isValidExpression(test));
		test="(f,hase)"; Sp.p(test + " <=> "+isValidExpression(test));
		test="("; Sp.p(test + " <=> "+isValidExpression(test));
		test=")"; Sp.p(test + " <=> "+isValidExpression(test));
	}
	*/
	

}