package at.nicolas.x.hibiscus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import at.nicolas.x.int_specs.Commons;

public class Updater {
	
	String classicFileName;
	String category;
	String name;
	int    nuberOfFields;
	int    bufferSize;
	String commandDoku;
	
	int status; /* 0=writing 1=reading 2=closed */
	
	// status 0 writing
	FileOutputStream fos = null;
	BufferedOutputStream  outbuffer = null;
	
	// status 1 reding
	
	FileInputStream fis = null;
	BufferedInputStream  inbuffer = null;
	
	//TaskLogger tl;
	//String fullname;
	TaskLogger tl = null;
	File file = null;
	protected Updater(
			String __classicFileName,
			String category,
			String name,
			int    nuberOfFields,
			int    bufferSize,
			TaskLogger tl,
			String commandDoku ) throws FileNotFoundException {
		this.tl = tl;
		
		if ( __classicFileName.startsWith("var/www") ) {
			this.classicFileName = "/"+__classicFileName ;
		} else
			this.classicFileName =__classicFileName ;

		this.category =category;
		this.name =name;
		//this.fullname=category+":"+name;
		this.nuberOfFields=nuberOfFields;		
		this.bufferSize=bufferSize;
		this.commandDoku = commandDoku;

		tl.printUpdateDetails("   Updater.start {{ "+name+" }} started and will write to: "+this.classicFileName);
		if ( this.commandDoku != null )
			tl.printUpdateDetails(Commons.StringTimes(" ", ("                 {{ "+name+" }} ").length())+"will do later: "+this.commandDoku);
		
		status = 0;
		file = new File(this.classicFileName);
		fos = new FileOutputStream(file);
		outbuffer = new BufferedOutputStream (fos, bufferSize);
		
		// tl.printUpdateDetails("  Updater {{ "+name+" }} buffer opened");
		
		
	}
	
	/*
	private static int fromByteArray(byte[] bytes) {
	     return ((bytes[0] & 0xFF) << 24) | 
	            ((bytes[1] & 0xFF) << 16) | 
	            ((bytes[2] & 0xFF) << 8 ) | 
	            ((bytes[3] & 0xFF) << 0 );
	}	*/
	
	protected void setInt ( int i ) throws IOException {
		
		  byte[] result = new byte[4];

		  result[0] = (byte) (i >> 24);
		  result[1] = (byte) (i >> 16);
		  result[2] = (byte) (i >> 8);
		  result[3] = (byte) (i /*>> 0*/);
		  // tl.printUpdateDetails("  Updater {{ "+fullname+" }} writes to buffer: "+i);
		  outbuffer.write(result);
		  
		  // tl.printUpdateDetails("  Updater {{ "+fullname+" }} did write to buffer: "+i);
		
	}
	
	int expected =-1;
	protected void switchFromWritingToReading(int expected) throws Exception {
		String add="";
		if ( this.commandDoku != null )
			add = ", does now: "+this.commandDoku;
		
		tl.printUpdateDetails("   Updater.switch {{ "+name+" }} is switching"+add);
		//if ( this.commandDoku != null )
			//tl.printUpdateDetails(Commons.StringTimes(" ",("                  {{ "+name+" }} ").length())+"does now: "+this.commandDoku);
		
		this.expected = expected;

		Exception ex = null;
		try {
			outbuffer.close();
		} catch (IOException e) {
			ex = e;
			//tl.print("Exception/Updater.switch "+fullname+":outbuffer.close"+e.getMessage());
		}
		
		try {
			fos.close();
		} catch (IOException e) {
			if ( ex==null ) ex = e;
			//tl.print("Exception/Updater.switch "+fullname+":fos.close"+e.getMessage());
		}		
		if ( ex != null ) {
			tl.print("Updater having problem during closing writings: "+this.classicFileName);
			throw ex;
		}
		// tl.printUpdateDetails("  Updater {{ "+name+" }} is switching from reading to writing: "+this.classicFileName);
		// tl.printUpdateDetails("p1"); 
		//fos=null;
		// tl.printUpdateDetails("p2"); 
		//outbuffer = null;
		// tl.printUpdateDetails("p3"); 
		
		//tl.print("  Updater will open for reading: "+this.classicFileName);
		try {
			// tl.printUpdateDetails("p3b"); 
			// Thread.sleep(1000);
			// tl.printUpdateDetails("p4"); 
			// File file = new File(this.classicFileName);
			// tl.printUpdateDetails("p5");
			fis = new FileInputStream(file);
			// tl.printUpdateDetails("p6");
			inbuffer = new BufferedInputStream (fis,bufferSize);
			// tl.printUpdateDetails("p6.1");
			// Thread.sleep(1000);
			// tl.printUpdateDetails("p7");
			// tl.printUpdateDetails("  Updater {{ "+name+" }} did open inputbuffer: "+this.classicFileName);
			

		} catch (Exception e) {
			// tl.printUpdateDetails("p8");
			// System.out.println("aha:"+e.getMessage());
			tl.printUpdateDetails("  Updater.fail {{ "+name+" }} FAiLED: "+this.classicFileName);
			tl.printUpdateDetails(e.getMessage());
			tl.print("Updater having problem during opening for reading: "+this.classicFileName);
			throw ( e );
		}	
		// tl.printUpdateDetails("p9");
			
	}
	protected void close() throws Exception {
		tl.printUpdateDetails("   Updater.close {{ "+name+" }} is closing down");
		try { if ( outbuffer!= null ) outbuffer.close();} catch (Exception e) {tl.print("xz1");}
		try { if ( fos!= null ) fos.close();} catch (Exception e) {tl.print("xz2");}
		try { if ( inbuffer!= null ) inbuffer.close();} catch (Exception e) {tl.print("xz3");}
		try { if ( fis!= null ) fis.close();} catch (Exception e) {tl.print("xz4");}
		try { file.delete(); } catch (Exception e) {tl.print("xz5");}
		
	}
	
	protected void close( int done) throws Exception {
		
		if ( done != expected ) {
			tl.print("Updater.close ["+name+"] does not fullfill expectations: done="+
					done+" but expected was "+expected);
			throw new Exception("Updater.close ["+name+"] does not fullfill expectations: done="+
		done+" but expected was "+expected);
		}
		
		if ( inbuffer.available() > 0 ) {
			tl.print("Updater.close ["+name+"] has still data available in bytes:"+inbuffer.available());
			throw new Exception("Updater.close ["+name+"] has still data available in bytes:"+inbuffer.available());
		}
		close();

		
			
	}	
	
	protected int getInt() throws IOException {
		
		
		byte[] bytes = new byte[4];
		int read= inbuffer.read(bytes);
		if ( read == 4 ) {
			// alles ok
		     return ((bytes[0] & 0xFF) << 24) | 
			            ((bytes[1] & 0xFF) << 16) | 
			            ((bytes[2] & 0xFF) << 8 ) | 
			            ((bytes[3] & 0xFF) << 0 );			
		} else {
			/*System.out.println("**   did read   *** number  ** of bytes="+read);
			System.out.println("**   did read   *** number  ** of bytes="+read);
			System.out.println("**   did read   *** number  ** of bytes="+read);
			System.out.println("**   did read   *** number  ** of bytes="+read);*/
			return -1;
		}
			
	}
}
