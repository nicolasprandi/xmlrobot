/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.term_map.Map_JobButtonsSc;

public class Tc_GuiSizer {
	private static String myfont = "Serif";
	private static String tkfont = "Arial";
	private static int buttonstyle = Font.BOLD;
	private static int appProLabelButton = Font.PLAIN;
	private static int buttonstyleInfo = Font.ITALIC+ Font.BOLD;
	private static String body_padding =  "padding-left: 3px;padding-right: 3px;";	
	int size = 0;
	int size_offset = 0;
	public Font dbFont_tablet = new Font(myfont,0 , 23);
	public Font dbFontApps_tablet = new Font(myfont,0 , 23);
	
	public Font dbFont = new Font(myfont,0 , 14);
	public Font dbFontApps = new Font(myfont,0 , 15);
	public int  dbRowHeight = 18;
	public Font jobButtonFont = new Font(myfont,buttonstyle , 14);
	public Font tkFont = new Font(tkfont,Font.PLAIN , 13);
	public Font tkFontTablet = new Font(tkfont,Font.PLAIN,20);
	public Font tkFontOff = new Font(tkfont,Font.BOLD , 13);
	public Font jobButtonFontSmall = new Font(myfont,Font.PLAIN , 11);
	public Font jobButtonViewFont = new Font(myfont,Font.PLAIN , 14); // Application Help
	public Font proInfoButtonFont = new Font(myfont,buttonstyle , 14);
	public Font buttonAppProTechInfo = new Font(myfont,appProLabelButton , 12); // App & Pro Tech Info
	public Font proInfoFont = new Font(myfont,buttonstyleInfo , 16); // Project Info
	public Font applicationStatus = new Font(myfont,buttonstyle , 12);
	public Font serviceButtonFont = new Font(myfont,buttonstyle , 11);
	public Font serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 10);
	public Font logSmallFont = new Font("Courier",0 , 11);
	public Font logBigFont = new Font("Courier",0 , 12);
	public Color grau = Color.green;
	public int tpFontSize = 16;
	
	public Dimension normalBestButtonDim = new Dimension(140,25);
	public Dimension tabletBestButtonDim = new Dimension(140,70);	
	public Dimension targetBestButtonDim = new Dimension(45,23);	
	public int tkSumLabelWidth = 246;	
	public int tabletSizeadder = 140;
	public int normalSizeadder = 0;	
	/*
	public Dimension dim_100_100 = new Dimension(100,100);
	public Dimension dim_200_250 = new Dimension(200,250);
	public Dimension dim_500_100 = new Dimension(500,100);
	public Dimension dim_300_600 = new Dimension(300,600);
	public Dimension dim_400_100 = new Dimension(400,100);
	public Dimension dim_200_260 = new Dimension(200,260);
	public Dimension dim_200_600 = new Dimension(200,600);
	public Dimension dim_260_600 = new Dimension(260,600);
	public Dimension dim_260_400 = new Dimension(260,400);
	public Dimension dim_260_200 = new Dimension(260,200);
	public Dimension dim_260_230 = new Dimension(260,230);
	public Dimension dim_300_400 = new Dimension(300,400);
	public Dimension dim_100_400 = new Dimension(100,400);
	*/
	public int value100_scaled = 100;
	/*
	public int value_100 = 100;
	public int value_160 = 160;
	public int value_200 = 200;
	public int value_300 = 300;
	public int value_400 = 400;
	public int value_500 = 500;
	public int value_520 = 520;
	public int value_600 = 600;
	public int value_260 = 260;
	public int value_250 = 250;
	*/
	
	private int leftPanelWidthStd = 246;
	private int leftPanelWidthTablet = 280;
	public void setTabletServicesSize(Map_JobButtonsSc mapScButtons) {
		if ( CoreBuilder.ws.isTablet ) {
			if      ( size == 0 ) mapScButtons.getJComponent().setPreferredSize(new Dimension(150,100));
			else if ( size == 1 ) mapScButtons.getJComponent().setPreferredSize(new Dimension(160,100));
			else if ( size == 2 ) mapScButtons.getJComponent().setPreferredSize(new Dimension(170,100));
			else if ( size == 3 ) mapScButtons.getJComponent().setPreferredSize(new Dimension(180,100));
			else                  mapScButtons.getJComponent().setPreferredSize(new Dimension(190,100));
		}
	}
	
	public int getLeftPanelWidth() {
		
		if ( CoreBuilder.ws.isTablet )
			return leftPanelWidthTablet;
		else
			return leftPanelWidthStd;
	}
	/*
	public int getLeftPanelWidthSmall() {
		if ( CoreBuilder.ws.isTablet )
			return 280;
		else
			return 246;
	}	
	*/
	public Font tabletFontToolbar = new Font("Dialog",Font.PLAIN,16);
	
	public int stdToolbarHeight = 22;
	public int tabletToolbarHeight = 28;
	
	// public String tiBodyTag = "<body style='font-size:medium;'>";
	public String tiBodyTag = "<body style='font-size:medium;"+body_padding+"'>";
	
	public double px_scale = 1;
	
	public String scalePx( String in ) {
		if (in.endsWith("px")) {
			String v1 = in.substring(0, in.length()-2);
			int v2 = Integer.parseInt(v1);
			double v3 = v2*px_scale;
			int v4 = (int)v3;
			String v5 = Integer.toString(v4)+"px";
			// Sp.p("SCALE ("+size+"): "+in+" -> "+v5 );
			return v5;
			
		} else
			return in;
	}
	public int scaleSize( int in ) {
		int ret = in + size_offset;
		return ret;
	}
	
	public void setSize( int size ) {
		this.size = size;
		boolean linux = false;
		if ( CoreBuilder.ws.getOs().equals("linux"))
			linux = true;
		
		if ( size == 0 ) {
			size_offset = 0;

			dbFont = new Font(myfont,0 , 15);
			dbFontApps = new Font(myfont,0 , 16);

			dbRowHeight = 18;
			jobButtonFont = new Font(myfont,buttonstyle , 15);
			proInfoFont = new Font(myfont,buttonstyleInfo , 15);
			tkFont = new Font(tkfont,Font.PLAIN , 13);
			tkFontOff = new Font(tkfont,Font.BOLD , 13);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 11);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 13);
			//buttonAppProTechInfo.set
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 13);
			proInfoButtonFont = new Font(myfont,Font.ITALIC , 14);
			serviceButtonFont = new Font(myfont,buttonstyle , 11);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 10);
			
			logSmallFont = new Font("Courier",0 , 12);
			logBigFont = new Font("Courier",0 , 13);	
			
			tiBodyTag = "<body style='font-size:medium;"+body_padding+"'>";
			if ( linux )
				px_scale = 1.2;
			else
				px_scale = 1;
			leftPanelWidthStd = 246;
			leftPanelWidthTablet = 280;
			value100_scaled = 100;
			tpFontSize = 16;
			
			normalBestButtonDim = new Dimension(140,25);
			tabletBestButtonDim = new Dimension(140,70);	
			targetBestButtonDim = new Dimension(45,23);	
			tkSumLabelWidth = 246;	
			tabletSizeadder = 140;
			normalSizeadder = 0;					
			
		} else if ( size == 1 ) {
			size_offset = 1;
			dbFont = new Font(myfont,0 , 17);
			dbFontApps = new Font(myfont,0 , 18);
			dbRowHeight = 20;
			jobButtonFont = new Font(myfont,buttonstyle , 16);
			proInfoFont = new Font(myfont,buttonstyleInfo , 16);
			tkFont = new Font(tkfont,Font.PLAIN , 14);
			tkFontOff = new Font(tkfont,Font.BOLD , 14);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 12);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton, 14);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 14);
			proInfoButtonFont = new Font(myfont,buttonstyle , 14);
			serviceButtonFont = new Font(myfont,buttonstyle , 12);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 11);
			
			logSmallFont = new Font("Courier",0 , 12);
			logBigFont = new Font("Courier",0 , 13);		
			
			tiBodyTag = "<body style='font-size:large;"+body_padding+"'>";

			if ( linux )
				px_scale = 1.4;
			else			
				px_scale = 1.2;
			
			leftPanelWidthStd = 246;
			leftPanelWidthTablet = 280;	
			value100_scaled = 100;
			tpFontSize = 16;
			
			normalBestButtonDim = new Dimension(150,28);
			tabletBestButtonDim = new Dimension(157,82);	
			targetBestButtonDim = new Dimension(48,23);	
			tkSumLabelWidth = 265;	
			tabletSizeadder = 163;
			normalSizeadder = 17;	
			
		} else if ( size == 2 ) {
			size_offset = 2;
			dbFont = new Font(myfont,0 , 18);
			dbFontApps = new Font(myfont,0 , 19);
			dbRowHeight = 21;
			jobButtonFont = new Font(myfont,buttonstyle , 17);
			proInfoFont = new Font(myfont,buttonstyleInfo , 17);
			tkFont = new Font(tkfont,Font.PLAIN , 15);
			tkFontOff = new Font(tkfont,Font.BOLD , 15);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 13);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 15);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 15);
			proInfoButtonFont = new Font(myfont,buttonstyle , 15);
			serviceButtonFont = new Font(myfont,buttonstyle , 13);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 12);
			
			logSmallFont = new Font("Courier",0 , 13);
			logBigFont = new Font("Courier",0 , 14);	
			
			tiBodyTag = "<body style='font-size:large;"+body_padding+"'>";

			if ( linux )
				px_scale = 1.4;
			else			
				px_scale = 1.2;
			
			leftPanelWidthStd = 248;
			leftPanelWidthTablet = 284;		
			value100_scaled = 102;
			tpFontSize = 17;
						
			normalBestButtonDim = new Dimension(160,31);
			tabletBestButtonDim = new Dimension(173,93);	
			targetBestButtonDim = new Dimension(50,24);	
			tkSumLabelWidth = 284;	
			tabletSizeadder = 187;
			normalSizeadder = 33;			
			
		}  else if ( size == 3 ) {
			size_offset = 3;
			dbFont = new Font(myfont,0 , 25);
			dbFontApps = new Font(myfont,0 , 22);
			dbRowHeight = 23;
			jobButtonFont = new Font(myfont,buttonstyle , 18);
			proInfoFont = new Font(myfont,buttonstyleInfo , 18);
			tkFont = new Font(tkfont,Font.PLAIN , 16);
			tkFontOff = new Font(tkfont,Font.BOLD , 16);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 14);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 16);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 16);
			proInfoButtonFont = new Font(myfont,buttonstyle , 16);
			serviceButtonFont = new Font(myfont,buttonstyle , 14);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 13);
			
			logSmallFont = new Font("Courier",0 , 14);
			logBigFont = new Font("Courier",0 , 15);	
			
			tiBodyTag = "<body style='font-size:x-large;"+body_padding+"'>";
			
			if ( linux )
				px_scale = 1.8;
			else			
				px_scale = 1.6;

			leftPanelWidthStd = 250;
			leftPanelWidthTablet = 286;		
			value100_scaled = 104;
			tpFontSize = 18;
			
			normalBestButtonDim = new Dimension(170,34);
			tabletBestButtonDim = new Dimension(190,105);	
			targetBestButtonDim = new Dimension(53,24);	
			tkSumLabelWidth = 303;	
			tabletSizeadder = 210;
			normalSizeadder = 50;			
			
		}  else if ( size == 4 ){
			size_offset = 4;
			dbFont = new Font(myfont,0 , 30);
			dbFontApps = new Font(myfont,0 , 26);
			dbRowHeight = 25;
			jobButtonFont = new Font(myfont,buttonstyle , 21);
			proInfoFont = new Font(myfont,buttonstyleInfo , 21);
			tkFont = new Font(tkfont,Font.PLAIN , 19);
			tkFontOff = new Font(tkfont,Font.BOLD , 19);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 16);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 17);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 17);
			proInfoButtonFont = new Font(myfont,buttonstyle , 17);
			serviceButtonFont = new Font(myfont,buttonstyle , 18);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 15);
			
			logSmallFont = new Font("Courier",0 , 16);
			logBigFont = new Font("Courier",0 , 17);	
			
			tiBodyTag = "<body style='font-size:24px;"+body_padding+"'>";

			if ( linux )
				px_scale = 1.9;
			else				
				px_scale = 1.7;
			
			leftPanelWidthStd = 252;
			leftPanelWidthTablet = 288;	
			value100_scaled = 112;
			tpFontSize = 19;
			
			normalBestButtonDim = new Dimension(180,36);
			tabletBestButtonDim = new Dimension(207,117);	
			targetBestButtonDim = new Dimension(55,24);	
			tkSumLabelWidth = 322;	
			tabletSizeadder = 233;
			normalSizeadder = 67;	
			
		}  else if ( size == 5 ){
			size_offset = 5;
			dbFont = new Font(myfont,0 , 34);
			dbFontApps = new Font(myfont,0 , 30);
			dbRowHeight = 28;
			jobButtonFont = new Font(myfont,buttonstyle , 23);
			proInfoFont = new Font(myfont,buttonstyleInfo , 23);
			tkFont = new Font(tkfont,Font.PLAIN , 20);
			tkFontOff = new Font(tkfont,Font.BOLD , 20);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 19);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 19);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 21);
			proInfoButtonFont = new Font(myfont,buttonstyle , 21);
			serviceButtonFont = new Font(myfont,buttonstyle , 21);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 19);
			
			logSmallFont = new Font("Courier",0 , 19);
			logBigFont = new Font("Courier",0 , 20);	
			
			tiBodyTag = "<body style='font-size:26px;"+body_padding+"'>";

			if ( linux )
				px_scale = 2.1;
			else				
				px_scale = 2.1;
			
			leftPanelWidthStd = 254;
			leftPanelWidthTablet = 290;			
			
			value100_scaled = 114;
			tpFontSize = 21;
			
			normalBestButtonDim = new Dimension(190,39);
			tabletBestButtonDim = new Dimension(223,128);	
			targetBestButtonDim = new Dimension(58,25);	
			tkSumLabelWidth = 341;	
			tabletSizeadder = 257;
			normalSizeadder = 83;	
			
		} else {
			size_offset = 6;
			dbFont = new Font(myfont,0 , 34);
			dbFontApps = new Font(myfont,0 , 33); //35
			dbRowHeight = 34;
			jobButtonFont = new Font(myfont,buttonstyle , 28);
			proInfoFont = new Font(myfont,buttonstyleInfo , 28);
			tkFont = new Font(tkfont,Font.PLAIN , 27);
			tkFontOff = new Font(tkfont,Font.BOLD , 27);
			jobButtonFontSmall = new Font(myfont,Font.PLAIN , 23);
			buttonAppProTechInfo = new Font(myfont,appProLabelButton , 24);
			jobButtonViewFont = new Font(myfont,Font.PLAIN , 25);
			proInfoButtonFont = new Font(myfont,buttonstyle , 25);
			serviceButtonFont = new Font(myfont,buttonstyle , 18);
			serviceButtonFontSmall = new Font(myfont,Font.PLAIN , 25);
			
			logSmallFont = new Font("Courier",0 , 25);
			logBigFont = new Font("Courier",0 , 26);	
			
			tiBodyTag = "<body style='font-size:26px;"+body_padding+"'>";

			if ( linux )
				px_scale = 2.5;
			else				
				px_scale = 2.5;
			
			leftPanelWidthStd = 300; //276;
			leftPanelWidthTablet = 315;		
			value100_scaled = 120;
			tpFontSize = 23;
			
			normalBestButtonDim = new Dimension(200,42);
			tabletBestButtonDim = new Dimension(240,140);	
			targetBestButtonDim = new Dimension(60,25);	
			tkSumLabelWidth = 360;	
			tabletSizeadder = 280;
			normalSizeadder = 100;	
			
		}
		/*
		if ( CoreBuilder.ws.isTablet) {
			dbFont = dbFont_tablet;
			dbFontApps = dbFontApps_tablet;
		} */
		
		
	}

}