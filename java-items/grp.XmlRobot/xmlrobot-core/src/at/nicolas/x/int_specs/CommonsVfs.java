package at.nicolas.x.int_specs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileUtil;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.task_quickscan.FlexSpecs;
import at.nicolas.x.tasks.FileSelectorAll;
import at.nicolas.x.tasks.FileSelectorAllContent;

public class CommonsVfs {

	public static void deleteFile ( String filename, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		FileObject file = CoreBuilder.fsm.resolveFile(filename);
		// hier kann noch optimiert werden
		
		if ( file.exists() && file.isFile()) {
			long opnum = sitzung.grantsUpdateFileP1(file,"deleteFile");
			file.delete();
			sitzung.grantsUpdateFileP2(file,opnum);
		}
		
	}
	public static void deleteFile ( FileObject file, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		
		if ( file.exists() && file.isFile()) {
			long opnum = sitzung.grantsUpdateFileP1(file,"deleteFile");
			file.delete();
			sitzung.grantsUpdateFileP2(file,opnum);
		}
		
	}	
	
	public static void deleteDir ( String dirname, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		CommonsVfs.deleteContentOfDir(dirname, true, sitzung);
		//deleteDir ( CoreBuilder.fsm.resolveFile(dirname), sitzung);

	}
	
	public static void deleteDir ( FileObject dir, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		CommonsVfs.deleteContentOfDir(dir, true, sitzung);
		/*
		if ( dir.exists() && dir.isFolder()) {
	
			long opnum = sitzung.grantsUpdateFolderP1(dir,"deleteDir");

			dir.deleteAll();
			sitzung.grantsUpdateFolderP2(dir, opnum);
			
		} */
		
	}	
	
	public static void deleteFilesOfDir ( FileObject dir, ISitzung sitzung, FlexSpecs flexSpecs ) throws FileSystemException, StorageLimitException {
		// hier kann noch optimiert werden
		
		
		if ( dir.exists() && dir.isFolder()) {
			long opnum = sitzung.grantsUpdateFolderP1(dir,"deleteFilesOfDir");
			dir.delete(flexSpecs);
			sitzung.grantsUpdateFolderP2(dir, opnum);
		
		}
	}	
	
	public static void deleteContentOfDir ( FileObject dir, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		// hier kann noch optimiert werden
	
		deleteContentOfDir (  dir, false,  sitzung );
		/*
		if ( dir.exists() && dir.isFolder()) {
			long opnum = sitzung.grantsUpdateFolderP1(dir,"deleteContentOfDir");
			dir.delete(new FileSelectorAllContent());
			sitzung.grantsUpdateFolderP2(dir, opnum);
		
		}*/
	}		
	
	public static void deleteContentOfDir ( String dir, boolean deleteDir, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		// hier kann noch optimiert werden
		
		FileObject fdir;
		if ( deleteDir )
			fdir = CommonsVfs.resolveFolderDelete(dir, sitzung);
		else
			fdir = CommonsVfs.resolveFolderRead(dir,sitzung);
		deleteContentOfDir ( fdir,  deleteDir,  sitzung );
		
	}
	
	public static void deleteContentOfDir ( FileObject dir, boolean deleteDir, ISitzung sitzung ) throws FileSystemException, StorageLimitException {
		// hier kann noch optimiert werden
		

		if ( deleteDir )
			sitzung.grantsUpdateFolder(dir);
		else
			sitzung.grantsFolderRead(dir);
		
		FileObject[] childs = dir.getChildren();

		for (int i=0; i<childs.length; i++) {
			if ( childs[i].isFile() ) {
				CommonsVfs.deleteFile(childs[i], sitzung);
			} else if ( childs[i].isFolder() ) {
				CommonsVfs.deleteContentOfDir(childs[i], true, sitzung);
			}
		
		}
		if ( deleteDir ) {
			dir.deleteAll();
		}
	
	}		
	
	public static String getLm ( FileObject file, ISitzung sitzung) throws FileSystemException {
		sitzung.grantsFileRead(file);
		
		long last_mod = file.getContent().getLastModifiedTime();
		Date date_mod = new Date(last_mod);
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd-HHmmss");
		String dateString = formatter.format(date_mod);
		return dateString;
	}
	
	public static String getRelativPath (FileObject root, FileObject relative, boolean slashAtEnd, ISitzung sitzung ) throws Exception {
		
		
		String res = root.getName().getRelativeName(relative.getName());
		if ( res == null )
			throw new Exception("no relative dir available");
		
		if ( res.equals("."))
			res = "";
		
		if ( slashAtEnd && res.length()> 0)
			res = res + FileName.SEPARATOR;
		return res;
		
	}
	
