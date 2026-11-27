/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

public class Scan_DirReturner {
	public static int ERC_EXCEPTION = 0;
	public static int ERC_LEVEL_LIMIT_REACHED = 1;
	public static int ERC_DIR_SEL_CRIT_NOT_FULLFILLED = 2;
	public static int ERC_OS_PROBLEM = 3;
	public static int ERC_BONE = 4;
	public static int ERC_APP_NO_PRO = 5;
	public static int ERC_IS_NOT_END = 98;
	public static int ERC_OTHERS = 99;
	
	 int files_tested = 0;
	 int count_files =0;
	 int count_files_total =0;
	 boolean end_reached = false;
	 int end_reason = ERC_IS_NOT_END; 	// 0=Some Exception 
									// 1=LevelLimitReached 
									// 2=Selection-Criterias-for-dirs-not-fullfilled
									// 3=os problem
									// 99=some unknown reason
	Scan_DirReturner ( int files_tested, int count_files, int count_files_total ) {
		this.files_tested = files_tested;
		this.count_files = count_files;
		this.count_files_total = count_files_total;
		
	}
	Scan_DirReturner () {
		
	}
	public void setEndReached ( int end_reason ) {
		this.end_reached = true;
		this.end_reason = end_reason;
		
	}
	public int getFilesTested() {
		return this.files_tested;
	}
	
	public boolean isLeaf() {
		return end_reached;
	}
	
	public int plusFilesTested(int add ) {
		files_tested = files_tested + add;
		return files_tested;
	}
	public int plusCountFiles(int add ) {
		count_files = count_files + add;
		return count_files;
	}	
	
	public void aggregateUp() {
		sum_childs_files_tested = sum_childs_files_tested + files_tested;
		sum_childs_count_files = sum_childs_count_files + count_files;
	}
	
	int sum_childs_files_tested = 0;
	int sum_childs_count_files = 0;
	public void addChild (Scan_DirReturner child ) {
		sum_childs_files_tested = sum_childs_files_tested+ child.files_tested;
		sum_childs_count_files = sum_childs_count_files+ child.count_files;
		
	}

	

}