package at.nicolas.x.int_specs;



import org.apache.commons.vfs2.FileObject;
import org.jdom2.Element;

import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.UserInfo;

public class Surrounding {
	






	private TkMatcher tkMatcherFast = null;
	private TkMatcher tkMatcherSumOnly = null;
	/* private TkMatcher tkMatcherOfferFast = null;
	private TkMatcher tkMatcherOfferSequ = null; */
	
		
	public String projectDir =null;
	public boolean cloneProjectDir = false;

	public String firstJob= "??";
	public int header=0;
	public String headerJob=null;	
	public int footer=0;
	public String footerJob=null;	

	public String css=null;
	public String pub=null;
	

	
	public String bits = new String("");
	public boolean bitsHasSelWriteFile = false;
	public boolean bitsHasSelWriteDir = false;
	public boolean bitsHasSelReadFile = false;
	public boolean bitsHasSelReadDir = false;
	public boolean bitsPanoramaChoose = false;
	
	public SurrSelInfo selInfoWriteFile;
	public SurrSelInfo selInfoReadFile;
	public SurrSelInfo selInfoWriteDir;
	public SurrSelInfo selInfoReadDir;	
	
	public String extOn = "";
	public String extBits = "";

	private String extProDbConfString = "";
	Parameters pExtProDbConfString = null;
	private String extGuiprivs = null;
	
	public boolean extHardProject = false;
	/*

	*/
	
	public String proDbServiceJob = "Project-Database Actions";
	public String proDbCommitJob = "Project-Database Commit";
	public String proDbRollbackJob = "Project-Database Rollback";
	public String proDbCheckinJob = "Project-Database Write";
	public String proDbCheckoutJob = "Project-Database Read";
	public String proDbCheckoutShutdownx = "Project-Database Shutdown";
	




	public String getDescription() {
		String ret = 
				"SURROUNDING: "+
				"\n public               =["+pub+"] "+
				"\n bits                 =["+bits+"] "+
				"\n bitsHasSelWriteFile  =["+bitsHasSelWriteFile+"] "+
				"\n bitsHasSelWriteDir   =["+bitsHasSelWriteDir+"] "+
				"\n bitsHasSelReadFile   =["+bitsHasSelReadFile+"] "+
				"\n bitsHasSelReadDir    =["+bitsHasSelReadDir+"] "+
				"\n bitsPanoramaChoose   =["+bitsPanoramaChoose+"] "+			
				"\n projectDir           =["+projectDir+"] "+
				"\n cloneProjectDir      =["+cloneProjectDir+"] "+
				"\n firstJob             =["+firstJob+"] "+
				"\n header               =["+header+"] "+
				"\n headerJob            =["+headerJob+"] "+
				"\n footer               =["+footer+"] "+
				"\n footerJob            =["+footerJob+"] "+
				"\n css                  =["+css+"] "+
			//	"\n trace                =["+trace+"] "+
				"\n extOn                =["+extOn+"] "+
				"\n extBits              =["+extBits+"] "+

				"\n extProDbConfString   =["+extProDbConfString+"] "+
				"\n extGuiprivs          =["+extGuiprivs+"] "+				
				"\n extHardProject       =["+extHardProject+"] "+
						
				" ";		
	
		return ret;
		
		/*

	public String  = "";
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	public String  = "";
	
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;
	
	public boolean  = false;
	public boolean  = false;
	public boolean  = false;	
		 * 
		 * 
		 */
		
				
	}
	public Surrounding() {
		// System.out.println("new Surrounding() ------------------------------");
		
	}

