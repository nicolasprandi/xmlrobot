package at.nicolas.x.WebVfs;

public abstract class ReqRes {

	public ReqRes() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void treatException(XrRqRpEx answerEx);
	
	public abstract void treatTiResponse(XrRqRpTi answerTi);
	public abstract void treatTkResponse(XrRqRpTk answerTk);
	//public abstract void performMarkSelection(String marker);

}
