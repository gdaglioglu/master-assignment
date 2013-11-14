package suncertify.ui.example.views;

import java.awt.*;
import java.awt.event.WindowAdapter; //for CloseListener()
//ViewObserverDraft.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)
//inspired by Joseph Bergin's MVC gui at http://csis.pace.edu/~bergin/mvc/mvcgui.html
//ViewObserverDraft is an Observer
import java.awt.event.WindowEvent; //for CloseListener()
import java.util.Observable; //for update();

import suncertify.ui.example.controllers.ControllerObserver;

public class ViewObserverDraft implements java.util.Observer {

	// attributes as must be visible within class
	private TextField myTextField;
	private Button button;

	// private ModelObserver modelObserver; //Joe: ModelObserver is hardwired
	// in,
	// needed only if viewObserverDraft initialises modelObserver (which we
	// aren't doing)

	public ViewObserverDraft() {
		System.out.println("ViewObserverDraft()");

		// frame in constructor and not an attribute as doesn't need to be
		// visible to whole class
		Frame frame = new Frame("simple MVC");
		frame.add("North", new Label("counter"));

		myTextField = new TextField();
		frame.add("Center", myTextField);

		// panel in constructor and not an attribute as doesn't need to be
		// visible to whole class
		Panel panel = new Panel();
		button = new Button("PressMe");
		panel.add(button);
		frame.add("South", panel);

		frame.addWindowListener(new CloseListener());
		frame.setSize(200, 100);
		frame.setLocation(100, 100);
		frame.setVisible(true);

	} // ViewObserverDraft()

	// Called from the ModelObserver
	public void update(Observable obs, Object obj) {

		// who called us and what did they send?
		// System.out.println ("ViewObserverDraft      : Observable is " +
		// obs.getClass() + ", object passed is " + obj.getClass());

		// modelObserver Pull
		// ignore obj and ask modelObserver for value,
		// to do this, the viewObserverDraft has to know about the modelObserver
		// (which I decided I didn't want to do)
		// uncomment next line to do ModelObserver Pull
		// myTextField.setText("" + modelObserver.getValue());

		// modelObserver Push
		// parse obj
		myTextField.setText("" + ((Integer) obj).intValue()); // obj is an
																// Object, need
																// to cast to an
																// Integer

	} // update()

	// to initialise TextField
	public void setValue(int v) {
		myTextField.setText("" + v);
	} // setValue()

	public void addControllerObserver(ControllerObserver controllerObserver) {
		System.out
				.println("ViewObserverDraft      : adding controllerObserver");
		button.addActionListener(controllerObserver); // need controllerObserver
														// before adding it as a
														// listener
	} // addControllerObserver()

	// uncomment to allow controllerObserver to use viewObserverDraft to
	// initialise modelObserver
	// public void addModelObserver(ModelObserver m){
	// System.out.println("ViewObserverDraft      : adding modelObserver");
	// this.modelObserver = m;
	// } //addModelObserver()

	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		} // windowClosing()
	} // CloseListener

} // ViewObserverDraft