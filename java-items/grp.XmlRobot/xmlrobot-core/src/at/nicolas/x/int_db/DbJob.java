/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;

public class DbJob {
	
	public static final String V_LEVEL_DB = "ws";
	public static final String V_LEVEL_APP = "app";
	public static final String V_LEVEL_INST = "pro";

	public static final String V_ROLE_HIDDEN = "hidden";
	public static final String V_ROLE_DEV = "dev";
	public static final String V_ROLE_TEST = "test";
	public static final String V_ROLE_ADMIN = "admin";
	public static final String V_ROLE_USER = "user";
	public static final String V_ROLE_EXPERT = "expert";
	public static final String V_ROLE_PUBLIC = "public";
	public static final String V_ROLE_BUSINESS = "business";
	
	
	private String fullfn = null;
	private String fullfnEscaped = null;
	private String jobid = null;
	private String jobidEscaped = null;
	private String jobinfo = null;
	private String jobinfoEscaped = null;
	
	private String v_role = null;
	private String v_level = null;
	
	private int v_role_index = -1;

	private String f_lang = null;
	private String f_os = null;
	private String f_computer = null;
	private String f_langEscaped = null;
	private String f_osEscaped = null;
	private String f_computerEscaped = null;
	private String f_class_menu = null;
	private int pri = -1;

	private String tip = null;
	private String tipEscaped = null;
	private Element element  = null;
	private DbJm jm = null;
	
	private boolean tl_scroll = false;
	
	public boolean getTlScroll() {
		return tl_scroll;
	}
	private ArrayList<Element> prolog = null;

	public Element getDbDump (boolean jobfile, boolean natural) {
		Element x = new Element("job");
		if ( natural ) {
			if ( jobfile )
				x.setAttribute("file", fullfn);
			x.setAttribute("id", jobid);
			if ( v_role != null )
				x.setAttribute("role", v_role);
			if ( v_level != null )
				x.setAttribute("joblevel", v_level);
			if ( f_lang != null )
				x.setAttribute("lang", f_lang);
			if ( f_os != null )
				x.setAttribute("os", f_os);
			if ( f_computer != null )
				x.setAttribute("computer", f_computer);
			if ( tip != null )
				x.setAttribute("tip", tip);
			if ( jobinfo != null )
				x.setAttribute("info", jobinfo);
			if ( pri != -1 )
				x.setAttribute("pri", Commons.int2String(pri));			
		} else {
			if ( jobfile )
				x.setAttribute("file", fullfnEscaped);
			x.setAttribute("id", jobidEscaped);
			if ( v_role != null )
				x.setAttribute("role", v_role);
			if ( v_level != null )
				x.setAttribute("joblevel", v_level);
			if ( f_lang != null )
				x.setAttribute("lang", f_langEscaped);
			if ( f_os != null )
				x.setAttribute("os", f_osEscaped);
			if ( f_computer != null )
				x.setAttribute("computer", f_computerEscaped);
			if ( f_class_menu != null )
				x.setAttribute("class_menu", f_class_menu);			
			if ( tip != null )
				x.setAttribute("tip", tipEscaped);
			if ( jobinfo != null )
				x.setAttribute("info", jobinfoEscaped);
			if ( pri != -1 )
				x.setAttribute("pri", Commons.int2String(pri));			
		}


		return x;
	}
	
