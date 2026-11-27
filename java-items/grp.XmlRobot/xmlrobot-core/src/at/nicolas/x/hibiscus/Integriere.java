package at.nicolas.x.hibiscus;


import org.jdom2.Element;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;



public class Integriere implements ITask {
	

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		// TODO Auto-generated method stub
		/*[requo-timer]*/
		/*[requo-customer]*/	
		ItegrationMain integrationMain = new ItegrationMain();
		Parameters ret = integrationMain.execute(e, rh);		
		return ret;
	}
	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		/*[requo-taskdef-integrate]*/
	}




}
