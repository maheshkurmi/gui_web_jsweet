
package org.shikhar;

import def.dom.HTMLImageElement;

/**
 * A image object can be used for rendering.
 * 
 * @author Mahesh kurmi
 */
public class AWTImage extends  HTMLImageElement{
	String src;
	int width, height;
	/**
	 * Create new Empty Image of zero width and zero height
	 * @see {@link #src} property of image to load image into it
	 */
	public AWTImage(String src){
		this.src=src;
	}
	
	
	public static AWTImage createImage(String src) {
		AWTImage img=new AWTImage(src);
		//img.src=src;
		return img;
	}
	
	
	
	/**
	 * Creates image with  
	 * @param src
	 * @param width
	 * @param height
	 */
	public AWTImage(String src ,int width, int height){
		this(src);
		
	}
	
    /**
     * Returns the width in pixels of the image
     * @return the width in pixels of the image
     */
    public int getWidth(){
    	return (int) width;//
    }
    /**
     * Returns the height in pixels of the image
     * @return the height in pixels of the image
     */
    public int getHeight(){
    	return (int) height;//iconFont!=null?iconFont.getHeight():height;
    }
    
  
    public String getName(){
    	return src;//src+(index==-1?"":"["+index+"]");
    }


	public int getScaledWidth() {
		// TODO Auto-generated method stub
		return (int) width;
	}
 
	public int getScaledHeight() {
		// TODO Auto-generated method stub
		return (int) height;
	}
 

}
