package at.nicolas.x.term_core;

import java.util.ArrayList;

import at.nicolas.x.term_ti.Ti_TerminalLink;

public class Seitenlinks {
	public ArrayList<Ti_TerminalLink> tlList;
	public int seitenlinksId;
	public Seitenlinks(int seitenlinksId) {
		tlList = new ArrayList<Ti_TerminalLink>();
		this.seitenlinksId = seitenlinksId;
	}

}
