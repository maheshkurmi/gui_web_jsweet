package quickstart;

import static def.dom.Globals.alert;
import static def.dom.Globals.document;
import static def.jquery.Globals.$;

import java.util.ArrayList;
import java.util.List;

import def.dom.HTMLCollectionOf;
import def.dom.HTMLDivElement;
import def.js.Array;
import jsweet.util.StringTypes;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import sun.awt.geom.AreaOp;
/**
 * This class is used within the webapp/index.html file.
 */
public class QuickStart {

	public static void main(String[] args) {
		// you can use regular Java API
		List<String> l = new ArrayList<>();
		l.add("Hello");
		l.add("world");
		// and you can also use regular JavaScript APIs
		Array<String> a = new Array<>();
		a.push("Hello", "world");
		// use of jQuery with the jQuery candy
		$("#target").text(l.toString());
		// use of the JavaScript DOM API
		alert(a.toString());

		HTMLCollectionOf<HTMLDivElement> nodeList = document.getElementsByTagName(StringTypes.div);
		for (HTMLDivElement element : nodeList) {
			element.innerText = "Hello again in vanilla JS";
		}
		
		 Area area = new Area(new Rectangle2D.Double(0, 0, 200, 100));
		    System.out.println(area.getBounds2D());
		    System.out.println(new Rectangle2D.Double(0, 0, 200, 100).getPathIterator(null));
		    
		    area.add(new Area(new Rectangle2D.Double(0, 0, 200, 100)));
		    
		    area.add(new Area(new Rectangle2D.Double(50, -50, 300, 100)));
		    // Check area bounds
		    Rectangle2D rectangle = area.getBounds2D();
		    // assertEquals("Wrong rectangle", 0, rectangle.getX());
		    System.out.println(rectangle.getX());
		    // assertEquals("Wrong rectangle", -50, rectangle.getY());
		    System.out.println(rectangle.getY());
		    // assertEquals("Wrong rectangle", 350, rectangle.getWidth());
		    System.out.println(rectangle.getWidth());
		    // assertEquals("Wrong rectangle", 150, rectangle.getHeight());
		    System.out.println(rectangle.getHeight());
		    
		    for (HTMLDivElement element : nodeList) {
				element.innerText = "Hello again in vanilla JS+rectangle.getWidth()"+rectangle.getWidth();
			}
	}

}
