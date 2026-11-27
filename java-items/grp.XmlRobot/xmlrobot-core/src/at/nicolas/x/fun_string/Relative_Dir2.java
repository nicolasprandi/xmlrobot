/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;
import java.util.ArrayList;

import org.apache.commons.vfs2.FileName;
import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Relative_Dir2 implements IFun {

	private static final String funn = "relative_dir";
	private static final String pm = "function 'relative_dir'";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {


		if ( vcar_list.size() !=2 ) 
			throw new LangException(pm+"wants 2 parameters: 1=Dir, 2=Dir");

		String   a = ((Vcar)vcar_list.get(0)).s;
		String   b = ((Vcar)vcar_list.get(1)).s;

		String res = convertToRelativePathWrapper(b,a,rh);
		if ( res == null )
			throw new LangException("no relative dir available");

		return (new Vcar(res));
	}

	public String getExtensionId() {
		return funn;
	}

	private static String convertToRelativePath(String absolutePath, String relativeTo, IRequestHandler rh) {
		StringBuilder relativePath = null;


		absolutePath = absolutePath.replaceAll("\\\\", "/"); 
		relativeTo = relativeTo.replaceAll("\\\\", "/");
		

		if (absolutePath.equals(relativeTo) == true) {

		} else {

			
			if ( rh.isMac() || rh.isLinux()  ) {
				if (absolutePath.startsWith("/"))
					absolutePath = absolutePath.substring(1); 
				if (relativeTo.startsWith("/"))
					relativeTo = relativeTo.substring(1);
			}
			
			String[] absoluteDirectories = absolutePath.split("/");
			String[] relativeDirectories = relativeTo.split("/");
			
			if ( rh.isMac() ) {
				// lc.error("rd received: "+relativeTo +" - "+absolutePath);
				if ( absoluteDirectories.length>=2 && relativeDirectories.length>=2 ) {
					// lc.error("rd received absolut: "+absoluteDirectories[0]+" + "+absoluteDirectories[1]);
					// lc.error("rd received relativ: "+relativeDirectories[0]+" + "+relativeDirectories[1]);
					if ( absoluteDirectories[0].equals("Volumes") &&
						 relativeDirectories[0].equals("Volumes") &&
						 (!absoluteDirectories[1].equals(relativeDirectories[1])) ) {
						// in this case we have different volumes
						return null;
					}
				}
			} else if ( rh.isLinux() ) {
				// lc.error("rd received: "+relativeTo +" - "+absolutePath);
				if ( absoluteDirectories.length>=2 && relativeDirectories.length>=2 ) {
					// lc.error("rd received absolut: "+absoluteDirectories[0]+" + "+absoluteDirectories[1]);
					// lc.error("rd received relativ: "+relativeDirectories[0]+" + "+relativeDirectories[1]);
					if ( absoluteDirectories[0].equals("media") &&
						 relativeDirectories[0].equals("media") &&
						 (!absoluteDirectories[1].equals(relativeDirectories[1])) ) {
						// in this case we have different volumes
						return null;
					}
				}
			}
			
			//Get the shortest of the two paths
			int length = absoluteDirectories.length < relativeDirectories.length ? 
					absoluteDirectories.length : relativeDirectories.length;

			//Use to determine where in the loop we 
			int lastCommonRoot = -1;
			int index;

			//Find common root
			for (index = 0; index < length; index++) {
				if (absoluteDirectories[index].equals(relativeDirectories[index])) {
					lastCommonRoot = index;
				} else {
					break;
					//If we didn't find a common prefix then throw
				}
			}
			
			if (lastCommonRoot != -1) {
				//Build up the relative path
				relativePath = new StringBuilder();
				//Add on the ..
				for (index = lastCommonRoot + 1; index < absoluteDirectories.length; index++) {
					if (absoluteDirectories[index].length() > 0) {
						relativePath.append("../");
				
					}
				}
				for (index = lastCommonRoot + 1; index < relativeDirectories.length; index++) {
					relativePath.append(relativeDirectories[index] + "/");
				}

			}
			else
				return null;
		} 

		return relativePath == null ? null : relativePath.toString();
	}	

	private static String convertToRelativePathWrapper(String absolutePath, String relativeTo, IRequestHandler rh) {
		if ( absolutePath.contentEquals(relativeTo)) {
			return "";
		} else {
			String ap = absolutePath;
			if ( ap.endsWith("/") || ap.endsWith("\\"))
				ap = ap.substring(0, ap.length()-1);
			if ( ap.endsWith("/") || ap.endsWith("\\"))
				ap = ap.substring(0, ap.length()-1);
			
			String rp = relativeTo;
			if ( rp.endsWith("/") || rp.endsWith("\\"))
				rp = rp.substring(0, rp.length()-1);
			if ( rp.endsWith("/") || rp.endsWith("\\"))
				rp = rp.substring(0, rp.length()-1);
			
			String res = convertToRelativePath(ap,rp,rh);

			if ( res != null && FileName.SEPARATOR.equals("\\"))			
				res = res.replaceAll("/","\\\\" );

			return res;			
		}



	}


	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	

}