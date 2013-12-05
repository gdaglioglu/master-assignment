package example.ui;

import example.ui.controllers.ControllerObserver;
import example.ui.models.ModelObserver;
import example.ui.views.ViewObserver;

//RunnerObserver.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

public class RunnerObserver {

	// The order of instantiating the objects below will be important for some
	// pairs of commands.
	// I haven't explored this in any detail, beyond that the order below works.

	private int start_value = 10; // initialise modelObserver, which in turn
									// initialises viewObserver

	public RunnerObserver() {

		// create ModelObserver and ViewObserver
		ModelObserver myModelObserver = new ModelObserver();
		ViewObserver myViewObserver = new ViewObserver();

		// tell ModelObserver about ViewObserver.
		myModelObserver.addObserver(myViewObserver);
		/*
		 * init modelObserver after viewObserver is instantiated and can show
		 * the status of the modelObserver (I later decided that only the
		 * controllerObserver should talk to the modelObserver and moved
		 * initialisation to the controllerObserver (see below).)
		 */
		// uncomment to directly initialise ModelObserver
		// myModelObserver.setValue(start_value);

		// create ControllerObserver. tell it about ModelObserver and
		// ViewObserver, initialise modelObserver
		ControllerObserver myControllerObserver = new ControllerObserver();
		myControllerObserver.addModelObserver(myModelObserver);
		myControllerObserver.addViewObserver(myViewObserver);
		myControllerObserver.initModelObserver(start_value);

		// tell ViewObserver about ControllerObserver
		myViewObserver.addControllerObserver(myControllerObserver);
		// and ModelObserver,
		// this was only needed when the viewObserver inits the modelObserver
		// myViewObserver.addModelObserver(myModelObserver);

	} // RunnerObserver()

} // RunnerObserver