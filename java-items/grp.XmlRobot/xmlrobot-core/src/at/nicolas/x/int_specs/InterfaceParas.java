package at.nicolas.x.int_specs;

public class InterfaceParas {
	public String overwriteSystemp =null;
	public ClassLoader overwriteClassloader=null;
	
	public static InterfaceParas getEmpty() {
		InterfaceParas empty = new InterfaceParas();
		return empty;

	}
	
	public static InterfaceParas get(String overwriteSystemp,ClassLoader overwriteClassloader) {
		InterfaceParas neu = new InterfaceParas();
		neu.setOverwriteSystemp(overwriteSystemp);
		neu.setOverwriteClassloader(overwriteClassloader);
		return neu;

	}	

	public String getOverwriteSystemp() {
		return overwriteSystemp;
	}

	public void setOverwriteSystemp(String overwriteSystemp) {
		this.overwriteSystemp = overwriteSystemp;
	}

	public ClassLoader getOverwriteClassloader() {
		return overwriteClassloader;
	}

	public void setOverwriteClassloader(ClassLoader overwriteClassloader) {
		this.overwriteClassloader = overwriteClassloader;
	}

}
