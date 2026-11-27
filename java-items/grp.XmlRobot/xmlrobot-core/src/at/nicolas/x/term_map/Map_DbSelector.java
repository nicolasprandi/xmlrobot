/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdom2.Element;

import at.nicolas.x.int_const.Zet_Privileg;
import at.nicolas.x.int_db.Db;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbPro;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_DbSelector implements Tc_UiMap {

	Tc_AppConsole appConsole3 = null; 
	DefaultMutableTreeNode jtable_root = null;
	JScrollPane jScrollPane = null;
	JTree jtree = null;
	Tc_UiContext myUiContext = null;
	boolean fire = true;
	Map_ButtonProjectManager bpm = null;
	Map_ButtonRefresh buttonRefresh = null;
	JPanel a = null;
	JButton  pmbutton = null;
	
	public static Color myBlue = new Color(0,20,142); // new Color(103,103,225);
	Color myBlack = Color.BLACK;
	
	List<MyNode> myAppList = new ArrayList<MyNode>();
	
	public static Map_DbSelector single = null;
	TreeRenderer treeRenderer = new TreeRenderer();
	
	public JPanel getJPanel() {
		return a;
	}
	public Map_DbSelector ( Font font, Color foregroundColor , Color backgroundColor, boolean viewButtons ) {
		single = this;
		

		myUiContext = new Tc_UiContext();
				
		jtable_root = new DefaultMutableTreeNode("Applications // Projects");
		
		
		ArrayList<DbApp> appList = CoreBuilder.db.getAppList();
		Iterator<DbApp> iAppList = appList.iterator();
		while ( iAppList.hasNext()) {
			DbApp app = (DbApp)iAppList.next();

			boolean showThisApp = true;
			String myAppId = app.getId();
			if ( app.isHidden() )
				showThisApp = false;
			
			if ( showThisApp ) {
				DefaultMutableTreeNode appNode = new DefaultMutableTreeNode(app.getLabel());
				MyNode myAppNode = new MyNode(true,app.getLabel(),null);
				appNode.setUserObject(myAppNode);
				
				myAppList.add(myAppNode);
			
				jtable_root.add( appNode );

				
				ArrayList<DbPro> instList = app.getInstList();
				Iterator<DbPro> iInstList = instList.iterator();
				while ( iInstList.hasNext()) {
					DbPro inst = (DbPro)iInstList.next();
					if ( !inst.getId().equals("Standard-Project")) {
						DefaultMutableTreeNode instNode = new DefaultMutableTreeNode(inst.getId());
						MyNode myInstNode = new MyNode(false,app.getLabel(),inst.getId());
						instNode.setUserObject(myInstNode);
						appNode.add(instNode);						
					}

				}				
			}

		}
		
		jtree = new JTree(jtable_root);
		// jtree.getComponent(1).setBackground(Color.GREEN);
		// funktioniert im java 7 leider nicht
		if ( CoreBuilder.ws.isTablet)
			jtree.setRowHeight(34);
		else
			jtree.setRowHeight(Tc_AppConsole.guiSizer.dbRowHeight);
		/*
		jtree.setFont(Tc_AppConsole.guiSizer.dbFont);		
		
		if ( backgroundColor != null )
			jtree.setBackground(backgroundColor);
		if ( foregroundColor != null )
			jtree.setForeground(foregroundColor);
		*/
		
		jtree.setToggleClickCount(0);
		// treeRenderer.setMinimumSize(new Dimension(200,50));
		jtree.setCellRenderer(treeRenderer);
		jtree.addTreeSelectionListener(new TreeSelectionListener () {
		    public void valueChanged(TreeSelectionEvent e) {
		    	if ( fire ) {
		    		
			    	TreePath[] treePathArray = e.getPaths();
			    	TreePath wichtig=treePathArray[0];
			    	// der is es nicht
			    	// Sp.p("TreeSelectionListener.valueChanged-pathcount="
			    	// +wichtig.getPathCount()+" at time "+System.currentTimeMillis());
			    	
			    	String thisAppId = null;
			    	if ( wichtig.getPathCount() >= 2 )
			    		thisAppId = Db.label_to_AppId(wichtig.getPathComponent(1).toString());
			    	
			    	if ( wichtig.getPathCount() == 1 ) {
			    		// Sp.p("Vor aufruf selectRoot");
			    		selectRoot();
			    	} else if ( wichtig.getPathCount() == 2 ) {
			    		// applikationsebene
			    		// pruefen ob diese app ein static_project hat
			    		if ( thisAppId !=null) {
			    			// Sp.p("-a--"+thisAppId);
			    			
			    			selectApp(thisAppId);
			    			
			    		}
			    	} else if ( wichtig.getPathCount() == 3 ) {
			    		// Sp.p(" pathcount 3: app="+thisAppId);
			    		if (thisAppId !=null && (wichtig.getPathComponent(2).toString() !=null))
			    		selectInst(thisAppId, wichtig.getPathComponent(2).toString());
			    	}		    		
		    	}
		    }
		});
		
		jtree.addMouseListener( new MouseAdapter()  {
			public void mousePressed(MouseEvent e) {
				
				int selRow = jtree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = jtree.getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						doubleClick();
						jtree.expandRow(selRow);
					} 
				}
				
				
			}
		});

		bpm = new Map_ButtonProjectManager();
		buttonRefresh = new Map_ButtonRefresh(null,null,null);
		jScrollPane = new JScrollPane(jtree);
		
		if ( CoreBuilder.ws.isTablet) {
			jScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(38,38));
			jScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(20,20));
			// jScrollPane.getVerticalScrollBar().
			// jScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(30,30));
		} else
			jScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		a = new JPanel ( new BorderLayout());

		int count_buttons = 1;
		if ( Zet_Privileg.showRestartButton())
			count_buttons = 2;
		
		boolean additional_button = CoreBuilder.ws.isTablet;
		additional_button = true;

		if ( additional_button) {
			count_buttons++;
		}
		
		JPanel x = new JPanel ( new GridLayout(1,count_buttons));
		
		if ( additional_button) {
			pmbutton = new JButton("View all");
			pmbutton.setPreferredSize(new Dimension(20,40));
			x.add(pmbutton);
			SwitchButtonHandler switchButtonHandler = new SwitchButtonHandler();
			pmbutton.addActionListener(switchButtonHandler);
			x.add(bpm.getJComponent());
		}
				
		if ( Zet_Privileg.showRestartButton())
			x.add(buttonRefresh.getJComponent());	
				
		a.add(x,BorderLayout.NORTH);
		a.add(jScrollPane,BorderLayout.CENTER);	
		//a.setPreferredSize(new Dimension(200,100));
		
		
	}
	private void expandApp ( String thisapp ) {
		Element x = new Element("apps");
		Element y = new Element("app");
		y.setText(thisapp);
		x.addContent(y);
		// Sp.p("expandApp "+thisapp);
		setExpandedApps ( x);
		
	}
	public void setExpandedApps ( Element expanded_nodes ) {
		if ( expanded_nodes == null )
			return;
		try {
			List<Element> applist = expanded_nodes.getChildren("app");
			Iterator<Element> it = applist.iterator();
			while ( it.hasNext()) {
				Element now = (Element)it.next();
				String appname = now.getText();

				int count = jtable_root.getChildCount(); 
				// Sp.p("... count="+count);
				for ( int i=0;i<count;i++) {
					// Sp.p("... i="+i);
					DefaultMutableTreeNode child = (DefaultMutableTreeNode)jtable_root.getChildAt(i);
					
					Object userObject = child.getUserObject();
					if ( userObject instanceof MyNode ) {

						MyNode myNode = (MyNode)userObject;
						
						if (myNode.isApp) {
							

							if ( (myNode.appname).equals(appname)) {
								TreePath tp = new TreePath(child.getPath());
								jtree.expandPath(tp);
							}
							
						}
					} 			

				
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
		// Sp.p("Move to left ");
		goLeft();

		
	}	
	private void goLeft() {
		if ( CoreBuilder.ws.isTablet) {
			
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					JScrollBar hori =jScrollPane.getHorizontalScrollBar();
					hori.setValue(hori.getMaximum());
				}
			});			
			
		}
		
	}
	public void setExpandedApps_ALT ( Element expanded_nodes ) {
		if ( expanded_nodes == null )
			return;
		try {
			List<Element> applist = expanded_nodes.getChildren("app");
			Iterator<Element> it = applist.iterator();
			while ( it.hasNext()) {
				Element now = (Element)it.next();
				String appname = now.getText();

				int count = jtable_root.getChildCount(); 
				// Sp.p("... count="+count);
				for ( int i=0;i<count;i++) {
					// Sp.p("... i="+i);
					DefaultMutableTreeNode child = (DefaultMutableTreeNode)jtable_root.getChildAt(i);
					TreePath tp = new TreePath(child.getPath());

					String this_app = Db.label_to_AppId(tp.getPathComponent(1).toString());
					if ( this_app.equals(appname)) {
						jtree.expandPath(tp);
					}				
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
		
	}
	public Element getExpandedApps() {

		Element ret = new Element("expanded_nodes");
		
		Iterator it = myAppList.iterator();
		while ( it.hasNext()) {
			MyNode myAppNode = (MyNode)it.next();
			if ( myAppNode.expanded) {
				// Sp.p("EXPANDED: "+myAppNode.appname);
				Element expanded = new Element ("app");
				expanded.setText(myAppNode.appname);
				ret.addContent(expanded);
			}
		}
		
		return ret;
		
	}	
	public Element getExpandedApps_ALT() {

		Element ret = new Element("expanded_nodes");
		
		// TreePath anchorPath = jtree.getAnchorSelectionPath();
		int count = jtable_root.getChildCount(); 

		for ( int i=0;i<count;i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)jtable_root.getChildAt(i);
			TreePath tp = new TreePath(child.getPath());
			if ( jtree.isExpanded(tp)) {
				Element expanded = new Element ("app");
				// Sp.p("--- SAVE EXPANDES ---- "+tp.getPathComponent(1).toString());
				expanded.setText(Db.label_to_AppId(tp.getPathComponent(1).toString()));
				ret.addContent(expanded);
			}
			
		}
		return ret;
		
	}
	
	private void selectRoot( ) {
		// Sp.p("DB: selectRoot");
		myUiContext = new Tc_UiContext();
		appConsole3.sendMapMessage(this,myUiContext);
		
	}

	private void selectApp( String _app ) {
		// Sp.p("DbSelector.selectApp " + _app );
		// Conf.logger.info("select inst, app="+_app );
		try {
			Tc_UiContext currentContext = appConsole3.getCurrentContext();
			// Sp.p("DB: selectApp:"+_app +", current app="+currentContext.getApp()
			// 		+", current pro="+currentContext.getInst());
			if ( currentContext == null );
			else if ( currentContext.getApp()==null || !currentContext.getApp().equals(_app) ) {
				// Sp.p("Different app");
				myUiContext.setApp(_app);
				myUiContext.setInst(null);
				myUiContext.setJob(null);
				appConsole3.sendMapMessage(this,myUiContext);
				// expandApp(_app); leider nicht
			} else {
				// Sp.p("Same app");
				// Sp.p("DO SAMMA");
				myUiContext.setApp(_app);
				if ( myUiContext.getInst() != null ) {
					myUiContext.setInst(null);
					appConsole3.sendMapMessage(this,myUiContext);	
				}
				//myUiContext.setInst(null);
				//appConsole3.sendMapMessage(this,myUiContext);			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
			
		
		
	}

	private void selectInst( String _app, String _inst ) {
		Tc_UiContext currentContext = appConsole3.getCurrentContext();
		if ( currentContext.getApp()!=_app || currentContext.getInst()!=_inst) {
			myUiContext.setApp(_app);
			myUiContext.setInst(_inst);
			myUiContext.setJob(null);
			// Sp.p("ooooooooooooo setting selector context app="+_app+" inst="+_inst);
			appConsole3.sendMapMessage(this,myUiContext);
		}
								
	}
	
	private void doubleClick() {
		// Sp.p("UU: Double Click");
		appConsole3.dbDoubleClick();
	}
	private void threeClick() {
		// Sp.p("UU: Three Click");
		appConsole3.dbThreeClick();
	}	

	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
		// bdba.setMapSetMaster(appConsole3);
		buttonRefresh.setMapSetMaster(appConsole3);
		bpm.setMapSetMaster(_appConsole3);
	}

	public void bind() {
		/*Sp.p("DB: bind to app="
				+appConsole3.getCurrentContext().getApp()
				+", pro="
				+appConsole3.getCurrentContext().getInst()); */
		if ( Tc_UiContext.changedInst(myUiContext,appConsole3.getCurrentContext())) {
			// Conf.logger.warn("changed Inst");
			DefaultMutableTreeNode expandTo = null;
			if ( appConsole3.getCurrentContext().getApp() == null ) {
				
			} else {
				try {
					DbApp app = CoreBuilder.db.getAppById(appConsole3.getCurrentContext().getApp());
					// Sp.p("------> MapDbSelector goes to:"+appConsole3.getCurrentContext().getApp());
					int appIndex = CoreBuilder.db.getAppIndexById(appConsole3.getCurrentContext().getApp());
					// Conf.logger.warn("bind|start|appIndex="+appIndex);
					expandTo = (DefaultMutableTreeNode)jtable_root.getChildAt(appIndex);
					if ( appConsole3.getCurrentContext().getInst() != null ) {
						Sp.p("Searching for inst: "+appConsole3.getCurrentContext().getInst());
						try {
							ArrayList<DbPro> il = app.getInstList();
							Iterator<DbPro> ix = il.iterator();
							int xh=0;
							int sysadd=0;
							while ( ix.hasNext()) {
								DbPro aktPro = (DbPro)ix.next();
								
								Sp.p(xh+") Pro "+aktPro.getId()+" - in - "+aktPro.getDir());
								xh++;
							}
							int instIndex = app.getInstIndexByIdGui(appConsole3.getCurrentContext().getInst());
							Sp.p("== Requested index="+instIndex+" from:"+appConsole3.getCurrentContext().getInst());
							Sp.p("   Child count="+jtable_root.getChildAt(appIndex).getChildCount());
							expandTo = (DefaultMutableTreeNode)jtable_root.getChildAt(appIndex).getChildAt(instIndex);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Sp.p("== Requested index ...da hats was");
							Sp.pe(e);
						}
					}
					TreeNode x[] = expandTo.getPath();
					for ( int i=0;i<x.length;i++) {
						// Conf.logger.warn(i+"="+x[i].toString());
					}
					fire = false;
					expandAll(jtree, new TreePath(jtable_root), false);
					expandAll(jtree,new TreePath(x),true);
					jtree.setSelectionPath(new TreePath(x));
					fire = true;
					// jtree.expandPath(new TreePath(x[1]));
					// jtree.expandPath()
					// appNode.
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Sp.pe(e);
				}
			}
			myUiContext = appConsole3.getCurrentContext().cloneMe();
		}
		bpm.bind();
		
	}
	
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }	

	public JComponent getJComponent() {
		return ((JComponent)a);
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}
	private String app_selection =null;
	private void open_close_operation() {
		// Sp.p("DB: Switch");
		last_open_close++;
		if ( last_open_close == 2 )
			last_open_close = 0;
		// Sp.p("---- DB: Switch to: "+last_open_close);
		String current_app = myUiContext.getApp();
		if ( current_app==null && last_open_close==2)
			last_open_close = 0;
		if ( last_open_close == 0)
			pmbutton.setText("View all");
		else if  ( last_open_close == 1)
			pmbutton.setText("Close all");
		else
			pmbutton.setText(""+last_open_close);
		// Sp.p("Doing: "+last_open_close);
		/*
		ArrayList<DbApp> appList = CoreConf.db.getAppList();
		Iterator<DbApp> iAppList = appList.iterator();
		while ( iAppList.hasNext()) {
			DbApp app = (DbApp)iAppList.next();
			String appId = app.getId();
		}
		*/
		
		int count = jtable_root.getChildCount(); 
		// Sp.p("... count="+count);
		for ( int i=0;i<count;i++) {
			// Sp.p("... i="+i);
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)jtable_root.getChildAt(i);
			TreePath tp = new TreePath(child.getPath());
			// Sp.p("Label="+tp.getPathComponent(1).toString());
			
			String this_app = Db.label_to_AppId(tp.getPathComponent(1).toString());

			if ( last_open_close == 0 ) {
				jtree.collapsePath(tp);
				goLeft();
			} else if ( last_open_close == 1 ) {
				jtree.expandPath(tp);
				goLeft();
			} else if ( last_open_close == 2 ) {
				// jtree.collapsePath(tp);
				if ( current_app != null) {
					//SetSwitchApp(current_app);
					app_selection = current_app;
					// Sp.p("   this_app="+this_app);
					// Sp.p("current_app="+current_app);
					if ( this_app == null );
					else if ( this_app.equals(current_app)) {						
						java.awt.EventQueue.invokeLater(new Runnable() {
							public void run() {
								SetSwitchAppWithSelection( app_selection );
							}
						});							
					} else {
						java.awt.EventQueue.invokeLater(new Runnable() {
							public void run() {
								SetSwitchAppWithoutSelection( app_selection );
							}
						});							
					}

					
					goLeft();
					
					
				}
					
				/*
				if ( this_app.equals(current_app))
					jtree.expandPath(tp);
				else
					jtree.collapsePath(tp);
					*/
			}
		}
		
	}
	private int last_open_close = 0; /* 0,1,2 */
	
	private void SetSwitchAppWithoutSelection( String go_app ) {
		// Sp.p("-- detail SetSwitchApp: "+go_app);
		if ( go_app != null && go_app.length() > 0) {
			DbApp app = CoreBuilder.db.getAppById(go_app);
			int appIndex = CoreBuilder.db.getAppIndexById(go_app);
			// Conf.logger.warn("bind|start|appIndex="+appIndex);
			DefaultMutableTreeNode expandTo = (DefaultMutableTreeNode)jtable_root.getChildAt(appIndex);
			TreeNode x[] = expandTo.getPath();
			fire = false;
			expandAll(jtree, new TreePath(jtable_root), false);
			expandAll(jtree,new TreePath(x),true);			
		}

	}
	
	private void SetSwitchAppWithSelection( String go_app ) {
		// Sp.p("-- detail SetSwitchApp: "+go_app);
		if ( go_app != null && go_app.length() > 0) {
			DbApp app = CoreBuilder.db.getAppById(go_app);
			int appIndex = CoreBuilder.db.getAppIndexById(go_app);
			// Conf.logger.warn("bind|start|appIndex="+appIndex);
			DefaultMutableTreeNode expandTo = (DefaultMutableTreeNode)jtable_root.getChildAt(appIndex);
			TreeNode x[] = expandTo.getPath();
			fire = false;
			expandAll(jtree, new TreePath(jtable_root), false);
			expandAll(jtree,new TreePath(x),true);
			jtree.setSelectionPath(new TreePath(x));			
		}

	}	
	
	private class SwitchButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )    {
			single.open_close_operation();

		}
	}
	
	class TreeRenderer extends JLabel implements TreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			// TODO Auto-generated method stub


			// Sp.p("RENDERER: "+value.toString()+"  "+row);
			
			Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			if ( userObject instanceof MyNode ) {

				MyNode myNode = (MyNode)userObject;

				
				if (myNode.isApp) {
					setText(myNode.appname);
					setForeground(myBlue);
					if ( selected )
					selectApp(myNode.appname);
					myNode.setExpanded(expanded);
					setFont(Tc_AppConsole.guiSizer.dbFontApps);
					// kommt of dran Sp.p("-z--"+myNode.appname);
				} else {
					setText(myNode.proname);
					setForeground(myBlack);
					if ( selected )
					selectInst(myNode.appname,myNode.proname);
					setFont(Tc_AppConsole.guiSizer.dbFont);	
				}
			} else {
				setForeground(myBlue);
				setText(value.toString());
				if ( selected )
				selectRoot();
				setFont(Tc_AppConsole.guiSizer.dbFontApps);	
				// Sp.p("--- SELECT ROOT ---");
				
			}
		
			
			// setForeground(Color.GREEN);
			if ( selected ) {
				setBackground(Color.GRAY);
				setForeground(Color.WHITE);
				setOpaque(true);
			} else {
				
				setBackground(Color.WHITE);
			}

			/*
			if ( CoreBuilder.ws.isTablet)
				jtree.setRowHeight(28);
			else
				jtree.setRowHeight(Tc_AppConsole.guiSizer.dbRowHeight);
				*/
			// Border border = BorderFactory.createEmptyBorder(40, 40, 40, 40);
			// setBorder(border);
				
			return this;
		}
		
	}
	
	class MyNode{
		public boolean isApp;
		public String appname;
		public String proname;
		boolean expanded = false;
		
		public MyNode ( boolean isApp,String appname, String proname) {
			this.isApp = isApp;
			this.appname = appname;
			this.proname = proname;
		}
		public void setExpanded( boolean expanded ) {
			this.expanded = expanded;
		}
	}
}