package at.nicolas.x.int_setup;

import java.util.ArrayList;

public class Rollen {
	
	private static final String ROLE_PUBLIC = "public"; // 1
	private static final int iROLE_PUBLIC = 1;
	
	private static final String ROLE_USER = "user"; // 2
	private static final String ROLE_USER2 = "user2"; // 3
	private static final int iROLE_USER = 2;
	private static final int iROLE_USER2 = 3;

	private static final String ROLE_EXPERT = "expert"; // 4
	private static final String ROLE_EXPERT2 = "expert2"; // 5
	private static final String ROLE_EXPERT3 = "expert3"; // 6
	private static final int iROLE_EXPERT = 4;
	private static final int iROLE_EXPERT2 = 5;
	private static final int iROLE_EXPERT3 = 6;

	private static final String ROLE_BUSINESS = "boss"; // 7
	private static final String ROLE_BUSINESS2 = "boss2"; // 8
	private static final String ROLE_BUSINESS3 = "boss3"; // 9
	private static final String ROLE_BUSINESS4 = "boss4"; // 10
	
	private static final int iROLE_BUSINESS = 7;
	private static final int iROLE_BUSINESS2 = 8;
	private static final int iROLE_BUSINESS3 = 9;
	private static final int iROLE_BUSINESS4 = 10	;
	
	private static final String ROLE_ADMIN = "admin"; // 11
	private static final String ROLE_ADMIN2 = "admin2"; // 12
	private static final String ROLE_ADMIN3 = "admin3"; // 13
	private static final String ROLE_ADMIN4 = "admin4"; // 14
	private static final String ROLE_ADMIN5 = "admin5"; // 15
	
	private static final int iROLE_ADMIN = 11;
	private static final int iROLE_ADMIN2 = 12;
	private static final int iROLE_ADMIN3 = 13;
	private static final int iROLE_ADMIN4 = 14;
	private static final int iROLE_ADMIN5 = 15	;
	
	private static final String ROLE_TEST = "test"; // 16
	private static final String ROLE_TEST2 = "test2"; // 17
	private static final String ROLE_TEST3 = "test3"; // 18
	private static final String ROLE_TEST4 = "test4"; // 19
	private static final String ROLE_TEST5 = "test5"; // 20
	private static final String ROLE_TEST6 = "test6"; // 21
	
	private static final int iROLE_TEST = 16;
	private static final int iROLE_TEST2 = 17;
	private static final int iROLE_TEST3 =  18;
	private static final int iROLE_TEST4 = 19;
	private static final int iROLE_TEST5 =  20;
	private static final int iROLE_TEST6 = 21	;
	
	private static final String ROLE_DEV = "dev"; // 22
	private static final String ROLE_DEV2 = "dev2"; // 23
	private static final String ROLE_DEV3 = "dev3"; // 24
	private static final String ROLE_DEV4 = "dev4"; // 25
	private static final String ROLE_DEV5 = "dev5"; // 26
	private static final String ROLE_DEV6 = "dev6"; // 27
	private static final String ROLE_DEV7 = "dev7"; // 28
	
	private static final int iROLE_DEV = 22;
	private static final int iROLE_DEV2 = 23;
	private static final int iROLE_DEV3 = 24;
	private static final int iROLE_DEV4 = 25;
	private static final int iROLE_DEV5 = 26;
	private static final int iROLE_DEV6 = 27;
	private static final int iROLE_DEV7 = 28	;
	
	private static final String ROLE_HIDDEN = "hidden";
	
	private static final int iROLE_HIDDEN = 29;	
	
