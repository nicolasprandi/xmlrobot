package at.nicolas.x.int_coop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class CoopDbci  implements ITask, ICoop {
	private String id = null;
	private String url = null;
	private String db = null;
	private String user = null;
	private String password = null;
	private boolean autocommit = false;
	private String id_app = null;
	private static final int act_open   = 0;
	private static final int act_close   = 1;
	
	//private boolean didAlreadyRegister = false;
	/*
	private static final int act_get_autocommit   = 1;
	private static final int act_ps_add   = 2;
	*/

	private static KeyWordMapper tasknames;
	
	String tname ="dbci";		
	IRequestHandler rh;	
	ILogConnector _lc=null;
	
	public String getDb () {
		return db;
	}
	@Override
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		
		this.rh = rh;
		this._lc = rh.getLogConnector();
		
		if ( e.getAttribute("id") == null ) 
			throw new Exception(tname+" needs attribute @id");
		else	
			id = e.getAttributeValue("id");
		
		if ( e.getAttribute("id_app") != null ) {
			if ( e.getAttributeValue("id_app").trim().length() > 0 )
				id_app =  e.getAttributeValue("id_app");
		}				
		
		//String action = e.getAttributeValue("act");
		int ac = tasknames.convert(e.getAttributeValue("act"));
		
		Parameters retParas = null;
		switch ( ac ) {
		case act_open: {	
			open(e);
 
			
			
			break;			
			/*


			*/
		} 
		default: { 	


			CoopDbci dbci;
			
			if ( id_app == null )
				dbci = (CoopDbci)rh.getNo(id, Wellknown.coop_type_dbci);
			else
				dbci = (CoopDbci)rh.getNoApp(id, Wellknown.coop_type_dbci,id_app);
			
			if ( dbci == null ) 
				throw new Exception("The "+tname+" with id '"+id+"' was not found");
			
		    
			switch ( ac ) {
		    case act_close: {	
		    	
				if ( id_app == null )
					rh.removeNo(id, Wellknown.coop_type_dbci);
				else
					rh.removeNoApp(id, Wellknown.coop_type_dbci, id_app);
		    	_lc.log_ext("Closed "+dbci.id, this);
		    	break;
		    }	    
		    } 
		  	break;

		}
		}  
				
		 
		return retParas;
	}
	
	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		/*
		try {
			conn.close();
			_lc.debug("Autoclose: "+id, this);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} */
	}

	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Dbci-Actions",2);
		tasknames.put("open", act_open);
		tasknames.put("close", act_close);
		
		tasknames.putFinished();		
				
	}
	public void closeConnection(Connection conn ) throws Exception {
		conn.close();
	}
	public Connection getConnection(IRequestHandler _rh) throws Exception {
		Connection conn = null;
		//if ( true) { //!didAlreadyRegister 
			try {
				Commons.registerJdbcDriverByDbtypeUrl(db, url);
				
				//didAlreadyRegister = true;
			} catch (Exception e2) {
				System.out.println("dbci-open failed with "+db);
				rh.printTrace(e2.getLocalizedMessage());
				e2.printStackTrace();
				throw new Exception("dbci-open failed with db ["+db+"] with requested url ["+url+"], Exception: "+e2.getMessage());
			}			
		//}
		try {
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(autocommit);
		} catch (Exception e1) {
			System.out.println("Problem with database-connection");
			e1.printStackTrace();
			_lc.error("Problem with database-connection");
			throw ( e1);
		}


		

			
		if ( db.equals("mysql")) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("SET sql_mode = 'PAD_CHAR_TO_FULL_LENGTH';");
			System.out.println("Did set sql_mode");
		}
		return conn;
	}

	public void open ( Element e ) throws Exception {
		System.out.println("Received for open: "+Commons.toText(e));
		String idx = e.getAttributeValue("id");
		if ( idx == null ) throw new LangException("task ["+tname+"] needs attribute @id");

		String id_appx = null;
		if ( e.getAttribute("id_app") != null ) {
			if ( e.getAttributeValue("id_app").trim().length() > 0 )
				id_appx =  e.getAttributeValue("id_app");
		}		
		url = e.getAttributeValue("url");
		if ( url == null ) throw new LangException("task ["+tname+"] needs attribute @url");
		
		db = e.getAttributeValue("db");
		if ( db == null )
			throw new LangException("task ["+tname+"] needs attribute @db");		
		
		/*

		*/
		
		user = e.getAttributeValue("user");
		if ( user == null ) throw new LangException("task ["+tname+"] needs attribute @user");
		
		password = e.getAttributeValue("password");
		if ( password == null ) throw new LangException("task ["+tname+"] needs attribute @password");

		if ( e.getAttribute("crypto") != null ) {
			if ( e.getAttributeValue("crypto").trim().length() > 0 ) {
				String crypto_app = null;
				if ( e.getAttribute("crypto_app") != null ) {
					if ( e.getAttributeValue("crypto_app").trim().length() > 0 )
						crypto_app =  e.getAttributeValue("crypto_app");
				}	
				
				// password entschluesseln
				CoopCrypto coopCrypto 	;	
				if ( crypto_app == null )
					coopCrypto= (CoopCrypto)rh.getNo(e.getAttributeValue("crypto"), CoopCrypto.myCoopType);
				else
					coopCrypto= (CoopCrypto)rh.getNoApp(e.getAttributeValue("crypto"), CoopCrypto.myCoopType,crypto_app);
				if ( coopCrypto == null )
					throw new Exception("Crypto-object was not found");
				password = coopCrypto.decrypt(password);
			}
				
		}	
		
		if ( e.getAttribute("autocommit") == null )
		throw new Exception("dbci requests @autocommit");
		
		autocommit = Commons.stringToBool(e.getAttributeValue("autocommit"));
		
		if ( id_app == null )
			rh.putNo(id, Wellknown.coop_type_dbci,  this);
		else
			rh.putNoApp(id, Wellknown.coop_type_dbci,  this,id_app);
		_lc.log_ext("Opened "+id, this);
		
		if ( Commons.hasFlag(e, "test") ) {
			Connection requested = getConnection(rh);
			closeConnection(requested);
			
		}
		
						
	}



}