	public static String getRelativPathx (FileName root, FileName relative, boolean slashAtEnd, ISitzung sitzung ) throws Exception {
		
		
		String res = root.getRelativeName(relative);
		if ( res == null )
			throw new Exception("no relative dir available");
		
		if ( res.equals("."))
			res = "";
		
		if ( slashAtEnd && res.length()> 0)
			res = res + FileName.SEPARATOR;
		return res;
		
	}	
	private static String getRelativePath ( String rootUri, String relUri, boolean slashAtEnd, ISitzung sitzung  ) throws Exception {
		
		FileObject objectRoot = CoreBuilder.fsm.resolveFile(rootUri);		
		FileName nameRoot = objectRoot.getName();
		FileObject objectRel = CoreBuilder.fsm.resolveFile(relUri); //CoreBuilder.fsm.resolveFile(relUri);		
		FileName nameRel = objectRel.getName();	
		return getRelativPath(objectRoot,objectRel,slashAtEnd,sitzung);
	}	
	
	public static FileObject[] getNinos(String directoryname, ISitzung sitzung) throws FileSystemException {
		// TODO Auto-generated constructor stub
		FileObject fo = CommonsVfs.grantsFolderRead(directoryname,sitzung);
		
	
		return ( fo.getChildren());
	}
	
