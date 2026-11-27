package at.nicolas.x.int_interpreter;

public class Eden {
	
	private String homeFs;
	private String homeMem;
	public void Eden(String homeFs,String homeMem) {
		this.homeFs = homeFs;
		this.homeMem = homeMem;		
	}
	public void Eden() {
		
	}
	public final String getHomeFs() {
		return homeFs;
	}
	public final String getHomeMem() {
		return homeMem;
	}
	

}
