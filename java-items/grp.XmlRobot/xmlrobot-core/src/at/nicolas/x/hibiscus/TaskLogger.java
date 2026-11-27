package at.nicolas.x.hibiscus;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.DummySitzung;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;

public class TaskLogger {
	private void setLogFile( String _logFile ) {
		logFilex = _logFile;
	}
	private String getLogFile() {
		return logFilex;
	}
	private String logFilex = null;
	private String logFlags = null;
	private ISitzung sitzung = null;
	private SimpleDateFormat formatter = null;
	
	private PrintStream psPrinter=null;
	private OutputStreamRequest osr =null;
	boolean doPrintIs = false;
	boolean isOnline = false;

	protected void setDoPrintIs(boolean _doPrintIs) {
		this.doPrintIs = _doPrintIs;
		//print("isdetails="+_doPrintIs);
		System.out.println("setDoPrintIs("+this.doPrintIs+")");
		
	}
	long beginntime = System.currentTimeMillis();
	public TaskLogger( String _logFile, String _logFlags, ISitzung _sitzung  ) throws FileSystemException, StorageLimitException, UnsupportedEncodingException {
		setLogFile(_logFile);
		logFlags = _logFlags;
		sitzung = _sitzung;
		this.isOnline = sitzung.isOnline();
		

		formatter = new SimpleDateFormat ("mm:ss");
		
		if ( getLogFile() != null ) {
			String lf = getLogFile();
			System.out.println("logfile="+lf);
			osr = CommonsVfs.getOutputStreamByFilenameP1(getLogFile(), false, sitzung);
			psPrinter = new PrintStream(osr.getOutputStream(),false,"UTF-8");			
		}		
		
	}

	public void close() throws IOException, StorageLimitException  {	
		 print("   Going to close LogFile.");
		 
		 try {
			System.out.println("Starting Phase-1");
			if (osr != null && osr.getOutputStream() != null ) osr.getOutputStream().close();
		} catch (Exception e) {
			System.out.println("Problem at Integration.TaskLogger.close().Phase-1 = closing Stream");
			e.printStackTrace();
		}
		 
		 try {
			 //System.out.println("Starting Phase-2");
			if (psPrinter != null )  psPrinter.close();
		} catch (Exception e) {
			System.out.println("Problem at Integration.TaskLogger.close().Phase-2 = closing psPrinter");			
			e.printStackTrace();
		}
		 
		 
		 //System.out.println("-----------------------outputfilename-"+outputfilename);
		 //System.out.println("Starting Phase-3");
		 if ( getLogFile() != null ) {
			 //System.out.println("Starting Phase-3b");
			 try {
				CommonsVfs.finishOutputStreamP2(getLogFile(), sitzung, osr.getOpnum());
			} catch (Exception e) {
				System.out.println("Problem at Integration.TaskLogger.close().Phase-3 = CommonsVfs.finishOutputStreamP2");			
				
				e.printStackTrace();
			}
		 }
		 
	
	}	
	
	public void printException(Exception e )  {		
		print ( getTs()+" *** Exception *** : "+e.getMessage());		
	}		
		
	
	protected void logExceptionBy(Exception e, String guiltyIndentifier ){		
		print ( getTs()+" *** Exception *** : "+e.getMessage());	
		print ( getTs()+" *** By        *** : "+guiltyIndentifier);		
	}		
	private String getTs() {
		
		Duration duration = IntParas.getDurationString(beginntime, System.currentTimeMillis());
		

		return (duration.durationString.trim()+ "  ");		
		

	}
	
	private String getTsAlt() {
		Date mydate = new Date();

		return (formatter.format(mydate)+" ");		
		

	}	
	/*
	public void printTime()  {	
		//Timestamp now = new Timestamp( System.currentTimeMillis() );
		psPrinter.println(getTs()+now.toString());	
		System.out.println(getTs()+now.toString());
	}	
	public void printTime(String label)  {	
		Timestamp now = new Timestamp( System.currentTimeMillis() );
		psPrinter.println(getTs()+label+" "+now.toString());	
		System.out.println(getTs()+label+" "+now.toString());
	}		
	*/
	public void printUpdateDetails(String toprint)  {
		print(toprint);
		
	}
	public void print(String toprint)  {	
		
		psPrinter.println(getTs()+toprint);	
		psPrinter.flush();
		if (!isOnline) System.out.println(getTs()+toprint);
	}	
	
	public void printBlank(String toprint)  {	
		
		psPrinter.println(toprint);	
		psPrinter.flush();
		if (!isOnline) System.out.println(toprint);
	}		

	public void printFilled(String toprint,int fillingLeft )  {	
		
		
		printFilledLevel( toprint, fillingLeft, 0 );
					
	}	
	
	public void printLevel(String toprint,int level )  {	
		
		
		printFilledLevel( toprint, 0, level );
					
	}		
	
	public void printFilledLevel(String toprint,int fillingLeft, int level )  {	
		
		String resultLevel = Commons.StringTimes("  ", level);
		String resultContent = Commons.leadBlanks(toprint, fillingLeft);
		String result = resultLevel + resultContent;
		
		psPrinter.println(getTs()+result);	
		psPrinter.flush();
		if (!isOnline) System.out.println(getTs()+result);
					
	}		
	/*
	public void printStepIntro(String stepName)  {	
		//print (" ");
		//print (">> "+stepName+" >> Step started ...");
	}	
	
	public void printStepExtro(String stepName)  {	
		//print ("<< "+stepName+" << Step finished. ");
		//print (" ");
	}		*/
	
	int isLineCounter = 0;
	public void is(String toprint,int level)  {	
		if ( doPrintIs ) {
			isLineCounter++;
			
			psPrinter.println(Commons.leadBlanks(isLineCounter+"", 3)+": "+Commons.StringTimes("  ", level)+toprint);
			psPrinter.flush();
			if (!isOnline) System.out.println(Commons.leadBlanks(isLineCounter+"", 3)+": "+Commons.StringTimes("  ", level)+toprint);
		}
	}
	
	public void isSum(String toprint,int level)  {	
		
			isLineCounter++;
			
			psPrinter.println(Commons.leadBlanks(isLineCounter+"", 3)+": "+Commons.StringTimes("  ", level)+toprint);
			psPrinter.flush();
			if (!isOnline) System.out.println(Commons.leadBlanks(isLineCounter+"", 3)+": "+Commons.StringTimes("  ", level)+toprint);
		
	}		
	/*
	public void printOnFlag(String toprint, String flag)  {
		if (logFlags != null && Commons.hasFlag(logFlags, flag) )
			psPrinter.println(getTs()+toprint);	
		System.out.println(getTs()+toprint);
	}	*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
