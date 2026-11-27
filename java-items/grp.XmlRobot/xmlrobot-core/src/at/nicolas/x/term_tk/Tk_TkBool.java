/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import javax.swing.JOptionPane;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.pub.Vcar;
import at.nicolas.x.term_core.Tc_AppConsole;

public class Tk_TkBool extends Tk_TkBoolBase implements Tk_IKeysOn{

	
	@Override
	public void execute(boolean first_go) throws Exception {
		
		Object[] options_def = {"Yes","No","No Value"};
		Object[] options_nono = {"Yes","No"};
		
		int default_value = -1;
		Vcar x = new Vcar(curValN);
		
		if ( x.isBool && x.getBool())
			default_value = 0;
		else if ( x.isBool && !x.getBool())
			default_value = 1;	
		else if ( nono )
			default_value = 0;
		else if ( !( !nono && !no_is_0))
			default_value = 1;
		else
			default_value = 2;
		
		String label2 = Commons.stringReplace(tkN.getAttributeValue("label"), "/n", "\n");
		if (tipN != null ) {
			// label2 = label2+"\nTip:\n   "+ Commons.stringReplace(tip, "/n", "\n   ");
			label2 = label2+"\nTip: "+ Commons.stringReplace(tipN, "/n", " ");
		}
		
		int n;
		if ( !nono ) {
			int gogogo = Tk_Token.go(1);
			n=	JOptionPane.showOptionDialog(
				Tc_AppConsole.getJFrame(), 
				label2, 
				this.mainLabelN,				
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE,
				null, 
				options_def, options_def[default_value]);
			int gogogo2 = Tk_Token.leave(1);
			// Sp.p("BOOL-A n="+n);
		} else {
			int gogogo = Tk_Token.go(1);
			n=	JOptionPane.showOptionDialog(
					null, 
					label2, 
					this.mainLabelN,				
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE,
					null, 
					options_nono, options_nono[default_value]);	
			int gogogo2 = Tk_Token.leave(4);
			// Sp.p("BOOL-B n="+n);
		}
		
		if ( n == -1 )
			throw new TkStopException("Input stopped");
		else if ( n== 0 ) 
			// yes
			curValN = "1";
		else if ( n==1)
			curValN = "0";
		else if ( n==2 && !hasNono())
			clearValue();
			//throw new Exception(TkStatic.getQuitMessage(tk));
			

	}
	




}