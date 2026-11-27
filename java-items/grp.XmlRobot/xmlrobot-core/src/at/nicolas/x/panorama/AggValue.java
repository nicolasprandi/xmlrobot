// Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved.
package at.nicolas.x.panorama;


public class AggValue {
	
	public int count = 0;
	public int sum = 0;
	
	
	void addFile ( int add_sum ) {
		count++;
		sum = sum + add_sum;
		
	}
	
	void addValue ( AggValue add ) {
		count = count +add.count;
		sum = sum +add.sum;
	}

}