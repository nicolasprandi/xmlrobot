/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import at.nicolas.x.int_interpreter.Sp;

public class CommInstallation {
	
	CommInstallation() {
		
	}
	
	private JDialog jDialog = null;
	
	private boolean did_ok = false;
	public int askInstallationType2() {
		String[] choices = {"(0) STANDARD",
							"(1) Independent",
							"(2) SystemShare",
							"(3) Local",
							"(4) Choose Standard",
							"(5) Choose Indpendent",
							"(6) Choose SystemShare",	
							"(7) Choose Local",
							"(8) Workstation"
							};
		
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(choices[0]);
		listModel.addElement(choices[1]);
		listModel.addElement(choices[2]);
		listModel.addElement(choices[3]);
		listModel.addElement(choices[4]);
		listModel.addElement(choices[5]);
		listModel.addElement(choices[6]);
		listModel.addElement(choices[7]);
		listModel.addElement(choices[8]);

		JList list = new JList( listModel );
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new EmptyBorder(5,5,5,5));
		list.setSelectedIndex(0);
		
		JPanel buttonPanel = new JPanel();
		
		JButton okButton = new JButton("Ok");
		OkButtonHandler okButtonHandler = new OkButtonHandler();
		okButton.addActionListener(okButtonHandler);
		
		JButton cancelButton = new JButton("Cancel");
		CancelButtonHandler cancelButtonHandler = new CancelButtonHandler();
		cancelButton.addActionListener(cancelButtonHandler);	
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		JTextArea help = new JTextArea();
		help.setEditable(false);
		help.setText("Help:"
				+"\n(0) STANDARD"
					+"\n\t System=Takeoff, Museum=User"
				+"\n(1) Independent"
					+"\n\t System=Takeoff, Museum=Takeoff"
				+"\n(2) SystemShare"
					+"\n\t System=User, Museum=Takeoff"		
				+"\n(3) Local"
					+"\n\t System=User, Museum=User"					
				+"\n(4) Choose Standard"
					+"\n\t Choose location of takeoff manually"
					+"\n\t System=Takeoff, Museum=User"				
				+"\n(5) Choose Indpendent"
					+"\n\t Choose location of takeoff manually"
					+"\n\t System=Takeoff, Museum=Takeoff"				
				+"\n(6) Choose SystemShare"
					+"\n\t Choose location of takeoff manually"
					+"\n\t System=User, Museum=Takeoff"	
				+"\n(7) Choose Local"
					+"\n\t Choose location of takeoff manually"
					+"\n\t System=User, Museum=User"						
				+"\n(8) Workstation"
					+"\n\t System=User, Museum=User"
					+"\n\t Takeoff file in User"
				+"\n-----------------------"
				+"\nSystem = Services and Service Applications"
				+"\nMuseum = Temporary Data and Log Files, a lot of Input/Output-operations"
				+"\nWorkspace = Project and Applications, always near the takeoff!"
				);
		help.setBackground(Color.gray);
		help.setFont(new Font("Arial",Font.PLAIN , 12));
		help.setBorder(new EmptyBorder(5,5,5,5));
		help.setTabSize(5);
		
		JLabel header = new JLabel("If you dont know what to do, just select the STANDARD ...");
		header.setBorder(new EmptyBorder(5,5,5,5));
		header.setFont(new Font("Arial",Font.PLAIN , 12));
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(help,BorderLayout.CENTER);
		main.add(list,BorderLayout.WEST);
		
		JPanel totalPanel = new JPanel(new BorderLayout());
		totalPanel.add(main,BorderLayout.CENTER);
		totalPanel.add(buttonPanel,BorderLayout.SOUTH);
		totalPanel.add(header,BorderLayout.NORTH);
		jDialog = new JDialog();
		jDialog.setTitle("Select the type of Installation ...");
		jDialog.setContentPane(totalPanel);
		jDialog.setModal(true);
		jDialog.setLocation(400, 100);
		jDialog.pack();
		jDialog.setVisible(true);

		if ( !did_ok)
			return -1;
		else if ( list.getSelectedIndex() >= 0 ) {
			int to_return = list.getSelectedIndex();
			Sp.p("Selected was:"+to_return);
			return to_return;
		} else {
			return -1;
		}
	}	
	
	private class OkButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )
		{
			did_ok = true;
			jDialog.dispose();
		}
	}		

	private class CancelButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )
		{
			did_ok = false;
			jDialog.dispose();
		}
	}	

}