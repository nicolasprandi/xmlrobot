/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
// f�r den filechooser am lokalen filesystem

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.vfs2.FileName;
import org.jdom2.Element;

import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkFile  extends Tk_Tk implements Tk_IKeysOn{

	private Element tkN;
	private String idN;
	private String curValN = null;
	ITiHandler tih;
	String mainLabelN;
	String tipN;
	boolean nono;

	boolean force_extension = false;
	String my_extensionN = null;
    boolean my_flag_save = false;
    boolean flag_no_preselection = false;
    String flagN = null;
	
	public void built(Element tkN,ITiHandler tih,String mainLabelN, String tipN) throws Exception {
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabelN = mainLabelN;

		this.tipN = tipN;
		this.nono = Commons.hasFlag(tkN, "nono");
		this.flag_no_preselection = Commons.hasFlag(tkN, "no_preselection");
		idN = tkN.getAttributeValue("id");
		curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);
		if ( !Commons.hasFlag(tkN, "no_can") && curValN!=null && curValN.length()>0 )
			curValN = Commons.constructNormalizedFile(curValN);
		force_extension = Commons.hasFlag(tkN, "force_extension");
		my_extensionN = tkN.getAttributeValue("extension");
		my_flag_save = Commons.hasFlag(tkN, "save");
		
		if ( tkN.getAttribute("flag") != null )
			flagN  = tkN.getAttributeValue("flag");

	}

	public String getVarType() {
		return ("file");
	}

	public String getVarIdN() {
		return idN;
	}
	private boolean do_cancel_value = false;
	private void cancelInput() {
		do_cancel_value = true;
	}
	boolean first_go;
	public void execute(boolean first_go) throws Exception {
		do_cancel_value = false;
		this.first_go = first_go;
		if ( CoreBuilder.ws.isMac || CoreBuilder.ws.isLinux) 
			executeDialogMacLinux();
		else
			executeDialogWin();	
		
	}
	public void executeDialogWin() throws Exception {
		// win
		
		// Check base and default value
		String baseDirN = null;
		String setSelectedFileN = null;
		
		//Sp.p("TkSequ Default="+curVal);
		if ( curValN == null || curValN.equals("") ) {
			// no default value
			if ( tkN.getAttributeValue("base") != null &&
					CommonsVfs.existsDir(tkN.getAttributeValue("base"),tih.getSitzung()) ) {
				baseDirN = tkN.getAttributeValue("base");
				// Sp.p("TkSequA");
			} else if ( Tk_TkDir.last_dirN!= null && Tk_TkDir.last_dirN.length()>0)
				baseDirN = Tk_TkDir.last_dirN;
		} else {
			baseDirN = Commons.getDirOfFile(curValN);
			setSelectedFileN = curValN;
			// Sp.p("TkSequB");
		}
		baseDirN = Commons.dirClassic(baseDirN);
		setSelectedFileN = Commons.fileClassic(setSelectedFileN);
		Sp.p("TkSequ, win, baseDir="+baseDirN);
		Sp.p("TkSequ, win, setSelectedFileN="+setSelectedFileN);
		
		JFileChooser chooser;
		if ( baseDirN != null )
			chooser = new JFileChooser(baseDirN);
		else 
			chooser = new JFileChooser();
		// Sp.p("** setSelectedFile="+setSelectedFile);
		if ( setSelectedFileN != null )
			chooser.setSelectedFile(new File(setSelectedFileN));
		
		// chooser = new JFileChooser("C:\\Users\\nicolas\\Documents\\rhino1.3dm");
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed ( ActionEvent actionEvent ) {
				// JFileChooser theFileChooser = (JFileChooser)actionEvent.getSource();
				String command = actionEvent.getActionCommand();
				// Sp.p("Action performed");
				if ( command.equals(JFileChooser.APPROVE_SELECTION)) {
					wcFalse();
					// Sp.p("approve");
					dispose();
				} else if ( command.equals(JFileChooser.CANCEL_SELECTION)) {
					cancelInput();
					wcFalse();
					dispose();
					
				}
			}					
		};	
		
		
		if ( Commons.hasFlag(tkN, "save"))
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			
		if ( tkN.getAttribute("extension")!=null) {
			Tk_TkFilenameFilterWin tkFilter = new Tk_TkFilenameFilterWin(tkN.getAttributeValue("extension"));
			chooser.setFileFilter(tkFilter);			
		}

		chooser.addActionListener(actionListener);
		// dummy key adapter
		KeyAdapter keyAdapter = new KeyAdapter() {public void keyReleased(KeyEvent ke) {} };
		chooser.addKeyListener(keyAdapter);
		
		String mytip;
		if ( tipN==null)
			mytip="Press 'Cancel' to get the preselection of XmlRobot-Folders like eg. project-directories.";
		else
			mytip= tipN+"\n\n"+"Press 'Cancel' to get the preselection of XmlRobot-Folders like eg. project-directories.";

		
		JPanel panel = Tk_TkStatic.getPanel(tkN.getAttributeValue("label"), chooser, null,false,mytip);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				// windowCanceled =true;		
				wcTrue();
			}
			
		});
		
		if ( Tk_TkStatic.executeJDialog(jDialog, mainLabelN,Tk_TkStatic.getLabelOrId(tkN),new Dimension(900,700),this,null,-1)) {
			if ( windowCanceled )
				throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
			if ( do_cancel_value)
				cancelAction();
			else {
				curValN = chooser.getSelectedFile().getCanonicalPath();
			
				if ( Commons.hasFlag(tkN, "save") && !Commons.hasFlag(tkN, "no_warning")) {
					if ( CommonsVfs.existsFile(curValN,tih.getSitzung())) {
						int gogogo = Tk_Token.go(1);
						JOptionPane.showMessageDialog(Tc_AppConsole.getJFrame(), 
								"File exists already!");
						int gogogo2 = Tk_Token.leave(2);
					}
				}			
				String tx = tkN.getAttributeValue("extension");
				if ( 	force_extension 
						&& tx!=null 
						&& tx.indexOf(" ")==-1 ) {
					if ( !curValN.toUpperCase().endsWith(tx.toUpperCase()))
						curValN = curValN.concat(tx);				
				}
			}
		}
		
	}
	public void executeDialogMacLinux() throws Exception {
		// mac, linux
		try {		
		// Check base and default value
		String baseDirN = null;
		String setSelectedFileN = null;
		
		//Sp.p("TkSequ Default="+curVal);
		if ( curValN == null || curValN.equals("") ) {
			// no default value
			if ( tkN.getAttributeValue("base") != null &&
					CommonsVfs.existsDir(tkN.getAttributeValue("base"),tih.getSitzung())) {
				baseDirN = tkN.getAttributeValue("base");
			} else if ( Tk_TkDir.last_dirN!= null && Tk_TkDir.last_dirN.length()>0)
				baseDirN = Tk_TkDir.last_dirN;
		} else {
			String can = Commons.constructNormalizedFile(curValN);
			baseDirN = Commons.getDirOfFile(can);
			setSelectedFileN = Commons.getNameOfFile(can);
		}

		baseDirN = Commons.dirClassic(baseDirN);
		Sp.p("TkSequ, mac, baseDir="+baseDirN);
		Sp.p("TkSequ, mac, setSelectedFileN="+setSelectedFileN);
		
		JFrame frame = new JFrame();
		int initial = 0;
		if ( Commons.hasFlag(tkN, "save"))
			initial = FileDialog.SAVE;
		else
			initial = FileDialog.LOAD;
		String titN = tkN.getAttributeValue("label");
		if ( tkN.getAttribute("extension")!=null)
			titN = titN+" [extension="+tkN.getAttributeValue("extension")+"]";
		
		FileDialog dialog = new FileDialog(frame,titN,initial);

		

		// Sp.p("** setSelectedFile="+setSelectedFile);
		if ( setSelectedFileN != null )
			dialog.setFile(setSelectedFileN);
		if ( baseDirN != null )
			dialog.setDirectory(baseDirN);		
		
		if ( tkN.getAttribute("extension")!=null) {
			Tk_TkFilenameFilterMacLinux tkFilter = 
					new Tk_TkFilenameFilterMacLinux(tkN.getAttributeValue("extension"));
			dialog.setFilenameFilter(tkFilter);			
		}

		dialog.setVisible(true);
		
		String dirN = dialog.getDirectory();
		String fileN = dialog.getFile();		
		
		if ( fileN==null || dirN==null)
			cancelAction();
		else {
			curValN = dirN;
			if ( !curValN.endsWith(FileName.SEPARATOR))
				curValN = curValN + FileName.SEPARATOR;
			curValN = curValN + fileN;

			String tx = tkN.getAttributeValue("extension");
			if ( 	force_extension 
					&& tx!=null 
					&& tx.indexOf(" ")==-1 ) {
				if ( !curValN.toUpperCase().endsWith(tx.toUpperCase()))
					curValN = curValN.concat(tx);				
			}			
		}
		/* not in linux or mac
		if ( CoreBuilder.ws.isLinux && Commons.hasFlag(tkN, "save") && 
				!Commons.hasFlag(tkN, "no_warning")) {
			if ( Commons.existsFile(curValN)) {
				int gogogo = Tk_Token.go(1);
				JOptionPane.showMessageDialog(Tc_AppConsole.getJFrame(), 
						"File exists already!");
				int gogogo2 = Tk_Token.leave(7);
			}
		}	
		*/			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.p("+++++++++       file-mac canceled +++++++++++++++++");
			Sp.p("++++++++++++++++++++++++++++++++++++++++++++++++++");
			Sp.pe(e);
			throw e;
		}		
	}	
	public static String makePreselectionFile(ITiHandler tih, String jil_with_file, String extension, boolean flag_save) {
		String ret = null;
		
		// preselecitn aufrufen
		AppSystemcall appSystemcall = null;
		
		try {
			Element prolog = new Element("prolog");
			if ( jil_with_file != null ) {
				Element e_jil_with_dir = new Element("file");
				e_jil_with_dir.setAttribute("id", "with-jil");
				e_jil_with_dir.setAttribute("x", Entities.importEntities(jil_with_file));
				prolog.addContent(e_jil_with_dir);

			}
			
			if ( extension != null ) {
				Element e_jil_with_ext = new Element("prop");
				e_jil_with_ext.setAttribute("id", "with-extension-jil");
				e_jil_with_ext.setAttribute("x", Entities.importEntities(extension));
				prolog.addContent(e_jil_with_ext);					
			}
			
			

			Element e_jil_with_flag = new Element("prop");
			e_jil_with_flag.setAttribute("id", "with-flag-jil");
			if ( flag_save == true )
				e_jil_with_flag.setAttribute("x", "save");
			else
				e_jil_with_flag.setAttribute("x", "");
			prolog.addContent(e_jil_with_flag);					
						

			appSystemcall = new AppSystemcall ( 
									Wellknown.jil_file,
									tih.getAppId(),
									tih.getProId(),
									prolog,
									Interpreter.SILENCE_VERY,
						            tih.getTerminator() ,// ti something
						            0,
						            null);
			appSystemcall.run();
			ret = Entities.exportEntities(tih.getGlobalAndRemove("jil-result"));

		} catch (Exception e1) {
			LogConnector lce = new LogConnector("call_service: Wellknown.jil-file");
			lce.printExceptionStack(e1);
		}
		
		return ret;
	}
	public static String makePreselectionDir(ITiHandler tih, String jil_with_dir) {
		String ret = null;
		
		// preselecitn aufrufen
		AppSystemcall appSystemcall = null;
		
		try {
			Element prolog = null;
			if ( jil_with_dir != null ) {
				prolog= new Element("prolog");
				Element e_jil_with_dir = new Element("dir");
				e_jil_with_dir.setAttribute("id", "with-jil");
				e_jil_with_dir.setAttribute("x", Entities.importEntities(jil_with_dir));
				prolog.addContent(e_jil_with_dir);
			}

			appSystemcall = new AppSystemcall ( 
									Wellknown.jil_dir,
									tih.getAppId(),
									tih.getProId(),
									prolog,
									Interpreter.SILENCE_VERY,
						            tih.getTerminator() ,// ti something
						            0,
						            null);
			appSystemcall.run();
			ret = Entities.exportEntities(tih.getGlobalAndRemove("jil-result"));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			LogConnector lce = new LogConnector("call_service: Wellknown.jil-dir");
			lce.printExceptionStack(e1);

		}
		
		return ret;
	}
	
	private void cancelAction () throws Exception {
		/*
		if ( hasNono()) {
			// keeping the value if it has to have an input
			return;
		} else if (curVal != null && curVal.equals("")) {
			return;
		} else {
		*/
		boolean clear_is_possible = true;
		if ( hasNono()) {
			clear_is_possible = false;
		} else if ( this.first_go) { 
			clear_is_possible = true;
		}/* else if (curVal != null && curVal.equals("")) {
			clear_is_possible = false;
		}*/
		
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
				// make preselection
				if ( flag_no_preselection )
					JOptionPane.showMessageDialog(null, 
							"Preselection not possible");
				else {
					String result_preselection = makePreselectionFile(tih,
							curValN,my_extensionN,my_flag_save);
					if ( result_preselection != null )
						curValN=result_preselection;
				}
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
				// make preselection
				if ( flag_no_preselection )
					JOptionPane.showMessageDialog(null, 
							"Preselection not possible");
				else {
				String result_preselection = makePreselectionFile(tih,curValN,
						my_extensionN,my_flag_save);
				if ( result_preselection != null )
					curValN=result_preselection;
				}
			}			
		}
		
	
	}	
	@Override
	public String getValueN() {
		if ( curValN!=null && curValN.length()>0) {
			Tk_TkDir.last_dirN = null;
			try {
				Tk_TkDir.last_dirN = Commons.getDirOfFile(curValN);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}
		}
		return curValN;
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
	public String getValue4RegexN() {
		String x;
		try {
			Sp.p("--- getNameOfFile: "+getValueN());
			x = Commons.getNameOfFile(getValueN());
		} catch (Exception e) {
			Sp.p("--- PROBLEM getNameOfFile: "+getValueN()+", "
					+ " id="+idN +" label=? msg="
		+e.getMessage());
			Sp.pe(e);
			x="";
		}

		return x;
	}	
}