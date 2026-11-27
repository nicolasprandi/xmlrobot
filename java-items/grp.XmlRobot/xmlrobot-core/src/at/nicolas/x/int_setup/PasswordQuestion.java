package at.nicolas.x.int_setup;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import at.nicolas.x.int_interpreter.Sp;

public class PasswordQuestion {

	public PasswordQuestion() {
		// TODO Auto-generated constructor stub
	}
	

	
	public String go2() {
		

	        JPanel panel = new JPanel();
	        final JPasswordField passwordField = new JPasswordField(16);
	        panel.add(new JLabel("You Password"));
	        panel.add(passwordField);
	        JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
	            @Override
	            public void selectInitialValue() {
	                passwordField.requestFocusInWindow();
	            }
	        };
	        pane.createDialog(null, "Welcome to XmlRobot").setVisible(true);
	        return passwordField.getPassword().length == 0 ? null : new String(passwordField.getPassword());

	}	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PasswordQuestion x = new PasswordQuestion();
		Sp.p("pwd="+x.go2()+".");

	}

}
