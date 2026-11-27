package at.nicolas.x.int_interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.term_tk.FitsRule;

public class TempFileManager {
	private static ArrayList<TempFileManager> managers = new ArrayList<TempFileManager>();
	private static SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	
	private TempDir memTemp = null;
	private TempDir fileTemp = null;
	
	private File traceFile = null; 
	private FileOutputStream traceFos = null;
	private PrintStream tracePrinter =  null;
	
	private static String cosDir= "file:/"+
	Commons.addFsInCase(Commons.stringReplace(System.getProperty("java.io.tmpdir"),"\\","/"));
	private ISitzung userSitzung;
	public TempFileManager(Interpreter interpreter) {
		userSitzung = interpreter.isitzung;
		// TODO Auto-generated constructor stub
	}
	
	public PrintStream requestTracer(String introText, String jobid, int level) throws IOException, StorageLimitException {
		String jn = FitsRule.applyRule(jobid, false);
	
		String derdir = 
				Commons.dirClassic(userSitzung.getSessionHomeFs())+"trace/-"+level+"-"+jn+"/"+
				Commons.getTs()+"/";
		CommonsVfs.mkdirCascade(derdir, userSitzung);
		String derfn = derdir+"trace.txt";
		Sp.ph("Tracing to: "+derfn);
		traceFile = new File(derfn);
		// traceFile = File.createTempFile(level+"_trace_"+jn, ".txt"); 
		Sp.p("!!!       !!!");
		Sp.p("\n!!! TRACE !!! goes to ["+traceFile.getCanonicalPath()+"] for interpreter "+introText);
		Sp.p("!!!       !!!");
		traceFos = new FileOutputStream(traceFile);
		tracePrinter =  new PrintStream(traceFos,false,"UTF-8");
		Sp.p("Created trace-file: "+traceFile.getAbsolutePath());
		
		tracePrinter.println("START: "+formatter.format(new Date()));
		tracePrinter.println(introText);
		tracePrinter.println("=====================================================");
		return tracePrinter;
	}
	public String requestMemTempDir () throws FileSystemException, URISyntaxException, URIException, StorageLimitException {
		if ( memTemp == null ) {
			String derfn = 
					userSitzung.getSessionHomeMem()+"/"+
							Commons.getTs()+"_"+
							Commons.randomAlphaNumeric(3)+"/";			
			// memTemp = new TempDir();
			if ( userSitzung == null )
				Sp.p("Die userSitzung ist null");
			Sp.p("Created memtemp: session: "+userSitzung.getDescriptor());
			Sp.p("Created memtemp: "+derfn);
			
			memTemp = new TempDir(derfn);
			
		}
		
		return memTemp.dirname;
	}
	
	public String requestFileTempDir () throws FileSystemException, StorageLimitException {
		if ( fileTemp == null ) {
			String derfn = 
					userSitzung.getSessionHomeFs()+"/ft/"+
							Commons.getTs()+"_"+
							Commons.randomAlphaNumeric(3)+"/";			
			// memTemp = new TempDir();
			Sp.p("Created filetemp: "+derfn);
			fileTemp = new TempDir(derfn);
			
		}
		
		return fileTemp.dirname;
	}	
	public void finishJob (Exception ex) throws IOException {
		
		if ( memTemp != null ) {
			// memTemp.dir.deleteAll();
			try {
				
				CommonsVfs.deleteDir(memTemp.dir, userSitzung);
			} catch (StorageLimitException e) {
				// sollte ja eher weniger werden ...
			}
			Sp.p(memTemp.dir.getName().getURI()+" is cleared as MEM  temp");
		}
		if ( fileTemp != null ) {
			try {
				//if ( userSitzung.isOnline())
				CommonsVfs.deleteDir(fileTemp.dir, userSitzung);
			} catch (StorageLimitException e) {
				// sollte ja eher weniger werden ...
			}
			Sp.p(fileTemp.dir.getName().getURI()+" is cleared as FILE temp");
			
			/*
			FileObject[] have = CommonsVfs.getNinos(fileTemp.dir,userSitzung);
			if ( have.length < 1 ) {
			  fileTemp.dir.deleteAll();
			  Sp.p(fileTemp.dir.getName().getURI()+" is cleared as FILE temp");
			}*/
		}	
		if ( tracePrinter != null ) {
			tracePrinter.println("=====================================================");
			tracePrinter.println("END: "+formatter.format(new Date()));
			if ( ex != null )
				tracePrinter.println(ex.getMessage());
			tracePrinter.close();
			traceFos.close();
			
		}
		Iterator<TempFileManager> i = managers.iterator();
		int idx = 0;
		while ( i.hasNext()) {
			TempFileManager x = (TempFileManager)i.next();
			if ( x==this) {
				managers.remove(idx);
				break;
			}
			idx++;
		}		
		
	}
	public void finishAll () throws IOException {
		Iterator<TempFileManager> i = managers.iterator();
		while ( i.hasNext()) {
			TempFileManager x = (TempFileManager)i.next();
			x.finishJob(null);
		}
		managers = new ArrayList<TempFileManager>();
	}
	
	public static TempFileManager getTempFileManager( Interpreter interpreter ) {
		TempFileManager x = new TempFileManager(interpreter);
		managers.add(x);
		return x;
		
	}
	
	private class TempDir  {
		
		String dirname;
		FileObject dir;
		public TempDir(String dirname ) throws FileSystemException, StorageLimitException {
			dir = CommonsVfs.resolveAnySilent(dirname);
					
			long opnum = CommonsVfs.grantsUpdateFolderP1(dir,userSitzung,"TempDir");
			dir.createFolder();
			CommonsVfs.grantsUpdateFolderP2(dir,userSitzung,opnum);
			this.dirname = dirname;
			Sp.p("Did create temp-dir: "+dirname);
		}
		
	}		

}
