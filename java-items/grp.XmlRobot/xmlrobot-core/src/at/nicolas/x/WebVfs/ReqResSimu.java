package at.nicolas.x.WebVfs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Level;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Seitenlinks;
import at.nicolas.x.term_ti.RefCode;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class ReqResSimu extends ReqRes {
	JobRunner jr;
	ITerminator terminator;
	JFrame jFrame;
	JTabbedPane tabbedMain;
	
	JButton buttonTkConf;
	// JTextField inputTkConf ;
	ButtonHandlerTkConf buttonHandlerTkConf;
	
	JTextField inputTiConf ;
	ArrayList<Seitenlinks> seitenlinks;
	ButtonHandlerTiConf buttonHandlerTiConf;	
	JButton buttonTiConf;

	
	public ReqResSimu(JobRunner jr, ITerminator terminator) {
		this.jr = jr;
		this.terminator = terminator;
		seitenlinks = new ArrayList<Seitenlinks>();
		
		jFrame = new JFrame("hallo");
		jFrame.setSize(1200, 900);
		jFrame.setLocation(30, 10);
		jFrame.setTitle("XmlRobot Workspace" );
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //   JFrame.DISPOSE_ON_CLOSE);
		
		tabbedMain = new JTabbedPane();
		
				
		buttonTkConf = new JButton("Signal: Tk-Return");
		//inputTkConf = new JTextField();
		//inputTkConf.setEditable(true);	
		buttonHandlerTkConf = new ButtonHandlerTkConf();				
		buttonTkConf.addActionListener(buttonHandlerTkConf);
		
		buttonTiConf = new JButton("Signal: Request Link");
		buttonHandlerTiConf = new ButtonHandlerTiConf();
		buttonTiConf.addActionListener(buttonHandlerTiConf);
		inputTiConf = new JTextField();
		inputTiConf.setEditable(true);
		
		JPanel tiPanel = new JPanel(new GridLayout(1,2));
		tiPanel.add(inputTiConf);
		tiPanel.add(buttonTiConf);		
		
		JPanel cmd1 = new JPanel(new BorderLayout());
		//cmd1.add(new JTextField("TK-RETURN"),BorderLayout.WEST);
		//cmd1.add(inputTkConf, BorderLayout.CENTER);
		cmd1.add(buttonTkConf,BorderLayout.NORTH);
		cmd1.add(tiPanel,BorderLayout.SOUTH);
	
		JPanel cmd2 = new JPanel(new BorderLayout());
		cmd2.add(tabbedMain, BorderLayout.CENTER);
		cmd2.add(cmd1,BorderLayout.NORTH);
		
		jFrame.setContentPane(cmd2);
		jFrame.setVisible(true);
	}
	
	public void treatException(XrRqRpEx answerEx) {
		JEditorPane jEditorPaneTi = null;
		jEditorPaneTi = new JEditorPane();
		jEditorPaneTi.setContentType("text/html");
		jEditorPaneTi.setEditable(false);

		JScrollPane jScrollPaneTi = new JScrollPane(jEditorPaneTi);
		jEditorPaneTi.setText("<html><body><p>"+Commons.escapeXml(answerEx.getTp())+" and nobody</p>"+
		// answerEx.getTiBody()+
		"</body></html>");
		
		tabbedMain.add(jScrollPaneTi,"Ti-Exception");
		tabbedMain.setSelectedComponent(jScrollPaneTi);
		//buttonHandler = new ButtonHandlerTiConf();
		// buttonTiConf.addActionListener(buttonHandler);
		jEditorPaneTi.setCaretPosition(0);
		jFrame.setVisible(true);	
		
	
		Sp.p("EXCEPTION");
		Sp.p("EXCEPTION "+answerEx.getException().getMessage());
		Sp.p("EXCEPTION");
	}	
	
	public void treatTiResponse(XrRqRpTi answerTi) {
	
		
		String screen = terminator.getTiScreenBodyMemberAndConsume("ReqResSimu");

		JEditorPane jEditorPaneTi = null;
		jEditorPaneTi = new JEditorPane();
		jEditorPaneTi.setContentType("text/html");
		jEditorPaneTi.setEditable(false);

		JScrollPane jScrollPaneTi = new JScrollPane(jEditorPaneTi);
		jEditorPaneTi.setText("<html><body>"+screen+"</body</html>");

		Seitenlinks newseitenlinks = terminator.getTerminalLinkArrayLast();
		seitenlinks.add(newseitenlinks);
		
		Sp.p("Links was set to something with len "+newseitenlinks.tlList.size());
		Iterator<Ti_TerminalLink> j = newseitenlinks.tlList.iterator();
		String r0 = "";
		while ( j.hasNext()) {
			String r1= "";
			Ti_TerminalLink tl = (Ti_TerminalLink)j.next();
			
			r1="<tr>";
			r1 = r1+ 
				 "<td>"+Commons.cutNice(Entities.exportEntities(tl.getLabel()),20)+"</td>";
			
			String r2="";
			ArrayList<RefCode> refcodes = tl.getHtml().getRefcodes();
			if ( refcodes != null ) {
				Iterator<RefCode> fci = refcodes.iterator();
				while ( fci.hasNext() ) {
					RefCode refcode = (RefCode)fci.next();
	
					r2=r2+"<tr>";
					// r2=r2+"<td>Link </td>";
					r2=r2+"<td>"+refcode.about+"</td>";
					// r2=r2+"<td>"+refcode.refcode+"</td>";	
					r2=r2+"<td><a href='"+refcode.refcode+"'>"+refcode.refcode+"</a></td>";
					r2=r2+"</tr>";
					//""+refcode.refcode+"' style='color:black;'>"+refcode.refcode+"</a>"
					
				}
				r2 = "<table>"+r2+"</table>";
			}

			if ( r2.length() > 0) {
				r1 = r1+
						 "<td>"+r2+"</td>"
						 +"</tr>";
				r0=r0+r1;				
			}

		}
		r0 = "<table border='1'>"+r0+"</table>";

		
		JEditorPane jEditorPaneTi2 = null;
		jEditorPaneTi2 = new JEditorPane();
		jEditorPaneTi2.setContentType("text/html");
		jEditorPaneTi2.setEditable(false);

		JScrollPane jScrollPaneTi2 = new JScrollPane(jEditorPaneTi2);
		jEditorPaneTi2.setText("<html><body>"+r0+"</body</html>");
		
		
		jEditorPaneTi2.addHyperlinkListener (
			    new HyperlinkListener () {
			  public void hyperlinkUpdate(HyperlinkEvent e) {

			    if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {

			     } else if (e.getEventType() == 
			                          HyperlinkEvent.EventType.ACTIVATED) {
			    	 
			    	 String href = e.getDescription(); 
			    	 performMarkSelection(href);
			      } else if (e.getEventType() == 
                          HyperlinkEvent.EventType.ENTERED) {

				  } 
			      
			   }
			  });	


		/*
		JPanel inputPanel2 = new JPanel(new BorderLayout());
		inputPanel2.add(jScrollPaneTi2,BorderLayout.CENTER);
		inputPanel2.add(inputPanel,BorderLayout.NORTH);
		*/
		
		/*
		JTabbedPane tabbed = new JTabbedPane();
		tabbed.add(jScrollPaneTi,"print");
		tabbed.add(jScrollPaneTi2,"links");
		*/
		JPanel tabbed2 = new JPanel(new GridLayout(1,2));
		tabbed2.add(jScrollPaneTi);
		tabbed2.add(jScrollPaneTi2);	
		// DefaultCaret caret2 = ((DefaultCaret)jEditorPaneTi2.getCaret());
		jEditorPaneTi2.setCaretPosition(0);
		jEditorPaneTi.setCaretPosition(0);
		
		
		tabbedMain.add(tabbed2,"Ti-Response");
		tabbedMain.setSelectedComponent(tabbed2);
		//buttonHandler = new ButtonHandlerTiConf();
		// buttonTiConf.addActionListener(buttonHandler);
		jFrame.setVisible(true);	
		
	}	
		
	private class ButtonHandlerTiConf implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			performMarkSelection(inputTiConf.getText());
		}
	}
	private synchronized void doContinueAfterTk() {
		// jr.runNewJob(interpreter, reqRes);
		jr.continueAfterTk();
		
	}
	private class ButtonHandlerTkConf implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			doContinueAfterTk();
		}
	}	
	
	public synchronized void performMarkSelection(String marker) {
		// String marker = input.getText();
		//Sp.p("Performing marker for: "+marker);
		//Sp.p("Links was to search in with nrbOfPages "+seitenlinks.size());		
		Ti_TerminalLink found = null;
		
		Iterator<Seitenlinks> i = seitenlinks.iterator();
		while ( i.hasNext() && found == null ) {
			Seitenlinks aktuell = (Seitenlinks)i.next();
			Iterator<Ti_TerminalLink> j = aktuell.tlList.iterator();
			
			while ( j.hasNext() && found==null) {
				
				Ti_TerminalLink tl = (Ti_TerminalLink)j.next();
				
				ArrayList<RefCode> refcodes = tl.getHtml().getRefcodes();
				if ( refcodes != null ) {
					Iterator<RefCode> fci = refcodes.iterator();
					while ( fci.hasNext() && found == null ) {
						RefCode refcode = (RefCode)fci.next();
						// Sp.p("Checking "+refcode.refcode);
						if ( refcode.refcode.equals(marker))
							found = tl;				
					}
				}
				
			}			
		}
		

		if ( found != null ) {
			// Sp.p("Found Reactor: "+found.getLabel());
			
			Interpreter tocont = null;
			try {
				tocont = found.getInterpreterForReaction(
						marker, true, new GuiControllerDesktop(),Terminal.offlineSitzung);
			} catch (Exception e) {
				tocont = null;
			}
			if (tocont != null ) {
				//JOptionPane.showMessageDialog(null,"Found Interpreter to react:"+
				//		tocont.getDescriptionStringSmall());
				// das ist die Simulation zum testen
				Sp.p("VON B");
				//tocont.setIncludeHeaderAndFooter(true);
				jr.runNewJob(tocont, this);
			} else 
				Sp.p("Didnt receive an interpreter from link for ["+marker+"]");
		} else
			Sp.p("Cant find link ["+marker+"]");
			
	}	
	private int tkSequence = 0;
	public void treatTkResponse(XrRqRpTk answerTk) {
		tkSequence++;
		JOptionPane.showMessageDialog (null, 
				 
				Commons.toTextCompact(answerTk.getTranslatedForPrint()), 
				"Everything is shut down: ["+tkSequence+"]",
				JOptionPane.INFORMATION_MESSAGE);
		try {
			terminator.executeTk(answerTk.getTranslatedForPrint(), 
					Level.INFO, null, null, null, null, answerTk.ticketManager.tiHandler);
			JOptionPane.showMessageDialog (null, 
					"["+tkSequence+"]",
			"Everything is shut down: ["+tkSequence+"]",
			JOptionPane.INFORMATION_MESSAGE);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.p("Problem in executing tk");
		}
		
		return;
	}
}
