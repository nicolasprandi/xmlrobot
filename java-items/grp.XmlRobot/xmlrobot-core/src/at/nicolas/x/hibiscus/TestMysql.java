package at.nicolas.x.hibiscus;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.DummyRequestHandler;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;

public class TestMysql {

	public static void main(String[] args) throws Exception {

		

		CoreBuilder.buildTest();
		
			Element test = new Element("integrate");
			test.setAttribute("how", "url");
			test.setAttribute("db", "mysql");
			test.setAttribute("url", "jdbc:mysql://localhost:3306/hibiscus");
			test.setAttribute("user", "root");
			test.setAttribute("password", "root");		
			test.setAttribute("log_file", "file:///C:/Users/nicol/Documents/Dev-Stick/hibiscus-integration/integration-log.txt");	
			test.setAttribute("log_flag", "debug");
			test.setAttribute("drop_index", "1");
			test.setAttribute("runstats", "1");
			test.setAttribute("pronto", "2");
			test.setAttribute("background", "1");
			test.setAttribute("intensive_rounds", "33");
			test.setAttribute("report", "1000");
			test.setAttribute("flag", "xexplain xmvrefresh xkopfgrouped upp_mig upp_heal upp_top isdetails chexckonly dss numform smart smart_in_opti simple doubleoccupation adapt ");
			test.setAttribute("commit", "3001");
			test.setAttribute("explain", "file:///C:/Users/nicol/Documents/Dev-Stick/hibiscus-integration/integration-explain.xml");
			test.setAttribute("index","file:///C:/Users/nicol/Documents/Dev-Stick/XmlRobot-Dev/Echnaton-Scripts/xmlrobot-internals/system/service_apps/app.Hibiscus/pro.mysql/xDb/index/mysql-original/imp-list.xml");
			test.setAttribute("break", "file:///C:/Users/nicol/Documents/Dev-Stick/hibiscus-integration/breakpoint.txt");
			test.setAttribute("kickyy","file:///H:/XmlRobot-Dev/Echnaton-Scripts/xmlrobot-internals/system/service_apps/app.Hibiscus/pro.mysql/integro/ultimo/select-replacements.xml");
			test.setAttribute("log", "file:///C:/Users/nicol/Documents/Dev-Stick/hibiscus-integration/log/");
			test.setAttribute("mv","file:///C:/Users/nicol/Documents/Dev-Stick/XmlRobot-Dev/Echnaton-Scripts/xmlrobot-internals/system/service_apps/app.Hibiscus/pro.mysql/mv-defs-4-integration.xml");
			test.setAttribute("mv_style","2");
						
			
			//test.setAttribute("index","");
			test.setAttribute("break", "");			
			Integriere integrate = new Integriere();
			integrate.execute(test, new DummyRequestHandler());
			//integrate.execute(test, new DummyRequestHandler(), new DummyLogConnector());

	}

}
