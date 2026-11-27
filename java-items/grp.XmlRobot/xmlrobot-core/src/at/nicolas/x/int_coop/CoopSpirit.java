/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.tasks.Xml_Update;


public class CoopSpirit implements ITask, ICoop {

	
	int missing_strategy = 2;
	final static int MISSING_STRATEGY_EMPTY = 0;
	final static int MISSING_STRATEGY_EXCEPTION = 1;
	final static int MISSING_STRATEGY_JOVIAL = 2;
	
	private static KeyWordMapper missingtypes;
	
	private static final int act_open   = 0;
	private static final int act_xml_update   = 1;
	private static final int act_save   = 2;
	private static final int act_close   = 3;
	private static final int act_run   = 4;
	private static KeyWordMapper tasknames;
	
	int logic_id = -1;
		
	IRequestHandler rh;
	
	
	String filename; 	
	Document doc;
	Element root;
	public boolean flag_dont_escape_on_read;
	public boolean old = false;	
	String idE;
	
	public void setLogicId ( int logic_id ) {
		this.logic_id = logic_id;
	}
	
	public Document getDoc() {
		return doc;
	}

	public Element getRoot() {
		return root;
	}
	
	public static CoopSpirit createSpirit(
			String spirit_id, 
			Element xmlElement, 
			IRequestHandler rh) throws Exception {
		/*[echnaton-customer]*/
		CoopSpirit myCoopSpirit = new CoopSpirit();
		myCoopSpirit.root = xmlElement;
		/*[echnaton-crypto]*/
		myCoopSpirit.idE = spirit_id; 
		myCoopSpirit.rh = rh;
		myCoopSpirit.doc = new Document(xmlElement);;
		myCoopSpirit.root = xmlElement;
									
		// starting the loop
		rh.putNo(spirit_id, Wellknown.coop_type_spirit, myCoopSpirit);
				
		return myCoopSpirit;
	}
	
