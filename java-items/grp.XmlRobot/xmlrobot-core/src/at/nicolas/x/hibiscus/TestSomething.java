package at.nicolas.x.hibiscus;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.DummyRequestHandler;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;

public class TestSomething {

	public static void main(String[] args) throws Exception {

		
		int x = Integer.parseInt("0.68876");
		
		//var pad_vork = ("00000000000000000002"+x).slice(-19);
		
		String int_inhalt;
		if(x<0 ) {
			int_inhalt= String.format("%016d", x);
		} else {
			int_inhalt ="+"+String.format("%015d", x);
		}
		System.out.println(int_inhalt);

	}

}
