/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.http.client.utils.URIBuilder;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.InputSource;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.HtmlEscape;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Commons  {
	
	public static String jdbcDbType2Classname ( String db ) throws Exception {
		String wishefForName = null;
		if ( db.equals("maria")) {
			wishefForName ="org.mariadb.jdbc.Driver";
		} else if ( db.equals("postgres"))
			wishefForName ="org.postgresql.Driver";				
		else if ( db.equals("mysql"))
			wishefForName ="com.mysql.jdbc.Driver";
		else
			throw new Exception("dbc-open failed with db ["+db+"] witch is unknown use postgres mysql maria");
		return wishefForName;
	}
	public static void registerJdbcDriverByClassname ( String classname ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		//System.out.println("Versuch ueber classname: "+classname);
		Sp.p("Requested JDBC.Driver: classname="+classname);	
		Sp.p("Requested JDBC.Driver: classname=        "+classname);
		Sp.p("Requested JDBC.Driver: classname=               "+classname);
		Class.forName(classname).newInstance();
		//System.out.println("OK");
		/*
		 * int version = 0;
		if ( version == 0) {
			
		} else if ( version == 1) {
			
		} */
		
	}
	public static void registerJdbcDriverByClassnameUrl ( String classname, String url ) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if ( DriverManager.getDriver(url) == null ) {
			registerJdbcDriverByClassname(classname);
		}
		
	}	
	
	public static void registerJdbcDriverByDbtypeUrl ( String dbtype, String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
		Driver foundDriver = null;
		System.out.println("registerJdbcDriverByDbtypeUrl "+dbtype+" "+url);
		try {
			//System.out.println("Versuch ueber url: "+url);
			foundDriver = DriverManager.getDriver(url);
		} catch (SQLException e) {
			foundDriver = null;
		}
		if ( foundDriver == null ) {
			//System.out.println("Versuch ueber dbtype: "+dbtype);
			registerJdbcDriverByClassname(jdbcDbType2Classname(dbtype));
		}
		System.out.println("registerJdbcDriverByDbtypeUrl done.");
		
	}		
public static String getExtOfBaseName ( String basename ) {
	int last_index_of = basename.lastIndexOf(".");
	if ( last_index_of > -1 && last_index_of<basename.length() )
		return basename.substring(last_index_of+1).toLowerCase();
	else
		return null;
}


/*
public static String getPreOfBaseName ( String basename ) {
	int last_index_of = basename.lastIndexOf(".");
	if ( last_index_of > -1 && last_index_of<basename.length() )
		return basename.substring(last_index_of+1).toLowerCase();
	else
		return null;
}
*/
public static UriString introduceDirAsUriString ( String fulldir ) throws FileSystemException, URISyntaxException, URIException {
	if ( fulldir == null ) return null;
	else if ( fulldir.length() == 0 ) return new UriString(fulldir);
	String ret = fulldir;
	String schema = UriParser.extractScheme(ret);
	if ( schema == null ) 
		ret = "file://"+ret;
	ret = fixSeparators(ret);
	Sp.p("vor normalize: "+ret);
	ret = normalizePathOfUri(ret);
	Sp.p("nach normalize: "+ret);
	if ( ret.endsWith("/"))
		ret = ret.substring(0, ret.length()-1);
	
	return new UriString(ret);
	
}
public static String getSchema ( String uri ) {
	String schema = UriParser.extractScheme(uri);
	if ( schema == null )
		schema = "file";
	return schema;
}
public static UriString introduceFileAsUriString ( String fullfile ) throws FileSystemException, URISyntaxException, URIException {

	if ( fullfile == null ) return null;
	else if ( fullfile.length() == 0 ) return new UriString(fullfile);
	
	String ret = fullfile;
	String schema = UriParser.extractScheme(ret);
	if ( schema == null ) 
		ret = "file://"+ret;
	ret = fixSeparators(ret);
	ret = normalizePathOfUri(ret);
	
	return new UriString(ret);	
	
}
public static String constructNormalizedDir( String fulldir ) throws FileSystemException, URISyntaxException, URIException {
	return (introduceDirAsEgoDirString(fulldir)).string;
}
public static String constructNormalizedFile( String fullfile ) throws FileSystemException, URISyntaxException, URIException {
	return (introduceFileAsEgoFileString(fullfile)).string;
}
public static EgoDirString introduceDirAsEgoDirString ( String fulldir ) throws FileSystemException, URISyntaxException, URIException {
	if ( fulldir == null ) return null;
	else if ( fulldir.length() == 0 ) return new EgoDirString(fulldir);
	
	String ret = fulldir;
	String schema = UriParser.extractScheme(ret);
	if ( schema == null ) {
	
		ret = "file:///"+ret;

	}
	//ret = stringReplace(ret, "\\", "/");
	ret = fixSeparators(ret);
	
	ret = normalizePathOfUri(ret);

	if ( !ret.endsWith("/"))
		ret = ret + "/";

	return new EgoDirString(ret);	
	
}
public static EgoFileString introduceFileAsEgoFileString ( String fullfile ) throws FileSystemException, URISyntaxException, URIException {
	if ( fullfile == null ) return null;
	else if ( fullfile.length() == 0 ) return new EgoFileString(fullfile);
	String ret = fullfile;
	String schema = UriParser.extractScheme(ret);
	if ( schema == null ) 
		ret = "file:///"+ret;
	ret = fixSeparators(ret);
	ret = normalizePathOfUri(ret);

	return new EgoFileString(ret);		
}

public static String constructNameFromEgoDirString ( EgoDirString eds ) {
	if ( eds.string.length()== 0 ) return "";
	int li = eds.string.lastIndexOf("/", eds.string.length()-2);
	if ( li==-1) return "";
	String ret = eds.string.substring(li+1, eds.string.length()-1);
	
	return ret;
}
public static String constructNameFromEgoFileString ( EgoFileString eds ) {
	if ( eds.string.length()== 0 ) return "";
	int li = eds.string.lastIndexOf("/", eds.string.length());
	if ( li==-1) return "";
	String ret = eds.string.substring(li+1);
	
	return ret;
}

public static String constructParentFromEgoDirString ( EgoDirString eds ) {
	if ( eds.string.length()== 0 ) return "";
	int li1 = eds.string.lastIndexOf("/", eds.string.length()-2);
	if ( li1==-1) return "";
	
	String ret = eds.string.substring(0, li1+1);
	
	return ret;
}
public static String constructParentFromEgoFileString ( EgoFileString eds ) {
	if ( eds.string.length()== 0 ) return "";
	int li = eds.string.lastIndexOf("/");
	if ( li==-1) return "";
	String ret = eds.string.substring(0,li+1);

	return ret;
}

public static String getUriByFilename( String file_name) throws FileSystemException, URISyntaxException, URIException {
	if ( file_name == null ) return null;	
	EgoFileString eds = Commons.introduceFileAsEgoFileString(file_name);  	
	return eds.string;		
}	

