package at.nicolas.x.task_quickscan;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;

public class FlexSpecsBundle {
	private static boolean print = true;
	Element es;
	private ArrayList<SpecDir> listSpecDir = new ArrayList<SpecDir>();
	private ArrayList<SpecFile> listSpecFile = new ArrayList<SpecFile>();
	
	public boolean hasEvidentDirSpec(
			DirFileProps dfp)
			 {
		if ( print ) {
			Sp.p("FlexSpecsBundle.hasEvidenDirSpec:");
			Sp.p("   compare iPath   = "+dfp.compareIPath);
			Sp.p("   compare rPath   = "+dfp.compareRPath);

			Sp.p("   compare pPath    = "+dfp.comparePPath);			
			Sp.p("   compare Depth   = "+dfp.compareDepth);
		}
		Iterator<SpecDir> i = listSpecDir.iterator();
		boolean found = false;
		while ( i.hasNext() && !found ) {
			
			SpecDir sd = (SpecDir)i.next();
			if ( sd.fullfillsCriterias(dfp)) {
				found = true;
			}
		}
		return found;
		
	 }	
	public boolean hasEvidentFileSpec(
			DirFileProps dfp)
			 {
		if ( print ) {
			Sp.p("FlexSpecsBundle.hasEvidenFileSpec:");
			Sp.p("   compare iPath   = "+dfp.compareIPath);
			Sp.p("   compare rPath   = "+dfp.compareRPath);
			Sp.p("   compare pPath   = "+dfp.comparePPath);
			

			Sp.p("   compare Depth   = "+dfp.compareDepth);
			
			Sp.p("   compareFileBase = "+dfp.compareFileBase);
			Sp.p("   compareFileName = "+dfp.compareFileName);
			Sp.p("   compareFileDot  = "+dfp.compareFileDot);
			Sp.p("   compareFileDot  = "+dfp.compareFileDot);			
		}
		Iterator<SpecFile> i = listSpecFile.iterator();
		boolean found = false;
		while ( i.hasNext() && !found ) {
			
			SpecFile sd = (SpecFile)i.next();
			if ( sd.fullfillsFileCriterias(dfp)) {
				found = true;
			}
		}
		return found;
		
	 }		
	FlexSpecsBundle ( Element ex) {
		// JOptionPane.showMessageDialog(null, "FlexSpecsBundle "+Commons.toText(ex));
		es = ex;
		Iterator<Element> it = es.getChildren().iterator();
		while ( it.hasNext() ) {
			Element thisElement = (Element)it.next();
			if ( thisElement.getName().equals("dir")) {
				/*
				SpecDir sd = new SpecDir(thisElement);
				listSpecDir.add(sd);
				Sp.p("Added [ dir ] to specsBundle: "+sd.getPathDocu());
				*/
				// JOptionPane.showMessageDialog(null, "added DIR to FlexSpecsBundle "+sd.getPathDocu());
			} else {
				// file
				SpecFile sf = new SpecFile(thisElement);
				listSpecFile.add(sf);
				Sp.p("Added [ file ] to specsBundle: "+sf.getFileDocu()+" " + sf.getPathDocu());
				// JOptionPane.showMessageDialog(null, "added FiLE to FlexSpecsBundle "+sf.getFileDocu()+" " + sf.getPathDocu());			
			}
		}
		
	}

}
