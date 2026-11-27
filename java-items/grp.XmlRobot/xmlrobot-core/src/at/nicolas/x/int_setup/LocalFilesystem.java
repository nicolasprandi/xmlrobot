/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.io.File;
//
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.apache.commons.vfs2.FileName;

import at.nicolas.x.int_specs.Commons;

public class LocalFilesystem {
	
	/*
	private void copyResourceFolder(String from, String to,CommStartMsg sm ) throws Exception {
			
		sm.out.println("copyResourceFolder from="+from+", to="+to);
		RessourceExtractor re = new RessourceExtractor();
		File fromDir = null;
		if ( Constants.resources_from_DEVELOPMENT ) {
			fromDir = new File("resources"+FileName.SEPARATOR+from);			
		} else {
			sm.out.println("Getting Url: ["+"/resources/"+from+"/"+"]");
			// URL dir_url = ClassLoader.getSystemResource("/resources"+FileName.SEPARATOR+from);
			URL dir_url = this.getClass().getResource( "/resources/"+from+"/" );
			if ( dir_url == null ) {
				sm.out.println("the fetched url is null");
				return;
			}
			fromDir = new File(dir_url.toURI());
		}
		
		// Anlegen des Verzeichnisses
		
		String myto = to;
		while ( myto.endsWith("/") || myto.endsWith(FileName.SEPARATOR)) {
			myto = myto.substring(0, myto.length()-1);
		}
		
		copyResourceFolderIntern(fromDir,""+from,myto,re,sm);
	}
	
	private void copyResourceFolderIntern( File from, String relFrom, String to, RessourceExtractor re,CommStartMsg sm) throws Exception {
		sm.out.println("copyResourceFolderIntern from="+from.getCanonicalPath()+", to="+to);
	
		Commons.mkdirCascade(to,"initial phase");
		
		String[] files = from.list();		
		if ( files == null ) {
			Sp.p("Achso "+from.getAbsolutePath());
			return;
		}
		for (int i=0; i<files.length; i++) {
			File etwas =  new File(from.getAbsolutePath(),files[i]);
			sm.out.println("   Working on etwas: "+etwas.getAbsolutePath()+", file[i]="+files[i]);
			
			if ( etwas.isFile()) {
				re.resourceToFile(relFrom+FileName.SEPARATOR+files[i], to+FileName.SEPARATOR+files[i], sm);
			} else if ( etwas.isDirectory() ) {
				// ein directory
				File nextFile = null;
				if ( Constants.resources_from_DEVELOPMENT ) {
					nextFile = etwas;
				} else {
					// URL dir_url = ClassLoader.getSystemResource(etwas.getAbsolutePath());
					sm.out.println("   Getting Url: "+"/resources/"+relFrom+FileName.SEPARATOR+ files[i]+"/");
					URL dir_url = this.getClass().getResource( "/resources/"+relFrom+FileName.SEPARATOR+ files[i]+"/");
					if ( dir_url == null ) {
						sm.out.println("the fetched url is null");
					}
					nextFile = new File(dir_url.toURI());					
				}
				String nextTo = to+FileName.SEPARATOR+files[i];
				copyResourceFolderIntern( nextFile,relFrom+FileName.SEPARATOR+files[i],nextTo, re,sm);
			}
		}
	}
	*/
	
	public static ClassLoader getClassLoaderFromFilename(String jarname) throws MalformedURLException {
		if (jarname==null) return null;
		File jarfile = new File(Commons.fileClassic(jarname));
		ArrayList<URL> urlArray = new ArrayList<URL>();
		urlArray.add(jarfile.toURI().toURL());	
		
		URL[] urlArray2 = (URL[])urlArray.toArray(new URL[urlArray.size()]);
		ClassLoader loaderX = new URLClassLoader(urlArray2);
		return loaderX;
	}
		
	public static ClassLoader getClassLoaderFrom2Filenames(String jarname1,String jarname2) throws MalformedURLException {
		if (jarname1 ==null && jarname2 ==null) return null;
		
		ArrayList<URL> urlArray = new ArrayList<URL>();
		if ( jarname1 != null ) {
			File jarfile = new File(jarname1);			
			urlArray.add(jarfile.toURI().toURL());			
		}
		if ( jarname2 != null ) {
			File jarfile = new File(jarname2);			
			urlArray.add(jarfile.toURI().toURL());			
		}		

		
		URL[] urlArray2 = (URL[])urlArray.toArray(new URL[urlArray.size()]);
		ClassLoader loaderX = new URLClassLoader(urlArray2);
		return loaderX;
	}		
	
	public static String getHomeDir() {
		return ( new File("").getAbsolutePath());

	}
	
	public static String getHomeDirXrFormat() {
		String standardHome = new File("").getAbsolutePath();
		if ( standardHome.endsWith("/") || standardHome.endsWith(FileName.SEPARATOR));
		else standardHome = standardHome+FileName.SEPARATOR;
		return (standardHome );
	}	
	
	public static String getFilenameOfResource (String resourcename) throws IOException {
		File dir1 = new File (".");
		String fnIn = dir1.getCanonicalPath()+FileName.SEPARATOR+".."+FileName.SEPARATOR+"resources"+FileName.SEPARATOR+""+resourcename;
		return fnIn;
	}
	
	/*

	public static String getHomeDir() {
		return new String("");		
	}
	
	public static String getHomeDirManual(String manual) throws FileSystemException {
		if (manual==null) return getCurrent();	
		else if ( manual.equals("")) return getCurrent();
		else {
			if ( Commons.existsDir(manual))
				return manual;
			else
				return getCurrent();
		}
	}	
	
	private static String getCurrent () {
		
		File f = new File("");
		String rv = null;
		try {
			rv = "" +f.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rv = "";
		}
		return rv;
	}
	*/
	
}