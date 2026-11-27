package at.nicolas.x.hibiscus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class MeasureSystem {

TaskLogger tl;
IntParas intParas ;
Date startOfMeasurements;
ArrayList<Mp> mps = new ArrayList<Mp>();
SimpleDateFormat formatter;
public MeasureSystem( TaskLogger tl,IntParas intParas ) {
	this.tl = tl;
	this.intParas = intParas;
	startOfMeasurements = new Date();
	formatter = new SimpleDateFormat ("HH:mm:ss");
}

protected void printMeasurePoints(boolean withNumbers) {
	printAllPoints(mps, withNumbers);
}
private void printAllPoints ( ArrayList<Mp> _mps,boolean withNumbers ) {
	
	for ( int i = 0; i<_mps.size(); i++ ) {
		try {
			Mp mp = _mps.get(i);
			mp.print(withNumbers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			intParas.printExceptionBy(e, "printing a measure-point");
		}
	}
}

protected Mp beginMeasurePoint(Mp _parent, String _key, String _title, int _level ) {
	
	Mp newMp = new Mp(  _key,  _title,   _level,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}
/*
protected Mp beginMeasurePoint(Mp _parent, String _key, String _title) {
	
	Mp newMp = new Mp(  _key,  _title,  0,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}*/


/*
protected Mp beginMeasurePoint(Mp _parent, int _level, String _key, String _title ) {
	
	Mp newMp = new Mp(  _key,  _title,  _title,  _level,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}*/

protected Mp beginMeasurePoint(Mp _parent, String _key, String _title) {
	
	Mp newMp = new Mp(  _key,  _title,  _parent.level+1,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}
protected Mp beginMeasurePoint(Mp _parent, String _key) {
	
	Mp newMp = new Mp(  _key,  _key ,  _parent.level+1,  _parent,this);
	mps.add( newMp ) ;
	
	String state = _key;
	if ( _parent != null && !_parent.key.equals("integration") ) {
		state = state.concat(" / "+_parent.key);
		if ( _parent.parent != null && !(_parent.parent).key.equals("integration")) {
			state = state.concat(" / "+(_parent.parent).key);
		}
	}
	intParas.setState(state);
		
	
	return newMp;
}

protected Mp beginMeasurePoint(String _key,Mp _parent ) {
	
	Mp newMp = new Mp(  _key,  _key ,  _parent.level+1,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}

protected Mp beginMeasurePoint(String _key, String _title,  int _level ) {
	
	Mp newMp = new Mp(  _key,  _title,    _level,  null,this);
	mps.add( newMp ) ;
	
	return newMp;
}

protected Mp beginMeasurePoint(String _key, String _title) {
	
	Mp newMp = new Mp(  _key,  _title,   0,  null,this);
	mps.add( newMp ) ;
	
	return newMp;
}
/*
protected Mp beginMeasurePoint(String _key, String _title) {
	
	Mp newMp = new Mp(  _key,  _title,  null,  0,  null,this);
	mps.add( newMp ) ;
	
	return newMp;
}*/
protected Mp beginMeasurePoint( String _key) {
	
	Mp newMp = new Mp(  _key,  _key ,    0,  null,this);
	mps.add( newMp ) ;
	
	return newMp;
}

protected Mp beginMeasurePoint( String _key, int _level) {
	
	Mp newMp = new Mp(  _key,  _key ,  _level,  null,this);
	mps.add( newMp ) ;
	
	return newMp;
}

protected Mp beginMeasurePoint( String _key, int _level, Mp _parent) {
	
	Mp newMp = new Mp(  _key,  _key ,  _level,  _parent,this);
	mps.add( newMp ) ;
	
	return newMp;
}
	
	

}
