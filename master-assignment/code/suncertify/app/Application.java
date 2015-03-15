package suncertify.app;

/**
 * Interface that defines the contract for three application modes.
 * 
 * @author Gokhan Daglioglu
 */
public interface Application {

	/**
	 * This method is the initial method that gets called when an application is
	 * created. This method is responsible for ensuring all prerequisites to the
	 * starting of the application are correctly handled.
	 */
	void init();

	/**
	 * This method gets called when the application prerequisites have been
	 * satisfied and the user is ready to start the applications main
	 * functionality.
	 */
	void launch();
}