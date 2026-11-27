/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_const;

import at.nicolas.x.int_setup.CoreBuilder;


public class Zet_PrivilegGOLD {
	
	public static boolean viewJobSources() {
		return false;
	}
	
	public static boolean technicalInterface() {
		return false;
	}	
	
	public static boolean logLevelSelection() {
		return CoreBuilder.ws.isRoleAdminUp();
	}	
	
	public static boolean aboutOnStartup() {
		return true;
	}	
	
	public static boolean isDummy() {
		return true;
	}
	
	public static boolean mayContinueWithServerError() {
		return (false);
	}	
	
	public static boolean askBeforeLicReturnExecution() {
		return false;
	}	

}