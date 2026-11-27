package at.nicolas.x.int_specs;

import at.nicolas.x.pub.Parameters;

public class TkMatcher {
	String spec;
	String exceptList;
	String titel;
	/*
	public void report () {
		System.out.println("#### report tkMatcher, titel="+titel+", "+", spec="+spec+", exceptList="+exceptList);
	}*/
	public boolean doesMatch (String tkid ) {
	
		
		boolean ret = false;
		if ( spec!= null && (spec.equals("all") ||  Commons.hasFlag(spec, tkid)) )  {
			if ( exceptList == null )
				ret = true;
			else {
				if ( Commons.hasFlag(exceptList, tkid))
					ret = false;
				else
					ret = true;
			}
		} else {
			// keine positiv-liste definiert 
			if ( exceptList == null )
				ret = false;
			else {
				if ( Commons.hasFlag(exceptList, tkid))
					ret = false;
				else
					ret = true;
			}			
		}
		// System.out.println("doesMatch of "+titel+" asked for tkid="+tkid +" -is:"+ret);
		return ret;
	}
	
	public TkMatcher ( 
				String titel,
				Parameters p,
				String specName,
				String exceptName ) {
		
		/*
		 * 	TkMatcher tkMatcherFast = new TkMatcher(p,"fast","fast_except","all");
			TkMatcher tkMatcherOfferFast = new TkMatcher(p,"offerfast","offerfast_except","all");
			TkMatcher tkMatcherOfferSequ = new TkMatcher(p,"offersequ","offersequ_except","all");

		 */
		
		if ( p.getParameter(specName) != null ) {
			spec = p.getParameter(specName);
		} else
			spec = null;// defaultSpecNameContent;
		
		if ( p.getParameter(exceptName) != null )
			exceptList = p.getParameter(exceptName);
		else
			exceptList=null;
		this.titel = titel;
		/*
		System.out.println("new MatcherTk: "+
				" titel="+titel+
				" specName="+specName+
				" exceptName="+exceptName+
				" p="+p.getString()+" --- "+
				" spec="+spec+
				" exceptList="+exceptList );
		
		*/
	}

}
