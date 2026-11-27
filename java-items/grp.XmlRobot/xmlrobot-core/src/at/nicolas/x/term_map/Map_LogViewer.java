/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import at.nicolas.x.int_const.Zet_Privileg;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Modalmanager;
import at.nicolas.x.term_core.Tc_UiMap;
import at.nicolas.x.term_core.Tc_UiModal;


public class Map_LogViewer  extends AppenderSkeleton implements Tc_UiMap, Tc_UiModal {
	
	public static Map_LogViewer single = null;
	public static final int loglevelstyle_combo = 0;
	public static final int loglevelstyle_buttons = 1;
	
	
	// Logging - Buttons
	private JButton   butClearLog = null;
	private JButton   butZoomLog = null;
	private Map_ButtonRefresh   buttonRefresh = null;

	private JScrollPane jsp =null;
	private JPanel    panel = null;
	private JTextArea textarea = null;
	private Layout layout = null;
	private static Map_LogViewer zoomViewer = null;
	private static String zoomViewerId = null;

	private String[] logStrings = { "OFF","FATAL","ERROR","WARN","INFO","DEBUG","DEBUG_LOGIC","DEBUG_EXPRESSION","TRACE","ALL" };
	// combo
	JComboBox combo = null;
	// butlist
	private JButton[] butArray  = { 	new JButton("OFF"),
									new JButton("FATAL"),
									new JButton("ERROR"),
									new JButton("WARN"),
									new JButton("INFO"),
									new JButton("DEBUG"),
									new JButton("DEBUG_LOGIC"),
									new JButton("DEBUG_EXPRESSION"),
									new JButton("TRACE"),
									new JButton("ALL"),
									};
	
	Font fntSmall = null;
	Font fntBig = null;
	Map_LogViewer me = null;
	
	String buttonpos = null;
	private boolean standardDirection = true;
		
	private LogConnector lc = new LogConnector("map.LogViewer");

	int loglevel_style =0;
	
	private Font stdButtonFont;
	private Font boldButtonFont;
	
	private Color stdBackColor;
	private Color boldBackColor;
	private boolean slim = false;
	public Map_LogViewer ( Layout _layout, String _buttonpos, int _loglevel_style, boolean _slim ) {
		// Sp.p("Bin im LogViewer");
		if ( single == null )
			single = this;
		layout = _layout;
		me = this;

		buttonpos = _buttonpos;
		loglevel_style = _loglevel_style;
		slim = _slim;
		start();
	}	
	
