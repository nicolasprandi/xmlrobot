package at.nicolas.x.hibiscus;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.DummyRequestHandler;

public class TestPostgres {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CoreBuilder.buildTest();
		Element test = new Element("integrate");
		test.setAttribute("db", "postgres");
		test.setAttribute("url", "jdbc:postgresql://localhost:5432/hibiscus");
		test.setAttribute("user", "postgres");
		test.setAttribute("password", "servas");
		test.setAttribute("log_file", "file:///C:/Users/nicolas/Documents/dumps/integration.txt");	
		test.setAttribute("log_flag", "debug");		
		
		Integriere integrate = new Integriere();
		integrate.execute(test, new DummyRequestHandler());

		
	}

}
