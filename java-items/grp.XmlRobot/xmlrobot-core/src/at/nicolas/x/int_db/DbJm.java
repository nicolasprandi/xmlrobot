/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_coop.CryptoBase;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.LangException;

public class DbJm {
	private static boolean printWarnings = false;
	

	private static final String POSTFIX_JOBS = ".xrj";
	private static final String POSTFIX_JOBS2 = ".ljx";
	private static final String NEGATIVE_PREFIX_JOBS = ".";

	
	private LogConnector lc = new LogConnector("Jm");
	
	private String dir = null;
	
	private String v_level_default;
	private String ws_role = null;
	private String ws_lang = null;
	private String ws_os = null;
	private String ws_computer = null;
	private boolean isServiceApp = false;
	private boolean hasStaticPro = false;
	
	private ArrayList<DbJob> jobList = null;

	private ArrayList<DbJob> joblistTerminal = null;
	
	private boolean hasProjectInfo = false;
	private int  projectInfoLevel = 1; // inst;
	private boolean hasAppHelp = false;
	public static String futureFile = null;


	public boolean getHasProjectInfo() {
		return hasProjectInfo;
	}

	public int getProjectInfoLevel() {
		return projectInfoLevel;
	}	
	
	public boolean getHasAppHelp() {
		return hasAppHelp;
	}
	
	public Element getDbDump (String elementname, boolean jobfile, boolean natural ) {
		Element x = new Element(elementname);
		
		Iterator<DbJob> i = jobList.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			Element y = jme.getDbDump(jobfile,natural );
			x.addContent(y);
		}
					
