package at.nicolas.webwalk;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;

public class NicsTab implements ICoop {
	
	public static String coop_type ="at.nicolas.webwalk.tab";	
	public static void setCoopType ( String _coop_type ) {
		coop_type = _coop_type;		
	}		
	
	WebDriver.TargetLocator tl ;
	
	NicsTab (WebDriver.TargetLocator tl  ) throws Exception {
		this.tl = tl;
		/*[requo-timer]*/
	
	}	
	public WebElement activeElement() {
		return tl.activeElement();
	}
	
	public WebDriver frame(WebElement frameElement) {
		return tl.frame(frameElement);
	}		
	
	public WebDriver parentFrame() {
		return tl.parentFrame();
	}		
	
	public WebDriver window(String nameOrHandle) {
		return tl.window(nameOrHandle);
	}		
	
	public Alert alert() {
		return tl.alert();
	}
	public WebDriver defaultContent() {
		return tl.defaultContent();
	}
	public WebDriver frameByIndex(int index) {
		return tl.frame(index);
	}	
	
	public WebDriver frameByNameOrId(String nameOrId) {
		return tl.frame(nameOrId);
	}	
	
	

	@Override
	public void autoClose() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void createNewTabCoop(
			NicsTab nicsTab, 
			String coopId,
			IRequestHandler rh ) throws Exception {

		
		rh.putNo(	coopId, 
				NicsTab.coop_type, 
				"WebTab", 
				nicsTab, 
				null);
	
	}	
	
	public static void createNewTabCoop(
			WebDriver.TargetLocator tl, 
			String coopId,
			IRequestHandler rh ) throws Exception {
		
		NicsTab nt = new NicsTab(tl);

		
		rh.putNo(	coopId, 
				NicsTab.coop_type, 
				"WebTab", 
				nt, 
				null);
	
	}		

}
