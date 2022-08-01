package org.shikhar;

public class Rectangle {
	public int x, y, width, height;

	public Rectangle() {
		
	}
	
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public Rectangle set(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}
	
	/**
	 * Computes the intersection of this <code>Rectangle</code> with the
	 * specified <code>Rectangle</code> and sets result in this rectangle If the
	 * two rectangles do not intersect, the result will be an empty rectangle.
	 * 
	 * @param x
	 *            top left x coordinate of other rect
	 * @param y
	 *            top left y coordinate of other rect
	 * @param width
	 *            Width of other rect
	 * @param height
	 *            Height coordinate of other rect
	 * @return this Rectangle for chaining
	 */
	public Rectangle intersect(int x, int y, int width, int height) {
		int tx1 = this.x;
		int ty1 = this.y;
		int rx1 = x;
		int ry1 = y;
		int tx2 = tx1;
		tx2 += this.width;
		int ty2 = ty1;
		ty2 += this.height;
		int rx2 = rx1;
		rx2 += width;
		int ry2 = ry1;
		ry2 += height;
		if (tx1 < rx1)
			tx1 = rx1;
		if (ty1 < ry1)
			ty1 = ry1;
		if (tx2 > rx2)
			tx2 = rx2;
		if (ty2 > ry2)
			ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;

		if (tx2 < 0)
			tx2 = 0;
		if (ty2 < 0)
			ty2 = 0;
		return this.set(tx1, ty1, tx2, ty2);
	}
	
	public boolean contains(int rx, int ry) {
		if ((this.x <= rx) && (this.x + this.width >= rx) && (this.y <= ry) && (this.y + this.height >= ry))
			return true;
		return false;
	}

	public boolean contains(Rectangle r) {
		if ((x <= r.x) && ((x + width) >= (r.x + r.width)) && (y <= r.y)
				&& ((y + height) >= (r.y + r.height))) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "["+this.x+","+this.y+","+this.width+","+this.height+"]";
	}

	public Rectangle copy() {
		// TODO Auto-generated method stub
		return new Rectangle().set(this.x,this.y,this.width,this.height);
	}
}
