/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;



public class File_Count_Regex implements IFun {

	
	private final static String funn = "file_count_regex";
	private final static String pm = "function 'file_count_regex' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() < 2 || vcar_list.size() > 4 ) 
			throw new LangException(353,pm+"wants 2/3 parameters: 1=File 2=Regex-To-Search [3=Encoding, Default ?=UTF-8] [4=Regex-Flags]");
		
		String filename  = ((Vcar)vcar_list.get(0)).s;
		if ( !CommonsVfs.existsFile(filename,rh.getSitzung())) 
			throw new Exception(pm+": File does not exist: '"+filename+"'");
		
		String format = "UTF-8";
		if ( vcar_list.size() >= 3 ) {
			format = ((Vcar)vcar_list.get(2)).s;
			if ( format.equals("?"))
				format = "UTF-8";
		}

		int pattern_flags = 0;
		if ( vcar_list.size() >= 4 ) {
			String f = ((Vcar)vcar_list.get(3)).s;
			pattern_flags = Commons.regexFlagsString2int(f);
			
		}
		
		String fileconent = CommonsVfs.readTextfileFormatLfDefault(filename,format,rh.getSitzung());
		
		String searchString = ((Vcar)vcar_list.get(1)).s;
		
		int occurrences = Commons.countRegexMatches(fileconent, searchString,pattern_flags);

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