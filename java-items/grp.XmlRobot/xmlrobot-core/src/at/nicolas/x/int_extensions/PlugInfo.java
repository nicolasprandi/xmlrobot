/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_const.Info;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LogLogger;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugInfo implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() < 1 ) 
			throw new Exception("info: I want at least one parameter: [1] variation: version|sys_jar|documents_dir");

		String r;
		
		Vcar   p= (Vcar)vcar_list.get(0);

		if ( p.s.equals("version")) {
			r = Info.version;		
		
		} else if ( p.s.equals("sys_jar")){
			Class cls = this.getClass();

			ProtectionDomain pd = cls.getProtectionDomain();
			 
			String result = pd.getCodeSource().getLocation().getPath();
			//try {
				result = URLDecoder.decode(result, "UTF-8");
				if ( CoreBuilder.ws.isWin && result.startsWith("/"))
					result = result.substring(1);
				if ( CoreBuilder.ws.isWin )
					result = Commons.stringReplace(result, "/", "\\");
				else
					result = Commons.stringReplace(result, "\\", "/");
				r= result;
			

		}  else if ( p.s.equals("documents_dir")) {
			r = CoreBuilder.ws.getUserDocDir();
			if ( r == null )
				throw new Exception("info says: no documents dir available");
			
		} else if ( p.s.equals("level")) {
			r = LogLogger.level2String(CoreBuilder.rslogger.getLevel());
			
		}  else if ( p.s.equals("jovial")) {
			r = Commons.boolToStringXr(rh.isJovial());
		}/* else if ( p.s.equals("strategy")) {
			if ( vcar_list.size() != 2 ) 
				throw new Exception(pm+"strategy needs a second parameter with the strategy-name");
			Vcar s = (Vcar)vcar_list.get(1);
			String sr = CoreBuilder.ws.getStrategy().getStrategy(s.s);
			if ( sr == null )
				r = "";
			else
				r = sr;
		} */ else
			throw new Exception("info unknown request: '"+p.s+"'");			

				
		return (new Vcar(r));
	}

	private String getDefault() {
		return Wellknown.copyright;}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}