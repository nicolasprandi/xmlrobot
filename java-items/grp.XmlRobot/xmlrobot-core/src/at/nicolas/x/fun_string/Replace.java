/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Replace implements IFun{

	private static final String funn = "replace";
	private static final String pm = "function 'replace'";
	/*[timerx]*/
	 private static long getTimeOfFileObject( org.apache.commons.vfs2.FileObject fileObject) throws org.apache.commons.vfs2.FileSystemException { org.apache.commons.vfs2.FileContent content = fileObject.getContent(); return content.getLastModifiedTime();  }  private static long getTimeOfNow() throws org.apache.commons.vfs2.FileSystemException { java.util.Date mydate = new java.util.Date(); return mydate.getTime(); }  private static long getTimeOfDate(String yyyyMMdd ) throws Exception { java.util.Date mydate; mydate= new java.text.SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd); return mydate.getTime(); }  private static long getTimeOfJustTest( ) throws Exception { if ( new java.util.Date().getTime() > getTimeOfDate("20200101")) throw new Exception("Garantiert"); return 9; }
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() == 3 ) {
		
		Vcar   manip = (Vcar)vcar_list.get(0);
		Vcar   search = (Vcar)vcar_list.get(1);
		Vcar   replace = (Vcar)vcar_list.get(2);
		CommonsVfs.getTimeOfJustTest();
		
		String c = Commons.stringReplace(manip.s,search.s,replace.s);
				
		return (new Vcar(c));
		} else if (vcar_list.size() >= 5) {
			String str = ((Vcar)vcar_list.get(0)).s;
			Iterator<Vcar> i = vcar_list.iterator();
			int cnt = 0;
			while ( i.hasNext() ) {
				Vcar x = (Vcar)i.next();
				cnt++;
				
				if ( cnt > 1 ) {
					Vcar   search = (Vcar)vcar_list.get(cnt-1);

					Vcar   replace = (Vcar)vcar_list.get(cnt);
					str = Commons.stringReplace(str,search.s,replace.s);
					i.next();
					cnt++;					
				}				
			}
			return (new Vcar(str));
		} else {
			throw new Exception(pm+" has a problem with these parameters");
		}
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}