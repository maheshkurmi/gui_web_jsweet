package org.shikhar;

import static def.dom.Globals.addEventListener;
import static def.dom.Globals.console;
import static def.dom.Globals.document;
import static def.dom.Globals.window;
import static jsweet.util.Lang.union;

import java.io.IOException;


import def.dom.CanvasRenderingContext2D;
import def.dom.Element;
import def.dom.Event;
import def.dom.HTMLCanvasElement;
import def.dom.XMLHttpRequest;
import def.js.Array;
import def.js.Math;
import jsweet.util.StringTypes;

public class CanvasDrawing {

	public static void main(String[] args) {
		window.onload = (e) -> {
			return new CanvasDrawing();
		};
	}

	public static HTMLCanvasElement canvas;
	private CanvasRenderingContext2D ctx;
	private double angle = 0;
	private double prevTime=-1;
	private static Gui gui;
	public static HTMLCanvasElement backgroundLayer;
	
	public CanvasDrawing() {
		console.info("creating canvas drawing example");
		canvas = (HTMLCanvasElement) document.getElementById("canvas");
		backgroundLayer= (HTMLCanvasElement) document.getElementById("backgroundLayer");
		Element body = document.querySelector("body");
		double size = Math.min(body.clientHeight, body.clientWidth);
		canvas.width = 2*body.clientWidth ;
		canvas.height = 2*body.clientHeight ;
		//canvas.style.width
		canvas.style.top = "0px";//(body.clientHeight / 2 - size / 2 + 10) + "px";
		canvas.style.left = "0px";//(body.clientWidth / 2 - size / 2 + 10) + "px";
		canvas.style.width=body.clientWidth+"px" ;
		canvas.style.height=body.clientHeight+"px" ;
		
		backgroundLayer.style.top = "0px";//(body.clientHeight / 2 - size / 2 + 10) + "px";
		backgroundLayer.style.left = "0px";//(body.clientWidth / 2 - size / 2 + 10) + "px";
		backgroundLayer.style.width=body.clientWidth+"px" ;
		backgroundLayer.style.height=body.clientHeight+"px" ;
	
		window.addEventListener("resize", CanvasDrawing::onWindowResize, false);

	
		ctx = canvas.getContext(StringTypes._2d);
		gui =new Gui(canvas);
		
		//console.log("Loading gui " +gui);
		
		String demoxml="<dialog text=\"Find\" columns=\"3\" top=\"4\" left=\"4\" bottom=\"4\" right=\"4\" gap=\"4\">\r\n"
				+ "	<label text=\"Find what:\" colspan=\"3\" />\r\n"
				+ "	<combobox name=\"ch_what\" colspan=\"3\" valign=\"center\" />\r\n"
				+ "	<label text=\"Direction:\" alignment=\"right\" />\r\n"
				+ "	<checkbox name=\"rb_up\" text=\"Up\" group=\"direction\" />\r\n"
				+ "	<checkbox name=\"rb_down\" text=\"Down\" group=\"direction\" selected=\"true\" />\r\n"
				+ "	<checkbox name=\"cb_match\" text=\"Match case\" selected=\"true\" colspan=\"3\"/>\r\n"
				+ "	<button name=\"b_cancel\" text=\"Cancel\" type=\"default\" action=\"closeDialog\" />\r\n"
				+ "	<button name=\"b_find\" type=\"default\" text=\"Find next\"\r\n"
				+ "			action=\"findText(ch_what, ch_what.text, cb_match.selected, rb_down.selected)\" />\r\n"
				+ "	<label />\r\n"
				+ "</dialog>"
				;
		/*
		try {
			//console.log(demoxml);
			Object o=gui.parsefromString(demoxml,false,false,null);
			gui.add(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//console.log("Adding Dialog");
		Object dlg=Gui.create("dialog");
		console.log(dlg);
		
		gui.setInteger(dlg, "columns",2);
		gui.setBoolean(dlg, "resizable", true);
		
		gui.setInteger(dlg, "gap",6);
		//	gui.setInteger(dlg, "height",200);
		gui.setString(dlg, "text","Text Header");
		Object btn=Gui.create("button");
		gui.setString(btn, "text","Click Me !");
		gui.setString(btn, "tooltip","Don't try to mesh with Me!");
		gui.setInteger(btn, "width",100);
		gui.setInteger(btn, "height",50);
		gui.add(dlg,btn);
		
		btn=Gui.create("button");
		gui.setString(btn, "text","Hello world!");
		gui.setInteger(btn, "width",100);
		gui.setInteger(btn, "height",50);
		//gui.setBoolean(btn, "resizable",true);
		
		gui.add(dlg,btn);
		btn=Gui.create("slider");
			//gui.setString(btn, "text","Hello world!");
			gui.setInteger(btn, "minimum",10);
			gui.setInteger(btn, "maximum",50);
			gui.setInteger(btn, "colspan",2);
		gui.add(dlg,btn);
		
		btn=Gui.create("label");
		gui.setString(btn, "text","Textarea Demo!");
		//gui.setInteger(btn, "weightx",1);
		//gui.setInteger(btn, "weighty",1);
		//gui.setInteger(btn, "colspan",2);
		gui.add(dlg,btn);
		
		btn=Gui.create("textarea");
		gui.setString(btn, "text","Hello world! \n Whats ip......\n Thats good !");
		gui.setInteger(btn, "weightx",1);
		gui.setInteger(btn, "weighty",1);
		//gui.setInteger(btn, "colspan",2);
		
		
		gui.add(dlg,btn);
		
		gui.add(dlg);
	//	console.log(gui.getDesktop());
		
		XMLHttpRequest client = new XMLHttpRequest();
		client.open("GET", "grapher.xml");
		client.onloadend=(def.dom.ProgressEvent e)-> {
			try {
				String xml=client.responseText;
				//console.log(xml);
				Object o=gui.parsefromString(xml,true,true,null);
				console.log(o);
				gui.add(o);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			return null;
		};
		client.send();
		prevTime=window.performance.now();
		draw();
	}
	


	public static Object onWindowResize(Event event) {

		gui.setViewPort((int)window.innerWidth , (int)window.innerHeight);
		//.aspect = window.innerWidth / window.innerHeight;
		//camera.updateProjectionMatrix();

		//renderer.setSize(window.innerWidth, window.innerHeight);

		return null;
	}

	private void draw() {
		int color = (int) (Math.pow(2, 8 * Math.floor(angle / Math.PI * 2) - 1));

		//ctx.fillStyle = union("rgb(" + (color >> 16 & 0xFF) + "," + (color >> 8 & 0xFF) + "," + (color & 0xFF) + ")");
		//console.log(ctx.fillStyle, color + "opp" + Math.floor(angle / Math.PI * 2));
/*
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		
		ctx.beginPath();
		ctx.moveTo(canvas.width / 2, canvas.height / 2);
		ctx.lineTo(canvas.width, canvas.height / 2);
		ctx.arc(canvas.width / 2, canvas.height / 2, canvas.width / 20, 0, angle);
		ctx.fill();
		if (angle < Math.PI ) {
			angle += 0.5;
			
		}else {
			//gui.render();
		}
		*/
		window.requestAnimationFrame((time) -> {
			int dt=(int) (time-prevTime);
			prevTime=time;
			this.draw();
			gui.updateUI(dt, false);
		});
		
		//console.log("gui drawing begins");
		gui.render();
	}

	void test() {
		Array<String> a = new Array<>();
		for (@SuppressWarnings("unused")
		String aTestVar : a) {
			console.log(a);
		}
	}

	void test1(String aTestParam1) {
		console.log(aTestParam1);
	}

	void test1(int aTestParam2) {
		console.log(aTestParam2);
	}

}
