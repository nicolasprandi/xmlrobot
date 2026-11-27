/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;



public class MountConfig {


	public static final int DIR_DATA          =  0;

	public static final int DIR_SERVICES  =  4;
	public static final int DIR_SERVICE_APPS  =  25;
	public static final int FILE_SYSTEMCALLS_BUILTIN  =  13;

	public static final int DIR_COMPILESRC    =  5;
	public static final int DIR_COMPILEBIN    =  6;
	public static final int FILE_COMPILEJAR   =  7;
	public static final int PROP_COMPUTER     =  8;
	public static final int DIR_BACKUP        = 10;
	public static final int DIR_FASTDATA      = 11;
	public static final int DIR_SYSTEMP       = 12;
	public static final int DIR_ANTLIBS_BUILTIN       = 16;
	public static final int DIR_ANTLIBS_CUSTOM       = 17;
	public static final int DIR_JAVA_COMPILE_SRC_STD       = 19;
		
	public static final int FILE_GUI_PERSPLIST       = 20;
	public static final int FILE_GUI_STATUS       = 21;
	public static final int FILE_GUI_RECALL       = 22;
	public static final int FILE_COMPILE_XML       = 23;

	public static final int DIR_LIBS       = 24;	
	ArrayList<Attribute> list = new ArrayList<Attribute>();
	

	private Element mountConfig = null;
	private Element mountConfigS = null;
	private boolean isOnline = false;
	

	public MountConfig(boolean _isOnline) {
		isOnline = _isOnline;
		
	}


	/**
	 * @param id
	 * @return
	 */
	public String getValue ( int id ) {
		String retval = null;
		if ( id == DIR_DATA )                retval = getValue("dir","data");
		else if ( id == DIR_SERVICES )   retval = getValue("dir","services");
		else if ( id == DIR_SERVICE_APPS )   retval = getValue("dir","service_apps");
		else if ( id == FILE_SYSTEMCALLS_BUILTIN )  retval = getValue("file","systemcalls_builtin");
		else if ( id == DIR_COMPILEBIN )     retval = getValue("dir","compileBin");
		else if ( id == FILE_COMPILEJAR )    retval = getValue("file","compileJar");
		else if ( id == PROP_COMPUTER )      retval = getValue("prop","computer");
		else if ( id == DIR_BACKUP )         retval = getValue("dir","backup");
		else if ( id == DIR_FASTDATA )       retval = getValue("dir","fastdata");
		else if ( id == DIR_SYSTEMP )        retval = getValue("dir","systemp");
		else if ( id == DIR_LIBS )        	retval = getValue("dir","libs");		
		else if ( id == DIR_ANTLIBS_BUILTIN )        retval = getValue("dir","antlibsBuiltin");
		else if ( id == DIR_ANTLIBS_CUSTOM )         retval = getValue("dir","antlibsCustom");
		else if ( id == DIR_JAVA_COMPILE_SRC_STD )   retval = getValue("dir","compileSrcStd");
		else if ( id == FILE_COMPILE_XML )           retval = getValue("file","compile");
		else if ( id == FILE_GUI_PERSPLIST )         retval = getValue("file","guiPerspList");
		else if ( id == FILE_GUI_STATUS )            retval = getValue("file","guiStatus");
		else if ( id == FILE_GUI_RECALL )            retval = getValue("file","guiRecall");
		
		return retval;
	}
	/**
	 * @param _type
	 * @param _id
	 * @param _value
	 * @throws Exception
	 */
	public void add ( String _type, String _id, String _value ) throws Exception {
		Attribute x = new Attribute(_type,_id,_value);
		list.add(x);
	}
	
	/**
	 * 
	 */
	public void definitionFinished() {
		toConfig();
		toConfigSmall();
	}
	
	/**
	 * @return
	 */
	public Element getConfig() {
		return mountConfig;
	}
	public Element getConfigSmall() {
		return mountConfigS;
	}	
	/**
	 * @param _type
	 * @param _id
	 * @return
	 */
	public String getValue ( String _type, String _id ) {
		
		Iterator<Attribute> i = list.iterator();
		Attribute found = null;
		while ( i.hasNext() && found==null) {
			Attribute a = (Attribute)i.next();
			if ( _type.equals(a.type) && _id.equals(a.id))
				found = a;
		}
		if ( found == null )
			return null;
		else
			return found.value;
	}
	
