package at.nicolas.x.int_specs;

public class SurrSelInfo {
	
	public boolean enterAllManually = false;
	public boolean enterConfirmOnly = false;
	public boolean chooseTk = false;
	public boolean hasAppProv = false;
	public boolean hasProProv = false;
	
	public String appProvPath = null;
	public String proProvPath = null;
	public String getDescription() {
		return ( "hasAppProv=["+hasAppProv+"] " +
				 ", hasProProv=["+hasProProv+"] " +
				 ", appProvPath=["+appProvPath+"] " +
				 ", proProvPath=["+proProvPath+"] " +
				 ", enterAllManually=["+enterAllManually+"] " +
				 ", chooseTk=["+chooseTk+"] " );				 
	}
	public SurrSelInfo() {
		
	}
	public SurrSelInfo(
			String mybits,
			String prov_app,
			String prov_pro) {
		if ( mybits != null ) {
			enterAllManually = Commons.hasFlag(mybits, "enterAllManually");
			enterConfirmOnly = Commons.hasFlag(mybits, "enterConfirmOnly");
			// enterConfirmOnly = true;
			if ( enterConfirmOnly )
				enterAllManually = false;
			chooseTk = Commons.hasFlag(mybits, "chooseTk");
			hasAppProv = Commons.hasFlag(mybits, "hasAppProv");
			hasProProv  = Commons.hasFlag(mybits, "hasProProv");	
		}
		if ( hasAppProv ) {
			appProvPath = prov_app;
			if ( appProvPath == null )
				appProvPath = "";			
		}
		if ( hasProProv ) {
			proProvPath = prov_pro;
			if ( proProvPath == null )
				proProvPath = "";			
		}		
	}

}
