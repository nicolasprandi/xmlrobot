/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.jdom2.Element;


public interface IRequestHandler {

	public boolean getTurbo();
	public boolean triggerButtonsByButton();
	public ISitzung getSitzung();
	public IGuiController getGuiController();
	public void printTrace(String msg );
	
	// Interface: Variablen

	public int getRoleOverall();
	public String request(String type, String keyname) throws Exception;
	public  void setVar(String varType, String varIdE, String valueE) throws Exception;
	public String  translateValueSave( String workon ) throws Exception;
	public String  translateValue( String workon ) throws Exception;
	public boolean doesExistVariable(String type, String id)throws Exception;
	public boolean doesExistCoop(String internal_type, String id)throws Exception;
	public boolean doesExistCoopApped(String internal_type, String id, String app)throws Exception;
	public String  getVariableValue(String type, String id )throws Exception;
	public String  getVariableValue2(String type, String id )throws Exception;
	public Date    getJobdate();
	public String  getTempDir ();
	boolean isJovial ();

	public String sequenceFun(String paraN1, String paraN2 ) throws Exception;
	
	public void createSpirit (String spirit_id, Element xmlElement, IRequestHandler rh, ILogConnector lc) throws Exception;
	
	// Interface: Classloaders
	public ClassLoader getPoiClassLoader();	
	public ClassLoader getAntClassLoader();	
	
	// Interface: Remember
	public void pleaseRememberMe( ITask itask ) throws Exception;
	
	// Interface: Terminal
	public void    terminalProgress ( String msg, boolean do_log_info, ITask from );
	public void    terminalProgress ( String msg, ITask from );
	public boolean shouldBreakOnNextOccations();
	public boolean shouldBreakLoops();
	
	// Interface: Named Objects, for storage of cooperations
	// named objects agieren alle auf natural basis
	public void  putNo ( String idN, String type,  ICoop object) throws Exception;
	public void  putNo ( String idN, String type, String type_string, ICoop object, Object client) throws Exception;
	
	public ICoop getNo ( String idN, String type ) throws Exception;
	public ICoop getNo ( String idN, String type, Object client ) throws Exception;
	public void  removeNo ( String id, String type ) throws Exception;
	public void  removeNo ( String id, String type, Object client ) throws Exception;
	public void  putNoApp ( String idN, String type, ICoop object,  String app ) throws Exception;
	public ICoop getNoApp ( String idN, String type,  String app  ) throws Exception;
	public void  removeNoApp ( String id, String type,  String app  ) throws Exception;

	// Interface: Computer
	public boolean isMac();
	public boolean isLinux();

	// Interface: Depression
	public void depressive(String message);
	public void addToStackSimple( List<Element> list );
	
	// Math
	public int getMathScale();
	public int getMathRoundMethod();
	
	// VFS
	public FileSystemManager getFsm();	
	
	// Security
	public  FileObject[] getChildDirs(String directoryname) throws FileSystemException;
	
	public  FileObject[] getChildDirs(FileObject fileObject) throws FileSystemException;
	//public  UserInfo getUserInfo();
	public  ILogConnector getLogConnector();
	
	public boolean hasPriv(String requestedPriv);
	public String getPrivPara(String requestedPrivPara);
	public String getAppId( ) throws Exception;
	
	public void runRhJob ( String job, Element parameters ) throws Exception;
}