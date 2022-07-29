package org.shikhar.simphy.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.shikhar.Vector2;




/**
 * Represents a polled Mouse input device.
 * @author:Mahesh kurmi
 */
public class Mouse implements MouseListener, MouseMotionListener,MouseWheelListener{
	/** The map of mouse buttons */
	private Map<Integer, MouseButton> buttons = new Hashtable<Integer, MouseButton>();
	/** The map of mouse buttons */
	private ArrayList<Finger> fingers = new ArrayList<Finger>();
	
	/** The current mouse location */
	private Vector2 location=new Vector2(0,0);
	
	/** The previous mouse press location */
	private Vector2 prevPresslocation=new Vector2(0,0);
	
	/** The previous mouse button press id */
	private int prevPressButton=0;
	
	/** time when mouse was pressed*/
	private long prevPressTime=0;
	
	/** last event id recieved*/
	private long prevAwtEventId=0;
	
	/** Whether the mouse has moved */
	private boolean moved;
	
	/** The scroll amount */
	private int scroll;

	private int activePointerId=-1;

	private boolean hasGestures=false;
	private Logger Log= Logger.getLogger("GestureDetector");

	/**Flag to check if mouse has some unconsumed events*/
	public boolean consumed=true;
	
	/**Number of fingers currenty pressed*/
	public int activeFingerCount=0;
	
	
	public GestureEvent gestureEvent=new GestureEvent();
	
	public void init(Simphy instance){
		//zoomGesture=new PinchToZoomGesture(instance.canvas.getNEWTChild(),this,false);
		fingers=new ArrayList<Finger>(10);
		for(int i=0;i<10;i++) {
			fingers.add(i, new Finger());
		}
		
		//gesturePoints=new ArrayList<Vector2>();
		//Debug.
	}
	
	public Map<Integer, MouseButton> getActiveButtons(){
		return this.buttons;
	}
	
	public ArrayList< Finger> getFingers(){
		return this.fingers;
	}
	/**
	 * Sometimes polling does't catch MOUSEPRESS event, because it  is overridden by next MOUSEDRAG event 
	 */
	public void fixAWTEventData(){
		Iterator<MouseButton> buttons = this.buttons.values().iterator();
		while (buttons.hasNext()) {
			buttons.next().fixAWTEventData();
		}
	}
	
	/**
	 * Returns true if the given MouseEvent code was clicked.
	 * @param code the MouseEvent code
	 * @return boolean
	 */
	public boolean wasClicked(int code) {
		MouseButton mb = this.buttons.get(code);
		// check if the mouse button exists
		if (mb == null) {
			return false;
		}
		// return the clicked state
		return mb.wasClicked();
	}
	
	
	/**
	 * Returns true if the given MouseEvent code was double clicked.
	 * @param code the MouseEvent code
	 * @return boolean
	 */
	public boolean wasDoubleClicked(int code) {
		MouseButton mb = this.buttons.get(code);
		// check if the mouse button exists
		if (mb == null) {
			return false;
		}
		// return the double clicked state
		return mb.wasDoubleClicked();
	}
	
	
	/**
	 * Returns true if the given MouseEvent code was clicked.
	 * @param code the MouseEvent code
	 * @return boolean
	 */
	public boolean isClicked() {
		for(MouseButton mb :this.buttons.values()){
			if (mb != null&&mb.wasClicked())return true;
		}
		return false;
	}
	/**
	 * Returns true if any of button is currently pressed and is waiting to be released.
	 */
	public  boolean isPressed(){
		for(MouseButton mb :this.buttons.values()){
			if (mb != null&&mb.isPressed())return true;
		}
		return false;
	}
	/**
	 * Returns true if the given MouseEvent code was clicked and is waiting to be released.
	 * @param code the MouseEvent code
	 * @return boolean
	 */
	public boolean isPressed(int code) {
		MouseButton mb = this.buttons.get(code);
		// check if the mouse button exists
		if (mb == null) {
			return false;
		}
		// return the double clicked state
		return mb.isPressed();
	}
	
	/**
	 * Returns true if any of button is  released.
	 */
	public  boolean wasReleased(){
		for(MouseButton mb :this.buttons.values()){
			if (mb != null&&mb.wasReleased())return true;
		}
		return false;
	}
	/**
	 * Returns true if the given MouseEvent code was clicked and was waiting to be released
	 * but is now released.
	 * @param code the MouseEvent code
	 * @return boolean
	 */
	public boolean wasReleased(int code) {
		MouseButton mb = this.buttons.get(code);
		// check if the mouse button exists
		if (mb == null) {
			return false;
		}
		// return the double clicked state
		return mb.wasReleased();
	}
	
	/**
	 * Returns the current location of the mouse relative to
	 * the listening component.
	 * @return Point
	 */
	public Point getLocation() {
		return this.location;
	}
	
	/**
	 * Returns true if the mouse has moved.
	 * @return boolean
	 */
	public boolean hasMoved() {
		return this.moved;
	}
	
