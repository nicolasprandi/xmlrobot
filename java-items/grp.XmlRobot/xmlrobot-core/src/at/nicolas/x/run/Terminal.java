/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.run;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.vfs2.FileName;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class Terminal {
	

	public static ISitzung offlineSitzung = null;
	public static void mainParameters(String wsFilename, String homeDir ) throws Exception, InstantiationException, IllegalAccessException, ClassNotFoundException, JDOMException, IOException {

		
		boolean tryAgain = true;
		boolean doneWell = false;
		CommStartMsg sm = new CommStartMsg();
		
		sm.out.println("======================= FIRST TRY ================== ");
		CoreBuilder.allStartupActions(sm,false);
		sm.out.println("---- XmlRobot Core init done ----");
		Sp.p("---- XmlRobot Core init done ----");
		// JOptionPane.showMessageDialog (null, 
		//		 "mainParameters:1 _wsFilename="+wsFilename, "Information", JOptionPane.INFORMATION_MESSAGE);

		
		while ( tryAgain ) {
		
			sm.out.println("Starting XmlRobot-Terminal with Takeoff "+wsFilename);
			
			try {
				//offlineSitzung 
				OfflineSitzung offline2 = new OfflineSitzung(wsFilename,"TerminalOffline2");
				offlineSitzung = offline2;
				CoreBuilder.build(wsFilename,homeDir,sm,InterfaceParas.getEmpty(),Terminal.offlineSitzung);
				/* JOptionPane.showMessageDialog (null, 
						 "mainParameters:2 wsFilename="+CoreBuilder.ws.getWsFilename(), "Information", JOptionPane.INFORMATION_MESSAGE);				
				 */
				 offline2.setWsFilename(CoreBuilder.ws.getWsFilename()); 
				 offline2.setRole(CoreBuilder.ws.getRole());
				offline2.setLang(CoreBuilder.ws.getLang());

				doneWell = true;
				tryAgain = false;

			} catch (Exception e) {
				boolean again = sm.presentWindow(e);
				Sp.p("After presenting Startup-Message-Window");
				if ( again ) {
					sm.out.println("======================= NEXT TRY ================== ");
					tryAgain = true;
				} else {
					tryAgain = false;
					doneWell = false;
				}
			}			
		}
				
		if ( doneWell ) {
			Ti_TerminalLink.setup(false);
			Tc_AppConsole cg = Tc_AppConsole.getInstance();
			cg.run(sm);	
		}
		
	}
	
	public static void main(String[] args) throws Exception, InstantiationException, IllegalAccessException, ClassNotFoundException, JDOMException, IOException {

		
		try {
			
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
			System.setProperty("apple.awt.graphics.UseQuartz", "false");
	        
	    } 
	    catch (UnsupportedLookAndFeelException e) {

	    }
	    catch (ClassNotFoundException e) {

	    }
	    catch (InstantiationException e) {

	    }
	    catch (IllegalAccessException e) {

	    }
	    
		
		String all_args = new String();
		int i;
		for ( i =0; i < args.length; i++ ) {
			all_args = all_args + args[i] ;
		}
		if ( all_args.length() == 0 )
			all_args = null;
		
		String standardHome = LocalFilesystem.getHomeDirXrFormat();
		Sp.p("Received from os ["+all_args+"]");
		if ( all_args == null || all_args.trim().length()==0 ) {
			// nothing, set home to standard
			mainParameters(null,standardHome);
		} else if ( all_args.indexOf("home=") > -1) {
			// there is a definition of home
			int point = all_args.indexOf("home=");
			String home_para = all_args.substring(point+5);
			if ( home_para.startsWith("'") || home_para.startsWith("\""))
				home_para = home_para.substring(1);
			if ( home_para.endsWith("'") || home_para.endsWith("\""))
				home_para = home_para.substring(0, home_para.length()-1);
			home_para = home_para+FileName.SEPARATOR;
			Sp.p("       home_para ["+home_para+"]");
			String takeoff_para = null;
			if ( point > 0 )
				takeoff_para = all_args.substring(0, point);
			Sp.p("    takeoff_para ["+takeoff_para+"]");
			mainParameters(takeoff_para,home_para);
		} else {
			// no definition of home
			mainParameters(all_args,standardHome);
		}
		
		
	}	


}