	public static FileObject[] getNinos(FileObject fileObject, ISitzung sitzung) throws FileSystemException {
		// TODO Auto-generated constructor stub
		sitzung.grantsFolderRead(fileObject);
		if ( fileObject.exists()) 
			return ( fileObject.getChildren());
		else {
			FileObject[] dummy = new FileObject[0];
			return dummy;
		}
	}		
	/*
	public static String getCanoDirXrIfExists ( String dirname ) throws FileSystemException {
		if ( dirname == null ) return null;
		if (existsDir(dirname))
			return getCanoDirXr(dirname);
		else
			return dirname;
	}
	

	public static String getNameOfDir ( String fullDir ) throws Exception {
		if ( fullDir == null ) return null;
		try {
			FileSystemManager fsm = CoreBuilder.fsm;
			FileObject file = fsm.resolveFile(fullDir);
			
			FileName fileName = file.getName();
			return (fileName.getBaseName());
		} catch (Exception e) {
			throw new Exception("From Commons.getNameOfDir() :"+e.getMessage());

		}
	}*/









public static boolean existsFile ( String filename, ISitzung sitzung ) throws FileSystemException {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(filename);
	sitzung.grantsFileRead(fileObject);
	return ( fileObject.exists() && fileObject.isFile());
}
public static boolean existsFileForProcess ( String filename, ISitzung sitzung ) throws Exception {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(filename);
	sitzung.grantsFileProcess(fileObject);
	sitzung.checkMaxFileMemory(fileObject);
	return ( fileObject.exists() && fileObject.isFile());
}


public static boolean existsDir ( String dirname2, ISitzung sitzung ) throws FileSystemException  {
	//String dirname = xr2vfsDir(dirname2);

	//try {
		FileObject fileObject;
		try {
			fileObject = null;
			fileObject = CoreBuilder.fsm.resolveFile(dirname2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}

		sitzung.grantsFolderRead(fileObject);
		boolean ret=fileObject.exists() && fileObject.isFolder();
		
		return ( ret );
	/*} catch (FileSystemException e) {
		return false;
	}		*/
	

}	

public static void mkdirCascade ( String dirname , ISitzung sitzung) throws FileSystemException, StorageLimitException  {
	mkdirCascade ( CoreBuilder.fsm.resolveFile(dirname),sitzung);

}	

public static void mkdirCascade ( FileObject fo, ISitzung sitzung ) throws FileSystemException, StorageLimitException  {
	// sitzung.grantsDelete(fo);

	long opnum = sitzung.grantsUpdateFolderP1(fo,"mkdirCascade");	

	fo.createFolder();

	sitzung.grantsUpdateFolderP2(fo, opnum);

}

public static String makeyBeautifulUri ( String fileDirName) {
	try {
		FileSystemManager fsm = CoreBuilder.fsm;
		FileObject fileObject = fsm.resolveFile(fileDirName);
		
		
		return fileObject.getName().getURI();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		
		return "";
	}
}
/*
public static boolean existsFileDir ( String filename, ISitzung sitzung ) throws FileSystemException {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(filename);
	
	sitzung.grants(fileObject);

	return ( fileObject.exists() );
} */
/*
public static FileObject vfsResolve ( String filename, ISitzung sitzung ) throws FileSystemException {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(filename);
	if ( fileObject.isFolder())
		sitzung.grantsFolder(fileObject);
	else
		sitzung.grants(fileObject);

	return ( fileObject );
}*/

public static FileObject resolveFolderDelete ( String foldername, ISitzung sitzung ) throws FileSystemException, StorageLimitException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(foldername);
	sitzung.grantsUpdateFolder(fileObject);
	return ( fileObject );
}

public static FileObject resolveFolderRead ( String foldername, ISitzung sitzung ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(foldername);
	sitzung.grantsFolderRead(fileObject);
	return ( fileObject );
}

public static FileObject grantsFolderRead ( String foldername, ISitzung sitzung ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(foldername);
	sitzung.grantsFolderRead(fileObject);
	return ( fileObject );
}

public static FileObject resolveFolderSilent ( String foldername ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(foldername);
	return ( fileObject );
}

public static FileObject resolveAnySilent ( String folderFileName ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(folderFileName);
	return ( fileObject );
}

public static FileObject resolveFileRead ( String filename, ISitzung sitzung ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
	sitzung.grantsFileRead(fileObject);
	return ( fileObject );
}
public static FileObject resolveFileProcess ( String filename, ISitzung sitzung ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
	sitzung.grantsFileProcess(fileObject);
	return ( fileObject );
}

public static FileObject resolveFileSilent ( String filename ) throws FileSystemException {

	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
	return ( fileObject );
}
/*
public static FileObject resolveFileRead ( String fileObjektName ) throws FileSystemException  {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(fileObjektName);	
	return fileObject;
	

}*/



/*
public static void grantsDelete ( FileObject fo, ISitzung sitzung ) throws FileSystemException, StorageLimitException  {
	sitzung.grantsDelete(fo);
	
}

public static void grantsWrite ( String fo, ISitzung sitzung , long newSize ) throws FileSystemException, StorageLimitException  {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(fo);	
	sitzung.grantsWrite(fileObject, newSize);
	
} */
/*
public static FileObject getFileObjectByFilenameToWrite( 
		String filename,
		ISitzung dieSitzung ) throws FileSystemException, StorageLimitException {
	
	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);  
	dieSitzung.grantsUpdate1(fileObject);

	return fileObject;		
} */
/*
public static FileObject getFileObjectByFilenameToRead( 
		String filename,
		ISitzung dieSitzung) throws FileSystemException {
	
	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename); 
	dieSitzung.grantsFileRead(fileObject);

	return fileObject;		
}
*/

public static void grantsFileRead ( FileObject fo, ISitzung sitzung ) throws FileSystemException  {
	sitzung.grantsFileRead(fo);
}
public static void grantsFileProcess ( FileObject fo, ISitzung sitzung ) throws FileSystemException  {
	sitzung.grantsFileProcess(fo);
}

public static FileObject grantsFileRead( 
		String filename, 
		FileSystemOptions fileSystemOptions, ISitzung sitzung) throws FileSystemException {
	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename,fileSystemOptions);    
	sitzung.grantsFileRead(fileObject);
	return fileObject;		
}

public static FileObject grantsFileRead ( String fi, ISitzung sitzung ) throws FileSystemException  {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(fi);	
	sitzung.grantsFileRead(fileObject);
	return fileObject;
	
}
/*
public static FileObject grantsUpdateFileP1 ( String foName, ISitzung sitzung ) throws FileSystemException, StorageLimitException  {
	FileObject fileObject = resolveFileSilent(foName);
	sitzung.grantsUpdateFileP1(fileObject);
	return fileObject;
	
} 
*/
public static long grantsUpdateFileP1 ( FileObject fo, ISitzung sitzung, String titel ) throws FileSystemException, StorageLimitException  {
	return sitzung.grantsUpdateFileP1(fo,titel);
}

public static void grantsUpdateFileP2 ( FileObject fo, ISitzung sitzung, long opnum ) throws FileSystemException, StorageLimitException  {
	sitzung.grantsUpdateFileP2(fo,opnum);
	
}



public static long grantsUpdateFolderP1 ( FileObject fo, ISitzung sitzung, String titel ) throws FileSystemException, StorageLimitException  {
	return sitzung.grantsUpdateFolderP1(fo,titel);	
}

