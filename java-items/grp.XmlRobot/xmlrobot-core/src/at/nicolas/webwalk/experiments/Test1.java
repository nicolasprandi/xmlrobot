// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk.experiments;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Test1 {

	void Test1 () {
		
	}
	public void go () throws InterruptedException {
		

		// TODO Auto-generated method stub
		
		// Create a new instance of the Firefox driver
		//System.setProperty("webdriver.gecko.driver", "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.App-Development\\app.WebWalk\\nobackup\\geckodriver.exe");
		System.setProperty("webdriver.gecko.driver", "C:\\ProgrammeSimple\\geckodriver-v0.26.0-win32\\geckodriver.exe");
		
		WebDriver driver = new FirefoxDriver();
		
        //Launch the Online Store Website
		driver.get("https://www.martin-huber.at/");
 
        // Print a Log In message to the screen
        System.out.println("Successfully opened the website www.nicolas.at");
 
		//Wait for 5 Sec
		Thread.sleep(5000);
		
		List<WebElement> links = driver.findElements(By.tagName("a"));
		
		for (int i = 0; i<links.size(); i=i+1)
			 
		{
			WebElement we = links.get(i);
			// System.out.println("a-text="+we.getText());
	    	if ( we.isDisplayed()) {
	    		System.out.println("Class-Name:"+ (we.getClass()).getName() );
	    		if ( we.getText().equals("Linked In")) {
	    			we.click();
	    			i = links.size()+2;
	    			
	    		}

	    	} else {
	    		System.out.println("NOT Displayed");
	    	} 
		}
		
		Thread.sleep(5000);
		WebElement we1 = driver.findElement(By.xpath(".//a[text()='Einloggen']"));
		printElements(driver, "a");
		
		/*
	    for ( WebElement we: allOptions) { 
	    	System.out.println("----");
	    	if ( we.isDisplayed()) {
	    	System.out.println("Class-Name:"+ (we.getClass()).getName() );

	    	} else {
	    		System.out.println("NOT Displayed");
	    	}
	    } */
	    
		// WebElement leaveAMessage = driver.findElement(By.name("Leave a Message"));
		// leaveAMessage.click();
		
        // Close the driver
        driver.quit();				
		
	}

	public static void main(String[] args) throws Exception {
		Test1 test1 = new Test1();
		test1.go();

	}
	private void printElements(WebDriver driver, String tagname) {
		System.out.println("list for tags:"+tagname);
		
		List<WebElement> links = driver.findElements(By.tagName(tagname));
		
		for (int i = 0; i<links.size(); i=i+1)
			 
		{
			WebElement we = links.get(i);
			System.out.println("a-text="+we.getText());
	    	if ( we.isDisplayed()) {
	    		System.out.println("Class-Name:"+ (we.getClass()).getName() );

	    	} else {
	    		System.out.println("NOT Displayed");
	    	} 
		}		
	}	
	

}