/* Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import org.jdom2.Element;

import at.nicolas.x.int_setup.Core;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;

public class DbPro implements Comparable<DbPro>{
	private String id ;
	private String dir;
	private String idEscaped ;
	private String dirEscaped;
	private boolean online;
	private ISitzung projektSitzung;
	
	//
	// id und dirRel
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		this.idEscaped = Entities.importEntities(id);
	}
	public String getDir() {
		return dir;
	}
	/*
	public void setDir(String _dir) {
		dir = _dir;
		this.dirEscaped = Entities.importEntities(_dir);
	}
	*/

	public DbPro ( String _id, String __dir, LogConnector lc, boolean _online,
			ISitzung _projektsitzung) throws Exception {
		//System.out.println("DbPro("+_id+","+__dir);
		projektSitzung = _projektsitzung;
		online = _online;
		id = _id;
		this.idEscaped = Entities.importEntities(id);
		/*
		String testedId = Entities.importEntities(id);
		if ( !id.equals(testedId))
			throw new Exception("Project-id '"+id+"' not possible. Can't have xmlrobot-entites like (,) ... dir is: "+_dir);
		*/
		
		dir = Commons.constructNormalizedDir(__dir);
		this.dirEscaped = Entities.importEntities(dir);
		// checking pro-info
		String possible = dir+"pro-info.xml";
		
		if (  CommonsVfs.existsFile ( possible,projektSitzung ) ) {
			// it exists
			Element e = CommonsVfs.getXmlRootOfFile(possible,projektSitzung);
			
			if ( !(e.getName().equals("pro-info")))
				throw new Exception("Project-Info file of project '"+
			_id+"' does not have a root-element named 'pro-info', happend in: "+dir);
			
			/*
			if ( e.getAttribute("nbr-of-gods") == null ) {
				throw new Exception("Project-Info file of project '"+
			_id+"' does not have an attribute 'nbr-of-gods', happend in: "+dir);				
			}
			
			if ( !e.getAttributeValue("nbr-of-gods").equals("0-1000") ) {
				throw new Exception("Project-Info file of project '"+
			_id+"' does not know the nbr-of-gods, its '0-1000' , happend in: "+dir);				
			}			*/

		}
	}
	
	public Element getDbDump (boolean natural,DbApp app) {
		Element x = new Element("pro");
		if ( natural ) {
			x.setAttribute("id", id);
			x.setAttribute("dir", dir);
			x.setAttribute("data_dir",getProjectDataDir_Natural(app) );
		} else {
			x.setAttribute("id", idEscaped);
			x.setAttribute("dir", dirEscaped);	
			x.setAttribute("data_dir",Entities.importEntities(getProjectDataDir_Natural(app)) );
		}
		return x;
	}
	private static void appendExecutionPrologLogging(
			Element ret, boolean newLogAppender, boolean instExists, boolean service, boolean appExists,
			ISitzung sitzung, String systemHome) {
		
		// System.out.println("DbPro.appendExecutionPrologLogging");
		
		if ( CoreBuilder.ws.getLogFile() == false ) {
			/*
			Element e8 = new Element("dir");
			e8.setAttribute("id", "temp");
			e8.setText("(dir,systemp)");
			ret.addContent(e8);	
			*/
			
		} else	if ( newLogAppender ) {
		
			
			// (dir,log)
		
			Element e3 = new Element("dir");
			e3.setAttribute("id", "log");
			
			e3.setText(sitzung.getSessionHomeFs()+"/log/"+Commons.getTs());
			e3.setAttribute("flag", "lock");
			ret.addContent(e3);
			/*
			Element e3y = new Element("ti");
			e3y.setAttribute("type", "dir");
			e3y.setAttribute("key", "systeminfo");
			e3y.setAttribute("label", "Logger");
			e3y.setAttribute("x", "(dir,log)"); */
			
			//ret.addContent(e3y);			
			/*
			Element l1 = new Element("lock");
			l1.setAttribute("id", "log");
			l1.setAttribute("type", "dir");
			ret.addContent(l1);*/
			

			
			
			Element e6 = new Element("interpreter");
			e6.setAttribute("add_file_logger", "(dir,log)log.txt");
			ret.addContent(e6);
			
			Element e7 = new Element("file");
			e7.setAttribute("id", "log");
			e7.setAttribute("flag", "lock");
			e7.setText("(dir,log)log.txt");
			ret.addContent(e7);	
			
			/*
			Element l2 = new Element("lock");
			l2.setAttribute("id", "log");
			l2.setAttribute("type", "file");
			ret.addContent(l2);		*/	
			
			// (dir,temp)
			/*
			Element e8 = new Element("dir");
			e8.setAttribute("id", "temp");
			e8.setText("(dir,log)");
			ret.addContent(e8);	
			
			Element l3 = new Element("lock");
			l3.setAttribute("id", "temp");
			l3.setAttribute("type", "dir");
			ret.addContent(l3);	
			*/
			// ant, mkdir (dir,log)
			//Element ant = new Element("ant");
			
			/*
			Element e5x = new Element("echo");
			e5x.setAttribute("type", "dir");
			e5x.setAttribute("id", "log");
			e5x.setAttribute("label", "Logging directory");
			// e4.addContent(e5);
			ret.addContent(e5x);	*/		
			
			Element mkdir = new Element("mkdir");
			mkdir.setAttribute("dir", "(dir,log)");
			// e4.addContent(e5);
			//ant.addContent(mkdir);
			// ret.addContent(mkdir);

			
		} else {
			// (dir,temp)
			/*
			Element e8 = new Element("dir");
			e8.setAttribute("id", "temp");
			e8.setText(getLogDir(instExists,service,appExists));

			ret.addContent(e8);		
			*/			
		}
		
	}
	/*
	private static String getLogDir(boolean instExists, boolean service, boolean appExists ) {
		if ( appExists ) {
			if ( service ) {
				if ( instExists )
					return ("(dir,logs)srv/(prop,app)/(f,time,yyyyMMdd)/(prop,pro).(f,time,yyyyMMdd_HHmm_ss,job)");
				else
					return ("(dir,logs)srv/(prop,app)/(f,time,yyyyMMdd)/nopro.(f,time,yyyyMMdd_HHmm_ss,job)");
			} else {
				if ( instExists )
					return ("(dir,logs)(prop,app)/(f,time,yyyyMMdd)/(prop,pro).(f,time,yyyyMMdd_HHmm_ss,job)");
				else
					return ("(dir,logs)(prop,app)/(f,time,yyyyMMdd)/nopro.(f,time,yyyyMMdd_HHmm_ss,job)");			
			}
		} else {
			return ("(dir,logs)srv/(f,time,yyyyMMdd)/noapp.(f,time,yyyyMMdd_HHmm_ss,job)");
		}
	}*/
	private String getStatusDirForProlog ( ) {
		if ( online )
			return Core.getMountSystem()+"syscache/"+idEscaped+"/";
		else
			return Core.getMountSystem()+"status/";
	}
	public Element getExecutionProlog(boolean newLogAppender, boolean service) {
		// System.out.println("DbPro.getExecutionProlog");
		Element ret = new Element("config");	
		
		// (dir,inst)
		/*
		Element e1 = new Element("dir");
		e1.setAttribute("id", "inst");
		e1.setText(dirEscaped);
		ret.addContent(e1);
		
		
		Element l1 = new Element("lock");
		l1.setAttribute("id", "inst");
		l1.setAttribute("type", "dir");
		ret.addContent(l1); */
		
		// (dir,pro)
		Element e3 = new Element("dir");
		e3.setAttribute("id", "pro");
		e3.setAttribute("idxxx", "proxxx");
		e3.setAttribute("flag", "lock");
		e3.setText(dirEscaped);
		ret.addContent(e3);	
		
		Element e3x = new Element("dir");
		e3x.setAttribute("id", "status");
		e3x.setAttribute("flag", "lock");
		e3x.setText(getStatusDirForProlog());

		ret.addContent(e3x);		
		
		// (prop,inst)
		/*
		Element e2 = new Element("prop");
		e2.setAttribute("id", "inst");
		e2.setText(idEscaped);
		ret.addContent(e2);

		Element l2 = new Element("lock");
		l2.setAttribute("id", "inst");
		l2.setAttribute("type", "prop");
		ret.addContent(l2);			*/

		// (prop,pro)
		Element e4 = new Element("prop");
		e4.setAttribute("id", "pro");
		e4.setAttribute("flag", "lock");
		e4.setText(idEscaped);
		ret.addContent(e4);		
		
		appendExecutionPrologLogging ( ret, newLogAppender, true,service,true,projektSitzung,Core.getMountSystem());

		return ret;
	}
	
	public static Element getExecutionPrologAnonym(
			String pro_id,
			String pro_dir,
			boolean newLogAppender, boolean service, boolean _online, ISitzung psitzung) {
		
		// System.out.println("DbPro.getExecutionPrologAnonym");
		Element ret = new Element("config");	
		
		// (dir,inst)
		/*
		Element e1 = new Element("dir");
		e1.setAttribute("id", "inst");
		e1.setText(pro_dir);
		ret.addContent(e1);
		
		
		Element l1 = new Element("lock");
		l1.setAttribute("id", "inst");
		l1.setAttribute("type", "dir");
		ret.addContent(l1); */
		
		// (dir,pro)
		Element e3 = new Element("dir");
		e3.setAttribute("id", "pro");
		e3.setAttribute("flag", "lock");
		e3.setText(Entities.importEntities(pro_dir));
		ret.addContent(e3);		
		
		// (prop,pro)
		Element e4 = new Element("prop");
		e4.setAttribute("id", "pro");
		e4.setAttribute("flag", "lock");
		e4.setText(Entities.importEntities(pro_id));
		ret.addContent(e4);		
			
		
		Element e3x = new Element("dir");
		e3x.setAttribute("id", "status");
		e3x.setAttribute("flag", "lock");
		if ( _online ) {
			e3x.setText(Core.getMountSystem()+"syscache/"+Entities.importEntities(pro_id)+"/");
		} else {
			e3x.setText(Core.getMountSystem()+"status/");
		}
		ret.addContent(e3x);
				
		
		appendExecutionPrologLogging ( ret, newLogAppender, true,service,true,psitzung,Core.getMountSystem());

		return ret;
	}	
	
	public static Element getExecutionPrologNoInst(boolean newLogAppender, boolean service, boolean appExists,
			ISitzung _sitzung) {
		// System.out.println("DbPro.getExecutionPrologNoInst");
		
		Element ret = new Element("config");			

		Element e3x = new Element("dir");
		e3x.setAttribute("id", "status");
		e3x.setAttribute("flag", "lock");
		e3x.setText(Core.getMountSystem()+"status/");
		ret.addContent(e3x);
				
		appendExecutionPrologLogging ( ret, newLogAppender, false,service,appExists,_sitzung,Core.getMountSystem());

		return ret;
	}	
	
	public static Element getExecutionPrologUsage(boolean newLogAppender, String instDir, String instId,
			boolean _online , ISitzung _sitzung ) {
		// System.out.println("DbPro.getExecutionPrologUsage");
		Element ret = new Element("config");	
		
		// (dir,inst)
		Element e1 = new Element("dir");
		e1.setAttribute("id", "pro");
		e1.setAttribute("flag", "lock");
		e1.setText(Entities.importEntities(instDir));
		ret.addContent(e1);
		
			
		
		/* (prop,proj) */
		Element e3 = new Element("prop");
		e3.setAttribute("id", "pro");
		e3.setAttribute("flag","lock");
		e3.setText(Entities.importEntities(instId));
		ret.addContent(e3);
		
		Element e3x = new Element("dir");
		e3x.setAttribute("id", "status");
		e3x.setAttribute("flag", "lock");
		if ( _online ) {
			e3x.setText(Core.getMountSystem()+"syscache/"+Entities.importEntities(instId)+"/");
		} else {
			e3x.setText(Core.getMountSystem()+"status/");
		}
		ret.addContent(e3x);		
		
		appendExecutionPrologLogging ( ret, newLogAppender, true,false,true,_sitzung,Core.getMountSystem());

		return ret;
	}
	
	@Override
	public int compareTo(DbPro o) {
		// TODO Auto-generated method stub
		return this.id.toLowerCase().compareTo(o.id.toLowerCase());
	}	
	
	public String getProjectDataDir_Natural(DbApp app ) {
		// System.out.println("DbPro.getProjectDataDir_Natural");
		String retvalNatural = dir;
		String pro_data_dir = app.getProjectDataDir();
		if ( pro_data_dir != null ) {
			retvalNatural = retvalNatural + pro_data_dir;
		}
		return retvalNatural;
	}	
	
}
