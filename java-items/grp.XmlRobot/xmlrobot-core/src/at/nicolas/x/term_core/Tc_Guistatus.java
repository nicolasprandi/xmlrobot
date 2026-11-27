/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;

public class Tc_Guistatus {
	String filename = null;
	
	private String perspective = null;
	
	public String getPerspective() {
		return perspective;
	}

	public void setPerspective(String perspective) {
		this.perspective = perspective;
	}

	private int frame_size_x = 0;
	private int frame_size_y = 0;
	private int frame_loc_x = 0;
	private int frame_loc_y = 0;
	
	private static int def_frame_size_x = 1250;
	private static int def_frame_size_y = 600;
	private static int def_frame_loc_x = 30;
	private static int def_frame_loc_y = 10;
	
	private String marked_appid = null;
	private String marked_instid = null;
	private String marked_jobid = null;
	
	private String loglevel = null;
	
	private int guisize = 2;
	
	private boolean auto = true;
	
	private Element expanded_nodes = null;
			
	public Element getExpanded_nodes() {
		return expanded_nodes;
	}

	public void setExpanded_nodes(Element expanded_nodes) {
		this.expanded_nodes = expanded_nodes;
	}
	ISitzung sitzung;
	public Tc_Guistatus( ISitzung sitzung)  {
		this.sitzung = sitzung;

	}
	
	public void readFile( String _filename ) throws JDOMException, IOException, Exception {
		
		// Reset
		perspective = null;		
		frame_size_x = 0;
		frame_size_y = 0;
		frame_loc_x = 0;
		frame_loc_y = 0;
		marked_appid = null;
		marked_instid = null;
		marked_jobid = null;		
		loglevel = null;	
		guisize = 2;
		auto = true;
		
		// Read File
		filename = _filename;
		FileObject file = CommonsVfs.grantsFileRead(filename,sitzung);
		LogConnector lc = new LogConnector("Guistatus.readFile(String):");
		if ( file.exists() ) {
			Document document = CommonsVfs.getXmldocumentByFilename(filename,sitzung);
			Element root = document.getRootElement();
			
			// perspective
			Element ep = root.getChild("perspective");
			if ( ep != null ) perspective = ep.getText();
			
			// frame
			Element ef = root.getChild("frame");
			if ( ef != null ) {
				String ef_size_x = ef.getAttributeValue("size_x");
				if ( ef_size_x!= null )
					frame_size_x = Commons.string2int(ef_size_x);
				
				String ef_size_y = ef.getAttributeValue("size_y");
				if ( ef_size_y!= null )
					frame_size_y = Commons.string2int(ef_size_y);
				
				String ef_loc_x = ef.getAttributeValue("loc_x");
				if ( ef_loc_x!= null )
					frame_loc_x = Commons.string2int(ef_loc_x);

				String ef_loc_y = ef.getAttributeValue("loc_y");
				if ( ef_loc_y!= null )
					frame_loc_y = Commons.string2int(ef_loc_y);				
			}
			
			// marked
			Element em = root.getChild("marked");
			if ( em != null ) {
				String em_appid = em.getAttributeValue("appid");
				if ( em_appid!= null )
					marked_appid = em_appid;
				
				String em_instid = em.getAttributeValue("instid");
				if ( em_instid!= null )
					marked_instid = em_instid;
				
				String em_jobid = em.getAttributeValue("jobid");
				if ( em_jobid!= null )
					marked_jobid = em_jobid;				
			}
			
			// logging
			Element el = root.getChild("logging");
			if ( el !=null ) {
				String el_loglevel = el.getAttributeValue("loglevel");
				if ( el_loglevel != null )
					loglevel = el_loglevel;
			}
			
			// logging
			Element egs = root.getChild("guisize");
			if ( egs !=null ) {
				String found_guisize = egs.getText();
				if ( found_guisize != null && found_guisize.length()>0 ) {
					try {
						guisize = Commons.string2int(found_guisize);
					} catch (Exception e) {
						guisize = 2;
					}
				}
			}			
			
			// auto
			Element eauto = root.getChild("auto");
			if ( eauto !=null ) {
				String found_auto = eauto.getText();
				if ( found_auto != null && found_auto.length()>0 ) {
					auto = Commons.stringxr2bool(found_auto);
					// Sp.p("LOADED AUTO:"+auto+", "+found_auto);
				}
			}				
			
			// opennodes
			expanded_nodes = root.getChild("expanded_nodes");
			if ( expanded_nodes == null ) {
				expanded_nodes = new Element("expanded_nodes");
			}else {
				// marked_appid= "Hibiscus";
				/*[reco-marked-app-startup]*/
			}
			
		}

		// Default Values
		
		if ( frame_size_x == 0 ) frame_size_x = def_frame_size_x;
		if ( frame_size_y == 0 ) frame_size_y = def_frame_size_y;
		if ( frame_loc_x == 0  ) frame_loc_x  = def_frame_loc_x;
		if ( frame_loc_y == 0  ) frame_loc_y  = def_frame_loc_y;
		
		if ( loglevel == null ) loglevel = "info";
	}
	