	/**
	 * Clears the state of the given MouseEvent code.
	 * @param code the MouseEvent code
	 */
	public void clear(int code) {
		MouseButton mb = this.buttons.get(code);
		// check if the mouse button exists
		if (mb == null) {
			return;
		}
		// clear the state
		mb.clear();
	}
	
	/**
	 * Clears the state of all MouseEvents.
	 */
	public void clear() {
		Iterator<MouseButton> buttons = this.buttons.values().iterator();
		while (buttons.hasNext()) {
			buttons.next().clear();
		}
	
		this.moved = false;
		this.scroll = 0;
		this.consumed=true;
		gestureEvent.consume();
	}
	

	/**
	 * Resets all the keys, assumes are are consumed
	 */
	public void reset() {
		clear();
		for(MouseButton mb :this.buttons.values()){
			if (mb != null) {
				mb.setPressed(false);
			}
		}
		consumed=true;
	}

	
	/**
	 * Returns true if the user has scrolled the mouse wheel.
	 * @return boolean
	 */
	public boolean hasScrolled() {
		return this.scroll != 0;
	}
	
	/**
	 * Returns true if the user has scrolled the mouse wheel.
	 * @return boolean
	 */
	public boolean hasGestures() {
		//return false;
		return activeFingerCount>1||gestureEvent.isActive(); 
	}
	

	/**
	 * Returns the number of 'clicks' the mouse wheel has scrolled.
	 * @return int
	 */
	public int getScrollAmount() {
		return this.scroll;
	}
	
	/**
	 * returns true if mouse has some unconsumed events
	 * @return
	 */
	public boolean isActive() {
		return !consumed;
	}

	
	
	/*s
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseClicked(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Mouse clicked :"+ "type="+e.getPointerType(0)+"ptr_id "+e.getPointerId(0)+" activeid="+activePointerId);
		this.location = new Point(e.getX(), e.getY());
		
		int code = e.getButton();
		
		
		
		MouseButton mb = this.buttons.get(code);
		// check if the mouse event is in the map
		if (mb == null) {
			// if not, then add it
			mb = new MouseButton(code,java.awt.event.MouseEvent.MOUSE_CLICKED,e.getModifiers());
			this.buttons.put(code, mb);
		}
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_CLICKED;
		// set the value directly (since this can be a single/double/triple etc click)
		mb.setValue(e.getClickCount());
		//mb.updateEventData(java.awt.event.MouseEvent.MOUSE_CLICKED,e.getModifiers());
		this.consumed=false;
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e.toString());//
		
	}
	


/**
	 * Needed since java AWT sends mouseButton Code only when state is changed,
	 * Ex. in mouseDrag event getButton() returns -1, button info is stored as modifier
	 * @param e
	 * @return
	 */
	private int getMouseCode(MouseEvent e) {
		int code=0;
		// set the mouse button pressed flag
					if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
						code = MouseEvent.BUTTON1;
					} else if ((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0) {
						code = MouseEvent.BUTTON2;
					} else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
						code = MouseEvent.BUTTON3;
					}
					
					return code;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mousePressed(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// called when a mouse button is pressed and is waiting for release
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Mouse pressed :"+ "type="+e.getPointerType(0)+"ptr_id "+e.getPointerId(0)+" activeid="+activePointerId);
		this.location = new Point(e.getX(), e.getY());
		//System.out.println("Mouse Pressed id:"+e.getPointerId(0)+" count:"+e.getPointerCount()+" ids="+Arrays.toString(e.getAllPointerIDs()));
		if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e.toString());//
		// set the mouse state to pressed + held for the button
		int code =e.getButton();
		
		prevPressTime=e.getWhen();
		prevPressButton=code;
		prevPresslocation.setLocation(e.getX(), e.getY());
		/*
		if(e.getPointerType(0)!=PointerType.Mouse){
			if(activePointerId==-1){
				activePointerId=e.getPointerId(0);
				code=MouseEvent.BUTTON1;
			}
		}
		*/
		
