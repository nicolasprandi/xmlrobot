/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.task_quickscan.FlexSpecs;


public class Delete  implements ITask{
	/* es fehlt noch der einbau des conn-objects wie im quickscan, erledingen wenn gebraucht */
	/* es fehlt noch der einbau des FlexSpecs (select & except) wie im quick_scan */


	String tname="delete";
	
	
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		FileSystemManager fsm = CoreBuilder.fsm;
		
		if ( e.getAttribute("file") != null ) {
			String filename = e.getAttributeValue("file");
			CommonsVfs.deleteFile(filename, rh.getSitzung());
		} else if ( e.getAttribute("dir") != null ) {
			String dirname = e.getAttributeValue("dir");
			CommonsVfs.deleteDir(dirname, rh.getSitzung());
;
			
		}  else if ( e.getAttribute("files_of_dir") != null ) {
			String dirname = e.getAttributeValue("files_of_dir");
			FileObject targetDir = CommonsVfs.resolveAnySilent(dirname);
			CommonsVfs.deleteFilesOfDir(targetDir, rh.getSitzung(),new FlexSpecs(e));

			
		}else if ( e.getAttribute("content_of_dir") != null ) {
			String dirname = e.getAttributeValue("content_of_dir");
			FileObject targetDir = CommonsVfs.resolveAnySilent(dirname);
			CommonsVfs.deleteContentOfDir(targetDir, rh.getSitzung());

			
		} else {
			throw new LangException("delete needs parameter: eg file, dir, content_of_dir, files_of_dir");
		}

		 		 
		return null;
	}

	public String getExtensionId() {
		return ( tname);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}