package org.shikhar;



import static def.dom.Globals.console;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.shikhar.Gui.CustomComponent;

import def.dom.HTMLCanvasElement;
import def.js.JSON;



public class Gui   {
	/**
	 * Some default values
	 */
	private static final int DEFAULT_COLUMN_WIDTH = 80;
	private static final int MINIMUM_COLUMN_WIDTH = 30;
	private static final String PROPERTY_SMARTWIDTHS = ":smartwidths";

	private  Font font;
	private  Color c_bg;
	private  Color c_fg;
	private  Color c_border;
	private  Color c_hover;
	private  Color c_press;
	private  Color c_focus;
	private  Color c_disable;
	private  Color c_shadow;
	
	private  Color c_text_fg;
	private  Color c_text_bg;
	private  Color c_select_bg;
	private  Color c_select_fg;
	
	private  Color c_tooltip_bg;
	private  Color c_tooltip_fg;
	private  Color c_menu_bg;
	private  Color c_menu_fg;
			
	
	private  Color c_bgimgae_tint=new Color(255,0,0,1);
	private  Color c_icon_tint=new Color(255,0,0,1);



	private  Color c_resizeborder;	
	private  Color c_window_border;
	private  Color c_ctrl;
	private  Color col_gradient1, col_gradient2;
	private  Color c_overlay=new Color(0,0,0,50);
	/**Size of block in pixels*/
	private  int block=12;
	/**Pixel equivalent margin*/
	private int margin_1=1;
	public static Color[] themeColors;


	private transient String clipboard;

	private transient boolean allI18n = false; // for I18N

	// enter the starting characters of a list item text within a short time to
	// select
	private transient String findprefix = "";
	private transient long findtime;
	/**Root Widget =>Desktop*/

	private Object content = createImpl("desktop");
	public transient Object mouseinside;
	private transient Object insidepart;
	public transient Object mousepressed;
	private transient Object pressedpart;
	private transient int referencex, referencey;
	private transient int mousex, mousey;
	private transient Object focusowner;
	private transient boolean focusinside;
	protected transient Object popupowner;
	private transient Object tooltipowner;
	// private transient int pressedkey;

	private static final int DRAG_ENTERED = AWTEvent.RESERVED_ID_MAX + 1;
	private static final int DRAG_EXITED = AWTEvent.RESERVED_ID_MAX + 2;

	private static long WHEEL_MASK = 0;
	private static int MOUSE_WHEEL = 0;
//	private static Method wheelrotation = null;
	private static int evm = 0;
	private Graphics g = null;
	private Input input = null;
	private int width=300; private int height=200;

	private HTMLCanvasElement awtComponent;
	
	int cursor_timer=0;
	/**cursor is visible/hidded for 30 frames each*/
	private int CURSOR_DELAY=20;

	private DefaultGuiEventsHandler defaultHandler;
	
	
	//FBO Setup
	private static int TEXTURE_WIDTH = 2048; // NOTE: texture size cannot be
	// larger than
	private static int TEXTURE_HEIGHT = 2048; // the rendering window size in

	private static boolean useFBO = false;
	private static boolean invalidate=true;
	protected static boolean contextCreated = false;
	private static int fboId, fboMsaaId; // IDs of FBOs
	protected static int textureId; // ID of texture
	private static int rboId, rboColorId,  rboStencilId; // IDs of
																// Renderbuffer
																// objects
	static boolean repaintNeeded=true;
	/** List of beans to be rendered, needed due to offscreen rendering*/
	private ArrayList<CustomComponent> beans=new ArrayList<CustomComponent>();
	public Graphics getRenderer(){
		return g;
	}
	
	public Input getInput(){
		return input;
	}
	
	/**
	 * Sets default  gui event handler
	 * if no method is explicitely defined on widget event, it calls handlers actionPerformedMethod
	 * whener widget event occurs
	 * @param handler
	 */
	public void setDefauthandler(DefaultGuiEventsHandler  handler){
		this.defaultHandler=handler;
	}
	
	/**
	 * creates gui without renderer or input manager and hence used only in xml reader to read 
	 * gui from xml
	 */
	
	
	public Gui(HTMLCanvasElement component  ){
		this.awtComponent=component;
	
		this.g=new Graphics(component);
		
		this.input=new Input(this,component);

		activeTimer=new GuiTimer();
		
		setDefaultFont(new Font("15px Arial",g.context));
		setColors(DEFAULT_THEME);
		reset();
	}
	
	/**
	 * removes all widgets and adds a fresh empty desktop
	 */
	public void reset(){
		content = createImpl("desktop");
		beans.clear();
		width=(int) (awtComponent.width/g.PIXEL_SCALE_FACTOR);
		height=(int) (awtComponent.height/g.PIXEL_SCALE_FACTOR);
		block=font.getHeight();
		setRectangle(content, "bounds", 0, 0, width, height);
		setViewPort(width, height);
	}
	
	public Color getDefaultBGColor(){
		return c_bg;
	}
	
	public Color getDefaultFGColor(){
		return c_text_fg;
	}
	
	public Color getDefaultDisabledColor(){
		return c_disable;
	}
	
	public Color getDefaultBorderColor(){
		return c_border;
	}
	
	/**Colors in 0x alpha red green blue argb order*/
	
	/*rgba*/
	public static final int[] GRAY_THEME={0xe6e6e6ff, 0x000000ff, 0xf0f0f0ff, 0x505050ff, 0xb0b0b0ff, 0xedefffaa, 0xb9b9b9ff, 0x89899aff, 0xc5c5ddff,0x3a3a3dff,0x000000ff,0x000000ff,0xbbbbddff};
	public static final int[] SANDSTONE_THEME={0xeeeeccff, 0x362c00ff, 0xffffffff,0x999966ff, 0xb0b096ff, 0xededcbff, 0xcccc99ff, 0xcc6600ff, 0xffcc66ff,0x362c00ff,0xffffffff,0x00000000,0x00000000};
	public static final int[] SKY_THEME={0xf0f0ffff, 0x0000a0ff, 0xffffffff, 0x8080ffff, 0xb0b0b0ff, 0xedededff, 0xb0b0ffff, 0xff0000ff, 0xfde0e0ff,0x0000a0ff,0x00000000,0x00000000,0x00000000};
	public static final int[] BLUE_THEME={0x6375d6ff, 0xffffffff, 0x7f8fddff,	0xd6dff5ff, 0x9caae5ff, 0x666666ff, 0x003399ff, 0xff3333ff, 0x666666ff,0xffffffff,0xffffffff,0x00000000,0x00000000};
	public static final int[] GREEN_THEME={0xC2FF69ff, 0x004D40ff,0xEFFF90ff,  0x2B7402FF, 0x999999FF, 0xCCFF90AA, 0x4cd137BB, 0x86B90E59, 0xFDD835DD,0x004D40ff,0x00000000,0x00000000,0x00000000};
	public static final int[] BLACK_THEME={0x1a1729af,0xffedb6ff,0x666769b4,0xc9863fff,0x9caaa3ff,0x666666cd,0x2d3333ae,0xfff1d4ff,0xff990eff,0xbbbbbbff,0xffffffff,0xffffffff,0x507050ff};
	public static final int[] DARK_THEME={0x3e3f41cc,0xbbbeb6ff,0x404040bc,0xc0c0c083,0x9caaa3cc,0x666666cd,0x2f2f28cc,0xfff1d4cc,0xff990e5a,0xbbbbbbcc,0xffffffff,0xffffffff,0x507050ff};

	public static  int[] DEFAULT_THEME=DARK_THEME.clone();
	 
	/*
	public static final int[] GRAY_THEME={0xffe6e6e6, 0xff000000, 0xffffffff, 0xff909090, 0xffb0b0b0, 0xffededed, 0xffb9b9b9, 0xff89899a, 0xffc5c5dd,0xffffffff,0xff000000,0x00000000,0x00000000};
	public static final int[] SANDSTONE_THEME={0xffeeeecc, 0xff362c00, 0xffffffff,0xff999966, 0xffb0b096, 0xffededcb, 0xffcccc99, 0xffcc6600, 0xffffcc66,0xff362c00,0xffffffff,0x00000000,0x00000000};
	public static final int[] SKY_THEME={0xfff0f0ff, 0xff0000a0, 0xffffffff, 0xff8080ff, 0xffb0b0b0, 0xffededed, 0xffb0b0ff, 0xffff0000, 0xfffde0e0,0xff0000a0,0x00000000,0x00000000,0x00000000};
	public static final int[] BLUE_THEME={0xff6375d6, 0xffffffff, 0xff7f8fdd,	0xffd6dff5, 0xff9caae5, 0xff666666, 0xff003399, 0xffff3333, 0xff666666,0xffffffff,0xffffffff,0x00000000,0x00000000};
	public static final int[] GREEN_THEME={0xffC2FF69, 0xff004D40,0xffEFFF90,  0xFF2B7402, 0xFF999999, 0xAACCFF90, 0xBB4cd137, 0x5986B90E, 0xDDFDD835,0xff004D40,0x00000000,0x00000000,0x00000000};
	public static final int[] BLACK_THEME={-1357244631,-4682,-1268357271,-3570113,-6509917,-848927130,-1372769485,-3628,-26354,0xffbbbbbb,0xffffffff,0xffffffff,-11505584};
	public static  int[] DEFAULT_THEME=BLACK_THEME;
	*/
	public Color[] getColors() {
		return new Color[]{c_bg,c_text_fg,c_text_bg,c_border,c_disable,c_hover,c_press,c_focus,c_select_bg,c_icon_tint};
		//setColors(0xC2FF69, 0x004D40,0xEFFF90,  0xFF2B7402, 0xFF999999, 0xCCFF90, 0x4cd137, 0x5986B90E, 0xFDD835,DEFAULT_RESIZE_BORDER_COLOR);
	}
	
	/*
	public  void setCurrentUITheme(){
	//	UIDefaults defaults=	UIManager.getLookAndFeelDefaults();
	c_bg = SystemColor.control;
	c_fg = SystemColor.controlText;
	c_text_bg = SystemColor.text;
	c_text_fg =SystemColor.controlText;
	c_border=SystemColor.windowBorder;
	c_hover=SystemColor.controlLtHighlight;
	c_disable=c_bg.brighter();
	c_press=SystemColor.controlDkShadow;
	c_focus =SystemColor.textInactiveText;
	//c_focus=new Color(c_focus.getRed(),c_focus.getGreen(),c_focus.getBlue(),100);
	c_select_bg =SystemColor.textHighlight;
	c_select_fg =SystemColor.textHighlightText;
	
	c_icon_tint=c_fg;
	c_tooltip_fg=SystemColor.info;
	c_tooltip_fg=SystemColor.infoText;
	c_menu_fg=SystemColor.menu;
	c_menu_bg=SystemColor.menuText;
	//c_resizeborder =c_border.darker();
	DEFAULT_THEME[0]=c_bg.getRGB();
	DEFAULT_THEME[1]=c_text_fg.getRGB();
	DEFAULT_THEME[2]=c_text_bg.getRGB();
	DEFAULT_THEME[3]=c_border.getRGB();
	DEFAULT_THEME[4]=c_disable.getRGB();
	DEFAULT_THEME[5]=c_hover.getRGB();
	DEFAULT_THEME[6]=c_press.getRGB();
	DEFAULT_THEME[7]=c_focus.getRGB();
	DEFAULT_THEME[8]=c_select_bg.getRGB();
	DEFAULT_THEME[9]=c_select_fg.getRGB();
	DEFAULT_THEME[10]=c_icon_tint.getRGB();
	//DEFAULT_THEME[11]=c_window_border.getRGB();


	
	int r1 = c_bg.getRed();
	int r2 = c_press.getRed();
	int g1 = c_bg.getGreen();
	int g2 = c_press.getGreen();
	int b1 = c_bg.getBlue();
	int b2 = c_press.getBlue();
	int a1 = (c_bg.getAlpha()+255)/2;
	int a2 = (c_press.getAlpha()+255)/2;
	
	col_gradient1=new Color(r1,g1,b1,a1);
	col_gradient2=new Color(r2,g2,b2,a2);

	
	c_shadow=UIManager.getColor("Button.shadow");
	themeColors=getColors();
	}
	*/
	/**
	 * Sets the 9 colors used for components, and repaints the whole UI
	 *
	 * @param background
	 *            the backround of panels (dialogs, desktops), and disabled
	 *            controls, not editable texts, lines between list items (the
	 *            default value if <i>#e6e6e6</i>)
	 * @param text
	 *            for text, arrow foreground (<i>black</i> by default)
	 * @param textbackground
	 *            the background of text components, and lists (<i>white</i> by
	 *            default)
	 * @param border
	 *            for outer in inner borders of enabled components
	 *            (<i>#909090</i> by default)
	 * @param disable
	 *            for text, border, arrow color in disabled components
	 *            (<i>#b0b0b0</i> by default)
	 * @param hover
	 *            indicates that the mouse is inside a button area
	 *            (<i>#ededed</i> by default)
	 * @param press
	 *            for pressed buttons, gradient image is calculated using the
	 *            background and this press color (<i>#b9b9b9</i> by default)
	 * @param focus
	 *            for text caret and rectagle color marking the focus owner
	 *            (<i>#89899a</i> by default)
	 * @param select
	 *            used as the background of selected text, and list items, and
	 *            in slider (<i>#c5c5dd</i> by default)
	 * @param resizeBorder
	 *            used as color for the border decorator for resizable dialogs.
	 *            (<i>DEFAULT_RESIZE_BORDER_COLOR</i> by default)
	 * @param iconTintColor
	 *            used as color for tinting images.
	 *            (<i>white</i> by default)
	 */
	public void setColors(int background, int text, int textbackground,
			int border, int disable, int hover, int press, int focus,
			int select,int iconTintColor) {
		c_bg = new Color(background);
		
		c_text_fg = new Color(text);
		c_fg=c_text_fg;
		c_text_bg = new Color(textbackground);
		c_border = new Color(border);
		c_disable = new Color(disable);
		c_hover = new Color(hover);
		c_press = new Color(press);
		c_focus = new Color(focus);
		c_ctrl=Color.blendColors(c_bg,c_press,0.5f);
		//c_focus=new Color(c_focus.getRed(),c_focus.getGreen(),c_focus.getBlue(),100);
		c_select_bg = new Color(select);
		c_select_fg= c_select_bg.brighter();
		c_tooltip_bg=c_hover;//Color.blendColors(Color.YELLOW, c_hover,0.5f);//
		c_tooltip_fg=c_text_fg;
		c_menu_bg=Color.blendColors(c_bg,c_text_bg,0.5f);
		c_menu_fg=c_text_fg;
		c_resizeborder = c_border.brighter();
		c_window_border=c_border.darker();
		float r1 = c_bg.r;
		float r2 = c_press.r;
		float g1 = c_bg.g;
		float g2 = c_press.g;
		float b1 = c_bg.b;
		float b2 = c_press.b;
		float a1 = (c_bg.a+1)/2;
		float a2 = (c_press.a+1)/2;
		
		col_gradient1=new Color(c_bg.r,c_bg.g,c_bg.b,(c_bg.a+1)/2);
		col_gradient2=new Color(c_press.r,c_press.g,c_press.b,(c_press.a+1)/2);

		c_icon_tint=new Color(iconTintColor);
		c_shadow=c_press.darker();  
		themeColors=getColors();
		repaintNeeded=true;
	}

	/**
	 * Sets current theme color
	 * @param colors array of 10 colors in order 
	 * "Background","Text","Text-Background","Border","Disable",
	 * "Hover","Press","Focus","Select","Resize Border" 
	 */
	public void setColors(int[] c){
		setColors(c[0],c[1],c[2],c[3],c[4],c[5],c[6],c[7],c[8],c[9]);
		
	}
				
	
	public void setColors(int background, int text, int textbackground,
			int border, int disable, int hover, int press, int focus, int select) {
		setColors(background, text, textbackground, border, disable, hover,
				press, focus, select,text);
	}
	
	
	public void resetCanvasWidget(Object comp) {
		// Call init on all canvas components
		if(getComponentClass(comp)=="bean" && getComponent(comp, "bean") instanceof Canvas){
			//System.out.println("found canvas "+toString(comp));
			((Canvas)(getComponent(comp, "bean"))).reset();
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			resetCanvasWidget(comp);
		}
	}
	
	/** Disposes resources created by component, so must be called in opengl thread*/
	public void disposeComponent(Object comp) {
		// Call init on all canvas components
		if(getComponentClass(comp)=="bean" && getComponent(comp, "bean") instanceof Canvas){
			//System.out.println("found canvas "+toString(comp));
			(getComponent(comp, "bean")).dispose(g);
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			disposeComponent(comp);
		}
		repaintNeeded=true;
	}
	
	/** Searches component and children and adds to list of beans if found any*/
	private void addBeanToList(Object comp) {
		// Call init on all canvas components
		if(getComponentClass(comp)=="bean"){
			CustomComponent bean =getComponent(comp, "bean");
			if(bean!=null && !beans.contains(bean)){
				beans.add(bean);
			}
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			addBeanToList(comp);
		}
	}
	
	/** Searches component and children and removes from list of beans if found any*/
	private void removeBeanFromList(Object comp) {
		// Call init on all canvas components
		if(getComponentClass(comp)=="bean"){
			CustomComponent bean =getComponent(comp, "bean");
			if(bean!=null &&beans.contains(bean)){
				beans.remove(bean);
			}
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			removeBeanFromList(comp);
		}
	}
	
	/*
	public void initGL(GL2 gl,Object comp) {
		// Call init on all canvas components
		if(getClass(comp)=="bean" && getComponent(comp, "bean") instanceof CanvasBean){
			System.out.println("found canvas "+toString(comp));
			((CanvasBean)(getComponent(comp, "bean"))).initGL(gl);
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			initGL(gl,comp);
		}
	}
	
	
	
	public void disposeGL(GL2 gl,Object comp) {
		// Call init on all canvas components
		if(getClass(comp)=="bean" && getComponent(comp, "bean") instanceof CanvasBean){
			System.out.println("found canvas "+toString(comp));
			((CanvasBean)(getComponent(comp, "bean"))).disposeGL(gl);
			return;
		}
		for (comp = get(comp, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			disposeGL(gl,comp);
		}
	}
	
	*/
	public int getViewPortWidth(){
		return width;
	}
	
	public int getViewPortHeight(){
		return height;
	}
	
	
	public void setViewPort(int  width,int height){
		this.width=width;
		this.height=height;
		if(width>TEXTURE_WIDTH||height>TEXTURE_HEIGHT){
			height=Math.max(width,TEXTURE_WIDTH);
			width=Math.max(height,TEXTURE_HEIGHT);
			TEXTURE_WIDTH=Math.max(TEXTURE_WIDTH,MathUtils.nextPowerOfTwo(width));
			TEXTURE_HEIGHT=Math.max(TEXTURE_HEIGHT, MathUtils.nextPowerOfTwo(height));
			
			invalidate=true;
			contextCreated=false;
		}
		setRectangle(content, "bounds", 0, 0, width, height);
		//g.setVieportSize(width,height);
		validate(content);
		closeup();
		if (!focusinside) {
			//requestFocus();
		}
		evm=0;
		repaintNeeded=true;
		
	}

	/**
	 * Gets the preferred size of the root component
	 *
	 * @return a dimension object indicating the root component's preferred size
	 */
	public Dimension getPreferredSize() {
		return getPreferredSize(content);
	}


	public void renderComp(CustomComponent component){
		if(g==null ||component==null)return;
		
		Object c=component.component;
		Object p= this.getParentDialog(c);
		if( p!=null && getBoolean(p,"visible") && get(p, ":minimized")==null) {
			Rectangle bounds = getAbsoluteRectangle(c, "bounds");
			if (bounds != null) {
				Rectangle prevClip=g.getClipBounds();
				g.translate(bounds.x, bounds.y);
				Rectangle clip = component.clipRect;
				g.setClip(clip.x, clip.y, clip.width, clip.height);
				component.paint(g);
				g.translate(-bounds.x, -bounds.y);
				g.setClip(prevClip.x, prevClip.y, prevClip.width,prevClip.height);
			}
			if ((tooltipowner != null)) {
				Rectangle r = getRectangle(tooltipowner, ":tooltipbounds");
				paintRect(r.x, r.y, r.width, r.height,
					c_border, c_tooltip_bg, true, true, true, true, true);
				String text = getString(tooltipowner, "tooltip", null);
				g.setColor(c_tooltip_fg);
				g.drawString(text, r.x + 2, r.y + font.getAscent() + 2); //+nullpointerexception
			}			
			if(popupowner!=null) {
				Object popup = get(popupowner, ":popup");
				if(popup!=null) {
					//paint(popup);
					paint(0, 0, width, height, popup, true);
				}
				popup = get(popupowner, ":combolist");
				if(popup!=null) {
					//paint(popup);
					paint(0, 0, width, height, popup, true);
				}
			}
		}
	}
	
	/**
	 * renders gui
	 * @param gl
	 */
	public boolean render() {
			
		
		if(repaintNeeded||true){
			repaintNeeded=false;
			g.setFont(font);
			g.begin(width,height);
			paint();
			g.end();
			//return true;
		}
		/*
		//Now update gui widget
		if(useFBO && contextCreated){
			if(repaintNeeded){
				repaintNeeded=false;
				gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboMsaaId);
				gl.glBlendFuncSeparate(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA,GL2.GL_ONE,GL2.GL_ONE_MINUS_SRC_ALPHA);  
				
				// clear buffers
				// make sure we clear the framebuffer's content
		        gl.glClearColor(0,0,0,0);
				gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
				g.setFont(font);
				g.begin(gl,width,height);
				paint();
				g.end(gl);
				gl.glFlush();
				
				gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
				
				//return true;
			}
			gl.glDisable(GL.GL_STENCIL_TEST);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glOrthof(0, width, height,0, -1, 1);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glPushMatrix();
			// initialize the matrix
			gl.glLoadIdentity();
			//gl.glTranslatef(-width/ 2.0f+1, height / 2.0f-1, 0);
			//gl.glScalef(1, -1, 1);
		//	gl.glEnable(GL2.GL_BLEND );
			
		//	gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				gl.glActiveTexture(GL.GL_TEXTURE0);
				gl.glEnable(GL2.GL_TEXTURE_2D);  
				gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
				gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA); 
					 
				float w=TEXTURE_WIDTH,h=TEXTURE_HEIGHT;
				w=width; h=height;
				float sw=w/TEXTURE_WIDTH;
				float sh=h/TEXTURE_HEIGHT;
			    //Draw Back Buffer Texture
			    gl.glColor4f(1,1,1, 1);	 	
			    gl.glBegin(GL2.GL_QUADS);
			    gl.glTexCoord2d(0,0);
			    gl.glVertex2d(0,h);
			   
			    gl.glTexCoord2d(sw,0);
			    gl.glVertex2d(w, h);
			
			    gl.glTexCoord2d(sw,sh);
			    gl.glVertex2d(w, 0);
			
			    gl.glTexCoord2d(0,sh);
			    gl.glVertex2d(0,0);
			
			    gl.glEnd();
				gl.glMatrixMode(GL2.GL_PROJECTION);
				gl.glPopMatrix();
				
				gl.glMatrixMode(GL2.GL_MODELVIEW);
				gl.glPopMatrix();
				gl.glDisable(GL2.GL_TEXTURE_2D); 
				gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA); 
			
				//For offscreen rendering we need to render widgets which are updated regularly on default buffer
				g.setFont(font);
				g.begin(gl,width,height);
				for (CustomComponent bean : beans) {
					renderComp(bean);
				}
				g.end(gl);
				//return false;
		}else{
			g.setFont(font);
			g.begin(gl,width,height);
			paint();
			g.end(gl);
			
		}
		*/
		return true;
	}
	
	
	

	   /**
     * Polls input by calling {@link Input#pollInput(org.shikhar.simphy.agui.Input) }
     * if an input source was specified, otherwise it does nothing.
     * 
     * <p>If you don't want to use polled input you can easily use a push model
     * for handling input. Just call the following methods:</p><ul>
     * <li>{@link #handleKey(int, int,char, int) } for every keyboard event
     * <li>{@link #handleMouse(int, int, int, int,int) } for every mouse event (buttons or move)
     * <li>{@link #handleMouseWheel(int,int,int) } for any mouse wheel event
     * </ul> These methods (including this one) needs to be called after {@link #updateTime() }
	 * @return 
     */
    public boolean handleInput() {
    	boolean handled=false;
    	//handled=input.pollInput(this);
    	if(handled)repaint(g);
        if(popupowner!=null || insidepart=="modal" || (mousepressed!=null && !getComponentClass(mousepressed).equals("desktop"))){
        	 if(insidepart=="modal") {/// && (input.mouse.wasClicked(AWTMouseEvent.BUTTON1)||input.mouse.wasClicked(AWTMouseEvent.BUTTON2)||input.mouse.wasClicked(AWTMouseEvent.BUTTON3))){
        		 //if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(mousepressed);
        		// Toolkit.getDefaultToolkit().beep();
        		 flicker=false;
        		 setTimer(90,7);
        		 mousepressed=null;
        	 }
        	 
        	return true;
        }
        if(focusowner!=null && focusowner!=content){
        	
        	return true;
        }
        
        if ("dialog" == getComponentClass(focusowner) && getBoolean(focusowner, "modal")){
        	
        	return true;
        }
        if(handled)repaint(g);
        return handled;
    }

	
	GuiTimer activeTimer;

	  long curTime;
	    private int deltaTime;
	  /**
	     * Returns the current UI time in milliseconds.
	     * This time is updated via {@link #updateTime() }
	     *
	     * @return the current UI time in milliseconds.
	     */
	    public long getCurrentTime() {
	        return curTime;
	    }

	    /**
	     * Returns the delta time to the previous frame in milliseconds.
	     * This time is updated via {@link #updateTime() }
	     * 
	     * @return the delta time
	     */
	    public int getCurrentDeltaTime() {
	        return deltaTime;
	    }
	    
	    /**
	     * Polls inputs, updates layout and renders the GUI by calls the following method:<ol>
	     * <li> {@link #setSize() }
	     * <li> {@link #updateTime() }
	     * <li> {@link #handleInput() }
	     * <li> {@link #handleKeyRepeat() }
	     * <li> {@link #handleTooltips() }
	     * <li> {@link #updateTimers() }
	     * <li> {@link #invokeRunables() }
	     * <li> {@link #validateLayout() }
	     * <li> {@link #draw() }
	     * <li> {@link #setCursor() }
	     * </ol>
	     * 
	     * This is the easiest method to use this GUI.
	     * 
	     * <p>When not using this method care must be taken to invoke the methods
	     * in the right order. See the javadoc of the individual methods for details.</p>
	     * @param 
	     * @param widget needs to revalidate itself  as something (global variables etc) has changed in this step which may affect widget state
	     */
	    public void updateUI(int dt_ms,boolean revalidate) {
	       // setSize();
	       // updateTime(dt_ms);
	        //handleInput();
	        //handleKeyRepeat();
	        //handleTooltips();
	    	deltaTime=dt_ms;
	        updateTimers(dt_ms);
	       // invokeRunables();
	        //validateLayout();
	       // draw();
	        updateWidgets(content,revalidate);
	        if(flingeVel.getMagnitudeSquared()>1 && mouseinside!=null) {
	    		boolean handled=false;
	    		Rectangle port = getRectangle(mouseinside, ":port");
	    		if (port != null) { // is scrollable
	    				Rectangle view = getRectangle(mouseinside, ":view");
	    				int dx = 0;
	    				int dy = 0;
	    				dx=(int) flingeVel.x;
	    				dy=(int) flingeVel.y;
	    				if (dx != 0) {
	    					dx = (dx < 0) ? Math.max(-view.x, dx) : Math.min(dx, view.width
	    							- port.width - view.x);
	    				} 
	    				if (dy != 0) {
	    					dy = (dy < 0) ? Math.max(-view.y, dy) : Math.min(dy, view.height
	    							- port.height - view.y);
	    				} 
	    				view.x+=dx;
	    				view.y+=dy;
	    		}
	    		flingeVel.multiply(damping);
	    		repaintNeeded=true;
	    	}else {
	    		//activeTimer.stop();
	    	}
	    }

	    /**
	     * update widgets for this time step
	     * 
	     * @param component
	     * @param revalidate widget needs to revalidate itself  as something (global variables etc) have changed in this step which may affect widget state
	     */
	    public void  updateWidgets(Object component,boolean revalidate){
	    	int n=getCount(component);
	    	for(int i=0;i<n;i++){
	    		Object o=getItem(component,i);
	    		if(o==null)continue;
	    		if(getComponentClass(o).equals("bean")){
	    			((CustomComponent)(getComponent(o,"bean"))).update(deltaTime,revalidate);
	    			//repaint(o);
	    		}else if(getComponentClass(o).equals("slider")){
	    			GuiTimer timer=(GuiTimer) getProperty(o,"timer");
	    			if(timer!=null)timer.update(deltaTime);
	    		}else{
	    			updateWidgets(o,revalidate);
	    		}
	    	}

	    }
	    


	  
	 /**
   * Updates all active timers with the delta time computed by {@code updateTime}.
   * 
   * <p>This method must be called exactly once after a call to {@code updateTime}.</p>
   * 
   * @see #updateTime() 
   */
  public void updateTimers(int dt_ms) {
  	/*
      for(int i=0 ; i<activeTimers.size() ;) {
          if(!activeTimers.get(i).tick(deltaTime)) {
              activeTimers.remove(i);
          } else {
              i++;
          }
      }
      */
  	 activeTimer.update(dt_ms);
  }
	

public void onTimerTick(GuiTimer timer){
	  
		if (timer == activeTimer) {
			if (activeTimer.getDelay() == 300 || activeTimer.getDelay() == 60) {
				if (processScroll(mousepressed, pressedpart)) {
					activeTimer.setDelay(60);
					activeTimer.setContinuous(true);
					activeTimer.reStart();
				} else {
					activeTimer.stop();
				}
			} else if (activeTimer.getDelay() == 375
					|| activeTimer.getDelay() == 75) {
				if (processSpin(mousepressed, pressedpart)) {
					activeTimer.setDelay(75);
					activeTimer.setContinuous(true);
					activeTimer.reStart();
				} else {
					activeTimer.stop();
				}
			} else if (activeTimer.getDelay() == 750) {
				activeTimer.stop();
				showTip();
			} else if (activeTimer.delay == 90) {
				flicker = !flicker;
				if (activeTimer.num_ticks >= 6) {
					activeTimer.stop();
					flicker = false;
				}
			}
		}else{
			if((timer.invokerCompoenent!=null ||getComponentClass(timer.invokerCompoenent)=="slider")  ){
				double minimum = getDouble(timer.invokerCompoenent, "minimum");
				double maximum = getDouble(timer.invokerCompoenent, "maximum");
				double oldValue = getDouble(timer.invokerCompoenent, "value");
				double unit = getDouble(timer.invokerCompoenent, "unit");
				
				double value=oldValue;
				String animMode=getString(timer.invokerCompoenent,"animmode","none");
				if(animMode=="increasing"){
					value+=unit;
					if(value>maximum)value=minimum;
				}else if(animMode=="decreasing"){
					value-=unit;
					if(value<minimum)value=maximum;
				}else if(animMode=="increasing-once"){
					value+=unit;
					if(value>maximum){
						value=maximum;
						return;
					}
				}else if(animMode=="decreasing-once"){
					value-=unit;
					if(value<minimum){
						value=minimum;
						return;
					}
				}else if(animMode=="oscillating"){
					boolean incr=getProperty(timer.invokerCompoenent,"reverse")==null;
					value+=(incr?unit:-unit);
					if(value>=maximum && incr){
						value=maximum;
						putProperty(timer.invokerCompoenent,"reverse","true");
					}else if(value<=minimum && !incr){
						value=minimum;
						putProperty(timer.invokerCompoenent,"reverse",null);
					}

				}else{
					//timer=null;
				}

				value=minimum+Math.round(((value-minimum)/unit))*unit;
				value=MathUtils.clamp(value, minimum, maximum);
				if(value!=oldValue){
					setDouble(timer.invokerCompoenent,"value",value);
					invoke(timer.invokerCompoenent,null,"action");
				}
			
			}
			
		}
   }
  
  boolean flicker=false;
  /**
   * Starts/Stops  timer
   * @param delay interval in millisec between two ticks , timer stops id delay is zero 
   * @param tickcount number of times timer ticks -1=infinite, 0=stops timer, 1= runs only once
	 *
	 */
	private void setTimer(int delay, long tickcount) {
		flicker=false;
		if(delay==0 ||tickcount==0){
			activeTimer.setDelay(0);
			activeTimer.stop();
			return;
		}
		activeTimer.setDelay(delay);
		if(tickcount<0){
			activeTimer.setContinuous(true);
		}else if(tickcount==1){
			activeTimer.setContinuous(false);
		}else{
			activeTimer.setMaxTickCount(tickcount);
		}
		activeTimer.start();
	}

  
  

	


	private Color getColor(Object component, String key, Color defaultcolor) {
		Object value = get(component, key);
		return (value != null) ? (Color) value : defaultcolor;
	}
	
	// setDesktopProperty+

	/**
	 * Sets the only one font used everywhere, and revalidates the whole UI.
	 * Scrollbar width/height, spinbox, and combobox button width, and slider
	 * size is the same as the font height
	 *
	 * @param font
	 *            the default font is <i>SansSerif</i>, <i>plain</i>, and
	 *            <i>12pt</i>
	 */
	public void setDefaultFont(Font font) {
		if(font==null)return;//font=new Font("default-normal");
		font.setContext(g.context);
		this.font = font;
		this.g.setFont(font);
		block = font.getHeight();
		console.log("Gui Block="+block);
		/*
		try{
			block = font.getHeight();
		}catch(Exception e){
			block=12;
		}
		*/
		margin_1=1;//(int) Math.max(1,(g.PIXEL_SCALE_FACTOR));
		//if (content != null)
			//validate(content);
		reValidateGui();
	}

	/**returns current font*/
	public Font getFont() {
		return this.font;
	}
		
	/**
	 *
	 */
	private void doLayout(Object component) {
		//if(true)return;
		//System.out.println("{"+c_bg+","+c_text_fg+","+c_text_bg+","+c_border+","+c_disable+","+c_hover+","+c_press+","+c_focus+","+c_select_bg+","+c_icon_tint+"}");
		
		String classname = getComponentClass(component);
	//	if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(new Serializer(this).serialize(content,0,'h',""));
		if ("combobox" == classname) {
			if (getBoolean(component, "editable", true)) {
				AWTImage icon = getIcon(component, "icon", null);
				layoutField(component, block, false,
						(icon != null) ? icon.getScaledWidth() : 0);
			} // set editable -> validate (overwrite textfield repaint)
			else {
				int selected = getInteger(component, "selected", -1);
				if (selected != -1) { // ...
					Object choice = getItem(component, selected);
					if(getComponentClass(choice)=="choice"){
						set(component, "text", get(choice, "text"));
						set(component, "icon", get(choice, "icon"));
					}
				}
			}
		} else if (("textfield" == classname) || ("passwordfield" == classname)) {
			layoutField(component, 0, ("passwordfield" == classname), 0);
		} else if ("textarea" == classname) {
			String text = getString(component, "text", "");
			if(text==null)text="";
			int start = getInteger(component, "start", 0);
			if (start > text.length()) {
				setInteger(component, "start", start = text.length(), 0);
			}
			int end = getInteger(component, "end", 0);
			if (end > text.length()) {
				setInteger(component, "end", end = text.length(), 0);
			}

			boolean wrap = getBoolean(component, "wrap", false);
			char[] chars = null;
			defaultStyle=new DrawStyle(0,0,null,null,'n');
			defaultStyle.font=getFont(component);
			DrawStyle[] styles=(DrawStyle[]) getProperty(component,"drawstyle");
			
			if (wrap) {
				Rectangle bounds = getRectangle(component, "bounds");
				chars = getChars(component, text, true, bounds.width - 4*margin_1,
						bounds.height);
				if (chars == null) { // need scrollbars
					chars = getChars(component, text, true, bounds.width
							- block - 4*margin_1, 0);
				}
			} else {
				chars = getChars(component, text, false, 0, 0);
			}

			Font currentfont = getFont(component);
			currentfont.update();
			int width = 0, height = 0;
			int caretx = 0;
			int carety = 0;
			
			for (int i = 0, j = 0; j <= chars.length; j++) {
				if ((j == chars.length) || (chars[j] == '\n')) {
					width = Math.max(width, styles==null?currentfont.charsWidth(chars, i, j - i):getCharsWidth(chars, i, j - i,styles));
					if ((end >= i) && (end <= j)) {
						caretx = (styles==null?currentfont.charsWidth(chars, i, end - i):getCharsWidth(chars, i, j - i,styles));
						carety = height;
					}
					height += (styles==null?currentfont.getHeight():getCharsHeight(chars, i, j - i,styles));
					i = j + 1;
				}
			}
			layoutScroll(component, width+ 2*margin_1 , height - currentfont.getLeading() + 2*margin_1, 0,
					0, 0, 0, getBoolean(component, "border", true), 0);
			scrollToVisible(component, caretx, carety, 2*margin_1,
					currentfont.getAscent() + currentfont.getDescent() + 2*margin_1); // ?
		} else if ("tabbedpane" == classname) {
			// tabbedpane (not selected) tab padding are 1, 3, 1, and 3 pt
			Rectangle bounds = getRectangle(component, "bounds");
			String placement = getString(component, "placement", "top");
			boolean horizontal = ((placement == "top") || (placement == "bottom"));
			boolean stacked = (placement == "stacked");
			boolean hideTabs=(placement == "none");
			
			// draw up tabs in row/column
			int tabd = 0;
			Rectangle first = null; // x/y location of tab left/top
			int tabsize = 0; // max height/width of tabs
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				if ((tabd == 0)
						&& ((first = getRectangle(tab, "bounds")) != null)) {
					tabd = horizontal ? first.x : first.y; // restore previous
															// offset
				}
				Dimension d = getSize(tab, stacked ? 8*margin_1 : horizontal ? 12*margin_1 : 9*margin_1,
						stacked ? 3*margin_1 : horizontal ? 5*margin_1 : 8*margin_1,false);
				setRectangle(tab, "bounds", horizontal ? tabd : 0,
						horizontal ? 0 : tabd,
						stacked ? bounds.width : d.width, d.height);
				if (stacked) {
					tabd += d.height;
				} else {
					tabd += (horizontal ? d.width : d.height) - 3*margin_1;
					tabsize = Math
							.max(tabsize, horizontal ? d.height : d.width);
				}
			}

			if(hideTabs)tabsize=0;
			// match tab height/width, set tab content size
			int cx = (placement == "left") ? (tabsize + 1*margin_1) : 2*margin_1;
			int cy = (placement == "top") ? (tabsize + 1*margin_1) : 2*margin_1;
			int cwidth = bounds.width
					- ((horizontal || stacked) ? 4*margin_1 : (tabsize + 3*margin_1));
			int cheight = bounds.height
					- (stacked ? (tabd + 3*margin_1) : (horizontal ? (tabsize + 3*margin_1) : 4*margin_1));
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				Rectangle r = getRectangle(tab, "bounds");
				if (!stacked) {
					if (horizontal) {
						if (placement == "bottom") {
							r.y = bounds.height - tabsize;
						}
						r.height = tabsize;
					} else {
						if (placement == "right") {
							r.x = bounds.width - tabsize;
						}
						r.width = tabsize;
					}
				}

				Object comp = get(tab, ":comp"); // relative to the tab location
				if ((comp != null) && getBoolean(comp, "visible", true)) {
					setRectangle(comp, "bounds", cx - r.x,
							stacked ? (r.height + 1*margin_1) : (cy - r.y), cwidth,
							cheight);
					doLayout(comp);
				}
			}
			checkOffset(component);
		} else if (("panel" == classname) || (classname == "dialog")) {
			if(true){
				//return;
			}
			int gap = getInteger(component, "gap", 0);
	       //new Serializer(this).serialize(content, 0, ' ', ""));
		
			int[][] grid = getGrid(component);
			int top = 0;
			int left = 0;
			int contentwidth = 0;
			int contentheight = 0;
			if (grid != null) { // has subcomponents
				top = getInteger(component, "top", 0);
				left = getInteger(component, "left", 0);
				int bottom = getInteger(component, "bottom", 0);
				int right = getInteger(component, "right", 0);
				// sums the preferred size of cell widths and heights, gaps
				contentwidth= left
						+ getSum(grid[0], 0, grid[0].length, gap, false)
						+ right;
				contentheight= top
						+ getSum(grid[1], 0, grid[1].length, gap, false)
						+ bottom;
			}
			AWTImage icon = getIcon(component, "bgimage", null);
			if(icon!=null && ("panel" == classname)){
				contentwidth=Math.max(icon.getScaledWidth(),contentwidth);
				contentheight=Math.max(icon.getScaledHeight(),contentheight);
			}

			int titleheight = getSize(component, 0, 0,false).height; // title text and
																// icon
			setInteger(component, ":titleheight", titleheight, 0);
			boolean scrollable = getBoolean(component, "scrollable", false);
			boolean border = ("panel" == classname)
					&& getBoolean(component, "border", false);
			int iborder = (border ? margin_1 : 0);
			if (scrollable) { // set scrollpane areas
				if ("panel" == classname) {
					int head = titleheight / 2;
					int headgap = (titleheight > 0) ? (titleheight - head - iborder)
							: 0;
					scrollable = layoutScroll(component, contentwidth,
							contentheight, head, 0, 0, 0, border, headgap);
				} else { // dialog
					scrollable = layoutScroll(component, contentwidth,
							contentheight, 3*margin_1 + titleheight, 3*margin_1, 3*margin_1, 3*margin_1, true, 0);
				}
			}
			if (!scrollable) { // clear scrollpane bounds //+
				set(component, ":view", null);
				set(component, ":port", null);
			}

			if (grid != null) {
				int areax = 0;
				int areay = 0;
				int areawidth = 0;
				int areaheight = 0;
				if (scrollable) {
					// components are relative to the viewport
					Rectangle view = getRectangle(component, ":view");
					areawidth = view.width;
					areaheight = view.height;
				} else { // scrollpane isn't required
							// components are relative to top/left corner
					Rectangle bounds = getRectangle(component, "bounds");
					areawidth = bounds.width;
					areaheight = bounds.height;
					if ("panel" == classname) {
						areax = iborder;
						areay = Math.max(iborder, titleheight);
						areawidth -= 2 * iborder;
						areaheight -= areay + iborder;
					} else { // dialog
						areax = 4*margin_1;
						areay = 4*margin_1 + titleheight;
						areawidth -= 8*margin_1;
						areaheight -= areay + 4*margin_1;
					}
				}

				for (int i = 0; i < 2; i++) { // i=0: horizontal, i=1: vertical
					// remaining space
					int d = ((i == 0) ? (areawidth - contentwidth)
							: (areaheight - contentheight));
					if (d != 0) { // + > 0
						int w = getSum(grid[2 + i], 0, grid[2 + i].length, 0,
								false);
						if (w > 0) {
							for (int j = 0; j < grid[i].length; j++) {
								if (grid[2 + i][j] != 0) {
									grid[i][j] += d * grid[2 + i][j] / w;
								}
							}
						}
					}
				}

				Object comp = get(component, ":comp");
				for (int i = 0; comp != null; comp = get(comp, ":next")) {
					if (!getBoolean(comp, "visible", true)) {
						continue;
					}
					int ix = areax + left
							+ getSum(grid[0], 0, grid[4][i], gap, true);
					int iy = areay + top
							+ getSum(grid[1], 0, grid[5][i], gap, true);
					int iwidth = getSum(grid[0], grid[4][i], grid[6][i], gap,
							false);
					int iheight = getSum(grid[1], grid[5][i], grid[7][i], gap,
							false);
					String halign = getString(comp, "halign", "fill");
					String valign = getString(comp, "valign", "fill");
					if ((halign != "fill") || (valign != "fill")) {
						Dimension d = getPreferredSize(comp);
						if (halign != "fill") {
							int dw = Math.max(0, iwidth - d.width);
							if (halign == "center") {
								ix += dw / 2;
							} else if (halign == "right") {
								ix += dw;
							}
							iwidth -= dw;
						}
						if (valign != "fill") {
							int dh = Math.max(0, iheight - d.height);
							if (valign == "center") {
								iy += dh / 2;
							} else if (valign == "bottom") {
								iy += dh;
							}
							iheight -= dh;
						}
					}
					setRectangle(comp, "bounds", ix, iy, iwidth, iheight);
					doLayout(comp);
					i++;
					//if(getClass(comp)=="dialog"){
					//	int j=0;
					//}
					//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(toString(comp));
				}
			}
			
		} else if ("desktop" == classname) {
			Rectangle bounds = getRectangle(component, "bounds");
			if(bounds!=null && bounds.width<0){
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("");
			}
			for (Object comp = get(component, ":comp"); comp != null; comp = get(
					comp, ":next")) {
				String iclass = getComponentClass(comp);
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("dsktp layout "+toString(comp));
				//if(true)return;
				int boundsWidth=Math.abs(bounds.width);
				if (iclass == "dialog" ) {
					Dimension d = getPreferredSize(comp);
					
					if (get(comp, "bounds") == null){
						if(getProperty(comp,"pintoworld")!=null){
							setRectangle(comp, "bounds",
									Math.max(0, (boundsWidth - d.width) / 2),
									Math.max(0, (bounds.height - d.height) / 2),
									Math.min(d.width, boundsWidth),
									Math.min(d.height, bounds.height));
							repaintNeeded=true;
						}else{
							setRectangle(comp, "bounds",
									bounds.x,
									bounds.y,
									Math.min(d.width, boundsWidth),
									Math.min(d.height, bounds.height));
						}
					}
						
				} else if (iclass=="menubar") {
					Dimension d = getPreferredSize(comp);
					String placement = getString(comp, "placement", "top");
					if(placement=="top"){
						setRectangle(comp, "bounds", 0, 0, bounds.width,
								d.height);
					}else{
						setRectangle(comp, "bounds", 0, bounds.height-d.height, bounds.width,
								d.height);
					}
					//dd Dimension d = getPreferredSize(comp);
					//mwnubar placemtne
					//if(bounds.y>)
					
				}else if((iclass != ":combolist") && (iclass != ":popup")){
					Dimension d = getPreferredSize(comp);
					/*
					if (get(comp, "bounds") == null){
						setRectangle(comp, "bounds",
								Math.max(0, (boundsWidth - d.width) / 2),
								Math.max(0, (bounds.height - d.height) / 2),
								Math.min(d.width, boundsWidth),
								Math.min(d.height, bounds.height));
					}else{
						Rectangle R=getRectangle(comp,"bounds");
						setRectangle(comp, "bounds",
								Math.min(Math.max(R.x,0), (boundsWidth - d.width) ),
								Math.min(Math.max(R.y,0), (bounds.height - d.height) ),
								Math.min(d.width, boundsWidth),
								Math.min(d.height, bounds.height));	
					}
					*/
					if (get(comp, "bounds") == null){
						setRectangle(comp, "bounds",
								Math.max(0, (boundsWidth - d.width) / 2),
								Math.max(0, (bounds.height - d.height) / 2),
								Math.min(d.width, boundsWidth),
								Math.min(d.height, bounds.height));
					}else{
						Rectangle R=getRectangle(comp,"bounds");
						setRectangle(comp, "bounds",
								R.x,//Math.min(Math.max(R.x,0), (boundsWidth - d.width) ),
								R.y,//Math.min(Math.max(R.y,0), (bounds.height - d.height) ),
								Math.min(d.width, boundsWidth),
								Math.min(d.height, bounds.height));	
					}
				}
				
				validate(comp);
				doLayout(comp);
			}
		} else if ("spinbox" == classname) {
			double minimum = getDouble(component, "minimum");
			double maximum = getDouble(component, "maximum");
			double value = getDouble(component, "value");
			double step= getDouble(component, "step");
			if(value<minimum ||value>maximum){
				value=MathUtils.clamp(value, minimum, maximum);
				value=Math.floor((value-minimum)/step)*step+minimum;
				setDouble(component,"value",value);
			}
		
			//set(component,"text",value+"");
			layoutField(component, block, false, 0);
		} else if ("splitpane" == classname) {
			Rectangle bounds = getRectangle(component, "bounds");
			boolean horizontal = ("vertical" != get(component, "orientation"));
			int divider = getInteger(component, "divider", -1);
			int maxdiv = Math.max(0,
					(horizontal ? Math.abs(bounds.width) : Math.abs(bounds.height)) - 5*margin_1);

			Object comp1 = get(component, ":comp");
			boolean visible1 = (comp1 != null)
					&& getBoolean(comp1, "visible", true);
			if (divider == -1) {
				int d1 = 0;
				if (visible1) {
					Dimension d = getPreferredSize(comp1);
					d1 = horizontal ? d.width : d.height;
				}
				divider = Math.min(d1, maxdiv);
				setInteger(component, "divider", divider, -1);
			} else if (divider > maxdiv) {
				//setInteger(component, "divider", divider = maxdiv, -1); //@TODO commented so that it may be restored after saved
			}

			if (visible1) {
				setRectangle(comp1, "bounds", 0, 0, horizontal ? divider
						: bounds.width, horizontal ? bounds.height : divider);
				doLayout(comp1);
			}
			Object comp2 = (comp1 != null) ? get(comp1, ":next") : null;
			if ((comp2 != null) && getBoolean(comp2, "visible", true)) {
				setRectangle(comp2, "bounds", horizontal ? (divider + 5*margin_1) : 0,
						horizontal ? 0 : (divider + 5*margin_1),
						horizontal ? (bounds.width - 5*margin_1 - divider)
								: bounds.width, horizontal ? bounds.height
								: (bounds.height - 5*margin_1 - divider));
				doLayout(comp2);
			}
			

		} else if (("list" == classname) || ("table" == classname)
				|| ("tree" == classname)) {
			int line = getBoolean(component, "line", true) ? 1*margin_1 : 0;
			int width = 0;
			int columnheight = 0;
			if ("table" == classname) {
				Object header = get(component, "header");
				int[] columnwidths = null;
				if (header != null) {
					columnwidths = new int[getCount(header)];
					Object column = get(header, ":comp");
					for (int i = 0; i < columnwidths.length; i++) {
						if (i != 0) {
							column = get(column, ":next");
						}
						columnwidths[i] = getInteger(column, "width",
								DEFAULT_COLUMN_WIDTH);
						width += columnwidths[i];
						Dimension d = getSize(column, 2*margin_1, 2*margin_1,false);
						columnheight = Math.max(columnheight, d.height);
					}
				}
				set(component, ":widths", columnwidths);
				if (header != null && getBoolean(header, "resizable")) {
					int[] smartWidths = new int[columnwidths.length];
					for (int i = 0; i < smartWidths.length; i++)
						smartWidths[i] = MINIMUM_COLUMN_WIDTH;
					set(component, PROPERTY_SMARTWIDTHS, smartWidths);
				}
			}
			int y = 0;
			int level = 0;
			for (Object item = get(component, ":comp"); item != null;) {
				int x = 0;
				int iwidth = 0;
				int iheight = 0;
				if ("table" == classname) {
					iwidth = width;
					for (Object cell = get(item, ":comp"); cell != null; cell = get(
							cell, ":next")) {
						Dimension d = getSize(cell, 2*margin_1, 2*margin_1,false);
						iheight = Math.max(iheight, d.height);
					}
				} else {
					if ("tree" == classname) {
						x = (level + 1) * block;
					}
					Dimension d = getSize(item, 6*margin_1, 2*margin_1,false);
					iwidth = d.width;
					iheight = d.height;
					width = Math.max(width, x + d.width);
				}
				setRectangle(item, "bounds", x, y, iwidth, iheight);
				y += iheight + line;
				if ("tree" == classname) {
					Object next = get(item, ":comp");
					if ((next != null) && getBoolean(item, "expanded", true)) {
						level++;
					} else {
						while (((next = get(item, ":next")) == null)
								&& (level > 0)) {
							item = getParent(item);
							level--;
						}
					}
					item = next;
				} else {
					item = get(item, ":next");
				}
			}
			layoutScroll(component, width, y - line, columnheight, 0, 0, 0,
					true, 0);
		} else if ("menubar" == classname) {
			Rectangle bounds = getRectangle(component, "bounds");
			int x = 0;
			for (Object menu = get(component, ":comp"); menu != null; menu = get(
					menu, ":next")) {
				Dimension d = getSize(menu, 8*margin_1, 4*margin_1,false);
				setRectangle(menu, "bounds", x, 0, d.width, bounds.height);
				x += d.width;
			}
		} else if ("bean" == classname) {
			Rectangle r = getRectangle(component, "bounds");
			//System.out.println(r+"");
			((CustomComponent) get(component, "bean")).setBounds(r);
		}
	}

	/**
	 * Scroll tabs to make the selected one visible
	 * 
	 * @param component
	 *            a tabbedpane
	 */
	private void checkOffset(Object component) {
		String placement = getString(component, "placement", "top");
		int selected = getInteger(component, "selected", 0);
		int i = 0;
		if (placement == "stacked") {
			int dy = 0;
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				Rectangle r = getRectangle(tab, "bounds");
				r.y = dy;
				dy += r.height;
				if (i == selected) {
					dy += getRectangle(get(tab, ":comp"), "bounds").height + 2;
				}
				i++;
			}
			if (mouseinside == component) { // layout changed, check the hovered
											// tab
				checkLocation();
			}
			return;
		}
		boolean horizontal = ((placement == "top") || (placement == "bottom"));
		Rectangle bounds = getRectangle(component, "bounds");
		int panesize = horizontal ? bounds.width : bounds.height;
		int first = 0;
		int last = 0;
		int d = 0;
		for (Object tab = get(component, ":comp"); tab != null; tab = get(tab,
				":next")) {
			Rectangle r = getRectangle(tab, "bounds");
			if (i == 0) {
				first = (horizontal ? r.x : r.y);
			}
			last = (horizontal ? (r.x + r.width) : (r.y + r.height));
			if (i == selected) {
				int ifrom = (horizontal ? r.x : r.y) - 6;
				int ito = (horizontal ? (r.x + r.width) : (r.y + r.height)) + 6;
				if (ifrom < 0) {
					d = -ifrom;
				} else if (ito > panesize) {
					d = panesize - ito;
				}
			}
			i++;
		}
		d = Math.min(-first, Math.max(d, panesize - last));
		if (d != 0) {
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				Rectangle r = getRectangle(tab, "bounds");
				if (horizontal) {
					r.x += d;
				} else {
					r.y += d;
				}
				Object comp = get(tab, ":comp"); // relative to the tab location
				if ((comp != null) && getBoolean(comp, "visible", true)) {
					Rectangle rc = getRectangle(comp, "bounds");
					if (horizontal) {
						rc.x -= d;
					} else {
						rc.y -= d;
					}
				}
			}
			if (mouseinside == component) { // layout changed, check the hovered
											// tab
				checkLocation();
			}
		}
	}

	/**
	 * returns array of characters with line breaks as per width and height of component if wrapsi set true
	 * @param component
	 * @param text
	 * @param wrap
	 * @param width ignores break if set to zero
	 * @param height 
	 * @return
	 */
	private char[] getChars(Object component, String text, boolean wrap,
			int width, int height) {
		char[] chars = (char[]) get(component, ":text");
		if ((chars == null) || (chars.length != text.length())) {
			chars = text.toCharArray();
			set(component, ":text", chars);
		} else
			text.getChars(0, chars.length, chars, 0);

		if (wrap) {
			DrawStyle[] styles=(DrawStyle[]) getProperty(component,"drawstyle");
			
			Font currentfont = getFont(component);
			int lines = (height - 4 + currentfont.getLeading()) / currentfont.getHeight();
			boolean prevletter = false;
			int n = chars.length;
			int linecount = 0;
			//if(styles!=null)currentfont=getDrawStyle(0,styles).font;
			for (int i = 0, j = -1, k = 0; k <= n; k++) { // j is the last space
															// index (before k)
				//currentfont=getDrawStyle(i,styles).font;
				if (((k == n) || (chars[k] == '\n') || (chars[k] == ' '))
						&& (j > i) && ((styles==null?currentfont.charsWidth(chars, i, k-i):getCharsWidth(chars, i, k - i,styles)) > width)) {
					chars[j] = '\n';
					k--; // draw line to the begin of the current word (+
							// spaces) if it is out of width
					
				} else if ((k == n) || (chars[k] == '\n')) { // draw line to the
																// text/line end
					j = k;
					prevletter = false;
				} else {
					if ((chars[k] == ' ') && (prevletter || (j > i))) {
						j = k;
					} // keep spaces starting the line
					prevletter = (chars[k] != ' ');
					continue;
				}
				linecount++;
				if ((lines != 0) && (linecount == lines)) {
					return null;
				}
				i = j + 1;
			}
		}
		return chars;
	}

	/**
	 *
	 */
	/*
	 * private boolean wrap(char[] chars, int width, int lines) { FontMetrics fm
	 * = getFontMetrics(font); boolean prevletter = false; int n = chars.length;
	 * int linecount = 0; for (int i = 0, j = -1, k = 0; k <= n; k++) { // j is
	 * the last space index (before k) if (((k == n) || (chars[k] == '\n') ||
	 * (chars[k] == ' ')) && (j > i) && (fm.charsWidth(chars, i, k - i) >
	 * width)) { chars[j] = '\t'; k--; // draw line to the begin of the current
	 * word (+ spaces) if it is out of width } else if ((k == n) || (chars[k] ==
	 * '\n')) { // draw line to the text/line end j = k; prevletter = false; }
	 * else { if (chars[k] == '\t') { chars[k] = ' '; } if ((chars[k] == ' ') &&
	 * (prevletter || (j > i))) { j = k; } // keep spaces starting the line
	 * prevletter = (chars[k] != ' '); continue; } linecount++; if ((lines != 0)
	 * && (linecount == lines)) { return false; } i = j + 1; } return true; }
	 */

	/**
	 * @param component
	 *            a menuitem
	 * @return key modifier strings and key text
	 */
	private String getAccelerator(Object component) {
		Object accelerator = get(component, "accelerator");
		if (accelerator != null) {
			long keystroke = ((Long) accelerator).longValue();
			int keycode = (int) (keystroke >> 32);
			int modifiers = (int) (keystroke & 0xffff);
			return AWTKeyEvent.getKeyModifiersText(keycode) + " "
					+ AWTKeyEvent.getKeyText(modifiers);
		}
		return null;
	}

	/**
	 * Pop up the list of choices for the given combobox
	 * 
	 * @param combobox
	 * @return the created combolist
	 */
	private Object popupCombo(Object combobox) {
		this.closeup();
		// combobox bounds relative to the root desktop
		int combox = 0, comboy = 0, combowidth = 0, comboheight = 0;
		for (Object comp = combobox; comp != content; comp = getParent(comp)) {
			Rectangle r = getRectangle(comp, "bounds");
			combox += r.x;
			comboy += r.y;
			Rectangle view = getRectangle(comp, ":view");
			if (view != null) {
				combox -= view.x;
				comboy -= view.y;
				Rectangle port = getRectangle(comp, ":port");
				combox += port.x;
				comboy += port.y;
			}
			if (comp == combobox) {
				combowidth = r.width;
				comboheight = r.height;
			}
		}
		// :combolist -> combobox and combobox -> :combolist
		Object combolist = createImpl(":combolist");
		set(combolist, "combobox", combobox);
		set(combobox, ":combolist", combolist);
		// add :combolist to the root desktop and set the combobox as popupowner
		popupowner = combobox;
		insertItem(content, ":comp", combolist, 0);
		set(combolist, ":parent", content);
		// lay out choices verticaly and calculate max width and height sum
		int pw = 0;
		int ph = 0;
		for (Object item = get(combobox, ":comp"); item != null; item = get(
				item, ":next")) {
			//Dimension d = getSize(item, 8, 4);
			Dimension d = (getComponentClass(item) == "separator") ? new Dimension(3*margin_1, 3*margin_1)
			: getSize(item, 8*margin_1, 4*margin_1,false);
			setRectangle(item, "bounds", 0, ph, d.width, d.height);
			pw = Math.max(pw, d.width);
			ph += d.height;
		}
		// set :combolist bounds
		int listy = 0, listheight = 0;
		int bellow = getRectangle(content, "bounds").height - comboy
				- comboheight - 1;
		if ((ph + 2 > bellow) && (comboy - 1 > bellow)) { // popup above
															// combobox
			listy = Math.max(0, comboy - 1 - ph - 2);
			listheight = Math.min(comboy - 1, ph + 2);
		} else { // popup bellow combobox
			listy = comboy + comboheight + 1;
			listheight = Math.min(bellow, ph + 2);
		}
		setRectangle(combolist, "bounds", combox, listy, combowidth, listheight);
		layoutScroll(combolist, pw, ph, 0, 0, 0, 0, true, 0);
		repaint(combolist);
		// hover the selected item
		int selected = getInteger(combobox, "selected", -1);
		setInside(combolist, (selected != -1) ? getItem(combobox, selected)
				: null, true);
		return combolist;
	}

	/**
	 * @param component
	 *            menubar or :popup
	 * @return the created popupmenu
	 */
	protected Object popupMenu(Object component) {
		Object popup = get(component, ":popup"); // first :popup child
		Object selected = get(component, "selected"); // selected menu in of the
														// component
		if (popup != null) { // remove its current :popup
			if (get(popup, "menu") == selected) {
				return null;
			} // but the currect one
			set(popup, "selected", null);
			set(popup, "menu", null);
			repaint(popup);
			removeItemImpl(content, popup);
			set(popup, ":parent", null);
			set(component, ":popup", null);
			if (mouseinside == popup) {
				checkLocation();
			}
			popupMenu(popup); // remove recursively
		}
		// pop up the selected menu only
		if ((selected == null) || (getComponentClass(selected) != "menu")) {
			return null;
		}
		// create the :popup, :popup.menu -> menu,
		// menubar|:popup.:popup -> :popup, menubar|:popup.selected -> menu
		popup = createImpl(":popup");
		set(popup, "menu", selected);
		set(component, ":popup", popup);
		insertItem(content, ":comp", popup, 0);
		set(popup, ":parent", content);

		// calculates the bounds of the previous menubar/:popup relative to the
		// root desktop
		int menux = 0, menuy = 0, menuwidth = 0, menuheight = 0;
		for (Object comp = component; comp != content; comp = getParent(comp)) {
			if(!getBoolean(comp,"visible",true))continue;
			Rectangle r = getRectangle(comp, "bounds");
			menux += r.x;
			menuy += r.y;
			Rectangle view = getRectangle(comp, ":view");
			if (view != null) {
				menux -= view.x;
				menuy -= view.y;
				Rectangle port = getRectangle(comp, ":port");
				menux += port.x;
				menuy += port.y;
			}
			if (comp == component) {
				menuwidth = r.width;
				menuheight = r.height;
			}
		}
		// set :popup bounds
		Rectangle menubounds = getRectangle(selected, "bounds");
		boolean menubar = ("menubar" == getComponentClass(component));
		if (menubar) {
			popupowner = component;
		}
		popup(selected, popup,
				menubar ? (("bottom" != get(component, "placement")) ? 'D'
						: 'U') : 'R', menubar ? (menux + menubounds.x) : menux,
				menuy + menubounds.y, menubar ? menubounds.width : menuwidth,
				menubar ? menuheight : menubounds.height, menubar ? margin_1 : 3*margin_1);
		return popup;
	}

	/**
	 * @param popupmenu
	 */
	protected void popupPopup(Object popupmenu, int x, int y) {
	//	if(Preferences.isSimulationLocked())return;

		closeup();	
		invoke(popupmenu, null, "menushown"); // TODO before

		// :popup.menu -> popupmenu, popupmenu.:popup -> :popup
		Object popup = createImpl(":popup");
		set(popup, "menu", popupmenu);
		set(popupmenu, ":popup", popup);
		
		// add : to the root desktop and set the combobox as owner
		popupowner = popupmenu;
		insertItem(content, ":comp", popup, 0);
		set(popup, ":parent", content);
		// invoke menushown listener
		// lay out
		popup(popupmenu, popup, 'D', x, y, 0, 0, 0);
	
	}

	/**
	 * Lays out a popupmenu
	 * 
	 * @param menu
	 *            menubar's menu, menu's menu, or component's popupmenu
	 *            including items
	 * @param popup
	 *            created popupmenu
	 * @param direction
	 *            'U' for up, 'D' for down, and 'R' for right
	 * @param x
	 *            menu's x location relative to the desktop
	 * @param y
	 *            menu's y location
	 * @param width
	 *            menu's width, or zero for popupmenu
	 * @param height
	 *            menu's height
	 * @param offset
	 *            inner padding relative to the menu's bounds
	 */
	private void popup(Object menu, Object popup, char direction, int x, int y,
			int width, int height, int offset) {
		int pw = 0;
		int ph = 0;
	
		for (Object item = get(menu, ":comp"); item != null; item = get(item,
				":next")) {
			boolean visible=getBoolean(item,"visible",true);
			if(!visible){
				continue;
			}
			String itemclass = getComponentClass(item);
			Dimension d = (itemclass == "separator") ? new Dimension(margin_1,margin_1)
					: getSize(item, 8*margin_1, 4*margin_1,false);
			if (itemclass == "checkboxmenuitem") {
				d.width = d.width + block + 3*margin_1;
				d.height = Math.max(block, d.height);
			} else if (itemclass == "menu") {
				d.width += block;
			}else if(itemclass == "panel"){
				
				Dimension d1=getPreferredSize(item);
				setRectangle(item, "bounds", 1, 1 + ph, d.width, d.height);doLayout(item);
				d.width = d1.width;
				d.height =  d1.height;
			}
			String accelerator = getAccelerator(item); // add accelerator width
			if (accelerator != null) {
				d.width += 4*margin_1 + font.stringWidth(accelerator); // TODO
																				// font,
																				// height
																				// and
																				// gap
			}
			setRectangle(item, "bounds", 1, 1 + ph, d.width, d.height);
			pw = Math.max(pw, d.width);
			ph += d.height;
		}
		pw += 2*margin_1;
		ph += 2*margin_1; // add border widths
		// set :popup bounds
		Rectangle desktop = getRectangle(content, "bounds");
		if(desktop.width<0)desktop.width=-desktop.width;
		if (direction == 'R') {
			x += ((x + width - offset + pw > desktop.width) && (x >= pw
					- offset)) ? (offset - pw) : (width - offset);
			if ((y + ph > desktop.height) && (ph <= y + height)) {
				y -= ph - height;
			}
		} else {
			boolean topspace = (y >= ph - offset); // sufficient space above
			boolean bottomspace = (desktop.height - y - height >= ph - offset);
			y += ((direction == 'U') ? (topspace || !bottomspace)
					: (!bottomspace && topspace)) ? (offset - ph)
					: (height - offset);
		}
		setRectangle(popup, "bounds",
				Math.max(0, Math.min(x, desktop.width - pw)),
				Math.max(0, Math.min(y, desktop.height - ph)), pw, ph);
		repaint(popup);
	}

	/**
	 * @param item
	 *            //TODO can be scrollbar string
	 */
	private void closeCombo(Object combobox, Object combolist, Object item) {
		if ((item != null) && getBoolean(item, "enabled", true)) {
			String text = getString(item, "text", "");
			set(combobox, "text", text); // if editable
			putProperty(combobox, "i18n.text", null); // for I18N
			setInteger(combobox, "start", text.length(), 0);
			setInteger(combobox, "end", 0, 0);
			set(combobox, "icon", get(item, "icon"));
			set(combobox, "background", get(item, "background"));
			//set(combobox, "foreground", get(item, "foreground"));
			validate(combobox);
			setInteger(combobox, "selected", getIndex(combobox, item), -1);
			invoke(combobox, item, "action");
		}
		set(combolist, "combobox", null);
		set(combobox, ":combolist", null);
		removeItemImpl(content, combolist);
		repaint(combolist);
		set(combolist, ":parent", null);
		popupowner = null;
		if (mouseinside == combolist) {
			checkLocation();
		}
	}

	/**
	 *
	 */
	public void closeup() {
		if (popupowner != null) {
			String classname = getComponentClass(popupowner);
			if ("menubar" == classname) {
				set(popupowner, "selected", null);
				popupMenu(popupowner);
				repaint(popupowner); // , selected
			} else if ("combobox" == classname) {
				closeCombo(popupowner, get(popupowner, ":combolist"), null);
			} else { // "popupmenu"
				popupMenu(popupowner);
			}
			popupowner = null;
			repaintNeeded=true;
		}
	}

	/**
	 *
	 */
	private void showTip() {
		String text = null;
		tooltipowner = null;
		String classname = getComponentClass(mouseinside);
		if ((classname == "tabbedpane") || (classname == "menubar")
				|| (classname == ":popup")) {
			if (insidepart != null) {
				text = getString(insidepart, "tooltip", null);
			}
		} else if (classname == ":combolist") {
			if (insidepart instanceof Object[]) {
				text = getString(insidepart, "tooltip", null);
			}
		}
		// TODO list table tree
		if (text == null) {
			text = getString(mouseinside, "tooltip", null);
		} else {
			tooltipowner = insidepart;
		}
		if (text != null) {
			int width = font.stringWidth(text) + 4;
			int height = font.getAscent() + font.getDescent() + 4;
			if (tooltipowner == null) {
				tooltipowner = mouseinside;
			}
			Rectangle bounds = getRectangle(content, "bounds");
			int tx = Math.max(0, Math.min(mousex + 10, bounds.width - width));
			int ty = Math.max(0, Math.min(mousey + 10, bounds.height - height));
			setRectangle(tooltipowner, ":tooltipbounds", tx, ty, width, height);
			//repaint(tx, ty, width, height);
		}
	}

	/**
	 *
	 */
	private void hideTip() {
		if (tooltipowner != null) {
			Rectangle bounds = getRectangle(tooltipowner, ":tooltipbounds");
			set(tooltipowner, ":tooltipbounds", null);
			tooltipowner = null;
			//repaint(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	/**
	 *
	 */
	private void layoutField(Object component, int dw, boolean hidden, int left) {
		int width = getRectangle(component, "bounds").width - left - dw;
		String text = getString(component, "text", "");
		if(text==null)text="";
		int start = getInteger(component, "start", 0);
		if (start > text.length()) {
			setInteger(component, "start", start = text.length(), 0);
		}
		int end = getInteger(component, "end", 0);
		if (end > text.length()) {
			setInteger(component, "end", end = text.length(), 0);
		}
		int offset = getInteger(component, ":offset", 0);
		int off = offset;
		Font currentfont =  getFont(component);
		currentfont.update();
		int caret = hidden ? (currentfont.charWidth('*') * end) : currentfont.stringWidth(text
				.substring(0, end));
		if (off > caret) {
			off = caret;
		} else if (off < caret - width + 4*margin_1) {
			off = caret - width + 4*margin_1;
		}
		off = Math.max(
				0,
				Math.min(off, (hidden ? (currentfont.charWidth('*') * text.length())
						: currentfont.stringWidth(text)) - width + 4*margin_1));
		if (off != offset) {
			setInteger(component, ":offset", off, 0);
		}
	}

	/**
	 * Set viewport (:port) bounds excluding borders, view position and content
	 * size (:view), horizontal (:horizontal), and vertical (:vertical)
	 * scrollbar bounds
	 *
	 * @param component
	 *            scrollable widget
	 * @param contentwidth
	 *            preferred component width
	 * @param contentheight
	 *            preferred component height
	 * @param top
	 *            top inset (e.g. table header, dialog title, half of panel
	 *            title)
	 * @param left
	 *            left inset (e.g. dialog border)
	 * @param bottom
	 *            bottom inset (e.g. dialog border)
	 * @param right
	 *            right inset (e.g. dialog border)
	 * @param topgap
	 *            (lower half of panel title)
	 * @return true if scrollpane is required, otherwise false
	 *
	 *         list: 0, 0, 0, 0, true, 0 | table: header, ... | dialog: header,
	 *         3, 3, 3, true, 0 title-border panel: header / 2, 0, 0, 0, true,
	 *         head
	 */
	private boolean layoutScroll(Object component, int contentwidth,
			int contentheight, int top, int left, int bottom, int right,
			boolean border, int topgap) {
		Rectangle bounds = getRectangle(component, "bounds");
		int iborder = border ? margin_1 : 0;
		int iscroll = block + margin_1 - iborder;
		int portwidth = bounds.width - left - right - 2 * iborder; // available
																	// horizontal
																	// space
		int portheight = bounds.height - top - topgap - bottom - 2 * iborder; // vertical
																				// space
		boolean hneed = contentwidth > portwidth; // horizontal scrollbar
													// required
		boolean vneed = contentheight > portheight - (hneed ? iscroll : 0); // vertical
																			// scrollbar
																			// needed
		if (vneed) {
			portwidth -= iscroll;
		} // subtract by vertical scrollbar width
		hneed = hneed || (vneed && (contentwidth > portwidth));
		if (hneed) {
			portheight -= iscroll;
		} // subtract by horizontal scrollbar height

		setRectangle(component, ":port", left + iborder,
				top + iborder + topgap, portwidth, portheight);
		if (hneed) {
			setRectangle(component, ":horizontal", left, bounds.height - bottom
					- block - margin_1, bounds.width - left - right
					- (vneed ? block : 0), block + margin_1);
		} else {
			set(component, ":horizontal", null);
		}
		if (vneed) {
			setRectangle(component, ":vertical", bounds.width - right - block
					- margin_1, top, block + margin_1, bounds.height - top - bottom
					- (hneed ? block : 0));
		} else {
			set(component, ":vertical", null);
		}

		contentwidth = Math.max(contentwidth, portwidth);
		contentheight = Math.max(contentheight, portheight);
		int viewx = 0, viewy = 0;
		Rectangle view = getRectangle(component, ":view");
		if (view != null) { // check the previous location
			viewx = Math.max(0, Math.min(view.x, contentwidth - portwidth));
			viewy = Math.max(0, Math.min(view.y, contentheight - portheight));
		}
		setRectangle(component, ":view", viewx, viewy, contentwidth,
				contentheight);
		return vneed || hneed;
	}

	/**
	 *
	 */
	private void scrollToVisible(Object component, int x, int y, int width,
			int height) {
		Rectangle view = getRectangle(component, ":view");
		Rectangle port = getRectangle(component, ":port");
		int vx = Math.max(x + width - port.width, Math.min(view.x, x));
		int vy = Math.max(y + height - port.height, Math.min(view.y, y));
		if ((view.x != vx) || (view.y != vy)) {
			repaint(component); // horizontal | vertical
			view.x = vx;
			view.y = vy;
		}
	}


	/**
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	Dimension getPreferredSize(Object component) {
		int width = getInteger(component, "width", 0);
		int height = getInteger(component, "height", 0);
		if ((width > 0) && (height > 0)) {
			return new Dimension(width, height);
		}
	
		String classname = getComponentClass(component);
		if ("label" == classname) {
			return getSize(component, 0, 0,true);
		}
		if (("button" == classname) || ("togglebutton" == classname)) {
			boolean link = ("button" == classname)
					&& (get(component, "type") == "link");
			return getSize(component, link ? 0 : 12*margin_1, link ? 0 : 6*margin_1,true);
		}
		if ("checkbox" == classname) {
			Dimension d = getSize(component, 0, 0,false);
			int block = getFont(component).getHeight();
			d.width = d.width + block + 3*margin_1;
			d.height = Math.max(block, d.height);
			return d;
		}
		if ("combobox" == classname) {
			if (getBoolean(component, "editable", true)) {
				Dimension size = getFieldSize(component);
				AWTImage icon = getIcon(component, "icon", null);
				if (icon != null) {
					size.width += icon.getScaledWidth();
					size.height = Math.max(size.height,
							icon.getScaledHeight() + margin_1*2);
				}
				size.width += block;
				return size;
			} else {
				int margin=4*margin_1;
				// maximum size of current values and choices including 2-2-2-2
				// insets
				Dimension size = getSize(component, margin, margin,false);
				for (Object item = get(component, ":comp"); item != null; item = get(
						item, ":next")) {
					Dimension d = getSize(item, margin, margin,false);
					size.width = Math.max(d.width, size.width);
					size.height = Math.max(d.height, size.height);
				}
				size.width += block;
				if (size.height == margin) { // no content nor items, set text height
					Font customfont = getFont(component);
					customfont.update();
					size.height = customfont.getAscent() + customfont.getDescent() + margin;
				}
				return size;
			}
		}
		if (("textfield" == classname) || ("passwordfield" == classname)) {
			return getFieldSize(component);
		}
		if ("textarea" == classname) {
			int columns = getInteger(component, "columns", 0);
			int rows = getInteger(component, "rows", 0); // 'e' -> 'm' ?
			Font currentfont = getFont(component);
			int margin=2*margin_1;
			return new Dimension(
					((columns > 0) ? (columns * currentfont.charWidth('e') + margin) : 5*block)
							+ margin + block, ((rows > 0) ? (rows * currentfont.getHeight()
							- currentfont.getLeading() + margin) : 3*block)
							+ margin + block);
		}
		if ("tabbedpane" == classname) {
			String placement = getString(component, "placement", "top");
			boolean horizontal = ((placement != "left") && (placement != "right"));
			int tabsize = 0; // max tab height (for horizontal),
			// max tabwidth (for vertical), or sum of tab heights for stacked
			int contentwidth = 0;
			int contentheight = 0; // max content size
			int margin=3*margin_1;
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				Dimension d = getSize(tab, 0, 0,false);
				if (placement == "stacked") {
					tabsize += d.height + margin;
				} else {
					tabsize = Math.max(tabsize, horizontal ? d.height + margin+margin_1*2
							: d.width + margin*3);
				}

				Object comp = get(tab, ":comp");
				if ((comp != null) && getBoolean(comp, "visible", true)) {
					Dimension dc = getPreferredSize(comp);
					contentwidth = Math.max(contentwidth, dc.width);
					contentheight = Math.max(contentheight, dc.height);
				}
			}
			
			if((placement == "none"))tabsize=0;
			return new Dimension(contentwidth
					+ (horizontal ? margin+margin_1 : (tabsize + margin)), contentheight
					+ (horizontal ? (tabsize + margin) : margin+margin_1));
		}
		if (("panel" == classname) || (classname == "dialog")) {
			// title text and icon height
			
			Dimension size = getSize(component, 0, 0,false);
			int iw=size.width;
			int ih=size.height;
			AWTImage icon = getIcon(component, "bgimage", null);
			if(icon!=null && ("panel" == classname) ){
				iw=icon.getScaledWidth();
				ih=icon.getScaledHeight()+ih;
			}
			// add border size
			if (classname == "dialog") {
				size.width= 8*margin_1;
				size.height += 8*margin_1; // title width neglected
			} else if (getBoolean(component, "border", false)) { // bordered
																	// panel
				size.width = 2*margin_1;
				size.height += (size.height > 0) ? margin_1 : 2*margin_1; // title includes line
			} else {
				size.width = 0;
			} // title width is clipped
			// add paddings
			size.width += getInteger(component, "left", 0)
					+ getInteger(component, "right", 0);
			size.height += getInteger(component, "top", 0)
					+ getInteger(component, "bottom", 0);
			// add content preferred size
			int gap = getInteger(component, "gap", 0);
			int[][] grid = getGrid(component);
			if (grid != null) { // has components
				size.width += getSum(grid[0], 0, grid[0].length, gap, false);
				size.height += getSum(grid[1], 0, grid[1].length, gap, false);
			}
			size.width=Math.max(size.width,iw);
			size.height=Math.max(size.height,ih);
			return size;
		} else if ("desktop" == classname) {
			Dimension size = new Dimension();
			for (Object comp = get(component, ":comp"); comp != null; comp = get(
					comp, ":next")) {
				String iclass = getComponentClass(comp);
				if ((iclass != "dialog") && (iclass != ":popup")
						&& (iclass != ":combolist")) {
					Dimension d = getPreferredSize(comp);
					size.width = Math.max(d.width, size.width);
					size.height = Math.max(d.height, size.height);
				}
			}
			return size;
		}
		if ("spinbox" == classname) {
			Dimension size = getFieldSize(component);
			size.width += block;
			return size;
		}
		if ("progressbar" == classname) {
			boolean horizontal = ("vertical" != get(component, "orientation"));
			return new Dimension(horizontal ? 6*block : block/2, horizontal ? block/2 :6*block);
		}
		if ("slider" == classname) {
			boolean horizontal = ("vertical" != get(component, "orientation"));
			Dimension size = getSize(component, 0, 0,false);
			return new Dimension(horizontal ? Math.max(size.width,8*block ): (int)(0.8*block), horizontal ?(int)(0.8*block+size.height) : 6*block);
			//return new Dimension(horizontal ? 5*block : (int)(0.8*block), horizontal ?(int)(0.8*block) : 5*block);
		}
		if ("splitpane" == classname) {
			boolean horizontal = ("vertical" != get(component, "orientation"));
			Object comp1 = get(component, ":comp");
			Dimension size = ((comp1 == null) || !getBoolean(comp1, "visible",
					true)) ? new Dimension() : getPreferredSize(comp1);
			Object comp2 = get(comp1, ":next");
			if ((comp2 != null) && getBoolean(comp2, "visible", true)) {
				Dimension d = getPreferredSize(comp2);
				size.width = horizontal ? (size.width + d.width) : Math.max(
						size.width, d.width);
				size.height = horizontal ? Math.max(size.height, d.height)
						: (size.height + d.height);
			}
			if (horizontal) {
				size.width += (5*margin_1);
			} else {
				size.height += (5*margin_1);
			}
			return size;
		}
		if (("list" == classname) || ("table" == classname)
				|| ("tree" == classname)) {
			return new Dimension(6*block + 2*margin_1 , 6*block+ 2*margin_1 );
		}
		if ("separator" == classname) {
			return new Dimension(margin_1, margin_1);
		}
		if ("menubar" == classname) {
			Dimension size = new Dimension(0, 0);
			for (Object menu = get(component, ":comp"); menu != null; menu = get(
					menu, ":next")) {
				Dimension d = getSize(menu, 8*margin_1, 4*margin_1,false);
				size.width += d.width;
				size.height = Math.max(size.height, d.height);
			}
			return size;
		}
		if ("bean" == classname) {
			return ((CustomComponent) get(component, "bean")).getPreferredSize();
		}
		throw new IllegalArgumentException(classname);
	}

	/**
	 * @param component
	 *            a container
	 * @return null for zero visible subcomponent, otherwise an array contains
	 *         the following lists:
	 *         <ul>
	 *         <li>columnwidths, preferred width of grid columns</li>
	 *         <li>rowheights, preferred heights of grid rows</li>
	 *         <li>columnweights, grid column-width weights</li>
	 *         <li>rowweights, grid row-height weights</li>
	 *         <li>gridx, horizontal location of the subcomponents</li>
	 *         <li>gridy, vertical locations</li>
	 *         <li>gridwidth, column spans</li>
	 *         <li>gridheight, row spans</li>
	 *         </ul>
	 */
	private int[][] getGrid(Object component) {
		int count = 0; // count of the visible subcomponents
		for (Object comp = get(component, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			if (getBoolean(comp, "visible", true)) {
				count++;
			}
		}
		if (count == 0) {
			return null;
		} // zero subcomponent
		int columns = getInteger(component, "columns", 0);
		int icols = (columns != 0) ? columns : count;
		int irows = (columns != 0) ? ((count + columns - 1) / columns) : 1;
		int[][] grid = { new int[icols], new int[irows], // columnwidths,
															// rowheights
				new int[icols], new int[irows], // columnweights, rowweights
				new int[count], new int[count], // gridx, gridy
				new int[count], new int[count] }; // gridwidth, gridheight
		int[] columnheight = new int[icols];
		int[][] cache = null; // preferredwidth, height, columnweight, rowweight

		int i = 0;
		int x = 0;
		int y = 0;
		int nextsize = 0;
		for (Object comp = get(component, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			if (!getBoolean(comp, "visible", true)) {
				continue;
			}
			int colspan = ((columns != 0) && (columns < count)) ? Math.min(
					getInteger(comp, "colspan", 1), columns) : 1;
			int rowspan = (columns != 1) ? getInteger(comp, "rowspan", 1) : 1;

			for (int j = 0; j < colspan; j++) {
				if ((columns != 0) && (x + colspan > columns)) {
					x = 0;
					y++;
					j = -1;
				} else if (columnheight[x + j] > y) {
					x += (j + 1);
					j = -1;
				}
			}
			if (y + rowspan > grid[1].length) {
				int[] rowheights = new int[y + rowspan];
				System.arraycopy(grid[1], 0, rowheights, 0, grid[1].length);
				grid[1] = rowheights;
				int[] rowweights = new int[y + rowspan];
				System.arraycopy(grid[3], 0, rowweights, 0, grid[3].length);
				grid[3] = rowweights;
			}
			for (int j = 0; j < colspan; j++) {
				columnheight[x + j] = y + rowspan;
			}

			int weightx = getInteger(comp, "weightx", 0);
			int weighty = getInteger(comp, "weighty", 0);
			Dimension d = getPreferredSize(comp);

			if (colspan == 1) {
				grid[0][x] = Math.max(grid[0][x], d.width); // columnwidths
				grid[2][x] = Math.max(grid[2][x], weightx); // columnweights
			} else {
				if (cache == null) {
					cache = new int[4][count];
				}
				cache[0][i] = d.width;
				cache[2][i] = weightx;
				if ((nextsize == 0) || (colspan < nextsize)) {
					nextsize = colspan;
				}
			}
			if (rowspan == 1) {
				grid[1][y] = Math.max(grid[1][y], d.height); // rowheights
				grid[3][y] = Math.max(grid[3][y], weighty); // rowweights
			} else {
				if (cache == null) {
					cache = new int[4][count];
				}
				cache[1][i] = d.height;
				cache[3][i] = weighty;
				if ((nextsize == 0) || (rowspan < nextsize)) {
					nextsize = rowspan;
				}
			}
			grid[4][i] = x; // gridx
			grid[5][i] = y; // gridy
			grid[6][i] = colspan; // gridwidth
			grid[7][i] = rowspan; // gridheight

			x += colspan;
			i++;
		}

		while (nextsize != 0) {
			int size = nextsize;
			nextsize = 0;
			for (int j = 0; j < 2; j++) { // horizontal, vertical
				for (int k = 0; k < count; k++) {
					if (grid[6 + j][k] == size) { // gridwidth, gridheight
						int gridpoint = grid[4 + j][k]; // gridx, gridy

						int weightdiff = cache[2 + j][k];
						for (int m = 0; (weightdiff > 0) && (m < size); m++) {
							weightdiff -= grid[2 + j][gridpoint + m];
						}
						if (weightdiff > 0) {
							int weightsum = cache[2 + j][k] - weightdiff;
							for (int m = 0; (weightsum > 0) && (m < size); m++) {
								int weight = grid[2 + j][gridpoint + m];
								if (weight > 0) {
									int weightinc = weight * weightdiff
											/ weightsum;
									grid[2 + j][gridpoint + m] += weightinc;
									weightdiff -= weightinc;
									weightsum -= weightinc;
								}
							}
							grid[2 + j][gridpoint + size - 1] += weightdiff;
						}

						int sizediff = cache[j][k];
						int weightsum = 0;
						for (int m = 0; (sizediff > 0) && (m < size); m++) {
							sizediff -= grid[j][gridpoint + m];
							weightsum += grid[2 + j][gridpoint + m];
						}
						if (sizediff > 0) {
							for (int m = 0; (weightsum > 0) && (m < size); m++) {
								int weight = grid[2 + j][gridpoint + m];
								if (weight > 0) {
									int sizeinc = weight * sizediff / weightsum;
									grid[j][gridpoint + m] += sizeinc;
									sizediff -= sizeinc;
									weightsum -= weight;
								}
							}
							grid[j][gridpoint + size - 1] += sizediff;
						}
					} else if ((grid[6 + j][k] > size)
							&& ((nextsize == 0) || (grid[6 + j][k] < nextsize))) {
						nextsize = grid[6 + j][k];
					}
				}
			}
		}
		return grid;
	}

	/**
	 *
	 */
	private int getSum(int[] values, int from, int length, int gap, boolean last) {
		if (length <= 0) {
			return 0;
		}
		int value = 0;
		for (int i = 0; i < length; i++) {
			value += values[from + i];
		}
		return value + (length - (last ? 0 : 1)) * gap;
	}

	/**
	 *
	 */
	private Dimension getFieldSize(Object component) {
		int columns = getInteger(component, "columns", 0);
		Font currentfont = getFont(component);
		return new Dimension(((columns > 0) ? (columns * currentfont.charWidth('e'))
				: 76*margin_1) + 4*margin_1, currentfont.getAscent() + currentfont.getDescent() + 4*margin_1); // fm.stringWidth(text)
	}

	/**
	 * @param component
	 *            a widget including the text and icon parameters
	 * @param dx
	 *            increase width by this value
	 * @param dy
	 *            increase height by this value
	 * @param includeImage
	 *            size takes background image in account         
	 * @return size of the text and the image (plus a gap) including the given
	 *         offsets
	 */
	private Dimension getSize(Object component, int dx, int dy,boolean includeImage) {
		String text = getString(component, "text", null);
		int tw = 0;
		int th = 0;
		if (text != null && !text.isEmpty()) {
			Font customfont = getFont(component);
			customfont.update();
			tw = customfont.stringWidth(text);
			th = customfont.getAscent() + customfont.getDescent();
		}
		AWTImage icon = getIcon(component, "icon", null);
		int iw = 0;
		int ih = 0;
		if (icon != null) {
			iw = icon.getScaledWidth();
			ih = icon.getScaledHeight();
			if (text != null) {
				iw += 3*margin_1;
			}
		}
		if(!includeImage)return new Dimension(tw + iw + dx, Math.max(th, ih) + dy);

		icon = getIcon(component, "bgimage", null);

		if (icon != null) {
			//icon.updateBrush();
			return new Dimension((int)(Math.max(tw + iw + dx,icon.width)), (int) Math.max(Math.max(th, ih) + dy,icon.height));
		}
		
		return new Dimension(tw + iw + dx, Math.max(th, ih) + dy);
	}

	
	/**
	 * Paints the components inside the graphics clip area
	 */
	private void paint() {
		g.context.save();
		g.setFont(font);
		paint(0, 0, width, height, content, true);
		g.context.restore();
	}

	/**
	 * @param clipx the cliping rectangle is relative to the component's
	 * parent location similar to the component's bounds rectangle
	 * @param clipy
	 * @param clipwidth
	 * @param clipheight
	 * @throws java.lang.IllegalArgumentException
	 */
	private void paint(
			int clipx, int clipy, int clipwidth, int clipheight,
			Object component, boolean enabled) {
		if (!getBoolean(component, "visible", true)) { return; }
		String classname = getComponentClass(component);
		
		Rectangle bounds = getRectangle(component, "bounds");
		if (bounds == null) { 	
			//console.log("bounds for "+classname+ "before layout  is ="+bounds);
			return; 
		}
		// negative component width indicates invalid component layout
		if (bounds.width < 0) {
			bounds.width = Math.abs(bounds.width);
			//console.log("bounds for "+classname+ "before layout  is ="+bounds);
			
			doLayout(component);
		}
		// return if the component was out of the cliping rectangle
		if ((clipx + clipwidth < bounds.x) ||
				(clipx > bounds.x + bounds.width) ||
				(clipy + clipheight < bounds.y) ||
				(clipy > bounds.y + bounds.height)) {
			console.log("bounds for "+classname+ "out of clip "+bounds );
			
			return;
		}
		
		Rectangle prevClip=g.getClipBounds();
		// set the clip rectangle relative to the component location
		clipx -= bounds.x; clipy -= bounds.y;
		
		g.translate(bounds.x, bounds.y); 
		
		g.clipRect(0, 0, bounds.width, bounds.height);

		if (useFBO && "bean" == classname){
			CustomComponent c=getComponent(component,"bean");
			if(c!=null){
				Rectangle r=g.getClipBounds();
				c.clipRect.set(r.x,r.y,r.width,r.height);
				g.clearRect(0, 0, bounds.width-1, bounds.height-1);
				g.translate(-bounds.x, -bounds.y);
				clipx += bounds.x; clipy += bounds.y;
				g.setClip(prevClip.x,prevClip.y,prevClip.width,prevClip.height);
				return;
			}
		}
		
		boolean pressed = (mousepressed == component);
		boolean inside = (mouseinside == component) &&
			((mousepressed == null) || pressed);
		boolean hover=(mouseinside == component) &&
				(mousepressed == null);
		boolean focus = focusinside && (focusowner == component);
		enabled = getBoolean(component, "enabled", true); //enabled &&


		if ("label" == classname) {
			paint(component, 0, 0, bounds.width, bounds.height,
						clipx, clipy, clipwidth, clipheight, false, false, false, false,
						0, 0, 0, 0, false, enabled ? 'e' : 'd', "left", true, false,true);
		}
		else if (("button" == classname) || ("togglebutton" == classname)) {
			boolean toggled = ("togglebutton" == classname) && getBoolean(component, "selected", false);
			boolean link = ("button" == classname) && (get(component, "type") == "link");
			boolean parent=getParent(component)!=content;
			if (link) {
					paint(component, 0, 0, bounds.width, bounds.height,
							clipx, clipy, clipwidth, clipheight, false, false, false, false,
							0, 0, 0, 0, focus, enabled ? (pressed ? 'e' : 'l') : 'd', "center",
							true, enabled && (inside != pressed),false);
			} else { // disabled toggled
					char mode = enabled ? ((inside != pressed) ? 'h' : ((pressed || toggled) ? 'p' : 'g')) : 'd';
					
					if(parent==false && mode=='g')mode='b';
							paint(component, 0, 0, bounds.width, bounds.height,
							clipx, clipy, clipwidth, clipheight, true, true, true, true,
							2*margin_1, 5*margin_1, 2*margin_1, 5*margin_1, focus, mode, "center", true, false,true);
					
					//(enabled && ("button" == classname) && get(component, "type") == "default")...
			}
		}else if ("checkbox" == classname) {
			int block = getFont(component).getHeight();
			paint(component, 0, 0, bounds.width, bounds.height,
				 clipx, clipy, clipwidth, clipheight, false, false, false, false,
				 2*margin_1, block + 3*margin_1, 0, 0, false, enabled ? 'e' : 'd', "left", true, false,false);
			double lw=g.setLineWidth((float)block/this.block);
			boolean selected = getBoolean(component, "selected", false);
			String group = getString(component, "group", null);
			Color border = enabled ? c_border : c_disable;
			//Color background = enabled ? ((inside != pressed) ? c_hover :
			//	(pressed ? c_press : c_ctrl)) :  (Color) get(component, "background");
			Color background = (inside != pressed) ? c_hover :(Color) get(component, "background");
			
			//Color background = (Color) get(component, "background"); 
			if (background==null)background= c_bg;
			
			Color foreground = (Color) get(component, "foreground");
			if (foreground==null)foreground= c_text_fg;
			
			int dy = (bounds.height - block + 2) / 2;
			if (group == null) {
				paintRect(margin_1, dy + margin_1, block - 2*margin_1, block - 2*margin_1,
					border, background, true, true, true, true, true);
			} else {
				//paintRect(1, dy + 1, block - 2, block - 2,
				//		border, background, true, true, true, true, true);
				//g.gl.glEnable(GL2.GL_MULTISAMPLE);
				g.setColor((background != c_ctrl) ? background : c_bg);
				g.fillOval(1, dy + 1, block -2, block - 2);
				//g.gl.glEnable(GL2.GL_LINE_SMOOTH);
				g.setColor(border);
				g.drawOval(1, dy + 1, block - 2, block - 2);
				//g.gl.glDisable(GL2.GL_LINE_SMOOTH);
				//g.gl.glDisable(GL2.GL_MULTISAMPLE);
			}
			g.setLineWidth(lw);
			if (focus) {
				drawFocus(0, 0, bounds.width, bounds.height);
			}
			if((!selected && inside && pressed) ||
					(selected && (!inside || !pressed))) {
				g.setColor(enabled ? foreground : c_disable);
				//g.gl.glEnable(GL2.GL_LINE_SMOOTH);

				if (group == null) {
				//	g.fillRect(3, dy + block - 9, 2 + evm, 6 + evm);
				//	g.drawLine(2, dy + block - 4, block - 4, dy + 3);
				//	g.drawLine(4, dy + block - 4, block - 4, dy + 4);
					lw=g.setLineWidth(2.0f);//Math.max(1.5f,block/9   ));
					g.drawLine(2*margin_1, dy +block-block/4-4*margin_1, 2*margin_1+block/4, dy +block-3*margin_1);
					g.drawLine(2*margin_1+block/4, dy +block-3*margin_1,block-4*margin_1,dy+3*margin_1);
					g.setLineWidth(lw);
				} else {
					//g.fillRect(5, dy + 5, block - 10 + evm, block - 10 + evm);
					//g.drawRect(4, dy + 4, block - 9, block - 9);
					
					g.fillOval(3*margin_1, dy + 3*margin_1, block - 6*margin_1 , block - 6*margin_1 );
					//g.drawOval(4, dy + 4, block - 10, block - 10);
					g.drawOval(3*margin_1, dy + 3*margin_1, block - 6*margin_1, block - 6*margin_1);
				
				}
				//g.gl.glDisable(GL2.GL_LINE_SMOOTH);
			}
		}
		else if ("combobox" == classname) {
			if (getBoolean(component, "editable", true)) {
				AWTImage icon = getIcon(component, "icon", null);
				int left = (icon != null) ? icon.getScaledWidth() : 0;
				paintField(clipx, clipy, clipwidth, clipheight, component,
					bounds.width - block, bounds.height, focus, enabled, false, left);
				if (icon != null) {
					Color tintColor=enabled ?c_icon_tint : c_disable;
					//if(icon.iconFont==null)tintColor=null;
					//g.setColor(enabled ?);
					g.drawImage(icon, 2*margin_1, (bounds.height - icon.getScaledHeight()) / 2,tintColor );
				}
				paintArrow(bounds.width - block, 0, block, bounds.height,
					'S', enabled, inside, pressed, "down", true, false, true, true, true);
			} else {
				paint(component, 0, 0, bounds.width, bounds.height,
					clipx, clipy, clipwidth, clipheight,
					true, true, true, true, 1, 3*margin_1, 1, 1 + block, focus,
					enabled ? ((inside != pressed) ? 'h' : (pressed ? 'p' : 'g')) : 'd',
					"left", false, false,true);
				g.setColor(enabled ? c_text_fg : c_disable);
				paintArrow(bounds.width - block, 0, block, bounds.height, 'S');
			}
		}
		else if (":combolist" == classname) {
			paintScroll(component, classname, pressed, inside, focus, false, enabled,
				 clipx, clipy, clipwidth, clipheight);
		}
		else if (("textfield" == classname) || ("passwordfield" == classname)) {
			paintField(clipx, clipy, clipwidth, clipheight, component,
				bounds.width, bounds.height, focus, enabled, ("passwordfield" == classname), 0);
		}
		else if ("textarea" == classname) {
			paintScroll(component, classname, pressed, inside, focus, true, enabled,
				 clipx, clipy, clipwidth, clipheight);
		}
		else if ("tabbedpane" == classname) {
			int i = 0; Object selectedtab = null;
			int selected = getInteger(component, "selected", 0);
			String placement = getString(component, "placement", "top");
			boolean horizontal = ((placement == "top") || (placement == "bottom"));
			boolean stacked = (placement == "stacked");
			int bx = stacked ? 0 : horizontal ? 2*margin_1 : 1*margin_1, by = stacked ? 0 : horizontal ? 1*margin_1 : 2*margin_1,
				bw = 2 * bx, bh = 2 * by;
			// paint tabs except the selected one
			int pcx = clipx, pcy = clipy, pcw = clipwidth, pch = clipheight;
			clipx = Math.max(0, clipx); clipy = Math.max(0, clipy);
			clipwidth = Math.min(bounds.width, pcx + pcw) - clipx;
			clipheight = Math.min(bounds.height, pcy + pch) - clipy;
			g.clipRect(clipx, clipy, clipwidth, clipheight); // intersection of clip and bound
			boolean hideTabs=(placement == "none");
			for (Object tab = get(component, ":comp"); tab != null; tab = get(tab, ":next")) {
				Rectangle r = getRectangle(tab, "bounds");
				if (selected != i&& !hideTabs) {
					hover = inside && (mousepressed == null) && (insidepart == tab);
					boolean tabenabled = enabled && getBoolean(tab, "enabled", true);
					paint(tab, r.x + bx, r.y + by, r.width - bw, r.height - bh,
						 clipx, clipy, clipwidth, clipheight,
						(placement != "bottom"), (placement != "right"),
						!stacked && (placement != "top"), (placement != "left"),
						1, 3, 1, 3, false, tabenabled ? (hover ? 'h' : 'g') : 'd', "left", true, false,true);
				} else {
					selectedtab = tab;
					// paint tabbedpane border
					if(hideTabs){
						paintRect(0,0,bounds.width,bounds.height,c_border, c_bg, false,false, false, false, false);
					}else{
						paint(tab, (placement == "left") ? r.width - 1*margin_1 : 0,
						stacked ? (r.y + r.height - 1) : (placement == "top") ? r.height - 1 : 0,
						(horizontal || stacked) ? bounds.width : (bounds.width - r.width + 1),
						stacked ? (bounds.height - r.y - r.height + 1) :
						horizontal ? (bounds.height - r.height + 1) : bounds.height,
						true, true, true, true, enabled ? 'e' : 'd',true);
					}
					Object comp = get(selectedtab, ":comp");
					if ((comp != null) && getBoolean(comp, "visible", true)) {
						clipx -= r.x; clipy -= r.y; g.translate(r.x, r.y); // relative to tab
						paint(clipx, clipy, clipwidth, clipheight, comp, enabled);
						clipx += r.x; clipy += r.y; g.translate(-r.x, -r.y);
					}
				}
				i++;
			}
			
			// paint selected tab and its content
			if (selectedtab != null) {
				Rectangle r = getRectangle(selectedtab, "bounds");
				// paint selected tab
				int ph = stacked ? 3*margin_1 : (horizontal ? 5*margin_1 : 4*margin_1);
				int pv = stacked ? 1*margin_1 : (horizontal ? 2*margin_1 : 3*margin_1);
				paint(selectedtab, r.x, r.y, r.width, r.height,
					clipx, clipy, clipwidth, clipheight,
					(placement != "bottom"), (placement != "right"),
					!stacked && (placement != "top"), (placement != "left"),
					pv, ph, pv, ph, focus, enabled ? 'b' : 'i', "left", true, false,true);
			}
			//g.setClip(pcx, pcy, pcw, pch);
		}else if (("panel" == classname) || ("dialog" == classname)) {
			int titleheight = getInteger(component, ":titleheight", 0);
			
			g.clipRect(0, 0, bounds.width, bounds.height);

			AWTImage bgicon = getIcon(component, "bgimage", null);
			boolean border = getBoolean(component, "border", false);
			int offset=border?margin_1:0;
			if ("dialog" == classname) {
				boolean isModel=getBoolean(component, "modal", false);
				boolean isActive=containedIn(component,focusowner);
				if(isModel){
					g.setClipEnabled(false);
					g.setColor(c_overlay);
					g.fillRect(-bounds.x, -bounds.y, width, height);
					g.setClipEnabled(true);
				}
	
				if( titleheight>0){
					offset=3*margin_1;
					paint(component, 0, 0, bounds.width, offset + titleheight,
							clipx, clipy, clipwidth, clipheight, border, border, false, border,
							2*margin_1, 2*margin_1,2*margin_1, 2*margin_1, false, isModel && flicker?'p':'g', "left", false, false,true);
					int controlx = bounds.width - titleheight - 1;
					if (getBoolean(component, "closable", true)) {
						hover =  (mouseinside==component) && (insidepart == ":closebutton");
						pressed=(mousepressed==component)  && (insidepart == ":closebutton");
						paint(component,  controlx, offset, titleheight - offset, titleheight - offset, 'c',pressed?'p':(hover?'h':'g'));
						controlx -= titleheight;
					}
					if (getBoolean(component, "maximizable", false)) {
						hover =  (mouseinside==component) && (insidepart == ":maximizebutton");
						pressed= (mousepressed==component) && (insidepart == ":maximizebutton");
						paint(component,controlx, offset, titleheight - offset, titleheight - offset, 'm',pressed?'p':(hover?'h':'g'));
						controlx -= titleheight;
					}
					if (getBoolean(component, "iconifiable", false)) {
						hover =  (mouseinside==component) && (insidepart == ":iconifybutton");
						pressed=(mousepressed==component)  && (insidepart == ":iconifybutton");
						paint(component,controlx, offset, titleheight - offset, titleheight - offset, 'i',pressed?'p':(hover?'h':'g'));
						//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("insidepart="+insidepart);
					}
					
					paintRect(0, offset + titleheight, bounds.width, bounds.height -offset - titleheight,
							c_border, c_press, false, border, border, border, true); // lower part excluding titlebar
					//drawing a visible decorator in the lower right corner of the dialog if it is resizable
					if(getBoolean(component, "resizable", false)&& get(component,":minimized")==null) {
							g.setColor(c_resizeborder);
							g.fillRect(bounds.width-offset,bounds.height-3*offset,2*margin_1,9*margin_1);
							g.fillRect(bounds.width-3*offset,bounds.height-offset,9*margin_1,3*margin_1);
					}
					g.setColor(c_window_border);
					g.drawRect(0,0,bounds.width,bounds.height);
					if(isActive){
						g.setColor(c_select_bg);
						//controlx -= titleheight;
						//drawFocus(-1,-1,bounds.width+1,bounds.height+1);
						if(!isModel || !flicker){
							g.drawRect(0,0,bounds.width,bounds.height);
						}
					}
					//titleheight+=1; //to clip an extra pixel in case of dialog

					//draw only if sufficient space (not minimised)
					if( get(component, ":minimized")==null ){
						g.clipRect(offset, offset, bounds.width-2*offset, bounds.height-2*offset);

						if (bgicon != null && get(component, ":port") == null) {
							g.setColor(c_bgimgae_tint);
							g.drawImage(bgicon, offset,offset+titleheight,c_bgimgae_tint);
						}
						paint(component, // content area
								offset, offset + titleheight, bounds.width - 2*offset-1, bounds.height - 2*offset - titleheight,
								true, border, border, border, 'b',bgicon == null);
						
						g.clipRect(offset, offset, bounds.width-offset*2, bounds.height-offset*2);
					}
				}else{
	
				
					paint(component, // content area
							0, 0, bounds.width, bounds.height,
							border, border, border, border, 'b',true);
					if(isActive){
						g.setColor(c_focus);
						//controlx -= titleheight;
						//drawFocus(0,0,bounds.width,bounds.height);
						if(!flicker){
							g.drawRect(0,0,bounds.width,bounds.height);
						}
					}

					g.clipRect(offset,offset, bounds.width-2*offset, bounds.height-2*offset);

					if (bgicon != null) {
						g.setColor(c_bgimgae_tint);
						g.drawImage(bgicon, offset,offset,bounds.width-2*offset,bounds.height-2*offset);
					}
				}
			} else { // panel
			
				
				paint(component, 0, 0, bounds.width, bounds.height,
					 border, border, border, border, enabled ? 'e' : 'd',true);
				//to paint panel background enable following 2 lines
				//paintRect(0, 3 + titleheight, bounds.width, bounds.height - 3 - titleheight,
				//		c_border, c_bg, false, true, true, true, true); // lower part excluding titlebar
				
				g.clipRect(offset, offset, bounds.width-2*offset, bounds.height-2*offset);
				
				if (bgicon != null && get(component, ":port") == null) {
					g.setColor(c_bgimgae_tint);
					//g.drawImage(bgicon, offset,offset);
					g.drawImage(bgicon, offset,offset,bounds.width-2*offset,bounds.height-2*offset);
					
				}
				
				paint(component, 0, 0, bounds.width, titleheight, // panel title
					 clipx, clipy, clipwidth, clipheight, false, false, false, false,
					0, 0, 0, 0, false, enabled ? 'x' : 'd', "left", false, false,bgicon==null);
			}
	
			g.clipRect(offset, titleheight+offset, bounds.width-2*offset, bounds.height-2*offset-titleheight);
			if (get(component, ":port") != null) {
				//paintScroll(component, classname, pressed, inside, focus, false, enabled,
				//	clipx, clipy, clipwidth, clipheight);
				paintScroll(component, classname, pressed, inside, focus, false, enabled,
						clipx, clipy, clipwidth, clipheight);
			} else {
				
				//draw only if sufficient space (not minimised)
				if(get(component, ":minimized")==null){
					for (Object comp = get(component, ":comp");
						comp != null; comp = get(comp, ":next")) {
						//paint(clipx, clipy, clipwidth, clipheight, comp, enabled);
						paint(offset, offset, bounds.width-2*offset, bounds.height-2*offset, comp, enabled);
						
					}

				}
			}
			//g.setClip(clipx, clipy, clipwidth, clipheight);
			
		}else if ("desktop" == classname) {
			//disable desktop painting
			if(component!= content)paintRect(0, 0, bounds.width, bounds.height,
					c_border, c_bg, true, true, true, true, true);
			paintReverse(clipx, clipy, clipwidth, clipheight,
				get(component, ":comp"), enabled);
			//g.setColor(Color.red); if (clip != null) g.drawRect(clipx, clipy, clipwidth, clipheight);
			if ((tooltipowner != null) && (component == content)) {
				Rectangle r = getRectangle(tooltipowner, ":tooltipbounds");
				paintRect(r.x, r.y, r.width, r.height,
					c_border, c_tooltip_bg, true, true, true, true, true);
				String text = getString(tooltipowner, "tooltip", null);
				g.setColor(c_tooltip_fg);
				g.drawString(text, r.x + 2, r.y + font.getAscent() + 2); //+nullpointerexception
			}			
		}else if ("spinbox" == classname) {
			paintField(clipx, clipy, clipwidth, clipheight, component,
				bounds.width - block, bounds.height, focus, enabled, false, 0);
			paintArrow(bounds.width - block, 0, block, bounds.height / 2,
					'N', enabled, inside, pressed, "up", true, false, false, true, true);
			paintArrow(bounds.width - block, bounds.height / 2,
				block, bounds.height - (bounds.height / 2),
				'S', enabled, inside, pressed, "down", true, false, true, true, true);
		}else if ("progressbar" == classname) {
			double minimum = getDouble(component, "minimum", 0);
			double maximum = getDouble(component, "maximum", 100);
			double value = getDouble(component, "value", (maximum+minimum)/2);
			// fixed by by Mike Hartshorn and Timothy Stack
			boolean horizontal = ("vertical" != get(component, "orientation"));
			int length = (int) ((value - minimum) *
				((horizontal ? bounds.width : bounds.height) - 1) / (maximum - minimum));
			paintRect(0, 0, horizontal ? length : bounds.width,
				horizontal ? bounds.height : length, enabled ? c_border : c_disable,
				c_select_bg, true, true, horizontal, !horizontal, true);
			paintRect(horizontal ? length : 0, horizontal ? 0 : length,
				horizontal ? (bounds.width - length) : bounds.width	,
				horizontal ? bounds.height : (bounds.height - length),
				enabled ? c_border : c_disable, c_bg, true, true, true, true, true);
		}else if ("slider" == classname) {
			
			double minimum = getDouble(component, "minimum");
			double maximum = getDouble(component, "maximum");
			double value = getDouble(component, "value");
			if(value<minimum ||value>maximum){
				value=MathUtils.clamp(value, minimum, maximum);
				setDouble(component,"value",value);
			}
			boolean parent=getParent(component)!=content;
			boolean horizontal = ("vertical" != get(component, "orientation"));
			int length = (int) ((value - minimum) *
				((horizontal ? bounds.width : bounds.height) - block) /
				(maximum - minimum));
			if(!horizontal)length= bounds.height-length-block;
			int barSizeBy2=(int) (block*0.2);
			int width=bounds.width;
			int height=bounds.height;
			String text = getString(component, "text", null);

			Color background = enabled?(Color) get(component, "background"):c_bg;
			if (background==null)background= c_bg;
			Color foreground = (Color) get(component, "foreground");
			if (foreground==null)foreground= c_fg;
			if(text!=null){
				if(horizontal)height-=getSize(component,0,0,false).height;//- barSizeBy2*2;
				//if(enabled)paintRect(-1, 0,bounds.width+2, bounds.height+1, c_border,c_bg, false, false, false,false, false);
			}

			if(parent==false){
				//background.a=0.8f;
				g.setClipEnabled(false);
				//float lw=g.setLineWidth(3);
				
				double r=block*0.12f;
				
				g.setColor(foreground);
				int hby2=height+block/2;
				if(horizontal){
					//g.gl.glEnable(GL2.GL_LINE_SMOOTH);
					g.setColor(background);
					g.fillRect(r, hby2-r, bounds.width-2*r, 2*r);
					g.drawRect(r, hby2-r, bounds.width-2*r, 2*r);
					//g.drawLine(0, hby2,bounds.width ,hby2);
					r=(int) (focus?block*0.45:block*0.4);
				 	barSizeBy2=(int) r;
					g.setColor(foreground);//,foreground.a*0.8f);
					g.fillRect(length+barSizeBy2-r,hby2-r,2*r, 2*r);
					g.drawRect(length+barSizeBy2-r,hby2-r,2*r, 2*r);
					r=(int) (block*0.3f);
					//foreground.a=0.9f;
					g.setColor(foreground);
					g.fillRect(length+barSizeBy2-r,hby2-r,2*r, 2*r);
					//g.setColor(c_bg);
					//foreground.a=1f;
					g.drawRect(length+barSizeBy2-r,hby2-r,2*r, 2*r);
					//g.gl.glDisable(GL2.GL_LINE_SMOOTH);
				}else{
					hby2=block/2;
					//g.gl.glEnable(GL2.GL_LINE_SMOOTH);
					g.setColor(background);
					g.fillRect(hby2-r, 0, 2*r, bounds.height-2*r);
					g.drawRect(hby2-r, 0, 2*r, bounds.height-2*r);
					//g.drawLine(0, hby2,bounds.width ,hby2);
					r=(int) (focus?block*0.45:block*0.4);
					g.setColor(foreground);//,foreground.a*0.8f);
				 	barSizeBy2=(int) r;
					g.fillRect(hby2-r,length+barSizeBy2-r,2*r, 2*r);
					g.drawRect(hby2-r,length+barSizeBy2-r,2*r, 2*r);
					r=(int) (block*0.3f);
					//foreground.a=0.9f;
					g.setColor(foreground);
					g.fillRect(hby2-r,length+barSizeBy2-r,2*r, 2*r);
					//g.setColor(c_bg);
					//foreground.a=1f;
					g.drawRect(hby2-r,length+barSizeBy2-r,2*r, 2*r);
					//g.gl.glDisable(GL2.GL_LINE_SMOOTH);
				}
				//background.a=1;
				if(text!=null){
					Font currentFont=g.getFont();
					Font customfont = (Font) get(component, "font");
					if (customfont != null) { g.setFont(customfont); }
			
				//	value=value.toFixed(2);//MathUtils.roundOffToPreferredSigFigures(value);
					text=text.replaceAll("value", value+"");
					text=text.replaceAll("name", getString(component,"name")+"");
					g.setColor(foreground);//,Math.max(0.8f,foreground.a));
					if(horizontal)
						g.drawString(text, parent?0:length, (int) (hby2-block*0.7));// block/2+barSizeBy2+g.getFont().getDescent()/4);
					else
						g.drawString(text, block+4*margin_1, (int) (length+block*0.8f));
					if (customfont != null)g.setFont(currentFont);
				}
				g.setClipEnabled(true);
			}else{
			
				paintRect(horizontal ? 0 : barSizeBy2, horizontal ? bounds.height-height+barSizeBy2 : 0,
					horizontal ? length : (width - barSizeBy2*2),
					horizontal ? (height - barSizeBy2*2) : length,
					enabled ? c_border : c_disable,
					background, true, true, horizontal, !horizontal, true);
				
			
				paintRect(horizontal ? (block + length) : barSizeBy2,
					horizontal ? bounds.height-height+barSizeBy2  : (block + length),
					width - (horizontal ? (block + length) : barSizeBy2*2),
					height - (horizontal ? barSizeBy2*2 : (block + length)),
					enabled ? c_border : c_disable,
					c_hover, horizontal, !horizontal, true, true, true);
				paintRect(horizontal ? length : 0, horizontal ? bounds.height-height  : length,
					horizontal ? block : width, horizontal ? height : block,
								enabled ? c_border : c_disable,
										(inside != pressed && enabled) ?   c_hover : c_bg, true, true, true, true, true);
				if (focus) {
					drawFocus(0, 0, bounds.width , bounds.height );
				}
				if(text!=null){
					Font currentFont=g.getFont();
					Font customfont = (Font) get(component, "font");
					if (customfont != null) { g.setFont(customfont); }
			
					//value=MathUtils.roundOffToPreferredSigFigures(value);
					text=text.replaceAll("value", value+"");
					text=text.replaceAll("name", getString(component,"name")+"");
					g.setColor(foreground);
					if(horizontal)
						g.drawString(text, parent?0:length, g.getFont().getHeight()-3*margin_1);// block/2+barSizeBy2+g.getFont().getDescent()/4);
					else
						g.drawString(text, block, length+block-barSizeBy2*2);
					g.setFont(currentFont);
				}
				
			}
			
		}else if ("splitpane" == classname) {
			boolean horizontal = ("vertical" != get(component, "orientation"));
			int divider = getInteger(component, "divider", -1);
			paintRect(horizontal ? divider : 0, horizontal ? 1*margin_1 : divider,
				horizontal ? 5*margin_1 : bounds.width, horizontal ? bounds.height-2*margin_1 : 5*margin_1,
				c_border, c_bg, false, false, false, false, true);
			if (focus) {
				if (horizontal) { drawFocus(divider, 0, 4*margin_1, bounds.height ); }
				else { drawFocus(0, divider, bounds.width - margin_1, 4*margin_1); }
			}
			g.setColor(enabled ? c_border : c_disable);
			int xy = horizontal ? bounds.height : bounds.width;
			int xy1 = Math.max(0, xy / 2 - 12*margin_1);
			int xy2 = Math.min(xy / 2 + 12*margin_1, xy - 1);
			for (int i = divider + 1*margin_1; i < divider + 4*margin_1; i += 2*margin_1) {
				if (horizontal) { g.drawLine(i, xy1, i, xy2); }
					else { g.drawLine(xy1, i, xy2, i); }
			}
			Object comp1 = get(component, ":comp");
			if (comp1 != null) {
				if(divider>0)paint(clipx, clipy, clipwidth, clipheight, comp1, enabled);
				Object comp2 = get(comp1, ":next");
				if (comp2 != null) {
					paint(clipx, clipy, clipwidth, clipheight, comp2, enabled);
				}
			}
		}else if (("list" == classname) ||
				("table" == classname) || ("tree" == classname)) {
			paintScroll(component, classname, pressed, inside, focus,
				focus && (get(component, ":comp") == null), enabled,
				 clipx, clipy, clipwidth, clipheight);
		}else if ("separator" == classname) {
			g.setColor(enabled ? c_border : c_disable);
			g.fillRect(0, 0, bounds.width + evm, bounds.height + evm);
		}else if ("menubar" == classname) {
			Object selected = get(component, "selected");
			boolean placement = getString(component, "placement", "top")=="top";
			int lastx = 0;
		
			for (Object menu = get(component, ":comp");
					menu != null; menu = get(menu, ":next")) {
				Rectangle mb = getRectangle(menu, "bounds");
				if (clipx + clipwidth <= mb.x) { break; }
				if (clipx >= mb.x + mb.width) { continue; }
				boolean menuenabled = enabled && getBoolean(menu, "enabled", true);
				boolean armed = (selected == menu);
				boolean hoover = (selected == null) && (insidepart == menu);
				paint(menu, mb.x, 0, mb.width, bounds.height,
					clipx, clipy, clipwidth, clipheight, // TODO disabled
					placement?armed:true, armed, placement?true:armed, armed, 1, 3, 1, 3, false,
					enabled ? (menuenabled ? (armed ? 's' : (hoover ? 'h' : 'g')) : 'r') : 'd', "left", true, false,true);
				lastx = mb.x + mb.width;
			}
			paintRect(lastx, 0, bounds.width - lastx, bounds.height,
				enabled ? c_border : c_disable, enabled ? c_ctrl : c_bg,
					!placement, false, placement, false, true);
		}else if (":popup" == classname) {
			//repaintNeeded=true;
			c_shadow.a=0.3f;
			g.setColor(c_shadow);//,0.3f);
			g.fillRect(2,2,bounds.width, bounds.height);
			paintRect(0, 0, bounds.width+1, bounds.height,
				c_border, c_menu_bg, true, true, true, true, true);
			
			//g.setColor(c_focus,.);
			//g.fillRect(1,1,22, bounds.height-2);
			boolean leftShadowDrawn=true;
			Object selected = get(component, "selected");
			for (Object menu = get(get(component, "menu"), ":comp");
					menu != null; menu = get(menu, ":next")) {
				if(!getBoolean(menu,"visible",true)){
					continue;
				}
				Rectangle r = getRectangle(menu, "bounds");
				if (clipy + clipheight <= r.y) { break; }
				if (clipy >= r.y + r.height) { continue; }
				String itemclass = getComponentClass(menu);
				if (itemclass == "separator") {
					g.setColor(c_border);
					g.fillRect(r.x-1, r.y, bounds.width  + evm, r.height + evm+margin_1);
				} else {
					boolean armed = (selected == menu);
					boolean menuenabled = getBoolean(menu, "enabled", true);
					paint(menu, r.x, r.y, bounds.width -1, r.height+1,
						clipx, clipy, clipwidth, clipheight, false, false, false, false,
						2*margin_1, (itemclass == "checkboxmenuitem") ? (block + 7*margin_1) : 4*margin_1, 2*margin_1, 2*margin_1, false,
						menuenabled ? (armed ? 's' : 't') : 'd', "left", true, false,true);
					if(itemclass == "panel"){
						paint(clipx, clipy, clipwidth, clipheight, menu, enabled);
					}else if (itemclass == "checkboxmenuitem") {
						leftShadowDrawn=false;
						boolean checked = getBoolean(menu, "selected", false);
						String group = getString(menu, "group", null);
						g.translate(r.x + 3*margin_1, r.y + 2*margin_1);
						g.setColor(menuenabled ? c_border : c_disable);
						if (group == null) {
							g.drawRect(margin_1, margin_1, block - 3*margin_1, block - 3*margin_1);
						} else {
							g.drawRect(margin_1, margin_1, block - 3*margin_1, block - 3*margin_1);
							//g.drawOval(1, 1, block - 3, block - 3);
						}
						if (checked) {
							g.setColor(menuenabled ? c_menu_fg : c_disable);
							if (group == null) {
								//g.fillRect(3, block - 9, 2 + evm, 6 + evm);
								//g.drawLine(3, block - 4, block - 4, 3);
								//g.drawLine(4, block - 4, block - 4, 4);
								//g.gl.glEnable(GL2.GL_LINE_SMOOTH);
								double lw=g.setLineWidth(1.5f);
								g.drawLine(2*margin_1, 3*block/4-4*margin_1, 2*margin_1+block/4, block-4*margin_1);
								g.drawLine(2*margin_1+block/4, block-4*margin_1,block-5*margin_1,block/4);
								g.setLineWidth(lw);
								//g.gl.glDisable(GL2.GL_LINE_SMOOTH);
							} else {
								g.fillRect(5*margin_1, 5*margin_1, block - 10*margin_1 + evm, block - 10*margin_1 + evm);
								g.drawRect(4*margin_1, 4*margin_1, block - 9*margin_1, block - 9*margin_1);
								//g.fillOval(5, 5, block - 10 + evm, block - 10 + evm);
								//g.drawOval(4, 4, block - 9, block - 9);
							}
						}
						g.translate(-r.x - 3*margin_1, -r.y - 2*margin_1);
					}
					if (itemclass == "menu") {
						paintArrow(r.x + bounds.width - block, r.y, block, r.height, 'E');
					}
					else {
						String accelerator = getAccelerator(menu);
						if (accelerator != null) { //TODO
							g.drawString(accelerator, bounds.width - 4*margin_1 -
								(font).stringWidth(accelerator), r.y + 12*margin_1);
						}
					}
				}
			}
			if(leftShadowDrawn) {
				c_shadow.a=0.1f;
				g.setColor(c_shadow);//,0.10f);
				g.fillRect(2,1,9*margin_1+block, bounds.height-1);
				c_shadow.a=0.8f;
			}
		}else if ("bean" == classname) {
			//if(g.clipRect(0, 0, bounds.width, bounds.height)){
				((CustomComponent) get(component, "bean")).paint(g);
			//}
			//g.setClip(clipx, clipy, clipwidth, clipheight);
		}else {
			//throw new IllegalArgumentException(classname);
		}
		g.translate(-bounds.x, -bounds.y);
		clipx += bounds.x; clipy += bounds.y;
		g.setClip(prevClip.x,prevClip.y,prevClip.width,prevClip.height);
	}

	private void paintReverse(
			int clipx, int clipy, int clipwidth, int clipheight,
			Object component, boolean enabled) {
		if (component != null) {
			Rectangle bounds = getRectangle(component, "bounds");
			if (bounds!=null && ((clipx < bounds.x) ||
					(clipx + clipwidth > bounds.x + bounds.width) ||
					(clipy < bounds.y) ||
					(clipy + clipheight > bounds.y + bounds.height))) {
				paintReverse(clipx, clipy, clipwidth, clipheight,
					get(component, ":next"), enabled);
			}
			paint(clipx, clipy, clipwidth, clipheight, component, enabled);
		}
	}

	private void paintField(
			int clipx, int clipy, int clipwidth, int clipheight, Object component,
			int width, int height,
			boolean focus, boolean enabled, boolean hidden, int left) {
		boolean editable = getBoolean(component, "editable", true);
		paintRect(0, 0, width, height, enabled ? c_border : c_disable,
			editable ? getColor(component, "background", c_text_bg) : c_bg,
			true, true, true, true, true);
		/*
		if(!g.clipRect(1 + left, 1, width - left - 2, height - 2)){
			g.setClip(clipx, clipy, clipwidth, clipheight);//reset clip
			return;
		}
		 */
		String text = getString(component, "text", "");
		if(text==null){
			text="";
			
		}
		int offset = getInteger(component, ":offset", 0);
		Font currentfont = (Font) get(component, "font");
		if (currentfont != null) { g.setFont(currentfont); }
		currentfont=g.getFont();
		int caret = 0;
		int start = getInteger(component, "start", 0); 
		int end = getInteger(component, "end", 0);
		if(start>text.length())start=text.length();
		if(end>text.length())end=text.length();
		if (focus) { 
			caret = hidden ? (currentfont.charWidth('*') * end) :
				currentfont.stringWidth(text.substring(0, end));
			if (start != end) {
				int is = hidden ? (currentfont.charWidth('*') * start) :
					currentfont.stringWidth(text.substring(0, start));
				g.setColor(c_select_bg);
				
				g.fillRect(2*margin_1 + left - offset + Math.min(is, caret), margin_1,
					Math.abs(caret - is) + evm, height - 2*margin_1 + evm);
			}
		}

		if (focus) { // draw caret
			cursor_timer++;
			repaintNeeded=true;
			if(cursor_timer>=3*CURSOR_DELAY)cursor_timer=0;
			if(cursor_timer<1.8*CURSOR_DELAY){
				g.setColor(c_focus);
				g.fillRect(margin_1 + left - offset + caret, margin_1, margin_1 + evm, height - 2*margin_1 + evm);
			}
		}
		
		int fx = 2*margin_1 + left - offset;
		int fy = (height + currentfont.getAscent() - currentfont.getDescent()) / 2;
		
		g.setColor(enabled ? getColor(component, "foreground", c_text_fg) : c_disable);
		if (hidden) {
			int fh = currentfont.charWidth('*');
			for (int i = text.length(); i > 0; i--) {
				g.drawString("*", fx, fy);
				fx += fh;
			}
		} else {
			if(focus && start!=end){
				if(start>end){
					int temp=end;
					end=start;
					start=temp;
				}
				start=Math.min(start, end);
				end=Math.max(start, end);
				
				for (int i = 0;i<text.length(); i++) {
					if(i==start)g.setColor(c_select_fg);
					if(i==end)g.setColor(enabled ? getColor(component, "foreground", c_text_fg) : c_disable);
					int fh = currentfont.charWidth(text.charAt(i));
					g.drawString(text.charAt(i)+"", fx, fy);
					fx += fh;
				}
			}else{
				g.drawString(text, fx, fy);
			}
		}
		if (currentfont != null) { g.setFont(font); }
		//g.setClip(clipx, clipy, clipwidth, clipheight);

		if (focus) { // draw dotted rectangle
			drawFocus(1, 1, width - 2, height - 2);
		}
	}
	
		
	/**
	 * @param component scrollable widget
	 * @param classname
	 * @param pressed
	 * @param inside
	 * @param focus
	 * @param enabled
	 * @param g grahics context
	 * @param clipx current cliping x location relative to the component
	 * @param clipy y location of the cliping area relative to the component
	 * @param clipwidth width of the cliping area
	 * @param clipheight height of the cliping area
	 * @param header column height
	 * @param topborder bordered on the top if true
	 * @param border define left, bottom, and right border if true
	 */
	private void paintScroll(Object component, String classname, boolean pressed, boolean inside, boolean focus, boolean drawfocus,
			boolean enabled, int clipx, int clipy, int clipwidth, int clipheight) {
		Rectangle port = getRectangle(component, ":port");
		Rectangle horizontal = getRectangle(component, ":horizontal");
		Rectangle vertical = getRectangle(component, ":vertical");
		Rectangle view = getRectangle(component, ":view");

		Rectangle prevClip = g.getClipBounds();
		if (horizontal != null && horizontal.width > 10 && port.height > 0) { // paint horizontal scrollbar
			int x = horizontal.x;
			int y = horizontal.y+1;
			int width = horizontal.width;
			int height = horizontal.height-1;
			int block = Math.min(horizontal.width / 2, this.block);
			paintArrow(x, y, block, height, 'W', enabled, inside, pressed, "left", true, true, true, false, true);
			paintArrow(x + width - block, y, block, height, 'E', enabled, inside, pressed, "right", true, false, true, true, true);

			int track = width - (2 * block);
			if (track < 10) {
				paintRect(x + block, y, track, height, enabled ? c_border : c_disable, c_bg, true, true, true, true, true);
			} else {
				int knob = Math.max(track * port.width / view.width, 10);
				int decrease = view.x * (track - knob) / (view.width - port.width);
				paintRect(x + block, y, decrease, height, enabled ? c_border : c_disable, c_bg, false, true, true, false, true);
				paintRect(x + block + decrease, y, knob, height, enabled ? c_border : c_disable, enabled ? c_ctrl : c_bg, true, true, true,
						true, true);
				int n = Math.min(5, (knob - 4) / 3);
				g.setColor(enabled ? c_border : c_disable);
				int cx = (x + block + decrease) + (knob + 2 - n * 3) / 2;
				for (int i = 0; i < n; i++) {
					g.drawLine(cx + i * 3, y + 3, cx + i * 3, y + height - 5);
				}
				int increase = track - decrease - knob;
				paintRect(x + block + decrease + knob, y, increase, height, enabled ? c_border : c_disable, c_bg, false, false, true, true,
						true);
			}
		}

		if (vertical != null && port.width > 0 && vertical.height > 10) { // paint vertical scrollbar
			int x = vertical.x+1;
			int y = vertical.y;
			int width = vertical.width-1;
			int height = vertical.height;
			int block = Math.min(vertical.height / 2, this.block);
			paintArrow(x, y, width, block, 'N', enabled, inside, pressed, "up", true, true, false, true, false);
			paintArrow(x, y + height - block, width, block, 'S', enabled, inside, pressed, "down", false, true, true, true, false);

			int track = height - (2 * block);
			if (track < 10) {
				paintRect(x, y + block, width, track, enabled ? c_border : c_disable, c_bg, true, true, true, true, false);
			} else if (view.height > 0) {
				int knob = Math.max(track * port.height / view.height, 10);
				int decrease = view.y * (track - knob) / (view.height - port.height);
				paintRect(x, y + block, width, decrease, enabled ? c_border : c_disable, c_bg, true, false, false, true, false);
				paintRect(x, y + block + decrease, width, knob, enabled ? c_border : c_disable, enabled ? c_ctrl : c_bg, true, true, true,
						true, false);
				int n = Math.min(5, (knob - 4) / 3);
				g.setColor(enabled ? c_border : c_disable);
				int cy = (y + block + decrease) + (knob + 2 - n * 3) / 2;
				for (int i = 0; i < n; i++) {
					g.drawLine(x + 3, cy + i * 3, x + width - 5, cy + i * 3);
				}
				int increase = track - decrease - knob;
				paintRect(x, y + block + decrease + knob, width, increase, enabled ? c_border : c_disable, c_bg, true, false, true, true,
						false);
			}
		}

		boolean hneed = (horizontal != null);
		boolean vneed = (vertical != null);
		boolean border=getBoolean(component, "border", true);
		if (("panel" != classname) && ("dialog" != classname) ) {
			//draw border and paint background
			paintRect(port.x-1 , port.y-1 , port.width + (vneed ? 1 : 2), port.height + (hneed ? 1 :2), enabled ? c_border : c_disable,
				getColor(component, "background", c_text_bg), border, border, border&&!hneed, border&&!vneed, true); // TODO
			if(border){
				//g.setColor(c_border);
				//g.drawRect(port.x-2 , port.y-2 , port.width + (vneed ? 1 : 2)+2, port.height + (hneed ? 1 :2)+2); // TODO
			}
																						// color
			if ("table" == classname) {
				Object header = get(component, "header");
				if (header != null) {
					int[] columnwidths = (int[]) get(component, ":widths");
					Object column = get(header, ":comp");
					int x = 0;
					g.clipRect(0, 0, port.width + 2, port.y); // note 2 and
																// decrease clip
																// area...
					for (int i = 0; i < columnwidths.length; i++) {
						if (i != 0) {
							column = get(column, ":next");
						}
						boolean lastcolumn = (i == columnwidths.length - 1);
						int width = lastcolumn ? (view.width - x + 2) : columnwidths[i];

						paint(column, x - view.x, 0, width, port.y - 1, clipx, clipy, clipwidth, clipheight, true, true, false, lastcolumn,
								1, 1, 0, 0, false, enabled ? 'g' : 'd', "left", false, false, true);

						Object sort = get(column, "sort"); // "none", "ascent",
															// "descent"
						if (sort != null) {
							paintArrow(x - view.x + width - block, 0, block, port.y, (sort == "ascent") ? 'S' : 'N');
						}
						x += width;

					}
					g.setClip(prevClip.x, prevClip.y, prevClip.width, prevClip.height);

				}
			}
		}
		int x1 = Math.max(clipx, port.x);
		int x2 = Math.min(clipx + clipwidth, port.x + port.width);
		int y1 = Math.max(clipy, port.y);
		int y2 = Math.min(clipy + clipheight, port.y + port.height);
		if ((x2 > x1) && (y2 > y1)) {

			// if(g.clipRect(x1, y1, x2 - x1, y2 - y1)){
			g.clipRect(x1, y1, x2 - x1, y2 - y1);
			g.translate(port.x - view.x, port.y - view.y);
			// 0,0, view.width, view.height);
			paint(component, classname, focus, enabled, view.x - port.x + x1, view.y - port.y + y1, x2 - x1, y2 - y1, port.width,
					view.width);
			g.translate(view.x - port.x, view.y - port.y);
			// }

		}
		g.setClip(prevClip.x, prevClip.y, prevClip.width, prevClip.height);

		if (focus && drawfocus) { // draw dotted rectangle around the viewport
			drawFocus(port.x, port.y, port.width , port.height );
		}

	}
	
	/**
	 * Paint scrollable content
	 * @param component a panel
	 */
	private void paint(Object component,
			String classname, boolean focus, boolean enabled,
			 int clipx, int clipy, int clipwidth, int clipheight,
			int portwidth, int viewwidth) {
		if ("textarea" == classname) {
			AWTImage bgicon = getIcon(component, "bgimage", null);
			if (bgicon != null) {
				g.setColor(c_bgimgae_tint);
				g.drawImage(bgicon, 0, 0,null);
			}
			boolean editable=getBoolean(component,"editable");
			char[] chars = (char[]) get(component, ":text");
			int start = focus ? getInteger(component, "start", 0) : 0;
			int end = focus ? getInteger(component, "end", 0) : 0;
			int is = Math.min(start, end); int ie = Math.max(start, end);
			Font customfont = (Font) get(component, "font");
			if (customfont != null) { g.setFont(customfont); }
			customfont =g.getFont();
			
			int fontascent = customfont.getAscent(); int fontheight = customfont.getHeight();
			int ascent = 1;
			DrawStyle[] styles=(DrawStyle[]) getProperty(component,"drawstyle");
			Color textcolor = enabled ? getColor(component, "foreground", c_text_fg) : c_disable;
		
			for (int i = 0, j = 0; j <= chars.length; j++) {
				if ((j == chars.length) || (chars[j] == '\n')) {
					if (clipy + clipheight <= ascent) { break; } // the next lines are below paint rectangle
					if (true||clipy < ascent + fontheight) { // this line is not above painting area
						int h=fontheight;
						if(styles!=null){
							/*
							Font newfont=customfont;
							for(int k=i;k<j;k++){
								newfont=customfont;
								for(DrawStyle style:styles){
									if(style.font!=null && style.begin<=k && style.end>=k){
										newfont=style.font;
										break;
									}
								}
								h= Math.max(newfont.getHeight(),h);
							}
							*/
							h=getCharsHeight(chars,i,j-i,styles);
							if(clipy > ascent+h){
								ascent += h;
								i = j + 1;
								continue;
							}
							
							ascent+=h-fontheight;
							//fontheight=h;
						}
						
						
						g.setColor(textcolor);
						g.drawChars(chars, i, j - i, margin_1, ascent + fontascent);//,styles,false,c_select_fg,c_select_bg);
						if (focus && (is != ie) && (ie >= i) && (is <= j)) {
							int xs = (is < i) ? -1 : ((is > j) ? (viewwidth - margin_1) :
								getCharsWidth(chars, i, is - i,styles))+margin_1*3/2;
							int xe = ((j != -1) && (ie > j)) ? (viewwidth - margin_1) :
								getCharsWidth(chars, i, ie - i,styles);
							g.setColor(c_select_bg);
							g.fillRect( xs, ascent-(h-fontheight), xe - xs + evm, h + evm);
							g.setColor(c_select_fg);
							g.drawChars(chars, (is<i?i:is), (ie>j?j:ie)-(is<i?i:is), xs, ascent+ fontascent);//,styles,true,c_select_fg,c_select_bg);
						}
						
						if (editable && focus && (end >= i) && (end <= j)) {
							cursor_timer++;
							repaintNeeded=true;
							if(cursor_timer>=3*CURSOR_DELAY)cursor_timer=0;
							if(cursor_timer<1.8*CURSOR_DELAY){
								int caret = (styles==null?customfont.charsWidth(chars, i, end - i):getCharsWidth(chars, i, end - i,styles));
								g.setColor(textcolor);
								h=(styles==null?fontheight:getCharsHeight(chars,end>0?end-1:0,1,styles));
								g.fillRect(caret+margin_1, ascent-(h-fontheight), margin_1 + evm, h + evm);
							}
						}
					}
					ascent += fontheight;
					i = j + 1;

				}
			}
			if (customfont != null) { g.setFont(font); } //restore the default font
		}
		else if (":combolist" == classname) {
			Object lead = get(component, ":lead");
			Rectangle bounds = getRectangle(component, "bounds");

			for (Object choice = get(get(component, "combobox"), ":comp");
					choice != null; choice = get(choice, ":next")) {
				Rectangle r = getRectangle(choice, "bounds");
				if(r==null)continue;
				if (clipy + clipheight <= r.y) { break; }
				if (clipy >= r.y + r.height) { continue; }
				String itemclass=getComponentClass(choice);
				//Object selected = get(choice, "selected");
				boolean itemenabled=getBoolean(choice, "enabled", true);
				if (itemclass == "checkboxmenuitem") {
					boolean armed = (lead == choice);
					paint(choice, r.x, r.y, bounds.width - 2, r.height,
							clipx, clipy, clipwidth, clipheight, false, false, false, false,
							2*margin_1,  (block + 7*margin_1) , 2*margin_1, 4*margin_1, false,
									itemenabled ? (armed ? 's' : 't') : 'd', "left", true, false,true);
					boolean checked = getBoolean(choice, "selected", false);
					g.translate(r.x + 3*margin_1, r.y + 2*margin_1);
					g.setColor(itemenabled  ? c_border : c_disable);
					g.drawRect(margin_1, margin_1, block - 3*margin_1, block - 3*margin_1);
					if (checked) {
						g.setColor(itemenabled ? c_text_fg : c_disable);
						//g.fillRect(3, block - 9, 2 + evm, 6 + evm);
						//g.drawLine(3, block - 4, block - 4, 3);
						//g.drawLine(4, block - 4, block - 4, 4);
						//g.gl.glEnable(GL2.GL_LINE_SMOOTH);
						double lw=g.setLineWidth(1.5f);
						g.drawLine(2*margin_1, 3*block/4-4*margin_1, 2*margin_1+block/4, block-4);
						g.drawLine(2*margin_1+block/4, block-4*margin_1,block-5*margin_1,block/4);
						g.setLineWidth(lw);
						//g.gl.glDisable(GL2.GL_LINE_SMOOTH);

					}
					g.translate(-r.x - 3*margin_1, -r.y - 2*margin_1);
				}else if ("separator" == itemclass) {
					g.setColor(enabled ? c_border : c_disable);
					g.fillRect(r.x, r.y+1, bounds.width - 2 + evm, 1 + evm);
				}else{
					paint(choice, r.x, r.y, portwidth, r.height,
							clipx, clipy, clipwidth, clipheight,
							false, false, false, false, 2, 4, 2, 4, false,
							itemenabled ? ((lead == choice) ? 's' : 't') : 'd',
									"left", false, false,true);
				}
			}
		}
		else if (("panel" == classname) || ("dialog" == classname)) {
			AWTImage bgicon = getIcon(component, "bgimage", null);
			if (bgicon != null) {
				g.setColor(c_bgimgae_tint);
				g.drawImage(bgicon, 0, 0,null);
			}
			if(clipwidth>1&&clipheight>1){
				for (Object comp = get(component, ":comp");
					comp != null; comp = get(comp, ":next")) {
				paint(clipx, clipy, clipwidth, clipheight, comp, enabled);
				}
			}
		}
		else { //if (("list" == classname) || ("table" == classname) || ("tree" == classname))
			Object lead = get(component, ":lead");
			int[] columnwidths = ("table" == classname) ? ((int []) get(component, ":widths")) : null;
			boolean line = getBoolean(component, "line", true); int iline = line ? 1 : 0;
			boolean angle = ("tree" == classname) && getBoolean(component, "angle", false);
			for (Object item = get(component, ":comp"), next = null; item != null; item = next) {
				if (focus && (lead == null)) {
					set(component, ":lead", lead = item); // draw first item focused when lead is null
				}
				Rectangle r = getRectangle(item, "bounds");
				if(r==null) continue;
				if (clipy + clipheight <= r.y) { break; } // clip rectangle is above
				boolean subnode = false; boolean expanded = false;
				if ("tree" != classname) {
					next = get(item, ":next");
				}else {
					subnode = (next = get(item, ":comp")) != null;
					expanded = subnode && getBoolean(item, "expanded", true);
					if (!expanded) {
						for (Object node = item; (node != component) &&
							((next = get(node, ":next")) == null); node = getParent(node));
					}
				}
				if (clipy >= r.y + r.height + iline) {
					if (angle) { // TODO draw dashed line
						Object nodebelow = get(item, ":next");
						if (nodebelow != null) { // and the next node is below clipy
							g.setColor(c_bg); int x = r.x - block / 2;
							g.drawLine(x, r.y, x, getRectangle(nodebelow, "bounds").y);
						}
					}
					continue; // clip rectangle is below
				}
				
				//draw background of each item based on selection state
				Color background = (Color) get(component, "background");
				if(background==null)background=c_text_bg;
				boolean selected = getBoolean(item, "selected", false);
				
				paintRect(("tree" != classname) ? 0 : r.x, r.y,
					("tree" != classname) ? viewwidth : r.width, r.height, null,
					selected ? c_select_bg : background, false, false, false, false, true);
				if (focus && (lead == item)) { // focused
					drawFocus(("tree" != classname) ? 0 : r.x, r.y,
						(("tree" != classname) ? viewwidth : r.width) , r.height-1 );
				}
				if (line) {
					g.setColor(background.darker());//,0.6f);//c_bg);
					g.drawLine(0, r.y + r.height, viewwidth, r.y + r.height);
					g.setColor(c_text_bg);
				}
				//draw item text and icon
				if ("table" != classname) { // list or tree
					boolean itemenabled = enabled && getBoolean(item, "enabled", true);
					paint(item, r.x, r.y, viewwidth, r.height,
						clipx, clipy, clipwidth, clipheight, false, false, false, false,
						1, 3, 1, 3, false, itemenabled ? 'e' : 'd', "left", false, false, false);
					if ("tree" == classname) {
						int x = r.x - block / 2; int y = r.y + (r.height - 1) / 2;
						if (angle) {
							g.setColor(c_bg);
							g.drawLine(x, r.y, x, y); g.drawLine(x, y, r.x - margin_1, y);
							Object nodebelow = get(item, ":next");
							if (nodebelow != null) {
								g.drawLine(x, y, x, getRectangle(nodebelow, "bounds").y);
							}
						}
						if (subnode) {
							int dx=(int) (block*0.3);
							paintRect(x - dx, y - dx, 2*dx, 2*dx, itemenabled ? c_border : c_disable,
								itemenabled ? c_ctrl : c_bg, true, true, true, true, true);
							g.setColor(itemenabled ? c_text_fg : c_disable);
							dx=block/5;//2*margin_1+1;
							g.drawLine(x - dx, y, x + dx, y);
							if (!expanded) { g.drawLine(x, y -dx, x, y + dx); }
						}
					}
				}else { // table
					int i = 0; int x = 0;
					for (Object cell = get(item, ":comp"); cell != null; cell = get(cell, ":next")) {
						if (clipx + clipwidth <= x) { break; }
						//column width is defined by header calculated in layout, otherwise is 80
						int iwidth = 80;
						if ((columnwidths != null) && (columnwidths.length > i)) {
							iwidth = (i != columnwidths.length - 1) ?
								columnwidths[i] : Math.max(iwidth, viewwidth - x);
						}
						if (clipx < x + iwidth) {
							boolean cellenabled = enabled && getBoolean(cell, "enabled", true);
							paint(cell, r.x + x, r.y, iwidth, r.height - 1,
								clipx, clipy, clipwidth, clipheight, false, false, false, false,
								1, 1, 1, 1, false, cellenabled ? 'e' : 'd', "left", false, false,false);
						}
						i++; x += iwidth;
					}
				}
			}
		}
	}

	private void paintRect( int x, int y, int width, int height,
			Color border, Color bg,
			boolean top, boolean left, boolean bottom, boolean right, boolean horizontal) {
		if ((width <= 0) || (height <= 0)) return;

		if(bg!=null){
			if (bg == c_ctrl) {
				fill( x, y, width, height, horizontal,null);
			}else {
				g.setColor(bg);
				g.fillRect(x, y, width + evm, height + evm);
			}
		}
		g.setColor(border);
		if(top && bottom && left && right){
			g.drawRect(x, y, width, height);
		}else{
			if (top) {
				g.drawLine(x + width , y, x, y);
				//y++; height--; 
				if (height <= 0) return;
			}
			if (left) {
				g.drawLine(x, y, x, y + height );
				//x++; width--; 
				if (width <= 0) return;
			}
			if (bottom) {
				g.drawLine(x, y + height , x + width - 1, y + height );
				//height--; 
				if (height <= 0) return;
			}
			if (right) {
				g.drawLine(x + width , y + height , x + width , y);
				//width--; 
				if (width <= 0) return;
			}
		}
	}
	
	/**
	 * Fill the given rectangle with gradient
	 */
	private void fill( int x, int y, int width, int height, boolean horizontal,Color color) {
		Color c1=col_gradient1;
		Color c2=col_gradient2;
		if(color!=null){
			c1=color;//.brighter();
			c2=color.darker();
		}
		if (horizontal) {
			if (height > block) {
				g.setColor(c_bg);
				g.fillRect(x, y, width + evm, height - block + evm);
			}
			g.drawHGradient(c1, c2, x , y,
					 width + evm, height + evm);
		}
		else {
			if (width > block) {
				g.setColor(c_bg);
				g.fillRect(x, y, width - block + evm, height + evm);
			}
			g.drawVGradient(c1, c2, x, y,
					width + evm, height + evm);
		}
	}
	

	private void paintArrow( int x, int y, int width, int height,
			char dir, boolean enabled, boolean inside, boolean pressed, String part,
			boolean top, boolean left, boolean bottom, boolean right, boolean horizontal) {
		inside = inside && (insidepart == part);
		pressed = pressed && (pressedpart == part);
		paintRect(x, y, width, height, enabled ? c_border : c_disable,
			enabled ? ((inside != pressed) ? c_hover :
				(pressed ? c_press : c_ctrl)) : c_bg,
			top, left, bottom, right, horizontal);
		g.setColor(enabled ? c_text_fg : c_disable);
		paintArrow(x + (left ? 1 : 0), y + (top ? 1 : 0),
			width - (left ? 1 : 0) - (right ? 1 : 0), height - (top ? 1 : 0) - (bottom ? 1 : 0), dir);
	}

	private void paintArrow(
			int x, int y, int width, int height, char dir) {
		int cx = x + width / 2 - 2*margin_1;
		int cy = y + height / 2 - margin_1;
		int s=4*margin_1;
		for (int i = 0; i < s; i++) {
			if (dir == 'N') { // north
				g.drawLine(cx + margin_1 - i, cy + i, cx + margin_1 + i, cy + i);
			}
			else if (dir == 'W') { // west
				g.drawLine(cx + i, cy + margin_1 - i, cx + i, cy + margin_1 + i);
			}
			else if (dir == 'S') { // south
				g.drawLine(cx + margin_1 - i, cy + s - i, cx + margin_1 + i, cy + s - i);
			}
			else { // east
				g.drawLine(cx + s- i, cy + margin_1 - i, cx + s - i, cy + margin_1 + i);
			}
		}
	}
	
	/**
	 * Paint component's borders and background
	 * mode: selects background color or gradient or skips , based on component
	 */
	private void paint(Object component, int x, int y, int width, int height,
			 boolean top, boolean left, boolean bottom, boolean right,
			char mode,boolean fill) {
		if ((width <= 0) || (height <= 0)) { return; }
		//Color foreground = (Color) get(component, "foreground");
		//if(foreground==null)foreground=c_border;

		if(fill){
			Color background = (Color) get(component, "background");
			switch (mode) {
				case 'e': case 'l': case 'd': case 'g': case 'r': break;
				case 'b': case 'i': case 'x': if (background == null) { background = c_bg; } break;
				case 'h': background = (background != null) ? background.brighter() : c_hover; break;
				case 'p': background = (background != null) ? background.darker() : c_press; break;
				case 't': if (background == null) { background = c_text_bg; } break;
				case 's': background = c_select_bg; break;
				default: throw new IllegalArgumentException();
			}

			if (((mode == 'g') || (mode == 'r'))) {
				fill( x, y, width, height, true,background);
			}else if (background != null) {
				g.setColor(background);
				if (mode != 'x') { g.fillRect(x, y, width + evm, height + evm); } 
			}
		}
		boolean toggled = ("togglebutton" == getComponentClass(component)) && getBoolean(component, "selected", false);

		if(toggled){
			g.setColor(c_shadow);
			g.drawRect(x, y, width, height);
			//g.setColor(c_press);
			//g.drawRect(x+1, y+1, width-3, height-3);
		}else if(top && left && right &&bottom){
			g.setColor(((mode != 'd') && (mode != 'i')) ? c_border : c_disable);
			g.drawRect(x, y, width, height);
		}else if (top || left || bottom || right) { // draw border
			g.setColor(((mode != 'd') && (mode != 'i')) ? c_border : c_disable);
			if (top) {
				g.drawLine(x + width , y, x, y);
				//y++; height--; 
				if (height <= 0) { return; }
			}
			if (left) {
				g.drawLine(x, y, x, y + height );
				//x++; width--; 
				if (width <= 0) { return; }
			}
			if (bottom) {
				g.drawLine(x, y + height , x + width , y + height );
				//height--; 
				if (height <= 0) { return; }
			}
			if (right) {
				g.drawLine(x + width , y + height , x + width , y);
				//width--; 
				if (width <= 0) { return; }
			}
		}
		

	}

	/**
	 * Renders buttons on dialog title bar
	 * @param component
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param type 'c' =close button, 'm'=maximise ,'i'=iconify
	 * @param mode 'h' =hover, 'p'=pressed,'g'=gradient/normal
	 */
	private void paint(Object component, 
			int x, int y, int width, int height, char type,char mode) {
		paint(component, x, y, width, height,true, true, true, true, mode,true);
		g.setColor(c_icon_tint);
		if(mode=='p'){
			//x++;y++;
		}
		switch (type) {
			case 'c': // closable dialog button;
				int margin3=3*margin_1;
				int margin4=3*margin_1;
				g.drawLine(x + margin3, y + margin3+1, x + width - margin4-1, y + height - margin4);
				g.drawLine(x + margin3, y + margin3, x + width - margin4, y + height - margin4);
				g.drawLine(x + margin3+1, y + margin3, x + width - margin4, y + height - margin4-1);
				g.drawLine(x + width - margin4-1, y + margin3, x + margin3, y + height - margin4-1);
				g.drawLine(x + width - margin4, y + margin3, x + margin3, y + height - margin4);
				g.drawLine(x + width - margin4, y + margin3+1, x + margin3+1, y + height - margin4);
				/*
				g.drawLine(x + 3, y + 4, x + width - 5, y + height - 4);
				g.drawLine(x + 3, y + 3, x + width - 4, y + height - 4);
				g.drawLine(x + 4, y + 3, x + width - 4, y + height - 5);
				g.drawLine(x + width - 5, y + 3, x + 3, y + height - 5);
				g.drawLine(x + width - 4, y + 3, x + 3, y + height - 4);
				g.drawLine(x + width - 4, y + 4, x + 4, y + height - 4);
				*/
				break;
			case 'm': // maximizable dialog button
				g.drawRect(x + 3*margin_1, y + 3*margin_1, width - 7*margin_1, height - 7*margin_1);
				g.drawLine(x + 4*margin_1, y + 4*margin_1, x + width - 5*margin_1, y + 4*margin_1);
				break;
			case 'i': // iconifiable dialog button
				g.fillRect(x + 3*margin_1, y + height - 5*margin_1, width - 6*margin_1, 2*margin_1);
				break;
		}
	}

	/**
	 * Paint component icon and text (using default or custom font) and fills background
	 * @param mnemonic find mnemonic index and underline text
	 */
	private void paint(Object component, int x, int y, int width, int height,
			 int clipx, int clipy, int clipwidth, int clipheight,
			boolean top, boolean left, boolean bottom, boolean right,
			int toppadding, int leftpadding, int bottompadding, int rightpadding, boolean focus,
			char mode, String alignment, boolean mnemonic, boolean underline,boolean fill) {
		if ((width <= 1) || (height <= 1)) { return; }
		Rectangle prevClip=g.getClipBounds();
		paint(component, x, y, width, height,top, left, bottom, right, mode,fill);
		AWTImage icon = getIcon(component, "bgimage", null);
		if (icon != null && getComponentClass(component)!="panel" && getComponentClass(component)!="dialog") {
			boolean pressed=mode!='d'&&(mousepressed==component) && getComponentClass(component)=="button";
			
			if(pressed){
				g.setColor(Color.blendColors(c_bgimgae_tint, c_press, 0.6f));
			}else if(underline){
				g.setColor(Color.blendColors(c_bgimgae_tint, c_press, 0.75f));
			}else{
				g.setColor(c_bgimgae_tint);
			}
			if(pressed){
				g.drawImage(icon, x+1, y+1, null);
			}else{
				g.drawImage(icon, x, y,null);
			}
			
			paint(component, x, y, width, height,top, left, bottom, right, mode,false);
		}

		//if (top) { y++; height--; } if (left) { x++; width--; }
		//if (bottom) { height--; } if (right) { width--; }
		if ((width <= 0) || (height <= 0)) { return; }

		if (focus && icon==null) {
			drawFocus(x + 1, y + 1, width - 2, height - 2);
		}

		String text = getString(component, "text", null);
		icon = getIcon(component, "icon", null);
		if ((text == null) && (icon == null)) { return; }
	
		x += leftpadding; y += toppadding;
		width -= leftpadding + rightpadding; height -= toppadding + bottompadding;

		alignment = getString(component, "alignment", alignment);
		Font customfont = (text != null) ? (Font) get(component, "font") : null;
		if (customfont != null) { g.setFont(customfont); }
		customfont=g.getFont();

		int tw = 0, th = 0;
		int ta = 0;
		if (text != null) {
			tw = customfont.stringWidth(text);
			ta = customfont.getAscent();
			th = customfont.getDescent() + ta;
		}
		int iw = 0, ih = 0;
		if (icon != null) {
			iw = icon.getScaledWidth();
			ih = icon.getScaledHeight();
			if (text != null) { iw += 3*margin_1; }
		}

		boolean pressed= mode!='d'&& (mousepressed==component)&&getComponentClass(component)=="button" ||getComponentClass(component)=="togglebutton" ;
		if(pressed){
			x++;
			y++;
		}
		boolean clipped = (tw + iw > width) || (th > height) || (ih > height);
		int cx = x;
		if ("center" == alignment) { cx += (width - tw - iw) / 2; }
			else if ("right" == alignment) { cx += width - tw - iw; }

		if (clipped){

			g.clipRect(x, y, width, height);
			//g.setClip(clipx, clipy, clipwidth, clipheight);//reset clip
			//return;
		}
		if (mode == 'x') { g.drawLine(cx, y + height / 2, cx + iw + tw, y + height / 2); }

		if (icon != null) {
			Color fg=(Color) get(component, "foreground");
			if(fg==null)fg=c_icon_tint;
			if(mode=='d') fg=c_disable;
			if(mode=='s')fg=c_select_fg;
			//if(icon.iconFont==null)fg=null;
			g.drawImage(icon, cx, y + (height - ih) / 2,fg);
			cx += iw;
		}
		if (text != null) { 
			Color foreground = (Color) get(component, "foreground");
			if (foreground == null) {
				foreground = (mode == 'l') ? c_fg :
					(((mode != 'd') && (mode != 'r')) ? c_text_fg : c_disable);
			}
			if(mode=='s')foreground=c_select_fg;
			g.setColor(foreground);
			int ty = y + (height - th) / 2 + ta-margin_1;
			g.drawString(text, cx, ty);
			if (mnemonic) {
				int imnemonic = getInteger(component, "mnemonic", -1);
				if ((imnemonic != -1) && (imnemonic < text.length())) {
					int mx = cx + customfont.stringWidth(text.substring(0, imnemonic));
					g.drawLine(mx, ty + margin_1, mx + customfont.charWidth(text.charAt(imnemonic)), ty + margin_1);
				}
			}
			if (underline) { // for link button
				g.drawLine(cx, ty + margin_1, cx + tw, ty + margin_1);
			}
		}
		if (clipped) { g.setClip(prevClip.x, prevClip.y, prevClip.width, prevClip.height); }
		
		if (customfont != null) { g.setFont(font); } //restore the default font
	}
	
	private void drawFocus( int x, int y, int width, int height) {
		g.setColor(c_focus);
		g.drawDottedRect(x, y, width, height);
		/*
		//x--;
		int x2 = x  - height % 2;
		for (int i = 0; i <= width; i += 2) {
			g.fillRect(x + i, y, 1, 1); g.fillRect(x2 + i, y + height, 1, 1);
		}
		int y2 = y - width % 2;
		for (int i = 2; i <= height; i += 2) {
			g.fillRect(x, y + i, 1, 1); g.fillRect(x + width, y2 + i, 1, 1);
		}
		*/
	}



	
	/**
	 * This component can be traversed using Tab or Shift-Tab keyboard focus
	 * traversal, although 1.4 replaced this method by <i>isFocusable</i>, so
	 * 1.4 compilers write deprecation warning
	 *
	 * @return true as focus-transverable component, overwrites the default
	 *         false value
	 */
	public boolean isFocusTraversable() {
		return true;
	}
	
	
	private Vector2 flingeVel=new Vector2();
	private double damping=0.8;
	
	
	/**
	 * 
	 * @param x x coordinate of mouse in window frame
	 * @param y y coordinate of mouse in window frame
	 * @param id AWT event id  @see {java.awt.event.MouseEvent}
	 * @param button 0==none, 1=left, 2=middle, 3=right
	 * @param clickcount number of clicks
	 * @param modifiers shift modifiers
	 */
	public boolean handleMouse(int x, int y,int id,int button, int clickcount,int modifiers){
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(e+"");
		boolean consumed=false;
		boolean isPopUpActive=popupowner!=null;
		/*
		if(button==0 && id==AWTMouseEvent.MOUSE_DRAGGED) {
			id=AWTMouseEvent.MOUSE_MOVED;
		}
		*/
		if ((id == AWTMouseEvent.MOUSE_ENTERED || (id == AWTMouseEvent.MOUSE_MOVED))
				|| (id == AWTMouseEvent.MOUSE_EXITED)
				|| (id == AWTMouseEvent.MOUSE_PRESSED)
				|| (id == AWTMouseEvent.MOUSE_DRAGGED)
				|| (id == AWTMouseEvent.MOUSE_RELEASED)||(id == AWTMouseEvent.MOUSE_CLICKED)) {
			boolean controldown=(modifiers & (InputEvent.CTRL_MASK|InputEvent.META_MASK)) != 0;
			boolean shiftdown=(modifiers & InputEvent.SHIFT_MASK) != 0;
			boolean popuptrigger=((button==AWTMouseEvent.BUTTON3) && (id==AWTMouseEvent.MOUSE_CLICKED ||id==AWTMouseEvent.MOUSE_RELEASED));
			if (id == AWTMouseEvent.MOUSE_ENTERED) {
				if (mousepressed == null) {
					findComponent(content, x, y);
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_ENTERED,
							mouseinside, insidepart);
					consumed=true;
				}
			} else if (id == AWTMouseEvent.MOUSE_MOVED) {
				Object previnside = mouseinside;
				Object prevpart = insidepart;
				findComponent(content, x, y);
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(" mousemoved= "+getClass(mouseinside));

				if ((previnside == mouseinside) && (prevpart == insidepart)) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_MOVED, mouseinside,
							insidepart);
				} else {
					handleMouseEvent(x, y, button,clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_EXITED, previnside,
							prevpart);
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_ENTERED,
							mouseinside, insidepart);
				}
				consumed=(mouseinside!=null);
				//consumed=consumed;
			} else if (id == AWTMouseEvent.MOUSE_EXITED) {
				if (mousepressed == null) {
					Object mouseexit = mouseinside;
					Object exitpart = insidepart;
					mouseinside = insidepart = null;
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_EXITED, mouseexit,
							exitpart);
					consumed=true;
				}
			} else if (id == AWTMouseEvent.MOUSE_PRESSED ) {
				findComponent(content, x, y);
				//bring dialog to front if one of its child gets focus
				if(mousepressed!=mouseinside && mouseinside!=null && mouseinside!=content){
					Object dialog=getParentDialog(mouseinside);
					if(dialog!=null){
						Object parent = getParent(dialog);
						if (get(parent, ":comp") != dialog) { // to front
							removeItemImpl(parent, dialog);
							insertItem(parent, ":comp", dialog, 0);
							set(dialog, ":parent", parent);
							repaint(dialog); // to front always...
							//setNextFocusable(component, false);
						}
					}

				}
				hideTip(); // remove tooltip
				//System.out.println("Press: Mousepressed= "+getClass(mouseinside)+" btn ="+button);
				mousepressed = mouseinside;
				pressedpart = insidepart;
				if(mousepressed!=null && mousepressed!=content){
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
						popuptrigger, AWTMouseEvent.MOUSE_PRESSED, mousepressed,
						pressedpart);
					
					//if(focusowner==null)
					if(focusowner!=this)setFocus(mousepressed);
				}else{
					if(insidepart!="modal")setFocus(null);
				}
			} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
				hideTip(); // remove tooltip
				Object previnside = mouseinside;
				Object prevpart = insidepart;
				findComponent(content, x, y);
				//System.out.println("Mouse Drragged on "+toString(mousepressed));

				boolean same = (previnside == mouseinside)
						&& (prevpart == insidepart);
				boolean isin = (mousepressed == mouseinside)
						&& (pressedpart == insidepart);
				boolean wasin = (mousepressed == previnside)
						&& (pressedpart == prevpart);
				//System.out.println("Drag: Mousepressed= "+getClass(mouseinside)+" clickcount ="+clickcount);
				if (mousepressed!=null) {//==true) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_DRAGGED,
							mousepressed, pressedpart);
				}else if (wasin && !isin) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_EXITED,
							mousepressed, pressedpart);
				} else if (!same && (popupowner != null) && !wasin) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, DRAG_EXITED, previnside, prevpart);
				}
				if (isin && !wasin) {
					handleMouseEvent(x, y, button,clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_ENTERED,
							mousepressed, pressedpart);
				} else if (!same && (popupowner != null) && !isin) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, DRAG_ENTERED, mouseinside, insidepart);
				}
				
				if (isin == wasin==true) {
					handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_DRAGGED,
							mousepressed, pressedpart);
				}
			} else if (id == AWTMouseEvent.MOUSE_RELEASED ||id == AWTMouseEvent.MOUSE_CLICKED) {
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("id="+id+" pop= "+isPopUpActive);
				if (popupowner != null) { // remove popup
					String classname = getComponentClass(mouseinside);
					if ((popupowner != mouseinside) && (classname != ":popup")
							&& (classname != ":combolist")) {	
						closeup();
						return true;
						//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(" close up1");
					}
				}

				hideTip(); // remove tooltip
				Object mouserelease = mousepressed;
				Object releasepart = pressedpart;
				mousepressed = pressedpart = null;
				findComponent(content, x, y);
				if(clickcount==1){
					if(mouserelease==null||mouserelease==getDesktop()){
						mouserelease=mouseinside;
						releasepart=insidepart;
					}
					id = AWTMouseEvent.MOUSE_RELEASED;
				}
				if(clickcount>1 && mouseinside != null){
					handleMouseEvent(x, y, button,clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_PRESSED, mouserelease,
							releasepart);
				}else {
					handleMouseEvent(x, y, button,clickcount, shiftdown, controldown,
							popuptrigger, id, mouserelease,
							releasepart);
					if ((mouseinside != null)&& ((mouserelease != mouseinside) || (releasepart != insidepart))) {
						handleMouseEvent(x, y,button, clickcount, shiftdown, controldown,
							popuptrigger, AWTMouseEvent.MOUSE_ENTERED,
							mouseinside, insidepart);
						consumed=true;
					}
				}
			}
		} 
		consumed=(insidepart=="modal" || mousepressed!=null);
		if(mouseinside==content)consumed=false;
		if(consumed||isPopUpActive)repaintNeeded=true;
		return consumed||isPopUpActive;
	}
	
	/**
	 * 
	 * @param x mouse-x in canvas frame
	 * @param y mouse-y in canvas frame
	 * @param rotation
	 * @return
	 */
	public boolean handleMouseWheel(int x, int y,int rotation){
		if(mouseinside==null)return false;
		boolean handled=false;
		Rectangle port = getRectangle(mouseinside, ":port");
		if (port != null) { // is scrollable
			// TODO hide tooltip?
			Rectangle bounds = getRectangle(mouseinside, "bounds");

			if (port.x + port.width < bounds.width) { // has vertical
															// scrollbar
				handled=processScroll(mouseinside, (rotation > 0) ? "down"
							: "up"); // TODO scroll panels too
			} else if (port.y + port.height < bounds.height) { // has
																	// horizontal
																	// scrollbar
				handled=processScroll(mouseinside, (rotation > 0) ? "right"
							: "left");
			}
			return handled;
		}else if (getComponentClass(mouseinside)=="bean") {
			CustomComponent bean = (CustomComponent) get(mouseinside, "bean");
			Vector2 v=new Vector2(x,y);
			getScreenToLocal(mouseinside,v);
			return bean.handleMouseWheel((int)v.x,(int)v.y,rotation);
		}else{
			if(getComponentClass(mouseinside)=="slider"){
				double minimum = getDouble(mouseinside, "minimum");
				double maximum = getDouble(mouseinside, "maximum");
				double value = getDouble(mouseinside, "value");
				double step = getDouble(mouseinside, "unit");
				double newvalue = value+(rotation>0?step:-step);
				newvalue = Math.max(minimum, Math.min(newvalue, maximum));
				if (value != newvalue) {
					setDouble(mouseinside, "value", newvalue);
					invoke(mouseinside, null, "action");
					//if(id == AWTMouseEvent.MOUSE_PRESSED)repaint(component);
				}
			}else if(getComponentClass(mouseinside)=="spinbox"){
				String text = getString(mouseinside, "text", "");
				/*
				try {
					double itext = Double.parseDouble(text);
					double step = getDouble(mouseinside, "step");
					
					if (rotation>0 ? (itext + step <= getDouble(mouseinside,
							"maximum", Double.MAX_VALUE))
							: (itext - step >= getDouble(mouseinside, "minimum",
									Double.MIN_VALUE))) {
						itext=(rotation>0 ) ? (itext + step)
								: (itext - step);
						itext=MathUtils.roundOffToPreferredSigFigures(itext);
						String value = String.valueOf(itext);
						setDouble(mouseinside,"value",itext);
						setString(mouseinside, "text", value, null);
						setInteger(mouseinside, "start", value.length(), 0);
						setInteger(mouseinside, "end", 0, 0);
						repaint(mouseinside, "spinbox", "text");
						invoke(mouseinside, null, "action");
						return true;
					}
				} catch (NumberFormatException nfe) {
				}
				*/
				return processSpin(mouseinside,rotation>0 ?"up":"down");
				//return false;
			}
		}
		return false;
	}
	
	public boolean handleKey(int id, int keycode, String keycharS, int modifiers,
			boolean actionkey) {
		if ((popupowner == null) && (focusowner == null)) return false;
		boolean consumed = false;

		boolean controldown =  (modifiers & (InputEvent.CTRL_MASK|InputEvent.META_MASK)) != 0;
		boolean shiftdown =  (modifiers & InputEvent.SHIFT_MASK) != 0;
		boolean altdown =  (modifiers & InputEvent.ALT_MASK) != 0;

		hideTip(); // remove tooltip
		
		int keychar=keycharS.length()!=1?0:keycharS.charAt(0);
		boolean control = (keychar <= 0x1f)
				|| ((keychar >= 0x7f) && (keychar <= 0x9f))
				|| (keychar >= 0xffff) || controldown;
		// control=false;
		int tempKeyCode = control ? keycode : 0;
		if("bean"==getComponentClass(focusowner))tempKeyCode=keycode; //added on 20-6-19
		if (id != AWTKeyEvent.KEY_RELEASED
				&& (control == (id == AWTKeyEvent.KEY_PRESSED))
				& processKeyPress((popupowner != null) ? popupowner
						: focusowner, shiftdown, controldown, modifiers,
						control ? 0 : keychar, tempKeyCode)) {
			consumed = true;
		} else if ((tempKeyCode == AWTKeyEvent.VK_TAB)
				|| ((tempKeyCode == AWTKeyEvent.VK_F6) && (altdown || controldown))) {
			boolean outgo = true;// (keycode == AWTKeyEvent.VK_F6);
			if (!shiftdown ? setNextFocusable(focusowner, outgo)
					: setPreviousFocusable(focusowner, outgo)) {
				consumed = true;
			}
			repaint(focusowner);
			closeup();
		} else if (tempKeyCode == AWTKeyEvent.VK_F8) {
			for (Object splitpane = focusowner; splitpane != null; splitpane = getParent(splitpane)) {
				if (getComponentClass(splitpane) == "splitpane") {
					setFocus(splitpane);
					repaint(splitpane);
					consumed = true;
					break; // middle
				}
			}
		} else if ((id == AWTKeyEvent.KEY_PRESSED)
				&& ((keychar != 0) || actionkey)
				&& checkMnemonic(focusowner, true, null, keycode, modifiers)) {
			consumed = true;
		} else if(id==AWTKeyEvent.KEY_RELEASED && focusowner!=null &&"bean"==getComponentClass(focusowner)){
			CustomComponent bean = (CustomComponent) get(focusowner, "bean");
			bean.handleKeyEvent(keychar, keycode,AWTKeyEvent.KEY_RELEASED, shiftdown, controldown, modifiers);
		}
		//else if(id==AWTKeyEvent.KEY_TYPED && focusowner!=null &&"bean"==getClass(focusowner)){
		//	CustomComponent bean = (CustomComponent) get(focusowner, ");
		//	bean.handleKey(keychar, keycode,AWTKeyEvent.KEY_TYPED, shiftdown, controldown, modifiers);
		//}

		return consumed;
	}
	


	/**
	 * Check the previous mouse location again because of a possible layout
	 * change
	 */
	private void checkLocation() {
		findComponent(content, mousex, mousey);
		handleMouseEvent(mousex, mousex, 0,1, false, false, false,
				AWTMouseEvent.MOUSE_ENTERED, mouseinside, insidepart);
	}

	/**
	 *
	 */
	private boolean processKeyPress(Object component, boolean shiftdown,
			boolean controldown, int modifiers, int keychar, int keycode) {
		String classname = getComponentClass(component);

		if ("combobox" == classname) {
			Object combolist = get(component, ":combolist");
			if (combolist == null) { // the drop down list is not visible
				boolean editable = getBoolean(component, "editable", true);
				if (editable
						&& processField(component, shiftdown, controldown,
								modifiers, keychar, keycode, false, false,
								false)) {
					setInteger(component, "selected", -1, -1);
					return true;
				}
				if ((keychar == AWTKeyEvent.VK_SPACE)
						|| (keycode == AWTKeyEvent.VK_DOWN)) {
					popupCombo(component);
				}
			} else {
				if ((keycode == AWTKeyEvent.VK_UP)
						|| (keycode == AWTKeyEvent.VK_DOWN)
						|| (keycode == AWTKeyEvent.VK_PAGE_UP)
						|| (keycode == AWTKeyEvent.VK_PAGE_DOWN)
						|| (keycode == AWTKeyEvent.VK_HOME)
						|| (keycode == AWTKeyEvent.VK_END)) {
					Object next = getListItem(component, combolist, keycode,
							get(combolist, ":lead"), false);
					if (next != null) {
						setInside(combolist, next, true);
					}
					return (true);
				} else if ((keycode == AWTKeyEvent.VK_ENTER)
						|| (keychar == AWTKeyEvent.VK_SPACE)) {
					closeCombo(component, combolist, get(combolist, ":lead")); // Alt+Up
					return (true);
				} else if (keycode == AWTKeyEvent.VK_ESCAPE) {
					closeCombo(component, combolist, null);
					return (true);
				} else if (!processField(component, shiftdown, controldown,
						modifiers, keychar, keycode, false, false, false)) {
					Object item = findText((char) keychar, component,
							combolist, false);
					if (item != null) {
						setInside(combolist, item, true);
					} else
						return false;
				}
			}
		}

		Object parentDialog = null;
		Object t = component;
		do {
			parentDialog = t;
		} while ("dialog" != getComponentClass(t) && (t = getParent(t)) != null);
		if (parentDialog != null && "dialog" == getComponentClass(parentDialog)) {
			if (keycode == AWTKeyEvent.VK_ESCAPE)
				if (invoke(parentDialog, null, "close"))
					return (true);
		}
		
		if ("bean" == classname) {
			CustomComponent bean = (CustomComponent) get(component, "bean");
			bean.handleKeyEvent(keychar, keycode,AWTKeyEvent.KEY_PRESSED, shiftdown, controldown, modifiers);
		}else if ("button" == classname) {
			if (keychar == AWTKeyEvent.VK_SPACE
					|| ((keycode == AWTKeyEvent.VK_ENTER) && (get(component,
							"type") == "default"))
					|| ((keycode == AWTKeyEvent.VK_ESCAPE) && // ...
					(get(component, "type") == "cancel"))) {
				// pressedkey = keychar;
				invoke(component, null, "action");
				repaint(component);
				return true;
			}
		} else if (("checkbox" == classname) || ("togglebutton" == classname)) {
			if (keychar == AWTKeyEvent.VK_SPACE) {
				changeCheck(component, true);
				repaint(component);
				return true;
			}
		} else if (("textfield" == classname) || ("passwordfield" == classname)) {
			return processField(component, shiftdown, controldown, modifiers,
					keychar, keycode, false, ("passwordfield" == classname),
					false);
		} else if ("textarea" == classname) {
			char[] chars = (char[]) get(component, ":text");
			int start = getInteger(component, "start", 0);
			int end = getInteger(component, "end", 0);

			int istart = start;
			int iend = end;
			String insert = null;
			if( controldown && (keycode == AWTKeyEvent.VK_S||keycode == AWTKeyEvent.VK_Q||keycode == AWTKeyEvent.VK_W||keycode == AWTKeyEvent.VK_L||keycode == AWTKeyEvent.VK_B ||keycode == AWTKeyEvent.VK_I||keycode == AWTKeyEvent.VK_N||keycode == AWTKeyEvent.VK_U||keycode == AWTKeyEvent.VK_E||keycode == AWTKeyEvent.VK_R)){
				char ch=(keycode == AWTKeyEvent.VK_B?'b':(keycode == AWTKeyEvent.VK_I?'i':'0'));
				if(keycode == AWTKeyEvent.VK_L)ch='l';
				if(keycode == AWTKeyEvent.VK_W)ch='w';
				if(keycode == AWTKeyEvent.VK_Q)ch='q';
				if(keycode == AWTKeyEvent.VK_S)ch='s';
				if(keycode == AWTKeyEvent.VK_E)ch='e';
				if(keycode == AWTKeyEvent.VK_R)ch='r';
				if(keycode == AWTKeyEvent.VK_U)ch='u';
				updateDrawSytle(component,null,null,(char) ch);
			} else if ((keycode == AWTKeyEvent.VK_HOME) && !controldown) {
				while ((iend > 0) && (chars[iend - 1] != '\n')) {
					iend--;
				}
				if (!shiftdown) {
					istart = iend;
				}
			} else if ((keycode == AWTKeyEvent.VK_END) && !controldown) {
				while ((iend < chars.length) && (chars[iend] != '\n')) {
					iend++;
				}
				if (!shiftdown) {
					istart = iend;
				}
			} else if ((keycode == AWTKeyEvent.VK_UP)
					|| (keycode == AWTKeyEvent.VK_PAGE_UP)
					|| (keycode == AWTKeyEvent.VK_DOWN)
					|| (keycode == AWTKeyEvent.VK_PAGE_DOWN)) {
				Font currentfont = getFont(component);
				
				DrawStyle[] styles=(DrawStyle[]) getProperty(component,"drawstyle");
				int fh = currentfont.getHeight();
				int y = 0; //current caret location in pixels
				int linestart = 0;
				for (int i = 0; i < end; i++) {
					if ((chars[i] == '\n') || (chars[i] == '\t')) {
						if(styles!=null){
							fh=getCharsHeight(chars,linestart,i-linestart,styles);
						}
						linestart = i + 1;
						y+=fh;
					}
				}
				/*

				for (int i = 0; i < iend; i++) {
					if ((chars[i] == '\n') || (chars[i] == '\t')) {
						linestart = i + 1;
						y += fh;
					}
				}
				*/
				if (keycode == AWTKeyEvent.VK_UP) {
					//increase y by height of current line
					y -= fh;
				} else if (keycode == AWTKeyEvent.VK_DOWN) {
					//increase y by height of next line
					for (int i = linestart; i < chars.length; i++) {
						if ((chars[i] == '\n') || (chars[i] == '\t')) {
							if(styles!=null)fh=getCharsHeight(chars,linestart,i-linestart,styles);
							y+=fh;
							break;
						}
					}
				} else {
					int dy = getRectangle(component, ":port").height;
					y += (keycode == AWTKeyEvent.VK_PAGE_UP) ? -dy : dy; // VK_PAGE_DOWN
				}
				int x = (styles==null)?currentfont.charsWidth(chars, linestart, iend - linestart):getCharsWidth(chars, linestart, iend - linestart,styles);
				iend = getCaretLocation(component, x, y, true, false);
				if (!shiftdown) {
					istart = iend;
				}
			} else
				return processField(component, shiftdown, controldown,
						modifiers, keychar, keycode, true, false, false);
			return changeField(component, getString(component, "text", ""),
					insert, istart, iend, start, end);
		} else if ("tabbedpane" == classname) {
			if ((keycode == AWTKeyEvent.VK_RIGHT) || (keycode == AWTKeyEvent.VK_DOWN)
					|| (keycode == AWTKeyEvent.VK_LEFT)
					|| (keycode == AWTKeyEvent.VK_UP)) {
				String placement = getString(component, "placement", "top");
				if(placement == "none") return false;
				int selected = getInteger(component, "selected", 0);
				boolean increase = (keycode == AWTKeyEvent.VK_RIGHT)
						|| (keycode == AWTKeyEvent.VK_DOWN);
				int newvalue = selected;
				int n = increase ? getItemCountImpl(component, ":comp") : 0;
				int d = (increase ? 1 : -1);
				for (int i = selected + d; increase ? (i < n) : (i >= 0); i += d) {
					if (getBoolean(getItem(component, i), "enabled", true)) {
						newvalue = i;
						break;
					}
				}
				if (newvalue != selected) {
					setInteger(component, "selected", newvalue, 0);
					checkOffset(component);
					repaint(component);
					invoke(component, getItem(component, newvalue), "action");
				}
			}
		} else if ("spinbox" == classname) {
			if ((keycode == AWTKeyEvent.VK_UP) || (keycode == AWTKeyEvent.VK_DOWN)) {
				processSpin(component, (keycode == AWTKeyEvent.VK_UP) ? "up"
						: "down");
				return true;
			}else if(keycode==AWTKeyEvent.VK_ENTER){
				double value = getDouble(component, "value");
				String text=getString(component,"text");
				double v=value;
				if(text==null)text="";
				try{
					v=Double.parseDouble(text);
					double minimum = getDouble(component, "minimum");
					double maximum = getDouble(component, "maximum");
					double step= getDouble(component, "step");
					v=MathUtils.clamp(v, minimum, maximum);
					v=Math.floor((v-minimum)/step)*step+minimum;
					text=v+"";
				}catch(Exception e){
				}
				text=v+"";
				setString(component,"text",text);
				setInteger(component, "start", text.length());
				setInteger(component, "end", text.length());
				setInteger(component, "caret", text.length());
				if(v!=value){
					setDouble(component,"value",v);
					return invoke(component, null, "action");
				}
				return true;
			}else{
				return processField(component, shiftdown, controldown, modifiers,
					keychar, keycode, false, false, true);
			}
		
		} else if ("slider" == classname) {
			double value = getDouble(component, "value");
			double d = 0;
			if ((keycode == AWTKeyEvent.VK_HOME) || (keycode == AWTKeyEvent.VK_LEFT)
					|| (keycode == AWTKeyEvent.VK_UP)
					|| (keycode == AWTKeyEvent.VK_PAGE_UP)) {
				d = getDouble(component, "minimum") - value;
				if ((keycode == AWTKeyEvent.VK_LEFT)
						|| (keycode == AWTKeyEvent.VK_UP)) {
					d = Math.max(d, -getDouble(component, "unit"));
				} else if (keycode == AWTKeyEvent.VK_PAGE_UP) {
					d = Math.max(d, -getDouble(component, "block"));
				}
			} else if ((keycode == AWTKeyEvent.VK_END)
					|| (keycode == AWTKeyEvent.VK_RIGHT)
					|| (keycode == AWTKeyEvent.VK_DOWN)
					|| (keycode == AWTKeyEvent.VK_PAGE_DOWN)) {
				d = getDouble(component, "maximum") - value;
				if ((keycode == AWTKeyEvent.VK_RIGHT)
						|| (keycode == AWTKeyEvent.VK_DOWN)) {
					d = Math.min(d, getDouble(component, "unit"));
				} else if (keycode == AWTKeyEvent.VK_PAGE_DOWN) {
					d = Math.min(d, getDouble(component, "block"));
				}
			}
			if (d != 0) {
				setDouble(component, "value", value + d);
				repaint(component);
				invoke(component, null, "action");
			}
		} else if ("splitpane" == classname) {
			int divider = getInteger(component, "divider", -1);
			int d = 0;
			if (keycode == AWTKeyEvent.VK_HOME) {
				d = -divider;
			} else if ((keycode == AWTKeyEvent.VK_LEFT)
					|| (keycode == AWTKeyEvent.VK_UP)) {
				d = Math.max(-10, -divider);
			} else if ((keycode == AWTKeyEvent.VK_END)
					|| (keycode == AWTKeyEvent.VK_RIGHT)
					|| (keycode == AWTKeyEvent.VK_DOWN)) {
				boolean horizontal = ("vertical" != get(component,
						"orientation"));
				Rectangle bounds = getRectangle(component, "bounds");
				int max = (horizontal ? bounds.width : bounds.height) - 5*margin_1;
				d = max - divider;
				if (keycode != AWTKeyEvent.VK_END) {
					d = Math.min(d, 10*margin_1);
				}
			}
			if (d != 0) {
				setInteger(component, "divider", divider + d, -1);
				validate(component);
			}
		} else if (("list" == classname) || ("table" == classname)) {
			return processList(component, shiftdown, controldown, keychar,
					keycode, false);
		} else if ("tree" == classname) {
			// ? clear childs' selection, select this is its subnode was
			// selected
			if (keycode == AWTKeyEvent.VK_LEFT) {
				Object lead = get(component, ":lead");
				if ((get(lead, ":comp") != null)
						&& getBoolean(lead, "expanded", true)) { // collapse
					setBoolean(lead, "expanded", false, true);
					selectItem(component, lead, true);
					validate(component);
					invoke(component, lead, "collapse"); // lead
					return true;
				} else { // select parent
					Object parent = getParent(lead);
					if (parent != component) {
						selectItem(component, parent, true);
						setLead(component, lead, parent);
						return true;
					}
				}
			}
			// ? for interval mode select its all subnode or deselect all after
			else if (keycode == AWTKeyEvent.VK_RIGHT) {
				Object lead = get(component, ":lead");
				Object node = get(lead, ":comp");
				if (node != null) {
					if (getBoolean(lead, "expanded", true)) { // select its
																// first subnode
						selectItem(component, node, true);
						setLead(component, lead, node);
					} else { // expand
						setBoolean(lead, "expanded", true, true);
						selectItem(component, lead, true);
						validate(component);
						invoke(component, lead, "expand"); // lead
					}
					return true;
				}
			}
			return processList(component, shiftdown, controldown, keychar,
					keycode, true);
		} else if (("menubar" == classname) || ("popupmenu" == classname)) {
			// find the last open :popup and the previous one
			Object previous = null;
			Object last = null;
			for (Object i = get(component, ":popup"); i != null; i = get(i,
					":popup")) {
				previous = last;
				last = i;
			}
			// selected is the current item of the last, or the previous :popup,
			// or null
			Object selected = get(last, "selected");
			Object hotpopup = ((selected != null) || (previous == null)) ? last
					: previous;
			if ((selected == null) && (previous != null)) {
				selected = get(previous, "selected");
			}

			if ((keycode == AWTKeyEvent.VK_UP) || (keycode == AWTKeyEvent.VK_DOWN)) {
				Object next = getMenu(hotpopup, selected,
						keycode == AWTKeyEvent.VK_DOWN, true);
				if (next != null) {
					set(hotpopup, "selected", null);
					popupMenu(hotpopup);
					set(hotpopup, "selected", next);
					repaint(hotpopup);
				}
			} else if (keycode == AWTKeyEvent.VK_LEFT) {
				if (previous != null) { // close the last :popup
					selected = get(previous, "selected");
					set(previous, "selected", null);
					popupMenu(previous);
					set(previous, "selected", selected);
					repaint(previous); // , selected
				} else if ("menubar" == classname) { // select the previous
														// menubar menu
					Object next = getMenu(component,
							get(component, "selected"), false, false);
					if (next != null) {
						set(component, "selected", next);
						Object popup = popupMenu(component);
						set(popup, "selected", getMenu(popup, null, true, true));
						repaint(component); // , selected
					}
				}
			} else if (keycode == AWTKeyEvent.VK_RIGHT) {
				if ((previous != null) && (selected == null)) { // ?
					set(last, "selected", get(get(last, "menu"), ":comp"));
					repaint(last); // , selected
				} else if ((selected != null) && (getComponentClass(selected) == "menu")) { // expand
																					// menu
					Object popup = popupMenu(last);
					set(popup, "selected", getMenu(popup, null, true, true));
				} else if ("menubar" == classname) { // select the next menubar
														// menu
					Object next = getMenu(component,
							get(component, "selected"), true, false);
					if (next != null) {
						set(component, "selected", next);
						Object popup = popupMenu(component);
						set(popup, "selected", getMenu(popup, null, true, true));
						repaint(component); // , selected
					}
				}
			} else if ((keycode == AWTKeyEvent.VK_ENTER)
					|| (keychar == AWTKeyEvent.VK_SPACE)
					|| (keycode == AWTKeyEvent.VK_ESCAPE)) {
				if ((keycode != AWTKeyEvent.VK_ESCAPE)
						&& getBoolean(selected, "enabled", true)) {
					if ((selected != null)
							&& (getComponentClass(selected) == "checkboxmenuitem")) {
						changeCheck(selected, false);
					}if ((selected != null)
							&& (getComponentClass(selected) == "panel")) {
						//do nothing
					} else
						invoke(selected, null, "action");
				}
				closeup();
			} else
				return false;
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	private boolean changeCheck(Object component, boolean box) {
		String group = getString(component, "group", null);
		if (group != null) {
			if (getBoolean(component, "selected", false)) {
				return false;
			}
			for (Object comp = get(getParent(component), ":comp"); comp != null; comp = get(
					comp, ":next")) {
				if (comp == component) {
					setBoolean(component, "selected", true);
				} else if (group.equals(get(comp, "group"))
						&& getBoolean(comp, "selected", false)) {
					setBoolean(comp, "selected", false);
					if (box) {
						repaint(comp);
					} // checkbox only
				}
			}
		} else {
			setBoolean(component, "selected",
					!getBoolean(component, "selected", false), false);
		}
		invoke(component, null, "action");
		return true;
	}

	/**
	 * @param component
	 *            a :popup or a menubar
	 * @param part
	 *            the currently selected item, return the first/last if null
	 * @param forward
	 *            find the next item if true, the previous otherwise
	 * @param popup
	 *            the given component is :popup if true, menubar otherwise
	 * @return the next/previous item relative to the current one excluding
	 *         separators, or null
	 */
	private Object getMenu(Object component, Object part, boolean forward,
			boolean popup) {
		Object previous = null;
		for (int i = 0; i < 2; i++) { // 0: next to last, 1: first to previous
			for (Object item = (i == 0) ? get(part, ":next") : get(
					popup ? get(component, "menu") : component, ":comp"); (i == 0) ? (item != null)
					: (item != part); item = get(item, ":next")) {
				if ((getComponentClass(item) != "separator")
						&& getBoolean(item, "enabled", true)) {
					if (forward) {
						return item;
					}
					previous = item;
				}
			}
		}
		return previous;
	}

	/**
	 * Process keyboard events for textfield, passwordfield, textarea, combobox,
	 * and spinbox
	 * 
	 * @param multiline
	 *            true for textarea, otherwise false
	 * @param hidden
	 *            true for passwordfield, otherwise false
	 * @param filter
	 *            true for spinbox, otherwise false
	 */
	private boolean processField(Object component, boolean shiftdown,
			boolean controldown, int modifiers, int keychar, int keycode,
			boolean multiline, boolean hidden, boolean filter) {
		String text = getString(component, "text", "");
		int start = getInteger(component, "start", 0);
		int end = getInteger(component, "end", 0);
		boolean editable = getBoolean(component, "editable", true);

		int istart = start;
		int iend = end;
		String insert = null;
		if (editable && (keychar != 0) &&
		// ((modifiers == 0) || (modifiers == InputEvent.SHIFT_MASK))) {
				(modifiers != InputEvent.ALT_MASK)) {
			insert = String.valueOf((char) keychar);
		} else if (editable && (keycode == AWTKeyEvent.VK_ENTER)) {
			if (multiline) {
				insert = "\n";
			} else {
				return invoke(component, null, "perform");
			}
		} else if (editable && (keycode == AWTKeyEvent.VK_BACK_SPACE)) {
			insert = "";
			if (start == end) {
				istart -= 1;
			}
		} else if (keycode == AWTKeyEvent.VK_END) {
			iend = text.length();
			if (!shiftdown) {
				istart = iend;
			}
		} else if (keycode == AWTKeyEvent.VK_HOME) {
			iend = 0;
			if (!shiftdown) {
				istart = iend;
			}
		} else if (keycode == AWTKeyEvent.VK_LEFT) {
			if (controldown) {
				for (int i = 0; i < 2; i++) {
					while ((iend > 0)
							&& ((i != 0) == Character.isLetterOrDigit(text
									.charAt(iend - 1)))) {
						iend--;
					}
				}
			} else {
				iend -= 1;
			}
			if (!shiftdown) {
				istart = iend;
			}
		} else if (keycode == AWTKeyEvent.VK_RIGHT) {
			if (controldown) {
				for (int i = 0; i < 2; i++) {
					while ((iend < text.length())
							&& ((i == 0) == Character.isLetterOrDigit(text
									.charAt(iend)))) {
						iend++;
					}
				}
			} else {
				iend += 1;
			}
			if (!shiftdown) {
				istart = iend;
			}
		} else if (editable && (keycode == AWTKeyEvent.VK_DELETE)) {
			insert = "";
			if (start == end) {
				iend += 1;
			}
		} else if (controldown
				&& ((keycode == AWTKeyEvent.VK_A) || (keycode == 0xBF))) {
			istart = 0; // AWTKeyEvent.VK_SLASH
			iend = text.length();
		} else if (controldown && (keycode == 0xDC)) {
			istart = iend = text.length(); // AWTKeyEvent.VK_BACK_SLASH
		} else if ((editable && !hidden && controldown && (keycode == AWTKeyEvent.VK_X))
				|| (!hidden && controldown && (keycode == AWTKeyEvent.VK_C))) {
			if (start != end) {
				clipboard = text.substring(Math.min(start, end),
						Math.max(start, end));
				
				if (keycode == AWTKeyEvent.VK_X) {
					insert = "";
				} else {
					return true;
				}
			}
		} else if (editable && controldown && (keycode == AWTKeyEvent.VK_V)) {
			
			if (insert != null) { // no text on system clipboard nor internal
									// clipboard text
				insert = filter(insert, multiline);
			}
		}
		if (filter && (insert != null)) { // contributed by Michael Nascimento
			try{
				Double.parseDouble(text+insert);
			}catch(Exception e){
				return false;
			}
			/*
			for (int i = insert.length() - 1; i >= 0; i--) {
				if (!Character.isDigit(insert.charAt(i))) {
					return false;
				}
			}
			*/
		}
		return changeField(component, text, insert, istart, iend, start, end);
	}

	/**
	 * @param text
	 * @param multiline
	 * @return
	 */
	private static String filter(String text, boolean multiline) {
		StringBuffer filtered = new StringBuffer(text.length());
		for (int i = 0; i < text.length(); i++) {
			char ckey = text.charAt(i);
			if (((ckey > 0x1f) && (ckey < 0x7f))
					|| ((ckey > 0x9f) && (ckey < 0xffff))
					|| (multiline && (ckey == '\n'))) {
				filtered.append(ckey);
			}
		}
		return (filtered.length() != text.length()) ? filtered.toString()
				: text;
	}

	/**
	 * @param component
	 *            a textfield, passwordfield, textarea, combobox, or spinbox
	 * @param text
	 *            current text
	 * @param insert
	 *            a string to replace thr current selection
	 * @param movestart
	 *            new selection start position
	 * @param moveend
	 *            new caret (selection end) position
	 * @param start
	 *            current selection start position
	 * @param end
	 *            current caret position
	 * @return true if selection, caret location, or text content changed
	 */
	private boolean changeField(Object component, String text, String insert,
			int movestart, int moveend, int start, int end) {
		movestart = Math.max(0, Math.min(movestart, text.length()));
		moveend = Math.max(0, Math.min(moveend, text.length()));
		if ((insert == null) && (start == movestart) && (end == moveend)) {
			return false;
		}
		if (insert != null) {
			int min = Math.min(movestart, moveend);
			text=text.substring(0, min) + insert
					+ text.substring(Math.max(movestart, moveend));
			set(component,
					"text",
					text);
			updateDrawStyle(component ,min,-Math.abs(movestart-moveend)+insert.length());
			movestart = moveend = min + insert.length();
			/*
			if(getClass(component)=="spinbox"){
				try{
					double v=Double.parseDouble(text);
					double minimum = getDouble(component, "minimum");
					double maximum = getDouble(component, "maximum");
					double value = getDouble(component, "value");
					double step= getDouble(component, "step");
					v=MathUtils.clamp(v, minimum, maximum);
					v=Math.floor((v-minimum)/step)*step+minimum;
					if(v!=value){
						//setDouble(component,"value",v);
					
					}
					//setString(component,"text",v+"");
					//invoke(component, null, "action");
					//return false;
				}catch(Exception e){
					//shouldn't occur
					//return false;
				}
				//setString(component,"text",value+"");
			}
			if (start != movestart) {
				setInteger(component, "start", movestart, 0);
			}
			if (end != moveend) {
				setInteger(component, "end", moveend, 0);
			}
			validate(component);
			//invoke(component, null, "action"); // deprecated
			return false;
			*/
		}
		
		if (start != movestart) {
			setInteger(component, "start", movestart, 0);
		}
		if (end != moveend) {
			setInteger(component, "end", moveend, 0);
		}
		validate(component);
		if(getComponentClass(component)=="spinbox")return false;
		invoke(component,
				null,
				(insert != null) ? ((insert.length() > 0) ? "insert" : "remove")
						: "caret");
		return true;
	}

	/**
	 *
	 */
	private boolean processList(Object component, boolean shiftdown,
			boolean controldown, int keychar, int keycode, boolean recursive) {
		if ((keycode == AWTKeyEvent.VK_UP)
				|| // select previous/next/first/... item
				(keycode == AWTKeyEvent.VK_DOWN)
				|| (keycode == AWTKeyEvent.VK_PAGE_UP)
				|| (keycode == AWTKeyEvent.VK_PAGE_DOWN)
				|| (keycode == AWTKeyEvent.VK_HOME)
				|| (keycode == AWTKeyEvent.VK_END)) {
			Object lead = get(component, ":lead");
			Object row = getListItem(component, component, keycode, lead,
					recursive);
			if (row != null) {
				String selection = getString(component, "selection", "single");
				if (shiftdown && (selection != "single") && (lead != null)) {
					extend(component, lead, row, recursive);
				} else if (!controldown) {
					selectItem(component, row, recursive);
				}
				setLead(component, lead, row);
				return true;
			}
		} else if (keycode == AWTKeyEvent.VK_LEFT) {
			return processScroll(component, "left");
		} else if (keycode == AWTKeyEvent.VK_RIGHT) {
			return processScroll(component, "right");
		} else if (keychar == AWTKeyEvent.VK_SPACE) { // select the current item
			select(component, get(component, ":lead"), recursive, shiftdown,
					controldown); // ...
			return true;
		} else if (controldown) {
			if (((keycode == AWTKeyEvent.VK_A) || (keycode == 0xBF)) && // AWTKeyEvent.VK_SLASH
					(getString(component, "selection", "single") != "single")) { // select
																					// all
				selectAll(component, true, recursive);
				return true;
			} else if (keycode == 0xDC) { // AWTKeyEvent.VK_BACK_SLASH // deselect
											// all
				selectAll(component, false, recursive);
				return true;
			}
		} else {
			Object item = findText((char) keychar, component, component,
					recursive);
			if (item != null) {
				select(component, item, recursive, false, false);
				return true;
			}
		}
		return false;
	}

	/**
	 * Search for the next/first appropriate item starting with the collected
	 * string or the given single character
	 * 
	 * @param keychar
	 *            the last typed character
	 * @param component
	 *            a list, tree, table, or combobox
	 * @param leadowner
	 *            the list, tree, table, or the combobox's drop down list
	 * @param recursive
	 *            if the component is a tree
	 * @return the appropriate item or null
	 */
	private Object findText(char keychar, Object component, Object leadowner,
			boolean recursive) {
		if (keychar != 0) {
			long current = System.currentTimeMillis();
			int i = (current > findtime + 1000) ? 1 : 0; // clear the starting
															// string after a
															// second
			findtime = current;
			Object lead = get(leadowner, ":lead");
			for (; i < 2; i++) { // 0: find the long text, 1: the stating
									// character only
				findprefix = (i == 0) ? (findprefix + keychar) : String
						.valueOf(keychar);
				for (int j = 0; j < 2; j++) { // 0: lead to last, 1: first to
												// lead
					for (Object item = (j == 0) ? ((i == 0) ? lead
							: getNextItem(component, lead, recursive)) : get(
							component, ":comp"); (j == 0) ? (item != null)
							: (item != lead); item = getNextItem(component,
							item, recursive)) {
						/*
						if (getString(item, "text", "").regionMatches(true, 0,
								findprefix, 0, findprefix.length())) { // table
																		// first
																		// column...
							return item;
						}
						*/
					}
				}
			}
		}
		return null;
	}

	/**
	 *@warning Will hang (infinite loop) if list contains only separators or all disabled items
	 */
	private Object getListItem(Object component, Object scrollpane,
			int keycode, Object lead, boolean recursive) {
		Object row = null;
		if (keycode == AWTKeyEvent.VK_UP) {
			for (Object prev = get(component, ":comp"); prev != lead; prev = getNextItem(
					component, prev, recursive)) {
				row = prev; // component -> getParent(lead)
			}
			//Will hang (infinite loop) if list contains only separators or all disabled items
			if(row!=null && (getComponentClass(row)=="separator" ||!getBoolean(row,"enabled")))row=getListItem(component, scrollpane,
					 keycode,  row,  recursive);
		} else if (keycode == AWTKeyEvent.VK_DOWN) {
			row = (lead == null) ? get(component, ":comp") : getNextItem(
					component, lead, recursive);
			//Will hang (infinite loop) if list contains only separators or all disabled items
			if(row!=null && (getComponentClass(row)=="separator"||!getBoolean(row,"enabled")))row=getListItem(component, scrollpane,
					 keycode,  row,  recursive);
		} else if ((keycode == AWTKeyEvent.VK_PAGE_UP)
				|| (keycode == AWTKeyEvent.VK_PAGE_DOWN)) {
			Rectangle view = getRectangle(scrollpane, ":view");
			Rectangle port = getRectangle(scrollpane, ":port");
			Rectangle rl = (lead != null) ? getRectangle(lead, "bounds") : null;
			int vy = (keycode == AWTKeyEvent.VK_PAGE_UP) ? view.y
					: (view.y + port.height);
			if ((keycode == AWTKeyEvent.VK_PAGE_UP) && (rl != null)
					&& (rl.y <= view.y)) {
				vy -= port.height;
			}
			if ((keycode == AWTKeyEvent.VK_PAGE_DOWN) && (rl != null)
					&& (rl.y + rl.height >= view.y + port.height)) {
				vy += port.height;
			}
			for (Object item = get(component, ":comp"); item != null; item = getNextItem(
					component, item, recursive)) {
				Rectangle r = getRectangle(item, "bounds");
				if (keycode == AWTKeyEvent.VK_PAGE_UP) {
					row = item;
					if (r.y + r.height > vy) {
						break;
					}
				} else {
					if (r.y > vy) {
						break;
					}
					row = item;
				}
			}
		} else if (keycode == AWTKeyEvent.VK_HOME) {
			row = get(component, ":comp");
		} else if (keycode == AWTKeyEvent.VK_END) {
			for (Object last = lead; last != null; last = getNextItem(
					component, last, recursive)) {
				row = last;
			}
		}
		return row;
	}

	/**
	 * Select all the items
	 * 
	 * @param component
	 *            a list/tree/table
	 * @param selected
	 *            selects or deselects items
	 * @param recursive
	 *            true for tree
	 */
	private void selectAll(Object component, boolean selected, boolean recursive) {
		boolean changed = false;
		for (Object item = get(component, ":comp"); item != null; item = getNextItem(
				component, item, recursive)) {
			if (setBoolean(item, "selected", selected, false)) {
				repaint(component, null, item);
				changed = true;
			}
		}
		set(component, ":anchor", null);
		if (changed) {
			invoke(component, null, "action");
		}
	}

	/**
	 * Select a single given item, deselect others
	 * 
	 * @param component
	 *            a list/tree/table
	 * @param row
	 *            the item/node/row to select
	 * @param recursive
	 *            true for tree
	 */
	private void selectItem(Object component, Object row, boolean recursive) {
		boolean changed = false;
		for (Object item = get(component, ":comp"); item != null; item = getNextItem(
				component, item, recursive)) {
			if (setBoolean(item, "selected", (item == row), false)) {
				repaint(component, null, item);
				changed = true;
			}
		}
		set(component, ":anchor", null);
		if (changed) {
			invoke(component, row, "action");
		}
	}

	/**
	 *
	 */
	private void extend(Object component, Object lead, Object row,
			boolean recursive) {
		Object anchor = get(component, ":anchor");
		if (anchor == null) {
			set(component, ":anchor", anchor = lead);
		}
		char select = 'n';
		boolean changed = false;
		for (Object item = get(component, ":comp"); // anchor - row
		item != null; item = getNextItem(component, item, recursive)) {
			if (item == anchor)
				select = (select == 'n') ? 'y' : 'r';
			if (item == row)
				select = (select == 'n') ? 'y' : 'r';
			if (setBoolean(item, "selected", (select != 'n'), false)) {
				repaint(component, null, item);
				changed = true;
			}
			if (select == 'r')
				select = 'n';
		}
		if (changed) {
			invoke(component, row, "action");
		}
	}

	/**
	 * Update the lead item of a list/tree/table, repaint, and scroll
	 * 
	 * @param component
	 *            a list, tree, or table
	 * @param oldlead
	 *            the current lead item
	 * @param lead
	 *            the new lead item
	 */
	private void setLead(Object component, Object oldlead, Object lead) {
		if (oldlead != lead) { // ?
			if (oldlead != null) {
				repaint(component, null, oldlead);
			}
			set(component, ":lead", lead);
			repaint(component, null, lead);

			Rectangle r = getRectangle(lead, "bounds");
			scrollToVisible(component, r.x, r.y, 0, r.height);
		}
	}

	/**
	 * Update the lead item of a combolist, repaint, and scroll
	 * 
	 * @param component
	 *            a combobox drop down list
	 * @param part
	 *            the current hotspot item
	 * @param scroll
	 *            scroll to the part if true
	 */
	private void setInside(Object component, Object part, boolean scroll) {
		Object previous = get(component, ":lead");
		if (previous != null) {
			repaint(component, ":combolist", previous);
		}
		set(component, ":lead", part);
		if (part != null) {
			repaint(component, ":combolist", part);
			if (scroll) {
				Rectangle r = getRectangle(part, "bounds");
				scrollToVisible(component, r.x, r.y, 0, r.height);
			}
		}
	}
	
	private boolean draggingComponent=false;
	public void zoomDesktop(int x, int y){
		
	}
		
	public void handleWorldCamera(){
		getItems(content);
		for (Object comp = get(content, ":comp"); comp != null; comp = get(comp,
				":next")) {
			if(getProperty(comp,"pintoworld")==null)continue;
			Rectangle bounds = getRectangle(comp, "bounds");
			/*
			if(!(getProperty(comp,"pintoworld") instanceof Vector2))putProperty(comp,"pintoworld",Simphy.instance.screenToWorld(new Vector2(bounds.x,bounds.y).divide(g.PIXEL_SCALE_FACTOR)));
			Vector2 v=(Vector2) getProperty(comp,"pintoworld");
			v=Simphy.instance.worldToScreen(v);//.divide(g.PIXEL_SCALE_FACTOR);
			v.set(v.x+Simphy.instance.canvas.getSurfaceWidth()/2,-v.y+Simphy.instance.canvas.getSurfaceHeight()/2); 
			bounds.x= (int) v.x;
			bounds.y= (int) v.y;
			repaintNeeded=true;
			*/
		}
	}
	
	/**
	 * return true if widget is being dragged on desktop
	 * @param x
	 * @param y
	 * @param btn
	 * @param id
	 * @param component
	 * @param part
	 * @return
	 */
	private boolean handleDrag(int x, int y,int btn,	int id, Object component, Object part){
		if(id == AWTMouseEvent.MOUSE_CLICKED||id == AWTMouseEvent.MOUSE_RELEASED)return false;
		Object parent=getParent(component);
		if(component ==null || btn!=1||part!=null ||getComponentClass(component)=="menubar"||getComponentClass(parent)!="desktop"||getProperty(component,"lockobject")!=null){
			draggingComponent=false;
			return false;
		}

		Rectangle bounds = getRectangle(component, "bounds");
		
		if(id == AWTMouseEvent.MOUSE_PRESSED){
			draggingComponent=false;
			//Check if knob of slider is being dragged
			if(getComponentClass(component)=="slider"){
				
				double minimum = getDouble(component, "minimum");
				double maximum = getDouble(component, "maximum");
				double value = getDouble(component, "value");
				boolean horizontal = ("vertical" != get(component, "orientation"));
				if(!horizontal)value= maximum+minimum-value;
				double length = (value - minimum) *
						((horizontal ? bounds.width : bounds.height) - block) /
						(maximum - minimum);
				//if(!horizontal)length= bounds.height-length-block;
				setReference(component,0,0);//else setReference(component,0,length+block/2);
			
				boolean onKnob= horizontal?((x-referencex)>length && (x-referencex)<length+block):((y-referencey)>length && (y-referencey)<length+block);
				if(onKnob)return false;
			}else if(getComponentClass(component)=="checkbox"){
				
			}
			draggingComponent=true;
			referencex=x-bounds.x;
			referencey=y-bounds.y;
			return true;
		}else if(draggingComponent){
			Rectangle parents = getRectangle(getParent(component),
					"bounds");
			int mx=x - referencex;
			int my=y - referencey;
			boolean pinnedtoworld=getProperty(component,"pintoworld")!=null	;	
			if(!pinnedtoworld){
				mx = Math.max(
					0,
					Math.min(mx, parents.width
							- bounds.width));
			    my = Math.max(
					0,
					Math.min(my, parents.height
							- bounds.height));
			}
		
			if ((bounds.x != mx) || (bounds.y != my)) {
				// repaint the union of the previous and next bounds
				repaint(component, Math.min(bounds.x, mx),
						Math.min(bounds.y, my),
						bounds.width + Math.abs(mx - bounds.x),
						bounds.height + Math.abs(my - bounds.y));
				bounds.x = mx;
				bounds.y = my;
				if(pinnedtoworld)putProperty(component,"pintoworld",true);
				
			}
			//referencex=x-bounds.x;
			//referencey=y-bounds.y;
			return true;
		}
		
		draggingComponent=false;
		return false;
	}

	/**
	 * @param x
	 *            mouse x position relative to thinlet component
	 * @param y
	 *            mouse y position relative to the main desktop
	 */
	private void handleMouseEvent(int x, int y, int button,int clickcount,
			boolean shiftdown, boolean controldown, boolean popuptrigger,
			int id, Object component, Object part) {
		if (id == AWTMouseEvent.MOUSE_ENTERED) {
			setTimer(750,1);
		} else if (id == AWTMouseEvent.MOUSE_EXITED) {
			hideTip();
		}
		if (!getBoolean(component, "enabled", true)) {
			return;
		}
		
		
		if(handleDrag(x,y,button,id,component,part))return ;
		
		if (clickcount > 2)
			clickcount = (clickcount % 2) + 1;
	
		String classname = getComponentClass(component);
		if (("list" == classname) || ("table" == classname)
				|| ("tree" == classname)) {
			// check if we need to change the column selection and invoke the
			// header its action
			Object header = get(component, "header");
			Object resizeComponent = null;
			boolean noScroll = false;
			if (header != null
					&& (resizeComponent = get(header, ":resizecomponent")) != null) {
				noScroll = true;
				if (clickcount == 2) {
					setSmartWidth(resizeComponent);
					doLayout(component);
					repaint(component);
				} else {
					if (id == AWTMouseEvent.MOUSE_PRESSED) {
						referencex = x;
						set(header, ":resizing", "true");
					} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
						// resize the column, but limit its minimum size to a
						// width of MINIMUM_COLUMN_WIDTH
						int offset = x - referencex;

						int newSize = getInteger(resizeComponent, "width")
								+ (offset >= 0 ? offset : offset);
						if (newSize > MINIMUM_COLUMN_WIDTH) {
							setInteger(resizeComponent, "width", newSize);
							referencex = x;
							doLayout(component);
							repaint(component);
						}
					} else if (id == AWTMouseEvent.MOUSE_RELEASED)
						set(header, ":resizing", null);
					else if (id == AWTMouseEvent.MOUSE_ENTERED)
						awtComponent.style.cursor=(Cursor
								.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				}
			}
			if (header != null && get(header, ":resizecomponent") == null) {
				set(header, ":resizing", null);
				awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			if (header != null && get(header, "action") != null) {
				if (insidepart != null && insidepart instanceof Object[]
						&& "column" == getComponentClass(insidepart)) {
					noScroll = true;
					if (id == AWTMouseEvent.MOUSE_ENTERED
							|| id == AWTMouseEvent.MOUSE_PRESSED
							|| id == AWTMouseEvent.MOUSE_RELEASED) {
						if (id == AWTMouseEvent.MOUSE_RELEASED
								&& mouseinside == component) {
							// set selected column its sort and selected
							// property
							// and set the sort of all other columns to none
							// (null)
							Object column = get(get(component, "header"),
									":comp");
							Object sort = null;
							while (column != null) {
								if (column == insidepart) {
									sort = get(column, "sort");
									if (null == sort || "none" == sort
											|| "descent" == sort)
										sort = "ascent";
									else if ("ascent" == sort)
										sort = "descent";
								} else
									sort = null;
								set(column, "sort", sort);
								setBoolean(column, "selected", sort != null
										&& sort != "none");
								column = get(column, ":next");
							}
							invoke(header, null, "action");
						}
						repaint(component);
					}
				} else if (id == AWTMouseEvent.MOUSE_EXITED)
					repaint(component);
			}
			if (!noScroll && !processScroll(x, y, id, component, part,false)) {
				if (((id == AWTMouseEvent.MOUSE_PRESSED) || ((id == AWTMouseEvent.MOUSE_DRAGGED)
						&& !shiftdown && !controldown))) {
					// Rectangle view = getRectangle(component, ":view");
					Rectangle port = getRectangle(component, ":port");
					int my = y + port.y - referencey;
					for (Object item = get(component, ":comp"); item != null;) {
						Rectangle r = getRectangle(item, "bounds");
						if (my < r.y + r.height) {
							if (id == AWTMouseEvent.MOUSE_DRAGGED) { // !!!
								scrollToVisible(component, r.x, r.y, 0,
										r.height);
							} else if ("tree" == classname) {
								int mx = x + port.x - referencex;
								if (mx < r.x) {
									if ((mx >= r.x - block)
											&& (get(item, ":comp") != null)) {
										boolean expanded = getBoolean(item,
												"expanded", true);
										setBoolean(item, "expanded", !expanded,
												true);
										selectItem(component, item, true);
										setLead(component,
												get(component, ":lead"), item);
										setFocus(component);
										validate(component);
										invoke(component, item,
												expanded ? "collapse"
														: "expand"); // item
									}
									break;
								}
							}
							if ((id != AWTMouseEvent.MOUSE_DRAGGED)
									|| !getBoolean(item, "selected", false)) {
								if (id != AWTMouseEvent.MOUSE_DRAGGED) {
									if (setFocus(component)) {
										repaint(component, classname, item);
									} // ?
								}
								select(component, item, ("tree" == classname),
										shiftdown, controldown);
								if (clickcount == 2) {
									invoke(component, item, "perform");
								}
							}
							break;
						}
						item = getNextItem(component, item,
								("tree" == classname));
					}
				}
			}
		} else if ("bean" == classname) {
			CustomComponent bean = (CustomComponent) get(component, "bean");
			/*
			int modifiers = 0;
			if (shiftdown)
				modifiers |= InputEvent.SHIFT_MASK;
			if (controldown)
				modifiers |= InputEvent.CTRL_MASK;
			if (id == AWTMouseEvent.MOUSE_PRESSED
					|| id == AWTMouseEvent.MOUSE_RELEASED
					|| id == AWTMouseEvent.MOUSE_EXITED
					|| id == AWTMouseEvent.MOUSE_ENTERED
					|| id == AWTMouseEvent.MOUSE_MOVED
					|| id == AWTMouseEvent.MOUSE_DRAGGED) {
				bean.handleMouseEvent(((Integer) get(
						component, ":mousex")).intValue(), ((Integer) get(
						component, ":mousey")).intValue(),clickcount,id,shiftdown,controldown,
						popuptrigger);
			}
			*/
			boolean b=false;
			if(clickcount>0){
				b=bean.handleMouseEvent(((Integer) get(
						component, ":mousex")).intValue(), ((Integer) get(
						component, ":mousey")).intValue(),clickcount,AWTMouseEvent.MOUSE_CLICKED,button,shiftdown,controldown,
						popuptrigger);
				b=bean.handleMouseEvent(((Integer) get(
						component, ":mousex")).intValue(), ((Integer) get(
						component, ":mousey")).intValue(),0,AWTMouseEvent.MOUSE_RELEASED,button,shiftdown,controldown,
						popuptrigger);
			}else{

				b=bean.handleMouseEvent(((Integer) get(
					component, ":mousex")).intValue(), ((Integer) get(
					component, ":mousey")).intValue(),clickcount,id,button,shiftdown,controldown,
					popuptrigger);
			}
			if(b){
				return;
			}
		} else if (clickcount < 2 && ("button" == classname)
				|| ("checkbox" == classname) || ("togglebutton" == classname)) {
			if ((id == AWTMouseEvent.MOUSE_ENTERED)
					|| (id == AWTMouseEvent.MOUSE_EXITED)
					|| (id == AWTMouseEvent.MOUSE_PRESSED)
					|| (id == AWTMouseEvent.MOUSE_RELEASED)) {
				if (id == AWTMouseEvent.MOUSE_PRESSED) {
					setFocus(component);
				}
				if (("button" == classname)
						&& ((mousepressed == null) || (mousepressed == component))
						&& ((id == AWTMouseEvent.MOUSE_ENTERED) || (id == AWTMouseEvent.MOUSE_EXITED))
						&& (get(component, "type") == "link")) {
					awtComponent.style.cursor=(Cursor
							.getPredefinedCursor((id == AWTMouseEvent.MOUSE_ENTERED) ? Cursor.HAND_CURSOR
									: Cursor.DEFAULT_CURSOR));
				} else if ((id == AWTMouseEvent.MOUSE_RELEASED ||id == AWTMouseEvent.MOUSE_CLICKED  && button==AWTMouseEvent.BUTTON1)
						&& (mouseinside == component)) {
					if ("button" != classname) {
						changeCheck(component, true);
					} else
						invoke(component, null, "action");
				}
				repaint(component);
			}
		} else if ("combobox" == classname) {
			boolean editable = getBoolean(component, "editable", true);
			if (editable && (part == null)) { // textfield area
				AWTImage icon = null;
				int left = ((id == AWTMouseEvent.MOUSE_PRESSED) && ((icon = getIcon(
						component, "icon", null)) != null)) ? icon
						.getScaledWidth() : 0;
				processField(x, y, clickcount, id, component, false, false,
						left);
			} else if (part != "icon") { // part = "down"
				if (((id == AWTMouseEvent.MOUSE_ENTERED) || (id == AWTMouseEvent.MOUSE_EXITED))
						&& (mousepressed == null)) {
					if (editable) {
						repaint(component, "combobox", part);
					} // hover the arrow button
					else {
						repaint(component);
					} // hover the whole combobox
				} else if (id == AWTMouseEvent.MOUSE_PRESSED) {
					Object combolist = get(component, ":combolist");
					if (combolist == null) { // combolist is closed
						setFocus(component);
						repaint(component);
						popupCombo(component);
					} else { // combolist is visible
						closeCombo(component, combolist, null);
					}
				} else if (id == AWTMouseEvent.MOUSE_RELEASED) {
					if (mouseinside != component) {
						Object combolist = get(component, ":combolist");
						closeCombo(
								component,
								combolist,
								((mouseinside == combolist) && (insidepart instanceof Object[])) ? insidepart
										: null);
					} else {
						repaint(component);
					}
				}
			}
		} else if (":combolist" == classname) {
			if (!processScroll(x, y, id, component, part,false)) {
				if ((id == AWTMouseEvent.MOUSE_ENTERED) || (id == DRAG_ENTERED)) {
					if (part != null) { // + scroll if dragged
						setInside(component, part, false);
					}
				} else if (clickcount>0||id == AWTMouseEvent.MOUSE_CLICKED) {
					if ((part != null)&& getBoolean(part, "enabled", true)) {
						Object combobox=get(component, "combobox");
						if (getComponentClass(part) == "checkboxmenuitem") {
							//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("item checked"+insidepart);
							changeCheck(part, false);
							setInteger(combobox, "selected", getIndex(combobox, part), -1);
							invoke(combobox, part, "action");
						} else{
							//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("item clicked"+insidepart);
							//Object combobox=get(component, "combobox");
							closeCombo(combobox, component, part);
							setFocus(combobox);
						}
					}
				}
			}
		} else if (("textfield" == classname) || ("passwordfield" == classname)) {
			
			processField(x, y, clickcount, id, component, false,
					("passwordfield" == classname), 0);
		} else if ("textarea" == classname) {
			if(clickcount>0){
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("click count="+clickcount);
				//processField(x, y, clickcount, id, component, true, false, 0);
			}

			//if (!processScroll(x, y, id, component, part,input.mouse.activeFingerCount>0)) {
			//	if(input.mouse.activeFingerCount==0)processField(x, y, clickcount, id, component, true, false, 0);
			//}
		} else if ("panel" == classname) {
			//if (id == AWTMouseEvent.MOUSE_PRESSED) setFocus(component);
			//processScroll(x, y, id, component, part,input.mouse.activeFingerCount>0);
		} else if ("desktop" == classname) {
			if (part == "modal") {
				if (id == AWTMouseEvent.MOUSE_ENTERED) {
					awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				} else if (id == AWTMouseEvent.MOUSE_EXITED) {
					awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			
			}
		} else if ("spinbox" == classname) {
			if (part == null) {
				processField(x, y, clickcount, id, component, false, false, 0);
			} else { // part = "up" || "down"
				if ((id == AWTMouseEvent.MOUSE_ENTERED)
						|| (id == AWTMouseEvent.MOUSE_EXITED)
						|| (id == AWTMouseEvent.MOUSE_PRESSED)
						|| (id == AWTMouseEvent.MOUSE_RELEASED)) {
					if (id == AWTMouseEvent.MOUSE_PRESSED) {
						setFocus(component);
						if (processSpin(component, part)) {
							setTimer(375,1);
						}
						// settext: start end selection, parse exception...
					} else {
						if (id == AWTMouseEvent.MOUSE_RELEASED) {
							setTimer(0,0);
						}
					}
					repaint(component, classname, part);
				}
			}
		} else if ("tabbedpane" == classname) {
			String placement = getString(component, "placement", "top");
			if(placement == "none") return;
			if ((id == AWTMouseEvent.MOUSE_ENTERED)
					|| (id == AWTMouseEvent.MOUSE_EXITED)) {
				if ((part != null)
						&& getBoolean(part, "enabled", true)
						&& (getInteger(component, "selected", 0) != getIndex(
								component, part))) {
					repaint(component, "tabbedpane", part);
				}
			} else if ((part != null) && (id == AWTMouseEvent.MOUSE_PRESSED)
					&& getBoolean(part, "enabled", true)) {
				int selected = getInteger(component, "selected", 0);
				int current = getIndex(component, part);
				if (selected == current) {
					setFocus(component);
					repaint(component, "tabbedpane", part);
				} else {
					setInteger(component, "selected", current, 0);
					// Object tabcontent = getItem(component, current);
					// setFocus((tabcontent != null) ? tabcontent : component);
					setNextFocusable(component, false);
					checkOffset(component);
					repaint(component);
					invoke(component, part, "action");
				}
			}
		} else if ("slider" == classname) {
			if (button==AWTMouseEvent.BUTTON1 &&((id == AWTMouseEvent.MOUSE_PRESSED)
					|| (id == AWTMouseEvent.MOUSE_DRAGGED))) {
				if (id == AWTMouseEvent.MOUSE_PRESSED) {
					setReference(component, block / 2, block / 2);
					setFocus(component);
				}
				double minimum = getDouble(component, "minimum");
				double maximum = getDouble(component, "maximum");
				double value = getDouble(component, "value");
				double step = getDouble(component, "unit");
				Rectangle bounds = getRectangle(component, "bounds");
				boolean horizontal = ("vertical" != get(component,
						"orientation"));
				double newvalue = minimum
						+ (horizontal ? (x - referencex) : (y - referencey))
						* (maximum - minimum)
						/ ((horizontal ? bounds.width : bounds.height) - block); 
				newvalue=minimum+Math.floor((newvalue-minimum)/step)*step;
				if(!horizontal)	newvalue=maximum+minimum-newvalue;																// +0.5
				newvalue = Math.max(minimum, Math.min(newvalue, maximum));
	
				if (Math.abs(value-newvalue)>step/2) {
					//System.out.println("new Value="+newvalue+", old="+value);

					setDouble(component, "value", newvalue, -1);
					invoke(component, null, "action");
					//if(id == AWTMouseEvent.MOUSE_PRESSED)repaint(component);
				}
			}
		} else if ("splitpane" == classname) {
			if (id == AWTMouseEvent.MOUSE_PRESSED) {
				setReference(component, 2, 2);
			} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
				int divider = getInteger(component, "divider", -1);
				boolean horizontal = ("vertical" != get(component,
						"orientation"));
				int moveto = horizontal ? (x - referencex) : (y - referencey);
				Rectangle bounds = getRectangle(component, "bounds");
				moveto = Math.max(0,
						Math.min(moveto, Math.abs(horizontal ? bounds.width
								: bounds.height) - 5));
				if (divider != moveto) {
					setInteger(component, "divider", moveto, -1);
					validate(component);
				}
			} else if ((id == AWTMouseEvent.MOUSE_ENTERED)
					&& (mousepressed == null)) {
				boolean horizontal = ("vertical" != get(component,
						"orientation"));
				awtComponent.style.cursor=(Cursor
						.getPredefinedCursor(horizontal ? Cursor.E_RESIZE_CURSOR
								: Cursor.S_RESIZE_CURSOR));
			} else if (((id == AWTMouseEvent.MOUSE_EXITED) && (mousepressed == null))
					|| ((id == AWTMouseEvent.MOUSE_RELEASED) && (mouseinside != component))) {
				awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else if ("menubar" == classname) {
			Object selected = get(component, "selected");
			if (((id == AWTMouseEvent.MOUSE_ENTERED) || (id == AWTMouseEvent.MOUSE_EXITED))
					&& (part != null)
					&& (selected == null)
					&& getBoolean(part, "enabled", true)) {
				repaint(component, classname, part);
			} else if ((part != null)
					&& ((selected == null) ? (id == AWTMouseEvent.MOUSE_PRESSED)
							: ((id == AWTMouseEvent.MOUSE_ENTERED) || (id == DRAG_ENTERED)))
					&& getBoolean(part, "enabled", true)) {
				// || ((id == AWTMouseEvent.MOUSE_PRESSED) && (insidepart != part))
				set(component, "selected", part);
				popupMenu(component);
				repaint(component, classname, part);
			} else if ((id == AWTMouseEvent.MOUSE_PRESSED) && (selected != null)) {
				closeup();
				
			} else if (id == AWTMouseEvent.MOUSE_RELEASED ||id == AWTMouseEvent.MOUSE_CLICKED) {
				if ((part != insidepart)
						&& ((insidepart == null) || ((insidepart instanceof Object[]) && (getComponentClass(insidepart) != "menu")))) {
					if ((insidepart != null)
							&& getBoolean(insidepart, "enabled", true)) {
						if (getComponentClass(insidepart) == "checkboxmenuitem") {
							changeCheck(insidepart, false);
						}else if (getComponentClass(insidepart) == "menuitem") {
							invoke(insidepart, null, "action");
							closeup();
						} else{
							//invoke(insidepart, null, "action");
						}						
					}
					//closeup();
				}
			}
		} else if (":popup" == classname) {
			
			if (part != null) {
				if (((id == AWTMouseEvent.MOUSE_ENTERED) || (id == DRAG_ENTERED)||(id==AWTMouseEvent.MOUSE_PRESSED))
						&& getBoolean(part, "enabled", true)) {
					set(component, "selected", part);
					popupMenu(component);
					repaint(component, classname, part);
				} else if (id == AWTMouseEvent.MOUSE_RELEASED||id == AWTMouseEvent.MOUSE_CLICKED) {
					if ((insidepart == null)
							|| (getComponentClass(insidepart) != "menu")) {
						if ((insidepart != null) && getBoolean(insidepart, "enabled", true)) {
							//if(getClass(insidepart) != "checkboxmenuitem" && getClass(insidepart) != "panel")closeup();

							if (getComponentClass(insidepart) == "checkboxmenuitem") {
								changeCheck(insidepart, false);
							}else if (getComponentClass(insidepart) == "menuitem") {
								invoke(insidepart, null, "action");
								closeup();
								if(popupowner==null)remove(component);
								//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(" close up2");
							} else{
								//invoke(insidepart, null, "action");
							}						
						}
					}
				} else if (((id == AWTMouseEvent.MOUSE_EXITED) || (id == DRAG_EXITED))
						&& getBoolean(part, "enabled", true)) {
					if (getComponentClass(part) != "menu") {
						set(component, "selected", null);
					}
					repaint(component, classname, part);
				}
			}
		} else if ("dialog" == classname) {
			if (part == "header") {
				if (id == AWTMouseEvent.MOUSE_PRESSED ||id == AWTMouseEvent.MOUSE_CLICKED) {
					Rectangle bounds = getRectangle(component, "bounds");
					referencex = x - bounds.x;
					referencey = y - bounds.y;
					Object parent = getParent(component);
					if (get(parent, ":comp") != component) { // bring to front
						removeItemImpl(parent, component);
						insertItem(parent, ":comp", component, 0);
						set(component, ":parent", parent);
						repaint(component); // to front always...
						setNextFocusable(component, false);
					}else if(focusowner==null){
						setNextFocusable(component, false);
					}
				} else if (id == AWTMouseEvent.MOUSE_DRAGGED && getProperty(component,"lockobject")==null) {
					Rectangle bounds = getRectangle(component, "bounds");
					Rectangle parents = getRectangle(getParent(component),
							"bounds");
					boolean pinnedtoworld=getProperty(component,"pintoworld")!=null;
					int mx = x - referencex;
					int my = y - referencey;
					if(!pinnedtoworld){
						 mx = Math.max(
								0,
								Math.min(mx, parents.width
										- bounds.width));
						 my = Math.max(
								0,
								Math.min(my, parents.height
										- bounds.height));
					}
				
					if ((bounds.x != mx) || (bounds.y != my)) {
						// repaint the union of the previous and next bounds
						repaint(component, Math.min(bounds.x, mx),
								Math.min(bounds.y, my),
								bounds.width + Math.abs(mx - bounds.x),
								bounds.height + Math.abs(my - bounds.y));
						bounds.x = mx;
						bounds.y = my;
						if(pinnedtoworld)putProperty(component,"pintoworld",true);
						
					}
				}
			}
			// close dialog button
			else if (part == ":closebutton" ||part == ":maximizebutton" || part == ":iconifybutton") {
				if (id == AWTMouseEvent.MOUSE_ENTERED
						|| id == AWTMouseEvent.MOUSE_EXITED
						|| id == AWTMouseEvent.MOUSE_PRESSED
						|| id == AWTMouseEvent.MOUSE_RELEASED) {
					if (id == AWTMouseEvent.MOUSE_RELEASED
							&& mouseinside == component){
						if(part==":closebutton"){
							closeDialog(component);
						}else if(part==":maximizebutton"){
							maximizeDialog(component);
						}else if(part==":iconifybutton"){
							minimizeDialog(component);
						}
					}
					repaint(component);
				}
			} else if (!processScroll(x, y, id, component, part,false)
					&& (part != null)) {
				if (id == AWTMouseEvent.MOUSE_PRESSED) {
					referencex = x;
					referencey = y;
				} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
					//resize dialog with restriction of min/max size
					repaint(component);
					int minHeight = getInteger(component, ":titleheight", 0)+4*margin_1;
					int minWidth = minHeight*3-8*margin_1;
					
					Rectangle bounds = getRectangle(component, "bounds");
					Rectangle parents = getRectangle(getParent(component),"bounds");
					if(part==":se" ||part==":s"||part==":e"){
						//resize keeping top left claped
						if(part!=":s"){
							bounds.width += x - referencex;
							if(bounds.width>parents.width-bounds.x)bounds.width=parents.width-bounds.x;
							if(bounds.width<minWidth)bounds.width=minWidth;
						}
						if(part!=":e"){
							bounds.height += y - referencey;
							if(bounds.height>parents.height-bounds.y)bounds.height=parents.height-bounds.y;
							if(bounds.height<minHeight)bounds.height=minHeight;
						}
					}else if(part==":nw" ||part==":n"||part==":w"){
						//resize keeping bottom right clamped
						int xo=bounds.x+bounds.width;
						int yo=bounds.y+bounds.height;
						if(part!=":n"){
							bounds.x+= x - referencex;
							bounds.width -= x - referencex;
							if(bounds.x<0){
								bounds.x=0;
								bounds.width=xo;
							}
							if(bounds.width<minWidth){
								bounds.width=minWidth;
								bounds.x=xo-bounds.width;
							}
						}
						if(part!=":w"){
							
							bounds.y+= y - referencey;
							bounds.height-= y - referencey;
							if(bounds.y<0){
								bounds.y=0;
								bounds.height=yo;
							}
							if(bounds.height<minHeight){
								bounds.height=minHeight;
								bounds.y=yo-bounds.height;
							}
						}
						
					}else if(part==":ne"){
						//int xo=bounds.x;
						int yo=bounds.y+bounds.height;
						bounds.width += x - referencex;
						bounds.y+= y - referencey;
						bounds.height-= y - referencey;
						if(bounds.y<0){
							bounds.y=0;
							bounds.height=yo;
						}
						if(bounds.width>parents.width-bounds.x)bounds.width=parents.width-bounds.x;
						if(bounds.width<minWidth){
							bounds.width=minWidth;
						}
						if(bounds.height<minHeight){
							bounds.height=minHeight;
							bounds.y=yo-bounds.height;
						}
					}else if(part==":sw"){
						int xo=bounds.x+bounds.width;
						bounds.x+= x - referencex;
						bounds.width -= x - referencex;
						bounds.height += y - referencey;
						if(bounds.x<0){
							bounds.x=0;
							bounds.width=xo;
						}
						if(bounds.width<minWidth){
							bounds.width=minWidth;
							bounds.x=xo-bounds.width;
						}
						if(bounds.height>parents.height-bounds.y)bounds.height=parents.height-bounds.y;
						if(bounds.height<minHeight)bounds.height=minHeight;
						
					}
					/*
					if ((part == ":nw") || (part == ":n") || (part == ":ne")) {
						bounds.y += y - referencey;
						bounds.height -= y - referencey;
					}
					if ((part == ":ne") || (part == ":e") || (part == ":se")) {
						bounds.width += x - referencex;
					}
					if ((part == ":sw") || (part == ":s") || (part == ":se")) {
						bounds.height += y - referencey;
					}
					if ((part == ":nw") || (part == ":w") || (part == ":sw")) {
						bounds.x += x - referencex;
						bounds.width -= x - referencex;
					}
					
					
					setRectangle(component, "bounds",
							Math.max(0, bounds.x),
							Math.max(0, bounds.y),
							Math.min(bounds.width, parents.width-bounds.x),
							Math.min(bounds.height, parents.height-bounds.y));
					*/
					referencex = x;
					referencey = y;
					doLayout(component);
					repaint(component);
				} else if (id == AWTMouseEvent.MOUSE_ENTERED) {
					awtComponent.style.cursor=(Cursor
							.getPredefinedCursor((part == ":n") ? Cursor.N_RESIZE_CURSOR
									: (part == ":ne") ? Cursor.NE_RESIZE_CURSOR
											: (part == ":e") ? Cursor.E_RESIZE_CURSOR
													: (part == ":se") ? Cursor.SE_RESIZE_CURSOR
															: (part == ":s") ? Cursor.S_RESIZE_CURSOR
																	: (part == ":sw") ? Cursor.SW_RESIZE_CURSOR
																			: (part == ":w") ? Cursor.W_RESIZE_CURSOR
																					: Cursor.NW_RESIZE_CURSOR));
				} else if (id == AWTMouseEvent.MOUSE_EXITED) {
					awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}

		if (popuptrigger) {// && (id == AWTMouseEvent.MOUSE_RELEASED)) {
			if(getComponentClass(component)=="bean"){
				CustomComponent c=getComponent(component,"bean");
				if(c.handlePopUp(x, y))return ;
			}

			Object popupmenu = get(component, "popupmenu");
			if (popupmenu != null) {
				putProperty(popupmenu,"invoker",component);
				popupPopup(popupmenu, x, y);
				
			}
		}
	}

	/**
	 * Calculate the given point in a component relative to the thinlet desktop
	 * and set as reference value
	 * 
	 * @param component
	 *            a widget
	 * @param x
	 *            reference point relative to the component left edge
	 * @param y
	 *            relative to the top edge
	 */
	private void setReference(Object component, int x, int y) {
		referencex = x;
		referencey = y;
		for (; component != null; component = getParent(component)) {
			Rectangle bounds = getRectangle(component, "bounds");
			referencex += bounds.x;
			referencey += bounds.y;

			Rectangle port = getRectangle(component, ":port");
			if (port != null) { // content scrolled
				Rectangle view = getRectangle(component, ":view");
				referencex -= view.x - port.x;
				referencey -= view.y - port.y;
			}
		}
	}

	/**
	 *
	 */
	private void select(Object component, Object row, boolean recursive,
			boolean shiftdown, boolean controldown) {
		String selection = getString(component, "selection", "single");
		Object lead = null;
		if (shiftdown && (selection != "single")
				&& ((lead = get(component, ":lead")) != null)) {
			extend(component, lead, row, recursive);
		} else {
			if (controldown && (selection == "multiple")) {
				setBoolean(row, "selected",
						!getBoolean(row, "selected", false), false);
				repaint(component, null, row);
				invoke(component, row, "action");
				set(component, ":anchor", null);
			} else if (controldown && getBoolean(row, "selected", false)) {
				for (Object item = row; item != null; item = getNextItem(
						component, item, recursive)) {
					if (setBoolean(item, "selected", false, false)) {
						repaint(component, null, item);
					}
				}
				invoke(component, row, "action");
				set(component, ":anchor", null);
			} else {
				selectItem(component, row, recursive);
			}
		}
		setLead(component, (lead != null) ? lead : get(component, ":lead"), row);
	}

	/**
	 * Find the next item after the given
	 * 
	 * @param component
	 *            a list/tree/table widget
	 * @param item
	 *            the next item after this, or the first if null
	 * @param recursive
	 *            true if tree
	 * @return next (or first) item
	 */
	private Object getNextItem(Object component, Object item, boolean recursive) {
		if (!recursive) {
			return get(item, ":next");
		}
		Object next = get(item, ":comp");
		if ((next == null) || !getBoolean(item, "expanded", true)) {
			while ((item != component) && ((next = get(item, ":next")) == null)) {
				item = getParent(item);
			}
		}
		return next;
	}

	/**
	 *
	 */
	private void processField(int x, int y, int clickcount, int id,
			Object component, boolean multiline, boolean hidden, int left) {
		if (id == AWTMouseEvent.MOUSE_PRESSED ||(id == AWTMouseEvent.MOUSE_RELEASED && clickcount>0) ) {
			// + middle=alt paste clipboard content
			setReference(component, 2*margin_1 + left, 2*margin_1);
			int mx = x - referencex;
			int my = 0;
			if (!multiline) {
				mx += getInteger(component, ":offset", 0);
			} else {
				Rectangle port = getRectangle(component, ":port");
				mx += port.x - margin_1;
				my = y - referencey + port.y - margin_1;
			}
			int caretstart = getCaretLocation(component, mx, my, multiline,
					hidden);
			int caretend = caretstart;
			
			if (clickcount > 1) {
				String text = getString(component, "text", "");
				while ((caretstart > 0)
						&& ((clickcount == 2) ? Character.isLetterOrDigit(text
								.charAt(caretstart - 1)) : (text
								.charAt(caretstart - 1) != '\n'))) {
					caretstart--;
				}
				while ((caretend < text.length())
						&& ((clickcount == 2) ? Character.isLetterOrDigit(text
								.charAt(caretend))
								: (text.charAt(caretend) != '\n'))) {
					caretend++;
				}
			}
			setInteger(component, "start", caretstart, 0);
			setInteger(component, "end", caretend, 0);
			if(id != AWTMouseEvent.MOUSE_PRESSED)return ;
			setFocus(component);
			validate(component); // caret check only
		} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
			int mx = x - referencex;
			int my = 0;
			if (!multiline) {
				mx += getInteger(component, ":offset", 0);
			} else {
				Rectangle port = getRectangle(component, ":port");
				mx += port.x - margin_1;
				my = y - referencey + port.y - margin_1;
			}
			int dragcaret = getCaretLocation(component, mx, my, multiline,
					hidden);
			if (dragcaret != getInteger(component, "end", 0)) {
				setInteger(component, "end", dragcaret, 0);
				validate(component); // caret check only
			}
		} else if ((id == AWTMouseEvent.MOUSE_ENTERED) && (mousepressed == null)) {
			awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		} else if (((id == AWTMouseEvent.MOUSE_EXITED) && (mousepressed == null))
				|| ((id == AWTMouseEvent.MOUSE_RELEASED) && ((mouseinside != component) || (insidepart != null)))) {
			awtComponent.style.cursor=(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 *
	 */
	private int getCaretLocation(Object component, int x, int y,
			boolean multiline, boolean hidden) {
		Font currentfont = getFont(component);
		
		char[] chars = multiline ? ((char[]) get(component, ":text"))
				: getString(component, "text", "").toCharArray(); // update it
		int linestart = 0; // find the line start by y value
		DrawStyle[] styles=(DrawStyle[]) getProperty(component,"drawstyle");
		if (multiline) {
			int height = 0; // height of line
			for (int i = 0; (y >= height) && (i < chars.length); i++) {
				if ((chars[i] == '\n') || (chars[i] == '\t')) {
					y-= height;
					height = (styles==null?currentfont.getHeight():getCharsHeight(chars,linestart,i-linestart,styles));
					if(y < height)break;
					linestart = i + 1;
				}
			}
		}
		for (int i = linestart; i < chars.length; i++) {
			if ((chars[i] == '\n') || (chars[i] == '\t')) {
				return i;
			}
			int charwidth =(styles!=null)?getDrawStyle(i,styles).font.charWidth(chars[i]):currentfont.charWidth(hidden ? '*' : chars[i]);
			if (x <= (charwidth / 2)) {
				return i;
			}
			x -= charwidth;
		}
		return chars.length;
	}

	/**
	 *
	 */
	private boolean processScroll(int x, int y, int id, Object component,
			Object part,boolean touchEvent) {
		if ((part == "up") || (part == "down") || (part == "left")
				|| (part == "right")) {
			if ((id == AWTMouseEvent.MOUSE_ENTERED)
					|| (id == AWTMouseEvent.MOUSE_EXITED)
					|| (id == AWTMouseEvent.MOUSE_PRESSED)
					|| (id == AWTMouseEvent.MOUSE_RELEASED)) {
				if (id == AWTMouseEvent.MOUSE_PRESSED) {
					if (processScroll(component, part)) {
						setTimer(300,1);
						return true;
					}
				} else {
					if (id == AWTMouseEvent.MOUSE_RELEASED) {
						setTimer(0,0);
					}
					repaint(component, null, part);
				}
			}
		} else if ((part == "uptrack") || (part == "downtrack")
				|| (part == "lefttrack") || (part == "righttrack")) {
			if (id == AWTMouseEvent.MOUSE_PRESSED) {
				if (processScroll(component, part)) {
					setTimer(300,1);
				}
			} else if (id == AWTMouseEvent.MOUSE_RELEASED) {
				setTimer(0,0);
			}
		} else if (touchEvent || part == "vknob" || part == "hknob") {
			if (id == AWTMouseEvent.MOUSE_PRESSED) {
				Rectangle port = getRectangle(component, ":port");
				Rectangle view = getRectangle(component, ":view");
				if(view==null||port==null)return false;
				if (part == "hknob") {
					referencex = x - view.x * (port.width - 2 * block)
							/ view.width;
				} else if(part=="vknob"){
					referencey = y - view.y * (port.height - 2 * block)
							/ view.height;
				}else {
					referencex = x - view.x * (port.width - 2 * block)
							/ view.width;
					referencey = y - view.y * (port.height - 2 * block)
							/ view.height;
				}
			} else if (id == AWTMouseEvent.MOUSE_DRAGGED) {
				Rectangle port = getRectangle(component, ":port");
				Rectangle view = getRectangle(component, ":view");
				if(view==null||port==null)return false;
				if (part == "hknob") {
					int viewx = (x - referencex) * view.width
							/ (port.width - 2 * block);
					viewx = Math.max(0,
							Math.min(viewx, view.width - port.width));
					if (view.x != viewx) {
						view.x = viewx;
						repaint(component, null, "horizontal");
					}
				} else if (part == "vknob") {
					int viewy = (y - referencey) * view.height
							/ (port.height - 2 * block);
					viewy = Math.max(0,
							Math.min(viewy, view.height - port.height));
					if (view.y != viewy) {
						view.y = viewy;
						repaint(component, null, "vertical");
					}
				}else {
					int viewx = -(x - referencex) * view.width
							/ (port.width - 2 * block);
					int viewy = -(y - referencey) * view.height
							/ (port.height - 2 * block);
					viewx = Math.max(0,
							Math.min(viewx, view.width - port.width));
					viewy = Math.max(0,
							Math.min(viewy, view.height - port.height));
					if (view.x != viewx ||view.y != viewy) {
						view.x = viewx;
						view.y = viewy;
						repaint(component, null, "horizontal");
					}else {
						return false;
					}
				}
			}else {
				return false;
			}
			//if(input.mouse.activeFingerCount>0 && !(id==AWTMouseEvent.MOUSE_DRAGGED))return false;
		} else if (part == "corner") {
			part = "corner"; // compiler bug
		} else { // ?
			if (id == AWTMouseEvent.MOUSE_PRESSED) {
				Rectangle port = getRectangle(component, ":port");
				if (port != null) {
					setReference(component, port.x, port.y);
				}
			}
			return false;
		}
		return true;
	}

	/**
	 *
	 */
	private boolean processScroll(Object component, Object part) {
		Rectangle view = getRectangle(component, ":view");
		Rectangle port = ((part == "left") || (part == "up")) ? null
				: getRectangle(component, ":port");
		int dx = 0;
		int dy = 0;
		if (part == "left") {
			dx = -10*margin_1;
		} else if (part == "lefttrack") {
			dx = -port.width;
		} else if (part == "right") {
			dx = 10*margin_1;
		} else if (part == "righttrack") {
			dx = port.width;
		} else if (part == "up") {
			dy = -10*margin_1;
		} else if (part == "uptrack") {
			dy = -port.height;
		} else if (part == "down") {
			dy = 10*margin_1;
		} else if (part == "downtrack") {
			dy = port.height;
		}
		if (dx != 0) {
			dx = (dx < 0) ? Math.max(-view.x, dx) : Math.min(dx, view.width
					- port.width - view.x);
		} else if (dy != 0) {
			dy = (dy < 0) ? Math.max(-view.y, dy) : Math.min(dy, view.height
					- port.height - view.y);
		} else
			return false;
		if ((dx == 0) && (dy == 0)) {
			return false;
		}
		view.x += dx;
		view.y += dy;
		repaint(component, null, (dx != 0) ? "horizontal" : "vertical");
		return (((part == "left") || (part == "lefttrack")) && (view.x > 0))
				|| (((part == "right") || (part == "righttrack")) && (view.x < view.width
						- port.width))
				|| (((part == "up") || (part == "uptrack")) && (view.y > 0))
				|| (((part == "down") || (part == "downtrack")) && (view.y < view.height
						- port.height));
	}

	/**
	 *
	 */
	private boolean processSpin(Object component, Object part) {
		String text = getString(component, "text", "");
		try {
			double itext =Double.parseDouble(text);
				
			double step = getDouble(component, "step", 1);

			if ((part == "up") ? (itext + step <= getDouble(component,
					"maximum", Integer.MAX_VALUE))
					: (itext - step >= getDouble(component, "minimum",
							Integer.MIN_VALUE))) {
				itext=(part == "up") ? (itext + step)
						: (itext - step);
				//itext=MathUtils.roundOffToPreferredSigFigures(itext);
				double value = itext;
				
				double minimum = getDouble(component, "minimum");
				double maximum = getDouble(component, "maximum");
				double oldValue = getDouble(component, "value");
				value=Math.round((value-minimum)/step)*step+minimum;
				if(value<minimum ||value>maximum){
					value=MathUtils.clamp(value, minimum, maximum);
					value=Math.floor((value-minimum)/step)*step+minimum;
				}
				value=MathUtils.roundOffToSigFigures(value,3);//Preferences.significantFigures+2);
				setDouble(component,"value",value);
				setDouble(mouseinside,"value",value);
				setString(mouseinside, "text", ""+value);
				setInteger(component, "start", (""+value).length(), 0);
				setInteger(component, "end", 0, 0);
				repaint(component, "spinbox", "text");
				
				if(value!=oldValue)invoke(component, null, "action");
				return true;
			}
		} catch (NumberFormatException nfe) {
		}
		
		String oldValue = getDouble(component, "value")+"";
		setString(mouseinside, "text", oldValue);
		setInteger(component, "start", oldValue.length(), 0);
		setInteger(component, "end", 0, 0);
		return false;
	}
	
	
	

	/**
	 *
	 */
	private boolean invoke(Object component, Object part, String event) {

		Object method = get(component, event);
		boolean b=false;
		if (method != null) {
			invokeImpl(method, part);
			b= true;
		}
		if(defaultHandler!=null){
			if(event.equals("action")){
				defaultHandler.onAction(component, event);
				b= true;
			}else if(event.equals("perform")){
				defaultHandler.onPerform(component, event,part);
				b= true;
			}
		}
		repaintNeeded=true;
		return b;
	}

	/**
	 *
	 */
	private void invokeImpl(Object method, Object part) {
		if (method == null)
			return;
		Object[] data = (Object[]) method;
		if (data[1] == null)
			return;
		boolean script =false;//!(data[1] instanceof Method);
		Object[] args = (data.length > 2) ? new Object[(data.length - 2) / 3] : null;
		if (args != null)
			for (int i = 0; i < args.length; i++) {
				Object target = data[2 + 3 * i];
				if ("gui" == target) {
					args[i] = this;
				} else if (("constant" == target)) { // constant value
					args[i] = data[2 + 3 * i + 1];
				} else {
					if ("item" == target) {
						target = part;
					}
					Object parametername = data[2 + 3 * i + 1];
					if (parametername == null) {
						args[i] = target;
						//if(script)args[i]=Widgets.getWrappedWidget(args[i]);
						// args[i] = new Widget(this, target);
					} else {
						args[i] = (target != null) ? get(target, parametername) : null;
						if (args[i] == null) {
							args[i] = data[2 + 3 * i + 2];
						}
					}
				}
			}
		try {
			if (data[1] == null)
				return;
			//if (data[1] instanceof Method) {
			//	((Method) data[1]).invoke(data[0], args);
			//} else {
				//ScriptManager.runEventFunction((String) data[1], args);
			//}
		} catch (Throwable throwable) {
			handleException(throwable);
		}
	}

	/**
	 * Overwrite this method to handle exceptions thrown by the invoked custom
	 * methods
	 *
	 * @param throwable
	 *            the thrown exception by the bussiness logic
	 */
	protected void handleException(Throwable throwable) {
		//if(Simphy.DEBUG)throwable.printStackTrace();
		//MyLogger.LOGGER.log(Level.WARNING, throwable.getMessage(), throwable);
	}

	/**
	 *Called by handleMouse()
	 *Sets 'mousenside' 'insidepart' objects as current widget and part of widget respectively which are under under mouse
	 *Sets it null if no widget is under mouse
	 */
	private boolean findComponent(Object component, int x, int y) {
		if (component == content) {
			mouseinside = insidepart = null;
			mousex = x;
			mousey = y;
		}
		if (!getBoolean(component, "visible", true)) {
			return false;
		}
		Rectangle bounds = getRectangle(component, "bounds");
		if ((bounds == null) || !(bounds.contains(x, y))) {
			return false;
		}
		mouseinside = component;
		x -= bounds.x;
		y -= bounds.y;
		String classname = getComponentClass(component);

		if ("combobox" == classname) {
			if (getBoolean(component, "editable", true)
					&& (x <= bounds.width - block)) {
				AWTImage icon = getIcon(component, "icon", null);
				insidepart = ((icon != null) && (x <= 2 + icon.getScaledWidth())) ? "icon"
						: null;
			} else {
				insidepart = "down";
			}
		} else if ("bean" == classname) {
			set(component, ":mousex", new Integer(x));
			set(component, ":mousey", new Integer(y));
		} else if (":combolist" == classname) {
			if (!findScroll(component, x, y)) {
				y += getRectangle(component, ":view").y;
				for (Object choice = get(get(component, "combobox"), ":comp"); choice != null; choice = get(
						choice, ":next")) {
					Rectangle r = getRectangle(choice, "bounds");
					if ((y >= r.y) && (y < r.y + r.height)) {
						insidepart = choice;
						break;
					}
				}
			}
		} else if ("textarea" == classname) {
			findScroll(component, x, y);
		} else if ("tabbedpane" == classname) {
			int selected = getInteger(component, "selected", 0);
			int i = 0;
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				Rectangle r = getRectangle(tab, "bounds");
				if (i == selected) {
					Object tabcontent = get(tab, ":comp");
					if ((tabcontent != null)
							&& findComponent(tabcontent, x - r.x, y - r.y)) {
						break;
					}
				}
				if (r.contains(x, y)) {
					insidepart = tab;
					break;
				}
				i++;
			}
		} else if (("panel" == classname) || ("desktop" == classname)
				|| ("dialog" == classname)) {
			if ("dialog" == classname) {
				int titleheight = getInteger(component, ":titleheight", 0);
				boolean resizable = getBoolean(component, "resizable", false)&& get(component, ":minimized")==null;
				int margin=4;
				if (resizable && (x < margin)) {
					insidepart = (y < block) ? ":nw" : (y >= bounds.height
							- block) ? ":sw" : ":w";
				} else if (resizable && (y < margin)) {
					insidepart = (x < block) ? ":nw" : (x >= bounds.width
							- block) ? ":ne" : ":n";
				} else if (resizable && (x >= bounds.width - margin)) {
					insidepart = (y < block) ? ":ne" : (y >= bounds.height
							- block) ? ":se" : ":e";
				} else if (resizable && (y >= bounds.height - margin)) {
					insidepart = (x < block) ? ":sw" : (x >= bounds.width
							- block) ? ":se" : ":s";
				} else {
					if (y < 4 + titleheight) {
						insidepart = "header";
					}
				}
				// close/maximize/iconify dialog button
				int buttonX = bounds.width - titleheight - 1;
				int buttonY = 3;
				if (getBoolean(component, "closable",true)) {
					if (x > buttonX && x < buttonX + titleheight - 2
							&& y > buttonY && y < buttonY + titleheight - 2)
						insidepart = ":closebutton";
					buttonX -= titleheight;
				}
				if(getBoolean(component, "maximizable",false)){
					buttonX -= titleheight;
					if (x > buttonX && x < buttonX + titleheight - 2
							&& y > buttonY && y < buttonY + titleheight - 2)
						insidepart = ":maximizebutton";
				}
				if(getBoolean(component, "iconifiable",true)){
					//buttonX -= titleheight;
					if (x > buttonX && x < buttonX + titleheight - 2
							&& y > buttonY && y < buttonY + titleheight - 2){
						insidepart = ":iconifybutton";
					}
				}
			}
			if ((insidepart == null) && !findScroll(component, x, y)) {
				Rectangle port = getRectangle(component, ":port");
				if (port != null) { // content scrolled
					Rectangle view = getRectangle(component, ":view");
					x += view.x - port.x;
					y += view.y - port.y;
				}
				for (Object comp = get(component, ":comp"); comp != null; comp = get(
						comp, ":next")) {
					if (findComponent(comp, x, y)) {
						break;
					}
					if (("desktop" == classname)
							&& getBoolean(comp, "modal", false)) {
						insidepart = "modal";
						break;
					} // && dialog
				}
			}
		} else if ("spinbox" == classname) {
			insidepart = (x <= bounds.width - block) ? null
					: ((y <= bounds.height / 2) ? "up" : "down");
		} else if ("splitpane" == classname) {
			Object comp1 = get(component, ":comp");
			if (comp1 != null) {
				if (!findComponent(comp1, x, y)) {
					Object comp2 = get(comp1, ":next");
					if (comp2 != null) {
						findComponent(comp2, x, y);
					}
				}
			}
		} else if ("list" == classname) {
			findScroll(component, x, y);
		} else if ("table" == classname) {
			// check if we are inside the header, then if we have an action on
			// this header or it is a
			// resizable one, do further checks to see if we need to change the
			// column drawing states or
			// even need to invoke the action or to resize the column. We do not
			// need to make this whole
			// block if we are resizing a column right now
			Object header = get(component, "header");
			if (header != null && get(header, ":resizing") == null) {
				boolean isResizable = getBoolean(header, "resizable");
				boolean hasAction = null != get(header, "action");
				if (isResizable || hasAction) {
					Rectangle view = getRectangle(component, ":view");
					Rectangle port = getRectangle(component, ":port");
					if (0 < x && x < port.width && 0 < y && y < port.y - 1) {
						int[] columnwidths = (int[]) get(component, ":widths");
						Object column = get(header, ":comp");
						int left = -view.x;
						for (int i = 0; i < columnwidths.length; i++) {
							if (i != 0) {
								column = get(column, ":next");
							}
							int width = (i == columnwidths.length - 1) ? (view.width
									- left + 2)
									: columnwidths[i];
							if (isResizable
									&& ((x > left + width - 4 && x < left
											+ width) || (i < columnwidths.length - 1
											&& x >= left + width && x < left
											+ width + 4))) {
								set(header, ":resizecomponent", column);
								break;
							} else {
								set(header, ":resizecomponent", null);
								if (hasAction && getCount(component) > 0
										&& left < x && x < left + width) {
									insidepart = column;
									break;
								}
							}
							left += width;
						}
					} else if (isResizable) {
						set(header, ":resizecomponent", null);
						set(header, ":resizing", null);
					}
				}
			}
			if (insidepart == null && get(header, ":resizecomponent") == null)
				findScroll(component, x, y);
		} else if ("tree" == classname) {
			findScroll(component, x, y);
		} else if ("menubar" == classname) {
			for (Object menu = get(component, ":comp"); menu != null; menu = get(
					menu, ":next")) {
				Rectangle r = getRectangle(menu, "bounds");
				if ((x >= r.x) && (x < r.x + r.width)) {
					insidepart = menu;
					break;
				}
			}
		} else if (":popup" == classname) {
			for (Object menu = get(get(component, "menu"), ":comp"); menu != null; menu = get(
					menu, ":next")) {
				if(!getBoolean(menu,"visible",true))continue;
				Rectangle r = getRectangle(menu, "bounds");
				if ((y >= r.y) && (y < r.y + r.height)) {
					insidepart = menu;
					break;
				}
			}
			if(getComponentClass(insidepart)=="panel"){
				Object pnl=insidepart;
				insidepart=null;
				findComponent(pnl, x, y);
			}
		}
		return true;
	}

	/**
	 * @param component
	 *            a scrollable widget
	 * @param x
	 *            point x location
	 * @param y
	 *            point y location
	 * @return true if the point (x, y) is inside scroll-control area
	 *         (scrollbars, corners, borders), false otherwise (vievport,
	 *         header, or no scrollpane)
	 */
	private boolean findScroll(Object component, int x, int y) {
		Rectangle port = getRectangle(component, ":port");
		if ((port == null) || port.contains(x, y)) {
			return false;
		}
		Rectangle view = getRectangle(component, ":view");
		Rectangle horizontal = getRectangle(component, ":horizontal");
		Rectangle vertical = getRectangle(component, ":vertical");
		if ((horizontal != null) && horizontal.contains(x, y)) {
			findScroll(x - horizontal.x, horizontal.width, port.width, view.x,
					view.width, true);
		} else if ((vertical != null) && vertical.contains(x, y)) {
			findScroll(y - vertical.y, vertical.height, port.height, view.y,
					view.height, false);
		} else {
			insidepart = "corner";
		}
		return true;
	}

	/**
	 * @param p
	 *            x or y relative to the scrollbar begin
	 * @param size
	 *            scrollbar width or height
	 * @param portsize
	 *            viewport width or height
	 * @param viewp
	 *            view x or y
	 * @param viewsize
	 *            view width or height
	 * @param horizontal
	 *            if true horizontal, vertical otherwise
	 */
	private void findScroll(int p, int size, int portsize, int viewp,
			int viewsize, boolean horizontal) {
		if (p < block) {
			insidepart = horizontal ? "left" : "up";
		} else if (p > size - block) {
			insidepart = horizontal ? "right" : "down";
		} else {
			int track = size - 2 * block;
			if (track < 10*margin_1) {
				insidepart = "corner";
				return;
			} // too small
			int knob = Math.max(track * portsize / viewsize, 10);
			int decrease = viewp * (track - knob) / (viewsize - portsize);
			if (p < block + decrease) {
				insidepart = horizontal ? "lefttrack" : "uptrack";
			} else if (p < block + decrease + knob) {
				insidepart = horizontal ? "hknob" : "vknob";
			} else {
				insidepart = horizontal ? "righttrack" : "downtrack";
			}
		}
	}

	/**
	 *
	 */
	private void repaint(Object component, Object classname, Object part) {
		/*
		Rectangle b = getRectangle(component, "bounds");
		if (classname == "combobox") { // combobox down arrow
			repaint(component, b.x + b.width - block, b.y, block, b.height); // icon?+
		} else if (classname == "spinbox") {
			if (part == "text") { // spinbox textfield content
				repaint(component, b.x, b.y, b.width - block, b.height);
			} else { // spinbox increase or decrease button
				repaint(component, b.x + b.width - block, (part == "up") ? b.y
						: (b.y + b.height - b.height / 2), block, b.height / 2);
			}
		}
		// else if (classname == "dialog") {}
		// int titleheight = getInteger(component, ":titleheight", 0);
		// else if (classname == "splitpane") {}
		else if ((classname == "tabbedpane") || // tab
				(classname == "menubar") || (classname == ":popup")) { // menuitem
			Rectangle r = getRectangle(part, "bounds");
			repaint(component, b.x + r.x, b.y + r.y,
					(classname == ":popup") ? b.width : r.width, r.height);
		}
		// classname: ":combolist" "textarea" "list" "table" "tree"
		else if ((part == "left") || (part == "right")) { // horizontal
															// scrollbar button
			Rectangle r = getRectangle(component, ":horizontal");
			repaint(component, b.x
					+ ((part == "left") ? r.x : (r.x + r.width - block)), b.y
					+ r.y, block, r.height);
		} else if ((part == "up") || (part == "down")) { // vertical scrollbar
															// button
			Rectangle r = getRectangle(component, ":vertical");
			repaint(component, b.x + r.x, b.y
					+ ((part == "up") ? r.y : (r.y + r.height - block)),
					r.width, block);
		} else if ((part == "text") || (part == "horizontal")
				|| (part == "vertical")) {
			Rectangle port = getRectangle(component, ":port"); // textarea or
																// content
			repaint(component, b.x + port.x, b.y + port.y, port.width,
					port.height);
			if (part == "horizontal") {
				Rectangle r = getRectangle(component, ":horizontal");
				repaint(component, b.x + r.x, b.y + r.y, r.width, r.height);
				repaint(component, b.x + r.x, b.y, r.width, port.y); // paint
																		// header
																		// too
			} else if (part == "vertical") {
				Rectangle r = getRectangle(component, ":vertical");
				repaint(component, b.x + r.x, b.y + r.y, r.width, r.height);
			}
		} else { // repaint the whole line of its subcomponent
			Rectangle port = getRectangle(component, ":port");
			Rectangle view = getRectangle(component, ":view");
			Rectangle r = getRectangle(part, "bounds");
			if ((r.y + r.height >= view.y) && (r.y <= view.y + port.height)) {
				repaint(component, b.x + port.x, b.y + port.y - view.y + r.y,
						port.width, r.height);
				// ? need cut item rectangle above/bellow viewport
			}
		}
		*/
		repaint(g);
	}

	protected void reValidateGui(){
		//ImageFont.updateImages();
		try {
		doLayout(content);
		}catch(Exception e) {
			e.printStackTrace();
		}
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("called gui.reValidateGui");
	}
	/**
	 * Layout and paint the given component later
	 * 
	 * @param component
	 */
	private void validate(Object component) {
		repaint(component);
		Rectangle bounds = getRectangle(component, "bounds");
		if (bounds != null) {
			bounds.width = -1 * Math.abs(bounds.width);
		}
	}

	/**
	 * Repaint the given component's area later
	 * 
	 * @param component
	 *            a visible widget inside thinlet desktop
	 */
	public void repaint(Object component) {
		repaintNeeded=true;
	}

	/**
	 * Repaint the given component's area later
	 * 
	 * @param component
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void repaint(Object component, int x, int y, int width, int height) {
		repaintNeeded=true;
		/*
		while ((component = getParent(component)) != null) {
			Rectangle bounds = getRectangle(component, "bounds");
			x += bounds.x;
			y += bounds.y;
			Rectangle view = getRectangle(component, ":view");
			if (view != null) {
				Rectangle port = getRectangle(component, ":port");
				x += -view.x + port.x;
				y += -view.y + port.y; // + clip :port
			}
		}
		repaint(x, y, width, height);
*/
		
	}

	/**
	 * Requests that both the <i>Thinlet</i> component, and the given widget get
	 * the input focus
	 *
	 * @param component
	 *            a focusable widget inside visible and enabled parents, and
	 *            tabbedpane's selected tab
	 * @return true, if the given component was focusable
	 */
	public boolean requestFocus(Object component) { // #
		if (isFocusable(component, true)) {
			focusinside=true;
			setFocus(component);
			focusowner=component;
			mousepressed=component;
			mouseinside=component;
			repaint(component);
			return true;
		}
		return false;
	}
	
	
	public Object getFocusedWidget(){
		return focusowner;
	}

	/**
	 * Request focus for the given component
	 * 
	 * @param component
	 *            a focusable component
	 * @return true if the focusowner was changed, otherwise false
	 */
	public boolean setFocus(Object component) {
		
		
		if(component==null &&focusowner!=null){
			// invoke the focus listener of the previously focused component
			invoke(focusowner, null, "focuslost");
			focusowner=null;
			//System.out.println("lost Focus ="+getString(focusowner,"name"));
			return true;
		}
		if (!focusinside) { // request focus for the thinlet component
			if(awtComponent!=null){
				//awtComponent.requestFocus();
				focusinside = true;
			}
		}
		
		
		

		if (focusowner != component && getBoolean(component,"enabled",true)) {
			
			Object focused = focusowner;
			if (focusowner != null) {
				focusowner = null; // clear focusowner
				repaint(focused);
				// invoke the focus listener of the previously focused component
				invoke(focused, null, "focuslost");
				
			}
			if (focusowner == null) { // it won't be null, if refocused
				focusowner = component;
				// invoke the focus listener of the new focused component
				invoke(component, null, "focusgained");
				
			}
			
			return true;
		}

		return false;
	}

	/**
	 * @return next focusable component is found (not the first of the
	 *         desktop/dialog)
	 */
	private boolean setNextFocusable(Object current, boolean outgo) {
		boolean consumed = true;
		outgo=true;
		for (Object next = null, component = current; true; component = next) {
			next = get(component, ":comp"); // check first subcomponent
			if (next == null) {
				next = get(component, ":next");
			} // check next component
			while (next == null) { // find the next of the parents, or the
									// topmost
				component = getParent(component); // current is not on the
													// desktop
				if (component == null) {
					return false;
				}
				if ((component == content)
						|| ((getComponentClass(component) == "dialog") && (!outgo || getBoolean(
								component, "modal", false)))) {
					consumed = false; // find next focusable but does not
										// consume event
					next = component; // the topmost (desktop or modal dialog)
				} else {
					next = get(component, ":next");
				}
			}
			if (next == current) {
				return false;
			} // one fucusable, no loop
			if (isFocusable(next, false)) {
				setFocus(next);
				return consumed;
			}
		}
	}

	/**
	 * @return previous focusable component is found (not the last of the
	 *         desktop/dialog)
	 */
	private boolean setPreviousFocusable(Object component, boolean outgo) {
		for (int i = 0; i < 2; i++) { // 0 is backward direction
			Object previous = getPreviousFocusable(component, null, true,
					false, (i == 0), outgo);
			if (previous != null) {
				setFocus(previous);
				return (i == 0);
			}
		}
		return false;
	}

	/**
	 * For the starting component search its parent direction for a focusable
	 * component, and then its next component (if not search backward from the
	 * component).<br />
	 * For its parent components check its first component, the current one, and
	 * its parent direction (backward search), or its parent, then next
	 * component (forward direction).<br />
	 * For the rest components check the next, then the first subcomponent
	 * direction, and finally check whether the component is focusable.
	 */
	private Object getPreviousFocusable(Object component, Object block,
			boolean start, boolean upward, boolean backward, boolean outgo) {
		Object previous = null;
		if ((component != null) && (component != block)) {
			boolean go = ((getComponentClass(component) != "dialog") || (outgo && !getBoolean(
					component, "modal", false)));
			if (!start && !upward && go) {
				previous = getPreviousFocusable(get(component, ":next"), block,
						false, false, backward, outgo);
			}
			if ((previous == null)
					&& ((upward && backward) || (!start && !upward))) {
				previous = getPreviousFocusable(get(component, ":comp"), block,
						false, false, backward, outgo);
				if ((previous == null) && isFocusable(component, false)) {
					previous = component;
				}
			}
			if ((previous == null) && (start || upward) && go) {
				previous = getPreviousFocusable(getParent(component),
						component, false, true, backward, outgo);
			}
			if ((previous == null) && (start || upward) && !backward && go) {
				previous = getPreviousFocusable(get(component, ":next"), block,
						false, false, backward, outgo);
			}
		}
		return previous;
	}

	/**
	 * Check whether the given widget can become focusowner
	 * 
	 * @param component
	 *            check this widget
	 * @param forced
	 *            splitpane is also checked (e.g. false for tab navigating, and
	 *            true for mouse selection or application request)
	 * @return true if focusable, otherwise false
	 */
	private boolean isFocusable(Object component, boolean forced) {
		String classname = getComponentClass(component);
		if ((classname == "panel") ||(classname == "button") || (classname == "checkbox")
				|| ("togglebutton" == classname) || (classname == "combobox")
				|| (classname == "textfield") || (classname == "passwordfield")
				|| (classname == "textarea") || (classname == "spinbox")
				|| (classname == "slider") || (classname == "list")
				|| (classname == "table") || (classname == "tree")
				|| (classname == "tabbedpane")
				|| (classname == "bean")
				|| (forced && (classname == "splitpane")
				|| (forced && (classname == "dialog")))) {
			for (Object comp = component; comp != null;) {
				// component and parents are enabled and visible
				if (!getBoolean(comp, "enabled", true)
						|| !getBoolean(comp, "visible", true)) {
					return false;
				}
				Object parent = getParent(comp);
				// inside the selected tabbedpane tab
				if ((getComponentClass(comp) == "tab")
						&& (getItem(parent, getInteger(parent, "selected", 0)) != comp)) {
					return false;
				}
				comp = parent;
			}
			return true;
		}
		return false;
	}

	// ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

	/**
	 * Creates a new component
	 *
	 * @param classname
	 *            the widget type (e.g. <i>button</i>)
	 * @return a new component, every component is simply an <i>Object</i>
	 * @throws java.lang.IllegalArgumentException
	 *             for unknown widget type
	 */
	public static Object create(String classname) { // #
		for (int i = 0; i < dtd.length; i += 3) {
			if (dtd[i].equals(classname)) {
				return createImpl((String) dtd[i]);
			}
		}
		throw new IllegalArgumentException("unknown " + classname);
	}

	/**
	 * Gets the type of the given component
	 *
	 * @param component
	 *            a widget
	 * @return the class name of the component (e.g. <i>button</i>)
	 */
	public static String getComponentClass(Object component) { // #
		return (String) get(component, ":class");
	}

	/**
	 * Get the topmost component
	 *
	 * @return the root object (it is a <i>desktop</i>), never <i>null</i>
	 */
	public Object getDesktop() {// #
		return content;
	}

	/**
	 *
	 */
	private static Object createImpl(String classname) {
		return new Object[] { ":class", classname, null };
	}

	/**
	 *
	 */
	static boolean set(Object component, Object key, Object value) {
		Object[] previous = (Object[]) component;
		for (Object[] entry = previous; entry != null; entry = (Object[]) entry[2]) {
			if (entry[0] == key) {
				if (value != null) { // set the row's value
					Object oldvalue = entry[1];
					entry[1] = value;
					return !value.equals(oldvalue);
				} else { // remove the row
					previous[2] = entry[2];
					entry[2] = null;
					return true;
				}
			}
			previous = entry;
		}
		if (value != null && previous!=null) { // append a new row
			previous[2] = new Object[] { key, value, null };
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	protected static Object get(Object component, Object key) {
		if(!(component instanceof Object[]))return null;
		for (Object[] entry = (Object[]) component; entry != null; entry = (Object[]) entry[2]) {
			if (entry[0] == key) {
				if(key=="next" &&  entry[1]==component){
					//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Next compo set as itself  in "+toString(component));
					return null;
				}
				return entry[1];
			}
		}
		return null;
	}

	/**
	 * Gets the count of subcomponents in the list of the given component
	 *
	 * @param component
	 *            a widget
	 * @return the number of components in this component
	 */
	public int getCount(Object component) {
		return getItemCountImpl(component, ":comp");
	}

	/**
	 * Gets the parent of this component
	 *
	 * @param component
	 *            a widget
	 * @return the parent container of this component or item
	 */
	public Object getParent(Object component) {
		return get(component, ":parent");
	}

	/**
	 * Gets the index of the first selected item in the given component
	 *
	 * @param component
	 *            a widget (combobox, tabbedpane, list, table, header, or tree)
	 * @return the first selected index or -1
	 */
	public int getSelectedIndex(Object component) {
		String classname = getComponentClass(component);
		if ((classname == "combobox") || (classname == "tabbedpane")) {
			return getInteger(component, "selected",
					(classname == "combobox") ? -1 : 0);
		}
		if ((classname == "list") || (classname == "table")
				|| (classname == "header") || (classname == "tree")) {
			Object item = get(component, ":comp");
			for (int i = 0; item != null; i++) {
				if (getBoolean(item, "selected", false)) {
					return i;
				}
				item = get(item, ":next");
			}
			return -1;
		}
		throw new IllegalArgumentException(classname);
	}

	/**
	 * Gets the first selected item of the given component
	 *
	 * @param component
	 *            a widget (combobox, tabbedpane, list, table, header or tree)
	 * @return the first selected item or null
	 */
	public Object getSelectedItem(Object component) {
		String classname = getComponentClass(component);
		if ((classname == "combobox") || (classname == "tabbedpane")) {
			int index = getInteger(component, "selected",
					(classname == "combobox") ? -1 : 0);
			return (index != -1) ? getItemImpl(component, ":comp", index)
					: null;
		}
		if ((classname == "list") || (classname == "table")
				|| (classname == "header") || (classname == "tree")) {
			for (Object item = findNextItem(component, classname, null); item != null; item = findNextItem(
					component, classname, item)) {
				if (getBoolean(item, "selected", false)) {
					return item;
				}
			}
			return null;
		}
		throw new IllegalArgumentException(classname);
	}

	/**
	 * Gets the selected item of the given component (list, table, or tree) when
	 * multiple selection is allowed
	 *
	 * @param component
	 *            a widget
	 * @return the array of selected items, or a 0 length array
	 */
	public Object[] getSelectedItems(Object component) {
		String classname = getComponentClass(component);
		Object[] selecteds = new Object[0];
		for (Object item = findNextItem(component, classname, null); item != null; item = findNextItem(
				component, classname, item)) {
			if (getBoolean(item, "selected", false)) {
				Object[] temp = new Object[selecteds.length + 1];
				System.arraycopy(selecteds, 0, temp, 0, selecteds.length);
				temp[selecteds.length] = item;
				selecteds = temp;
			}
		}
		return selecteds;
	}

	/**
	 * @return the first or the next item of the (list, table, or tree)
	 *         component
	 */
	private Object findNextItem(Object component, String classname, Object item) {
		if (item == null) { // first item
			return get(component, ":comp");
		} else if ("tree" == classname) { // next tree node
			Object next = get(item, ":comp");
			if ((next == null) || !getBoolean(item, "expanded", true)) { // no
																			// subnode
																			// or
																			// collapsed
				while ((item != component)
						&& ((next = get(item, ":next")) == null)) {
					item = getParent(item); // next node of in backward path
				}
			}
			return next;
		} else { // next list or tree item
			return get(item, ":next");
		}
	}

	/**
	 * Removes all the components from this container's specified list
	 *
	 * @param component
	 *            the specified container
	 */
	public void removeAll(Object component) {
		
		for (Object item = get(component, ":comp"); item != null; item = get(
				item, ":next")) {
			remove(item);
		}
		
		if (get(component, ":comp") != null) {
			set(component, ":comp", null);
			update(component, "validate");
		}
		

	}

	/**
	 * 
	 */
	private static int getItemCountImpl(Object component, String key) {
		int i = 0;
		for (Object comp = get(component, key); comp != null; comp = get(comp,
				":next")) {
			i++;
			
			if(i>100){
				//may stuck in loop if same itemis added twice to same parent
				//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("stuck in loop in gui.getItemCountImpl"+Gui.getClass(component));
				return 3;
			}
			
		}
		return i;
	}

	/**
	 * Returns the subcomponent of the given component's specified list at the
	 * given index
	 *
	 * @param component
	 *            a specified container
	 * @param index
	 *            the index of the component to get
	 * @return the index<sup>th</sup> component in this container
	 */
	public Object getItem(Object component, int index) {
		return getItemImpl(component, ":comp", index);
	}

	/**
	 * Gets all the components in this container
	 *
	 * @param component
	 *            a specified container
	 * @return an array of all the components in this container
	 */
	public static Object[] getItems(Object component) {
		Object[] items = new Object[getItemCountImpl(component, ":comp")];
		Object comp = get(component, ":comp");
		for (int i = 0; i < items.length; i++) {
			items[i] = comp;
			comp = get(comp, ":next");
		}
		return items;
	}

	/**
	 * Referenced by DOM, replace by getItem for others
	 */
	private static Object getItemImpl(Object component, Object key, int index) {
		int i = 0;
		for (Object item = get(component, key); item != null; item = get(item,
				":next")) {
			if (i == index) {
				return item;
			}
			i++;
		}
		return null;
	}

	/**
	 *
	 */
	protected int getIndex(Object component, Object value) {
		int index = 0;
		for (Object item = get(component, ":comp"); item != null; item = get(
				item, ":next")) {
			if (value == item) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * Adds the specified component to the root desktop
	 *
	 * @param component
	 *            a widget to be added
	 */
	public void add(Object component) {
		add(content, component, 0);
	}

	/**
	 * Adds the specified component to the root desktop at the given position
	 *
	 * @param component
	 *            a widget to be added
	 * @param index
	 * 			  the position at which to insert the component, or -1 to insert
	 *            the component at the end
	 */
	public void add(Object component,int index) {
		add(content, component, index);
	}

	/**
	 * Adds the specified component to the end of the specified container
	 *
	 * @param parent
	 *            a container
	 * @param component
	 *            a component to be added
	 */
	public void add(Object parent, Object component) {
		add(parent, component, -1);
	}

	/**
	 * Adds the specified component to the container at the given position
	 *
	 * @param parent
	 *            a container
	 * @param component
	 *            a component to be
	 *             inserted
	 * @param index
	 *            the position at which to insert the component, or -1 to insert
	 *            the component at the end
	 */
	public void add(Object parent, Object component, int index) {
		if(getParent(component)==parent){
			//return ;
			//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(new Serializer(this).serialize(parent, 0, ' ', ""));
			//return ;
		}
		addImpl(parent, component, index);
		addBeanToList(component);
		update(component, "validate");
		if ("dialog" == getComponentClass(component) && getBoolean(component, "modal")) {
			setModal(component,true);
		}
		if (parent == content) {
			setNextFocusable(component, false);
		}
	}

	public void setModal(Object dialog, boolean modal) {
		if ("dialog" != getComponentClass(dialog))
			return;
		setBoolean(dialog, "modal", modal);
		if (modal == true) {
			checkLocation();
			mousepressed = dialog;
			insidepart = "modal";
			setFocus(dialog);
			findComponent(content, mousex, mousey);
			// if(mousepressed!=mouseinside && mouseinside!=null && mouseinside!=content){
			if (dialog != null) {
				Object parent = getParent(dialog);
				if (get(parent, ":comp") != dialog) { // to front
					removeItemImpl(parent, dialog);
					insertItem(parent, ":comp", dialog, 0);
					set(dialog, ":parent", parent);
					repaint(dialog); // to front always...
					// setNextFocusable(component, false);
				}
			}
		}
	}
	/**
	 * Referenced by DOM
	 */
	private void insertItem(Object parent, Object key, Object component,
			int index) {
		Object item = parent, next = get(parent, key);
		
		for (int i = 0;; i++) {
			
			if ((i == index) || (next == null)) {
				set(item, key, component);
				set(component, ":next", next);
				break;
			}
			next = get(item = next, key = ":next");
		}
	}

	/**
	 * Remove the specified component from its parent list, or delete
	 * component's popupmenu or table's header
	 *
	 * @param component
	 *            the component to be removed
	 */
	public void remove(Object component) {
		update(component, "validate");
		Object parent = getParent(component);
		Object classname = getComponentClass(component);
		if (("popupmenu" == classname) || ("header" == classname)) {
			set(parent, classname, null);
		} else {
			removeBeanFromList(component);
			removeItemImpl(parent, component);
			// reuest focus for its parent if the component (or subcomponent) is
			// currently focused
			for (Object comp = focusowner; comp != null; comp = getParent(comp)) {
				if (comp == component) {
					setNextFocusable(parent, false);
					break;
				}
			}
		}
	}

	/**
	 * Delete the give component from its parent list
	 * 
	 * @param parent
	 * @param component
	 */
	private void removeItemImpl(Object parent, Object component) {
		Object previous = null; // the widget before the given component
		for (Object comp = get(parent, ":comp"); comp != null;) {
			Object next = get(comp, ":next");
			if (next == component) {
				previous = comp;
				break;
			}
			comp = next;
		}
	
		set((previous != null) ? previous : parent,
				(previous != null) ? ":next" : ":comp", get(component, ":next"));
		set(component, ":next", null);
		// required 
		set(component, ":parent", null);
		
	}

	/**
	 * Returns canvas widget 
	 * @param {String} name
	 * @return {Canvas} widget if exists with the name specified else returns null
	 */
	public Canvas findCanvas(String name){
		Object widget=this.find(name);
		if(getComponentClass(widget)=="bean" && getComponent(widget, "bean") instanceof Canvas){
			return (Canvas)getComponent(widget, "bean");
		}
		return null;
	}
	/**
	 * Finds the first component from the root desktop by a specified name value
	 *
	 * @param name
	 *            parameter value identifies the widget
	 * @return the first suitable component, or null
	 */
	public Object find(String name) {
		return find(content, name);
	}

	/**
	 * Finds the first component from the specified component by a name
	 *
	 * @param component
	 *            the widget is searched inside this component
	 * @param name
	 *            parameter value identifies the widget
	 * @return the first suitable component, or null
	 */
	public Object find(Object component, String name) {
		if (name.equals(get(component, "name"))) {
			return component;
		}
		// otherwise search in its subcomponents
		Object found = null;
		for (Object comp = get(component, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			if ((found = find(comp, name)) != null) {
				return found;
			}
		}
		// search in table header
		Object header = get(component, "header"); // if ("table" == classname)
		if ((header != null) && ((found = find(header, name)) != null)) {
			return found;
		}
		// search in component's popupmenu
		Object popupmenu = get(component, "popupmenu"); // if
														// instance(classname,
														// "component")
		if ((popupmenu != null) && ((found = find(popupmenu, name)) != null)) {
			return found;
		}
		return null;
	}

	/**
	 * Returns true if widget is contained in parent or in one of child of the parent
	 *
	 * @param parent
	 *            the widget is searched inside this component
	 * @param child
	 *            the child widget to be searched
	 * @return the first suitable component, or null
	 */
	public boolean containedIn(Object parent, Object child) {
		if (child==parent) {
			return true;
		}
		// otherwise search in its subcomponents
		boolean found = false;
		for (Object comp = get(parent, ":comp"); comp != null; comp = get(
				comp, ":next")) {
			if ((found = containedIn(comp, child)) != false) {
				return true;
			}
		}
		// search in table header
		Object header = get(parent, "header"); // if ("table" == classname)
		if ((header != null) && ((found = containedIn(header, child)) != false)) {
			return found;
		}
		// search in component's popupmenu
		Object popupmenu = get(parent, "popupmenu"); // if
														// instance(classname,
														// "component")
		if ((popupmenu != null) && ((found = containedIn(popupmenu, child)) != false)) {
			return found;
		}
		return false;
	}


	/**
	 * Returns the parent dialig of child if any
	 *
	 * @param parent
	 *            the widget is searched inside this component
	 * @return the first parent which is dialog,  returns null if it is not contained in dialog
	 */
	public Object getParentDialog(Object child) {
		if(child==null ||getComponentClass(child)=="desktop")return null;
		if (getComponentClass(child)=="dialog") {
			return child;
		}
		// otherwise find its parent
		return getParentDialog(getParent(child)); 
		
	}

	public Vector2 getScreenToLocal(Object comp,Vector2 v) {
		if(comp==null ||getComponentClass(comp)=="desktop")return v;
		Object parent=getParent(comp);
		Rectangle bounds = getRectangle(comp, "bounds");
		if(bounds==null)return v;
		v.subtract(bounds.x, bounds.y);
		if(parent!=null){
			return getScreenToLocal(parent, v);
		}
		return v;
	}


	/**
	 * mnemonic (e.g. Alt-X): - check: label, button, checkbox, togglebutton,
	 * menubar menus, tabbedpane tabs - path: panel, desktop, dialog, splitpane
	 * components, tabbedpane selected component accelerator (e.g. Ctrl-Shift-X,
	 * F4): - check: menuitem, checkboxmenuitem - path: see above, and menubar,
	 * and menu items menubar F10: check menubar only button enter, escape:
	 * check button only
	 * 
	 * @param component
	 * @param parent
	 *            check upwards if true
	 * @param checked
	 *            this leaf is already checked
	 * @param keycode
	 * @param modifiers
	 * @return true if the char was consumed
	 */
	private boolean checkMnemonic(Object component, boolean parent,
			Object checked, int keycode, int modifiers) {
		if ((component == null) || !getBoolean(component, "visible", true)
				|| !getBoolean(component, "enabled", true)) { // + enabled comp
																// in disabled
																// parent
			return false;
		}
		String classname = getComponentClass(component);
		if ("label" == classname) {
			if (hasMnemonic(component, keycode, modifiers)) {
				Object labelfor = get(component, "for");
				if (labelfor != null) {
					requestFocus(labelfor);
					return true;
				}
			}
		} else if ("button" == classname) {
			if (((modifiers == 0) && (((keycode == AWTKeyEvent.VK_ENTER) && (get(
					component, "type") == "default")) || ((keycode == AWTKeyEvent.VK_ESCAPE) && (get(
					component, "type") == "cancel"))))
					|| hasMnemonic(component, keycode, modifiers)) {
				invoke(component, null, "action");
				repaint(component);
				return true;
			}
		} else if (("checkbox" == classname) || ("togglebutton" == classname)) {
			if (hasMnemonic(component, keycode, modifiers)) {
				changeCheck(component, true);
				repaint(component);
				return true;
			}
		} else if ("menubar" == classname) {
			for (Object menu = get(component, ":comp"); menu != null; menu = get(
					menu, ":next")) {
				if (hasMnemonic(menu, keycode, modifiers)
						|| ((modifiers == 0) && (keycode == AWTKeyEvent.VK_F10))) {
					closeup();
					set(component, "selected", menu);
					popupMenu(component);
					repaint(component, "menubar", menu);
					return true;
				}
			}
		} else if (("menuitem" == classname)
				|| ("checkboxmenuitem" == classname)) {
			if (hasAccelerator(component, keycode, modifiers)) {
				invoke(component, null, "action");
			}
		} else if ("tabbedpane" == classname) {
			int selected = getInteger(component, "selected", 0);
			int i = 0;
			for (Object tab = get(component, ":comp"); tab != null; tab = get(
					tab, ":next")) {
				if (hasMnemonic(tab, keycode, modifiers)) {
					if (selected != i) {
						setInteger(component, "selected", i, 0);
						repaint(component);
						invoke(component, getItem(component, i), "action");
					}
					return true;
				}
				i++;
			}
			Object comp = get(getItem(component, selected), ":comp");
			if ((comp != null) && (comp != checked)
					&& checkMnemonic(comp, false, null, keycode, modifiers)) {
				return true;
			}
		}
		// check subcomponents
		if (("panel" == classname) || ("desktop" == classname)
				|| ("dialog" == classname) || ("splitpane" == classname)
				|| ("menubar" == classname) || ("menu" == classname)) {
			for (Object comp = get(component, ":comp"); comp != null; comp = get(
					comp, ":next")) {
				if ((comp != checked)
						&& checkMnemonic(comp, false, null, keycode, modifiers)) {
					return true;
				}
			}
		}
		// check parent
		if (parent
				&& (("dialog" != classname) || !getBoolean(component, "modal",
						false))) {
			if (checkMnemonic(getParent(component), true,
					("tab" == classname) ? checked : component, keycode,
					modifiers)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param component
	 * @param keycode
	 * @param modifiers
	 * @return true if the component has the given mnemonic
	 */
	private boolean hasMnemonic(Object component, int keycode, int modifiers) {
		if (modifiers == InputEvent.ALT_MASK) {
			int index = getInteger(component, "mnemonic", -1);
			if (index != -1) {
				String text = getString(component, "text", null);
				return (text != null)
						&& (text.length() > index)
						&& (Character.toUpperCase(text.charAt(index)) == keycode);
			}
		}
		return false;
	}

	/**
	 * @param component
	 * @param keycode
	 * @param modifiers
	 * @return true if the component has the given accelerator
	 */
	private boolean hasAccelerator(Object component, int keycode, int modifiers) {
		Object accelerator = get(component, "accelerator");
		if (accelerator != null) {
			long keystroke = ((Long) accelerator).longValue();
			return ((keystroke >> 32) == modifiers)
					&& ((keystroke & 0xffff) == keycode);
		}
		return false;
	}

	/**
	 * Binds the specified key to the specified value, and stores in this
	 * component. <i>Null</i> value removes the property. Use the parameter tag
	 * in the xml resource to bind a string value, the format is:
	 * <i>parameter='key=value'</i>
	 *
	 * @param component
	 *            the hashtable is binded to this component
	 * @param key
	 *            the client property key
	 * @param value
	 *            the new client property value
	 */
	public void putProperty(Object component, Object key, Object value) {
		Object table = get(component, ":bind");
		if (value != null) {
			if (table == null) {
				set(component, ":bind", table = new Hashtable());
	
			}
			if(key=="pintoworld"&& value!=null && !(value instanceof Vector2)){
				//update current location of widget in world coordinates to allow move with world
				Rectangle bounds = getRectangle(component, "bounds");
				//value=Simphy.instance.screenToWorld(new Vector2(bounds.x,bounds.y).divide(g.PIXEL_SCALE_FACTOR));
			}
			((Hashtable) table).put(key, value);
		} else if (table != null) {
			((Hashtable) table).remove(key);
		}
	}

	/**
	 * Returns the value of the property with the specified key.
	 *
	 * @param component
	 *            searches the hashtable of this component
	 * @param key
	 *            the client property key
	 * @return the value to which the key is mapped or null if the key is not
	 *         mapped to any value
	 */
	public Object getProperty(Object component, Object key) {
		Object table = get(component, ":bind");
		return (table != null) ? ((Hashtable) table).get(key) : null;
	}

	// ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

	/**
	 * Creates a component (and its subcomponents, and properties) from the
	 * given xml resource
	 *
	 * @param path
	 *            is relative to your thinlet instance or the classpath (if the
	 *            path starts with an <i>/</i> character), or a full URL
	 * @return the root component of the parsed resource, if there exits multimple root elements it return first element
	 * @throws java.io.IOException
	 */
	public Object parse(String path) throws IOException {
		return parse(path, this);
	}

	/**
	 * Creates a component from the given xml resource using the specified event
	 * handler
	 *
	 * @param path
	 *            is relative to your application package or the classpath, or
	 *            an URL
	 * @param handler
	 *            bussiness methods are implemented in this object
	 * @return the parsed components' root
	 * @throws java.io.IOException
	 */
	public Object parse(String path, Object handler) throws IOException {
		InputStream inputstream = null;
		try {
			inputstream = getClass().getResourceAsStream(path);
			if (inputstream == null) {
				try {
					inputstream = new URL(path).openStream();
				} catch (MalformedURLException mfe) { /*
													 * thows
													 * nullpointerexception
													 */
				}
			}
		} catch (Throwable e) {
		}
		return parse(inputstream, handler);
	}

	public Object parsefromString(String xml,boolean validate,boolean  dom, Object handler) throws IOException {
		return parse(new StringReader(xml),validate,dom,handler);
	}
		
	
	
	/**
	 * Creates a component from the given stream
	 *
	 * @param inputstream
	 *            e.g. <i>new URL("http://myserver/myservlet").openStream()</i>
	 * @return the root component of the parsed stream
	 * @throws java.io.IOException
	 */
	public Object parse(InputStream inputstream) throws IOException {
		return parse(inputstream, this);
	}

	/**
	 * Creates a component from the given stream and event handler
	 *
	 * @param inputstream
	 *            read xml from this stream
	 * @param handler
	 *            event handlers are implemented in this object
	 * @return the parsed components' root
	 * @throws java.io.IOException
	 */
	public Object parse(InputStream inputstream, Object handler)
			throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(inputstream));
		return parse(reader, true, false, handler);
	}

	/**
	 * You can use the internal xml parser as a simple SAX-like parser, during
	 * the process it calls the <i>startElement</i>, <i>characters</i>, and
	 * <i>endElement</i> methods
	 *
	 * @param inputstream
	 *            e.g. <i>new URL("http://myserver/myservlet").openStream()</i>
	 * @throws java.io.IOException
	 */
	protected void parseXML(InputStream inputstream) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(inputstream));
		parse(reader, false, false, null);
	}

	/**
	 * The SAX-like parser calls this method, you have to overwrite it
	 *
	 * @param name
	 *            of the tag
	 * @param attributelist
	 *            a list of attributes including keys and value pairs
	 */
	protected void startElement(String name, Hashtable attributelist) {
	}

	/**
	 * The SAX-like parser calls this method, you have to overwrite it
	 *
	 * @param text
	 *            the content of a tag
	 */
	protected void characters(String text) {
	}

	/**
	 * The SAX-like parser calls this method, you have to overwrite it
	 */
	protected void endElement() {
	}

	/**
	 * You can use the internal xml parser as a simple DOM-like parser, use the
	 * <i>getDOMAttribute</i>, <i>getDOMText</i>, <i>getDOMCount</i>,
	 * <i>getDOMNode</i>, <i>getClass</i>, and <i>getParent</i> methods to
	 * analyse the document
	 *
	 * @param inputstream
	 *            e.g. <i>new URL("http://myserver/myservlet").openStream()</i>
	 * @return the root tag
	 * @throws java.io.IOException
	 */
	protected Object parseDOM(InputStream inputstream) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(inputstream));
		return parse(reader, false, true, null);
	}

	/**
	 * Gets the attribute value by the specified key for a DOM tag
	 *
	 * @param node
	 *            a specified tag
	 * @param key
	 *            a string to identify the value pair
	 * @return the value, or null
	 */
	protected static String getDOMAttribute(Object node, String key) {
		return (String) get(node, key);
	}

	/**
	 * Gets the content string of a tag
	 *
	 * @param node
	 *            a specified tag
	 * @return the value, or null
	 */
	protected static String getDOMText(Object node) {
		return (String) get(node, ":text");
	}

	/**
	 * Gets the number of tags in a tag by a specified tagname
	 *
	 * @param node
	 *            a specified tag
	 * @param key
	 *            the searched tagname
	 * @return the number of tags
	 */
	protected static int getDOMCount(Object node, String key) {
		return getItemCountImpl(node, key);
	}

	/**
	 * Gets the subtag of the specified tag by tagname and index
	 *
	 * @param node
	 *            a specified tag
	 * @param key
	 *            the searched tagname
	 * @param index
	 *            the index of the requested subtag
	 * @return the found tag, or null
	 */
	protected static Object getDOMNode(Object node, String key, int index) {
		return getItemImpl(node, key, index);
	}

	
	/**
	 *
	 * @param inputstream
	 * @param validate
	 *            parse GUI from xml if true
	 * @param dom
	 *            parse an xml resoource
	 * @param handler
	 * @return
	 * @throws java.io.IOException
	 * @throws java.lang.IllegalArgumentException
	 */
	private Object parse(Reader reader, boolean validate,
			boolean dom, Object handler) throws IOException {
		
		try {
			Object[] parentlist = null;
			Object current = null;
			Hashtable attributelist = null;
			dom=false;
			Vector methods = (validate && !dom) ? new Vector() : null;

			StringBuffer text = new StringBuffer();
			for (int c = reader.read(); c != -1;) {
				if (c == '<') {
					if ((c = reader.read()) == '/') { // endtag
						if (text.length() > 0) {
							if (text.charAt(text.length() - 1) == ' ') {
								text.setLength(text.length() - 1);
							}
							if (!validate) {
								if (dom) {
									set(current, ":text", text.toString());
								} else {
									characters(text.toString());
								}
							}
							// else {
							// addContent(current, text.toString());
							// }
							text.setLength(0);
						}
						String tagname = (String) parentlist[2]; // getClass(current);
						for (int i = 0; i < tagname.length(); i++) { // current-tag
							if ((c = reader.read()) != tagname.charAt(i)) {
								throw new IllegalArgumentException(tagname);
							}
						}
						while (" \t\n\r".indexOf(c = reader.read()) != -1)
							; // whitespace
						if (c != '>')
							throw new IllegalArgumentException(); // '>'
						c = reader.read();
						if (!validate && !dom) {
							endElement();
						}
						if (parentlist[0] == null) {
							reader.close();
							finishParse(methods, current, handler);
							//console.log("Parsed root to "+JSON.stringify(current));
							return current;
						}
						current = parentlist[0];
						//console.log("Parsed parentlist to "+JSON.stringify(current));
						parentlist = (Object[]) parentlist[1];
					} else if (c == '!') { // DOCTYPE
						while ((c = reader.read()) != '>')
							; // +(-1)
					} else if (c == '?') { // Processing Instructions
						boolean question = false; // read until '?>'
						while (((c = reader.read()) != '>') || !question) {
							question = (c == '?');
						}
					} else { // start or standalone tag
						text.setLength(0);
						boolean iscomment = false;
						while (">/ \t\n\r".indexOf(c) == -1) {
							text.append((char) c);
							if ((text.length() == 3) && (text.charAt(0) == '!')
									&& (text.charAt(1) == '-')
									&& (text.charAt(2) == '-')) {
								int m = 0;
								while (true) {
									c = reader.read();
									if (c == '-') {
										m++;
									} else if ((c == '>') && (m >= 2)) {
										break;
									} else {
										m = 0;
									}
								}
								iscomment = true;
							}
							c = reader.read();
						}
						if (iscomment) {
							continue;
						}
						String tagname = text.toString();
						parentlist = new Object[] { current, parentlist,
								tagname };
						if (validate) {
							current = (current != null) ? addElement(current,
									tagname) : create(tagname);
						} else {
							if (dom) {
								Object parent = current;
								current = createImpl(tagname);
								if (parent != null) {
									insertItem(parent, tagname, current, -1);
									// set(current, ":parent", parent);
								}
							} else {
								current = tagname;
							}
						}
						text.setLength(0);
						while (true) {
							boolean whitespace = false;
							while (" \t\n\r".indexOf(c) != -1) {
								c = reader.read();
								whitespace = true;
							}
							if (c == '>') {
								if (!validate && !dom) {
									startElement((String) current,
											attributelist);
									attributelist = null;
								}
								c = reader.read();
								break;
							} else if (c == '/') {
								if ((c = reader.read()) != '>') {
									throw new IllegalArgumentException(); // '>'
								}
								if (!validate && !dom) {
									startElement((String) current,
											attributelist);
									attributelist = null;
									endElement();
								}
								if (parentlist[0] == null ) {
									reader.close();
									finishParse(methods, current, handler);
									return current;
								}
								current = parentlist[0];
								parentlist = (Object[]) parentlist[1];
								c = reader.read();
								break;
							} else if (whitespace) {
								while ("= \t\n\r".indexOf(c) == -1) {
									text.append((char) c);
									c = reader.read();
								}
								String key = text.toString();
								text.setLength(0);
								while (" \t\n\r".indexOf(c) != -1)
									c = reader.read();
								if (c != '=')
									throw new IllegalArgumentException();
								while (" \t\n\r".indexOf(c = reader.read()) != -1)
									;
								char quote = (char) c;
								if ((c != '\"') && (c != '\''))
									throw new IllegalArgumentException();
								while (quote != (c = reader.read())) {
									if (c == '&') {
										StringBuffer eb = new StringBuffer();
										while (';' != (c = reader.read())) {
											eb.append((char) c);
										}
										String entity = eb.toString();
										if ("lt".equals(entity)) {
											text.append('<');
										} else if ("gt".equals(entity)) {
											text.append('>');
										} else if ("amp".equals(entity)) {
											text.append('&');
										} else if ("quot".equals(entity)) {
											text.append('"');
										} else if ("apos".equals(entity)) {
											text.append('\'');
										} else if (entity.startsWith("#")) {
											boolean hexa = (entity.charAt(1) == 'x');
											text.append((char) Integer
													.parseInt(entity
															.substring(hexa ? 2
																	: 1),
															hexa ? 16 : 10));
										} else
											throw new IllegalArgumentException(
													"unknown " + "entity "
															+ entity);
									} else
										text.append((char) c);
								}
								if (validate) {
									addAttribute(current, key, text.toString(),
											methods);
								} else {
									if (dom) {
										set(current, key,
												text.toString());
									} else {
										if (attributelist == null) {
											attributelist = new Hashtable();
										}
										attributelist.put(key, text.toString());
									}
								}
								// '<![CDATA[' ']]>'
								text.setLength(0);
								c = reader.read();
							} else
								throw new IllegalArgumentException();
						}
					}
				} else {
					if (" \t\n\r".indexOf(c) != -1) {
						if ((text.length() > 0)
								&& (text.charAt(text.length() - 1) != ' ')) {
							text.append(' ');
						}
					} else {
						text.append((char) c);
					}
					c = reader.read();
				}
			}
			throw new IllegalArgumentException();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 *
	 */
	private void finishParse(Vector methods, Object root, Object handler) {
		/*
		if (methods != null) {
			for (int i = 0; i < methods.size(); i += 3) {
				Object component = methods.elementAt(i);
				Object[] definition = (Object[]) methods.elementAt(i + 1);
				String value = (String) methods.elementAt(i + 2);

				if ("method" == definition[0]) {
					Object[] method = getMethod(component, value, root, handler);
					if ("init" == definition[1]) {
						invokeImpl(method, null);
					} else {
						set(component, definition[1], method);
					}
				} else { // ("component" == definition[0])
					Object reference = find(root, value); // +start find from
															// the component
					if (reference == null){
						//throw new IllegalArgumentException(value + " not found");
					}
					set(component, definition[1], reference);
				}
			}
		}
		*/
	}

	/**
	 * Add the component to the parent's ':comp' list, and set its ':parent' or
	 * set single components
	 *
	 * @param index
	 *            add at the specified index
	 * @throws java.lang.IllegalArgumentException
	 */
	private void addImpl(Object parent, Object component, int index) {
		String parentclass = getComponentClass(parent);
		String classname = getComponentClass(component);
		if ((("combobox" == parentclass) && (("choice" == classname)||"checkboxmenuitem"==classname||"separator" == classname))
				|| (("tabbedpane" == parentclass) && ("tab" == classname))
				|| (("list" == parentclass) && ("item" == classname))
				|| (("desktop" == parentclass) && ("popupmenu" == classname))
				|| (("table" == parentclass) && ("row" == classname))
				|| (("header" == parentclass) && ("column" == classname))
				|| (("row" == parentclass) && ("cell" == classname))
				|| ((("tree" == parentclass) || ("node" == parentclass)) && ("node" == classname))
				|| (("menubar" == parentclass) && ("menu" == classname))
				|| ((("menu" == parentclass) || ("popupmenu" == parentclass)) && (("menu" == classname)
						|| ("menuitem" == classname)|| ("panel" == classname)
						|| ("checkboxmenuitem" == classname) || ("separator" == classname)))
				|| ((("panel" == parentclass) || ("desktop" == parentclass)
						|| ("splitpane" == parentclass)
						|| ("dialog" == parentclass) || ("tab" == parentclass))
						&& instance(classname, "component") && (classname != "popupmenu"))) {
			insertItem(parent, ":comp", component, index);
			set(component, ":parent", parent);
		} else if ((("table" == parentclass) && ("header" == classname))
				|| (("popupmenu" == classname) && instance(parentclass,
						"component"))) {
			set(parent, classname, component);
			set(component, ":parent", parent);
		} else
			//console.log("IllegalArgumentException:"+classname + " add "
			//		+ parentclass);
			//console.log(component);
			throw new IllegalArgumentException(classname + " add "
				+ parentclass);
	}

	/**
	 *
	 */
	private boolean instance(Object classname, Object extendclass) {
		if (classname == extendclass) {
			return true;
		}
		for (int i = 0; i < dtd.length; i += 3) {
			if (classname == dtd[i]) {
				return instance(dtd[i + 1], extendclass);
			}
		}
		return false;
	}

	/**
	 *
	 */
	private Object addElement(Object parent, String name) {
		Object component = create(name);
		addImpl(parent, component, -1);
		return component;
	}

	/**
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private void addAttribute(Object component, String key, String value,
			Vector lasts) {
		// replace value found in the bundle
		

		Object[] definition = getDefinition(getComponentClass(component), key, null);
		if(definition==null){
			//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Missing definition "+key+" in class "+getClass(component));
			//return;
		}
		key = (String) definition[1];
		if ("string" == definition[0]) {
			if(key=="rectbounds" && value!=null){
				String[] arr=value.split(",");
				if(arr.length==4){
					int[] attr =new int[arr.length];
					int i=0;
					for(String str:arr){
						attr[i]=Integer.parseInt(str);
						i++;
					}
					setRectangle(component, "bounds", attr[0],attr[1],attr[2],attr[3]);
				}
			}else
				setString(component, key, value, (String) definition[3]);
		} else if ("choice" == definition[0]) {
			String[] values = (String[]) definition[3];
			setChoice(component, key, value, values, values[0]);
		} else if ("boolean" == definition[0]) {
			if ("true".equals(value)) {
				if (definition[3] == Boolean.FALSE) {
					set(component, key, Boolean.TRUE);
				}
			} else if ("false".equals(value)) {
				if (definition[3] == Boolean.TRUE) {
					set(component, key, Boolean.FALSE);
				}
			} else
				throw new IllegalArgumentException(value);
		} else if ("integer" == definition[0]) {
			double factor=1;
			if(key=="gap"||key=="left"||key=="right"||key=="top"||key=="bottom"||key=="divider"||key=="width"||key=="height") 
				set(component, key,(int)(factor* Integer.valueOf(value)));
			else
				set(component, key,Integer.valueOf(value));
		} else if ("double" == definition[0]) {
			set(component, key, Double.valueOf(value));
		} else if ("icon" == definition[0]) {
			set(component, key, getIcon(value));
		} else if (("method" == definition[0])
				|| ("component" == definition[0])) {
			lasts.addElement(component);
			lasts.addElement(definition);
			lasts.addElement(value);
		} else if ("property" == definition[0]) {
			/*
			StringTokenizer st = new StringTokenizer(value, ";");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int equals = token.indexOf('=');
				if (equals == -1) {
					throw new IllegalArgumentException(token);
				}
				String propKey=token.substring(0, equals);
				if(propKey.equals("drawstyle")){
					putProperty(component,propKey,
							stringToStyles(token.substring(equals + 1)));
				}else{
					putProperty(component,propKey,
						token.substring(equals + 1));
				}
			}
			*/
		} else if ("font" == definition[0]) {
			String name = null;
			/*
			boolean bold = false;
			boolean italic = false;
			int size = 0;
			StringTokenizer st = new StringTokenizer(value);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if ("bold".equalsIgnoreCase(token)) {
					bold = true;
				} else if ("italic".equalsIgnoreCase(token)) {
					italic = true;
				} else {
					try {
						size = Integer.parseInt(token);
					} catch (NumberFormatException nfe) {
						name = (name == null) ? token : (name + " " + token);
					}
				}
			}
			*/
			/*
			if (name == null) {
				name = font.getName();
			}
			if (size == 0) {
				size = font.getSize();
			}
			*/
			set(component, key, new Font(value,g.context));
		} else if ("color" == definition[0]) {
			Color color=new Color(value);
			set(component, key,color);
		} else if ("keystroke" == definition[0]) {
			setKeystrokeImpl(component, key, value);
		} else if ("bean" == definition[0]) {
			if(value.equals("org.shikhar.simphy.gui.CanvasBean")||value.equals("org.shikhar.simphy.scripting.canvas.Canvas2D")||value.equals("org.shikhar.simphy.scripting.canvas.Canvas"))value="org.shikhar.simphy.gfx.canvas.Canvas";
			/*
			try {
				Object beanObject = Class.forName(value).newInstance();
				if (beanObject instanceof CustomComponent) {
					CustomComponent bean = (CustomComponent) beanObject;
					bean.setGui(this);
					bean.setComponent(component);
				}
				set(component, key, beanObject);
				
			//}catch (ClassNotFoundException cex){
			//	cex.printStackTrace();
			//	throw new IllegalArgumentException("\n Bean creation Failed! It can't find class "+value);
			} catch (Exception exc) {
				exc.printStackTrace();
				throw new IllegalArgumentException(exc.getMessage()+value);
			}
			*/
			
		} else
			throw new IllegalArgumentException((String) definition[0]);
	}

	/**
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	private static Object[] getDefinition(Object classname, String key,	String type) {
		Object currentname = classname;
	if((classname+"").startsWith(":")){
		System.out.println("Seems Invalid class for gui "+classname);
		return null;
	}
	while (classname != null) {
		for (int i = 0; i < dtd.length; i += 3) {
			if (dtd[i] == classname) {
				Object[][] attributes = (Object[][]) dtd[i + 2];
				if (attributes != null) {
					for (int j = 0; j < attributes.length; j++) {
						if (attributes[j][1].equals(key)) {
							if ((type != null) && (type != attributes[j][0])) {
								throw new IllegalArgumentException(attributes[j][0].toString());
							}
							return attributes[j];
						}
					}
				}
				classname = dtd[i + 1];
				break;
			}
		}
	}
	
	return null;
		//throw new IllegalArgumentException("unknown " + key + " " + type
		//		+ " for " + currentname);
	}

	
	
	
	// ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----
	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setString(Object component, String key, String value) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), key, "string");
			if(definition==null)return;
			if (setString(component, (String) definition[1], value,
				(String) definition[3])) {
				update(component, definition[2]);
			}
		}catch(Exception e){
			
		}
	}

	/**
	 * Gets the property value of the given component by the property key
	 */
	public String getString(Object component, String key) {
		return (String) get(component, key, "string");

	}

	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setChoice(Object component, String key, String value) {
		Object[] definition = getDefinition(getComponentClass(component), key, "choice");
		if(definition==null)return;
		String[] values = (String[]) definition[3];
		if (setChoice(component, (String) definition[1], value, values,
				values[0])) {
			update(component, definition[2]);
		}
		if(key=="animmode" && getComponentClass(component)=="slider")updateAnimTimer(component,value);
	}

	/**
	 * Creates timer for the slider
	 */
	private void updateAnimTimer(Object component, String animMode) {
		if(animMode=="none"||animMode==null){
			putProperty(component,"timer",null);
		}else{
			int animInterVal=(int) (getDouble(component,"animinterval",0.1)*1000);
			GuiTimer timer=new GuiTimer(animInterVal,-1);	
			timer.invokerCompoenent=component;
			timer.setCallback(this);
			putProperty(component,"timer",timer);
			timer.start();
		}

	}
		
	

	/**
	 * Gets the property value of the given component by the property key
	 */
	public String getChoice(Object component, String key) {
		Object[] definition = getDefinition(getComponentClass(component), key, "choice");
		if(definition==null)return null;
		return getString(component, (String) definition[1],
				((String[]) definition[3])[0]);
	}

	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setBoolean(Object component, String key, boolean value) {
		try {
			Object[] definition = getDefinition(getComponentClass(component), key, "boolean");
			if (definition == null)
				return;
			if (setBoolean(component, (String) definition[1], value, (definition[3] == Boolean.TRUE))) {
				update(component, definition[2]);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Gets the property value of the given component by the property key
	 */
	public boolean getBoolean(Object component, String key) {
		return get(component, key, "boolean") == Boolean.TRUE;
	}

	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setInteger(Object component, String key, int value) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), key, "integer");
			if(definition==null)return;
			if (setInteger(component, (String) definition[1], value,
				((Integer) definition[3]).intValue())) {
				update(component, definition[2]);
			}
		}catch(Exception e){
			
		}
	}

	/**
	 * Gets the property value of the given component by the property key
	 */
	public int getInteger(Object component, String key) {
		return ((Integer) get(component, key, "integer")).intValue();
	}

	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setDouble(Object component, String key, double value) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), key, "double");
			if(definition==null)return;
			if (setDouble(component, (String) definition[1], value,
				((Double) definition[3]).doubleValue())) {
				update(component, definition[2]);
			}
			if(key=="animinterval" && getComponentClass(component)=="slider"){
				updateAnimTimer(component,getString(component,"animmode","none"));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Gets the property value of the given component by the property key
	 */
	public double getDouble(Object component, String key) {
		return ((Double) get(component, key, "double")).doubleValue();
	}
	
	/**
	 * Sets the given property pair (key and value) for the component
	 */
	public void setIcon(Object component, String key, AWTImage icon) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), key, "icon");
			if(definition==null)return;
			if (set(component, definition[1], icon)) {
				update(component, definition[2]);
			}

		}catch(Exception e){
		
		}

	}

	/**
	 * Sets Icon to the widget
	 * @param component
	 * @param icon identifier name of preloaded icons
	 */
	public void setIcon(Object component, String key, String icon) {
		if(icon==null||icon.isEmpty())return;
		setIcon(component,key,new AWTImage(icon));
	}
	
	/**
	 * Sets Icon to the widget
	 * @param component
	 * @param icon identifier name of preloaded icons
	 */
	public void setIcon(Object component, String icon) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), "icon", "icon");
			if(definition==null)return;
			if (set(component, definition[1], (icon==null||icon.isEmpty())?null:new AWTImage(icon))) {
				update(component, definition[2]);
			}
		}catch(Exception e){
			
		}
	}

	
	
	/**
	 * Gets the property value of the given component by the property key
	 */
	public AWTImage getIcon(Object component, String key) {
		return (AWTImage) get(component, key, "icon");
	}

	/**
	 *
	 */
	public void setKeystroke(Object component, String key, String value) {
		try{
			Object[] definition = getDefinition(getComponentClass(component), key,
					"keystroke");
			if(definition==null)return;
			// TODO check if changed
			setKeystrokeImpl(component, (String) definition[1], value);
			update(component, definition[2]);

		}catch(Exception e){
		
		}
	}
	


	/**
	 * Get the AWT component of the given (currently <i>bean</i>) widget
	 *
	 * @param component
	 *            a <i>bean</i> widget
	 * @param key
	 *            the identifier of the parameter
	 * @return an AWT component, or null
	 */
	public CustomComponent getComponent(Object component, String key) {
		return (CustomComponent) get(component, key, "bean");
	}

	/**
	 * Sets font of widget
	 * @param component
	 * @param fontName predefined or  loaded font name
	 */
	public void setFont(Object component,String fontName) {
		setFont( component,  "font", new Font(fontName,g.context)); 
	}
		

	/**
	 * Set custom font on a component
	 *
	 * @param component
	 *            component to use the custom font
	 * @param font
	 *            custom font to use, or null to reset component to use default
	 *            font
	 */
	public void setFont(Object component, String key, Font font) {
		Object[] definition = getDefinition(getComponentClass(component), key, "font");
		String classname= getComponentClass(component);
		if(("list" == classname)|| ("table" == classname) || ("tree" == classname)
				||("combobox" == classname)||("menubar" == classname)||("menu" == classname)||("node" == classname)||("header" == classname)||("row" == classname)){
			for (Object comp = get(component, ":comp"); comp != null; comp = get(
					comp, ":next")) {
				if(getFont(comp)==null)setFont(comp,  key,  font);
			}
			if("table" == classname){
				Object header=get(component, "header");
				//if(header!=null)setFont(header,  key,  font);
			}
			return;
		}
		if(definition==null)return;

		if(definition.length<3)return;
		if (set(component, definition[1], font)) {
			update(component, definition[2]);
		}
		
		
	}
	
	

	/**
	 * Set custom color on a component. Notes: For "foreground" key, this sets
	 * the text color. For "background" key, on gradient-filled components (such
	 * as tabs, buttons etc) this will result in a component filled with solid
	 * background color, and not a new gradient. Also, Color.brighter() will be
	 * used for highlight, and Color.darker() will be used for pressed or not
	 * selected.
	 *
	 * @param component
	 *            component to use for custom color
	 * @param key
	 *            currently "background" and "foreground" are supported
	 * @param color
	 *            custom color to use, or null to reset component to use default
	 *            color
	 */
	public void setColor(Object component, String key, Color color) {
		Object[] definition = getDefinition(getComponentClass(component), key, "color");
		//if(definition==null)return;
		String classname= getComponentClass(component);
		if(("list" == classname)|| ("row" == classname)||("table" == classname) || ("tree" == classname)
				||("combobox" == classname)||("menubar" == classname)||("menu" == classname)||("node" == classname)||("header" == classname)||("row" == classname)){
			for (Object comp = get(component, ":comp"); comp != null; comp = get(
					comp, ":next")) {
				if(getColor(comp,key)==null)setColor(comp,  key,  color);
			}
			if("table" == classname){
				//Object header=get(component, "header");
				//if(header!=null)setColor(header,  key,  color);
			}
		}
		if(definition==null)return;
		if (set(component, definition[1], color)) {
			update(component, definition[2]);
		}
	}

	/**
	 * Set the AWT component for the given (currently <i>bean</i>) widget
	 *
	 * @param component
	 *            a <i>bean</i> widget
	 * @param key
	 *            the identifier of the parameter
	 * @param bean
	 *            an AWT component, or null
	 */
	public void setComponent(Object component, String key, CustomComponent bean) {
		Object[] definition = getDefinition(getComponentClass(component), key, "bean");
		if(definition==null)return;
		if (set(component, definition[1], bean)) {
			update(component, definition[2]);
			bean.setGui(this);
			bean.setComponent(component);
		}
	}

	/**
	 *
	 */
	private void setKeystrokeImpl(Object component, String key, String value) {
		Long keystroke = null;
		if (value != null) {
			String token = value;
			try {
				int keycode = 0, modifiers = 0;
				/*
				StringTokenizer st = new StringTokenizer(value, " \r\n\t+");
				while (st.hasMoreTokens()) {
					token = st.nextToken().toUpperCase();
					try {
						modifiers = modifiers
								| InputEvent.class.getField(token + "_MASK")
										.getInt(null);
					} catch (Exception exc) { // not mask value
						keycode = AWTKeyEvent.class.getField("VK_" + token)
								.getInt(null);
					}
				}
				*/
				keystroke = new Long(((long) modifiers) << 32 | keycode);
			} catch (Exception exc) {
				throw new IllegalArgumentException(token);
			}
		}
		set(component, key, keystroke);
	}

	// TODO add set/getComponent for popupmenu and header

	/**
	 *
	 */
	public Object getWidget(Object component, String key) {
		if ("popupmenu".equals(key)) {
			return get(component, "popupmenu");
		} else if ("header".equals(key)) {
			return get(component, "header");
		} else
			throw new IllegalArgumentException(key);
	}

	/**
	 *
	 */
	private static Object get(Object component, String key, String type) {
		Object[] definition = getDefinition(getComponentClass(component), key, type);
		if(definition==null)return null;
		Object value = get(component, definition[1]);
		return (value != null) ? value : definition[3];
	}

	/**
	 * Sets a new event handler method for a component
	 *
	 * @param component
	 *            the target component
	 * @param key
	 *            the key name of the parameter (e.g. <i>action</i>)
	 * @param value
	 *            the method name and parameters (e.g. <i>foo(this, this.text,
	 *            mybutton, mybutton.enabled)</i> for <i>public void foo(Object
	 *            component, String text, Object mybutton, boolean enabled)</i>)
	 * @param root
	 *            the search starting component for name components in the
	 *            arguments
	 * @param handler
	 *            the target event handler object including the method
	 * @throws java.lang.IllegalArgumentException
	 */
	public void setMethod(Object component, String key, String value,
			Object root, Object handler) {
		key = (String) getDefinition(getComponentClass(component), key, "method")[1];
		if(value==null||value.isEmpty()){
			set(component, key, null);
		}else{
			Object[] method = getMethod(component, value, root, handler);
			set(component, key, method);
		}
	}

	/**
	 * returns method name
	 * @param component
	 * @param action
	 * @return
	 */
	public String getMethodText(Object component, String action){
		Object[] method = (Object[]) get(component, action);
		if(method==null)return  null;
		if(method[0] instanceof String){
			return (String) method[0];
		}else{
			method[1].toString();
		}
		return null;
	}
	/**
	 * @return an object list including as follows:
	 * - handler object,
	 * - method,
	 * - list of parameters including 3 values:
	 * - ("thinlet", null, null) for the single thinlet component,
	 * - (target component, null, null) for named widget as parameter, e.g. mybutton,
	 * - (target, parameter name, default value) for a widget's given property, e.g. mylabel.enabled,
	 * - ("item", null, null) for an item of the target component as parameter, e.g. tree node,
	 * - ("item", parameter name, default value) for the item's given property e.g. list item's text,
	 * - ("constant", string object, null) for constant number (no space is permitted)
	 * (int, long, double, float) or string given as 'text'.
	 */
	private Object[] getMethod(Object component, String value, Object root, Object handler) {
		/*
		StringTokenizer st = new StringTokenizer(value, "(, \r\n\t)");
		String methodname = st.nextToken();
		int n = st.countTokens();
		Object[] data = new Object[2 + 3 * n];
		Class[] parametertypes = (n > 0) ? new Class[n] : null;
		for (int i = 0; i < n; i++) {
			String arg = st.nextToken();
			if ("gui".equals(arg)) {
				data[2 + 3 * i] = "gui"; // the target component
				parametertypes[i] = Gui.class;
			}
			else if ((arg.length() > 1) && // constant string value
					(arg.charAt(0) == '\'') && (arg.charAt(arg.length() - 1) == '\'')) {
				data[2 + 3 * i] = "constant";
				data[2 + 3 * i + 1] = new String(arg.substring(1, arg.length() - 1));
				parametertypes[i] = String.class;
			}
			else {
				int dot = arg.indexOf('.');
				String compname = (dot == -1) ? arg : arg.substring(0, dot);
				Object comp = null;
				String classname = null;
				if ("item".equals(compname)) {
					comp = "item";
					String parentclass = getClass(component);
					if ("list" == parentclass) { classname = "item"; }
					else if ("tree" == parentclass) { classname = "node"; }
					else if ("table" == parentclass) { classname = "row"; }
					else if ("combobox" == parentclass) { classname = "choice"; }
					else if ("tabbedpane" == parentclass) { classname = "tab"; }
					else throw new IllegalArgumentException(parentclass + " has no item");
				}
				else if ("this".equals(compname)) {
					comp = component; classname = getClass(comp);
				}
				else if ((comp = find(root, compname)) != null) { // a widget's name
					classname = getClass(comp);
				}
				else {
					try { // maybe constant number
						if (arg.regionMatches(true, arg.length() - 1, "F", 0, 1)) { // float
							data[2 + 3 * i + 1] = Float.valueOf(arg.substring(0, arg.length() - 1));
							parametertypes[i] = Float.TYPE;
						} else if (arg.regionMatches(true, arg.length() - 1, "L", 0, 1)) { // long
							data[2 + 3 * i + 1] = Long.valueOf(arg.substring(0, arg.length() - 1));
							parametertypes[i] = Long.TYPE;
						} else if (dot != -1) { // double
							data[2 + 3 * i + 1] = Double.valueOf(arg);
							parametertypes[i] = Double.TYPE;
						} else if(arg.equalsIgnoreCase("true")||arg.equalsIgnoreCase("false")){
							data[2 + 3 * i + 1] = Boolean.valueOf(arg);
							parametertypes[i] = Boolean.TYPE;
						} else { // integer
							try {
								Integer.parseInt(arg);
								data[2 + 3 * i + 1] = Integer.valueOf(arg);
								parametertypes[i] = Integer.TYPE;
							}catch(NumberFormatException e) {
								data[2 + 3 * i + 1] = String.valueOf(arg);
								parametertypes[i] = String.class;
							}
						}
						data[2 + 3 * i] = "constant";
						continue;
					} catch (NumberFormatException nfe) { // widget's name not found nor constant
						throw new IllegalArgumentException("unknown " + arg+" in class "+component);
					}
				}
				data[2 + 3 * i] = comp; // the target component
				if (dot == -1) {
					parametertypes[i] = Object.class; // Widget.class
				}
				else {
					Object[] definition = getDefinition(classname, arg.substring(dot + 1), null);
					if(definition==null)return null;
					data[2 + 3 * i + 1] = definition[1]; // parameter name, e.g. enabled
					data[2 + 3 * i + 2] = definition[3]; // default value, e.g. Boolean.TRUE
					Object fieldclass = definition[0];
					if ((fieldclass == "string") || (fieldclass == "choice")) {
						parametertypes[i] = String.class;
					}
					else if (fieldclass == "boolean") {
						parametertypes[i] = Boolean.TYPE;
					}
					else if (fieldclass == "integer") {
						parametertypes[i] = Integer.TYPE;
					}
					else if (fieldclass == "double") {
						parametertypes[i] = Double.TYPE;
					}
					else if (fieldclass == "icon") {
						parametertypes[i] = Image.class;
					}
					else throw new IllegalArgumentException((String) fieldclass);
				}
			}
		}
		data[0] = handler;
		//if handler is non null events are handled by scripts else handler has methods to process them
		if(handler!=null){
			try {
				data[1] = handler.getClass().getMethod(methodname, parametertypes);
				return data;
			}catch (Exception exc) {
				//throw new IllegalArgumentException(value + " " + exc.getMessage());
			}
		}
		data[0]=value;
		data[1] = methodname;
		return  data;
		*/
		return null;
	}


	/**
	 *updates component ased on mode
	 *modes are
	 *"parent" ->parent is validated
	 *"paint:" ->repaint is called
	 *"layout" ->relayout is called for component
	 */
	private void update(Object component, Object mode) {
		if ("parent" == mode) {
			component = getParent(component);
			mode = "validate";
		}
		boolean firstpaint = true;
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		while (component != null) {
			if (!getBoolean(component, "visible", true)) {
				break;
			}
			if ("paint" == mode) {// || (firstpaint && (component == content))
				Rectangle bounds = getRectangle(component, "bounds");
				if (bounds == null) {
					return;
				}
				if (firstpaint) {
					x = bounds.x;
					y = bounds.y;
					width = Math.abs(bounds.width);
					height = bounds.height;
					firstpaint = false;
				} else {
					x += bounds.x;
					y += bounds.y;
				}
				if (component == content) {
					//repaint(x, y, width, height);
					repaintNeeded=true;
				}
			}
			Object parent = getParent(component);
			String classname = getComponentClass(parent);
			if ("combobox" == classname) {
				parent = get(parent, ":combolist");
			} else if ("menu" == classname) {
				parent = get(parent, ":popup");
			} else if (("paint" == mode) && ("tabbedpane" == classname)) {
				if (getItem(parent, getInteger(parent, "selected", 0)) != component) {
					break;
				}
			}
			if (("layout" == mode)
					|| (("validate" == mode) && (("list" == classname)
							|| ("table" == classname) || ("tree" == classname)
							|| ("dialog" == classname) || (parent == content)))) {
				Rectangle bounds = getRectangle(parent, "bounds");
				if (bounds == null) {
					return;
				}
				bounds.width = -1 * Math.abs(bounds.width);
				mode = "paint";
			}
			component = parent;
		}
	}

	// ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

	/**
	 *
	 */
	private boolean setString(Object component, String key, String value,
			String defaultvalue) {
		
		if(getComponentClass(component)=="bean" && key=="text"){
			if(getComponent(component,"bean")!=null)((CustomComponent)(getComponent(component,"bean"))).setText(value);
		}

		return set(component, key, value); // use defaultvalue
	}

	/**
	 *
	 */
	protected String getString(Object component, String key, String defaultvalue) {
		Object value = get(component, key);
		return (value == null) ? defaultvalue : getI18NString(component, key,
				(String) value);
	}

	
	
	/**
	 * Sets the default behaviour of internationalization code. If set to
	 * "true", try to translate all components' "text" and "tooltip" values,
	 * unless explicitly prohibited by setting <code>i18n="false"</code> on a
	 * specific component. If set to "false", do not translate unless explicitly
	 * requested by setting <code>i18n="true"</code> on a specific component. <br />
	 * Default value is "false", to provide backwards compatibility.
	 *
	 * @param val
	 *            if "true", translate by default; if "false", do not translate
	 *            by default.
	 */
	public void setAllI18n(boolean val) { // for I18N
		allI18n = val;
	}

	/**
	 *
	 */
	private String getI18NString(Object component, String key, String text) { // for
																				// I18N
		
		if(text.isEmpty())text="";
		return text;
	}

	/**
	 *
	 * @throws java.lang.IllegalArgumentException
	 */
	private boolean setChoice(Object component, String key, String value,
			String[] values, String defaultvalue) {
		if (value == null) {
			return set(component, key, defaultvalue);
		}
		for (int i = 0; i < values.length; i++) {
			if (value.equals(values[i])) {
				return set(component, key, values[i]);
			}
		}
		throw new IllegalArgumentException("unknown " + value + " for " + key);
	}

	/**
	 *
	 */
	private AWTImage getIcon(Object component, String key, AWTImage defaultvalue) {
		Object value = get(component, key);
		return (value == null) ? defaultvalue : (AWTImage) value;
	}

	/**
	 *
	 */
	private boolean setBoolean(Object component, String key, boolean value,
			boolean defaultvalue) {
		return set(component, key, (value == defaultvalue) ? null
				: (value ? Boolean.TRUE : Boolean.FALSE));
	}

	/**
	 *
	 */
	private boolean getBoolean(Object component, String key,
			boolean defaultvalue) {
		Object value = get(component, key);
		return (value == null) ? defaultvalue : ((Boolean) value)
				.booleanValue();
	}

	/**
	 *Sets integral value of property
	 *sets null if default value is passed , Ex in list if no element is selected, selected index is set to -1
	 */
	private boolean setInteger(Object component, String key, int value,
			int defaultvalue) {
		return set(component, key, (value == defaultvalue) ? null
				: new Integer(value));
	}

	/**
	 *
	 */
	private int getInteger(Object component, String key, int defaultvalue) {
		Object value = get(component, key);
		return (value == null) ? defaultvalue : ((Integer) value).intValue();
	}

	/**
	 *Sets integral value of property
	 *sets null if default value is passed , Ex in list if no element is selected, selected index is set to -1
	 */
	private boolean setDouble(Object component, String key, double value,
			double defaultvalue) {
		return set(component, key, new Double(value));
	}

	/**
	 *
	 */
	private double getDouble(Object component, String key, double defaultvalue) {
		Object value = get(component, key);
		return (value == null) ? defaultvalue : ((Double) value).doubleValue();
	}

	/**
	 *
	 */
	public void setRectangle(Object component, String key, int x, int y,
			int width, int height) {
		Rectangle rectangle = getRectangle(component, key);
		if (rectangle != null) {
			rectangle.x = x;
			rectangle.y = y;
			rectangle.width = width;
			rectangle.height = height;
		} else {
			set(component, key, new Rectangle(x, y, width, height));
		}
	}

	/**
	 *
	 */
	public  Rectangle getRectangle(Object component, String key) {
		return (Rectangle) get(component, key);
	}

	private static Rectangle tmpRect=new Rectangle(0,0,1,1);
	
	Rectangle getAbsoluteRectangle(Object component, String key) {
		Rectangle r = getRectangle(component, "bounds");
		if(r==null)return null;
		tmpRect.set(r.x,r.y,r.width,r.height);
		while ((component = getParent(component)) != null) {
			Rectangle bounds = getRectangle(component, "bounds");
			tmpRect.x += bounds.x;
			tmpRect.y += bounds.y;
			Rectangle view = getRectangle(component, ":view");
			if (view != null) {
				Rectangle port = getRectangle(component, ":port");
				tmpRect.x += -view.x + port.x;
				tmpRect.y += -view.y + port.y; // + clip :port
				//if(tmpRect.width>port.width)tmpRect.width=port.width;
				//if(tmpRect.height>port.height)tmpRect.height=port.height;

			}
		}
		return tmpRect;
	}
	
	// ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

	/**
	 * Creates an image, and loads it immediately by default
	 *
	 * @param path
	 *            is relative to your thinlet instance or the classpath (if the
	 *            path starts with <i>'/'</i> character), or a full URL
	 * @return the loaded image or null
	 */
	private AWTImage getIcon(String path) {
		return new AWTImage(path);//,16,16);
	}

	
	
	/**
	 * Creates an image from the specified resource. To speed up loading the
	 * same images use a cache (a simple hashtable). And flush the resources
	 * being used by an image when you won't use it henceforward
	 *
	 * @param path
	 *            is relative to your thinlet instance or the classpath, or an
	 *            URL
	 * @param preload
	 *            waits for the whole image if true, starts loading (and
	 *            repaints, and updates the layout) only when required (painted,
	 *            or size requested) if false
	 * @return the loaded image or null
	 */
	/*
	public Image getIcon(String path, boolean preload) {
		if ((path == null) || (path.length() == 0)) {
			return null;
		}
		Image image = null; // (Image) imagepool.get(path);
		try {
			URL url = getClass().getResource(path); // ClassLoader.getSystemResource(path)
			if (url != null) { // contributed by Stefan Matthias Aust
				image = Toolkit.getDefaultToolkit().getImage(url);
			}
		} catch (Throwable e) {
		}
		if (image == null) {
			try {
				InputStream is = getClass().getResourceAsStream(path);
				// InputStream is = ClassLoader.getSystemResourceAsStream(path);
				if (is != null) {
					byte[] data = new byte[is.available()];
					is.read(data, 0, data.length);
					image = awtComponent.getToolkit().createImage(data);
					is.close();
				} else { // contributed by Wolf Paulus
					image = Toolkit.getDefaultToolkit().getImage(new URL(path));
				}
			} catch (Throwable e) {
			}
		}
		if (preload && (image != null)) {
			MediaTracker mediatracker = new MediaTracker(awtComponent);
			mediatracker.addImage(image, 1);
			try {
				mediatracker.waitForID(1, 5000);
			} catch (InterruptedException ie) {
			}
			// imagepool.put(path, image);
		}
		return image;
	}
	*/
	
	/**
	 * This method is called by the FrameLauncher if the window was closing, or
	 * AppletLauncher's destroy method. Overwrite it to e.g. save the
	 * application changes.
	 *
	 * @return true to exit, and false to keep the frame and continue the
	 *         application
	 */
	 public void destroy() {
	 return ;
	 }

	private static Object[] dtd;
	static {
		Integer integer_1 = Integer.valueOf(-1);
		Integer integer0 = Integer.valueOf(0);
		Integer integer1 = Integer.valueOf(1);
		Double double01 =Double.valueOf(0.1);
		Double double1 = Double.valueOf(1);
		Double double0 = Double.valueOf(0);
		Double double10 = Double.valueOf(10);
		Double double5 = Double.valueOf(5);
		String[] orientation = { "horizontal", "vertical" };
		String[] leftcenterright = { "left", "center", "right" };
		String[] selections = { "single", "interval", "multiple" }; // +none
		String[] animmode = { "none","increasing", "decreasing", "increasing-once","decreasing-once","oscillating" }; 
		dtd = new Object[] {
				"component",
				null,
				new Object[][] {
						{ "string", "name", null, null },
						{ "boolean", "enabled", "paint", Boolean.TRUE },
						{ "boolean", "visible", "parent", Boolean.TRUE },
						{ "boolean", "i18n", "validate", Boolean.FALSE }, // for
																			// I18N
						{ "icon", "bgimage", "validate", null },
						{ "string", "text", "validate", null },
						{ "string", "tooltip", null, null },
						{ "font", "font", "validate", null },
						{ "color", "foreground", "paint", null },
						{ "color", "background", "paint", null },
						{ "integer", "width", "validate", integer0 },
						{ "integer", "height", "validate", integer0 },
						{ "integer", "colspan", "validate", integer1 },
						{ "integer", "rowspan", "validate", integer1 },
						{ "integer", "weightx", "validate", integer0 },
						{ "integer", "weighty", "validate", integer0 },
						{ "string", "rectbounds", "validate", null },
						{
								"choice",
								"halign",
								"validate",
								new String[] { "fill", "center", "left",
										"right" } },
						{
								"choice",
								"valign",
								"validate",
								new String[] { "fill", "center", "top",
										"bottom" } },
						// component class String null*
						// parent Object null
						// (bounds) Rectangle 0 0 0 0
						{ "property", "property", null, null },
						{ "method", "init" }, { "method", "focuslost" },
						{ "method", "focusgained" } },
				"label",
				"component",
				new Object[][] { 
						{ "icon", "icon", "validate", null },
						{ "choice", "alignment", "validate", leftcenterright },
						{ "integer", "mnemonic", "paint", integer_1 },
						{ "component", "for", null, null } },
				"button",
				"label",
				new Object[][] {
						{ "choice", "alignment", "validate",
								new String[] { "center", "left", "right" } },
						{ "method", "action" },
						{
								"choice",
								"type",
								"paint",
								new String[] { "normal", "default", "cancel",
										"link" } } },
				"checkbox",
				"label",
				new Object[][] {
						{ "boolean", "selected", "paint", Boolean.FALSE }, // ...group
						{ "string", "group", "paint", null }, // ...group
						{ "method", "action" } },
				"togglebutton",
				"checkbox",
				null,
				"combobox",
				"textfield",
				new Object[][] { { "icon", "icon", "validate", null },
						{ "integer", "selected", "layout", integer_1 } },
				"choice",
				null,
				new Object[][] {
						{ "string", "name", null, null },
						{ "boolean", "enabled", "paint", Boolean.TRUE },
						{ "boolean", "visible", "parent", Boolean.TRUE },
						{ "boolean", "i18n", "validate", Boolean.FALSE }, // for
																			// I18N
						{ "string", "text", "parent", null },
						{ "icon", "icon", "parent", null },
						{ "choice", "alignment", "parent", leftcenterright },
						{ "string", "tooltip", null, null },
						{ "font", "font", "validate", null },
						{ "color", "foreground", "paint", null },
						{ "color", "background", "paint", null },
						{ "property", "property", null, null } },
				"textfield",
				"component",
				new Object[][] { 
						{ "integer", "columns", "validate", integer0 },
						{ "boolean", "editable", "paint", Boolean.TRUE },
						{ "integer", "start", "layout", integer0 },
						{ "integer", "end", "layout", integer0 },
						{ "method", "action" }, 
						{ "method", "insert" },
						{ "method", "remove" }, 
						{ "method", "caret" },
						{ "method", "perform" } },
				"passwordfield",
				"textfield",
				null,
				"textarea",
				"textfield",
				new Object[][] { { "integer", "rows", "validate", integer0 },
						{ "boolean", "border", "validate", Boolean.TRUE },
						{ "boolean", "wrap", "layout", Boolean.FALSE } },
				"tabbedpane",
				"component",
				new Object[][] {
						{
								"choice",
								"placement",
								"validate",
								new String[] { "top", "left", "bottom",
										"right", "stacked","none" } },
						{ "integer", "selected", "paint", integer0 },
						{ "method", "action" } }, // ...focus
				"tab",
				"choice",
				new Object[][] { { "integer", "mnemonic", "paint", integer_1 } },
				"panel",
				"component",
				new Object[][] {
						{ "integer", "columns", "validate", integer0 },
						{ "integer", "top", "validate", integer0 },
						{ "integer", "left", "validate", integer0 },
						{ "integer", "bottom", "validate", integer0 },
						{ "integer", "right", "validate", integer0 },
						{ "integer", "gap", "validate", integer0 },
						{ "string", "text", "validate", null },
						{ "icon", "icon", "validate", null },
						{ "boolean", "border", "validate", Boolean.FALSE },
						{ "boolean", "scrollable", "validate", Boolean.FALSE } },
				"desktop",
				"component",
				null,
				"dialog",
				"panel",
				new Object[][] { { "boolean", "modal", null, Boolean.FALSE },
						{ "boolean", "resizable", null, Boolean.FALSE },
						{ "method", "close" },
						{ "boolean", "closable", "paint", Boolean.TRUE },
						{ "boolean", "maximizable", "paint", Boolean.FALSE },
						{ "boolean", "iconifiable", "paint", Boolean.FALSE } },
				"spinbox",
				"textfield",
				new Object[][] {
						{ "double", "minimum", null,
								double0 },
						{ "double", "maximum", null,
								double10 },
						{ "double", "step", null, double1 },
						{ "double", "value", null, double5 } }, // == text?
																	// deprecated
				"progressbar",
				"component",
				new Object[][] {
						{ "choice", "orientation", "validate", orientation },
						{ "double", "minimum", "paint", double0 }, // ...checkvalue
						{ "double", "maximum", "paint", double10 },
						{ "double", "value", "paint", double5 } },
				// change stringpainted
				"slider",
				"progressbar",
				new Object[][] { { "double", "unit", null, double01 },
						{ "double", "block", null, double1 },
						{ "choice", "animmode", null,animmode  },
						{ "double", "animinterval", null, double01 },
						{ "method", "action" } },
				// minor/majortickspacing
				// inverted
				// labelincrement labelstart
				"splitpane",
				"component",
				new Object[][] {
						{ "choice", "orientation", "validate", orientation },
						{ "integer", "divider", "layout", integer_1 } },
				"list",
				"component",
				new Object[][] {
						{ "choice", "selection", "paint", selections },
						{ "method", "action" }, { "method", "perform" },
						{ "boolean", "line", "validate", Boolean.TRUE } },
				"item",
				"choice",
				new Object[][] { { "boolean", "selected", null, Boolean.FALSE } },
				"table",
				"list",
				new Object[][] {
				/*
				 * { "choice", "selection", new String[] { "singlerow",
				 * "rowinterval", "multiplerow", "cell", "cellinterval",
				 * "singlecolumn", "columninterval", "multiplecolumn" } }
				 */},
				"header",
				null,
				new Object[][] { { "method", "action" },
						{ "boolean", "resizable", null, Boolean.FALSE } },
				// reordering allowed
				// autoresize mode: off next (column boundries) subsequents last
				// all columns
				// column row selection
				// selection row column cell
				// editing row/column
				"column",
				"choice",
				new Object[][] {
						{ "integer", "width", null,
								new Integer(DEFAULT_COLUMN_WIDTH) },
						{ "choice", "sort", null,
								new String[] { "none", "ascent", "descent" } },
						{ "choice", "datatype", null,
								new String[] { "text", "numeric" } },
						{ "boolean", "selected", null, Boolean.FALSE } },
				"row",
				null,
				new Object[][] { { "boolean", "selected", null, Boolean.FALSE },
								 { "boolean", "enabled", null, Boolean.TRUE }},
				"cell",
				"choice",
				null,
				"tree",
				"list",
				new Object[][] { { "boolean", "angle", null, Boolean.FALSE },
						{ "method", "expand" }, { "method", "collapse" } },
				"node",
				"choice",
				new Object[][] {
						{ "boolean", "selected", null, Boolean.FALSE },
						{ "boolean", "expanded", null, Boolean.TRUE } },
				"separator",
				"component",
				null,
				"menubar",
				"component",
				new Object[][] { { "choice", "placement", "validate",
						new String[] { "top", "bottom" } } },
				"menu",
				"choice",
				new Object[][] { { "integer", "mnemonic", "paint", integer_1 } },
				"menuitem",
				"choice",
				new Object[][] { { "keystroke", "accelerator", null, null },
						{ "method", "action" },
						{ "boolean", "visible", "parent", Boolean.TRUE },
						{ "integer", "mnemonic", "paint", integer_1 } },
				"checkboxmenuitem",
				"menuitem",
				new Object[][] {
						{ "boolean", "selected", "paint", Boolean.FALSE }, // ...group
						{ "string", "group", "paint", null } }, // ...group
				"popupmenu",
				"component",
				new Object[][] { { "method", "menushown" } }, // Post menu:
																// Shift+F10
				"bean", "component",
				new Object[][] { 
						{ "bean", "bean", null, null }, 
						{ "boolean", "border", "validate", Boolean.FALSE }} };
	}
	
	/**
	 * @param column the column for which we like to set its width to its current smartwidth
	 */
	private void setSmartWidth(Object column) {
		Object header = getParent(column);
		Object item = get(header, ":comp");
		int index = 0;
		while(item!=null) {
			if(item==column) break;
			index++;
			item =get(item,":next");
		}
		setInteger(column,"width",(((int [])get(getParent(header),PROPERTY_SMARTWIDTHS))[index]));
	}
	
	/**
	 * returns currently set value of widget
	 * 
	 * @param widget
	 * @return 
	 *      boolean for checkbox and togglebutton 
	 *      Integer for slider and spinbox 
	 * 		String for label, textarea and textfield int : 
	 *      first selection index for tab,table, list, combo 
	 *      else returns null(for dialog, panel, splitpane etc)
	 */
	public  Object getValue(Object widget) {
		String classname = getComponentClass(widget);
		if (classname == "slider" || classname == "spinbox") {
			return getDouble(widget, "value");
		}
		if (classname == "checkbox" || classname == "togglebutton") {
			return getBoolean(widget, "selected");
		}
		if (classname == "textfield" || classname == "textarea" ||classname == "label") {
			return getString(widget, "text");
		}
		if (classname == "combobox" || classname == "tabbedpane"
				|| classname == "list" || classname == "table") {
			return getSelectedIndex(widget);
		}
		
		return getString(widget, "text");
	}
	
	/**
	 * sets current value of widget
	 * 
	 * @param widget
	 * @param value
	 *   	boolean for checkbox and togglebutton 
	 *      Integer for slider and spinbox 
	 * 		String for label, textarea and textfield int : 
	 *      first selection index for tab,table, list, combo 
	 *      else returns null(for dialog, panel, splitpane etc)
	 */
	public  void setValue(Object widget,Object value) {
		String classname = getComponentClass(widget);
		if (classname == "slider" || classname == "spinbox") {
			if(classname=="spinbox")setString(widget, "text",value+"");
			setDouble(widget, "value",((Number) value).doubleValue());
		}
		
		if (classname == "checkbox" || classname == "togglebutton") {
			setBoolean(widget, "selected",(boolean) value);
		}
		
		if (classname == "textfield" || classname == "textarea" ||classname == "label") {
			setString(widget, "text",value+"");
		}
		
		if (classname == "combobox" || classname == "tabbedpane"
				|| classname == "list" || classname == "table") {
			setSelectedItem(widget,getItem(widget,((Number) value).intValue()));
		}

		
	}
	 /**
     * Returns the index of the specified item in the item list of the
     * specified component.
     * Note that trees are <i>not</i> searched recursively.
     * This method should really be in Thinlet.java.
     *
     * @param thinlet  the Thinlet
     * @param component  the component; this should be a component that
     *   has sub-items such as tree, table, node etc.
     * @param item  the item to search for.
     * @return the index of the item, or -1 if the item cannot be found.
     */
    public  int getIndexOfItem(Object component, Object item)
    {
        Object[] items = getItems(component);
        for(int i = 0; i < items.length; ++i)
            if(item == items[i])
                return i;
        return -1;
    }


    /**
     * Sets the selected item of the specified component, deselects all others.
     * This method should really be in Thinlet.java.
     */
    public  void setSelectedItem( Object component, Object item)
    {
        Object[] items = getItems(component);
        if(items != null)
        {
            for(int i = 0; i < items.length; ++i)
                setBoolean(items[i], "selected", items[i] == item);
        }
    }


   
    /**
     * Returns a short string representation of the specified component,
     * for example "button:btnOk". Useful for debugging output.
     *
     * @param component  the component. If it is null, the string "null" is
     *                   returned.
     * @return a string representation of the component.
     */
    public  static String toString(Object component)
    {
        if(component == null)
            return "null";
        StringBuffer buf = new StringBuffer("[");
        String classname = getComponentClass(component);
        if(classname == null)
            buf.append("not a component: ").append(component.toString());
        else
        {
            buf.append(classname);
            String name = (String) get(component, "name");
            if(name != null)
                buf.append(':').append(name);
        }
        buf.append(']');
        return buf.toString();
    }
   
    /**
     * Gets the property value of the given component by the property key.
     */
    public static Color getColor(Object component, String key)
    {
        return (Color) get(component, key);
    }

    /**
     * Gets color used to render object.
     */
    public  Color getColorUsed(Object component, String key)
    {
        Color c= (Color) get(component, key);
        if(c==null){
        	if(key.equals("foreground"))return c_fg;
        	if(key.equals("foreground"))return c_bg;
        }
        return c;
    }

    /**
     * Gets the font of the given component, or the font of the thinlet, if
     * the font of the component has not been set explicitely.
     * This method should really be in Thinlet.java.
     */
    public  Font getFont(Object component)
    {
        Font font = (Font) get(component, "font");
       
        return font == null ? this.font : font;
    }
    
    /**
     * Gets the font of the given component, or the font of the thinlet, if
     * the font of the component has not been set explicitely.
 
     */
    public  String getFontName(Object component)
    {
        Font font = (Font) getFont(component);
        if(font==null)return null;
        return font.getName();
    }
    

  
	private void closeDialog(Object dialog){
		boolean b=	invoke(dialog, null, "close");
		//this.remove(dialog);
		//actually close dialog if script doest return true
		//if(Preferences.isSimulationLocked())return;
		if(!b && defaultHandler!=null)defaultHandler.onDialogClose(dialog);
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(toString(dialog)+" closed !");
	}
	
	private void minimizeDialog(Object dialog){
		Rectangle bounds = getRectangle(dialog, "bounds");
		int titleheight = getInteger(dialog, ":titleheight", block)+4;
		boolean minimized=false;
		//boolean scrollable=getBoolean(dialog, "scrollable");
		//boolean resizable=getBoolean(dialog, "resizable");
		//getBoolean(dialog, "resizable");
		if(get(dialog,":minimized")!=null){
			int height = getInteger(dialog, "height", 0);
			if(height==0){
				Dimension d=getPreferredSize(dialog);
				height=d.height;
			}
			setRectangle(dialog,"bounds",bounds.x,bounds.y,bounds.width,height);
			minimized=true;
			//setBoolean(dialog, "scrollable", true);
			//setBoolean(dialog, "resizable", resizable);
			set(dialog,":minimized",null);
		}else{
			setInteger(dialog, "height", bounds.height);
			
			//setBoolean(dialog, "scrollable", false);
			//setBoolean(dialog, "resizable", false);
			setRectangle(dialog,"bounds",bounds.x,bounds.y,bounds.width,titleheight);
			set(dialog,":minimized","true");
		}
		doLayout(dialog);
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(toString(dialog)+ "minimized = "+minimized);
	}
	
	private void maximizeDialog(Object dialog){
	//	if(org.shikhar.simphy.Simphy.DEBUG )System.out.println(toString(dialog)+" maximized !");
	}
	
	public void createDemo(){
		//new  org.shikhar.simphy.agui.Timer(this,null);
		/*
		Object tree;
		try {
			tree = parse("Timer.xml",this);
			add(content, tree);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * CustomComponent extends the lightweight AWT Component class the way to be
	 * functional inside <a href="http://www.thinlet.com/">Thinlet</a>. Thinlet
	 * itself manages its own components from scratch and therefor we are not
	 * able to simply add new Components to it. This class, for instance,
	 * provides the strict mimimum for Components to work in a Thinlet
	 * environment by simply changing theyr inheritence from Component to
	 * CustomComponent. A much better technique would be a Proxy.
	 */
	public abstract static class CustomComponent {
		protected Object component;
		protected Gui gui;
		protected Rectangle bounds;
		protected Rectangle clipRect;//clip in canvas space
		protected int width;
		protected int height;
		/**
		 * Default constructor
		 */
		public CustomComponent() {
			Dimension d=getPreferredSize();
			bounds = new Rectangle(0,0,d.width,d.height);
			clipRect= new Rectangle(0,0,d.width,d.height);
			//gui=GuiManager.gui;
		}

		public void setText(String string) {
			// TODO Auto-generated method stub

		}

		/**
		 * Called by Gui when creating a new instance of this. For further
		 * processing we need to know which component we are inside GUi.
		 *
		 * @param component
		 */
		void setComponent(Object component) {
			this.component = component;
			this.setText(gui.getString(component, "text"));
	
		}

		/**
		 * Returns gui Component associcted with canvas if any
		 */
		Object getComponent(){
			return this.component;
		}
		/**
		 * Called by Gui, we need a reference back to it.
		 *
		 * @param gui
		 */
		void setGui(Gui gui) {
			this.gui = gui;
			if(!gui.beans.contains(this)){
				gui.beans.add(this);
			}
		}

		/**
		 * Set the cursor image to a predefined cursor.
		 * <P>
		 *
		 * @param cursor
		 *            One of the constants defined by the {@link Cursor} class.
		 *            If this parameter is <TT>null</TT> the parent cursor will
		 *            be inherited
		 */
		public void setCursor(String cursor) {
			this.gui.awtComponent.style.cursor=(cursor);
		}

		/**
		 * Gets the cursor set on this component.
		 * <P>
		 *
		 * @return The cursor for this component.
		 */
		public String getCursor() {
			return this.gui.awtComponent.style.cursor;//getCursor());
		}

		/**
		 * Painting is done by gui, so when this calls repaint it will
		 * simply be redirected the way it should back to gui.
		 */
		public void paintBackground(Graphics g) {
			if(gui==null)return;
			
			Color fill = gui.getColor(component, "background", null);
			AWTImage icon = gui.getIcon(component, "bgimage", null);
			if (fill != null) {
				g.setColor(fill);
				g.fillRect(-1, -1, width+1, height+2);
			}
			if(icon!=null){
				//icon.draw(g.gl,0, 0,null);
				g.drawImage(icon, 0, 0,width, height);
			}
			
			if ( gui.getBoolean(component, "border", false)) {
				g.setColor(component==gui.mousepressed?gui.c_border:gui.getColor(component, "foreground", null));
				g.drawRect(0, 0, width, height);
			}
		}

		/**
		 * coordinates are in frame of its parent with top left as origin and
		 * right down as positive axes
		 * 
		 * @param g
		 */
		public void paint(Graphics g) {
			paintBackground(g);
		}
		
		
		/**
		 * 
		 * @param dt
		 * @param revalidate
		 *            true if something which may affect widget state may have
		 *            changed (ex. global variables, theme)
		 */
		public void update(float dt, boolean revalidate) {
			//gui.renderComp(this.component);
			//Gui.repaintNeeded=true;
		}


		/**
		 * Called when the component no longer exists in gui tree, Notifies component to dispose resources created if any
		 * @param g
		 */
		public void dispose(Graphics g) {
			
		}

		/**
		 * returns true if handled (by defaut retiurns false)
	 * Handles Mouse motion Event, 
	 * @param id
	 * @param x
	 * @param y
	 * @param button {Mouse button} 
	 * @param clickCount {Number} Number of clicks
	 * @param ctrlKey {boolean} if ctrl key is pressed
	 * @param altkey {boolean} if alt key is pressed
	 * @param shiftkey {boolean} if shift key is pressed
	 */
		public boolean handleMouseEvent(int x, int y, int clickcount, int id,int button,boolean shiftdown, boolean controldown, boolean popuptrigger) {
			return false;
		}

		/**
		 * returns true if handled (by defaut retiurns false)
		 */
		public boolean handleMouseWheel(int x, int y,int wheel) {
			return false;
		}

		/**
		 * returns true if handled (by defaut retiurns false)
		 */
		public boolean handleKeyEvent(int keychar, int keycode, int id, boolean shiftdown, boolean controldown, int modifiers) {
			return false;

		}
		
	
		/**
		 * 
		 * @return if true then event is assumed as consumed and popup should not be handled by gui
		 */
		public boolean handlePopUp(int x,int y){
			return false;
		}
		/**
		 * preferred size used for layout
		 * 
		 * @return
		 */
		public abstract Dimension getPreferredSize();

		/**
		 * the fully qualified name of the desired class, so that object can be
		 * invoked
		 * 
		 * @return
		 */
		public abstract String getBeanClassName();

		/**
		 * called by gui when layout is done for the widgets
		 * @param r
		 */
		public void setBounds(Rectangle r) {
			this.bounds = r;
			if(r.width>0)width = (int) r.width;
			if(r.height>0)height = (int) r.height;
		}

		/**
		 * returns size of widget 
		 * @return
		 */
		public Dimension getSize() {
			return new Dimension(width, height);
		}

		/**returns widget Width in pixels*/
		public int getWidth() {
			return width;
		}
		/**returns widget Height in pixels*/
		public int getHeight() {
			return height;
		}

	
	}


	class DrawStyle{
		Color foreColor=c_text_fg;
		Color backColor=c_text_bg;
		char style=0;
		int begin=0;
		int end=0;
		Font font;
		int yOffset=0;
		boolean underline=false;
		
		DrawStyle(int begin,int end,Color foreColor,Color backColor,char style){
			this(begin, end, foreColor, backColor, style,	false,0);
		}
		
		DrawStyle(int begin,int end,Color foreColor,Color backColor,char style,boolean underline,int offset){
			this.begin=begin;
			this.end=end;
			this.foreColor=foreColor;
			this.backColor=backColor;
			this.underline=underline;
			this.yOffset=offset;
			
			if(style=='n'||style=='u' ||style=='e'||style=='r'){
				if(style=='u'){
					this.underline=true;
				}else if(style=='e'){
					this.yOffset=3;
				}else if(style=='r'){
					this.yOffset=-3;
				}
				style=0;
			}
			setStyle(style);

		}
		
		public String toString(){
			char s=style;
			if(style==0){
				if(font==null){
					s='n';
				}else if(font.getName().equals("default-large")){
					s='l';
				}else if(font.getName().equals("default-small")){
					s='s';
				}else if(font.getName().equals("default-bold")){
					s='b';
				}else if(font.getName().equals("default-italic")){
					s='i';
				}else if(font.getName().equals("default-normal")){
					s='n';
				}
			}
			return  begin+","+ end+","+(foreColor==null?"": foreColor.toString())+","+ (backColor==null?"": backColor.toString())+","+s+","+underline+","+yOffset;
		}
		
		public void setStyle(char style){
			/*
			this.style=style;
			switch(style){
			
			case 'b':
				font=new Font("default-bold",g.context);
				break;
			case 'i':
				font=new Font("default-italic",g.context);
				break;
			case 'l':
				font=new Font("default-large",g.context);
				break;
			case 's':
				font=new Font("default-small",g.context);
				break;
			case 'w':
				//font=new Image("");
				//foreColor=Color.WHITE;
				break;
			case 'q':
				font=new Font("default-icons",g.context);
				break;
			case 'n': //reset styles
				font=new Font("default-normal",g.context);
				yOffset=0;
				underline=false;
				foreColor=null;
				backColor=null;
				break;
			default:
				this.style=0;
				if(font==null)font=new Font("default-normal",g.context);
				if(yOffset>0)yOffset=font.getDescent();
				if(yOffset<0)yOffset=-font.getDescent();
			}
			*/
		}
		
		void reset(){
			style=0;
			yOffset=0;;
			font=new Font("default-normal",g.context);
			foreColor=null;
			backColor=null;
			underline=false;
		}

	}

	
	
	private  DrawStyle readStyle(String s){
		if(s==null||s.trim().isEmpty())return null;
		String[] params=s.split(",");if(s.length()<5)return null;
		int start; int end; Color foreColor;Color backColor;char style;boolean underline=false;int yOffset=0;
		try{
			start=Integer.parseInt(params[0]);
			end=Integer.parseInt(params[1]);
			foreColor=new Color(params[2]);
			backColor=new Color(params[3]);
			//if(foreColor==null && backColor==null)return null;
			style=params[4].charAt(0);
			if(params.length>5)underline=Boolean.parseBoolean(params[5]);
			if(params.length>6)yOffset=Integer.parseInt(params[6].trim());
			return new DrawStyle(start,end,foreColor,backColor,style,underline,yOffset);
		}catch(Exception e){
			return null;
		}
	}
	
	public String stylesToString(DrawStyle[] styles){
		if(styles==null||styles.length==0)return "";
		String s="";
		for(DrawStyle style:styles){
			s+=style.toString()+" : ";
		}
		return s.substring(0, s.length()-2);
	}

	public DrawStyle[] stringToStyles(String s){
		defaultStyle=new DrawStyle(0,0,null,null,'n');
		if(s==null||s.trim().isEmpty())return null;
		
		String[] strStyles=s.split(" : ");
		if(strStyles.length==0)return null;
		ArrayList<DrawStyle> styles=new ArrayList<DrawStyle>();
		
		for(String str:strStyles){
			DrawStyle style=readStyle(str);
			if(style!=null)styles.add(style);
		}
		return styles.toArray(new DrawStyle[0]);
	}


	DrawStyle defaultStyle;
	private void updateDrawStyle(Object textarea,int min,int length){
		DrawStyle[] styles=(DrawStyle[]) getProperty(textarea,"drawstyle");
		if(styles==null)return;
		for(DrawStyle style:styles){
			//special case if styles completely overlap
			if(style.end<=min){
				continue;
			}else if(style.begin>=min){
				style.begin+=length;
				style.end+=length;
			}else{
				style.end+=length;
			}
		}
	}
	
	/**
	 * returns style at specified character position
	 * @param charPos
	 * @return
	 */
	private DrawStyle getDrawStyle(int charPos,DrawStyle[] styles){
		if(styles==null)return defaultStyle;
		for(DrawStyle style:styles){
			if(style.begin<=charPos && style.end>charPos){
				return style;
			}
		}
		return defaultStyle;
	}
	
	private int getCharsWidth(char[] chars,int s, int length,DrawStyle[] styles){
		if(styles==null)return defaultStyle.font.charsWidth(chars, s, length);
		int w=0;
		for(int i=s;i<s+length;i++){
			if(i>=chars.length)break;
			w+=getDrawStyle(i,styles).font.charWidth(chars[i]);
		}
		return w;
	}
	
	private int getCharsHeight(char[] chars,int s, int length,DrawStyle[] styles){
		if(styles==null)return defaultStyle.font.getHeight();
		int h=0;//defaultStyle.font.getHeight();
		if(length==0)length=1;
		for(int i=s;i<s+length;i++){
			if(i>=chars.length)break;
			h=Math.max(h, getDrawStyle(i,styles).font.charHeight(chars[i]));
		}
		return h;
	}
	
	public void updateDrawSytle(Object textarea,Color fg, Color bg,char mode){
		updateDrawSytle(textarea,fg,bg,mode,-1,-1);
	}
		
	public void updateDrawSytle(Object textarea,Color fg, Color bg,char mode,int start,int end){
		int i = getInteger(textarea, "start", 0);
		int e =  getInteger(textarea, "end", 0);
		if(start==-1) start=Math.min(i,e);
		if(end==-1) end=Math.max(i,e);
		int length=getString(textarea, "text").length();
		if(end==start){
			setColor(textarea,fg!=null?"foreground":"background",fg!=null?fg:bg);
			return;
		}
		DrawStyle[] styles=(DrawStyle[]) getProperty(textarea,"drawstyle");
		DrawStyle newStyle=new DrawStyle(start,end,fg,bg,mode);
		if(styles==null ||styles.length==0){
			styles=new DrawStyle[]{newStyle};
		}else{
			boolean newAdded=false;
			ArrayList<DrawStyle> drawStyles=new ArrayList<DrawStyle>();
			for(DrawStyle style:styles){
				if(style.end<=style.begin)continue;
				//special case if styles completely overlap
				if(style.begin==start && style.end==end){
					if(newStyle.backColor!=null)style.backColor=newStyle.backColor;
					if(newStyle.foreColor!=null)style.foreColor=newStyle.foreColor;
					if(newStyle.style==0){
						if(mode=='u')style.underline=newStyle.underline;
						if(mode=='e'||mode=='r')style.yOffset=newStyle.yOffset;
						if(mode=='n')style.reset();
					}else if(newStyle.style=='n'||mode=='n'){
						style.reset();
					}
					style.setStyle(newStyle.style);
					return;
				}
				//add all styles that lie completeley before this style
				if(style.end<start){
					drawStyles.add(style);
					continue;
				}
				
				//check over lapping styles
				if(style.begin<=start && style.end>=start && style.end<=end){
					int se=style.end;
					style.end=start;
					if(style.end>style.begin)drawStyles.add(style);
					if(!newAdded)drawStyles.add(newStyle);
					newAdded=true;
				}else if(style.begin<=start && style.end>end){
					int se=style.end;
					style.end=start;
					if(style.end>style.begin)drawStyles.add(style);
					if(!newAdded){
						if(newStyle.backColor==null)newStyle.backColor=style.backColor;
						if(newStyle.foreColor==null)newStyle.foreColor=style.foreColor;
						if(newStyle.style==0)newStyle.setStyle(style.style);
						drawStyles.add(newStyle);
					}
					newAdded=true;
					drawStyles.add(new DrawStyle(end,se,style.foreColor,style.backColor,style.style));
				}else if(style.begin>=start && style.begin<=end && style.end<=end){
					if(!newAdded)drawStyles.add(newStyle);
					newAdded=true;
					continue;
				}else if(style.begin>=start && style.begin<=end && style.end>end){
					if(!newAdded)drawStyles.add(newStyle);
					newAdded=true;
					//truncate partially overlapped styles
					style.begin=end;
					drawStyles.add(style);
				}else if(style.begin>end && style.begin<length){
					if(!newAdded)drawStyles.add(newStyle);
					newAdded=true;
					drawStyles.add(style);
				}
			}
			if(!newAdded)drawStyles.add(newStyle);
			//remove empty styles and default styles with no colors
			for(i=drawStyles.size()-1;i>=0;i--){
				DrawStyle s=drawStyles.get(i);
				if(s.begin<0)s.begin=0;
				if(s.end>=length-1)s.end=length;
				if(((s.style==0||s.style=='n')&& (!s.underline) &&(s.yOffset==0) && s.foreColor==null && s.backColor==null) ||s.begin>s.end){
					drawStyles.remove(i);
				}
			
			}
			styles=drawStyles.toArray(new DrawStyle[0]);
		}
		
		
		putProperty(textarea,"drawstyle",styles);
		
	}

	

}


/**
 * A timer class for UI timing - like auto scrolling.
 * 
 * @author Mahesh Kurmi
 */
 class GuiTimer {

	private  enum State{RUNNING,PAUSED,STOPPED};
	private State state=State.STOPPED;

    /**number of millisoconds between 2 successive ticks*/
    int delay = 100;
    /** number of times timer ticked, resets to zero on start*/
    long num_ticks=0;
    /**maximum number of times timer ticks, -1 for infinity*/
    private long max_ticks=1;
    /**Listener for tick events*/
    private Gui callback;
    /**elapsed time in millisec since start/last tick*/
    protected int  elapsedTime=0;
   
    protected Object invokerCompoenent=null;
    /**
     * Constructs a new timer which runs only once after 100 millisec
     *
     * @param delay delay in millisec
     */
    public GuiTimer() {
       this(100);
    }
    /**
     * Constructs a new timer which runs only once after specified delay
     *
     * @param delay delay in millisec
     */
    public GuiTimer(int delay) {
       this.delay=delay;
    }

    /**
     * Constructs a new timer which runs 'numTicks' times (provided not stopped in between)
     *
     * @param delay delay in millisec
     * @param maxTicks number of times timer runs, pass -1 to run it continuously
     */
    public GuiTimer(int delay, long maxTicks) {
       this.delay=delay;
       this.max_ticks=maxTicks;
    }
    
    /**
     * Sets the delay in ms till next expiration.
     *
     * @param delay in ms, setting delay<1 disables timer
     * @throws
     */
    public void setDelay(int delay) {
        this.delay = delay;
        num_ticks=0;
        elapsedTime=0;
    }
    
    /**
     * Returns delay in millisec 
     * @return
     */
    public int getDelay(){
    	return this.delay;
    }
    
    
  
    /**
     * Returns true if the timer is a continuous firing timer.
     * @return true if the timer is a continuous firing timer.
     */
    public boolean isContinuous() {
        return  this.max_ticks<0;
    }

    /**
     * Sets the timer continous mode. A timer in continuous mode must be stopped manually.
     * @param continuous true if the timer should auto restart after firing.
     */
    public void setContinuous(boolean continuous) {
        if(max_ticks<0 && !continuous){
        	max_ticks=1;//allow infinite ticks if continuous
        }else if(continuous){
        	max_ticks=-1;
        }
        num_ticks=0;
        elapsedTime=0;
    }
    
    /**
     * Sets the timer in counter mode. A timer in counter mode stops after it ticks specified number of times.
     * @param count number of times timer runs, pass -1 to run it continuously
     */
    public void setMaxTickCount(long count) {
        this.max_ticks=count;
        num_ticks=0;
        elapsedTime=0;
    }
    
    /**
     * Returns max number of times timer will tick
     * @return
     */
    public long getMaxTickCount(){
    	return this.max_ticks;
    }
    /**
     * Sets the callback that should be executed once the timer expires.
     * @param callback the callback.
     */
    public void setCallback(Gui callback) {
        this.callback = callback;
    }

    /**
     * Returns true if the timer is already running.
     * @return true if the timer is already running.
     */
    public boolean isRunning() {
        return state==State.RUNNING && (max_ticks<0 ||num_ticks<max_ticks);
    }
    
    /**
     * returns true if timer is either running or paused
     * @return
     */
    public boolean isActive() {
        return state!=State.STOPPED && (max_ticks<0 ||num_ticks<max_ticks);
    }
    
    /**
     * pauses timer if running, else does nothing
     * @return
     */
    public void pause() {
        if(state==State.RUNNING)state=State.PAUSED;
    }
    /**
     * resumes timer  if paused , else does nothing
     * @return
     */
    public void resume() {
        if( state==State.PAUSED) state=State.RUNNING;
    }
 
    /**
     * Starts the timer from beginning irrespective of currently running,paused or stopped. 
     */
    public void reStart() {
        state=State.RUNNING;
        num_ticks=0;
        elapsedTime=0;
    }
    
    /**
     * Starts the timer from beginning irrespective of currently paused or stopped. 
     * If it is already running then this method does nothing.
     */
    public void start() {
    	if( state==State.RUNNING) return;
        state=State.RUNNING;
        num_ticks=0;
        elapsedTime=0;
    }
    
    /**
     * Stops the timer. If the timer is not running then this method does nothing.
     */
    public void stop() {
      state=State.STOPPED;
      num_ticks=0;
      elapsedTime=0;
    }
    
  
    
    /**
     * Called when time elapsed is more then or equal to delay specified
     * @param delta
     * @return true if timer is still running, false if timer has ticked allowed number of times
     */
   public boolean update(int delta) {
         if(state==State.RUNNING && delay>0) {
        	elapsedTime+=delta;
        	if(elapsedTime>=delay){
        		//elapsedTime-=delay;
        		elapsedTime=elapsedTime % delay;
        		num_ticks++;
        		doCallback();
            	if((num_ticks>=max_ticks) && max_ticks>0){
            		stop();
            		return false;
            	}
            	Gui.repaintNeeded=true;
                //gui.timerCallBack(this);
        	}
        	return true;
         }
        return false;  
    }

    private void doCallback() {
        if(callback != null) {
            try {
                callback.onTimerTick(this);
            } catch (Throwable ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, "Exception in callback", ex);
            }
        }
    }
    
   
    public class Canvas extends CustomComponent{

    	@Override
    	public Dimension getPreferredSize() {
    		// TODO Auto-generated method stub
    		return new Dimension(200,100);
    	}

    	@Override
    	public String getBeanClassName() {
    		// TODO Auto-generated method stub
    		return null;
    	}

    	public void reset() {
    		// TODO Auto-generated method stub
    		
    	}
    	
    }


}
 
 