	/*
	public static Surrounding getSurroundingForChoose() {
		Surrounding x = new Surrounding();
		return x;
	}
	*/
	public static Surrounding getSurroundingDesktop() {
		Surrounding x = new Surrounding();

		x.selInfoWriteFile = new SurrSelInfo();
		x.selInfoReadFile = new SurrSelInfo();
		x.selInfoWriteDir = new SurrSelInfo();
		x.selInfoReadDir = new SurrSelInfo();
		x.setExtOn("mayDump");
		return x;
	}	
	private Element footerElement = null;
	private boolean hasFooterElement = false;
	public Element getFooterElement() throws Exception {
		if ( !hasFooterElement ) {
			hasFooterElement = true;
			if ( footer != Surrounding.INCLUDE_OFF) { // && silence == SILENCE_NO) {
				String taskname;
				String jobname;
				if ( footer == Surrounding.INCLUDE_YES_STD) {
					taskname ="call_service";
					jobname ="include-footer";
				} else if ( footer == Surrounding.INCLUDE_YES_SERVICE_JOB) {
					taskname ="call_service";
					jobname =footerJob;
				} else if ( footer == Surrounding.INCLUDE_YES_APP_JOB) {
					taskname ="call_job";
					jobname =footerJob;
				} else {
					throw new Exception("xxx unknon footer="+footer);
				}
				Element e2 = new Element(taskname);
				e2.setAttribute("job",Entities.importEntities(jobname));
				e2.setAttribute("flag","catch silent");
				footerElement = e2;
						
			} else
				footerElement = null;
		}
		return footerElement;
		
	}
	private Element headerElement = null;
	private boolean hasHeaderElement = false;	
	public Element getHeaderElement() throws Exception {
		if ( !hasHeaderElement ) {
			hasHeaderElement = true;
			if ( header != Surrounding.INCLUDE_OFF) { // && silence == SILENCE_NO) {
				String taskname;
				String jobname;
				if ( header == Surrounding.INCLUDE_YES_STD) {
					taskname ="call_service";
					jobname ="include-header";
				} else if ( header == Surrounding.INCLUDE_YES_SERVICE_JOB) {
					taskname ="call_service";
					jobname =headerJob;
				} else if ( header == Surrounding.INCLUDE_YES_APP_JOB) {
					taskname ="call_job";
					jobname =headerJob;
				} else {
					throw new Exception("xxx unknon header="+header);
				}
				Element e2 = new Element(taskname);
				e2.setAttribute("job",Entities.importEntities(jobname));
				e2.setAttribute("flag","catch silent");
				headerElement= e2;
						
			} else
				headerElement = null;
			}
			return headerElement;
	}	

	

	public void setFirstJob(String firstJob) {
		if ( firstJob==null) return;
		this.firstJob = firstJob;
	}

	

	public void setProjectDir(String projectDir, boolean cloneProject) {
		if ( projectDir==null) return;
		this.projectDir = projectDir;
		cloneProjectDir = cloneProject;
	}
	



	public static final int INCLUDE_OFF = 0;
	public static final int INCLUDE_YES_STD = 1;
	public static final int INCLUDE_YES_SERVICE_JOB = 2;
	public static final int INCLUDE_YES_APP_JOB = 3;

	public void setHeader(String header, String headerJob) {
		if ( header==null) return;
		this.header = Commons.string2int(header);
		if ( headerJob != null)
		this.headerJob = headerJob;
	}
	
	public void setHeader(int header, String headerJob) {

		this.header = header;
		this.headerJob = headerJob;
	}	
	
	public void setHeaderOFF() {

		this.header = INCLUDE_OFF;
		this.headerJob = null;
	}		
	
	public void setFooterOFF() {

		this.footer = INCLUDE_OFF;
		this.footerJob = null;
	}		

	public void setFooter(String footer, String footerJob) {
		if ( footer==null) return;
		this.footer = Commons.string2int(footer);
		if ( footerJob != null)
		this.footerJob = footerJob;
	}
	
	public void setFooter(int footer, String footerJob) {

		this.footer = footer;
		this.footerJob = footerJob;
	}		

	public void setCss(String css) {
		if ( css==null) return;
		this.css = css;
	}
	
