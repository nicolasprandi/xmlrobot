package at.nicolas.x.WebVfs;

import org.apache.log4j.Level;

import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.ExceptionTracer;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;

public class XrRqRpEx extends XrRqRp {

	private Exception exception = null;
	private ExceptionTracer exceptionTracer=null;
	
	private String proId = null;
	private String proDir = null;
	private String appId = null;
	private String appDir = null;
	private Level loglevel = null;
	private LogConnector lc = null;
	private String jobid = null;
	private ITerminator terminator = null;
	private DbJob job	 = null;
	/*
	public String getTiBody() {
		return tiBody;
	}
	public void setTiBody(String tiBody) {
		this.tiBody = tiBody;
	}
	*/
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	//String tiBody;
	String tp;
	public XrRqRpEx(
			Exception exception,
			ExceptionTracer exceptionTracer,
			String tiBody,
			String tp)  {

		Sp.pe(exception);
		if ( exception != null)
			this.exception = exception; //new Exception(exception);
		this.exceptionTracer = exceptionTracer;
		// this.tiBody = tiBody;
		this.tp = tp;
		Sp.p("Gesetzter Exception="+exception.getLocalizedMessage());
	}
	public void set (	 
			String proId,
	 String proDir,
	 String appId,
	 String appDir,
	 Level loglevel,
	 LogConnector lc,
	 String jobid,
	 ITerminator terminator,
	 DbJob job) {
		this.job = job;
		this.jobid = jobid;
		this.proId =proId;
		this.proDir=proDir;
		this.appId=appId;
		this.appDir=appDir;
		this.loglevel=loglevel;
		this.lc		=lc;
		this.terminator = terminator;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public ExceptionTracer getExceptionTracer() {
		return exceptionTracer;
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
	public LogConnector getLc() {
		return lc;
	}
	public ITerminator getTerminator() {
		return terminator;
	}
	public DbJob getJob() {
		return job;
	}

}
