/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.jdom2.Element;

import at.nicolas.x.int_coop.CryptoBase;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;

public class PlugFundef {

	public String name = null;
	public String classname = null;
	private String jarFile;
	LogConnector lc = new LogConnector("Fundef.?");
	static LogConnector lcs=new LogConnector("Fundef");
	IRequestHandler rh;
	
	Class cIxrf = null;
	
	public String getUrlString () {
		return jarFile;
	}
	public PlugFundef( String _name,
			        String _classname,
			        ClassLoader loader,
			        String jarFile,
			        IRequestHandler _rh,
			        Element eN ) throws Exception {
		name = _name;
		//CryptoBase cb = new CryptoBase("karla",rh.getSitzung());
		String errorMsg = Reserve.test(name, true);
		if ( errorMsg != null ) 
			throw new Exception("'fundef' cannot register a custom-function. Reason: "+errorMsg);
		
		classname = _classname;
		this.jarFile = jarFile;
		rh = _rh;
		lc.setFrom("Fundef."+name);
		
		// Ermitteln der Class
		
		try {
			
			cIxrf = loader.loadClass(classname);
			
		

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			Sp.p("Class "+classname+" could NOT be loaded as function.");
			throw new Exception ( "The class '"+classname+"' could not be loaded from "+jarFile);
		}
		
		Class[] ci = cIxrf.getInterfaces();
		boolean interface_implemented = false;
		for ( Class cc: ci ){
			// lc.core("implements: "+cc.getName(),LogConnector.CORE_CLASSLOAD);
			if ( cc.getName().equals("at.nicolas.x.pub.IFun"))
				 interface_implemented = true;
		}
		if ( !interface_implemented) 
			throw new Exception ( "The class '"+classname+"' does not implement the Interface 'at.nicolas.x.pub.IFun'");
		
		// Calling the static init
		IFun newtask = (IFun)this.getObject();
		newtask.staticFundef(eN, name, _rh);
	}
	
	public String getName() {
		return name;
	}
	
	public static PlugFundef newInstance( Element elementE, IRequestHandler rh, boolean webModus ) throws Exception {
		Commons.logNode(elementE);
		
		String derName = elementE.getAttributeValue("id");		
		String derClassname = elementE.getAttributeValue("class");		
		
		if ( (derName == null) ||(derClassname == null)  ) {
			String msg = "Must contain @id, @class and @jar, but it is "+Commons.toText(elementE);
			throw new Exception(msg);
		}
		if ( elementE.getAttribute("job") != null )
			throw new Exception("attribute 'job' for fundef not implemented yet.");


		Element elementNatural = Entities.exportEntitiesClone(elementE);
		ClassloaderInfo cli = ClassloaderInfo.getClassloaderInfo(elementE, lcs,webModus);
		return ( new PlugFundef(
					derName,
					derClassname,
					cli.classloader,
					Entities.exportEntities(cli.filelist),
					rh,
					elementNatural));
	}
	
	public void debug() {
		lc.debug("classname="+classname+", jar="+jarFile);

	}
	
	public Object getObject () throws InstantiationException, IllegalAccessException {
		return (cIxrf.newInstance() );
	}
	
}