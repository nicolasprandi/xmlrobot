package at.nicolas.x.web;

public class StorageMessage {
	
	private String message = null;
	StorageMessage(String _message ) {
		message = _message;
	}
	String getMessage () {
		return message;
	}

}
