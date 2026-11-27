/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import java.io.File;
//zum anzeigen eines bildes, muss f�r webmode noch ge�ndert werden
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdom2.Element;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.int_call.AppJobexecutionUsage;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_tk.Tester;

public class Ti_TerminalLinkRec extends Ti_TerminalLink {

	Element eE;
	private static final int TYPE_TEXT = 1;
	private static final int TYPE_CALL_SERVICE = 2;
	private static final int TYPE_CALL_JOB = 3;
	private static final int TYPE_FILE = 4;
	private static final int TYPE_DIR = 5;
	private static final int TYPE_IMG = 6;
	private static final int TYPE_UL = 7;
	private static final int TYPE_MENU = 8;
	private static final int TYPE_WEB = 9;
	
	private static KeyWordMapper rectypes;
	public static void startupActions() {
		
		rectypes = new KeyWordMapper("TiRecTypes",9);
		rectypes.put("text", TYPE_TEXT);
		rectypes.put("textblock", TYPE_TEXT);
		rectypes.put("call_service", TYPE_CALL_SERVICE);
		rectypes.put("call_job", TYPE_CALL_JOB);
		rectypes.put("dir", TYPE_DIR);
		rectypes.put("file", TYPE_FILE);
		rectypes.put("img", TYPE_IMG);
		rectypes.put("ul", TYPE_UL);
		rectypes.put("menu", TYPE_MENU);
		rectypes.put("web", TYPE_WEB);
		rectypes.putFinished();
		
	}
	
	ITiHandler rh;