public static String getDirOfFile( String file_name) throws FileSystemException, URISyntaxException, URIException {
	if ( file_name == null ) return null;	
	EgoFileString eds = Commons.introduceFileAsEgoFileString(file_name);
	String ret = constructParentFromEgoFileString(eds);

	return ret;		
}	

public static String getDirOfDir( String dir_name) throws FileSystemException, URISyntaxException, URIException {
	if ( dir_name == null ) return null;	
	EgoDirString eds = introduceDirAsEgoDirString(dir_name);
	String ret = constructParentFromEgoDirString(eds);

	return ret;		
}

public static String getUriByDirname( String dir_name) throws FileSystemException, URISyntaxException, URIException {
	if ( dir_name == null ) return null;
	EgoDirString eds = Commons.introduceDirAsEgoDirString(dir_name);   	
	return eds.string;		
}	
public static String fixSeparators ( String name ) {
	StringBuilder sb = new StringBuilder(name);
	UriParser.fixSeparators(sb);
	return sb.toString();
}


/*
 * 
 public static String normalizePath ( String path ) throws FileSystemException, URISyntaxException {
	URI uri = new URI(path);
    if (!uri.isAbsolute()) {
        throw new URISyntaxException(path, "Must provide an absolute URI for repositories");
    }
    String p = uri.getPath();
    Sp.p("extractedPath  -> "+p);

    // uri = uri.normalize();	
	StringBuilder sb = new StringBuilder(p);
	UriParser.normalisePath(sb);
	String p2 = sb.toString();

	URI ur = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
            p2, uri.getQuery(), uri.getFragment());
    Sp.p("normalizePath ("+path+") -> "+ur.toString());
	return ur.toString();
}
 */

public static String getNameOfDir (String dirname ) throws FileSystemException, URISyntaxException, URIException {
	EgoDirString eds = introduceDirAsEgoDirString(dirname);
	String ret = constructNameFromEgoDirString(eds);

	return ret;
}

public static String getNameOfFile (String filename ) throws FileSystemException, URISyntaxException, URIException {
	EgoFileString eds = introduceFileAsEgoFileString(filename);
	String ret = constructNameFromEgoFileString(eds);

	return ret;
}

public static String cutDir ( String dir, int len ) throws Exception {

	String result_rigth = FileName.SEPARATOR+getNameOfDir(dir);
	String result_left = "";
	if ( result_rigth.length() < len ) {
		int max = dir.length()- result_rigth.length()-1;
		int want = len - result_rigth.length();

		if ( want > max ) want = max;

		if ( want > 0)
		result_left = dir.substring(0, want) + "...";
	}
	String fr = result_left+result_rigth;
	if ( !fr.endsWith(FileName.SEPARATOR))
		fr = fr.concat(FileName.SEPARATOR);
	return ( new String(fr));
	
}
public static String normalizePathOfUri(String uriO ) throws URISyntaxException, FileSystemException, URIException {
	boolean q=false;
	if ( uriO == null || uriO.equals("") || uriO.equals("file://"))
		return uriO;
	if ( q) Sp.p("");
	if ( q) Sp.p("URI : "+uriO);

	String uri = 	URIUtil.encodePath(uriO);
	if ( q) Sp.p("URI ENCODE PATH: "+uri);
	//final 
	//Sp.ph("Gateway 1");
	URIBuilder b = new URIBuilder(uri);
	//Sp.ph("Gateway 2");
	String path=b.getPath();
	//Sp.ph("Gateway 3");
	if ( q) Sp.p("   path * "+path);
	//Sp.ph("Gateway 5");
	if ( path.length()==0 ) {
		// kein path
		if ( q) Sp.p("    * nopath");
		return uri;
	}
	String pathEncoded=URIUtil.encodePath(path);

	if ( q) Sp.p("   pathEncoded * "+pathEncoded);
	StringBuilder sb = new StringBuilder(path);
	UriParser.normalisePath(sb);
	String normalizedPath = sb.toString();
	if ( q) Sp.p("   normPath * "+normalizedPath);	
	//String normalizedPathEncoded = URIUtil.encodePath(normalizedPath);
	//Sp.p("   normPathEncoded * "+normalizedPathEncoded);			
	int pathStart = uri.indexOf(pathEncoded);
	if ( q) Sp.p("   pathStart * "+pathStart);
	// String ret = uri.substring(0,pathStart)+URIUtil.encodePath(normalizedPath);
	String ret = uri.substring(0,pathStart)+normalizedPath;
	if ( q) Sp.p("   newUriPath * "+ret);
	//Sp.p("   newUriPathD * "+URIUtil.decode(ret));
	
    URI urix = URI.create(uri);
    if ( q) Sp.p("   query * "+urix.getQuery());
    if ( urix.getQuery() != null )
    	ret = ret+"?"+urix.getQuery();
    if ( q) Sp.p("   mit query * "+ret);
    
    if ( q) Sp.p("   frag * "+urix.getFragment());
    if ( urix.getFragment() != null )
    	ret = ret+"#"+urix.getFragment();
    if ( q) Sp.p("   mit frag * "+ret);
		
	return ret;
		
}

public static String cutFile ( String dir, int len ) throws FileSystemException, URISyntaxException, URIException {

	String result_rigth = getNameOfFile(dir);
	String result_left = "";
	if ( result_rigth.length() < len ) {
		int max = dir.length()- result_rigth.length();
		int want = len - result_rigth.length();
		if ( want > max ) want = max;
		if ( want > 0)
		result_left = dir.substring(0, want) + "...";
	}
	return ( new String(result_left+result_rigth));
	
}
public static String cutFile2 ( String fn, int len ) {
	try {
		String name = getNameOfFile(fn);

		String retthis = name;
		if ( name.length() > len ) {
			int max = name.length();
			int want = len;
			if ( want > max ) want = max;
			if ( want > 0) {
				retthis =   name.substring(0,want)+"..." ;
			}
		} 
	
		return ( retthis);
	} catch (Exception e) {
		return fn;
	}
	
}

public static String lineSep;
public static char lineSepChar;

private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

public static String randomAlphaNumeric(int len) {
	// https://dzone.com/articles/generate-random-alpha-numeric
	StringBuilder builder = new StringBuilder();
	
	while (len-- != 0) {
	
	int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
	
	builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	
	}
	
	return builder.toString();

}

public static String randomText(int len, String pool) {
	// https://dzone.com/articles/generate-random-alpha-numeric
	StringBuilder builder = new StringBuilder();
	
	while (len-- != 0) {
	
	int character = (int)(Math.random()*pool.length());
	
	builder.append(pool.charAt(character));
	
	}
	
	return builder.toString();

}
public static Element getAttributesAsElementList ( Element ei ) {
	Element att = new Element("attributes");
	List<Attribute> alist = ei.getAttributes();
	Iterator<Attribute> i = alist.iterator();
	while ( i.hasNext()) {
		Attribute theAttribute = (Attribute)i.next();
		Element add = new Element("attribute");
		add.setAttribute("name", theAttribute.getName());
		add.setAttribute("value", theAttribute.getValue());
		att.addContent(add);
	}
	return att;
}