	public int getFrame_size_x() {
		return frame_size_x;
	}

	public void setFrame_size_x(int frame_size_x) {
		this.frame_size_x = frame_size_x;
	}

	public int getFrame_size_y() {
		return frame_size_y;
	}

	public void setFrame_size_y(int frame_size_y) {
		this.frame_size_y = frame_size_y;
	}

	public int getFrame_loc_x() {
		return frame_loc_x;
	}

	public void setFrame_loc_x(int frame_loc_x) {
		this.frame_loc_x = frame_loc_x;
	}

	public int getFrame_loc_y() {
		return frame_loc_y;
	}

	public void setFrame_loc_y(int frame_loc_y) {
		this.frame_loc_y = frame_loc_y;
	}

	public String getMarked_appid() {
		return marked_appid;
	}

	public void setMarked_appid(String marked_appid) {
		this.marked_appid = marked_appid;
	}

	public String getMarked_instid() {
		return marked_instid;
	}

	public void setMarked_instid(String marked_instid) {
		this.marked_instid = marked_instid;
	}

	public String getMarked_jobid() {
		return marked_jobid;
	}

	public void setMarked_jobid(String marked_jobid) {
		this.marked_jobid = marked_jobid;
	}

	public String getLoglevel() {
		return loglevel;
	}

	public void setLoglevel(String loglevel) {
		this.loglevel = loglevel;
	}

	public int getGuiSize() {
		return guisize;
	}

	public void setGuiSize(String in_guisize) {
		try {
			guisize = Commons.string2int(in_guisize);
		} catch (Exception e) {
			Sp.p("Problem on setting guisize "+in_guisize);
			guisize = 0;
		}
	}
	public void setGuiSize(int in_guisize) {
		guisize = in_guisize;
	}	
	
	public void setAuto ( boolean auto ) {
		this.auto = auto;
	}
	
	public boolean getAuto (  ) {
		return (auto);
	}	

	public void writeFile () throws IOException, StorageLimitException {
		
		Element ep = new Element ( "perspective" );
		ep.setText(perspective);
		
		Element ef = new Element ("frame");
		ef.setAttribute("size_x", Integer.toString(frame_size_x));
		ef.setAttribute("size_y", Integer.toString(frame_size_y));	
		ef.setAttribute("loc_x", Integer.toString(frame_loc_x));
		ef.setAttribute("loc_y", Integer.toString(frame_loc_y));
		
		Element em = new Element ("marked");
		if ( marked_appid != null ) em.setAttribute("appid", marked_appid);	
		if ( marked_instid != null ) em.setAttribute("instid", marked_instid);
		if ( marked_jobid != null ) em.setAttribute("jobid", marked_jobid);
		
		Element el = new Element ("logging");
		el.setAttribute("loglevel",loglevel);
		
		Element egs = new Element ("guisize");
		egs.setText(Integer.toString(guisize));

		Element egauto = new Element ("auto");
		egauto.setText(Commons.boolToStringXr(auto));		

		Element root = new Element ("guistatus");
		root.addContent(ep);
		root.addContent(ef);
		root.addContent(em);
		root.addContent(el);
		root.addContent(egs);
		root.addContent(egauto);
		root.addContent(expanded_nodes.clone());
		
		Document document = new Document();
		document.setRootElement(root);
		
		CommonsVfs.writeToFilePretty(document, filename,sitzung);		
			
	}
	

}