	public void setPub(String pub) {
		if ( pub==null) return;
		this.pub = pub;
	}	
	
	public boolean isPublic() {
		if ( pub == null ) return false;
		else if ( pub.equals("1")) return true;
		else return false;
	}




	
	public String getGuiprivsOfRole(int roleIndex) {
		if ( extGuiprivs != null ) {
			Parameters p = new Parameters(extGuiprivs,"'");
			String pn;
			try {
				// System.out.println("getGuiprivsOfRole called with roleIndex="+roleIndex);
				pn = Rollen.getRoleByHeight(roleIndex);
				//System.out.println("                                       ="+pn);
				//System.out.println("                                       ="+extGuiprivs);
				if ( p.getParameter(pn) != null )
					return p.getParameter(pn);
				else
					return "000";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "000";
			}
		} else {
			return "000";
		}

		
	}
	
	public Surrounding getSurroundingChoose() {
		return getSurroundingInterpreterSub();
		/*
		Surrounding x = new Surrounding();
		x.setTrace(trace);
		x.setCss(css);
		return x;*/
	}
	
	public Surrounding getSurroundingInterpreterSub() {
		Surrounding x = cloneThis();
		x.setFooterOFF();
		x.setHeaderOFF();
		return x;
	}	
	
	public Surrounding cloneThis() {
	
		Surrounding x = new Surrounding();
		x.setFirstJob( firstJob) ;
		x.setProjectDir( projectDir,  cloneProjectDir);
		x.setHeader( header,  headerJob);
		x.setFooter( footer,  footerJob);
		x.setCss( css);
		x.setPub(pub);


		x.setTkMatcherFast(tkMatcherFast);
		x.setTkMatcherSumOnly(tkMatcherSumOnly);
		/*x.setTkMatcherOfferFast(tkMatcherOfferFast);
		x.setTkMatcherOfferSequ(tkMatcherOfferSequ);*/
		

		
		
		x.bits = bits;
		
		x.bitsHasSelWriteFile = bitsHasSelWriteFile;
		x.bitsHasSelWriteDir = bitsHasSelWriteDir;
		x.bitsHasSelReadFile = bitsHasSelReadFile;
		x.bitsHasSelReadDir = bitsHasSelReadDir;
		x.bitsPanoramaChoose = bitsPanoramaChoose;
		
		x.selInfoWriteFile = selInfoWriteFile;
		x.selInfoReadFile = selInfoReadFile;
		x.selInfoWriteDir = selInfoWriteDir;
		x.selInfoReadDir = selInfoReadDir;			
		x.setExtOn(extOn);
		x.setExtProDbConfString(extProDbConfString);
		x.setExtGuiPrivs(extGuiprivs);
		x.setExtBits(extBits);
		
		//System.out.println("-- Cloning surrounding from = "+this.getDescription());
		//System.out.println("--                       to = "+x.getDescription());
		
		return x;	
	}
	public String getUserHomeInDb() {
		// Parameters p = new Parameters(extProDbConfString,"'");
		return pExtProDbConfString.getParameter("db_dir");
		
	}
	/*
	private String myLibroDir = null;
	public String getLibroDir() {
		if ( myLibroDir == null ) {
			Parameters p = new Parameters(extProDbConfString,"'");
			myLibroDir = CommonsVfs.makeyBeautifulUri(p.getParameter("libro_dir"));
		}
		return myLibroDir;
		
		
	}
	*/	
	
	public String getDbConfigText() {
		
		return extProDbConfString;
		
	}	
	
