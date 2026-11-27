package at.nicolas.x.pub;

import java.util.Hashtable;
import java.util.Map;

public class KeyWordMapper {
	String title;
	int size;
	Hashtable<String, Integer> hashtable;
	public KeyWordMapper ( String title, int size ) {
		this.title = title;
		this.size = size;		
		hashtable= new Hashtable<>(size,1);
	}
	
	public void put ( String keyword, int code ) {
		hashtable.put(keyword, code);
	}
	public void putFinished (  ) {
		
	}	
	
	public int convert ( String keyword ) throws Exception {
		Integer found = null;
		/*System.out.println("KeyWordMapper '"+title+
				"' was asked to translate "+keyword);*/
		if ( keyword != null ) {
			found = hashtable.get(keyword);
			/*System.out.println("KeyWordMapper '"+title+
					"' did successfully acess the map ");*/
		}
			
		
		if ( found == null ) {
			String shouldBe="";
			for (Map.Entry<String, Integer> e : hashtable.entrySet()) {
				shouldBe = shouldBe.concat(e.getKey()+", ");
			}
			if ( shouldBe.endsWith(", "))
				shouldBe = shouldBe.substring(0, shouldBe.length()-2);
			
			throw new Exception("The given keyword '"+keyword+
					"' is not a valid for the type '"+title+
					"'. It should be one of these: "+shouldBe);
		} else
			return found.intValue();

	}
	public int convertRes ( String keyword ) throws Exception {
		if ( keyword == null )
			throw new Exception("Received null for keyword of type '"
					+title+"'");
		
		Integer found = hashtable.get(keyword);
		
		if ( found == null ) 
			return -1;
		 else
			return found.intValue();

	}	
}
