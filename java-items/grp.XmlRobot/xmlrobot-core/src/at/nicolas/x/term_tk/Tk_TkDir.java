/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
// f�r den file-chooser am lokalen filesystem

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.vfs2.FileName;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Terminator;


public class Tk_TkDir extends Tk_Tk implements Tk_IKeysOn{

	private Element tkN;
	private String idN;
	private String curValN = null;
	private boolean selectedViaVFS = false;
	ITiHandler tih;
	String mainLabelN;
	String tipN;
	boolean nono;
	JTabbedPane tabbed ;
	JPanel vfspanel;
	JPanel buttonPanel;
	JTextArea textarea;
	JButton okButton;
	JButton cancelButton;
	static String ok= new String("Ok");
	static String cancel= new String("No Value");	

	ButtonHandlerVFS buttonHandler;	
	private boolean do_cancel_input = false;

	private void cancelInput() {
		do_cancel_input = true;
	}	
	
	public void built(Element tkN,ITiHandler tih,String mainLabelN, String tipN) throws Exception {
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabelN = mainLabelN;
		this.tipN = tipN;
		this.nono = Commons.hasFlag(tkN, "nono");
		idN = tkN.getAttributeValue("id");
		curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);

		if ( !Commons.hasFlag(tkN, "no_can") && 
			 curValN!=null && 
			 curValN.length()>0 ) {
			

			curValN = Commons.constructNormalizedDir(curValN);
			curValN = Commons.dirClassic(curValN);
		}
			
