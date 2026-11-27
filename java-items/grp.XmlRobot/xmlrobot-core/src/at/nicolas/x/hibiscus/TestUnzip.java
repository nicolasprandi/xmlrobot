package at.nicolas.x.hibiscus;

import org.apache.commons.vfs2.FileObject;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.DummySitzung;

public class TestUnzip {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CoreBuilder.buildTest();
		DummySitzung iSitzung = new DummySitzung();
		FileObject foFile  = CommonsVfs.resolveFileSilent(
			"file:///F:/XmlRobot-Dev/Echnaton-Scripts/xmlrobot-internals/system/service_apps/app.Arcadia/pro.03-echo-desk-app-dev/compile-xmlrobot-core/bin_download/ant-launcher-1.9.11.jar");


		
	}

}
