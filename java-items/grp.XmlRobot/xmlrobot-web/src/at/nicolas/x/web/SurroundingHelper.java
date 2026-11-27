package at.nicolas.x.web;

import javax.servlet.ServletContext;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.SurrSelInfo;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.int_specs.TkMatcher;
import at.nicolas.x.pub.Parameters;

public class SurroundingHelper {



	public static Surrounding getSurrounding(ServletContext cx, String username) {
		Surrounding s = new Surrounding();
		
		if ( cx.getInitParameter("ext_tk_string") != null ) {
			Parameters p = new Parameters(cx.getInitParameter("ext_tk_string"),"'");
			TkMatcher tkMatcherFast = new TkMatcher("fast",p,"fast","fast_except");
			TkMatcher tkMatcherSumOnly = new TkMatcher("sum_only",p,"sum_only","sum_only_except");
			//TkMatcher tkMatcherOfferFast = new TkMatcher("offer fast",p,"offerfast","offerfast_except","all");
			//TkMatcher tkMatcherOfferSequ = new TkMatcher("offer seq",p,"offersequ","offersequ_except","all");
			
			//
			s.setTkMatcherFast(tkMatcherFast);
			s.setTkMatcherSumOnly(tkMatcherSumOnly);
			/* s.setTkMatcherOfferFast(tkMatcherOfferFast);
			s.setTkMatcherOfferSequ(tkMatcherOfferSequ); */
		
			
			/*
			if ( p.getParameter("max_text_len") != null ) {
				Integer i1 = new Integer(p.getParameter("max_text_len"));
				s.setMaxTkTextLen(i1);
			}
			
			if ( p.getParameter("max_textblock_len") != null ) {
				Integer i1 = new Integer(p.getParameter("max_textblock_len"));
				s.setMaxTkTextblockLen(i1);
			}	
			*/		

		}
		if ( cx.getInitParameter("firstjob")!=null && cx.getInitParameter("firstjob").length() > 0) {
			s.setFirstJob(cx.getInitParameter("firstjob"));			
		}

		if ( cx.getInitParameter("project_dir")!=null && cx.getInitParameter("project_dir").length() > 0) {
			boolean cloneIt = true;
			String paraClone = cx.getInitParameter("clone_project_dir_on");
			if ( paraClone == null || paraClone.length() == 0 ) {
				cloneIt = true;
			} else {
				cloneIt = Commons.stringxr2bool(paraClone, true);
			}
			

			s.setProjectDir(
					cx.getInitParameter("project_dir"), 
					cloneIt);
			
		}
		


		
		if ( cx.getInitParameter("header")!=null && cx.getInitParameter("header").length() > 0) {
			s.setHeader(cx.getInitParameter("header"), cx.getInitParameter("header_job"));		
		}
		if ( cx.getInitParameter("footer")!=null && cx.getInitParameter("footer").length() > 0) {
			s.setFooter(cx.getInitParameter("footer"), cx.getInitParameter("footer_job"));		
		}		
		

		if ( cx.getInitParameter("css")!=null && cx.getInitParameter("css").length() > 0) {
			s.setCss(cx.getInitParameter("css"));			
		}
		
		if ( cx.getInitParameter("public")!=null && cx.getInitParameter("public").length() > 0) {
			s.setPub(cx.getInitParameter("public"));			
		}		

		
		/*
		if ( cx.getInitParameter("trace_on")!=null && cx.getInitParameter("trace_on").length() > 0) {
			s.setTrace( Commons.stringxr2bool(cx.getInitParameter("trace_on"),false));			
		}
		

		if ( cx.getInitParameter("role")!=null && cx.getInitParameter("role").length() > 0) {
		
			s.setRole(cx.getInitParameter("role"));		
		}
		*/
		//
		if ( cx.getInitParameter("bits")!=null ) {
			s.bits = cx.getInitParameter("bits");
			
			s.bitsHasSelWriteFile = Commons.hasFlag(s.bits, "hasSelFileWrite");
			s.bitsHasSelWriteDir = Commons.hasFlag(s.bits, "hasSelDirWrite");
			s.bitsHasSelReadFile = Commons.hasFlag(s.bits, "hasSelFileRead");
			s.bitsHasSelReadDir = Commons.hasFlag(s.bits, "hasSelDirRead");	
			s.bitsPanoramaChoose = Commons.hasFlag(s.bits, "panoramaChoose");	
				
		}		
		
		SurrSelInfo surSelInfoWriteFile;
		if ( s.bitsHasSelWriteFile ) 
			surSelInfoWriteFile = new SurrSelInfo(
										cx.getInitParameter("uri_wf_bits"),
										null,
										cx.getInitParameter("uri_wf_prov_pro"));			
		else
			surSelInfoWriteFile = new SurrSelInfo();
		s.selInfoWriteFile = surSelInfoWriteFile;
		
		SurrSelInfo surSelInfoReadFile;
		if ( s.bitsHasSelReadFile ) 
			surSelInfoReadFile = new SurrSelInfo(
										cx.getInitParameter("uri_rf_bits"),
										cx.getInitParameter("uri_rf_prov_app"),
										cx.getInitParameter("uri_rf_prov_pro"));			
		else
			surSelInfoReadFile = new SurrSelInfo();
		s.selInfoReadFile = surSelInfoReadFile;		

		//
		SurrSelInfo surSelInfoWriteDir;
		if ( s.bitsHasSelWriteDir ) 
			surSelInfoWriteDir = new SurrSelInfo(
										cx.getInitParameter("uri_wd_bits"),
										null,
										cx.getInitParameter("uri_wd_prov_pro"));			
		else
			surSelInfoWriteDir = new SurrSelInfo();
		s.selInfoWriteDir = surSelInfoWriteDir;
		
		SurrSelInfo surSelInfoReadDir;
		if ( s.bitsHasSelReadDir ) 
			surSelInfoReadDir = new SurrSelInfo(
										cx.getInitParameter("uri_rd_bits"),
										cx.getInitParameter("uri_rd_prov_app"),
										cx.getInitParameter("uri_rd_prov_pro"));			
		else
			surSelInfoReadDir = new SurrSelInfo();
		s.selInfoReadDir = surSelInfoReadDir;			


		if ( cx.getInitParameter("ext_ons")!=null && cx.getInitParameter("ext_ons").length() > 0) {
			s.setExtOn(cx.getInitParameter("ext_ons"));		
		}	
		
		Sp.p("ext_ebits="+cx.getInitParameter("ext_ebits"));
		if ( cx.getInitParameter("ext_ebits")!=null && cx.getInitParameter("ext_ebits").length() > 0) {
			s.setExtBits(cx.getInitParameter("ext_ebits"));		
		}	
		
		if ( cx.getInitParameter("ext_pro_check_string")!=null && cx.getInitParameter("ext_pro_check_string").length() > 0) {
			s.setExtProDbConfString(cx.getInitParameter("ext_pro_check_string"));			
		}			

		if ( cx.getInitParameter("ext_guiprivs")!=null && cx.getInitParameter("ext_guiprivs").length() > 0) {
			s.setExtGuiPrivs(cx.getInitParameter("ext_guiprivs"));			
		}		
		return s;

	}

}
