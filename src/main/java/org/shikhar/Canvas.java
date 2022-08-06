package org.shikhar;

import static def.dom.Globals.console;
import static def.dom.Globals.document;
import static def.dom.Globals.window;
import static jsweet.util.Lang.union;

import org.shikhar.Gui.CustomComponent;

import def.dom.CanvasRenderingContext2D;
import def.dom.HTMLElement;
import def.js.Math;
import def.stats.Stats;
import def.threejs.three.BoxGeometry;
import def.threejs.three.Color;
import def.threejs.three.DirectionalLight;
import def.threejs.three.Geometry;
import def.threejs.three.Intersection;
import def.threejs.three.Light;
import def.threejs.three.Mesh;
import def.threejs.three.MeshLambertMaterial;
import def.threejs.three.MeshLambertMaterialParameters;
import def.threejs.three.PerspectiveCamera;
import def.threejs.three.Raycaster;
import def.threejs.three.Scene;
import def.threejs.three.Vector2;
import def.threejs.three.WebGLRenderer;
import def.threejs.three.WebGLRendererParameters;
import def.threejs.three.Raycaster.Coords;

public class Canvas extends CustomComponent{
	static HTMLElement container;
	static Stats stats;
	static PerspectiveCamera camera;
	static Scene scene;
	static Raycaster raycaster;
	static WebGLRenderer renderer;

	static double mouseX,mouseY;// = new Vector2();
	static Mesh INTERSECTED;

	static double radius = 100;
	static double theta = 0;
	
	private boolean initialised=false;
	public Canvas() {
		super();
		init();
	}
	

	public  void init() {
		container = document.createElement("div");
		document.body.appendChild(container);
		camera = new PerspectiveCamera(70,width / height, 1, 10000);

		scene = new Scene();

		Light light = new DirectionalLight(0xffffff, 1);
		light.position.set(1, 1, 1).normalize();
		scene.add(light);

		Geometry geometry = new BoxGeometry(20, 20, 20);

		for (int i = 0; i < 100; i++) {

			Mesh object = new Mesh(geometry, new MeshLambertMaterial(new MeshLambertMaterialParameters() {
				{
					color = union(Math.random() * 0xffffff);
				}
			}));
			object.name="Obj"+i;
			object.position.x = Math.random() * 800 - 400;
			object.position.y = Math.random() * 800 - 400;
			object.position.z = Math.random() * 800 - 400;

			object.rotation.x = Math.random() * 2 * Math.PI;
			object.rotation.y = Math.random() * 2 * Math.PI;
			object.rotation.z = Math.random() * 2 * Math.PI;

			object.scale.x = Math.random() + 0.5;
			object.scale.y = Math.random() + 0.5;
			object.scale.z = Math.random() + 0.5;

			scene.add(object);

		}

		raycaster = new Raycaster();
		WebGLRendererParameters params=new WebGLRendererParameters() {};
		//params.canvas=CanvasDrawing.backgroundLayer;
		params.antialias=true;
		renderer = new WebGLRenderer(params);
		renderer.setClearColor(0xf0f0f0);
		renderer.setPixelRatio(window.devicePixelRatio);
		renderer.setSize(width, height);
		renderer.sortObjects = false;
		//container.appendChild(renderer.domElement);
		//container.appendChild(renderer.domElement);

		stats = new Stats();
		stats.domElement.style.position = "absolute";
		stats.domElement.style.top = "0px";
		document.body.appendChild(stats.domElement);
		//container.appendChild(stats.domElement);

	}
	
	public static void render3D() {
		camera.position.x = radius * Math.sin(def.threejs.three.Math.degToRad(theta));
		camera.position.y = radius * Math.sin(def.threejs.three.Math.degToRad(theta));
		camera.position.z = radius * Math.cos(def.threejs.three.Math.degToRad(theta));
		camera.lookAt(scene.position);

		camera.updateMatrixWorld(true);

		// find intersections

		raycaster.setFromCamera(new Coords() {
			{
				x = mouseX;
				y = mouseY;
			}
		}, camera);

		Intersection[] intersects = raycaster.intersectObjects(scene.children);

		if (intersects.length > 0) {
			console.log("intersection: "+intersects[0]);
			if (INTERSECTED != intersects[0].object) {

				// This part is horrible partially because there is a mistake in the definition file (emissive is a color)
				if (INTERSECTED != null) {
					((Color) (Object) ((MeshLambertMaterial) INTERSECTED.material).emissive)
							.setHex((double) INTERSECTED.$get("currentHex"));
				}
				INTERSECTED = (Mesh) intersects[0].object;
				INTERSECTED.$set("currentHex",
						((Color) (Object) ((MeshLambertMaterial) INTERSECTED.material).emissive).getHex());
				((Color) (Object) ((MeshLambertMaterial) INTERSECTED.material).emissive).setHex(0xff0000);

			}

		} else {

			if (INTERSECTED != null) {
				((Color) (Object) ((MeshLambertMaterial) INTERSECTED.material).emissive)
						.setHex((double) INTERSECTED.$get("currentHex"));
			}
			INTERSECTED = null;

		}
		renderer.render(scene, camera);

	}

	/**
	 * coordinates are in frame of its parent with top left as origin and
	 * right down as positive axes
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
//		if(initialised==false) {
//			init();
//		}
//		initialised=true;
		super.paint(g);
		//CanvasRenderingContext2D ctx=g.context;
		render3D();
		g.context.drawImage(renderer.domElement,0,0,this.width,this.height);
		
		g.context.fillText("Mouse on "+(INTERSECTED==null?null:INTERSECTED.name), 100, 100);
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

		theta += 0.1;

		
		 Canvas.stats.update();
	}
	
	public boolean handleMouseEvent(int x, int y, int clickcount, int id,int button,boolean shiftdown, boolean controldown, boolean popuptrigger) {
		mouseX=x;
		mouseY=y;
		mouseX=(mouseX/ this.width) * 2 - 1;
		mouseY=-(mouseY/ this.height) * 2 + 1;
		//mouse.set(x, y);
		if(id==AWTMouseEvent.MOUSE_MOVED ) {
			//camera.position.x+=(x-prevPoint.x);
			//camera.position.y+=(y-prevPoint.y);
			
			
		}
		
		camera.updateProjectionMatrix();
		return false;
	}

	/**
	 * returns true if handled (by defaut retiurns false)
	 */
	public boolean handleMouseWheel(int x, int y,int wheel) {
		
		camera.fov+=(wheel/10);
		camera.updateProjectionMatrix();
		return false;
	}

	/**
	 * returns true if handled (by defaut retiurns false)
	 */
	public boolean handleKeyEvent(int keychar, int keycode, int id, boolean shiftdown, boolean controldown, int modifiers) {
		return false;

	}
	
	/**
	 * called by gui when layout is done for the widgets
	 * @param r
	 */
	public void setBounds(Rectangle r) {
		this.bounds = r;
		if(r.width>0)width = (int) r.width;
		if(r.height>0)height = (int) r.height;
		renderer.setSize(width, height);
		camera.aspect = width / (double)height;
		camera.updateProjectionMatrix();

		
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(400,300);
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
