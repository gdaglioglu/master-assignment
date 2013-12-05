package example.ui.controllers;

import example.ui.models.ModelObserver;
import example.ui.views.ViewObserver;

//ControllerObserver.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

//inspired by Joseph Bergin's MVC gui at http://csis.pace.edu/~bergin/mvc/mvcgui.html

//ControllerObserver is a Listener

public class ControllerObserver implements java.awt.event.ActionListener {

	// Joe: ControllerObserver has ModelObserver and ViewObserver hardwired in
	ModelObserver modelObserver;
	ViewObserver viewObserver;

	public ControllerObserver() {
		System.out.println("ControllerObserver()");
	} // ControllerObserver()

	// invoked when a button is pressed
	public void actionPerformed(java.awt.event.ActionEvent e) {
		// uncomment to see what action happened at viewObserver
		/*
		 * System.out.println ("ControllerObserver: The " + e.getActionCommand()
		 * + " button is clicked at " + new java.util.Date(e.getWhen()) +
		 * " with e.paramString " + e.paramString() );
		 */
		System.out.println("ControllerObserver: acting on ModelObserver");
		modelObserver.incrementValue();
	} // actionPerformed()

	// Joe I should be able to add any modelObserver/viewObserver with the
	// correct API
	// but here I can only add ModelObserver/ViewObserver
	public void addModelObserver(ModelObserver m) {
		System.out.println("ControllerObserver: adding modelObserver");
		modelObserver = m;
	} // addModelObserver()

	public void addViewObserver(ViewObserver v) {
		System.out.println("ControllerObserver: adding viewObserver");
		viewObserver = v;
	} // addViewObserver()

	public void initModelObserver(int x) {
		modelObserver.setValue(x);
	} // initModelObserver()

} // ControllerObserver