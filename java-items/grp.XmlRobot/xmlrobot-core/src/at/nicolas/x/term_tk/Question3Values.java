/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import javax.swing.JOptionPane;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.TkStopException;

public class Question3Values {

	/**
	 * @param args
	 */
	Question3Values () {
		
	}
	public final int ANSWER1 = 1;
	public final int ANSWER2 = 2;
	public final int ANSWER3 = 3;
	public final int CANCEL = 0;
	public int question(String question_frame,String question, 
						String answer1, 
						String answer2, 
						String answer3,
						boolean throw_on_cancel,
						int default_button) throws Exception {
		int result = 0;
		String [] buttons = { answer1, answer2, answer3, "Forget it" };
		
		String default_value = "";
		if ( default_button == 1 )
			default_value = answer1;
		else if ( default_button == 2 )
			default_value = answer2;
		else if ( default_button == 3 )
			default_value = answer3;
		int gogogo = Tk_Token.go(1);
		int returnValue = JOptionPane.showOptionDialog(null, question, question_frame, JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, buttons,default_value ); //buttons[default_button-1]
		// Sp.p("returnValue="+returnValue);
		
		int gogogo2 = Tk_Token.leave(2);
		
		if ( returnValue == -1 || returnValue == 3) {
			// cancel
			if ( throw_on_cancel ) {
				throw new TkStopException("Stopped: "+question_frame);
			}
			result = CANCEL;
		} else if ( returnValue == 0 ) {
			// answer 1
			result = ANSWER1;
		} else if ( returnValue == 1 ) {
			// answer 2
			result = ANSWER2;
		}  else if ( returnValue == 2 ) {
			// answer 3
			result = ANSWER3;
		}else {
			if ( throw_on_cancel ) {
				throw new Exception("System delivered stupid things: ["+returnValue+"], question: "+question_frame);
			}			
		}
		
		
		return result;
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Question3Values x = new Question3Values();
		int ret = x.question(
				"You cancelled the input of HASIMAUSI",
				"What do you want to do?\n(1) Clear your input: []\n(2) Keep old input: [xykaklsdjf aslkfjd asfljasdf asdf]\n(3) Something", 
				"(1) Clear", 
				"(2) Keep", 
				"(3) Something",
				false, 
				1);
		Sp.p("ret="+ret);
	}

}