	public Map_LogViewer ( Layout _layout, String _buttonpos, int _loglevel_style, boolean _slim , boolean direction) {
		// Sp.p("Bin im LogViewer");
		if ( single == null )
			single = this;
		layout = _layout;
		me = this;

		standardDirection = direction;
		buttonpos = _buttonpos;
		loglevel_style = _loglevel_style;
		slim = _slim;
		start();
	}		
	public Map_LogViewer ( Layout _layout, String _buttonpos, int _loglevel_style ) {
		// Sp.p("Bin im LogViewer");
		if ( single == null )
			single = this;
		layout = _layout;
		me = this;

		buttonpos = _buttonpos;
		loglevel_style = _loglevel_style;
		start();
	}
	private void create() {
		this.setLayout(layout);
		ButtonHandler buttonHandler = new ButtonHandler();
		int butcount = 0;
		if      ( loglevel_style == loglevelstyle_combo )   butcount = 2;
		else if ( loglevel_style == loglevelstyle_buttons ) butcount = 9;
		if ( Zet_Privileg.showRestartButton() )
			butcount++;
		JPanel loggingButtones = null;
		int spalten = 0;
		int zeilen = 0;
		
		if      ( loglevel_style == loglevelstyle_combo ) {
			spalten = butcount;
			if ( slim)
				spalten = 2;
			zeilen = 1;
		} else if ( loglevel_style == loglevelstyle_buttons ) {
			spalten = (int)Math.ceil((double)butcount / 2);
			zeilen = 2;
		}
		
		JPanel slimButtonsContainer=null;
		if ( slim ) {
			if ( standardDirection )
				slimButtonsContainer = new JPanel(new GridLayout(1,butcount-1));
			else
				slimButtonsContainer = new JPanel(new GridLayout(butcount-1,1));
		}
		if ( buttonpos == BorderLayout.WEST || buttonpos == BorderLayout.EAST ) {			
			if ( slim )
				if ( standardDirection )
					loggingButtones = new JPanel(new GridLayout(zeilen,spalten));
				else
					loggingButtones = new JPanel(new GridLayout(spalten,zeilen));
			else
				if ( standardDirection )
					loggingButtones = new JPanel(new GridLayout(spalten,zeilen));
				else
					loggingButtones = new JPanel(new GridLayout(zeilen,spalten));
		} else {
			if ( slim )
				if ( standardDirection )
					loggingButtones = new JPanel(new GridLayout(spalten,zeilen));
				else
					loggingButtones = new JPanel(new GridLayout(zeilen,spalten));
			else
				if ( standardDirection )
					loggingButtones = new JPanel(new GridLayout(zeilen,spalten));
				else
					loggingButtones = new JPanel(new GridLayout(spalten,zeilen));
		}
		
		// butClearLog = new JButton("Clear");	
		butZoomLog = new JButton("Zoom");
		butZoomLog.setToolTipText("Opens another window to see the logging");
		buttonRefresh = new Map_ButtonRefresh(null,null,null);
		// butClearLog.addActionListener(buttonHandler);
		butZoomLog.addActionListener(buttonHandler);
		
		// loggingButtones.add(butClearLog );
		if ( Zet_Privileg.showRestartButton() ) {
			if ( slim )
				slimButtonsContainer.add(buttonRefresh.getJComponent());
			else
				loggingButtones.add(buttonRefresh.getJComponent());
		}
		// loggingButtones.add(butClearLog);
		if ( slim )
			slimButtonsContainer.add(butZoomLog);
		else
			loggingButtones.add(butZoomLog );
		
		if ( slim )
			loggingButtones.add(slimButtonsContainer);
		
		// loggingButtones.add(new JPanel());
		// loggingButtones.setOpaque(true);
		
		if ( loglevel_style == loglevelstyle_combo) {
			combo = new JComboBox(logStrings);
			combo.setToolTipText("Changes the Logging-Level");
			/* if ( slim ) {
				combo.setPreferredSize(new Dimension(10,10));
			} */
			combo.setLightWeightPopupEnabled(true);
			combo.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent arg0) {
			    	  
			    	selectIndex (combo.getSelectedIndex() );
			    			    		
			      }
			    });
			combo.setSelectedItem(CoreBuilder.rslogger.getLevel().toString().toUpperCase());
	
			combo.setOpaque(true);
			loggingButtones.add(combo);
					
		} else {
			for (int i=0;i<butArray.length;i++){
				butArray[i].addActionListener(buttonHandler);
				loggingButtones.add(butArray[i]);
			}
		}

		
		// cb.setSelectedIndex(nSelectedIndex);

		fntSmall = Tc_AppConsole.guiSizer.logSmallFont;
		fntBig = Tc_AppConsole.guiSizer.logBigFont;
				  
		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setFont(fntSmall);
		textarea.setBackground(Color.LIGHT_GRAY);
		jsp = new JScrollPane ( textarea );
		if ( CoreBuilder.ws.isTablet) {
			jsp.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
			// jScrollPaneTi.getV
		}
		
		panel = new JPanel( );
		
		if ( buttonpos == null ) {
			panel.setLayout(new BorderLayout());
			panel.add(jsp, BorderLayout.CENTER);
		} else  {
			panel.setLayout(new BorderLayout());
			panel.add(jsp, BorderLayout.CENTER);
			panel.add(loggingButtones, buttonpos);			
		} 
					
		textarea.append(CoreBuilder.rslogger.getLevel().toString()+"\n");
		
		stdButtonFont = butZoomLog.getFont();
		boldButtonFont = new Font ( stdButtonFont.getName(),Font.BOLD,stdButtonFont.getSize());
		
		stdBackColor = butZoomLog.getBackground();
		boldBackColor = Color.LIGHT_GRAY;
		if ( loglevel_style != loglevelstyle_combo) {
			String current_level = CoreBuilder.rslogger.getLevel().toString().toUpperCase();
			int found_level = -1;
			for ( int i_level =0; i_level < logStrings.length; i_level++ ) {
				String x = logStrings[i_level];
				if ( x.equals(current_level)) {
					found_level = i_level;
					selectIndex(i_level);
				}
			}
		}
		
	}
	
	public static void singleClear () {
		if ( single != null )
			single.clear();
	}
	private void clear () {
		textarea.setText("");
	}
	public static void removeZoomViewer() {
		try {
			CoreBuilder.rslogger.removeAppender(zoomViewerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
		zoomViewer = null;
		zoomViewerId = null;
	}
	public void setText( String setThis ) {
		textarea.setText(setThis);
	}

	public void setFont( Font setThis ) {
		textarea.setFont(setThis);
	}
	
	public void setPositionEnd() {
		textarea.setCaretPosition(textarea.getText().length());
	}
	
	private void selectIndex ( int index ) {
		
		// String y   = level.toString();
		// Sp.p("level="+level+"="+y);
		String myLevelText = logStrings[index];
		if ( myLevelText.startsWith("DEBUG")) {
			CoreBuilder.rslogger.setLevel(Level.DEBUG);
			if ( myLevelText.equals("DEBUG")) {
				CoreBuilder.rslogger.setDebug_filter_tasks(true);
				CoreBuilder.rslogger.setDebug_filter_expressions(false);
				CoreBuilder.rslogger.setDebug_filter_logic(false);
				CoreBuilder.rslogger.setDebug_filter_calls(true);

			} else if ( myLevelText.equals("DEBUG_CALLS")) {
				CoreBuilder.rslogger.setDebug_filter_tasks(true);
				CoreBuilder.rslogger.setDebug_filter_expressions(false);
				CoreBuilder.rslogger.setDebug_filter_logic(false);
				CoreBuilder.rslogger.setDebug_filter_calls(true);

			} else if ( myLevelText.equals("DEBUG_LOGIC")) {
				
				CoreBuilder.rslogger.setDebug_filter_tasks(true);
				CoreBuilder.rslogger.setDebug_filter_expressions(false);
				CoreBuilder.rslogger.setDebug_filter_logic(true);
				CoreBuilder.rslogger.setDebug_filter_calls(true);

			}  else if ( myLevelText.equals("DEBUG_EXPRESSION")) {
				CoreBuilder.rslogger.setDebug_filter_tasks(true);
				CoreBuilder.rslogger.setDebug_filter_expressions(true);
				CoreBuilder.rslogger.setDebug_filter_logic(true);
				CoreBuilder.rslogger.setDebug_filter_calls(true);

			}else {
				CoreBuilder.rslogger.setDebug_filter_tasks(true);
				CoreBuilder.rslogger.setDebug_filter_expressions(true);
				CoreBuilder.rslogger.setDebug_filter_logic(true);
				CoreBuilder.rslogger.setDebug_filter_calls(false);
			}
		} else
			CoreBuilder.rslogger.setLevel(Level.toLevel(myLevelText));

		// buttons einfaerben
		if ( loglevel_style != loglevelstyle_combo) {
			for (int i=0;i<butArray.length;i++){
				if ( i == index ) { // x logStrings[i].equals(y)) {
					butArray[i].setFont(boldButtonFont);
					butArray[i].setBackground(boldBackColor);
				} else {
					butArray[i].setFont(stdButtonFont);
					butArray[i].setBackground(stdBackColor);
				}
			}
		}
		
		
	}
	private class ButtonHandler implements ActionListener {
		public void actionPerformed ( ActionEvent e )
		{

			if (e.getSource() == butClearLog) {
				textarea.setText("");

			} else if (e.getSource() == butZoomLog) {
				
				zoomViewer = new Map_LogViewer(layout,null,loglevel_style);
				zoomViewer.setText(textarea.getText());
				zoomViewer.setFont(fntBig);
				zoomViewerId = CoreBuilder.rslogger.addAppender(zoomViewer);				
				Tc_Modalmanager.modalmanager.requestModal((Tc_UiModal)zoomViewer);
			} else {
				for (int i=0;i<butArray.length;i++){
					if ( e.getSource() == butArray[i]) {
						selectIndex ( i );
					}
				}
			}
		}
	}


	public void setMapSetMaster(Tc_AppConsole appConsole3) {

		buttonRefresh.setMapSetMaster(appConsole3);

		
	}

	public void bind() {

		
	}

	public JComponent getJComponent() {
		if ( panel == null )
			create();

		if (true ) //  Privileg.logLevelSelection()
			return (JComponent)panel;
		else
			return (JComponent)jsp;
	}

	protected void append(LoggingEvent arg0) {
		// TODO Auto-generated method stub
		if ( panel == null ) {
			// Sp.p("LogViewer seems not created yet.");		
			create();
		}
		String meldung;
		if ( arg0.getLevel() == Level.INFO )
			meldung = "  "+ arg0.getRenderedMessage()+"\n";
		else
			meldung = (arg0.getLevel().toString().substring(0,1)).toLowerCase()+" "+ arg0.getRenderedMessage()+"\n";

		textarea.append(meldung);
		textarea.setCaretPosition(textarea.getText().length());
		
		
	}

	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public void start() {

		
		if ( panel == null )
			create();
		
	}	
	
	public void closeDialog()  {
	 try {
		CoreBuilder.rslogger.removeAppender(zoomViewerId);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		Sp.pe(e);
	}
	 zoomViewer = null;
	 zoomViewerId = null;
  }
  
	public String getTitle() {
	 return "Logging";
  }

	private void printJob2 ( DbJob job ) {


			int pos = textarea.getText().length();
			int posThere = textarea.getText().length()+1102;

			textarea.append(
					  "\n  === PROLOG === "+job.getFullfn()+" ==========\n"
					+ "\n  "
					+ job.getPrologText().replaceAll("\n", "\n  ")					
					);
			
			textarea.append(
					  "\n  === JOB ======= "+job.getFullfn()+" ==========\n"
					+ "\n  "
					+ job.getJobText().replaceAll("\n", "\n  ")					
					+"\n");
		
	}
	public static void printJob ( DbJob job) {
		if ( single != null ) {
			single.printJob2(job);
		}
		
	}
	
}

