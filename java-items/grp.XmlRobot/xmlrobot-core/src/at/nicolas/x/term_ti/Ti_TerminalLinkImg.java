/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import org.apache.commons.httpclient.util.URIUtil;
import org.jdom2.Element;

import at.nicolas.x.image.ImageInfo;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;



public class Ti_TerminalLinkImg extends Ti_TerminalLink {

	Element eE;
	ISitzung sitzung;
	public Ti_TerminalLinkImg(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir, inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
		sitzung = rh.getSitzung();
	}

	public static String getImgHtml(Element eE, ITiHandler rh ) {
		String altText = "Bild";
		if ( eE.getAttribute("alt") != null )
			altText = eE.getAttributeValue("alt");
		else if ( eE.getAttribute("explain") != null )
			altText = eE.getAttributeValue("explain");	
		else if ( eE.getAttribute("job") != null )
			altText = eE.getAttributeValue("job");		
		return getImgHtml( eE,  rh,  altText );
		
	}

	public static String getImgHtml(Element eE, ITiHandler rh, String alternativeTextEscaped ) {
		return getImgHtml( eE,  rh,  alternativeTextEscaped,
				eE.getAttributeValue("pic") );
		
	}
	public static String getImgHtml(Element eE, ITiHandler rh, String alternativeTextEscaped,
			String filenameOfPictureE ) {
		String myAltE ;
		if ( alternativeTextEscaped != null )
			myAltE = alternativeTextEscaped;
		else if ( eE.getAttribute("explain") != null )
			myAltE = eE.getAttributeValue("explain");
		else
			myAltE = "Picture-Bild-Image";
		
		String imgsrc;
		
		try {
			String derInhalt = Entities.exportEntities(filenameOfPictureE);
			// CommonsVfs.grants(derInhalt, rh.getSitzung());
			//System.out.println("--X--  derInhalt   = "+derInhalt);
			String rel = rh.getSitzung().getRelativeLibroPath(derInhalt);
			//System.out.println("--X--  rel         = "+rel);
			String pathEncoded=URIUtil.encodePath(rel);
			//System.out.println("--X--  pathEncoded = "+pathEncoded);

			imgsrc = pathEncoded;
			//System.out.println("image="+imgsrc);
			String alt =" alt=\""+Commons.text_to_html(myAltE)+"\" ";
				
			
			// style='height: auto; width: auto;
			String sizer ="" ;
			if ( rh.getSitzung().isOnline()) {
				if ( ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0 ) || 
					 ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) ) {
					sizer = " style=\" height: auto; width: auto; ";
					if ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0) {
						sizer = sizer + "max-height: "+eE.getAttributeValue("max_y")+"px;";
					}
					if ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) {
						sizer = sizer + "max-width: "+eE.getAttributeValue("max_x")+"px;";
					}				
					sizer = sizer + "\" ";
					
				}		
			} else {
				ImageInfo imageInfo = new ImageInfo(derInhalt,rh.getSitzung());
				imageInfo.scale(eE.getAttributeValue("max_x"), eE.getAttributeValue("max_y"));
				sizer = " height=\""+Commons.int2String(imageInfo.getHeightScaled())+"\" "+
						" width=\""+Commons.int2String(imageInfo.getWidthScaled())+"\" ";				
			}
			// System.out.println("sizer="+sizer);
			String cssClass ="" ;
			if (  eE.getAttribute("class_img") != null && eE.getAttributeValue("class_img").length() > 0 )
					cssClass = " class=\""+eE.getAttributeValue("class_img")+"\" ";
				
			return "<img src='"+imgsrc+"' "+alt+sizer+cssClass+">";				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			return "<p>problem: "+e.getMessage()+"</p>";
		}		
	}
	public Ti_HtmlReturn getHtml() {
		
		String imgsrc;
		
		try {
			String derInhalt = Ti_TerminalLink.getXUnescaped(eE,rh);
			CommonsVfs.grantsFileRead(derInhalt, sitzung);
			//System.out.println("--X--  derInhalt   = "+derInhalt);
			String rel = rh.getSitzung().getRelativeLibroPath(derInhalt);
			//System.out.println("--X--  rel         = "+rel);
			String pathEncoded=URIUtil.encodePath(rel);
			//System.out.println("--X--  pathEncoded = "+pathEncoded);
			//CommonsVfs.constructNormalizedDir(filename)
			//Commons.decodeXmlEntitiesXr(in)
			// imgsrc = new File(Ti_TerminalLink.getXUnescaped(eE,rh)).toURI().toURL().toExternalForm();
			//imgsrc = Commons.fileClassic(imgsrc);
			imgsrc = pathEncoded;
			//System.out.println("image="+imgsrc);
			/*
			String alt ="" ;
			if ( eE.getAttribute("alt") != null && eE.getAttributeValue("alt").length() > 0 ) {
				alt = " alt=\""+Commons.text_to_html(eE.getAttributeValue("alt"))+"\" ";
				
			}*/
			// style='height: auto; width: auto;
			/*
			String sizer ="" ;
			if ( ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0 ) || 
				 ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) ) {
				sizer = " style=\" height: auto; width: auto; ";
				if ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0) {
					sizer = sizer + "max-height: "+eE.getAttributeValue("max_y")+"px;";
				}
				if ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) {
					sizer = sizer + "max-width: "+eE.getAttributeValue("max_x")+"px;";
				}				
				sizer = sizer + "\"";
				
			}	
			*/		
			String imageHtml = getImgHtml(eE, rh, eE.getAttributeValue("alt"), Ti_TerminalLink.getX(eE, rh) );
			Ti_HtmlReturn ret = new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						//"<img src='"+imgsrc+"' "+alt+sizer+">",
						imageHtml,
						null);

			ret.setKeyLabelRight(eE,rh.getSitzung());		
			return ret;				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			return new Ti_HtmlReturn(
					"problem 4713",
					"problem 4714:"+e.getMessage(),
						null);
		}
		
		/*
		try {			
			imgsrc = Commons.fileClassic(Ti_TerminalLink.getXUnescaped(eE,rh));
			System.out.println("image="+imgsrc);
		} catch (Exception e) {
			Sp.pe(e);
			imgsrc=null;
		} */
		

				
	}
	

	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		return Ti_TerminalLink.getXUnescaped(eE,rh);
	}

	@Override
	public void executeLink() {

	}



	@Override
	public Interpreter createInterpreterToReact(String mark,
			boolean interWebModus,IGuiController guiController, ISitzung isitzung ) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}