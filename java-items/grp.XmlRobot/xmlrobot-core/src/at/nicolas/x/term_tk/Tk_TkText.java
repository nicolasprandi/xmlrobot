/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.term_core.Tc_AppConsole;



public class Tk_TkText extends Tk_Tk {

	private Element tkN;
	private String idN;
	ITiHandler tih;
	String mainLabel;
	String tip;
	private String curValN = null;
	boolean nono;
	
	JButton okButton;
	JButton cancelButton;

	ButtonHandler buttonHandler;
	
	static String ok= new String("Ok");
	static String cancel= new String("No Value");
	
	String myLabel;
	private String no_valueN = "";
	
	//   TK STATIC - BEGIN
	
	//   TK STATIC - END
	
	
	public void built(Element tkN,ITiHandler tih,String mainLabel, String tip) throws Exception {
		// Sp.p("tk-text got: "+Commons.toText(tkN));
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabel = mainLabel;
		this.tip = tip;
		idN = tkN.getAttributeValue("id");
		this.nono = Commons.hasFlag(tkN, "nono");
		this.curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);
		if ( tkN.getAttribute("no_is")!=null)
			no_valueN = tkN.getAttributeValue("no_is");
		// Sp.p("TkText (id="+id+") has initial value: "+curVal);

	}

	public String getVarType() {
		return ("prop");
	}

	public String getVarIdN() {
		return idN;
	}
	
	
	
	public void execute(boolean first_go) throws Exception {
		did_a_cancel = false;
		// Sp.p("**** TkText.------ STARTED EXECUTE ---------THREAD-ID="+Thread.currentThread().getId());
		// Sp.p("Tk-Text received execute with value="+curVal);

		JTextField textField = new JTextField(curValN,30);
		

		if ( CoreBuilder.ws.isTablet ) {
			textField.setFont(Tc_AppConsole.guiSizer.tkFontTablet);
		} else {
			textField.setFont(Tc_AppConsole.guiSizer.tkFont);
		}
		
		JButton okButton = Tk_TkStatic.getJButton(ok);
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);
		int sizeadder = Tk_TkStatic.getSizeAdder();
			
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		if ( !nono)
			buttonPanel.add(cancelButton);		
		myLabel = Tk_TkStatic.getLabelOrId(tkN);
		// Sp.p("+++++++++++++ CREATING      : "+myLabel);
		JPanel panel = Tk_TkStatic.getPanel(myLabel, textField, buttonPanel,tip);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				wcTrue();			
			}
			
		});
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(new ClearButtonHandler());
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				// Sp.p("**** TkText.KeyEvent----------THREAD-ID="+Thread.currentThread().getId());
				ke.consume();
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER 
						&& keys_on && (ke.getWhen()>keys_on_start) ) {
					//Sp.p("***A*** mouse double clicked"+mainLabel);
					wcFalse();
					dispose();
				} else {
					//Sp.p("keys_on:"+keys_on);
					//Sp.p("ke.getWhen():"+ke.getWhen());
					//Sp.p("keys_on_start:"+keys_on_start);
				}
			}
			
		};	

		textField.addKeyListener(keyAdapter);
		textField.selectAll();
		
		
		if ( Tk_TkStatic.executeJDialog(jDialog, mainLabel,tkN.getAttributeValue("label"),new Dimension(500,200+sizeadder),this,null,300) ) {
			// Sp.p("**** TkText.NACH-EXEC DIALOG--THREAD-ID="+Thread.currentThread().getId());
			if ( windowCanceled )
				throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
			if ( !did_a_cancel )
			curValN = textField.getText();

			if ( curValN == null ) 
				throw new Exception("Problem #78, TkText received null as value");			
		}
		
	}
	boolean did_a_cancel;
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			// Sp.p("**** TkText.ActionButton------THREAD-ID="+Thread.currentThread().getId());
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				// JButton pressed = (JButton)source;
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {
					wcFalse();
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					clearValue2();
					wcFalse();
					dispose();
						
				}				
			}


		}

	}
	private class ClearButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{

					clearValue2();
					wcFalse();
					dispose();


		}	
	}	
	@Override
	public String getValueN() {
		return curValN;
	}
	
	public String getValuePN() {
		return curValN;
	}	

	public void clearValue() {
		// TODO Auto-generated method stub
		curValN = no_valueN;
		
	}
	public void clearValue2() {
		// TODO Auto-generated method stub
		curValN = no_valueN;
		did_a_cancel = true;
		
	}
	public String getValue4RegexN() {
		return getValueN();
	}
	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return nono;
	}
	
	@Override
	public   synchronized void setValueN(String value) {
		curValN = value;	
	}
	
}