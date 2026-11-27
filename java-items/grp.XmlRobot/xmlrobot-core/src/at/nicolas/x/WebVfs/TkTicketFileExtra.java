package at.nicolas.x.WebVfs;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.SurrSelInfo;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.ISitzung;

public class TkTicketFileExtra extends TkTicket {

	public boolean isFile = false;
	SurrSelInfo ssi = null;
	String ticketIdHelper = null;
	String ticketIdHelperSave = null;


	boolean chooseMode = true;
	public String base = null;
	private String x2 = null;
	public boolean forceExtension = false;
	public boolean allowsUpload = false;
	public String extension = null;
	public String mustHaveExtensions[] = null;
	String myprojectdir=null;
	public boolean isSave = false;	
	 ISitzung sitzung;
	 
	public String getX2() {
		return x2;
	}
	
	public TkTicketFileExtra(String tktype, String tip, String label, String regex, String regex_msg,
			String var_type, String var_id, String follow_job, String test_dyn_expression, String msg, boolean has_nono,
			Interpreter interpreter, Element spec, ITiHandler tiHandler) {
		super(tktype, tip, label, regex, regex_msg, var_type, var_id, follow_job, test_dyn_expression, msg, has_nono,
				interpreter, spec, tiHandler);
		// TODO Auto-generated constructor stub
		// if .isSave
		if ( tktype.equals("file") ) isFile=true;
		else						 isFile=false;
		isSave = Commons.hasFlag(spec, "save");
		this.sitzung = tiHandler.getSitzung();
		
		if      ( isFile && !isSave ) ssi = tiHandler.getSurrounding().selInfoReadFile;
		else if ( isFile && isSave ) ssi = tiHandler.getSurrounding().selInfoWriteFile;
		else if ( !isFile && !isSave ) ssi = tiHandler.getSurrounding().selInfoReadDir;
		else                           ssi = tiHandler.getSurrounding().selInfoWriteDir;
		Sp.p("TkTicketFileExtra.ssi ("+var_type+","+var_id+")="+ssi.getDescription());

		try {
			// chooseMode = interpreter.getTerminator().getFileChoose();
			if ( isFile)
				chooseMode = interpreter.getGuiController().chooseFileOn();
			else
				chooseMode = interpreter.getGuiController().chooseDirOn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}

		if ( spec.getAttribute("base") != null ) {
			base = spec.getAttributeValue("base");
		}

		
		if ( spec.getAttribute("x2") != null ) {
			x2 = spec.getAttributeValue("x2");
		}
		
		//Sp.p("reseived x2="+x2);	
		
		forceExtension = Commons.hasFlag(spec, "force_extension");
		extension = spec.getAttributeValue("extension");
	
		if ( forceExtension 
				&& extension!=null ) {
			mustHaveExtensions = extension.toUpperCase().split("\\s+");
		}		
		try {
			myprojectdir = tiHandler.getProDir();
		} catch (Exception e) {
			myprojectdir=null;
			Sp.pe(e);
		}
		/*
		System.out.println("+ + + + ");
		System.out.println("+ + + + = isFile ="+isFile);
		System.out.println("+ + + + = isSave ="+isSave);
		System.out.println("+ + + + = tiHandler.getSitzung().getUserInfo() ="+tiHandler.getSitzung().getUserInfo());
		System.out.println("+ + + + = tiHandler.getSitzung().hasPriv(uploadall) ="+tiHandler.getSitzung().hasPriv("uploadall"));
		System.out.println("+ + + + = tiHandler.getSitzung().hasPriv(upload) ="+tiHandler.getSitzung().hasPriv("upload"));
		System.out.println("+ + + + = Commons.hasFlag(spec, upload) ="+Commons.hasFlag(spec, "upload"));
		System.out.println("+ + + + ");		
		*/
		if ( isFile && 
			 !isSave && 
			 // tiHandler.getSitzung().getUserInfo()!=null &&
			 ( tiHandler.getSitzung().hasPriv("uploadall") ||
			   ( tiHandler.getSitzung().hasPriv("upload") && Commons.hasFlag(spec, "upload")) ))
			allowsUpload = true;
		buildMap(tiHandler.getSurrounding(), tiHandler);
	}
	
