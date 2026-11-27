package at.nicolas.x.int_interpreter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.KeyWordMapper;

public class CoopGlobalManager {
	
	private  ConcurrentHashMap<String, CoopListOfApp> coopListOfApp = new ConcurrentHashMap<String, CoopListOfApp>();
	//private Hashtable <String, ICoop> masterappMap = new Hashtable <String, ICoop>();
	private  ConcurrentHashMap <String, ICoop> sessionMap = new ConcurrentHashMap <String, ICoop>();
	
	public static KeyWordMapper loctypes;
	
	public static final int loctype_k = 1;
	public static final int loctype_a = 2;
	public static final int loctype_g = 3;
	public static final int loctype_s = 4;	
	public static void startupActions() {
		loctypes = new KeyWordMapper("Coop-Classes",4);
		loctypes.put("k", loctype_k);
		loctypes.put("a", loctype_a);
		loctypes.put("g", loctype_g);
		loctypes.put("s", loctype_s);		
		loctypes.putFinished();
				
	}	
private void printReportMap(ConcurrentHashMap <String, ICoop> printMap) {
	Set<String> keySet = printMap.keySet();
	Iterator<String> i = keySet.iterator();
	String   aktKey = null;

	while ( i.hasNext() ) {
		aktKey = i.next();	
		// System.out.println("  key="+aktKey+ " ICoop="+printMap.get(aktKey));
	}
}	
private void printReport() {
	System.out.println("Report CoopGlobalManager");
	System.out.println("1) sessionMap/g");
	printReportMap(sessionMap);
	System.out.println("2) coopListOfApp/a");
	Set<String> keySet = coopListOfApp.keySet();
	Iterator<String> i = keySet.iterator();
	String   aktKey = null;

	while ( i.hasNext() ) {
		aktKey = i.next();	
		System.out.println("appkey="+aktKey);
		printReportMap((coopListOfApp.get(aktKey)).noMapKeep);
	}	
	System.out.println("Done. Report CoopGlobalManager");
	System.out.println("");
}
	public synchronized ConcurrentHashMap<String, ICoop> getCoopMap(String appid,String coopId, int ct, boolean needsRightAgent, ISitzung isitzung) throws Exception {
		// System.out.println("## CoopGlobalManager.getCoopMap (appid="+appid+", coopId="+coopId+", ct="+ct+")");	
		//dummgelaufen
		
		if ( needsRightAgent && !isitzung.hasRightAgent()) {
			// berechtigungsproblem
			
			throw new Exception("You dont have the right to create or remove a coop in the public area");
		}
		printReport();
		ConcurrentHashMap<String, ICoop> r = null;
		switch ( ct ) {
	    case CoopGlobalManager.loctype_a: {	
	    	// System.out.println("CoopGlobalManager.typ a: "+appid);

			CoopListOfApp clop = coopListOfApp.get(appid);
			if ( clop == null ) {
				// gibts noch nicht anlegen
				clop = new CoopListOfApp();
				coopListOfApp.put(appid, clop);
				// System.out.println("CoopGlobalManager.typ a: added app. "+appid);
			}
			r = clop.noMapKeep;
    		
			break;
		}
	    case CoopGlobalManager.loctype_g: {	
	    		r= sessionMap;
				break;
			}
	                         

	    }
		
		return r;		
	

	}
	
	public void closeAllSessionCoops( ISitzung isitzung) {
		// TODO Auto-generated method stub
		Set<String> keySet = coopListOfApp.keySet();
		Iterator<String> i = keySet.iterator();
		while ( i.hasNext() ) {
			String aktKey   = i.next();

			CoopListOfApp toremove = coopListOfApp.get(aktKey);
			CoopListOfApp.closeAllCoops(toremove.noMapKeep, isitzung);
			
		}	
		//CoopListOfApp.closeAllCoops(masterappMap, isitzung);
		CoopListOfApp.closeAllCoops(sessionMap, isitzung);

	}	
}
