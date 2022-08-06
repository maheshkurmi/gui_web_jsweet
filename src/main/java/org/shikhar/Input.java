package org.shikhar;

import static def.dom.Globals.console;
import static def.dom.Globals.window;
import static jsweet.util.StringTypes.mouseover;
import static jsweet.util.StringTypes.mouseout;
import static jsweet.util.StringTypes.mousedown;
import static jsweet.util.StringTypes.mousemove;
import static jsweet.util.StringTypes.mouseup;
import static jsweet.util.StringTypes.mousewheel;
import static jsweet.util.StringTypes.touchend;
import static jsweet.util.StringTypes.touchmove;
import static jsweet.util.StringTypes.touchstart;
import static jsweet.util.StringTypes.keydown;
import static jsweet.util.StringTypes.keyup;
import static jsweet.util.StringTypes.contextmenu;
import static jsweet.util.StringTypes.click;
import static jsweet.util.StringTypes.dblclick;

import def.dom.ClientRect;
import def.dom.HTMLCanvasElement;
import def.dom.KeyboardEvent;
import def.dom.MouseEvent;
import def.dom.Touch;
import def.dom.UIEvent;

/**
 * Input handling 
 *
 * @author Mahesh Kurmi
 */
public class Input {
	Gui gui;
	HTMLCanvasElement canvas;
	Vector2 TMP_VEC=new Vector2();
	Vector2 TMP_PrevPressPoint=new Vector2();
	int MAX_CLICK_DISTANCE=20;
	public Input(Gui gui, HTMLCanvasElement canvas) {
		this.gui = gui;
		this.canvas=canvas;
		installListeners(canvas);
	}

	private Vector2 pageToCanvas(double x, double y) {
		/*
		ClientRect bb = canvas.getBoundingClientRect();
	    const x = Math.floor( (event.clientX - bb.left) / bb.width * canvas.width );
	    const y = Math.floor( (event.clientY - bb.top) / bb.height * canvas.height );
	   */
	    return TMP_VEC.set(x - canvas.offsetLeft, y - canvas.offsetTop);
	}
	
	private int getModifiers(UIEvent e) {
		int modifier=0;
		if(e instanceof KeyboardEvent) {
			KeyboardEvent event=(KeyboardEvent) e;
			if(event.altKey)modifier|=InputEvent.ALT_MASK;
			if(event.ctrlKey||event.metaKey)modifier|=InputEvent.CTRL_MASK;
			if(event.shiftKey)modifier|=InputEvent.SHIFT_MASK;
			//if(modifier>0)console.log(modifier);
		}else if(e instanceof MouseEvent) {
			MouseEvent event=(MouseEvent) e;
			if(event.altKey)modifier|=InputEvent.ALT_MASK;
			if(event.ctrlKey||event.metaKey)modifier|=InputEvent.CTRL_MASK;
			if(event.shiftKey)modifier|=InputEvent.SHIFT_MASK;
		}
		return modifier;
		
	}
	
	private int getButton(MouseEvent e) {
		int btn=e.button;
		if(btn==0)btn=e.which;
		else btn++;
		switch (btn) {
	    case 1:
	      return AWTMouseEvent.BUTTON1;
	    case 2:
	    	return AWTMouseEvent.BUTTON2;
	      //log.textContent = 'Middle button clicked.';
	    case 3:
	    	return AWTMouseEvent.BUTTON3;
	      //log.textContent = 'Right button clicked.';
	    default:
	     return 0;
	  }
	}
	
