package at.nicolas.x.tests;

import at.nicolas.x.int_interpreter.PlugTaskdef;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.int_specs.DummySitzung;
import at.nicolas.x.int_specs.Interface;
import at.nicolas.x.int_specs.InterfaceParas;

public class SimulateJ2ee {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String pn = "p4";
		String pdir = "C:\\Users\\nicolas\\Documents\\_temp\\"+pn;
		String antx = "C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\ant-4-xmlrobot.jar";
		String poix = "C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\poi-4-xmlrobot.jar";
		ClassLoader cl = LocalFilesystem.getClassLoaderFrom2Filenames(antx,poix);
		PlugTaskdef taskdef_ant = PlugTaskdef.newInstanceTrustedByClassloader("ant", "at.nicolas.x.tasks.Ant" , cl , null);
		if ( taskdef_ant == null)
			Sp.p("taskdef_ant == null");
		else
			Sp.p("taskdef_ant != null");
		Interface.startup(
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt", 
				InterfaceParas.get("C:\\Users\\nicolas\\Documents\\_temp\\systemp", cl), new DummySitzung(),false);
		
		Interface.runJob("run", "TestMorales", pn, pdir);
		Interface.runJob("ant", "TestMorales", pn, pdir);
		Interface.runJob("poi", "TestMorales", pn, pdir);
		Sp.p("Data in: "+pdir);
		Interface.shutdown();
	}
	/*
	private static ClassLoader getClassLoaderFromFilename(String jarname) throws MalformedURLException {
		if (jarname==null) return null;
		File jarfile = new File(jarname);
		ArrayList<URL> urlArray = new ArrayList<URL>();
		urlArray.add(jarfile.toURI().toURL());	
		
		URL[] urlArray2 = (URL[])urlArray.toArray(new URL[urlArray.size()]);
		ClassLoader loaderX = new URLClassLoader(urlArray2);
		return loaderX;
	}	
	*/

}
