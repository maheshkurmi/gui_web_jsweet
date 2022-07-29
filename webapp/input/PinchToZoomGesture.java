package org.shikhar.simphy.input;

import java.util.ArrayList;

import org.dyn4j.geometry.Vector2;
import org.shikhar.simphy.Preferences;

import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseEvent.PointerClass;

import jogamp.newt.Debug;

/**
 * 2 pointer zoom, a.k.a. <i>pinch to zoom</i>, gesture handler processing {@link MouseEvent}s
 * while producing {@link ZoomEvent}s if gesture is completed.
 * <p>
 * Zoom value lies within [0..2], with 1 as <i>1:1</i>.
 * </p>
 * <pre>
 *   - choosing the smallest surface edge (width/height -> x/y)
 *   - tolerating other fingers to be pressed and hence user to add functionality (scale, ..)
 * </pre>
 */
public class PinchToZoomGesture {
    public static final boolean DEBUG = false;//Debug.debug("Window.MouseEvent");
    private final NativeSurface surface;
    private final boolean allowMorePointer;
    private double zoom=1;
    private double angle=0;
    private Vector2 center;
    private boolean zoomFirstTouch;
    private boolean zoomMode;
    private final short[] pIds = new short[] { -1, -1};
    private double initialDist;
    private double initialAngle;
    private GestureListener gestureListener;
    private  Vector2[] gestureBeginPoints;
    private  Vector2[] gesturePoints;
    private long initialT, finalT, currentT;
    private long prevInitialT, prevFinalT;

    private long doubleTapMaxDelayMillis=500;
    private long doubleTapMaxDownMillis=100;
    private long swipeTimemaxMillis=1000;
    
    private double swipeSlopeIntolerance=3;
    private int distanceTolerance=100; //min distance in pixel which ca be ignored


    /**
     * @param surface the {@link NativeSurface}, which size is used to compute the relative zoom factor
     * @param gestureListener must not be null to receive gesture events
     * @param allowMorePointer if false, allow only 2 pressed pointers (safe and recommended), otherwise accept other pointer to be pressed.
     */
    public PinchToZoomGesture(final NativeSurface surface,GestureListener gestureListener, final boolean allowMorePointer) {
        clear();
        this.gestureListener=gestureListener;
        this.surface = surface;
        this.allowMorePointer = allowMorePointer;
        this.zoom = 1f;
        
    }

    @Override
    public String toString() {
        return "PinchZoom[1stTouch "+zoomFirstTouch+", in "+isWithinGesture()+", has "+" zoom= "+zoom+" angle= "+angle+"]";
    }

    public void clear() {
    	
       zoomFirstTouch = true;
       zoomMode = false;
       pIds[0] = -1;
       pIds[1] = -1;
       zoom=1;
       angle=0;
       gesturePoints=new Vector2[2];
       gestureBeginPoints=new Vector2[2];
    }


    public boolean isWithinGesture() {
        return zoomMode;
    }

    public Vector2[] getGesturePoints() {
        return gesturePoints;
    }

  
    public boolean process(final InputEvent in) {
        if(!(in instanceof MouseEvent))  {
            return false;
        }
        final MouseEvent pe = (MouseEvent)in;
        final int pointerDownCount = pe.getPointerCount();
       
        if( pe.getPointerType(0).getPointerClass() != MouseEvent.PointerClass.Onscreen ||
            ( !allowMorePointer && pointerDownCount > 2 ) ) {
            return false;
        }
        
        final int eventType = pe.getEventType();
        final boolean useY = surface.getSurfaceWidth() >= surface.getSurfaceHeight(); // use smallest dimension
        switch ( eventType ) {
            case MouseEvent.EVENT_MOUSE_PRESSED: 
                //System.err.println(this+".pressed: down "+pointerDownCount+", gPtr "+gesturePointers(pe, -1)+", event "+pe);
                if( 2 <= pointerDownCount ) { // && 1 == gesturePointers(pe, 0) /* w/o pressed pointer */) {
                 	beginGesture(pe);
                    return true;
                }
            	break;
            case MouseEvent.EVENT_MOUSE_RELEASED: 
                //System.err.println("released: down "+pointerDownCount);
             	if(gestureActive){
            		endGesture(pe);
            		return true;
            	}
             	gestureActive=false;
             	break;
            case MouseEvent.EVENT_MOUSE_DRAGGED:
                //System.err.println("dragged: down "+pe.getX(0)+","+pe.getY(0));
                //if(gestureActive ) {
               	calculateGesture(pe);
                return true;
                //}
           // break;

            default:
        }
        return false;
    }
    
    private boolean gestureActive=true;
    
