package at.nicolas.x.hibiscus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;

public class ValiResult {
	
	ArrayList<String> warnings = new ArrayList<String>();
	ArrayList<String> errors = new ArrayList<String>();
	
	ValiResult parent = null;
	
	String id = null;
	String idShort = null;
	String name = null;
	
	int baseLevel = 0;
	
	TaskLogger tl = null;

	Element v = null;
	Element vCore = null;
	
protected Imp buildImp(ImParasFilter imParasFilter, int point_index) throws Exception {
	String nachwaer = Commons.StringTimes("=", (IntParas.pointnames[point_index]).length());
	tl.is("  "+nachwaer,0);
	tl.is("> "+IntParas.pointnames[point_index]+ " < Building Realtime-IndexSzenario: "+id+" ...", 0);

	tl.is("  "+nachwaer,0);
	Imp imp = new Imp(id,point_index);
	
	List<Element> imaList = v.getChildren("ima");
	Iterator<Element> iIma = imaList.iterator();
	int imaPos = 0;
	// schleife je ima im imp-xml
	while ( iIma.hasNext() ) {
		imaPos++;
		ArrayList<String> reasons = new ArrayList<String>();
		Element ie = (Element)iIma.next();
		String id ="IMA (pos="+imaPos+" row="+ie.getAttributeValue("row")+") "+
				    "IMP (point="+point_index+1+" row="+v.getAttributeValue("row")+")";
		
		String idShort ="IMA (pos="+imaPos+" row="+ie.getAttributeValue("row")+")" ;		
				
		// on/offs filtern
		if ( ie.getAttributeValue("on").equals("0"))
			reasons.add("The @on is OFF");
		
		// runstat
		if ( ie.getAttribute("cond") != null && ie.getAttributeValue("cond").trim().length() > 0) {
			if ( Commons.hasFlag(ie.getAttributeValue("cond"), "stat") && !imParasFilter.runstatsOn)
				reasons.add("Statistic not performed during this integration-request");
		}
		
		// pronto
		if ( ie.getAttribute("pronto") != null && ie.getAttributeValue("pronto").trim().length() > 0) {
			String mp = ie.getAttributeValue("pronto").trim();
			
			if ( mp.equals("i0") ) {
				if ( imParasFilter.pronto != 0 )
					reasons.add("Pronto should be 0 but it is '"+imParasFilter.pronto+"'");			
			}
			
			else if ( mp.equals("i1") ) {
				if ( imParasFilter.pronto != 1 )
					reasons.add("Pronto should be 1 but it is '"+imParasFilter.pronto+"'");			
			}	
			
			else if ( mp.equals("l2") ) {
				if ( !(imParasFilter.pronto < 2) )
					reasons.add("Pronto should be lower 2 but it is '"+imParasFilter.pronto+"'");			
			}	
			
			else if ( mp.equals("i2") ) {
				if ( imParasFilter.pronto != 2 )
					reasons.add("Pronto should be 2 but it is '"+imParasFilter.pronto+"'");			
			}				
		}		
		
		// dbms
		if ( ie.getAttribute("dbms") != null && ie.getAttributeValue("dbms").trim().length() > 0) {
			if ( !Commons.hasFlag(ie.getAttributeValue("dbms"), imParasFilter.dbms))
				reasons.add("Dbms does not match, requestd was '"+ie.getAttributeValue("dbms").trim()+
						"' but it is '"+imParasFilter.dbms+"'");
		}	
		
		// cust_flag
		if ( ie.getAttribute("cust_flag") != null && ie.getAttributeValue("cust_flag").trim().length() > 0) {
			if ( ie.getAttributeValue("cust_how").equals("one")) {
				if ( !Commons.hasFlag(imParasFilter.cust_flag,ie.getAttributeValue("cust_flag") ))
					reasons.add("Customer-Condition is not present, requested was '"+ie.getAttributeValue("cust_flag")+"'"
							+
							" but it is '"+imParasFilter.cust_flag+"'");				
			} else if ( ie.getAttributeValue("cust_how").equals("and")) {
				// alle cust_flags muessen gegeben sein
				boolean result = true;
				ArrayList<String> cf = split(ie.getAttributeValue("cust_flag"));
				
				for ( int i = 0; i<cf.size(); i++ ) {
					String flag = cf.get(i);
					result = result && Commons.hasFlag(imParasFilter.cust_flag, flag);
				}
				if ( !result)
					reasons.add("Customer-Condition is not present, requested AND-Conditions have been '"+ie.getAttributeValue("cust_flag")+"'"+
							" but it is '"+imParasFilter.cust_flag+"'");		
				
			} else if ( ie.getAttributeValue("cust_how").equals("or")) {
				// oder, eines reicht
				boolean result = false;
				ArrayList<String> cf = split(ie.getAttributeValue("cust_flag"));
				
				for ( int i = 0; i<cf.size(); i++ ) {
					String flag = cf.get(i);
					result = result || Commons.hasFlag(imParasFilter.cust_flag, flag);
				}
				if ( !result)
					reasons.add("Customer-Condition is not present, requested OR-Conditions have been '"+ie.getAttributeValue("cust_flag")+"'"+
							" but it is '"+imParasFilter.cust_flag+"'");		
				
				
			} else
				throw new Exception("ValiResult.buildImp: Internal error with Customer-Condition-Treatment, found treatment was '"+ie.getAttributeValue("cust_how"));
			
		}	
		
		// breakers
		if ( ie.getAttributeValue("type").equals("break") &&
			 !imParasFilter.breakOn	)
			reasons.add("Breakpoints are in this integration-request turned off");
		
		// fertig, schaun ob das passt
		if ( reasons.size()==0 ) {
			// wird effektive
			Ima ima = new Ima(
							id, idShort,
							ie.getAttributeValue("type"), 
							ie.getAttributeValue("cmd"),
							ie.getAttributeValue("catch"));
			imp.addIma(ima);
			tl.is("OK EFFECTIVE "+id+" "+Commons.toTextCompact(ie),1);
		} else {
			tl.is(" X  REJECTED "+id+" "+Commons.toTextCompact(ie),1);
			String nachwaerts = Commons.StringTimes(" ", (" X REJECTED  "+id+" ").length());
			for ( int i = 0; i<reasons.size(); i++ ) {
				String reason = reasons.get(i);
				tl.is(nachwaerts+reason,1);

			}
		}
			
	}
	
	return imp;
	
}
	
private void validateRow() {
	// @row kontrollieren
	if ( v.getAttribute("row") == null ) 
		appendError("Has not attribute @row which identifies the row in the xml");
	else if ( !Commons.isInt(v.getAttributeValue("row")))
		appendError("Attribute @row is not numeric ["+v.getAttributeValue("row")+"]");	
}

private void validateOn() {
	// @on kontrollieren
	if ( v.getAttribute("on") == null ) 
		appendError("Has not attribute @on");
	else if ( !(v.getAttributeValue("on").equals("1") ||v.getAttributeValue("on").equals("0") ) )
		appendError("Attribute @on is not 0 or 1 ["+v.getAttributeValue("on")+"]");	
}
protected void validateImp(Element _validated ) {
	// System.out.println("validateImp: "+_validated.toString());
	
	v = _validated;
	vCore = v.clone();

	// das ist ein im, da wird gekürztSystem.out.println("impElement="+Commons.toText(impElement));
	//validatedCore.removeChildren("ima");
	vCore.removeContent();
	
	tl.is("Validating IMP"+id.substring(4)+ "                   <"+Commons.toTextCompact(vCore)+">", 0);
	//+Commons.toTextCompact(_validated)
	
	validateRow();
	validateOn();

	
	// @point kontrollieren
	if ( v.getAttribute("point") == null ) 
		appendError("Has not attribute @point (should be between 1 and "+
				(IntParas.pointnames.length)+")");
	else if ( !Commons.isInt(v.getAttributeValue("point")))
		appendError("Attribute @point is not numeric ["+v.getAttributeValue("point")+"]");
	else if ( Commons.string2int(v.getAttributeValue("point")) < 1 || 
			Commons.string2int(v.getAttributeValue("point")) > (IntParas.pointnames.length) )
		appendError("Attribute @point is not between 1 and "+
				(IntParas.pointnames.length)+" but it is:"+v.getAttributeValue("point")+"");
	

	
}

protected void validateIma(Element _validated ) {
	v = _validated;
	vCore = _validated;
	String vorwaerts = "Validating "+id+ " ";
	
	tl.is(vorwaerts+Commons.toTextCompact(v), 0);
	
	String nachwaerts = Commons.StringTimes(" ", vorwaerts.length());
	
	//tl.is("Validating "+id+ " "+Commons.toTextCompact(v), 2);
	tl.is(nachwaerts+name,0);
	
	validateRow();
	validateOn();	
	
	// @type kontrollieren
	if ( v.getAttribute("type") == null ) 
		appendError("Has not attribute @type");
	else if ( !(v.getAttributeValue("type").equals("cmd") ||
			v.getAttributeValue("type").equals("commit")||
			v.getAttributeValue("type").equals("break")||
			v.getAttributeValue("type").equals("sel") ) )
		appendError("Attribute @type is not 'cmd' or 'sel' or 'commit' or 'break' ["+v.getAttributeValue("type")+"]");	
	
	// type mit cmd
	if (v.getAttribute("type") != null ) {
		
		// type==cmd
		if ( (v.getAttributeValue("type").equals("cmd")||v.getAttributeValue("type").equals("sel")) &&
			 (v.getAttribute("cmd")== null || v.getAttributeValue("cmd").trim().length() == 0)	)
			appendError("Attribute @cmd is empty although requested for type=cmd/query");
		
		// type==break
		if ( v.getAttributeValue("type").equals("break") ) {
			if ( v.getAttribute("cmd")!= null )
				appendWarning("Attribute @cmd is filled although not requested for type=break ["+
						v.getAttributeValue("cmd"));
		}
		
		// type==commit
		if ( v.getAttributeValue("type").equals("commit") ) {
			if ( v.getAttribute("cmd")!= null )
				appendWarning("Attribute @cmd is filled although not requested for type=commit ["+
						v.getAttributeValue("cmd"));
		}				
	}
	
	// @catch kontrollieren
	if ( v.getAttribute("catch") == null ) 
		appendError("Has not attribute @catch");
	else if ( !(v.getAttributeValue("catch").equals("1") ||v.getAttributeValue("catch").equals("0") ) )
		appendError("Attribute @catch is not 0 or 1 ["+v.getAttributeValue("catch")+"]");	
	
	// in cond darf es nur einen wert geben
	if ( v.getAttribute("cond") != null ) {
		ArrayList<String> conds = split(v.getAttributeValue("cond"));
		
		if ( conds.size() == 0 );
		else if ( conds.size()==1 && !(conds.get(0).equals("stat")))
			appendError("Attribute @cond may only be 'stat' ["+v.getAttributeValue("cond")+"]");
		else if ( conds.size() > 2 )
			appendError("Attribute @cond may only have one option 'stat' ["+v.getAttributeValue("cond")+"]");
	}
	
	// in dbms 
	if ( v.getAttribute("dbms") != null ) {
		ArrayList<String> dbms = split(v.getAttributeValue("dbms"));
		
		for ( int i = 0; i<dbms.size(); i++ ) {
			String db = dbms.get(i);
			if ( !(db.equals("maria") || db.equals("mysql") || db.equals("postgres") ))
				appendError("Attribute @dbms may only be 'maria', 'mysql' or 'postgres' but found ["+db+"]");
		}
	}	
	
	// @pronto kontrollieren
	if ( v.getAttribute("pronto") == null || v.getAttributeValue("pronto").trim().length()==0); 
	else if ( !(v.getAttributeValue("pronto").equals("i0") ||
			v.getAttributeValue("pronto").equals("i1") ||
			v.getAttributeValue("pronto").equals("l2") ||			
			v.getAttributeValue("pronto").equals("i2") ) )
		appendError("Attribute @cust_how is not 'i0' or 'i1' or 'l2' or 'i2' ["+v.getAttributeValue("pronto")+"]");	
	
	
	// @cust_how kontrollieren
	if ( v.getAttribute("cust_how") == null || v.getAttributeValue("cust_how").trim().length()==0); 
	else if ( !(v.getAttributeValue("cust_how").equals("one") ||
			v.getAttributeValue("cust_how").equals("and") ||
			v.getAttributeValue("cust_how").equals("or") ) )
		appendError("Attribute @cust_how is not 'one' or 'and' or 'or' ["+v.getAttributeValue("cust_how")+"]");	
	
	// @cust_flag
	if ( v.getAttribute("cust_flag") == null || v.getAttributeValue("cust_flag").trim().length()==0  ) {
		// kein cust_flag
		if ( v.getAttribute("cust_how") != null && v.getAttributeValue("cust_how").trim().length()>0) 
		appendError("Attribute @cust_how is not given although requed for @cust_flag [cust_flag="+v.getAttributeValue("cust_flag")+"]");	
	} else {
		// mit cust_flag
		if ( v.getAttribute("cust_how") == null || v.getAttributeValue("cust_how").trim().length()==0 ) 
		appendError("Attribute @cust_how is given although requed for @cust_flag [cust_flag="+v.getAttributeValue("cust_flag")+"]");	
		
	}
}

protected static ArrayList<String> split(String txt) {
	ArrayList<String> results = new ArrayList<String>();
	
	if ( txt== null) 
		return results;
	
	String regex = "\\s+";
	
	String founds[] = txt.split(regex);;

	for ( String found: founds ) {
		
		if ( found.length()> 0 ) {
			results.add(found);
		}
	}
	
	 		 
	return results;
}

ValiResult () {
	baseLevel = 0;
}

ValiResult (ValiResult _parent,String impId) {
	parent = _parent;
	id = impId;	
	baseLevel = 1;
}

ValiResult (ValiResult _parent ,String imaId, String imaIdShort, String imaName) {
	parent = _parent;
	id = imaId;
	idShort = imaIdShort;
	name = imaName;
	baseLevel = 2;
}

protected void setTaskLogger (TaskLogger _taskLogger) {
	tl = _taskLogger;
}
protected void printErrorsAndWarnings() {
	printErrors();
	printWarnings();
}
protected void printErrors() {
	for ( int i = 0; i<errors.size(); i++ ) {
		String error = errors.get(i);
		tl.is("           [ ERROR ] "+error, 1);
	}
}


protected void printWarnings() {
	for ( int i = 0; i<warnings.size(); i++ ) {
		String warning = warnings.get(i);
		tl.is("           [ WARN ] "+warning, 1);
	}
}
protected void propagateErrors() {
	
	for ( int i = 0; i<errors.size(); i++ ) {
		String error = errors.get(i);
		if ( idShort != null )
			parent.appendError(idShort+" -> "+error);
		else
			parent.appendError(id+" -> "+error);

	}
}


protected void appendError(String error ) {
	// System.out.println("-- appended error "+error);
	errors.add(error);
}

private void appendWarning(String warning ) {
	//System.out.println("-- appended warning "+warning);
	warnings.add(warning);
}

protected boolean hasErrors() {
	if ( errors.size() > 0 )
		return true;
	else
		return false;
}

protected boolean hasWarnings() {
	if ( warnings.size() > 0 )
		return true;
	else
		return false;
}
protected boolean hasErrorOrWarnings() {
	return ( hasErrors() || hasWarnings());
}
	
}
