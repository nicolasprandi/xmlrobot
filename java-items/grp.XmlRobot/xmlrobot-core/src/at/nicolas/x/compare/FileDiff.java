// Copyrigh 2015-2018 Nicolas Prandstaetter. All rights reserved.
/*
 * at.nicolas.x.compare.filediff.Filediff
 */
package at.nicolas.x.compare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;
import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class FileDiff implements ITask {


	IRequestHandler rh = null;

	
	@Override
	public Parameters execute(Element eCmd, 
			IRequestHandler _rh) throws Exception {
		

		rh = _rh;
 		/*[requo-timer]*/
		/*[requo-customer]*/ 
		if (   eCmd.getAttribute("old")== null
			|| eCmd.getAttribute("new")== null
			|| eCmd.getAttribute("out")== null)
				throw new Exception("filediff, define attributes: old, new and out");
			
			String old_string = eCmd.getAttributeValue("old");
			String new_string = eCmd.getAttributeValue("new");
			String out_string = eCmd.getAttributeValue("out");
			
			String old_format = null;
			if ( eCmd.getAttribute("old_format") != null ) {
				old_format = eCmd.getAttributeValue("old_format");
			} else if ( eCmd.getAttribute("format") != null ) {
				old_format = eCmd.getAttributeValue("format");
			}
			
			String new_format = null;
			if ( eCmd.getAttribute("new_format") != null ) {
				new_format = eCmd.getAttributeValue("new_format");
			} else if ( eCmd.getAttribute("format") != null ) {
				new_format = eCmd.getAttributeValue("format");
			}	
			
			String out_format = "UTF-8";
			if ( eCmd.getAttribute("out_format") != null ) {
				out_format = eCmd.getAttributeValue("out_format");
			} else if ( eCmd.getAttribute("format") != null ) {
				out_format = eCmd.getAttributeValue("format");
			}				
			
        List<String> original = fileToLines(old_string, old_format,_rh);
        List<String> revised  = fileToLines(new_string, new_format,_rh);
        
        Patch patch = DiffUtils.diff(original, revised);

        Element oe_top = new Element("deltas");
        
        int diff_count = 0;
        for (Delta delta: patch.getDeltas()) {
        		diff_count++;
        		Element oe = new Element("delta");

                Delta.TYPE type = delta.getType();

                String o_text = delta2String(delta.getOriginal());
                String r_text = delta2String(delta.getRevised());
                // System.out.println("Orig: "+delta.getOriginal());
                // System.out.println("Revi: "+delta.getRevised());
                
                oe.setAttribute("type", type.name().substring(0, 1));
                
                Element old_e = new Element("old");
                
                old_e.setAttribute("line", String.valueOf(delta.getOriginal().getPosition()+1));
                old_e.setAttribute("size", String.valueOf(delta.getOriginal().size()));
                if ( o_text != null )
                	old_e.setText( o_text );
                
                Element new_e = new Element("new");
                new_e.setAttribute("line", String.valueOf(delta.getRevised().getPosition()+1));
                new_e.setAttribute("size", String.valueOf(delta.getRevised().size()));
                if ( r_text != null )
                	new_e.setText( r_text );  
                
                oe.addContent(old_e);
                oe.addContent(new_e);
                
                oe_top.addContent(oe);
                

        }			
        oe_top.setAttribute("count", String.valueOf(diff_count));
        Document doc_to_write = new Document(oe_top);
        writeToFile(doc_to_write, out_string, out_format,_rh);
        //lc.debug(tname+": Written file with deltas to "+out_string, this);
		return null;
	}
	private static String delta2String ( Chunk in ) {
		String result = "";
		int parts = 0;
        for ( Object line: in.getLines()) {                	
        	result = result + line+"\n";
        	parts++;
        }
        if ( parts==0) return null;
        result = result.substring(0, result.length()-1);
        return result;	
	}
	
    private static List<String> fileToLines(String filename, String format, IRequestHandler rh) {
		String myFormat = format;
		if ( myFormat == null ) myFormat = "UTF-8";
		
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
        	InputStream is = MyCommons.getInputStreamByFilename(filename,rh);
        	BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = in.readLine()) != null) {
                	String UTF8Str = new String(line.getBytes(),myFormat);
                        lines.add(UTF8Str);
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return lines;
    }	
    
	public static void writeToFile( Document doc , String filename, String out_format, IRequestHandler rh ) throws IOException {
		OutputStream os = null;
	      try {
	    	  Format format = Format.getPrettyFormat();
	    	  format.setEncoding(out_format);
			XMLOutputter serializer = new XMLOutputter(format);
			os = MyCommons.getOutputStreamByFilename(filename, false,rh);
			
			  serializer.output(doc, os);
			  os.close();
		} catch (Exception e) {
			if ( os!= null ) os.close();
			e.printStackTrace();
			throw new IOException("writeToFile (Doc,"+filename+") not perforemd "+e.getLocalizedMessage());
		}
	}


	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		//this.tname = id;
		
	}
		
	


}