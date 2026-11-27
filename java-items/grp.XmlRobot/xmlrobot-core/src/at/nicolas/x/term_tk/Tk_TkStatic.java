/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Workspace;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.term_core.Tc_AppConsole;



public class Tk_TkStatic {


	public static JButton getJButtonTarget ( String title ) {
		JButton button = new JButton(title);
		button.setPreferredSize(Tc_AppConsole.guiSizer.targetBestButtonDim);
		button.setVerticalTextPosition(SwingConstants.CENTER);
		
		return button;
	}	

	public static JButton getJButton ( String title ) {
		JButton button = new JButton(title);
		
		if ( CoreBuilder.ws.isTablet ) {
			button.setFont(Tc_AppConsole.guiSizer.tkFontTablet);
			button.setPreferredSize(Tc_AppConsole.guiSizer.tabletBestButtonDim);
		} else {
			button.setFont(Tc_AppConsole.guiSizer.tkFont);
			button.setPreferredSize(Tc_AppConsole.guiSizer.normalBestButtonDim);
		}
		
		return button;
	}
	public static int getSizeAdder() {
		if ( CoreBuilder.ws.isTablet ) 
			return Tc_AppConsole.guiSizer.tabletSizeadder;
		else
			return Tc_AppConsole.guiSizer.normalSizeadder;
	}
	/*
	public static Dimension getBestButtonDim() {
		if ( CoreBuilder.ws.isTablet ) 
			return Tc_AppConsole.guiSizer.tabletBestButtonDim;
		else
			return Tc_AppConsole.guiSizer.normalBestButtonDim;
	}
	
	public static Dimension getNormalBestButtonDim() {
		return Tc_AppConsole.guiSizer.normalBestButtonDim;
	}
	public static Dimension getTabletBestButtonDim() {
		return Tc_AppConsole.guiSizer.tabletBestButtonDim;
	}	 */
	
			
	//private static  normalSizeadder
	private static Dimension size = new Dimension ( 400,200);
	private static Point location = new Point (100,100);

	public static String convertToolTipText( String naturalTip, int width_px ) {
		String drittens = "<html><body><p style='width: "+width_px+"px;'>"+Commons.text_to_html(naturalTip)+"</p></body></html>";
		return drittens;
	}	
	public static String getLabelOrId(Element e ) {
		String ret = e.getAttributeValue("label");
		if (ret == null || ret.equals(""))
			ret = e.getAttributeValue("id");
		return ret;
	}
	
	public static String getLabel(Element e ) {
		String ret = e.getAttributeValue("label");
		if (ret.equals(""))
			ret = null;
		return ret;
	}	
	public static JPanel getPanel ( String label, JComponent center, JComponent buttons, String tip ) {
		
		return getPanel ( label, center, buttons, true, tip);
	}
	
	public static String getQuitMessage(Element tk ) {
		return "Stopped input of '"+tk.getAttributeValue("label")+"'";
	}
	public static JPanel getPanel ( String label, JComponent center, JComponent buttons, boolean border, String tip ) {
		
		
		
		JLabel jlabel = new JLabel(label);

		jlabel.setHorizontalAlignment(SwingConstants.CENTER);

		
		JPanel panel = new JPanel(new BorderLayout(20,10));	
		panel.add(jlabel,BorderLayout.NORTH);
		panel.add(center,BorderLayout.CENTER);
		if ( buttons != null )
			panel.add(buttons,BorderLayout.SOUTH);
		if ( tip != null ) {
			JTextArea jt = new JTextArea (tip);
			// nic123
			int number = (tip.length())*(80/70);

			jt.setPreferredSize(new Dimension(200,number));
			// jt.setMinimumSize(new Dimension(150,number));

			
			jt.setFont(jlabel.getFont());
			
			jt.setBackground(jlabel.getBackground());
			jt.setEditable(false);
			jt.setWrapStyleWord(true);
			jt.setLineWrap(true);
			panel.add(jt,BorderLayout.EAST);
		}
		
		
		if ( border )
			panel.setBorder( new EmptyBorder(10,10,10,10));
		return panel;
	}	
	private static boolean windowIsReady = false;

