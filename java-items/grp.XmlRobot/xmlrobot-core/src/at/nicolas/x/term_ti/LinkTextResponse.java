package at.nicolas.x.term_ti;

public class LinkTextResponse {
	private String linktext = "";
	private String classes = "";
	
	private String openTag = "<a ";
	private String closeTag = "</a>";	
	
	private boolean isButton = false;

	protected boolean isButton() {
		return isButton;
	}
	protected void setIsButton(boolean isButton) {
		this.isButton = isButton;
	}
	
	protected String getOpenTag() {
		return openTag;
	}
	protected void setOpenTag(String openTag) {
		this.openTag = openTag;
	}
	protected String getCloseTag() {
		return closeTag;
	}
	protected void setCloseTag(String closeTag) {
		this.closeTag = closeTag;
	}
	protected String getLinktext() {
		return linktext;
	}
	protected void setLinktext(String linktext) {
		this.linktext = linktext;
	}
	protected String getClasses() {
		return classes;
	}
	protected String getHtmlClasses() {
		if ( classes.length() > 0 ) {
			return " class=\""+classes+"\" ";
			
		} else 
			return "";

	}	
	
	protected String getHtmlClasses(String additionalClasses ) {
		String ret = null;
		if ( classes.length() > 0 ) {
			if ( additionalClasses == null )
				ret =  " class=\""+classes+"\" ";
			else 
				ret =  " class=\""+classes+" "+additionalClasses+"\" ";
			
		} else {
			if ( additionalClasses == null )
				ret =  "";
			else 
				ret =  " class=\""+additionalClasses+"\" ";
		}
		// System.out.println("Gave back classes: "+ret);
		return ret;

	}	
	protected void setClasses(String classes) {
		this.classes = classes;
	}


}
