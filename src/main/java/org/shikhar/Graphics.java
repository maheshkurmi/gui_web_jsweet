package org.shikhar;

import static def.dom.Globals.console;
import static jsweet.util.Lang.union;

import def.dom.CanvasGradient;
import def.dom.CanvasPattern;
import def.dom.CanvasRenderingContext2D;
import def.dom.HTMLCanvasElement;
import jsweet.util.StringTypes;
import jsweet.util.union.Union4;

public class Graphics {
	public HTMLCanvasElement canvas;
	public CanvasRenderingContext2D context;
	public Rectangle clipRect = new Rectangle();
	public double transX, transY;
	public Color color;
	public double strokeWidth;
	private Font font;
	public double PIXEL_SCALE_FACTOR;
	public static int TOP = 0;
	public static int LEFT = 0;
	public static int BASELINE = 0;
	private Rectangle tmpRECT=new Rectangle(); 
	
	private CanvasGradient h_gradient,v_gradient;
	public Graphics(HTMLCanvasElement canvas) {
		// canvas = (HTMLCanvasElement) document.getElementById("canvas");
		this.canvas = canvas;
		PIXEL_SCALE_FACTOR = 2;
		context = canvas.getContext(StringTypes._2d);
	
	}
	
	
	
	public void begin(double width, double height, boolean clear) {
		context.setTransform(1, 0, 0, 1, 0, 0);
		context.scale(PIXEL_SCALE_FACTOR, PIXEL_SCALE_FACTOR);
		context.save();
		transX=0;
		transY=0;
		if(clear)context.clearRect(0, 0, width, height);
		clipRect.set(0, 0, (int)width, (int)height);
	}
	
	public void end() {
		context.restore();
	}

	public String toColor(int color) {
		return union("rgb(" + (color >> 16 & 0xFF) + "," + (color >> 8 & 0xFF) + "," + (color & 0xFF) + ")");
	}

	public void setColor(Color color) {
		if (color == null)
			return;
		this.color = color;
		String colorString = color.toString();// (color);
		context.strokeStyle = union(colorString);
		context.fillStyle = union(colorString);
		;
	}

	public void setAlpha(double alpha) {
		context.globalAlpha=alpha;
	}
	

	public double getAlpha() {
		return context.globalAlpha;
	}
	
	
	public Color getColor() {
		return color;
	}

	public void setStroke(double lineWidth) {
		this.strokeWidth = lineWidth;
		context.lineWidth = lineWidth;
	}

	public double getStroke() {
		return strokeWidth;
	}

	public void setFont(Font font) {
		// this.font=font;
		if (font != null) {
			this.font = font;
			// font.setContext(context);
			context.font = font.getName();
			// console.log(context.font+ ":"+font.name);
		}
	}

	public Font getFont() {
		return this.font;
	}

	public void clipRect(int x, int y, int width, int height) {
		
		this.context.beginPath();
        this.context.rect(x-0.5, y-0.5, width+1, height+1);
        this.context.clip();
        x+=transX;
        y+=transY;
        // console.log("before clip"+clipRect+ "x="+x+" y="+y+" w="+width+" h="+height);
        clipRect.intersect(x,y, width, height);
        // console.log("after clip"+clipRect);
        // clipRect.intersect((int)(x+transX),(int)(y+transY), width, height);
		// setClip(clipRect.x,clipRect.y,clipRect.width,clipRect.height);
	}

	public void setClip(double x, double y, double width, double height) {
		
		// x+=transX;
		// y+=transY;
		//if(height<200)height=200;
		clipRect.set( (int)(x+transX),(int)(y+transY),  (int)width,  (int)height);
		/*
		clipRect.x = (int) (x);
		clipRect.y = (int) (y);
		clipRect.width = (int) width;
		clipRect.height = (int) height;
		 */
		
		// x-=transX;
		// y-=transY;
		//Transform tx=context.getTransform();
	
		context.restore();
	    this.context.setTransform(this.PIXEL_SCALE_FACTOR, 0, 0, this.PIXEL_SCALE_FACTOR, this.transX*this.PIXEL_SCALE_FACTOR, this.transY*this.PIXEL_SCALE_FACTOR);
	    context.save();    
		/*
		 * context.moveTo(x, y); context.lineTo(x + width, y); context.lineTo(x + width,
		 * y + height); context.lineTo(x, y + height); context.lineTo(x, y);
		 * context.closePath();
		 */
		//context.transform(tx);
		//this.context.translate(this.transX, this.transY);
		context.beginPath();
		context.rect(x , y , width, height);
		context.clip();
		//console.log(clipRect);
		
		
	}

