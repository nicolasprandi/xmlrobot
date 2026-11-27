package at.nicolas.x.task_quickscan;

import javax.swing.JOptionPane;

import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;

public class FlexSpecs implements FileSelector {
	Element es;
	FlexSpecsBundle selectBundle = null;
	FlexSpecsBundle exceptBundle = null;
	int    level_limit = -1;
	
	static boolean print = false;
	
	private static boolean ask ( String message ) {
		int answer3 = JOptionPane.showConfirmDialog(null, message);
		if ( answer3== JOptionPane.OK_OPTION) 
			return true;
		else
			return false;
	}

	
	public FlexSpecs ( Element ex) {
		// JOptionPane.showMessageDialog(null, "FlexSpecs "+Commons.toText(ex));
		es = ex;
		if ( es.getAttribute("level_limit") != null ) {
			this.level_limit = Commons.string2int(es.getAttributeValue("level_limit"));
		}
		/*
		if ( level_limit_txt != null && level_limit_txt.length() > 0 ) {

				this.level_limit = ;
		}
		*/
		if ( es.getChild("select") != null )
			selectBundle = new FlexSpecsBundle(es.getChild("select"));
		else {
			if ( print )
			Sp.p("-- no select found --");
		}
		
		if ( es.getChild("except") != null )
			exceptBundle = new FlexSpecsBundle(es.getChild("except"));
		else {
			if ( print ) 
			Sp.p("-- no except found --");
		}
		
	}

	@Override
	public boolean includeFile(FileSelectInfo arg0) throws Exception {
		// return ask("includeFile: "+arg0.getFile().getName().getURI());
		
		if ( level_limit > -1 && arg0.getDepth() > level_limit)
			return false;
		if ( arg0.getFile().isFolder())
			return true;
		
		
		
		if ( print ) {
			Sp.p("Was asked for  [ includeFile ] ...... : "+arg0.getFile().getName().getBaseName());
			Sp.p("depth="+arg0.getDepth());			
		}

		DirFileProps props = new DirFileProps(arg0);
		
		boolean ans=false;
		if ( props.isFolder ) {
			// folder
			
			if ( exceptBundle != null && selectBundle == null ) {
				boolean isExcepted = 
						exceptBundle.hasEvidentDirSpec(props);
				ans= !isExcepted;
				
			} else if ( selectBundle != null && exceptBundle == null ) {
				boolean isSelected = 
						selectBundle.hasEvidentDirSpec(props);
				ans=isSelected;			
			}  else if ( selectBundle != null && exceptBundle != null ) {
				boolean isSelected = 
						selectBundle.hasEvidentDirSpec(props);
				boolean isExcepted = 
						exceptBundle.hasEvidentDirSpec(props);			
				ans=(isSelected && !isExcepted);			
			} else 
				ans=true;
		
		} else {
			// file
			if ( exceptBundle != null && selectBundle == null ) {
				boolean isExcepted = 
						exceptBundle.hasEvidentFileSpec(props);
				ans= !isExcepted;
				
			} else if ( selectBundle != null && exceptBundle == null ) {
				boolean isSelected = 
						selectBundle.hasEvidentFileSpec(props);
				ans=isSelected;			
			}  else if ( selectBundle != null && exceptBundle != null ) {
				boolean isSelected = 
						selectBundle.hasEvidentFileSpec(props);
				boolean isExcepted = 
						exceptBundle.hasEvidentFileSpec(props);			
				ans=(isSelected && !isExcepted);			
			} else 
				ans=true;			
		}
		
		if ( print )Sp.p("THE ANSWER IS >> "+ans+" <<");
		return ans;			
		

		
		/*
		Sp.p("bf_name_path_decoded="+bf.getName().getPathDecoded());
		Sp.p("bf_name_path="+bf.getName().getPath());
		Sp.p("bf_is_folder="+bf.isFolder());
		Sp.p("bf_is_content_open="+bf.isContentOpen());
		*/
	}

