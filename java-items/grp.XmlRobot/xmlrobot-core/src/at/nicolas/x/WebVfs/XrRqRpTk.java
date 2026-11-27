package at.nicolas.x.WebVfs;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;

public class XrRqRpTk extends XrRqRp {
	public TkTicketManager ticketManager ;

	public TkTicketManager getTicketManager() {
		return ticketManager;
	}
	public Level loglevel=null;
	public  String app=null;
	public String appdir=null;
	public String inst=null; 
	public String inst_dir=null;
	// public Interpreter interpreter;

	
	public Element getTranslatedForPrint() {
		try {
			return ticketManager.getTkElementForPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			return new Element("tk");
		}
	}

	

	public XrRqRpTk(
			TkTicketManager tkTicketManager ) throws Exception {
		// TODO Auto-generated constructor stub
		ticketManager = tkTicketManager;
		// this.interpreter = interpreter; 
	
	}
	public void set(
	Level loglevel,
	 String app,
	String appdir,
	String inst, 
	String inst_dir) {

		this.loglevel = loglevel;
		this.app = app;
		this.appdir = appdir;
		this.inst = inst;
		this.inst_dir = inst_dir;
		
	
	}

	public Level getLoglevel() {
		return loglevel;
	}
	public String getApp() {
		return app;
	}
	public String getAppdir() {
		return appdir;
	}
	public String getInst() {
		return inst;
	}
	public String getInst_dir() {
		return inst_dir;
	}


}
