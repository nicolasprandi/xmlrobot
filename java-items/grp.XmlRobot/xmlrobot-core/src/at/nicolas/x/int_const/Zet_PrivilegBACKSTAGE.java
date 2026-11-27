/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_const;

import at.nicolas.x.int_setup.CoreBuilder;


public class Zet_PrivilegBACKSTAGE {
	
	public static boolean viewJobSources() {
		return true;
	}
	
	public static boolean technicalInterface() {
		return true;
	}	
	
	public static boolean logLevelSelection() {
		return true;
	}	
	
	public static boolean aboutOnStartup() {
		return (!CoreBuilder.ws.isRoleAdminUp());
	}	
	
	public static boolean isDummy() {
		return (false);
	}
	
	public static boolean mayContinueWithServerError() {
		return (false);
	}	
	
	public static boolean askBeforeLicReturnExecution() {
		return false;
	}	

}