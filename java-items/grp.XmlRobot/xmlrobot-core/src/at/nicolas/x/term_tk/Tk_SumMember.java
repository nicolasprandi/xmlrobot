/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.term_core.Tc_AppConsole;


public class Tk_SumMember {

	Tk_Tk tk;
	private String regex;
	private String regex_msg;
	private Tk_MyMessageDialog regex_mmd;
	String label;
	String tip;
	
	private JPanel jPanel;
	private JButton edit_button;
	private JLabel label_label;
	private JLabel data_label;
	private JButton clear_button;
	private Tk_SumManager sumManager;
	private Element tk_elementN;
	private LogConnector lc;
	
	private boolean is_msg = false;
	private int height = 12;
	
	ButtonHandler buttonHandler;
	private boolean is_dynamo = false;
	
	private String follow_jobN = null;
	private int tk_width = 113;
	private int tk_width_divided = 60;
	
	public Tk_SumMember(
			Tk_SumManager sumManager, 
			Tk_Tk tk, 
			String regex, 
			String regex_msg,
			Tk_MyMessageDialog regex_mmd, 
			String label, 
			String tip, 
			boolean is_msg, 
			int height,
			Element tk_elementN,
			LogConnector lc) {
		this.sumManager = sumManager;
		this.tk = tk;
		this.regex = regex;
		this.regex_msg = regex_msg;
		this.regex_mmd = regex_mmd;		
		this.label = label;
		this.tip = tip;
		this.is_msg = is_msg;
		this.height = height;
		this.tk_elementN = tk_elementN;
		this.lc = lc;
		if ( this.tk_elementN.getAttribute("follow_job") != null ) {
			this.follow_jobN = this.tk_elementN.getAttributeValue("follow_job");
			// Sp.p("tk-sum-member with label ["+label+"] has follow_jobN ["+this.follow_jobN+"]");
		}
		
		int irgendwas = height;
		if ( CoreBuilder.ws.isTablet) {
			this.height = 20;
		}
		this.is_dynamo = Commons.hasFlag(this.tk_elementN, "dynamo");
		
		this.tk_width = this.sumManager.getTerminator().getTk_width();
		this.tk_width_divided = (this.tk_width - 3) / 2;
	}
	private Dimension editButtonLinuxDimension = new Dimension(20,height);
	private Dimension editButtonMacDimension = new Dimension(15,height);
	private EmptyBorder eBorder = new EmptyBorder(2,0,2,0);
	JComponent getJComponent () {
		edit_button = Tk_TkStatic.getJButtonTarget("");
		
		String el;
		if ( tip !=null)
			el = tip;
		else
			el = label;
		edit_button.setToolTipText(Tk_TkStatic.convertToolTipText(el, 370));
		
		label_label = new JLabel( "      "+Commons.stringReplace(label, "/n", ", ")+" " );
		label_label.setHorizontalAlignment(SwingConstants.RIGHT);
		label_label.setFont(Tc_AppConsole.guiSizer.tkFont);
		clear_button = Tk_TkStatic.getJButtonTarget("x");
		clear_button.setToolTipText("Clear "+label);
		
		if ( Tc_AppConsole.linux) {
			edit_button.setMinimumSize(editButtonLinuxDimension);
			clear_button.setMinimumSize(editButtonLinuxDimension);
			edit_button.setText("edit");
		} else if ( Tc_AppConsole.mac) {
			edit_button.setSize(editButtonMacDimension);
			clear_button.setSize(editButtonMacDimension);
			// edit_button.setMaximumSize(editButtonMacDimension);
			// clear_button.setMaximumSize(editButtonMacDimension);
		}
		
		clear_button.addActionListener(new ClearButtonHandler());
		// clear_button.setPreferredSize( JobButtonsApp.prefferedHelpButtonDimension);

		if ( !is_msg) {
			data_label = new JLabel(" [ "+cutData(tk.getValuePN())+" ]");
			data_label.setFont(Tc_AppConsole.guiSizer.tkFont);
			// data_label.setPreferredSize(new Dimension(500,20));
			data_label.setBorder(eBorder);
		}
		
		jPanel = new JPanel(new BorderLayout());
		
		JPanel rpanel = new JPanel(new BorderLayout());
		if ( !is_msg) {
		rpanel.add(label_label,BorderLayout.CENTER);
		rpanel.add(edit_button,BorderLayout.WEST);
		} else {
			JLabel msgLabel = new JLabel(tk.getValuePN());
			// Font x = msgLabel.getFont();
			// Font y = new Font(x.getFontName(),Font.BOLD , x.getSize());
			
			msgLabel.setFont(Tc_AppConsole.guiSizer.jobButtonFont);
			msgLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			rpanel.add(msgLabel,BorderLayout.CENTER);

		}
		
		jPanel.add(rpanel,BorderLayout.WEST);
		if (!is_msg) {
			jPanel.add(data_label,BorderLayout.CENTER);
			if ( !tk.hasNono())
				jPanel.add(clear_button, BorderLayout.EAST);
		} 
		
		if ( CoreBuilder.ws.isMac || CoreBuilder.ws.isLinux)
			rpanel.setPreferredSize( 
					new Dimension ((Tc_AppConsole.guiSizer.tkSumLabelWidth*4)/3,height));
		else
			rpanel.setPreferredSize( 
					new Dimension (Tc_AppConsole.guiSizer.tkSumLabelWidth,height));
	
		
		buttonHandler = new ButtonHandler();
				
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER ) {
					ke.consume();
					
				}
			}
			
		};

		edit_button.addActionListener(buttonHandler);
		edit_button.setFocusable(false);
		
		return (JComponent)jPanel;
		
	}