	public void setExtOn(String extOn) {
		this.extOn = extOn;


	}
	private String theDbUserDir;
	public String getDbUserDir() {
		return theDbUserDir;
	}
	public String getPostDirForSession (String sessId )  {
		try {
			String pd = getPostDir();
			return pd+sessId+"/";
		} catch (Exception e) {
			Sp.pl("#HARRY# es gibt hier kein post db_post beim db-config!!");
			return null;
		}
	}
	private String postDir = null;
	private String getPostDir ( ) throws Exception {
		if ( postDir == null ) {
			// Parameters p = new Parameters(extProDbConfString,"'");
			
			
			if ( pExtProDbConfString.getParameter("db_post") == null ) throw new Exception("kein [db_post] im config");

			if ( pExtProDbConfString.getParameter("project_token") == null ) throw new Exception("kein [project_token] im config");
			
			
			String result = pExtProDbConfString.getParameter("db_post");
			FileObject foResult = CommonsVfs.resolveFolderSilent(result);
			String result2 = foResult.getName().getURI()+"/"+pExtProDbConfString.getParameter("project_token");
			FileObject foResult2 = CommonsVfs.resolveFolderSilent(result2);
			postDir = foResult2.getName().getURI()+"/";			
		}
		return postDir;

	}
	
	private String libro2Uri = null;
	public String getLibro2Uri ( ) throws Exception {
		if ( libro2Uri == null ) {
			// Parameters p = new Parameters(extProDbConfString,"'");
			
			
			if ( pExtProDbConfString.getParameter("db_libro2") == null ) 
				throw new Exception("kein [db_libro2] im config");
			
			
			String result = pExtProDbConfString.getParameter("db_libro2");
			FileObject foResult = CommonsVfs.resolveFolderSilent(result);
			libro2Uri = foResult.getName().getURI();			
		}
		return libro2Uri;

	}	
	public String getDatabaseLink(ISitzung dbsitzung ) {
		UserInfo forUserInfo = dbsitzung.getUserInfo();
		
		
		// Parameters p = new Parameters(extProDbConfString,"'");
		String myusername= null;
		String mycollectionname = null;
		
		if ( forUserInfo == null ) {
			//System.out.println("da is nixxxxx");
			myusername= null;
			mycollectionname = null;
		}else {
			myusername = forUserInfo.getName();
			mycollectionname = forUserInfo.getCollection();
		}

			
		String theDbDir = null;
		
		if ( pExtProDbConfString== null ) // #arenga#2#
			return null;
		else if ( pExtProDbConfString.getParameter("db_dir") != null ) {
			theDbDir = pExtProDbConfString.getParameter("db_dir");
		} else {
			// System.out.println("geben retour mix");
			return null;		
		}
	
		// 
		/*
		 * if ( myusername == null || myusername.equals("gast"))
			
		else 
		 * 
		 */
		String newDbDir = null;
		if ( mycollectionname != null  && mycollectionname.length()>0) {
			newDbDir = theDbDir+"colls/"+ Commons.cureNameAsFile(mycollectionname.toLowerCase())+"/";
		} else if ( myusername != null  && myusername.length() > 0) {
			newDbDir = theDbDir+"users/"+ Commons.cureNameAsFile(myusername.toLowerCase())+"/";
		} else {
			
			newDbDir = "";
		}
		return newDbDir;
					
		
	}
	private static final long default_storageLimit_Mem = 30001;
	
	private long storageLimit_Mem = -1;
	public long getStorageLimit_Mem () {
		if ( storageLimit_Mem == -1 ) {
			String sStorage = pExtProDbConfString.getParameter("storage_mem");
			if ( sStorage != null )
				storageLimit_Mem = Long.parseLong(sStorage);
			else 
				storageLimit_Mem = default_storageLimit_Mem;
			
		}
		return storageLimit_Mem;		
	}
	
	private static final long default_storageLimit_Pro = 500001;
	
	private long storageLimit_Pro = -1;
	public long getStorageLimit_Pro () {
		if ( storageLimit_Pro == -1 ) {
			String sStorage = pExtProDbConfString.getParameter("storage_pro");
			if ( sStorage != null )
				storageLimit_Pro = Long.parseLong(sStorage);
			else 
				storageLimit_Pro = default_storageLimit_Pro;
			
		}
		return storageLimit_Pro;		
	}	
	
