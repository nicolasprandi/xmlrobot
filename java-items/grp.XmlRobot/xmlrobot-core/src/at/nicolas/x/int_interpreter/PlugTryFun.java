/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jdom2.Element;

import at.nicolas.webwalk.WebDriverFuncs;
import at.nicolas.webwalk.WebElementFuncs;
import at.nicolas.x.fun_bool.And;
import at.nicolas.x.fun_bool.Contains;
import at.nicolas.x.fun_bool.Dir_Exists;
import at.nicolas.x.fun_bool.Ends_With;
import at.nicolas.x.fun_bool.Eq;
import at.nicolas.x.fun_bool.EqNum;
import at.nicolas.x.fun_bool.EqText;
import at.nicolas.x.fun_bool.Exists;
import at.nicolas.x.fun_bool.File_Contains;
import at.nicolas.x.fun_bool.File_Contains_Regex;
import at.nicolas.x.fun_bool.File_Exists;
import at.nicolas.x.fun_bool.Gt;
import at.nicolas.x.fun_bool.GtNum;
import at.nicolas.x.fun_bool.GtText;
import at.nicolas.x.fun_bool.GteNum;
import at.nicolas.x.fun_bool.Has;
import at.nicolas.x.fun_bool.Has_Flag;
import at.nicolas.x.fun_bool.Implement;
import at.nicolas.x.fun_bool.Is_Bool;
import at.nicolas.x.fun_bool.Is_Num;
import at.nicolas.x.fun_bool.Is_Xml;
import at.nicolas.x.fun_bool.Lt;
import at.nicolas.x.fun_bool.LtNum;
import at.nicolas.x.fun_bool.LtText;
import at.nicolas.x.fun_bool.LteNum;
import at.nicolas.x.fun_bool.Matches_Regex;
import at.nicolas.x.fun_bool.Not;
import at.nicolas.x.fun_bool.Or;
import at.nicolas.x.fun_bool.Right;
import at.nicolas.x.fun_bool.Starts_With;
import at.nicolas.x.fun_misc.Matematico;
import at.nicolas.x.fun_num.Abs;
import at.nicolas.x.fun_num.Add;
import at.nicolas.x.fun_num.Ceil;
import at.nicolas.x.fun_num.Count_Regex;
import at.nicolas.x.fun_num.Count_String;
import at.nicolas.x.fun_num.Div;
import at.nicolas.x.fun_num.Dummy;
import at.nicolas.x.fun_num.File_Count_Regex;
import at.nicolas.x.fun_num.File_Count_String;
import at.nicolas.x.fun_num.Floor;
import at.nicolas.x.fun_num.IndexOf;
import at.nicolas.x.fun_num.LastIndexOf;
import at.nicolas.x.fun_num.Length;
import at.nicolas.x.fun_num.Max;
import at.nicolas.x.fun_num.Min;
import at.nicolas.x.fun_num.Modulo;
import at.nicolas.x.fun_num.Mul;
import at.nicolas.x.fun_num.NbrBaseConvert;
import at.nicolas.x.fun_num.Parallel;
import at.nicolas.x.fun_num.Pow;
import at.nicolas.x.fun_num.Random2;
import at.nicolas.x.fun_num.Remain;
import at.nicolas.x.fun_num.Round;
import at.nicolas.x.fun_num.Sub;
import at.nicolas.x.fun_string.Alltime;
import at.nicolas.x.fun_string.Buffer;
import at.nicolas.x.fun_string.Canonical_Dir;
import at.nicolas.x.fun_string.Canonical_File;
import at.nicolas.x.fun_string.Cut;
import at.nicolas.x.fun_string.Cut_Nice;
import at.nicolas.x.fun_string.Dir_Info;
import at.nicolas.x.fun_string.Dir_Of_Dir;
import at.nicolas.x.fun_string.Dir_Of_File;
import at.nicolas.x.fun_string.E;
import at.nicolas.x.fun_string.Echo;
import at.nicolas.x.fun_string.Escape;
import at.nicolas.x.fun_string.Ex;
import at.nicolas.x.fun_string.File_Info;
import at.nicolas.x.fun_string.Filecontent;
import at.nicolas.x.fun_string.Format;
import at.nicolas.x.fun_string.Global;
import at.nicolas.x.fun_string.Good_Filename;
import at.nicolas.x.fun_string.Guicontroller;
import at.nicolas.x.fun_string.Hash;
import at.nicolas.x.fun_string.Html;
import at.nicolas.x.fun_string.Known_Format;
import at.nicolas.x.fun_string.Locale_Prop;
import at.nicolas.x.fun_string.Lower;
import at.nicolas.x.fun_string.Name_Of_Dir;
import at.nicolas.x.fun_string.Name_Of_File;
import at.nicolas.x.fun_string.Name_Of_File2;
import at.nicolas.x.fun_string.Path_Of_Dir;
import at.nicolas.x.fun_string.Plural;
import at.nicolas.x.fun_string.Random_Text;
import at.nicolas.x.fun_string.Regex_Text;
import at.nicolas.x.fun_string.Relative_Dir2;
import at.nicolas.x.fun_string.Relative_Dir3;
import at.nicolas.x.fun_string.Remove;
import at.nicolas.x.fun_string.Replace;
import at.nicolas.x.fun_string.Replace_Regex;
import at.nicolas.x.fun_string.Replace_Regex_Literal;
import at.nicolas.x.fun_string.Request;
import at.nicolas.x.fun_string.Secret;
import at.nicolas.x.fun_string.Sequence;
import at.nicolas.x.fun_string.Sissi;
import at.nicolas.x.fun_string.Stack;
import at.nicolas.x.fun_string.Substring;
import at.nicolas.x.fun_string.Substring_After;
import at.nicolas.x.fun_string.Substring_Before;
import at.nicolas.x.fun_string.Substring_File;
import at.nicolas.x.fun_string.Sysprop;
import at.nicolas.x.fun_string.TextFun;
import at.nicolas.x.fun_string.Time;
import at.nicolas.x.fun_string.TimeFun;
import at.nicolas.x.fun_string.Tk_Sel_Translate;
import at.nicolas.x.fun_string.Translate;
import at.nicolas.x.fun_string.Trim;
import at.nicolas.x.fun_string.Try;
import at.nicolas.x.fun_string.Unescape;
import at.nicolas.x.fun_string.Upper;
import at.nicolas.x.fun_string.Vfs;
import at.nicolas.x.fun_string.Vob;
import at.nicolas.x.fun_string.When;
import at.nicolas.x.fun_string.X;
import at.nicolas.x.fun_string.Xpath;
import at.nicolas.x.fun_string.Xpath2;
import at.nicolas.x.fun_string.Xpath2String;
import at.nicolas.x.fun_string.Xquery;
import at.nicolas.x.fun_string.Xslp;
import at.nicolas.x.int_coop.CoopLr;
import at.nicolas.x.int_coop.CoopLw;
import at.nicolas.x.int_coop.CoopLx;
import at.nicolas.x.int_coop.CoopSpiritFunction;
import at.nicolas.x.int_extensions.PlugFunAsk;
import at.nicolas.x.int_extensions.PlugInfo;
import at.nicolas.x.int_extensions.PlugInterpreter;
import at.nicolas.x.int_extensions.PlugRole;
import at.nicolas.x.int_extensions.PlugXsl;
import at.nicolas.x.int_specs.DummyRequestHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.tasks.UniqueFunction;



