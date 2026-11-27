/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.term_core.Tc_AppConsole;


public class Tk_TerminalKeyboardSequ  implements Tk_ITerminalKeyboard{
	LogConnector lc = new LogConnector("tk");
	LogConnector lc2 = new LogConnector("tk");
	// private String 		 n = new String("   [TkSequ]  ");
	
	private boolean cancelWindow = false;
	JDialog jDialog;
	Tk_SumManager sumManager = null;
	boolean flag_sum = false;
	boolean flag_sum_only = false;

	public void executeTk ( Element tkN, Level logLevel, ITiHandler tih, ITerminator iTerminator) throws Exception {
		//Sp.p("TK got: "+Commons.toText(tkN));
		

		// if ( tkN.getAttributeValue("test") != null &&
		// 	 tkN.getAttributeValue("test").equals("0") ) {
		if ( Tester.testOrTestdynAttributeIs0(tkN,tih)) {
			lc.debug("tk-group skipped by test==0");
		} else {
			
			boolean do_editall= true;
			boolean first_time = true;
			boolean real_first_time = true;
			while ( do_editall ) {
				// Sp.p("Starting do_editall-loop");
			
				String topLabelN = tkN.getAttributeValue("label");
				if (topLabelN == null ) topLabelN = "Input Data";
				
				/*
				if ( Commons.hasFlag(tkN, "ti")) {
					Element o1 = new Element("ti");
					o1.setAttribute("label","Input of '"+Entities.importEntities(topLabelN)+"'");			
					iTerminator.executeTi(o1, null, "","", "","",tih);
				}*/

				if ( first_time && real_first_time) {
					if ( Commons.hasFlag(tkN, "sum") || Commons.hasFlag(tkN, "sum_only")) {
						flag_sum = true;
						if (Commons.hasFlag(tkN, "sum_only"))
							flag_sum_only = true;				
					}				
				}
	
				//sumManager = new Tk_SumManager(tih, iTerminator,topLabelN,Commons.hasFlag(tkN, "ti"));
				sumManager = new Tk_SumManager(tih, iTerminator,topLabelN,false);
	
				
				// JOptionPane.showMessageDialog(null, "Should stop now!!!"+Commons.toText(tkN));	
				
				List<Element> childs = tkN.getChildren();
				Iterator<Element> i = childs.iterator();
				
				int row_height = 15;
				if (CoreBuilder.ws.isLinux || CoreBuilder.ws.isMac ) {
					if ( childs.size() > 19 )
						row_height = 8;
					else if ( childs.size() > 17 )
						row_height = 9;				
					else if ( childs.size() > 15 )
						row_height = 10;
					else if ( childs.size() > 12 )
						row_height = 12;
					else if ( childs.size() > 8 )
						row_height = 13;
					else if ( childs.size() > 6 )
						row_height = 14;
				}									
			
				boolean stay_in_field_sequence = true;
				int curpos = -1;
				while ( i.hasNext() ) {
					curpos++;
					Element aktTkN = (Element)i.next();
									
					String tipe = aktTkN.getAttributeValue("type");
					if ( tipe == null ) tipe="text";
					
					if ( (aktTkN.getAttributeValue("id") == null ||
						  aktTkN.getAttributeValue("id").length() == 0 ) 
						  && !tipe.equals("msg")) {
						JOptionPane.showMessageDialog(null,"Terminator: KUKUK");
						throw new LangException ("Tk needs an attribute @id: "+Commons.toText(aktTkN));
					}
					// Sp.p("tk id="+aktTkN.getAttributeValue("id"));
					
					String tipN = aktTkN.getAttributeValue("tip");
					if ( tipN!=null && tipN.length()== 0) tipN=null;
									
					if ( Tester.testOrTestdynAttributeIs0(aktTkN,tih)) {
						lc.info("Input of id '"+aktTkN.getAttributeValue("id")+"' and type '"+tipe+"' is skipped because test==0=="+aktTkN.getAttributeValue("test"));				
					} else {
						// lc.core(n+"Next: "+Commons.toTextRaw(aktTk),LogConnector.CORE_TERMINAL);
	
						if ( aktTkN.getAttributeValue("label") == null && !tipe.equals("msg"))
							aktTkN.setAttribute("label", aktTkN.getAttributeValue("id"));	
						
						Tk_Tk tko;
						if ( tipe.equals("text")) {
							tko = new Tk_TkText();
						} else if ( tipe.equals("textblock")) {
							tko = new Tk_TkTextblock();
						} else if ( tipe.equals("bool")) {
							/*
							if ( Tc_AppConsole.mac || CoreBuilder.ws.isTablet)
								tko = new Tk_TkBoolMac();
							else 
								tko = new Tk_TkBool();
								*/
							tko = new Tk_TkBoolMac();
						} else if ( tipe.equals("file")) {
							tko = new Tk_TkFile();
						} else if ( tipe.equals("dir")) {
							tko = new Tk_TkDir();
						} else if ( tipe.equals("sel")) {
							tko = new Tk_TkSelection();
						} else if ( tipe.equals("accept")) {
							if ( flag_sum )
								throw new Exception("Accept with tk-flag 'sum' or 'sum_only' not allowed!");
							tko = new Tk_TkAccept();
						}  else if ( tipe.equals("msg")) {
							tko = new Tk_TkMsg();
						} else {
							throw new LangException("Unknow type of tk, type="+tipe);
						}
	
						tko.built(aktTkN, tih,topLabelN,tipN);
						
						String regex = aktTkN.getAttributeValue("regex");
						String regex_msg = aktTkN.getAttributeValue("regex_msg");
						if ( regex != null && regex_msg==null)
							regex_msg = "Enter a value according to the regular expression: "+regex;
						Tk_MyMessageDialog regex_mmd = null;
						if ( regex != null ) {
							regex_mmd = new Tk_MyMessageDialog();
							regex_mmd.built("Input of '"+aktTkN.getAttributeValue("label")+"' not valid!",regex_msg);
							regex_mmd.setTopHeaderText("Input not valid");
							regex_mmd.setFocusToOkButton();
						}
						
						Tk_SumMember sumMember;
						if ( !tipe.equals("msg")) 
							sumMember = new Tk_SumMember(sumManager,tko,regex,regex_msg,regex_mmd,aktTkN.getAttributeValue("label"),tipN,false,row_height,aktTkN,lc);					
						else
							sumMember = new Tk_SumMember(sumManager,tko,null,null,regex_mmd,"","",true,row_height,aktTkN,lc);
						sumManager.add(sumMember);
						
						boolean test_first = true;
						
						if ( real_first_time ) {
							// Sp.p("FIRST TEST: "+aktTk.getAttributeValue("id"));
							// if ( aktTkN.getAttribute("test_first")!=null && aktTkN.getAttributeValue("test_first").equals("0"))
							if ( Tester.testFirstOrTestfirstDynAttributeIs0(aktTkN,tih))
								test_first = false;
						}
						
						boolean valid_regex = false;
						while ( !valid_regex ) {
							if ( flag_sum && !test_first ) {
								// there is a sum and it falls out because test_first
								// xxx
								valid_regex = true;
							} else if ( flag_sum_only  ) {
								valid_regex = true;
							} else {
								boolean more = true;
								try {
									if (stay_in_field_sequence) {
										if ( tipe.equals("msg")) {
											// das ist das validieren
											if (showMsg(childs, curpos, first_time,tih))
												tko.execute(first_time);
										} else {
											tko.execute(first_time);
										}
										
									}
								} catch (Exception e) {
	
									if ( real_first_time ) {
										throw e;
									} else {
										more = false;
										valid_regex = true;
										stay_in_field_sequence = false;
										//Sp.p("----WAS HERE----");
									}
	
								}
								if ( more ) {
									String resultN = tko.getValueN();
	
									Tk_MyMessageDialog mmd2 = FitsRegex.fits(
											tko.getValue4RegexN(), tko.hasNono(), regex, regex_mmd,
											aktTkN.getAttributeValue("label"));
									if ( mmd2 == null ) {
										// regex is ok
										
										// lc2.debug_expressions(resultN+" >> ("+tko.getVarType()+","+aktTkN.getAttributeValue("id")+")"); 
										FitsRuleResult frr = FitsRule.check(
												resultN, 
												aktTkN.getAttributeValue("rule"), 
												tko, 
												aktTkN.getAttributeValue("label"));
						
										if ( frr.changed ) {
											Tk_MyMessageDialog rule_msg = new Tk_MyMessageDialog();
											rule_msg.built(
													frr.askMessageHeader,frr.askMessage);
											rule_msg.setOkText("Yes, I accept the update");
											rule_msg.setCancelText("No");
											rule_msg.setTopHeaderText("Warning");
											rule_msg.setFocusToOkButton();
											boolean rule_msg_ok = rule_msg.execute();
											
											if ( !rule_msg_ok) {
												valid_regex = false;
												// throw new TkStopException("Disagreement about reformating value of "+label);
											} else {
												tko.setValueN(frr.newValue);
												valid_regex = true;	
												sumMember.finalisieren(false,"Sequence:new rule-value");
											}
										} else {
											valid_regex = true;
											sumMember.finalisieren(false,"Sequence: rule + regex ok");
										}
									} else {
										// its not ok
										valid_regex = false;
										boolean good = mmd2.execute();
										// Sp.p("good="+good);
										if ( !good ) {
											throw new Exception("Message not confirmed, was about '"+
													aktTkN.getAttributeValue("label")+"'");
										}					
									} // if md2==null
	
								} // if more						
							}	// end ifs with sum flags and so					
						} // end of regex-loop	
						// Sp.p("Value is regex-valid");
						
					}  // end of if for input
					// Sp.p("finished tk id="+aktTkN.getAttributeValue("id")+" curpos="+curpos);
				}		// end of member-loop
				// Sp.p("HALLO");
				do_editall= false;
				if ( flag_sum ) {
					// sum or sum_only flag, perform the revie-page
					do_editall= sumManager.performDialog();	
					
					if ( !do_editall) {
						// Sp.p("--ok 1--");
						// pressed ok, finish
						boolean stay_in = sumManager.finalisiere(true,"Sequence final: A");
						if ( stay_in ) {
							// there is still something wrong;
							// go back to sum-page
							do_editall = true; // stay in
							flag_sum = true;
							flag_sum_only = true;
							// Sp.p("--ok 2--");
							first_time = true;
						} else {
							// everything is ok
							first_time = false;
							do_editall= false; // rausspringen
							// Sp.p("--ok 3--");
							// will stop since edit all was set to false
						}
					} else {
						// pressed edit alll
						do_editall= true; // stay in
						flag_sum_only = false;
						flag_sum = true;
						// not nessessary, test out next time
						sumManager.finalisiere(false,"Sequence final: B");
						// Sp.p("Going into Edit All again ...");
						first_time = true;
						// go back to edit
					}
				} else {
					// no sum, just finish it
				   // not nessassary, test out next time 
				   sumManager.finalisiere(false,"Sequence final: C"); 
				   do_editall= false; // fall out
				}
				
				real_first_time = false;

			} // end while loop: do edit all
			// Sp.p("finished do edit all loop");

		} // end test of general tk attibute
		// Sp.p("end test of general tk attibute");
		
	}
	
