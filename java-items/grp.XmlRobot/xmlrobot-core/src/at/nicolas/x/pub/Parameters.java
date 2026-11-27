/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;


import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameters {

   private HashMap<String, String> varList;
   private String hochkomma ="'";

   public void setHochkomma ( String pHochkomma ) {
   		hochkomma = pHochkomma;
   }
   
   public Parameters ( String pParameters, String pHochkomma ) {
	   	  hochkomma = pHochkomma;
	      varList = new HashMap<String, String> ();
	      if ( pParameters != null )
	      {
		      String regex = null;
		      if ( hochkomma.equals("'"))
		    	  regex = "([^=\\s]+)[\\s]*=[\\s]*'((?:[^']|'')*)'";
		      else if ( hochkomma.equals("\"")) // \"
		    	  regex = "([^=\\s]+)[\\s]*=[\\s]*\"((?:[^\"]|\"\")*)\"";	 
		      Pattern p = Pattern.compile(regex); 
		      Matcher matcher = p.matcher(pParameters);
		      while ( matcher.find() ) {
		    	  String name =  matcher.group(1);
		    	  String value =  matcher.group(2);
			      if ( hochkomma.equals("'"))
			    	  value = stringReplace(value,"''","'");
			      else if ( hochkomma.equals("\"")) // \"
			    	  value = stringReplace(value,"\"\"","\"");
		    	  varList.put ( name, value);
		      }
		      
		      /*
		      HashMap<String, String> old = getOldParameters ( pParameters, pHochkomma );
		      if ( !old.equals(varList)) {
		    	  Sp.p("DIFFERENT::: PARAMS ::::: "+pParameters);
		    	  Sp.p("DIFFERENT::: PARAMS ::::: "+pParameters);
		    	  Sp.p("DIFFERENT::: PARAMS ::::: "+pParameters);
		    	  Sp.p("DIFFERENT::: PARAMS ::::: "+pParameters);
		    	  Sp.p("DIFFERENT::: PARAMS ::::: "+pParameters);
		    	  Sp.p(this.getStringForLineprint());
		    	  
		      }
		      */
		      // varList = old;
	      }

	   }
   
   /*
   private HashMap<String, String> getOldParameters ( String pParameters, String pHochkomma ) {
   	  String hochkomma = pHochkomma;
   	HashMap<String, String> varList = new HashMap<String, String> ();

      if ( pParameters != null )
      {

			String work = new String ( pParameters.trim() );
			int    foundPos = 0;
			String foundParameter = "";
			String foundName = "";
			int    paraStart = 0;
			int    paraEnd   = 0;

			foundPos = work.indexOf ( "="+hochkomma );

			while ( foundPos != -1 )
			{
				
				paraStart = foundPos+2;
				paraEnd   = work.indexOf(hochkomma,paraStart);
				

				if ( paraEnd == -1 ) {
					foundPos = -1;
				} else {
					foundName = work.substring(0,paraStart-2);
					foundParameter = work.substring(paraStart,paraEnd);

					varList.put ( foundName, foundParameter);

					if ( (paraEnd+2) > work.length() ) {
						// Das war der letzte Parameter
						foundPos = -1;
					} else {
						work = work.substring(paraEnd+2,work.length());
						foundPos = work.indexOf ( "="+hochkomma );
					}
				}

			}
		}
      return varList;
   }
   */

   public Parameters ( ) {
      varList = new HashMap<String, String> ();
   }

   public void removeParameter(
		   String pName ) {
	   varList.remove(pName);
   }

   public void setParameter (
      String pName,   // Parametername
      String pValue ) // der Wert des Parameters
   {
		varList.put(pName,pValue);
   }

	//	Ermitteln eines einzelnen Parameters   
   public String getParameter ( String pName ) {
		String ret;

		if ( varList.containsKey(pName) ) {
			ret = (String)varList.get(pName);
		} else {
			ret = null;
		}

		return ( ret );
	}

	//	Ermitteln des Parameterstrings   
   public String getString () {
		String ret = "";

		Set<String> keySet = varList.keySet();
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;
		
		while ( i.hasNext() ) {
			aktKey = (String)i.next();
			ret = ret.concat( aktKey + "="+hochkomma + stringReplace ( varList.get(aktKey), hochkomma, hochkomma+hochkomma ) + hochkomma + " " );
		}
		return ( ret.trim() );
   }

	//	Ermitteln des Parameterstrings   
   public String getStringForLineprint () {
		String ret = "";

		Set<String> keySet = varList.keySet();
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;

		while ( i.hasNext() ) {
			aktKey = (String)i.next();
			ret = ret.concat( " \n"+aktKey + "="+hochkomma + stringReplace ( varList.get(aktKey), hochkomma, hochkomma+hochkomma ) + hochkomma  );
		}
		return ( ret.trim() );
   }   

	//	Ermitteln des Parameterstrings   
   public String getStringForLineprint2 () {
		String ret = "";

		Set<String> keySet = varList.keySet();
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;

		while ( i.hasNext() ) {
			aktKey = (String)i.next();
			ret = ret.concat( " "+aktKey + "="+hochkomma + stringReplace ( varList.get(aktKey), hochkomma, hochkomma+hochkomma ) + hochkomma  );
		}
		return ( ret.trim() );
   }      

	//	Ermitteln aller Parameterin einer HashMap   
   public HashMap<String, String> getParameters () {
		return ( varList );
	}

	//	Ermitteln aller Parameternamen   
   public Set<String> getParameterNames () {
		return ( varList.keySet() );
	}


   // Ausdrucken auf pProt
   public void print (
		PrintStream pProt ) {
		Set<String> keySet = varList.keySet();
		Iterator<String> i = keySet.iterator();
		String   aktKey = null;

		while ( i.hasNext() ) {
			aktKey = (String)i.next();
			pProt.println(
				aktKey + "="+hochkomma +stringReplace ( varList.get(aktKey), hochkomma, hochkomma+hochkomma ) + hochkomma );
		}
	}
   
   public static Parameters getParametersInstance(String argv[], String hochkomma ) {
	   
		String all_args = new String();
		int i;
		for ( i =0; i < argv.length; i++ ) {
			all_args = all_args + argv[i] + " ";
		}
		
	   Parameters retval = new Parameters ( all_args, hochkomma);
	   return retval;
   }
   
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

}




