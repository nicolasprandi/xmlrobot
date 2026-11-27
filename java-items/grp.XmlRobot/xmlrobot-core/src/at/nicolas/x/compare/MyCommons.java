// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.compare;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import at.nicolas.x.pub.IRequestHandler;

public class MyCommons  {
	
	public static FileObject getFileObjectByFilename( String filename, IRequestHandler rh) throws FileSystemException {
    	FileObject fileObject = rh.getFsm().resolveFile(filename);    	
    	return fileObject;		
	}	

	public static FileObject getFileObjectByFilename( 
			String filename, 
			FileSystemOptions fileSystemOptions, IRequestHandler rh) throws FileSystemException {
    	FileObject fileObject = rh.getFsm().resolveFile(filename,fileSystemOptions);    	
    	return fileObject;		
	}		
	public static Element getElementByFilename( String filename, IRequestHandler rh ) throws JDOMException, IOException, Exception {

		return ( (getXmldocumentByFilename(filename,rh)).getRootElement());

	}		
	public static boolean hasFlag( Element e, String flagname ) {

		String flag = e.getAttributeValue("flag");
		if ( flag != null ) {
			if ( 	flag.equals(flagname) || 
					flag.indexOf(" "+flagname+" ") > -1 || 
					flag.startsWith(flagname+" ") || 
					flag.endsWith(" "+flagname))
				return true;			
		}
		return false;
	}
	public static String dirClassic ( String in ) {
		if ( in==null) return null;
		String out =in;
		if ( out.startsWith("file:///")) {
			out = out.substring(8);
		} else if ( out.startsWith("file://")) {
			out = out.substring(7);
		} else if ( out.startsWith("file:/")) {
			out = out.substring(6);
		}
		out = addFsInCase(out);
		return out;
	}
	public static String addFsInCase( String pfadname )  {
		// hole letztes zeichen raus
		if ( pfadname == null ) return pfadname;
		if ( pfadname.length() > 0 ) {
			String letztesZeichen = pfadname.substring(pfadname.length()-1);
			if (letztesZeichen.equals("\\") || letztesZeichen.equals("/")) {
				return pfadname;
			} else 
				return pfadname + "/";			
		} else
			return pfadname;
	}	
	public static Document getXmldocumentByFilename( String filename, IRequestHandler rh)  throws Exception, JDOMException, IOException{
		
		InputStream fis = getInputStreamByFilename(filename,rh);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try {
			doc = builder.build(fis);
			fis.close();
		} catch (Exception e1) {
			fis.close();
			
			// JOptionPane.showMessageDialog(null, "JDOMException "+e1.getLocalizedMessage()+"\non Reading File "+filename);

			throw new Exception (e1.getClass().getCanonicalName() +":"+e1.getLocalizedMessage()+"\n\n  "+filename);
			
		} 
		
		return ( doc );
	}	
	public static InputStream getInputStreamByFilename( String filename, IRequestHandler rh) throws FileSystemException, org.apache.commons.vfs2.FileSystemException {
    	
		FileObject fileObject = rh.getFsm().resolveFile(filename);
    	FileContent content = fileObject.getContent();
    	return content.getInputStream();		
	}	
	
	public static OutputStream getOutputStreamByFilename( String filename, boolean append, IRequestHandler rh) throws FileSystemException {
    	FileObject fileObject = rh.getFsm().resolveFile(filename);
    	FileContent content = fileObject.getContent();
    	return content.getOutputStream(append);		
	}		
	
	public static void writeToUTF8File( String fileinhalt, String filename, IRequestHandler rh ) throws IOException  {
        OutputStream os = getOutputStreamByFilename(filename, false,rh);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
			    os, "UTF8"));
			out.write(fileinhalt);
			out.close();
			os.close();
		} catch (IOException e) {
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				os.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
        
}	
	public static void writeToFile( Document doc , String filename, IRequestHandler rh ) throws IOException {
		
		writeToFile ( doc,filename,Format.getRawFormat(),"utf-8",rh);
	}
	public static void writeToFile( 
			Document doc , 
			String filename, 
			Format format,
			String encoding, IRequestHandler rh) throws IOException {
	Format myformat = format.clone();
	OutputStream os = null ;
      try {
    	  
    	  if ( encoding != null )
    		  myformat.setEncoding(encoding);
    	  else
    		  myformat.setEncoding("utf-8");
    	  XMLOutputter serializer = new XMLOutputter(myformat);
		  os = getOutputStreamByFilename(filename, false,rh);
				  
		  serializer.output(doc, os);
		  os.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		if ( os!=null)
			os.close();
		e.printStackTrace();
		throw new IOException("writeToFile Xml "+
		"filename="+filename+") not perforemd, Exception="+e.getMessage());
	}
}		

}

