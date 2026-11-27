/* Copyright (C) 2015-2018 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
// zum Speichern der HTML-View auf dem lokalen filesystem, SAVE
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LogLogger;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.term_map.Map_TerminalInfoPanel;
import at.nicolas.x.term_ti.Ti_HtmlReturn;
import at.nicolas.x.term_ti.Ti_TerminalLink;
import at.nicolas.x.term_tk.Tester;
import at.nicolas.x.term_tk.Tk_TerminalKeyboardSequ;
import at.nicolas.x.term_tk.Tk_TkSelection;

/*key_space_used
 * red;
 * #870001;
 * #BA0001
 * #DB0000;
 * #540002;
 */
public class Tc_Terminator implements ITerminator, Tc_UiMap {

	private static boolean sage = false;
	
	final int KL_NLAB_NKEY = 1;
	final int KL_NLAB_KEY  = 2;
	final int KL_LAB_NKEY  = 3;
	final int KL_LAB_KEY   = 4;	

	private int tk_width = 113;
	
	JComboBox stopCombo ;
	JPanel panel;
	JEditorPane jEditorPaneTi = null;
	JScrollPane jScrollPaneTi = null;
	
	TextArea	progress = null;
	
	String lastfile = null;
	private LogConnector lc = new LogConnector("ti");
	// private LogConnector lcp = new LogConnector("Progress");
	private LogConnector lcp2 = new LogConnector("tp ");
	Document doc = null;
	
	private ArrayList<Seitenlinks> seitenlinks = null;
	private Seitenlinks currentSeitenlinks = null;
	private int seitenlinksindex = 0;
	
	private int cols = 70;
	private int thirty = 36;
	private Tc_Terminator me;
	
	private Map_TerminalInfoPanel terminalInfoPanel = null;

	private static String [] size_head_in = {
			"<div  class='tiLabelOnly tiLabelOnly0'>",
			"<div style='font-weight:bold;' class='tiLabelOnly tiLabelOnly1'>",
			"<div style='font-weight:bold;' class='tiLabelOnly tiLabelOnly2'>",
			"<div style='font-weight:bold; font-size:large;' class='tiLabelOnly tiLabelOnly3'>",
			"<div style='font-weight:bold; font-size:x-large;' class='tiLabelOnly tiLabelOnly4'>",
"<div style='font-weight:bold; font-size:xx-large;' class='tiLabelOnly tiLabelOnly5'>",
"<div style='font-weight:bold; font-size:xxx-large;' class='tiLabelOnly tiLabelOnly6'>"}
	
	;
	private static String [] size_head_in_mac = {
		"<div class='tiLabelOnly tiLabelOnly0' >",
		"<div class='tiLabelOnly tiLabelOnly1' style='font-weight:bold; padding-top: 4px;'>",
		"<div class='tiLabelOnly tiLabelOnly2' style='font-weight:bold; padding-top: 5px; padding-bottom: 1px; font-size:large;'>",
		"<div class='tiLabelOnly tiLabelOnly3' style='font-weight:bold; padding-top: 6px; padding-bottom: 2px; font-size:x-large;'>",
		"<div class='tiLabelOnly tiLabelOnly4' style='font-weight:bold; padding-top: 6px; padding-bottom: 2px; font-size:xx-large;'>",
		"<div class='tiLabelOnly tiLabelOnly4' style='font-weight:bold; padding-top: 6px; padding-bottom: 2px; font-size:xxx-large;'>",
		"<div class='tiLabelOnly tiLabelOnly4' style='font-weight:bold; padding-top: 7px; padding-bottom: 3px; font-size:xxxx-large;'>"};	
	private static String [] size_head_in_error = {
								"<div class='tiLabelError tiLabelError0' >",
								"<div class='tiLabelError tiLabelError1' style='font-weight:bold; color:#870001; text-align:center;'>",
								"<div class='tiLabelError tiLabelError2' style='font-weight:bold; font-size:large; color:#870001; text-align:center;'>",
								"<div class='tiLabelError tiLabelError3' style='font-weight:bold; font-size:x-large; color:#870001; text-align:center;'>",
								"<div class='tiLabelError tiLabelError4' style='font-weight:bold; font-size:xx-large; color:#870001; text-align:center;'>",
								"<div class='tiLabelError tiLabelError4' style='font-weight:bold; font-size:xxx-large; color:#870001; text-align:center;'>",
								"<div class='tiLabelError tiLabelError4' style='font-weight:bold; font-size:xxxx-large; color:#870001; text-align:center;'>"};

	private static String [] size_head_out = {
								"",
								"</div>",
								"</div>",
								"</div>",
								"</div>",
								"</div>",
								"</div>"
								};
	
	private DefaultCaret caret = null;
	
	private ArrayList<Tc_TiPage> pageList = new ArrayList<Tc_TiPage>();
	private int pagePosition = 0;
	private int visiblePagePosition = -1;
	
	private String screenString = new String("");
	private String flushString = new String("");

	private static String static_w1;
	private String twoDigitString(int number) {

	    if (number == 0) {
	        return "00";
	    }

	    if (number / 10 == 0) {
	        return "0" + number;
	    }

	    return String.valueOf(number);
	}
	private String tpFinal = null;
	private String tpLast = null;
	public void setProgressFinal ( String msg ) {
		tpFinal = msg;
		
	}
	public void setProgress ( String msg, boolean doLcInfo ) {

		int seconds = (int)(System.currentTimeMillis()-clockStart)/1000;
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		String timeString = null; 
		if ( hours > 0 )
			timeString = twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds); 
		else if ( minutes > 0 )
			timeString = twoDigitString(minutes) + ":" + twoDigitString(seconds);
		else
			timeString = twoDigitString(seconds);

		String msg2= Entities.exportEntities(msg);
		String w1 =  new String( timeString +": "+msg2);
		
