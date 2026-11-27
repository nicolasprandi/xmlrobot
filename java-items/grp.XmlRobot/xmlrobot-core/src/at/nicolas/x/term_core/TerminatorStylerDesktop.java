package at.nicolas.x.term_core;

public class TerminatorStylerDesktop implements ITerminatorStyler{

	public TerminatorStylerDesktop() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHref(String refcode) {
		// TODO Auto-generated method stub
		return " href='"+refcode+"' ";
	}
	public String getHrefButton(String refcode ) {
		return getHref( refcode);
	}

}
