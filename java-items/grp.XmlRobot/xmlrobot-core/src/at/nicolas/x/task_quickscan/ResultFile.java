package at.nicolas.x.task_quickscan;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ISitzung;

public class ResultFile {

	FileObject fo;
	IdSpender idSpender;
	ISitzung sitzung;
	public ResultFile(FileObject fo,IdSpender idSpender, ISitzung sitzung) {
		this.fo = fo;
		this.idSpender = idSpender;
		this.sitzung = sitzung;
		// TODO Auto-generated constructor stub
	}
	
	private static String getMd5 ( FileObject file , ISitzung mdSitzung ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,mdSitzung);
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}		
	public Element requestResult(
			boolean pre,
			boolean post,
			boolean post2,
			boolean id,
			boolean lm,
			boolean ext,
			boolean kb, boolean md5, boolean fn,int pathStartsFromUri,int level ) throws FileSystemException  {
		Element eFile = new Element("file");
		if ( fn )
			eFile.setAttribute("fn", fo.getName().getURI());
		// eFile.setAttribute("level", Commons.int2String(level));
		eFile.setText(fo.getName().getBaseName());
		
		if ( md5 ) {
			try {
				eFile.setAttribute("md5", getMd5(fo,sitzung));
			} catch (IOException e) {
				eFile.setAttribute("md5", "problem with requesting md5 "+e.getMessage());
			}
		}
		
		if ( kb ) {
			try {
				double bytes_double = fo.getContent().getSize();
				double kb_double = (bytes_double / 1024);
			
				eFile.setAttribute("kb", Commons.int2String((int)kb_double));
			} catch (IOException e) {
				eFile.setAttribute("kb", "problem with requesting kb "+e.getMessage());
			}
		}	
		
		String theBaseName = fo.getName().getBaseName();
		if ( ext ) {
			String theext = Commons.getExtOfBaseName(theBaseName);
			if ( theext != null)
				eFile.setAttribute("ext", theext);
		}		
		
		int x = theBaseName.indexOf(".");
		if ( pre) {
			if ( x==-1)
				eFile.setAttribute("pre",theBaseName);
			else
				eFile.setAttribute("pre",theBaseName.substring(0,x));						
		}
		
		if ( x != -1 && post )
			eFile.setAttribute("post",theBaseName.substring(x));
		
		if ( x != -1 && post2 ) {
			try {
				eFile.setAttribute("post2",theBaseName.substring(x+1));
			} catch (Exception e) {

			}
		}
		if ( id ) {
			eFile.setAttribute("id",idSpender.getNextId());
		}
		
		if ( lm ) {
			eFile.setAttribute("lm",CommonsVfs.getLm(fo,sitzung));
		}		
		
		return eFile;
	}

}
