package at.nicolas.x.task_quickscan;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;

public class SpecPath {
	//Element pathSpec;

	

	
	String ppath_equals = null;			// path				equals
	String ppath_regex = null;			// path				regex
	String ipath_equals = null;			// ipath			equals
	String ipath_regex = null;			// ipath			regex	
	String rpath_equals = null;			// rpath			equals
	String rpath_regex = null;			// rpath			regex
	
	
	String level_eq = null;		// level			eq  =
	String level_leq = null;		// level			leq <=
	String level_l = null;			// level			l   <
	String level_geq = null;		// level			geq >=
	String level_g = null;			// level			g	>
	
	Integer ilevel_eq = null;
	Integer ilevel_leq = null;
	Integer ilevel_l = null;	
	Integer ilevel_geq = null;	
	Integer ilevel_g = null;		
	
	
	
	public String getPathDocu() {
		String x =
				"ipath_equals="+ipath_equals + ", "+
				"path_level_eq="+ilevel_eq + ", "+
				"path_level_leq="+ ilevel_leq+ ", "+
				"path_level_l="+ ilevel_l+ ", "+
				"path_level_geq="+ ilevel_geq+ ", "+
				"path_level_g="+ ilevel_g+ ", "+				

				"";
		return x;
	}
	
	public boolean fullfillsDirCriterias(DirFileProps dfp)
			 {
		Sp.p("SpecPath.fullfillsCriterias? ");
		

		Sp.p("[ iPath ]    = [ "+dfp.compareIPath+" ] ");
		Sp.p("[ rPath ]    = [ "+dfp.compareRPath+" ] ");	
		Sp.p("[ pPath ]     = [ "+dfp.comparePPath+"] ");			
		Sp.p("[ depth ]    = [ "+dfp.compareDepth+" ] ");

		
		
		if ( ppath_equals != null ) {
			if ( ! dfp.comparePPath.equals(ppath_equals))
				return false;
		}		
		if ( ppath_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.comparePPath, ppath_regex))
				return false;
		}	
		
		if ( ipath_equals != null ) {
			if ( ! dfp.compareIPath.equals(ipath_equals))
				return false;
		}		
		if ( ipath_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareIPath, ipath_regex))
				return false;
		}
		
		if ( rpath_equals != null ) {
			if ( ! dfp.compareRPath.equals(rpath_equals))
				return false;
		}		
		if ( rpath_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareRPath, rpath_regex))
				return false;
		}			
				
			
	
		
		if ( ilevel_eq!= null ) {
			if ( ! ( ilevel_eq.intValue() ==  dfp.compareDepth ) ) 
				return false;			
		}
		
		if ( ilevel_leq != null ) {
			if ( ! ( dfp.compareDepth <= ilevel_leq.intValue()  ) ) 
				return false;
		}
		if ( ilevel_l != null ) {
			if ( ! ( dfp.compareDepth < ilevel_l.intValue()) ) 
				return false;
		}	
		
		if ( ilevel_geq != null ) {
			if ( ! (  dfp.compareDepth >= ilevel_geq.intValue()) ) 
				return false;
		}
		if ( ilevel_g != null ) {
			if ( ! (   dfp.compareDepth > ilevel_g.intValue()) ) 
				return false;
		}			

		
		return true;
		
	}
	SpecPath( Element pathSpec ) {
		// pathSpec = _pathSpec;
		
	
		
		ppath_equals = pathSpec.getAttributeValue("ppath_equals");
		ppath_regex = pathSpec.getAttributeValue("ppath_regex");
		
		ipath_equals = pathSpec.getAttributeValue("ipath_equals");
		ipath_regex = pathSpec.getAttributeValue("ipath_regex");
		
		rpath_equals = pathSpec.getAttributeValue("rpath_equals");
		rpath_regex = pathSpec.getAttributeValue("rpath_regex");		

		level_eq = pathSpec.getAttributeValue("level_eq");
		if ( level_eq != null )
			ilevel_eq = new Integer( Commons.string2int(level_eq));
		
		level_leq = pathSpec.getAttributeValue("level_leq");
		if ( level_leq != null )
			ilevel_leq = new Integer( Commons.string2int(level_leq));	
		
		level_l = pathSpec.getAttributeValue("level_l");
		if ( level_l != null )
			ilevel_l = new Integer( Commons.string2int(level_l));		
		
		level_geq = pathSpec.getAttributeValue("level_geq");
		if ( level_geq != null )
			ilevel_geq = new Integer( Commons.string2int(level_geq));	
		
		level_g = pathSpec.getAttributeValue("level_g");
		if ( level_g != null )
			ilevel_g = new Integer( Commons.string2int(level_g));			
	}

}