public boolean finalisieren(boolean performValidation, String from) throws Exception {
	boolean stay_in = false;
	if ( is_msg ) return false;

	String current_valN = tk.getValueN();

	if ( performValidation ) {
		Tk_MyMessageDialog mmd2 = FitsRegex.fits(
				tk.getValue4RegexN(), tk.hasNono(), regex, regex_mmd,label);
		if ( mmd2 == null ) {
			// regex is ok
		
			FitsRuleResult frr = FitsRule.check(
									current_valN, 
									tk_elementN.getAttributeValue("rule"), 
									tk, 
									label);
			
			if ( frr.changed ) {
				
				Tk_MyMessageDialog rule_msg = new Tk_MyMessageDialog();
				rule_msg.built(
						frr.askMessageHeader,frr.askMessage);
				rule_msg.setOkText("Yes, I accept the update");
				rule_msg.setCancelText("No");
				rule_msg.setTopHeaderText("Warning");
				rule_msg.setFocusToOkButton();
				boolean rule_msg_ok = rule_msg.execute();
				
				if ( !rule_msg_ok) {
					stay_in = true;
					// throw new TkStopException("Disagreement about reformating value of "+label);
				} else {
					tk.setValueN(frr.newValue);
					current_valN = frr.newValue;					
				}
			}
		} else {
			// regex is not ok
			boolean good = mmd2.execute();
			// Sp.p("good="+good);
			if ( !good ) {
				throw new Exception("Message not confirmed, was about '"+label+"'");
			}				
			stay_in = true;
		}
	}
	
	if ( !stay_in ) {
		if ( follow_jobN == null ) {
			sumManager.tih.setVar(
					tk.getVarType(), 
					Entities.importEntities(tk.getVarIdN()), 
					Entities.importEntities(current_valN));	
			/*
			lc.debug("["+from+"] [sum-member-finalisieren] "+tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");
			Sp.p("["+from+"] sum-member-finalisieren] Tk reflected to memory: ("+
					tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");
					*/
		} else {
			sumManager.tih.setVarFollow(
					tk.getVarType(), 
					Entities.importEntities(tk.getVarIdN()), 
					Entities.importEntities(current_valN),
					follow_jobN);	
			/*
			lc.debug("["+from+"] [sum-member-finalisieren] "+tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");
			Sp.p("["+from+"] sum-member-finalisieren] Tk reflected to memory: ("+
					tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");		
					*/	
		}
	}
	
	return stay_in;

}
	