	HashMap<String, FileEntry> map = null;
	String firstEntryInMapHomeDir = null;
	String proProvEgoDir = null;
	String proProvTitel = null;
	public void reflectToMemory(IInputRequestor request) throws Exception {
		Sp.p("Bin im Reflect to Memory isSave="+isSave);
		if ( isSave ) reflectToMemoryRead(request);
		else		  reflectToMemoryRead(request);		
		
	}
	
	public void reflectToMemoryWrite(IInputRequestor request) throws Exception {
		/*
		String valueFromForm = request.getParameter(ticketId);
		if (valueFromForm == null)
			valueFromForm ="";
		String nextValue ;
		if ( valueFromForm.equals(""))
			nextValue = "";
		else {
			nextValue = tiHandler.getMyFileResultsDir()+valueFromForm;
		}	
		

		reflectToMemo(var_type, var_id,nextValue);
		*/
		
	}	

	public void reflectToMemoryRead(IInputRequestor request) throws Exception {
			Sp.p("TkTicketFileExtra:Bin im Reflect to Memory READ fuer var_id = [ [ "+var_id+" ] ]");

			// String parametername = ticketId;
			String oldValue = getVarFromMemoryN();
			String valueFromForm = null;
			if ( ssi.enterAllManually)
				valueFromForm = request.getParameter(ticketId);
			else if ( oldValue != null)
				valueFromForm = oldValue;
			
			if (valueFromForm == null)
				valueFromForm ="";
			Sp.p("valueFromForm = ["+valueFromForm+"]");
			String valueFromSelection = null;
			if ( ticketIdHelper != null ) {
				String helperValue = request.getParameter(ticketIdHelper);
				Sp.p("helperValue = ["+helperValue+"]");
				if ( helperValue == null )
					valueFromSelection = null;
				else if ( helperValue.equals("sys_empty"))
					valueFromSelection = "";
				else if ( helperValue.equals("sys_spec"))
					valueFromSelection = null;
				else if ( helperValue.equals("sys_spec2") ) { // && firstEntryInMapHomeDir != null) {
					String valueFromInput2 = request.getParameter(ticketIdHelperSave); // map.get// ((FileEntry)map.get(ticketIdHelperSave)).uri;
					if ( valueFromInput2 == null ) {
						valueFromSelection = null;
					} else {

						if ( valueFromInput2.startsWith("/") || valueFromInput2.startsWith("\\") )
							valueFromInput2 = valueFromInput2.substring(1);
						Sp.p("valueFromInput2 = ["+valueFromInput2+"] home=["+firstEntryInMapHomeDir+"]");
						if ( valueFromInput2.length() > 0 ) {
							valueFromSelection = proProvEgoDir+valueFromInput2;
							// valueFromSelection = firstEntryInMapHomeDir+"/"+valueFromInput2;
							if ( !isFile && !valueFromSelection.endsWith("/"))
								valueFromSelection = valueFromSelection+"/";		
							else if ( isFile && (valueFromSelection.endsWith("/") || 
									valueFromSelection.endsWith("\\")))
								valueFromSelection= valueFromSelection.substring(0, valueFromSelection.length()-1);
						} else {
							valueFromSelection=null;
						}
						
						
					}

				} else {
					valueFromSelection = ((FileEntry)map.get(helperValue)).uri;
				}				
			}
			Sp.p("valueFromSelection = ["+valueFromSelection+"]");
			String nextValue ;
			if ( valueFromSelection == null ) {
				nextValue = valueFromForm;
			} else
				nextValue = valueFromSelection;
			Sp.p("finally nextValue = ["+nextValue+"]");
			/*
			if ( valueFromForm.equals("sys_empty"))
				nextValue = "";
			else {
				nextValue = ((FileEntry)map.get(valueFromForm)).uri;
			}
			
					
					
			Sp.p(" found selection '"+valueFromForm+","+valueFromSelection+" for para '"+ticketId+"' which is value "+nextValue);
*/
			
			Sp.p("Reflecting to memory "+var_type+","+var_id+"="+nextValue);
	
			reflectToMemo(var_type, var_id,nextValue);
	

	}		
	public void printHtmlDiv ( PrintWriter out,
			boolean autofocus) throws Exception {
		printHtmlDivRead(out,autofocus,false);	
	}

