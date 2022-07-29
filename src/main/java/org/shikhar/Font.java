package org.shikhar;

import def.dom.CanvasRenderingContext2D;
import def.dom.TextMetrics;

public class Font {
	public CanvasRenderingContext2D context;
	String name;
	TextMetrics textMatrix;
	
	/**
	 * Creates new font  from resource
	 * @param name can be the name of loaded string
	 */
	public Font(String name,CanvasRenderingContext2D context){
		this.name=name;
		this.setContext(context);
		//this.name=name;
	}
	
	public void setContext(CanvasRenderingContext2D context){
		this.context=context;
		textMatrix=context.measureText("M");
	}
	
	public String getName(){
		return this.name;
	}
	
	
    /**
     * Gets the standard height of  text in this font.  
     * It is the sum of the  ascent + descent. 
     * @return    the standard height of the font.
     * @see       #getAscent()
     * @see       #getDescent()
     */
    public int getHeight() {
        return  (int) (textMatrix.width * 1.7);
    }
    

    /**
     * Returns the advance width of the specified character in this
     * <code>Font</code>.  The advance is the
     * distance from the leftmost point to the rightmost point on the
     * character's baseline.  Note that the advance of a
     * <code>String</code> is not necessarily the sum of the advances
     * of its characters.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="../lang/Character.html#supplementary"> supplementary
     * characters</a>. To support all Unicode characters, including
     * supplementary characters, use the {@link #charWidth(int)} method.
     *
     * @param ch the character to be measured
     * @return     the advance width of the specified character
     *                  in the <code>Font</code> described by this
     *                  <code>FontMetrics</code> object.
     * @see        #charsWidth(char[], int, int)
     * @see        #stringWidth(String)
     */
    public int charWidth(char ch) {
    	return (int) context.measureText(ch+"").width;
     }

    /**
     * Returns the total advance width for showing the specified
     * <code>String</code> in this <code>Font</code>.  The advance
     * is the distance from the leftmost point to the rightmost point
     * on the string's baseline.
     * <p>
     * Note that the advance of a <code>String</code> is
     * not necessarily the sum of the advances of its characters.
     * @param str the <code>String</code> to be measured
     * @return    the advance width of the specified <code>String</code>
     *                  in the <code>Font</code> described by this
     *                  <code>FontMetrics</code>.
     * @throws NullPointerException if str is null.
     * @see       #bytesWidth(byte[], int, int)
     * @see       #charsWidth(char[], int, int)
     * @see       #getStringBounds(String, Graphics)
     */
    public int stringWidth(String str) {
    	return (int) context.measureText(str+"").width;
    }

    
    /**
     * Returns the total advance width for showing the specified array
     * of characters in this <code>Font</code>.  The advance is the
     * distance from the leftmost point to the rightmost point on the
     * string's baseline.  The advance of a <code>String</code>
     * is not necessarily the sum of the advances of its characters.
     * This is equivalent to measuring a <code>String</code> of the
     * characters in the specified range.
     * @param data the array of characters to be measured
     * @param off the start offset of the characters in the array
     * @param len the number of characters to be measured from the array
     * @return    the advance width of the subarray of the specified
     *               <code>char</code> array in the font described by
     *               this <code>FontMetrics</code> object.
     * @throws    NullPointerException if <code>data</code> is null.
     * @throws    IndexOutOfBoundsException if the <code>off</code>
     *            and <code>len</code> arguments index characters outside
     *            the bounds of the <code>data</code> array.
     * @see       #charWidth(int)
     * @see       #charWidth(char)
     * @see       #bytesWidth(byte[], int, int)
     * @see       #stringWidth(String)
     */
    public int charsWidth(char data[], int off, int len) {
        return stringWidth(new String(data, off, len));
    }

    /**
     * returns height of character
     * @param ch
     * @return
     */
	public int charHeight(char ch) {
		return getHeight();
	}

	
	public int getAscent() {
		// TODO Auto-generated method stub
		return 15;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public int getLeading() {
		// TODO Auto-generated method stub
		return 2;
	}

	public int getDescent() {
		// TODO Auto-generated method stub
		return 6;
	}

  
}
