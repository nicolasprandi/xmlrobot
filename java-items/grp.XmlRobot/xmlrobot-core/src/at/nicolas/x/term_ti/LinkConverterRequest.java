package at.nicolas.x.term_ti;

public class LinkConverterRequest {
	private boolean seeAppData = false;
	private boolean seeProData = false;
	private boolean seeWorkspaceData = false;
	private boolean seeSystemData = false;
	private boolean seeFileFunction = false;
	private boolean seeDirFunction = false;
	private boolean seeOtherData = false;
	
	private boolean seeLibro = false;
	private boolean seeSession = false;	
	
	private boolean extDontCrossFnPro =false;
	private boolean extDontCrossFnApp = false;
	private boolean extDontCrossFnElse = false;
	private static long idSequence = 0;
	private long id = 0;
	public long getId() {
		return id;
	}
	public LinkConverterRequest(
			boolean seeAppData,
			boolean seeProData,
			boolean seeWorkspaceData,
			boolean seeSystemData,
			boolean seeFileFunction,
			boolean seeDirFunction,
			boolean seeOtherData,
			boolean extDontCrossFnPro,
			boolean extDontCrossFnApp,
			boolean extDontCrossFnElse,
			boolean seeLibro,
			boolean seeSession			) {
		id=++idSequence;
		this.seeAppData = seeAppData;
		this.seeProData = seeProData;
		this.seeWorkspaceData = seeWorkspaceData;
		this.seeSystemData = seeSystemData;
		this.seeFileFunction = seeFileFunction;
		this.seeDirFunction = seeDirFunction;
		this.seeOtherData = seeOtherData;
		this.seeLibro = seeLibro;
		this.seeSession = seeSession;
		
		this.extDontCrossFnPro = extDontCrossFnPro;
		this.extDontCrossFnApp = extDontCrossFnApp;
		this.extDontCrossFnElse = extDontCrossFnElse;
		
	}
	public String getDoku() {
		String result = 
				"seeAppData ="+seeAppData +", "+
				"seeProData ="+ seeProData+", "+
				"seeWorkspaceData ="+seeWorkspaceData +", "+
				"seeSystemData ="+seeSystemData +", "+
				"seeFileFunction ="+seeFileFunction +", "+
				"seeDirFunction ="+seeDirFunction +", "+
				"seeOtherData ="+seeOtherData +", "+
				"seeLibro ="+seeLibro +", "+
				"seeSession ="+seeSession +", "+
				" extDontCrossFnPro="+ extDontCrossFnPro+", "+
				" extDontCrossFnApp="+ extDontCrossFnApp+", "+
				" extDontCrossFnElse="+extDontCrossFnElse;
		return result;
		
	}
	
	LinkConverterRequest() {
		
	}

	public boolean isSeeAppData() {
		return seeAppData;
	}

	public void setSeeAppData(boolean seeAppData) {
		this.seeAppData = seeAppData;
	}

	public boolean isSeeProData() {
		return seeProData;
	}

	public void setSeeProData(boolean seeProData) {
		this.seeProData = seeProData;
	}

	public boolean isSeeWorkspaceData() {
		return seeWorkspaceData;
	}
	
	public boolean isSeeLibroData() {
		return seeLibro;
	}	
	public boolean isSeeSessionData() {
		return seeSession;
	}	

	public void setSeeWorkspaceData(boolean seeWorkspaceData) {
		this.seeWorkspaceData = seeWorkspaceData;
	}

	public boolean isSeeSystemData() {
		return seeSystemData;
	}

	public void setSeeSystemData(boolean seeSystemData) {
		this.seeSystemData = seeSystemData;
	}

	public boolean isSeeFileFunction() {
		return seeFileFunction;
	}
	public boolean isSeeDirFunction() {
		return seeDirFunction;
	}


	public boolean isSeeOtherData() {
		return seeOtherData;
	}

	public void setSeeOtherData(boolean seeOtherData) {
		this.seeOtherData = seeOtherData;
	}

	public boolean isExtDontCrossFnPro() {
		return extDontCrossFnPro;
	}

	public boolean isExtDontCrossFnApp() {
		return extDontCrossFnApp;
	}

	public boolean isExtDontCrossFnElse() {
		return extDontCrossFnElse;
	}	

}
