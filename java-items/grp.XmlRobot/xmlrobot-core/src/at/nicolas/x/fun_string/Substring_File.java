/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Substring_File implements IFun {

	private static final String funn = "substring_file";
	private static final String pm = "function 'substring_file' ";

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		String res;
		if ( vcar_list.size() < 2 ||  vcar_list.size() > 4 ) 
			throw new LangException(pm+"wants 2/3/4 parameters: 1=filename, 2=fromIndex, [3=endIndex, -1=end], [4=encoding], but i received "+vcar_list.size()+" Parameters");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		String filecontent = null;
		if ( vcar_list.size() == 4) {
			Vcar   d= (Vcar)vcar_list.get(3);
			filecontent = CommonsVfs.readTextfileFormatLfDefault(a.s, d.s,rh.getSitzung());
		} else	
			filecontent = CommonsVfs.readTextfileFormatLfDefault(a.s,"UTF-8",rh.getSitzung());
		
		Vcar   from= (Vcar)vcar_list.get(1);
		if ( !from.isNum ) 
			throw new Exception(pm+"says fromIndex is not numeric: "+from.s);		
		
		if ( vcar_list.size() >= 3) {
			int until = -1;
			Vcar   end= (Vcar)vcar_list.get(2);
			if ( end.s.equals("?"))
				until = -1;
			else if ( !end.isNum ) 
				throw new Exception(pm+"says endIndex is not numeric: "+end.s);	
			else 
				until = end.getInt();
			if ( until == -1 )
				res = filecontent.substring(from.getInt());
			else
				res = filecontent.substring(from.getInt(),end.getInt());
			
		} else {

			res = filecontent.substring(from.getInt());			
		}

		
		return (new Vcar(res));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}