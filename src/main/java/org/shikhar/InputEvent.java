package org.shikhar;

public class InputEvent extends AWTEvent {

	public static final int ALT_MASK = 1;
	public static final int SHIFT_MASK = 4;
	public static final int CTRL_MASK = 2;
	public static final int META_MASK =8; 
	int mask;

	protected InputEvent(int id, int mask) {
		super(id);
		this.mask = mask;
	}

	public boolean isAltDown() {
		return (mask&ALT_MASK) !=0;
	}

	public boolean isShiftDown() {
		return (mask&SHIFT_MASK) !=0;
	}

	public boolean isControlDown()  {
		return (mask&CTRL_MASK) !=0;
	}

	public boolean isMetaDown() {
		return (mask&META_MASK) !=0;
	}

	public int getModifiers() {
		return mask;
	}

	// linje 6096 TODO

	public static int getField(String string) {
		return 0;
	}

}
