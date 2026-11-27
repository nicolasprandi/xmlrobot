/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;


import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import at.nicolas.x.int_interpreter.Sp;


public class Map_HtmlViewer {

	private JPanel container = null;
	JEditorPane content = null;
	
	String lastfile = null;

		
	public Map_HtmlViewer ( ) {
	

		content = new JEditorPane();
		content.setContentType("text/html");
		content.setEditable(false);
		container = new JPanel ( new BorderLayout());
		container.add( new JScrollPane(content), BorderLayout.CENTER );
		
				
	}
		
	public JComponent getJComponent() {
		return ((JComponent)container);		
	}
	
	public void showLink ( String linkname ) {
		if ( lastfile != null ) {
			String x = "/"+lastfile+"#"+linkname;
			Sp.p("--- x="+x);
			try {
				content.setPage(new URL(x));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}
		}
	}
	
	public void setText(String that ) {
		content.setContentType("text");
		content.setText(that);
		lastfile = null;
		
	}
	/*
	public void setFile( String filename ) {
		lastfile = filename;
		if ( filename == null ) {
			content.setContentType("text");
			content.setText("");

		} else {
			try {
				content.setPage ( (new File(filename)).toURL() );
				
				content.addHyperlinkListener (
					    new HyperlinkListener () {
					  public void hyperlinkUpdate(HyperlinkEvent e) {
					    // Das �ndern des Mauszeigers geht ab  
					    if (e.getEventType() == 
					                       HyperlinkEvent.EventType.ENTERED) {
					       ((JEditorPane) e.getSource()).setCursor(
					          Cursor.getPredefinedCursor(
					              Cursor.HAND_CURSOR));
					    } else
					      if (e.getEventType() == 
					                       HyperlinkEvent.EventType.EXITED) {
					       ((JEditorPane) e.getSource()).setCursor(
					           Cursor.getPredefinedCursor(
					              Cursor.DEFAULT_CURSOR));
					      } else
					       // Hier wird auf ein Klick reagiert
					        if (e.getEventType() == 
					                          HyperlinkEvent.EventType.ACTIVATED) {
					          JEditorPane pane = (JEditorPane) e.getSource(); 
					          if (e instanceof HTMLFrameHyperlinkEvent) {
					            HTMLFrameHyperlinkEvent evt = 
					                                   (HTMLFrameHyperlinkEvent)e;
					            HTMLDocument doc = 
					                 (HTMLDocument)pane.getDocument();
					            doc.processHTMLFrameHyperlinkEvent(evt);
					          } else try {
					                   // Normaler Link
					                   pane.setPage(e.getURL());
					                 } catch (Throwable t) {
					               

					                 }
					        }
					   }
					  });			
				
			} catch (Exception e1) {
				content.setContentType("text");
				content.setText("Das Html-File "+filename+" wurde nicht gefunden.");
			} 	
			
		}

	}
	*/
			
}