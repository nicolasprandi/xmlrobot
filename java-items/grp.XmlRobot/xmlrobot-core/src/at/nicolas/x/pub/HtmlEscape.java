/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.pub;

/**
* HtmlEscape in Java, which is compatible with utf-8
* @author Ulrich Jensen, http://www.htmlescape.net
* Feel free to get inspired, use or steal this code and use it in your
* own projects.
* License:
* You have the right to use this code in your own project or publish it
* on your own website.
* If you are going to use this code, please include the author lines.
* Use this code at your own risk. The author does not warrent or assume any
* legal liability or responsibility for the accuracy, completeness or usefullness of
* this program code.
* 
* Modified by Nicolas Prandstaetter on 2015-04-23: methods: escape2, escapeAmp
*/

public class HtmlEscape {

  private static char[] hex={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

  /**
   * Method for html escaping a String, for use in a textarea
   * @param original The String to escape
   * @return The escaped String
   */
  public static String escapeTextArea(String original)
  {
    return escapeSpecial(escapeTags(original));    
  }
  
  /**
   * Normal escape function, for Html escaping Strings
   * @param original The original String
   * @return The escape String
   */
  public static String escape(String original)
  {
    return escapeSpecial(escapeBr(escapeTags(original)));
  }
  
  public static String escape2(String original)
  {
    return escapeSpecial(escapeBr(escapeTags(escapeAmp(original))));
  }  
  public static String escape3(String original)
  {
    return escapeSpecial(escapeTags(escapeAmp(original)));
  }    
  
  public static String escapeMeins(String original)
  { 
    return escapeSpecial(escapeBr(original));
  
  }    
  
  public static String escapeTags(String original)
  {
    if(original==null) return "";
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
      boolean found=true;
      switch(chars[i])
      {
        case 60:out.append("&lt;"); break; //<
        case 62:out.append("&gt;"); break; //>
        case 34:out.append("&quot;"); break; //"
        case 39:out.append("&#39;"); break; //'
        default:found=false;break;
      }
      if(!found) out.append(chars[i]);
      
    }
    return out.toString();
    
  }

