package at.nicolas.x.WebVfs;

public abstract class GuiResponder {

	public GuiResponder() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void treatException(XrRqRpEx answerEx);
	
	public abstract void treatTiResponse(XrRqRpTi answerTi);
	public abstract void treatTkResponse(XrRqRpTk answerTk);

}