	@Override
	public boolean traverseDescendents(FileSelectInfo arg0) throws Exception {
		// return ask("traverseDescendents: "+ arg0.getFile().getName().getURI());
		if ( print ) {
			Sp.pl("");
			Sp.pl("Was asked for [ traverseDescendetns ]");
			Sp.pl("       folder= [ "+arg0.getFile().getName().getURI()+" ] ");
			Sp.pl("       depth= [ "+arg0.getDepth()+" ] ");			
		}
		boolean ret;
		if ( arg0.getDepth() == 0 )
			ret= true;
		else if ( level_limit > -1 && arg0.getDepth() > level_limit) {
			if ( print )
			Sp.pl("XXXXXX level_limit="+level_limit+", depth="+arg0.getDepth());
			ret= false;
		} else
			ret= true;
		Sp.pl("       RESuLT= [ "+ret+" ] ");
		return ret;

		/*

		FileObject startFolder = arg0.getBaseFolder();
		if ( print ) {
			Sp.p("startFolder_name_basename= [ "+startFolder.getName().getBaseName()+" ] ");
			Sp.p("startFolder_name_uri="+startFolder.getName().getURI());			
		}

		



		if ( print ) {

			Sp.p("bf_name_path="+startFolder.getName().getPath());
			Sp.p("bf_is_folder="+startFolder.isFolder());
			
		}		

		FileObject directory = arg0.getFile();
		if ( print ) {
			Sp.p("fo_name_basename= [ "+directory.getName().getBaseName()+" ] ");
			Sp.p("fo_name_uri="+directory.getName().getURI());

			Sp.p("fo_name_path="+directory.getName().getPath());
			Sp.p("fo_is_folder="+directory.isFolder());
			Sp.p("fo_is_content_open="+directory.isContentOpen());				
		}		

	
		String fromPath = startFolder.getName().getPath();
		String toPath = directory.getName().getPath();
		String rel1 = toPath.substring(fromPath.length());
		if ( print ) Sp.p("rel1 = [ "+rel1+"] ");
		String dasWeg = directory.getName().getBaseName();
		if ( print ) Sp.p("dasWeg="+dasWeg);
		String rel2 = null;
		if ( rel1.length()>0)
			rel2 = rel1.substring(0, rel1.length()-dasWeg.length());
		else
			rel2=rel1;
		if ( print ) Sp.p("rel2 = [ "+rel2+"] ");
		
		String rel3 = rel2;
		if ( rel3 != null && rel3.length()>0 && rel3.startsWith("/"))
			rel3 = rel3.substring(1);
		if ( print ) Sp.p("rel3 = [ "+rel3+"] ");
		
		String compareDirBasi = directory.getName().getBaseName()+"/";
		String compareDirBase = directory.getName().getBaseName();		
		String comparePath = rel3;
		String compareRoot = startFolder.getName().getURI()+"/";
		if ( print ) {
			Sp.p("[ DirBase ] = [ "+compareDirBase+" ] ");			
			Sp.p("[ DirBasi ] = [ "+compareDirBasi+" ] ");		
			Sp.p("[ path ]    = [ "+comparePath+"] ");		
			Sp.p("[ root ]    = [ "+compareRoot+" ] ");
			Sp.p("[ depth ]   = [ "+arg0.getDepth()+" ] ");			
		}			
		*/

			


		
		/*
		 * pruefe ob dieses directory moeglich ist
		 * wenn ein select existiert muesste geschaut werden ob es iregndjemand selektiert
		 * 
		 * : dir is_in_selection
		 * 
		 * : dir is_in_exception
		 * 
		 * wenn ein excpept vorhanden ist wird geschaut ob es im exception selection wird
		 */
		/* Sp.p("bf_="+bf.getName().
		Sp.p("bf_="+bf);
		Sp.p("bf_="+bf);
		Sp.p("bf_="+bf);		*/
		
		// TODO Auto-generated method stub
		
	}

}