  public static String escapeBr(String original)
  {
    if(original==null) return "";
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
      boolean found=true;
      switch(chars[i])
      {
        case '\n': out.append("<br/>"); break; //newline
        case '\r': break;
        default:found=false;break;
      }
      if(!found) out.append(chars[i]);
      
    }
    return out.toString();
  }
 
  public static String escapeAmp(String original)
  {
    if(original==null) return "";
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
      switch(chars[i]) {
        case 38:out.append("&amp;"); break; 

        default: out.append(chars[i]); break;
      }
 
      
    }  
    return out.toString();
  }
  
  public static String escapeSpecial(String original)
  {
    if(original==null) return "";
    //System.out.println("TUX        IN: "+original);
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
        boolean found=true;
      switch(chars[i]) {
        // case 38:out.append("&amp;"); break; 
      	case 162:out.append("&cent;"); break;
        case 198:out.append("&AElig;"); break; 
        case 193:out.append("&Aacute;"); break; 
        case 194:out.append("&Acirc;"); break; 
        case 192:out.append("&Agrave;"); break; 
        case 197:out.append("&Aring;"); break; 
        case 195:out.append("&Atilde;"); break; 
        case 196:out.append("&Auml;"); break; 
        case 199:out.append("&Ccedil;"); break; 
        case 208:out.append("&ETH;"); break; 
        case 201:out.append("&Eacute;"); break; 
        case 202:out.append("&Ecirc;"); break; 
        case 200:out.append("&Egrave;"); break; 
        case 203:out.append("&Euml;"); break; 
        case 205:out.append("&Iacute;"); break; 
        case 206:out.append("&Icirc;"); break; 
        case 204:out.append("&Igrave;"); break; 
        case 207:out.append("&Iuml;"); break; 
        case 209:out.append("&Ntilde;"); break; 
        case 211:out.append("&Oacute;"); break; 
        case 212:out.append("&Ocirc;"); break; 
        case 210:out.append("&Ograve;"); break; 
        case 216:out.append("&Oslash;"); break; 
        case 213:out.append("&Otilde;"); break; 
        case 214:out.append("&Ouml;"); break; 
        case 222:out.append("&THORN;"); break; 
        case 218:out.append("&Uacute;"); break; 
        case 219:out.append("&Ucirc;"); break; 
        case 217:out.append("&Ugrave;"); break; 
        case 220:out.append("&Uuml;"); break; 
        case 221:out.append("&Yacute;"); break; 
        case 225:out.append("&aacute;"); break; 
        case 226:out.append("&acirc;"); break; 
        case 230:out.append("&aelig;"); break; 
        case 224:out.append("&agrave;"); break; 
        case 229:out.append("&aring;"); break; 
        case 227:out.append("&atilde;"); break; 
        case 228:out.append("&auml;"); break; 
        case 231:out.append("&ccedil;"); break; 
        case 233:out.append("&eacute;"); break; 
        case 234:out.append("&ecirc;"); break; 
        case 232:out.append("&egrave;"); break; 
        case 240:out.append("&eth;"); break; 
        case 235:out.append("&euml;"); break; 
        case 237:out.append("&iacute;"); break; 
        case 238:out.append("&icirc;"); break; 
        case 236:out.append("&igrave;"); break; 
        case 239:out.append("&iuml;"); break; 
        case 241:out.append("&ntilde;"); break; 
        case 243:out.append("&oacute;"); break; 
        case 244:out.append("&ocirc;"); break; 
        case 242:out.append("&ograve;"); break; 
        case 248:out.append("&oslash;"); break; 
        case 245:out.append("&otilde;"); break; 
        case 246:out.append("&ouml;"); break; 
        case 223:out.append("&szlig;"); break; 
        case 254:out.append("&thorn;"); break; 
        case 250:out.append("&uacute;"); break; 
        case 251:out.append("&ucirc;"); break; 
        case 249:out.append("&ugrave;"); break; 
        case 252:out.append("&uuml;"); break; 
        case 253:out.append("&yacute;"); break; 
        case 255:out.append("&yuml;"); break;
        /*
        case 169:out.append("&copy;"); break;
        case 174:out.append("&reg;"); break;
        case 153:out.append("&trade;"); break;
        case 171:out.append("&laquo;"); break;
        case 187:out.append("&raquo;"); break;
        case 149:out.append("&bull;"); break;
        case 183:out.append("&middot;"); break;
        case 161:out.append("&iexcl;"); break;
        case 191:out.append("&iquest;"); break;
        case 166:out.append("&brvbar;"); break;
        case 167:out.append("&sect;"); break;
        case 182:out.append("&para;"); break;
        case 176:out.append("&deg;"); break;

        case 177:out.append("&plusmn;"); break;
        case 247:out.append("&divide;"); break;
        case 215:out.append("&times;"); break;
        case 8730:out.append("&radic;"); break;
        case 8747:out.append("&int;"); break;
        case 178:out.append("&sup2;"); break;
        case 180:out.append("&acute;"); break;
        case 137:out.append("&permill;"); break;
        case 181:out.append("&micro;"); break;
        case 172:out.append("&not;"); break;
        case 189:out.append("&frac12;"); break;
        case 704:out.append("&forall;"); break;
        case 8706:out.append("&part;"); break;
        case 8707:out.append("&exist;"); break;   
        case 8709:out.append("&empty;"); break;
        case 8834:out.append("&sub;"); break;
        case 8835:out.append("&sup;"); break;
        case 8836:out.append("&nsub;"); break;
        case 8838:out.append("&sube;"); break;
        case 8839:out.append("&supe;"); break;
        case 8801:out.append("&equiv;"); break;
        case 8804:out.append("&le;"); break;
        case 8805:out.append("&ge;"); break;
        case 8793:out.append("&wedgeq;"); break;         
        
        case 915:out.append("&Gamma;"); break;
        case 916:out.append("&Delta;"); break;
        case 931:out.append("&Sigma;"); break;
        case 937:out.append("&Omega;"); break;
        case 960:out.append("&pi;"); break;  

        case 160:out.append("&nbsp;"); break;
        
        case 8592:out.append("&#8592;"); break;
        case 8593:out.append("RuFF"); break; // "&uarr;"
        
        case 2191:out.append("&#8593;"); break;       
        case 2192:out.append("&#8594;"); break;

        case 2193:out.append("&#8595;"); break; 
        
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;  
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break; 
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;
        case :out.append(""); break;      */  
        default:
          found=false;
          break;

      }
      if(!found)
      {
    	  
        if(chars[i]>127) {
        	// System.out.println("TUX UNBEKANNT: "+chars[i]+".");
          char c=chars[i];
          int a4=c%16;
          c=(char) (c/16);
          int a3=c%16;
          c=(char) (c/16);
          int a2=c%16;
          c=(char) (c/16);
          int a1=c%16;
          out.append("&#x"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+";");  
          //System.out.println("TUX         : &#x"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+";");
         
        }
        else
        {
          out.append(chars[i]);
        }
      }
    }  
    //System.out.println("TUX       OUT: "+out.toString());
    return out.toString();
  }
  
} 