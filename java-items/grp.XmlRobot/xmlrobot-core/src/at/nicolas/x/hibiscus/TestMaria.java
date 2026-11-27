package at.nicolas.x.hibiscus;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.DummyRequestHandler;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;

public class TestMaria {

	public static void main(String[] args) throws Exception {

		

		CoreBuilder.buildTest();
		
			Element test = new Element("integrate");
			test.setAttribute("db", "maria");
			test.setAttribute("url", "jdbc:mysql://localhost:3307/hibiscus?zeroDateTimeBehavior=convertToNull");
			test.setAttribute("user", "root");
			test.setAttribute("password", "root");		
			test.setAttribute("log_file", "file:///C:/Users/nicolas/Documents/dumps/integration.txt");	
			test.setAttribute("log_flag", "debug");
			test.setAttribute("drop_index", "1");
			test.setAttribute("runstats", "1");
			test.setAttribute("pronto", "1");
			test.setAttribute("background", "0");
			test.setAttribute("explain", "file:///C:/Users/nicolas/Documents/dumps/integration-explain.xml");
			Integriere integrate = new Integriere();
			integrate.execute(test, new DummyRequestHandler());
			//integrate.execute(test, new DummyRequestHandler(), new DummyLogConnector());

	}

}
