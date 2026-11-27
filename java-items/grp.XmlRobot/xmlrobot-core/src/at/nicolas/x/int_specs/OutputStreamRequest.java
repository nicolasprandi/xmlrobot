package at.nicolas.x.int_specs;

import java.io.OutputStream;

public class OutputStreamRequest {
	
	private OutputStream outputStream = null;
	private long opnum = -1;

	public OutputStreamRequest(OutputStream _outputStream, long _opnum) {
		outputStream = _outputStream;
		opnum = _opnum;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public long getOpnum() {
		return opnum;
	}	

}
