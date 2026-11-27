package at.nicolas.x.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;

import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import at.nicolas.x.int_coop.CoopCrypto;
import at.nicolas.x.int_coop.CryptoBase;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_interpreter.CoopGlobalManager;
import at.nicolas.x.int_interpreter.ProzessInfo;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_GuiSizer;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class EeSystem {
	/*[timer]*/
	protected CoopGlobalManager cgm = new CoopGlobalManager();
	private static final boolean hasOfflineUserRepo = 
			/*[replace-hasOfflineUserRepo]*/ true; 
	private static final String offlineUserRepoDir =
			/*[replace-offlineUserRepoDir]*/ "C:\\Users\\neben\\Documents\\_xml_robot\\runningserver\\userrepo\\" ;


	
	

	//private static boolean old=false;
	private static final boolean usesMySql = false; /*[replace]*/
	private static final String postgres_schema = "formixat001."; /*[replace]*/

	public String tomcatTempDir;
	private static String me="EeSystem.";
	public String takeofffilename;
	private ISitzung theSystemSitzung;
	private static final String mySqlTablePrefix = "formixat001."; /*[replace]*/
	

	
	
	public DbApp getDbApp(String appId ) {
		return CoreBuilder.db.getAppById(appId);
	}
	private ArrayList<EeProject> registeredEeProjects = new ArrayList<EeProject>();
	public boolean mayAcceptNewProjects( String theappid ) {
		return true;
	}
	

	public boolean isIpBlocked(String _ipToTest ) {
		System.out.println("eeSystem isIpBlocked: "+_ipToTest);
		return false;
	}
	private void pdb(String header,String secret ) {
		System.out.println("DB   DB   DB  ");
		System.out.println("DB   DB   DB  "+header);
		System.out.println("DB   DB   DB  ");
	}

	private Connection connect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		/*
		MysqlDataSource dataSource = null;
		Connection conn = null;
		dataSource = new MysqlDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("sunny94?");
		dataSource.setServerName("localhost");
		conn = dataSource.getConnection();*/
		
		
		if ( hasOfflineUserRepo ) {
			return null;
		} else {
			// sicherheitsstufe
			final String url = "jdbc:postgresql://localhost:5432/formixat001"; /*[replace]*/
			final String user = "postgres"; /*[replace]*/
			final String password = "servas"; /*[replace]*/ // "hasimausi12";	//	sunny94?=
			final String driverClass ="org.postgresql.Driver"; /*[replace]*/  // "com.mysql.jdbc.Driver"
		
			
			try {
				Commons.registerJdbcDriverByClassnameUrl(driverClass, url);
				
				pdb("build com.my.jdbc.Driver Instance with success","");
			} catch (Exception e) {
				pdb("problem with class.forname::"+driverClass+" "+e.getLocalizedMessage(),"");
				e.printStackTrace();
				return null;
			}
			// Establish connection to MySQL
			try {
				pdb("Trying to connect to: "+url,user+" "+password);
				Connection conn = DriverManager.getConnection(url, user, password);
				pdb("----- connection ok for "+url,"");
				return conn;
			} catch (Exception e) {
				pdb("----- problem with connection for "+url+" :"+e.getLocalizedMessage(),"");
				//System.out.println("----- problem with connection for "+url+" "+user+" "+password+" :"+e.getLocalizedMessage());
				e.printStackTrace();
				return null;
			}			
		}

	}
	
	public void performUserLogin( 
			String _user, 
			String _pwd, 
			String _role, 
			String _appid, 
			Surrounding _surrounding,
			ISitzung _sitzung)throws Exception {
			System.out.println("start EeSystem.performUserLogin");

			performUserLoginDb(_user, _pwd, _role, _appid, _surrounding, _sitzung);
	}
    /*
     *     role VARCHAR(10),
valid_begin DATE,
valid_end DATE,
privs VARCHAR(255),
privsRoles VARCHAR(255),
appPrivs VARCHAR(255),
appPrivsRoles VARCHAR(255),
privParas VARCHAR(255),
privParasRoles VARCHAR(255), 
     */	

	private boolean isCorrectPwd ( String _pwd, String dbPwd ) {
	 try {		
	    System.out.println("isCorrectPwd _pwd="+_pwd+" dbPwd="+dbPwd);
			System.out.println("isCorrectPwd crypto_external_on=true");
			System.out.println("isCorrectPwd crypto_id="+crypto_id);
			// dieses crypto objekt herholen
			//System.out.println("pwd-1");
			ConcurrentHashMap<String, ICoop> mymap = 
			cgm.getCoopMap(null, crypto_id, CoopGlobalManager.loctype_g,false,null);
			System.out.println("pwd-2 mymap="+mymap);
			String theKey = CoopCrypto.myCoopType+":"+crypto_id;
			System.out.println("pwd-3 theKey="+theKey);
			CoopCrypto cryptoCoop  = (CoopCrypto)mymap.get(theKey);
			System.out.println("pwd-4 cryptoCoop="+cryptoCoop);
			boolean isValid = cryptoCoop.isValid(_pwd, dbPwd);
			System.out.println("pwd-5 isValid="+isValid);
			return isValid;
			/*
			final String pwdUser = cryptoCoop.encrypt(_pwd);
			System.out.println("pwd-5 pwdUser="+pwdUser);
			System.out.println("pwd-6 dbPwd="+dbPwd);
			if ( dbPwd == null || pwdUser == null || !dbPwd.equals(pwdUser))
				return false;
			else
				return true;			*/


	      } catch (Exception e) {
	    	  System.out.println("isCorrectPwd throws exception");
				e.printStackTrace();
				return false;
		}        
  
		
	}
	
	private static Document getXmldocumentByFilename( String filename)  throws Exception, JDOMException, IOException{
		File theFile = new File(filename);
		

						
		FileInputStream fis = new FileInputStream(theFile);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			doc = builder.build(fis);
			fis.close();
		} catch (Exception e1) {
			fis.close();
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);

			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  "+filename);
			
		} 
		
		return ( doc );
	}	
	private Element getUserRepoByXpath (  String username, String xpath, boolean isApplicationRequest ) {
		try {
			String gn = offlineUserRepoDir+username+".xml";
			System.out.println("gn="+gn);
			Document document = getXmldocumentByFilename(gn);
			Element root = document.getRootElement();
			System.out.println("Reading User definition: " +
					Commons.toText(root)
					);
			System.out.println("Reading User definition isApplicationRequest="+isApplicationRequest);
			//System.out.println("root element="+Commons.toText(root));
			Element result = Commons.xpathSelectSingleNode(root, xpath);
			if ( result == null ) System.out.println("result by "+xpath+" is null");
			else
				System.out.println("result by "+xpath+" is:"+Commons.toText(result));
			if ( result != null ){
				// gesuchtes element gefunden
				//System.out.println("found element="+Commons.toText(result));
				Element back = result.clone();
				if ( back.getAttribute("pwd")==null && root.getAttribute("pwd")!= null )
					back.setAttribute("pwd", root.getAttributeValue("pwd"));
				if ( isApplicationRequest) {

					if ( back.getAttribute("privs")==null && root.getAttribute("privs")!= null )
						back.setAttribute("privs", root.getAttributeValue("privs"));		
					
					if ( back.getAttribute("privsRoles")==null && root.getAttribute("privsRoles")!= null )
						back.setAttribute("privsRoles", root.getAttributeValue("privsRoles"));	
					
					if ( back.getAttribute("privParas")==null && root.getAttribute("privParas")!= null )
						back.setAttribute("privParas", root.getAttributeValue("privParas"));		
					
					if ( back.getAttribute("privParasRoles")==null && root.getAttribute("privParasRoles")!= null )
						back.setAttribute("privParasRoles", root.getAttributeValue("privParasRoles"));	
					
					if ( back.getAttribute("role")==null && root.getAttribute("role")!= null )
						back.setAttribute("role", root.getAttributeValue("role"));	
					
					if ( back.getAttribute("appPrivs")==null && root.getAttribute("appPrivs")!= null )
						back.setAttribute("appPrivs", root.getAttributeValue("appPrivs"));	
					
					if ( back.getAttribute("rights")==null && root.getAttribute("rights")!= null )
						back.setAttribute("rights", root.getAttributeValue("rights"));
					
					if ( back.getAttribute("appPrivsRoles")==null && root.getAttribute("appPrivsRoles")!= null )
						back.setAttribute("appPrivsRoles", root.getAttributeValue("appPrivsRoles"));	
									
				} else {
					// db
					if ( back.getAttribute("size")==null && root.getAttribute("size")!= null )
						back.setAttribute("size", root.getAttributeValue("size"));
				}
				back.setAttribute("found", "1");	
				//System.out.println("Returned: "+Commons.toText(back));
				return back;
			} else {
				// gesuchtes element nicht gefunden
				Element notfound = new Element("response");
				notfound.setAttribute("found", "0");
				return notfound;
			}
	
			
		} catch (Exception e) {
			Element hicks = new Element("response");
			hicks.setAttribute("found", "0");
			hicks.setAttribute("Reason", e.getMessage());
			Sp.pUserRepoLoginProblem (e.getMessage());

			return hicks;
			
		}
		
	}

	
	private Element getUserRepoFsPdbUser( String username) {
		return getUserRepoByXpath (  username, "pdb[@type='user']",false );
	}	
	
	private Element getUserRepoFsPdbColl( String collname) {
		return getUserRepoByXpath (  collname, "pdb[@type='coll']",false );
	}	
	private Element getUserRepoFsApp( String username, String appname) {
		 return getUserRepoByXpath (  username, "app[@appid='"+appname+"']",true );
	}		
	
	private Element getUserRepoApp( String username, String _appname) {
		String appname = _appname.replaceAll("[^A-Za-z0-9 ]", "");
		
		// nic123 hier wird angemeldet
		if ( hasOfflineUserRepo ) {
			return getUserRepoFsApp(username,_appname);
		} else {
			Element back = new Element("response");
			
			String table;
			if ( usesMySql ) 
				table = mySqlTablePrefix+"prv"+appname;
			else 
				table = "\"prv"+appname+"\"";
			
			//pdb("performUserLoginDb goes for table: "+table,"");
			pdb("----- performing login: user=["+username.toLowerCase()+"] app=["+appname+"] table=["+table+"]", username+" "+username);
			/*
			 * SELECT * FROM formixat001.prvformixrezeptblock where user='nicolas' and pwd='noi';
			 */
			
		    Statement stmt = null;
		    String query;
		    
		    if ( usesMySql )
		    	query = "select pwd, role, privs, privsRoles, appPrivs, appPrivsRoles, privParas, privParasRoles , rights " +
		                   "from " + table+ " where lower(user)='"+username.toLowerCase()+"'";
		    else
		    	query = "select \"pwd\", \"role\", \"privs\", \"privsRoles\", \"appPrivs\", \"appPrivsRoles\", \"privParas\", \"privParasRoles\", \"rights\" " +
		                   "from " + postgres_schema+table+ " where lower(\"user\")='"+username.toLowerCase()+"'";
		
		    //pdb("query =  "+query,null);

		    //Exception problem = null;
		    ResultSet rs = null;
		
		        
			try {
				try {
					stmt = myconnection.createStatement();
					rs = stmt.executeQuery(query);
				} catch (Exception e) {
					Sp.ph("Received Exception in accessing the base" + e.getMessage());
					this.redoConnection();
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					stmt = myconnection.createStatement();
					rs = stmt.executeQuery(query);				
				}
				
				if ( rs.next() ) {
					/*
					 * pwd, role, privs, privsRoles, appPrivs, appPrivsRoles, privParas, privParasRoles , rights " +
		            
					 */
					back.setAttribute("found", "1");
					back.setAttribute("pwd",rs.getString("pwd"));
					back.setAttribute("privs",rs.getString("privs"));
					back.setAttribute("privsRoles",rs.getString("privsRoles"));					
					back.setAttribute("privParas",rs.getString("privParas"));		
					back.setAttribute("privParasRoles",rs.getString("privParasRoles"));	
					back.setAttribute("role",rs.getString("role"));		
					back.setAttribute("appPrivs",rs.getString("appPrivs"));	
					back.setAttribute("rights",rs.getString("rights"));		
					back.setAttribute("appPrivsRoles",rs.getString("appPrivsRoles"));			
					
				} else {
					back.setAttribute("found", "0");
					back.setAttribute("reason", "no such user");
				}
				rs.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				back.setAttribute("found", "0");
				back.setAttribute("reason", e.getMessage());
				try {
					rs.close();
				} catch (SQLException e1) {
				}
			} 
			return back;
		}
	}	
	// das ist das login fuer eine applikation !!
	
	public void performUserLoginDb( 
			String _user, 
			String _pwd, 
			String _role, 
			String _appid, 
			Surrounding _surrounding,
			ISitzung _sitzung)throws Exception {
		
		// String _appReformated = _appid.replaceAll("[^A-Za-z0-9 ]", "");
		System.out.println("start EeSystem.performUserLoginDb");
		boolean doFinalTransactions = false;
		Element ui = getUserRepoApp(_user,_appid);
		if ( ui.getAttributeValue("found").equals("0")) {
			// not found
			_sitzung.setLoginSuspension(1*60*1000);
			throw new Exception("performUserLoginDb no such user: "+_user);
		} else {
			// yes found
			System.out.println("performUserLoginDb ui="+Commons.toText(ui));
			
	        if ( !isCorrectPwd(_pwd, ui.getAttributeValue("pwd"))) {
	        	// passwort falsh
	        	_sitzung.setLoginSuspension(1*60*1000);
        		Sp.pWrongLogin("WRONG LOGIN WRONG PWD",_sitzung);
        		throw new Exception("Wrong ship");       		
	        } else {
	        	// passwort richtig
	        	doFinalTransactions = true;
	        }
		}
		if ( doFinalTransactions ) {
			
	        	String txPrivs = ui.getAttributeValue("privs");
	        	
	        	final String privsRoles = ui.getAttributeValue("privsRoles");
	        	if ( txPrivs == null ) txPrivs = "";
	        	if ( privsRoles != null ) {
	        		if ( Commons.hasFlag(privsRoles, "super") ) {
	        			txPrivs = txPrivs + "  opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip opCrypto" ;
		        		txPrivs = txPrivs + " linkFnPro linkFnApp linkFnLibro linkFnSession linkFnElse viewFnPro viewFnApp viewFnElse " ;
		        		txPrivs = txPrivs + "  eeFooter dbConnect dbRead dbWrite dbService dbDecrypt downPro " ;
		        		txPrivs = txPrivs + "  dump " ;
		        		txPrivs = txPrivs + "  down drop " ;
		        		txPrivs = txPrivs + "  libro " ;		
	        		
	        		} else if ( Commons.hasFlag(privsRoles, "all") ) {
	        			txPrivs = txPrivs + "  opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip" ;
		        		txPrivs = txPrivs + " linkFnPro linkFnApp linkFnLibro linkFnSession linkFnElse viewFnPro viewFnApp viewFnElse " ;
		        		txPrivs = txPrivs + "  eeFooter dbConnect dbRead dbWrite dbService dbDecrypt downPro " ;
		        		txPrivs = txPrivs + "  dump drop " ;
		        		txPrivs = txPrivs + "  down " ;		
	        		
	        		} 
	        		
	        		if ( Commons.hasFlag(privsRoles, "opsBasic")) txPrivs = txPrivs +  "  opFiles opDirs opContent opInfo " ;
	        		if ( Commons.hasFlag(privsRoles, "opsSuper")) txPrivs = txPrivs +  "  opFiles opDirs opContent opInfo opAnalyze " ;
	        		if ( Commons.hasFlag(privsRoles, "opsZip")) txPrivs = txPrivs +    "  opFiles opDirs opContent opInfo opAnalyze opZip opUnzip " ;
	        		if ( Commons.hasFlag(privsRoles, "opsCopy")) txPrivs = txPrivs +   "  opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip" ;
	        		if ( Commons.hasFlag(privsRoles, "opsCrypto")) txPrivs = txPrivs + "  opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip opCrypto" ;
	        		if ( Commons.hasFlag(privsRoles, "opsAll")) txPrivs = txPrivs +    "  opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip opCrypto" ;
	        		
	        		if ( Commons.hasFlag(privsRoles, "files")) txPrivs = txPrivs + " linkFnPro linkFnApp linkFnLibro linkFnSession linkFnElse viewFnPro viewFnApp viewFnElse " ;
	        		if ( Commons.hasFlag(privsRoles, "db")) txPrivs = txPrivs + "  eeFooter dbConnect dbRead dbWrite dbService dbDecrypt downPro " ;
	        		if ( Commons.hasFlag(privsRoles, "dump")) txPrivs = txPrivs + "  dump " ;
	        		if ( Commons.hasFlag(privsRoles, "down")) txPrivs = txPrivs + "  down " ;
	        		if ( Commons.hasFlag(privsRoles, "libro")) txPrivs = txPrivs + "  libro " ;	
	        		if ( Commons.hasFlag(privsRoles, "trace")) txPrivs = txPrivs + "  trace " ;
	        		if ( Commons.hasFlag(privsRoles, "crypto")) txPrivs = txPrivs + "  opCrypto " ;
	        		
	        		if ( Commons.hasFlag(privsRoles, "drop")) txPrivs = txPrivs + "  drop " ;
	        		

	        	}
	        	
	        	String txPrivParas = ui.getAttributeValue("privParas");
	        	
	        	final String privParaRoles = ui.getAttributeValue("privParasRoles");
	        	if ( txPrivParas == null ) txPrivParas = "";
	        	if ( privParaRoles != null ) {
	        		if ( Commons.hasFlag(privParaRoles, "cut")) 
	        			txPrivParas = txPrivParas + " max_text_len='100' max_textblock_len='300' " ;
	        	}	 
	        	
	        	String tr = ui.getAttributeValue("role");
	        	
	        	String txAppPrivs = ui.getAttributeValue("appPrivs");
	        	String txRights = ui.getAttributeValue("rights");
	        	
	        	final String appPrivsRoles = ui.getAttributeValue("appPrivsRoles");
	        	if ( txAppPrivs == null ) txAppPrivs = "";
	        	if ( appPrivsRoles != null ) {
	        		/*
	        		if ( Commons.hasFlag(appPrivsRoles, "all")) 
	        			txAppPrivs = txAppPrivs + " Formix-Rezeptblock Compare Panorama " ;
	        		else if ( Commons.hasFlag(appPrivsRoles, "min")) 
	        			txAppPrivs = txAppPrivs + " Formix-Rezeptblock " ;	 
	        			*/       		
	        	}	 	       
	        	
	        	Sp.pGrantUserPrivs("privs for user "+_user+" are "+tr+", "+txPrivs+", "+txPrivParas+", "+txAppPrivs+", rights="+txRights);
	        	
	        	
				int trInt = Rollen.getRoleHeightDefaultFail(tr);		
				int neueRolleIndex =0;
				// rolle gegebenen fall runtersetzen
				if ( _role == null || _role.length() == 0 ) {
					// keine rolle eingegeben
					neueRolleIndex=trInt; // neue rolle ist die aus der pwddb
				} else {
					int eigegeben = Rollen.getRoleHeightDefaultFail(_role);
					if ( trInt < eigegeben )
						neueRolleIndex = trInt;
					else
						neueRolleIndex = eigegeben;
				}		
					
				boolean libroUpdate = Commons.hasFlag(txPrivs, "libro")	;
				//System.out.println("libroUpdate = "+libroUpdate);
				
				_sitzung.setRoleIndex(neueRolleIndex);
				_sitzung.setGuiPrivs(_surrounding.getGuiprivsOfRole(neueRolleIndex));
				_sitzung.setLibroUpdatePrivileg(libroUpdate);
				
				_sitzung.setPrivs(txPrivs);
				_sitzung.setRights(txRights);
				_sitzung.setPrivParas(txPrivParas);
				if ( txAppPrivs != null )
					_sitzung.setAppPrivs(txAppPrivs);
				
				if ( neueRolleIndex == 1 )
					_sitzung.setLoggedIn(false);
				else
					_sitzung.setLoggedIn(true);		
				_sitzung.resetLoginAttempt();      	
	        }
	        	


	}	
	
	// das ist das login fuer die project db
	
	public DbLoginResponse isValidDbXX( String user, String pwd,
			ISitzung _sitzung, boolean isCollection ) {
		System.out.println("start EeSystem.isValidDbXX");
		DbLoginResponse toret = new DbLoginResponse();
		toret.setHasAccess(false);
		
		if ( hasOfflineUserRepo ) {
			
			Element userEntry = null;
			if ( isCollection )
				userEntry = getUserRepoFsPdbColl(user);
			else
				userEntry = getUserRepoFsPdbUser(user);
			
			if ( userEntry.getAttributeValue("found").equals("1")) {
				System.out.println("isValidDbXX found="+userEntry.getAttributeValue("found")+"with XML: "+Commons.toText(userEntry));
				// gefunden userEntry.getAttributeValue
				toret.setHasAccess(isCorrectPwd(pwd, userEntry.getAttributeValue("pwd")));
				System.out.println("has access="+toret.isHasAccess());
				if ( toret.isHasAccess() && userEntry.getAttribute("size") != null ) 
					toret.setSize(Commons.string2int(userEntry.getAttributeValue("size")));				
				
			} else {
				// nicht gefunden
			}
			
		} else {
	
		    try {
		    	pdb("selecting in prvDbColls or user for user","");
		    	String query;
		    	if ( isCollection ) {
			    	if ( usesMySql )
			    		query = "select user, pwd, size " +
					               "from "+mySqlTablePrefix+"prvDbColls where lower(user)='"+user.toLowerCase()+"'";
			    	else
			    		query = "select \"user\", \"pwd\" , \"size\"" +
				               "from "+postgres_schema+"\"prvDbColls\" where lower(\"user\")='"+user.toLowerCase()+"'";		    		
		    	} else {
			    	if (usesMySql)
			    		query = "select user, pwd, size " +
					               "from "+mySqlTablePrefix+"prvDbUsers where lower(user)='"+user.toLowerCase()+"'";
			    	else
			    		query = "select \"user\", \"pwd\"  , \"size\"" +
				               "from "+postgres_schema+"\"prvDbUsers\" where lower(\"user\")='"+user.toLowerCase()+"'";			    	
		    	}
				Statement stmt = myconnection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				
				if ( rs.next()){
					toret.setHasAccess(isCorrectPwd(pwd, rs.getString("pwd")));
					if ( toret.isHasAccess() && rs.getObject("size") != null ) 
						toret.setSize(rs.getLong("size"));
					
					// es ist ein satz da
					rs.close();
					
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();								
			}
	        	
		}
		
        if ( !toret.isHasAccess()) {
        	_sitzung.setLoginSuspension(1*60*1000);
        }
        	
        return toret;			
	}
	public DbLoginResponse isValidDbCollection( String user, String pwd,
			ISitzung _sitzung ) {
		
		return isValidDbXX(user, pwd, _sitzung, true);
		/*
		DbLoginResponse toret = new DbLoginResponse();
		toret.setHasAccess(false);
		
	
	    try {
	    	pdb("selecting in prvDbColls for user","");
	    	String query;
	    	if ( usesMySql )
	    		query = "select user, pwd, size " +
			               "from "+mySqlTablePrefix+"prvDbColls where lower(user)='"+user.toLowerCase()+"'";
	    	else
	    		query = "select \"user\", \"pwd\" , \"size\"" +
		               "from "+postgres_schema+"\"prvDbColls\" where lower(\"user\")='"+user.toLowerCase()+"'";	    	
			
			Statement stmt = myconnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if ( rs.next()){
				toret.setHasAccess(isCorrectPwd(this.cbDb, pwd, rs.getString("pwd")));
				if ( toret.isHasAccess() && rs.getObject("size") != null ) 
					toret.setSize(rs.getLong("size"));
				
				// es ist ein satz da
				rs.close();
				
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
        
        if ( !toret.isHasAccess()) {
        	_sitzung.setLoginSuspension(1*60*1000);
        }
        	
        return toret;
        */
		
	}
	public DbLoginResponse isValidDbUser( String user, String pwd,
			ISitzung _sitzung ) {
		
		return isValidDbXX(user, pwd, _sitzung, false);
		/*
		DbLoginResponse toret = new DbLoginResponse();
		toret.setHasAccess(false);
		
	
	    try {
	    	pdb("selecting in "+mySqlTablePrefix+"prvDbUsers","");
	    	String query;
	    	if (usesMySql)
	    		query = "select user, pwd, size " +
			               "from "+mySqlTablePrefix+"prvDbUsers where lower(user)='"+user.toLowerCase()+"'";
	    	else
	    		query = "select \"user\", \"pwd\"  , \"size\"" +
		               "from "+postgres_schema+"\"prvDbUsers\" where lower(\"user\")='"+user.toLowerCase()+"'";	    	
			
			Statement stmt = myconnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if ( rs.next()){
				toret.setHasAccess(isCorrectPwd(this.cbDb, pwd, rs.getString("pwd")));
				if ( toret.isHasAccess() && rs.getObject("size") != null ) 
					toret.setSize(rs.getLong("size"));
				
				rs.close();
				
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
        
        if ( !toret.isHasAccess()) {
        	_sitzung.setLoginSuspension(1*60*1000);
        }
        	
        return toret;*/
		
	}


	public  void registerNewProjects( EeProject eeProject ) {
		registeredEeProjects.add(eeProject);
		return;
	}	
	
	public  void unRegisterNewProjects( EeProject eeProject ) {
		registeredEeProjects.remove(eeProject);
	}	
	public  void listRegisteredProjects(  ) {
		Iterator<EeProject> i = registeredEeProjects.iterator();
		while ( i.hasNext()) {
			EeProject tp = (EeProject)i.next();
			System.out.println(tp.getProjectId()+", "+tp.getProjectDir()+", "+tp.sessionId); 
		}
	}
	
	public  void shutdownRegisteredProjects(  ) {
		//this.cbDb = null;
		//this.cbLogin = null;
		try {
			if ( this.myconnection != null )
				this.myconnection.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Iterator<EeProject> i = registeredEeProjects.iterator();
		while ( i.hasNext()) {
			EeProject tp = (EeProject)i.next();
			System.out.println("starting to shut down      " + tp.getProjectId()+", "+tp.getProjectDir()+", "+tp.sessionId); 
			
			try {
				tp.shutDown();
				
				System.out.println("did shut down with success " + tp.getProjectId()+", "+tp.getProjectDir()+", "+tp.sessionId);
			} catch (FileSystemException e) {
				// TODO Auto-generated catch block
				Sp.pShutDownProblem(
						"PROBLEM with ShuTTInG DOWN projectid=[" + 
				tp.getProjectId()+"] projectdir=["+tp.getProjectDir()+"] "
				+"sessionid=["				
				+tp.sessionId+
				"] msg="+e.getMessage()
						);
			} 
		}
	}	
	public void appendApps(ServletContextEvent evt ) throws FileSystemException, Exception {
		String webapproot = evt.getServletContext().getRealPath("/")+"/";
		Sp.ph("EeSystem.appendApps : "+webapproot);
	
		if ( CommonsVfs.getTimeOfNow() > CommonsVfs.getTimeOfDate("20300101")) {
			throw new Exception("mist");
		}				
		/*[echnaton-customer]*/
		String webinf = webapproot+"WEB-INF/";
		Sp.p("WEB-INF ="+webinf);
		String serviceAppsZippedDir = webinf+"service_apps/";
		CoreBuilder.appendAppsOnline(serviceAppsZippedDir,theSystemSitzung,xmlrobotSystemDir);
		
	}
	public String xmlrobotSystemDir;
	private Connection myconnection = null;
	
	// CryptoBase cbLogin;
	
	private static final String crypto_id = /*crypto_id*/ "frog__";
	//private static final boolean crypto_external_on = true;
	public void redoConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		myconnection = connect();
	}
	
	public EeSystem (	String _tomcatTempDir,ServletContextEvent evt, ISitzung systemSitzung ) throws Exception {
		theSystemSitzung = systemSitzung;
		prozesstable = new Hashtable<Integer, ProzessInfo>(); 
		/*[echnaton-timer]*/
			
		// String takeofffilename = "C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt";
		Sp.ph(me+"creating with tempdir "+_tomcatTempDir);		
		Sp.ph("JEE/BoWebapp.new()      : ip.tomcatTempDir    ="+_tomcatTempDir);
		tomcatTempDir = _tomcatTempDir;
		
		String webapproot = evt.getServletContext().getRealPath("/")+"/";
		Sp.ph("webapproot ="+webapproot);
	
		
		ClassLoader mcl = this.getClass().getClassLoader();
		InterfaceParas ip = InterfaceParas.get(tomcatTempDir, mcl);
		/*[echnaton-customer]*/	
		CommStartMsg sm = new CommStartMsg(System.out);
		if ( ip == null )
			sm.out.println("JEE/Interface.startup() : received null as InterfaceParas");
		else {
			sm.out.println("JEE/Interface.startup() : ip.overwriteSystemp    ="+ip.overwriteSystemp);
			sm.out.println("                          ip.overwriteClassloader="+ip.overwriteClassloader.toString());
		}
		
		String standardHome = LocalFilesystem.getHomeDirXrFormat();
		
		CoreBuilder.allStartupActions(sm,true);
		//if ( old ) {
		//	CoreBuilder.build(takeofffilename,standardHome,sm, ip);	
		//} else {
			String webinf = webapproot+"WEB-INF/";
			Sp.ph("WEB-INF ="+webinf);
			String zippedServices = webinf+"services.zip";
			String serviceAppsZippedDir = webinf+"service_apps/";
			
			SimpleDateFormat formatter = new SimpleDateFormat ("yyMMddHHmmss");
			String isystemDir = "sys/"+formatter.format(new Date())+Commons.randomAlphaNumeric(5);
			xmlrobotSystemDir = tomcatTempDir + isystemDir+"/";
			
			CoreBuilder.buildOnline(zippedServices, serviceAppsZippedDir,					
					_tomcatTempDir, standardHome, sm, ip,theSystemSitzung,xmlrobotSystemDir);
		//}
		Ti_TerminalLink.setup(true);
		Tc_AppConsole.guiSizer = new Tc_GuiSizer();
		Tc_AppConsole.guiSizer.setSize(1);
		try {
			myconnection = connect();
			/*[echnaton-crypto]*/
			// sicherheitsstufe nic321
			// cbLogin = new CryptoBase("mariandel"); /*[replace]*/
			 		 
			 /*
			  * 	String crypto_id = null;
	boolean crypto_external_on = true;
			  */

			 
			 /*[echnaton-crypto]*/
			 pdb("Connected to db with success","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			myconnection = null;
			//cbLogin = null;
			//cbDb = null;
			System.out.println("PROBLEM with database");
		}
		
		

		
		//
		/*
		JobRunner.createCorebuilder(
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt", 
				"C:\\Users\\nicolas\\Documents\\_temp\\systemp\\", 
				false, 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\ant-4-xmlrobot.jar", 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\poi-4-xmlrobot.jar");
		
		*/
		// Interface.startup("C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt",
		// 		mp);
		///
	
		/*
		 * 	public static void startup(
			String takeoff, 
			InterfaceParas ip ) throws Exception {
		// TODO Auto-generated method stub

			
		
		
		
		CoreBuilder.allStartupActions(sm);
		CoreBuilder.build(takeoff,standardHome,sm, ip);
	}
		 * 
		 * 
		 */
		
	}
	
	public String getTomcatTempDirxx() {
		return tomcatTempDir;
	}	
	public String getTomcatTempDirForProject(String projectId) {
		return Commons.removeFsInCase(tomcatTempDir)+"/"+projectId+"/";
	}		
	
	Hashtable<Integer, ProzessInfo> prozesstable;
	private static int prozessIdCounter = 0;
	public synchronized int registerProcess(StoppableThread thread, String typetitle, String details, String username ) throws Exception {
		
		ProzessInfo pi = new ProzessInfo();
		pi.setThread(thread);
		pi.setTypetitle(typetitle);
		pi.setDetails(details);
		pi.setUsername(username);
		pi.setDate(new Date());
		prozessIdCounter++;
		prozesstable.put(prozessIdCounter, pi);
		return prozessIdCounter;
	}
	public synchronized void killProcess(int prozessId, 
			String username, 
			boolean kill_right) throws Exception {
		
		if ( !prozesstable.containsKey(prozessId))
			throw new Exception("Prozess with id '"+prozessId+"' does not exist");
		
		ProzessInfo pi = prozesstable.get(prozessId);
		if ( !pi.getUsername().equals(username) && !kill_right)
			throw new Exception("Prozess with id '"+prozessId+"' is not your process and you have not the appropriate right");
		StoppableThread thread = prozesstable.get(prozessId).getThread();
		//prozesstable.remove(prozessId);			
		// thread.stop();
		// thread.interrupt();
		thread.stopExecution();
		
	}
	
	public synchronized void finishedProcess(int prozessId) throws Exception {
		
		if ( !prozesstable.containsKey(prozessId))
			throw new Exception("Prozess with id '"+prozessId+"' does not exist");
		
		ProzessInfo pi = prozesstable.get(prozessId);
		
		prozesstable.remove(prozessId);			

		
	}
	
	public Element getProcesses( boolean all, String username ) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Element result = new Element("procs");
		result.setAttribute("now", formatter.format(new Date()));		

		
		Set<Integer> setOfKeys = prozesstable.keySet();
		for (Integer key : setOfKeys) {
			ProzessInfo pi = prozesstable.get(key);
			boolean take = all;
			if ( !take && pi.getUsername().equals(username))
				take = true;
			if ( take) {
				Element neu = new Element("proc");
				neu.setAttribute("pid", ""+key);
				String state = pi.getState();
				if (state!=null )
					neu.setAttribute("state", state);
				
				if (pi.getTypetitle()!=null )
					neu.setAttribute("type", pi.getTypetitle());
				if (pi.getDetails()!=null )
					neu.setAttribute("details", pi.getDetails());
				if (pi.getDate()!=null )
					neu.setAttribute("start", formatter.format(pi.getDate()));				
				if (all && pi.getUsername()!=null)
					neu.setAttribute("user", pi.getUsername());
				
				try {
					neu.setAttribute("id", "" + pi.getThread().getId());
					neu.setAttribute("alive", Commons.boolToString(pi.getThread().isAlive()));
				} catch (Exception e) {
					// TODO: handle exception
				}				
				result.addContent(neu);
			}			
		}
		return result;
	}
		

}
