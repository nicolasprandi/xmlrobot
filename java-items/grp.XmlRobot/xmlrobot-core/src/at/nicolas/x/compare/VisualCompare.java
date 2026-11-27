// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.compare;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.vfs2.FileName;
import org.jdom2.Element;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class VisualCompare implements ITask {
	Element e;
	IRequestHandler rh;

	
	JDialog jFrame;

	@Override
	public Parameters execute(Element e, 
			IRequestHandler rh) throws Exception {
		// TODO Auto-generated method stub
		this.e = e;
		this.rh = rh;


		if ( e.getAttribute("files") == null)
			throw new Exception("visual-file-compare: needs attribute 'files' with a filename!");
		boolean modal = MyCommons.hasFlag(e, "modal");
		
		Element root = MyCommons.getElementByFilename( e.getAttributeValue("files"),rh );

		int number = root.getChildren().size();
		String encoding = root.getAttributeValue("encoding");
		if ( encoding==null || encoding.equals(""))
			encoding = "UTF-8";
		String topheader = null;
		if ( root.getAttribute("label") != null )
			topheader = root.getAttributeValue("label");
	
		String header = "";
		JPanel panel = new JPanel ( new GridLayout(1,number));
		List<Element> list = root.getChildren();
		Iterator<Element> iterator = list.iterator();
		boolean first = true;
		while ( iterator.hasNext()) {
			Element xfile = (Element)iterator.next();
			String file_encoding = xfile.getAttributeValue("encoding");
			if ( file_encoding==null || file_encoding.equals(""))
				file_encoding =encoding;			
			FileView fileview = new FileView(xfile.getAttributeValue("fn"),file_encoding,xfile.getAttributeValue("label"),this.rh);
			panel.add(fileview.getJPanel());
			if ( !first )
				header = header.concat(", ");
			header = header.concat(fileview.getHeader());
			
			first = false;
		}
		
		jFrame = new JDialog();
		jFrame.setSize(1000, 500);
		jFrame.setLocation(1, 1);
		if ( topheader != null )
			jFrame.setTitle(topheader+" ("+header+")" );
		else
			jFrame.setTitle("Viewing "+number+" Files ("+header+")" );
		// jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
		jFrame.setContentPane(panel);
		
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				jFrame.dispose();				
			}
			
		});

		/*
		out.flush();		
		if ( e!= null ) {
			textarea.append("\nMessage: \n\n  " +e.getLocalizedMessage()+"\n\n");		
			textarea.append("ExceptionClass: "+ e.getClass().getCanonicalName()+"\n");
	        StackTraceElement[] trace = e.getStackTrace();
	        for(int i = 0; i<trace.length; i++){
	        	textarea.append("  "+trace[i].toString()+"\n");
	        }
	        	
		}
		textarea.append(outputStream.toString());
		textarea.append("\n");
		*/
		jFrame.setModal(modal);
		
		jFrame.setVisible(true);		
		
		return null;
	}



	private class FileView {
		String filename;
		TextArea ta;
		JPanel panel;
		Font myfont= new Font("Courier",0 , 14);
		String header;
		FileView(String filename,String encoding, String name, IRequestHandler rh) throws Exception {
			this.filename = filename;
			ta = new TextArea();
			
			ta.setFont(myfont);
			ta.append(readTextfileFormatNumbered(filename, encoding,rh));
			panel = new JPanel(new BorderLayout());
			FileName fn =  MyCommons.getFileObjectByFilename(filename, rh).getName();//  MyCommons.getFileNameByFilename(filename,rh);
			
			String labeltext = "";
			if ( name != null &&  !name.equals("")) {
				labeltext = name+", ";
				header = name;
			} else
				header = fn.getBaseName();
			
			labeltext = labeltext + fn.getBaseName()+", "+filename;
			panel.add(new JLabel(labeltext),BorderLayout.NORTH);
			panel.add(ta,BorderLayout.CENTER);
			
		}
		TextArea getTextarea() {
			return ta;
		}
		JPanel getJPanel() {
			return panel;
		}		
		String getHeader() {
			return header;
		}
	}	
	private String readTextfileFormatNumbered(String fileName, String format, IRequestHandler rh) throws Exception {
		String myFormat = format;
		if ( myFormat == null ) myFormat = "UTF-8";
	    try {
	    	InputStream is = MyCommons.getInputStreamByFilename(fileName,rh);
			StringBuffer sb = new StringBuffer();
	    	BufferedReader in = new BufferedReader(new InputStreamReader(is, myFormat));

			String s;
			int curLn = 1;
			while((s = in.readLine()) != null) {
				String UTF8Str = new String(s.getBytes(),myFormat);
				sb.append(curLn);
				sb.append(" ");
				sb.append(UTF8Str);
				sb.append("\n");
				curLn++;
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("--- News from readTextfileFormatNumbered("+fileName+", "+format+") ---");
			e.printStackTrace();
			throw e;
		}
	  }		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VisualCompare x = new VisualCompare();
		Element e = new Element("x");

	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		
		
	}

}