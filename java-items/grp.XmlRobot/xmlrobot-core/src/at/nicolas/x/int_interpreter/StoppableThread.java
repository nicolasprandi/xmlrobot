package at.nicolas.x.int_interpreter;

public abstract class StoppableThread extends Thread {
	
	public abstract void stopExecution( );
	public  abstract String getStateStoppable( );

}
