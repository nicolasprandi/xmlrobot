package at.nicolas.x.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Element;

class KeyTargetRegister {
	
	private HashMap<String, Integer> keyTypes = new HashMap<String, Integer>();
	
	Element go ( Element sourceElement ) {
		scan(sourceElement, ".");
		return getResultElement();
	}
	private Element getResultElement() {
		Element keyTargetReport = new Element("keyTargetReport");
		Set<String> keyTypeSet = keyTypes.keySet();
		Iterator<String> i = keyTypeSet.iterator();
		while ( i.hasNext() ) {
			String curKeyType   = i.next();
			Integer curMaxInteger = keyTypes.get(curKeyType);
			
			Element element = new Element("t");
			element.setAttribute("en", curKeyType);
			element.setAttribute("m", ""+curMaxInteger);
			
	
			keyTargetReport.addContent(element);			
			
			
		}
		return keyTargetReport;
	}
	private void registerRow (String keyType, Integer keyValue ) {
		//System.out.println(":::: keyValue="+keyValue);
		if ( !keyTypes.containsKey(keyType)) {			
			keyTypes.put(keyType, keyValue);			
		} else {
			Integer currentMax = keyTypes.get(keyType);
			if ( keyValue > currentMax)
				keyTypes.put(keyType, keyValue);
		}
			
	}
	/*
	private void registerRef (String keyType, Integer keyValue, String an, String xp ) {
		System.out.println(".... keyValue="+keyValue);
		if ( !keyTypes.containsKey(keyType)) {
			KeySourceType keySourceType = new KeySourceType();
			keyTypes.put(keyType, keySourceType);			
		}
		keyTypes.get(keyType).registerRef(keyValue,an,xp);			
	}	
	
	

	private class KeyTargetType {
		private HashMap<Integer, KeySourceValue> keyValues = new HashMap<Integer, KeySourceValue>();
		
		void registerRow (Integer keyValue ) {
			if ( !keyValues.containsKey(keyValue)) {
				KeySourceValue keySourceValue = new KeySourceValue();
				keyValues.put(keyValue, keySourceValue);			
			}				
			keyValues.get(keyValue).foundRow();
		}	
		
		void registerRef (Integer keyValue, String an, String xp ) {
			if ( !keyValues.containsKey(keyValue)) {
				KeySourceValue keySourceValue = new KeySourceValue();
				keyValues.put(keyValue, keySourceValue);			
			}
			keyValues.get(keyValue).foundRef();
			keyValues.get(keyValue).registerRef ( an,  xp );				
		}	
		void postResults ( Element parent, String keyType ) {
			
			Element element = new Element("t");
			element.setAttribute("en", keyType);
			
			Set<Integer> keyValueSet = keyValues.keySet();
			Iterator<Integer> i = keyValueSet.iterator();
			while ( i.hasNext() ) {
				Integer curKeyValue   = i.next();
				KeySourceValue curKeySourceValue = keyValues.get(curKeyValue);
				curKeySourceValue.postResults(element, curKeyValue);
				
			}			
			parent.addContent(element);
			
		}
		
	}
	
	private class KeySourceValue {
		ArrayList<KeySourceRef> refList = new ArrayList<KeySourceRef> ();
		int found_row = 0;
		int found_ref = 0;
		
		void registerRef (String an, String xp ) {
			KeySourceRef keySourceRef = new KeySourceRef(xp,an);
			refList.add(keySourceRef);
				
		}		
		void foundRow() {
			found_row++;
		}
		void foundRef() {
			found_ref++;
		}		
		
		void postResults ( Element parent, Integer keyValue ) {
			System.out.println("keyValue="+keyValue);
			Element element = new Element("v");
			element.setAttribute("v", keyValue+"");
			if ( !(found_row == 1) )
				element.setAttribute("f", found_row+"");
			if ( found_ref > 0 )
				element.setAttribute("fr", found_ref+"");
			
			for ( int i = 0; i<refList.size(); i++ ) {
				KeySourceRef curRef = refList.get(i);
				curRef.postResults(element);
			}			
			parent.addContent(element);
		}		
		
	}	
	
	private class KeySourceRef {
		String xp;
		String an;
		KeySourceRef ( String _xp, String _an ) {
			xp = _xp;
			an = _an;
			
		}
		
		void postResults ( Element parent ) {
			
			Element element = new Element("r");
			element.setAttribute("xp", xp);
			element.setAttribute("an", an);
			parent.addContent(element);
					
		}		
		
	}	
	*/
	
	private void scan ( Element startElement,
						String startXPath ) {
		
		List<Element> childs = startElement.getChildren();
		Iterator<Element> i_childs = childs.iterator();
		int childPos = 0;
		while ( i_childs.hasNext()) {
			Element current_child = i_childs.next();
			childPos++;
			scan ( current_child, startXPath+"/node()["+childPos+"]");
		}
		
		List<Attribute> attributes = startElement.getAttributes();
		Iterator<Attribute> i_attributes = attributes.iterator();

		while ( i_attributes.hasNext()) {
			Attribute current_attribute = i_attributes.next();
			if ( current_attribute.getName().equals("row")) {
				// System.out.println("a) row="+current_attribute.getValue());
				registerRow(startElement.getName(), Integer.valueOf(current_attribute.getValue()));
			}/* else if ( current_attribute.getName().endsWith("_row")) {
				int first_underscore = current_attribute.getName().indexOf("_");
				registerRef(current_attribute.getName().substring(0, first_underscore), 
							Integer.valueOf(current_attribute.getValue()), 
							current_attribute.getName(), 
							startXPath);				
			}*/
				
		}		
		
	}
}
