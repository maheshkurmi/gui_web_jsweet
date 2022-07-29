package org.shikhar.simphy.input;

import org.dyn4j.geometry.Vector2;
import org.shikhar.simphy.input.Finger.VelocityTracker;
import org.shikhar.simphy.log.Logger;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseEvent.PointerType;

/** Touch gestures (tap, long press, fling, pan, zoom, pinch) and hands them to a
 * {@link GestureListener}.
*/
public class GestureDetector {
	final GestureListener listener;
	private long maxFlingDelay;
	private boolean pinching=false;
	private boolean panning=false;

	private long gestureStartTime;
	private VelocityTracker tracker1 ;
	private  VelocityTracker tracker2 ;
	private Vector2 v1=new Vector2();
	private Vector2 v2=new Vector2();

	private Logger Log= Logger.getLogger("GestureDetector");

	/** Creates a new GestureDetector with default values: halfTapSquareSize=20, tapCountInterval=0.4f, longPressDuration=1.1f,
	 * maxFlingDelay=0.15f. */
	public GestureDetector (GestureListener listener) {
		this.listener = listener;
	}

	public boolean process(Mouse mouse) {
		tracker1=null;
		tracker2=null;
		for(Finger f:mouse.getFingers()) {
			if(tracker1==null && f.isActive()) {
				tracker1=f.velTracker;
			}else if(tracker2==null && f.isActive()) {
				tracker2=f.velTracker;
			}
		}
		
		if(tracker1==null || tracker2==null) {
			if(panning) {
				panning=false;
				listener.panStop();
				if(v1.getMagnitudeSquared()>1000 &&v2.getMagnitudeSquared()>1000) {
					listener.fling((v1.x+v2.x)/2, (v1.y+v2.y)/2);
				}
			}
			if(pinching) {
				pinching=false;
				listener.pinchStop();
			}
			return false;
		}
		v1.set(tracker1.getVelocityX(),tracker1.getVelocityY());
		v2.set(tracker2.getVelocityX(),tracker2.getVelocityY());
		
		double th=Math.abs(v1.getAngleBetween(v2));
		if(th<0.2) {
			panning=true;
			pinching=false;
			listener.pan(tracker1.lastX,tracker1.lastY,tracker1.deltaX,tracker1.deltaY);
		}else if(th>1.2) {
			pinching=true;
			panning=false;
			listener.pinch(tracker1.initialPointer,tracker2.initialPointer,v1.set(tracker1.lastX,tracker1.lastY),v2.set(tracker2.lastX,tracker2.lastY));
		}else {
			return false;
		}
		
		return true;
	}
	
	



	public boolean isPanning () {
		return panning;
	}

	public void reset () {
		gestureStartTime = 0;
		panning = false;
		pinching = false;
	
	}

	public void setMaxFlingDelay (long maxFlingDelay) {
		this.maxFlingDelay = maxFlingDelay;
	}

	/** Register an instance of this class with a {@link GestureDetector} to receive gestures such as taps, long presses, flings,
	 * panning or pinch zooming. Each method returns a boolean indicating if the event should be handed to the next listener (false
	 * to hand it to the next listener, true otherwise).
	 **/
	public static interface GestureListener {
		/** Called when the user drags a finger over the screen.
		 * @param deltaX the difference in pixels to the last drag event on x.
		 * @param deltaY the difference in pixels to the last drag event on y. */
		public boolean pan (double x, double y, double deltaX, double deltaY);

		/** Called when no longer panning. */
		public boolean panStop ();

		/** Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
		 * pixels per second.
		 * @param velocityX velocity on x in seconds right as positive
		 * @param velocityY velocity on y in seconds down as positive*/
		public boolean fling (double velocityX, double velocityY);

		/** Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
		 * current positions.
		 * @param initialPointer1
		 * @param initialPointer2
		 * @param pointer1
		 * @param pointer2 */
		public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2);

		/** Called when no longer pinching. */
		public void pinchStop ();
	}

	
}