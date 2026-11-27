/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkBoolMac extends Tk_TkBoolBase implements Tk_IKeysOn{
		
	JButton okButton;
	JButton cancelButton;
	ButtonHandler buttonHandler;
		
	static String ok= new String("Ok");
	static String cancel= new String("No value");	
		
	boolean did_a_cancel;
	
	public void execute(boolean first_go) throws Exception {
		did_a_cancel = false;
				
		DefaultListModel<String> listModel = new DefaultListModel<String>();

		listModel.addElement(tih.getSitzung().lang().getYes());
		listModel.addElement(tih.getSitzung().lang().getNo());

		JList list = new JList( listModel );

		Vcar x = new Vcar(curValN);
		if ( x.isBool && x.getBool())
			list.setSelectedIndex(0);
		else if ( x.isBool && !x.getBool())
			list.setSelectedIndex(1);		
		
		list.setPreferredSize(new Dimension(100,100));		
		list.setFont(Tc_AppConsole.guiSizer.tkFont);
		Tk_TkSelBoolRenderer renderer = new Tk_TkSelBoolRenderer();
		
		list.setCellRenderer(renderer);
		list.setBorder(new EmptyBorder(5,5,5,5));
		
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if ( me.getClickCount() == 2 ) {
					wcFalse();
					dispose();
				}
			}
			
		};
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER && keys_on && (ke.getWhen()>keys_on_start)) {
					wcFalse();
					dispose();

				} else
					ke.consume();
			}
			
		};		
		
		list.addMouseListener(mouseAdapter);
		list.addKeyListener(keyAdapter);
		
		JButton okButton = Tk_TkStatic.getJButton(ok);

		
		
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);

		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(new ClearButtonHandler());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		if ( !nono && !no_is_0)
			buttonPanel.add(cancelButton);		
		
		
		JScrollPane scroll = new JScrollPane(list);
		String label2 = Commons.stringReplace(tkN.getAttributeValue("label"), "/n", "<BR/>");
		String label3 = "<html><div style=\"text-align: center;\">"+label2+"</div></html>";
		JPanel panel = Tk_TkStatic.getPanel(label3, scroll, buttonPanel,tipN);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				wcTrue();
			}
			
		});
		
		Tk_TkStatic.executeJDialog(jDialog, 
				mainLabelN,
				label3,new Dimension(801,300),this,null,-1);
		
		if ( windowCanceled )
			throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
		
		if ( !did_a_cancel) {
			String selval =  (String)list.getSelectedValue();
			
			if ( selval == null ) {
				// didnt select anything
				// said ok, but didnt select anything
				// we leave it like it is
			} else if ( selval.equals(tih.getSitzung().lang().getYes()) )
				curValN = "1";
			else if ( selval.equals(tih.getSitzung().lang().getNo()) )
				curValN = "0";
			else
				throw new Exception("Problem #90, TkBool received '"+selval+"' as value");			
		}


	}
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				// JButton pressed = (JButton)source;
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {

					dispose();
				} else if ( buttonText.equals(cancel) ) {

					clearValue();
					dispose();
					
						
				}				
			}

		}
	}



	public void clearValue2() {
		// TODO Auto-generated method stub
		if ( no_is_0)
			curValN = "0";
		else
			curValN = "";
		did_a_cancel = true;
		
	}	
	private class ClearButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{

					clearValue2();
					wcFalse();
					dispose();


		}	
	}


	


}