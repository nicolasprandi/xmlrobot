package at.nicolas.x.hibiscus;

import java.util.Date;

import at.nicolas.x.int_specs.Commons;

public class MeasuredAttribute {

String key;
String title;
Date   measuredAt;
int value;
String remark;

MeasureSystem mm;

boolean timeCritical = false;


public String getPrintString(int nameLen, int valueLen ) {
	/* String ret = Commons.trailBlanks(title, nameLen)+" = "+
			Commons.leadBlanks(""+value, valueLen); */
	
	String ret = title +" = "+IntParas.tausender(value);
	
	if ( timeCritical )
		ret = ret + " at "+mm.formatter.format(measuredAt);
	return ret;
}
public MeasuredAttribute ( String _key, String _title, int _value, MeasureSystem _mm ) {
	createInternal (  _key,  _title,  _value, null,_mm,false);

}
public MeasuredAttribute ( String _key, String _title, int _value, String _remark, MeasureSystem _mm ) {
	createInternal (  _key,  _title,  _value,  _remark,_mm,false );
}

public MeasuredAttribute ( String _key, String _title, int _value, MeasureSystem _mm, boolean _timeCritical ) {
	createInternal (  _key,  _title,  _value, null,_mm,_timeCritical);

}
public MeasuredAttribute ( String _key, String _title, int _value, String _remark, MeasureSystem _mm , boolean _timeCritical) {
	createInternal (  _key,  _title,  _value,  _remark,_mm,_timeCritical );
}

private void createInternal ( String _key, String _title, int _value, String _remark, MeasureSystem _mm,
		boolean _timeCritical ) {
	timeCritical = _timeCritical;
	mm = _mm;
	key = _key;
	if ( title == null )
		title = _key;
	else
		title = _title;
	
	value = _value;
	remark = _remark;
	measuredAt = new Date();
}
}