public static void grantsUpdateFolderP2 ( FileObject fo, ISitzung sitzung, long opnum ) throws FileSystemException, StorageLimitException  {
	sitzung.grantsUpdateFolderP2(fo, opnum);
}

/*
public static void checkFolderToRead ( FileObject fo, ISitzung sitzung ) throws FileSystemException  {
	sitzung.grantsDeleteFolder(fo);
	
}*/

/*
public static void checkFolderForUpdatePhase1 ( FileObject fo, ISitzung sitzung ) throws FileSystemException  {
	sitzung.grantsDeleteFolder(fo);
	
}
public static void checkFolderForUpdatePhase2 ( FileObject fo, ISitzung sitzung ) throws FileSystemException  {
	sitzung.grantsDeleteFolder(fo);
	
}
*/




/*
public static void grantsDelete ( String fo, ISitzung sitzung ) throws FileSystemException  {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(fo);	
	sitzung.grantsDelete(fileObject);
	

}*/


/*
public static long getSizeOfFile( FileObject fo, ISitzung sitzung ) throws FileSystemException {
	
	return getSizeOfFileSilent(fo,sitzung);	
	
}*/

public static long getSizeOfFileSilent( FileObject fo ) throws FileSystemException {
	if ( fo.isFile() && fo.exists()) {
		FileContent content = fo.getContent();
		return content.getSize();	
	} else
		return 0;
	
}
/*
public static long getSizeOfDir( FileObject fo, ISitzung sitzung ) throws FileSystemException {
	return getSizeOfDirVfs(fo,sitzung);
}*/
public static long getSizeOfDirSilent( FileObject fo ) throws FileSystemException {
	//System.out.println("-IN--DIRSIZE----------------------------------------------");
	//System.out.println("--  "+fo.getName().getURI());
	//System.out.println("----------------------------------------------------------");
	return getSizeOfDirSilentInternal( fo, 0 );
	//System.out.println("-OUT------------------------------------------------------");
	/*
	long total = 0;
	// FileObject[] kinder = fo.findFiles(new FileSelectorAllContent());
	FileObject[] kinder = fo.getChildren();
	
	for (FileObject aktuell : kinder) {
		if ( aktuell.isFile()) {
			total = total + aktuell.getContent().getSize();
		} else if ( aktuell.isFolder()) {
			total = total + getSizeOfDirSilent(aktuell);
		}
		
	}

	return total; */

	
}