/* CREDIT:
 * https://www.programcreek.com/java-api-examples/?api=org.apache.commons.vfs.FileName
 * public static String getFilename(FileObject fileObject) 
 */
/*
public static String getFilename(FileObject fileObject)
{

    FileName fileName = fileObject.getName();
    String root = fileName.getRootURI();
    if (!root.startsWith("file:")) return fileName.getURI(); // nothing we can do about non-normal files. //$NON-NLS-1$
    if (root.startsWith("file:////")) return fileName.getURI(); // we'll see 4 forward slashes for a windows/smb network share
    if (root.endsWith(":/")) // Windows //$NON-NLS-1$
    {
        root = root.substring(8,10);
    }
    else // *nix & OSX
    {
        root = ""; //$NON-NLS-1$
    }
    String fileString = root + fileName.getPath();
    if (!"/".equals(FileName.SEPARATOR)) //$NON-NLS-1$ nic123
    {
        fileString = stringReplace(fileString, "/", FileName.SEPARATOR); //$NON-NLS-1$
    }
    return fileString;
}
*/
public static String fileClassic ( String in ) {
	if ( in==null) return null;
	String out =in;
	if ( out.startsWith("file:///")) {
		out = out.substring(8);
	} else if ( out.startsWith("file://")) {
		out = out.substring(7);
	} else if ( out.startsWith("file:/")) {
		out = out.substring(6);
	}
	return out;
}
/*
public static String fileClassicWindows ( String in ) {
	if ( in==null) return null;
	String out =in;
	if ( out.startsWith("file:///")) {
		out = out.substring(8);
	} else if ( out.startsWith("file://")) {
		out = out.substring(7);
	} else if ( out.startsWith("file:/")) {
		out = out.substring(6);
	}
	out = stringReplace(out, "\\", "/");
	return out;
}
*/

public static String dirClassic ( String in ) {
	if ( in==null) return null;
	String out =in;
	if ( out.startsWith("file:///")) {
		out = out.substring(8);
	} else if ( out.startsWith("file://")) {
		out = out.substring(7);
	} else if ( out.startsWith("file:/")) {
		out = out.substring(6);
	}
	out = addFsInCase(out);
	return out;
}

public static void startupActions() {
	lineSep = new String("\n");
	lineSepChar = lineSep.charAt(0);
	
}
public static int RoundMethodCode ( String name ) throws LangException {
	if      ( name.equals("CEILING")) return BigDecimal.ROUND_CEILING;
	else if ( name.equals("DOWN")) return BigDecimal.ROUND_DOWN;
	else if ( name.equals("FLOOR")) return BigDecimal.ROUND_FLOOR;
	else if ( name.equals("HALF_DOWN")) return BigDecimal.ROUND_HALF_DOWN;
	else if ( name.equals("HALF_EVEN")) return BigDecimal.ROUND_HALF_EVEN;
	else if ( name.equals("HALF_UP")) return BigDecimal.ROUND_HALF_UP;
	else if ( name.equals("UNNECESSARY")) return BigDecimal.ROUND_UNNECESSARY;
	else if ( name.equals("UP")) return BigDecimal.ROUND_UP;
	else throw new LangException(237,"Unknown type of rounding-method '"+name+"'"
			+", try: CEILING, DOWN, FLOOR, HALF_DOWN, HALF_EVEN, HALF_UP, UNNECESSARY, UP");
}	

public static String RoundMethodText ( int in ) throws LangException {
	if      ( in == BigDecimal.ROUND_CEILING) return "CEILING";
	else if ( in == BigDecimal.ROUND_DOWN) return "DOWN";
	else if ( in == BigDecimal.ROUND_FLOOR) return "FLOOR";
	else if ( in == BigDecimal.ROUND_HALF_DOWN)  return "HALF_DOWN";
	else if ( in == BigDecimal.ROUND_HALF_EVEN)  return "HALF_EVEN";
	else if ( in == BigDecimal.ROUND_HALF_UP ) return "HALF_UP";
	else if ( in == BigDecimal.ROUND_UNNECESSARY) return "UNNECESSARY";
	else if ( in == BigDecimal.ROUND_UP) return "UP";
	else throw new LangException(238,"Unknown type of rounding-method '"+in+"'");
}	


public static String escapeXml(String original) {
	    if(original==null) return "";
	    StringBuffer out=new StringBuffer("");
	    char[] chars=original.toCharArray();
	    for(int i=0;i<chars.length;i++)
	    {
	      boolean found=true;
	      switch(chars[i])
	      {
	        case 38:out.append("&amp;"); break; 	      
	        case 60:out.append("&lt;"); break; //<
	        case 62:out.append("&gt;"); break; //>
	        case 39:out.append("&apos;"); break; //'
	        case 34:out.append("&quot;"); break; //"
	        case 10:out.append("&#xA;"); break; //"
	        
	        default:found=false;break;
	      }
	      if(!found) out.append(chars[i]);
	      
	    }
	    return out.toString();	    
}	

public static String unescapeXml ( String in ) {
	String ret = in;
	ret = Commons.stringReplace(ret,"&lt;","<");
	ret = Commons.stringReplace(ret,"&gt;",">");
	ret = Commons.stringReplace(ret,"&quot;","\"");
	ret = Commons.stringReplace(ret,"&apos;","'");
	ret = Commons.stringReplace(ret,"&#xA;",lineSep);
	ret = Commons.stringReplace(ret,"&amp;","&");
		
	return ret;
}

/*
public static String cdata_xml(String original) {
    if(original==null) return "";
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
      boolean found=true;
      switch(chars[i])
      {
        case 38:out.append("&amp;"); break; 	      
        case 60:out.append("&lt;"); break; //<
        case 62:out.append("&gt;"); break; //>
        case 39:out.append("&apos;"); break; //'
        case 34:out.append("&quot;"); break; //"
        
        default:found=false;break;
      }
      if(!found) out.append(chars[i]);
      
    }
    return out.toString();
    
}	
*/


public static String xpathstring( String in ) {
	String result = null;
	
	if (in==null)
		return null;
	else if ( in.indexOf("\"") == -1 ) {
		// there is no quote
		result = "\""+in+"\"";
	} else {
	    StringBuffer out=new StringBuffer("");
	    out.append("concat(\"");
	    char[] chars=in.toCharArray();
	    for(int i=0;i<chars.length;i++) {
	    	
	    	switch(chars[i]) {
	        // case 38:out.append("&amp;"); break; 
	        case 34:out.append("\",'\"',\""); break; // quote "
	        default:
	            out.append(chars[i]);
	            break;
	    	}
	    	
	    }
	    out.append("\")");
	    result = out.toString();			
	}
	return result;
}
public static String cdata_html(String original) {
    if(original==null) return "";
    StringBuffer out=new StringBuffer("");
    char[] chars=original.toCharArray();
    for(int i=0;i<chars.length;i++)
    {
      boolean found=true;
      switch(chars[i])
      {
        case 38:out.append("&amp;"); break; 	      
        case 60:out.append("&lt;"); break; //<
        case 62:out.append("&gt;"); break; //>
        case 39:out.append("&#39;"); break; //'
        case 34:out.append("&quot;"); break; //"
        
        default:found=false;break;
      }
      if(!found) out.append(chars[i]);
      
    }
    return out.toString();
    
}		

