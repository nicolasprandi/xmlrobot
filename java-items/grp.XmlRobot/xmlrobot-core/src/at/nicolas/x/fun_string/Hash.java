/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Hash implements IFun{

	
	private static final String funn = "hash";
	private static final String pm = "hash: ";
	
	  static final int act_md2   = 0;
	   static final int act_md5   = 1;
	   static final int act_sha1   = 2;
	   static final int act_sha256   = 3;
	   static final int act_sha384   = 4;
	   static final int act_sha512   = 5;
	  static KeyWordMapper tasknames;
	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		String code = "";
		if ( vcar_list.size() != 2 ) throw new LangException(375,"I want one parameters: 1=type 2=base");
		
		String alg= ((Vcar)vcar_list.get(1)).s;
		String base= ((Vcar)vcar_list.get(0)).s;
		
		int ac = tasknames.convert(alg);

		switch ( ac ) {
		case act_md2: {	
			code = org.apache.commons.codec.digest.DigestUtils.md2Hex(base);
		    break;
		}
		case act_md5: {	
			code = org.apache.commons.codec.digest.DigestUtils.md5Hex(base);
		    break;
		}
		case act_sha1: {
			code = org.apache.commons.codec.digest.DigestUtils.sha1Hex(base);
		    break;
		}
		case act_sha256: {
			code = org.apache.commons.codec.digest.DigestUtils.sha256Hex(base);
		    break;
		}
		case act_sha384: {	
			code = org.apache.commons.codec.digest.DigestUtils.sha384Hex(base);
			break;
		}
		case act_sha512: {	
			code = org.apache.commons.codec.digest.DigestUtils.sha512Hex(base);
		    break;
		}

		}  		
		
		Vcar r = new Vcar(code);
		r.dontImport();
		return r;
	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Function-Hash",6);
		tasknames.put("md2",act_md2 );
		tasknames.put("md5", act_md5);
		tasknames.put("sha1", act_sha1);
		tasknames.put("sha256", act_sha256);
		tasknames.put("sha384", act_sha384);
		tasknames.put("sha512", act_sha512);
		tasknames.putFinished();		
	}



}