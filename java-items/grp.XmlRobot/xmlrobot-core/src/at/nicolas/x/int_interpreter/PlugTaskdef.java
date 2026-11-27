/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. *//* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;

public class PlugTaskdef  {

	public String name = null;
	public String classname = null;
	LogConnector lc = new LogConnector("Taskdef.?");
	static LogConnector lcs=new LogConnector("Taskdef");
	IRequestHandler rh;
	
	Class cIxrt = null;
	
	public boolean isLoop = false;
	
	/*
	private String jarFile;
	
	public String getUrlString () {

		return jarFile;
	}
	*/
	private ClassLoader classLoader = null;

		
	public PlugTaskdef( String _name,
			        String _classname,
			        ClassLoader loader,
			        // String jarFile,
			        IRequestHandler _rh,
			        Element eN,
			        boolean nottrusted) throws Exception {
		name = _name;
		
		if ( nottrusted) {
			String errorMsg = Reserve.test(name, false);
			if ( errorMsg != null ) 
				throw new Exception("'taskdef' cannot register a custom-task. Reason: "+errorMsg);
		}
		/*
		if ( name.startsWith("sys_") ||
				name.startsWith("tk_") ||
				name.startsWith("ti_") ||
				name.startsWith("tp_") ||
				name.startsWith("tx_") )
		*/
		
		classname = _classname;
		classLoader = loader;

		rh = _rh;
		lc.setFrom("Taskdef."+name);
		
		// Ermitteln der Class
	
		try {
			cIxrt = loader.loadClass(classname);
			Sp.p("Class "+classname+" could be loaded as task.");

		} catch (ClassNotFoundException e1) {
			Sp.p("Class "+classname+" could NOT be loaded as task.");
			throw new Exception ( "The class '"+classname+"' could not be loaded from "+getClassLoaderDesciption());
		}
		
		Class[] ci = cIxrt.getInterfaces();
		boolean interface_implemented = false;
		for ( Class cc: ci ){
			// lc.core("implements: "+cc.getName(),LogConnector.CORE_CLASSLOAD);
			if ( cc.getName().equals("at.nicolas.x.pub.ITask"))
				interface_implemented = true;
			else if ( cc.getName().equals("at.nicolas.x.pub.ITaskLogic"))
				isLoop = true;
		}
		if ( !interface_implemented) 
			throw new Exception ( "The class '"+classname+"' does not implement the Interface 'at.nicolas.x.pub.ITask'");
		
		// Calling the static init
		ITask newtask = (ITask)this.getObject();
		newtask.staticTaskdef(eN, name, _rh);

		
		
	}
	
	public String getName() {
		return name;
	}
	
	public static PlugTaskdef newInstance( Element elementE, IRequestHandler rh, boolean webModus ) throws Exception {
		Commons.logNode(elementE);

		String derName = elementE.getAttributeValue("id");
		String derClassname = elementE.getAttributeValue("class");		

		if ( (derName == null) ||(derClassname == null)  ) {
			String msg = "Must contain @id, @class and @jar, but it is "+Commons.toText(elementE);
			throw new Exception(msg);
		} 
		
		if ( elementE.getAttribute("java") != null )
			throw new Exception("attribute 'java' for taskdef not implemented yet.");		

		Element elementNatural = Entities.exportEntitiesClone(elementE);
		ClassloaderInfo cli = ClassloaderInfo.getClassloaderInfo(elementE, lcs,webModus);
		return ( new PlugTaskdef(derName,
								derClassname,
								cli.classloader,
								// Entities.exportEntities(cli.filelist),
								rh,
								elementNatural,
								true));		
		
	}
	
	// this is just called for ant
	public static PlugTaskdef newInstance( String derName, String derClassname, String derJarname, IRequestHandler rh,
			boolean webModus ) throws Exception {
	
		ClassloaderInfo cli = ClassloaderInfo.getClassloaderByFilename(derJarname, lcs,webModus);
		return ( new PlugTaskdef(
						derName,
						derClassname,
						cli.classloader,
						// derJarname,
						rh,
						null,
						true));
	}	
	
	public static PlugTaskdef newInstanceTrustedByClassloader( 
			String derName, String derClassname, 
			ClassLoader derClassLoader, IRequestHandler rh ) throws Exception {
		
		/*
		ClassloaderInfo cli = ClassloaderInfo.getClassloaderByFilename(derJarname, lcs);
		
		boolean loadTomcatClasses = false;
		ClassLoader cl;
		if ( loadTomcatClasses) {
			// cl = this.getClass().getClassLoader();
			cl = cli.classloader;
		} else {
			cl = cli.classloader;
		}	
		*/
		
		return ( new PlugTaskdef(
						derName,
						derClassname,
						derClassLoader,
						rh,
						null,
						false));
	}		
	/*
	public static PlugTaskdef newInstanceTrustedByJarname( String derName, String derClassname, String derJarname, IRequestHandler rh ) throws Exception {
	
		ClassloaderInfo cli = ClassloaderInfo.getClassloaderByFilename(derJarname, lcs);
		
		boolean loadTomcatClasses = false;
		ClassLoader cl;
		if ( loadTomcatClasses) {
			// cl = this.getClass().getClassLoader();
			cl = cli.classloader;
		} else {
			cl = cli.classloader;
		}	
		
		return ( new PlugTaskdef(
						derName,
						derClassname,
						cl,
						derJarname,
						rh,
						null,
						false));
	}	
	*/	
	
	/*
	public void debug() {
		lc.debug("classname="+classname);
		for (int i = 0; i<urlArray.length;i++) {
			lc.debug("url="+urlArray[i].toString());			
		}
	}
	*/
	
	public Object getObject () throws InstantiationException, IllegalAccessException {
		return (cIxrt.newInstance() );
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
	
	public String getClassLoaderDesciption() {
		return classLoader.toString();
		// return classLoader;
	}	

	
}