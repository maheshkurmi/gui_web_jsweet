package org.shikhar.simphy.input;

import java.awt.event.KeyEvent;

/**
 * Represents an input of an input device.
 * @author:Mahesh kurmi
 */
public class InputKey {
		
	/**
	 * This enumeration represents the state of an input.
	 */
	public static enum State {
		/** the key has been released */
		RELEASED,
		/** the key has been pressed */
		PRESSED
	}


	/** The input state */
	private InputKey.State state = InputKey.State.RELEASED;
	

	/** The keyCode id */
	private short keyCode = 0;
	
	/** The input count  (the number of times key event  is generated) */
	private short value = 0;

	/**key char associated with key if any*/
	private char keyChar=0;
	
	/**modifier associated with keyevent*/
	private int modifiers=0;
	
	/** The awt event id needed for gui @see java.awt.event.MouseEvent*/
	private int id=0;
	

	private boolean isActionKey=false;
	
	private boolean consumed=true;
	/**
	 * constructor.
	 * @param keyCode keyCode constant defined in {@link com.jogamp.newt.event.KeyEvent} for the key
	 * @param keyChar unicode char for the key
	 */
	public InputKey(short keyCode,char keyChar) {
		this.keyCode = keyCode;
		this.keyChar = keyChar;
	}

	
	/** 
	 * Resets the input.
	 */
	public synchronized void reset() {
		this.state = InputKey.State.RELEASED;
		consumed=true;
		this.value = 0;
		this.modifiers=0;
		this.id=0;
		this.isActionKey=false;
	}
	
	/** 
	 * Notify that the input has been released.
	 */
	public synchronized void release(char keyChar, int id, int modifiers, boolean actionKey) {
		this.state = InputKey.State.RELEASED;
		this.id=id;
		consumed=false;
		this.keyChar=keyChar;
		this.modifiers=modifiers;
		this.isActionKey=actionKey;
	}
	
	/** 
	 * Notify that the input was pressed.
	 */
	public synchronized void press(char keyChar, int id, int modifiers, boolean actionKey) {
		if(this.state ==InputKey.State.RELEASED)this.value ++;
		this.id=id;
		consumed=false;
		this.keyChar=keyChar;
		this.state = InputKey.State.PRESSED;
		this.modifiers=modifiers;
		this.isActionKey=actionKey;
	}

	
	/**
	 * Returns true if the input has been pressed since the last check.
	 * <p>
	 * Calling this method will clear the value of this input if the input
	 * has already been released or if the input is {@link Hold#NO_HOLD} and
	 * the input has not been released.
	 * @return boolean
	 */
	public synchronized boolean isPressed() {
		return  this.state ==InputKey.State.PRESSED;
	}

	/**
	 * Checks if key  is set to be consumed, and set it consumed (used for gui events only)
	 * @return
	 */
	public boolean isConsumed(){
		boolean f= consumed;
		consumed=true;
		return f;
	}
	
	/**
	 * <p>
	 * Returns the value of the input, number of times key is pressed, since {@method InputKey.reset} is called 
	 * @return int
	 */
	public synchronized int getValue() {
		return this.value;
	}
	

	/**
	 * Returns the state.
	 * @return {@link InputKey.State}
	 */
	public InputKey.State getState() {
		return state;
	}
	

	/**
	 * returns unicode keychar for the event
	 * @return
	 */
	public char getKeyChar() {
		return keyChar;
	}
	
	/**
	 * Returns the keyCode.
	 * @return int
	 */
	public short getKeyCode() {
		return this.keyCode;
	}
	
	public int getModifier(){
		return modifiers;
	}
	
	/**
	 * returns AWtEvent id (keypressed, keytyped or keyreleased)
	 * @return
	 */
	public int getId(){
		return id;
	}
	
