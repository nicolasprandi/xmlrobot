/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import at.nicolas.x.pub.Parameters;

public class ExceptionNotice {
	
	private String msg = null;
	private Parameters paras = null;
	private Exception e = null;
	
	public String getJobFile() {
		return jobFile;
	}

	private String job = null;
	private String jobFile = null;
	
	public String getMsg() {
		return msg;
	}

	public Parameters getParas() {
		return paras;
	}
	
	public Exception getException() {
		return e;
	}	
	
	public String getJob() {
		return job;
	}	
	
	public String getFile() {
		return jobFile;
	}	

	public ExceptionNotice ( String _msg, Parameters _paras, Exception _e )  {
		msg = _msg;
		paras = _paras;
		try {
			e = _e;
			
		} catch (Exception e) {

		}
	}
	
	public ExceptionNotice ( String _msg, Parameters _paras, Exception _e, String job, String jobFile ) {
		msg = _msg;
		paras = _paras;
		try {
			
			e = _e;
		} catch (Exception e) {

		}
		this.job = job;
		this.jobFile = jobFile;
	}	
	

}