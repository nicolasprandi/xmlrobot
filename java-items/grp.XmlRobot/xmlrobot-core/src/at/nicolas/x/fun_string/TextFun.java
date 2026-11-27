/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.term_tk.FitsRule;

public class TextFun implements IFun {

	private static final String funn="text";
	private static final String pm ="function 'text' ";
	private static final int act_fill_left   = 0;
	private static final int act_fill_right   = 1;
	private static final int act_cut   = 2;
	private static final int act_cut_nice   = 3;
	private static final int act_filename   = 4;
	private static final int act_filename_with_blanks   = 5;

	private static KeyWordMapper tasknames;	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(pm+"wants at least 1 parameter: [1] fill_left|fill_right|cut|cut_nice ");
		

		
		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_fill_left: {	
			if ( vcar_list.size() != 4 ) 
				throw new LangException(pm+" variation 'fill_left' wants 4 parameters: [1] 'fill_left' [2] text [3] len [4] fill character");
			String   result = ((Vcar)vcar_list.get(1)).s;
			int   	 len = ((Vcar)vcar_list.get(2)).getInt();
			String   cha = ((Vcar)vcar_list.get(3)).s;
			
			if ( cha.length() != 1 )
				throw new LangException(pm+" variation 'fill_left' wants just ONE filling-character, but found: "+cha);				

			if ( result.length() < len ) {
				String add = Commons.StringTimes(cha, len-result.length());
				result = add+result;
			}
			ret = new Vcar(result);
			break;
		}
		case act_fill_right: {	
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
			break;
		}
		case act_cut: {	
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
			break;
		}
		case act_cut_nice: {	
			if ( vcar_list.size() != 3 ) 
				throw new LangException(pm+" variation 'cut_nice' wants 3 parameters: [1] 'cut_nice' [2] text [3] len");
			String   value = ((Vcar)vcar_list.get(1)).s;
			int   	 len = ((Vcar)vcar_list.get(2)).getInt();
			if ( len < 4 )
				throw new LangException(pm+" variation 'cut_nice' wants at least a length of 4, but found "+((Vcar)vcar_list.get(2)).s);

			String cutted = Commons.cutNice(value,len);
			ret = new Vcar(cutted);
			break;
		}
		case act_filename: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(pm+" variation 'filename' wants 3 parameters: [1] 'filename' [2] text");
			String   value = ((Vcar)vcar_list.get(1)).s;


			String cutted = FitsRule.applyRule(value,false);
			ret = new Vcar(cutted);
			break;
		}
		case act_filename_with_blanks: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(pm+" variation 'filename_with_blanks' wants 3 parameters: [1] 'filename' [2] text");
			String   value = ((Vcar)vcar_list.get(1)).s;


			String cutted = FitsRule.applyRule(value,true);
			ret = new Vcar(cutted);
			break;
		}

		} 		
		
		
							
		return (ret);
	}

	public String getExtensionId() {
		return "text";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("text-function",6);
		tasknames.put("fill_left", act_fill_left);
		tasknames.put("fill_right", act_fill_right);
		tasknames.put("cut", act_cut);
		tasknames.put("cut_nice", act_cut_nice);
		tasknames.put("filename", act_filename);
		tasknames.put("filename_with_blanks", act_filename_with_blanks);
		tasknames.putFinished();		
	}

}