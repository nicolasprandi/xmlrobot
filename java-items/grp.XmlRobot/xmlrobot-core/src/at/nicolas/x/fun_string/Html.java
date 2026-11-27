/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Html implements IFun {

	private static final String funn= "html";
	private static final String pm= "function 'html' ";
	
	private static Hashtable<String, String> openMap;
	private static Hashtable<String, String> closeMap;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() == 0  ) 
			throw new LangException(pm+"wants 1/2 parameters: 1=tag-name, [2=open/close] but i received "+vcar_list.size()+" Parameters");
		
		String tagname = ((Vcar)vcar_list.get(0)).s;
/*
 * 
(f,html,big,open)
(f,html,big,close)
(f,html,fs2,open)
(f,html,fs2,close)
(f,html,fs3,open)
(f,html,fs3,close)
(f,html,fs4,open)
(f,html,fs4,close)
(f,html,u,open)
(f,html,u,close)
(f,html,i,open)
(f,html,i,close)
(f,html,br)		
 */
		String ret;
		if ( tagname.equals("br"))
			ret = "[N:X]jfu4";
		else {
			if ( vcar_list.size() <2   ) 
				throw new LangException(pm+"wants 1/2 parameters: 1=tag-name, 2=open/close but i received "+vcar_list.size()+" Parameters");
			
			
			if ( (((Vcar)vcar_list.get(1)).s).startsWith("c") ) 
				// close
				ret = closeMap.get(tagname);				
			else
				ret = openMap.get(tagname);
			
			if ( ret == null )
				throw new LangException("unbekanntes html-tag '"+tagname+"' for open/close='"+
						((Vcar)vcar_list.get(1)).s+"'");
		}
				
		return (new Vcar(ret));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		openMap = new Hashtable<String, String>(10,1);
		openMap.put("bold","[N:B]oeuw" );
		openMap.put("b","[N:B]oeuw" );
		openMap.put("big", "[N:G]49k");
		openMap.put("small", "[N:S]p85f");
		openMap.put("s", "[N:S]p85f");
		openMap.put("u", "[N:U]Pu1u");	
		openMap.put("underline", "[N:U]Pu1u");
		openMap.put("italic", "[N:I]vC12R");
		openMap.put("i","[N:I]vC12R");
		openMap.put("strike","[N:R]w84t");
		
		closeMap = new Hashtable<String, String>(10,1);					
		closeMap.put("bold" ,"[F:B]94kd" );
		closeMap.put( "b","[F:B]94kd" );
		closeMap.put( "big", "[F:G]pi8d");
		closeMap.put( "small", "[F:S]p6Juz");
		closeMap.put( "s", "[F:S]p6Juz");	
		closeMap.put( "u","[F:U]Pl3C" );
		closeMap.put( "underline","[F:U]Pl3C" );
		closeMap.put( "italic", "[F:I]Pi4q");
		closeMap.put("i" , "[F:I]Pi4q");
		closeMap.put( "strike","[F:R]ieks" );

	}

}