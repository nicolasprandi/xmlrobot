package at.nicolas.x.task_quickscan;

import org.jdom2.Element;

public class SpecDir extends SpecPath {

	public SpecDir(Element _pathSpec) {
		super(_pathSpec);
		// TODO Auto-generated constructor stub
	}
	
	public boolean fullfillsCriterias(DirFileProps dfp){	
		
		return fullfillsDirCriterias(dfp);
		
	}

}