public static String text_to_html ( String in) {
	return HtmlEscape.escape2(in);
}
public static String text_to_html3 ( String in) {
	return HtmlEscape.escape3(in);

}




public static String boolToStringXr ( boolean in ) {
	if ( in ) return "1";
	else return "0";
}
public static String boolToString ( boolean in ) {
	if ( in ) return "1";
	else return "0";
}

public static boolean stringxr2bool ( String in ) throws Exception {
	if ( in.equals("1"))
		return true;
	else if ( in.equals("0"))
		return false;
	else
		throw new Exception("That should be 1 or 0, but it is: ["+in+"]");
}
public static boolean stringxr2bool ( String in, boolean def )  {
	if ( in.equals("1"))
		return true;
	else if ( in.equals("0"))
		return false;
	else
		return def;
}
public static boolean stringjava2bool ( String in, boolean def ) {
	if ( in == null )
		return def;
	else if ( in.equals("true"))
		return true;
	else if ( in.equals("false"))
		return false;
	else
		return def;		
}	

public static double round(double value, int nbr_decimals ) throws Exception {


	int power = (int)Math.pow(10, nbr_decimals);
	if ( nbr_decimals > 0 )
		value = value * power;

	value=value+ 0.5;

	Double X = new Double(value);
	int back_a = X.intValue();

	double back_b = (double)back_a;
	

	double back = 0;
	if ( nbr_decimals > 0 )
		back = back_b / power;
	else
		back = back_b;

	
	return (back);
}




public static String getDbLoc( String app, String inst ) {
	String ret;
	
	if ( inst != null ) {
		ret = "@"+Entities.importEntities(app)+"."+Entities.importEntities(inst);
	} else if ( app != null )
		ret = "@"+Entities.importEntities(app);
	else 
		ret = "@[Workspace]";
	
	return ret;
}


public static String convertSlashes( String path ) {
	return stringReplace(path, "\\", "/");
}



public static String addFsInCase( String pfadname )  {
	// hole letztes zeichen raus
	if ( pfadname == null ) return pfadname;
	if ( pfadname.length() > 0 ) {
		String letztesZeichen = pfadname.substring(pfadname.length()-1);
		if (letztesZeichen.equals("\\") || letztesZeichen.equals("/")) {
			return pfadname;
		} else 
			return pfadname + "/";			
	} else
		return pfadname;
}	

public static String removeFsInCase( String dirname ) {
	String out = new String (dirname);
	while ( out.endsWith("/") || out.endsWith("\\") ) {
		out = out.substring(0, out.length()-1);
	}
	return out;
}
/*
public static String xvfs2xrFileXXX( String uri ) {
	return uri;
}

public static String vfs2xrDirXXX( String uri ) {
	String vorschlag = uri;
	
	return addFsInCase(vorschlag);
}

public static String vfs2xrDirXXY( String uri ) {
	String vorschlag = uri;
	if ( vorschlag.startsWith("file:///")) {
		vorschlag = vorschlag.substring(8);
	}
	
	return addFsInCase(vorschlag);
}
public static String vfs2xrFileXXY( String uri ) {
	String vorschlag = uri;
	if ( vorschlag.startsWith("file:///")) {
		vorschlag = vorschlag.substring(8);
	}
	return vorschlag;
}
*/


/*
public static String vfs2xrDirXXY( FileObject fo ) {
	String vorschlag = fo.getName().getURI();
	if ( vorschlag.startsWith("file:///")) {
		vorschlag = vorschlag.substring(8);
	}
	
	return addFsInCase(vorschlag);
}
*/


/*
public static String vfs2xrDirXXY( FileName filename ) {
	String vorschlag = filename.getURI();
	if ( vorschlag.startsWith("file:///")) {
		vorschlag = vorschlag.substring(8);
	}
	
	return addFsInCase(vorschlag);
}
*/
/*
public static String xr2vfsFile( String filename ) {
	String uri = filename;
	
	if (!uri.startsWith("jar:") && 
		!uri.startsWith("zip:") && 
		!uri.startsWith("tar:") && 
		!uri.startsWith("tgz:") && 
		!uri.startsWith("tbz2:") && 
		!uri.startsWith("gz:") && 
		!uri.startsWith("bz2:") && 
		!uri.startsWith("hdfs:") && 
		!uri.startsWith("http:") && 
		!uri.startsWith("https:") && 
		!uri.startsWith("webdav:") && 
		!uri.startsWith("ftp:") && 
		!uri.startsWith("ftps:") && 
		!uri.startsWith("sftp:") && 
		!uri.startsWith("smb:") && 
		!uri.startsWith("res:") && 
		!uri.startsWith("ram:") && 
		!uri.startsWith("mime:") ) {
		uri = "file:///" + uri;
	} 
	
	return uri;
}


public static String xr2vfsDirXXX( String dirname ) {
	String uri = removeFsInCase(dirname);
	
	if (!uri.startsWith("jar:") && 
			!uri.startsWith("zip:") && 
			!uri.startsWith("tar:") && 
			!uri.startsWith("tgz:") && 
			!uri.startsWith("tbz2:") && 
			!uri.startsWith("gz:") && 
			!uri.startsWith("bz2:") && 
			!uri.startsWith("hdfs:") && 
			!uri.startsWith("http:") && 
			!uri.startsWith("https:") && 
			!uri.startsWith("webdav:") && 
			!uri.startsWith("ftp:") && 
			!uri.startsWith("ftps:") && 
			!uri.startsWith("sftp:") && 
			!uri.startsWith("smb:") && 
			!uri.startsWith("res:") && 
			!uri.startsWith("ram:") && 
			!uri.startsWith("mime:") ) {
		uri = "file:///" + uri;
	} 
	
	return uri;
}
*/
public static boolean xr2isLocalDirFile( String x ) {

	boolean ret;
	
	
	if (x.startsWith("jar:") || 
			x.startsWith("zip:") || 
			x.startsWith("tar:") || 
			x.startsWith("tgz:") || 
			x.startsWith("tbz2:") || 
			x.startsWith("gz:") || 
			x.startsWith("bz2:") || 
			x.startsWith("hdfs:") || 
			x.startsWith("http:") || 
			x.startsWith("https:") || 
			x.startsWith("webdav:") || 
			x.startsWith("ftp:") || 
			x.startsWith("ftps:") || 
			x.startsWith("sftp:") || 
			x.startsWith("smb:") || 
			x.startsWith("res:") || 
			x.startsWith("ram:") || 
			x.startsWith("mime:") )  {

		ret = false;
	} else {

		ret = true;
	}
	return ret;
}
/*
public static String getResourceFilename ( String resourcepath ) throws IOException {
	File dir1 = new File (".");
	String fn2 = dir1.getCanonicalPath()+"/resources/"+resourcepath;
	boolean exists = existsFile(fn2);
	if ( !exists )
		throw new IOException ("the resourcepath "+resourcepath+", fn="+fn2+" does not exist");
	
	return ( fn2);
}
public static String java2xrDir( String dirname ) throws IOException {
	String vorschlag = removeFsInCase(dirname);
	vorschlag = addFsInCase(vorschlag);
	return convertSlashes(vorschlag);
	
}





public static String dirNameAdjust (String dirname) {
	if (dirname==null) return null;
	return addFsInCase(convertSlashes(dirname));
}


*/
public static String java2WinInCase (String dirname) {
	
	if ( System.getProperty("os.name").startsWith("Windows")) {
		return stringReplace(dirname,"/","\\");
		// return stringReplace(removeFsInCase(dirname),"/","\\");
	} else {
		return dirname;
	}
		
}

