package at.nicolas.x.term_tk;


import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkSelBoolRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  //private Tk_TkSelection tk_TkSelection = null;


	public Component getListCellRendererComponent(
		    JList list,
			Object value,   // value to display
			int index,      // cell index
			boolean iss,    // is the cell selected
			boolean chf)    // the list and the cell have the focus
		    {

		        Component c = super.getListCellRendererComponent(list, value, index, iss, chf);
		       
		        Boolean isVisible = true;
		        // System.out.println("Rendering: "+value);
		        if ( !isVisible) {
		         
		         
		         //c.setBackground(Color.gray);
		         c.setFont(Tc_AppConsole.guiSizer.tkFontOff);
		         c.setEnabled(false);
		         // c.setFocusable(false);
		        
		         // c.setForeground(Color.black);
		         // c.set
		         // c.setFocusTraversalKeysEnabled(false);
		        }
		       


			return this;
		    }
}
