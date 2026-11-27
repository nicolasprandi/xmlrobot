package at.nicolas.x.tests;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.WebVfs.ReqResSimu;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.DummySitzung;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;

public class TestJava2 {

	public void go() throws Exception {
		JobRunner.createCorebuilder(
				"C:\\Users\\nicolas\\Desktop\\XS-0518\\xmlrobot-base-RINO-2018-08-28\\default.xrt", 
				"C:\\Users\\nicolas\\Documents\\_temp\\systemp", 
				false, 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\ant-4-xmlrobot.jar", 
				"C:\\Users\\nicolas\\workspace-mars\\primero\\WebContent\\WEB-INF\\lib\\poi-4-xmlrobot.jar", new DummySitzung()
				, false);
		
		
		
		Tc_Terminator terminator = new Tc_Terminator(
				false,
				new GuiControllerDesktop(), Terminal.offlineSitzung);
		// terminator.setWebModus(true);
		terminator.setupDone();
		
		// Interpreter erzeugen
		Interpreter interpreter = JobRunner.createInterpreter(
				"Anton", // "Anton", 
				false, 
				"Test-Sanchez", // "Test-Sanchez", 
				"first", 
				null, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
				null, 
				null, 
				Interpreter.SILENCE_NO, 
				null, 
				0, 
				true, 
				false,
				terminator,
				true ,new GuiControllerDesktop(), Terminal.offlineSitzung,false);

		//interpreter.setIncludeHeaderAndFooter(true);
		JobRunner jr = new JobRunner();
		jr.runNewJob(interpreter, new ReqResSimu(jr,terminator));
		
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TestJava2 s = new TestJava2();
		s.go();
	
	}
	public TestJava2() {
	}
}

