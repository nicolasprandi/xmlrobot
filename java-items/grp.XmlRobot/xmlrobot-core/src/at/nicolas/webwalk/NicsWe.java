// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnector;

public class NicsWe implements ICoop, WebElement {


	WebElement we = null;
	ILogConnector logConnector;

	NicsWe (WebElement _we) {
		we = _we;
	}
	public static String coop_type ="at.nicolas.webwalk.we";	
	public static void setCoopType ( String _coop_type ) {
		coop_type = _coop_type;		
	}	
	
	@Override
	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		we.clear();

	}

	@Override
	public void click() {
		we.click();
		

	}

	@Override
	public WebElement findElement(By arg0) {
		// TODO Auto-generated method stub
		return we.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		// TODO Auto-generated method stub
		return we.findElements(arg0);
	}

	@Override
	public String getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return we.getAttribute(arg0);
	}
	
	public int getSizeX() {
		return we.getSize().width;
	}	
	
	public int getSizeY() {
		return we.getSize().height;
	}	
	
	public int getLocationX() {
		return we.getLocation().x;
	}	
	
	public int getLocationY() {
		return we.getLocation().y;
	}	

	@Override
	public String getCssValue(String arg0) {
		// TODO Auto-generated method stub
		return we.getCssValue(arg0);
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return we.getTagName();
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return we.getText();
	}

	@Override
	public boolean isDisplayed() {
		// TODO Auto-generated method stub
		return we.isDisplayed();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return we.isEnabled();
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return we.isSelected();
	}

	@Override
	public void sendKeys(CharSequence... arg0) {
		we.sendKeys(arg0);

	}
	


	@Override
	public void submit() {
		we.submit();

	}

	@Override
	public void autoClose() {

	}
	

}