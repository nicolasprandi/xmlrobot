/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_call.AppJobexecution;
import at.nicolas.x.int_call.AppJobexecutionThread;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_const.Zet_Privileg;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_db.DbPro;
import at.nicolas.x.int_extensions.PlugJar;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LogLogger;
import at.nicolas.x.int_setup.MountConfig;
import at.nicolas.x.int_specs.CoreDummyTerminator;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_map.Map_ButtonClear;
import at.nicolas.x.term_map.Map_ButtonClearLog;
import at.nicolas.x.term_map.Map_ButtonGarage;
import at.nicolas.x.term_map.Map_ButtonHelp;
import at.nicolas.x.term_map.Map_ButtonRefresh;
import at.nicolas.x.term_map.Map_ContextViewerApp;
import at.nicolas.x.term_map.Map_ContextViewerInst;
import at.nicolas.x.term_map.Map_ContextViewerJob;
import at.nicolas.x.term_map.Map_ContextViewerSystemJob;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_Dummy;
import at.nicolas.x.term_map.Map_LogViewer;
import at.nicolas.x.term_persp.Persp_IZP;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class Tc_AppConsole extends Object {  
	
	public static Tc_GuiSizer guiSizer = null;
	private static final String layout_prolog = "Layout: ";
	private static final int layout_prolog_len = 0;
	
	private LogConnector lc = new LogConnector ("Terminal");
	private Container myContainer = null;

	public static Tc_AppConsole lastInstance= null;
	private static JFrame jFrame;
	public static JFrame getJFrame() {
		return jFrame;
	}
	Tc_AppConsole appConsole3Dispose = null;

	private ArrayList uiMapList = new ArrayList();  // Liste mit UiMapElem

	private Tc_UiContext uiContext = new Tc_UiContext();
	
	private Tc_PerspManager perspManager = null;
	private Persp_IZP perspective = null;
	String perspectiveName = null;
	
	int guisize = 0;
	public static boolean mac = false;
	public static boolean linux = false;
	
	boolean auto = true;
	
	JComboBox perspCombo = null;
	String [] perspNames = null;
	String [] perspNamesWrap = null;
	
	JComboBox wsCombo = null;
	String [] wsNames = null;
	String [] wsNamesWrap = null;
	
	JComboBox compCombo = null;
	String [] compNames = null;
	String [] compNamesWrap = null;

	JComboBox rolesCombo = null;
	String [] rolesNames = null;
	String [] rolesNamesWrap = null;
	
	JComboBox autoCombo = null;
	boolean [] autoNames = {true,false};
	String [] autoNamesWrap = null;	
	
	JComboBox sizesCombo = null;
	int [] sizesNames = {0,1,2,3,4,5,6};
	String [] sizesNamesWrap = null;
	
	Map_ContextViewerApp contextViewerApp = null;
	Map_ContextViewerInst contextViewerInst = null;
	Map_ContextViewerJob contextViewerJob = null;
	Map_ContextViewerSystemJob contextViewerSystemJob = null;
	JPanel boxA = null;
	
	Tc_Guistatus gs = null;
	
	CommStartMsg sm;
	private boolean firstMessage = true;
	public ITerminator terminator = null;
	private ISitzung systemSitzung;
	public Tc_AppConsole (
			Container _myContainer,
			Tc_AppConsole _appConsole3Dispose,
			ISitzung systemSitzung) throws JDOMException, IOException, ParseException, Exception {
		this.systemSitzung = systemSitzung;
		myContainer = _myContainer;
		appConsole3Dispose = _appConsole3Dispose;
		lastInstance = this;
		/*[echnaton-timer]*/
	}
	
	private void setFontSize (JComboBox jComboBox) {
		if ( CoreBuilder.ws.isTablet ) {
			jComboBox.setFont(Tc_AppConsole.guiSizer.tabletFontToolbar);
		}
		
	}
	public void setComputer( String newComputer ) throws Exception {
		boolean found = false;
		int found_index = -1;
		for ( int i=0; i < compNames.length; i++ ) {
			if ( compNames[i].equals(newComputer) ) {
				found=true;
				found_index = i;
			}
				
		}
		if (  found ) {
			compCombo.setSelectedIndex(found_index);
		} else
			throw new Exception("The computer '"+newComputer+"' is not defined in takeoff-file.");
		
	}	
	
	public void tiMinus() {
		terminator.backButtonPressed();
	}
	public void tiPlus() {
		terminator.foreButtonPressed();
	}
	private String roleWrap( String in ) {
		return "role: "+in;
	}
	private String sizeWrap( int in ) {
		return "size: "+in;
	}	
	private String autoWrap( boolean in ) {
		if (in)
			return "auto: on";
		else
			return "auto: off";
	}	
	private String compWrap( String in ) {
		if ( in != null)
			return "cp: "+in;
		else
			return "cp: ";
	}	
	
	private String wsWrap( String in ) {
		return "ws: "+in;
	}	
	
	private String perspWrap( String in ) {
		return in;
	}		
	/*
	private String roleWrapBack( String in ) {
		return in.substring(4);
	}	
	*/
	public void dbDoubleClick() {
		Sp.p("bin im double-click");
		runScCurrDb(Wellknown.open_prohome, Interpreter.SILENCE_VERY);
		// runScOpenFolder();
		
	}
	public void dbThreeClick() {
		
	}	
	public void run(CommStartMsg _sm) throws Exception, JDOMException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException  {

		if ( CoreBuilder.ws.getOs().equals("mac") )
			mac = true;
		else if ( CoreBuilder.ws.getOs().equals("linux") )
			linux = true;
		
		sm = _sm;
		gs = new Tc_Guistatus(systemSitzung);
		gs.readFile(Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.FILE_GUI_STATUS)));

		guiSizer = new Tc_GuiSizer();
		guisize = gs.getGuiSize();
		guiSizer.setSize(guisize);
		
		if ( appConsole3Dispose != null )
			appConsole3Dispose=null;
		
		perspManager = new Tc_PerspManager(CoreBuilder.mc.getValue(MountConfig.FILE_GUI_PERSPLIST));

		myContainer.setLayout(new BorderLayout());
		
		// maps fuer top
		Color myColor = Color.GRAY;
		Font myFont = new Font("Dialog",Font.PLAIN,12);
		Border myBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);
		// DefinedText textComputer = new DefinedText("Comp:"+Conf.mc.getValue(MountConfig.PROP_COMPUTER),null,null,myColor,myBorder);
		// DefinedText textProfile  = new DefinedText("Prof:"+Conf.mc.getValue(MountConfig.PROP_PROFILE),null,null,myColor,myBorder);
		Map_ButtonGarage buttonGarage = new Map_ButtonGarage(null,null,null); // new ButtonGeneralHelp(myFont,null,myColor);
		Map_ButtonHelp buttonHelp = new Map_ButtonHelp(null,null,null); // new ButtonGeneralHelp(myFont,null,myColor);
		Map_ButtonRefresh buttonRefresh = new Map_ButtonRefresh(null,null,null);	
		Map_ButtonClear buttonClear = new Map_ButtonClear(null,null,null);
		Map_ButtonClearLog buttonClearLog = new Map_ButtonClearLog(null,null,null);

		// Director director = new Director(new PatternLayout( "%-5p %m%n" ),null,null,myColor,myBorder);
		
		// perspCombo.setBackground(myColor);
		// perspCombo.setFont(myFont);
		
		perspNames = perspManager.getNames();
		perspNamesWrap = new String[perspNames.length];
		for (int ix=0; ix < perspNames.length;ix++ ) {
			perspNamesWrap[ix] = perspWrap(perspNames[ix]);

		}		
		perspCombo =  new JComboBox(perspNamesWrap);
		setFontSize(perspCombo);
		perspCombo.setToolTipText("Changes the layout of the screen");
		
		
		wsNames = CoreBuilder.ws.getWsNames();
		wsNamesWrap = new String[wsNames.length];
		for (int ix=0; ix < wsNames.length;ix++ ) {
			wsNamesWrap[ix] = wsWrap(wsNames[ix]);

		}			
		wsCombo =  new JComboBox(wsNamesWrap);
		setFontSize(wsCombo);
		wsCombo.setToolTipText("Changes the workspace");
		
		compNames = CoreBuilder.ws.getComputers();
		compNamesWrap = new String[compNames.length+1];
		for (int ix=0; ix < compNames.length;ix++ ) {
			compNamesWrap[ix] = compWrap(compNames[ix]);
		}	
		compNamesWrap[compNamesWrap.length-1] = compWrap("");
		compCombo = new JComboBox(compNamesWrap);
		setFontSize(compCombo);
		compCombo.setToolTipText("Changes the computer");
		/*[echnaton-timer]*/
		rolesNames = CoreBuilder.ws.getRoles(); 
		rolesNamesWrap = new String[rolesNames.length];
		for (int ix=0; ix < rolesNames.length;ix++ ) {
			rolesNamesWrap[ix] = roleWrap(rolesNames[ix]);

		}
		rolesCombo = new JComboBox(rolesNamesWrap);
		setFontSize(rolesCombo);
		rolesCombo.setToolTipText("Changes the user-role and the attached privilegs");

		// auto
		autoNamesWrap = new String[autoNames.length];
		for (int ix=0; ix < autoNames.length;ix++ ) {
			autoNamesWrap[ix] = autoWrap(autoNames[ix]);

		}		
		autoCombo = new JComboBox(autoNamesWrap);
		setFontSize(autoCombo);
		autoCombo.setToolTipText("Auto fires dynamically project-info and application-help on selection");

		
		sizesNamesWrap = new String[sizesNames.length];
		for (int ix=0; ix < sizesNames.length;ix++ ) {
			sizesNamesWrap[ix] = sizeWrap(sizesNames[ix]);

		}
		sizesCombo = new JComboBox(sizesNamesWrap);
		setFontSize(sizesCombo);
		sizesCombo.setToolTipText("Changes the size of letters in the screen");

		
		
		contextViewerApp = new Map_ContextViewerApp(null,null, myColor,myBorder,jFrame);
		contextViewerInst = new Map_ContextViewerInst(null,null,myColor,myBorder);
		contextViewerJob = new Map_ContextViewerJob(null,null,myColor,myBorder);
		contextViewerSystemJob = new Map_ContextViewerSystemJob(null,null,myColor,myBorder);			

		JPanel p1 = new JPanel ( new BorderLayout());
		JLabel l1 = new JLabel("   Screen ");
		l1.setBackground(myColor);
		l1.setFont(myFont);
		
		p1.add(l1,BorderLayout.WEST);
		p1.add(perspCombo,BorderLayout.CENTER);
		
		// List of Workspaces
		JPanel p2 = new JPanel ( new BorderLayout());
		JLabel l2 = new JLabel("   Workspace ");
		l2.setBackground(myColor);
		l2.setFont(myFont);
		
		p2.add(l2,BorderLayout.WEST);
		p2.add(wsCombo,BorderLayout.CENTER);
		
		// List of Computers
		JPanel p3 = new JPanel ( new BorderLayout());
		JLabel l3 = new JLabel("   Computer ");
		l3.setBackground(myColor);
		l3.setFont(myFont);
		
		p3.add(l3,BorderLayout.WEST);
		p3.add(compCombo,BorderLayout.CENTER);
		
		// List of Roles
		JPanel p4 = new JPanel ( new BorderLayout());
		JLabel l4 = new JLabel("   Roles ");
		l4.setBackground(myColor);
		l4.setFont(myFont);
		
		p4.add(l4,BorderLayout.WEST);
		p4.add(rolesCombo,BorderLayout.CENTER);		
		
		// List of Auto
		JPanel p4b = new JPanel ( new BorderLayout());
		JLabel l4b = new JLabel("   Auto ");
		l4b.setBackground(myColor);
		l4b.setFont(myFont);
		
		p4b.add(l4b,BorderLayout.WEST);
		p4b.add(autoCombo,BorderLayout.CENTER);				

		// List of Sizes
		JPanel p5 = new JPanel ( new BorderLayout());
		JLabel l5 = new JLabel("   Sizes ");
		l5.setBackground(myColor);
		l5.setFont(myFont);
		
		p5.add(l5,BorderLayout.WEST);
		p5.add(sizesCombo,BorderLayout.CENTER);			

		perspCombo.setMaximumRowCount(20);
		wsCombo.setMaximumRowCount(20);
		compCombo.setMaximumRowCount(20);
		rolesCombo.setMaximumRowCount(20);
		sizesCombo.setMaximumRowCount(20);
		int anzahl = 8;
		boolean give_me_restart = true; //Zet_Privileg.showRestartButton()
		if ( give_me_restart ) {
			anzahl = 9;
		}		
		JPanel boxA = new JPanel(new GridLayout(1,anzahl));
		boxA.add(perspCombo);
		boxA.add(wsCombo);
		boxA.add(compCombo);		
		boxA.add(rolesCombo);
		boxA.add(autoCombo);
		boxA.add(sizesCombo);
		boxA.add(buttonClearLog.getJComponent());
		boxA.add(buttonClear.getJComponent());
		
		if ( give_me_restart) {
			boxA.add(buttonRefresh.getJComponent());
		}			
		
		// boxA.add(part3);
		// boxA.add(buttonClear.getJComponent());

		if ( CoreBuilder.ws.isTablet)
			boxA.setPreferredSize(new Dimension(200,Tc_AppConsole.guiSizer.tabletToolbarHeight)); 
		else
			boxA.setPreferredSize(new Dimension(200,Tc_AppConsole.guiSizer.stdToolbarHeight)); 
		
		// zur map haengen
		appendUiMapStd(contextViewerApp);
		appendUiMapStd(contextViewerInst);
		appendUiMapStd(contextViewerJob);
		appendUiMapStd(contextViewerSystemJob);		
		appendUiMapStd(buttonRefresh);
		appendUiMapStd(buttonClear);
		appendUiMapStd(buttonClearLog);
		appendUiMapStd(buttonGarage);
		appendUiMapStd(buttonHelp);
		Map_Dummy dummy = new Map_Dummy();
		appendUiMapStd(dummy);

		myContainer.add(boxA,BorderLayout.SOUTH);
		lc.core("+ + + + + + + perspectiveName="+perspectiveName,LogConnector.CORE_TERMINAL);
		
		uiContext = new Tc_UiContext();
		
		import_guistatus();

		// 	Perspectice selection	

		String wentThis = buildPerspectiveGuiElement(perspectiveName);	

		perspCombo.setSelectedItem(perspWrap(wentThis));

		perspCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	String gewaehlt =  perspNames[perspCombo.getSelectedIndex()];
		    	selectNewPersp(gewaehlt);
		    			    		
		      }
		    });
		
		// 	Workspace selection			
		wsCombo.setSelectedItem(wsWrap(CoreBuilder.ws.getWsId()));
		wsCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	int si = wsCombo.getSelectedIndex();
		    	String gewaehlt =  wsNames[si];
		    	CoreBuilder.ws.updateLastWorkspace(gewaehlt);
		    	refresh();
		    			    		
		      }
		    });		
		

		// 	Computer selection			
		// Sp.p("cp="+Conf.ws.getComputer());
		compCombo.setSelectedItem(compWrap(CoreBuilder.ws.getComputer()));
		compCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	int si = compCombo.getSelectedIndex();
		    	String gewaehlt;
		    	if ( si == 	compNamesWrap.length-1 )
		    		gewaehlt =  null;
		    	else
		    		gewaehlt =  compNames[si];
		    	CoreBuilder.ws.updateLastComputer(gewaehlt);
		    	refresh();
		    			    		
		      }
		    });			
		
		// 	Role selection		
		rolesCombo.setSelectedItem(roleWrap(CoreBuilder.ws.getRole()));
		rolesCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	int si = rolesCombo.getSelectedIndex();
		    	String gewaehlt =  rolesNames[si];
		    	CoreBuilder.ws.updateLastRole(gewaehlt);
		    	refresh();
		    			    		
		      }
		    });		
		
		// 	Sizes selection			
		sizesCombo.setSelectedItem(sizeWrap(guisize));
		sizesCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	int si = sizesCombo.getSelectedIndex();
		    	int gewaehlt =  sizesNames[si];
		    	selectNewGuisize(gewaehlt);		    	
		    			    		
		      }
		    });			
		
		// 	Auto selection			
		autoCombo.setSelectedItem(autoWrap(auto));
		autoCombo.addActionListener(new ActionListener() {

		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	int si = autoCombo.getSelectedIndex();
		    	boolean newauto =  autoNames[si];
		    	selectNewAuto(newauto);
		    			    		
		      }
		    });			
		
		jFrame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  try {
		    		  lc.core("Window CLOSE .................",LogConnector.CORE_TERMINAL);
			    	  shutdown();
		    	  } catch (Exception e1) {
		    		  Sp.pe(e1);
		    	  }
		      	}
			});			
		//		

		// 
		
		start();
	
		try {
			/*
			Element ti99 = new Element("ti");
			ti99.setAttribute("label","Welcome to XmlRobot (e:o)Testversion(e:c)");
			this.terminator.executeTi(ti99, Level.INFO, null, null,null,null,null);
			Element ti98 = new Element("ti");
			ti98.setText(Wellknown.copyright_xr_escaped);
			this.terminator.executeTi(ti98, Level.INFO, null, null,null,null,null);
			Element ti4 = new Element("ti");
			ti4.setText(Wellknown.license_line1_xr_escaped);
			// this.terminator.executeTi(ti4, Level.INFO, null, null,null,null,null);	
			Element ti5 = new Element("ti");
			ti5.setText(Wellknown.license_line2_xr_escaped);
			this.terminator.executeTi(ti5, Level.INFO, null, null,null,null,null);
			*/
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.pe(e1);
		}	
	
		PlugJar jar = new PlugJar();
		Class cls = jar.getClass();
		ProtectionDomain p = cls.getProtectionDomain();
		String jar_result = p.getCodeSource().getLocation().getPath();
		try {
			jar_result = URLDecoder.decode(jar_result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}	
		if ( CoreBuilder.ws.isWin && jar_result.startsWith("/"))
			jar_result = jar_result.substring(1);
	


		// startup
		runScCurrDb(Wellknown.startup_info, Interpreter.SILENCE_VERY);

		sendMapMessage(dummy,getCurrentContext());
		
		if ( Map_DbSelector.single != null ) {

			Map_DbSelector.single.setExpandedApps(gs.getExpanded_nodes());
		}
		
		sm.presentLc(lc);	
		
			java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				jFrame.setVisible(true);		

			}
			});
		
	}
	
	private void import_guistatus() {
		
		perspectiveName = gs.getPerspective();
		auto = gs.getAuto();
		jFrame.setSize(gs.getFrame_size_x(), gs.getFrame_size_y());
		jFrame.setLocation(gs.getFrame_loc_x(), gs.getFrame_loc_y());
		DbApp requested_app = CoreBuilder.db.getAppById(gs.getMarked_appid());
		if ( requested_app != null ) {
			// Sp.p("____ set app to:"+requested_app.getId());
			uiContext.setApp(requested_app.getId());
			DbPro requested_inst = requested_app.getInstById(gs.getMarked_instid());
			if ( requested_inst != null ) {
				uiContext.setInst(requested_inst.getId());
			}						
		}
		uiContext.setJob(gs.getMarked_jobid());
		CoreBuilder.rslogger.setLevel(LogLogger.string2Level(gs.getLoglevel()));
	}
	
	private void export_guistatus(String app, String inst ) throws IOException, StorageLimitException {
				
		gs.setPerspective(perspectiveName);
		gs.setFrame_size_x(jFrame.getSize().width);
		gs.setFrame_size_y(jFrame.getSize().height);
		gs.setFrame_loc_x(jFrame.getLocation().x);
		gs.setFrame_loc_y(jFrame.getLocation().y);
		if ( app == null )
			gs.setMarked_appid(uiContext.getApp());
		else
			gs.setMarked_appid(app);
		if ( inst == null )
			gs.setMarked_instid(uiContext.getInst());
		else
			gs.setMarked_instid(inst);
		gs.setMarked_jobid(uiContext.getJob());
		gs.setLoglevel(CoreBuilder.rslogger.getLevel().toString().toLowerCase());
		gs.setGuiSize(guisize);
		gs.setAuto(auto);

		if ( Map_DbSelector.single != null ) {
			gs.setExpanded_nodes(Map_DbSelector.single.getExpandedApps());			
		} else {
			gs.setExpanded_nodes(new Element("expanded_nodes"));
		}
		gs.writeFile();
	}	
	
	private String buildPerspectiveGuiElement ( String perspName ) {
		String retString = null;
		//System.out.println("buildPerspectiveGuiElement with ["+perspName+"]");
		perspective = perspManager.getPersp(perspName);
		if ( perspective == null ) {
			perspective = perspManager.getPersp("Rio +");
			retString = "Rio +";
		} else
			retString = perspName;
		perspective.build();
		
		ArrayList mapList = perspective.getMaps();
		Iterator iMapList = mapList.iterator();
		while (iMapList.hasNext()) {
			Tc_UiMap uiMap = (Tc_UiMap)iMapList.next();
			this.appendUiMapStd ( uiMap );
		}
		myContainer.add(perspective.getJComponent(),BorderLayout.CENTER);
		terminator = perspective.getTerminator();
		if ( terminator == null )
			terminator = new CoreDummyTerminator();
		return retString;
	}

	private static void buildJFrame() {
		jFrame = new JFrame();	
		jFrame.setTitle("XmlRobot.net - Echnaton - Testversion " );
	}
	
	private void selectNewPersp ( String newPerspName ) {
		perspectiveName = newPerspName;
		refresh();			
	}
	
	private void selectNewGuisize ( int newGuiSize ) {
		guisize = newGuiSize;
		refresh();			
	}	
	
	private void selectNewAuto ( boolean newAuto ) {
		auto = newAuto;	
	}	
	
	private void shutdown() throws Exception {
		this.terminator.flush();
		/*
		Element e1 = new Element("ti");
		e1.setText("Thanks for your cooperation(e:s) See you next time.");
		terminator.executeTi(e1, null, null, null,null,null,null);
		*/
		System.out.println("Thanks for your cooperation, See you next time.");
		//Interpreter.removeAllNoKEEP();
		Terminal.offlineSitzung.closeAllSessionCoops();
		systemSitzung.closeAllSessionCoops();
					
			try {
				runScThrow( "sys_shut_down", null, null, Interpreter.SILENCE_NO,null );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.p("service 'sys_shut_down' was not executed.");
			} 
				try {
					Sp.p("Shutting down ...");
					terminator.flush();
					export_guistatus(null,null);
					gs.writeFile();
					Tc_Modalmanager.modalmanager.closeDialog();
					jFrame.dispose();
					System.exit(0);
				} catch (Exception e) {
					Sp.p("SOME PROBLEM DURING SHUTDOWN !");
					Sp.pe(e);
				}

		
		

	}
	
	public void runScCurrDb( String _syscallid, int silence ) {
		if ( CoreBuilder.appManager.canRunNewJob(terminator)) {
		runSc(	_syscallid, 
				this.getCurrentContext().getApp(),
				this.getCurrentContext().getInst(),
				silence,
				this.getCurrentContext().getInstDir());	
		}

	}
	

	
	public void runSc( String _syscallid, String app, String inst, int silence, String instdir ) {

		AppSystemcall appSystemcall = null;
		
		try {
			appSystemcall = new AppSystemcall ( 
									_syscallid,
									app,
									inst,
						            null,
						            silence ,
						            terminator,
						            0,
						            instdir);
			appSystemcall.run();

		} catch (Exception e1) {

			Sp.p("Problem in starting terminal: "+e1.getLocalizedMessage()+" ExceptionClass: "+e1.getClass().getCanonicalName());
			LogConnector lce = new LogConnector("call_service: "+_syscallid);
			lce.printExceptionStack(e1);

		}

		CoreBuilder.rslogger.clearFatalStack();		

	}	
	
	public void runScThrow( String _syscallid, String app, String inst, int silence, String instdir ) throws Exception {

		AppSystemcall appSystemcall = null;
		

			appSystemcall = new AppSystemcall ( 
									_syscallid,
									app,
									inst,
						            null,
						            silence ,
						            terminator,
						            0,
						            instdir);
			appSystemcall.run();


		CoreBuilder.rslogger.clearFatalStack();		

	}		

	public void runJob(String job, String app, String inst, int silence,String instdir) {

		if ( CoreBuilder.appManager.canRunNewJob(terminator)) {
		if ( app!= null  && job!=null) { // && inst!=null

			String jobname = app+"_"+inst+"_"+job;

			AppJobexecutionThread appJobexecution = new AppJobexecutionThread (app,inst,job,null,false,Interpreter.SILENCE_NO,terminator,0,instdir);
			try {

				appJobexecution.run();
			} catch (Exception e) {
				lc.warn("Problematisch A: running job '"+jobname+"' caused an exception");
			}
			CoreBuilder.rslogger.clearFatalStack();
		} else
			lc.warn("Problematisch B");
	
		}
	}	
	
	public void runJobCurrDb( String job, int silence) {
		// perspective.beforeJobrun();
		Tc_UiContext uiContext = getCurrentContext();
		String app = uiContext.getApp();
		String inst = uiContext.getInst();
		String instdir = uiContext.getInstDir();
		lc.debug("starting job="+job+" app="+app+" pro="+inst);
		
		/*JOptionPane.showMessageDialog (null, 
				 "kukuk starting job="+job+" app="+app+" pro="+inst)
		;		*/
		runJob ( job, app, inst, silence,instdir );
		
	}		
	
	public synchronized void runProjectInfo ( ) {
		if ( CoreBuilder.appManager.canRunNewJob(terminator)) {
		Tc_UiContext currentContext = getCurrentContext();
		// Sp.p("Console.runProjectInfo.inst==1=="+currentContext.getInst());
		AppJobexecution x = new AppJobexecution( 	
				currentContext.getApp(),
				currentContext.getInst(),
				"??",
				null,false,Interpreter.SILENCE_NO, // Interpreter.SILENCE_VERY,
				terminator,
				0,
				null);
		try {
			x.run();
			terminator.flush();
			terminator.top();
		} catch (Exception ex) {
			Sp.pe(ex);
			lc.fatal("Could not run ??-Project Info for "+currentContext.getApp());
		}
		CoreBuilder.rslogger.clearFatalStack();		
		}
	}
	// nic123
	public void runAppHelp ( boolean fromDb) {
		if ( CoreBuilder.appManager.canRunNewJob(terminator)) {
			Tc_UiContext currentContext = getCurrentContext();
			String infoJobToDo = null;
			if ( ! fromDb )
				infoJobToDo = "?"; //app_has_static_pro app_has_static_pro_real
			else if (CoreBuilder.db.getAppById(currentContext.getApp()).app_has_static_pro())
				infoJobToDo = "???";
			else
				infoJobToDo = "?";
			// Sp.p("..... Run '"+infoJobToDo+"' for app "+currentContext.getApp());
			AppJobexecution x = new AppJobexecution( 	
					currentContext.getApp(),
					currentContext.getInst(),
					infoJobToDo,
					null,false,Interpreter.SILENCE_NO, // Interpreter.SILENCE_VERY,
					terminator,
					0,
					null);
			try {
				x.run();
				terminator.flush();
				terminator.top();
			} catch (Exception ex) {
				lc.fatal("Could not run ?-App Info for "+currentContext.getApp());
			}
			CoreBuilder.rslogger.clearFatalStack();	
		}
	}	
	public void runAboutJob (DbJob job, boolean isSystemcall ) {
		
		boolean has_explain = job.hasExplain();
		Tc_UiContext currentContext = getCurrentContext();
		// Sp.p("want to run ABOUT job"+job.getJobid());	
		
		Element tiJobLocations = null;
		if ( Zet_Privileg.viewJobSources() ) {
			
			printJob(job);
			
			tiJobLocations = new Element("steps");
			
			Element h1b=new Element("ti_hr");

			tiJobLocations.addContent(h1b);			
			
			Element h1=new Element("ti");
			h1.setAttribute("label", "Developer-Infos");
			tiJobLocations.addContent(h1);

			{
				Element ti1 = new Element("ti");
				ti1.setText("Locations of Source-Code for Job '"+job.getJobidEscaped()+"' are:");
				tiJobLocations.addContent(ti1);
			}
			Element ti1 = new Element("ti");
			ti1.setAttribute("type","file");
			ti1.setAttribute("label","Job:");
			ti1.setText(job.getFullfnEscaped());
			tiJobLocations.addContent(ti1);
			
			if ( has_explain ) {
				DbJob aboutJob = job.getJm().selectJob(job.getJobid()+"?");
				Element ti3 = new Element("ti");
				ti3.setAttribute("type","file");
				ti3.setAttribute("label","Help:");
				ti3.setText(aboutJob.getFullfnEscaped());
				tiJobLocations.addContent(ti3);

			}
			/*							
			try {
				terminator.executeTi(tiJobLocations, null, currentContext.getApp(), currentContext.getAppDir(), currentContext.getInst(), currentContext.getInstDir(),null);
				terminator.flush();
			} catch (Exception e1) {
			
			}	*/					
		}			
		if ( has_explain ) {
			
			try {

				if ( CoreBuilder.appManager.canRunNewJob(terminator)) {	
					
					if ( isSystemcall ) {
						AppSystemcall x = new AppSystemcall ( 	job.getJobid()+"?",
																currentContext.getApp(),
																currentContext.getInst(),
																null,Interpreter.SILENCE_NO,
																terminator,
																0,
																currentContext.getInstDir(),
																tiJobLocations);	
						x.run();					
					} else {
						AppJobexecution x = new AppJobexecution(currentContext.getApp(),
																currentContext.getInst(),
																job.getJobid()+"?",
																null,false,Interpreter.SILENCE_NO,
																terminator,
																0,
																tiJobLocations);
						x.run();					
					}
				}

				
			} catch (Exception e1) {
				lc.fatal("Could not run About for "+job.getJobid());
			}
			CoreBuilder.rslogger.clearFatalStack();
		} else {
			// no help job
						
			try {
				AppSystemcall x = new AppSystemcall ( 	
						"no-help",
						null,
						null,
						null,Interpreter.SILENCE_NO,
						terminator,
						0,
						currentContext.getInstDir(),tiJobLocations);	
				x.run();	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}
		}
		

		terminator.top();
		// terminator.gotoLastLine();
	}	
	public void refresh() {
		// terminator.setProgress("Restarting ...", true);
		refreshInternal(null, null);		
	}
	
	public void refresh(String app, String inst) {
		refreshInternal(app, inst);		
	}	
	
	private void refreshInternal(String app, String inst) {		
		boolean again = true;
		while ( again ) {
			again = false;
			
			CommStartMsg sm = new CommStartMsg();
			try {
				// Closing
				try {
					CoreBuilder.rslogger.removeAllAppender();
				} catch (Exception e) {
					lc.warn("Appenders could not be removed");
				}
				export_guistatus(app,inst);

				jFrame.dispose();
				Map_LogViewer.single = null;
				
				// Starting
				// Zet_ComLicenseOut.refresh();
				String dh = CoreBuilder.ws.getHome();

				CoreBuilder.build(CoreBuilder.ws.getWsFilename(),CoreBuilder.ws.getHome(),sm,InterfaceParas.getEmpty(),
						systemSitzung);
				Ti_TerminalLink.setup(false);
				buildJFrame();
				
				Tc_AppConsole appConsole3 = new Tc_AppConsole(
						jFrame.getContentPane(),
						this,systemSitzung);
				
				jFrame.setVisible(true);
				appConsole3.run(sm);
			} catch (Exception e) {
				try {
					Sp.p("Exception in terminal-startup: "+e.getLocalizedMessage()+" in class "+e.getClass().getCanonicalName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					
				}
				// TODO Auto-generated catch block
				again = sm.presentWindow(e);
				jFrame.dispose();
			} 
		}
	}
	


   //	*****************************************
	//  Einmelden einer neuen Map
   //	*****************************************
	public void appendUiMap (
		Tc_UiMap	 pUiMap,
		String pReceiveFrequency,
		String pSendFrequency )
   //	*****************************************
	{

		// Instanzieren eines neuen UiMapElem
		UiMapElem uiMapElem = new UiMapElem (
												pUiMap,
												pReceiveFrequency,
												pSendFrequency );

		uiMapList.add ( uiMapElem );

		pUiMap.setMapSetMaster ( this );

	}

	public void appendUiMapStd (
			Tc_UiMap	 pUiMap ) {
		appendUiMap(pUiMap,"B","B");
		
	}
	public void sendMapMessage (
			Tc_UiMap	pFromUiMap,
			Tc_UiContext _uiContext ) {
		sendMapMessageInternal( pFromUiMap, _uiContext,true,true);
	}
	public void sendMapMessageSwitch (
			Tc_UiMap	pFromUiMap,
			Tc_UiContext _uiContext ) {
		if ( auto )
			sendMapMessageInternal( pFromUiMap, _uiContext,false,false);
		else
			sendMapMessageInternal( pFromUiMap, _uiContext,true,false);
	}	
   //	*****************************************
	public synchronized void sendMapMessageInternal (
		Tc_UiMap	pFromUiMap,
		Tc_UiContext _uiContext,
		boolean showInfo,
		boolean careAuto)
   //	*****************************************
	{
		uiContext = _uiContext;
		// Variablen
		Iterator i = null;
		UiMapElem sendingUiMapElem = null;
		UiMapElem aktUiMapElem = null;

		// Ermitteln des sendingUiMapElem
		i = uiMapList.iterator();
		while ( i.hasNext() && sendingUiMapElem==null)
		{
			// Ermitteln des Aktuellen Keys
			aktUiMapElem = (UiMapElem)i.next();

			// Pr�fen der Selektionskriterien,
			// Selektiert werden jene Eintraege ohne parent
			if ( aktUiMapElem.uiMap == pFromUiMap  )
				sendingUiMapElem = aktUiMapElem;
		}

		if ( sendingUiMapElem == null )
		{
			Exception ex = new Exception ("sendingUiMapElem nicht gefunden");
			// lc.fatalException (ex );
		} else {
			
			if ( firstMessage)
				firstMessage = false;
			else if ( showInfo && ( auto || !careAuto) && uiContext.haveInst()) {
				// Sp.p("!!!! project info: i am ahere i am here: auto + "+uiContext.getInst());
				runProjectInfo();
			}			
			else if ( showInfo && ( auto || !careAuto) && uiContext.haveApp()) {
				Sp.p("Running app-help from db for "+uiContext.getApp());
				runAppHelp(true);
				
			}
			// Weitersenden auf der Frequenz von sen

			i = uiMapList.iterator();
			while ( i.hasNext() )
			{
				// Ermitteln des Aktuellen Keys
				aktUiMapElem = (UiMapElem)i.next();

				// Pr�fen ob der was empf�ngt
				if ( aktUiMapElem.receiveFrequency.equals (
							sendingUiMapElem.sendFrequency) && (aktUiMapElem!=sendingUiMapElem) )
					  // && aktUiMapElem != sendingUiMapElem )
				{
					aktUiMapElem.uiMap.bind ( );
				}
			}
		}
	}

	public void start ()
	{
		Iterator i = uiMapList.iterator();
		UiMapElem aktUiMapElem = null;

		while ( i.hasNext() )
		{
			// Ermitteln des Aktuellen Keys
			aktUiMapElem = (UiMapElem)i.next();
			aktUiMapElem.uiMap.start();
		}		
	}
	
  final class UiMapElem {
  
  	Tc_UiMap   	uiMap;			// die zugeordnete UiMap-Instanz
  	String   receiveFrequency;
  	String   sendFrequency;
  
  	UiMapElem ( Tc_UiMap  pUiMap,
  					String pReceiveFrequency,
  					String pSendFrequency )
  	{
  		uiMap = pUiMap;
  		receiveFrequency = pReceiveFrequency;
  		sendFrequency = pSendFrequency;
  	}
  }
  
  public Tc_UiContext getCurrentContext() {
  	return uiContext;
  }
  
  public void reload(Tc_UiMap pFromUiMap) {
  	// TODO Auto-generated method stub  	
  }
  
  public void printJob(DbJob job) {
	  Map_LogViewer.printJob(job);	  
  }
	public static Tc_AppConsole getInstance (  ) {		
		Tc_AppConsole appConsole3 = null;
		try {
			buildJFrame();
			
			appConsole3 = new Tc_AppConsole(	jFrame.getContentPane(),
											null, Terminal.offlineSitzung);
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			Sp.pe(e);
		}				
		return appConsole3;		
	}
	
	public void clearLog() {
		terminator.clear();
		Map_LogViewer.singleClear();
	}
	
	public void clearLogOnly() {
		Map_LogViewer.singleClear();
	}	
	
	public void runScLicense() {
		runScCurrDb(Wellknown.license, Interpreter.SILENCE_VERY);
		lc.info("XmlRobot: Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.");
	}	
	
	public void runScOpenFolder() {
		runScCurrDb(Wellknown.open_lowest_dir, Interpreter.SILENCE_VERY);
	}	

	public void runScHelp() {
		runScCurrDb(Wellknown.help, Interpreter.SILENCE_VERY);
	}	

	public void runDbAdmin() {
		runScCurrDb(Wellknown.dbAdmin, Interpreter.SILENCE_NO);
	}	
	
	public void runProject() {
		runScCurrDb(Wellknown.serviceProject, Interpreter.SILENCE_NO);
	}		
	public void runProjectManager() {
		runScCurrDb(Wellknown.serviceProjectManager, Interpreter.SILENCE_NO);
	}		
	
}