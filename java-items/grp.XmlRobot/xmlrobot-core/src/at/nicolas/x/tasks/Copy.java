/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.task_quickscan.FlexSpecs;


public class Copy implements ITask{
	/* es fehlt noch der einbau des conn-objects wie im quickscan, erledingen wenn gebraucht */

	String tname="copy";

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {

		FileSystemManager fsm = rh.getFsm();
		String msg_header = "Did Copy File: ";

		
		
		if ( e.getAttribute("todir") !=null ) throw new Exception("todir ist jetzt intodir");
		
		
		
		if ( e.getAttribute("dir") != null && e.getAttribute("intodir2") != null ) {
			/* es fehlt noch der einbau des conn-objects (2x) wie im quickscan */
			FlexSpecs theFileSelector = new FlexSpecs(e);
			// Commons.copy_filename(e.getAttributeValue("uri"), e.getAttributeValue("touri"));
			FileObject fromDir = CommonsVfs.grantsFolderRead(e.getAttributeValue("dir"),rh.getSitzung());
			FileObject tothatdir = CommonsVfs.resolveAnySilent(e.getAttributeValue("intodir2"));
					
			long opnum = CommonsVfs.grantsUpdateFolderP1(tothatdir,rh.getSitzung(),"Copy dir-intodir2");

			try {
				tothatdir.copyFrom(fromDir, theFileSelector);
			} catch (Exception e1) {
				Sp.pe(e1);					
				throw e1;
			} finally {
				if (fromDir!=null) fromDir.close();
				if (tothatdir!=null) tothatdir.close();
				CommonsVfs.grantsUpdateFolderP2(tothatdir,rh.getSitzung(),opnum);				
			}

			
		} else if ( e.getAttribute("uri") != null && e.getAttribute("touri") != null ) {
			/* es fehlt noch der einbau des conn-objects (2x) wie im quickscan */
			FlexSpecs theFileSelector = new FlexSpecs(e);
			
			FileObject uri = CommonsVfs.resolveAnySilent(e.getAttributeValue("uri"));
			if ( !uri.exists()) throw new Exception("The uri ["+e.getAttributeValue("uri")+"] does not exists");
			
			String toUriString = e.getAttributeValue("touri");
			if ( toUriString == null || toUriString.length() == 0 ) throw new Exception("The touri is not defined but must. it is "
					+ toUriString );
			
			FileObject touri =  CommonsVfs.resolveAnySilent(toUriString);
			long opnum = -1;
			
			if ( uri.isFile()) {
				
				opnum = CommonsVfs.grantsUpdateFileP1(touri, rh.getSitzung(),"copy uri-uri/file") ;
				try {
					touri.copyFrom(uri, theFileSelector);
				} catch (Exception e1) {
					Sp.pe(e1);					
					throw e1;
				} finally {
					if ( uri!=null) uri.close();
					if ( touri!=null) touri.close();		
					CommonsVfs.grantsUpdateFileP2(touri, rh.getSitzung(),opnum) ;
				}
				

									
			} else {
				
	
						
				opnum = CommonsVfs.grantsUpdateFolderP1(touri, rh.getSitzung(),"copy uri-uri/dir") ;
				try {
					touri.copyFrom(uri, theFileSelector);
				} catch (Exception e1) {
					Sp.pe(e1);					
					throw e1;
				} finally {
					if ( uri!=null) uri.close();
					if ( touri!=null) touri.close();		
					CommonsVfs.grantsUpdateFolderP2(touri, rh.getSitzung(),opnum) ;	
				}
				
			
				
			}
			

		} else if ( e.getAttribute("file") == null ) {
			throw new Exception("copy needs attribute 'file'");
		} else if ( e.getAttribute("file") != null && e.getAttribute("intodir") != null ) {
			String todir = Commons.constructNormalizedDir(e.getAttributeValue("intodir"));

			String target_file = todir+Commons.getNameOfFile(e.getAttributeValue("file"));
			FileObject target_fileFO  = CommonsVfs.resolveAnySilent(target_file);
			long opnum=		CommonsVfs.grantsUpdateFileP1(target_fileFO,rh.getSitzung(),"copy file-intodir");
			
			// ....
			if ( target_fileFO.exists()) {
				// Commons.delete_file(target_file);
				msg_header = "Did Copy and Overwrite File: ";
			}
			
			FileObject readingFile = CommonsVfs.grantsFileRead(e.getAttributeValue("file"),rh.getSitzung());
			
			
			try {
				CommonsVfs.copy_filename(readingFile, target_fileFO,rh.getSitzung());
			} catch (Exception e1) {
				Sp.pe(e1);					
				throw e1;
			} finally {
				if ( target_fileFO != null ) target_fileFO.close();
				CommonsVfs.grantsUpdateFileP2(target_fileFO, rh.getSitzung(),opnum);
			}

			
			// lc.log_ext(msg_header+e.getAttributeValue("file")+" to "+target_file, this);			
			
		} else if (e.getAttribute("tofile") != null) {
			if ( Commons.hasFlag(e, "hard")) {
				CommonsVfs.grantsFileRead(e.getAttributeValue("file"),rh.getSitzung());
				FileObject to = CommonsVfs.resolveAnySilent(e.getAttributeValue("tofile"));
						
				long opnum=		CommonsVfs.grantsUpdateFileP1(to,rh.getSitzung(),"copy file-to-file/hard");					
				File f = new File(Commons.fileClassic(e.getAttributeValue("file")));
				File t = new File(Commons.fileClassic(e.getAttributeValue("tofile")));
				Sp.p("hard-coopy: "+Commons.fileClassic(e.getAttributeValue("file")) 
				+" - "+Commons.fileClassic(e.getAttributeValue("tofile"))
						);
				
		
				try {
					FileUtils.copyFile(f, t);
				} catch (Exception e1) {
					Sp.pe(e1);					
					throw e1;
				} finally {
					CommonsVfs.grantsUpdateFileP2(to, rh.getSitzung(),opnum);
				}
				
			} else {
				String target_file = e.getAttributeValue("tofile");
				FileObject target_fileFO = CommonsVfs.resolveAnySilent(target_file);
				//long opnum =		CommonsVfs.grantsUpdateFileP1(target_fileFO,rh.getSitzung(),"copy file tofile");
				if ( target_fileFO.exists()) {
					// Commons.delete_file(target_file);
					msg_header = "Did Copy and Overwrite File: ";
				}
				FileObject fromFile =  CommonsVfs.grantsFileRead(e.getAttributeValue("file"),rh.getSitzung());
								
				try {
					CommonsVfs.copy_filename(fromFile, target_fileFO,rh.getSitzung());
					
				} catch (Exception e1) {
					Sp.pe(e1);					
					throw e1;
				} finally {
					if (target_fileFO!=null) target_fileFO.close();
					//CommonsVfs.grantsUpdateFileP2(target_fileFO, rh.getSitzung(),opnum);		
				}				
			}


			// lc.log_ext(msg_header+e.getAttributeValue("file")+" to "+target_file, this);			
		} else {
			throw new Exception("copy needs attribute 'tofile' or 'todir' or ...");
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