	public DbJm getJm() {
		return jm;
	}
	// storing job into memory, _element has all steps
	public DbJob(	String __fullfn,
					String _jobid,
					String _role,
					String _lang,
					String _os,
					String _computer,
					String _desc,
					String _levels,
					Element _element,
					ArrayList<Element>header,
					DbJm _jm,
					String _jobinfo,
					String _pri,
					CommStartMsg sm,
					ISitzung dbJobSitzung,
					String _class_menu) throws Exception {
		fullfn = Commons.constructNormalizedFile(__fullfn);
		f_class_menu = _class_menu;
		
		fullfnEscaped = Entities.importEntities(fullfn);

		jobid = _jobid;
		jobidEscaped = Entities.importEntities(_jobid);
		v_role = _role;
		v_role_index = Rollen.getRoleHeightDefaultUser(v_role);
		
		f_lang = _lang;
		f_langEscaped = Entities.importEntities(_lang);
		
		f_os = _os;
		f_osEscaped = Entities.importEntities(_os);
		
		f_computer = _computer;
		f_computerEscaped = Entities.importEntities(_computer);
		
		tip = _desc;
		tipEscaped = Entities.importEntities(_desc);
		
		v_level = _levels;
		
		jobinfo = _jobinfo;
		jobinfoEscaped = Entities.importEntities(_jobinfo);
		// sm.out.println("called job ["+jobid+"] dbJob for"+_fullfn);
		try {
			pri = Commons.string2int(_pri);
		} catch (Exception e) {
			pri = 1;
		}
		if ( v_level.equals("inst"))
			v_level = V_LEVEL_INST;
		element = _element;
		
		// 0802 here should be the steps 'insert' be manipulated
		// sm.out.println("current element has name "+element.getName());
		List<Element> insert_nodes = Commons.xpathSelectNodes ( element, ".//insert" );
		// sm.out.println("found ["+insert_nodes.size()+"] insertnodes in "+fullfn);
		if ( insert_nodes.size() > 0 ) {
			// there are inserts,
			
			// _fullfn has the filename, we want the directory
			String home = Commons.getDirOfFile(fullfn);
			
			sm.out.println(jobid+"=job ["+insert_nodes.size()+"] insertnodes in "+fullfn);
			
			Iterator<Element> i = insert_nodes.iterator();
			while ( i.hasNext()) {
				// this goes here for every insert node
				// sm.out.println("2) INSERT under test: found an insert-node");
				
				Element current_insert = (Element)i.next();
				
				// 1) read attribute file
				String fn = current_insert.getAttributeValue("file");
				if ( fn == null )
					throw new Exception("insert needs attribute 'file' happend in file:"+fullfn);
				
				// compose full filename
				String ffn = home+fn;
				//sm.out.println("3) INSERT under test: Trying to insert: "+ffn);
				Document document = CommonsVfs.getXmldocumentByFilename(ffn,dbJobSitzung);
				Element root = document.getRootElement();
				
				// getting parent and from that position number
				Element parent = current_insert.getParentElement();
				int posInParent = -1;
				if ( parent != null ) {
					
					
					posInParent = parent.indexOf(current_insert);		
					// sm.out.println("4) selected index ("+posInParent+")parent node :"+parent.toString());				
				} else {
					throw new Exception("Sollte nicht sein");
				}
				
				// eine schleife �ber die reinzukopierenden
				List<Element> ch = root.getChildren();
				Iterator iter = ch.iterator();
				int offset = 0;
				//sm.out.println("X) removing pos ["+posInParent+"]:"+"["+(parent.getContent(posInParent)).toString()+"]:");
				parent.removeContent(posInParent);
				offset = -1;
				while ( iter.hasNext()) {
					Element e2i = (Element)iter.next();
					parent.addContent(posInParent+offset, e2i.clone());
					// sm.out.println("5) inserting :"+e2i.toString()+"on pos +posInParent="+posInParent+"+offset="+offset+" "+(posInParent+1+offset));
					// sm.out.println("");
					offset++;
				}
				/*
				sm.out.println("posInParent="+posInParent);
				sm.out.println("offset="+offset);
				sm.out.println("contentsize="+parent.getContentSize());
				*/
				
				int kill = posInParent+offset-2;
				// sm.out.println("6) removing pos ["+kill+"]:");
				
				// sm.out.println("6) removing pos ["+(kill)+"]:"+parent.getChildren().get(kill).toString());
				
				// parent.removeContent(kill);
				// sm.out.println("inserting : ready");			
			}
			//sm.out.println("inserting : ready 2");
			
		}
		
		jm = _jm;

		prolog = new ArrayList<Element>();
		
		prolog.addAll(header); // header
		Element a = new Element("dir");
		a.setAttribute("id", "job");
		a.setAttribute("flag", "lock");
		a.setText(Commons.getDirOfFile(fullfnEscaped));
		
		prolog.add(a);
	
		
		Element a2 = new Element("prop");
		a2.setAttribute("id", "job");
		a2.setAttribute("flag", "lock");
		a2.setText(jobidEscaped);		
		prolog.add(a2);
		
	}