		return x;
	}
	
	public DbJm( 	String __dir,
				String _role,
				String _lang,
				String _os,
				String _computer,
				String _v_level_default,
				boolean isServiceApp,
				CommStartMsg sm,
				boolean _hasStaticPro,
				boolean isEncrypted,
				ISitzung systemSitzung ) throws Exception  {

		/*[echnaton-timer]*/
		
		int isEncryptedNew;
		if ( isEncrypted )
			isEncryptedNew = 1;
		else
			isEncryptedNew = 0;

		int force_script_decryption = 4; /*caution*/
		/*
		 * 0 ... no, as defined in app
		 * 1 ... just offline
		 * 2 ... just online
		 * 3 ... always, offline and online
		 * 4 ... depending on individual file-encryption
		 */
		if ( force_script_decryption == 0 );
		else if ( force_script_decryption == 1 ) {
			if (systemSitzung.isOnline() == false )
				isEncryptedNew = 1;
		} else if ( force_script_decryption == 2 ) {
			if (systemSitzung.isOnline() == true )
				isEncryptedNew = 1;
		} else if ( force_script_decryption == 3 ) {
				isEncryptedNew = 1;
		} else if ( force_script_decryption == 4 ) {
			isEncryptedNew = -1;
		} else throw new Exception("unknown force_script_decryption="+force_script_decryption);
			
		Sp.p("++ DbJm dir = "+__dir+", isEcrypted="+isEncryptedNew);
		
		dir = Commons.constructNormalizedDir(__dir);
		ws_role = _role;
		ws_lang = _lang;
		ws_os = _os;
		ws_computer = _computer;
		v_level_default = _v_level_default;
		this.isServiceApp = isServiceApp;
		this.hasStaticPro = _hasStaticPro;
		CryptoBase cb = null;
		
		if ( isEncryptedNew==1 || isEncryptedNew == -1  ) {
			/*[echnaton-crypto]*/
			/* sicherheitsstufe */ cb = new CryptoBase("q=EvE%Mt2?F44*+-");
		}
		
		if ( !CommonsVfs.existsDir(dir,systemSitzung))
			throw new IOException("Jm says: The directory '"+dir+"' does not exist on the filesystem");
				
		FileObject fdir = CommonsVfs.resolveFolderRead(dir,systemSitzung);
		
		FileObject[] fileObjectV1 = CommonsVfs.getNinos(fdir,systemSitzung);
		String[] fileBasenames = new String[fileObjectV1.length];
		for (int i=0; i<fileObjectV1.length; i++) {
			fileBasenames[i] = fileObjectV1[i].getName().getBaseName();			
		}
				
		java.util.Arrays.sort(fileBasenames,String.CASE_INSENSITIVE_ORDER);
		
		FileObject[] fileObject = new FileObject[fileBasenames.length];

		for (int i=0; i<fileBasenames.length; i++) {
			fileObject[i] = fdir.getChild(fileBasenames[i]);			
		}		
		
		String absPathUri = fdir.getName().getURI();

		
		jobList = new ArrayList<DbJob>();
		
		
		for (int i=0; i<fileObject.length; i++) {
			FileObject einFile =  fileObject[i];
			String einFileBasename = einFile.getName().getBaseName();
			String einFileUri = einFile.getName().getURI();
		
			if ( einFileBasename.endsWith("-integrate.xml")) {
				throw new LangException(215,"Found a file in an application or services which ends with '-integrate.xml'"+
						" This is reserved for future use. you cant do that. regards: "+einFileUri);
			}
			
			if ( einFile.isFile() ) {
				
				if ( !einFile.isHidden() && !einFileBasename.startsWith(".") && 
						( einFileBasename.endsWith(POSTFIX_JOBS)   
								|| einFileBasename.endsWith(POSTFIX_JOBS2))) {
					if ( einFileBasename.endsWith(POSTFIX_JOBS2) && printWarnings)
						sm.out.print("Not amused with "+einFileBasename+" [use *.xrj] ");
					// we take this file
					
					int punktPos = einFileBasename.indexOf(".");
					
					boolean thisFileIsEncrypted = false;
					if ( punktPos > -1) {
						String nurFileName= einFileBasename.substring(0, punktPos);
						//System.out.println("nurFileName="+nurFileName);
						if ( nurFileName.endsWith("-encrypted"))
							thisFileIsEncrypted = true;
					} 
					
					
										
					String fullfilename = einFileUri;
					
					// lc.core("reading jobfile -> "+files[i]+"="+fullfilename,LogConnector.CORE_JOBMANAGER);
					LogConnector lc = new LogConnector("Jm():");
					
					Document doc = null;
					boolean performEncryption;
					if ( isEncryptedNew == 1 )
						performEncryption = true;
					else if ( isEncryptedNew == 0 )
						performEncryption = false;
					else
						performEncryption = thisFileIsEncrypted;
					
					// System.out.println("Reading jobfile: "+fullfilename);
					if ( performEncryption ) {
						// System.out.println("   ... is encrypted");
						try {
							//System.out.println("going to decrypt: "+einFile.getName().getBaseName()+"="+fullfilename);
							
							String blank = cb.decryptFile(fullfilename,systemSitzung);
							/*
							if ( fullfilename.endsWith("app.Panorama/translators-encrypted.xrj")) {
							System.out.println("--------------------------------");
							System.out.println(blank);
							System.out.println("--------------------------------");
							System.out.println("");
							} */
							
							//InputStream stream = new ByteArrayInputStream(blank.getBytes(StandardCharsets.UTF_8));
							doc = Commons.getXmldocumentByString(blank);
							
							// System.out.println("   ... decrypt finished");
						
						} catch (Exception e) {
							// TODO Auto-generated catch block
							// System.out.println("   ... decrypt with ERROR");
							Element ee = new Element("empty");
							doc =new Document(ee);
						}
					} else {
						// System.out.println("   ... going to take plain");
						doc = CommonsVfs.getXmldocumentByFilename(fullfilename,systemSitzung);	
						// System.out.println("   ... did plain with success");
					}
					// System.out.println("   ... did read it!");
					// jetzt kann das geloescht werden
					if (systemSitzung.isOnline()) {
						// nic123 
						CommonsVfs.deleteFile(fullfilename, systemSitzung);
					}
					
				

					Element root = doc.getRootElement();
					// prepare header
					/*
					svjzmh23 nic123 hier den header ersetzung
					der header wird sozusagen anders aufgef�llt
					hier werden alle child element ausser die jobs einghaengt, schraeg
					stattdess fix
					ein call_service/call_job introx */
					
					ArrayList<Element> header = new ArrayList<Element>();
					List<Element> hl = root.getChildren();
					Iterator<Element> k = hl.iterator();
					while ( k.hasNext() ) {
						Element ch = (Element)k.next();
						if ( ch.getName().equals("job")) ;
						else {
							 header.add(ch);
						}
					}
					
					// define jobs
					List<Element> list = root.getChildren ( "job" );
					Iterator<Element> j = list.iterator();

					// iterating all the jobs, job-master-eleemnet in je
					while ( j.hasNext()) {
						Element je = (Element)j.next();
						
						String jobid = je.getAttributeValue("id");
						/*
						if ( jobid.startsWith("sys_")) {
							sm.out.println("");
							sm.out.println("Take care, sys_* job/service found:"+jobid);
						} 
						*/
						if ( true) {
							String v_level = je.getAttributeValue("joblevel");
							if ( je.getAttribute("level") != null && printWarnings) {
								sm.out.println("");
								sm.out.println("! WARNING ! job '"+jobid+"' is still using attribute 'level'. Should be 'joblevel'!");								
							}
							String v_role  = je.getAttributeValue("role");
							String f_os    = je.getAttributeValue("os");	
							String f_lang  = je.getAttributeValue("lang");	
							String f_computer    = je.getAttributeValue("computer");
							
							String f_pri = je.getAttributeValue("pri");
							
							String jobinfo = null;
							if ( je.getAttribute("info")!=null)
								jobinfo = je.getAttributeValue("info");
	
							// defaults
							if ( v_level == null || v_level.equals(""))
								v_level = v_level_default;
							
							if ( v_role == null || v_role.equals(""))
								v_role = Rollen.getJobsDefaultRole();
							if ( f_pri == null || f_pri.equals(""))
								f_pri = "1";
							
							// validations
							if ( f_os != null && f_lang!=null )
								throw new Exception ("Jm says: Only ONE job-attribute of lang '"+f_lang+"' or f_os '"+f_os+"' of the job '"+jobid+"' in the jobfile '"+fullfilename+"' is allowed to be defined.");
	
							if ( !DbJob.isLevelValid(v_level) && !v_level.equals("inst")) {
								Exception theProblem = new Exception ("Jm says: The job-attribute level '"+v_level+"' of the job '"+jobid+"' in the jobfile '"+fullfilename+"' is not correct");
								// lc.fatalException(theProblem.getLocalizedMessage(),theProblem);
								throw theProblem;
							}
							
							if ( !Rollen.isRoleValid(v_role)) {
								Exception theProblem = new Exception ("ROLE Jm says: The job-attribute role '"+v_role+"' of the job '"+jobid+"' in the jobfile '"+fullfilename+"' is not correct");
								// lc.fatalException(theProblem.getLocalizedMessage(),theProblem);
								throw theProblem;
							}
							
							if (f_os!=null && f_os.indexOf(" ") > -1 )
								throw new Exception ("Jm says: The job-attribute os '"+f_os+"' of the job '"+jobid+"' in the jobfile '"+fullfilename+"' does not have more than ONE value.");
							// nic123 hier wird der job mit dem 
							// header reingeschrieben
							String f_class_menu    = je.getAttributeValue("class_menu");
							
							DbJob jme = new DbJob ( fullfilename,
														jobid,
														v_role,
														f_lang,
														f_os,
														f_computer,
														je.getAttributeValue("tip"),
														v_level,
														je,
														header,
														this,
														jobinfo,
														f_pri,
														sm,
														systemSitzung,
														f_class_menu);
							
							 jobList.add(jme);
							
							if ( jme.getJobid().equals("??")) {
								hasProjectInfo = true;
								projectInfoLevel = DbJob.getLevelHeight(jme.getLevel());
							} else if ( jme.getJobid().equals("?")) {
								hasAppHelp = true;
							}
						}
								
						// lc.trace("workspace[@id='"+ce.getAttributeValue("id")+"'] [@default='"+ce.getAttributeValue("default")+"']");
						// s.addSelection(ce.getAttributeValue("id"), ce.getAttributeValue("id"));				
					}												
				}				
			}
		}		
		
		// define joblistTerminal


		
		joblistTerminal = new ArrayList<DbJob>();
		
		Iterator<DbJob> k = jobList.iterator();
		
		while ( k.hasNext() ) {
			
			DbJob cj = (DbJob)k.next();
			//System.out.println("ueberprue: job="+cj.getJobid()+" dessen rolle = "+cj.getRole()+"="+Rollen.getRoleHeightDefaultUser(cj.getRole()));
			lc.core("**** evaluating: jobid="+cj.getJobid(),LogConnector.CORE_JOBMANAGER);
			// & !DbJob.roleIsMinHidden(ws_role)
			if ( cj.isExplain()  ) lc.core("**** not taken: explain and no dev",LogConnector.CORE_JOBMANAGER);
			else if ( cj.getOs() != null  && !Rollen.roleIsMinHidden(ws_role)) lc.core("**** not taken: has os",LogConnector.CORE_JOBMANAGER);
			else if ( cj.getLang() != null && ws_lang!=null && (!cj.getLang().equals(ws_lang)) && !Rollen.roleIsMinHidden(ws_role)) lc.core("**** not taken: has lang",LogConnector.CORE_JOBMANAGER);
			// else if ( cj.getLang() != null && !Rollen.roleIsMinHidden(ws_role)) lc.core("**** not taken: has lang",LogConnector.CORE_JOBMANAGER);
			else if ( cj.getComputer() != null && !Rollen.roleIsMinHidden(ws_role) ) lc.core("**** not taken: has computer",LogConnector.CORE_JOBMANAGER);
			
			// else if ( cj.getRole().equals("hidden") );
			else if ( Rollen.getRoleHeightDefaultUser(ws_role) < Rollen.getRoleHeightDefaultUser(cj.getRole()) )
				lc.info("no taken job="+cj.getJobid()+", ws@role="+Rollen.getRoleHeightDefaultUser(ws_role)+", job@role="+Rollen.getRoleHeightDefaultUser(cj.getRole()));
			else {
				//System.out.println("    ---- Den nehmen wir: "+Rollen.getRoleHeightDefaultUser(ws_role)+ ">="+ Rollen.getRoleHeightDefaultUser(cj.getRole()));
				// System.out.println("---- role="+cj.getRole()+" "+cj.getJobid());
				
				/*[requo-role-dbjm-1]*/
				//if ( cj.getRole()!=null && Commons.hasFlag("user",cj.getRole()) ) {

					// System.out.println("          ---- did take: "+appId+" "+cj.getJobid());
					
					joblistTerminal.add(cj);
					lc.core("**** job is taken: "+cj.getJobid(),LogConnector.CORE_JOBMANAGER);
				/*[requo-role-dbjm-2]*/	
				//} 
			}
		}
		
		print_list();

	}
	

	public void print_list () {
		lc.core("\nJobs in Dir "+dir+" :",LogConnector.CORE_JOBMANAGER);

		Iterator<DbJob> i = jobList.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			jme.print_id();
		}
		System.out.print(" | ");
	}
	
	public void print_list_terminal () {
		lc.core("\nTerminalJobs in Dir "+dir+" :",LogConnector.CORE_JOBMANAGER);
		Iterator<DbJob> i = joblistTerminal.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			jme.print_id();
		}
	}	

	public ArrayList<DbJob> getJobList() {
		return jobList;
	}

	public ArrayList<DbJob> getJoblistTerminal() {
		return joblistTerminal;
	}
	
	public static ArrayList<JComponent> arrangeJoblistGridLayout( ArrayList<JComponent> inList, int spalten ) {
		int elements = inList.size();
		int zeilen = (int)Math.ceil((double)elements / spalten);
		
		JComponent tab[][] = new JComponent[spalten][zeilen];
		
		int i = 0;
		for (int iSpalten = 0; iSpalten < spalten; iSpalten++) {
			for (int iZeilen = 0; iZeilen < zeilen; iZeilen++) {
			
				int curElement = i;
				
				if ( curElement < inList.size() ) {
					tab[iSpalten][iZeilen] = inList.get(curElement);
					
				} else {
					tab[iSpalten][iZeilen] = null;
					
				}
				i++;
			}
		}
		
		ArrayList<JComponent> outList = new ArrayList<JComponent>();
		i = 0;
		for (int iZeilen = 0; iZeilen < zeilen; iZeilen++) {
			for (int iSpalten = 0; iSpalten < spalten; iSpalten++) {
			
				

				if (tab[iSpalten][iZeilen] != null ) {
					outList.add(tab[iSpalten][iZeilen]);
					
				} else {
					
					outList.add(null);
				}
				
				i++;

			}
		}		

		return outList;
	}

	
	public static ArrayList<DbJob> arrangeJoblistGridLayoutGUADEOIDEZEIT( ArrayList<DbJob> inList, int offset, int spalten ) {
		int elements = inList.size()+offset;
		int zeilen = (int)Math.ceil((double)elements / spalten);
		
		DbJob tab[][] = new DbJob[spalten][zeilen];
		
		int i = 0;
		for (int iSpalten = 0; iSpalten < spalten; iSpalten++) {
			for (int iZeilen = 0; iZeilen < zeilen; iZeilen++) {
			
				int curElement = i - offset;
				
				if ( i < offset ) {
					tab[iSpalten][iZeilen] = null;
					   
				} else if ( curElement < inList.size() ) {
					tab[iSpalten][iZeilen] = inList.get(curElement);
					
				} else {
					tab[iSpalten][iZeilen] = null;
					 
				}
				i++;
			}
		}
		
		ArrayList<DbJob> outList = new ArrayList<DbJob>();
		i = 0;
		for (int iZeilen = 0; iZeilen < zeilen; iZeilen++) {
			for (int iSpalten = 0; iSpalten < spalten; iSpalten++) {
			
				

				if (tab[iSpalten][iZeilen] != null ) {
					outList.add(tab[iSpalten][iZeilen]);
					
				} else if ( i< offset) {
					
				} else {
					
					outList.add(null);
				}
				
				i++;

			}
		}		

		return outList;
	}	
	
	public DbJob getJobOfJoblistTerminal( String jobid ) {

		DbJob found = null;
		
		Iterator<DbJob> i = joblistTerminal.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			if ( jme.getJobid().equals(jobid) ) {
				found = jme;
				break;
			}
		}
		
		return ( found );
		
	}
	
	public DbJob selectJob ( String sjobid ) {
		return ( selectJobNeu(sjobid));
		
	}
	/*
	private DbJob selectJobAlt ( String sjobid ) {
	
		DbJob found = null;
		
		Iterator<DbJob> i = jobList.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			if ( jme.getJobid().equals(sjobid) ) {
				found = jme;
				break;
			}
		}
		
		return ( found );		
	}	*/
	
	private DbJob selectJobNeu ( String sjobid ) {
		
		ArrayList<DbCandidate> candidates = new ArrayList<DbCandidate>();
		int max_pri = -1;

		Iterator<DbJob> i = jobList.iterator();
		while ( i.hasNext() ) {
			DbJob jme = (DbJob)i.next();
			int cur_pri = -1;

			if ( jme.getJobid().equals(sjobid) ) {
				cur_pri = 0;
				
				// This job has the right jobid
				
				// if it has also a lang then the lang must fit and the pri++
				/* lang ist schnee von gestern
				if ( jme.getLang() == null )
					// we take it 
					;				
				else {
					
					if ( CoreBuilder.ws.getLang() == null ) {
						// no lang defined in ws, we take it anyway
						cur_pri = -1;
					} else {
						// now we can match the lang
						if (jme.getLang().indexOf(CoreBuilder.ws.getLang()) != -1 ) {
							// the lang matches
							cur_pri++;
						} else {
							// the lang does not match, forget about this job
							cur_pri = -1;
						}
					}
				} 
				*/
				
				
				// os ...
				if ( jme.getOs() != null ) {
					if ( CoreBuilder.ws.getOs() == null ) {
						// no os defined in ws, we take it anyway
					} else {
						// now we can match the os
						if (jme.getOs().indexOf(CoreBuilder.ws.getOs()) != -1 ) {
							// the os matches
							cur_pri++;
						} else {
							// the os does not match, forget about this job
							cur_pri = -1;
						}
					}
				}				
				
				// computer
				if ( jme.getComputer() != null ) {
					if ( CoreBuilder.ws.getComputer() == null ) {
						// no computer defined in ws, we take it anyway
					} else {
						// now we can match the computer
						if (jme.getComputer().indexOf(CoreBuilder.ws.getComputer()) != -1 ) {
							// the computer matches
							cur_pri = cur_pri +2;
						} else {
							// the computer does not match, forget about this job
							cur_pri = -1;
						}
					}
				}				
				
				if ( cur_pri > -1 ) {
					// ok, we have a new candidate
					DbCandidate c = new DbCandidate ( jme,cur_pri);
					candidates.add(c);
					if ( cur_pri > max_pri ) max_pri = cur_pri;
				}
								
			}
		}		// end Loop

		// Now we make a backwards loop through candidates and select the first
		// Occurence with the max pri
		/* es fehlt die selektion nach os, lang */
		DbCandidate fc = null;
		
		Collections.reverse(candidates);
		
		Iterator<DbCandidate> j = candidates.iterator();
		while ( j.hasNext() ) {
			DbCandidate c = (DbCandidate)j.next();
			if ( c.getPri() == max_pri ) {
				fc = c;
				break;
			}
		}
		
		if ( fc != null )
			return ( fc.getJob());
		else {
			if ( !sjobid.endsWith("?"))
			lc.warn("The job '"+sjobid+"' was not found, searched directory is '"+dir+"'");
			return null;
		}
				
	}
	public String getDir() {
		return dir;
	}

	public boolean isServiceApp() {
		return isServiceApp;
	}
	
	public boolean hasStaticPro() {
		return hasStaticPro;
	}
		

}