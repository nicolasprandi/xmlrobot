package at.nicolas.x.WebVfs;

import org.apache.log4j.Level;

import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;

public class XrRqRpTi extends XrRqRp {
	private ITerminator terminator = null;
	private String proId;
	private String proDir;
	private String appId;
	private String appDir;
	private Level loglevel;
	private ITiHandler tiHandler;
	private LogConnector lc;
	public XrRqRpTi(ITerminator terminator,
					String proId,
					String proDir,
					String appId,
					String appDir,
					Level loglevel,
					ITiHandler tiHandler ,
					LogConnector lc) {
		// TODO Auto-generated constructor stub
		this.terminator = terminator;
		
		this.lc = lc;
		this.proId = proId;
		this.proDir = proDir;
		this.appId = appId;
		this.appDir = appDir;
		this.loglevel = loglevel;
		this.tiHandler = tiHandler;		
	}
	
	String jobText;
	String zeile_job;
	String zeile_app2;
	String zeile_inst;
	String jobSimu;
	boolean doFlush	;
	
	public ITerminator getTerminator() {
		return terminator;
	}
	public String getProId() {
		return proId;
	}
	public String getProDir() {
		return proDir;
	}
	public String getAppId() {
		return appId;
	}
	public String getAppDir() {
		return appDir;
	}
	public Level getLoglevel() {
		return loglevel;
	}
	public ITiHandler getTiHandler() {
		return tiHandler;
	}
	public LogConnector getLc() {
		return lc;
	}
	public String getJobText() {
		return jobText;
	}
	public void setJobText(String jobText) {
		this.jobText = jobText;
	}
	public String getZeile_job() {
		return zeile_job;
	}
	public void setZeile_job(String zeile_job) {
		this.zeile_job = zeile_job;
	}
	public String getZeile_app2() {
		return zeile_app2;
	}
	public void setZeile_app2(String zeile_app2) {
		this.zeile_app2 = zeile_app2;
	}
	public String getZeile_inst() {
		return zeile_inst;
	}
	public void setZeile_inst(String zeile_inst) {
		this.zeile_inst = zeile_inst;
	}
	public String getJobSimu() {
		return jobSimu;
	}
	public void setJobSimu(String jobSimu) {
		this.jobSimu = jobSimu;
	}
	public boolean isDoFlush() {
		return doFlush;
	}
	public void setDoFlush(boolean doFlush) {
		this.doFlush = doFlush;
	}
	public void setTerminator(ITerminator terminator) {
		this.terminator = terminator;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public void setProDir(String proDir) {
		this.proDir = proDir;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setAppDir(String appDir) {
		this.appDir = appDir;
	}
	public void setLoglevel(Level loglevel) {
		this.loglevel = loglevel;
	}
	public void setTiHandler(ITiHandler tiHandler) {
		this.tiHandler = tiHandler;
	}
	public void setLc(LogConnector lc) {
		this.lc = lc;
	}

}