public class PlugTryFun {
	
	String fn;
	ArrayList<Vcar> vcar_list;
	Interpreter interpreter;

	HashMap<String, PlugFundef>  fundefMap;
	
	static HashMap<String,Class<?>> classMap;
	static boolean salamander_mode_is_production = false;
	public static void startupActions( boolean report ) throws Exception {
		classMap = new HashMap<String, Class<?>>();
		Element dummyElement = new Element("dummy");
		IRequestHandler dummyRequestHandler = new DummyRequestHandler();

		
		// get: proxy for expressions
		classMap.put("right",Right.class);
		classMap.put("implement",Implement.class);
		classMap.put("sissi",Sissi.class);
		classMap.put("random_text",Random_Text.class);
		classMap.put("request",Request.class);
		classMap.put("guicontroller",Guicontroller.class);
		classMap.put("vfs",Vfs.class);
		classMap.put("uri",Vfs.class);
		classMap.put("remove",Remove.class);
		classMap.put("get",Dummy.class);
		classMap.put("getx",Dummy.class);
		classMap.put("xml_get",Dummy.class);
		classMap.put("testdata",Dummy.class);
		classMap.put("index",Dummy.class);
		classMap.put("tp",Dummy.class);
		classMap.put("tk",Dummy.class);
		classMap.put("ti",Dummy.class);
		classMap.put("alt",Dummy.class);
		classMap.put("base",Dummy.class);
		classMap.put("like",Dummy.class);
		classMap.put("terminal",Dummy.class);
		classMap.put("interpreter",PlugInterpreter.class);
		classMap.put("ask",PlugFunAsk.class);
		classMap.put("echo",Echo.class);
		classMap.put("net",Dummy.class);
		classMap.put("varius",Dummy.class);
		classMap.put("priv",Dummy.class);
		classMap.put("dir",Dir_Info.class);
		classMap.put("file_info",File_Info.class);	
		classMap.put("file",File_Info.class);
		classMap.put("text",TextFun.class);
		// classMap.put("format_known",Dummy.class);
		
		// classMap.put("e",Dummy.class);
		classMap.put("e",E.class);
		// classMap.put("export",E.class);
		classMap.put("xe",Ex.class);
		classMap.put("xescape",Ex.class);
		//classMap.put("xf",Ex.class);
		classMap.put("x",X.class);
		// classMap.put("h",H.class);
		classMap.put("eq",Eq.class);
		classMap.put("equals",Eq.class);
		classMap.put("not",Not.class);
		classMap.put("and",And.class);
		classMap.put("or",Or.class);
		classMap.put("and2",Dummy.class);
		classMap.put("or2",Dummy.class);
		classMap.put("spirit",CoopSpiritFunction.class);
		classMap.put("contains",Contains.class);
		classMap.put("contains_regex",Dummy.class);
		classMap.put("gt",Gt.class);
		classMap.put("gt_num",GtNum.class);		
		classMap.put("gt_text",GtText.class);
						
		classMap.put("lt",Lt.class);
		classMap.put("lt_num",LtNum.class);		
		classMap.put("lt_text",LtText.class);
		
		classMap.put("gte_num",GteNum.class);
		classMap.put("lte_num",LteNum.class);
		
		classMap.put("loop_xml",CoopLx.class);
		classMap.put("loop_regex",CoopLr.class);
		classMap.put("loop_while",CoopLw.class);
		classMap.put("loop",Dummy.class);
		classMap.put("sequence",Sequence.class);
		classMap.put("loop_until",Dummy.class);
		classMap.put("len",Length.class);
		classMap.put("index_of",IndexOf.class);
		classMap.put("has_flag",Has_Flag.class);
		classMap.put("xpath",Xpath.class);
		classMap.put("xpathx",Xpath2.class);
		// classMap.put("not_eq",Not_Eq.class);
		classMap.put("when",When.class);
		classMap.put("exists",Exists.class);
		classMap.put("has",Has.class);
		classMap.put("trim",Trim.class);
		// classMap.put("jar",PlugJar.class);
		// classMap.put("loglevel",PlugLoglevel.class);
		// classMap.put("strategy",PlugStrategy.class);
		classMap.put("dir_exists",Dir_Exists.class);
		classMap.put("exists_dir",Dir_Exists.class);
		classMap.put("file_contains_regex",File_Contains_Regex.class);
		classMap.put("file_contains",File_Contains.class);
		classMap.put("file_exists",File_Exists.class);
		classMap.put("exists_file",File_Exists.class);
		// classMap.put("count_flag_in_flag",Count_Flag_In_Flag.class);
		classMap.put("is_num",Is_Num.class);
		classMap.put("is_xml",Is_Xml.class);
		classMap.put("is_bool",Is_Bool.class);
		classMap.put("matches_regex",Matches_Regex.class);
		// classMap.put("every",Dummy.class);
		// classMap.put("every_second",Every_Second.class);
		// classMap.put("akeys",Akeys.class);
		// classMap.put("exists_job",PlugExists_Job.class);
		classMap.put("starts_with",Starts_With.class);
		classMap.put("ends_with",Ends_With.class);
		classMap.put("role",PlugRole.class);
		// classMap.put("documents_dir",PlugDocumentsDir.class);
		// classMap.put("concat",Concat.class);	
		// classMap.put("decode_html_specials",Decode_Html_Specials.class);
		// classMap.put("text_to_html",Text_To_Html.class);
		classMap.put("dir_of_file",Dir_Of_File.class);
		classMap.put("format",Format.class);
		classMap.put("known_format",Known_Format.class);
		classMap.put("dir_of_dir",Dir_Of_Dir.class);
		classMap.put("name_of_dir",Name_Of_Dir.class);
		// classMap.put("encode_html_specials",Encode_Html_Specials.class);	
		// classMap.put("xml",Cdata_Xml.class);
		// classMap.put("xml_back",Cdata_Xml_Back.class);
		classMap.put("name_of_file",Name_Of_File.class);
		classMap.put("sys_prop",Sysprop.class);
		classMap.put("locale_prop",Locale_Prop.class);
		classMap.put("name_of_file2",Name_Of_File2.class);	
		// classMap.put("filecontent",Filecontent.class);
		classMap.put("file_content",Filecontent.class);
		classMap.put("tk_sel_translate",Tk_Sel_Translate.class);	
		classMap.put("lower",Lower.class);
		classMap.put("vob",Vob.class);	
		classMap.put("replace",Replace.class);
		classMap.put("replace_regex",Replace_Regex.class);	
		classMap.put("replace_regex_literal",Replace_Regex_Literal.class);
		classMap.put("text_to_regex",Regex_Text.class);
		// classMap.put("fill",Fill.class);	
		classMap.put("round",Round.class);
		// classMap.put("round2",Dummy.class);	
		classMap.put("var",Dummy.class);
		classMap.put("scope",Dummy.class);
		// classMap.put("bp",Bp.class);	
		classMap.put("substring_after",Substring_After.class);
		classMap.put("substring_before",Substring_Before.class);	
		classMap.put("substring",Substring.class);	
		classMap.put("substring_file",Substring_File.class);
		classMap.put("upper",Upper.class);	
		classMap.put("xsl",PlugXsl.class);
		// classMap.put("loc",Loc.class);
		// classMap.put("pro_dir",PlugPro_Dir.class);
		// classMap.put("app_dir",PlugApp_Dir.class);
		// classMap.put("last_mod",Last_Mod.class);
		classMap.put("info",PlugInfo.class);	
		classMap.put("hash",Hash.class);
		classMap.put("relative_dir",Relative_Dir3.class);
		classMap.put("relative_dir2",Relative_Dir2.class);
		// classMap.put("canonical_dir",Canonical_Dir.class);
		classMap.put("can_dir",Canonical_Dir.class);	
		classMap.put("can_file",Canonical_File.class);
		classMap.put("path_of_dir",Path_Of_Dir.class);	
		classMap.put("plural",Plural.class);	
		classMap.put("add",Add.class);
		classMap.put("pow",Pow.class);
		classMap.put("div",Div.class);	
		// classMap.put("len_file",Len_File.class);	
		classMap.put("mul",Mul.class);
		classMap.put("sub",Sub.class);
		classMap.put("nbr_base_convert", NbrBaseConvert.class);
		// classMap.put("bin",Bin.class);	
		// classMap.put("file_count_string",File_Count_String.class);
		classMap.put("file_count_text",File_Count_String.class);
		// classMap.put("count_string",Count_String.class);
		classMap.put("count_text",Count_String.class);
		classMap.put("file_count_regex",File_Count_Regex.class);	
		classMap.put("count_regex",Count_Regex.class);
		classMap.put("last_index_of",LastIndexOf.class);
		classMap.put("remain",Remain.class);
		classMap.put("mod",Modulo.class);
		classMap.put("random",Random2.class);
		classMap.put("min",Min.class);
		classMap.put("max",Max.class);
		// classMap.put("lib",PlugLib.class);		
		classMap.put("floor",Floor.class);
		classMap.put("ceil",Ceil.class);
		// classMap.put("size",Size.class);
		classMap.put("good_filename",Good_Filename.class);
		classMap.put("html",Html.class);
		classMap.put("unique",UniqueFunction.class);
		classMap.put("xpath_string",Xpath2String.class);
		classMap.put("parallel",Parallel.class);
		classMap.put("cut_nice",Cut_Nice.class);
		classMap.put("cut",Cut.class);
		classMap.put("buffer",Buffer.class);
		classMap.put("secret",Secret.class);
		classMap.put("stack",Stack.class);	
		classMap.put("abs",Abs.class);	
		classMap.put("xor",Dummy.class);
		classMap.put("xquery",Xquery.class);
		classMap.put("timef",TimeFun.class);
		classMap.put("time",Time.class);
		classMap.put("escape",Escape.class);
		classMap.put("unescape",Unescape.class);
		classMap.put("alltime",Alltime.class);
		classMap.put("code",Dummy.class);
		classMap.put("math",Matematico.class);
		classMap.put("verify",Dummy.class);
		classMap.put("xslp",Xslp.class);

		classMap.put("translate",Translate.class);
		classMap.put("try",Try.class);
		
		classMap.put("eq_text",EqText.class);			
		classMap.put("eq_num",EqNum.class);
				
		classMap.put("case",Dummy.class);
		classMap.put("case_text",Dummy.class);
		classMap.put("case_num",Dummy.class);	
		
		classMap.put("rule",Dummy.class);
		classMap.put("key",Dummy.class);
		
		classMap.put("translate2",Dummy.class);
		classMap.put("expression",Dummy.class);
		classMap.put("red",Dummy.class);
		classMap.put("blue",Dummy.class);
		classMap.put("green",Dummy.class);
		classMap.put("pink",Dummy.class);
		classMap.put("wdriver",WebDriverFuncs.class);
		classMap.put("welement",WebElementFuncs.class);		
		(new CoopLx()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Cut()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Cut_Nice()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new TextFun()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Unescape()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Vfs()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Html()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Hash()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Global()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Dir_Info()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);	
		(new File_Info()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);
		(new Matematico()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);		
		(new Round()).staticFundef(
				dummyElement, "?4?", dummyRequestHandler);	
			
		if ( report ) {
			Sp.p("");
			Sp.p("-) Registered builtin functions:");
			SortedSet<String> keys = new TreeSet<String>(classMap.keySet());
			for (String key : keys) { 
				System.out.print(key);
			}
			Sp.p(""); 
		}
		
	}	
	public PlugTryFun ( 	String fn, 
						ArrayList<Vcar> _vcar_list,
						Interpreter interpreter,
						HashMap<String, PlugFundef>  fundefMap) throws LangException {
		this.fn = fn;
		
		// this.vcar_list = vcar_list;
		
		// Export begin
		Iterator<Vcar> i = _vcar_list.iterator();
		vcar_list = new ArrayList<Vcar> ();
		while ( i.hasNext() ) {
			Vcar akt = (Vcar)i.next();
			String current = akt.s;
		
			String check = Entities.isValidExpression(current);
			
			if ( check != null )
				throw new LangException(209,"Wrong! function para: "+check);
				
			current = Entities.exportEntities(current);
			Vcar next = new Vcar(current);
			next.calculate();
			vcar_list.add(next);
		}
		// Export end
		
		this.interpreter = interpreter;
		this.fundefMap = fundefMap;

		
	}
	
	public PlugTryFunResult run() throws Exception {

			
			IFun fun = null;
			
			Class<?> regClass = (Class<?>) classMap.get(fn);
			
			if ( regClass != null ) {
				// found it!!
				Constructor<?> cons = regClass.getConstructor();
				fun = (IFun)cons.newInstance();
			} else if (fn.startsWith("sys_")) {
				throw new Exception("Functions 'sys_*' are reserved for Interpreter-Core");
			} else {
				// custom function ?
				PlugFundef oFundef = (PlugFundef)fundefMap.get(fn);
				if ( oFundef != null ) {
					fun = (IFun)oFundef.getObject();
				} 	
			}
			
			if ( fun != null ) {
				
							
				Vcar vcar = fun.execute(vcar_list, interpreter);
				

				Vcar inhalt_imported;
				
				if ( vcar.shouldBeImporte()) {
				
					inhalt_imported = new Vcar(Entities.importEntities(vcar.s));
				} else {
			
					inhalt_imported = new Vcar(vcar.s);
				}
				
				inhalt_imported.calculate();
				
				return ( new PlugTryFunResult(true,inhalt_imported) );
				
			} else {
				return ( new PlugTryFunResult(false,null) );
			} 		 
						
		
		

	}

}