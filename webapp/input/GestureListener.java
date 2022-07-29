package org.shikhar.simphy.input;

import org.dyn4j.geometry.Vector2;

public interface GestureListener {


	/**
	 * Pinch to zoom gesture is performed
	 *@param center of rotation 
	 * @param scale
	 * @param angle
	 */
	public abstract void onZoomAndScale(Vector2 center,double scale, double angle);
	
	/**
	 * Swipe with 3 fingers from left edge
	 */
	public abstract void onSwipeFromLeft();
	
	/**
	 * Swipe with 3 fingers from right edge
	 */
	public abstract void onSwipeFromRight();
	
	/**
	 * Swipe with 3 fingers from top edge
	 */
	public abstract void onSwipeFromTop();
	
	/**
	 * Swipe with 3 fingers from bottom edge
	 */
	public abstract void onSwipeFromBottom();
	
	/**
	 * Swipe with 3 fingers horizontally but not from edges
	 */
	public abstract void onSwipeVerticallyMidScreen();
	
	/**
	 *Swipe with 3 fingers vertically but not from edges
	 */
	public abstract void onSwipeHorizontallyMidScreen();
	
	
	/**
	 *Swipe with 3 fingers vertically but not from edges
	 */
	public abstract void onDoubleTap();
	
}