	public void installListeners(HTMLCanvasElement canvas) {
		canvas.addEventListener(mousedown, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			pointerPressed((int)v.x, (int)v.y, getButton(event),(int) event.detail,getModifiers(event));
			return null;
		}, true);
		canvas.addEventListener(mousemove, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			pointerDragged((int)v.x, (int)v.y, getButton(event),(int) event.detail,getModifiers(event));
			return null;
		}, true);
		canvas.addEventListener(mouseup, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			pointerReleased((int)v.x, (int)v.y, getButton(event),(int) event.detail,getModifiers(event));
			return null;
		}, true);
		/*
		canvas.addEventListener(click, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			pointerClicked((int)v.x, (int)v.y, getButton(event),(int) event.detail,getModifiers(event));
			return null;
		}, true);
		*/
		canvas.addEventListener(contextmenu, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			pointerReleased((int)v.x, (int)v.y, AWTMouseEvent.BUTTON2,1,getModifiers(event));
			event.stopPropagation();
			// Stops touch from being also reported as mouse events
	        // in desktop browsers.
			event.preventDefault();
			return null;
		}, true);
		canvas.addEventListener(mouseover, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			gui.handleMouse((int)v.x, (int)v.y,  AWTMouseEvent.MOUSE_ENTERED, getButton(event),(int) event.detail, getModifiers(event));
			gui.handleInput();
			//pointerDragged((int)v.x, (int)v.y, getButton(event),(int) event.detail,getModifiers(event));
			return null;
		}, true);
		canvas.addEventListener(mouseout, event -> {
			Vector2 v=pageToCanvas(event.pageX,event.pageY);
			gui.handleMouse((int)v.x, (int)v.y, AWTMouseEvent.MOUSE_EXITED, getButton(event),(int) event.detail, getModifiers(event));
			return null;
		}, true);
		canvas.addEventListener(touchstart, event -> {
			 // We do not support multi-touch
			if (event.touches.length == 1) {
				Touch touch = event.touches.item(0);
				Vector2 v=pageToCanvas(touch.pageX,touch.pageY);
				pointerPressed((int)v.x, (int)v.y, AWTMouseEvent.BUTTON1,(int) event.detail,getModifiers(event));
			}
			event.stopPropagation();
			// Stops touch from being also reported as mouse events
	        // in desktop browsers.
			event.preventDefault();
			return null;
		}, true);
		canvas.addEventListener(mousewheel ,event -> {
			/*
			if (event.ctrlKey) {
                 this.mouseScrolled((int) TMP_VEC.x,(int) TMP_VEC.y,(int) -event.wheelDeltaY);
            }else {
 				 this.mouseScrolled((int) TMP_VEC.x,(int) TMP_VEC.y,(int) event.wheelDelta);
            }
            */
			this.mouseScrolled((int) TMP_VEC.x,(int) TMP_VEC.y,(int) event.wheelDelta);
			event.preventDefault();
			event.stopPropagation();
			return null;
		}, true);
		
		canvas.addEventListener(touchmove, event -> {
			if (event.changedTouches.length == 1) {
				Touch touch = event.changedTouches.item(0);
				Vector2 v=pageToCanvas(touch.pageX,touch.pageY);
				pointerDragged((int)v.x, (int)v.y, AWTMouseEvent.BUTTON1,(int) event.detail,getModifiers(event));
			}
			event.stopPropagation();
			// Stops touch from being also reported as mouse events
	        // in desktop browsers.
			event.preventDefault();
			return null;
		}, true);
		canvas.addEventListener(touchend, event -> {
			if (event.changedTouches.length == 1) {
				Touch touch = event.changedTouches.item(0);
				Vector2 v=pageToCanvas(touch.pageX,touch.pageY);
				pointerReleased((int)v.x, (int)v.y, AWTMouseEvent.BUTTON1,(int) event.detail,getModifiers(event));
			}
			event.stopPropagation();
			// Stops touch from being also reported as mouse events
	        // in desktop browsers.
			event.preventDefault();
			return null;
		}, true);
		/*
		 canvas.addEventListener("touchcancel", event ->{
			 if (event.changedTouches.length == 1) {
					Touch touch = event.changedTouches.item(0);
					Vector2 v=pageToCanvas(touch.pageX,touch.pageY);
					pointerReleased((int)v.x, (int)v.y, AWTMouseEvent.BUTTON1,(int) event.detail,getModifiers(event));
				}
		    });
		   */
		window.addEventListener(keydown, event -> {
			char keychar=event.key.length()!=1?0:event.key.charAt(0);
			keyPressed((int)  event.keyCode,keychar,getModifiers(event));
			//console.log(getModifiers(event));
			event.preventDefault();
			
			return null;
		}, true);
		window.addEventListener(keyup, event -> {
			char keychar=event.key.length()!=1?0:event.key.charAt(0);
			keyReleased((int)  event.keyCode, keychar,getModifiers(event));
			event.preventDefault();
			return null;
		}, true);
	}