/*
public static String fileNameAdjust (String _in, String fileseparator) {
	if (_in==null) return null;
	String i1 = Commons.stringReplace(_in, "/", fileseparator);
	String i2 = Commons.stringReplace(i1, "\\", fileseparator);
	if (!i2.endsWith(fileseparator))
		i2 = i2.substring(0, i2.length()-1);
	if (!i2.endsWith(fileseparator))
		i2 = i2.substring(0, i2.length()-1);
	return i2;
}
*/	





/*
public static String getFileOfFileOUTDATEDVFS ( String file ) {
	int lp1 = file.lastIndexOf("/");
	int lp2 = file.lastIndexOf("\\");
	
	if ( lp2 > lp1 )
		lp1 = lp2;
	
	if ( lp1 > -1 )
		return (file.substring(lp1+1));
	else
		return ( file );
}
*/




	// ************************* getClosingBracket ********************************************	
	static public int getClosingBracket ( String x, int fromIndex) {
		// fromIndex is not the opening bracket, but the first character after the opening bracket
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
	

public static boolean hasFlag( Element e, String flagname ) {

	String flag = e.getAttributeValue("flag");
	if ( flag != null ) {
		if ( 	flag.equals(flagname) || 
				flag.indexOf(" "+flagname+" ") > -1 || 
				flag.startsWith(flagname+" ") || 
				flag.endsWith(" "+flagname))
			return true;			
	}
	return false;
}

public static boolean isFlag( Element e, String flagname ) {

	String flag = e.getAttributeValue("flag");
	if ( flag != null && flag.equals(flagname)) 
		return true;
	else
		return false;
}

public static boolean hasFlag( String evaluate, String flagname ) {

	if ( evaluate != null ) {
		if ( 	evaluate.equals(flagname) || 
				evaluate.indexOf(" "+flagname+" ") > -1 || 
						evaluate.startsWith(flagname+" ") || 
						evaluate.endsWith(" "+flagname))
			return true;			
	}
	return false;
}
public static boolean hasFlagIn( Element e, String attribute, String flagname ) {
	String flags = e.getAttributeValue(attribute);
	if ( flags != null ) {
		if ( 	flags.equals(flagname) || 
				flags.indexOf(" "+flagname+" ") > -1 || 
				flags.startsWith(flagname+" ") || 
				flags.endsWith(" "+flagname)) {
			
			return true;			
		}
	}
	/*
	Sp.p("NO FLAG: '"+flagname+"' in '"+e.getAttributeValue(attribute)+"' flags="+flags);
	if ( flagname.equals("fn") || flagname.equals("lm") ) {
		if ( flags !=null ) {
			Sp.p("index= "+flags.indexOf(flagname));
			Sp.p("starts with= "+flags.startsWith(flagname));
		}
	}
	*/
	return false;
}

public static String hasFlagString( Element e, String flagname ) {
	String flags = e.getAttributeValue("flags");
	if ( flags!=null)
		JOptionPane.showMessageDialog(null, "flags not appreciated\n"+Commons.toTextRaw(e));

	String flag = e.getAttributeValue("flag");
	if ( flag != null ) {
		if ( flag.indexOf(" "+flagname+" ") > -1 || flag.startsWith(flagname) || flag.endsWith(flagname))
			return "true";			
	}
	return "false";
}	
// html
//html
/*
public static String decodeHtmlEntities ( String in ) {
	String ret = in;
	ret = Commons.stringReplace(ret,"&lt;","<");
	ret = Commons.stringReplace(ret,"&gt;",">");
	ret = Commons.stringReplace(ret,"&quot;","\"");
	ret = Commons.stringReplace(ret,"&apos;","'");
	ret = Commons.stringReplace(ret,"&amp;","&");
		
	return ret;
}
*/



public static String decodeXmlEntitiesXr ( String in ) {
	String ret = in;
	ret = Commons.stringReplace(ret,"&dagger;","X ");
	ret = Commons.stringReplace(ret,"&rarr;","-> ");
	// ret = Commons.stringReplace(ret,"&radic;","ok ");
	ret = Commons.stringReplace(ret,"&nbsp;"," ");
	return ret;
}
/*
public static String createHtmlEntities ( String in ) {
	String ret = in;
	ret = Commons.stringReplace(ret,"<","&lt;");
	ret = Commons.stringReplace(ret,">","&gt;");
	return ret;
}
*/
	
public static String getTs() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("yyyyMMdd_HHmm_ss");
	return formatter.format(new Date());

}
public static String getTsShort() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("yyyyMMdd_HHmm");
	return formatter.format(new Date());

}
public static String getTsShortDate() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("yyMMdd");
	return formatter.format(new Date());

}

public static String xxgetTsHourly() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("yyMMdd");
	return formatter.format(new Date());

}

public static String getTsShortTime() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("HHmmSSS");
	return formatter.format(new Date());

}

public static String getTsMs() {
	SimpleDateFormat formatter
    = new SimpleDateFormat ("yyyyMMdd_HHmm_ss_SSS");
	return formatter.format(new Date());

}


public static String leadBlanks ( String in, int len ) {
	int bisher = in.length();
	String drauf = "";
	if ( bisher < len )
		drauf = StringTimes(" ",len-bisher);
	return ( new String (drauf+in));
}

public static String leadSymbols ( String in, int len, String symbol ) {
	int bisher = in.length();
	String drauf = "";
	if ( bisher < len )
		drauf = StringTimes(symbol,len-bisher);
	return ( new String (drauf+in));
}
public static String postlanks ( String in, int len ) {
	if ( in != null ) {
		int bisher = in.length();
		String drauf = "";
		if ( bisher < len )
			drauf = StringTimes(" ",len-bisher);
		return ( new String (in+drauf));
	} else {
		return ( new String(StringTimes(" ",len)));
	}

}
public static String trailBlanks ( String in, int len ) {
	int bisher = in.length();
	String drauf = "";
	if ( bisher < len )
		drauf = StringTimes(" ",len-bisher);
	return ( new String (in+drauf));
}



public static String cutNice ( String in, int len ) {
	if (in == null ) {
		return "";
	
	} else {
		int l = in.length();
		if ( l > len)
			try {
				return ( in.substring(0, len-3) + " ..");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "";
			}
		else
			return in;			
	}

}

