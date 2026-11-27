// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By; //
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnector;

public class NicsWd implements WebDriver, ICoop {


	public  HashMap<String, WebElement> register = new HashMap<String, WebElement>();
	
	 
	
	// Thats a logconnector used for logic things
	ILogConnector logConnector;

	public static String coop_type ="at.nicolas.webwalk.wd";	
	public static void setCoopType ( String _coop_type ) {
		coop_type = _coop_type;		
	}	
	public WebDriver driver = null;
	private String lightBorder = "3px solid red";
	String getLightBorder() {
		return lightBorder;
	}
	void setLightBorder(String lightBorder) {
		this.lightBorder = lightBorder;
	}	
	NicsWd (String driverImplType, 
			String firefoxDriverExe,
			String chromeBinary,
			String chromeDriver,
			String chromeOptionHeadless,
			String chromeOptionDisableGpu, 
			String edgeDriverExe,
			int implicitWaitsSeconds) throws Exception {
		/*[requo-timer]*/
		/*[requo-customer]*/
		if ( driverImplType.equals("firefox")) {		
			if (firefoxDriverExe!=null && firefoxDriverExe.length()>0 ) {
				
				System.setProperty("webdriver.gecko.driver", Commons.fileClassic(firefoxDriverExe));		
			}
					
			driver = new FirefoxDriver();
		} else if ( driverImplType.equals("chrome")) {			
			
				if (chromeDriver!=null && chromeDriver.length()>0 ) {					
					System.setProperty("webdriver.chrome.driver", Commons.fileClassic(chromeDriver));		
				} 	
				
				ChromeOptions options = new ChromeOptions();
				if ( (chromeOptionHeadless!= null && chromeOptionHeadless.length()>0 && chromeOptionHeadless.equals("1")) ||
					(chromeOptionDisableGpu!= null && chromeOptionDisableGpu.length()>0  && chromeOptionDisableGpu.equals("1") ) ) {
					// mit options
					
					if ( chromeOptionHeadless!= null && chromeOptionHeadless.length()>0  && chromeOptionHeadless.equals("1") )
						options.addArguments("--headless");
					if ( chromeOptionDisableGpu!= null && chromeOptionDisableGpu.length()>0   && chromeOptionDisableGpu.equals("1") )
						options.addArguments("--disable-gpu");										
				}
				if (chromeBinary!=null && chromeBinary.length()>0 ) {	
					options.setBinary(Commons.fileClassic(chromeBinary));	
				} 
				driver = new ChromeDriver(options);
			
				
		} else if ( driverImplType.equals("edge")) {		
			if (edgeDriverExe!=null && edgeDriverExe.length()>0 ) {
				
				System.setProperty("webdriver.edge.driver", Commons.fileClassic(edgeDriverExe));		
			}
					
			driver = new EdgeDriver();
		} else {
			throw new Exception("Unbekannter Driver driverImplType: "+firefoxDriverExe);
		}
		if ( implicitWaitsSeconds > 0 ) {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitsSeconds));
			System.out.println("Did set implicit-wait-seconds to "+implicitWaitsSeconds+" says NicsWd");
		}
	}
	/*
	CoopWd (NicsWd nicsWd, ILogConnector logConnectorLogic) {
		this.nicsWd = nicsWd;
		this.logConnectorLogic = logConnectorLogic;
	}	
	*/
	@Override
	public void autoClose() {
		quit();

	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		quit();
		/*[requo-customer]*/
	}

	@Override
	public WebElement findElement(By arg0) {
		// TODO Auto-generated method stub
		return driver.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		return driver.findElements(arg0);
	}

	@Override
	public void get(String arg0) {
		this.driver.get(arg0);

	}

	@Override
	public String getCurrentUrl() {
		return this.driver.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return this.driver.getPageSource();
	}

	@Override
	public String getTitle() {
		return this.driver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return this.driver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		// TODO Auto-generated method stub
		return this.driver.getWindowHandles();
	}

	@Override
	public Options manage() {
		// TODO Auto-generated method stub
		return this.driver.manage();
	}

	@Override
	public Navigation navigate() {
		// TODO Auto-generated method stub
		return this.driver.navigate();
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		this.driver.quit();
	}

	@Override
	public TargetLocator switchTo() {
		// TODO Auto-generated method stub
		return this.driver.switchTo();
	}


}