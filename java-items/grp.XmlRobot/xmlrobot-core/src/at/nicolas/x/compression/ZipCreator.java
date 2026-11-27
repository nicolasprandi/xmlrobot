package at.nicolas.x.compression;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;

public class ZipCreator {
	ZipOutputStream zos;
	String destFile = null;
	ISitzung sitzung;
	long opnum = -1;
	
	FileObject zipFile = null;
	
	
	 private static long getTimeOfFileObject( org.apache.commons.vfs2.FileObject fileObject) throws org.apache.commons.vfs2.FileSystemException { org.apache.commons.vfs2.FileContent content = fileObject.getContent(); return content.getLastModifiedTime();  }  private static long getTimeOfNow() throws org.apache.commons.vfs2.FileSystemException { java.util.Date mydate = new java.util.Date(); return mydate.getTime(); }  private static long getTimeOfDate(String yyyyMMdd ) throws Exception { java.util.Date mydate; mydate= new java.text.SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd); return mydate.getTime(); }  private static long getTimeOfJustTest( ) throws Exception { if ( new java.util.Date().getTime() > getTimeOfDate("20300101")) throw new Exception("Garantiert"); return 9; }	
	public ZipCreator(String destfileUri, ISitzung _sitzung) throws FileSystemException, StorageLimitException {
		sitzung = _sitzung;
		destFile = destfileUri;
		 zipFile = CommonsVfs.resolveAnySilent(destFile);
				
		opnum=		CommonsVfs.grantsUpdateFileP1(zipFile,_sitzung,"zipCreator");
		zipFile.createFile();
		
		zos = new ZipOutputStream(zipFile.getContent().getOutputStream());

	}
	
	public void addFolder ( String basedirUri ) throws IOException {
		addFolder(basedirUri,"");
		
	}
	
	public void addFolder ( String basedirUri, String internalPath ) throws IOException {
		FileObject fo = CommonsVfs.resolveFolderRead(basedirUri,sitzung);
		addDirectory ( fo,internalPath);
		
	}	
	
	private void addDirectory ( FileObject dir, String internalPath ) throws IOException {
		FileObject[] files = CommonsVfs.getNinos(dir,sitzung);
		if ( files != null ) {
			for (int i=0; i<files.length; i++) {
				FileObject einFile =  files[i];
				String einFileBaseName = einFile.getName().getBaseName();
				if ( einFile.isFile()) {
					ZipEntry zipEntry = new ZipEntry(internalPath+einFileBaseName);
					InputStream is = einFile.getContent().getInputStream();

					byte[] buf = new byte[1024];
					zos.putNextEntry(zipEntry);
					for (int readNum; (readNum = is.read(buf)) != -1;) {
					   zos.write(buf, 0, readNum);
					}
					is.close();
					
				} else {
					addDirectory(einFile,internalPath+einFileBaseName+"/");
				}
			}
		}
		
	}
	
	public void close() throws IOException, StorageLimitException {
		zos.close();
		CommonsVfs.grantsUpdateFileP2(zipFile,sitzung,opnum);
	}

}
