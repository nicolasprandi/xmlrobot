package at.nicolas.x.tasks;

import java.io.OutputStream;
import java.io.PrintStream;

import at.nicolas.x.int_specs.OutputStreamRequest;

public class JavascriptPrinter {

	private PrintStream printStream;
	private OutputStream os;
	private String outputFile;
	private OutputStreamRequest osr;
	
	JavascriptPrinter (PrintStream printStream, OutputStream os, String outputFile,OutputStreamRequest osr ) {
		this.printStream = printStream;
		this.os = os;
		this.outputFile = outputFile;
		this.osr = osr;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public PrintStream getPrintStream() {
		return printStream;
	}
	public OutputStream getOs() {
		return os;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public OutputStreamRequest getOutputStreamRequest() {
		return osr;
	}	

}