	private static final long default_storageLimit_Session = 5000001;
	
	private long storageLimit_Session = -1;
	public long getStorageLimit_Session () {
		if ( storageLimit_Session == -1 ) {
			String sStorage = pExtProDbConfString.getParameter("storage_session");
			if ( sStorage != null )
				storageLimit_Session = Long.parseLong(sStorage);
			else 
				storageLimit_Session = default_storageLimit_Session;
			
		}
		return storageLimit_Session;		
	}		
	
	
	public String getCurrentProDbConfStringForServices(ISitzung curSitzung) {
		
		String neuer_db_dir = getDatabaseLink(curSitzung);
		Parameters p = new Parameters(extProDbConfString,"'");
		p.setParameter("db_dir", neuer_db_dir);
		return p.getString();
	}

	
	
	public void setExtProDbConfString(String receivedExtProDbConfString ) {
		this.extProDbConfString = receivedExtProDbConfString;
		this.pExtProDbConfString = new Parameters(this.extProDbConfString,"'");
		
	}
	public void setExtGuiPrivs(String receivedExtGuiPrivs ) {
		this.extGuiprivs = receivedExtGuiPrivs;
		// System.out.println("Surrounding did set guiprivs to: "+receivedExtGuiPrivs);
		
	}	
	
	public void setExtBits(String extBits) {
		Sp.p("-- Surroundg received extBits:" + extBits);
		this.extBits = extBits;
		this.extHardProject = Commons.hasFlag(extBits, "hardProject");
		
		/*
		this.extSeeFnPro = Commons.hasFlag(extBits, "seeFnPro");
		this.extClickFnPro = Commons.hasFlag(extBits, "clickFnPro");
		this.extDontCrossFnPro = Commons.hasFlag(extBits, "dontCrossFnPro");
		
		this.extSeeFnApp = Commons.hasFlag(extBits, "seeFnApp");
		this.extDontCrossFnApp = Commons.hasFlag(extBits, "dontCrossFnApp");
		
		this.extSeeFnElse = Commons.hasFlag(extBits, "seeFnElse");		
		this.extDontCrossFnElse = Commons.hasFlag(extBits, "dontCrossFnElse");	
		*/	
	}
	public TkMatcher getTkMatcherFast( ) {
		return tkMatcherFast;
	}
	public void setTkMatcherFast(TkMatcher tkMatcherFast) {
		this.tkMatcherFast = tkMatcherFast;
	}
	
	public TkMatcher getTkMatcherSumOnly() {
		return tkMatcherSumOnly;
	}
	public void setTkMatcherSumOnly(TkMatcher tkMatcherSumOnly) {
		this.tkMatcherSumOnly = tkMatcherSumOnly;
	}
	/*
	public TkMatcher getTkMatcherOfferFast() {
		return tkMatcherOfferFast;
	}
	public void setTkMatcherOfferFast(TkMatcher tkMatcherOfferFast) {
		this.tkMatcherOfferFast = tkMatcherOfferFast;
	}
	public TkMatcher getTkMatcherOfferSequ() {
		return tkMatcherOfferSequ;
	}
	public void setTkMatcherOfferSequ(TkMatcher tkMatcherOfferSequ) {
		this.tkMatcherOfferSequ = tkMatcherOfferSequ;
	}*/



	public boolean replaceButtonQmByI() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getDefaultColsText() {
		// TODO Auto-generated method stub
		return "60";
	}
	public String getDefaultColsMsg() {
		// TODO Auto-generated method stub
		return "80";
	}
	
	public String getDefaultRowsMsg() {
		// TODO Auto-generated method stub
		return "20";
	}
	public String getDefaultFileEnterSize() {
		// TODO Auto-generated method stub
		return "140";
	}		
	public String getDefaultFileNameEnterSize() {
		// TODO Auto-generated method stub
		return "60";
	}		

}
