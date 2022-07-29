package org.shikhar.simphy.input;


/**
 * Represents a Touch finger used to hold its current state.
 * @author:Mahesh kurmi
 */
public class Finger {

	/** pointer id of finger*/
	public int id=-1;
	
	public float x=-1,y=-1;
	
	public float pressure=0;
	
	public static final int PRESSED=501;
	public static final int DRAGGED=503;
	public static final int RELEASED=502;
	
	//public enum EventState{PRESSED,DRAGGED,RELEASED};
	
	/**State of Finger representing current event*/ 
	public int eventState=RELEASED;
	
	/**Flag to make sure press event is always fired before mouse drag*/
	private boolean pressEventActive=false;
	
	private boolean consumed=true;
	/**
	 * Creates new Finger
	 */
	public Finger() {
		
	}
	
	/**
	 * Updates the finger with new event
	 * @param id
	 * @param x
	 * @param y
	 * @param pressure
	 * @param eventState
	 */
	protected void update(int id,float x, float y,float pressure,int eventState) {
		this.id = id;
		this.pressure=pressure;
		consumed=false;
		switch(eventState) {
		case DRAGGED:
			this.eventState=pressEventActive?Finger.PRESSED:Finger.DRAGGED;
			break;
		case PRESSED:
			this.eventState=Finger.PRESSED;
			pressEventActive=true;
			break;
		case RELEASED:
			this.eventState=Finger.RELEASED;
			pressEventActive=false;
			break;
		}
		this.x=x;
		this.y=y;
		//System.out.println("Finger "+id+" updated "+this.eventState);
	}
	
	/**
	 * Checks if The finger is active (still pressed)
	 * @return boolean
	 */
	public boolean isActive(){
		return id>0&&this.eventState!=Finger.RELEASED;
	}
	
	/**
	 * Return true if the finger does't have any ready to consume event
	 * @return
	 */
	public boolean isConsumed(){
		return consumed;
	}
	
	/**
	 * Consumes current event from this finger, finger no more will fire this event until it receives another event
	 */
	public  void consumeEvent() {
		consumed=true;
		pressEventActive=false;
		
	}

	public void reset() {
		x=-1;
		y=-1;
		id=-1;
		this.eventState=Finger.RELEASED;
		//consumed=true;
		//pressEventActive=false;
		pressure=0;
		//System.out.println("Finger reset");
	}
	

	
}
