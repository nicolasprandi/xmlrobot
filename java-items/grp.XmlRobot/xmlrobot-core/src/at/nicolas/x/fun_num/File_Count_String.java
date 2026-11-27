/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class File_Count_String implements IFun {

	
	private final static String funn= "file_count_string";
	private final static String pm= "function 'file_count_string' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
						
		if ( vcar_list.size() < 2 || vcar_list.size() > 3 ) 
			throw new LangException(354,pm+"wants 2/3 parameters: 1=File 2=Text-To-Search [3=Encoding Default UTF-8]");
		
		String filename  = ((Vcar)vcar_list.get(0)).s;
		if ( !CommonsVfs.existsFile(filename,rh.getSitzung())) 
			throw new Exception(pm+"File does not exist: '"+filename+"'");
		
		String fileconent = null;
		if ( vcar_list.size() == 2 )
			fileconent = CommonsVfs.readTextfileFormatLfDefault(filename,"UTF-8",rh.getSitzung());
		else {
			fileconent = CommonsVfs.readTextfileFormatLfDefault(filename, ((Vcar)vcar_list.get(2)).s,rh.getSitzung());
		}
		
		String searchString = ((Vcar)vcar_list.get(1)).s;
		int searchStringLen = searchString.length();
		
		int occurrences = 0;
		int index = 0;
		while (index < fileconent.length() && (index = fileconent.indexOf(searchString, index)) >= 0) {
		    occurrences++;
		    index = index + searchStringLen;
		}		
		
		Vcar r = new Vcar(occurrences);
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
		
	}

}