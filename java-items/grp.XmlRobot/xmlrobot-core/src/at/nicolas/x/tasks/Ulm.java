/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.vfs2.FileContent;
import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Ulm  implements ITask{


	
	
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {

		
		String file = e.getAttributeValue("file");

		
		if ( !CommonsVfs.existsFile(file,rh.getSitzung()));
			//lc.log_ext("Skipping, Does not exists and therefore will not be updated: "+file, this);
		else {
			String format = "yyyy-MM-dd-HH-mm-ss-SSS";
			if ( e.getAttribute("format") != null )
				format = e.getAttributeValue("format");
			
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			Date newdate = sdf.parse(e.getAttributeValue("time"));
			FileContent fc = CommonsVfs.getFileContentByFilename(file,rh.getSitzung());
			
			
			fc.setLastModifiedTime(newdate.getTime());

		}
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	

}