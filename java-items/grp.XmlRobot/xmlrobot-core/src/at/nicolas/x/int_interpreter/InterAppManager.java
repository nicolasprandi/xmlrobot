/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;


public class InterAppManager  {
	
	LogConnector lc = new LogConnector("Process Manager");
	ArrayList<AmMember> list = new ArrayList<AmMember>();
	public InterAppManager() {

		
	}
	
	public void startInterpreter ( Interpreter in ) {
		// lc.warn("start-Interpreter:"+in);
		AmMember amm = new AmMember();
		amm.i = in;
		
		amm.t = new Thread(in);
		 list.add(amm);
		 amm.t.start();
	}
	public void stopInterpreterx ( Interpreter in ) {
		lc.warn("stop-Interpreter:"+in);
		Iterator<AmMember> it = list.iterator();
		AmMember toremove = null;
		while ( it.hasNext() ) {
			AmMember x = (AmMember)it.next();
			lc.warn("stop-Interpreter checks:"+x.i);
			if ( in == x.i ) {
				toremove = x;
				break;
				
			}
		}
		if ( toremove != null ) {
			list.remove(toremove);
			lc.warn("Interpreter terminated well: "+in.jobConstr.getJobid());
		} else
			lc.warn("Interpreter could not be finished: "+in.jobConstr.getJobid());		
	}	
	
	private static Level last_level = null;
	
	public void sleepAll (ITerminator terminator) {
		
		Iterator<AmMember> it = list.iterator();
		while ( it.hasNext() ) {
			AmMember x = (AmMember)it.next();
			// x.i.halt();
			try {
				lc.warn("going to sleep 300 ...");
				x.t.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}
			lc.fatal("Trying to set to sleep :"+x.i.appid+","+x.i.jobConstr.getJobid()+","+x.i.instid+","+x.i.isSystemcall);

		}
		try {
			lc.warn("going to sleep 200 ...");
			Thread.sleep(200);
		} catch (InterruptedException e2) {
			
			Sp.pe(e2);
		}

	}	
	public void halt (ITerminator terminator) {
		
		Iterator<AmMember> it = list.iterator();
		boolean all_off = true;
		while ( it.hasNext() ) {
			AmMember x = (AmMember)it.next();
			x.i.halt();
			lc.fatal("Found One Running Process :"+x.i.appid+","+x.i.jobConstr.getJobid()+","+x.i.instid+","+x.i.isSystemcall);
			// x.getMyThread().st
			terminator.setProgress(x.i.jobConstr.getJobidEscaped()+" is still running, couldn't stop :-( ...", true);
			all_off = false;
		}
		if ( all_off ) {
			lc.warn("Everything Shut Down on Request");
			// JOptionPane.showMessageDialog (null, "Everything is shut down", "Information", JOptionPane.INFORMATION_MESSAGE);
			terminator.setProgress("Stopped ALL ", true);
			/*
			if ( last_level != null ) {
				lc.setLevel(last_level);
				last_level = null;
			} */
		} else {
			lc.warn("Could not stop all processes :-(");
			/*
			last_level = Conf.rslogger.getLevel();
			lc.setLevel(Level.TRACE);			*/
		}
	}
	
	public boolean canRunNewJob (ITerminator terminator) {
		
		if ( list.size() == 0 )
			return true;
		else if ( CoreBuilder.ws.isCan_run_multi_jobs()) {
			terminator.setProgress("Starting another job #"+list.size(), true);
			
			//
			Element eHr = new Element("ti_hr");			
			terminator.tiHr(eHr);
			
			//
			Element exi = new Element("ti");
			exi.setText("Starting another job. Switched to next page ...");
			try {
				terminator.executeTi(exi, Level.INFO, null, null, null, null, null);
			} catch (Exception e) {
				Sp.pe(e);
			}
			
			//
			terminator.flush();
			terminator.bottom();

			
			return true;
		} else {
			terminator.setProgress("A Job is already running, no duplicates possible !", true);
			return false;
		}
	}	
	
	public void halted( Interpreter ix ) {

		
		ArrayList itList = (ArrayList)list.clone();
		Iterator it = itList.iterator();
		while ( it.hasNext() ) {
				AmMember x = (AmMember)it.next();
				if ( x.i == ix ) {
					lc.log("Halted:"+x.i.appid+","+x.i.jobConstr.getJobid()+","+x.i.instid+","+x.i.isSystemcall+" STOPPED",Level.TRACE);
					list.remove(x);
				}
		}
		
	}
	private class AmMember {
		Thread t;
		Interpreter i;
	}

}