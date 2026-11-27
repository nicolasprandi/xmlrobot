package at.nicolas.x.hibiscus;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;

public class MvUpdateDefintions {
	Hashtable<Integer, MvUpdateDefinition> defList = null;
	MvUpdateDefinition getMvUpdateDefinition ( int obj_typ ) {
		if ( defList == null) return null;
		else if ( defList.containsKey(obj_typ) ) return defList.get(obj_typ);
		else return null;
	}
	MvUpdateDefintions (  ) {
		defList = new Hashtable<>();
		
	}
	MvUpdateDefintions ( Document xmlDocument ) {
		
		Element rootElement = xmlDocument.getRootElement();
		
		defList = new Hashtable<>(rootElement.getChildren().size(),1);
		
		List<Element>      headList = rootElement.getChildren();
		Iterator<Element>  headIterator = headList.iterator();
		while ( headIterator.hasNext()) {
			Element aktElement = (Element)headIterator.next();
			
			int obj_typ = Commons.string2int(aktElement.getAttributeValue("obj_typ"));
			
			MvUpdateDefinition mvud = new MvUpdateDefinition(
					aktElement.getAttributeValue("table"),
					aktElement.getAttributeValue("view"),
					aktElement.getAttributeValue("cols")
					);
			defList.put(obj_typ, mvud);								
		}
		
	}
}
