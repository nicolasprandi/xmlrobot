package at.nicolas.x.WebVfs;

public class XrRqRp {

	public XrRqRp() {
		// TODO Auto-generated constructor stub
	}
	public String getType() {
		if ( this instanceof XrRqRpEx)
			return "-- Exception--";
		else if ( this instanceof XrRqRpTk)
			return "-- Tk --";	
		else if ( this instanceof XrRqRpTi)
			return "-- Ti --";
		else
			return "-- ? --";
	}

}
