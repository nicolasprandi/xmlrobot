/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
// zum speichern des accept-textes auf dem lokalen file-systems, SAVE
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkAccept extends Tk_Tk implements Tk_IKeysOn{

	private Element tkN;
	private String idN;
	private String curValN = null;
	ITiHandler tih;
	String mainLabelN;
	String tipN;
	
	JButton okButton;
	JButton cancelButton;
	JButton saveButton;

	ButtonHandler buttonHandler;
	
	String ok= new String("Accept");
	String cancel= new String("Decline");
	String save_textN = null;
	Question questionsN[];
	
	boolean confirmed = false;
	String labelN;
	String fileN;
	JEditorPane jEditorPane;
	
	
	public void built(Element tkN,ITiHandler tih,String mainLabel, String tip) throws Exception {
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabelN = mainLabel;
		this.tipN = tip;
		idN = tkN.getAttributeValue("id");
		curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);
		

	}

	public String getVarType() {
		return ("prop");
	}

	public String getVarIdN() {
		return idN;
	}

	public String getDefaultValueN() throws Exception {
		// TODO Auto-generated method stub
		return "0";
	}
	@Override
	public void execute(boolean first_go) throws Exception {
		
		buttonHandler = new ButtonHandler();
		
		JPanel main = new JPanel( new BorderLayout());

		// jLabel		
		labelN = tkN.getAttributeValue("label");
		if ( labelN == null )
			labelN = "Do you accept this request?";
		// JLabel jLabel = new JLabel ( label );
		// main.add(jLabel);

		if ( tkN.getAttribute("label_accept") != null )
			ok= tkN.getAttributeValue("label_accept");

		if ( tkN.getAttribute("label_decline") != null )
			cancel= tkN.getAttributeValue("label_decline");
		
		// jScrollPane
		fileN = tkN.getAttributeValue("file");
		String filetype = tkN.getAttributeValue("file_type");
		if ( filetype == null )
			filetype = "text";
				
		jEditorPane = new JEditorPane();
				
		if ( tkN.getAttribute("width")!=null && tkN.getAttribute("height")!=null) {
			String x_string = tkN.getAttributeValue("width");		
			String y_string = tkN.getAttributeValue("height");	
			jEditorPane.setPreferredSize(new Dimension(Commons.string2int(x_string),Commons.string2int(y_string)));
		}
		jEditorPane.setBorder(new EmptyBorder(10,10,10,10));
		jEditorPane.setFont(Tc_AppConsole.guiSizer.tkFont);
		
		jEditorPane.setEditable(false);	
		if ( tkN.getAttribute("x")!=null) {
			jEditorPane.setText(tkN.getAttributeValue("x"));
			//System.out.println("AA x="+tkN.getAttributeValue("x"));
		} else if ( filetype.equals("html") && fileN != null) {
			jEditorPane.setContentType("text/html");
			jEditorPane.setText(CommonsVfs.readTextfile(fileN,tih.getSitzung()));
		} else if ( fileN != null){
			jEditorPane.setText(String.format(CommonsVfs.readTextfile(fileN,tih.getSitzung())));
		} else {
			throw new Exception("tk-accept needs attribute 'x' or 'file'");
		}
			
		
		JScrollPane jScrollPane = new JScrollPane(jEditorPane);
		jEditorPane.setCaretPosition(0);
		
		
		// saveButton
		if ( Commons.hasFlag(tkN, "save")) {
			JPanel x = new JPanel( new BorderLayout());
			save_textN = "Save this text on file";
			saveButton = Tk_TkStatic.getJButton(save_textN);
			x.add(jScrollPane,BorderLayout.CENTER);
			x.add(saveButton,BorderLayout.SOUTH);
			main.add(x,BorderLayout.CENTER);
			saveButton.addActionListener(buttonHandler);
		} else {
			saveButton = null;
			main.add(jScrollPane,BorderLayout.CENTER);
		}
			
		
		// Questions
		CheckBoxHandler checkBoxHandler = new CheckBoxHandler();
		int count_q = tkN.getChildren("q").size();
		//Sp.p("count_q="+count_q);
		if ( count_q  > 0 ) {
			questionsN = new Question[count_q];
			Sp.p("count_q-b="+count_q);
			JPanel qPanel = new JPanel ( new GridLayout(count_q,1  ));
			
			List<Element> lq = tkN.getChildren("q");
			Iterator<Element> iq = lq.iterator();
			int i = 0;
			while ( iq.hasNext()) {
				Element ceN = (Element)iq.next();
				Question q = new Question(ceN.getAttributeValue("label"),ceN.getAttributeValue("id"));
				if (Commons.hasFlag(ceN, "load")) {
					Sp.p(ceN.getAttributeValue("id")+" is with load");
					int isOn = 0;
					try {
						String getVar = tih.getVar("prop", ceN.getAttributeValue("id"));
				
						try {
							isOn = Commons.string2int(getVar);
						} catch (Exception e1) {
							isOn = 0;
						}
						Sp.p(ceN.getAttributeValue("id")+"="+getVar);
						Sp.p("isOn="+isOn);
					} catch (Exception e1) {
						isOn = 0;
					}
					if ( isOn==1 ) {
						// q.jCheckBox.setSelected(true);
					    q.answer=true;
						q.jCheckBox.setSelected(true);
					}
					
				}
				
				q.jCheckBox.addItemListener(checkBoxHandler);
				qPanel.add(q.jCheckBox);
				// Sp.p("i="+i);
				questionsN[i]= q;
				
				i++;
			}
			
			JLabel x = null;
			if ( i > 1 )
				x = new JLabel("Click the statements you agree to:");
			else
				x = new JLabel("Click the statement you agree to:");
			
			JPanel y = new JPanel ( new BorderLayout());
			y.add(qPanel,BorderLayout.CENTER);
			y.add(x,BorderLayout.NORTH);
			main.add(y,BorderLayout.SOUTH);
			
			x.setBorder(new EmptyBorder(10,0,10,10));
			
			for (Question q : questionsN ) {
				
				//Sp.p("Q "+q.question);
			}
			
		}
		
		
		// buttonPanel
		JButton okButton = Tk_TkStatic.getJButton(ok);
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);	
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(buttonHandler);
		// main.add(buttonPanel);
		
		int sizeadder = Tk_TkStatic.getSizeAdder();

			
		
		JPanel panel = Tk_TkStatic.getPanel(labelN, main, buttonPanel,tipN);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				// windowCanceled =true;	
				wcTrue();
			}
			
		});

		// textarea.selectAll();
		Tk_TkStatic.executeJDialog(jDialog, mainLabelN,
				tkN.getAttributeValue("label"),new Dimension(100,100),this,null,500);
		
		if ( windowCanceled )
			throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
		
		if ( confirmed )
			curValN = "1";
		else
			curValN = "0";
		
		if ( questionsN != null ) {
		Sp.p("questions.length="+questionsN.length);
		LogConnector lc2 = new LogConnector(null);
		for (Question qN : questionsN ) {
			String y = "0";
			if ( qN.answer == true )
				y = "1";
			Sp.p("Question mit id "+qN.prop_idN +" is "+y);
			tih.setVar("prop", 
					Entities.importEntities(qN.prop_idN), 
					Entities.importEntities(y));
			
			lc2.debug("   tk: " +y+" => (prop,"+qN.prop_idN+")"); 

		}
		}

	}
	private void msg ( String msg ) {
		Sp.p("Confirm Message: "+msg);
	}
	public synchronized void confirmedTrue() {
		confirmed =true;		
	}	
	public synchronized void confirmedFalse() {
		confirmed =false;		
	}		
	
	public synchronized void itemStateChangedSync(ItemEvent e)
	{
		Object source = e.getItemSelectable();
		for (Question qN : questionsN ) {
			if ( source == qN.jCheckBox ) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					Sp.p("set "+qN.questionN+" to true");
					qN.answer = true;
				} else if ( e.getStateChange() == ItemEvent.DESELECTED ) {
					qN.answer = false;
					Sp.p("set "+qN.questionN+" to false");
				}
				break;
			}
			
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
					// confirmed = true;
					confirmedTrue();
					// jDialog.dispose();
					dispose();

				} else if ( buttonText.equals(cancel) ) {
					confirmedFalse();	
					dispose();
				} else if ( buttonText.equals(save_textN) ) {
					// Sp.p("im buttonhandler,cancel");
					JFileChooser chooser = new JFileChooser();
					int retVal = chooser.showSaveDialog(null);
					if ( retVal == JFileChooser.APPROVE_OPTION ) {
						File destN = chooser.getSelectedFile();
						
						try {
							CommonsVfs.copy_filename(fileN, destN.getAbsolutePath(),tih.getSitzung());
							int gogogo = Tk_Token.go(1);
							msg("Copy of "+labelN+" was created under "+destN.getAbsolutePath());
							JOptionPane.showMessageDialog (null, "Copy of "+
							labelN+" was created under "+destN.getAbsolutePath(), 
							"Successfull Copy", JOptionPane.INFORMATION_MESSAGE);
							int gogogo2 = Tk_Token.leave(2);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							Sp.pe(e1);
							int gogogo = Tk_Token.go(1);
							JOptionPane.showMessageDialog (null, "Technical Problem:"+e1.getLocalizedMessage(), "Problem", JOptionPane.ERROR_MESSAGE);
							int gogogo2 = Tk_Token.leave(2);
						}
						
					} else if ( retVal == JFileChooser.CANCEL_OPTION) {
						msg("The saving of "+labelN+" was cancelled!");
						int gogogo = Tk_Token.go(1);
						JOptionPane.showMessageDialog (null, "The saving of "+
						labelN+" was cancelled!", "Warning", JOptionPane.WARNING_MESSAGE);
						int gogogo2 = Tk_Token.leave(2);
						
					} 
					
				}				
			}


		}
	}

	private class CheckBoxHandler  implements ItemListener  {
		
		public void itemStateChanged(ItemEvent e)
		{
			itemStateChangedSync(e);

		}
	}	
	private class Question {
		boolean answer = false;
		JCheckBox jCheckBox;
		String prop_idN;
		String questionN;
		
		public Question ( String q1N, String prop_idN ) throws Exception {
			if (q1N==null || prop_idN == null )
				throw new Exception("Tk-Accept-Questions must have attribute 'id' and  'label'");
			this.jCheckBox = new JCheckBox(q1N);
			this.prop_idN = prop_idN;
			this.questionN = q1N;

			this.jCheckBox.setFont(Tc_AppConsole.guiSizer.tkFont);
			
		}
	}
	@Override
	public String getValueN() {
		return curValN;
	}
	public String getValue4RegexN() {
		return getValueN();
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
		return false;
	}	
	
	@Override
	public synchronized  void setValueN(String value) {
		curValN = value;	
	}
	

}