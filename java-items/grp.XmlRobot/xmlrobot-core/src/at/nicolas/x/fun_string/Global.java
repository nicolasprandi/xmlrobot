/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Global implements IFun{

	private static HashMap<String, String> global_map = new HashMap<String, String>();
	private static final String funn = "global";
	private static final String pm = "global: ";
	
	private static final int act_set   = 0;
	private static final int act_get   = 1;
	private static final int act_exists   = 2;
	private static final int act_remove   = 3;

	private static KeyWordMapper tasknames;	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				
		if ( vcar_list.size() < 2 ) 
			throw new LangException(pm+"I want at least 2 parameters: 1=fuction(set,get,remove,exists) 2=name, but i received "+vcar_list.size()+" parameters");
		
		
		String   p_name = ((Vcar)vcar_list.get(1)).s;
		
		String retvalue="";
		
		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_set: {	
			if ( vcar_list.size() < 3 ) 
				throw new LangException(pm+"3rd parameter required: value");
			String p_value = ((Vcar)vcar_list.get(2)).s;
			
			global_map.put(p_name, Entities.importEntities(p_value));
			retvalue=new String(p_value);
			break;
		}
		case act_get: {	
			retvalue = Entities.exportEntities((String)global_map.get(p_name));			
			break;
		}
		case act_exists: {	
			Object found = global_map.get(p_name);		
			if (found==null)
				retvalue = "0";
			else
				retvalue = "1";
			break;
		}
		case act_remove: {	
			global_map.remove(p_name);
			retvalue = "1";
			break;
		}
		} 		
		

				
		return (new Vcar(retvalue));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Function-Global",4);
		tasknames.put("set", act_set);
		tasknames.put("get", act_get);
		tasknames.put("exists", act_exists);
		tasknames.put("remove", act_remove);
		tasknames.putFinished();		
	}

}