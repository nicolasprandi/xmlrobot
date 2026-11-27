/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.jdom2.Element;

import at.nicolas.x.int_coop.CryptoBase;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;

public class InterLogg {
	
	LogConnector lc = new LogConnector("Logger  ");
	LogConnector lcd = new LogConnector("");
	

	private String filenameEscaped = null;
	private FileAppender fileAppender = null;
	private String fileAppenderId = null;
	
	
	public InterLogg (Element element) throws Exception {
				
		//CryptoBase cb = new CryptoBase("karla");
		filenameEscaped = element.getAttributeValue("add_file_logger");
		filenameEscaped= Commons.fileClassic(filenameEscaped);
		
		if ( filenameEscaped != null ) {
			Sp.p("Creating file-appender for "+Entities.exportEntities(filenameEscaped));
			
			fileAppender = new FileAppender( new PatternLayout( "%-5p %m%n" ), Entities.exportEntities(filenameEscaped), false );
			Sp.p("did Creating file-appender for "+Entities.exportEntities(filenameEscaped));
			fileAppenderId = CoreBuilder.rslogger.addAppender( fileAppender );			
			
		} else {
			throw new Exception("file_logger needs an attribute 'out' which specifies the output-file!");
		}
		lcd.info("Logging goes to "+Entities.exportEntities(filenameEscaped));

	}
	
	
	public void close() throws Exception {
		// Conf.rslogger.systemdebug();
		lc.trace("wants to close logger "+fileAppenderId);
		CoreBuilder.rslogger.removeAppender(fileAppenderId);
	}
	
	public void debugx(int index, Level level, String prefix) {
		
		lcd.log(prefix+ Interpreter.declareDebug
				+Commons.leadBlanks(fileAppenderId,Interpreter.lb2)
				+Interpreter.debug_equal_nolock
				+"file:  "
				+Entities.exportEntities(filenameEscaped)
				, level);		
			
	}
	
}