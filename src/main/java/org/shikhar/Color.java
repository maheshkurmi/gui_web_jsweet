package org.shikhar;



/** A color class, holding the r, g, b and alpha component as floats in the range [0,1]. All methods perform clamping on the
 * internal values after execution.
  */
public class Color {
		/** the red, green, blue and alpha components **/
	public int r, g, b;
	public float a=1;
	final double FACTOR=1.1;
	String color;

	/** Constructs a new Color with all components set to 0. */
	public Color (int red, int green, int blue,float alpha) {
		this.r=red;
		this.g=green;
		this.b=blue;
		this.a=alpha;
		this.color="rgba("+red+","+green+","+blue+","+alpha+")";
	}
 
    public Color(int rgba8888) {
    	this.r = ((rgba8888 & 0xff000000) >>> 24) ;
		this.g = ((rgba8888 & 0x00ff0000) >>> 16) ;
		this.b = ((rgba8888 & 0x0000ff00) >>> 8) ;
		this.a = ((rgba8888 & 0x000000ff)) / 255f;
		
    	//this.b=c&255;
    	//this.g=(c >> 8) & 255;
    	//this.a=(c >> 16) & 255;
    	this.color="rgba("+this.r+","+this.g+","+this.b+","+this.a+")";
    	/*
    	String value = Integer
    			.toHexString(c);
    		while (value.length() < 8)
    			value = "0" + value;
    		this.color= "#"+value;
    		*/
	}
    

    public Color(String cssColor) {
    	this.color=cssColor;
    }
    
    public void setAlpha(float a) {
    	this.a=a;
    	this.color="rgba("+r+","+g+","+b+","+a+")";
    }
    
	public Color brighter() {
        if ( r == 255 && g == 255 && b == 255) {
            return new Color(255,255,255,a);
        }
        int r=(int) (this.r*FACTOR);
        int g=(int) (this.g*FACTOR);
        int b=(int) (this.b*FACTOR);

        if ( r > 255 ) r = 255;
        if ( g >  255 ) g = 255;
        if ( b >  255 ) b = 255;

        return new Color(r,g,b,a);
    }
    
   
    public Color darker() {
    	 
    	  if ( r == 0 && g == 0 && b == 0) {
              return new Color(0,0,0,a);
          }
          int r=(int) (this.r/FACTOR);
          int g=(int) (this.g/FACTOR);
          int b=(int) (this.b/FACTOR);

          return new Color(r,g,b,a);
    }
    
 
	/** Returns the color encoded as hex string with the format RRGGBBAA. */
	public String toString () {
		return this.color;
	}

	public static Color blendColors(Color c1, Color c2, float f) {
		Color c=new Color((int) (c1.r*(1-f)+c2.r*(f)),
				(int) (c1.r*(1-f)+c2.r*(f)),
				(int) (c1.r*(1-f)+c2.r*(f)),
				(c1.a*(1-f)+c2.a*(f)));
		return c;
	}

	
}