	/**
	 * 
	 */
	private void toConfig() {
		mountConfig = new Element("config");
		Iterator<Attribute> i = list.iterator();
		while ( i.hasNext()) {
			Attribute a = (Attribute)i.next();
			Element add = null;
			
			/*
			Element lock = new Element("lock");
			lock.setAttribute("id",a.id);
			lock.setAttribute("type",a.type);
			mountConfig.addContent(lock); */
			
			if ( a.type.equals("prop")) {
				add = new Element("prop");
				add.setAttribute("id", a.id);
				add.setText(a.value);
			} else if ( a.type.equals("dir")) {
				add = new Element("dir");
				add.setAttribute("id", a.id);
				add.setText(a.value);
			} else if ( a.type.equals("file")) {
				add = new Element("file");
				add.setAttribute("id", a.id);
				add.setText(a.value);
			}
			add.setAttribute("flag", "lock");
			mountConfig.addContent(add);


		}
		
		if ( CoreBuilder.ws.getRole() != null ) {
			Element a = new Element ("prop");
			a.setAttribute("id", "role");
			a.setText(CoreBuilder.ws.getRole());
			mountConfig.addContent(a);
			
			Element al = new Element("lock");
			al.setAttribute("id","role");
			al.setAttribute("type","prop");
			mountConfig.addContent(al);				
		}
		
		if ( CoreBuilder.ws.getLang() != null ) {
			Element b = new Element ("prop");
			b.setAttribute("id", "lang");
			b.setText(CoreBuilder.ws.getLang());
			mountConfig.addContent(b);
			
			Element bl = new Element("lock");
			bl.setAttribute("id","lang");
			bl.setAttribute("type","prop");
			mountConfig.addContent(bl);				
		}
		
		Element c66 = new Element ("dir");
		c66.setAttribute("id", "status");
		c66.setAttribute("flag","lock");
		c66.setText(Entities.importEntities(CoreBuilder.ws.getStatusDir()));
		// mountConfig.addContent(c66);

		/*
		Element c67 = new Element("lock");
		c67.setAttribute("id","status");
		c67.setAttribute("type","dir");
		mountConfig.addContent(c67);	
		*/
		Element c = new Element ("prop");
		c.setAttribute("id", "os");
		c.setText(CoreBuilder.ws.getOs());
		mountConfig.addContent(c);
		
		Element cl = new Element("lock");
		cl.setAttribute("id","os");
		cl.setAttribute("type","prop");
		mountConfig.addContent(cl);	
		
		if ( CoreBuilder.ws.getComputer() != null ) {}
			Element b = new Element ("prop");
			b.setAttribute("id", "computer");
			if ( CoreBuilder.ws.getComputer() != null ) {
				b.setText(Entities.importEntities(CoreBuilder.ws.getComputer()));
			} else {
				b.setText("");
			}
			mountConfig.addContent(b);
			
			Element bl = new Element("lock");
			bl.setAttribute("id","computer");
			bl.setAttribute("type","prop");
			mountConfig.addContent(bl);				
				
	
		
		
	}
	
	private void toConfigSmall() {
		mountConfigS = new Element("config");
		Iterator<Attribute> i = list.iterator();
		
		if ( !isOnline ) {
			Element c = new Element ("prop");
			c.setAttribute("id", "os");
			c.setText(CoreBuilder.ws.getOs());
			mountConfigS.addContent(c);			
		}
		

		
		while ( i.hasNext()) {
			Attribute a = (Attribute)i.next();
			Element add = null;
			boolean isselected = true;
			
			if ( a.type.equals("dir") && a.id.equals("logs") )
				isselected = true;
			/*
			if ( a.type.equals("dir") && a.id.equals("status") )
				isselected = true;			*/
			if ( isselected ) {
				if ( a.type.equals("prop")) {
					add = new Element("prop");
					add.setAttribute("id", a.id);
					add.setText(a.value);
				} else if ( a.type.equals("dir")) {
					add = new Element("dir");
					add.setAttribute("id", a.id);
					add.setText(a.value);
				} else if ( a.type.equals("file")) {
					add = new Element("file");
					add.setAttribute("id", a.id);
					add.setText(a.value);
				}
				mountConfigS.addContent(add);
				
				Element lock = new Element("lock");
				lock.setAttribute("id",a.id);
				lock.setAttribute("type",a.type);
				mountConfigS.addContent(lock);				
			}



		}
		
		Element c66 = new Element ("dir");
		c66.setAttribute("id", "service_status");
		c66.setAttribute("flag", "lock x1");
		c66.setText(Entities.importEntities(CoreBuilder.ws.getStatusDir()));
		mountConfig.addContent(c66);
		
		
	}
		

		/*Element c67 = new Element("lock");
		c67.setAttribute("id","status");
		c67.setAttribute("type","dir");
		mountConfig.addContent(c67);			

		
	}	
	
	/**
	 * 
	 */
	public void reportToSysprint() {
		Iterator<Attribute> i = list.iterator();
		Sp.p("------ Report Mountconfig -----------------------------------");
		while ( i.hasNext()) {
			Attribute a = (Attribute)i.next();
			Sp.p(Commons.trailBlanks(a.type, 5)
							+ Commons.trailBlanks(a.id, 20)
							+ a.value);
		}
		Sp.p("------------------------------------------------------------");
	}

	private class Attribute {
		private String type = null;
		private String id = null;
		private String value = null;
		
		public Attribute (String _type, String _id, String _value ) throws Exception {
			type = _type;
			id = _id;
			value = _value;
			if ( type.endsWith("prop") || type.endsWith("dir") || type.endsWith("file"));
			else
				throw new Exception("MountConfig.Attribute, type "+_type +" is not valid. Must be prop, dir or file.");
			
		}
	}
	
}