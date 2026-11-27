package at.nicolas.webwalk;

import org.jdom2.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;

public class WebTabulator implements ITask {
	// TabTasks
	private static String task_id ="wtabulator";
	private static String task_logo ="task 'wtabulator'";
	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parameters execute(Element eExecute, IRequestHandler rh) throws Exception {
		if ( eExecute.getAttribute("act") == null ) {
			throw new LangException("tab: needs attribute @act (...)");
		}
		/*[requo-timer]*/
		String action = eExecute.getAttributeValue("act");
		
		String id = null;
		if ( eExecute.getAttribute("id") != null ) {
			id=eExecute.getAttributeValue("id");
		} else {
			throw new LangException("tab: needs attribute @id (...)");
		}
		NicsTab tab = (NicsTab)rh.getNo(id, NicsTab.coop_type, this);
		if ( action.equals("active_element")){
			WebElement wx = tab.activeElement();
			if ( eExecute.getAttribute("new_element_id") != null ) {
				rh.putNo(
						eExecute.getAttributeValue("new_element_id"), 
						NicsWe.coop_type, 
						"WebElement", 
						(ICoop)wx, 
						null);
			}
		} else if ( action.equals("alert")){
			tab.alert();
		} else if ( action.equals("default_content")){
			tab.defaultContent();
		}  else if ( action.equals("parent_frame")){
			tab.parentFrame();
		}  else if ( action.equals("window")){
			if ( eExecute.getAttribute("by_name") != null ) {
				tab.window(eExecute.getAttributeValue("by_name"));
			} else if ( eExecute.getAttribute("by_handle") != null ) {
				tab.window(eExecute.getAttributeValue("by_handle"));
			} else if ( eExecute.getAttribute("by_name_or_handle") != null ) {
				tab.window(eExecute.getAttributeValue("by_name_or_handle"));
			}  else
				throw new LangException(task_logo+" with @action='window' needs one of the following attributes: @by_name, @by_handle or @by_name_or_handle");
		} else if ( action.equals("frame")){
			WebDriver rw = null;
			if ( eExecute.getAttribute("by_index") != null ) {
				rw = tab.frameByIndex(Commons.string2int(eExecute.getAttributeValue("by_index") ));
			}else if ( eExecute.getAttribute("by_name") != null ) {
				rw = tab.frameByNameOrId(eExecute.getAttributeValue("by_name"));
			}else if ( eExecute.getAttribute("by_id") != null ) {
				rw = tab.frameByNameOrId(eExecute.getAttributeValue("by_id"));
			}else if ( eExecute.getAttribute("by_name_or_id") != null ) {
				rw = tab.frameByNameOrId(eExecute.getAttributeValue("by_name_or_id"));
			}else if ( eExecute.getAttribute("by_element_id") != null ) {
				String searched = eExecute.getAttributeValue("by_element_id");
				ICoop nicsWeCoop = rh.getNo(searched, NicsWe.coop_type, null);
				if ( nicsWeCoop == null) {
					throw new Exception("tab.frame rh.getNo hat nix gebracht "+searched+ " "+ NicsWe.coop_type);
				}
				
				// NicsWe nicsWe = (NicsWe)nicsWeCoop;
				NicsWe nicsWe = (NicsWe)nicsWeCoop;
				if ( nicsWe == null ) {
					throw new Exception("tab.frame Could not locate  "+searched+ " "+ NicsWe.coop_type);
				}				
				tab.frame(nicsWe.we);
				
			} else {
				throw new LangException(task_logo+" with @action='frame' needs one of the following attributes: @by_name, @by_id, @by_name_or_id or @by_element_id");
				
			}
		} else
			throw new LangException(task_logo+" received unknown @action='"+action+"'");
		
		return null;
	}



}
