/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.UserInfo;


public class RequestHandlerDummy implements IRequestHandler {

	@Override
	public String getTempDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateValueSave(String workon) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getPoiClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getAntClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pleaseRememberMe(ITask itask) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* @Override
	public void pleaseRememberMe(IFun ifun) throws Exception {
		// TODO Auto-generated method stub
		
	} */

	/*@Override
	public boolean executeTiFromTask(Element e) {
		// TODO Auto-generated method stub
		return false;
	}*/

	@Override
	public void terminalProgress(String msg, boolean do_log_info,  ITask from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldBreakOnNextOccations() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBreakLoops() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMac() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLinux() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesExistVariable(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public String getVariableValue(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Date getJobdate() {
		// TODO Auto-generated method stub
		return new Date();
	}

	@Override
	public void depressive(String message) {
		Sp.p("Depressive: "+message);		
	}

	@Override
	public boolean isJovial() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void terminalProgress(String msg, ITask from) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getMathScale() {
		// TODO Auto-generated method stub
		return 10;
	}


	@Override
	public int getMathRoundMethod() {
		// TODO Auto-generated method stub
		return BigDecimal.ROUND_HALF_UP;
	}

	@Override
	public String sequenceFun(String paraN1, String paraN2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVar(String varType, String varIdE, String valueE) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSpirit(String spirit_id, Element xmlElement, IRequestHandler rh, ILogConnector lc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doesExistCoop(String internal_type, String id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileSystemManager getFsm()  {
		try {
			return VFS.getManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			return null;
		}
	}

	@Override
	public FileObject[] getChildDirs(String directoryname) throws FileSystemException {
		// TODO Auto-generated method stub
		return null;
		/*
		FileObject fo = CommonsVfs.getFileObjectByFilename(directoryname);
		return ( fo.getChildren()); */
	}


	@Override
	public FileObject[] getChildDirs(FileObject fileObject) throws FileSystemException {
		// TODO Auto-generated method stub
		return ( fileObject.getChildren());
	}

	@Override
	public IGuiController getGuiController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String request(String type, String keyname) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void printTrace(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRoleOverall() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getVariableValue2(String type, String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ILogConnector getLogConnector() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String translateValue(String workon) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ISitzung getSitzung() {
		// TODO Auto-generated method stub
		return null;
	}


	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPriv(String requestedPriv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPrivPara(String requestedPrivPara) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean triggerButtonsByButton() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAppId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addToStackSimple(List<Element> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runRhJob(String job, Element parameters) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getTurbo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putNo(String idN, String type,  ICoop object) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICoop getNo(String idN, String type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeNo(String id, String type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putNoApp(String idN, String type,  ICoop object, String app) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICoop getNoApp(String idN, String type, String app) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeNoApp(String id, String type, String app) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putNo(String idN, String type, String type_string, ICoop object,Object client) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICoop getNo(String idN, String type, Object client) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeNo(String id, String type, Object client) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doesExistCoopApped(String internal_type, String id, String app) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


}