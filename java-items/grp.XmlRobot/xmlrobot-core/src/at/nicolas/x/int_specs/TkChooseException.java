package at.nicolas.x.int_specs;

import at.nicolas.x.int_interpreter.Sp;

public class TkChooseException extends Exception {

	public String choosenVarType;
	public String choosenValue;
	public String actionType;
	public TkChooseException() {
		// TODO Auto-generated constructor stub
	}
	private void say(String x) {
		Sp.p("TkChooseException: ");
	}
	public TkChooseException(String message,String choosenVarType, String choosenValue,
			String actionType) {
		super(message);
		this.choosenVarType = choosenVarType;
		this.choosenValue = choosenValue;
		this.actionType = actionType;
		say("init, type="+choosenVarType+", value="+choosenValue+", actiontype="+actionType);
		

	}

	public TkChooseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TkChooseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TkChooseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
