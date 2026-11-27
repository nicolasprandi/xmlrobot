/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Scan  implements ITask{

	String dir;
	String out;
	String home;
	String fef;
	boolean fef_bool;
	String fef_not;
	boolean do_lm = false;
	boolean fef_not_bool;	
	String extensions[];
	String extensions_not[];
	int    level_limit = -1;

	boolean report_files = false;
	boolean report_files_tested = false;
	boolean report_level = false;
	boolean report_attributes = false;
	int max_level = 0;
	
	int cur_id = 0;
	int living = 100;
	String tname="scan";
	boolean no_empty_dirs = false;
	
	boolean ignore_files = false;

	private String getNextId () {
		cur_id++;
		return ( Integer.toString(cur_id) );
	}
	int current_living = 0;
	int user_living = 0;
	
	boolean no_gfn = true;
	boolean no_pre = false;
	boolean no_post = false;
	boolean no_post2 = false;
	boolean no_fn = false;
	boolean flag_hidden = false;
	int min_bytes = -1;
	int max_bytes = -1;
	boolean do_md5 = false;
	boolean do_md2 = false;
	boolean app_no_pro = false;
	boolean do_sha1 = false;
	boolean do_sha256 = false;
	boolean do_sha384 = false;
	boolean do_sha512 = false;
	
	
	boolean do_ipath = false;
	boolean do_ext = false;
	boolean do_ext2 = false;
	String do_ext2_ext = null;
	boolean do_more = false;
	
	Pattern file_regex_pattern = null;
	Pattern dir_regex_pattern = null;
	int count_dirs = 0;
	int until_now_aggregated = 0;
	
	String label_md2 = "md2";
	String label_md5 = "md5";
	String label_sha1 = "sha1";
	String label_sha256 = "sha256";
	String label_sha384 = "sha384";
	String label_sha512 = "sha512";
	
	String says = "task 'scan' says: ";

	
	
	private void siganlLiving() throws Exception {
		current_living++;
		if ( (current_living%living) == 0 ) {
			user_living++;
			// lc.log_ext("Still working "+user_living,this);
			//rh.pleaseRememberMe(this);
			//rh.terminalProgress("Scan (#"+Integer.toString(cur_id)+") "+dir,false, this);
		}		
	}
	private IRequestHandler rh;
	public Parameters execute(Element e, IRequestHandler _rh) throws Exception {

		rh = _rh;
		
		ScanElement.itask = this;
		
		dir = e.getAttributeValue("dir");
		System.out.println("scan dir = "+dir);
		out = e.getAttributeValue("out");
		if ( dir==null)
			throw new Exception(says+"Define a paramater 'dir' with the directory to scan");
		if ( out==null)
			throw new Exception(says+"Define a paramater 'out' with the output-xml-file which should be created");
		Parameters retParas = new Parameters();
		no_empty_dirs = Commons.hasFlag(e, "kill_empty") || Commons.hasFlag(e, "no_empty_dirs");
		/* file_extension, file_extension_not */
		
		fef = e.getAttributeValue("file_extension");		
		if ( fef != null && fef.length() > 0 ) {
			fef_bool = true;
			//lc.log_ext("only files with extensions: "+fef,this);
			extensions = fef.toUpperCase().split("\\s+");
		} else
			fef_bool = false;
		
		fef_not = e.getAttributeValue("file_extension_not");		
		if ( fef_not != null && fef_not.length() > 0 ) {
			fef_not_bool = true;
			//lc.log_ext("only files with not_extensions: "+fef_not,this);
			extensions_not = fef_not.toUpperCase().split("\\s+");
		} else
			fef_not_bool = false;		
		
		if ( fef_bool && fef_not_bool )
			throw new Exception(tname+" cannot handle 'file_extension' and 'file_extension_not', use only one of those.");
		
		String file_regex = e.getAttributeValue("file_regex");
		if ( file_regex != null && file_regex.length() > 0 ) {
			file_regex_pattern = Pattern.compile(file_regex);
			//lc.log_ext("only files with regex: "+file_regex,this);
		} 
		
		String dir_regex = e.getAttributeValue("dir_regex");
		if ( dir_regex != null && dir_regex.length() > 0 ) {
			dir_regex_pattern = Pattern.compile(dir_regex);
			//lc.log_ext("only dirs with regex: "+dir_regex,this);
		} 		
		
		/* max_level */
		String tmpMaxLevel =  e.getAttributeValue("max_level");
		if ( tmpMaxLevel !=null)
			JOptionPane.showMessageDialog(null, "Scan: max_level depriciated, use 'level_limit' instead!");
		tmpMaxLevel =  e.getAttributeValue("level_limit");
		if ( tmpMaxLevel == null ) tmpMaxLevel =  e.getAttributeValue("levels");
		if ( tmpMaxLevel == null ) tmpMaxLevel =  e.getAttributeValue("limit");
		if ( tmpMaxLevel != null ) {
			level_limit = Commons.string2int(tmpMaxLevel);
			//if ( level_limit == 0);
			//lc.log_ext("level_limit '0' is crap, but we do it",this);			
		}

		// field
		
		String para_field = "";
		if ( e.getAttribute("field") != null ) {
			para_field = e.getAttributeValue("field");
		}
		do_md2 = Commons.hasFlagIn(e, "field","md2");
		do_md5 = Commons.hasFlagIn(e, "field","md5");
		do_sha1 = Commons.hasFlagIn(e, "field","sha1");
		do_sha256 = Commons.hasFlagIn(e, "field","sha256");
		do_sha384 = Commons.hasFlagIn(e, "field","sha384");
		do_sha512 = Commons.hasFlagIn(e, "field","sha512");	
		
		app_no_pro = Commons.hasFlag(e, "app_no_pro");
		
		if ( Commons.hasFlag(e, "hash")) {
			label_md2 = "hash";
			label_md5 = "hash";
			label_sha1 = "hash";
			label_sha256 = "hash";
			label_sha384 = "hash";
			label_sha512 = "hash";
		}
		
		do_ext = Commons.hasFlagIn(e, "field","ext");
		do_ext2 = Commons.hasFlagIn(e, "field","ext2");
		if ( do_ext2) {
			if ( e.getAttribute("ext2_ext") == null )
				throw new Exception("scan needs addtionally to the field 'ext' a list of extension in 'ext2_ext'");
			else
				do_ext2_ext = e.getAttributeValue("ext2_ext");
		}
		do_more = Commons.hasFlagIn(e, "field","more");
		
		do_ipath = Commons.hasFlagIn(e, "field","ipath");
		
		boolean kb = Commons.hasFlagIn(e, "field","size");
		if ( kb )
			JOptionPane.showMessageDialog(null, "Scan: field: size depriciated, use 'kb' instead!");
		// kb, bytes, mb, id
		
		
		
		kb = Commons.hasFlagIn(e, "field","kb");
		boolean bytes = Commons.hasFlagIn(e, "field","bytes");
		boolean mb = Commons.hasFlagIn(e, "field","mb");
		
		boolean id = Commons.hasFlagIn(e, "field","id");
		do_lm =  Commons.hasFlagIn(e, "field","lm");

		// no_..
		if ( 	Commons.hasFlagIn(e, "field","no_gfn") 
				|| Commons.hasFlagIn(e, "field","no_pre")
				|| Commons.hasFlagIn(e, "field","no_post")
				|| Commons.hasFlagIn(e, "field","no_fn") 
				|| Commons.hasFlagIn(e, "field","no_*")) 
			throw new Exception( "scan: flag 'no_???' depressive, no not supported any more, define the field! field="+e.getAttributeValue("field"));

		no_gfn = !Commons.hasFlagIn(e, "field","gfn");
		no_pre = !Commons.hasFlagIn(e, "field","pre");
		no_post = !Commons.hasFlagIn(e, "field","post");
		no_post2 = !Commons.hasFlagIn(e, "field","post2");
		no_fn = !Commons.hasFlagIn(e, "field","fn");			

		report_files = Commons.hasFlagIn(e, "field","files");
		report_files_tested = Commons.hasFlagIn(e, "field","files_tested");
		report_level = Commons.hasFlagIn(e, "field","level");
		report_attributes = Commons.hasFlagIn(e, "field","attributes");
		
		// flag_all
		flag_hidden = Commons.hasFlag(e, "hidden");
		ignore_files = Commons.hasFlag(e, "no_files") || Commons.hasFlag(e, "dirs_only");
		

		
		// min_bytes, max_bytes
		if ( e.getAttributeValue("min_bytes") != null && !e.getAttributeValue("min_bytes").equals("-1"))
			min_bytes = Commons.string2int(e.getAttributeValue("min_bytes"));
		if ( e.getAttributeValue("max_bytes") != null && !e.getAttributeValue("max_bytes").equals("-1"))
			max_bytes = Commons.string2int(e.getAttributeValue("max_bytes"));	
		
		if ( e.getAttributeValue("min_kb") != null && !e.getAttributeValue("min_kb").equals("-1"))
			min_bytes = Commons.string2int(e.getAttributeValue("min_kb"))*1024;
		if ( e.getAttributeValue("max_kb") != null && !e.getAttributeValue("max_kb").equals("-1"))
			max_bytes = Commons.string2int(e.getAttributeValue("max_kb"))*1024;			

		if ( e.getAttributeValue("min_mb") != null && !e.getAttributeValue("min_mb").equals("-1"))
			min_bytes = Commons.string2int(e.getAttributeValue("min_mb"))*1048576;
		if ( e.getAttributeValue("max_mb") != null && !e.getAttributeValue("max_mb").equals("-1"))
			max_bytes = Commons.string2int(e.getAttributeValue("max_mb"))*1048576;	
		
		// not documented
		if ( e.getAttributeValue("living") != null ) {
			living = Commons.string2int(e.getAttributeValue("living"));
		}
		

		
		String aggregate = null;
		if ( e.getAttribute("agg") != null ) {
			aggregate = e.getAttributeValue("agg");
			//lc.log_ext("agg: "+aggregate,this);
		}
		if ( do_more  ) {
			if ( aggregate == null )
				aggregate = "more";
			else if (!Commons.hasFlag(aggregate, "more"))
				aggregate = aggregate + " more";
		}
		
		String aggregate_avg = null;
		if ( e.getAttribute("avg") != null ) {
			aggregate_avg = e.getAttributeValue("avg");
			//lc.log_ext("avg: "+aggregate_avg,this);
			if ( !report_files) {
				report_files = true;
				//lc.log_ext("field 'files' will be reported because avg is set!",this);
			}
		}		
		//System.out.println("-------------- dir="+dir);
		//System.out.println("-------------- dsc="+rh.getSitzung().getDescriptor());
		Sp.ph("Scan requests dir: "+dir);
		FileObject rootDir = CommonsVfs.resolveFolderRead(dir, rh.getSitzung());
				
				
			//	CommonsVfs.fsm.resolveFile(dir);
		FileName rootDirFn = rootDir.getName();
		if ( !rootDir.isFolder())
			throw new Exception("The parameter dir '"+dir+"' is no directory");
		
		ScanElement  rootElement = new ScanElement("dir");
		home = rootDirFn.getPathDecoded();
		if ( !home.endsWith(FileName.SEPARATOR))
			home = home + FileName.SEPARATOR;
		// Sp.p("home="+home);


		rootElement.setAttribute("dir", CommonsVfs.constructNormalizedDir(rootDirFn) );
		
		if ( id )
			rootElement.setAttribute("id",getNextId());
		/*
		if ( fef_bool ) {
			rootElement.setAttribute("file_extension", fef );
		}
		
		if ( fef_not_bool ) {
			rootElement.setAttribute("file_extension_not", fef_not );
		}		
		
		if ( file_regex_pattern != null )
			rootElement.setAttribute("file_regex", file_regex );
		if ( dir_regex_pattern != null )
			rootElement.setAttribute("dir_regex", dir_regex );
			*/
		
		Scan_DirReturner files_tested = bearbeiteDirectoryCatcher(rootElement,rootDir,1,kb,mb,bytes,id);
		if ( report_files_tested) {
			// rootElement.setAttribute("files_tested", Integer.toString(files_tested));
			rootElement.putInt("files_tested", files_tested.getFilesTested());
			
		}
	//etParas.setParameter("scanned_files_tested", Integer.toString(.));
		
		if ( report_level) {
			rootElement.setAttribute("max_level", Integer.toString(max_level));
			
		}
		retParas.setParameter("scanned_max_level", Integer.toString(max_level));
		
		//lc.log_ext("Found "+count_dirs+" Directories.", this);
		retParas.setParameter("scanned_agg_dirs", Integer.toString(count_dirs));
		if ( aggregate != null ) {
			// lc.log_ext("Aggregating now ...", this);
			String[] agg_val = aggregate.split("\\s+");
			for ( String cur_agg_val: agg_val ) {

				until_now_aggregated = 0;
				//lc.log_ext("Aggregating '"+cur_agg_val+"' now ...", this);
				if ( !Commons.hasFlag(para_field, cur_agg_val)) {
					throw new Exception("If you want to aggregate the field '"+cur_agg_val+"' you have also to define it as field -> field='"+cur_agg_val+"' is missing!");
				}
				
				if ( cur_agg_val.equals("files") || cur_agg_val.equals("files_tested")|| cur_agg_val.equals("more"))
					aggregateDirectory2(rootElement, cur_agg_val);
				else
					aggregateDirectory(rootElement, cur_agg_val);
				// lc.log_ext("Aggregated '"+cur_agg_val+"' "+until_now_aggregated + " of "+count_dirs+" Directories.", this);
			}			
		}
		
		if ( aggregate_avg != null ) {
			// lc.log_ext("Aggregating now ...", this);
			String[] avg_val = aggregate_avg.split("\\s+");
			for ( String cur_avg_val: avg_val ) {
				until_now_aggregated = 0;
				//lc.log_ext("Averaging '"+cur_avg_val+"' now ...", this);
				if ( aggregate==null || !Commons.hasFlag(aggregate, cur_avg_val)) {
					throw new Exception("If you want to average the field '"+cur_avg_val+"' you have also to define it as aggregate -> aggregate='"+cur_avg_val+"' is missing!");
				}				
				/*
				if ( cur_avg_val.equals("files") || cur_avg_val.equals("files_tested"))
					//avgDirectory2(rootElement, cur_avg_val);
					;
				else */
					avgDirectory(rootElement, cur_avg_val);
				// lc.log_ext("Aggregated '"+cur_agg_val+"' "+until_now_aggregated + " of "+count_dirs+" Directories.", this);
			}			
		}		
		rootElement.setAttribute("agg_dirs", Integer.toString(count_dirs));
		
		List<Attribute> al = e.getAttributes();
		Iterator<Attribute> ali = al.iterator();
		while ( ali.hasNext()) {
			Attribute ca = (Attribute)ali.next();
			if ( !ca.getName().equals("out"))
				rootElement.setAttribute("para_"+ca.getName(), ca.getValue());
		}
		rootElement.setAttribute("ipath", "" );		
		if ( do_more )
			clearMore ( rootElement);
		
		if ( rootElement.getAttribute("agg_files")!=null)
			retParas.setParameter("scanned_agg_files", rootElement.getAttributeValue("agg_files"));
		
		// Dokument schreiben
		Document document = new Document(rootElement);
		CommonsVfs.grantsFileRead(out, rh.getSitzung());
		
		CommonsVfs.writeToFile(document, out,rh.getSitzung());
	
		 
		return retParas;
	}
	private void clearMore( Element e ) {
		
		if ( e.getAttribute("agg_more") != null ) {
			if ( e.getAttributeValue("agg_more").equals("0"))
				e.setAttribute("more", "0");
			else
				e.setAttribute("more", "1");
			e.removeAttribute("agg_more");
		}
		
		List<Element> childs = e.getChildren("dir");
		Iterator<Element> i = childs.iterator();
		while ( i.hasNext() ) {
			Element now = (Element)i.next();
			clearMore(now);
		}		
	}

	
	private int aggregateDirectory2 ( ScanElement e, String att_name ) throws Exception {
		until_now_aggregated++;
		siganlLiving();
		int sum = 0;
		List<Element> childs = e.getChildren("dir");
		Iterator<Element> i = childs.iterator();
		while ( i.hasNext() ) {
			ScanElement now = (ScanElement)i.next();
			sum = sum + aggregateDirectory2 ( now, att_name);
		}

		try {
			sum = sum + e.getInt(att_name) ;
		} catch (Exception e1) {

		}
		// e.setAttribute("agg_"+att_name, Integer.toString(sum));
		e.putInt("agg_"+att_name, sum);
		
		return sum;
		
	}	
	

	private int aggregateDirectory ( ScanElement e, String att_name ) throws Exception {
		until_now_aggregated++;
		siganlLiving();
		int sum = 0;
		List<Element> childs = e.getChildren();
		Iterator<Element> i = childs.iterator();
		while ( i.hasNext() ) {
			ScanElement now = (ScanElement)i.next();
			if ( now.getName().equals("file")) {
				sum = sum + now.getInt(att_name);				
			} else if ( now.getName().equals("dir")) {
				sum = sum + aggregateDirectory ( now, att_name);
			}
		}
		
		// e.setAttribute("agg_"+att_name, Integer.toString(sum));
		e.putInt("agg_"+att_name, sum);

		return sum;
		
	}
	
	private void avgDirectory ( ScanElement e, String att_name ) throws Exception {
		until_now_aggregated++;
		siganlLiving();

		List<Element> childs = e.getChildren("dir");
		Iterator<Element> i = childs.iterator();
		while ( i.hasNext() ) {
			ScanElement now = (ScanElement)i.next();
			avgDirectory ( now, att_name);
		}
		
		// double sum = (double)(Commons.string2int(e.getAttributeValue("agg_"+att_name)));
		// double double_files = (double)(Commons.string2int(e.getAttributeValue("agg_files")));
		double sum = (double)(e.getInt("agg_"+att_name));
		double double_files = (double)(e.getInt("agg_files"));
		
		if ( double_files != 0 ) {
			double x = sum /  double_files;

			e.setAttribute("avg_"+att_name, ScanElement.cutDouble(x)); 				
		} else
			e.setAttribute("avg_"+att_name, "NaN"); 
		
	}	
	boolean temporary_trace_on = false;
	private Scan_DirReturner bearbeiteDirectoryCatcher(
			ScanElement rootElement,
			FileObject    directory,
			int     current_level,
			boolean kb,
			boolean mb,
			boolean bytes,
			boolean id ) {
		
		
		Scan_DirReturner result = null;
		try {
			temporary_trace_on = false;
			result = bearbeiteDirectory(
					rootElement,
					directory,
					current_level,
					kb,
					mb,
					bytes,
					id );
		} catch (Exception e) {
			Sp.pe(e);

			//lc.warn("*** Problem with directory: "+directory.getName().getURI(), this);

			//lc.warn("*** "+e.getLocalizedMessage(), this);
			result = new Scan_DirReturner();
			result.setEndReached(Scan_DirReturner.ERC_EXCEPTION);
		}
		
		if ( rh.shouldBreakOnNextOccations() ) {

			//lc.warn("Received Bone Request)", this);
			result.setEndReached(Scan_DirReturner.ERC_BONE);
		}
		return result;
		
	}
	private Scan_DirReturner bearbeiteDirectory(
			ScanElement rootElement,
			FileObject    directory,
			int     current_level,
			boolean kb,
			boolean mb,
			boolean bytes,
			boolean id ) throws Exception {
		
			count_dirs++;
			int tested_files = 0;
			int count_files = 0;
			int count_files_total = 0;
			double sum_dir_bytes =0, sum_dir_mb=0, sum_dir_kb = 0;
			siganlLiving();
			Scan_DirReturner finisher = new Scan_DirReturner();

			if ( (level_limit > -1 ) && (current_level > level_limit )) {
				// level ende erreicht
				
				if ( report_files ) {
					// rootElement.setAttribute("files",Integer.toString(count_files));
					rootElement.putInt("files",count_files);
				}
				if ( report_level ) {
					rootElement.setAttribute("level",Integer.toString(current_level));
					if ( max_level < current_level )
						max_level = current_level;
				}		

				finisher.setEndReached(Scan_DirReturner.ERC_LEVEL_LIMIT_REACHED);
			}
			
			
			if ( !finisher.isLeaf() && app_no_pro && current_level==2) {
				if ( directory.getName().getBaseName().startsWith("pro.") || directory.getName().getBaseName().equals("pro_standard")) {
			
					finisher.setEndReached(Scan_DirReturner.ERC_APP_NO_PRO);
				} 
			}
			
			if ( !finisher.isLeaf() && (dir_regex_pattern != null && current_level > 1)) {
				
				Matcher matcher = dir_regex_pattern.matcher(directory.getName().getBaseName());
				if ( !matcher.matches() ) {
					
					finisher.setEndReached(Scan_DirReturner.ERC_DIR_SEL_CRIT_NOT_FULLFILLED);
				} 
			}
			if ( finisher.isLeaf()) {
				// finisher kann jetzt 0 oder -1 sein
				// 0 =  ende der levels errreicht
				// -1 = levelende zwar nicht erreicht, 
				if ( do_more || no_empty_dirs) {
					System.out.println("GETTING NINOS FUER "+directory.getName().getURI());
					FileObject[] files = CommonsVfs.getNinos(directory,rh.getSitzung());
					if ( files != null && files.length>0) {
						if ( do_more )
							rootElement.putInt("more",1);
						if ( no_empty_dirs) {
							finisher.plusFilesTested(files.length);
							finisher.plusCountFiles(files.length);
						}
					} else {
						if ( do_more )
							rootElement.putInt("more",0);
					}
				}
				


				return finisher;
			}
		
			// finisher == 9
			
			FileObject einFile = null;
			String einFileBaseName = null;
			FileObject[] files = CommonsVfs.getNinos(directory,rh.getSitzung());		
			if ( files == null ) {
				
				// lc.warn("Problem: "+directory.getCanonicalPath()+", OS didn't deliver directories  ", this);
				if ( do_more ) {
					rootElement.putInt("more",1);
				}
				//lc.log_ext("Problem "+directory.getName().getURI()+", OS didn't deliver directories ", this);
				// rh.terminalProgress("Problem "+directory.getCanonicalPath()+", OS didn't deliver directories ", this);
				rootElement.setAttribute("no_os_data","1");
				finisher.setEndReached(Scan_DirReturner.ERC_OS_PROBLEM);
				return finisher;
			} else {
			// Loopen, nur echte Files und keine Verzeichnisse verarbeiten
			boolean has_a_sub_dir = false;	
			for (int i=0; i<files.length; i++) {
				einFile =  files[i];
				einFileBaseName = einFile.getName().getBaseName();
				
				if ( einFile.isFile()) {
					

					tested_files++;	
					finisher.plusFilesTested(1);
					boolean write_it = true;
					double bytes_double = 0;
					if ( ignore_files ) {
						write_it = false;
					} else {
						if ( fef_bool ) {
							write_it = false;
							
							
							for ( String thisExtension: extensions ) {
								if ( einFileBaseName.toUpperCase().endsWith(thisExtension)) {
									write_it=true;
									
									break;
								} else {
									
								}
							}
						} else if ( fef_not_bool ) {
							write_it = true;
							
							for ( String thisExtension: extensions_not ) {
								if ( einFileBaseName.toUpperCase().endsWith(thisExtension)) {
									write_it=false;
									break;
								}							
							}
						} 
						// frueher mit sign_mac && 
						if ( !flag_hidden && einFile.isHidden() ) {
							write_it = false;						
						}
						
						if ( file_regex_pattern != null ) {
							Matcher matcher = file_regex_pattern.matcher(einFileBaseName);
							if ( !matcher.matches() ) 
								write_it = false;
						}
						
						bytes_double = einFile.getContent().getSize();
						int bytes_intt = (int)bytes_double;
						
						if ( min_bytes != -1 && (bytes_intt < min_bytes) )
							write_it = false;
						if ( max_bytes != -1 && (bytes_intt > max_bytes) )
							write_it = false;						
					}

					
					
					if ( write_it ) {
						count_files++;
						count_files_total++;
						finisher.plusCountFiles(1);
						ScanElement fileElement = new ScanElement("file");
						fileElement.setText(einFileBaseName);
						if ( id )
							fileElement.setAttribute("id",getNextId());
						
						if ( !no_gfn)
							fileElement.setAttribute("gfn",getGoodfilename(einFileBaseName));
						
						if ( do_lm ) {
							fileElement.setAttribute("lm",CommonsVfs.getLm(einFile,rh.getSitzung()));
						}
						
						if ( do_md2 )
							fileElement.setAttribute(label_md2,getMd2(einFile));
						if ( do_md5 )
							fileElement.setAttribute(label_md5,getMd5(einFile));
						
						if ( do_sha1 )
							fileElement.setAttribute(label_sha1,getSha1(einFile));
						if ( do_sha256 )
							fileElement.setAttribute(label_sha256,getSha256(einFile));
						if ( do_sha384 )
							fileElement.setAttribute(label_sha384,getSha384(einFile));
						if ( do_sha512 )
							fileElement.setAttribute(label_sha512,getSha512(einFile));							
						
						if ( do_ext ) {
							
							String theext = Commons.getExtOfBaseName(einFileBaseName);
							if ( theext != null)
								fileElement.setAttribute("ext",theext);
													
						}
						
						if ( do_ext2 ) {
							int last_index_of = einFileBaseName.lastIndexOf(".");
							if ( last_index_of > -1 && last_index_of<einFileBaseName.length() ) {
								String untilNow = einFileBaseName.substring(last_index_of+1).toLowerCase();
								if ( Commons.hasFlag(do_ext2_ext, untilNow)) {
									int last_index_of2 = einFileBaseName.lastIndexOf(".", last_index_of-1);
									if ( last_index_of2 > -1 ) {
										untilNow = einFileBaseName.substring(last_index_of2+1).toLowerCase();										
									} 
								} 
								fileElement.setAttribute("ext2",untilNow);
																
							}							
						}						
						
						if ( do_ipath )
							fileElement.setAttribute("ipath",getIntermediatePath(einFile.getParent().getName().getPathDecoded()));

						if ( report_attributes ) {
							fileElement.setAttribute("can_exe",Commons.boolToStringXr(einFile.isExecutable()));
							fileElement.setAttribute("can_read",Commons.boolToStringXr(einFile.isReadable()));
							fileElement.setAttribute("can_write",Commons.boolToStringXr(einFile.isWriteable()));
							fileElement.setAttribute("is_hidden",Commons.boolToStringXr(einFile.isHidden()));
							
						}
					
						
						
						int x = einFileBaseName.indexOf(".");
						if ( !no_pre) {
							if ( x==-1)
								fileElement.setAttribute("pre",einFileBaseName);
							else
								fileElement.setAttribute("pre",einFileBaseName.substring(0,x));						
						}
						
						if ( x != -1 && !no_post )
							fileElement.setAttribute("post",einFileBaseName.substring(x));
						
						if ( x != -1 && !no_post2 ) {
							try {
								fileElement.setAttribute("post2",einFileBaseName.substring(x+1));
							} catch (Exception e) {

							}
						}
						
						if ( !no_fn)
							fileElement.setAttribute("fn",CommonsVfs.constructNormalizedDir(directory.getName())+einFileBaseName);
											
						if ( kb || mb || bytes) {
							
							if ( bytes ) {
								// String bytes_int = Integer.toString((new Double(bytes_double)).intValue());
								fileElement.putInt("bytes",bytes_double);
								sum_dir_bytes = sum_dir_bytes + bytes_double;
							}
							
							if ( kb ) {
								double kb_double = (bytes_double / 1024);
								// String kb_int = Integer.toString((new Double(kb_double)).intValue());
								// fileElement.setAttribute("kb",kb_int);
								fileElement.putInt("kb", kb_double);
								sum_dir_kb = sum_dir_kb + kb_double;
							}
							
							if ( mb ) {
								double mb_double = (bytes_double / 1048576);
								// String mb_int = Integer.toString((new Double(mb_double)).intValue());
								// fileElement.setAttribute("mb",mb_int);
								fileElement.putInt("mb", mb_double);
								sum_dir_mb = sum_dir_mb + mb_double;
							}
							
						}

						rootElement.addContent(fileElement);						
					}

				} else if ( einFile.isFolder() ) {
					has_a_sub_dir = true;
					// lc.log_ext(this, "Reading Directory "+einFile.getName());
					ScanElement dirElement = new ScanElement("dir");
					if ( id )
						dirElement.setAttribute("id",getNextId());					
					dirElement.setAttribute("rpath",einFileBaseName);
					dirElement.setAttribute("ipath",getIntermediatePath(einFile.getName().getPathDecoded()));
					/*if ( do_lm ) {
						dirElement.setAttribute("lm",CommonsVfs.getLm(einFile));
					}*/
					
					Scan_DirReturner dc=
					 bearbeiteDirectoryCatcher(dirElement,einFile,current_level+1,kb,mb,bytes,id);
					dc.aggregateUp();
					finisher.addChild(dc);

					if ( report_files_tested)
					// dirElement.setAttribute("files_tested",Integer.toString(dir_tested));
						dirElement.putInt("files_tested",dc.getFilesTested());
					
					if ( dc.end_reason == Scan_DirReturner.ERC_BONE ) {
						break;
					} else if ( dc.end_reason == Scan_DirReturner.ERC_APP_NO_PRO ) {
					} else if ( !no_empty_dirs) {
						// alles beim alten
						rootElement.addContent(dirElement);	
					} else {
						// im kill_empty -modus
						if ( dc.sum_childs_count_files > 0)
							rootElement.addContent(dirElement);	
						else {
							String mx = "files_tested="+dc.files_tested+", count_files="+dc.count_files
									+" isLeafe="+dc.isLeaf()
									+" sum_childs_count_files="+dc.sum_childs_count_files
									+" sum_childs_files_tested="+dc.sum_childs_files_tested;
							//lc.trace("not writing because nothing here: "+dirElement.getAttributeValue("ipath")+"  "+mx,this);
						}
						

	
					}

				} else {
					// no file, no dir, so what ...
					//if (temporary_trace_on)
					//	lc.trace("ignoring unknown filetype: "+einFile.getName().getPathDecoded(),this);
				}
			}		
			if ( do_more )
				rootElement.putInt("more",0);
			
			}
			if ( report_files ) {
				// rootElement.setAttribute("files",Integer.toString(count_files));
				rootElement.putInt("files",count_files);
			}
			if ( report_level ) {
				rootElement.setAttribute("level",Integer.toString(current_level));
				if ( max_level < current_level )
					max_level = current_level;
			}
			
			if ( bytes ) {
				rootElement.setAttribute("bytes", ScanElement.cutDouble(sum_dir_bytes));
			}
			
			if ( kb ) {
				rootElement.setAttribute("kb", ScanElement.cutDouble(sum_dir_kb));
			}
			
			if ( mb ) {
				rootElement.setAttribute("mb", ScanElement.cutDouble(sum_dir_mb));
			}			
			return new Scan_DirReturner(tested_files,count_files,count_files_total);
	}	

	private String getGoodfilename ( String _in ) {
		int wo = _in.indexOf(".");
		if ( wo != -1 )
			return ( _in.substring(0,wo) + _in.substring(wo+1) );
		else
			return ( _in );
	}
	
	private String getCuttedfilename ( String _in ) {
		int wo = _in.indexOf(".");
		if ( wo != -1 )
			return ( _in.substring(0,wo) );
		else
			return ( _in );
	}

	private String getIntermediatePath ( String file1 ) {

		String file = file1;
		if ( !file.endsWith(FileName.SEPARATOR))
			file = file + FileName.SEPARATOR;
		String retval = null;
		if ( file.length()>home.length())
			retval = file.substring(home.length());
		else
			retval ="";
	
		return retval;
	}
	
	private String getMd5 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}	
	
	private String getMd2 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.md2Hex(fis);
		fis.close();
		return md5;
	}		

	private String getSha1 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.sha1Hex(fis);
		fis.close();
		return md5;
	}	
	
	private String getSha256 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(fis);
		fis.close();
		return md5;
	}		
	
	private String getSha384 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.sha384Hex(fis);
		fis.close();
		return md5;
	}		
	
	private String getSha512 ( FileObject file ) throws IOException {
		InputStream fis = CommonsVfs.getInputStreamByFileObject(file,rh.getSitzung());
		String md5 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(fis);
		fis.close();
		return md5;
	}		
	
	public String getExtensionId() {
		return ( tname );
	}
	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}