	public static boolean executeJDialog ( 
			JDialog jDialog, 
			String label1, 
			String label2, 
			Dimension dim, 
			Tk_IKeysOn ikeysOn, 
			JButton focusButton,
			int min_width) {
		//focusButton.setPreferredSize(new Dimension(100,20));
		if ( dim != null)
			jDialog.setSize(dim);
		jDialog.setTitle(label1); // +": "+label2
		jDialog.pack();
		// Sp.p("Station B");
		int current_width = jDialog.getSize().width;
		int current_height = jDialog.getSize().height;	
		int target_width = current_width;
		int target_height = current_height;

		if ( dim !=null ) {
			if ( dim.width == 800 && dim.height == 500 ) {
				target_width = current_width + 80; 								
			} else 	if ( dim.width == 801  ) {
				target_height = dim.height;				
			}
		} else {	
			if ( current_width < 700 || current_height < 600 ) {
				if ( current_width < 700  ) target_width = 700;
				if ( current_height < 600  ) target_height = 600;
			}			
		}
		
		if ( target_width < 250)
			target_width = 250;
		if ( min_width != -1 && target_width < min_width )
			target_width = min_width;
		Dimension sd = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if ( (target_height+100)>sd.height )
			target_height = sd.height - 100;
		if ( (target_width+100)>sd.width )
			target_width = sd.width - 100;
		
		if ( current_height != target_height || current_width != target_width ) {
			jDialog.setSize(new Dimension(target_width,target_height));
		}
		

		if ( !CoreBuilder.ws.isTablet ) {
			jDialog.setLocationRelativeTo(Tc_AppConsole.lastInstance.getJFrame());// AppConsole.lastInstance.getJFrame()s
			
		} else {
			// hier sind wir im tablet
			int vollbreite = Tc_AppConsole.lastInstance.getJFrame().getWidth();
			// das y ist klar: 0, aber das x ist unklar 
			
			int gebrauchte_breite = jDialog.getWidth();
			int position = (vollbreite - gebrauchte_breite) / 2;
			
			jDialog.setLocation(position, 0);			
		}

		windowIsReady =false;
		// Sp.p("Vor Set Modal");
		int gogogo = Tk_Token.go(0);
		jDialog.setModal(true);
		// Sp.p("Nac set modal");
		int gogogo2 = Tk_Token.leave(0);
		if ( Workspace.speed > 0 ) {
			try {
				// TimeUnit.MILLISECONDS.sleep(150);
				Sp.p("Went to sleep for "+Workspace.speed); // +": " + label1); // + " " + label2);
				// LogConnector lcx = new LogConnector ("SleeperInfo");
				// lcx.debug("Interface went to sleep for "+ Workspace.speed+": " + label1+ " " + label2);
				Thread.sleep(Workspace.speed);
			} catch (InterruptedException e) {
				Sp.pe(e);
			}			
		}
		
		// mal sehen: so nicht!
		/*
		iAmDone = false;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					iAmDone = true;
				}
			});
		} catch (InterruptedException e) {
		
		} catch (InvocationTargetException e) {
			
		}
		while (!iAmDone) {
			Sp.p("Waiting for Swing to be ready...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			
			}
		}
		

		*/
		// Sp.p("Station 5");
		if ( focusButton!=null ) {
			jDialog.getRootPane().setDefaultButton(focusButton);
			// focusButton.setFocusable(true);
			focusButton.requestFocus();
		}
		
		// Sp.p("**** TkStatic.executeJDialog, B-VOR-SET-VIS, THREAD-ID="+Thread.currentThread().getId());
		
		ikeysOn.keysOn();
		// Sp.p("vor Set visible");
		jDialog.setVisible(true);
		// Sp.p("nach set visible");
				
		size = jDialog.getSize();
		// Sp.p("Station 8");
		// Sp.p("jDialog size "+size.getWidth()+", "+size.getHeight());
		location = jDialog.getLocation();
		// dialog_on = false;
		// Sp.p("Station 9");
		// Sp.p("Hier geht das refresh rein");
		
		// jDialog.pack();
		// jDialog.setVisible(true);
		return true;
				
	}
	/*
	public static String getDefaultValueALT_TEXT (Element tk, String tipe, String id, ITiHandler tih) throws Exception {
		String what3 = "";
		
		String what = "("+tipe+","+id+")";
		String what2 = tih.translateString(what);

		if ( !what.equals(what2))
			what3 = what2;	
		else if ( tk.getText()!=null && tk.getText().length() > 0 ) {
			what3 = tk.getText();
		}
		return what3;
	}	
	
	public static String getDefaultValueALT (Element tk, String tipe, String id, ITiHandler tih) throws Exception {
		String what3 = "";
		if ( tk.getText()!=null && tk.getText().length() > 0 ) {
			what3 = tk.getText();
		} else {
			String what = "("+tipe+","+id+")";
			String what2 = tih.translateString(what);

			if ( !what.equals(what2))
				what3 = what2;			
		}

		return what3;
	}		
	*/
	/*
	public static String getDefaultValue20130605 (Element tk, String tipe, String id, ITiHandler tih) throws Exception {

		
		String what2 = tih.getVar(tipe, id);
		

		if ( tk.getAttributeValue("default")!= null) 
			JOptionPane.showMessageDialog(null, "tk-"+tk.getAttributeValue("type")+": default depressive, use x, found="+tk.getAttributeValue("default"));

		//if ( !what.equals(what2))
		//	what3 = what2;	
		// else 
		if ( tk.getAttributeValue("x")!=null && tk.getAttributeValue("x").length() > 0 ) {
			what2 = tk.getAttributeValue("x");
		}
		
		return what2;
	}	
	*/
	/*
	public static String getDefaultValueBis20130606 (Element tk, String tipe, String id, ITiHandler tih) throws Exception {
		String what3 = "";
		
		String what = "("+tipe+","+id+")";
		String what2 = tih.translateString(what);

		if ( tk.getAttributeValue("default")!= null) 
			JOptionPane.showMessageDialog(null, "tk-"+tk.getAttributeValue("type")+": default depressive, use x, found="+tk.getAttributeValue("default"));

		if ( !what.equals(what2))
			what3 = what2;	
		else if ( tk.getAttributeValue("x")!=null && tk.getAttributeValue("x").length() > 0 ) {
			what3 = tk.getAttributeValue("x");
		}
		
		return what3;
	}		
	*/	
	public static String getDefaultValue (Element tkN, String tipe, String idN, ITiHandler tih) throws Exception {
		String retval = null;
		
		try {
			retval = Entities.exportEntities(tih.getVar(tipe, Entities.importEntities(idN)));
		} catch (Exception e) {
			if ( tkN.getAttributeValue("x")!=null && tkN.getAttributeValue("x").length() > 0 ) {
				retval = tkN.getAttributeValue("x");
			} else
				retval = new String("");
		}
		
		return retval;
	}			

}