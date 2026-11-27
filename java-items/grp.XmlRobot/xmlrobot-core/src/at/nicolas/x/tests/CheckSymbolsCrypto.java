package at.nicolas.x.tests;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.DummySitzung;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;

public class CheckSymbolsCrypto {

	public CheckSymbolsCrypto(String in_file, String in_encoding, String tmp1, String tmp2, String secret) throws Exception {
		CommStartMsg sm = new CommStartMsg();
		
		sm.out.println("======================= FIRST TRY ================== ");
		CoreBuilder.allStartupActions(sm,false);
		OfflineSitzung offline2 = new OfflineSitzung(CoreBuilder.ws.getWsFilename(),"CheckSymbolsCrypto");
		
		String standardHome = LocalFilesystem.getHomeDirXrFormat();
		CoreBuilder.build("C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt",
				standardHome,sm,InterfaceParas.getEmpty(),offline2);
		offline2.setRole(CoreBuilder.ws.getRole());		
		
		// TODO Auto-generated constructor stub
		/*
		JobRunner.createCorebuilder(
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt", 
				"C:\\Users\\nicolas\\Documents\\_temp\\systemp", 
				false, 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\ant-4-xmlrobot.jar", 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\poi-4-xmlrobot.jar", new DummySitzung());

		 * 
		 
		JobRunner.createCorebuilder(
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt", 
				"C:\\Users\\nicolas\\Documents\\_temp\\systemp", 
				false, 
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\xmlrobot-internal\\system\\libs\\ant-4-xmlrobot.jar", 
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\xmlrobot-internal\\system\\libs\\poi-4-xmlrobot.jar", new DummySitzung());
		Tc_Terminator terminator = new Tc_Terminator(false,
				new GuiControllerDesktop(), Terminal.offlineSitzung);
		// terminator.setWebModus(true);
		terminator.setupDone();		
		ISitzung sitzung = new DummySitzung();
		String content = Commons.readTextfileFormat(in_file, in_encoding, Terminal.offlineSitzung);
		System.out.println("Content: "+content);
		*/
	}
	public static void main(String[] args) throws Exception {
		
		String hTestsets = "C://Users//nicolas//Documents//_testsets//";
		
		CheckSymbolsCrypto x = new CheckSymbolsCrypto(hTestsets+"einfach.ljx","UTF-8",null,null,"hase");
		
	}

}
