/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

public interface ICoop {
	// autoClose is called from interpreter when job is finished, but cooperation
	// still exists. this applies for 'job-cooperations'
	// it is called for background-cooperations if terminal is shut down
	public void autoClose() throws Exception;
}