private static long getSizeOfDirSilentInternal( FileObject fo, int level ) throws FileSystemException {
	long total = 0;
	// FileObject[] kinder = fo.findFiles(new FileSelectorAllContent());
	FileObject[] kinder = fo.getChildren();
	
	for (FileObject aktuell : kinder) {
		if ( aktuell.isFile()) {
			total = total + aktuell.getContent().getSize();
		} else if ( aktuell.isFolder()) {
			total = total + getSizeOfDirSilentInternal(aktuell, level+1);
		}
		
	}
	
	//System.out.println(level+ " [ "+Commons.leadBlanks(""+total, 10)+"] "+Commons.StringTimes("  ", level)+fo.getName().getURI());
	return total;

	
}
/*
public static String getNameOfFile ( String file ) throws FileSystemException { 
	
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(file);
	
	FileName fileName = fileObject.getName();
	return (fileName.getBaseName());
	
}
	
	public static String getCanoDirXr ( String dirname ) throws FileSystemException {
		if ( dirname == null ) return null;
		FileObject fo = CoreBuilder.fsm.resolveFile(dirname);
		FileName fn = fo.getName();
		return Commons.vfs2xrDirXXX(fn.getURI());
		
	}	
	*/
	public CommonsVfs() {
		// TODO Auto-generated constructor stub
	}
	
	public static FileObject constructNormalizedDirPerText( String filename ) throws FileSystemException {

		
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	return fileObject;		
	}	

	public static String constructNormalizedDir( FileName filename ) throws FileSystemException {
		String vorschlag = filename.getURI();
		
		return Commons.addFsInCase(vorschlag);		
	}
	public static String constructNormalizedFile( FileName filename ) throws FileSystemException {
		return (filename.getURI());
	}
	/*	
	public static String vfs2xrDirXXX( FileName filename ) {
		String vorschlag = filename.getURI();
		
		return Commons.addFsInCase(vorschlag);
	}

	public static String getCanoFileXr ( String filename ) throws FileSystemException {
		if ( filename == null ) return null;
		FileObject fo = CoreBuilder.fsm.resolveFile(filename);
		FileName fn = fo.getName();
		return Commons.vfs2xrFileXXX(fn.getURI());
		
	}
	
	public static String getCanoFileXrIfExists ( String filename ) throws FileSystemException {
		if ( filename == null ) return null;
		if (existsFile(filename))
			return getCanoFileXr(filename);
		else
			return Commons.convertSlashes(filename);
	}
	*/

	/*
	public static String getDirOfFile2 ( String filename ) throws FileSystemException {
	FileSystemManager fsm = CoreBuilder.fsm;
	FileObject fileObject = fsm.resolveFile(filename);
	String vorschlag = fileObject.getParent().getName().getURI();
	vorschlag = Commons.vfs2xrDirXXX(vorschlag);

	return ( vorschlag );

	}
	
	
	public static String getDirOfDir ( String dirname ) throws FileSystemException {
		
		FileSystemManager fsm = CoreBuilder.fsm;
		FileObject fileObject = fsm.resolveFile(dirname);
		if ( fileObject.getParent() == null )
			return null;
		String vorschlag = fileObject.getParent().getName().getURI();
		vorschlag = Commons.vfs2xrDirXXX(vorschlag);

		return ( vorschlag );
		
	}
	public static String vfs2xrDirXXX( FileObject fo ) {
		String vorschlag = fo.getName().getURI();

		
		return Commons.addFsInCase(vorschlag);
	}
	

	public static String getDirOfFile ( String filename ) throws FileSystemException {
		return getDirOfFile2 (filename);

	}
	public static String getDirOfDir2 ( String dirname ) throws FileSystemException {
		return getDirOfDir(dirname);

	}
	
	public static String getFileOfFile2 ( String file ) throws FileSystemException {
		String first = getNameOfFile ( file );
		int dot_pos = first.indexOf(".");
		if ( dot_pos == -1 )
			return first;
		else {
			String second = first.substring(0, dot_pos);
			return second;
		}

	}
	*/





	
	public static long getSizeByFilename( String filename, ISitzung sitzung) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	sitzung.grantsFileRead(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getSize();
		
	}
	/*
	public static FileObject getFileObjectByFilenameUsave( 
			String filename) throws FileSystemException {
		
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);  

    
    	return fileObject;		
	}	
	*/
	
	
	/*
	 * 	public static FileObject getFileObjectByFilename( String filename) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);    	
    
    	return fileObject;		
	}
	 
	
	public static FileObject getFileObjectByFilename( 
			String filename, 
			FileSystemOptions fileSystemOptions, ISitzung sitzung) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename,fileSystemOptions);    
    	sitzung.grantsFileRead(fileObject);
    	return fileObject;		
	}	*/
	
	/*
	public static String getUriByFilename( String file_name) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(file_name);    	
    	return fileObject.getName().getURI();		
	}	
	
	
	public static String getUriByDirname( String dir_name) throws FileSystemException {
		if ( dir_name == null ) return null;
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(dir_name);    	
    	return fileObject.getName().getURI()+"/";		
	}	
		
	
	public static FileName getFileNameByFilename( String filename) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);    	
    	return fileObject.getName();		
	}	
	*/
	
	public static FileContent getFileContentByFilename( String filename, ISitzung sitzung) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename); 
    	sitzung.grantsFileRead(fileObject);
    	return fileObject.getContent();		
	}	
	
	public static InputStream getInputStreamByFilename( String filename, ISitzung sitzung) throws Exception {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	sitzung.grantsFileRead(fileObject);
    	sitzung.checkMaxFileMemory(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getInputStream();		
	}
	public static InputStream getInputStreamByFilenameBlind( String filename) throws Exception {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);

    	FileContent content = fileObject.getContent();
    	return content.getInputStream();		
	}	
	public static InputStream getInputStreamByFilenameForProcessing( String filename, ISitzung sitzung) throws Exception {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	sitzung.grantsFileProcess(fileObject);
    	sitzung.checkMaxFileMemory(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getInputStream();		
	}	
	
	public static InputStream getInputStreamByFileObject( FileObject fileObject, ISitzung sitzung) throws FileSystemException {
		sitzung.grantsFileRead(fileObject);
		FileContent content = fileObject.getContent();
    	return content.getInputStream();		
	}	
	
	// Vfs-Version
	public static OutputStreamRequest getOutputStreamByFilenameP1( String filename, boolean append, ISitzung sitzung) throws FileSystemException, StorageLimitException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	long opnum = sitzung.grantsUpdateFileP1(fileObject,"getOutputStreamFN");
    	
    	FileContent content = fileObject.getContent();
    	return new OutputStreamRequest(content.getOutputStream(append),opnum);		
	}	
	
	public static void finishOutputStreamP2 ( String filename, ISitzung sitzung, long opnum ) throws FileSystemException, StorageLimitException{
		FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
		sitzung.grantsUpdateFileP2(fileObject,opnum);
	}

	
	public static long getLastModByFilename( String filename, ISitzung sitzung) throws FileSystemException {
    	FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
    	sitzung.grantsFileRead(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getLastModifiedTime();
		
	}	
	
	public static long getTimeOfFileObject( FileObject fileObject, ISitzung sitzung) throws FileSystemException {
		sitzung.grantsFileRead(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getLastModifiedTime();
		
	}	
	
	public static long getTimeOfFileObjectForProcessing( FileObject fileObject, ISitzung sitzung) throws FileSystemException {
		sitzung.grantsFileProcess(fileObject);
    	FileContent content = fileObject.getContent();
    	return content.getLastModifiedTime();
		
	}	
	
	public static long getTimeOfNow( ) throws FileSystemException {
		Date mydate = new Date();
		return mydate.getTime();		
	}	
	
	public static long getTimeOfDate(String yyyyMMdd ) throws Exception {
		Date mydate;
		mydate= new SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd);
		return mydate.getTime();		
	}	
	
	public static long getTimeOfJustTest( ) throws Exception {
		if ( new Date().getTime() > getTimeOfDate("21000101"))
			throw new Exception("Garantiert");
		return 9;		
	}	
	
	public static void writeToFileVA( String fileinhalt, String filename, ISitzung sitzung ) throws IOException, StorageLimitException {
    	// FileObject fileObject = CoreBuilder.fsm.resolveFile(filename);
		FileObject fileObject = resolveAnySilent(filename);
    	long opnum = sitzung.grantsUpdateFileP1(fileObject,"writeToFile");
    	// sitzung.grants(fileObject);
    	FileContent content = fileObject.getContent();
    	OutputStream os = content.getOutputStream();

    	byte[] ptext = fileinhalt.getBytes("UTF-8");
    	
    	os.write(ptext);
    	os.close();
    	grantsUpdateFileP2(fileObject, sitzung,opnum);

	}	
	
	
	public static void copy_filename ( String fni, String fno, ISitzung sitzung) throws IOException, StorageLimitException {
		
		
		FileObject i = CommonsVfs.resolveFileSilent(fni);
		FileObject o = CommonsVfs.resolveFileSilent(fno);
		copy_filename(i,o,sitzung);
	
	}	
	
	public static void copy_filename_blind ( String fni, String fno) throws IOException, StorageLimitException {
		
		
		FileObject i = CommonsVfs.resolveFileSilent(fni);
		FileObject o = CommonsVfs.resolveFileSilent(fno);
		FileUtil.copyContent(i,o);
	
	}	
	
	public static void copy_filename ( FileObject fni, FileObject fno, ISitzung sitzung) throws IOException, StorageLimitException {
		grantsFileRead(fni, sitzung);
		long opnum = sitzung.grantsUpdateFileP1(fno,"copy_filename");
				
		FileUtil.copyContent(fni,fno);
		grantsUpdateFileP2(fno, sitzung, opnum);
	}		
	
	public static void unzip ( FileObject targetDir, FileObject sourceFile, 
			ISitzung mySitzung) throws FileSystemException, StorageLimitException  {
		
        final FileObject zipFileSystem = CoreBuilder.fsm.createFileSystem(sourceFile);
        
        CommonsVfs.grantsFileRead(sourceFile, mySitzung);
        long opnum = CommonsVfs.grantsUpdateFolderP1(targetDir, mySitzung,"unzip");

        try {	
        	targetDir.copyFrom(zipFileSystem,new FileSelectorAll());
        	
        } catch (FileSystemException e2) {
	    	  Sp.p("Problem in creating the filesystem "); 
	    	  if ( e2.getCause() != null && 
	    		   e2.getCause().getMessage() !=null &&
	    		   e2.getCause().getMessage().equals("zip file is empty")) {
	    		  Sp.p(".. because the zip file is empty: "+sourceFile.getName().getURI());
	    		  Sp.pe(e2);
	    		  CommonsVfs.mkdirCascade(targetDir,mySitzung);
	    	  } else {
	    		  Sp.pe(e2);
	    		  // throw e2;
	    	  }
        } finally {
            zipFileSystem.close();
            sourceFile.close();
            CommonsVfs.grantsUpdateFolderP2(targetDir, mySitzung,opnum);
        }	
		
	}

	public static Document getXmldocumentByFilename( String filename, ISitzung sitzung)  throws Exception, JDOMException, IOException{
		FileObject foxml =resolveFileSilent(filename);
		if ( CommonsVfs.getTimeOfFileObject(foxml,sitzung) > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}
		grantsFileRead(foxml, sitzung);				
		sitzung.checkMaxFileMemory(foxml);
		InputStream fis = getInputStreamByFilename(filename, sitzung);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			doc = builder.build(fis);
			// fis.close();
		} catch (Exception e1) {
			
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);
			//String inh = CommonsVfs.readTextfile(filename, sitzung);
			//System.out.println("--------------------inhalt-----------"+inh);
			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  "+filename
						);
			
		} finally {
			if ( fis != null ) fis.close();
		}
		
		return ( doc );
	}

	public static Document getXmldocumentByFilenameBlind( String filename)  throws Exception, JDOMException, IOException{


		InputStream fis = getInputStreamByFilenameBlind(filename);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			doc = builder.build(fis);
			// fis.close();
		} catch (Exception e1) {
			
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);
			//String inh = CommonsVfs.readTextfile(filename, sitzung);
			//System.out.println("--------------------inhalt-----------"+inh);
			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  "+filename
						);
			
		} finally {
			if ( fis != null ) fis.close();
		}
		
		return ( doc );
	}	
	public static Element getElementByFilename( String filename, ISitzung sitzung ) throws JDOMException, IOException, Exception {
	
		return ( (getXmldocumentByFilename(filename,sitzung)).getRootElement() );
	
	}

	public static Element getXmlRootOfFile( String filename, ISitzung sitzung ) throws Exception {
		
		Document doc = getXmldocumentByFilename(filename,sitzung);
		Element root = doc.getRootElement();
		return root;
	
	}

	public static void writeToFile( 
				Document doc , 
				String filename, 
				Format format,
				String encoding, ISitzung sitzung ) throws IOException, StorageLimitException {
		  Format myformat = format.clone();
		  OutputStream os = null ;
	  	  if ( encoding != null )
			  myformat.setEncoding(encoding);
		  else
			  myformat.setEncoding("utf-8");
		  XMLOutputter serializer = new XMLOutputter(myformat);
		  OutputStreamRequest osr = getOutputStreamByFilenameP1(filename, false,sitzung);
	      try {
	    	  
	    	  
			  os = osr.getOutputStream();
			  serializer.output(doc, os);	
			  
			  
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
	
			Sp.pe(e);
			throw new IOException("writeToFile Xml "+
			"filename="+filename+") not perforemd, Exception="+e.getMessage());
		} finally {
			if ( os!=null) os.close();
			finishOutputStreamP2(filename, sitzung,osr.getOpnum());
		}
	}

	public static void writeToFile( Document doc , String filename, ISitzung sitzung ) throws IOException, StorageLimitException {
		
		writeToFile ( doc,filename,Format.getRawFormat(),"utf-8", sitzung );
	}

	public static void writeToFilePretty( Document doc , String filename, ISitzung sitzung ) throws IOException, StorageLimitException {
		writeToFile ( doc,filename,Format.getPrettyFormat(),"utf-8",sitzung);
	}

	public static void writeToFileFormat( Document doc , String filename, Format format, ISitzung sitzung ) throws IOException, StorageLimitException {
		writeToFile ( doc,filename,format,"utf-8",sitzung); 
	}

	public static void writeToUTF8File( String fileinhalt, String filename, ISitzung sitzung ) throws IOException, StorageLimitException  {
				OutputStreamRequest osr = getOutputStreamByFilenameP1(filename, false,sitzung);
				OutputStream os = osr.getOutputStream();
				Writer out = null;
				try {
					out = new BufferedWriter(new OutputStreamWriter(
					    os, "UTF8"));
					out.write(fileinhalt);
					out.close();
					os.close();
					finishOutputStreamP2(filename, sitzung,osr.getOpnum());
					
				} catch (Exception e) {
					Sp.pe(e);
					throw e;
				} finally {
					if ( out!= null ) out.close();
					if ( os != null ) os.close();
	
				}
		        
	}

	public static void writeToEncodedFile( String fileinhalt, String filename, String encoding, ISitzung sitzung ) throws IOException, StorageLimitException  {
		OutputStreamRequest osr = getOutputStreamByFilenameP1(filename, false,sitzung);
		OutputStream os = osr.getOutputStream();
	    
		Writer out =null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
			    os, encoding ));
			out.write(fileinhalt);
			out.close();
			os.close();
			finishOutputStreamP2(filename, sitzung, osr.getOpnum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			throw e;
		} finally {
			if ( out!= null ) out.close();
			if ( os != null ) os.close();			
		}
	}	
	/* alt
	public static void writeToEncodedFile( String fileinhalt, String filename, String encoding ) throws Exception {
	    Writer out;
		out = new BufferedWriter(new OutputStreamWriter(
		    new FileOutputStream(filename), encoding ));
	    out.write(fileinhalt);
	    out.close();
	    }
	*/

	public static Element xmltextToElement( String in, ISitzung sitzung, String tempDir) throws Exception {
			String theTempFileName = tempDir+"tempTextIsXml"+Commons.randomAlphaNumeric(5)+Commons.getTsMs()+".xml";
			writeToUTF8File( in,theTempFileName ,sitzung );
			Document document = getXmldocumentByFilename(theTempFileName,sitzung);
			Element root = document.getRootElement();
			deleteFile(theTempFileName, sitzung);
			return root;		
	}
	/*
	public static Document getXmldocumxxxentByText( String filetext ) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		
		File file = new File(filetext);
		Document doc = builder.build(file);
		return ( doc );
	
	}	
	*/

	public static String readTextfilePlain(String fileName, ISitzung sitzung) throws Exception {
	
	
	    StringBuffer sb = new StringBuffer();
	    BufferedReader in =
	      new BufferedReader(new InputStreamReader(getInputStreamByFilename(fileName,sitzung)));
	    String s;
	    while((s = in.readLine()) != null) {
	      sb.append(s);
	    }
	    in.close();
	    return sb.toString();
	  }

	public static String readTextfile(String fileName, ISitzung sitzung) throws Exception {
		return ( CommonsVfs.readTextfileFormat(fileName,"UTF-8",sitzung));
	  }

	public static String readTextfileFormat(String fileName, String format, ISitzung sitzung) throws Exception {
		return CommonsVfs.readTextfileFormatLfURI(fileName, format, null,sitzung);
	}

	public static String readTextfileFormatLfDefault(String fileName, String format, ISitzung sitzung) throws Exception {
		return CommonsVfs.readTextfileFormatLfURI(fileName,format,Commons.lineSep,sitzung);
	}

	public static String readTextfileFormatLfURI(
			String uri, 
			String format, 
			String lf, ISitzung sitzung) throws Exception {
		String myFormat = format;
		if ( myFormat == null ) myFormat = "UTF-8";
		FileObject fileObject = grantsFileRead(uri, sitzung);
		sitzung.checkMaxFileMemory(fileObject);
		BufferedReader in = null;
	    try {
	    	
	    	FileContent content = fileObject.getContent();
	    	
			StringBuffer sb = new StringBuffer();
			in =
			  new BufferedReader(new InputStreamReader(content.getInputStream()));
			String currentLine = in.readLine();
			String nextLine = in.readLine();
			while ( null != currentLine ) {
				
				String UTF8Str = new String(currentLine.getBytes(),myFormat);
				sb.append(UTF8Str);			
				
				if ( null != nextLine && lf !=null )
					sb.append(lf);
				
				currentLine = nextLine;
				nextLine = in.readLine();
				
				
			}
			/* problem with last line and linefeed
			String s;
			while((s = in.readLine()) != null) {
				String UTF8Str = new String(s.getBytes(),myFormat);
				sb.append(UTF8Str);
				if ( lf != null ) {
					sb.append(lf);
				}
			} */
			//in.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( in != null ) 
			Sp.p("--- News from readTextfileFormatLfURI("+uri+", "+format+") ---");
			Sp.pe(e);
			throw e;
		} finally {
			if ( in != null ) in.close();
		}
	  }

	public static boolean textIsXml( String in, ISitzung sitzung, String tempDir) {
		
		boolean isxml = false;
		try {
			Element element = xmltextToElement ( in,sitzung,tempDir);
			isxml=true;
		} catch ( Exception e ) {}
		
		return isxml;
		
	}	
/*
 * 	public static void copy_filename_silent ( String fni, String fno) throws IOException, StorageLimitException {
		
		
		FileObject i = CommonsVfs.resolveFileSilent(fni);
		FileObject o = CommonsVfs.resolveFileSilent(fno);
		FileUtil.copyContent(i,o);
		
	
	}
 */
}