	public void printHtmlDivEditorOnly ( 
			PrintWriter out,
			boolean autofocus) throws Exception {
		printHtmlDivRead(out,autofocus,true);	
	}	
	
	


	public void printHtmlDivReadManually (
			PrintWriter out,
			boolean autofocus,
			String value
			) throws Exception {
		
		if ( myprojectdir != null ) {
			// out.println("<div>"+"PROJECT @ "+html(myprojectdir)+"</div>");	
		}
		

		out.println("<input size=\""+tiHandler.getSurrounding().getDefaultFileEnterSize()+"\" type=\""+"text"+"\""+
		" name=\""+ticketId+"\""+
		" type=\"text\"");
		
		if ( value!=null ) {
			out.println(" value=\""+htmlByEscaped(value)+"\"");
		}

		if ( autofocus ) 
			out.println(" autofocus ");
		if ( !ssi.enterAllManually)
			out.println(" readonly ");
		
		out.println(" />"); // end of input		
		if ( ssi.chooseTk ) {
			String chosserText;
			if ( isFile )
				chosserText = lang.getEeFfChooseFile();
			else
				chosserText = lang.getEeFfChooseDir();
			

			
			out.println("<div class='tkChooseButton'><a href='tkWishChoose?ticket="+ticketId+"'>"+chosserText+"</a></div>");
			if ( allowsUpload )
				out.println("<div class='tkUploadButton'><a href='tkWishUpload?ticket="+ticketId+"'>"+lang.getEeFfUpload()+"</a></div>");
			out.print("</div>");				
		}		
	}
			
