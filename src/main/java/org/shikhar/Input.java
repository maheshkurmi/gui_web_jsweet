package org.shikhar;

import static def.dom.Globals.window;
import static jsweet.util.StringTypes.mousedown;
import static jsweet.util.StringTypes.mousemove;
import static jsweet.util.StringTypes.mouseup;
import static jsweet.util.StringTypes.touchend;
import static jsweet.util.StringTypes.touchmove;
import static jsweet.util.StringTypes.touchstart;
import static jsweet.util.StringTypes.keydown;
import static jsweet.util.StringTypes.keyup;

import def.dom.Event;
import def.dom.HTMLCanvasElement;
import def.dom.MouseEvent;
import def.dom.Touch;
import def.dom.TouchEvent;

/**
 * Input handling based on LWJGL's Mouse & Keyboard classes.
 *
 * @author Mahesh Kurmi
 */
public class Input {
	Gui gui;
	HTMLCanvasElement canvas;
	
	public Input(Gui gui, HTMLCanvasElement canvas) {
		this.gui = gui;
		this.canvas=canvas;
		installListeners(canvas);
	}

	public Vector2 pageToCanvas(double x, double y) {
	    return new Vector2(x - canvas.offsetLeft, y - canvas.offsetTop);
	}
	
	public void installListeners(HTMLCanvasElement canvas) {
		canvas.addEventListener(mousedown, event -> {
			this.onInputDeviceDown(event, false);
			return null;
		}, true);
		canvas.addEventListener(mousemove, event -> {
			this.onInputDeviceMove(event, false);
			return null;
		}, true);
		canvas.addEventListener(mouseup, event -> {
			this.onInputDeviceUp(event, false);
			return null;
		}, true);
		canvas.addEventListener(touchstart, event -> {
			this.onInputDeviceDown(event, true);
			return null;
		}, true);
		canvas.addEventListener(touchmove, event -> {
			this.onInputDeviceMove(event, true);
			return null;
		}, true);
		canvas.addEventListener(touchend, event -> {
			this.onInputDeviceUp(event, true);
			return null;
		}, true);
		 canvas.addEventListener("touchcancel", event ->{
			 this.onInputDeviceUp(event, true);
		    });
		window.addEventListener(keydown, event -> {
			this.onInputDeviceKeyDown(event);
			return null;
		}, true);
		window.addEventListener(keyup, event -> {
			this.onInputDeviceKeyUp(event);
			return null;
		}, true);
	}

	public void onInputDeviceKeyDown(def.dom.KeyboardEvent event) {
		event.preventDefault();
		keyPressed((int)  event.keyCode, (int)event.charCode);
	}

	public void onInputDeviceKeyUp(def.dom.KeyboardEvent event) {
		event.preventDefault();
		keyReleased((int) event.keyCode, (int)event.charCode);
	}

	public void onInputDeviceDown(Event event, boolean touchDevice) {
		if (touchDevice) {
			for (int i = 0; i < ((TouchEvent) event).changedTouches.length; i++) {
				Touch t = ((TouchEvent) event).changedTouches.item(i);
				pointerPressed(t.pageX, t.pageY, AWTMouseEvent.BUTTON1);
				// point = this.factory.getGameManager().deviceToWorld(t.pageX, t.pageY);
				// player.onInputDeviceDown(point);
			}
		} else {
			int x = (int) ((MouseEvent) event).pageX;
			int y = (int) ((MouseEvent) event).pageY;

			pointerPressed(x, y, AWTMouseEvent.BUTTON1);
			// point = this.factory.getGameManager().deviceToWorld(((MouseEvent)
			// event).pageX, ((MouseEvent) event).pageY);
			// player.onInputDeviceDown(point);
		}
	}

	public void onInputDeviceUp(Event event, boolean touchDevice) {
		if (touchDevice) {
			for (int i = 0; i < ((TouchEvent) event).changedTouches.length; i++) {
				Touch t = ((TouchEvent) event).changedTouches.item(i);
				pointerReleased(t.pageX, t.pageY, AWTMouseEvent.BUTTON1);
				// point = this.factory.getGameManager().deviceToWorld(t.pageX, t.pageY);
				// player.onInputDeviceDown(point);
			}
		} else {
			int x = (int) ((MouseEvent) event).pageX;
			int y = (int) ((MouseEvent) event).pageY;

			pointerReleased(x, y, AWTMouseEvent.BUTTON1);
			// point = this.factory.getGameManager().deviceToWorld(((MouseEvent)
			// event).pageX, ((MouseEvent) event).pageY);
			// player.onInputDeviceDown(point);
		}
	}

	public void onInputDeviceMove(Event event, boolean touchDevice) {
		if (touchDevice) {
			for (int i = 0; i < ((TouchEvent) event).changedTouches.length; i++) {
				Touch t = ((TouchEvent) event).changedTouches.item(i);
				pointerDragged(t.pageX, t.pageY, AWTMouseEvent.BUTTON1);
				// point = this.factory.getGameManager().deviceToWorld(t.pageX, t.pageY);
				// player.onInputDeviceDown(point);
			}
		} else {
			int x = (int) ((MouseEvent) event).pageX;
			int y = (int) ((MouseEvent) event).pageY;

			pointerDragged(x, y, AWTMouseEvent.BUTTON1);
			// point = this.factory.getGameManager().deviceToWorld(((MouseEvent)
			// event).pageX, ((MouseEvent) event).pageY);
			// player.onInputDeviceDown(point);
		}
	}

	/**
	 *
	 * Key press key pre-processing.
	 *
	 * TODO abc type field in the upper right corner, mobile style.
	 *
	 */

	protected void keyPressed(int keyCode, int keyChar) {
		gui.handleKey(AWTKeyEvent.KEY_PRESSED, keyCode, keyChar, 0, false);
		gui.render();
	}

	protected void keyReleased(int keyCode, int keyChar) {
		gui.handleKey(AWTKeyEvent.KEY_RELEASED, keyCode, keyChar, 0, false);

		/*
		 * if ((popupowner != null) || (focusowner != null)) { hideTip(); int keychar =
		 * 0, key = 0;
		 * 
		 * if (key != 0) { AWTKeyEvent event = new AWTKeyEvent(AWTKeyEvent.KEY_RELEASED,
		 * 0); event.setKey(key); event.setKeyChar(keychar); processEvent(event); }
		 * paint(); }
		 */
		gui.render();
	}

	protected void pointerPressed(int x, int y, int button) {
		Vector2 v=pageToCanvas(x,y);
		gui.handleMouse((int) v.x, (int)y, AWTMouseEvent.MOUSE_PRESSED, button, 0, 0);
		gui.render();
	}

	// evm (touchscreen) events: entered/moved/pressed -> dragged ->
	// dragged/released/exited

	protected void pointerDragged(int x, int y, int btn) {
		Vector2 v=pageToCanvas(x,y);
		gui.handleMouse((int) v.x, (int)y, AWTMouseEvent.MOUSE_DRAGGED, btn, 0, 0);
		gui.render();

	}

	// evm (touchscreen) events: entered/moved/pressed -> dragged ->
	// dragged/released/exited

	protected void pointerReleased(int x, int y, int button) {
		Vector2 v=pageToCanvas(x,y);
		gui.handleMouse((int) v.x, (int)y, AWTMouseEvent.MOUSE_RELEASED, button, 0, 0);
		gui.render();
	}

	
	
}