	public static boolean showMsg(List<Element> childs, int msg, boolean first_time,ITiHandler tih ) {
		// dieses ding schaut nach, ob nach der msg (position durch int msg) noch was kommt
		//Sp.p("�� showMsg msgpos="+msg);
		// hund hundling
		if ( true )
			return true;
		if ( childs.size() == 1 )
			return true;
		// Sp.p("MSG: "+Commons.toText(childs.get(msg)));
		int pos = msg+1;
		int size = childs.size();
		// Sp.p("MSG: "+msg+" POS: "+pos+" SIZE="+size);
		
		boolean retval = false;
		
		while ( pos < size ) {
			Element now = childs.get(pos);
			// Sp.p("POS "+pos+": "+Commons.toText(childs.get(pos)));
			if (now.getAttribute("type")!=null && now.getAttributeValue("type").equals("msg")) {
				retval = false;
				pos=size;
		    } else {
				boolean would = true;
				// if ( now.getAttributeValue("test")!=null && now.getAttributeValue("test").equals("0") ) 
				if ( Tester.testOrTestdynAttributeIs0(now,tih))
					would = false;
				if ( would && first_time) {
					// if ( now.getAttribute("test_first")!=null && now.getAttributeValue("test_first").equals("0"))
					if ( Tester.testFirstOrTestfirstDynAttributeIs0(now,tih))
						would = false;
				}
				if (would ) {
					retval = true;
					pos=size;
				}
				pos++;				
			}

		}
		//Sp.p("�� showMsg msgpos="+msg+" => "+retval);
		return retval;
	}
}