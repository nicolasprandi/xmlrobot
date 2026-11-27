/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Systempost implements ITask{

		
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		
		ISitzung isitzung = rh.getSitzung();
		
		String systempostdir = isitzung.getSystempostDir(); // das ist in der session
		String channelDir = systempostdir+"c_"+Commons.cureName(e.getAttributeValue("channel"))+"/";
		String idDir = channelDir+"p_"+Commons.cureName(e.getAttributeValue("postid"))+"/";
		System.out.println("Systempost auf dir = "+idDir);

		List<Element> fsi = e.getChildren("file");
		Iterator<Element> iFsis = fsi.iterator();
		//System.out.println("-ss  2 ");
		while ( iFsis.hasNext() ) {
			Element akt = (Element)iFsis.next();
			String curFileName = akt.getAttributeValue("file");
			String curName = Commons.cureName(akt.getAttributeValue("name"));
			String curMaxkb = akt.getAttributeValue("maxsize");
			int maxkb = Commons.string2int(curMaxkb);
			String thispostFile = idDir+curName;
			long fileskb = CommonsVfs.getSizeByFilename(curFileName, isitzung);
			if ( fileskb > maxkb ) throw new Exception("file for post too big");

			//CommonsVfs.grantsDelete(thispostFile, isitzung);
			//asdf
			System.out.println("kopiere "+curFileName);
			
			CommonsVfs.copy_filename(curFileName, thispostFile,isitzung);
			
		}
		
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}