	public Rectangle getClipBounds() {
		return new Rectangle(clipRect.x-(int)transX, clipRect.y-(int)transY, clipRect.width, clipRect.height);
	}

	
	public void translate(int tx, int ty) {
		transX += tx;
		transY += ty;
		context.translate(tx, ty);
	}


	// drawing
	public void clearRect(double x, double y, double width, double height) {
		context.clearRect(x, y, width, height);

	}

	public void fillRect(double x, double y, double width, double height) {
		context.fillRect(x, y, width, height);
	}

	public void drawRect(double x, double y, double width, double height) {
		context.strokeRect(x, y, width, height);
	}
	
	public void drawDottedRect(double x, double y, double width, double height) {
		context.setLineDash(new double[] {1});
		context.strokeRect(x, y, width, height);
		context.setLineDash(new double[0]);
	}

	public void fillOval(double x, double y, double width, double height) {
		context.save();
		context.translate(x + width / 2, y + height / 2);
		context.scale(width, height);
		context.beginPath();
		context.arc(0, 0, 1, 0, 2 * Math.PI, false);
		context.restore();
		// context.fillStyle = '#000000';
		// context.fill();
		// context.lineWidth = 2;
		// context.strokeStyle = 'yellow';
		context.fill();
	}

	public void drawOval(double x, double y, double width, double height) {
		context.save();
		context.translate(x + width / 2, y + height / 2);
		context.scale(width, height);
		context.beginPath();
		context.arc(0, 0, 1, 0, 2 * Math.PI, false);
		context.restore();
		// context.fillStyle = '#000000';
		// context.fill();
		// context.lineWidth = 2;
		// context.strokeStyle = 'yellow';
		context.stroke();
	}

	public void drawString(String str, double x, double y) {
		context.fillText(str, x, y);
	}

	public void drawChars(char data[], int offset, int length, double x, double y) {
		drawString(new String(data, offset, length), x, y);
	}

	public void drawLine(double x, double y, double x2, double y2) {
		context.beginPath();
		context.moveTo(x, y);
		context.lineTo(x2, y2);
		context.stroke();
	}

	public void drawArc(double x, double y, double w, double h, double startAngle, double arcAngle) {
		context.arc(x, y, w, startAngle * 57.2957795131, (startAngle + arcAngle) * 57.2957795131, false);
	}

	public void drawImage(AWTImage img, double x, double y, double w, double h) {
		context.drawImage(img, x, y, w, h);
	}

	public void drawImage(AWTImage img, double x_src, double y_src, double width, double height, double transform,
			double x_dest, double y_dest, double anchor) {
		context.drawImage(img, x_src, y_src, width, height, x_dest, y_dest, width, height);
	}

	public void drawRegion(CanvasGradient gradient, double x_src, double y_src, double width, double height,
			double x_dest, double y_dest, double anchor) {
		Union4<String, CanvasGradient, CanvasPattern, Object> f = context.fillStyle;
		context.fillStyle = union(gradient);// (Union4<String, CanvasGradient, CanvasPattern, Object>) gradient;
		context.fillRect(x_dest, y_dest, width, height);
		context.fillStyle = f;
	}

	public double setLineWidth(double f) {
		double lw = context.lineWidth;
		context.lineWidth = f;
		return lw;
	}

	public double getLineWidth() {
		return context.lineWidth;
	}

	public void setClipEnabled(boolean b) {
		// TODO Auto-generated method stub

	}

	public void drawHGradient(Color c1, Color c2, double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		context.save();
		h_gradient=context.createLinearGradient(x, y, x, y+h);
		h_gradient.addColorStop(0, c1.toString());
		h_gradient.addColorStop(1, c2.toString());
		context.fillStyle=(Union4<String, CanvasGradient, CanvasPattern, Object>) h_gradient;
		//setColor(c1);
		context.fillRect(x, y, w, h);
		context.restore();
	}

	public void drawVGradient(Color c1, Color c2, double x, int y, double w, double h) {
		// TODO Auto-generated method stub
		context.save();
		h_gradient=context.createLinearGradient(x, y, x+w, y);
		h_gradient.addColorStop(0, c1.toString());
		h_gradient.addColorStop(1, c2.toString());
		context.fillStyle=(Union4<String, CanvasGradient, CanvasPattern, Object>) h_gradient;
		//setColor(c1);
		context.fillRect(x, y, w, h);
		context.restore();
	}

	public void drawImage(AWTImage icon, int x, int y, Color tintColor) {
		context.drawImage(icon, x, y);
	}

}
