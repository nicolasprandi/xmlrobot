/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class CoopConnection implements ITask, ICoop {

	public String uri=null;
	String schema = null;
	FileSystemOptions fileSystemOptions = null;
	FileObject fileObject = null;
	FileName fileName=null;
	String connectionString = null;
			
	IRequestHandler rh;
		
	String idE;
		
	public FileObject getFileObject() {
		return fileObject;
	}
	public FileName getFileName() {
		return fileName;
	}	

	
	public Parameters execute(Element eN, IRequestHandler rh) throws Exception {
		
		if ( eN.getAttribute("act") == null ) {
			throw new Exception("connection needs attribute @act (open,close)");
		}
		
		if ( eN.getAttribute("id") == null ) {
			throw new Exception("connection needs attribute @id");
		}		
		
		String action = eN.getAttributeValue("act");
		idE = eN.getAttributeValue("id"); // ??nic321
		
		this.rh = rh;
				
		Parameters retParas = null;
		
		
		if ( action.equals("open")) {
			
			if ( eN.getAttribute("dir") == null ) {
				throw new Exception("connection needs attribute @dir");
			}				
			// uri = Commons.introduceDirAsUriString(eN.getAttributeValue("uri")).string;
			uri = eN.getAttributeValue("dir");
			schema = Commons.getSchema(uri);
			connectionString = eN.getAttributeValue("conn_string");
			
			fileSystemOptions = getFileSystemOptions(schema, connectionString);
			fileObject = CommonsVfs.grantsFileRead(uri, fileSystemOptions,rh.getSitzung());
			fileName = fileObject.getName();
			
			if ( CommonsVfs.getTimeOfFileObject(fileObject,rh.getSitzung()) > CommonsVfs.getTimeOfDate("20300101")) {
				throw new Exception("mist");
			}		
			rh.putNo(idE, Wellknown.coop_type_connection,  this);
			//_lc.log_ext("Opened "+idE, this);				
					
		} else {
			String id = eN.getAttributeValue("id");
			CoopConnection conn = (CoopConnection)rh.getNo(id, Wellknown.coop_type_connection);
			if ( conn == null ) 
				throw new Exception("The connection with id '"+id+"' was not found");

			if ( action.equals("xml_update")){
				/*
				Xml_Update xu = new Xml_Update();
				Element currentElement = null;
				if ( eN.getAttribute("where_is_loop_xpath")!=null) {
					throw new Exception("attribute 'where_is_loop_xpath' not supported, use 'where_is_loop_xml'");
				}
				
				if ( eN.getAttribute("where_is_loop_xml")!=null) {
					currentElement = CoopLx.getCurrentElement(rh, eN.getAttributeValue("where_is_loop_xml"),this);
				} 

				int countRet = xu.executeSpirit(spirit.root, currentElement,eN, rh, _lc);
				 if ( eN.getAttribute("count_prop_id") != null ) {
					 retParas = new Parameters();
					 retParas.setParameter(eN.getAttributeValue("count_prop_id"),
							 Integer.toString(countRet));
				 } 
				*/
			} else if ( action.equals("save")){

			}else if ( action.equals("close")){
				
				rh.removeNo(id, Wellknown.coop_type_connection);
				
				//_lc.log_ext("Closed "+id, this);
			} else
				throw new Exception("print attribute @act must be open or close");
			
		}
		 
		return retParas;
	}
	




	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		
	}
	

	private FileSystemOptions getFileSystemOptions (String scema, String connections_string ) throws Exception {
		if ( connections_string == null|| connections_string.length() == 0 )
			return new FileSystemOptions();
		Parameters p = new Parameters ( connections_string,"'" );
		FileSystemOptions opts = new FileSystemOptions();
		if ( scema.equals("ftp")) {
			
			if ( p.getParameter("ftp_PassiveMode") != null ) {
				boolean ftp_PassiveMode = Commons.stringxr2bool(p.getParameter("ftp_PassiveMode"));
				FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, ftp_PassiveMode);
			}
			
			if ( p.getParameter("ftp_UserDirIsRoot") != null ) {
				boolean ftp_UserDirIsRoot = Commons.stringxr2bool(p.getParameter("ftp_UserDirIsRoot"));
				FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, ftp_UserDirIsRoot);
			}			
			
		}
		
		return opts;
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}