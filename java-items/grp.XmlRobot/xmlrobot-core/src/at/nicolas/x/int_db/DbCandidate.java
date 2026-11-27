/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

public class DbCandidate {
	
	private DbJob job;
	private int pri;
	public DbCandidate ( DbJob _job, int _pri ) {
		job = _job;
		pri = _pri;
				
		
	}
	public DbJob getJob() {
		
		return job;
	}
	public int getPri() {
		return pri;
	}

}