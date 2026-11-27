/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Vfs implements IFun {
	private static final int act_exists   = 0;
	private static final int act_pub_uri_string   = 1;
	private static final int act_type   = 2;
	private static final int act_url   = 3;
	private static final int act_url_prot   = 4;
	private static final int act_is_attached   = 5;
	private static final int act_is_content_open   = 6;
	private static final int act_is_executable   = 7;
	private static final int act_is_file   = 8;
	private static final int act_is_folder   = 9;
	private static final int act_is_hidden   = 10;
	private static final int act_is_readable   = 11;
	private static final int act_is_writable   = 12;
	private static final int act_base   = 13;
	private static final int act_depth   = 14;
	private static final int act_extension   = 15;
	private static final int act_friendly_uri   = 16;
	private static final int act_path   = 17;
	private static final int act_path_decoded   = 18;
	private static final int act_root_path   = 19;
	private static final int act_root_base   = 20;
	private static final int act_root_uri   = 21;
	private static final int act_scheme   = 22;
	private static final int act_uri   = 23;
	private static final int act_name_is_file   = 24;
	private static final int act_root_path_decoded   = 25;
	
	private static KeyWordMapper tasknames;
	
	private static final String funn = "vfs";
	private static final String pm   = "vfs: ";
	/*[timerx]*/
	 private static long getTimeOfFileObject( org.apache.commons.vfs2.FileObject fileObject) throws org.apache.commons.vfs2.FileSystemException { org.apache.commons.vfs2.FileContent content = fileObject.getContent(); return content.getLastModifiedTime();  }  private static long getTimeOfNow() throws org.apache.commons.vfs2.FileSystemException { java.util.Date mydate = new java.util.Date(); return mydate.getTime(); }  private static long getTimeOfDate(String yyyyMMdd ) throws Exception { java.util.Date mydate; mydate= new java.text.SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd); return mydate.getTime(); }  private static long getTimeOfJustTest( ) throws Exception { if ( new java.util.Date().getTime() > getTimeOfDate("20200101")) throw new Exception("Garantiert"); return 9; }	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(pm+"I want 1/2 parameters: 1=filename, [2=algorithm] but i received "+vcar_list.size()+" Parameters");
		
		String filename = ((Vcar)vcar_list.get(0)).s;
		
		
		FileSystemManager fsm = CoreBuilder.fsm;
		FileObject fo = fsm.resolveFile(filename);		
		FileName fn = fo.getName();		
		
		String ret = null;
		int ac = tasknames.convert(((Vcar)vcar_list.get(1)).s);

		switch ( ac ) {
		case act_exists: {	
			if ( CommonsVfs.getTimeOfFileObject(fo,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) {
				throw new Exception("mist");
			}					
			ret = Commons.boolToStringXr(fo.exists());
			break;
		}
		case act_pub_uri_string: {	
			ret = fo.getPublicURIString();
		    break;
		}
		case act_type: {	
			ret = fo.getType().getName();
		    break;
		}
		case act_url: {	
			ret = fo.getURL().toString();
		    break;
		}
		case act_url_prot: {	
			ret = fo.getURL().getProtocol();
		    break;
		}
		case act_is_attached: {	
			ret = Commons.boolToStringXr(fo.isAttached());
		    break;
		}case act_is_content_open: {	
			ret = Commons.boolToStringXr(fo.isContentOpen());
		    break;
		}
		case act_is_executable: {	
			ret = Commons.boolToStringXr(fo.isExecutable());
		    break;
		}
		case act_is_file: {	
			ret = Commons.boolToStringXr(fo.isFile());
		    break;
		}
		case act_is_folder: {
			ret = Commons.boolToStringXr(fo.isFolder());
		    break;
		}
		case act_is_hidden: {	
			ret = Commons.boolToStringXr(fo.isHidden());
		    break;
		}
		case act_is_readable: {	
			ret = Commons.boolToStringXr(fo.isReadable());
		    break;
		}	
		case act_is_writable: {	
			ret = Commons.boolToStringXr(fo.isWriteable());
		    break;
		}
		case act_base: {	
			ret = fn.getBaseName();	
		    break;
		}
		case act_depth: {	
			ret = Commons.int2String(fn.getDepth()) ;
		    break;
		}
		case act_extension: {	
			ret = fn.getExtension() ;
		    break;
		}
		case act_friendly_uri: {	
			ret = fn.getFriendlyURI() ;
		    break;
		}
		case act_path: {	
			ret = fn.getPath() ;
		    break;
		}	
		case act_path_decoded: {	
			ret = fn.getPathDecoded() ;	
		    break;
		}
		case act_root_path_decoded: {
			ret = fn.getRoot().getPathDecoded() ;
		    break;
		}
		case act_root_path: {	
			ret = fn.getRoot().getPath() ;	
		    break;
		}		
		case act_root_base: {
			ret = fn.getRoot().getBaseName() ;	
		    break;
		}
		case act_root_uri: {	
			ret = fn.getRootURI() ;
		    break;
		}
		case act_scheme: {	
			ret = fn.getScheme() ;
		    break;
		}
		case act_uri: {	
			ret = fn.getURI() ;
		    break;
		}
		case act_name_is_file: {
			ret = Commons.boolToStringXr(fn.isFile()) ;
		    break;
		}		
		}		

				
		return (new Vcar(ret));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Uri-Function",26);
		tasknames.put("exists", act_exists);
		tasknames.put("pub_uri_string", act_pub_uri_string);
		tasknames.put("type", act_type);
		tasknames.put("url", act_url);
		tasknames.put("url_prot", act_url_prot);
		tasknames.put("is_attached", act_is_attached);
		tasknames.put("is_content_open", act_is_content_open);
		tasknames.put("is_executable", act_is_executable);
		tasknames.put("is_file", act_is_file);
		tasknames.put("is_folder", act_is_folder);
		tasknames.put("is_hidden", act_is_hidden);
		tasknames.put("is_readable", act_is_readable);
		tasknames.put("is_writable", act_is_writable);
		tasknames.put("base", act_base);
		tasknames.put("depth", act_depth);
		tasknames.put("extension", act_extension);
		tasknames.put("friendly_uri", act_friendly_uri);
		tasknames.put("path", act_path);
		tasknames.put("path_decoded", act_path_decoded);
		tasknames.put("root_path_decoded", act_root_path_decoded);
		tasknames.put("root_path", act_root_path);
		tasknames.put("root_base", act_root_base);
		tasknames.put("root_uri", act_root_uri);
		tasknames.put("scheme", act_scheme);
		tasknames.put("uri", act_uri);
		tasknames.put("name_is_file", act_name_is_file);
		tasknames.putFinished();		
	}

}