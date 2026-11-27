/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_const;

import at.nicolas.x.int_setup.CoreBuilder;


public class Zet_Privileg {
	
	public static boolean viewJobSources() {
		return (CoreBuilder.ws.isRoleAdminUp() || CoreBuilder.rslogger.debugIsOn());
		// CoreBuilder.rslogger
	}
	
	public static boolean technicalInterface() {
		return  CoreBuilder.ws.isRoleAdminUp();
	}	
	
	public static boolean showServiceHelp() {
		return true;
	}	
	
	public static boolean showRestartButton() {
		return CoreBuilder.ws.isRoleExpertUp();
		// return true;
	}	
	
	public static boolean logLevelSelection() {
		return CoreBuilder.ws.isRoleAdminUp();
	}	
	
	public static boolean aboutOnStartup() {
		return (!CoreBuilder.ws.isRoleAdminUp());
	}	
	
	public static boolean isDummy() {
		return (!CoreBuilder.ws.isRoleAdminUp());
	}
	
	public static boolean automaticProjectInfo() {
		return (!CoreBuilder.ws.isRoleExpertUp());
	}	
	
	public static boolean mayContinueWithServerError() {
		return false; //(Conf.ws.isRoleAdminUp());
	}	
	
	public static boolean askBeforeLicReturnExecution() {
		return false;
		// return (Conf.ws.isRoleAdminUp());
	}	

}