	// int wrap = -1;
	LogConnector lc = new LogConnector("ti.rec");
	boolean isRec; /* true bei rec, false bei menu */
	boolean isSubMenu;
	public Ti_TerminalLinkRec(Element eE, ITerminator _terminator, String app, String appdir,
			String inst,String instdir, ITiHandler rh, boolean isRec, boolean isSubMenu ) {
		super(eE, _terminator, app, appdir,inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE; 
		this.rh = rh;
		this.isRec = isRec;
		this.isSubMenu = isSubMenu;
		// System.out.println(" === rec-or-menu isRec= " + isRec);
		
		// Sp.p("* * * * Ti_TerminalLink REC:"+Commons.toText(e));

	}

	private String lableText= null;
	public static int getIndexDotExtension(String filename ) {
		File f = new File(filename);
		
		String name = f.getName();
		int index = name.indexOf(".");
		if ( index == -1 ) return -1;
		return ( filename.length()-name.length()+index);
		
	}
	public Ti_HtmlReturn getHtml() {
		try {
			return getHtmlCatched();
		} catch (Exception e) {
			new Ti_HtmlReturn(e.getMessage());
	
		}
		return new Ti_HtmlReturn();
	}
	
	
	private static String lastRecColumnHtmlAttrStyleGeneral = "style=\"width: 100%;\" ";

	public Ti_HtmlReturn getHtmlCatched() throws Exception {
		/*divs*/
		//boolean divs = Commons.hasFlag(eE, "divs");
		
		lableText = "";
		String kopf="";
		List<Element> l_jobchilds = eE.getChildren();
		
		int i = 0;
		int j = 0;
		
		Ti_HtmlReturn ret = new Ti_HtmlReturn();
		if ( eE.getAttribute("label") != null ) {
			// 7.4.2019 ret.setLabel(Ti_TerminalLink.getAttUnescapedXml(eE, "label", null));
			// System.out.println("###### label ###### "+eE.getAttributeValue("label"));
			//ret.setLabel("Ll"+Ti_TerminalLink.getAttUnescapedXml(eE, "label", null));
			ret.setLabel(getHtmlByTextFromEscaped(eE.getAttributeValue("label")));
		}		
		ret.setIsNoRecMenu(false);
		/*divs*/
		ret.setRec_is_menu(!isRec);

		ret.setKeyLabelRight(eE,rh.getSitzung());
		
		// Vorpruefung
		int sum_takes = 0;
		Iterator<Element> p_jobchilds = l_jobchilds.iterator();
		while ( p_jobchilds.hasNext()) {

			Element x = (Element)p_jobchilds.next();
			// if ( x.getAttribute("test")!=null && x.getAttributeValue("test").equals("0")) 
			if ( Tester.testOrTestdynAttributeIs0(x,rh))
				x.setAttribute("takeit", "0");
			else {
				x.setAttribute("takeit", "1");
				sum_takes++;
			}
		}
		
		if ( ret.getLabel() != null ) {
			kopf = ret.getTdLabel();
			i = 1;
		}
		
		Iterator<Element> i_jobchilds = l_jobchilds.iterator();
		int taken = 0;
		ArrayList<RefCode> refcodes = new ArrayList<RefCode>();
		while ( i_jobchilds.hasNext()) {
			j++;
			Element x = (Element)i_jobchilds.next();
			

			String tdStyleLastRecord = "";	
			String tdStyleLastRecordClass = "";	

			/*
			 * Die elemente die den test bestanden haben werden weiterverarbeitet
			 */
			if ( x.getAttributeValue("takeit").equals("1") ) {
				taken++;
				String colspan="";

				
				/*
				 * Handhaben der spans
				 * -wird bei menu nicht gebraucht
				 */				
				if ( !isRec );
				else if ( isSubMenu );
				else if (taken == sum_takes ) {

					// that is the last thing to take
					// so the span will be expanded by
					//   the remaining and
					//   the key
					// stiButtonTab = "tiButtonTab2";
					// hier wird der span für das letzte rec-element ausgerechnet: colspan
					tdStyleLastRecord = lastRecColumnHtmlAttrStyleGeneral;	
					tdStyleLastRecordClass = " tiLastGridCell ";
					int orig_span = 1;
					if ( x.getAttribute("span")!=null) {
						orig_span = Commons.string2int(x.getAttributeValue("span"));
					}
					// we want now 11 or 12 in total
					// until now we have i columns
					int final_span;
					if ( ret.getKey() == null )
						final_span = 12;
					else
						final_span = 11;
					int remain_span = final_span - i;
					i = remain_span;
					if ( i<1)
						i=1;
					colspan=" colspan='"+Commons.int2String(remain_span)+"'";
					
				} else {
					// das ist ein element irgendwo dazwischen oder vielleicht sogar das erste
					if ( x.getAttribute("span")==null) {
						i++;
					} else {
						i = i + Commons.string2int(x.getAttributeValue("span"));
						colspan=" colspan='"+x.getAttributeValue("span")+"'";
					}					
				}

				
				int tup = 0;
				
				String ar = x.getAttributeValue("type");
				if ( ar == null ) ar="text";
				if ( x.getAttribute("id") != null)
					ar = ar + x.getAttributeValue("id");
				else
					ar = ar + "X";
				if ( x.getAttribute("tk_id") != null)
					ar = ar + x.getAttributeValue("tk_id");
				else
					ar = ar + "Y";				
				
				
				
				if (x.getAttributeValue("type") == null )
					tup = TYPE_TEXT;
				else
					tup = rectypes.convert(x.getAttributeValue("type"));

				
				
				String dir_or_file = null;
				String extension ="";
				int punkt_pos = -1;
				
				switch ( tup ) {

			    case TYPE_FILE: {	
			    	dir_or_file = Ti_TerminalLink.getXUnescaped(x,rh);
					if (!Commons.hasFlag(x, "no_can"))
						try {
							dir_or_file = Commons.constructNormalizedFile(dir_or_file);
						} catch (Exception e1) {
							Sp.p("schaut nach cano-problem aus ["+dir_or_file+"]");
							dir_or_file = Ti_TerminalLink.getXUnescaped(x,rh);	
						}				
					dir_or_file = Xml(dir_or_file);
					punkt_pos = getIndexDotExtension(dir_or_file);
					if (punkt_pos > -1)
						extension = dir_or_file.substring(punkt_pos);	
			    	break;
					}  
			    case TYPE_DIR: {
					dir_or_file = Ti_TerminalLink.getXUnescaped(x,rh);	

					if (!Commons.hasFlag(x, "no_can"))
						try {
							dir_or_file = Commons.constructNormalizedDir(dir_or_file);
						} catch (Exception e1) {
							Sp.p("schaut nach cano-problem aus ["+dir_or_file+"]");
							dir_or_file = Ti_TerminalLink.getXUnescaped(x,rh);	
						}
					dir_or_file = Xml(dir_or_file);			    	
						break;
					}                     
			    }
												
				String myLabel = null;
				LinkTextResponse linkTextResponse = null;
				
				switch ( tup ) {
			    case TYPE_TEXT: {	
					linkTextResponse = getHtmlLinktextFromEscapedNoNbsp(getX(x),x,"tiTextTab tiTextTabGrid");
					
					myLabel = linkTextResponse.getLinktext();	
					break;
					}
			    case TYPE_CALL_SERVICE:
			    case TYPE_CALL_JOB: {	
			    	String visible = null;
					if ( x.getAttribute("pic") != null )
						visible = Ti_TerminalLinkImg.getImgHtml(x, rh);
						//visible = getAttUnescapedXml(eE,"explain",rh);
					else if ( x.getAttribute("explain") != null ) 
						visible = x.getAttributeValue("explain");
					else 
						visible = x.getAttributeValue("job"); 
					
					if ( !isRec )
						linkTextResponse = getHtmlLinktextFromEscaped(visible,x,"tiButtonMenu");
					else if ( Commons.hasFlag(x, "filler") )
						linkTextResponse = getHtmlLinktextFromEscaped(visible,x,"tiButtonTab1"); 
					else 
						linkTextResponse = getHtmlLinktextFromEscaped(visible,x,"tiButtonTab2"); 							

					myLabel = linkTextResponse.getLinktext();
						break;
					}
			     
			    case TYPE_FILE:
			    case TYPE_DIR:{	
			    	int len = 0;
					if ( x.getAttribute("len") != null )
						len = Commons.string2int(x.getAttributeValue("len"));
					
					if ( tup == TYPE_DIR ) {
						// Sp.p("-3-");
						myLabel = Commons.cutDir2(dir_or_file, len);
					} else {
						// Sp.p("-4-");
						if ( punkt_pos > 0 ) {
							// Sp.p("-5- dir_or_file:"+dir_or_file+", len="+len+", punkt_pos="+punkt_pos);
							// Sp.p("send:"+dir_or_file.substring(0,punkt_pos));
							myLabel = Commons.cutFile2(dir_or_file.substring(0,punkt_pos), len)+extension;
						} else {
							// Sp.p("-6-");
							myLabel = Commons.cutFile2(dir_or_file, len);
						}
					}
						break;
					}  
			     
			    case TYPE_IMG: {	
					/*
					String imgsrc;
					try {
						imgsrc = new File(Ti_TerminalLink.getXUnescaped(x,rh)).toURI().toURL().toExternalForm();
					} catch (MalformedURLException e) {
	
						Sp.pe(e);
						imgsrc=null;
					}*/
					// nic321 hier muss diese image html funktion verwendet werden
					//myLabel = "<img src='"+imgsrc+"'>";
					myLabel = Ti_TerminalLinkImg.getImgHtml(x, rh);
					//Sp.p("Image: "+myLabel);
					break;
					} 
			    case TYPE_UL: {	
					// myLabel = "<ul>"+Ti_TerminalLink.getMsg(x,rh)+"</ul>";
					myLabel = "&bull;&nbsp; "+getHtmlByXFromEscaped(x);
					break;
					}     
			    case TYPE_MENU: {	
			    	// das passiert nachher, braucht kein label
					break;
					}  
			    case TYPE_WEB: {
			    	linkTextResponse = getHtmlLinktextFromEscapedNoNbsp(getX(x),x,"tiWebTab tiWebTabGrid");					
					myLabel = linkTextResponse.getLinktext();
					break;
					}                           
			    
			    }				
				
				// vorbereitungsphase je typ abgeschlossen
				
				if ( linkTextResponse == null )
					linkTextResponse = new LinkTextResponse();
				
				// null verhindern
				if ( myLabel == null || myLabel.length()==0)
					myLabel="";
				
				/*
				String width="";
				if ( x.getAttributeValue("width") == null );
				else if ( x.getAttributeValue("width").endsWith("px")) 
					width = " width='"+  Tc_AppConsole.guiSizer.scalePx(x.getAttributeValue("width"))+"' " ; 
				 else
					width = " width='"+  x.getAttributeValue("width")+"' " ; 
				*/ 

				String align="";
				String alignClass="";
				if ( x.getAttribute("align")!=null && x.getAttributeValue("align").length() > 0 && !isSubMenu) {
					String alignPara = x.getAttributeValue("align");
					align = " align=\""+alignPara+"\" " ;
					if ( alignPara.equals("right"))
						alignClass=" tiCellRight ";
					else if ( alignPara.equals("center"))
						alignClass=" tiCellCenter ";					
				}				
				
				// hier wird noetigenfalls noch ein <a> um den myLabel gesetzt
				// input: myLabel
				// nun wird die variable center belegt
				String center = null;

				switch ( tup ) {
				case TYPE_IMG: 
			    case TYPE_UL:
			    case TYPE_TEXT: {
			    	center = myLabel;
					break;
				}
			    			     
			    case TYPE_MENU: {	
			    	// hier gehts los mit menu
					/* produziert wird ein 'center' dass dann in weiter folge ins td verpackt wird
					 * es wird ein neues sub TerminalLinkRec aufgerufen
					 * - dieses link-zeug wird an das htmt-result angehaengt 
					 * - und das ganze wird noch verpackt so wie das auch im terminator passiert
					 */
					Ti_TerminalLink newT2 = (Ti_TerminalLink)new Ti_TerminalLinkRec(x, terminator,app, appdir,inst,instdir,rh,false,true);
					
					// zur link liste, das wird dann im terminator zur page-list dazugehaengt
					ret.menuList.add(newT2);
					Ti_HtmlReturn hr = newT2.getHtml();
					
					// verpacken, das ist jetzt eine kopie aus dem terminator
					
					String master_class = "";
					if ( eE.getAttribute("master_class")==null )
						master_class = "appmenu";
					else if ( eE.getAttributeValue("master_class").length() == 0 ) 
						master_class = "appmenu";
					else if ( eE.getAttributeValue("master_class").trim().length()>0) {
						master_class = eE.getAttributeValue("master_class");
					}

					String theWidth = "";
					if ( eE.getAttribute("width")!=null) {
						theWidth = " width=\""+eE.getAttributeValue("width")+"\"";
					}			
					
					String theStyle = "";
					if ( eE.getAttribute("style")!=null) {
						theStyle = " style=\""+eE.getAttributeValue("style")+"\"";
					}		
					
					if ( Commons.hasFlag(eE, "block")) {
						if ( hr.rec_is_menu && terminator.isWeb())
							master_class = master_class + " blocked";
					}
					
					String theMasterClass = "";
					if ( hr.rec_is_menu && terminator.isWeb())
						theMasterClass = " class=\"tiTabFloatMaster "+master_class+"\"";
					else 
						theMasterClass = " class=\"tiTabGridMaster "+master_class+"\"";						
					
					String html; 
					if ( terminator.isWeb() ) {								
						html = "<div "+theMasterClass+">";
					} else {
						html = "<table "+theMasterClass+"><tr>";
					}

					html = html + hr.main;
					
					
					if ( terminator.isWeb() ) {
						html = html + "</div>";															
					} else {
						html = html + "</tr></table>";	
					}		
					//System.out.println("sub-menu produziert: "+html);
					center = html;
					break;
				}  
			    case TYPE_WEB: {	
			    	if ( terminator.isWeb() ) {								
						String boldClass = "";
						if ( Commons.hasFlag(x, "bold") )
							boldClass=" tiRecBoldValue ";
							
							center = "<a href='"+getHtmlByXFromEscaped(x)+"' "+
									linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getARecClass(ar)+alignClass+boldClass)
									+
									">"+ myLabel+"</a>";		
					} else {
						String boldClass = "";
						if ( Commons.hasFlag(x, "bold") )
							boldClass=" tiRecBoldValue ";
						
							center = "<a "+ts.getHref(getRefCode(j))+
									// linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getARecClass(ar)+alignClass+boldClass)
									Tc_Terminator.stil.getAnkerWebBLACK()
									+
									">"+ myLabel+"</a>";	
					}
					break;
				}                           
			    default: {
			    	if ( Commons.hasFlag(x, "no_link") || myLabel==null || myLabel.equals(""))
						center = myLabel;
					else {
						/*
						center = "<a "+ts.getHref(getRefCode(j))+
								Tc_Terminator.stil.getARec(ar)+">"+ myLabel+"</a>";
								*/
						if ( Tc_Terminator.stilIsWeb ) {
							String boldClass = "";
							if ( Commons.hasFlag(x, "bold") )
								boldClass=" tiRecBoldValue ";
								
								center = "<a "+ts.getHref(getRefCode(j))+
										linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getARecClass(ar)+alignClass+boldClass)
										+
										">"+ myLabel+"</a>";								
				
	
						} else {
							String tempLabel = myLabel;
							if ( Commons.hasFlag(x, "bold") )
								tempLabel = "<b>"+tempLabel+"</b>";
							center = "<a "+ts.getHref(getRefCode(j))+
									Tc_Terminator.stil.getARec(ar)+
									">"+ tempLabel+"</a>";		
							// menue-abtrenner-desktop&& isSubMenu tup
							center = center+"&nbsp;&nbsp;";
							/*if ( j > 1 && !isSubMenu)
								center = "&nbsp;&nbsp;"+center;*/
							// center = "&nbsp;"+tup+" "+isRec+" "+isSubMenu+"&nbsp;"+center;
						}
						//System.out.println("--HUHU ---- "+linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getARecClass(ar)));
						/*
						Sp.p("========================");
						Sp.p("========================");
						Sp.p("========================");
						Sp.p("REC-STIL="+Tc_Terminator.stil.getARec(ar));*/
						refcodes.add(new RefCode(getRefCode(j),"rec["+j+"] "+x.getAttributeValue("type")));
					}
			    	break;
			    }
			    }				
				
				
				

				if ( x.getAttribute("prefix") != null)
					center = getHtmlByTextFromEscaped(x.getAttributeValue("prefix"))+center;
				
				if ( x.getAttribute("postfix") != null)
					center = center+getHtmlByTextFromEscaped(x.getAttributeValue("postfix"));
				/*
				if ( x.getAttribute("postfix") != null) {
					String postfix_class = "";
					if ( x.getAttribute("postfix_class") != null ) {
						postfix_class = " class=\""+ x.getAttributeValue("postfix_class")+"\" ";
					}
					center = center+
							"<div "+postfix_class+">"+
							getHtmlByTextFromEscaped(x.getAttributeValue("postfix")) +
							"</div>";	
				}
				*/
				

				String nowrap =" nowrap";
				String intoStyle = "";
				

				if ( x.getAttribute("style") != null && x.getAttributeValue("style").length()>0) {
					intoStyle = x.getAttributeValue("style");
					
				}
				
				if ( Commons.hasFlag(x, "wrap")) {
					nowrap = "";
					if ( x.getAttribute("word_break") != null) {
						intoStyle = intoStyle+ "word-break:"+x.getAttributeValue("word_break")+";";
					} else {
						intoStyle = intoStyle+ "word-break:break-all;";
					}
					
				} else if ( Commons.hasFlag(x, "wrap_word")) {
					nowrap = "";
					intoStyle = intoStyle+ "word-break:break-word;";			
					
				} else if ( Commons.hasFlag(x, "wrap_normal")) {
					nowrap = "";
					intoStyle = intoStyle+ "word-break:normal;";					
				} else if ( Commons.hasFlag(x, "wrap_all")) {
					nowrap = "";
					intoStyle = intoStyle+ "word-break:break-all;";					
				} else if ( Commons.hasFlag(x, "wrap_keep")) {
					nowrap = "";
					intoStyle = intoStyle+ "word-break:keep-all;";					
				}
				
				// nun wird der 'kopf', das ist dort wo das html gesammelt wird
				// um dieses Element erweitert.
				// dh es wird ein 'td' angehaengt
				
				if ( !isRec ) {
					// menu
					if ( Tc_Terminator.stilIsWeb ) {
						String intoStyleSolo = "";
						if ( intoStyle != null) {
							intoStyleSolo="style=\""+intoStyle+"\"";
						}
						kopf = kopf + "<div "+intoStyleSolo+
								linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getDivRecClass(ar)+alignClass)+
							nowrap+">"+center+"</div>";
					} else {
						if ( Commons.hasFlag(x, "block")) 
							intoStyle = intoStyle+"padding-left: 9px;";
						kopf = kopf + "<td style=\""+
								Tc_Terminator.stil.getDivRecStyle(ar)+" "+intoStyle+"\""
								+ nowrap+">"+center+"</td>";							
					}					
										
				} else {
					// standard rec
					// alles andere in den kopf


					if ( Tc_Terminator.stilIsWeb ) {

						String classAdder = "";
						if ( Commons.hasFlag(x, "block")) {
							classAdder = " blocked ";
						}
						
						String styleSammler = intoStyle;
						String divAdderBlockStyle ="";							
						
						if ( x.getAttribute("margin_left") != null ) {
							// testerOffline=" margin-left=\"-20px\" " ;
							styleSammler = styleSammler+ " margin-left: "+x.getAttributeValue("margin_left")+";";
						}
					
						
						// String min_width_style_full ="";
						if (tdStyleLastRecord.length() > 0 );
						else if ( x.getAttributeValue("min_width") != null ) {
							//min_width_style_full = " style=\"min-width: "+x.getAttributeValue("min_width")+"; \"" ;
							styleSammler = styleSammler+ "min-width: "+x.getAttributeValue("min_width")+";";
						}
						
						if ( styleSammler.length() > 0) 
							divAdderBlockStyle = " style=\""+styleSammler+"\" ";								
					
						// das ist es
						kopf = kopf + "<td style=\""+styleSammler+"\""+linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getTdRecClass(ar)+tdStyleLastRecordClass+alignClass)+
								tdStyleLastRecord+align+colspan+" valign='top' " +
								nowrap+"><div "+linkTextResponse.getHtmlClasses(Tc_Terminator.stil.getTdRecClass(ar)+tdStyleLastRecordClass+alignClass+classAdder)
								+divAdderBlockStyle+">"+center+"</div></td>";								
					} else {
						// offline
						// das war mal so: String styleSammler = "";
						//System.out.println(".... styleSammler="+intoStyle);
						String styleSammler = intoStyle;
						String divAdderBlockStyle ="";

						if ( Commons.hasFlag(x, "block")) {
							styleSammler = styleSammler+"padding-left: 9px;";
						}
						
						if ( x.getAttribute("margin_left") != null ) {
							// testerOffline=" margin-left=\"-20px\" " ;
							styleSammler = styleSammler+ " margin-left: "+x.getAttributeValue("margin_left")+";";
						}
						if ( styleSammler.length() > 0) 
							divAdderBlockStyle = " style=\""+styleSammler+"\" ";
						
						String min_width="";
						
					

						if ( x.getAttributeValue("min_width") != null ) {
							min_width = " width=\""+x.getAttributeValue("min_width")+"\" " ;
						}
						/* nopadr ist in kombination mit margin_left zu verstehen
						 * es geht um die darstellung der nachkommastellen in hibiscus
						 * da werden die nachkommastellen in einer anderen ti-zelle dargestellt um das b�ndig zu halten
						 * allerdings macht einem dann die nat�rliche border der tab-zellen einen strich durch die 
						 * rechnung da dieser einfach zu gross ist.
						 * dieses ding behebt das problem. und zwar f�r desktop und web
						 * es werden 2 verschiedene algos angewendet die abwechselnd greifen
						 */
						//System.out.println("..-- divAdderBlockStyle="+divAdderBlockStyle);
						if ( Commons.hasFlag(x, "nopadr") )
							kopf = kopf + "<td "+tdStyleLastRecord+min_width+align+colspan+" valign='top' " +
									nowrap+"><div "+divAdderBlockStyle+">"+center+"</div></td>";
						else
							kopf = kopf + "<td "+tdStyleLastRecord+min_width+align+colspan+" valign='top' " +
									Tc_Terminator.stil.getTdRec(ar)+nowrap+"><div "+divAdderBlockStyle+">"+center+"</div></td>";
							
					}		
						
						
				}

			
				// Sp.p("kopf="+kopf);
				lableText = lableText.concat("->."+myLabel+" ");				
			} 		
					
		}
		
		if ( ret.getKey() != null && isRec ) 
			kopf = kopf + ret.getTdKey();
		/*
		if ( i<10) {
			kopf = kopf +"<td colspan='"+Integer.toString(10-i)+"'></td>";

		}
		*/
		

		// Sp.p("KoPF="+kopf);
		
		// der kopf mit der liste von td's wird ins ret geschreiben
		// die refcodes auch
		ret.setMain(kopf);
		ret.setRefcodes(refcodes);

		
		return ret;	
	}
	
	@Override
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus, IGuiController guiController, ISitzung isitzung) throws Exception {
		Interpreter ret = null;
		List<Element> l_jobchilds = eE.getChildren();
		Iterator<Element> i_jobchilds = l_jobchilds.iterator();
		int i = 0;
		while ( ret == null && i_jobchilds.hasNext()) {
			i++;
			Element x = (Element)i_jobchilds.next();
			//Sp.p("This mark:"+getRefCode(i));
			if ( getRefCode(i).equals(mark)) {
				// do it
				String myType = x.getAttributeValue("type");
				
				if (myType != null) {
					int tup = rectypes.convert(myType);
					switch ( tup ) {

				    case TYPE_CALL_SERVICE: {	
				    	int silence = Interpreter.SILENCE_NO;
						if ( Commons.hasFlag(x, "silent"))
							silence = Interpreter.SILENCE_YES;
						
						 ret= JobRunner.createInterpreter(
								 	getAttUnescaped(x,"job",rh), 
									true, 
									app, 
									inst, 
									instdir, 
									x, 
									null, 
									silence, 
									null, 
									0, 
									interWebModus, 
									interWebModus,
									terminator,
									true,
									guiController,
									isitzung,false);
						break;
					}
				    case TYPE_CALL_JOB: {
				    	String job = getAttUnescaped(x,"job",rh);

						int silence = Interpreter.SILENCE_NO;
						if ( Commons.hasFlag(x, "silent"))
							silence = Interpreter.SILENCE_YES;
						// rec get call_job Interprteer
						ret = JobRunner.createInterpreter(
								job, 
								false, 
								app, 
								inst, 
								instdir, 
								x, 
								null, 
								silence, 
								null, 
								0, 
								interWebModus,
								interWebModus,
								terminator,
								false,
								guiController,
								isitzung,false);
						
						
						ArrayList<Element> configList = 
								CoreBuilder.db.getExecutionPrologUsage(
										app, 
										inst, 
										instdir,
										true,isitzung);
						ret.addConfigList(configList);
						break;
					}
				    case TYPE_FILE: {	
				    	Element flex = new Element("config");								
						Element ca = new Element("file");
						ca.setAttribute("id", "with");
						ca.setText(Commons.java2WinInCase(Ti_TerminalLink.getX(x,rh)));
						flex.addContent(ca);
						if ( x.getAttribute("label")!=null) {
							Element e2 = new Element("prop");
							e2.setAttribute("id", "label");
							e2.setText(x.getAttributeValue("label"));
							flex.addContent(e2);
						}							
						
						ret =  JobRunner.createInterpreter(
								 Wellknown.see_file_actions, 
									true, 
									app, 
									inst, 
									instdir, 
									flex, 
									null, 
									Interpreter.SILENCE_NO, 
									null, 
									0, 
									interWebModus, 
									interWebModus,
									terminator,
									true,
									guiController,
									isitzung,false);
						break;
					}  
				    case TYPE_DIR: {	
				    	Element flex = new Element("config");								
						Element ca = new Element("dir");
						ca.setAttribute("id", "with");
						ca.setText(Commons.java2WinInCase(Ti_TerminalLink.getX(x,rh)));
						flex.addContent(ca);
						if ( x.getAttribute("label")!=null) {
							Element e2 = new Element("prop");
							e2.setAttribute("id", "label");
							e2.setText(x.getAttributeValue("label"));
							flex.addContent(e2);
						}					
						
						
						ret =  JobRunner.createInterpreter(
								 Wellknown.see_dir_actions, 
									true, 
									app, 
									inst, 
									instdir, 
									flex, 
									null, 
									Interpreter.SILENCE_NO, 
									null, 
									0, 
									interWebModus, 
									interWebModus,
									terminator,
									true,
									guiController,
									isitzung,false);
						break;
					} 
  
				    }					
					
				}
				
				
			}
		}
		
		return ret;

	}	
	
	public void reactOn(String mark ) {
		// calculate pos out of mark
		
		try {
			List<Element> l_jobchilds = eE.getChildren();
			Iterator<Element> i_jobchilds = l_jobchilds.iterator();
			int i = 0;
			int found_pos = -1;
			while ( i_jobchilds.hasNext()) {
				i++;
				Element x = (Element)i_jobchilds.next();
				// Sp.p("This mark:"+getRefCode(i));
				if ( getRefCode(i).equals(mark)) {
					// do it
					String myType = x.getAttributeValue("type");
									
					if (myType != null) {
						int tup = rectypes.convert(myType);

						switch ( tup ) {

					    case TYPE_CALL_SERVICE: {	
					    	int silence = Interpreter.SILENCE_NO;
							if ( Commons.hasFlag(x, "silent"))
								silence = Interpreter.SILENCE_YES;
							try {

								AppSystemcall sc = new AppSystemcall(
										getAttUnescaped(x,"job",rh),
										app,inst,
										x,
										silence,
										terminator,0,instdir);
								sc.run();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Sp.pe(e);
							}	
							break;
						}
					    case TYPE_CALL_JOB: {
					    	String job = getAttUnescaped(x,"job",rh);

							int silence = Interpreter.SILENCE_NO;
							if ( Commons.hasFlag(x, "silent"))
								silence = Interpreter.SILENCE_YES;

							AppJobexecutionUsage y = new AppJobexecutionUsage ( 	
									app,
									inst,
									instdir,
									job,
									x,
									false,
									silence,
									terminator,
									0,
									null);							
							try {
								y.run();
							} catch (Exception e) {
								Sp.pe(e);
							}
							
							found_pos = i;
							break;
						}
					    case TYPE_FILE: {	
					    	int silence = Interpreter.SILENCE_NO;
							if ( Commons.hasFlag(x, "silent"))
								silence = Interpreter.SILENCE_YES;
							try {
								
								Element e1 = new Element("file");
								e1.setAttribute("id", "with");
								e1.setText(Ti_TerminalLink.getX(x,rh));
								
								Element et = new Element("flex");
								et.addContent(e1);
								
								if ( x.getAttribute("label")!=null) {
									Element e2 = new Element("prop");
									e2.setAttribute("id", "label");
									e2.setText(x.getAttributeValue("label"));
									et.addContent(e2);
								}
								AppSystemcall sc = new AppSystemcall(
										"Goto-Panorama-File",
										app,inst,
										et,
										silence,
										terminator,0,instdir);
								sc.run();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Sp.pe(e);
							}
							break;
						}  
					    case TYPE_DIR: {
					    	int silence = Interpreter.SILENCE_NO;
							if ( Commons.hasFlag(x, "silent"))
								silence = Interpreter.SILENCE_YES;
							try {
								
								Element e1 = new Element("dir");
								e1.setAttribute("id", "with");
								e1.setText(Ti_TerminalLink.getX(x,rh));
								
								Element et = new Element("flex");
								et.addContent(e1);
								if ( x.getAttribute("label")!=null) {
									Element e2 = new Element("prop");
									e2.setAttribute("id", "label");
									e2.setText(x.getAttributeValue("label"));
									et.addContent(e2);
								}						
								AppSystemcall sc = new AppSystemcall(
										"Goto-Panorama",
										app,inst,
										et,
										silence,
										terminator,0,instdir);
								sc.run();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Sp.pe(e);
							}	
							break;
						} 
					    
					    case TYPE_WEB: {	
					    	Element flex = new Element("config");
							
							Element ca = new Element("prop");
							ca.setAttribute("id", "with");
							ca.setText(Ti_TerminalLink.getXUnescapedXml(x,rh));
							flex.addContent(ca);	

							try {
								AppSystemcall x2 = new AppSystemcall(
														Wellknown.open_web_with,null,null,
														flex,
														Interpreter.SILENCE_YES,
														terminator,0,null);
								x2.run();
							} catch (Exception e) {
								(new LogConnector("TerminalLinkWeb")).warn("The systemcall 'open_web_with' could not be executed !");
							}
							break;
						}                           

					    }					
						
					}
					
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

			
				
	}
	

	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		// return e.getText();
		return lableText;
	}
	public String getLabel () {
		return eE.getAttributeValue("label");
	}

	@Override
	public void executeLink() {
		// Sp.p("TerminalLinkFile:executeLink()");
		Element flex = new Element("config");
		
		Element ca = new Element("file");
		ca.setAttribute("id", "with");
		ca.setText(Commons.fileClassic(eE.getText()));
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_file_with,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkFile")).warn("The systemcall 'open_file_with' could not be executed !");
		}

	}


}