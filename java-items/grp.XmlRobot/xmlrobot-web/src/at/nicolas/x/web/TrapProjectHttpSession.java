package at.nicolas.x.web;

public class TrapProjectHttpSession  {

	 static String me="TrapProjectHttpSession.";
	 static int projectIndex = 0;
	
	 String getNextProjectId() {
		projectIndex++;
		String a = Integer.toString(projectIndex);
		if ( a.length() < 10 ) {
			a = ("00000000000" + a).substring(a.length());
		}
		return a;
	}
	 
	 public static String getNextProjectIdNbr() {
		projectIndex++;
		String a = Integer.toString(projectIndex);
		return a;
	}	 
	



}