	 /**
     * Returns whether the key in this event is an "action" key.
     * Typically an action key does not fire a unicode character and is
     * not a modifier key.
     *
     * @return <code>true</code> if the key is an "action" key,
     *         <code>false</code> otherwise
     */
    public  boolean isActionKey() {
    	return isActionKey;
    }
    
  
    /**
     * Returns String representation of key as JavaSript Key
     * @param keyCode AWT KeyCode
     * @return 
     */
    public static String getKeyText(int keyCode) {
    	//keyCode=jogamp.newt.awt.event.AWTNewtEventFactory.newtKeyCode2AWTKeyCode((short) keyCode);
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9 || keyCode >= KeyEvent.VK_A
            && keyCode <= KeyEvent.VK_Z) {
          return String.valueOf((char) keyCode).toLowerCase();
        }
       // if(true)return java.awt.event.KeyEvent.getKeyText(keyCode);
        
        switch (keyCode) {
         case KeyEvent.VK_ENTER:
          return "Enter";
        case KeyEvent.VK_BACK_SPACE:
          return "Backspace";
        case KeyEvent.VK_TAB:
          return "Tab";
        case KeyEvent.VK_CANCEL:
          return "Cancel";
        case KeyEvent.VK_CLEAR:
          return "Clear";
        case KeyEvent.VK_SHIFT:
          return "Shift";
        case KeyEvent.VK_CONTROL:
          return "Control";
        case KeyEvent.VK_ALT:
          return "Alt";
        case KeyEvent.VK_PAUSE:
          return "PAUSE";
        case KeyEvent.VK_CAPS_LOCK:
          return "CapsLock";
        case KeyEvent.VK_ESCAPE:
          return "Escape";
        case KeyEvent.VK_SPACE:
          return "Space";
        case KeyEvent.VK_PAGE_UP:
          return "PageUp";
        case KeyEvent.VK_PAGE_DOWN:
          return "PageDown";
        case KeyEvent.VK_END:
          return "End";
        case KeyEvent.VK_HOME:
          return "Home";
        case KeyEvent.VK_LEFT:
          return "ArrowLeft";
        case KeyEvent.VK_UP:
          return "ArrowUp";
        case KeyEvent.VK_RIGHT:
          return "ArrowRight";
        case KeyEvent.VK_DOWN:
          return "ArrowDown";
        case KeyEvent.VK_DELETE:
          return "Delete";
        case KeyEvent.VK_NUM_LOCK:
          return "NumLock";
        case KeyEvent.VK_SCROLL_LOCK:
          return "ScrollLock";
        case KeyEvent.VK_F1:
          return "F1";
        case KeyEvent.VK_F2:
          return "F2";
        case KeyEvent.VK_F3:
          return "F3";
        case KeyEvent.VK_F4:
          return "F4";
        case KeyEvent.VK_F5:
          return "F5";
        case KeyEvent.VK_F6:
          return "F6";
        case KeyEvent.VK_F7:
          return "F7";
        case KeyEvent.VK_F8:
          return "F8";
        case KeyEvent.VK_F9:
          return "F9";
        case KeyEvent.VK_F10:
          return "F10";
        case KeyEvent.VK_F11:
          return "F11";
        case KeyEvent.VK_F12:
          return "F12";
        case KeyEvent.VK_F13:
          return "F13";
        case KeyEvent.VK_F14:
          return "F14";
        case KeyEvent.VK_F15:
          return "F15";
        case KeyEvent.VK_F16:
          return "F16";
        case KeyEvent.VK_F17:
          return "F17";
        case KeyEvent.VK_F18:
          return "F18";
        case KeyEvent.VK_F19:
          return "F19";
        case KeyEvent.VK_F20:
          return "F20";
        case KeyEvent.VK_F21:
          return "F21";
        case KeyEvent.VK_F22:
          return "F22";
        case KeyEvent.VK_F23:
          return "F23";
        case KeyEvent.VK_F24:
          return "F24";
        case KeyEvent.VK_PRINTSCREEN:
          return "PrintScreen";
        case KeyEvent.VK_INSERT:
          return "Insert";
        case KeyEvent.VK_HELP:
          return "Help";
        case KeyEvent.VK_META:
          return "Meta";
        case KeyEvent.VK_ALT_GRAPH:
          return "Alt";
        }

        return java.awt.event.KeyEvent.getKeyText(keyCode);
        
        //return "unknown(0x" + Integer.toString(keyCode, 16) + ")";
      }
}
