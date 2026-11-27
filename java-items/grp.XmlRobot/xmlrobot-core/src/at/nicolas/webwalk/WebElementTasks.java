// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.jdom2.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class WebElementTasks implements ITask {
	// WeTasks
		
	
	private static String task_id ="welement";	
	private static String task_logo ="task 'welement' ";
	
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		/*
		task_id = id;
		rh.getLogConnector().info("Called staticTaskdef of at.nicolas.webwalk.NicsWeTasks with id '"+id+"'", 
				this);
		rh.getLogConnector().info("  Element="+Commons.toText(eTaskdef), this);
		if ( eTaskdef.getAttribute("coop_type") != null ) {
			NicsWe.setCoopType(eTaskdef.getAttributeValue("coop_type"));
			rh.getLogConnector().warn("did set coop_type for NicsWe to '"+eTaskdef.getAttributeValue("coop_type")+"'", this);
		}
		*/
		
	}

	@Override
	public Parameters execute(Element eExecute, IRequestHandler rh) throws Exception {
		if ( eExecute.getAttribute("act") == null ) {
			throw new Exception(task_id+" needs attribute @act (...)");
		}
		if ( eExecute.getAttribute("sleep") != null ) {
			Thread.sleep(Commons.string2int(eExecute.getAttributeValue("sleep")));
		}		
		/*[requo-timer]*/
		if ( eExecute.getAttribute("id") == null ) {
			throw new Exception(task_id+" needs attribute @id");
		}	
		
		String action = eExecute.getAttributeValue("act");
		String id = eExecute.getAttributeValue("id");
		
		ICoop nicsWeCoop = rh.getNo(id, NicsWe.coop_type, null);
		if ( nicsWeCoop == null) {
			System.out.println("rh.getNo hat nix gebracht and will buildMissingElement "+id+ " "+ NicsWe.coop_type);
			nicsWeCoop = WebDriverTasks.buildMissingWebElement(id, rh);
		}
		
		// NicsWe nicsWe = (NicsWe)nicsWeCoop;
		NicsWe nicsWe = (NicsWe)nicsWeCoop;
		if ( nicsWe == null ) {
			System.out.println("kein nicsWe "+id+ " "+ NicsWe.coop_type);
			throw new Exception("Could not locate  "+id+ " "+ NicsWe.coop_type);
		}
		
		if ( action.equals("clear")){
			nicsWe.clear();
			
		} else if ( action.equals("click")){
			nicsWe.click();				
			
		} else if ( action.equals("send_keys")){
			nicsWe.sendKeys(eExecute.getAttributeValue("keys"));	
			
		} else if ( action.equals("send_keys_control")){
			nicsWe.sendKeys(Keys.CONTROL,eExecute.getAttributeValue("keys"));
			
		} else if ( action.equals("send_keys_alt")){
			nicsWe.sendKeys(Keys.ALT,eExecute.getAttributeValue("keys"));	
			
		} else if ( action.equals("send_keycode")){
			//System.out.println("-!!   !!  --- send_keycode with keycode="+eExecute.getAttributeValue("keycode"));
			String keycode=eExecute.getAttributeValue("keycode");
			if ( keycode.equals("TAB")) nicsWe.sendKeys(Keys.TAB);
			else	
				throw new Exception(task_logo+" with @action '"+action+"' has unknown attribute @keycode="+keycode);
			
		} else if ( action.equals("submit")){
			nicsWe.submit();				
			
		} else if ( action.equals("closeit")){
			nicsWe.autoClose();
			rh.removeNo(id, NicsWd.coop_type, nicsWe);
			
		} else if ( action.equals("mouse_over")){
			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
			
			Actions builder = new Actions(driver);
			Action mouseOverHome = builder
					.moveToElement(nicsWe.we)
					.build();
			mouseOverHome.perform();			
			
		}  else if ( action.equals("control_click")){
			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
			
			Actions builder = new Actions(driver);
			Action mouseOverHome = builder
					.keyDown(Keys.LEFT_CONTROL)
					.click(nicsWe.we)
					.keyUp(Keys.LEFT_CONTROL)
					.build();
			mouseOverHome.perform();			
		
		} else if ( action.equals("switch_to_this_frame")){
			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
			driver.switchTo().frame(nicsWe.we);			
	
		} else if ( action.equals("scroll")){

			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
			
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					nicsWe.we);
			Thread.sleep(500); 

		} else if ( action.equals("light")){

			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
	
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='"+nicsWd.getLightBorder()+"';",
					nicsWe.we);

		}  else if ( action.equals("screenshot")){

			NicsWd nicsWd = (NicsWd)rh.getNo(
					getWebDriverId(eExecute), 
					NicsWd.coop_type, null);
			WebDriver driver = nicsWd.driver;
			// fotografieren
			WebElement ele = nicsWe.we;

			// Get entire page screenshot
			// File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// BufferedImage  fullImg = ImageIO.read(screenshot);
			// System.out.println("doing 'screenshot': did read BufferedImage: fullImg");
			// System.out.println("screenshotfile="+screenshot.getAbsoluteFile());
			// System.out.println("fullImg.getWidth()="+fullImg.getWidth());
			// System.out.println("fullImg.getHeight()="+fullImg.getHeight());
	
			// Get the location of element on the page
			// Point point = ele.getLocation();
			// System.out.println("point.getX()="+point.getX());
			// System.out.println("point.getY()="+point.getY());

			// Get width and height of the element
			// int eleWidth = ele.getSize().getWidth();
			// int eleHeight = ele.getSize().getHeight();
			// System.out.println("eleWidth=ele.getSize().getWidth()="+eleWidth);
			// System.out.println("eleHeight=ele.getSize().getHeight()="+eleHeight);

			// Crop the entire page screenshot to get only element screenshot
			// eleWidth, eleHeight);
			//ImageIO.write(fullImg,)
			int variante = 0;
			if ( variante == 0 ) {
				
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
						nicsWe.we);
				Thread.sleep(500); 
				
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript("window.scrollBy("+eExecute.getAttributeValue("x")+
						","+eExecute.getAttributeValue("y")+")", "");
				
			    File ss = ele.getScreenshotAs(OutputType.FILE);
			    
			    
				String screenshotLocationFn= Commons.fileClassic(eExecute.getAttributeValue("file"));
				
				// System.out.println("file screenshotLocationFn="+screenshotLocationFn);
				
				
				FileObject file = CoreBuilder.fsm.resolveFile(eExecute.getAttributeValue("file"));
				long opnum = rh.getSitzung().grantsUpdateFileP1(file,"write_screenshot");
				FileUtils.copyFile(ss, new File(screenshotLocationFn));	
				rh.getSitzung().grantsUpdateFileP2(file,opnum);	
				
				
			}/*  else if ( variante == 1 ) {
				String screenshotLocationFn= Commons.fileClassic(eExecute.getAttributeValue("file"));
				
				System.out.println("file screenshotLocationFn="+screenshotLocationFn);
				
				
				ImageIO.write(fullImg, "png", new File(screenshotLocationFn));
				BufferedImage eleScreenshot= fullImg.getSubimage(0, 0,
						fullImg.getWidth(), fullImg.getHeight());
				
			} else if (variante == 2 ){
			BufferedImage eleScreenshot= fullImg.getSubimage(point.getX()+1, point.getY()+1,
					eleWidth-2, eleHeight-2);
					
			System.out.println("doing 'screenshot': did read BufferedImage: eleScreenshot");
			System.out.println("screenshot="+screenshot);
			ImageIO.write(eleScreenshot, "png", screenshot);
			System.out.println("did ImageIO.write");
			
			String screenshotLocationFn= Commons.fileClassic(eExecute.getAttributeValue("file"));
			File screenshotLocation = new File(screenshotLocationFn);
			System.out.println("file attribute="+eExecute.getAttributeValue("file"));
			System.out.println("file screenshotLocationFn="+screenshotLocationFn);
				
			} */
			
			
						  
		} /*  else if ( action.equals("screenshot1")){
			System.out.println("doing 'screenshot1'");
			if ( eExecute.getAttribute("webdriver_id") == null)
				throw new Exception("need webdriver_id");

			NicsWd nicsWd = (NicsWd)rh.getNo(
					eExecute.getAttributeValue("webdriver_id"), 
					NicsWd.coop_type, null);

			// fotografieren
			File fullScreen = ((TakesScreenshot) nicsWd.driver).getScreenshotAs(OutputType.FILE);

			int width = nicsWe.we.getSize().getWidth();
			int height = nicsWe.we.getSize().getHeight(); 
			
			 Point point = nicsWe.we.getLocation();
			 int xcord = point.getX();
			 int ycord = point.getY();
			  
			 BufferedImage img = ImageIO.read(fullScreen); 
			 
			 BufferedImage dest = img.getSubimage(xcord, ycord, width, height);
			 ImageIO.write(dest, "png", fullScreen);
			 
			 // FileUtils.copyFile(fullScreen, new File(eExecute.getAttributeValue("file")));
			  
		} */ else
			throw new LangException(task_logo+" attribute @action="+action+" is unknown");			
	
		
		if ( eExecute.getAttribute("and_sleep") != null ) {
			Thread.sleep(Commons.string2int(eExecute.getAttributeValue("and_sleep")));
		}
		return null;
	}
	
	private String getWebDriverId ( Element eExecute ) throws Exception {
		String webdriverid = null ;
		
		if ( eExecute.getAttribute("webdriver_id") == null) {
			if ( WebDriverTasks.wd_default_id != null)
				webdriverid = WebDriverTasks.wd_default_id;
			else 
				throw new Exception("did not find @webdriver_id or default-webdriver");
		} else {
			webdriverid = eExecute.getAttributeValue("webdriver_id");
		}
		return webdriverid;	
	}


}