	public Parameters execute(Element eN, IRequestHandler rh) throws Exception {
		

		
		if ( eN.getAttribute("id") == null ) {
			throw new Exception("spirit needs attribute @id");
		}		
		
		//String action = eN.getAttributeValue("act");
		idE = eN.getAttributeValue("id"); // ??nic321
		
		this.rh = rh;
				
		Parameters retParas = null;
		
		int ac = tasknames.convert(eN.getAttributeValue("act"));

		switch ( ac ) {
		case act_open: {	
			if ( Commons.hasFlag(eN, "friend") )
				throw new Exception("spirit uses dont_escape_on_read but no friend");
			this.flag_dont_escape_on_read = Commons.hasFlag(eN, "dont_escape_on_read"); //??nic321 sollte das nicht nur bei open gemacht werden
			
			filename = null;
			if ( eN.getAttribute("xml") != null ) 
				filename = eN.getAttributeValue("xml");
			else if ( eN.getAttribute("file") != null ) 
				filename = eN.getAttributeValue("file");
			 else
				throw new Exception("spirit needs attribute 'xml' or 'file' with a filename");
										
			doc = CommonsVfs.getXmldocumentByFilename(filename,rh.getSitzung());
			if ( Commons.hasFlag(eN, "clone"))
				root = doc.getRootElement().clone();
			else
				root = doc.getRootElement();
			
			if (eN.getAttribute("missing_strategy")==null )
				missing_strategy = MISSING_STRATEGY_JOVIAL;
			else
				missing_strategy = missingtypes.convert(eN.getAttributeValue("missing_strategy"));

			rh.putNo(idE, Wellknown.coop_type_spirit,  this);

			break;
		}

		default: { 	
			String id = eN.getAttributeValue("id");
			CoopSpirit spirit = (CoopSpirit)rh.getNo(id, Wellknown.coop_type_spirit);
			if ( spirit == null ) 
				throw new Exception("The spirit with id '"+id+"' was not found");
			
		    switch ( ac ) {
		    case act_xml_update: {	
				Xml_Update xu = new Xml_Update();
				Element currentElement = null;
				if ( eN.getAttribute("where_is_loop_xpath")!=null) {
					throw new Exception("attribute 'where_is_loop_xpath' not supported, use 'where_is_loop_xml'");
				}
				
				if ( eN.getAttribute("where_is_loop_xml")!=null) {
					currentElement = CoopLx.getCurrentElement(rh, eN.getAttributeValue("where_is_loop_xml"),this);				    
				} 

				int countRet = xu.executeSpirit(spirit.root, currentElement,eN, rh);
				if ( eN.getAttribute("count_prop_id") != null ) {
					 rh.setVar("prop", eN.getAttributeValue("count_prop_id"), Integer.toString(countRet));					
				} 
		    	break;
		    }
		    case act_save: {	
		    	String outFile = null;
				if ( eN.getAttribute("out") != null ) {
					outFile = eN.getAttributeValue("out");
				}
				boolean pretty = Commons.hasFlag(eN, "pretty");
				if ( outFile != null ) {
					// dann schreiben wir das mal da raus.
					if ( pretty )
						CommonsVfs.writeToFilePretty(spirit.doc, outFile,rh.getSitzung());	
					else
						CommonsVfs.writeToFile(spirit.doc, outFile,rh.getSitzung());
					
				} else  {
					// dann schreiben wir das mal da raus.
					if ( pretty )
						CommonsVfs.writeToFilePretty(spirit.doc, spirit.filename,rh.getSitzung());
					else
						CommonsVfs.writeToFile(spirit.doc, spirit.filename,rh.getSitzung());
				}
		    	break;
		    }
		    case act_close: {	
		    	rh.removeNo(id, Wellknown.coop_type_spirit);
				boolean pretty = Commons.hasFlag(eN, "pretty");
				String outFile = null;
				if ( eN.getAttribute("out") != null ) {
					outFile = eN.getAttributeValue("out");
				}
				boolean save_flag = Commons.hasFlag(eN, "save");
				
				if ( outFile != null && save_flag ) {
					// dann schreiben wir das mal da raus.
	
					// Commons.writeToFileFormat(docO, out,Format.getRawFormat());
					if ( pretty )
						CommonsVfs.writeToFilePretty(spirit.doc, outFile,rh.getSitzung());
					else
						CommonsVfs.writeToFile(spirit.doc, outFile,rh.getSitzung());
				} else if ( outFile == null && save_flag ) {
					// dann schreiben wir das mal da raus.
					if ( pretty )
						CommonsVfs.writeToFilePretty(spirit.doc, spirit.filename,rh.getSitzung());
					else
						CommonsVfs.writeToFile(spirit.doc, spirit.filename,rh.getSitzung());
				} else {
					//_lc.log_ext("Closed print without saving", this);
				}
		    	break;
		    }
		    case act_run: {	
		    	String theXpath = null;
				if ( eN.getAttribute("xpath") != null ) {
					theXpath = eN.getAttributeValue("xpath");
				} else
					throw new LangException("Keinen parameter @xpath gefunden");
				
				Element found = Commons.xpathSelectSingleNode(spirit.root, theXpath);
				
				if ( found == null ) 
					throw new Exception("xpath '"+theXpath+"' hat kein Ergebnis gebracht");
				
				rh.addToStackSimple(found.getChildren());
		    	break;
		    }
 
		    } 
		  	break;
		}
		}  		
 
		//System.out.println("xxxyxxx "+retParas.getStringForLineprint()); 
		return retParas;
	}
	
	public String getExtensionId() {
		return ( "spirit");
	}



	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		
	}
	
	public String getMissingStrategy(String message ) throws Exception {
		if ( missing_strategy == MISSING_STRATEGY_EMPTY )
			return "";
		else if ( missing_strategy == MISSING_STRATEGY_JOVIAL ) {
			if ( rh.isJovial())
				return "";
			else 
				throw new Exception ( "We are not jovial. "+message+ " Spirit '"+idE+"' didnt delivered a value.");
		} else if ( missing_strategy == MISSING_STRATEGY_EXCEPTION ) {
			throw new Exception ( "Exception on Strategy. "+message+ " Spirit '"+idE+"' didnt delivered a value.");
		} else {
			throw new Exception ( "Houston, we have a problem. call the boss!");
		}
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		missingtypes = new KeyWordMapper("SpiritMissingType",3);
		missingtypes.put("empty", MISSING_STRATEGY_EMPTY);
		missingtypes.put("exception", MISSING_STRATEGY_EXCEPTION);
		missingtypes.put("jovial_like_interpreter", MISSING_STRATEGY_JOVIAL);
		missingtypes.putFinished();
		
		tasknames = new KeyWordMapper("SpiritActs",5);
		tasknames.put("open", act_open);
		tasknames.put("xml_update", act_xml_update);
		tasknames.put("save", act_save);
		tasknames.put("close", act_close);
		tasknames.put("run", act_run);
		tasknames.putFinished();
	}



}