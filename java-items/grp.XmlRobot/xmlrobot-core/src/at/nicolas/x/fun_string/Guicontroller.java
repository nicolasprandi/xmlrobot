/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Guicontroller implements IFun{

	private static final String funn= "guicontroller";
	private static final String pm= "function 'guicontroller' ";
	/*
	 * 1: action:     choose
	 * 2: type:       file|dir
	 * 3: requestor:  is-on
	 *                write-on
	 *                get-tip
	 *                get-label
	 *                get-extensions
	 *                  
	 * (non-Javadoc)
	 * @see at.nicolas.x.IFun#execute(java.util.ArrayList, at.nicolas.x.IRequestHandler, at.nicolas.x.ILogConnector)
	 */
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() != 3 ) 
			throw new LangException("I want 3 parameters, 1=action , 2=type, 3=request");

		String action = ((Vcar)vcar_list.get(0)).s;
		String type   = ((Vcar)vcar_list.get(1)).s;
		String request   = ((Vcar)vcar_list.get(2)).s;
		
		IGuiController guiController = rh.getGuiController();
		if ( guiController == null ) {
			
			String retProb = "";
			if ( request.equals("is-on") || request.equals("is-save") ) {
				retProb = "0";
			}
			return new Vcar(retProb);
		}

		Vcar retu = null;
		if ( action.equals("choose")) {
			if ( type.equals("file")) {
				// file
				if ( request.equals("is-on")) retu = new Vcar(guiController.chooseFileOn());
				else if ( request.equals("has-save")) retu = new Vcar(guiController.chooseSaveFile());
				else if ( request.equals("label")) retu = new Vcar(emtpyIfNull (guiController.getChooseLabel()));
				else if ( request.equals("tip")) retu = new Vcar(emtpyIfNull (guiController.getChooseTip()));
				else if ( request.equals("has-extension")) retu = new Vcar(guiController.getHasFileExtension());
				else if ( request.equals("extension")) retu = new Vcar(emtpyIfNull (guiController.getFileExtension()));
				else if ( request.equals("proposed-fn")) retu = new Vcar(emtpyIfNull (guiController.getFileProposedFilename()));
				else if ( request.equals("has-value")) retu = new Vcar(guiController.hasFileCurVal());
				else if ( request.equals("value")) retu = new Vcar(emtpyIfNull (guiController.getFileCurVal())); 
				else throw new Exception("unknown request: "+request);
			} else if ( type.equals("dir") ){
				// dir
				if ( request.equals("is-on")) retu = new Vcar(guiController.chooseDirOn());
				else if ( request.equals("has-save")) retu = new Vcar(guiController.chooseSaveFile());
				else if ( request.equals("label")) retu = new Vcar(emtpyIfNull (guiController.getChooseLabel()));
				else if ( request.equals("tip")) retu = new Vcar(emtpyIfNull (guiController.getChooseTip()));
				else if ( request.equals("has-extension")) retu = new Vcar(guiController.getHasFileExtension());
				else if ( request.equals("extension")) retu = new Vcar(emtpyIfNull (guiController.getFileExtension()));
				else if ( request.equals("proposed-fn")) retu = new Vcar(emtpyIfNull (guiController.getFileProposedFilename()));
				else if ( request.equals("has-value")) retu = new Vcar(guiController.hasFileCurVal());
				else if ( request.equals("value")) retu = new Vcar(emtpyIfNull (guiController.getFileCurVal())); 
				else throw new Exception("unknown request: "+request);
			}
			else
				throw new Exception("type: "+type+"??");
		} else if ( action.equals("general")) {
			if ( type.equals("is-web")) {
				retu = new Vcar(guiController.isSocalledWeb());
			}
			
		}/* else if ( action.equals("may")) {
			if ( type.equals("operate-file")) {				
				retu = new Vcar(guiController.mayOperateFiles());
			} else if ( type.equals("operate-dir")) {				
				retu = new Vcar(guiController.mayOperateDirs());
			}
			
		} */
		
		return retu;

	}

	private String emtpyIfNull ( String in ) {
		if ( in== null) return "";
		else return in;
	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}