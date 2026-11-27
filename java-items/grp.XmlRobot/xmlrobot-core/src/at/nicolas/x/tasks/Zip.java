/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.IOException;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.compression.ZipCreator;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.StorageLimitException;


public class Zip implements ITask{
	/* es fehlt noch der einbau des conn-objects wie im quickscan, erledingen wenn gebraucht */
	/* es fehlt noch der einbau des FlexSpecs (select & except) wie im quick_scan */



	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {


		ZipCreator zc = new ZipCreator(e.getAttributeValue("destfile"),rh.getSitzung());
		if (e.getAttribute("basedir") != null ) {
			String internalPaht = "";
			if ( e.getAttribute("target_path") != null ) {
				internalPaht = e.getAttributeValue("target_path");
			}
			zc.addFolder(e.getAttributeValue("basedir"),internalPaht);
		}
		if ( e.getChildren("basedir").size() > 0 ) {
			Iterator<Element> i = e.getChildren("basedir").iterator();
			while ( i.hasNext()) {
				Element j = (Element)i.next();

				String ip = "";
				if ( j.getAttribute("target_path") != null ) {
					ip = j.getAttributeValue("target_path");
				}
				zc.addFolder(j.getText(),ip);
			}				

		}
		zc.close();
		

				 		 
		return null;
	}
	
	public static void zip (String destfile, String basedir, String target_path, ISitzung _sitzung) throws IOException, StorageLimitException {
		
		ZipCreator zc = new ZipCreator(destfile,_sitzung);
		if (basedir != null ) {
			String internalPaht = "";
			if ( target_path != null ) {
				internalPaht = target_path;
			}
			zc.addFolder(basedir,internalPaht);
		}

		zc.close();		
		
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}