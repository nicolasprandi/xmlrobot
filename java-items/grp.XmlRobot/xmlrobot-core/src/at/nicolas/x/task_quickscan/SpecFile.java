package at.nicolas.x.task_quickscan;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;

public class SpecFile extends SpecPath {

	// base = name + dot + ext
	String base_equals = null;
	String name_equals = null;
	String dot_equals = null;
	String ext_equals = null;

	String base_regex = null;
	String name_regex = null;
	String dot_regex = null;
	String ext_regex = null;
	
	public String getFileDocu() {
		String x =
			"base_equals = ["+base_equals + "], "+
			"name_equals = ["+ name_equals+ "], "+		
			"dot_equals  = ["+dot_equals + "], "+	
			"ext_equals  = ["+ext_equals + "], "+	
			"base_regex  = ["+base_regex + "], "+
			"name_regex  = ["+ name_regex+ "], "+	
			"dot_regex   = ["+ dot_regex+ "], "+	
			"ext_regex   = ["+ext_regex + "], "+				

				"";
		return x;
	}
	public SpecFile(Element pathSpec) {
		super(pathSpec);
		base_equals = pathSpec.getAttributeValue("base_equals");
		name_equals = pathSpec.getAttributeValue("name_equals");
		dot_equals = pathSpec.getAttributeValue("dot_equals");
		ext_equals = pathSpec.getAttributeValue("ext_equals");
		
		base_regex = pathSpec.getAttributeValue("base_regex");
		name_regex = pathSpec.getAttributeValue("name_regex");
		dot_regex = pathSpec.getAttributeValue("dot_regex");
		ext_regex = pathSpec.getAttributeValue("ext_regex");		 
		// TODO Auto-generated constructor stub
	}
	
	public boolean fullfillsFileCriterias(DirFileProps dfp) {
	
		if ( !fullfillsDirCriterias(dfp) )
			return false;
		
		if ( base_equals != null ) {
			if ( ! dfp.compareFileBase.equals(base_equals))
				return false;
		}
		
		if ( name_equals != null ) {
			if ( ! dfp.compareFileName.equals(name_equals))
				return false;
		}
		
		if ( dot_equals != null ) {
			if ( ! dfp.compareFileDot.equals(dot_equals))
				return false;
		}
		
		if ( ext_equals != null ) {
			if ( ! dfp.compareFileExt.equals(ext_equals))
				return false;
		}		
		
		if ( base_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareFileBase, base_regex))
				return false;
		}

		if ( name_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareFileName, name_regex))
				return false;
		}	
		
		if ( dot_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareFileDot, dot_regex))
				return false;
		}	
		
		if ( ext_regex != null ) {
			if ( !Commons.stringMatchesRegex(dfp.compareFileExt, ext_regex))
				return false;
		}		
		return true;
		
		
	}

}
