/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.io.File;
// zum extrahieren der resourcen, nona
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.nicolas.x.int_const.Constants;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;

public class RessourceExtractor {

	// private static CoreLogConnector lc = new CoreLogConnector("Resource");
	public  void resourceToFile(String resourcename, String filename, CommStartMsg sm, ISitzung sitzung) throws IOException, StorageLimitException {
		Sp.p("bin im resourceToFile "+resourcename+" "+filename+" "+Constants.resources_from_DEVELOPMENT);
		if ( Constants.resources_from_DEVELOPMENT )
			resourceToFile_DEVELOPMENT(resourcename, filename,sm,sitzung );
		else
			resourceToFile_DEPLOYED(resourcename, filename,sm,sitzung );	

	}

	public  void resourceToFile2(String resourcename, String filename, CommStartMsg sm, ISitzung sitzung) throws IOException, StorageLimitException {
		if ( Constants.resources_from_DEVELOPMENT )
			resourceToFile_DEVELOPMENT2(resourcename, filename,sm,sitzung );
		else
			resourceToFile_DEPLOYED2(resourcename, filename,sm , sitzung);	

	}	
	public  void resourceToFile_DEVELOPMENT(String resourcename, String filename, CommStartMsg sm, ISitzung sitzung ) throws IOException, StorageLimitException {
		
		File dir1 = new File (".");
		String fnIn = dir1.getCanonicalPath()+"/resources/"+resourcename;
		
		// sm.out.println ("copy '"+fnIn+"' to '"+filename+"'");
		CommonsVfs.copy_filename(fnIn, filename,sitzung);
		sm.out.println("extracted to file '"+filename+"' from resource '"+resourcename+"'");
	}

	public void resourceToFile_DEPLOYED(String resourcename, String filename, CommStartMsg sm,ISitzung sitzung ) throws IOException, StorageLimitException {
		Sp.p("bin im resouceToFile_DePLOEY "+resourcename+" "+filename);
		String res = "/resources/"+resourcename;
		InputStream is = this.getClass().getResourceAsStream( res );
		OutputStreamRequest osr =CommonsVfs.getOutputStreamByFilenameP1(filename, false,sitzung);
		OutputStream os = osr.getOutputStream();
		sm.out.println("going to get file '"+filename+"' from resource '"+resourcename+"'");		
		Commons.copy_stream ( is, os);
		os.close();
		is.close();
		CommonsVfs.finishOutputStreamP2(filename, sitzung, osr.getOpnum());
		sm.out.println("extracted to file '"+filename+"' from resource '"+resourcename+"'");
	}	
	
	public  void resourceToFile_DEVELOPMENT2(String resourcename, String filename, CommStartMsg sm, ISitzung sitzung ) throws IOException, StorageLimitException {
		File dir1 = new File (".");
		String fnIn = dir1.getCanonicalPath()+resourcename;
		
		// sm.out.println ("copy '"+fnIn+"' to '"+filename+"'");
		CommonsVfs.copy_filename(fnIn, filename,sitzung);
		sm.out.println("extracted to file '"+filename+"' from resource '"+resourcename+"'");
	}

	public void resourceToFile_DEPLOYED2(String resourcename, String filename, CommStartMsg sm, ISitzung sitzung ) throws IOException, StorageLimitException {
		String res = resourcename;
		InputStream is = this.getClass().getResourceAsStream( res );
		OutputStreamRequest osr = CommonsVfs.getOutputStreamByFilenameP1(filename, false,sitzung);
		OutputStream os = osr.getOutputStream();
		Sp.p(".... EXTRACTiNG "+resourcename+" <> "+filename);
		sm.out.println("going to extract file '"+filename+"' from resource '"+resourcename+"'");
		Commons.copy_stream ( is, os);
		os.close();
		is.close();
		CommonsVfs.finishOutputStreamP2(filename, sitzung,osr.getOpnum());
		Sp.p(".... EXTRACTiNG success");
		sm.out.println("extracted to file '"+filename+"' from resource '"+resourcename+"'");
	}		
}