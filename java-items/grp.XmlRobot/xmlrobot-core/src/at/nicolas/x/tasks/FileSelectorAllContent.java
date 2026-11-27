package at.nicolas.x.tasks;

import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

public class FileSelectorAllContent implements FileSelector {

	public FileSelectorAllContent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean includeFile(FileSelectInfo arg0) throws Exception {
		/*
		Sp.p("FileSelectorAll.includeFile() depth="+arg0.getDepth()
			+", getBaseFolder="+arg0.getBaseFolder().getPublicURIString()
			+", getFile="+arg0.getFile().getPublicURIString()+" == "+(arg0.getDepth() > 0));
			*/
		return (arg0.getDepth() > 0);
	}

	@Override
	public boolean traverseDescendents(FileSelectInfo arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
