package example.ui;

//ModelObserver.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

//inspired by Joseph Bergin's MVC gui at http://csis.pace.edu/~bergin/mvc/mvcgui.html

//ModelObserver holds an int counter (that's all it is).
//ModelObserver is an Observable
//ModelObserver doesn't know about ViewObserver or ControllerObserver

public class ComplexModel extends java.util.Observable {

	private int counter; // primitive, automatically initialised to 0

	public ComplexModel() {

		System.out.println("ModelObserver()");

		/**
		 * Problem initialising both modelObserver and viewObserver:
		 * 
		 * On a car you set the speedometer (viewObserver) to 0 when the car
		 * (modelObserver) is stationary. In some circles, this is called
		 * calibrating the readout instrument. In this MVC example, you would
		 * need two separate pieces of initialisation code, in the modelObserver
		 * and in the viewObserver. If you changed the initialisation value in
		 * one you'd have to remember (or know) to change the initialisation
		 * value in the other. A recipe for disaster.
		 * 
		 * Alternately, when instantiating modelObserver, you could run
		 * 
		 * setValue(0);
		 * 
		 * as part of the constructor, sending a message to the viewObserver.
		 * This requires the viewObserver to be instantiated before the
		 * modelObserver, otherwise the message will be send to null (the
		 * unitialised value for viewObserver). This isn't a particularly
		 * onerous requirement, and is possibly a reasonable approach.
		 * 
		 * Alternately, have RunnerObserver tell the viewObserver to intialise
		 * the modelObserver. The requires the viewObserver to have a reference
		 * to the modelObserver. This seemed an unneccesary complication.
		 * 
		 * I decided instead in RunnerObserver, to instantiate modelObserver,
		 * viewObserver and controllerObserver, make all the connections, then
		 * since the ControllerObserver already has a reference to the
		 * modelObserver (which it uses to alter the status of the
		 * modelObserver), to initialise the modelObserver from the
		 * controllerObserver and have the modelObserver automatically update
		 * the viewObserver.
		 */

	} // ModelObserver()

	// uncomment this if ViewObserver is using ModelObserver Pull to get the
	// counter
	// not needed if getting counter from notifyObservers()
	// public int getValue(){return counter;}

	// notifyObservers()
	// modelObserver sends notification to viewObserver because of
	// RunnerObserver: myModelObserver.addObserver(myViewObserver)
	// myViewObserver then runs update()
	//
	// modelObserver Push - send counter as part of the message
	public void setValue(int value) {

		counter = value;
		System.out.println("ModelObserver init: counter = " + counter);
		setChanged();
		// modelObserver Push - send counter as part of the message
		notifyObservers(counter);
		// if using ModelObserver Pull, then can use notifyObservers()
		// notifyObservers()

	} // setValue()

	public void incrementValue() {

		++counter;
		System.out.println("ModelObserver     : counter = " + counter);
		setChanged();
		// modelObserver Push - send counter as part of the message
		notifyObservers(counter);
		// if using ModelObserver Pull, then can use notifyObservers()
		// notifyObservers()

	} // incrementValue()

} // ModelObserver