		/*
		if ( !Commons.hasFlag(tkN, "no_can") && 
			 curValN!=null && 
			 curValN.length()>0 && 
			 Commons.existsDir(curValN))
			curValN = Commons.getCanoDirXrIfExists(curValN);
		*/
	}

	public String getVarType() {
		return ("dir");
	}

	public String getVarIdN() {
		return idN;
	}

	boolean first_go;	
	public void execute(boolean first_go) throws Exception {
		this.first_go = first_go;
		do_cancel_input = false;
		if ( CoreBuilder.ws.isMac) 
			executeChooserMac();
		
		else if ( CoreBuilder.ws.isLinux ) {
			/*
			int answer3 = JOptionPane.showConfirmDialog(null, "Use Dialog? - otherwise it will be Chooser");
			if ( answer3== JOptionPane.OK_OPTION) {
				executeDialog();
			} else */
			executeChooserWinLinux();
		} else
			executeChooserWinLinux();
		

			
	}
	public static String last_dirN = null;
	public void executeChooserMac() throws Exception {
		// mac
		
		try {
			// Check base and default value
			String baseDirN = null;
			
			
			// Sp.p("TkDir: curVal= "+curVal);
			
			if ( curValN == null || curValN.equals("") ) {
				// no default value
				if ( tkN.getAttributeValue("base") != null &&
						CommonsVfs.existsDir(tkN.getAttributeValue("base"),tih.getSitzung())) {
					baseDirN = tkN.getAttributeValue("base");
					// Sp.p("TkSequA");
				} else if ( last_dirN!= null && last_dirN.length()>0)
					baseDirN = last_dirN;
			} else {
				baseDirN = curValN;
			}
			baseDirN = Commons.dirClassic(baseDirN);
			
			JFrame frame = new JFrame();
			if ( CoreBuilder.ws.isMac)
				System.setProperty("apple.awt.fileDialogForDirectories", "true");
			FileDialog d = new FileDialog(frame,tkN.getAttributeValue("label"));
			
			
			if ( baseDirN != null )
				d.setDirectory(baseDirN);
			d.setVisible(true);
			if ( CoreBuilder.ws.isMac)
				System.setProperty("apple.awt.fileDialogForDirectories","false");
			
			String dirN = d.getDirectory();
			String fileN = d.getFile();
			//Sp.p("Dialog-dir ="+dir);
			
			if ( dirN == null )
				Sp.p("dir is really null");
			if ( fileN == null )
				Sp.p("file is really null");			
			if ( fileN==null && dirN==null)
				cancelAction();
			else if ( dirN != null ) {
				
				if ( CoreBuilder.ws.isMac ) {
					curValN = dirN;
					if ( !curValN.endsWith(FileName.SEPARATOR))
						curValN = curValN + FileName.SEPARATOR;
					if ( fileN != null)
						curValN = curValN + fileN;
					if ( !curValN.endsWith(FileName.SEPARATOR))
						curValN = curValN + FileName.SEPARATOR;					
				} else {
					curValN = dirN;
					if ( !curValN.endsWith(FileName.SEPARATOR))
						curValN = curValN + FileName.SEPARATOR;
				}
			} else 
				cancelAction();
			// Sp.p("Dialog-final="+curVal);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.p("+++++++++       dir-mac-linux canceled +++++++++++");
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.pe(e);
			throw e;
		}
	}
	
	public void executeChooserWinLinux() throws Exception {
		// win, linux
		try {
			// Check base and default value
			String baseDirN = null;
			String setSelectedDirN = null;
			
			
			// Sp.p("TkDir: curVal= "+curVal);
			
			if ( curValN == null || curValN.equals("") ) {
				// no default value
				if ( tkN.getAttribute("base") != null &&
						CommonsVfs.existsDir(tkN.getAttributeValue("base"),tih.getSitzung())	) {
					baseDirN = tkN.getAttributeValue("base");
					// Sp.p("TkSequA");
				} else if ( last_dirN!= null && last_dirN.length()>0)
					baseDirN = last_dirN;
			} else {
				baseDirN = curValN;
				// Sp.p("TkDir: Selected dir should be "+baseDir);
			}
			baseDirN = Commons.dirClassic(baseDirN);
			// Sp.p("TkSequ, baseDir="+baseDir);
			
			JFileChooser chooser;
			if ( baseDirN != null ) {
				chooser = new JFileChooser(baseDirN);
				if ( CoreBuilder.ws.isLinux )
					chooser.setSelectedFile(new File (baseDirN));
			}
			else 
				chooser = new JFileChooser();
			
			
			ActionListener actionListener = new ActionListener() {
				public void actionPerformed ( ActionEvent actionEvent ) {
					// JFileChooser theFileChooser = (JFileChooser)actionEvent.getSource();
					String command = actionEvent.getActionCommand();
				
					if ( command.equals(JFileChooser.APPROVE_SELECTION) || command.equals(JFileChooser.APPROVE_OPTION)) {
						wcFalse();
						dispose();
					} else if ( command.equals(JFileChooser.CANCEL_SELECTION) || command.equals(JFileChooser.CANCEL_OPTION)) {

						cancelInput(); // sollte das nicht der selbe wie vorher sein?
						wcFalse();
						dispose();
					} else {
						int gogogo = Tk_Token.go(1);
						JOptionPane.showMessageDialog(null, "unknown event in Tk_TkDir,command: "+command);
						int gogogo2 = Tk_Token.leave(3);
					}
				}					
			};		
			
			
			
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			chooser.addActionListener(actionListener);
			
			//
			

			textarea = new JTextArea(curValN,5,50);
			JScrollPane scroll = new JScrollPane(textarea);
			textarea.setBackground(Color.LIGHT_GRAY);
			textarea.setFont(Tc_AppConsole.guiSizer.tkFont);
			textarea.setLineWrap(true);
			
			//int sizeadder = 0;
			


			
			okButton = Tk_TkStatic.getJButton(ok);
			cancelButton = Tk_TkStatic.getJButton(cancel);		

			buttonPanel = new JPanel();
			buttonPanel.add(okButton);
			if ( !nono)
				buttonPanel.add(cancelButton);	
			
			buttonHandler = new ButtonHandlerVFS();
			okButton.addActionListener(buttonHandler);
			cancelButton.addActionListener(new ClearButtonHandler());			

			JLabel advice = new JLabel ("<html><br>Use ctrl+enter to perform an 'ok' by key");
			advice.setHorizontalAlignment(SwingConstants.CENTER);	
			JPanel p2 = new JPanel(new BorderLayout());
			p2.add(buttonPanel,BorderLayout.CENTER);
			p2.add(advice,BorderLayout.SOUTH);	
			
			vfspanel = new JPanel(new BorderLayout());
			vfspanel.add(scroll, BorderLayout.CENTER);
			vfspanel.add(p2, BorderLayout.SOUTH);
		
			
			
			tabbed = new JTabbedPane();
			tabbed.add(chooser,"Local");
			tabbed.add(vfspanel,"Virtual");
			
			JButton focus = null;
			
			if ( curValN.length() > 0 ) {
				//Sp.p("xxx is of '"+curValN+"'= "+Commons.xr2isLocalDirFile(curValN));
				if ( ! Commons.xr2isLocalDirFile(curValN)) {
					tabbed.setSelectedComponent(vfspanel);
					focus = okButton;
				}
			}
			
			
			
			
			
			KeyAdapter keyAdapter = new KeyAdapter() {
				public void keyReleased(KeyEvent ke) {
					//Sp.p("**** TkText.KeyEvent----------THREAD-ID="+Thread.currentThread().getId());
					ke.consume();
					//Sp.p("BBB keys_on="+keys_on+"  rest "+(ke.getWhen()>keys_on_start));
					if ( ke.getKeyCode() == KeyEvent.VK_ENTER 
							&& keys_on && (ke.getWhen()>keys_on_start) ) {
						//Sp.p("ENTER");
						if ( tabbed.getSelectedComponent()== vfspanel ) {
							Sp.p("Key VFS");
							wcFalseByVfs();
						} else {
							Sp.p("Key STANDARD");
							wcFalse();
						}
						dispose();
					} else {
						//Sp.p("AAA");
						//Sp.p("ke.getWhen():"+ke.getWhen());
						//Sp.p("keys_on_start:"+keys_on_start);
					}
				}
				
			};	

			// textField.selectAll();			
			chooser.addKeyListener(keyAdapter);
			KeyAdapter keyAdapter2 = new KeyAdapter() {
				public void keyReleased(KeyEvent ke) {
					int code = ke.getKeyCode();  
					int modifiers = ke.getModifiers();  
					if(code == KeyEvent.VK_ENTER && 
							modifiers == KeyEvent.CTRL_MASK && 
							keys_on&& (ke.getWhen()>keys_on_start)) {
						wcFalseByVfs();
						dispose();
					}
				}			
			};	
			textarea.addKeyListener(keyAdapter2);			
			String mytip;
			if ( tipN==null)
				mytip="Press 'Cancel' to get the preselection of XmlRobot-Folders like eg. project-directories.";
			else
				mytip= tipN+"\n\n"+"Press 'Cancel' to get the preselection of XmlRobot-Folders like eg. project-directories.";
			JPanel panel = Tk_TkStatic.getPanel(
					tkN.getAttributeValue("label"), 
					tabbed, 
					null,
					false,mytip);
			
			jDialog = new JDialog();
			jDialog.setContentPane(panel);
			
			jDialog.addWindowListener(new WindowAdapter() {
				public void windowClosing ( WindowEvent e) {
					// windowCanceled =true;		
					wcTrue();
				}
				
			});
			performedVfs = false;
			if ( Tk_TkStatic.executeJDialog(
					jDialog, 
					mainLabelN,
					Tk_TkStatic.getLabelOrId(tkN),
					new Dimension(900,700),this,focus,-1)) {
				if ( windowCanceled )
					throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
				if ( do_cancel_input ) {
					cancelAction();

				} else {				
					if ( tabbed.getSelectedComponent()== vfspanel ){
						curValN = textarea.getText();
						selectedViaVFS = true;
						Sp.p("--- VFS: ["+curValN+"]");
					} else {
						Sp.p("--- Local");
						selectedViaVFS = false;
						curValN = chooser.getSelectedFile().getCanonicalPath();
						if ( CoreBuilder.ws.isWin )
							curValN = Commons.convertSlashes(curValN);
						if ( !curValN.endsWith(FileName.SEPARATOR) && curValN.length()>0)
							curValN = curValN+FileName.SEPARATOR;						
					}

				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.p("+++++++++       dir canceled +++++++++++++++++++++");
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.pe(e);
			throw e;
		}
	}	
	
	private void cancelAction () throws Exception {
		
		boolean clear_is_possible = true;
		if ( hasNono()) {
			clear_is_possible = false;
		} else if ( this.first_go) { 
			clear_is_possible = true;
		} /* else if (curVal != null && curVal.equals("")) {
			clear_is_possible = false;
		} */
		
		if ( clear_is_possible) {
			Question2Values q = new Question2Values();
			int ret = q.question(
					"You cancelled the input of '"+Tk_TkStatic.getLabelOrId(tkN)+"'",
					"What do you want to do?\n(1) No Value/Empty\n(2) Make Preselection of XmlRobot-Folders", 
					"(1) No Value", 
					"(2) Preselection",
					true, 
					1);
			if ( ret == 1 ) {
				// Clear
				curValN="";
			} else if ( ret== 2 ) {
				String result_preselection = Tk_TkFile.makePreselectionDir(tih,curValN);
				if ( result_preselection != null )
					curValN=result_preselection;
			}
		} else {
			Question1Values q = new Question1Values();
			int ret = q.question(
					"You cancelled the input of '"+Tk_TkStatic.getLabelOrId(tkN)+"'",
					"What do you want to do?\n(1) Make Preselection of XmlRobot-Folders", 
					"(1) Preselection",
					true, 
					1);
			if ( ret == 1 ) {
				String result_preselection = Tk_TkFile.makePreselectionDir(tih,curValN);
				if ( result_preselection != null )
					curValN=result_preselection;
			}			
		}
		
	
	}
	private class ButtonHandlerVFS implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			// Sp.p("**** TkText.ActionButton------THREAD-ID="+Thread.currentThread().getId());
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				// JButton pressed = (JButton)source;
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {
					wcFalseByVfs();
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					clearValue();
					wcFalseByVfs();
					dispose();						
				}				
			}
		}
	}	
	
	private class ClearButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )
		{
					clearValue();
					wcFalse();
					dispose();
		}	
	}		
	@Override
	public String getValueN() {
		if ( curValN!=null && curValN.length()>0)
			last_dirN = curValN;
		return curValN;
	}

	public String getValue4RegexN() {
		String a = getValueN();
		if ( a==null || a.length()==0)
			return "";
		
		String x=null; 
		try {
			try {
				x=Commons.getNameOfDir(a);
				if ( x.length() == 0 )
					return a;
			} catch (Exception e) {
				return a;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Tc_Terminator.valueProblem("Tk_TkDir.getValue4RegexN()", e);

		}
		
		return x;
	}		
	public String getValuePN() {
		return curValN;
	}	
	
	public void clearValue() {
		// TODO Auto-generated method stub
		curValN = "";
		
	}

	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return nono;
	}	
	
	@Override
	public  synchronized void setValueN(String value) {
		curValN = value;	
	}

	
}