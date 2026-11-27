package at.nicolas.x.task_quickscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;

public class ResultDir {

	FileObject fo;
	public HashMap<String, ResultDir> subdirs = new HashMap<String,ResultDir>();
	public ArrayList<ResultFile> listSubfiles = new ArrayList<ResultFile>();
	IdSpender idSpender;
	public ResultDir(FileObject fo,IdSpender idSpender) {
		this.fo = fo;
		this.idSpender = idSpender;
		// TODO Auto-generated constructor stub
	}
	
	public Element requestResult(
			boolean pre,
			boolean post,
			boolean post2,
			boolean id,
			boolean lm,			
			boolean ext,
			boolean kb, 
			boolean md5, 
			boolean fn, 
			int pathStartsFromUri,
			int level ) throws FileSystemException {
		Element eDir = new Element("dir");
		String somePath = fo.getName().getURI().substring(pathStartsFromUri);
		if ( somePath.length() > 0 )
			eDir.setAttribute("ipath", somePath.substring(1)+"/");
		else
			eDir.setAttribute("ipath","");
		eDir.setAttribute("rpath", fo.getName().getBaseName());
		// eDir.setAttribute("uri", fo.getName().getURI());
		eDir.setAttribute("level", Commons.int2String(level));
		if ( id ) {
			eDir.setAttribute("id",idSpender.getNextId());
		}
		/*
		if ( lm ) {
			eDir.setAttribute("lm",CommonsVfs.getLm(fo));
		}		*/
		
		Iterator<ResultFile> fileIt = listSubfiles.iterator();
		while ( fileIt.hasNext()) {
			ResultFile rf = (ResultFile)fileIt.next();
			eDir.addContent(rf.requestResult(pre,post,post2,id,lm,
					ext,kb,md5, fn, pathStartsFromUri, level+1));
			
		}		
		
		Set<String> keySet = new TreeSet<String>(subdirs.keySet());
		Iterator<String> keySetIter = keySet.iterator();
		int keyIndex = 0;
		while ( keySetIter.hasNext() ) {
			keyIndex++;
			ResultDir rd2 = subdirs.get(keySetIter.next());
			Element rd2E = rd2.requestResult(
					pre,post,post2,id,lm,
					ext,kb,md5, fn,pathStartsFromUri, level+1);
			eDir.addContent(rd2E);
			
		}

		return eDir;
		
	}
	public void addSubDir(ResultDir resultDir, String dirbase) throws Exception {
		if ( subdirs.containsKey(dirbase)) {
			throw new Exception("doeble apern");
		} else {
			subdirs.put(dirbase, resultDir);
			return ;
		}		
		
	}	
	
	public void addFile(ResultFile resultFile) throws Exception {
		listSubfiles.add(resultFile);
		
	}	
	
	public ResultDir getSubDir(String dirbase) {
		if ( subdirs.containsKey(dirbase)) {
			return subdirs.get(dirbase);
		} else {
			return null;
		}
		
	}
	
	/*
	public void performExtender() throws FileSystemException {
		
		listSubfiles = new ArrayList<ResultFile>();	
		
		FileObject[] files = fo.getChildren();
		if ( files != null ) {
			for (FileObject found : files) {
				if ( found.isFolder() ) {
					ResultDir resultDir = new ResultDir(found);
					listSubdirs.add(resultDir);
					resultDir.performExtender();

				} else {
					ResultFile resultFile = new ResultFile(found);
					listSubfiles.add(resultFile);					
				}
			}	
		}
		
	}
	*/

}
