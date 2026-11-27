// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.panorama;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class AggregateScan implements ITask {


	static IRequestHandler rh = null;
	static ITask itask = null;
	boolean singelton_running = false;
	static String s_out_dir = null;

	int max_g_id = -1;
	int max_level = -1;

	String tname;
	public Parameters execute(Element eCmd, IRequestHandler _rh) throws Exception {

 		/*[requo-timer]*/
		/*[requo-customer]*/ 
		
		rh = _rh;
		itask = this;
		if ( singelton_running )
			throw new Exception("Singelton Aggregate is already running :-((");
		singelton_running = true;
		
		if ( eCmd.getAttribute("scan")== null
			|| eCmd.getAttribute("org")== null
			|| eCmd.getAttribute("out")== null)
			throw new Exception("Define attributes: scan, org, out");
		
		String scan_string = eCmd.getAttributeValue("scan");
		String org_string = eCmd.getAttributeValue("org");
		String out_string = eCmd.getAttributeValue("out");
		
		Element orgRoot = CommonsVfs.getXmlRootOfFile(org_string,rh.getSitzung());
		
		if ( orgRoot.getAttribute("max_g_id")==null)
			throw new Exception("Missing attriubte @max_g_id in root of: "+org_string);
		max_g_id = string2int(orgRoot.getAttributeValue("max_g_id"));
		
		if ( orgRoot.getAttribute("max_id")==null)
			throw new Exception("Missing attriubte @max_id in root of: "+org_string);
		
		AggDir.setMaxId(string2int(orgRoot.getAttributeValue("max_id"))+1,max_g_id);
		
		orgRoot = null;
		
		Document scanDocument = CommonsVfs.getXmldocumentByFilename(scan_string,rh.getSitzung());
		Element scanRoot = scanDocument.getRootElement();
		max_level = string2int(scanRoot.getAttributeValue("max_level"));
		s_out_dir = null;
		if ( eCmd.getAttribute("out_dir")!=null)
			s_out_dir = eCmd.getAttributeValue("out_dir");
		aggregateDirs ( scanRoot,1);
		
		CommonsVfs.writeToFile(scanDocument, out_string,rh.getSitzung());
		//lc.log_ext("Written Aggretations to: "+out_string, this);
				
		
		rh = null;
		itask = null;		
		return null;
	}

	AggDir[] aggregateDirs ( Element dirElement, int current_level ) throws Exception {
		AggDir agg_dir0 = new AggDir();
		
		List<Element> fileChilds = dirElement.getChildren("file");
		Iterator<Element> fileIt = fileChilds.iterator();
		while ( fileIt.hasNext()) {
			Element curFile = (Element)fileIt.next();
			agg_dir0.addFile(curFile);
		}
		int requested_size = max_level+1-current_level;
		
		AggDir mother_dirs[] = new AggDir[requested_size];
		mother_dirs[0]=agg_dir0;
		
		for ( int k=1;k<requested_size;k++) {
			mother_dirs[k] =new AggDir();
		}
		
		
		List<Element> dirChilds = dirElement.getChildren("dir");
		Iterator<Element> dirIt = dirChilds.iterator();
		while ( dirIt.hasNext()) {
			Element curDir = (Element)dirIt.next();
			AggDir[] child_dirs = aggregateDirs(curDir,current_level+1);
			int target_position = 1;
			for ( int i=0;i<child_dirs.length;i++) {	

				mother_dirs[target_position].addDir(child_dirs[i]);
				
				target_position++;
			}			
		}	

		Element to_write = null;
		String new_file_name = AggregateScan.s_out_dir+"index_"+dirElement.getAttributeValue("id")+".xml";
		if ( AggregateScan.s_out_dir != null ) {
			to_write = new Element("index");
			to_write.setAttribute("dir_id", dirElement.getAttributeValue("id"));			
		}

		
		AggDir out = new AggDir();
		for ( int j=0;j<mother_dirs.length;j++) {
			out.addDir(mother_dirs[j]);
			out.updateElement(dirElement, current_level+j,j,to_write);
			
		}
		if ( AggregateScan.s_out_dir != null ) {
			Document doc_to_write = new Document(to_write);
			CommonsVfs.writeToFile(doc_to_write, new_file_name,rh.getSitzung());			
		}
		
		return mother_dirs;
		
	}
	

	
	public static int string2int ( String in ) {
			return Integer.parseInt(in);		
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rhs) {
		// TODO Auto-generated method stub
		this.tname = id;
		
	}		

}