		MouseButton mb = this.buttons.get(code);
		// check if the mouse event is in the map
		if (mb == null) {
			// if not, then add it
			mb = new MouseButton(code,java.awt.event.MouseEvent.MOUSE_PRESSED,e.getModifiers());
			this.buttons.put(code, mb);
		}
		mb.setPressed(true);
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_PRESSED;
		mb.updateEventData(java.awt.event.MouseEvent.MOUSE_PRESSED,e.getModifiers());
		//mb.setValue(e.getClickCount());
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e.toString());//
		
		this.consumed=false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseReleased(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// called when a mouse button is waiting for release and was released
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Mouse released :"+ "type="+e.getPointerType(0)+"ptr_id "+e.getPointerId(0)+" activeid="+activePointerId);
		this.location = new Point(e.getX(), e.getY());
			// set the mouse state to released for the button
		//System.out.println("Mouse Released id:"+e.getPointerId(0)+" count:"+e.getPointerCount()+" ids="+Arrays.toString(e.getAllPointerIDs()));

		int code = e.getButton();
		if(code==0)code=getMouseCode(e);
		
		gestureEvent.end();
	
		
		// set the mouse state to released for the button
		MouseButton mb = this.buttons.get(code);
		// check if the mouse event is in the map
		if (mb == null) {
			// if not, then add it
			mb = new MouseButton(code,java.awt.event.MouseEvent.MOUSE_RELEASED,e.getModifiers());
			this.buttons.put(code, mb);
		}
			mb.setPressed(false);
		mb.setWasReleased(true);
	
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e.toString());//
		
		if(prevAwtEventId==java.awt.event.MouseEvent.MOUSE_DRAGGED ) {
			if(e.getWhen()-prevPressTime<200 && prevPresslocation.distance(this.location)<50) {
				mb.setValue(1);
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Mouse Manually Clicked !");//
				
			}
		}
		
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_RELEASED;

		mb.updateEventData(java.awt.event.MouseEvent.MOUSE_RELEASED,e.getModifiers());
		
		this.consumed=false;
	}


	/*
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseDragged(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// called when a mouse button is waiting for release and the mouse is moving
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Mouse dragged :"+ "type="+e.getPointerType(0)+"ptr_id "+e.getPointerId(0)+" activeid="+activePointerId);
		this.location = new Point(e.getX(), e.getY());
		//System.out.println("Mouse Dragged id:"+e.getPointerId(0)+" count:"+e.getPointerCount()+" ids="+Arrays.toString(e.getAllPointerIDs()));
	
		int code = e.getButton();
		
		if(code==0)code=getMouseCode(e);
		// set the mouse location
		this.moved = true;
		this.location = new Point(e.getX(), e.getY());
		// set the mouse button pressed flag
		
		MouseButton mb = this.buttons.get(code);
		//heck if the mouse event is in the map
		if (mb == null) {
			// if not, then add it
			mb = new MouseButton(code,java.awt.event.MouseEvent.MOUSE_DRAGGED,e.getModifiers());
			this.buttons.put(code, mb);
		}
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_DRAGGED;
		mb.setPressed(true);
		mb.updateEventData(java.awt.event.MouseEvent.MOUSE_DRAGGED,e.getModifiers());
		//mb.setValue(e.getClickCount());
		//System.out.println("Dragged:"+e);
		this.consumed=false;
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e.toString());//
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseMoved(com.jogamp.newt.event.MouseEvent)
	 * e.getPointerId(0) doest represent current pointer it represents frst active pointer
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		/*
		 * if(zoomGesture.process(e))return;
		 */
		this.moved = true;
		this.location = new Point(e.getX(), e.getY());
		
		
		
		if(!(e.getButton()==MouseEvent.BUTTON1) && this.buttons.get(MouseEvent.BUTTON1)!=null){
			if(this.buttons.get(MouseEvent.BUTTON1).isPressed()){
				this.buttons.get(MouseEvent.BUTTON1).setPressed(false);
				this.buttons.get(MouseEvent.BUTTON1).setWasReleased(true);
			}
		}
		
		int code = e.getButton();
		
		// check if the mouse event is in the map
		MouseButton mb = this.buttons.get(code);		
		if (mb == null) {
			// if not, then add it
			mb = new MouseButton(code,java.awt.event.MouseEvent.MOUSE_MOVED,e.getModifiers());
			this.buttons.put(code, mb);
		}
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_MOVED;
		mb.updateEventData(java.awt.event.MouseEvent.MOUSE_MOVED,e.getModifiers());
		this.consumed=false;
	}
	
	/*
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.scroll += (Simphy.isMac?e.getWheelRotation():e.getWheelRotation());
		this.consumed=false;
	}
	
	// not used
	
	/* (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseEntered(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		int code = e.getButton();
		// check if the mouse event is in the map
		MouseButton mb = this.buttons.get(code);		
		if (mb != null) {
			mb.updateEventData(java.awt.event.MouseEvent.MOUSE_ENTERED,e.getModifiers());
			//System.out.println("Mouse entered: " +e);
		}
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_ENTERED;
		this.consumed=false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jogamp.newt.event.MouseListener#mouseExited(com.jogamp.newt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		int code = e.getButton();
		// check if the mouse event is in the map
		MouseButton mb = this.buttons.get(code);		
		if (mb != null) {
			mb.updateEventData(java.awt.event.MouseEvent.MOUSE_EXITED,e.getModifiers());
			//System.out.println("Mouse exited: " +e);
		}
		prevAwtEventId=java.awt.event.MouseEvent.MOUSE_EXITED;
	}

	/**
	 * 
	 * @return
	 */
	public int getActiveFingersCount() {
		return activeFingerCount;
		/*
		int count=0;
		for(Finger f:fingers) {
			if(f.isActive())count++;
		}
		return count;
		*/
	}
	/*
	public void onZoomAndScale(Vector2 center,double scale, double angle) {
		// TODO Auto-generated method stub
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(zoomGesture.toString());
		this.zoom=scale;
		this.angle=angle;
		this.center=center;
		
		//gesturePoints=zoomGesture.getGesturePoints();
	}
	*/
}
