// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. All rights reserved.
package at.nicolas.webwalk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class WebDriverTasks implements ITask {
	// WdTasks
	private static String task_id ="wdriver";
	private static String task_logo ="task 'wdriver' ";
	
	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		
	}
	public static String wd_default_id = null;
	public static ICoop buildMissingWebElement ( String elementCoopId, IRequestHandler rh ) throws Exception {

		if ( wd_default_id == null )
			throw new Exception("buildMissingWebElement didnt find a default driver");
		NicsWd nicsWd = (NicsWd)rh.getNo(wd_default_id, NicsWd.coop_type, null);	
		if ( nicsWd == null )
			throw new Exception("buildMissingWebElement didnt find the default-driver ["+wd_default_id+"]");		
		WebElement thewe = nicsWd.register.get(elementCoopId);
		if ( thewe == null )
			throw new Exception("buildMissingWebElement didnt find a pregegistered WebElement ["+elementCoopId+"]");			
		NicsWe newNicsWe = new NicsWe(thewe);
		rh.putNo(	elementCoopId, 
				NicsWe.coop_type, 
				"WebElement", 
				(ICoop)newNicsWe, 
				null);
		return (ICoop)newNicsWe;
		
	}
	private String getHtmlByHow (Element eExecute, NicsWd nicsWd, String action) throws Exception {
		String html;
		String how = "pagesource";
		if ( eExecute.getAttribute("how") != null ) 
			how = eExecute.getAttributeValue("how");				
		if ( how.equals("pagesource")) {
			html = nicsWd.getPageSource();					
		} else if ( how.equals("javascript")) {
			JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
			html = (String)jsx.executeScript(
					"return arguments[0].outerHTML",
					nicsWd.driver.findElement(By.tagName("html")));					
		} else
			throw new Exception (task_logo + " with action '"+action+"' needs attribute @how which may be [pagesource] or [javascript] but it is: "+how);
		return html;
	}
	@Override
	public Parameters execute(Element eExecute, IRequestHandler rh) throws Exception {
		if ( eExecute.getAttribute("act") == null ) {
			throw new Exception(task_logo+" needs attribute @act (open, next, close, ...)");
		}
		/*[requo-timer]*/
		if ( eExecute.getAttribute("sleep") != null ) {
			Thread.sleep(Commons.string2int(eExecute.getAttributeValue("sleep")));
		}
				
		String action = eExecute.getAttributeValue("act");
		
		String id = null;
		if ( eExecute.getAttribute("id") != null ) {
			id=eExecute.getAttributeValue("id");
		} else {
			if  ( wd_default_id != null ) {
				id = wd_default_id;
			} else if (action.equals("set_default_id")) ;
			else {
				throw new Exception("Coop webdriver needs attribute id or default_id");
			}
		}
		
		
		if ( action.equals("open")) {

			// Creating the pure Object
			
			int implicitWait = 0;
			if (eExecute.getAttribute("implicit_wait_seconds") != null ) {
				implicitWait = Commons.string2int(eExecute.getAttributeValue("implicit_wait_seconds"));
				System.out.println("Opening WebDriver called with implicit_wait_seconds="+implicitWait+" says open-act");
			}
			NicsWd nicsWd = new NicsWd(
					eExecute.getAttributeValue("driver_type"),
					eExecute.getAttributeValue("driver_firefox"),
					eExecute.getAttributeValue("conf_chrome_binary"),
					eExecute.getAttributeValue("conf_chrome_driver"),
					eExecute.getAttributeValue("conf_driver_headless_chrome"),
					eExecute.getAttributeValue("conf_driver_disablegpu_chrome"),
					eExecute.getAttributeValue("conf_edge_driver"),
					implicitWait);
			
					// Integer.parseInt(eExecute.getAttributeValue("count")));
			
			// Adding the cooop id to the logic log connector
			// lc.setLogicHeader(id);
			
			rh.putNo(id, NicsWd.coop_type, "WebDriver", nicsWd, this);
			/*
			cycloCoop.logConnectorLogic.logicStart(
							"(a)",
							"Opened with count "+
							Integer.parseInt(eExecute.getAttributeValue("count")));
							*/
			/*[requo-timer]*/
			
		} else if ( action.equals("set_default_id")) {
			if ( eExecute.getAttribute("default_id") == null )
					throw new Exception("webdriver action set_default_id needs attribte default_id, make it blank to errase");
			String para_default_id = eExecute.getAttributeValue("default_id");
			if ( para_default_id == null || para_default_id.equals(""))
				wd_default_id = null;
			else
				wd_default_id = para_default_id;
			
		} else {
			NicsWd nicsWd = (NicsWd)rh.getNo(id, NicsWd.coop_type, this);
			if ( action.equals("get_tab")){
				if ( eExecute.getAttribute("new_tab_id") != null ) {
					WebDriver wd = nicsWd.driver;
					
					WebDriver.TargetLocator x = (WebDriver.TargetLocator)wd;
					NicsTab.createNewTabCoop(x, eExecute.getAttributeValue("new_tab_id"), rh);
					
				} else {
					throw new LangException(task_logo+" with @action 'get_tab' needs an attribute @new_tab_id");
				}
				
			}else if ( action.equals("new_tab")){
				if ( eExecute.getAttribute("new_tab_id") != null ) {
					WebDriver wd = nicsWd.driver;
					
					WebDriver.TargetLocator x = wd.switchTo();
					NicsTab.createNewTabCoop(x, eExecute.getAttributeValue("new_tab_id"), rh);
					
				} else {
					throw new LangException(task_logo+" with @act 'new_tab' needs an attribute @new_tab_id");
				}
				
			} else if ( action.equals("get")){
				nicsWd.get(eExecute.getAttributeValue("url"));
			} else if ( action.equals("implicitly_wait") ){
				Duration duration = null;
				if ( eExecute.getAttribute("seconds") != null) 
					duration = Duration.ofSeconds(Commons.string2int(eExecute.getAttributeValue("seconds")));
				else if ( eExecute.getAttribute("millis") != null)
					duration = Duration.ofMillis(Commons.string2int(eExecute.getAttributeValue("millis")));
				else
					throw new LangException(task_logo+" with @act 'implicitly_wait' has no duration (seconds or millis)");

				nicsWd.manage().timeouts().implicitlyWait(duration);
				
			} else if ( action.equals("back")){				
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript("window.history.go(-1)");				
				// nicsWd.navigate().back();
			} else if ( action.equals("back")){							
				nicsWd.navigate().back();
			} else if ( action.equals("forward")){
				nicsWd.navigate().forward();
			}  else if ( action.equals("refresh")){
				nicsWd.navigate().refresh();
			}  else if ( action.equals("to")){
				nicsWd.navigate().to(eExecute.getAttributeValue("url"));
			}  else if ( action.equals("set_light_border")){
				nicsWd.setLightBorder(eExecute.getAttributeValue("light_border"));
			
			} else if ( action.equals("switch_frame")){
				if ( eExecute.getAttribute("by_index") != null ) {
					nicsWd.driver.switchTo().frame(Commons.string2int(eExecute.getAttributeValue("by_index")));					
				} else if ( eExecute.getAttribute("by_string") != null ) {
					nicsWd.driver.switchTo().frame(eExecute.getAttributeValue("by_string"));					
				} else if ( eExecute.getAttribute("by_element") != null ) {
					if ( eExecute.getAttribute("element_id")== null )
						throw new LangException(task_logo+" with @action 'switch_frame' needs an attribute @element_id");
					String strElementId = eExecute.getAttributeValue("element_id");
					NicsWe ref_nics_we = (NicsWe)rh.getNo(strElementId, NicsWe.coop_type, null);			
				
					nicsWd.driver.switchTo().frame(ref_nics_we.we);
				}  else if ( eExecute.getAttribute("by_parent") != null ){
					nicsWd.driver.switchTo().parentFrame();
					
				} else {
					throw new LangException(task_logo+" with @action 'switch_frame' needs one of the following attributes: @by_index, @by_string, @by_element or @by_parent");
				}		
				
			}  else if ( action.equals("switch_tab")){
				if ( eExecute.getAttribute("by_index") != null ) {
					 ArrayList<String> newTab = new ArrayList<String>(nicsWd.driver.getWindowHandles());
					 nicsWd.driver.switchTo().window(newTab.get(Commons.string2int(eExecute.getAttributeValue("by_index"))));				
				} else 	if ( eExecute.getAttribute("by_id") != null ) {
					 
					 nicsWd.driver.switchTo().window(eExecute.getAttributeValue("by_id"));				
				} else 
					throw new LangException(task_logo+" with @action 'switch_tab' needs one of the following attributes: @by_index or @by_id");

			}  else if ( action.equals("close_tab")){
				nicsWd.driver.close();

			}  /*  else if ( action.equals("find_element2")){
				WebElement we_result = nicsWd.findElement(Tools.xmlBy(eExecute));
				String theCoopId = null;
				if ( we_result != null ) {
					theCoopId = Tools.registerWebElement(we_result, rh, rh.getLogConnector(),
							Commons.hasFlag(eExecute, "keep"));
					
					if ( eExecute.getAttribute("return_prop") != null ) {
						rh.setVar(
								"prop", 
								eExecute.getAttributeValue("return_prop") , 
								theCoopId);
					}
				}
				
			} */ else if ( action.equals("build_element")){
				String ei = eExecute.getAttributeValue("element_id");
				WebElement thewe = nicsWd.register.get(ei);
				NicsWe newNicsWe = new NicsWe(thewe);
				rh.putNo(	ei, 
						NicsWe.coop_type, 
						"WebElement", 
						(ICoop)newNicsWe, 
						null);
				
				
			} /*else if ( action.equals("get_pagesource")){
				String html = nicsWd.getPageSource();
				Tools.saveHtml(html, eExecute);
				
			} */ else if ( action.equals("get_windows")){
				Element r = new Element("windows");
				if ( nicsWd == null )
					throw new Exception("the nicsWd is null!!!");
				ArrayList<String> newTab = new ArrayList<String>(nicsWd.getWindowHandles());
				
				Iterator<String> i = newTab.iterator();
				while ( i.hasNext()) {
					String hand = i.next();
					Element s = new Element("window");
					s.setText(hand);
					r.addContent(s);
					
				}
				
				if ( eExecute.getAttribute("to_file") != null ) {
					Document docRet = new Document(r);
					
					CommonsVfs.writeToFile(docRet, eExecute.getAttributeValue("to_file"),rh.getSitzung());									
				} else  if ( eExecute.getAttribute("to_spirit") != null ) {
					rh.createSpirit(eExecute.getAttributeValue(
							"to_spirit"), 
							r, 
							rh, 
							rh.getLogConnector());
								
				} else {
					throw new LangException(task_logo+" with @action 'get_windows' needs one of the following attributes: @to_file or @to_spirit");
				}				
				
			}  else if ( action.equals("get_cookies")){
				Element r = new Element("cookies");
				
				java.util.Set<Cookie> cookies = nicsWd.manage().getCookies();
				
				Iterator<Cookie> i = cookies.iterator();
				while ( i.hasNext()) {
					Cookie c = i.next();
					SimpleDateFormat formatter= new SimpleDateFormat ("yyyy-MM-dd");
					Element s = new Element("cookie");
					s.setAttribute("domain", c.getDomain());
					s.setAttribute("name", c.getName());
					s.setAttribute("path", c.getPath());
					s.setAttribute("value", c.getValue());
					s.setAttribute("expiry", formatter.format(c.getExpiry()));
					s.setAttribute("http_only", Commons.boolToString(c.isHttpOnly()));
					s.setAttribute("secure", Commons.boolToString(c.isSecure()));
					r.addContent(s);
					
				}
							
				if ( eExecute.getAttribute("to_file") != null ) {
					Document docRet = new Document(r);
					CommonsVfs.writeToFile(docRet, eExecute.getAttributeValue("to_file"),rh.getSitzung());									
				} else  if ( eExecute.getAttribute("to_spirit") != null ) {
					rh.createSpirit(eExecute.getAttributeValue(
							"to_spirit"), 
							r, 
							rh, 
							rh.getLogConnector());								
				} else {
					throw new LangException(task_logo+" with @action 'get_cookies' needs one of the following attributes: @to_file or @to_spirit");
				}
							
			}  else if ( action.equals("get_xml")){
								
				String html = getHtmlByHow(eExecute, nicsWd,action);
				
				org.jdom2.Element  resElement = ConvJSoup.convertHtmlstring2Element(html);
				
				if ( eExecute.getAttribute("to_file") != null ) {
					org.jdom2.Document odoc = new org.jdom2.Document(resElement);
					CommonsVfs.writeToFilePretty(odoc, eExecute.getAttributeValue("to_file"),rh.getSitzung());					
				} else if ( eExecute.getAttribute("to_spirit") != null ) {
					rh.createSpirit(
							eExecute.getAttributeValue("to_spirit"), 
							resElement, 
							rh, 
							rh.getLogConnector());					
				} else 
					throw new LangException(task_logo+" with @action 'get_xml' needs one of the following attributes: @to_file or @to_spirit");

									
				
			}  else if ( action.equals("get_html")){
				
				String html = getHtmlByHow(eExecute, nicsWd,action);
				Tools.saveHtml(html, eExecute,rh);

				/*
				if ( eExecute.getAttribute("to_file") == null ) {
					org.jdom2.Document odoc = new org.jdom2.Document(resElement);
					Commons.writeToFilePretty(odoc, eExecute.getAttributeValue("to_file"));					
				} else if ( eExecute.getAttribute("to_spirit") == null ) {
					rh.createSpirit(eExecute.getAttributeValue(
							"to_spirit"), 
							resElement, 
							rh, 
							lc);					
				} else 
					throw new Exception("webdriver: get_xml needs @to_file or @to_spirit");
									
				
				} else if ( action.equals("get_xml")){
				String html = nicsWd.getPageSource();
			
				if ( eExecute.getAttribute("to_file") == null )
					throw new Exception("webdriver: get_xml needs to_file");				
				ConvJSoup.convertHtmlstring2Xml(html, eExecute.getAttributeValue("to_file"));
				*/
				
			} /* else if ( action.equals("get_spirit")){
				if ( eExecute.getAttribute("to_spirit") == null )
					throw new Exception("webdriver: get_spirit needs to_spirit");
				String html = nicsWd.getPageSource();
			
				org.jdom2.Element root = ConvJSoup.convertHtmlstring2Element(html);
				rh.createSpirit(eExecute.getAttributeValue(
						"to_spirit"), 
						root, 
						rh, 
						lc);
				
				
			} else if ( action.equals("get_dom_html")){
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				String pageSrc = (String)jsx.executeScript(
						"return arguments[0].innerHTML",
						nicsWd.driver.findElement(By.tagName("html")));
				
				pageSrc = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"+pageSrc+"</html>";
				
				
				Tools.saveHtmlDom(pageSrc, nicsWd, rh, lc, eExecute);
				
			}  else if ( action.equals("get_dom")){
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				

				String pageSrc = (String)jsx.executeScript(
						"return arguments[0].outerHTML",
						nicsWd.driver.findElement(By.tagName("html")));

				// <html xmlns="http://www.w3.org/1999/xhtml">
				
				Tools.saveHtml(pageSrc, eExecute);
				
			} */ else if ( action.equals("find_elements")){
				/*[requo-customer]*/
				List <WebElement> we_result = null;
				int waits = 0;
				if ( eExecute.getAttribute("wait") != null ) 
					waits = Commons.string2int(eExecute.getAttributeValue("wait"));
				
				int expect = -1;
				if ( 	eExecute.getAttribute("expect") != null && 
						eExecute.getAttributeValue("expect").length() > 0 ) {
					expect = Commons.string2int(eExecute.getAttributeValue("expect"));
				}
									
				if ( waits > 0 ) {
					
					WebDriverWait wait = new WebDriverWait(nicsWd.driver, Duration.ofSeconds(waits));
					
					By by = Tools.xmlBy(eExecute);	
					
					String wait_for = null;
					if ( eExecute.getAttribute("wait_for") != null)
						wait_for = eExecute.getAttributeValue("wait_for");
					else
						throw new LangException("attribute @wait_for is missing. use clickable, presence,visibility");
					
					if ( expect == 1 ) {
						WebElement elementResult = null;
						if (wait_for.equals("clickable") ) {		
							elementResult = wait.until(ExpectedConditions.elementToBeClickable(by));
						} else if (wait_for.equals("presence") ) {		
							elementResult = wait.until(ExpectedConditions.presenceOfElementLocated(by));
						} else if (wait_for.equals("visibility") ) {		
							elementResult = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
						} else {
							throw new LangException("attribute @wait_for is unknown. it is '"+wait_for+"'. use clickable, presence, visibility");
						}
						if ( elementResult != null ) {
							we_result = new ArrayList<WebElement>();
							we_result.add(elementResult);
						} else {
							throw new Exception("expectation of 1 is not fullfilled.");
						}
					} else {
						if (wait_for.equals("clickable") ) {		
							throw new LangException("attribute @wait_for=clickable is not supported for multiple expected elements");
						} else if (wait_for.equals("presence") ) {		
							we_result = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
						} else if (wait_for.equals("visibility") ) {		
							we_result = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
						} else {
							throw new LangException("attribute @wait_for is unknown. it is '"+wait_for+"'. use clickable, presence, visibility");
						}		
						if ( expect > -1 ) {
							if ( we_result.size() != expect ) {
								throw new Exception("expectation of "+expect+" is not fullfilled. found number of elements="+we_result.size());
							}
						}
					}					
				} else {
					we_result = nicsWd.findElements(Tools.xmlBy(eExecute));
				}
								
				Element xmlList = null;
				if ( Commons.hasFlag(eExecute, "load_all")) {
					xmlList = 	Tools.registerWebElements(
								we_result, rh, rh.getLogConnector(),
								Commons.hasFlag(eExecute, "keep"));					
				} else {					
							
					xmlList =  	Tools.preRegisterWebElements(
								nicsWd,we_result, rh, rh.getLogConnector(),
								Commons.hasFlag(eExecute, "keep"));
				}
				if ( eExecute.getAttribute("new_spirit_id") != null ) {
					rh.createSpirit(eExecute.getAttributeValue(
							"new_spirit_id"), 
							xmlList, 
							rh, 
							rh.getLogConnector());
				} else if ( eExecute.getAttribute("new_file") != null ) {
				    Document docu = new Document(xmlList);
				    CommonsVfs.writeToFile(docu, eExecute.getAttributeValue("new_file"),rh.getSitzung());
				} else if ( eExecute.getAttribute("return_prop_spirit_id") != null ) {
					String cnt = Tools.getDynSpiritCounter();
					rh.createSpirit(cnt, 
							xmlList, 
							rh, 
							rh.getLogConnector());
					
					rh.setVar(
							"prop", 
							eExecute.getAttributeValue("return_prop_spirit_id") , 
							cnt);
				}
				int resultCount = xmlList.getContentSize();
				if ( eExecute.getAttribute("return_prop_count") != null ) {
					rh.setVar(
							"prop", 
							eExecute.getAttributeValue("return_prop_count") , 
							Commons.int2String(resultCount));					
				}
			}  else if ( action.equals("find_element")) {
				
				WebElement elementResult = null;
				
				int waits = 0;
				if ( eExecute.getAttribute("wait") != null ) 
					waits = Commons.string2int(eExecute.getAttributeValue("wait"));				
				
				By by = Tools.xmlBy(eExecute);
				
				if ( waits > 0 ) {

					WebDriverWait wait = new WebDriverWait(nicsWd.driver, Duration.ofSeconds(waits));
										
					String wait_for = null;
					if ( eExecute.getAttribute("wait_for") != null)
						wait_for = eExecute.getAttributeValue("wait_for");
					else
						throw new LangException("attribute @wait_for is missing. use clickable, presence,visibility");

					if (wait_for.equals("clickable") ) {		
						elementResult = wait.until(ExpectedConditions.elementToBeClickable(by));
					} else if (wait_for.equals("presence") ) {		
						elementResult = wait.until(ExpectedConditions.presenceOfElementLocated(by));
					} else if (wait_for.equals("visibility") ) {		
						elementResult = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
					} else {
						throw new LangException("attribute @wait_for is unknown. it is '"+wait_for+"'. use clickable, presence, visibility");
					}		
															
				} else {
					// kein wait
					elementResult = nicsWd.driver.findElement(by);
				}
				
				if ( elementResult == null ) 
					throw new Exception("expectation of 1 is not fullfilled.");				
				
				List <WebElement> we_result = new ArrayList<WebElement>();
				we_result.add(elementResult);
				
				Element xmlList = Tools.registerWebElements(
						we_result, rh, rh.getLogConnector(),
						Commons.hasFlag(eExecute, "keep"));						
				

				String elementId = (xmlList.getChild("we")).getAttributeValue("id");
				
							
				if ( eExecute.getAttribute("return_prop_element_id") == null )
					throw new LangException(task_logo+" with @action 'find_element' needs attribute @return_prop_element_id");
					
				rh.setVar(
						"prop", 
						eExecute.getAttributeValue("return_prop_element_id") , 
						elementId);
								
			} else if ( action.equals("quit")){
				nicsWd.quit();
			}  else if ( action.equals("close")){
				nicsWd.close();
			}  else if ( action.equals("closeit")){
				nicsWd.close();
				rh.removeNo(id, NicsWd.coop_type, nicsWd);
			} else if ( action.equals("screenshot")){
				
				File scrFile = ((TakesScreenshot)nicsWd.driver).getScreenshotAs(OutputType.FILE);
				
				if (eExecute.getAttribute("out") != null ) {
					FileObject file = CoreBuilder.fsm.resolveFile(eExecute.getAttributeValue("out"));
					long opnum = rh.getSitzung().grantsUpdateFileP1(file,"write_screenshot");
					FileUtils.copyFile(scrFile, 
							new File(Commons.fileClassic(eExecute.getAttributeValue("out"))) );
					rh.getSitzung().grantsUpdateFileP2(file,opnum);
					
				}
				if (eExecute.getAttribute("file") != null ) {
					
					FileObject file = CoreBuilder.fsm.resolveFile(eExecute.getAttributeValue("file"));
					long opnum = rh.getSitzung().grantsUpdateFileP1(file,"write_screenshot");
					FileUtils.copyFile(scrFile, 
							new File(Commons.fileClassic(eExecute.getAttributeValue("file"))) );
					rh.getSitzung().grantsUpdateFileP2(file,opnum);					
						
				}
			}  else if ( action.equals("scroll")){
				
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript("window.scrollBy("+eExecute.getAttributeValue("x")+
						","+eExecute.getAttributeValue("y")+")", "");
				
			}  else if ( action.equals("scroll_to_end")){
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				
			}  else if ( action.equals("scroll_to_begin")){
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript("window.scrollTo(0, 0)");
				
			} else if ( action.equals("scroll_to_element")){
				if ( eExecute.getAttribute("element_id")== null )
					throw new Exception("action [scroll_to_element] needs attribute [element_id]");
				String strElementId = eExecute.getAttributeValue("element_id");
				NicsWe ref_nics_we = (NicsWe)rh.getNo(strElementId, NicsWe.coop_type, null);			
				JavascriptExecutor jsx = (JavascriptExecutor)nicsWd.driver;
				jsx.executeScript(
			            "arguments[0].scrollIntoView();", ref_nics_we.we);
				
			} else if ( action.equals("sleep")){
				
			} /* else if ( action.equals("wait")){ 
				
				if  ( eExecute.getAttribute("timeout") == null) 
					throw new Exception("action 'wait' needs attribute @timeout with the milliseconds to wait");
				
				int timeout_ms = Commons.string2int(eExecute.getAttributeValue("timeout"));
				
				int timeout_seconds = (timeout_ms * 1000) / 1000000;
				System.out.println(timeout_ms+" milliseconds sind "+timeout_seconds+" seconds");

				WebDriverWait wait = new WebDriverWait(nicsWd.driver, Duration.ofSeconds(timeout_seconds));
				By by = Tools.xmlBy(eExecute);	
				WebElement elementResult = null;
				String how = "clickable";
				if ( eExecute.getAttribute("how") != null)
					how = eExecute.getAttributeValue("how");
					
				if (how.equals("clickable") ) {					
					elementResult = wait.until(ExpectedConditions.elementToBeClickable(by));
				} else if (how.equals("visibility")) {
				
					elementResult = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				} else 
					throw new LangException(task_logo+" with @action 'wait' has unknown attribute @how '"+how+"'"+
							"It should be 'clickable' or 'visibility'");
				
				if ( eExecute.getAttribute("new_element_id") != null ) {
					Tools.registerWebElement ( 
							elementResult,
							rh, 
							rh.getLogConnector(),
							Commons.hasFlag(eExecute, "keep") ) ;
				} else if ( eExecute.getAttribute("return_prop_element_id") != null ) {
					//String cnt = Tools.getDynSpiritCounter();
					String newId = Tools.registerWebElement ( 
							elementResult,
							rh, 
							rh.getLogConnector(),
							Commons.hasFlag(eExecute, "keep") ) ;
					
					rh.setVar(
							"prop", 
							eExecute.getAttributeValue("return_prop_element_id") , 
							newId);
				}
					

			} */  else
				throw new LangException(task_logo+" has unknown @action '"+action+"'");
						
		}
		if ( eExecute.getAttribute("and_sleep") != null ) {
			Thread.sleep(Commons.string2int(eExecute.getAttributeValue("and_sleep")));
		}		
		return null;
	}


	




}