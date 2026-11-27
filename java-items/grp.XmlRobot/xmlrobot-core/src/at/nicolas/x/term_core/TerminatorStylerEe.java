package at.nicolas.x.term_core;

public class TerminatorStylerEe implements ITerminatorStyler{

	public TerminatorStylerEe() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHref(String refcode) {
		// TODO Auto-generated method stub
		return " href='click?link="+refcode+"' ";
	}
	
	public String getHrefButton(String refcode ) {
		return " onclick=\"window.location.href='/"+refcode+"'\"";
	}	

}
