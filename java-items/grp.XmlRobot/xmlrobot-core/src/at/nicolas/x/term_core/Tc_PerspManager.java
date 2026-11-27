/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_persp.Persp_Afrika;
import at.nicolas.x.term_persp.Persp_Asien;
import at.nicolas.x.term_persp.Persp_Australien;
import at.nicolas.x.term_persp.Persp_AustralienCanberra;
import at.nicolas.x.term_persp.Persp_Confuzius;
import at.nicolas.x.term_persp.Persp_Cromwell2;
import at.nicolas.x.term_persp.Persp_Developers2;
import at.nicolas.x.term_persp.Persp_Developers2F;
import at.nicolas.x.term_persp.Persp_Developers2F2;
import at.nicolas.x.term_persp.Persp_Developers2F3;
import at.nicolas.x.term_persp.Persp_Developers2X;
import at.nicolas.x.term_persp.Persp_Developers4;
import at.nicolas.x.term_persp.Persp_Dorothea;
import at.nicolas.x.term_persp.Persp_Enduser;
import at.nicolas.x.term_persp.Persp_IZP;
import at.nicolas.x.term_persp.Persp_Noname11;
import at.nicolas.x.term_persp.Persp_Noname12;
import at.nicolas.x.term_persp.Persp_Noname5;
import at.nicolas.x.term_persp.Persp_Noname6;
import at.nicolas.x.term_persp.Persp_Noname9;
import at.nicolas.x.term_persp.Persp_WebWalk1;
import at.nicolas.x.term_persp.Persp_WebWalk2;
import at.nicolas.x.term_persp.Persp_Webwalk3;

public class Tc_PerspManager {

	HashMap<String, Persp_IZP> izpMap = new HashMap<String, Persp_IZP>();

	String fnPerspList = null;
	Element rootElement = null;
	String namesArray[]=null;
	private LogConnector lc = new LogConnector("PerspManager");
	
	public Tc_PerspManager( String _fnPerspList ) throws JDOMException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		fnPerspList = _fnPerspList;
	
		Persp_IZP izp;
		try {


			
			
			Persp_Asien platon = new Persp_Asien(true,false);
			platon.setButtonCount(4);
			addIZP("Rio",platon);
			
			Persp_Asien asien2 = new Persp_Asien(true,true);
			asien2.setButtonCount(4);			
			addIZP("Rio +",asien2);	
			
			Persp_Asien parmenides = new Persp_Asien(true,false);
			parmenides.setButtonCount(3);
			addIZP("Parmenides",parmenides);
			
			Persp_Asien parmenides2 = new Persp_Asien(true,true);
			parmenides2.setButtonCount(3);			
			addIZP("Parmenides +",parmenides2);				
			
			Persp_Australien australien = new Persp_Australien(true,false);
			australien.setButtonCount(4);
			addIZP("Konfuzius",australien);	
			

			Persp_Australien australien2 = new Persp_Australien(true,true);
			australien2.setButtonCount(4);
			addIZP("Konfuzius +",australien2);				

			Persp_AustralienCanberra canberra = new Persp_AustralienCanberra(true,false);
			canberra.setButtonCount(4);
			addIZP("at-Taqafi",canberra);		

			Persp_AustralienCanberra canberraWH = new Persp_AustralienCanberra(true,true);
			canberraWH.setButtonCount(4);
			addIZP("at-Taqafi +",canberraWH);				
			
			Persp_Afrika afrika = new Persp_Afrika(true,true,false);
			afrika.setButtonCount(4);
			addIZP("Lessing",afrika);
			
			Persp_Afrika afrika2 = new Persp_Afrika(true,true,true);
			afrika2.setButtonCount(4);
			addIZP("Lessing +",afrika2);				

			Persp_Afrika america = new Persp_Afrika(true,false,false);
			america.setButtonCount(4);
			addIZP("Kopernikus",america);	
			
		
			


			
			

			
			
			Persp_Afrika america2 = new Persp_Afrika(true,false,true);
			america2.setButtonCount(4);
			addIZP("Kopernikus +",america2);	
			
			
			Persp_Afrika thales = new Persp_Afrika(true,true,false);
			thales.setButtonCount(3);
			thales.setLeftSmall();
			addIZP("Thales",thales);
			
			Persp_Afrika ptahhotep = new Persp_Afrika(true,true,false);
			ptahhotep.setButtonCount(3);
			ptahhotep.setLeftSmall();
			ptahhotep.setButtonHoehe(135);
			addIZP("Ptahhotep",ptahhotep);	
			
			Persp_IZP izp10c = null;
			izp10c = new Persp_Dorothea(true,false);
			addIZP("Ovid", izp10c);	
			
			
			Persp_IZP izp6F =  new Persp_Developers2F();
			addIZP("Roth",izp6F);	
			
			Persp_IZP izp6F2 =  new Persp_Developers2F2();
			addIZP("Roth-2",izp6F2);		
			
			Persp_IZP izp6F3 =  new Persp_Developers2F3();
			addIZP("Roth-3",izp6F3);					
						
			Persp_Enduser izp1 = new Persp_Enduser();
			addIZP("Tutu",izp1);	
			
			//Persp_Enduser2 eu2 = new Persp_Enduser2();
			//addIZP("Eugen",eu2);			

			Persp_Dorothea ez2_b = new Persp_Dorothea(true);
			ez2_b.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*2);
			addIZP("Zap 1", ez2_b);
			
