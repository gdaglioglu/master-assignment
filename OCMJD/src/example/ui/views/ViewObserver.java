package example.ui.views;

//for CloseListener()	//for CloseListener()
import java.awt.*;
//ViewObserver.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)
//inspired by Joseph Bergin's MVC gui at http://csis.pace.edu/~bergin/mvc/mvcgui.html
//ViewObserver is an Observer
import java.awt.event.*; //for addControllerObserver()
import java.util.Observable; //for update();

public class ViewObserver implements java.util.Observer {

	// attributes as must be visible within class
	private TextField myTextField;
	private Button button;

	// private ModelObserver modelObserver; //Joe: ModelObserver is hardwired
	// in,
	// needed only if viewObserver initialises modelObserver (which we aren't
	// doing)

	public ViewObserver() {
		System.out.println("ViewObserver()");

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

	} // ViewObserver()

	// Called from the ModelObserver
	public void update(Observable obs, Object obj) {

		// who called us and what did they send?
		// System.out.println ("ViewObserver      : Observable is " +
		// obs.getClass() + ", object passed is " + obj.getClass());

		// modelObserver Pull
		// ignore obj and ask modelObserver for value,
		// to do this, the viewObserver has to know about the modelObserver
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

	public void addControllerObserver(ActionListener controllerObserver) {
		System.out.println("ViewObserver      : adding controllerObserver");
		button.addActionListener(controllerObserver); // need instance of
														// controllerObserver
														// before can add it as
														// a listener
	} // addControllerObserver()

	// uncomment to allow controllerObserver to use viewObserver to initialise
	// modelObserver
	// public void addModelObserver(ModelObserver m){
	// System.out.println("ViewObserver      : adding modelObserver");
	// this.modelObserver = m;
	// } //addModelObserver()

	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		} // windowClosing()
	} // CloseListener

} // ViewObserver