/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.task_quickscan.FlexSpecs;


public class Unzip implements ITask{



	public Parameters execute(Element e, IRequestHandler rh) throws LangException, FileSystemException, StorageLimitException  {
		
		if ( e.getAttribute("todir") ==null )
			throw new LangException("task 'unzip' needs attribute 'todir' as output-directory");
		
	  	if ( e.getAttribute("file") ==null && e.getAttribute("dir") ==null ) {
	  		throw new LangException("task 'unzip' needs attribute 'file' or 'dir' - dir allows fileselection");
		} 	
	  	
	  	
		String dest = e.getAttributeValue("todir");
		
		FileObject foDest = CommonsVfs.resolveFolderSilent(dest);
		
  	  	if ( e.getAttribute("file") !=null ) {
		  // unzip a file
  	  		String file = e.getAttributeValue("file");
  	  		System.out.println("file fuer unzip ="+file);
  	  		FileObject foFile  = CommonsVfs.resolveFileSilent(file);
		
  	  		CommonsVfs.unzip(foDest, foFile,rh.getSitzung());

	
  		  
  	  	} else if ( e.getAttribute("dir") !=null ) {
		    // unzip files in a specified directory
	  		String dir = e.getAttributeValue("dir");
	  		FileObject foDir = CommonsVfs.grantsFolderRead(dir,rh.getSitzung());
			FlexSpecs fs = new FlexSpecs(e);
		
			FileObject[] x = foDir.findFiles(fs);
			for (FileObject found : x) {
				if ( found.isFile()) {
					CommonsVfs.unzip(foDest, found,rh.getSitzung());
					
				}
			}
  	  	}	    	 
				 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}