	public void printHtmlDivRead (
			PrintWriter out,
			boolean autofocus,
			boolean editorOnly
			) throws Exception {
			// boolean didAutofocus = false;
					
			String value = getVarFromMemoryN();
			Sp.p("X - 1");

			if (!editorOnly) {
			out.print("<div class='tkMmbrOBACHT'>");
			
			out.print("<div class='tkMmbrLabelOBACHT'>"+getLabelHtml()+
			"</div>");
			}

			// Start direkt fileinput
			
			
			// target=\"_blank\"

			ticketIdHelperSave = null;
			
			/*if (map.isEmpty()&& firstEntryInMapHomeDir==null ) {
				printHtmlDivReadManually(out,autofocus,value);
				Sp.p("X - 2");
				ticketIdHelper = null;
			} else 
			*/{
				Sp.p("X - 3, firstEntryInMapHomeDir="+firstEntryInMapHomeDir);
				ticketIdHelper = ticketId+"Radio";
				out.print("<table>");
				boolean didCheckAlready = false;
				if ( ssi.enterAllManually || ( ssi.enterConfirmOnly && value != null && value.length()>0 )) {
					out.print("<tr><td class='tdTkFileSelRadio'>");
					out.print("<input type=\"radio\" name=\""+ticketIdHelper
							+"\" value=\""+"sys_spec"+"\"");	
					if ( !value.equals("")) {
						out.print(" checked ");
						didCheckAlready = true;
						if ( autofocus )
							out.println(" autofocus ");
					}				
					out.print(">");
					
					out.print("</td>"); 
					// erste spalte ist jetzt fertig
					// es fengt die zweite zeile an
					/*
					out.print("<td class='tdTkFileSelValue'>");
					out.print("Uri:");
					out.print("</td>");
					out.print("</tr>");
					out.print("<tr>"); <td class='tdTkFileSelRadio'></td>
					*/
					out.print("<td class='tdTkFileSelValue'>");
					printHtmlDivReadManually(out,autofocus,value);
					out.print("</td></tr>");
				}
				if ( isSave && proProvEgoDir != null) {
					ticketIdHelperSave = ticketId+"RadioSave";
					out.print("<tr><td class='tdTkFileSelRadio'>");
					out.print("<input type=\"radio\" name=\""+ticketIdHelper
							+"\" value=\""+"sys_spec2"+"\"");
					/*
					if ( !value.equals("")) {
						out.print(" checked ");
						didCheckAlready = true;				String proProvEgoDir = tiHandler.getProDir()+ssi.proProvPath;
				String proProvTitel = ssi.proProvPath;		
						if ( autofocus )
							out.println(" autofocus ");
					}			
					*/	
					out.print(">");
					out.print("</td><td class='tdTkFileSelValue'>");
					// out.print("Simple specification in folder '"+ html(proProvTitel)+"'");
					if ( isFile ) {
						out.print(lang.getEeFfPersonalFile());
						if ( extension != null )
							out.print(" (extension "+extension+")");
					} else
						out.print(lang.getEeFfPersonalFolder());
				
					
					// out.print("Simple specification in folder '"+ html(tiHandler.getUriPresentation(firstEntryInMapHomeDir))+"'");
					out.print("</td></tr>");
					out.print("<tr><td class='tdTkFileSelRadio'></td><td class='tdTkFileSelValue'>");
					//
					out.println("<input size=\""+tiHandler.getSurrounding().getDefaultFileNameEnterSize()+"\" type=\""+"text"+"\""+
							" name=\""+ticketIdHelperSave+"\""+
							" type=\"text\"");
					out.println(" />"); // end of input	
							/*
							if ( value!=null ) {
								out.println(" value=\""+html(value)+"\"");
							}

							if ( autofocus ) 
								out.println(" autofocus ");
							if ( !ssi.enterAllManually)
								out.println(" readonly ");
							*/
							
	
						
					//
					
					out.print("</td></tr>");					
				}

			
				String lastPrintetLocationDesc = null;
				for (Map.Entry<String, FileEntry> entry : map.entrySet())
				{
					String key = entry.getKey();
					FileEntry fe = (FileEntry)map.get(key);
					String keyUri = fe.uri;
					if ( lastPrintetLocationDesc == null || !lastPrintetLocationDesc.equals(fe.locationDesc)) {
						lastPrintetLocationDesc = fe.locationDesc;
						out.print("<tr><td colspan='2'>"+htmlByEscaped(lastPrintetLocationDesc)+"</td></tr>");
					}
					out.print("<tr><td class='tdTkFileSelRadio'>");
					out.print("</td><td class='tdTkFileSelValue'>");					
					out.print("<input type=\"radio\" name=\""+ticketIdHelper
							+"\" value=\""+htmlByEscaped(key)+"\"");
					
					if ( value.equals(keyUri)) {
						if (!didCheckAlready)
							out.print(" checked ");
						if ( autofocus )
							out.println(" autofocus ");
					}
									
					out.print(">");	

					out.print(htmlByEscaped(fe.basename)); // +" "+html(fe.locationDesc));
					out.print("</td></tr>");
				}
				
				
				if ( !has_nono ) { //&& !ssi.enterAllManually) {
					out.print("<tr><td class='tdTkFileSelRadio'>");
					out.print("<input type=\"radio\" name=\""+ticketIdHelper
							+"\" value=\""+"sys_empty"+"\"");	
					if ( value.equals("")) {
						if (!didCheckAlready)
							out.print(" checked ");
						if ( autofocus )
							out.println(" autofocus ");
					}				
					out.print(">");	
					out.print("</td><td class='tdTkFileSelValue'>");
					out.print(lang.getIgnore());
					out.print("</td></tr>");
				}		
				
				out.print("</table>");
				
			}

			/*
			if ( !has_nono) {
				out.print("<input type=\"radio\" name=\""+ticketId+"Radio"
						+"\" value=\""+"sys_empty"+"\"");	
				if ( value.equals("")) {
					out.print(" checked ");
					if ( autofocus )
						out.println(" autofocus ");
				}				
				out.print("> <br>");					
			}			
			*/
			if (!editorOnly) {			
			out.print("</div>");
			}
	}		
	public void printHtmlDivWrite ( PrintWriter out,
			boolean autofocus ) throws Exception {
		String value;
		String input;
		try {
			value = getVarFromMemoryN();
			if ( value.equals(""))
				input="";
			else {
				if ( value.endsWith("/"))
					input = Commons.getNameOfDir(value);
				else
					input = Commons.getNameOfFile(value);
			}

		} catch (Exception e) {
			value = "";
			input = "";
		}	
		
		out.print("<div class='tkMember'>");
		
		out.print("<div class='tkLabel'>"+getLabelHtml()+
		""); // = ("+var_type+","+var_id+")</div>");
		
		out.println("<input size=\"50\" type=\""+"text"+"\""+
		" name=\""+ticketId+"\""+
		" type=\"text\"");
		
		if ( input!=null ) {
			out.println(" value=\""+htmlByEscaped(input)+"\"");
		}
		if ( autofocus )
			out.println(" autofocus ");

		out.println(" />"); // end of input
		
		
		out.print("</div>");
		
	}		
	private int addMap ( String dir, int startIndex, String locationDesc, boolean takeFiles )  {
		Sp.p("Y Trying to add to map: "+dir+", "+startIndex+"--"+firstEntryInMapHomeDir);
		int myindex = startIndex;
		try {
			FileObject fo = CommonsVfs.resolveFolderSilent(dir);
			FileObject[] files = CommonsVfs.getNinos(fo,sitzung);
			if ( firstEntryInMapHomeDir == null ) {
				Sp.p("Y firstEntryInMapHomeDir is null");
				firstEntryInMapHomeDir = fo.getName().getURI();		
				Sp.p("Y firstEntryInMapHomeDir is now:"+firstEntryInMapHomeDir);
			}
			if ( files != null ) {
				for (int i=0; i<files.length; i++) {
					FileObject einFile =  files[i];
					try {
						if ( takeFiles ) {
							if ( einFile.isFile() ) {
								boolean takeit = true;
								if ( mustHaveExtensions != null) {
									takeit = false;
									for ( String mustHaveExtension: mustHaveExtensions ) {
										if ( einFile.getName().getURI().toUpperCase().endsWith(mustHaveExtension)){
											takeit = true;
											break;
										}
									}
															
								}
								if ( takeit ) {
									FileEntry fe = new FileEntry(
											einFile.getName().getBaseName(),
											einFile.getName().getURI(),
											locationDesc);

									map.put(Commons.int2String(myindex), fe);
									myindex++;								
								}

							}							
						} else {
							// directories
							if ( einFile.isFolder() ) {
								FileEntry fe = new FileEntry(
										einFile.getName().getBaseName(),
										einFile.getName().getURI()+"/",
										locationDesc);
								
								map.put(Commons.int2String(myindex), fe);
								myindex++;									
								
							}
						}

					} catch (FileSystemException e) {
						// TODO Auto-generated catch block
						Sp.p("Problem with: file "+einFile.getPublicURIString());
						Sp.pe(e);
					}
				}
				return myindex;
			} else {
				// aha
				Sp.p("Problem with: dir "+dir);
				return startIndex;
			}
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			Sp.p("Problem in TkTicketFileExtension, problem with directory "+dir);
			Sp.pe(e);
			return startIndex;
		}
		
	}
	private void buildMap(Surrounding surrounding, ITiHandler tiHandler) {
		map = new HashMap<String, FileEntry> ();
		firstEntryInMapHomeDir = null;
		int nextIndex = 0;
		

		if ( ssi.hasProProv ) {

			try {
				 proProvEgoDir = tiHandler.getProDir()+ssi.proProvPath;
				 proProvTitel = ssi.proProvPath;				
				nextIndex = addMap(tiHandler.getProDir()+ssi.proProvPath, nextIndex,lang.getEeFfInYourProject(),isFile);
			} catch (Exception e) {
				Sp.p("Problem in TkTicketFileExtension, problem getting the prodir ");
				Sp.pe(e);
			}			
		}	
		if ( ssi.hasAppProv  ) {
			try {
				nextIndex = addMap(tiHandler.getAppDir()+ssi.appProvPath, nextIndex,lang.getEeFfInYourApp(),isFile);
			} catch (Exception e) {
				Sp.p("Problem in TkTicketFileExtension, problem getting the appdir ");
				Sp.pe(e);
			}			
		}		
			
		
	}		
	
	private class FileEntry    {
		String basename;
		String uri;
		String locationDesc;
		FileEntry(String basename, String uri, String locationDesc ) {
			this.basename = basename;
			this.uri = uri;
			this.locationDesc = locationDesc;
			Sp.p("created FileEntry "+basename+", "+uri+", "+locationDesc);
		}
		
	}		

}
