package at.nicolas.x.hibiscus;

import java.util.ArrayList;

import at.nicolas.x.int_specs.Commons;

public class Imp {
	
	String id;
	int point_index;
	ArrayList<Ima> imas = new ArrayList<Ima>();
	Imp(String _id, int _point_index) {
		id = _id;
		point_index = _point_index;
		
	}
	protected void addIma( Ima ima ) {
		imas.add(ima);
	}
	
	protected void isSum (TaskLogger tl ) {
		// tl.is("POINT: "+IntegrateParas.pointnames[point_index]+" "+id,0);
		tl.isSum(Commons.trailBlanks(id, 22)+">> "+IntParas.pointnames[point_index]+
				" << ",0); // --- Effective by szenario ---
		for ( int i = 0; i<imas.size(); i++ ) {
			Ima ima = imas.get(i);
			ima.isSum(tl);
		}
		
	}	
	
	protected void run (ImParasRun imParasRun ) {
		
	}

}
