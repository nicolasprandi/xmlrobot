package at.nicolas.x.hibiscus;

import java.util.ArrayList;
import java.util.Date;

import at.nicolas.x.int_specs.Commons;

class Mp {
	
	String key;
	String title;
	int level;
	Mp parent;
	long begin;
	Date beginDate;
	Date endDate = null;
	long end = 0;
	long durationMs = 0;
	MeasureSystem mm ;
	
	private static final Duration startDuration = new Duration(" running","---");
	
	
	//String myDurationString = "running";
	String myFromToString = null;
	//String myUnit = "?";
	Duration duration = startDuration;
	
	ArrayList<MeasuredAttribute> mas = new ArrayList<MeasuredAttribute>();	
	
	protected void finalizeAndPrint( ) {
		try {
			finalize();
			printOnFinalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			mm.intParas.printExceptionBy(e, "finalize measure-point "+key+", "+title);
			System.out.println("Exception "+e.getMessage());
			System.out.println("by finalize measure-point"+key+", "+title);
			e.printStackTrace();
		}	
	}		
	
	protected void finalize( ) {
		endDate = new Date();
		end = System.currentTimeMillis(); // end = endDate.getTime();
		
		duration = IntParas.getDurationString(begin,end);

		
		myFromToString = mm.formatter.format(beginDate)+" -> "+mm.formatter.format(endDate);
	}
	/*
	private static String getLevelBlockIn(int ylevel ) {
		int mylevel = ylevel;
		if ( ylevel <= 1)
			mylevel = 0;
		return Commons.StringTimes(">>>   ", mylevel);		
	}
	
	private static String getLevelBlockOut(int ylevel ) {
		int mylevel = ylevel;
		if ( ylevel <= 1)
			mylevel = 0;
		return Commons.StringTimes("<<<   ", mylevel);		
	}	
	*/
	protected void printOnCreation( ) {
		mm.tl.print(">> "+title+ " at " +
				mm.formatter.format(beginDate) +", level "+(level-1)
				);
		
	}
	
	protected void printOnFinalize() {
		mm.tl.print("<< "+title+ " [ "+duration.durationString.trim()+" "+duration.unit+" ] "+myFromToString);
		
		

		for ( int i = 0; i<mas.size(); i++ ) {
			MeasuredAttribute ma = mas.get(i);
			mm.tl.print("   : "+ma.title +" = "+IntParas.tausender(ma.value));
		}			
		mm.tl.print(" ");
	}
	
	protected void print( boolean withNumbers ) {
		
		int titleLen = 40;
		
		String leftover ="";
		
		// String printHeader = ;
		String naming = "";
		if ( level < 1 )
			naming = "!";
		else if ( level < 2 )
			naming = "";
		else
			naming = Commons.StringTimes("  ", level-1 );
		
		naming = naming + title;
		
		if (titleLen < 0  );
		else if ( (naming.length()-3) > titleLen) {
			leftover ="... "+naming.substring(titleLen-3);
			naming = naming.substring(0, titleLen-3)+"...";
		} else
			naming = Commons.trailBlanks(naming, titleLen);
		
		String printHeader;
		int prefaceLen;



			printHeader = "|" + 
					duration.durationString+ " | "
			+naming ;	
			prefaceLen=printHeader.length();
			printHeader = printHeader+this.myFromToString+" "+leftover;

		mm.tl.printBlank(printHeader);

		if ( withNumbers ) {
		String preface = Commons.leadBlanks("", prefaceLen);
		for ( int i = 0; i<mas.size(); i++ ) {
			MeasuredAttribute ma = mas.get(i);
			mm.tl.printBlank(preface+": "+ma.getPrintString(15, 10));
			

		}	
		}
		

		
	}	
	

	

	
	protected Mp ( String _key, String _title, int _level, Mp _parent, MeasureSystem _mm ) {
		begin = System.currentTimeMillis();
		beginDate = new Date();
		
		// begin = beginDate.getTime();
		
		
		key = _key;
		if ( title == null )
			title = _key;
		else
			title = _title;

		level = _level;
		parent = _parent;
		mm = _mm;
		myFromToString = mm.formatter.format(beginDate)+" -> ??.??.??";
		this.printOnCreation();
		
	}
	

	
	
	protected MeasuredAttribute addMeasure(String _key, int _value) {
		
		MeasuredAttribute measuredAttribute = new MeasuredAttribute(  _key,  _key,  _value,mm);
		mas.add(measuredAttribute);
		return measuredAttribute;
		
	}
	
	protected void addMeasure(String _key, String _title, int _value) {
		
		MeasuredAttribute measuredAttribute = new MeasuredAttribute(  _key,  _title,  _value,mm );
		mas.add(measuredAttribute);
		
	}	

}
