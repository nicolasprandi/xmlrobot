/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class TimeFun implements IFun {

	private static final String funn="timef";
	private static final String pm ="function 'timef' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(pm+"wants at least 1 parameter: [1] get_time ");
		
		String  variation= ((Vcar)vcar_list.get(0)).s;
		
		if ( variation.equals("get_time")) {
			if ( vcar_list.size() != 3 ) 
				throw new LangException(pm+" variation 'get_time' wants 3 parameters: [1] 'get_time' [2] time-in-format [3] format");
			String   time = ((Vcar)vcar_list.get(1)).s;
			String   format = ((Vcar)vcar_list.get(2)).s;
			long res_time = new java.text.SimpleDateFormat(format).parse(time).getTime();

			ret = new Vcar(new BigDecimal(res_time));
						
		}/* else if ( variation.equals("fill_right")) {
			if ( vcar_list.size() != 4 ) 
				throw new LangException(pm+" variation 'fill_left' wants 4 parameters: [1] 'fill_left' [2] text [3] len [4] fill character");
			String   result = ((Vcar)vcar_list.get(1)).s;
			int   	 len = ((Vcar)vcar_list.get(2)).getInt();
			String   cha = ((Vcar)vcar_list.get(3)).s;
			
			if ( cha.length() != 1 )
				throw new LangException(pm+" variation 'fill_right' wants just ONE filling-character, but found: "+cha);				

			if ( result.length() < len ) {
				String add = Commons.StringTimes(cha, len-result.length());
				result = result + add;
			}
			ret = new Vcar(result);
						
		} else if ( variation.equals("cut")) {
			if ( vcar_list.size() != 3 ) 
				throw new LangException(pm+" variation 'cut' wants 3 parameters: [1] 'cut' [2] text [3] len");
			String   value = ((Vcar)vcar_list.get(1)).s;
			int   	 len = ((Vcar)vcar_list.get(2)).getInt();
			
			String cutted;
			if ( len < value.length())
				cutted = value.substring(0,len);
			else
				cutted = value;

			ret = new Vcar(cutted);
						
		} else if ( variation.equals("cut_nice")) {
			if ( vcar_list.size() != 3 ) 
				throw new LangException(pm+" variation 'cut_nice' wants 3 parameters: [1] 'cut_nice' [2] text [3] len");
			String   value = ((Vcar)vcar_list.get(1)).s;
			int   	 len = ((Vcar)vcar_list.get(2)).getInt();
			if ( len < 4 )
				throw new LangException(pm+" variation 'cut_nice' wants at least a length of 4, but found "+((Vcar)vcar_list.get(2)).s);

			String cutted = Commons.cutNice(value,len);
			ret = new Vcar(cutted);
						
		}*/ else {
			throw new LangException(pm+" has unknown variation '"+variation+"'");
		}
							
		return (ret);
	}

	public String getExtensionId() {
		return "timef";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}