		if ( !stilIsWeb)
			progress.setText(w1);
		tpLast = w1;
		// progress.set
		if ( doLcInfo ) {		
			lcp2.warn(timeString +": "+msg2);		
		}
	}	
	
	public void setProgressLength( int x ) {
		cols = x;
	}
	/*
	public Tc_Terminator ( ) {
		me=this;
	} */
	// private boolean webModus = false;
	private LogLogger logicLogger;
	// private LogConnector lc;
	private IGuiController guiController;
	private ISitzung userSitzung;
	// private Surrounding surrounding =null;
	
	private boolean isWeb = false;
	public static TiStyler stil=null;

	public static boolean stilIsWeb = false;
	
	// vom web
	public Tc_Terminator ( boolean addProgressPane, IGuiController guiController, ISitzung benutzerSitzung
			// ,Surrounding surrounding
			) {
		/*[echnaton-crypto]*/

		// this.surrounding = surrounding;
		userSitzung = benutzerSitzung;
		me=this;
		this.addProgressPane = addProgressPane;
		this.guiController = guiController;
		isWeb = this.guiController.isSocalledWeb();
		if ( stil== null ) {
			if ( isWeb ) {
				stil = new TiStyler();
			
			} else {
				TiStylerDesktop tiStylerDesktop = new TiStylerDesktop(table_border);
				stil = (TiStyler)(tiStylerDesktop);	
				


			}
			stilIsWeb = isWeb;
		}

		
		
		lcp2.setDotsOff(true);
		
		// lc = logicLogger
		// logicLogger.
		// lc.setDotsOff(true);
	}
	// vom offline
	public Tc_Terminator ( boolean addProgressPane, ISitzung benutzerSitzung ) {
		/*[echnaton-crypto]*/
		me=this;
		userSitzung = benutzerSitzung;
		this.addProgressPane = addProgressPane;
		
		
		lcp2.setDotsOff(true);
		this.guiController = new GuiControllerDesktop();
		isWeb = false;
		if ( stil== null ) {
			TiStylerDesktop tiStylerDesktop = new TiStylerDesktop(table_border);
			stil = (TiStyler)(tiStylerDesktop);	
			

			
		}
		stilIsWeb=false;
		// this.surrounding = Surrounding.getSurroundingDesktop();

	}	
	/*
	public Tc_Terminator ( boolean addProgressPane ) {
		me=this;
		this.addProgressPane = addProgressPane;

		lc.setDotsOff(true);
		lcp2.setDotsOff(true);
	} */
	
	private JPanel  	progressPanel = null;	

	private boolean addProgressPane = true;
	private int tp_lines = 1;
	
	public void setTpLines ( int tp_lines ) {
		this.tp_lines = tp_lines;
	}
	
	private String location_halt = BorderLayout.EAST;
	public void setLocationHalt ( String location_halt ) {
		this.location_halt = location_halt;
	}
	

	/*
	public void setTpFont ( Font tpFont ) {
		this.tpFont = tpFont;
	}
	public void setTpFontSize ( int size ) {
		tpFont = new Font ("Arial",0 , size);
	}
	*/
	
	private Dimension pref_dim_tp = new Dimension(12,27);
	public void setPrefDimTp ( Dimension pref_dim_tp ) {
		this.pref_dim_tp = pref_dim_tp;
	}
	private ITerminatorStyler ts;
	public ITerminatorStyler getStyler() {
		return ts;
	}
	public void setupDone (  ) {

		seitenlinks = new ArrayList<Seitenlinks>();
		if ( guiController == null ) {
			Sp.p("der dumme guicontroller ist null");
		}

		if ( stilIsWeb ) {
			Sp.pl("Parada x99");
			ts = new TerminatorStylerEe();
			return;
		} else
			ts = new TerminatorStylerDesktop();

		
		if ( logicLogger == null) 
			logicLogger = CoreBuilder.rslogger;
		


		progress = new TextArea("",tp_lines,10,TextArea.SCROLLBARS_NONE); // .SCROLLBARS_VERTICAL_ONLY);

		progress.setEditable(false);	
		


		

		progress.setFont(new Font("Arial",0 , Tc_AppConsole.guiSizer.tpFontSize));
		progress.setBackground(Color.LIGHT_GRAY);

		progressPanel  = new JPanel(new BorderLayout());
		if ( pref_dim_tp != null ) {
			progressPanel.setPreferredSize(pref_dim_tp);
		}		
		progressPanel.add(progress,BorderLayout.CENTER);
		

		JButton stopButton = new JButton("P");

		stopButton.setToolTipText("Pauses the execution of a job; will be Continued on request ...");
		stopButton.addActionListener(new StopButtonHandler());
		JButton goButton = new JButton("C");
		goButton.setToolTipText("Continues the execution of a job after Pausing it ");
		goButton.addActionListener(new GoButtonHandler());		

		JButton haltButton = new JButton("X");
		haltButton.setPreferredSize(new Dimension(40,40));
		haltButton.setToolTipText("Stops the execution of a job as much as possible");
		HaltButtonHandler haltButtonHandler = new HaltButtonHandler();
		haltButton.addActionListener(haltButtonHandler);

		JButton boneButton = new JButton("B");
		boneButton.setToolTipText("Break on next occasion: File-Scan will terminate");
		BoneButtonHandler boneButtonHandler = new BoneButtonHandler();
		boneButton.addActionListener(boneButtonHandler);

		JButton blButton = new JButton("Bl");
		blButton.setToolTipText("Break Loops: Break Language Loops");
		BlButtonHandler blButtonHandler = new BlButtonHandler();
		blButton.addActionListener(blButtonHandler);

		JButton alButton = new JButton("Al");
		alButton.setToolTipText("Activate Loops, Undo the Breakin of the Loops");
		AlButtonHandler alButtonHandler = new AlButtonHandler();
		alButton.addActionListener(alButtonHandler);

		JButton psButton = new JButton("Ps");
		psButton.setToolTipText("Put all Threads to sleep ...");
		PsButtonHandler psButtonHandler = new PsButtonHandler();
		psButton.addActionListener(psButtonHandler);
		

		JPanel yPanel = new JPanel(new GridLayout(2,3));

		yPanel.add(stopButton);
		yPanel.add(goButton);
		yPanel.add(boneButton);
		yPanel.add(psButton);
		yPanel.add(alButton);
		yPanel.add(blButton);
		if ( Tc_AppConsole.mac)
			yPanel.setPreferredSize(new Dimension(80,12));
		Sp.pl("Parada 16");		
		JPanel xPanel = new JPanel(new BorderLayout());
		xPanel.add(yPanel,BorderLayout.WEST);
		xPanel.add(haltButton,BorderLayout.CENTER);

		String stopList[] = new String[3];
		stopList[0] = "Go";
		stopList[1] = "Sleep";
		stopList[2] = "Stop";
		
		stopCombo = new JComboBox(stopList);
		stopCombo.setSelectedIndex(0);
		JPanel stopPanel = new JPanel();
		stopPanel.add(stopCombo);
		stopCombo.addActionListener(new StopComboListener());
	
		terminalInfoPanel = new Map_TerminalInfoPanel ( progressPanel, xPanel ); //xPanel

		jEditorPaneTi = new JEditorPane();
		jEditorPaneTi.setContentType("text/html");
		jEditorPaneTi.setEditable(false);
		

		jScrollPaneTi = new JScrollPane(jEditorPaneTi);
		if ( CoreBuilder.ws.isTablet) {
			jScrollPaneTi.getVerticalScrollBar().setPreferredSize(new Dimension(100,0));
		}

		jEditorPaneTi.setText("<html><body>Welcome to XmlRobot<br/>Copyright (C) 2015-2021 Nicolas Prandstaetter. All rights reserved.<br/></body</html>");
		
		caret = (DefaultCaret)jEditorPaneTi.getCaret();

		doc = jEditorPaneTi.getDocument();

				
		
		
		
		jEditorPaneTi.addHyperlinkListener (
			    new HyperlinkListener () {
			  public void hyperlinkUpdate(HyperlinkEvent e) {

			    if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {

			     } else if (e.getEventType() == 
			                          HyperlinkEvent.EventType.ACTIVATED) {
			    	 
			    	 String href = e.getDescription(); 
			    	 
			    	 Iterator<Seitenlinks> j = seitenlinks.iterator();
			    	 Ti_TerminalLink found = null;
			    	 
			    	 while ( j.hasNext() && found==null) {
			    		 Seitenlinks sl = (Seitenlinks)j.next();
			    		 
				    	 Iterator<Ti_TerminalLink> i = sl.tlList.iterator();
	
				    	 
				    	 while ( i.hasNext() && found==null) {
				    		 Ti_TerminalLink ctl = (Ti_TerminalLink)i.next();
				    		 if ( ctl.reactsOn(href)) { /*  || ctl.getRefCode().equals(href) */
				    			 found = ctl;
				    		 }
				    	 }			    		 
			    		 
			    	 }
			    	 //
			    	 
			    	 if ( found != null ) {
			    		 // found.executeLink();
			    		 found.reactOn(href);
			    	 }
			    	 
			      } else if (e.getEventType() == 
                          HyperlinkEvent.EventType.ENTERED) {

				  } 
			      
			   }
			  });	

		panel = new JPanel ( new BorderLayout());
		panel.add(jScrollPaneTi,BorderLayout.CENTER);
		if (  addProgressPane )
			panel.add(terminalInfoPanel.getJComponent(),BorderLayout.NORTH);
				
	}
		
	private class HaltButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Stop-Button pressed.");
			Interpreter.break_loops = true;
			Interpreter.break_one = true;
			
			Interpreter.setStopGo(false);
			setProgress("Stopped All upon your request", false);
			int pos1 = jEditorPaneTi.getCaretPosition();
			flush();
			
			jEditorPaneTi.setCaretPosition(pos1);
			CoreBuilder.appManager.halt(me);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}
		}
	}	
	
	private class PsButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Put To Sleep-Button pressed.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}

			CoreBuilder.appManager.sleepAll(me);
		}
	}		
	private class BoneButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Bone-Button pressed.");
			Interpreter.break_one = true;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}

		}
	}		
	
	private class BlButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Break-Loops-Button pressed.");
			Interpreter.break_loops = true;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}

		}
	}	
	
	private class AlButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Activate-Loops-Button pressed.");
			Interpreter.break_loops = false;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}

		}
	}	
	
	private class StopButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Pause-Button pressed.");

			setProgress(" Pause-Button pressed", false);
			Interpreter.setStopGo(true);
		}
	}		
	
	private class GoButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.error(" Continue-Button pressed.");
			setProgress(" Continue-Button pressed", false);
			Interpreter.setStopGo(false);
		}
	}		
	public void clear() {
		// JOptionPane.showMessageDialog(null,"Terminator: clear()");
		// seitenlinks = new ArrayList<Seitenlinks>();
		if (sage) Sp.p("++++ Tc_Terminator.clear() pagePosition="+pagePosition+", pageList.size()="+pageList.size());
		jEditorPaneTi.setText("<html><body></body</html>");
		screenString = new String("");
		flushString = new String("");
		progress.setText("");
		pagelistClear();
		pagePosition = 0;
	}
	private void pagelistClear() {
		if (sage) Sp.p("++++ +++ Tc_Terminator.pagelistClear() BEGIN pagePosition="+pagePosition+", pageList.size()="+pageList.size());		
		pageList = new ArrayList<Tc_TiPage>();
		if (sage) Sp.p("++++ +++ Tc_Terminator.pagelistClear() FINISHED pagePosition="+pagePosition+", pageList.size()="+pageList.size());				
	}
	public JComponent getJComponent() {
	
		return ((JComponent)panel);
	}
	
	private void performLogInfo ( String msg, boolean important, Level loglevel ) {
		if ( loglevel !=null ) {
			String z = Commons.unescapeXml(msg);

			if ( z.indexOf("&radic;") == -1) {
				z =  Commons.decodeXmlEntitiesXr(z);
				
				z = z.replaceAll("\n", "\n  "+CoreBuilder.rslogger.getCurrentPrefix()+Commons.StringTimes(" ", thirty));
				
				if ( important )
					lc.log(" "+z.toUpperCase(), loglevel);
				else 
					lc.log(" "+z, loglevel);		

							
			}

		}
	}
	private String table_border="0";
	private boolean tk_align_right=false;
	private boolean tk_msg_right=true;
	private int tk_wide_size=170;
	private int tk_wide_intro_size=30;
	private int tk_wide_extro_size=30;
	private boolean tk_wide_on = false;
	private String getTiHeader() {
		return "<table "+stil.getTable0()+">";
	}
	private String getTiCloser() {
		return "</table>";		
	}	
	

	public void executeTi ( Element tixE, Level loglevel, String app, String appdir, String inst, String instdir, ITiHandler rh) throws Exception {
		if (sage) Sp.p("++++ Tc_Terminator.executeTi()"+" pagePosition="+pagePosition+", pageList.size()="+pageList.size());	
		// dirty(tixE,rh);
		// Sp.p("Terminator.executeTi: app="+app+", inst="+inst+", instdir="+instdir);
		/*
		Sp.p("--------execute Ti--------------");
		Sp.p(Commons.toText(tix));
		Sp.p("--------------------------------");
		*/
		// Element tix_beautiful = tix.clone();
		// export_ti(tix_beautiful);
		// Sp.p("e-executeTi-Before="+Commons.toTextRaw(tix));
		// Sp.p("e-executeTi-After="+Commons.toTextRaw(tix_beautiful));
		String nextLine = getTiLine ( tixE, loglevel, app, appdir,inst,instdir,rh );
		//System.out.println("next lin="+nextLine);
		addToFlush( nextLine );
		
		
	}

	private void addToFlush( String add) {
		if (sage) Sp.p("++++ Tc_Terminator.addToFlush() STARTED add="+add+" pagePosition="+pagePosition+", pageList.size()="+pageList.size());		
		if ( add != null )
			flushString = flushString.concat(add);	
		if (sage) Sp.p("++++ Tc_Terminator.addToFlush() FINISheD flushString="+add);				
	}
	


	private void pagelistAdd( Tc_TiPage page ) {
		if (sage) Sp.p("++++ +++ Tc_Terminator.pagelistAdd()  STARTED "+" pagePosition="+pagePosition+", pageList.size()="+pageList.size());			
		//pageList.add(page);
		if ( isWeb || pageList.size() == 0 ) {
			pageList.add(page);
			pagePosition = pageList.size();
		} else {
			if ( pagePosition > 0 ){
				pageList.add(pagePosition,page);
				pagePosition++;
			} else {
				pageList.add(page);
				pagePosition = pageList.size();
			}
		}
		
		if (sage) Sp.p("++++ +++ Tc_Terminator.pagelistAdd()  FINISHED "+" pagePosition="+pagePosition+", pageList.size()="+pageList.size()+", content="+page.content);				
	}
	public void clearScreen() {
		if (sage) Sp.p("++++ +++ Tc_Terminator.clearScreen() STARTED pagePosition="+pagePosition+", pageList.size()="+pageList.size());
		if ( stilIsWeb)
			pagelistAdd(new Tc_TiPage(screenString));
		else
			pagelistAdd(new Tc_TiPage(screenString,jEditorPaneTi,jScrollPaneTi,lc));
		// lc.warn(" clearScreen: new TiPage(currentContent), pos="+(pageList.size()-1));
		
		screenString = new String("");
		visiblePagePosition = -1;
		if (sage) Sp.p("++++ +++ Tc_Terminator.clearScreen() FINISHED pagePosition="+pagePosition+", pageList.size()="+pageList.size());		

	}


	
	public void backButtonPressed() {
		if (sage) Sp.p("++++ Tc_Terminator.backButtonPressed() STARTED pagePosition="+pagePosition+", pageList.size()="+pageList.size());
		
		if (sage) printPages("intro");

		if ( pagePosition == pageList.size() ) {
			if (sage) Sp.p("    A");
			// visible page-pos muss -1 sein
			if ( visiblePagePosition != -1) {
				lc.warn("  VERDAMMT.--------------------------");
				if (sage) Sp.p("    B");
			}
		
			// pagelistAdd(new Tc_TiPage(screenString,jEditorPaneTi,jScrollPaneTi,lc)); gummi
			if (sage) Sp.p("    C");
			
		}
		if (sage) printPages("da");
		int oldPos = pagePosition;
		if (sage) Sp.p("    D oldPos="+oldPos+", pagePosition="+pagePosition);
		pagePosition--;
		if (sage) Sp.p("    E pagePosition="+pagePosition);
		if ( pagePosition <= 0 ) {
			
			pagePosition=1;
			if (sage) Sp.p("    F pagePosition="+pagePosition);
		}
		

		showPage(pagePosition-1,oldPos-1);

		if (sage) Sp.p("++++ Tc_Terminator.backButtonPressed() FINISHED pagePosition="+pagePosition+", pageList.size()="+pageList.size());
		}
	private void printPages(String titel) {
		if (sage) Sp.p("--- printing pages ---" +titel +"----");
		Iterator<Tc_TiPage> i = pageList.iterator();
		int p = 0;
		while ( i.hasNext() ) {
			Tc_TiPage r = i.next();
			if (sage) Sp.p(p+" = "+r.content);
			p++;
			
		}
	}
	public void foreButtonPressed() {
		if (sage) Sp.p("++++ Tc_Terminator.foreButtonPressed() pagePosition="+pagePosition+", pageList.size()="+pageList.size());	
		if (sage) Sp.p("  A  pagePosition="+pagePosition);
		if ( pagePosition == pageList.size() ) {
			if (sage) Sp.p("  B  pagePosition="+pagePosition);
			if ( visiblePagePosition != -1) {
				lc.warn("  VERDAMMT 2.--------------------------");
				if (sage) Sp.p("  C  pagePosition="+pagePosition);
			}
			return;
		}
		if (sage) Sp.p("  D  pagePosition="+pagePosition);
		int oldPos = pagePosition;
		pagePosition++;
		if (sage) Sp.p("  E  pagePosition="+pagePosition);
		if ( pagePosition > pageList.size() ) {
			pagePosition=pageList.size()-1;
			if (sage) Sp.p("  F  pagePosition="+pagePosition);
		}

		if (sage) Sp.p("    pagePosition="+pagePosition+", oldPos="+oldPos);
		if (sage) printPages("foreward");
		showPage(pagePosition-1,oldPos-1);

		

	}
	private void showPage( int nextPos, int oldPos ) {
		if (sage) Sp.p("++++ Tc_Terminator.showPage() pagePosition="+pagePosition+", pageList.size()="+pageList.size()+", nextPos="+nextPos+", oldPos="+oldPos);
		pageList.get(oldPos).storeCaretPoint(jEditorPaneTi, jScrollPaneTi,lc);
		screenString = new String("");
		flushString = new String("");		
		if (sage) Sp.p("++++ Tc_Terminator.showPage() going to addtoflush="+pageList.get(nextPos).content);
		addToFlush(pageList.get(nextPos).content);
		flushInternal(false);
		pageList.get(nextPos).applyCaretPoint(jEditorPaneTi, jScrollPaneTi,lc);
		visiblePagePosition = nextPos;
		
	}

	
	private String getBodyTag() {
		String x = new String( Tc_AppConsole.guiSizer.tiBodyTag);
		return x;

	}
	


	private String wrapScreenWithHtml( String screen ) {
		String toPrint = "<html>"+getBodyTag()+"<div style='min-width:260px;'>"
				+ getTiHeader() + 
				screen + 
				getTiCloser() +
				"</div></body></html>";	
		return toPrint;
	}
	
	private String wrapScreenAsBodyMember( String screen ) {
		if ( isWeb)
			return  "<div class=\"ti_screen\">"
				+ getTiHeader() + 
				screen + 
				getTiCloser() +
				"</div>";	
		else
			return  "<div style='min-width:260px;'>"
			+ getTiHeader() + 
			screen + 
			getTiCloser() +
			"</div>";	
	}
	
	private void flushInternal(boolean doRegisterFlushedContent ) {
		if (sage) Sp.p("++++ Tc_Terminator.flushInternal() pagePosition="+pagePosition+", pageList.size()="+pageList.size() +", doRegisterFlushedContent="+doRegisterFlushedContent);	
		if ( flushString.length() > 0 ) {
			if ( doRegisterFlushedContent && !isWeb) {
				try {
					pageList.get(pagePosition-1).addContent(flushString);
				} catch (Exception e) {
					if (sage) Sp.p("++++ Tc_Terminator.flush() HAS PROBLEM pagePosition="+pagePosition);
				}				
			}
			if (sage) Sp.p("       screenString="+screenString);
			if (sage) Sp.p("        flushString="+flushString);
			screenString = screenString.concat(flushString);
			flushString = new String("");
			String toPrint = wrapScreenWithHtml(screenString);
			if ( !stilIsWeb)
			jEditorPaneTi.setText(toPrint);	
			// hier den pagelistcontent updaten aber nur bei desktop!!
			
			
		}

	}		
	public void flush() {
		if (sage) Sp.p("++++ Tc_Terminator.flush() pagePosition="+pagePosition+", pageList.size()="+pageList.size());	
		flushInternal(true);


	}	
	
	private String getTiLine ( Element tixE, Level loglevel, String app, String appdir, String inst, String instdir, ITiHandler rh) throws Exception {
		// System.out.println("yyyyyyyyyy tiline with inst:"+instdir);
		// Sp.p("e-getTiLine="+Commons.toTextRaw(tix));
		// String ith2 = "<table border='"+table_border+"' width='100%' cellpadding='0' cellspacing='0'>";	
		String ith1 = "<table "+stil.getTable2()+">";
		
		boolean hasText = false;
		if ( tixE.getAttribute("type")!= null ){ 
			hasText = true;
		} else if ( tixE.getAttribute("x")!= null || 
				(tixE.getText() != null && tixE.getText().length() > 0)){ //  
			// Sp.p("text=["+tix.getText()+"]"+tix.getTextNormalize()+"]"+tix.getTextTrim()+"]");
			hasText = true;
		} /*else {
			Sp.p("attributevalue=["+tix.getAttributeValue("x")+"]");
			Sp.p("text=["+tix.getText()+"]");
			if (tix.getText() != null)
				Sp.p("textlen=["+tix.getText().length()+"]");			
		}*/
		Element tiE;
		String tipi = tixE.getAttributeValue("type");
		if ( tipi == null ) tipi ="text";
		if ( tipi.equals("call_system")) {
			JOptionPane.showMessageDialog(null,"Terminator: call_system is depressive");
		}
		// hund
		if ( (tixE.getChildren("ti").size() == 0 && (hasText || tipi.equals("call_job") || tipi.equals("call_service")))
				|| ( tipi.equals("rec") || tipi.equals("menu") )) {
			tiE = new Element ("ti");
			tiE.addContent(tixE.clone());

		} else {
			tiE = tixE;
		}
		
		// Check main-test
		// if ( tiE.getAttribute("test")!=null  && tiE.getAttributeValue("test").equals("0"))
		if ( Tester.testOrTestdynAttributeIs0(tiE,rh))
			return null;

		// Result initial
		String toadd = new String("");

		// Headline
		if ( tiE.getAttribute("label") != null) {
			String text_head = ""; // HTML
			String html_head = ""; // TEXT LOGGER
			String size = tiE.getAttributeValue("size");
			if ( size == null )
				size = "1";
			int size_int = Commons.string2int(size);
			size_int = Tc_AppConsole.guiSizer.scaleSize(size_int);
			if (size_int < 0 ) {
				size_int = 0; // throw new Exception("ti size must be between 0 and 4, but it is: "+size_int);
			} else 	if ( size_int > 6 ) {
				size_int = 6;
			}
			
			if ( tiE.getAttributeValue("label") != null ) {
				if ( Tc_AppConsole.mac ) {  
					html_head = html_head + 
							size_head_in_mac[size_int] + 
							Ti_TerminalLink.getHtmlByAttrFromEscaped (tiE, "label",  rh) + // ti.getAttributeValue("label") +  
							size_head_out[size_int];					
				} else {
					// System.out.println("-----------------");
					
					html_head = html_head + 
							size_head_in[size_int] + 
							//"KK "+ Ti_TerminalLink.getAttUnescapedXml (tiE, "label",  rh) + 

							Ti_TerminalLink.getHtmlByTextFromEscaped(tiE.getAttributeValue("label")) +
							size_head_out[size_int];					
				}

				// if ( ti.getAttributeValue("label").startsWith(Constants.errorPrefix)) {
				if ( Commons.hasFlag(tiE, "error")) {
					String netto = (Ti_TerminalLink.getHtmlByAttrFromEscaped (tiE, "label",  rh));

					html_head = // Constants.errorPrefix + 
							size_head_in_error[size_int] +
							netto + 
							size_head_out[size_int]
									;
					
				}
				
				text_head = text_head + " X34 "+Ti_TerminalLink.getHtmlByAttrFromEscaped (tiE, "label",  rh);
			}
			String theStyle;
			if ( isWeb) {
				theStyle = " class=\"tiLabelOnly\" "; //tiLabelOnly
			} else {
				String style = " style='";
				
				if ( tiE.getAttributeValue("padding_top") != null) {
					style = style.concat("padding-top: "+tiE.getAttributeValue("padding_top")+";");				
				} else {
					style = style.concat("padding-top: 2px;");
				}
				if ( tiE.getAttributeValue("padding_bottom") != null) {
					style = style.concat("padding-bottom: "+tiE.getAttributeValue("padding_bottom")+";");				
				}			
				
				style = style+"' ";	
				theStyle = style;
				
			}
			
	
			
			if ( tiE.getAttribute("key") == null) {
				// nothing on right side
				/* before bigtable
				toadd = "<tr><td colspan='2'"+style+">"+html_head+"</td></tr>";		
				*/
				toadd = "<tr><td colspan='12'"+theStyle+">"+html_head+"</td></tr>";	
			} else {
				// something on right side
				/* before bigtable
				toadd = "<tr><td colspan='2'"+style+">"+
						ith1 + "<tr><td>" + html_head + "</td><td align='right' valign='bottom'>"+ ti.getAttributeValue("right") 
						+"</td></tr></table></td></tr>";
				*/
				toadd = "<tr><td colspan='12'"+theStyle+">"
				+ith1+"<tr><td>"
				+html_head
				+"</td><td align='right' valign='bottom'>"
				+ Ti_TerminalLink.getHtmlByAttrFromEscaped (tiE, "key",  rh)
				+"</td></tr></table></td></tr>";
				
				text_head = text_head + " - "+Ti_TerminalLink.getHtmlByAttrFromEscaped (tiE, "key",  rh);
			}
			
			performLogInfo ( text_head, true, loglevel );
				
		}

		List<Element> l = tiE.getChildren("ti");
		Iterator<Element> i = l.iterator();
	
		while ( i.hasNext() ) {

			Element eE = (Element)i.next();
			boolean doit = true;

			// if ( eE.getAttribute("test")!=null  && eE.getAttributeValue("test").equals("0"))
			if ( Tester.testOrTestdynAttributeIs0(eE,rh))
				doit = false;

			if (  doit ) {
	
				// Modern
				Ti_TerminalLink newT2 = Ti_TerminalLink.getTerminalLink(eE,this,app,appdir,inst,instdir,rh);
				if ( newT2 != null ) {
					// Sp.p("newT2="+newT2.getHtml());
					try {
						currentSeitenlinks.tlList.add(newT2);
					} catch (Exception e) {
						if (sage) Sp.p("$$$$$$$$$ received ti-info without init: "+Commons.toText(eE));
					}
					// tlList.add(newT2);
					Ti_HtmlReturn hr = newT2.getHtml();
					
					// anfang
					for ( int iMenu = 0; iMenu<hr.menuList.size(); iMenu++ ) {
						Ti_TerminalLink link = hr.menuList.get(iMenu);
						
						try {
							currentSeitenlinks.tlList.add(link);
						} catch (Exception e) {
							if (sage) Sp.p("$$*$*$$$*$$ received ti-info without init: "+Commons.toText(eE));
						}						
					}					
					
					// ende
					
					// feststellen key und label 
					int kl_type;
					if ( hr.getLabel()==null && hr.getKey()==null)
						kl_type = KL_NLAB_NKEY;
					else if ( hr.getLabel()==null && hr.getKey()!=null)
						kl_type = KL_NLAB_KEY;
					else if ( hr.getLabel()!=null && hr.getKey()==null)
						kl_type = KL_LAB_NKEY;
					else 
						kl_type = KL_LAB_KEY;
					
					String html="";
					if ( !hr.is_no_rec_menu) {
						// its a record			
						String master_class = "";
						if ( eE.getAttribute("master_class")==null )
							master_class = "appmenu";
						else if ( eE.getAttributeValue("master_class").length() == 0 ) 
							master_class = "appmenu";
						else if ( eE.getAttributeValue("master_class").trim().length()>0) {
							master_class = eE.getAttributeValue("master_class");
						}

						
						String theWidth = "";
						if ( eE.getAttribute("width")!=null) {
							theWidth = " width=\""+eE.getAttributeValue("width")+"\"";
						}			
						
						String theStyle = "";
						if ( eE.getAttribute("style")!=null) {
							theStyle = " style=\""+eE.getAttributeValue("style")+"\"";
						}			
						
						if ( Commons.hasFlag(eE, "block")) {
							if ( hr.rec_is_menu && isWeb)
								master_class = master_class + " blocked";
						}
						
						String theMasterClass = "";
						if ( hr.rec_is_menu && isWeb)
							theMasterClass = " class=\"tiTabFloatMaster "+master_class+"\"";
						else 
							theMasterClass = " class=\"tiTabGridMaster "+master_class+"\"";		
						
						if ( hr.rec_is_menu ) {
							int first_cell = 1;
							if ( eE.getAttribute("first_cell")!=null )
								first_cell = Commons.string2int(eE.getAttributeValue("first_cell"));
							
							int last_cell = 12;
							if ( eE.getAttribute("last_cell")!=null )
								last_cell = Commons.string2int(eE.getAttributeValue("last_cell"));
							if ( last_cell < first_cell )
								throw new LangException(999,"ti menu mit falschen first_cell ["+first_cell+"] und last_cell ["
										+last_cell+"]");
							
							html = "<tr "+theStyle+theWidth+theMasterClass+">";
							
							// vorlauf
							int span_vorlauf = 0;
							if ( first_cell > 1 ) span_vorlauf = first_cell -1;
							if ( span_vorlauf > 0 )
								html = html + "<td colspan=\""+span_vorlauf+ "\"> </td>";
							
							// content dh. das eigentliche menu

							int span_content = last_cell - span_vorlauf;
							
							if ( span_content > 1)
								html = html + "<td colspan=\""+span_content+"\" "+theMasterClass+">";
							else 
								html = html + "<td "+theMasterClass+">";
							
							if ( isWeb ) {								
								html = html + "<div "+theMasterClass+">";
							} else {
								html = html + "<table "+theMasterClass+"><tr>";
							}

							
							// im web ist hier eine liste von divs drinnen
							// im offline eine liste von tds
							html = html + hr.main;
							
							//System.out.println("hr.main= "+hr.main);
							if ( isWeb ) {
								html = html + "</div>";															
							} else {
								html = html + "</tr></table>";	
							}
							
							html = html+"</td>";
							
																	
							
							// der nachlauf
							int span_nachlauf = 0;
							if ( span_vorlauf + span_content < 12)
								span_nachlauf = 12 - span_vorlauf - span_content;
							
							if ( span_nachlauf > 0) {
								html = html + "<td colspan=\""+span_nachlauf+"\"> </td>";								
							}
							
							// die tr schliessen
							html = html + "</tr>";
							/*
							System.out.println("ti menu");
							System.out.println("     first_cell    = "+first_cell);
							System.out.println("     last_cell     = "+last_cell);
							System.out.println("     span_vorlauf  = "+span_vorlauf);
							System.out.println("     span_content  = "+span_content);
							System.out.println("     span_nachlauf = "+span_nachlauf);*/

								
							
						
						} else {
							// eine div mit offline ist in der tabelle
							html = "<tr "+theStyle+theWidth+theMasterClass+">"+hr.main+"</tr>";
						}
						

					} else if ( hr.existsRight() & hr.flow) {
						// its a split with a main and a right content - dirs and files
						// should flow
						if ( kl_type == KL_NLAB_NKEY ) {
							// no label, no key
							html =   "<tr class='ti_tr1a_00'><td class='ti_td_tr1b_00' colspan='12' valign='middle'><span>"
								   + "<table "+stil.getTable3()+"><tr class='ti_tr1c_00'>"
								   + hr.getTdMain(1,true)+hr.getTdRight(1,true)
								   + "</tr></table>"
								   + "</span></td></tr>";
						} else if ( kl_type == KL_NLAB_KEY ) {
							// no label, key
							html =   "<tr class='ti_tr1a_0x'><td  class='ti_td_tr1b_0x' colspan='11' valign='middle'><span>"
									   + "<table "+stil.getTable3()+"><tr class='ti_tr1c_0x'>"
									   + hr.getTdMain(1,true)+hr.getTdRight(1,true)
									   + "</tr></table>"
									   + "</span></td>"+hr.getTdKey()+"</tr>";							
						} else if ( kl_type == KL_LAB_NKEY ) {
							// label, no key
							html =   "<tr class='ti_tr1a_x0'>"+hr.getTdLabel()+"<td  class='ti_td_tr1b_x0' colspan='11' valign='middle'><span>"
									   + "<table "+stil.getTable3()+"><tr class='ti_tr1c_x0'>"
									   + hr.getTdMain(1,true)+hr.getTdRight(1,true)
									   + "</tr></table>"
									   + "</span></td></tr>";								
						} else {
							// label, key
							html =   "<tr class='ti_tr1a_xx'>"+hr.getTdLabel()+"<td  class='ti_td_tr1b_xx' colspan='10' valign='middle'><span>"
									   + "<table "+stil.getTable3()+"><tr class='ti_tr1c_xx'>"
									   + hr.getTdMain(1,true)+hr.getTdRight(1,true)
									   + "</tr></table>"
									   + "</span></td>"+hr.getTdKey()+"</tr>";									
						}						
					}  else if ( hr.existsRight() ) {
						// its a split with a main and a right content - dirs and files
						// should not flow
						if ( kl_type == KL_NLAB_NKEY ) {
							// no label, no key
							html =  "<tr class='ti_tr2_00'>"+hr.getTdMain(1,true)+hr.getTdRight(11,false)+"</tr>";
						} else if ( kl_type == KL_NLAB_KEY ) {
							// no label, key
							html =  "<tr class='ti_tr2_0x'>"+hr.getTdMain(1,true)+hr.getTdRight(10,false)+hr.getTdKey()+"</tr>";
						} else if ( kl_type == KL_LAB_NKEY ) {
							// label, no key
							html =  "<tr class='ti_tr2_x0'>"+hr.getTdLabel()+hr.getTdMain(1,true)+hr.getTdRight(10,false)+"</tr>";
						} else {
							// label, key
							html =  "<tr class='ti_tr2_xx'>"+hr.getTdLabel()+hr.getTdMain(1,true)+hr.getTdRight(9,false)+hr.getTdKey()+"</tr>";
						}						
					} else {
						// everything else is plain content
						if ( kl_type == KL_NLAB_NKEY ) {
							// no label, no key
							html =  "<tr class='ti_tr3_00'>"+hr.getTdMain(12,false)+"</tr>";
						} else if ( kl_type == KL_NLAB_KEY ) {
							// no label, key
							html =  "<tr class='ti_tr3_0x'>"+hr.getTdMain(11,false)+hr.getTdKey()+"</tr>";
						} else if ( kl_type == KL_LAB_NKEY ) {
							// label, no key
							html =  "<tr class='ti_tr3_x0'>"+hr.getTdLabel()+hr.getTdMain(11,false)+"</tr>";
						} else {
							// label, key
							html =  "<tr class='ti_tr3_xx'>"+hr.getTdLabel()+hr.getTdMain(10,false)+hr.getTdKey()+"</tr>";
						}						
					}
					
					toadd = toadd.concat(html);
					performLogInfo ( Commons.cutNice2(newT2.getLabel(),thirty)+"  "+newT2.getLogText(), false, loglevel );
					
				}
			}
		}

		return toadd;
		
	}


	

	public void executeTk(Element tkE, Level loglevel,  String app, String appdir, String inst, String inst_dir, ITiHandler tih) throws Exception {
		// System.out.println("AAAAAAAAAAA "+inst_dir);
		// Sp.p("Tk-Terminator received:"+Commons.toText(tkE));
		//System.out.println("executeTk: "+Commons.toText(tkE));
		
		Element tkEArranged = arrangeElementTk(tkE);	
		Element tkN = Entities.exportEntitiesClone(tkEArranged);
		// Sp.p("Tk-Terminator after Arrange and Export:"+Commons.toText(tk));
		
		// hier brauchen wir app und pro und dirs
		boolean print_possible = true;
		if ( tkN.getChildren().size()==0) {
			print_possible = false;
		}
		
		boolean flag_print_before = false;
		boolean flag_print_only = false;
		boolean flag_print = false;
		boolean flag_print_tips_only = false;
		boolean flag_print_with_tips = false;
		boolean flag_print_all_fields = false;
		boolean flag_print_all_tested_fields = false;
		if ( print_possible) {
			flag_print_before = Commons.hasFlag(tkN, "print_before");
			flag_print_only = Commons.hasFlag(tkN, "print_only");
			flag_print = Commons.hasFlag(tkN, "print");
			flag_print_tips_only = Commons.hasFlag(tkN, "print_tips_only");
			flag_print_with_tips = Commons.hasFlag(tkN, "print_with_tips");
			flag_print_all_fields = Commons.hasFlag(tkN, "print_all_fields");
			flag_print_all_tested_fields = Commons.hasFlag(tkN, "print_all_tested_fields");
		}

		Tk_TerminalKeyboardSequ tkSimple = new Tk_TerminalKeyboardSequ();
		// resi
		// Entities.exportEntities( tk );
		
		String tipe = tkEArranged.getAttributeValue("type");
		if (tipe == null || tipe.equals("sequ")) {
			
			if ( print_possible && flag_print_only ) {
				// System.out.println("aa cccccccccc print_tk ");
				print_tk ( tkEArranged, loglevel, tih,null, app,  appdir,  inst,  inst_dir,flag_print_with_tips,flag_print_tips_only,flag_print_all_fields,flag_print_all_tested_fields );
				return;
			}
			if ( flag_print_before ) {
				print_tk ( tkEArranged, loglevel, tih,"before edit", app,  appdir,  inst,  inst_dir ,flag_print_with_tips,flag_print_tips_only,flag_print_all_fields,flag_print_all_tested_fields);
				flush();
			}
			
			// hauptveranstaltung
			tkSimple.executeTk(tkN, loglevel, tih, this);
	
			if ( flag_print )
				print_tk ( tkEArranged, loglevel, tih,"updated this ...", app,  appdir,  inst,  inst_dir,flag_print_with_tips,flag_print_tips_only,flag_print_all_fields,flag_print_all_tested_fields );
		} else
			throw new Exception("Unknown type of tk: "+tipe);		
	}
	static String io = "";	
	static String ic = "";

	
	public void print_tk ( Element eE, Level loglevel, ITiHandler tih, String key,String app, String appdir, String inst, String inst_dir
			, boolean print_with_tips, boolean print_tips_only, 
			boolean print_all_fields, boolean print_all_tested_fields) throws Exception {
		// System.out.println("cccccccccc print_tk "+inst_dir);
		String ioWide = interpreter.translateString("(f,html,i,o)");
		String icWide = interpreter.translateString("(f,html,i,c)");

		String boVal = "";
		String bcVal = "";	
		
		if ( print_with_tips ) {
			 boVal = interpreter.translateString("(f,html,b,o)");
			 bcVal = interpreter.translateString("(f,html,b,c)");			
		}
		
		String doppelpunkt;
		
		if ( print_with_tips && !print_tips_only) 
			doppelpunkt = " =";
		else
			doppelpunkt = ":";


		PendingMsg pendingMsg = null;
		Element ti_top = new Element("ti");

		
		if ( key != null)
			ti_top.setAttribute("key",key);
		if ( eE.getAttribute("label") != null && eE.getAttributeValue("label").length() > 0)
			ti_top.setAttribute("label",eE.getAttributeValue("label"));
		// else
		// 	ti_top.setAttribute("label","Data");
		if ( eE.getAttribute("tip") != null && (print_with_tips||print_tips_only)) {
			// das ist der tip zum kompletten tk
			Element ex = new Element("ti");
			ex.setAttribute("x",io+shortenTip(eE.getAttributeValue("tip"))+ic);
			ex.setAttribute("flag","wrap_word block");
			
			Element exr = new Element("ti");
			exr.setAttribute("type","rec");
			exr.addContent(ex);
			// tih.translateString("(f,html,bold,open)")+sayIt+tih.translateString("(f,html,bold,close)")
			
			ti_top.addContent(exr);
		}
		List<Element> el = eE.getChildren();
		Iterator<Element> it = el.iterator();
		int lamoda = 1;
		while ( it.hasNext()) {
			Element xE = (Element)it.next();
			// Sp.p("Schauen an: "+Commons.toText(xE));
			// if ( !print_all_fields && xE.getAttribute("test")!=null  && xE.getAttributeValue("test").equals("0"))
			if ( !print_all_fields && Tester.testOrTestdynAttributeIs0(xE,tih))
				;			
			else if ( xE.getAttributeValue("type") != null && xE.getAttributeValue("type").equals("msg")) {
				// this is a message
				// 
				Element ti_to_print = new Element("ti");
				ti_to_print.setAttribute("type", "rec");
				
				Element ti_to_print_inner = new Element("ti");
				String sayIt = xE.getAttributeValue("x");
				if ( sayIt == null ) sayIt = xE.getAttributeValue("msg");
				if ( sayIt == null ) sayIt = xE.getText();
				String out = tih.translateString("(f,html,bold,open)")+sayIt+tih.translateString("(f,html,bold,close)");

				
				ti_to_print_inner.setAttribute("x",out);
				if ( tk_msg_right)
					ti_to_print_inner.setAttribute("flag","block");

				ti_to_print.addContent(ti_to_print_inner);
				

				if ( tk_align_right)
					ti_to_print.setAttribute("flag","label_right");					

				pendingMsg = new PendingMsg();
				pendingMsg.setFirstElement(ti_to_print);
				if ( xE.getAttribute("tip") != null && (print_with_tips||print_tips_only)) {
					// ein tip zu einer message
					
					Element e2 = new Element("ti");
					e2.setAttribute("flag", "wrap_word");
					
					e2.setAttribute("x",io+shortenTip(xE.getAttributeValue("tip"))+ic); 
					e2.setAttribute("label", " ");
					pendingMsg.setSecondElement(e2);
				}
				
			} else {
				if ( !Commons.hasFlag(xE, "password")) {
					
			
					
				// not a message
				
				String value = getXofTk( xE, loglevel, tih );
				String theType = xE.getAttributeValue("type");
				//System.out.println("xxxx theType="+theType+"  value="+value);
				if ( theType==null || theType.length()== 0 )
					theType="text";
				
				// Sp.p("value="+value+", "+xE.getAttributeValue("id"));
				String tip = null;
				if ( xE.getAttribute("tip") != null ) {					
					tip = shortenTip(xE.getAttributeValue("tip"));
					if ( tip.length()==0 ) {
						tip = null;
					}
				}
				
				if ( (value != null && value.length()>0) || print_all_fields || print_all_tested_fields) {
					Element ti_to_print = new Element("ti");
					
					Element ti_to_print2 = null;
					Element ti_to_print3 = null;
					
					ti_to_print.setAttribute("type", "rec");
					

					
					// das ist der einzeiler
					Element ti_to_print_label = new Element("ti");
					Element ti_to_print_value = new Element("ti");
					
					
										
					if ( pendingMsg != null ) {
						ti_top.addContent(pendingMsg.getFirstElement());
						if ( pendingMsg.hasSecondElement())
							ti_top.addContent(pendingMsg.getSecondElement());
						pendingMsg = null;
					}

					
					// von requo rueber
					//String add="";
					String flagCollector= "";
					if ( Commons.hasFlag(xE, "download"))
						flagCollector = flagCollector +" download ";
						

					
					//boolean isWide = false;
					String the_type = getTiType2(xE);

					if ( value != null  && value.length()>0) {
						if ( value.length() > tk_wide_size  && Commons.hasFlag(xE, "wide") && tk_wide_on ) {
						// if ( value.length() > tk_wide_size  && Commons.hasFlag(xE, "wide") && isWeb) {
							String valueExported = Entities.exportEntities(value);
							int valueLen = valueExported.length();
						
							
							
							// neu sommer 
							
							ti_to_print_value.setAttribute("x",Entities.importEntities(valueExported.substring(0, tk_wide_intro_size)));
							/*if ( ti_to_print_value.getAttributeValue("x").indexOf("toprint") > 0 )
								System.out.println("ti_to_print_value.x="+ti_to_print_value.getAttributeValue("x"));*/
							
							

							flagCollector = flagCollector +" word_wrap ";
							
							ti_to_print2 = new Element("ti");		
							ti_to_print2.setAttribute("type", "rec");
							
							Element p2d = new Element("ti");
							p2d.setAttribute("flag", "wrap_word block");
							
							p2d.setAttribute("x", Entities.importEntities(valueExported.substring(tk_wide_intro_size, valueLen-tk_wide_extro_size)));
							/*if ( p2d.getAttributeValue("x").indexOf("toprint") > 0 ) {
								System.out.println("p2d.x="+p2d.getAttributeValue("x"));
							}*/
							
							ti_to_print2.addContent(p2d);

							ti_to_print3 = new Element("ti");
							ti_to_print3.setAttribute("label"," ");
							ti_to_print3.setAttribute("flag","word_wrap");
							ti_to_print3.setAttribute("x", ioWide+Entities.importEntities(valueExported.substring(valueLen-tk_wide_extro_size))+icWide);
							
							// alt
							/*
							ti_to_print_value.setAttribute("x","...");							
							ti_to_print2 = new Element("ti");							
							ti_to_print2.setAttribute("x", value);
							ti_to_print2.setAttribute("flag","wrap_word");
							ti_to_print3 = new Element("ti");
							ti_to_print3.setAttribute("x", ".");
							ti_to_print3.setAttribute("align", "right");
							*/
							
							
							/*
							String gesamt = "(f,html,i,o)"+value.substring(0, tk_wide_size)
									+"(f,html,i,c)"
									+value.substring(tk_wide_size);
									*/
						} else if (theType.equals("file") || theType.equals("dir")){
							//System.out.println("setze wert1: "+boVal+value+bcVal);
							ti_to_print_value.setAttribute("x",value);
							if( print_with_tips)
							flagCollector = flagCollector +" bold ";
							
							//System.out.println("setze wert2: "+ti_to_print_value.getAttributeValue("x"));
							//if ( ti_to_print_value.getAttributeValue("x").indexOf("toprint") > 0 )
							//	System.out.println("ti_to_print_valueB.x="+ti_to_print_value.getAttributeValue("x"));							
						} else {
							ti_to_print_value.setAttribute("x",boVal+value+bcVal);
						}
						
					} else {
						ti_to_print_value.setAttribute("x",boVal+"?"+bcVal);
						the_type = "text";
					}
					if (xE.getAttributeValue("label") != null )
						ti_to_print_label.setAttribute("x",xE.getAttributeValue("label")+doppelpunkt);
					else if (xE.getAttributeValue("id") != null )
						ti_to_print_label.setAttribute("x",xE.getAttributeValue("id")+doppelpunkt);
					else
						ti_to_print_label.setAttribute("x","value"+doppelpunkt);
					
					
					ti_to_print_value.setAttribute("type",the_type);
					
					// wrap_word bei textblock nic123
					// String theFlag = "";
					
					// hier gehört die isWide logik nic321
					if ( xE.getAttribute("type") != null && 
							(xE.getAttributeValue("type").equals("textblock") || 
							xE.getAttributeValue("type").equals("sel")))
						flagCollector = flagCollector +" wrap_word ";
						

					
					// das ist ein wert
					if ( !tk_align_right)
						ti_to_print_label.setAttribute("flag","block")
						;
					
					if ( flagCollector.length() > 0 )	
						ti_to_print_value.setAttribute("flag", flagCollector);
					
					ti_to_print.addContent(ti_to_print_label);
					ti_to_print.addContent(ti_to_print_value);
					//System.out.println("ti_to_print:: "+Commons.toText(ti_to_print));
					
					if ( !print_tips_only ) {
						ti_top.addContent(ti_to_print);	
						
						if ( ti_to_print2 != null )
							ti_top.addContent(ti_to_print2);	
						if ( ti_to_print3 != null )
							ti_top.addContent(ti_to_print3);
					}
								
					
					if ( (print_tips_only || print_with_tips) && tip != null) {
						Element ti_to_print_tip = new Element("ti");
						ti_to_print_tip.setAttribute("label"," ");
						if ( print_tips_only ) { 
							ti_to_print_tip.setAttribute("label",xE.getAttributeValue("label")+doppelpunkt);  // +" "
							ti_to_print_tip.setAttribute("flag","label_right");
						}
						ti_to_print_tip.setAttribute("x",io+tip+ic);
						ti_top.addContent(ti_to_print_tip);
					}
					
					if ( (print_tips_only || print_with_tips) && CoreBuilder.rslogger.debugIsOn()) {
						Element ti_to_print_tip_var = new Element("ti");
						ti_to_print_tip_var.setAttribute("label"," ");

						String raus="(e:o)"+getTiType(xE)+"(e:s)"+xE.getAttributeValue("id")+"(e:c) is the variable.";
						ti_to_print_tip_var.setText(raus);
						ti_top.addContent(ti_to_print_tip_var);
					}		
					// rueberkopieren ende
								
				}				
			}
			}

		}	
		// da
		if ( ti_top.getChildren().size()== 0 ) {
			Element ti_empty = new Element("ti");
			ti_empty.setText("No values defined");
			ti_empty.setAttribute("label", " ");
			ti_top.addContent(ti_empty);
		}
		// Sp.p("For TI: "+Commons.toText(ti_top));
		// System.out.println("zzzzzzzzzz tk ruft ti auf "+inst_dir); sommer
		//Element ti_topTranslated = interpreter.translate(ti_top);
		//System.out.println("ti-top="+Commons.toText(ti_top));
		// Entities.importEntities(ti_top);
		executeTi(ti_top, loglevel, app,appdir,inst,inst_dir,tih); // sommer
	
	}
	
	private String getXofTk( Element tk_elementE, Level loglevel, ITiHandler tih ) throws Exception {
		String varidE = tk_elementE.getAttributeValue("id");		
		try {
			if ( tk_elementE.getAttribute("type")== null)
				return tih.getVar(getTiType(tk_elementE), varidE);
			
			else if ( tk_elementE.getAttributeValue("type").equals("bool")) {
				String k = tih.getVar(getTiType(tk_elementE), varidE);
				if ( k==null) return k;
				Vcar vb = new Vcar(k);
				if ( vb.isBool ) {
					if ( vb.getBool() )
						return tih.getSitzung().lang().getYes();
					else
						return tih.getSitzung().lang().getNo();
				} else
					return k;
				
			}else if ( tk_elementE.getAttributeValue("type").equals("sel")) {
				// nic123
				String value = tih.getVar(getTiType(tk_elementE), varidE);
				boolean multi = Commons.hasFlag(tk_elementE, "multi");
				Element sels = null;
				if ( tk_elementE.getAttribute("xml")== null) {
					sels = tk_elementE;
				} else {
					sels = CommonsVfs.getElementByFilename(tk_elementE.getAttributeValue("xml"),userSitzung);
				}
				//return getXValueSel (value, sels, multi,  loglevel,  tih  );
				return Tk_TkSelection.getLabelOfCurrrentSelection ( 
						value,
						sels,
						multi,
						tih,
						CoreBuilder.rslogger.debugIsOn());

			} else {
				// Sp.p("Asked for type"+getTiType(tk_elementE)+" id="+varidE+" is= "+tih.getVar(getTiType(tk_elementE), varidE));
				return tih.getVar(getTiType(tk_elementE), varidE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("Exception: "+e.getMessage());
			return null;
		}
	}
	/*
	private String getXValueSel (String value, Element sels, boolean multi, Level loglevel, ITiHandler tih  ) {
		if ( value== null | value.equals(""))
			return "";
		else if ( multi ) {
			String[] splittet = value.split("\\s+");
			String ret = "";
			for ( String x : splittet ) {
				if ( !ret.equals(""))
					ret = ret+", ";
				Element found = Commons.xpathSelectSingleNode(sels, "child::*[text()="+Commons.xpathstring(x)+"]");

				
				if ( found == null )
					ret = ret+x;
				else if ( found.getAttribute("label")!=null )
					ret = ret+found.getAttributeValue("label");
				else
					ret = ret+x;
				
			}
			return ret;
		} else {
			// Element found = Commons.xpathSelectSingleNode(sels, "child::*[text()='"+value+"']");
			Element found = Commons.xpathSelectSingleNode(sels, "child::*[text()="+Commons.xpathstring(value)+"]");
			if ( found == null )
				return value;
			else if ( found.getAttribute("label")!=null )
				return found.getAttributeValue("label");
			else
				return value;
		}
		
	}
	*/
	
	private String getTiType( Element tk_element ) {
		String type = tk_element.getAttributeValue("type");
		String vartype;
		if ( type == null )
			vartype = "prop";
		else if ( type.equals("file"))
			vartype = "file";
		else if ( type.equals("dir"))
			vartype = "dir";
		else
			vartype = "prop";
		
		return vartype;
	}	
	private String getTiType2( Element tk_element ) {
		String type = tk_element.getAttributeValue("type");
		String vartype;
		if ( type == null )
			vartype = "text";
		else if ( type.equals("file"))
			vartype = "file";
		else if ( type.equals("dir"))
			vartype = "dir";
		else
			vartype = "text";
		
		return vartype;
	}		
	/*
	private void export_tkx ( Element retval ) throws Exception {
		
		// Iteriere Attribute
		List<Attribute> attributeList = retval.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(Entities.exportEntities(attribute.getValue()));
			// attribute.setValue(tiGoodies(Cdata_Xml.run(Entities.exportEntities(attribute.getValue()))));
		}

		List<Element> elementeList = retval.getChildren();
		Iterator<Element> it2 = elementeList.iterator();
		while ( it2.hasNext()) {
			Element x = (Element)it2.next();
			export_tk(x);
		}	

		
		if (  retval.getChildren().size() == 0 ) {
			// there are no child elements
			String textvalue = retval.getText();
			if ( textvalue != null && textvalue.length()>0 ) {
				retval.setText(Entities.exportEntities(textvalue));
			}			
		}
	
	}		*/
	
	Element arrangeElementTk( Element x ) {
		Element retTk;
		if ( x.getChildren("tk").size() == 0 ) {
			retTk = new Element ("tk");
			retTk.setAttribute("label", "Question");
			retTk.addContent(x.clone());
		} else {
			retTk = x;
		}
		return retTk;
	}
	
	public JComponent getProgressComponent() {
		return progressPanel;
	}
	
	public JComponent getProgressComponent2() {
		return terminalInfoPanel.getJComponent();
	}	

	Tc_AppConsole appConsole3 = null;
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
		terminalInfoPanel.setMapSetMaster(appConsole3);
		
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub		
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub		
	}
	@Override
	public void restart() {
		appConsole3.refresh();	
		// CheckDirty.notGoodForToday();
		// JOptionPane.showMessageDialog(null,"Terminator: restart()");
		seitenlinks = new ArrayList<Seitenlinks>();
		currentSeitenlinks = null;
		seitenlinksindex = 0;		
	}
	
	public void restart(String app, String inst) {
		// JOptionPane.showMessageDialog(null,"Terminator: restart2()");
		appConsole3.refresh(app,inst);	
		// CheckDirty.notGoodForToday();
		seitenlinks = new ArrayList<Seitenlinks>();;
		currentSeitenlinks = null;
		seitenlinksindex = 0;			
	}

	private static String getJobColor ( int call_depth ) {
		if ( call_depth == 0 )
			return new String ("AAAAA9");
		else if ( call_depth == 1 )
			return new String ("CDCDCC");
		else if ( call_depth == 2 )
			return new String ("ECEBEA");		
		else 
			return new String ("FEFEFE");		
	}
	
	private int getTableWidthHelper() {
		/*
		System.out.println("----------------------------");
		System.out.println("jEditorPaneTi.getWidth())                                    ="+jEditorPaneTi.getWidth());
		System.out.println("jScrollPaneTi.getWidth()                                     ="+jScrollPaneTi.getWidth());
		System.out.println("jScrollPaneTi.getPreferredSize().getWidth()                  ="+jScrollPaneTi.getPreferredSize().getWidth());
		System.out.println("jEditorPaneTi.getPreferredScrollableViewportSize().getWidth()="+jEditorPaneTi.getPreferredScrollableViewportSize().getWidth());
		System.out.println("jEditorPaneTi.getPreferredSize().getWidth()                  ="+jEditorPaneTi.getPreferredSize().getWidth());
		System.out.println("jScrollPaneTi.getPreferredSize().width                       ="+jScrollPaneTi.getPreferredSize().width);
		System.out.println("jEditorPaneTi.getPreferredScrollableViewportSize().width)    ="+jEditorPaneTi.getPreferredScrollableViewportSize().width);
		System.out.println("jEditorPaneTi.getPreferredSize().width                       ="+jEditorPaneTi.getPreferredSize().width);
		System.out.println("----------------------------"+(int)(jEditorPaneTi.getWidth() / 1.393));
		*/
		if (isWeb) return 0;
		else return (int)(jScrollPaneTi.getWidth() / 1.393);
		
		/*
		 * 		System.out.println("1jEditorPaneTi.getWidth()="+jEditorPaneTi.getWidth());
		System.out.println("2jEditorPaneTi.getWidth()="+jScrollPaneTi.getWidth());
		System.out.println("3jEditorPaneTi.getWidth()="+jScrollPaneTi.getPreferredSize().getWidth());
		System.out.println("4jEditorPaneTi.getWidth()="+jEditorPaneTi.getPreferredScrollableViewportSize().getWidth());
		System.out.println("5jEditorPaneTi.getWidth()="+jEditorPaneTi.getPreferredSize().getWidth());
		System.out.println("3jEditorPaneTi.getWidth()="+jScrollPaneTi.getPreferredSize().width);
		System.out.println("4jEditorPaneTi.getWidth()="+jEditorPaneTi.getPreferredScrollableViewportSize().width);
		System.out.println("5jEditorPaneTi.getWidth()="+jEditorPaneTi.getPreferredSize().width);

		 */
	}
	public void tiBreak(Element e) { 
		//nic123j


		
		//getPreferredScrollableViewportSize()
		//JEditorPane jEditorPaneTi = null;
		//JScrollPane jScrollPaneTi = null;
		//
		
 		if ( e == null )
 			addToFlush("</table><table "+stil.getTable1(null,getTableWidthHelper())+">");
 		else if ( e.getAttribute("class_table") != null )
 			addToFlush("</table><table "+stil.getTable1(e.getAttributeValue("class_table"),getTableWidthHelper())+">");
 		else	
 			addToFlush("</table><table "+stil.getTable1(null,getTableWidthHelper())+">");		

	

	}	

 	@Override
	public void tiBreakHr(Element e) { // nic123
 		if ( e == null )
 			addToFlush("</table><hr class=\"ti_break_hr\"/><table "+stil.getTable1(null,getTableWidthHelper())+">");
 		else if ( e.getAttribute("class_table") != null )
 			addToFlush("</table><hr class=\"ti_break_hr\"/><table "+stil.getTable1(e.getAttributeValue("class_table"),getTableWidthHelper())+">");
 		else	
 			addToFlush("</table><hr class=\"ti_break_hr\"/><table "+stil.getTable1(null,getTableWidthHelper())+">");
	}

	@Override
	public void tiClear(Element e) {
		// TODO Auto-generated method stub
		
		if ( Commons.hasFlag(e, "all")) {
			screenString = new String("");
			flushString = new String("");			
		} else {
			flushString = new String("");
		}  
		
	}	
	public void tiHr(Element e) {
		
		if ( e.getAttribute("style")==null ) {
			addToFlush("<tr><td colspan='12'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("2") ) {
			addToFlush("<tr><td colspan='1'></td><td colspan='11'><hr/></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("3") ) {
			addToFlush("<tr><td colspan='2'></td><td colspan='10'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("4") ) {
			addToFlush("<tr><td colspan='3'></td><td colspan='9'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("5") ) {
			addToFlush("<tr><td colspan='4'></td><td colspan='8'><hr/></td></tr>");				
		} 
		else if ( e.getAttributeValue("style").equals("1a") ) {
			addToFlush("<tr><td colspan='11'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("2a") ) {
			addToFlush("<tr><td colspan='1'></td><td colspan='10'><hr/></td><td></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("3a") ) {
			addToFlush("<tr><td colspan='2'></td><td colspan='9'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("4a") ) {
			addToFlush("<tr><td colspan='3'></td><td colspan='8'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("5a") ) {
			addToFlush("<tr><td colspan='4'></td><td colspan='7'><hr/></td><td></td></tr>");				
		} 		
		else if ( e.getAttributeValue("style").equals("2i") ) {
			addToFlush("<tr><td colspan='1'><hr/></td><td colspan='11'></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("3i") ) {
			addToFlush("<tr><td colspan='2'><hr/></td><td colspan='10'></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("4i") ) {
			addToFlush("<tr><td colspan='3'><hr/></td><td colspan='9'></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("5i") ) {
			addToFlush("<tr><td colspan='4'><hr/></td><td colspan='8'></td></tr>");			
		}
		/*
		
		if ( e.getAttribute("style")==null ) {
			addToFlush("<tr><td colspan='12'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("1") ) {
			addToFlush("<tr><td colspan='1'></td><td colspan='11'><hr/></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("2") ) {
			addToFlush("<tr><td colspan='2'></td><td colspan='10'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("3") ) {
			addToFlush("<tr><td colspan='3'></td><td colspan='9'><hr/></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("4") ) {
			addToFlush("<tr><td colspan='4'></td><td colspan='8'><hr/></td></tr>");				
		} 
		else if ( e.getAttributeValue("style").equals("5") ) {
			addToFlush("<tr><td colspan='11'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("6") ) {
			addToFlush("<tr><td colspan='1'></td><td colspan='10'><hr/></td><td></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("7") ) {
			addToFlush("<tr><td colspan='1' style='padding-left: 9px;'>"+
					Ti_TerminalLink.goodies( Entities.exportEntities(e.getAttributeValue("label")))+
					"</td><td colspan='10'><hr/></td><td></td></tr>");					
		}  else if ( e.getAttributeValue("style").equals("8") ) {
			addToFlush("<tr><td colspan='2'></td><td colspan='9'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("9") ) {
			addToFlush("<tr><td colspan='3'></td><td colspan='8'><hr/></td><td></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("10") ) {
			addToFlush("<tr><td colspan='4'></td><td colspan='7'><hr/></td><td></td></tr>");				
		} 		
		else if ( e.getAttributeValue("style").equals("11") ) {
			addToFlush("<tr><td colspan='1'><hr/></td><td colspan='11'></td></tr>");					
		} else if ( e.getAttributeValue("style").equals("12") ) {
			addToFlush("<tr><td colspan='2'><hr/></td><td colspan='10'></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("13") ) {
			addToFlush("<tr><td colspan='3'><hr/></td><td colspan='9'></td></tr>");			
		} else if ( e.getAttributeValue("style").equals("14") ) {
			addToFlush("<tr><td colspan='4'><hr/></td><td colspan='8'></td></tr>");			
		}
		*/

	}		
	
	public void jobgroupstart (
			Interpreter interpreter) {
		// if (sage) Sp.p("++++ Tc_Terminator.jobgroupstart() interpreter="+interpreter.getDescriptionStringSmall());		
		// ... das war nur zum testen
		tpFinal = null;
		tpLast = null;
		seitenlinksindex++;
		Seitenlinks aktuell = new Seitenlinks(seitenlinksindex);
		seitenlinks.add(aktuell);
		currentSeitenlinks = aktuell;		
		screenString = new String("");
		this.interpreter = interpreter;
		
	}
	Interpreter interpreter = null;
	public void jobstart(String job, String app, String pro, boolean isSc,	int call_depth, String tip) {
		if (sage) Sp.p("++++ Tc_Terminator.jobstart() STARTED pagePosition="+pagePosition+", pageList.size()="+pageList.size()+", job="+job+", app="+app+", pro="+pro+", isSc="+isSc+", call_depth="+call_depth+", tip="+tip);
		
			String naming;
			String namingWeb;
			if (job.equals("sys_project_setup")) {
				naming= "<b>&rarr;&nbsp;"+"Project Setup" + "</b>";
				namingWeb= "&rarr;&nbsp;"+"Project Setup" ;
			} else {
				naming= "<b>&rarr;&nbsp;"+job + "</b>";
				namingWeb= "&rarr;&nbsp;"+job ;
			}
			
			String news = null;
			
			
			if ( userSitzung.hasGuiPrivJobHeaderName() == false &&
				 userSitzung.hasGuiPrivJobHeaderTip() == false ) {
			} else if ( isWeb ) {
				if ( userSitzung.hasGuiPrivJobHeaderName() &&
					 userSitzung.hasGuiPrivJobHeaderTip()  ) {
					news = 
							"<tr class='ti_js_tr'><td colspan='12' class='ti_js_td"+call_depth+"'><div class='ti_js_div'>"
									+"<p class='ti_js_p'>"+namingWeb+"</p>"
									+getIntro(tip)
									+"</div></td></tr>";					
				} else if ( userSitzung.hasGuiPrivJobHeaderName() ) {
					// nur der name
					news = 
							"<tr class='ti_js_tr'><td colspan='12' class='ti_js_td"+call_depth+"'><div class='ti_js_div'>"
									+"<p class='ti_js_p'>"+namingWeb+"</p>"
									// +getIntro(tip)
									+"</div></td></tr>";					
				} else if (tip != null && tip.length()>0) {
					// nur der tip
					news = 
							"<tr class='ti_js_tr'><td colspan='12' class='ti_js_td"+call_depth+"'><div class='ti_js_div'>"
									// +"<p class='ti_js_p'>"+namingWeb+"</p>"
									+getIntro(tip)
									+"</div></td></tr>";					
				} else {
					news = null;
					
				}
					
				
			} else {
				
				if ( userSitzung.hasGuiPrivJobHeaderName() &&
						 userSitzung.hasGuiPrivJobHeaderTip()  ) {
					news = 
							"<tr style='min-width: 300px;'><td valign='top' colspan='12' align='center' style='background-color: "			
									+getJobColor(call_depth)+"; padding-top:3px; padding-bottom: 3px; margin-top: 9px;'><span>"
									+naming
									+getIntro(tip)
									+"</span></td></tr>";					
				} else if ( userSitzung.hasGuiPrivJobHeaderName() ) {
					// nur der name
					news = 
							"<tr style='min-width: 300px;'><td valign='top' colspan='12' align='center' style='background-color: "			
									+getJobColor(call_depth)+"; padding-top:3px; padding-bottom: 3px; margin-top: 9px;'><span>"
									+naming
									//+getIntro(tip)
									+"</span></td></tr>";					
				} else if (tip != null && tip.length()>0) {
					// nur der tip, sofern vorhanden
					news = 
							"<tr style='min-width: 300px;'><td valign='top' colspan='12' align='center' style='background-color: "			
									+getJobColor(call_depth)+"; padding-top:3px; padding-bottom: 3px; margin-top: 9px;'><span>"
									//+naming
									+getIntro(tip)
									+"</span></td></tr>";					
				} else {
					// kein tip
					news = null;
				}
				
			}
		
			
			
			if ( call_depth == 0) {
				clearScreen();
				clockStart = System.currentTimeMillis();
			} 
			if ( news != null ) {
				addToFlush(news); // gummi
				
				flush();				
			}


			if (sage) Sp.p("++++ Tc_Terminator.jobstart() FINISHED pagePosition="+pagePosition+", pageList.size()="+pageList.size());			

	}	
	private long clockStart = System.currentTimeMillis(); 
	
	static String getIntro ( String tip ){
		String ret = "";
		if ( tip != null ) {
			ret = "<p "+stil.getJsTip()+">"+tip+"</p>";
		}
		return ret;
	}
	
	String getTrailer ( String app, String pro, boolean isSc, String jobinfo ){
		String ret = "";
		if ( app != null ) {
			ret = "App='"+app+"'";
			if ( pro != null ) {
				ret = ret+" Pro='"+pro+"' ";
			}
		}
		String where;
		if ( isSc )
			where = " Jobtype='Service' ";
		else
			where = " Jobtype='Application'";
		SimpleDateFormat formatterdate = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat formattertime = new SimpleDateFormat ("HH:mm");
		ret = ret + " date='"+formatterdate.format(new Date())+"' "+ 
		" time='"+formattertime.format(new Date())+"' "+where;

		String jobinfo2 = "";
		if ( jobinfo != null )
			jobinfo2 = "<br>"+jobinfo;
		

		String ret2 = null;
		if ( isWeb ) {
			ret2 = "<p class='ti_je_trailer'>"+ret
			+ jobinfo2 +
			"</p>";			
		} else {
			ret2 = "<p style='color:light-grey; font-size:x-small;'>"+ret
			+ jobinfo2 +
			"</p>";
			
		}

		return ret2;
	}
	
	public void jobend(String job, String app, String pro, boolean isSc, int call_depth, String jobinfo) {
		// JOptionPane.showMessageDialog(null,"Terminator: jobend");
		if (sage) Sp.p("++++ Tc_Terminator.jobend() STARTED pagePosition="+pagePosition+", pageList.size()="+pageList.size()+", job="+job+", app="+app+", pro="+pro+", isSc="+isSc+", call_depth="+call_depth+", jobinfo="+jobinfo);
		
		if ( userSitzung.hasGuiPrivJobFooter() ) {
			String jobnaming;
			if (job.equals("sys_project_setup")) 
				jobnaming= "Project Setup";
			else
				jobnaming= job;		
			String naming = "<table border='"+table_border+"'><tr><td style='background-color: "+
					getJobColor(call_depth)+
					"; padding-top:3px; padding-bottom: 0px; padding-left:10px; padding-right: 10px; margin-bottom: 9px; text-align:center;'>"+
					//getTrailer(app, pro, isSc,jobinfo)+
					"<b>&radic;  "+
					jobnaming + 
					"</b></td></tr></table>";
	
			String news = "<tr><td valign='top' colspan='12' align='center' width='100%'><span>"+naming+"</span></td></tr>";	
			addToFlush(news);
			
		}
		flush();
		if (sage) Sp.p("++++ Tc_Terminator.jobend() FINISHED pagePosition="+pagePosition+", pageList.size()="+pageList.size());		
	}

	@Override
	public void joberror(String job, String app, String pro, boolean isSc,
			int call_depth, boolean red) {
		if (sage) Sp.p("++++ Tc_Terminator.jobend() pagePosition="+pagePosition+", pageList.size()="+pageList.size()+", job="+job+", app="+app+", pro="+pro+", isSc="+isSc+", call_depth="+call_depth+", red="+red);
		
		// interpreter.printTrace("SCRREN-STRING-BEGIN_OF-JORERROR "+screenString);
		String color = "#870001";
		if (!red)
			color = "#274021";
		String naming = "<table><tr><td style='background-color: "+
			getJobColor(call_depth)+
			"; padding-top:2px; padding-bottom: 2px; padding-left:3px; padding-right: 3px;'><b>"+
			" Error in Job: "+ job + " "+ "</b> </td></tr></table>";

		String news = "<tr><td valign='top' colspan='12' align='center' style='background-color: "+color+";'>"+naming+"</td></tr>";		
		
		addToFlush(news);
		// tiBreak();
		flush();
		// interpreter.printTrace("SCRREN-STRING-END_OF-JORERROR "+screenString);

	}

	private String last_save_dir = null;
	public void tiSave() {
		try {
			// TODO Auto-generated method stub
			JFileChooser chooser;
			if ( last_save_dir == null )
				chooser = new JFileChooser();
			else
				chooser = new JFileChooser(last_save_dir);
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			// TkFilter tkFilter = new TkFilter(".html .htm");
			// chooser.setFileFilter(tkFilter);
			int returnVal =chooser.showSaveDialog(null);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File sFile = chooser.getSelectedFile();
				String sDir = Commons.getDirOfFile(sFile.getCanonicalPath());
				last_save_dir = sDir;
				String sFn = sFile.getName();
				if ( sFn.indexOf(".") > -1);
				else {
					sFn = sFn.concat(".html");
				}
				String destiny = sDir.concat(sFn);
				boolean exists = false;
				boolean cont = true;
				if (CommonsVfs.existsFile(destiny,userSitzung)) {
					exists = true;
					int y = JOptionPane.showConfirmDialog(null, "Should the file '"+sFn+"' be replaced?",
					"Question: File exists already",
			        JOptionPane.OK_CANCEL_OPTION,
			        JOptionPane.QUESTION_MESSAGE,
			        null);
					Thread.sleep(150);
					// Sp.p("y="+y);
					if ( y!= JOptionPane.OK_OPTION)
						cont=false;
				}
			   // Sp.p("Going to save: " + destiny);
				Element ti1 = new Element("ti");
				if ( cont ) {
					CommonsVfs.writeToFileVA(wrapScreenWithHtml(screenString), destiny,userSitzung);
				   				   
				   ti1.setAttribute("label","Browser-Content saved");
				   
				   Element ti2 = new Element("ti");
				   ti2.setAttribute("type","file");
				   ti2.setAttribute("label","Output:");
				   ti2.setText(destiny);
				   ti1.addContent(ti2);
				   if ( exists ) {
					   Element ti3 = new Element("ti");
					   ti3.setAttribute("label"," ");
					   ti3.setText("The file was replaced!");
					   ti1.addContent(ti3);		   
				   }
				   executeTi(ti1, null, null, null,null,null,null);
				   flush();
					   				
				} else {
					ti1.setText("Operation terminated.");
					
				}

			   
			}
		} catch (Exception e) {
			   Element ti1 = new Element("ti");
			   ti1.setAttribute("label","Something terrible happend, but don't worry! But the file was not written, we are sorry");

			   Element ti2 = new Element("ti");
			   ti2.setAttribute("label","Exception:");
			   ti2.setText(e.getLocalizedMessage());
			   
			   ti1.addContent(ti2);
			   try {
				executeTi(ti1, null, null, null,null,null,null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Sp.pe(e1);
				}
			   flush();
		}
		
	}
	@Override
	public void top() {

		if ( !stilIsWeb)
		jEditorPaneTi.setCaretPosition(0);


	}
	
	@Override
	public void bottom() {


		if ( !stilIsWeb)
		jEditorPaneTi.setCaretPosition(jEditorPaneTi.getDocument().getLength());
		
	}	



	private class StopComboListener implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			int selected = stopCombo.getSelectedIndex();
			if ( selected == 0 ) {
				// go
				Interpreter.setStopGo(false);
			} else if ( selected == 1 ) {
				// sleep
				Interpreter.setStopGo(true);
			} else if ( selected == 2 ) {
				// stop
				lc.info(" Stop-Button pressed.");
				Interpreter.setStopGo(false);
				setProgress("StoppedAll upon your request", false);
				int pos1 = jEditorPaneTi.getCaretPosition();
				flush();
				
				jEditorPaneTi.setCaretPosition(pos1);
				CoreBuilder.appManager.halt(me);
			}
	
		}
	}
	
	public void executeSomething(Element txE, Level loglevel, ITiHandler rh)
			throws Exception {
		if ( txE.getName().equals("ti_space")) {
			// Sp.p("Ti Does Space");
			addToFlush("<tr><td style='font-size:0px; height:0px; padding-top:"+txE.getAttributeValue("height")+"; padding-bottom: 0px; margin-top:0px; margin-bottom: 0px;'>"+"</td></tr>");
		} else if ( txE.getName().equals("tx_style")) {	
			if (txE.getAttribute("tk_wide_size")!=null) {
				String tk_wide_size_para = txE.getAttributeValue("tk_wide_size");
				tk_wide_size = Commons.string2int(tk_wide_size_para);
			}	
			
			if (txE.getAttribute("tk_wide_on")!=null) {
				String tk_wide_size_para = txE.getAttributeValue("tk_wide_on");
				tk_wide_on = Commons.stringToBool(tk_wide_size_para);
			}				
			
			if (txE.getAttribute("tk_wide_intro_size")!=null) {
				String tk_wide_intro_size_para = txE.getAttributeValue("tk_wide_intro_size");
				tk_wide_intro_size = Commons.string2int(tk_wide_intro_size_para);
			}		
			
			if (txE.getAttribute("tk_wide_extro_size")!=null) {
				String tk_wide_extro_size_para = txE.getAttributeValue("tk_wide_extro_size");
				tk_wide_extro_size = Commons.string2int(tk_wide_extro_size_para);
			}				
			
			if (txE.getAttribute("tk_align")!=null) {
				String tk_align_para = txE.getAttributeValue("tk_align");
				if ( tk_align_para.equals("right"))
					tk_align_right = true;				
				else if ( tk_align_para.equals("left"))
					tk_align_right = false;
				else
					throw new LangException(999,"tx_style@tk_align must be 'left' or 'right' but is "+tk_align_para);
			}	
			if (txE.getAttribute("tk_msg")!=null) {
				String tk_msg_para = txE.getAttributeValue("tk_msg");
				if ( tk_msg_para.equals("right"))
					tk_msg_right = true;				
				else if ( tk_msg_para.equals("left"))
					tk_msg_right = false;
				else
					throw new LangException(999,"tx_style@tk_msg must be 'left' or 'right' but is "+tk_msg_para);
			}				
			
			if (txE.getAttribute("ti_border")!=null) {
				table_border = txE.getAttributeValue("ti_border");	
				if ( stil instanceof TiStylerDesktop ) {
					((TiStylerDesktop)stil).setTable_border(table_border);
				}
			}
			if (txE.getAttribute("tk_width")!=null) {
				tk_width = Commons.string2int(txE.getAttributeValue("tk_width"));
				int test = tk_width -3;
				if ( (test % 2) != 0) {
					throw new LangException(999,"tx_style@tk_width must have mod 0 after subtraction of 3");
				}
			}			
		} else {
			if (sage) Sp.p("Unknown to Ti: "+txE.getName());
			throw new Exception("Terminal-Task '"+txE.getName()+"' not known.");
		}
		
	}
	@Override
	public void clearMemory() {
		// TODO Auto-generated method stub
		appConsole3.clearLog();
		
	}
	public void tiSwitch ( String app, String pro ) {
		// Sp.p("ti switch");
		Tc_UiContext myUiContext = new Tc_UiContext();
		// if ( app != null)
			myUiContext.setApp(app);
		// if ( pro != null )
			myUiContext.setInst(pro);
		// myUiContext.setJob(null);
		appConsole3.sendMapMessageSwitch(this,myUiContext);
	}
	public int getTk_width() { return tk_width; }
	/*
	private void export_ti ( Element retval ) throws Exception {
		
		// Iteriere Attribute
		List<Attribute> attributeList = retval.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(tiGoodies(attribute.getValue()));
			// attribute.setValue(tiGoodies(Cdata_Xml.run(Entities.exportEntities(attribute.getValue()))));
		}

		if ( retval.getName().equals("ti") && 
			 retval.getAttribute("type") != null &&
			(retval.getAttributeValue("type").equals("call_job") ||
			 retval.getAttributeValue("type").equals("call_service")) );
		else {
			List<Element> elementeList = retval.getChildren();
			Iterator<Element> it2 = elementeList.iterator();
			while ( it2.hasNext()) {
				Element x = (Element)it2.next();
				export_ti(x);
			}	
		}

		if (  retval.getChildren().size() == 0 ) {
			// there are no child elements
			String textvalue = retval.getText();
			if ( textvalue != null && textvalue.length()>0 ) {
				if ( Commons.hasFlag(retval, "html"))
					retval.setText(tiGoodies(textvalue));
				else
					retval.setText(tiGoodies(textvalue));

			}			
		}

	
	}
	*/
	
	/*
	 * 
private void export_ti ( Element retval ) throws Exception {
		
		// Iteriere Attribute
		List<Attribute> attributeList = retval.getAttributes();
		Iterator<Attribute> it1 = attributeList.iterator();
		while ( it1.hasNext()) {
			Attribute attribute = (Attribute)it1.next();
			attribute.setValue(tiGoodies(Commons.cdata_xml(attribute.getValue())));
			// attribute.setValue(tiGoodies(Cdata_Xml.run(Entities.exportEntities(attribute.getValue()))));
		}

		if ( retval.getName().equals("ti") && 
			 retval.getAttribute("type") != null &&
			(retval.getAttributeValue("type").equals("call_job") ||
			 retval.getAttributeValue("type").equals("call_service")) );
		else {
			List<Element> elementeList = retval.getChildren();
			Iterator<Element> it2 = elementeList.iterator();
			while ( it2.hasNext()) {
				Element x = (Element)it2.next();
				export_ti(x);
			}	
		}

		if (  retval.getChildren().size() == 0 ) {
			// there are no child elements
			String textvalue = retval.getText();
			if ( textvalue != null && textvalue.length()>0 ) {
				if ( Commons.hasFlag(retval, "html"))
					retval.setText(tiGoodies(textvalue));
				else
					retval.setText(tiGoodies(Commons.cdata_xml(textvalue)));

			}			
		}

	
	}
	 */
	/*
	private static String tiGoodies ( String in ) {
		String worker = new String(in);
		worker = Commons.stringReplace(worker, "[BOLD:ON]oeuw", "<b>");
		worker = Commons.stringReplace(worker, "[BOLD:OFF]94kd", "</b>");
		worker = Commons.stringReplace(worker, "[BIG:ON]49k", "<big>");
		worker = Commons.stringReplace(worker, "[BIG:OFF]pi8d", "</big>");
		
		worker = Commons.stringReplace(worker, "[SMALL:ON]p85f", "<small>");
		worker = Commons.stringReplace(worker, "[SMALL:OFF]p6Juz", "</small>");	
		
		worker = Commons.stringReplace(worker, "[UNDERLINE:ON]Pu1u", "<u>");
		worker = Commons.stringReplace(worker, "[UNDERLINE:OFF]Pl3C", "</u>");	
		worker = Commons.stringReplace(worker, "[ITALIC:ON]vC12R", "<i>");
		worker = Commons.stringReplace(worker, "[ITALIC:OFF]Pi4q", "</i>");		
		worker = Commons.stringReplace(worker, "[BR:ONOFF]jfu4", "<br/>");	
		return worker;
		
	}	
	*/
	
	public static void valueProblem ( String from, Exception e ) {
		Sp.p("Terminal has problem in getting a value!");
		Sp.p("| Happend in: "+from);
		Sp.p("|  Exception: "+e.getLocalizedMessage());
		Sp.pe(e);
	}

	@Override
	public void write(String text) {
		//Sp.p("############# WRITE ###############");
		Sp.p(text);
		// Sp.p("");
		Element e = new Element("ti");
		e.setText(text);
		try {
			
			String writer = "<tr>"+"<td>"+"</td>"+"<td>"+Ti_TerminalLink.Xml(text)+"</td>"+"</tr>";
			addToFlush( writer );			
			//executeTi(e,Level.WARN,"appid?","appdir?","instid?","instdir=",null);
			flush();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.pe(e1);
		}
		
	}

	@Override
	public void write(String label, String text) {
		//Sp.p("############# WRITE WITH LABEL ###############");
		Sp.p(label+": "+text);
		// Sp.p("");
		Element e = new Element("ti");
		e.setText(text);
		e.setAttribute("label", label);
		try {
			/*
			 * <tr class='ti_tr3_x0'><td valign='top'
			 */
			String writer = "<tr>"+"<td>"+Ti_TerminalLink.Xml(label)+"</td>"+"<td>"+Ti_TerminalLink.Xml(text)+"</td>"+"</tr>";
			addToFlush( writer );
			// executeTi(e,Level.WARN,"appid?","appdir?","instid?","instdir=", new DummyTiHandler());
			flush();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.pe(e1);
		}
		
	}
	/*
	public boolean isWebModus() {
		return webModus;
	}

	public void setWebModus(boolean webModus) {
		this.webModus = webModus;
	}
	*/
	public LogLogger getLogicLogger() {
		if ( logicLogger == null )
			logicLogger = CoreBuilder.rslogger;

		return logicLogger;
	}

	public void setLogicLogger(LogLogger logicLogger) {
		this.logicLogger = logicLogger;
	}

	public String getTp() {
		if ( tpFinal != null )
			return tpFinal;
		else
			return tpLast;
		// TODO Auto-generated method stub
		// return wrapScreenAsBodyMember(screenString);
	}	
	@Override
	public String getTiScreenBodyMember() {
		/*Sp.p("++++ Tc_Terminator.getTiScreenBodyMember() pagePosition="+pagePosition+
				", pageList.size()="+pageList.size()
				);		*/

		return wrapScreenAsBodyMember(screenString+flushString);
	}
	public String getTiScreenBodyMemberAndConsume(String from) {
		/*Sp.p("++++ Tc_Terminator.getTiScreenBodyMemberAndConsume() pagePosition="+pagePosition+
				", pageList.size()="+pageList.size()
				);	*/
		return getTiScreenBodyMember();
		/*
		interpreter.printTrace("Terminator:getTiScreenBodyMemberAndConsume() called from '"+from
		+"' CONSUMED :"
				+screenString+flushString);

		String ret = wrapScreenAsBodyMember(screenString+flushString);

			screenString = new String("");
			flushString = new String("");			
	

		return ret ;
		*/
	}	

	@Override
	public Seitenlinks getTerminalLinkArrayLast() {
		//Sp.p("++++ Tc_Terminator.getTerminalLinkArrayLast() pagePosition="+pagePosition+
		//		", pageList.size()="+pageList.size()
		//		);
		return currentSeitenlinks;
	}
	@Override
	public boolean screenIsClear() {
		// TODO Auto-generated method stub
		return screenString.equals("") && flushString.equals("");
	}
	
	private boolean tiShouldSelectDir = false;
	private boolean tiShouldSelectFile = false;
	
	@Override
	public void setFileChoose(boolean on) {
		tiShouldSelectFile = on;
		
	}
	@Override
	public void setDirChoose(boolean on) {
		tiShouldSelectDir = on;
		
	}
	@Override
	public boolean getFileChoose() {
		// TODO Auto-generated method stub
		
		return guiController.chooseFileOn();
	}
	@Override
	public boolean getDirChoose() {
		// TODO Auto-generated method stub
		return guiController.chooseDirOn();
	}
	String parked = null;
	ArrayList<Seitenlinks> seitenlinksParked = null;
	Seitenlinks currentSeitenlinksParked = null;
	int seitenlinksindexParked = 0;
	public void parkIn() {
		// TODO Auto-generated method stub
		parked = screenString;
		screenString = "";
		
		seitenlinksParked = seitenlinks;
		currentSeitenlinksParked = currentSeitenlinks;
		seitenlinksindexParked = seitenlinksindex;
		
		seitenlinks = new ArrayList<Seitenlinks>();
		currentSeitenlinks = null;
		seitenlinksindex = 0;	
		
		Sp.p("TERMINATOR.parkIn()");
		
	}
	@Override
	public void parkOut() {
		// TODO Auto-generated method stub
		Sp.p("Throwing away: "+screenString);
		screenString = parked;
		
		seitenlinks = seitenlinksParked;
		currentSeitenlinks = currentSeitenlinksParked;
		seitenlinksindex = seitenlinksindexParked;
		
		parked = null;
		Sp.p("TERMINATOR.parkOut()");
		
	}
	@Override
	public void parkForget() {
		// TODO Auto-generated method stub
		parked=null;
		Sp.p("TERMINATOR.parkForget()");
		
	}
	@Override
	public boolean isWeb() {
		// TODO Auto-generated method stub
		return isWeb;
	}
	private String shortenTip (String tippex ) {
		if ( tippex == null ) return null;
		else if ( tippex.indexOf("Help:") > 0 )
			return tippex.substring(0, tippex.indexOf("Help:"));
		else
			return tippex;
	}
			
}