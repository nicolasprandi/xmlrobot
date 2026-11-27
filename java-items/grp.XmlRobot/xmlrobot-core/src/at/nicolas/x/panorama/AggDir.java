// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.

package at.nicolas.x.panorama;

import org.jdom2.Element;

public class AggDir extends Object {
	
	static int max_id = -1;
	static int max_g_id = -1;
	
	
	AggValue aggValueTab[];

	
	static void setMaxId( int p_max_id, int p_max_g_id ) {
		max_id = p_max_id;
		max_g_id = p_max_g_id;
	}
	
	AggDir () {

		aggValueTab = new AggValue[max_id];
		
		for ( int i=0; i<max_id;i++) {
			aggValueTab[i] = new AggValue();
		}
		
	}
	
	AggDir cloneMe() {
		AggDir neu = new AggDir();
		neu.aggValueTab = this.aggValueTab.clone();
		return neu;
	}
	
	void addFile ( Element fe ) {
		
		int e_id;
		try {
			e_id = AggregateScan.string2int(fe.getAttributeValue("e"));
		} catch (Exception e1) {
			System.out.println("Aggrator has a problem with extension=["+fe.getAttributeValue("e")+"] of file with id ["+fe.getAttributeValue("id")+"] file="+fe.getText());

			e_id=0;
		}
		int g_id;
		try {
			g_id = AggregateScan.string2int(fe.getAttributeValue("g"));
		} catch (Exception e1) {
			System.out.println("Aggrator has a problem with group-extensions=["+fe.getAttributeValue("g")+"] of file with id ["+fe.getAttributeValue("id")+"] file="+fe.getText());
			g_id=0;
		}
		int kb;
		try {
			kb = AggregateScan.string2int(fe.getAttributeValue("kb"));
		} catch (Exception e) {
			System.out.println("Aggrator has a problem with kb=["+fe.getAttributeValue("kb")+"] of file with id ["+fe.getAttributeValue("id")+"] file="+fe.getText());
			kb=0;
		}
		aggValueTab[e_id].addFile(kb);
		aggValueTab[g_id].addFile(kb);

	}
	
	void addDir ( AggDir add ) {
		for ( int i=0; i<max_id;i++) {
			aggValueTab[i].addValue(add.aggValueTab[i]);
		}		
	}
	static int output_id = 0;
	void updateElement ( Element e, int level, int l2, Element add_here) throws Exception {
		output_id++;
		Element aggs = new Element("aggs");

		aggs.setAttribute("level", Integer.toString(level));

		int sum_sum = 0;
		int sum_count = 0;
		for ( int i=0; i<max_id;i++) {
			if (aggValueTab[i].count>0 ) {
				int my_count = aggValueTab[i].count;
				int my_sum = aggValueTab[i].sum;
				
				Element a =null;
				if ( i ==0) {
					a = new Element("n");
					my_count = my_count / 2;
					my_sum = my_sum / 2;
					sum_sum = sum_sum + my_sum;
					sum_count = sum_count + my_count;
				} else if ( i <=max_g_id) {
					a = new Element("g");
					sum_sum = sum_sum + my_sum;
					sum_count = sum_count + my_count;					
				} else {
					a = new Element("e");
				}
				
				a.setAttribute("id", Integer.toString(i));				
				a.setAttribute("c", Integer.toString(my_count));
				a.setAttribute("kb", Integer.toString(my_sum));
				aggs.addContent(a);				
			}

		}
		if ( aggs.getContentSize()>0) {
			aggs.setAttribute("id", Integer.toString(output_id));
			aggs.setAttribute("c", Integer.toString(sum_count));
			aggs.setAttribute("kb", Integer.toString(sum_sum));
			e.addContent(0,aggs);
		} else if ( level == 1 && e.getAttributeValue("level").equals("1")) { // mal schauen
			aggs.setAttribute("id", Integer.toString(output_id));
			aggs.setAttribute("c", "0");
			aggs.setAttribute("kb", "0");
			e.addContent(0,aggs);			
		}
		
		if ( add_here != null) {
			add_here.addContent(aggs.clone());
		}		
	}

}