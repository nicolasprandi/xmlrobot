/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;

public class Tc_UiContext {
	
	private String app = null;
	private String inst = null;
	private String job = null;
	private String systemJob = null;


	private static LogConnector lcs = new LogConnector("UiContext");
	
	public Tc_UiContext() {
		
	}
    public Tc_UiContext cloneMe() {
    	Tc_UiContext ret = new Tc_UiContext();
    	ret.setApp(this.app);
    	ret.setInst(this.inst);
    	if ( !Commons.isEmpty(this.job)) {
    		ret.setJob(this.job);
    	} else if ( !Commons.isEmpty(this.systemJob)) {
    		ret.setJob(this.systemJob);
    	}

    	return ret;
    }
    
    public void setNull() {
    	app = null;
    	inst = null;
    	job = null;
    	systemJob = null;
    }
    
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getInst() {
		return inst;
	}

	public void setInst(String inst) {
		this.inst = inst;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
		this.systemJob = null;
	}
	
	public String getSystemJob() {
		return systemJob;
	}
	public void setSystemJob(String systemJob) {
		this.systemJob = systemJob;
		this.job = null;
	}
	
	public String getDesc() {
		String retval = "app "+app+", inst "+inst+", job "+job+", systemJob "+systemJob;
		return retval;
	}

	public String getAppDir() {
		String retval = null;
		if ( app != null )
			retval = CoreBuilder.db.getFullDirApp(app);
		return retval;
	}

	public String getInstDir() {
		String retval = null;
		if ( app != null && inst!=null )
			retval = CoreBuilder.db.getFullDirInst(app,inst);
		return retval;

	}

	
	
	
	public boolean haveApp() {
		if ( app!= null )
			return true;
		else
			return false;
	}

	public boolean haveInst() {
		if ( inst!= null )
			return true;
		else
			return false;
	}

	public boolean haveJob() {
		if ( app!= null && job!=null )
			return true;
		else
			return false;
	}

	public boolean haveJobForExecution() {
		if ( app!= null && inst!=null && job!=null )
			return true;
		else
			return false;
	}
	
	
	public static boolean changedApp ( Tc_UiContext alt, Tc_UiContext neu ) {
		boolean retval = changedXY( alt.getApp(), neu.getApp() );

		return 	retval;
	}
	public static boolean changedInst ( Tc_UiContext alt, Tc_UiContext neu ) {
		boolean retval = false;
		if ( changedApp(alt,neu) )
			retval =true;
		else
			retval =changedXY( alt.getInst(), neu.getInst() );		

		return 	retval;
	}
	public static boolean changedJob ( Tc_UiContext alt, Tc_UiContext neu ) {
		boolean retval = false;
		if ( changedInst(alt,neu))
			retval =  true;
		else
			retval =  changedXY( alt.getJob(), neu.getJob() );		

		return 	retval;
	}

	private static boolean changedXY ( String x, String y ) {
		
		boolean ret = false;
		// Conf.logger.info("ret ------------");
		if ( x==null && y==null )
			ret = false;
		else if ( (x==null && y!=null) || (x!=null && y== null))
			ret = true;
		else if ( x!= null && y!=null ){
			if (x.equals(y)) {
				ret = false;
			} else {
				ret = true;
			}
		} else
			ret = true;
		
		boolean ret2 = false;
		if ( (x==null && y!=null) ||(x!=null && y==null) )
			ret2 = true;
		else if ( x==null && y== null)
			ret2 = false;
		else if ( x.equals(y))
			ret2 = false;
		else
			ret2 = true;
		
		if ( ret!=ret2) {
			lcs.fatal("***************************************");
			lcs.fatal("***************************************");
			lcs.fatal("********progfehler in UiContext********");
			lcs.fatal("x   = "+x);
			lcs.fatal("y   = "+y);
			lcs.fatal("ret = "+ret);
			lcs.fatal("ret2= "+ret2);
			lcs.fatal("***************************************");
			lcs.fatal("***************************************");
		}
		
		return ret;
		
	}
	
	public String getLevel () {
		String ret = null;
		if ( getInst() != null )
			ret = "inst";
		else if ( getApp() != null )
			ret = "app";
		else 
			ret = "home";
		return ( ret );
	}
	
	public String getLevelOk () {
		String ret = null;
		if ( getInst() != null )
			ret = "inst";
		else if ( getApp() != null )
			ret = "app";
		else 
			ret = "db";
		return ( ret );
	}	
	
}