    private void beginGesture(MouseEvent e){
        pIds[0] = e.getPointerId(0);
        pIds[1] = e.getPointerId(1);
        zoomFirstTouch=false;
        zoomMode = true;
        final int p0Idx = e.getPointerIdx(pIds[0]);
        final int p1Idx = e.getPointerIdx(pIds[1]);
        gestureBeginPoints[0]=new Vector2(e.getX(p0Idx),e.getY(p0Idx));
        gestureBeginPoints[1]=new Vector2(e.getX(p1Idx),e.getY(p1Idx));
        gesturePoints[0]=gestureBeginPoints[0].copy();
    	gesturePoints[1]=gestureBeginPoints[1].copy();
    	initialT =System.currentTimeMillis();
        center=new Vector2((e.getX(p1Idx)+e.getX(p0Idx))/2,(e.getY(p1Idx)+e.getY(p0Idx))/2);
        
        Vector2 v=new Vector2(e.getX(p1Idx)-e.getX(p0Idx),e.getY(p1Idx)-e.getY(p0Idx));
        initialDist=v.getMagnitude();
        initialAngle=v.getDirection();
        gestureActive=true;
        if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Gesture Begins");
    }
    
    private void endGesture(MouseEvent e) {
    	if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Gesture Ends");
        finalT=System.currentTimeMillis();
        calculateGesture(e);

         if (isDoubleTap()) {
        	if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Douple Tapped !");
        	gestureListener.onDoubleTap();
            return ;
        }
        if(e.getPointerCount()>=0 && gesturePoints[0]!=null){
        	Vector2 disp1=gesturePoints[0].difference(gestureBeginPoints[0]);
        	Vector2 disp2=gesturePoints[1].difference(gestureBeginPoints[1]);
        	if(gestureBeginPoints[0].x<distanceTolerance && gestureBeginPoints[1].x<distanceTolerance && disp1.x>8 && disp2.x>8){
               	if(disp1.x>swipeSlopeIntolerance*Math.abs(disp1.y)&&disp2.x>swipeSlopeIntolerance*Math.abs(disp2.y)){
               		if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Swipe from left !");
                    return ;
               	}
            }
           	if(gestureBeginPoints[0].x>surface.getSurfaceWidth()-distanceTolerance && gestureBeginPoints[1].x>surface.getSurfaceWidth()-distanceTolerance){
               	if(-disp1.x>swipeSlopeIntolerance*Math.abs(disp1.y)&& -disp2.x>swipeSlopeIntolerance*Math.abs(disp2.y)){
               		if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Swipe from right !");
                    return ;
               	}
            }
           	if(gestureBeginPoints[0].y<distanceTolerance && gestureBeginPoints[1].y<distanceTolerance){
               	if(disp1.y>swipeSlopeIntolerance*Math.abs(disp1.x)&&disp2.y>swipeSlopeIntolerance*Math.abs(disp2.x)){
               		if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Swipe from top !");
                    return ;
               	}
            }
           	if(gestureBeginPoints[0].y>surface.getSurfaceHeight()-distanceTolerance && gestureBeginPoints[1].y>surface.getSurfaceHeight()-distanceTolerance){
               	if(-disp1.y>swipeSlopeIntolerance*Math.abs(disp1.x)&&-disp2.y>swipeSlopeIntolerance*Math.abs(disp2.x)){
               		if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Swipe from bottom !");
                    return ;
               	}
            }

        }
     
       	gestureActive=false;
        prevInitialT = initialT;
        prevFinalT = finalT;

        clear();
 	}
    
    private void calculateGesture(MouseEvent e){
    	if(!gestureActive)return;
        final int p0Idx = e.getPointerIdx(pIds[0]);
        final int p1Idx = e.getPointerIdx(pIds[1]);
        if(p0Idx<0||p1Idx<0)return;
        //update pointer locations
        center.set((e.getX(p1Idx)+e.getX(p0Idx))/2,(e.getY(p1Idx)+e.getY(p0Idx))/2);
        gesturePoints[0].set(e.getX(p0Idx),e.getY(p0Idx));
        gesturePoints[1].set(e.getX(p1Idx),e.getY(p1Idx));

        currentT = System.currentTimeMillis();
        finalT=currentT;
        
        if( zoomMode ) {
          	Vector2 v=new Vector2(e.getX(p1Idx)-e.getX(p0Idx),e.getY(p1Idx)-e.getY(p0Idx));
            double d=v.getMagnitude();
            double a=v.getDirection();
            zoom= (d/initialDist);
            angle=a-initialAngle;
            gestureListener.onZoomAndScale(center,zoom, angle);
            initialDist=d;
            initialAngle=a;
        }
    	
    }
        
    public boolean isDoubleTap() {
        if (initialT - prevFinalT < doubleTapMaxDelayMillis && finalT - initialT < doubleTapMaxDownMillis && prevFinalT - prevInitialT < doubleTapMaxDownMillis) {
             return true;
        } else {
             return false;
        }
    }
}
