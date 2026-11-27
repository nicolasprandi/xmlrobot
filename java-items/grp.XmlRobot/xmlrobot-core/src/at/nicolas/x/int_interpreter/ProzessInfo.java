package at.nicolas.x.int_interpreter;

import java.util.Date;

public class ProzessInfo {
	private StoppableThread thread; 
	private String typetitle;
	private String state;
	private String details;
	private String username;	
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public StoppableThread getThread() {
		return thread;
	}
	public void setThread(StoppableThread thread) throws Exception {
		if (thread == null )
			throw new Exception("Stoppable thread is null ");
		this.thread = thread;
	}
	public String getTypetitle() {
		return typetitle;
	}
	public void setTypetitle(String typetitle) {
		this.typetitle = typetitle;
	}
	public synchronized String getState() {
		return thread.getStateStoppable();
	}

	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


}
