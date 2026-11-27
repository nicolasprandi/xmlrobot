package at.nicolas.x.int_interpreter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;

public class CoopListOfApp {
	
	public ConcurrentHashMap <String, ICoop> noMapKeep = new ConcurrentHashMap <String, ICoop>();
	
	
	public static void closeAllCoops(
			ConcurrentHashMap<String, ICoop> noMapKeep,
			ISitzung isitzung) {
		// TODO Auto-generated method stub
		Set<String> keySet = noMapKeep.keySet();
		Iterator<String> i = keySet.iterator();
		while ( i.hasNext() ) {
			String aktKey   = i.next();

			ICoop toremove = noMapKeep.get(aktKey);
			Sp.sl("AutoClose for Coop-KEEP "+aktKey);
			//
			try {
				toremove.autoClose();
				Sp.sl("Performed AutoClose for KEEP-Coop: '"+aktKey+"'");
			} catch (Exception e) {
				Sp.pProblem("PROBMLEM when performing AutoClose for KEEP-Coop: '"+aktKey+"'; "+e.getLocalizedMessage(),isitzung);
				Sp.pe(e);
			}		
			//
			
			// noMapKeep.remove(aktKey);				
		}		
		noMapKeep = new ConcurrentHashMap<String, ICoop>();
		if ( noMapKeep.size() > 0 )
			Sp.pProblem("Could not remove all left coops !",isitzung);	
		/* SessionLog: finished */
	}

}