			Persp_Dorothea ez2_x = new Persp_Dorothea(true);
			ez2_x.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*3);
			addIZP("Zap 2", ez2_x);				
			
			Persp_Dorothea ez2_a = new Persp_Dorothea(true);
			ez2_a.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*4);
			addIZP("Zap 3 ", ez2_a);	
			
			Persp_Dorothea ez2 = new Persp_Dorothea(true);
			ez2.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*5);
			addIZP("Zap 4 ", ez2);

			Persp_Dorothea ez3 = new Persp_Dorothea(true);
			ez3.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*6);

			addIZP("Zap 5", ez3);

			Persp_Dorothea ez4 = new Persp_Dorothea(true);
			ez4.setLogsizer(Tc_AppConsole.guiSizer.value100_scaled*7);
	
			addIZP("Zap 6", ez4);			
			
			/*
			Persp_IZP ww1 = new Persp_WebWalk1(true,false);
			addIZP("WebWalk",ww1);
			
			Persp_IZP ww23 = new Persp_WebWalk2(3,false);
			addIZP("WebWalk 2 3",ww1); */
			Persp_IZP ww32 = new Persp_Webwalk3(2,false);
			addIZP("Ww 2",ww32);	
			
			Persp_IZP ww32t = new Persp_Webwalk3(2,true);
			addIZP("Ww 2+",ww32t);	
			
			Persp_IZP ww33 = new Persp_Webwalk3(3,false);
			addIZP("Ww 3",ww33);	
						
			Persp_IZP ww33t = new Persp_Webwalk3(3,true);
			addIZP("Ww 3+",ww33t);
			
			Persp_IZP ww34 = new Persp_Webwalk3(4,false);
			addIZP("Ww 4",ww34);		
			
			Persp_IZP ww34t = new Persp_Webwalk3(4,true);
			addIZP("Ww 4+",ww34t);		
			
			Persp_IZP ww35 = new Persp_Webwalk3(5,false);
			addIZP("Ww 5",ww35);			
			
			
								
			// Long List
								
			Persp_IZP izp6 =  new Persp_Developers2();
			addIZP("Euclid",izp6);	
											
			// Uppers
			Persp_IZP some52 = new Persp_Noname5(false);
			addIZP("Zweig",some52);		

			Persp_IZP some9 = new Persp_Noname9();
			addIZP("Orwell",some9);	
			
			Persp_IZP some63 = new Persp_Noname12(false);
		
			addIZP("Laozi",some63);		

			Persp_IZP some62 = new Persp_Noname6(false,false);
			addIZP("Abulcasis",some62);
				
			// Sparprogramm
			
			Persp_Developers2X izpxx = new Persp_Developers2X();
			izpxx.setButtonCount(3);
			izpxx.setHelp(false);
			addIZP("Voltaire",izpxx);

			Persp_Developers2X jefferson = new Persp_Developers2X();
			jefferson.setButtonCount(3);
			jefferson.setHelp(true);
			addIZP("Rousseau",jefferson);

			Persp_Noname12 some63a = new Persp_Noname12(true);
			some63a.setButtonCount(4);
			addIZP("Spinoza",some63a);		

				
			
			Persp_Cromwell2 oc2 = new Persp_Cromwell2();
			addIZP("Aristoteles",oc2);
			
			Persp_Cromwell2 oc3 = new Persp_Cromwell2();
			oc3.setSmall();
			addIZP("Babapapa",oc3);			
			
			Persp_Confuzius izp8 = null;
			izp8 = new Persp_Confuzius();
			izp8.setHelp(false);
			izp8.setColumns(4);
			
			addIZP("Chaldun",izp8);	
			
			Persp_Confuzius izpPlato = null;
			izpPlato = new Persp_Confuzius();
			izpPlato.setHelp(false);
			izpPlato.setColumns(3);
			
			addIZP("Newton",izpPlato);	
			
			Persp_Confuzius izpPlatox = null;
			izpPlatox = new Persp_Confuzius();
			izpPlatox.setHelp(true);
			izpPlatox.setColumns(3);
			
			addIZP("Newton +",izpPlatox);				
			
			Persp_Confuzius che = null;
			che = new Persp_Confuzius();
			che.setHelp(false);
			che.setColumns(3);
			che.setDbDimension(new Dimension(Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.value100_scaled));
			
			addIZP("Che",che);				
			
			Persp_Confuzius Dante = new Persp_Confuzius();
			Dante.setHelp(false);
			Dante.setColumns(4);	
			Dante.setDbDimension(new Dimension(Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.value100_scaled));
			addIZP("Dante",Dante);		
			
			Persp_Confuzius Locke = null;
			Locke = new Persp_Confuzius();
			Locke.setHelp(false);
			Locke.setColumns(5);	
			Locke.setDbDimension(new Dimension(Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.value100_scaled));

			addIZP("Locke",Locke);				

			Persp_Noname6 some67y = new Persp_Noname6(false,true);
			some67y.setSpace(3,Tc_AppConsole.guiSizer.value100_scaled*3,Tc_AppConsole.guiSizer.getLeftPanelWidth());
			addIZP("Ptole",some67y);
			
			Persp_Noname6 some67x = new Persp_Noname6(false,true);
			some67x.setSpace(3,Tc_AppConsole.guiSizer.value100_scaled*2,Tc_AppConsole.guiSizer.getLeftPanelWidth());
			addIZP("Ho Chi Minh",some67x);	
			
			Persp_Noname6 some67yb = new Persp_Noname6(false,true);
			some67yb.setSpace(3,Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.getLeftPanelWidth());
			addIZP("Curie",some67yb);	
			/*
			Persp_Noname6 some67 = new Persp_Noname6(false,false);
			some67.setSpace(3,100,Tc_AppConsole.guiSizer.getLeftPanelWidth());
			addIZP("Da Vinci",some67);	*/			
			
			/*
			Persp_IZP izp7 = new Persp_Developers3();
			addIZP("Ho Chi Minh",izp7);*/
			/*
			izp = new Persp_Developers();
			addIZP("Dali",izp);		
			
			Persp_Developers dulli = new Persp_Developers();
			dulli.setSmallTime();
			addIZP("Atri",dulli);		*/
			
			Persp_Developers4 dulli2 = new Persp_Developers4();
			dulli2.setSmallTime();
			addIZP("Ghosha",dulli2);				

			Persp_IZP some11 = new Persp_Noname11();
			addIZP("Pythagoras",some11);
			/*
			Persp_IZP some7 = new Persp_Noname7();
			addIZP("Hippocrates",some7); */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}		
		
		
		// Aus dem File
		/* #no_dynamic_perspectives# 
		List persplist = rootElement.getChildren("perspdef");
		Iterator iterator = persplist.iterator();
		while ( iterator.hasNext() ) {
			Element aktPerspdef = (Element)iterator.next();
			String name = aktPerspdef.getChild("name").getText();
			Sp.p("persp name="+name);
			String classname = aktPerspdef.getChild("classname").getText();
			Sp.p("persp classname="+classname);
			Element classpath = aktPerspdef.getChild("classpath");
		    List urlList = classpath.getChildren("url");
		    ArrayList urlArray = new ArrayList();
		    Iterator i = urlList.iterator();
	  		while ( i.hasNext()) {
	  			Element curUrl = (Element)i.next();
	  			urlArray.add(new URL(curUrl.getText()));
	  		}
		

      URL[] urls = (URL[])urlArray.toArray(new URL[urlArray.size()]);
      
      Class c = null;
		
  		try {
  		  c = Class.forName(classname);  // Dynamically load the class
  		  lc.debug("die classe "+classname+" konnte aus dem default classloader nicht geladen werden.");
  		  
  		} catch (Exception e) { 
  
  			try {
  				
  				ClassLoader loader = new URLClassLoader(urls);
  				c = loader.loadClass(classname);
  			} catch (ClassNotFoundException e1) {
  				Sp.p("urllist:");
  				for (int ix=0;ix<urls.length;ix++) {
  					Sp.p("url:"+urls[ix].getPath());
  				}
  				Sp.p("***** pos A");
  			
  			}
  		}

  		if ( c != null ) {	
			Object o = c.newInstance();
			izp = (IZP)o;
			addIZP(izp.getName(),izp);
		} else {
			throw new ClassNotFoundException(classname+" konnte nicht geladen werden");
		}

      
		}
		*/
		
	}
	ArrayList namesList = new ArrayList();
	private void addIZP( String name, Persp_IZP x ) {
		izpMap.put(name,x);
		namesList.add(name);
	}	

	
	public String[] getNames() {
		if ( namesArray == null ) {
			/*
			Set keyset = izpMap.keySet();
			Iterator iterator = keyset.iterator();
			
			while ( iterator.hasNext() ) {
				String name  = (String)iterator.next();
				namesList.add(name);
			}
			*/
			namesArray = (String[])namesList.toArray(new String[namesList.size()]);
		}
		     
		return namesArray;
	}
	
	/*
	private void addIZP( IZP x ) {
		Sp.p("sx "+x.getClass().getSimpleName());
		String name = x.getClass().getSimpleName();
		izpMap.put(name,x);
		Sp.p("sy "+x.getClass().getSimpleName());
	}	
	*/
	
	/*
	public String[] getNames() {
		if ( names == null ) {
			ArrayList namesList = new ArrayList();
			Set keyset = izpMap.keySet();
			Iterator iterator = keyset.iterator();
			
			while ( iterator.hasNext() ) {
				String name  = (String)iterator.next();
				namesList.add(name);
			}
			names = (String[])namesList.toArray(new String[namesList.size()]);
		}
		     
		return names;
	}	*/
	public Persp_IZP getPersp(String perspName ) {
		Persp_IZP ret = (Persp_IZP)izpMap.get(perspName);
		if ( ret == null )
			ret = (Persp_IZP)izpMap.get("Developers");
		return ( ret );
	}
	
}