public static String cutNice2 ( String in, int len ) {
	String x = cutNice(in,len);
	return ( postlanks(x,len));
}


public static String cutDir2Att ( String dir, Attribute len ) {
	if (len==null) return dir;
	
	
	String x = cutDir2(dir,Commons.string2int(len.getValue()));
	return x;
	
}
public static String cutDir2 ( String dir, int len ) {

	String retthis;
	try {
	
		String name = dir;
		if ( !name.endsWith(FileName.SEPARATOR))
			name = name + FileName.SEPARATOR;
		retthis = name;
		if ( name.length() > len ) {
			int max = name.length();
			int want = len;

			if ( want > max ) want = max;
			// Sp.p("want2   ["+want+"]");
			if ( want > 0) {
				int pos_slash = name.lastIndexOf(FileName.SEPARATOR, name.length()-2);
			
				if ( pos_slash > - 1) {
					String part1 = name.substring(0, pos_slash); // linker teil
					String part2 = name.substring(pos_slash); // rechter teil
					if ( part2.length() > want ) {
						retthis = part2.substring(1);
						// part 2 voll + rest von part 1
						/*
						int to = part1.length()-want;
						if (to < 0 ) {
							to = part1.length();
						}
						retthis = part1.substring(0, to) + "..."+part2;
						*/
					} else {
						int wantthis = want;
						if ( wantthis > part2.length())
							wantthis = part2.length();
						int wantfirst = want - wantthis;
						if ( wantfirst > part1.length())
							wantfirst = part1.length();
						retthis =   part1.substring(0,wantfirst)+"..."+part2.substring(0,wantthis) ;
					}

							
				} else {
					retthis =   name.substring(0,want)+"..." ;
				}
				
			} else {
				
				retthis = getNameOfDir(dir)+FileName.SEPARATOR;
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		retthis = dir;
	} 
	return ( retthis);	
	
}



public static String killSubstring ( String source, String tokill ) {
	String retval = null;
	int position = source.indexOf(tokill);
	if ( position != -1 ) {
		retval = source.substring(0,position)+
				 source.substring(position+tokill.length());
	} else
		retval = source;
	return ( retval );
	
}
public static boolean did_string_replace = false;
public static String stringReplace ( String source, String search, String replace ) {
		
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
	            did_string_replace = true;
	        } else { //nicht gefunden
	            result.append(source.substring(pos)); //letzter abschnitt
	        }
	    } while(nPos!= -1);


	    return result.toString();
	}

	public static String StringTimes ( String in, int times ) {
		String retval = "";
		if ( in.length()>0 && times>0  ) {
			for ( int i=0;i<times;i++) {
				retval = retval+in;
				
			}
		}
		
		return retval;
	}


	/*
    public static boolean delete_file(String path) {
        File file = new File(path);
        return file.delete();
    }*/


	
    public static void copy_stream ( InputStream is, OutputStream os ) throws IOException {
    	org.apache.commons.io.IOUtils.copy(is, os);

    }/*
    public static void copy_file(File input, File output) throws IOException {
    	org.apache.commons.io.FileUtils.copyFile(input, output);

    } 

	*/
	
    
   
	public static String cureName( String in ) {
		String out = in.replaceAll("([^a-zA-z0-9])","_");
		return out;
	}
	public static String cureNameAsFile( String in ) {
		String out = in.replaceAll("([^a-zA-z0-9\\.])","_");
		return out;
	}	
	/* a.replaceAll("([^a-zA-z0-9])","\\\\$1")
	 * String c = a.replaceAll("([\\/\\\\\\?\\%\\*\\:\\|\\\"\\<\\>\\ \\'])","_");
	public static boolean isTrue ( Element e, String attribute, boolean def ) {
		boolean ret = def;
		String istrue = e.getAttributeValue("isTrue");
		if ( Commons.isEmpty(istrue )) istrue = "";
		String isfalse = e.getAttributeValue("isFalse");			
		if ( Commons.isEmpty(isfalse )) isfalse = "";		
		
		if ( istrue.indexOf(attribute) != -1 )
			ret = true;
		else if ( isfalse.indexOf(attribute) != -1 )
			ret = false;
		else
			ret = def;
		return ret;
	}
	*/


	


	public static String toText( Element e )  {
	      XMLOutputter serializer = new XMLOutputter();
	      // serializer.setFormat(Format.getCompactFormat());
	      return ( serializer.outputString(e));
	}
	
	public static String toTextUTF8( Element e )  {
	      XMLOutputter serializer = new XMLOutputter();
	      Format f = Format.getCompactFormat();
	      f.setEncoding("UTF-8");
	      serializer.setFormat(f);
	      
	      String x = serializer.outputString(e);
	      return x;
	    		  
	}	

	public static String toTextCompact( Element e )  {
		if (e == null)
			return "";
		
      XMLOutputter serializer = new XMLOutputter();
 
      String r;
      try {
			r = serializer.outputString(e);
      } catch (Exception e1) {
    	  Sp.pe(e1);
    	  String addMsg = null;
    	  if ( e == null ) addMsg = " which is null";
    	  else addMsg = "which has name '"+e.getName()+"'";
    	  // printExceptionStackTrace( e1, "Commons.toTextCompact", "Trying to serialize an XmlElement, "+addMsg);
    	  r = "??? toTextCompact ???";
      }
	  return r;
	}	

	/*
	public static String toTextPretty( Element e )  {
	      XMLOutputter serializer = new XMLOutputter();
	      serializer.setFormat(Format.getPrettyFormat());
	      String r;
	      try {
				r = serializer.outputString(e);
	      } catch (Exception e1) {
	    	  
	    	  r = "??? toTextPretty ???";
	      }
		  return r;
		}		

*/
	public static String toTextRaw( Element e )  {
	      XMLOutputter serializer = new XMLOutputter();
	      serializer.setFormat(Format.getRawFormat());
	      String r;
	      try {
				r = serializer.outputString(e);
	      } catch (Exception e1) {
	    	  Sp.pe(e1);
	    	  r = "??? getRawFormat ???";
	      }
		  return r;
		}		
	
	/*
	public static String toTextBad( Element e )  {
	      String x = toTextRaw(e);
	      String y = Commons.stringReplace(x, " ", "\n");
	      String z = Commons.stringReplace(y, "<", "\n");
	      return ( z );
	}		
	
	public static String toTextBad2( Element e )  {
			String x = toTextRaw(e);
	      String z = Commons.stringReplace(x, "<", "\n");
	      return ( z );
	}	
		
	
	public static void printElementList ( List<Element> list, String titel ) {
		// LogConnector lc = new LogConnector("printElementList");
		Sp.p("--- Start-ExecElements: "+titel+" --------------");
		Iterator<Element> i = list.iterator();
		while ( i.hasNext()) {
			Object o = i.next();
			
			if (o.getClass().getName().equals("org.jdom2.Element") ) {
				Element e = (Element)o;
				List<Element> childs = e.getChildren();
				Iterator<Element> ix = childs.iterator();
				while ( ix.hasNext()) {
					Element e1 = (Element)ix.next();
					Sp.p("   "+toText(e1));
				}				
			}
			
		}
		Sp.p("--- End -ExecElements: "+titel+" --------------");		
	}	*/
	
	
	public static Document getXmldocumentByInputStream( InputStream fis )  throws Exception, JDOMException, IOException{
		

		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			doc = builder.build(fis);
			//fis.close();
		} catch (Exception e1) {
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);

			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  ");
			
		}  finally {
			if ( fis != null ) fis.close();
		}
		
		return ( doc );
	}	
	
	public static Document getXmldocumentByString( String in )  throws Exception, JDOMException, IOException{
		

		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			//doc = builder.build(new StringReader(in));
			doc = builder.build(new InputSource(new StringReader(in)));
			
			//fis.close();
		} catch (Exception e1) {
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);

			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  ");
			
		}  finally {
			//if ( fis != null ) fis.close();
		}
		
		return ( doc );
	}	
			

	/*
	public static boolean isNumber ( String check ) {
		return ( stringMatchesRegex(check,"^[0-9]+\\.?[0-9]*$"));
	}
	*/
	
	public static boolean stringMatchesRegex ( String string, String regex ) {
		return stringMatchesRegex ( string,regex,0);
	}	
	
	public static boolean stringMatchesRegex ( String string, String regex, int regex_flags ) {
		Pattern p = Pattern.compile(regex,regex_flags); 
		Matcher matcher = p.matcher(string);
		return ( matcher.matches() );
	}	
	
	public static int regexFlagsString2int( String flags ) {
		String flags2 = flags.toUpperCase();
		int result = 0;
		
		if (hasFlag(flags2, "CASE_INSENSITIVE"))
			result = result | Pattern.CASE_INSENSITIVE;
		
		if (hasFlag(flags2, "MULTILINE"))
			result = result | Pattern.MULTILINE;
		
		if (hasFlag(flags2, "DOTALL"))
			result = result | Pattern.DOTALL;
		
		if (hasFlag(flags2, "UNICODE_CASE"))
			result = result | Pattern.UNICODE_CASE;
		
		if (hasFlag(flags2, "CANON_EQ"))
			result = result | Pattern.CANON_EQ;
		
		if (hasFlag(flags2, "UNIX_LINES"))
			result = result | Pattern.UNIX_LINES;
		
		if (hasFlag(flags2, "LITERAL"))
			result = result | Pattern.LITERAL;
		
		if (hasFlag(flags2, "UNICODE_CASE"))
			result = result | Pattern.UNICODE_CASE;
		
		if (hasFlag(flags2, "COMMENTS"))
			result = result | Pattern.COMMENTS;		
		
		
		return result;
	}
	public static int countRegexMatches ( String string, String regex ) {
		return countRegexMatches(string,regex,0);

	}	
	
	public static int countRegexMatches ( String string, String regex, int regex_flags ) {
		
		Pattern p = Pattern.compile(regex,regex_flags); 
		
		Matcher matcher = p.matcher(string);
		
		int count = 0;
		while ( matcher.find())
			count++;
		
		return ( count );
	}	
	/*
	 * 	public static int countRegexMatches ( String string, String regex, int regex_flags ) {
		
		Pattern p = Pattern.compile(regex,regex_flags); 
		
		String[] parts = p.split(string);	
		int occurrences = parts.length-1;
		return ( occurrences );
	}	
	 */
	
	public static String int2Prefixedstring ( int zahl, int laenge ) {
		String teil2 = Integer.toString(zahl);
		int plusnullen = laenge - teil2.length();
		String retval = Commons.StringTimes("0",plusnullen)
						+ teil2;
		return ( retval );
	}
	
	public static String int2String ( int zahl ) {
		return ( Integer.toString(zahl));
	}	
	
	public static String argsToString (String[] args) {
		String all_args = new String();
		for ( int i =0; i < args.length; i++ ) {
			all_args = all_args + args[i] + " ";
		}
		return ( all_args );
		
	}
	
	public static List<Element> xpathSelectNodes ( 
									  Element element,
									  String  xpath ) {		
		XPathExpression<Element> xpathe =
			    XPathFactory.instance().compile(xpath, Filters.element());
			
		List<Element> list = xpathe.evaluate(element);
		
		return list;
		
	}
	
	public static List<Element> xpathSelectNodes ( 
			  Document document,
			  String  xpath ) {		
		
		XPathExpression<Element> xpathe = 
				XPathFactory.instance().compile(xpath, Filters.element());
		
		List<Element> list = xpathe.evaluate(document);
		
		return list;
	
	}	

	public static Element xpathSelectSingleNode (   
									Element element,
									String  xpath ) {
		
		List<Element> found = xpathSelectNodes(element,xpath);
		if ( found == null ) return null;
		else if ( found.size()==0 ) return null;
		return found.get(0);
					
	}
	
	public static Element xpathSelectSingleNode (   
			Document document,
			String  xpath ) {

		List<Element> found = xpathSelectNodes(document,xpath);
		if ( found == null ) return null;
		else if ( found.size()==0 ) return null;
		return found.get(0);
	
	}	

	public static String xpathGetValue (   
									Element element,
									String  xpath ) {		
		
		Element e = xpathSelectSingleNode ( element, xpath);
		if ( e==null) return null;
		
		return e.getValue();
	}
	
	public static String xpathGetValue (   
			Document document,
			String  xpath ) {		

		Element e = xpathSelectSingleNode ( document, xpath);
		if ( e==null) return null;
		
		return e.getValue();
	}	

	public static void logNodes( List<Element> list ) {
		Iterator<Element> i = list.iterator();
		while ( i.hasNext()) {
			Element element = (Element)i.next();
			logNode(element);
		}
	}
	
	public static void logNode( Element element )  {
	      XMLOutputter serializer = new XMLOutputter();
	      serializer.setFormat(Format.getPrettyFormat());
	      Sp.p(serializer.outputString(element));
	}	
	

	/*
	public static String evalXField ( Element e, String f ) {
		String ret = null;
		if ( f.equals("."))
			ret = e.getText();
		else if ( f.startsWith("@"))
			ret = e.getAttributeValue(f.substring(1));
		else 
			ret = e.getChildText(f);
		
		return ( ret );	
	}
	
	public static void addInternalKey( Element e, String internalKey, String keyvalue ) {
		if ( internalKey != null && !internalKey.equals("")) {
			String x = keyvalue;
			if ( x == null ) x = new String("");
			if ( internalKey == null || internalKey.equals(""));
			if ( internalKey.startsWith("@")) {
				String wert =internalKey.substring(1);
		
				e.setAttribute(wert, x);
			} else {
				Element y = new Element(internalKey);
				y.setText(x);
				e.addContent(y);
			}
		}
	}
	
	public static Element createElementByNames ( String defaultval, String individual ) {
		Element ret = new Element ( defaultval );
		if ( individual != null  ) ret.setName(individual);
		return ( ret );
	}
	
	public static double string2double ( String in ) {

		try {
			double x = Double.parseDouble(in);
			return x;
		} catch (NumberFormatException e ) {
			return -1;
		}		
	}
	

	
	public static boolean isDouble ( String check ) {
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
	public static String double2string ( double d ) {

		String ds = Double.toString(d);
		String ret = (new BigDecimal(ds).toPlainString() );
		if ( ret.endsWith(".0"))
			ret = ret.substring(0,ret.length()-2);
			
		return ret;

	}	
	public static boolean isInt ( String check ) {
		try {
			int x = Integer.parseInt(check);
			// log_debug(check+ " is double");
			return true;
		} catch (NumberFormatException e ) {
			// log_debug(check+ " is NOT double");
			return false;
		}
	}	
	
	public static int string2int ( String in ) {

		try {
			int x = Integer.parseInt(in);
			return x;
		} catch (NumberFormatException e ) {
			return -1;
		}

		
	}	
	
	
	

	
	public static boolean isEmpty ( String in ) {
		if ( in == null )
			return true;
		if ( (in.trim()).length()>0 )
			return false;
		else
			return true;
	}
	/*
	public static void mkdir ( String x ) throws IOException {
		File f = new File (x);
		if ( !f.exists()) {
			if ( ! f.mkdir() )
				throw new IOException  ( "Error in creating the directory '"+x+"'.");
			
		}
	}
	*/
	

	
	public static int para2int ( String value, int def, String name) {
		if ( value == null || (value.trim()).length()==0) {
			//lc.log_ext("value '"+name+ "' set to "+def+ " (default)",iBug);
			return def;
		} else {			
			//lc.log_ext("value '"+name+ "' set to "+value,iBug);
			return Commons.string2int(value);
		}
	}
	
	public static String para2string ( String value, String def, String name) {
		
		if ( value == null ) {
			//if ( lc.traceIsOn())
			//	lc.log_ext("value '"+name+ "' set to "+def+ " (default)",iBug);
			return def;
		} else {	
			//if ( lc.traceIsOn())
			//	lc.log_ext("value '"+name+ "' set to "+value,iBug);
			return value;
		}
		
	}	
	
	public static String para2string ( String value, String def, String name,ILogConnector lc, ITask iBug) {
		return para2string ( value, def, name);
		
	}	
	

	/*
	public static boolean para2bool ( String value, boolean def, String name, ILogConnector lc, ITask iBug ) {
		
		if ( value == null ) {
			lc.debug("value '"+name+ "' set to "+def+ " (default)",iBug);
			return def;
		} else {			
			lc.debug("value '"+name+ "' set to "+value,iBug);			
			return Commons.string2bool(value, def);
		}
		
	}	
	*/
	

	/*
	public static void printExceptionStack ( Exception e , ILogConnector lc) {
		if ( lc != null ) {
			lc.fatal("*X        *   Java-ExceptionStack:" );
			
			StackTraceElement[] trace = e.getStackTrace();
	        for (int i = 0; i < trace.length; i++)
	            lc.fatal("*         *      " + trace[i]);

	        Throwable throwable = e.getCause();
	        if (throwable != null)
	            lc.fatal("*         *      Caused by:"+throwable.getMessage());				
		} else {
			Sp.pe(e);
		}
		
		
	}
	
	
	public static boolean hasAttributeValue ( Element e, String attname ) {
		if ( e.getAttribute(attname)!= null && (e.getAttributeValue(attname).trim()).length()>0 )
			return true;
		else
			return false;
	}
	
	public static String getDefaultValue( Element e ) {
		if ( e.getAttribute("x") != null )
			return (e.getAttributeValue("x"));
		else
			return (e.getText());
	}
	*/
	
	public static String getXValue( Element e ) {
		if ( e.getAttribute("x") != null )
			return (e.getAttributeValue("x"));
		else
			return (e.getText());
	}	
	/*
	public static String consumeFlag(String container, String flag_to_subtract ) {
		if (container==null) return null;
		String d = Pattern.quote(flag_to_subtract);
		String p1 = container.replaceAll(d, "");
		return ( p1.trim());
	}
	*/
	public static boolean stringtoBoolean( String in, String failMessage ) throws Exception {
		Vcar x = new Vcar(in);
		
		if ( x.isBool) {
			return x.getBool();
		} else {
			throw new LangException(105,failMessage+", Should be boolean but is '"+in+"'");
		}
	}		
	public static boolean stringToBool( String in ) throws Exception {
		Vcar x = new Vcar(in);
		
		if ( x.isBool) {
			return x.getBool();
		} else {
			throw new LangException(105,"Should be boolean but is '"+in+"'");
		}
	}		
	/*
	public static String xxnormalizePathOfUri(String uri ) throws URISyntaxException, FileSystemException {

		Sp.p("URI: "+uri);
		final URIBuilder b = new URIBuilder(uri);
		String path=b.getPath();
		Sp.p("   path * "+path);
		if ( path.length()==0 ) {
			Sp.p("    * nopath");
			return uri;
		}
		StringBuilder sb = new StringBuilder(path);
		UriParser.normalisePath(sb);
		String normalizedPath = sb.toString();
		Sp.p("   normPath * "+normalizedPath);			
		int pathStart = uri.indexOf(path);
		Sp.p("   pathStart * "+pathStart);
		String ret = uri.substring(0,pathStart)+normalizedPath;
		Sp.p("   newUriPath * "+ret);
		
	    URI urix = URI.create(uri);
	    Sp.p("   query * "+urix.getQuery());
	    if ( urix.getQuery() != null )
	    	ret = ret+"?"+urix.getQuery();
	    Sp.p("   mit query * "+ret);
	    
	    Sp.p("   frag * "+urix.getFragment());
	    if ( urix.getFragment() != null )
	    	ret = ret+"#"+urix.getFragment();
	    Sp.p("   mit frag * "+ret);
			
		return ret;
			
	}
	*/
	/*
	public static String normalizePathxxx ( String uri ) throws URISyntaxException  {
		Sp.p("-- --- -- -- -- normalizePath("+uri+")");
		
	    final URIBuilder b = new URIBuilder(new URI(uri).normalize());
	    final String host = b.getHost();
	    if (host != null) {
	      b.setHost(host.toLowerCase(Locale.ROOT));
	    }
	    final String path = b.getPath();
	    if (TextUtils.isEmpty(path)) {
	      b.setPath("/");
	    }
	    
	    return ((b.build()).toString());

	}
	*/

	public static String normalizePathALT ( String path ) throws FileSystemException, URISyntaxException {
		/*
		URI uri = new URI(path);
	    if (!uri.isAbsolute()) {
	        throw new URISyntaxException(path, "Must provide an absolute URI for repositories");
	    }
	    String p = uri.getPath();
	    Sp.p("extractedPath  -> "+p);

	    // uri = uri.normalize();	
	     * 
	     */
		StringBuilder sb = new StringBuilder(path);
		UriParser.normalisePath(sb);
		String p2 = sb.toString();


		return p2;
	}	
}