	public static String getRoleByHeight ( int hoehe ) throws Exception {
		String ret;

		
		switch(hoehe){
        case iROLE_PUBLIC: ret = ROLE_PUBLIC; break;
        case iROLE_USER: ret = ROLE_USER; break;
        case iROLE_USER2: ret = ROLE_USER2; break;
        case iROLE_EXPERT: ret = ROLE_EXPERT; break;
        case iROLE_EXPERT2: ret = ROLE_EXPERT2; break;
        case iROLE_EXPERT3: ret = ROLE_EXPERT3; break;
     
        case iROLE_BUSINESS: ret = ROLE_BUSINESS; break;
        case iROLE_BUSINESS2: ret = ROLE_BUSINESS2; break;
        case iROLE_BUSINESS3: ret = ROLE_BUSINESS3; break;
        case iROLE_BUSINESS4: ret = ROLE_BUSINESS4; break;

    	
        case iROLE_ADMIN: ret = ROLE_ADMIN; break;
        case iROLE_ADMIN2: ret = ROLE_ADMIN2; break;
        case iROLE_ADMIN3: ret = ROLE_ADMIN3; break;
        case iROLE_ADMIN4: ret = ROLE_ADMIN4; break;
        case iROLE_ADMIN5: ret = ROLE_ADMIN5; break;
    	
        case iROLE_TEST: ret = ROLE_TEST; break;
        case iROLE_TEST2: ret = ROLE_TEST2; break;
        case iROLE_TEST3: ret = ROLE_TEST3; break;
        case iROLE_TEST4: ret = ROLE_TEST4; break;
        case iROLE_TEST5: ret = ROLE_TEST5; break;
        case iROLE_TEST6: ret = ROLE_TEST6; break;
        

    			
        case iROLE_DEV: ret = ROLE_DEV; break;   
        case iROLE_DEV2: ret = ROLE_DEV2; break;
        case iROLE_DEV3: ret = ROLE_DEV3; break;
        case iROLE_DEV4: ret = ROLE_DEV4; break;
        case iROLE_DEV5: ret = ROLE_DEV5; break;
        case iROLE_DEV6: ret = ROLE_DEV6; break;
        case iROLE_DEV7: ret = ROLE_DEV7; break;
        
        case iROLE_HIDDEN: ret = ROLE_HIDDEN; break;        
        default:
        	throw new Exception("Da hats was groeberes");
        } 		

		return ret;

	}		
	private static int getMaxRoleHight () {
		return iROLE_HIDDEN;
	}
	public static String getJobsDefaultRole() {
		return "public";
	}
	public static int getRoleHeightDefaultUser ( String role ) {
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)return iROLE_USER; // iROLE_PUBLIC; // #arenga#2#
		else return hoehe;		
	}
	
	public static int getRoleHeightDefaultMax ( String role ) {
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)return 30;
		else return hoehe;		
	}	
	
	public static int getRoleHeightDefaultFail ( String role ) throws Exception {
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)throw new Exception("Expected a role, but did find: "+role);
		else return hoehe;		
	}		
	public static boolean isRoleValid( String role ) {
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)return false;
		else return true;				
	}
	/*
	public static boolean roleIsMinDev( String role ) {
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)return false;
		else if ( hoehe >= iROLE_DEV ) return true;
		else return false;
	}
	*/
	

	
	private static int getRoleHeightInternal ( String role ) {
		
		if ( role == null || role.equals("") ) return -1;

		if ( role.equals(ROLE_PUBLIC)) return iROLE_PUBLIC;
		else if ( role.startsWith("user")) {
			if ( role.equals(ROLE_USER)) return iROLE_USER;
			else if ( role.equals(ROLE_USER2)) return iROLE_USER2;
			else return -1;
		} else if ( role.startsWith("expert")) {
			if ( role.equals(ROLE_EXPERT)) return iROLE_EXPERT;
			else if ( role.equals(ROLE_EXPERT2)) return iROLE_EXPERT2;
			else if ( role.equals(ROLE_EXPERT3)) return iROLE_EXPERT3;
			else return -1;
		} else if ( role.startsWith("boss")) {
			if ( role.equals(ROLE_BUSINESS)) return iROLE_BUSINESS;
			else if ( role.equals(ROLE_BUSINESS2)) return iROLE_BUSINESS2;
			else if ( role.equals(ROLE_BUSINESS3)) return iROLE_BUSINESS3;
			else if ( role.equals(ROLE_BUSINESS4)) return iROLE_BUSINESS4;
			else return -1;
		} else if ( role.startsWith("admin")) {
			if ( role.equals(ROLE_ADMIN)) return iROLE_ADMIN;
			else if ( role.equals(ROLE_ADMIN2)) return iROLE_ADMIN2;
			else if ( role.equals(ROLE_ADMIN3)) return iROLE_ADMIN3;
			else if ( role.equals(ROLE_ADMIN4)) return iROLE_ADMIN4;
			else if ( role.equals(ROLE_ADMIN5)) return iROLE_ADMIN5;
			else return -1;
		} else if ( role.startsWith("test")) {
			if ( role.equals(ROLE_TEST)) return iROLE_TEST;
			else if ( role.equals(ROLE_TEST2)) return iROLE_TEST2;
			else if ( role.equals(ROLE_TEST3)) return iROLE_TEST3;
			else if ( role.equals(ROLE_TEST4)) return iROLE_TEST4;
			else if ( role.equals(ROLE_TEST5)) return iROLE_TEST5;
			else if ( role.equals(ROLE_TEST6)) return iROLE_TEST6;
			else return -1;
		} else if ( role.startsWith("dev")) {
			if ( role.equals(ROLE_DEV)) return iROLE_DEV;
			else if ( role.equals(ROLE_DEV2)) return iROLE_DEV2;
			else if ( role.equals(ROLE_DEV3)) return iROLE_DEV3;
			else if ( role.equals(ROLE_DEV4)) return iROLE_DEV4;
			else if ( role.equals(ROLE_DEV5)) return iROLE_DEV5;
			else if ( role.equals(ROLE_DEV6)) return iROLE_DEV6;
			else if ( role.equals(ROLE_DEV7)) return iROLE_DEV7;
			else return -1;
		}
		else if ( role.equals(ROLE_HIDDEN)) return iROLE_HIDDEN;
		else return -1;
	}	
	
	public static boolean roleIsMinHidden( String role ) {
		boolean reto;
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe == -1)reto=false;
		else if ( hoehe >= iROLE_HIDDEN ) reto=true;
		else reto=false;
		// System.out.println("angefragt: roleIsMinHidden: "+role+"="+hoehe+" - "+reto);
		return reto;			
	}		
	public static boolean roleIsTestUp(String role) {
		boolean reto;
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe >= iROLE_TEST )reto=true;
		else reto=false;
		// System.out.println("angefragt: roleIsTestUp: "+role+"="+hoehe+" - "+reto);
		return reto;		
	}	
	public static boolean roleIsAdminUp(String role) {
		boolean reto;
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe >= iROLE_ADMIN )reto=true;
		else reto=false;
		// System.out.println("angefragt: roleIsAdminUp: "+role+"="+hoehe+" - "+reto);
		return reto;		
	}	
	public static boolean roleIsExpertUp(String role) {
		boolean reto;
		int hoehe = getRoleHeightInternal(role);
		if ( hoehe >= iROLE_EXPERT )reto=true;
		else reto=false;
		// System.out.println("angefragt: roleIsExpertUp: "+role+" - "+reto);
		return reto;
	}	
	public static int getMaxRollenHoehe(String allowedRoleFlags ) throws Exception {
		if ( allowedRoleFlags == null ) return iROLE_HIDDEN;
		String founds[] = allowedRoleFlags.split("\\s+");
		int reto = -1;
		for ( String found: founds ) {
			if ( found.length()> 0) {
				//System.out.println("on: "+found);
				int dieser = getRoleHeightDefaultFail(found);
				if ( dieser > reto )
					reto = dieser;
			}
		}
		return reto;
		
	}
	public static String[] getRoleArray(int maxRollenHoehe, String allowedRoleFlags ) {
		ArrayList<String> roles1 = new ArrayList<String>();
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_PUBLIC,iROLE_PUBLIC); 
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_USER,iROLE_USER);
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_USER2,iROLE_USER2);

		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_EXPERT,iROLE_EXPERT); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_EXPERT2,iROLE_EXPERT2); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_EXPERT3,iROLE_EXPERT3); 
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_BUSINESS,iROLE_BUSINESS); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_BUSINESS2,iROLE_BUSINESS2);		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_BUSINESS3,iROLE_BUSINESS3); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_BUSINESS4,iROLE_BUSINESS4);		
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_ADMIN,iROLE_ADMIN);
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_ADMIN2,iROLE_ADMIN2); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_ADMIN3,iROLE_ADMIN3);		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_ADMIN4,iROLE_ADMIN4); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_ADMIN5,iROLE_ADMIN5);	
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST,iROLE_TEST);				
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST2,iROLE_TEST2);
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST3,iROLE_TEST3); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST4,iROLE_TEST4);		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST5,iROLE_TEST5); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_TEST6,iROLE_TEST6);	
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV,iROLE_DEV);	
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV2,iROLE_DEV2);				
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV3,iROLE_DEV3);
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV4,iROLE_DEV4); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV5,iROLE_DEV5);		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV6,iROLE_DEV6); 
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_DEV7,iROLE_DEV7);	
		
		addArraySize(roles1,maxRollenHoehe,allowedRoleFlags,ROLE_HIDDEN,iROLE_HIDDEN);
		
		String[] roles = roles1.toArray(new String[roles1.size()]);
		return roles;
	}
	private static void addArraySize(ArrayList<String> roles1,
								int maxRollenHoehe, String allowedRoleFlags, 
							 String thatRoleName, int thatRoleHeight ) {
		
		if ( thatRoleHeight <= maxRollenHoehe && allowedRoleFlags==null )
			roles1.add(thatRoleName);
		else if ( thatRoleHeight <= maxRollenHoehe && hasFlag(allowedRoleFlags, thatRoleName) )
			roles1.add(thatRoleName);

	}
	
	private static boolean hasFlag( String evaluate, String flagname ) {

		if ( evaluate != null ) {
			if ( 	evaluate.equals(flagname) || 
					evaluate.indexOf(" "+flagname+" ") > -1 || 
							evaluate.startsWith(flagname+" ") || 
							evaluate.endsWith(" "+flagname))
				return true;			
		}
		return false;
	}	
}
