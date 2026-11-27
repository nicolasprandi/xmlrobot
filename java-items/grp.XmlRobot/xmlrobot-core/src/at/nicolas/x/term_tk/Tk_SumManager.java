/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.term_core.Tc_AppConsole;


public class Tk_SumManager implements Tk_IKeysOn{
	String ok= new String("Ok");
	static String cancel= new String("Cancel");	
	static String editall= new String("Edit All");	
	String refresh= new String("Refresh");
	JButton okButtonUsed;
	JButton cancelButton;
	ButtonHandler buttonHandler;
	JDialog jDialog;
	boolean windowCanceled = false;
	boolean doEditAll = false;
	
	private ArrayList<Tk_SumMember> memberList;
	private ITerminator terminator;
	private String labelN;
	private boolean flag_ti;
	ITiHandler tih;
	// private static Dimension minDim = new Dimension(800,500);
	private static Dimension initDim = new Dimension(800,500);
	private static Dimension initDimTablet = new Dimension(800,500);
	Tk_SumManager(ITiHandler tih, ITerminator terminator, String labelN, boolean flag_ti) {
		this.tih = tih;
		this.terminator = terminator;
		this.labelN = labelN;
		
		this.flag_ti = flag_ti;
		memberList = new ArrayList<Tk_SumMember>();
	}
	public void add( Tk_SumMember sumMember ) {
		memberList.add(sumMember);
	}
	public void callbackEnter() {
		jDialog.dispose();
	}
	
	public void setFocusOk() {
		if ( okButtonUsed!=null ) {
			okButtonUsed.requestFocus();
		}
	}

	public ITerminator getTerminator() { return terminator; }
	public void callbackInputDone(Tk_SumMember sumMember, String newValueN ) {
		// Sp.p("IN CALLBACK--------------");
		// Dimension before = jDialog.getSize();
		Dimension before = jDialog.getSize();
		jDialog.setMinimumSize(new Dimension(jDialog.getSize().width,0));
		//jDialog.pack();
		
		
		int vollbreite = Tc_AppConsole.lastInstance.getJFrame().getWidth();
		// das y ist klar: 0, aber das x ist unklar 
				
		Dimension d = jDialog.getSize();
		int diff = d.width - before.width;

		if ( diff > 50  ) {
			// do th pack
			int gebrauchte_breite = jDialog.getWidth();
			int position = (vollbreite - gebrauchte_breite) / 2;
			
			jDialog.setLocation(position, jDialog.getLocation().y);	
			// jDialog.setMinimumSize(new Dimension(before.width,0));
			// Sp.p("--- new");
		} else {
			// restore old size
			// jDialog.setSize(before);
			jDialog.setMinimumSize(new Dimension(before.width,0));
			// Sp.p("--- old");
		}
		
		limitJDialog(jDialog);
		/*
		if ( flag_ti) {
			Element o1 = new Element("ti");
			o1.setText(Entities.importEntities(newValueN));
			o1.setAttribute("label", sumMember.label+"+ :");
			
			if (  sumMember.tk.getVarType().equals("dir"))
				o1.setAttribute("type", "dir");
			else if ( sumMember.tk.getVarType().equals("file"))
				o1.setAttribute("type", "file");
			try {
				terminator.executeTi(o1, null, "","", "","",tih);
				terminator.flush(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			
			}
			
		}
		*/	
		/*
		int cur_width = jDialog.getWidth();
		
		jDialog.pack();
		if ( jDialog.getWidth() < cur_width)
			jDialog.setMinimumSize(new Dimension(cur_width,0));
			*/
		jDialog.setMinimumSize(new Dimension(300,200));
		

	}
	
	public synchronized void wcTrue() {
		windowCanceled =true;		
	}
	
	public synchronized void wcFalse() {
		windowCanceled =false;		
	}	

	public synchronized void KeysOnFalse() {
		keys_on = false;	
	}	
	
	public synchronized void doEditAllTrue() {
		doEditAll =true;
	}
	public synchronized void dispose() {
		jDialog.dispose();	
	}	
	
	private void refresh() {
		jDialog.pack();
		// Sp.p("Station A");
		// do location central
	}
	
	
	
	public boolean finalisiere(boolean performValidation,String from) throws Exception {
		Iterator<Tk_SumMember> it = memberList.iterator();

		while ( it.hasNext()) {
			Tk_SumMember aktMember = (Tk_SumMember)it.next();
			boolean stay_in=aktMember.finalisieren(performValidation,from);
			if ( stay_in ) {
				Sp.p("*** Stay in because: "+aktMember.label);
				return true;
			}
		}	
		return false;
		// Sp.p("Am ende von finalize");
	}
	private void limitJDialog( JDialog jdialog ) {
		Dimension sd = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension md = new Dimension ( sd.width-100, sd.height-100);
		jDialog.setMaximumSize(md);
	}
	public boolean performDialog() throws Exception {
		
		int numberOfMembers = memberList.size();
		JPanel dataPanel = new JPanel( new GridLayout(memberList.size(),1));
		Iterator<Tk_SumMember> it = memberList.iterator();
		int numberOfChars = 0;
		while ( it.hasNext()) {
			Tk_SumMember aktMember = (Tk_SumMember)it.next();
			dataPanel.add(aktMember.getJComponent());
			int numberOfThisChars = (aktMember.tk.getValuePN()).length();
			if (numberOfThisChars > numberOfChars)
				numberOfChars = numberOfThisChars;
		}
		// System.out.println("numberOfChars="+numberOfChars);
		JButton okButton = Tk_TkStatic.getJButton(ok);
		


		
		okButtonUsed = okButton;
		okButton.setToolTipText("Confirm this data and continue the job.");
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);
		cancelButton.setToolTipText("Cancel the input and stop the job.");
		JButton editallButton = Tk_TkStatic.getJButton(editall);
		editallButton.setToolTipText("Edit all fields and see this summary once again.");
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(buttonHandler);
		editallButton.addActionListener(buttonHandler);
		