private void refreshValue ( String neuN, String from ) {
	tk.setValueN(neuN);
	data_label.setText(" [ "+cutData(tk.getValuePN())+" ] ");
	reflectVariableToMemory(from+" refresh");
	sumManager.callbackInputDone(this, neuN);	
}
private void reflectVariableToMemory(String from) {
	try {
		if ( follow_jobN == null ) {
			sumManager.tih.setVar(
					tk.getVarType(), 
					Entities.importEntities(tk.getVarIdN()), 
					Entities.importEntities(tk.getValueN()));
			/*
			lc.debug("["+from+"] "+tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");
			Sp.p("["+from+"] "+"Tk reflected to memory: ("+
					tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");		
					*/	
		} else {
			sumManager.tih.setVarFollow(
					tk.getVarType(), 
					Entities.importEntities(tk.getVarIdN()), 
					Entities.importEntities(tk.getValueN()),
					follow_jobN);
			/*
			lc.debug("["+from+"] "+tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");
			Sp.p("["+from+"] "+"Tk reflected to memory: ("+
					tk.getVarType()+","+tk.getVarIdN()+") is '"+tk.getValueN()+"'");	
					*/			
		}

	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, "#999 Could not reflect ("+
				tk.getVarType()+","+tk.getVarIdN()+") to "+tk.getValueN(), 
				"Call your Script-Developer!", JOptionPane.ERROR_MESSAGE);
	}	
}
	private synchronized void edit() {
		
		
		try {
			boolean valid_regex = false;
			String oldN = tk.getValueN();
			if ( (oldN==null || oldN.equals(""))
				&& tk_elementN.getAttribute("x2") != null ) {
				lc.debug_expressions("taking now x2: "+tk_elementN.getAttributeValue("x2"));
				oldN=tk_elementN.getAttributeValue("x2");
				tk.setValueN(oldN);
			}
			
			if ( is_dynamo ) {
				Tk_MyMessageDialog dyn_msg = new Tk_MyMessageDialog();
				dyn_msg.built(
						"Dynamic Field",
						"The field '"+label+"' triggers a dynamic behaviour of other input-fields.\n "+
						"To make that dynamics work you have to use the button [ Edit All ] "
						);
				boolean ok = dyn_msg.execute();
				if ( !ok )
					valid_regex = true;
				

			}
			
			while ( !valid_regex ) {
				
				tk.execute(false);
				String resultN = tk.getValueN();
				// Sp.p("nach tk get value");
				
				if ( resultN != null ) {
					data_label.setText(" [ "+cutData(tk.getValuePN())+" ] ");
					
					sumManager.callbackInputDone(this, resultN);
				}		
				Tk_MyMessageDialog mmd2 = FitsRegex.fits(
						tk.getValue4RegexN(), tk.hasNono(), regex, regex_mmd,label);
				if ( mmd2 == null ) {
					// regex is  ok
					
					FitsRuleResult frr = FitsRule.check(
							resultN, 
							tk_elementN.getAttributeValue("rule"), 
							tk, 
							label);
	
					if ( frr.changed ) {
						Tk_MyMessageDialog rule_msg = new Tk_MyMessageDialog();
						rule_msg.built(
								frr.askMessageHeader,frr.askMessage);
						rule_msg.setOkText("Yes, I accept the update");
						rule_msg.setCancelText("No");
						rule_msg.setTopHeaderText("Warning");
						rule_msg.setFocusToOkButton();
						boolean rule_msg_ok = rule_msg.execute();
						
						if ( !rule_msg_ok) {
							valid_regex = false;
							// throw new TkStopException("Disagreement about reformating value of "+label);
						} else {
							refreshValue(frr.newValue,"edit: rule-update");								
							valid_regex = true;
						}
					} else {					
						valid_regex = true;
						reflectVariableToMemory("edit: rule + regex ok");
					}
					
					
				} else {
					// the regex it not ok
					valid_regex = false;
					boolean good = mmd2.execute();
					// Sp.p("good="+good);
					if ( !good ) {
						refreshValue(oldN,"edit: restore old value");	
						// Sp.p("did set to old="+old);
						throw new Exception("Request for valid input not confirmed, was about '"+label+"'");
					}					
				}
				sumManager.setFocusOk();
			}

		} catch (Exception e) {
			
		}
		
	}
	private int count_callbackEnter = 0;
	private void callbackEnter() {
		count_callbackEnter++;
		if ( count_callbackEnter > 1)
			sumManager.callbackEnter();
	}
	
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				JButton pressed = (JButton)source;
				edit();								
			}
		}
	}
	private synchronized void clear() {
		
		/*if ( regex != null ) {
			JOptionPane.showMessageDialog(null, "Clearing for this value not possible, there is a VALIDATION, go to the Edit-Button!");
			
		} else {*/
			try {
				tk.clearValue();
				String resultN = tk.getValueN();
				if ( resultN != null ) {
					data_label.setText(" [ "+cutData(tk.getValuePN())+" ] ");
					this.reflectVariableToMemory("Clear Button");
					//sumManager.tih.setVar(tk.getVarType(), 
					//		Entities.importEntities(tk.getVarIdN()), 
					//		Entities.importEntities(resultN));
					sumManager.callbackInputDone(this, resultN);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.p("Problem in Clearing a value");
				Sp.pe(e);
			}			
		/* } */
			
		
	
	}
	private class ClearButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			clear();				

		}
	}	
	

	
	private  String cutData(String in ) {
		
		if ( in == null )
			return in;
		else if ( in.length() < tk_width )
			return in;
		else {
			String out = in.substring(0, tk_width_divided) + 
					"..."+in.substring(in.length()-tk_width_divided);
			return out;
		}
	}	
}