	public void print_id () {
		System.out.print("| "+ jobid);
	}
	
	public void print () {
		Sp.p("+JmEntry : "+fullfn+", "+jobid+", "+v_role+", "+f_lang+", "+f_os+", "+tip);
	}
	
	public void print_full () {
		Sp.p("+JmEntry : "+fullfn+", "+jobid+", "+v_role+", "+f_lang+", "+f_os+", "+tip+", \n"+Commons.toText(element));
	}	
	
	public String getFullfn() {
		return fullfn;
	}
	public String getFullfnEscaped() {
		return fullfnEscaped;
	}

	public String getJobid() {
		return jobid;
	}
	public String getJobidEscaped() {
		return jobidEscaped;
	}	

	public String getRole() {
		return v_role;
	}
	
	public int getRoleIndex() {
		return v_role_index;
	}	

	public String getLang() {
		return f_lang;
	}
	public int getPri() {
		return pri;
	}

	public String getOs() {
		return f_os;
	}
	
	public String getComputer() {
		return f_computer;
	}	
	
	public String getJobinfo() {
		return jobinfo;
	}		

	public String getDesc() {
		return tip;
	}
	
	public String getLevel() {
		return v_level;
	}	

	public Element getElement() {
		return element;
	}

	public boolean hasExplain () {
		if ( jm.selectJob(jobid+"?") != null)
			return true;
		else
			return false;
	}
	
	public boolean isExplain () {
		if ( jobid.endsWith("?"))
			return true;
		else
			return false;
	}
	
	public String getJobText() {
		return Commons.toText(element);
	}
	
	public String getPrologText() {
		String ret = new String("");
		Iterator<Element> it = prolog.iterator();
		int max_size = prolog.size() -2 ;
		int pos = 0;
		while ( it.hasNext() ) {
			
				Element now = (Element) it.next();
			if ( pos < max_size ) {
				ret = ret + Commons.toText(now) + "\n";
			}			
			pos++;
		}
		return ret;
	}
	

	


	
	

	
	public static boolean isLevelValid( String x ) {
		if ( x.equals(V_LEVEL_DB) || x.equals(V_LEVEL_APP) || x.equals(V_LEVEL_INST)  )
			return true;
		else
			return false;
	}	
	
	public static int getLevelHeight ( String x ) {
		int ret;
		if ( x == null ) ret = 1;
		else if ( x.equals(V_LEVEL_DB)) ret = 3;
		else if ( x.equals(V_LEVEL_APP)) ret = 2;
		else if ( x.equals(V_LEVEL_INST)) ret = 1;
		else ret = 1;
		
		return ret;
	}	

		
	public ArrayList<Element> getExeProlog() {
		return prolog;
	}
		
	
	public String getToolTip () {
		String tt = this.getDesc();
		if ( tt == null ) tt = "?";
		if ( getLang() != null || getOs() != null || getComputer() != null ) {
			tt = tt.concat(" (");
			if ( getLang() != null )
				tt = tt + "lang="+getLang()+" ";
			if ( getOs() != null )
				tt = tt + "os="+getOs()+" ";
			if ( getComputer() != null )
				tt = tt + "computer="+getComputer()+" ";			
			tt = tt.concat(")");			
		}

		if ( tt.equals("?"))
			return null;
		else
			return tt;
	}
	public String getToolTipHelp () {
		/*
		if ( this.getDesc() != null )
			return ( "Help: "+this.jobid+": "+this.getDesc() );
		else
			return ("");
			*/
		return null;
	}	

}