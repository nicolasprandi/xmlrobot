/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Tc_Modalmanager extends Object {

  public static Tc_Modalmanager modalmanager = new Tc_Modalmanager();
  
	JFrame jFrame = null;
	Tc_UiModal uiModal = null;
	Dimension dim = new Dimension(600,200);
	Point point = new Point(0,0);
	
	public Tc_Modalmanager ( )  {

	}
	
	public void requestModal ( Tc_UiModal _uiModal ) {
		closeDialog();
		uiModal = _uiModal;
		jFrame = new JFrame();
		loadProps();
		jFrame.setTitle(uiModal.getTitle() );
		Container container = jFrame.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(uiModal.getJComponent(), BorderLayout.CENTER);
		jFrame.addWindowListener(new WindowAdapter() {
					      public void windowClosing(WindowEvent e) {
					       closeDialog();
					      }
					    });			
		jFrame.show();
  }
  
  public void closeDialog() {

    if ( uiModal != null ) {
      uiModal.closeDialog();
      uiModal = null;
    }
    
    if ( jFrame != null ) {
      saveProps();
      jFrame.dispose();
    }

  }
  
  private void saveProps() {
    dim = jFrame.getSize();
		point = jFrame.getLocation();
  }

  private void loadProps() {
    jFrame.setLocation(point);
    jFrame.setSize(dim);
  }
		
}