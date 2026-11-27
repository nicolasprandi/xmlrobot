package at.nicolas.x.int_interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.jdom2.Element;

import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;

public class ExceptionTracer {
	
	private Exception exception = null;
	private Stack<LogicStackMember> logicStack = null;
	private ArrayList<DbJob> jobcalllist = new ArrayList<DbJob>();
	DbJob job = null;
	
	ExceptionTracer(DbJob _job) {
		job = _job;
	}
	public void noteException( 
			Exception _e, 
			Stack<LogicStackMember> _logicStack,
			ArrayList<DbJob> _calllist ) {
		exception = _e;
		logicStack = (Stack<LogicStackMember>)_logicStack.clone();
		jobcalllist = (ArrayList<DbJob>)_calllist.clone();
		
		
	}
	public ArrayList<Element> getTiDokuNoDump () { 
		ArrayList<Element> ret = new ArrayList<Element>();
		Element a =new Element("ti");
		a.setText("A Problem occurred.");
		ret.add(a);
		return ret;
		
	}
	public ArrayList<Element> getTiDoku () {
		
		ArrayList<Element> ret = new ArrayList<Element>();


		
		// TerminalLogger: Exception
		Element ret2 = new Element("ti");
		try {
			String msg1 = null;
			try {
				msg1 = exception.getLocalizedMessage();
			} catch (Exception erich) {
				try {
					msg1 = exception.getMessage();
				} catch (Exception e1) {
					msg1 = "Some Error";
				}
			}
			
			String msg2= null;
			try {
				msg2 = exception.getClass().getCanonicalName();
			} catch (Exception egon) {
				msg2 = "Java Runtime Error";
			}
			String fmsg = null;
			if ( msg2.equals("at.nicolas.x.CustomException"))
				fmsg = msg1;		
			else if ( msg2.equals("at.nicolas.x.LangException"))
				fmsg = msg1; // + " [Type] XmlRobot-Language Exception";
			else if ( msg2.equals("java.lang.Exception"))
				fmsg = msg1;
			else
				fmsg = msg1 + ", "+msg2; // " [Type] "+ msg2;
			
			ret2.setAttribute("label",Entities.importEntities(fmsg));
		} catch (Exception emil) {
			// TODO Auto-generated catch block
			ret2.setAttribute("label","Java Runtime Exception");
		}
		if ( !isTkStop(exception)) {
			ret2.setAttribute("flag","error");
		}
		ret2.setAttribute("size","2");		
		
		ret.add(ret2);
		
		ret.add(getLabel("Error Tracing"));			
		
		ret.add(getLabel2("1) REQUESTED JOB"));		
		

		//ret.addContent(v);		
		
		Element tif = new Element("ti");
		tif.setAttribute("type","file");
		tif.setText(Entities.importEntities(job.getFullfn()));
		tif.setAttribute("label",Entities.importEntities(job.getJobid()));		
		ret.add(tif);			
					
		ret.add(getSpace());

		ret.add(getLabel2("2) LIST OF JOBS CALLS, sorted from last to first"));	
		
		for (int cur_job = jobcalllist.size(); cur_job >0 ; cur_job--) {
		
			DbJob ajob = jobcalllist.get(cur_job-1);
			Element jti = new Element("ti");
			jti.setAttribute("type", "file");
			jti.setAttribute("label", ajob.getJobidEscaped());
			jti.setAttribute("x", ajob.getFullfnEscaped());
			ret.add(jti);	
		}		
		ret.add(getSpace());		
		ret.add(getLabel2("3) HIERARCHY of PROBLEMATIC Steps"));
		Iterator<LogicStackMember> i = logicStack.iterator();

		while ( i.hasNext()) {
			LogicStackMember x = (LogicStackMember)i.next();
			Element cleared = x.getBaseElement().clone();
			while ( cleared.getChildren().size()> 0 )
				cleared.removeContent(cleared.getContent(0));
			
			Element problem = new Element("ti");
			if ( Commons.hasFlag(x.getBaseElement(), "secret")) {
				// da reden wir nicht druebeer
				if ( x.getBaseElement().getAttribute("secret") == null ) {
					// auch kein pseudonym
					problem.setAttribute("label", "Secret Task");
					problem.setAttribute("x", "... keeping silent about this task");					
				} else {
					problem.setAttribute("label", x.getBaseElement().getAttributeValue("secret"));
					problem.setAttribute("x", "... keeping silent about this task");					
				}

			} else {
				problem.setAttribute("label", x.getTypeName());
				problem.setAttribute("x", Commons.toTextCompact(cleared));				
			}

			ret.add(problem);	
			
		}		
		ret.add(getSpace());			
		
		
		return ret;
		
	}
	private boolean isTkStop( Exception e ) {
		boolean trace = false;
		try {
			String classname = e.getClass().getCanonicalName();
			if ( classname.equals("at.nicolas.x.TkStopException"))
				trace = true;	
		} catch (Exception ex) {}
		return trace;

	}	
	private Element getLabel(String text ) {
		Element v01 = new Element("ti");
		v01.setAttribute("label", Entities.importEntities(text));
		return v01;		
	}

	private Element getLabel2(String text ) {
		Element v01 = new Element("ti");
		v01.setAttribute("x", Entities.importEntities(text));
		return v01;		
	}
	
	private Element getText(String text ) {
		Element v01 = new Element("ti");
		v01.setAttribute("x", Entities.importEntities(text));
		return v01;		
	}	
	
	private Element getSpace( ) {
		Element v01 = new Element("ti");
		v01.setAttribute("x", " ");
		return v01;		
	}		

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
