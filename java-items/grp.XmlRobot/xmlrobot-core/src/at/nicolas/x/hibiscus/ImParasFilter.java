package at.nicolas.x.hibiscus;

import java.util.ArrayList;

public class ImParasFilter {

 boolean breakOn=false;

 ArrayList<String> custFlags = new ArrayList<String>();
 String cust_flag ="";
 
 boolean runstatsOn=false;
 int pronto=-1;
 
 String dbms;
 
 /*
	this.breaker = _break;
	this.cust_flag
runstats = _runstats;
dbms = _dbms;*/

protected ImParasFilter ( String breakerFile,
				String _cust_flag,
				boolean _runstats,
				String _dbms,
				int pronto,
				TaskLogger tl
				) {
	this.pronto = pronto;
	if (breakerFile !=null && breakerFile.length() > 0 )
		this.breakOn = true;
	
	if (cust_flag !=null && cust_flag.length() > 0 ) {
		custFlags = ValiResult.split(cust_flag);
		cust_flag = _cust_flag;
	}
	runstatsOn = _runstats; 
	
	dbms = _dbms;
	
	tl.is("Created IndexSzenario-Filter by Integration-Request", 0);
	
	tl.is("break     = '"+breakOn+"'",2);
	tl.is("cust_flag = '"+cust_flag+"'",2);
	tl.is("statistic = '"+runstatsOn+"'" ,2);
	tl.is("dbms      = '"+dbms+"'",2);
	tl.is("pronto    = '"+pronto+"'",2);

	
	
	
	
}

}
