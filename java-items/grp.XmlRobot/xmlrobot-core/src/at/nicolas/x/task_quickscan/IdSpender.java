package at.nicolas.x.task_quickscan;

import at.nicolas.x.int_specs.Commons;

public class IdSpender {

	int id = 0;
	public IdSpender() {
		// TODO Auto-generated constructor stub
	}
	public String getNextId() {
		id++;
		return Commons.int2String(id);
	}

}
