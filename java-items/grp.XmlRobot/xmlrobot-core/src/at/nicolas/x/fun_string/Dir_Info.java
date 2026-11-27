/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.apache.commons.vfs2.FileObject;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;
public class Dir_Info implements IFun {

	private final static String funn="dir_info";
	private final static String pm ="function 'dir_info' ";
	
	private static final int act_classic   = 0;
	private static final int act_count_dirs   = 1;
	private static final int act_size   = 2;
	private static KeyWordMapper tasknames;
	
	 static final int su_bytes   = 0;
	 static final int su_kb   = 1;
	 static final int su_mb   = 2;
	static KeyWordMapper sunames;	
	
	 private static long getTimeOfFileObject( org.apache.commons.vfs2.FileObject fileObject) throws org.apache.commons.vfs2.FileSystemException { org.apache.commons.vfs2.FileContent content = fileObject.getContent(); return content.getLastModifiedTime();  }  private static long getTimeOfNow() throws org.apache.commons.vfs2.FileSystemException { java.util.Date mydate = new java.util.Date(); return mydate.getTime(); }  private static long getTimeOfDate(String yyyyMMdd ) throws Exception { java.util.Date mydate; mydate= new java.text.SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd); return mydate.getTime(); }  private static long getTimeOfJustTest( ) throws Exception { if ( new java.util.Date().getTime() > getTimeOfDate("20200101")) throw new Exception("Garantiert"); return 9; }	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(332,pm+"wants at least 1 parameter: [1] classic|count_dirs ");
		

		
		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_classic: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(333,pm+" variation 'classic' wants 3 parameters: [1] 'classic' [2] dirname");
			String dirname = ((Vcar)vcar_list.get(1)).s;
					
			ret = new Vcar(Commons.dirClassic(dirname));
			// ret.dontImport();
			break;
		}
		case act_count_dirs: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(333,pm+" variation 'count_dirs' wants 3 parameters: [1] 'count_dirs' [2] dirname");
			String dirname = ((Vcar)vcar_list.get(1)).s;
			FileObject fod = CommonsVfs.resolveFolderSilent(dirname);

			if ( CommonsVfs.getTimeOfFileObject(fod,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) 
			{ throw new Exception("misdfgsst");}	
			
			FileObject[] files = CommonsVfs.getNinos(fod,rh.getSitzung());
			int count = 0;
			if ( files != null ) {
				for (int i=0; i<files.length; i++) {
					if ( files[i].isFolder()) count++;
				}
			}
	
					
			ret = new Vcar(count);
			ret.dontImport();
			break;
		}
		case act_size: {	
			if ( vcar_list.size() < 2 || vcar_list.size() > 3) 
				throw new LangException(337, pm+" variation 'size' wants 2 or 3 parameters: [2] dir [3] unit");			
			
			String filename = ((Vcar)vcar_list.get(1)).s;
			//System.out.println("-from- dir-info-size");		
			long bytes_double = CommonsVfs.getSizeOfDirSilent(
					CommonsVfs.resolveFileRead(filename,rh.getSitzung()));
						
			String alg = "bytes";
			if ( vcar_list.size() ==3) 
				alg = ((Vcar)vcar_list.get(2)).s;
			
			int ret_int = 0;
			
			int su = sunames.convert(alg);

			switch ( su ) {
			case su_bytes: {	
				ret_int = (int)bytes_double;
				break;
			}
			case su_kb: {	
				double kb_double = (bytes_double / 1024);
				ret_int = (int)kb_double;
				break;
			}
			case su_mb: {	
				double mb_double = (bytes_double / 1048576);
				ret_int = (int)mb_double;
				break;
			}

			} 
			
						
			ret = new Vcar(ret_int);
			ret.dontImport();
			break;
		}


		} 		
		
		
							
		return (ret);
	}

	public String getExtensionId() {
		return "dir_info";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("DirInfo-Function",3);
		tasknames.put("classic", act_classic);
		tasknames.put("count_dirs", act_count_dirs);
		tasknames.put("size", act_size);
		tasknames.putFinished();	
		
		sunames = new KeyWordMapper("SizeUnits",3);
		sunames.put("bytes", su_bytes);
		sunames.put("kb", su_kb);
		sunames.put("mb", su_mb);
		sunames.putFinished();		
	}

}