/*
	public void onInputDeviceKeyDown(def.dom.KeyboardEvent event) {
		event.preventDefault();
		keyPressed((int)  event.keyCode, event.key,getModifiers(event));
	}

	public void onInputDeviceKeyUp(def.dom.KeyboardEvent event) {
		event.preventDefault();
		keyReleased((int) event.keyCode, event.key,getModifiers(event));
	}

	public void onInputDeviceDown(Event event, boolean touchDevice) {
		if (touchDevice) {
			for (int i = 0; i < ((TouchEvent) event).changedTouches.length; i++) {
				Touch t = ((TouchEvent) event).changedTouches.item(i);
				pointerPressed(t.pageX, t.pageY, AWTMouseEvent.BUTTON1,0,0);
				// point = this.factory.getGameManager().deviceToWorld(t.pageX, t.pageY);
				// player.onInputDeviceDown(point);
			}
		} else {
			int x = (int) ((MouseEvent) event).pageX;
			int y = (int) ((MouseEvent) event).pageY;

			pointerPressed(x, y, AWTMouseEvent.BUTTON1,(int) ((MouseEvent) event).detail,0);
			// point = this.factory.getGameManager().deviceToWorld(((MouseEvent)
			// event).pageX, ((MouseEvent) event).pageY);
			// player.onInputDeviceDown(point);
		}
	}

	public void onInputDeviceUp(Event event, boolean touchDevice) {
		if (touchDevice) {
			for (int i = 0; i < ((TouchEvent) event).changedTouches.length; i++) {
				Touch t = ((TouchEvent) event).changedTouches.item(i);
				pointerReleased(t.pageX, t.pageY, AWTMouseEvent.BUTTON1,0,0);
				// point = this.factory.getGameManager().deviceToWorld(t.pageX, t.pageY);
				// player.onInputDeviceDown(point);
			}
		} else {
			int x = (int) ((MouseEvent) event).pageX;
			int y = (int) ((MouseEvent) event).pageY;

			pointerReleased(x, y, AWTMouseEvent.BUTTON1,0,0);
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
*/

	/**
	 *
	 * Key press key pre-processing.
	 *
	 * TODO abc type field in the upper right corner, mobile style.
	 * @param modifier 
	 *
	 */
	protected void keyPressed(int keyCode, char keyChar, int modifiers) {
		//console.log("Key pressed" + "code=="+keyCode+" keyChar="+keyChar);
		gui.handleKey(AWTKeyEvent.KEY_PRESSED, keyCode, keyChar, modifiers, false);
	
		gui.handleInput();
	}

	protected void keyReleased(int keyCode, char keyChar,int modifiers) {
		//console.log("Key pressed" + "code=="+keyCode+" keyChar="+keyChar);
		gui.handleKey(AWTKeyEvent.KEY_RELEASED, keyCode, keyChar, modifiers, false);

		/*
		 * if ((popupowner != null) || (focusowner != null)) { hideTip(); int keychar =
		 * 0, key = 0;
		 * 
		 * if (key != 0) { AWTKeyEvent event = new AWTKeyEvent(AWTKeyEvent.KEY_RELEASED,
		 * 0); event.setKey(key); event.setKeyChar(keychar); processEvent(event); }
		 * paint(); }
		 */
		gui.handleInput();
	}

	protected void pointerPressed(int x, int y, int button,int clickcount,int modifiers) {
		TMP_PrevPressPoint.set(x,y);
		gui.handleMouse(x,y , AWTMouseEvent.MOUSE_PRESSED, button, 0,  modifiers);
		gui.handleInput();
	}

	// evm (touchscreen) events: entered/moved/pressed -> dragged ->
	// dragged/released/exited

	protected void pointerDragged(int x, int y, int btn,int clickcount,int modifiers) {
		gui.handleMouse(x,y , btn>0?AWTMouseEvent.MOUSE_DRAGGED:AWTMouseEvent.MOUSE_MOVED, btn, 0, modifiers);
		gui.handleInput();

	}

	// evm (touchscreen) events: entered/moved/pressed -> dragged ->
	// dragged/released/exited

	protected void pointerReleased(int x, int y, int button,int clickcount,int modifiers) {
		if(clickcount==0||TMP_PrevPressPoint.distance(x,y)>MAX_CLICK_DISTANCE) {
			gui.handleMouse(x,y ,AWTMouseEvent.MOUSE_RELEASED, button, 0,modifiers);
		}else {
			gui.handleMouse(x,y ,AWTMouseEvent.MOUSE_CLICKED, button, clickcount,modifiers);
		}
		gui.handleInput();
	}
	

	protected void mouseScrolled(int x, int y, int scroll) {
		gui.handleMouseWheel(x, y, scroll);
		//console.log("Mousewheel at " +x+","+y+" scroll="+scroll);
		gui.handleInput();
	}
	
}
