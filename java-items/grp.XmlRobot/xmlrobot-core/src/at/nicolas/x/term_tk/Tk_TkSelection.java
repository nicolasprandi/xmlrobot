/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkSelection extends Tk_Tk implements Tk_IKeysOn{

	private Element tk;
	private String id;
	private String curVal = null;
	ITiHandler tih;
	String mainLabel;
	String tip;

	
	private String no_value = "";	
	
	JButton okButton;
	JButton cancelButton;
	ButtonHandler buttonHandler;
	
	static String ok= new String("Ok");
	static String cancel= new String("No Value");	
	
	ArrayList<String> keyList = null; 
	ArrayList<Boolean> visibleList = null; 	
	
	Element options = null;
	boolean multi = false;
	boolean nono = false;
	boolean manual = false;
	boolean fresh_xml = false;
	
	private HashMap<String, String> map;
	
	public void built(Element tk,ITiHandler tih,String mainLabel, String tip) throws Exception {
		this.tk = tk;
		this.tih = tih;
		this.mainLabel = mainLabel;
		this.tip = tip;

		this.nono = Commons.hasFlag(tk, "nono");
		id = tk.getAttributeValue("id");
		curVal =  Tk_TkStatic.getDefaultValue(tk,getVarType(), id, tih);
		if ( tk.getAttribute("no_is")!=null)
			no_value = tk.getAttributeValue("no_is");		
		
		if ( tk.getAttributeValue("xml") != null ) {
			options = CommonsVfs.getElementByFilename(tk.getAttributeValue("xml"),tih.getSitzung());
			this.fresh_xml = Commons.hasFlag(tk, "fresh_xml");
			/*
			if ( Commons.hasFlag(tk, "xml_translate")) {
				options = tih.translate(options);
				Entities.exportEntities(options);
				
			} else if ( Commons.hasFlag(tk, "xml_import")) {
				Entities.importEntities(options);				
			} 
			*/
		} else {
			options = tk;
		}
		if ( Commons.hasFlag(tk, "manual") || Commons.hasFlag(tk, "update") )
			throw new Exception("tk sel flags update and manual not implemented yet.");

		multi = Commons.hasFlag(tk, "multi");
		manual = Commons.hasFlag(tk, "manual");
		
		buildMap();

	}

	private void buildMap() {
		map = new HashMap<String, String> ();
		List<Element> lo = options.getChildren();
		Iterator<Element> io = lo.iterator();
		
		while ( io.hasNext()) {
			Element ao = (Element)io.next();
			boolean do_show = true;
			//if ( ao.getAttribute("test") != null ) {
			//	if ( ao.getAttributeValue("test").equals("0"))
			//		do_show = false;
			//}
			if ( Tester.testOrTestdynAttributeIs0(ao,tih)) {
				do_show = false;
			}
			
			if ( do_show ) {
				String aoText = ao.getText();
				String aoLabel = ao.getAttributeValue("label");
				if ( aoLabel == null )
					aoLabel = aoText;				

				map.put ( aoText, aoLabel);		

				
			}

		}
		
	}
	public String getVarType() {
		return ("prop");
	}

	public String getVarIdN() {
		return id;
	}

	private boolean contains(String where, String what ) {

		if ( where != null ) {
			if ( 	where.equals(what) || 
					where.indexOf(" "+what+" ") != -1 || 
					where.startsWith(what+" ") || 
					where.endsWith(" "+what))
				return true;			
		}
		return false;		
	}
	
	boolean did_a_clear;
	public void execute(boolean first_go) throws Exception {
		did_a_clear = false;
		Sp.p("tk-sel-execution for id ["+id+"]");
		if ( fresh_xml ) {
			Sp.p("                    with fresh_xml");
			options = CommonsVfs.getElementByFilename(this.tk.getAttributeValue("xml"),tih.getSitzung());
			buildMap();
		}
		Sp.p("tk-sel-execution will work with: "+Commons.toText(options));
		DefaultListModel listModel = new DefaultListModel();
		List<Element> lo = options.getChildren();
		int number_of_selections = lo.size();
		Iterator<Element> io = lo.iterator();
		int[] initial2 = new int[lo.size()];
		int initial2_size = 0;
		int r = 0;
		keyList = new ArrayList<String>();
		visibleList = new ArrayList<Boolean>();
		while ( io.hasNext()) {
			Element ao = (Element)io.next();
			boolean do_show = true;
			if ( Tester.testOrTestdynAttributeIs0(ao,tih))
				do_show = false;
			
			if ( do_show ) {
				boolean isVisible = true;
				String aoText = ao.getText();
				String aoLabel = ao.getAttributeValue("label");
				if ( aoText == null || aoText.equals("")) {
					listModel.addElement(aoLabel);
					isVisible = false;
				} else if ( aoLabel == null ) {
					listModel.addElement(aoText);				
				} else {
					listModel.addElement(aoLabel);
				}
				//String listmodeltext = (String)listModel.get(r);
				
				visibleList.add(new Boolean(isVisible));
				keyList.add(aoText);
				
				// selektierten wert ermitteln
				if ( multi && aoText.indexOf(" ")>-1) {
					LogConnector lc = new LogConnector("tk-sel");
					lc.error("A blank is not allowed in multi-selection: id="+id+", value="+aoText);
					lc.error("A blank is not allowed in multi-selection: id="+id+", value="+aoText);
					lc.error("A blank is not allowed in multi-selection: id="+id+", value="+aoText);
					throw new LangException("A blank is not allowed in multi-selection: "+aoText);
				} 
				
				if ( contains(curVal,aoText)) {
					// Sp.p("1 added to inital "+r);
					initial2[initial2_size] = r;
					initial2_size++;
				}
				r++;			
			}
		}	
				
		JList list = new JList( listModel );
		
		// selektierte werte setzten
		int[] initial3 = new int[initial2_size];
		System.arraycopy(initial2, 0, initial3, 0, initial2_size);		
		list.setSelectedIndices(initial3);
		
		if ( multi) 
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		else
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(Tc_AppConsole.guiSizer.tkFont);
		Tk_TkSelRenderer renderer = new Tk_TkSelRenderer();
		renderer.setTk_TkSelection(this);
		list.setCellRenderer(renderer);
		//list.setForeground(Color.yellow);
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if ( multi==false && me.getClickCount() == 2 ) {
					// Sp.p("mouse double clicked");
					// windowCanceled=false;
					wcFalse();
					// jDialog.dispose();
					dispose();
				}
			}
			
		};
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER && keys_on && (ke.getWhen()>keys_on_start)) {
					wcFalse();
					dispose();
				}
			}
			
		};		
		
		list.addMouseListener(mouseAdapter);
		list.addKeyListener(keyAdapter);
		
		
		JButton okButton = Tk_TkStatic.getJButton(ok);
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(buttonHandler);
		
		
		JPanel buttonPanel = new JPanel();
		int sizeadder = Tk_TkStatic.getSizeAdder();

		buttonPanel.add(okButton);
		if ( !nono)
			buttonPanel.add(cancelButton);		
		
		JPanel buttonPanel2 ;
		if ( multi ) {
			buttonPanel2 = new JPanel ( new BorderLayout());
			buttonPanel2.add(buttonPanel, BorderLayout.CENTER);
			JLabel multi_label;
			if ( Tc_AppConsole.mac)
				multi_label = new JLabel ("<html><body><br>This is a Multi-Selection,<br>Use cmd+click to select</body></html>");			
			else
				multi_label = new JLabel ("<html><body><br>This is a Multi-Selection,<br>Use ctrl+click to select</body></html>");

			multi_label.setHorizontalAlignment(SwingConstants.CENTER);
			buttonPanel2.add(multi_label, BorderLayout.SOUTH);
		} else
			buttonPanel2 = buttonPanel;
		
		list.setBorder(new EmptyBorder(5,5,5,5));
		JScrollPane scroll = new JScrollPane(list);
		JPanel panel = Tk_TkStatic.getPanel(tk.getAttributeValue("label"), scroll, buttonPanel2,tip);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				wcTrue();
			}
			
		});
	
		int my_hight = 0;
		if ( number_of_selections > 20) {
			my_hight = 600;

			// Sp.p("--- 20 ----");
		} else if ( number_of_selections > 10) {
			my_hight = 410;
			// Sp.p("--- 10 ----");
		} else if ( number_of_selections > 6) {
			my_hight = 330;
			// Sp.p("---6  ----");
		} else {
			my_hight = 280;
			// Sp.p("--- ? ----");
		}
		// Sp.p("my_hight="+my_hight);
		int my_hight2 = number_of_selections*list.getFont().getSize()*2+110; 
		if (my_hight2 > 700 ) my_hight2 = 700;
		
		int target_hight = 0;
		if ( my_hight > my_hight2 )
			target_hight = my_hight;
		else
			target_hight = my_hight2;
		
		windowCanceled = false;
		
		Tk_TkStatic.executeJDialog(jDialog, mainLabel,Tk_TkStatic.getLabelOrId(tk),new Dimension(801,target_hight+sizeadder),this,null,300);
		Sp.p("executeJDialog done");
		if ( windowCanceled )
			throw new TkStopException(Tk_TkStatic.getQuitMessage(tk));
		
		if ( !did_a_clear) {
			
			if ( multi) {
				// Object ol[] = list.getSelectedValues();
				int il [] = list.getSelectedIndices();

				int index = 0;
				curVal = "";
				while ( index < il.length ) {
					if (visibleList.get(il[index])) {
						curVal = curVal + keyList.get(il[index]);
						
						if ( index < il.length)
							curVal = curVal + " ";							
					}
					index++;
				
				}
				
			} else {
				if ( list.getSelectedIndex() >= 0 ) {
					if (visibleList.get(list.getSelectedIndex())) 
						curVal = keyList.get(list.getSelectedIndex());
					else
						curVal = "";
				} else
					curVal="";
			}		
			Sp.p("Selection resultet in curVal ["+curVal+"]");
		} else {
			Sp.p("did a clear");
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
					// Sp.p("im buttonhandler,ok");
					// jDialog.dispose();
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					Sp.p("im buttonhandler,cancel");
					clearValue2();
					dispose();

						
				}				
			}

		}
	}	
	@Override
	public String getValueN() {
		return curVal;
	}
	
	public synchronized String getValuePN() {
		String retval = null;
		if ( curVal !=null && curVal.length() > 0 ) {
			if ( multi ) {
				String[] splittet = curVal.split("\\s+");
				for ( String x : splittet ) {
					String add = map.get(x);
					if ( add==null)
						add = x;
					if ( retval == null )
						retval = add+", ";
					else
						retval = retval+add+", ";
				}
				if ( retval !=null && retval.endsWith(", "))
					retval = retval.substring(0, retval.length()-2);
				
			} else {
				retval = map.get(curVal);			
				if ( retval == null )
					retval = curVal;
			}
			
		}
		if (retval == null )
			retval = "";

		return retval;
	}	

	public void clearValue() {
		// TODO Auto-generated method stub
		curVal = no_value;
		
	}
	public void clearValue2() {
		// TODO Auto-generated method stub
		curVal = no_value;
		did_a_clear = true;
		
	}

	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return nono;
	}
	
	@Override
	public  synchronized void setValueN(String value) {
		curVal = value;	
	}
	public String getValue4RegexN() {
		return getValueN();
	}
	/*
	public void finalize() throws Exception {
		// TODO Auto-generated method stub
		if ( out != null && out_fn != null ) {
			 Document doc = new Document(out);
			 
			 Commons.writeToFile(doc, out_fn);
			 Sp.p("created out="+out_fn);
		}
		
	}	
	*/
	
	public static String getLabelOfCurrrentSelection ( 
			String currentValue,
			Element options,
			boolean multi,
			ITiHandler thetih,
			boolean debugMode) {
		try {
			String retval = null;
			if ( currentValue == null || currentValue.equals(""))
				retval = null;
			else if ( !multi) {
				String label = getLabelOfCurrrentSelectionSingle (currentValue,options,thetih,debugMode);

				retval = label;
			} else {
				String[] splittet = currentValue.split("\\s+");
				retval = "";
				for ( String x : splittet ) {
					if ( !retval.equals(""))
						retval = retval+", ";
					
					String foundLabel = getLabelOfCurrrentSelectionSingle (x,options,thetih,debugMode);
					if ( foundLabel == null )
						retval = retval+x;
					else
						retval = retval+foundLabel;				
				}

			}
				
			return retval;
		} catch (Exception e) {
			return currentValue;
		}
	}	
	
	public static String getLabelOfCurrrentSelectionSingle ( 
			String currentValue,
			Element options,
			ITiHandler thetih,
			boolean debugMode) {
		if ( currentValue == null ) return null;
		
		List<Element> lo = options.getChildren();
		Iterator<Element> io = lo.iterator();
		
		String label = null;
		boolean keepOn = true;
		
		while ( io.hasNext() && keepOn ) {
			Element ao = (Element)io.next();
			String aoText = ao.getText();
			if ( aoText == null );
			else if ( aoText.equals(currentValue)) {
				// da schauen wir weiter
				boolean do_show = true;
				/*
				if ( Tester.testAttributeIs0(ao,thetih)) {
					do_show = false;
				} */
				
				if ( do_show ) {
					// der ist es
					label = ao.getAttributeValue("label");
					keepOn = false;
				}
			}
		}
		String giveBack;
		if (label == null )
			giveBack = currentValue;
		else if ( debugMode )
			giveBack = label+"="+currentValue;
		else
			giveBack = label;		
		return giveBack;
	}		
}