		JButton refreshButton = Tk_TkStatic.getJButton(refresh);
		refreshButton.setToolTipText("Do refresh this screen-layout!");
		refreshButton.addActionListener(buttonHandler);	
		
		JPanel chefPanel = new JPanel ( new BorderLayout ());
		
		JPanel rechtsPanel = new JPanel ( new GridLayout(2,1) );
		rechtsPanel.add(refreshButton);
		rechtsPanel.add(cancelButton);
		
		JPanel mittePanel = null;
		
		int numberOfCharsLimited = numberOfChars;
		int theLimitOfChars = 160;
		if (numberOfCharsLimited >  theLimitOfChars)numberOfCharsLimited=theLimitOfChars;
		
		int padding_left_right = 20 +  numberOfCharsLimited + numberOfCharsLimited/2;
		
		if ( numberOfMembers > 3 ) {
			int numberOfMembersLimited = numberOfMembers;
			int theLimit = 20;
			if (numberOfMembersLimited >  theLimit)numberOfMembersLimited=theLimit;
			

			
			mittePanel= new JPanel ( new GridLayout(1,1) );
			Border border3 =
				// BorderFactory.createMatteBorder(0, 20, 9+theLimit-numberOfMembersLimited, 20, okButton.getBackground());
				BorderFactory.createMatteBorder(0, padding_left_right, 9+theLimit-numberOfMembersLimited, padding_left_right, okButton.getBackground());
			
			JPanel zwischenPanel = new JPanel(new BorderLayout ());
						
			zwischenPanel.add(okButton, BorderLayout.CENTER);
			
			zwischenPanel.setBorder(border3);
			
			mittePanel.add(zwischenPanel);		
			// mittePanel.setPreferredSize(new Dimension(30,30));
		} else {
			mittePanel= new JPanel ( new GridLayout(2,1) );
			Border border3 =
					BorderFactory.createMatteBorder(0, padding_left_right, 0, padding_left_right, okButton.getBackground());
			
			JPanel zwischenPanel = new JPanel(new BorderLayout ());
			
			zwischenPanel.add(okButton, BorderLayout.CENTER);
			
			zwischenPanel.setBorder(border3);
			
			mittePanel.add(zwischenPanel);		
			mittePanel.add(new JLabel(" "));
		}
		
		
		int sizeadder = Tk_TkStatic.getSizeAdder();
	
		
		JPanel linksPanel = new JPanel ( new GridLayout(2,1) );
		
		linksPanel.add(editallButton);
		linksPanel.add(new JLabel(" "));			
		
		chefPanel.add(linksPanel,BorderLayout.WEST);
		chefPanel.add(mittePanel,BorderLayout.CENTER);
		chefPanel.add(rechtsPanel,BorderLayout.EAST);	
		chefPanel.add(new JLabel(" "),BorderLayout.NORTH);
		
		
		JLabel errorLabel = new JLabel("Do not press ENTER!");
		
		JPanel panel = Tk_TkStatic.getPanel(labelN, dataPanel, chefPanel,null);
		
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		// get Screen dimesion
		limitJDialog(jDialog);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				// windowCanceled =true;	
				wcTrue();
			}
			
		});
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			
			public void keyReleased(KeyEvent ke) {
				ke.consume();
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER && keys_on && (ke.getWhen()>keys_on_start)) {
					// Sp.p("mouse double clicked");
					//windowCanceled=false;
					//jDialog.dispose();
				}
			}
			public void keyPressed(KeyEvent ke) {
				ke.consume();
			}	
			public void keyTyped(KeyEvent ke) {
				ke.consume();
			}				
			
		};	

		jDialog.addKeyListener(keyAdapter);
		
	// buttonPanel 
		// Sp.p("hase 1");
		Tk_TkStatic.executeJDialog(jDialog, "Review: "+labelN,"Review2",initDim,this,okButton,-1); // okButton
		// Sp.p("hase 2222 - save");
		if ( windowCanceled ) {
			// Sp.p("hase 1");
			throw new TkStopException("Stopped Review '"+labelN+"'");
		}

		return doEditAll;

	}
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					wcTrue();
					dispose();
						
				} else if ( buttonText.equals(editall) ) {
					doEditAllTrue();
					dispose();
						
				} else if ( buttonText.equals(refresh) ) {
					refresh();
						
				}						
			}

		}
	}
	
	boolean keys_on = false;
	long    keys_on_start = 0;
	public void keysOn()  {
		keys_on = true;		
		keys_on_start = System.currentTimeMillis()+40;
	}		
}