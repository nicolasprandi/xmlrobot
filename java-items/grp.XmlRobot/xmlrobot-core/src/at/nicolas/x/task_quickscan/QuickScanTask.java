/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.task_quickscan;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_coop.CoopConnection;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class QuickScanTask implements ITask{


		
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		Sp.p("hallo quickscan: "+Commons.toText(e));

		boolean wantsPre = Commons.hasFlag(e.getAttributeValue("field"), "pre");
		boolean wantsPost = Commons.hasFlag(e.getAttributeValue("field"), "post");
		boolean wantsPost2 = Commons.hasFlag(e.getAttributeValue("field"), "post2");		
		boolean wantsId = Commons.hasFlag(e.getAttributeValue("field"), "id");
		boolean wantsLm = Commons.hasFlag(e.getAttributeValue("field"), "lm");		
		
		boolean wantsMd5 = Commons.hasFlag(e.getAttributeValue("field"), "md5");
		boolean wantsKb = Commons.hasFlag(e.getAttributeValue("field"), "kb");
		boolean wantsFn = Commons.hasFlag(e.getAttributeValue("field"), "fn");
		boolean wantsExt = Commons.hasFlag(e.getAttributeValue("field"), "ext");
		IdSpender idSpender = new IdSpender();

		String paraDir = e.getAttributeValue("dir");
		if ( paraDir== null) throw new LangException(506,"Task 'quick_scan' needs attribute @dir");
		FileObject paraDirFo1 = CommonsVfs.grantsFolderRead(paraDir,rh.getSitzung());
		FileName paraDirFn = paraDirFo1.getName();
		// paraDir = (Commons.introduceDirAsEgoDirString(paraDir)).string;

		String paraOut = e.getAttributeValue("out");
		if ( paraOut== null) throw new LangException(506,"Task 'quick_scan' needs attribute @out");
		
		FileObject connectionFileObject = null;
		FileName connectionFileName = null;
		CoopConnection coopConnection = null;
		if ( e.getAttribute("conn") != null ) {
			String connId = e.getAttributeValue("conn");
			coopConnection = (CoopConnection)rh.getNo(connId, Wellknown.coop_type_connection);
			
			if ( coopConnection == null ) 
				throw new Exception("The connection with id '"+connId+"' was not found");
			connectionFileObject = coopConnection.getFileObject();
			connectionFileName = coopConnection.getFileName();
		}
		/*
		 * FileObject fileObject = CoreBuilder.fsm.resolveFile(filename,fileSystemOptions);
		 */
		
		FileObject paraDirFo;
		if ( connectionFileName != null ) {
			Sp.p("1) connection-fileobject="+coopConnection.uri);
			Sp.p("2) requested="+paraDir);
			
			try {
				String relation = CommonsVfs.getRelativPath(connectionFileObject, paraDirFo1, false,rh.getSitzung());
				Sp.p("3)      diff="+relation);
				paraDirFo = connectionFileObject.resolveFile(relation);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new Exception("a must be part of b; a="+paraDir+" b="+coopConnection.uri);
			}
			// if ( !paraDir.startsWith(coopConnection.uri))
				
			// String diff = paraDir.substring(coopConnection.uri.length());

		} else {
			paraDirFo = CommonsVfs.resolveFolderRead(paraDir,rh.getSitzung());	
		}
		

		String basisUri = paraDirFo.getName().getURI();		
		
		FlexSpecs fs = new FlexSpecs(e);
		ResultDir resultDir = new ResultDir(
				CommonsVfs.resolveFolderRead(paraDir,rh.getSitzung()),idSpender);
	
		FileObject[] x = paraDirFo.findFiles(fs);
		for (FileObject found : x) {
							
			if ( found.isFile()) {
				String uriOfParent = found.getName().getParent().getURI();
				FileObject foOfParent = CoreBuilder.fsm.resolveFile(uriOfParent);
				String intermediatePath = CommonsVfs.getRelativPath(
						paraDirFo, 
						foOfParent, 
							false,
							rh.getSitzung());
				/*
				String base_name = found.getName().getBaseName();
				Sp.p("Found: URI        "+found.getName().getURI());
		
				Sp.p("       BN         "+base_name);
				Sp.p("       PATH       "+found.getName().getPath());
				Sp.p("       BASIS-DIR  "+basisUri);	
				String t2 = found.getName().getURI()+"/";
				String t3 = t2.substring(basisUri.length());
				String t4 = t3.substring(1);
				Sp.p("       T3     "+t3);
				Sp.p("       T4     "+t4);
				String t5 = t3.substring(0,t3.length()-base_name.length());
				Sp.p("       T5     "+t5);
				String t6 = t5.substring(1);
				Sp.p("       T6     "+t6);
				*/
				ResultDir meineHeimat = resultDir;
				if ( intermediatePath.length() > 0 ) {
					// zwischenpfade
					String regex= "/";
					String parts[]= intermediatePath.split(regex);
					int part_count =0;
					for (String part : parts) {
						part_count++;
						
						if ( meineHeimat.getSubDir(part) == null ) {
							
							// dieses verzeichnis gibts noch nicht
							String neueHeimat = meineHeimat.fo.getName().getURI();
							if ( !neueHeimat.endsWith("/"))
								neueHeimat = neueHeimat+"/";
							neueHeimat = neueHeimat + part+"/";
							ResultDir neueHeimatResult = new ResultDir(
									CommonsVfs.grantsFolderRead(neueHeimat,rh.getSitzung()),
									idSpender
									);
							meineHeimat.addSubDir(neueHeimatResult, part);
							meineHeimat = neueHeimatResult;
							
						} else {
							
							ResultDir neueHeimatResult = meineHeimat.getSubDir(part);
							meineHeimat = neueHeimatResult;
						}
					}
					

					
				} else {
					meineHeimat=resultDir;
					
				}
				/*
				Sp.p("das file "+found.getName().getURI()+
						" wurde an angehaengt an ");
						*/
				meineHeimat.addFile(new ResultFile(found,idSpender,rh.getSitzung()));
				// String t5 = 
			
			}
		}
		
		Element fin = resultDir.requestResult(
				wantsPre, wantsPost, wantsPost2, wantsId, wantsLm,
				wantsExt,wantsKb,wantsMd5, wantsFn, basisUri.length(), 1);
		fin.setAttribute("dir", basisUri);
		fin.setAttribute("para_dir", basisUri);
		if ( e.getAttribute("field") != null )
			fin.setAttribute("para_field", e.getAttributeValue("field"));
		if ( e.getAttribute("level_limit") != null )
			fin.setAttribute("para_level_limit", e.getAttributeValue("level_limit"));
		fin.removeAttribute("rpath");
		// Dokument schreiben
		Document document = new Document(fin);
		
		Sp.p("did wirte "+paraOut);
		
		CommonsVfs.writeToFile(document, paraOut,rh.getSitzung());
		
		// ResultDir resultDir = new ResultDir(Commons.getFileObjectByFilename(paraDir));
		/*
		String regex;
		if ( e.getAttribute("regex")!=null)
			regex = e.getAttributeValue("regex");
		else
			regex = "\\s+";
		
		boolean flag_empty = Commons.hasFlag(e, "empty");
		
		
		String out_file = e.getAttributeValue("out");
		if ( out_file== null) throw new LangException(507,"Task 'split' needs attribute @out");
		
		Element eSplits = new Element("splits");
		String founds[];
		if ( flag_empty )
			founds = txt.split(regex,-1);
		else
			founds = txt.split(regex);

		for ( String found: founds ) {
			
			if ( found.length()> 0 || flag_empty) {
				Element eSplit = new Element("split");
				eSplit.setAttribute("x", found);
				eSplits.addContent(eSplit);
			}
		}
		
		Document document = new Document(eSplits);
		
		Commons.writeToFile(document, out_file);
		*/ 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}