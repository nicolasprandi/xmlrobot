/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_interpreter;

import java.io.File;
// f�r classloader am desktop
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_setup.Workspace;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;

public class ClassloaderInfo {

	public ClassLoader classloader = null;
	public String filelist = null;
	private static void s ( String s ) {
		Sp.p("��������� "+s);
	}
	public static ClassloaderInfo getClassloaderInfo(Element elementE, LogConnector lc, 
			boolean webModus ) throws Exception {
		Sp.p("1) ===== getClassloaderInfo with webmodus="+webModus+" for def:"
				+Commons.toText(elementE));
		
		if ( webModus ) {
			ClassLoader webClassLoader = Workspace.overwrittenClassloader;
			ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
			if ( webClassLoader == null )
				s("webClassLoader == null");
			if ( sysClassLoader == null )
				s("sysClassLoader == null");	
			
			if ( webClassLoader == sysClassLoader ) 
				s("webClassLoader == sysClassLoader");
			else
				s("webClassLoader != sysClassLoader");
			ClassloaderInfo r = new ClassloaderInfo();
			r.classloader = webClassLoader;
			return r;
			
		} else if ( elementE.getAttribute("jar") != null ) {
			String derJarname = Entities.exportEntities(elementE.getAttributeValue("jar"));
			derJarname = Commons.fileClassic(derJarname);
			return getClassloaderByFilename(derJarname,lc,webModus);			
		} else {
			return getClassloaderByList(elementE, lc,webModus);
		}

	}
	
	public static ClassloaderInfo getClassloaderByFilename(String jarFile, LogConnector lc, 
			boolean webModus ) throws Exception {
		ArrayList<URL> urlArray;
		ClassloaderInfo r = new ClassloaderInfo();
		if ( webModus ) {
			
			r.classloader = jarFile.getClass().getClassLoader();
			
		} else {
			try {
				lc.debug("Loading from jar-file: "+jarFile);
				File file = new File(jarFile);
				urlArray = new ArrayList<URL>();
				urlArray.add(file.toURI().toURL());
				
				r.classloader = getClassloader ( urlArray,lc);
				r.filelist = jarFile;
				return  r;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				lc.warn("Problem in accessing a jar: "+jarFile);
				Sp.pe(e);
				throw e;
			}				
			
		}
		return r;
	
		
	}
	public static ClassloaderInfo getClassloaderByList(Element e, LogConnector lc,boolean webModus ) throws Exception {
		ArrayList<URL> urlArray;
		ClassloaderInfo r = new ClassloaderInfo();
		
		Sp.p("2) ===== getClassloaderInfo with webmodus="+webModus+" for def:"
				+Commons.toText(e));		
		
		if ( webModus ) {
		
			r.classloader = e.getClass().getClassLoader();
			
		} else {
			r.filelist = "";
			try {
				
				urlArray = new ArrayList<URL>();
				
				 List<Element> confList = e.getChildren("jar");
				Iterator<Element> i = confList.iterator();
				while ( i.hasNext()) {
					Element jar = i.next();
					String jarname = Entities.exportEntities(jar.getText());
					jarname = Commons.fileClassic(jarname);
					Sp.p("3) adding jar: "+jarname);
					File file = new File(jarname);
					urlArray.add(file.toURI().toURL());
					r.filelist = r.filelist+jarname+"; "; 
				}
						
				r.classloader = getClassloader ( urlArray,lc);
				return r ;

			} catch (Exception ex) {
				// TODO Auto-generated catch block
				lc.warn("Problem in accessing a jar-element-list: ");
				Sp.pe(ex);
				throw ex;
			}			
		}
		return r;
		
		
	}	
	public static ClassLoader getClassloader(ArrayList<URL> _classloaderUrlList, LogConnector lc) {
		URL[] urlArray2 = (URL[])_classloaderUrlList.toArray(new URL[_classloaderUrlList.size()]);
		ClassLoader loader = new URLClassLoader(urlArray2);
		return loader;
	}	

}