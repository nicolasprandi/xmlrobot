/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Cut implements IFun{

	private static final String funn = "cut";
	private static final String pm = "cut: ";
	private static final int act_nice   = 0;
	private static final int act_hard   = 1;

	private static KeyWordMapper tasknames;	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() != 3 ) throw new LangException(pm+"I wants 3 parameters: 1=type [nice,hard,mid] 2=string, 3=length");
		


		String value = ((Vcar)vcar_list.get(1)).s;
		int len = ((Vcar)vcar_list.get(2)).getInt();
		String result = null;
		
		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_nice: {	
			result = Commons.cutNice(value,len);
			break;
		}
		case act_hard: {	
			int p = len;
			if ( len > value.length())
				p = value.length();
			result = value.substring(0,p);
			break;
		}

		} 

		return (new Vcar(result));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		tasknames = new KeyWordMapper("fun-cut-type",2);
		tasknames.put("nice", act_nice);
		tasknames.put("hard", act_hard);
		tasknames.putFinished();
		
	}
	



}