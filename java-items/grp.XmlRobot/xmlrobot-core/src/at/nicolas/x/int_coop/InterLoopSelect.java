/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.tasks.Unique;

public class InterLoopSelect implements IInterLoop {

private ResultSetMetaData rsmd = null;
private ILogConnector lc ;
private LogConnector lcd = new LogConnector("");	

private String idN = null;
private String idE = null;

private static String zero = new String("0");



private String[] name_array;
private HashMap<String, Integer> name_map = null; 

private int limit = -1;
boolean trim_flag = false;

private ILogConnectorLogic lcl=null;
private IRequestHandler rh = null;

int missing_strategy = 2;
private static final int MISSING_STRATEGY_EMPTY = 0;
private static final int MISSING_STRATEGY_EXCEPTION = 1;
private static final int MISSING_STRATEGY_JOVIAL = 2;
private static KeyWordMapper missingtypes;

private     ResultSet rs = null;

private int recordCounter = 0;


//private String idE = null;
/*

private String filename = null;



private List<Element> list = null;
private Iterator<Element> iterator = null;
private Element aktElement = null;
public int pos = 0;
public int length = -1;

private boolean flag_dont_escape_on_read;*/



// todo nic321
/*
public boolean hasMissingStrategyEXCEPTION() {
	if ( missing_strategy == MISSING_STRATEGY_EXCEPTION)
		return true;
	else
		return false;
} */

public InterLoopSelect (
		Element elementN, 
		ILogConnector lc, 
		ILogConnectorLogic lcl, 
		IRequestHandler rh) throws Exception {
	// element is in natural
	// 1, variablen belegen
	idN = elementN.getAttributeValue("id");
	idE = Entities.importEntities(idN);
	if ( idN== null )
		throw new Exception("must specify @id for loop-select");
	else if ( idN.startsWith("sys_"))
		throw new LangException(234,"Loops can't have an id starting with 'sys_' but found: "
				+ idN);



	if ( elementN.getAttribute("dbc")== null )
		throw new LangException("definieren sie @dbc fuer das select");
	String dbc_usedN = elementN.getAttributeValue("dbc");
	
	CoopDbc dbc = (CoopDbc)rh.getNo(dbc_usedN,  Wellknown.coop_type_dbc);
	
	String select_text = null;
	PreparedStatement theps = null;
	if ( elementN.getAttribute("ps") != null ) {
		theps = dbc.getPs(elementN.getAttributeValue("ps"));
	} else if ( elementN.getAttribute("select") != null ) {
		select_text = elementN.getAttributeValue("select");
	} else if ( elementN.getAttribute("select_file") != null ) {
		String file_encoding = "UTF-8";
		if ( elementN.getAttribute("select_file_encoding")!=null ) {
			file_encoding =elementN.getAttributeValue("select_file_encoding") ;
		}		

		select_text = CommonsVfs.readTextfileFormatLfDefault(elementN.getAttributeValue("select_file"), file_encoding,rh.getSitzung());
		
	} else {
		throw new LangException(2311,"loop-select must have @select or @select_file or @ps "
				+ idN);		
	}
	if ( elementN.getAttribute("limit") != null && (elementN.getAttributeValue("limit")).length()> 0 )
		limit = Commons.string2int(elementN.getAttributeValue("limit"));
	
	trim_flag = Commons.hasFlag(elementN, "trim");
	

	this.rh = rh;
	if (elementN.getAttribute("missing_strategy")==null )
		missing_strategy = MISSING_STRATEGY_JOVIAL;
	else
		missing_strategy = missingtypes.convert(elementN.getAttributeValue("missing_strategy"));

	
	this.lcl = lcl;
	//this.lcl.setLogicHeader(idN+".loop_xml");
	this.lcl.setLogicHeader(idN);
	this.lc = lc;
   
    if ( select_text != null ) {
    	
    	Statement stmt = dbc.conn.createStatement();
    	rs = stmt.executeQuery(select_text);	
    } else if ( theps != null ) {
    	rs = theps.executeQuery();
    }
    
	
	rsmd = rs.getMetaData();
	int columnCount = rsmd.getColumnCount();    
	

	Unique unique = new Unique("xml_attribute");
	name_array = new String[columnCount];
	name_map = new HashMap<String, Integer>();
	for (int j = 1; j <= columnCount; j++ ) {
		String cn = unique.getValue(rsmd.getColumnName(j));
		name_array[j-1] = cn;	
		name_map.put(cn, new Integer(j-1));
	}
	
	if ( elementN.getAttribute("fields") != null ) {
		Element eFields = new Element("fields");
		eFields.setAttribute("count", Commons.int2String(columnCount));
		
		
		for (int i = 1; i <= columnCount; i++ ) {
			Element eField = new Element("field");
			eField.setAttribute("catalog_name", rsmd.getCatalogName(i));
			eField.setAttribute("display_size", Commons.int2String(rsmd.getColumnDisplaySize(i)));
			eField.setAttribute("label", rsmd.getColumnLabel(i));
			eField.setAttribute("name", rsmd.getColumnName(i));
			eField.setAttribute("type_name", rsmd.getColumnTypeName(i));
			eField.setAttribute("type", ""+rsmd.getColumnType(i));
			eField.setAttribute("precision", Commons.int2String(rsmd.getPrecision(i)));
			eField.setAttribute("scale", Commons.int2String(rsmd.getScale(i)));
			eField.setAttribute("schema", rsmd.getSchemaName(i));
			eField.setAttribute("table", rsmd.getTableName(i));
			eField.setAttribute("nullable", Commons.int2String(rsmd.isNullable(i)));
			eFields.addContent(eField);
		}
		Document documentFields = new Document(eFields);
		
		String encoding_fields = "UTF-8";
		if ( elementN.getAttribute("encoding_fields") != null ) {
			encoding_fields = elementN.getAttributeValue("encoding_fields");
			if ( encoding_fields.equals("") ) encoding_fields = "UTF-8";
		}
		
		CommonsVfs.writeToFile(
					documentFields, 
					elementN.getAttributeValue("fields"), 
					Format.getRawFormat(), encoding_fields,
					rh.getSitzung());
	}
		

	lcl.logicStart("(a)","Started");
	recordCounter = 0;
	
	// lc.core(name+" started ...",LogConnector.CORE_STRUCTURE);

}
public ResultSet getRs() {
	return rs;
}
public void close()  {
	
	try {
		if (rs != null) rs.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	/*
	try {
		if (stmt != null) stmt.close();
	} catch (SQLException e) {
		e.printStackTrace();
	} */

}
public boolean hasNext() throws SQLException {
	recordCounter++;
	// considering the limit
	if ( limit != -1 && recordCounter>limit) { 
		// lc.core(name+" stopped by reaching the max.",LogConnector.CORE_STRUCTURE);
		lcl.logicEnd("(z)","Finished, by reaching max of "+limit);
		close();
		return false;
	}
	
	boolean goon = rs.next();

	if (!goon) {
		// lc.core(name+" finished.",LogConnector.CORE_STRUCTURE);
		lcl.logicEnd("(z)","Finished.");
		close();
	} else {
		
		// lc.core(name+pos+") next iteration",LogConnector.CORE_STRUCTURE);
		lcl.logic("("+Integer.toString(recordCounter)+")",null);
	} return (goon);		
}
	
private String getColumnByName(String fieldname ) throws Exception {
	Integer index = name_map.get(fieldname);
	if ( index!= null ) {
		return getColumnByIndex(index.intValue()+1 ); 
	} else
		return getMissingStrategy("Unknown fieldname for Select-Loop["+idN+"]: '"+fieldname+"'");
	
}
private String getColumnByIndex(int k ) throws SQLException {
	
	int the_column_type = rsmd.getColumnType(k);
	String colValueString = null;
	Object columnObject = rs.getObject(k);
	
	if ( the_column_type == 2 ) {
		// numeric
        if (columnObject != null) {
        	String work = columnObject.toString();
        	if ( work.indexOf(".") != -1 ) {
        		while ( work.endsWith(zero)) {
        			//System.out.println(work);
        			work = work.substring(0, work.length()-1);
        			//System.out.println("  "+work);
        		}
        	}
        	if ( work.endsWith("."))
        		work = work.substring(0, work.length()-1);
            colValueString = work;

        }											
	} else if ( the_column_type == 8 ) {
		// float
		// BigDecimal bd = rs.getBigDecimal(k);
		float fl = rs.getFloat(k);
		String f = String.format("%.5f", fl);
		int i =(int) (fl * 10000f);
		//System.out.println("i="+i);
		int b = i / 10000;
		//System.out.println("fl="+fl+" i="+i+" b="+b+" f="+f);
		colValueString =f; // = Integer.toString(b);
		//float b = (float)
		//Float myFloat = Float.valueOf(fl);
		//BigDecimal bd = new BigDecimal(b);
		//colValueString = bd.toPlainString();
	} else {
		
        
		
        if (columnObject != null) {
            colValueString = columnObject.toString();

        }										
	}
	
    if ( trim_flag && colValueString!= null )
    	colValueString = colValueString.trim();
    
    return colValueString;
	
}

private String hasColumnByName(String fieldname ) throws Exception {
	Integer index = name_map.get(fieldname);
	if ( index!= null ) {
		return Commons.boolToString(hasColumnByIndex(index.intValue()+1 )); 
	} else
		return "0";
	
}
private boolean hasColumnByIndex(int k ) throws SQLException {
	
	if ( rs.getObject(k) == null )
		return false;
	else
		return true;
	
}
// called from Cooperation to get a value, 
public String functionValue ( ArrayList<Vcar> vcar_list ) throws Exception {
	// normaler value, ohne default wert
	// wird von der cooperatation-loop verwendet
	// gives back a natural value!
	// 1= loop-id

	String r = null;
	boolean has_user_data = false;
	if ( vcar_list.size() == 2 ) {
		String x = ((Vcar)vcar_list.get(1)).s;
		// nur ein wert vorhanden
		if (x.startsWith("@")) {
			// ein feld
			r = getColumnByName(x.substring(1));
			has_user_data = true;
		} else if ( x.equals("has_next?") ) {
			r = Commons.boolToString(hasNext());
		} else if ( x.equals("#i") ) {
				
			r = Commons.int2String(recordCounter);	
		}  else {
			// ein feld
			r = getColumnByName(x);
			has_user_data = true;
		}
		
		
	} else if ( vcar_list.size() == 3 ) {
		String x = ((Vcar)vcar_list.get(1)).s;
		String y = ((Vcar)vcar_list.get(2)).s;
		// nur ein wert vorhanden
		if ( x.equals("exists?") ) {
				
			r = hasColumnByName(y);
			has_user_data = true;
			
		}  else {
			throw new LangException("sx received unknown function parameter: "+x);
		}
		
		
	} else {
		throw new Exception("request does not fit, sx gets more than 2 parameters");
	}

	if ( r== null)
		return null;
	else {
		// since we have to give here back natural but it is a friend we do export
		if ( has_user_data)
			return Entities.exportEntities(r);
		else
			return r;
		
	} 
}
/*
public String lxExists ( ArrayList<Vcar> vcar_list ) throws Exception {

	String r = "0";
	if ( vcar_list.size() == 3 ) {
		String x = ((Vcar)vcar_list.get(2)).s;

		if ( x.startsWith("@") ) {
	
			if ( aktElement.getAttribute(x.substring(1)) != null ) 
				r="1";
		} else {
			// xpath only
			Element y = Commons.xpathSelectSingleNode(aktElement, x);
			if ( y!= null ) {
				r = "1";
			}	
		}		
		
	} else if ( vcar_list.size() == 4 ) {

		String x = ((Vcar)vcar_list.get(2)).s;
		String a = ((Vcar)vcar_list.get(3)).s;
		if ( a.startsWith("@"))
			a = a.substring(1);
		else
			throw new Exception("LoopXml: fourth argument must be an attribute and therefore begin with an @");

		Element y = Commons.xpathSelectSingleNode(aktElement, x);
		if ( y!= null ) {
			if ( y.getAttribute(a) != null)
				r = "1";
		}
	} else if ( vcar_list.size() == 2 ) {
	
		if ( aktElement.getText() != null )
			r="1";
	} else {
		throw new Exception("request does not fit, lx gets more than 4 parameters");
	}
	return r;
}*/

// translate is called by the interpreter for plain xml-loops
public String translate ( String i ) throws Exception {
	
	// wird vom interpreter aufgerufen
	// gives back null if not applicable
	String r = null;
	
	//  lc.info("Start translate for "+i);
	 
	// lc.info("***** loop-translate input:"+i);
	int klammerauf = i.indexOf("(loop,"+idE);
	if ( klammerauf == -1 ) {
		return null;			
	}
	
	int klammerzu = Commons.getClosingBracket(i, klammerauf+6);
	if ( klammerzu == -1 )
		throw new LangException(23612,"Select-Loop '"+idN+"' didnt find closing bracket in: "+i);
		//return i;
	
	String x = new String ( i.substring(klammerauf+6+idE.length(),klammerzu+1));
	
	// this is a flag for performance, if a value is returned that cant have entites at all
	// then the my_import_entities will be false
	// examle is the number of iterations #i or #max, but also the exists? returns
	boolean has_user_data = false;
	
	if ( x.equals(")") ) {
		throw new LangException("there is no no-parameter for the select-loop-function");
	} else if ( x.equals(",#i)") ) {
		r = Commons.int2String(recordCounter);	
	} else if ( x.equals(",columns?)") ) {
		r = "";
		Iterator<Entry<String, Integer>> it = name_map.entrySet().iterator();
		while ( it.hasNext()) {
			Map.Entry pair=(Map.Entry)it.next();
			r = pair.getKey()+" ";
		}				
		has_user_data = true;
	} else if ( x.startsWith(",exists?,@")) { 
		String xname = x.substring(10, x.length()-1);
		r = hasColumnByName(xname);	
	} else if ( x.startsWith(",exists?,")) { 
		String xname = x.substring(9, x.length()-1);
		r = hasColumnByName(xname);	
	} else if ( x.startsWith(",@") ) {
		r = getColumnByName(x.substring(2, x.length()-1));	
		if (r==null) r = getMissingStrategy(i);
		has_user_data = true;		
	} else if ( x.startsWith(",") ) {
		r = getColumnByName(x.substring(1, x.length()-1));	
		if (r==null) r = getMissingStrategy(i);
		has_user_data = true;		
	} else 
		throw new LangException("select-loop schaut schlecht aus: "+i);		
		

	
	// The retured value must return escaped entities
	// if it is a friend (flag friend) they are already escaped and the import is not needed
	if ( r!=null && r.length()>0  && has_user_data)
		r = Entities.importEntities(r);	
	return r;
	
}
	
public String getMissingStrategy(String message ) throws Exception {
	if ( missing_strategy == MISSING_STRATEGY_EMPTY )
		return "";
	else if ( missing_strategy == MISSING_STRATEGY_JOVIAL ) {
		if ( rh.isJovial())
			return "";
		else 
			throw new Exception ( "We are not jovial. Select-Loop [id="+Entities.importEntities(idN)+"] didnt delivered a value: "+ message);
	} else if ( missing_strategy == MISSING_STRATEGY_EXCEPTION ) {
		throw new Exception ( "Select-Loop [id="+Entities.importEntities(idN)+"] didnt delivered a value: "+ message);
	} else {
		throw new Exception ( "London, we have a problem. call the boss!");
	}
}
	
public void debugx(int index, Level level, LogConnector myLc) {
	
	if ( index == 0 )
		myLc.log(Interpreter.declareDebug
				+Commons.trailBlanks("loop",Interpreter.lb)
				+Commons.leadBlanks(idE, Interpreter.lb2)
				+"= ..."
				/*+filename
				+","
				+iteratortext*/
				, level);
	else
		myLc.log(Interpreter.declareDebug
				+Commons.trailBlanks("",Interpreter.lb)
				+Commons.leadBlanks(idE, Interpreter.lb2)
				+"= ..."
				/*+filename
				+","
				+iteratortext*/
				, level);			
	
	
}
@Override
public void next() throws Exception {
	// TODO Auto-generated method stub
	
}	
	

public static void staticStartup() {
	missingtypes = new KeyWordMapper("SpiritMissingType",3);
	missingtypes.put("empty", MISSING_STRATEGY_EMPTY);
	missingtypes.put("exception", MISSING_STRATEGY_EXCEPTION);
	missingtypes.put("jovial_like_interpreter", MISSING_STRATEGY_JOVIAL);
	missingtypes.putFinished();	
}
	
}