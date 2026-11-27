package at.nicolas.x.web;

public class DbLoginResponse {
	public boolean isHasAccess() {
		return hasAccess;
	}
	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size*1000*1000;
	}
	private boolean hasAccess = false;
	private long size = -1;
	

}
