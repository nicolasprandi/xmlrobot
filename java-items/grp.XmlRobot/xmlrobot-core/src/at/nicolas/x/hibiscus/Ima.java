package at.nicolas.x.hibiscus;

import at.nicolas.x.int_specs.Commons;

public class Ima {
	protected static final int type_cmd = 0;
	protected static final int type_commit = 1;
	protected static final int type_break = 2;
	protected static final int type_sel = 3;
	
	String id;
	String idShort;
	int type; 
	String typeText;
	boolean catcher;
	String cmd = null;
	
	Ima(String _id, String _idShort, String _type, String _cmd, String _catcherText) throws Exception {
		id = _id;
		idShort = _idShort;
		this.typeText = _type;
		if ( _type.equals("cmd")) {
			type = type_cmd;
			cmd = _cmd;
		}else if ( _type.equals("sel")) {
			type = type_sel;
			cmd = _cmd;
		} else if ( _type.equals("break"))
			type = type_break;
		else if ( _type.equals("commit"))
			type = type_commit;
		else
			throw new Exception("Ima.create: Unknow type ["+_type+"] for ima["+_id+"]");
		
		catcher = Commons.stringtoBoolean(_catcherText, "@catch must be '0' or '1'");		
		
	}
	protected String getDesciption ( ) {
		String mainer = "";
		if ( type == type_cmd || type == type_sel)
			mainer="       "+cmd.replaceAll("\n", " ");
		else if ( type == type_commit )
			mainer="Commit ";
		else if ( type == type_break )
			mainer="Break  ";
			
		String p = "";
		if (!catcher) p=" (mayFail)";
		return mainer.concat(p);

	}
	
	protected String getDesciptionWithId ( ) {

		return getDesciption().concat(" "+idShort);
	}	
	protected void isSum (TaskLogger tl ) {
	
		tl.is("   "+Commons.trailBlanks(idShort, 20)+" "+getDesciption(),0);
	
	}

}
