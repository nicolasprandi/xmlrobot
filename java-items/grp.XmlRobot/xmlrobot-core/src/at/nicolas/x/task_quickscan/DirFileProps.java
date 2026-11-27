package at.nicolas.x.task_quickscan;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_interpreter.Sp;

public class DirFileProps {
	private static boolean print=false;
	

	boolean isFolder = false;
			
	String comparePPath;
	String compareIPath;
	String compareRPath;
	
	String compareFileBase = null;
	String compareFileName = null;
	String compareFileDot = null;
	String compareFileExt = null;
	
	int compareDepth ;
	private void report(FileObject startFolder,int depth, FileObject directory, FileObject file) {
		Sp.p("");
		Sp.p("---------PARAS------------------------");
		Sp.p("startFolder : "+startFolder.getName().getURI());
		Sp.p("depth       : "+depth);
		Sp.p("directory   : "+directory.getName().getURI());
		if ( file == null )
			Sp.p("file        : none");
		else
			Sp.p("file        : "+file.getName().getURI());
		Sp.p("---------RESULTS------------------------");
		

		Sp.p("compare pPath    = ["+comparePPath+"]");
		Sp.p("compare iPath    = ["+compareIPath+"]");	
		Sp.p("compare rPath    = ["+compareRPath+"]");		
		Sp.p("compare FileBase = ["+compareFileBase+"]");
		Sp.p("compare FileName = ["+compareFileName+"]");
		Sp.p("compare FileDot  = ["+compareFileDot+"]");
		Sp.p("compare FileExt  = ["+compareFileExt+"]");
		
	
	}
	void createByBothRequest(FileObject startFolder,int depth, FileObject directory, FileObject file) {
		this.compareDepth = depth;
		if ( print ) {
			Sp.p("---------xxx------------------------");
			Sp.p("startFolder : "+startFolder.getName().getURI());
			Sp.p("depth       : "+depth);
			Sp.p("directory   : "+directory.getName().getURI());			
		}

		
		if (  depth <= 1 ){ // file != null &&
			// file ganz oben
			comparePPath="";
			compareIPath = "";
			compareRPath = "";	
		} else {
			String fromPath = startFolder.getName().getPath();
			String toPath = directory.getName().getPath();
			String rel1 = toPath.substring(fromPath.length());
			comparePPath = rel1+"/";
			if ( print ) Sp.p("rel1 = [ "+rel1+"] ");
			String dasWeg = directory.getName().getBaseName();
			if ( print ) Sp.p("dasWeg="+dasWeg);

			if ( rel1.length()>0)
				compareIPath = rel1.substring(0, rel1.length()-dasWeg.length());
			else
				compareIPath=rel1;			
		
		

		if ( print ) Sp.p("comparePath/1 = [ "+compareIPath+"] ");

		if ( comparePPath != null && comparePPath.length()>0 && comparePPath.startsWith("/"))
			comparePPath = comparePPath.substring(1);
		if ( print ) Sp.p("comparePath = [ "+comparePPath+"] ");		

		if ( compareIPath != null && compareIPath.length()>0 && compareIPath.startsWith("/"))
			compareIPath = compareIPath.substring(1);
		/*
		if ( comparePath1 != null && comparePath1.length()>0 && comparePath1.endsWith("/"))
			comparePath1 = comparePath1.substring(0,comparePath1.length()-1);	
			*/	
		if ( print ) Sp.p("comparePath1 = [ "+compareIPath+"] ");
		
		// compareDirBasi = directory.getName().getBaseName()+"/";
		compareRPath = directory.getName().getBaseName();		
		}
		if ( file != null ) {
			compareFileBase = file.getName().getBaseName();
			compareFileExt = file.getName().getExtension();
			if ( compareFileExt == null || compareFileExt.length() == 0 ) {
				// keine extension
				compareFileDot = "";
				compareFileName = compareFileBase;
			} else {
				// es gibt eine extension
				compareFileDot=".";
				compareFileName = compareFileBase.substring(0, compareFileBase.length()-(compareFileExt.length()+1));
			}
		}
		
		String x = compareIPath;
		compareIPath = comparePPath;
		comparePPath = x;
		
		if ( print ) report(startFolder, depth, directory, file);
	
	}
	
	public DirFileProps(FileSelectInfo fsi) throws FileSystemException {
		if ( fsi.getFile().isFile() ) {
			isFolder = false;
			createByBothRequest(
					fsi.getBaseFolder(),
					fsi.getDepth(),
					fsi.getFile().getParent(),
					fsi.getFile());
		} else {
			isFolder = true;
			createByBothRequest(
					fsi.getBaseFolder(),
					fsi.getDepth(),
					fsi.getFile(),
					null);
		}
	}	
	

}
