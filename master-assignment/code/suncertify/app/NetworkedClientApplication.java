package suncertify.app;

import suncertify.app.ui.ClientUI;
import suncertify.ui.HotelRoomController;

/**
 * This class is responsible for establishing a networked client, using RMI in
 * this case.
 * 
 * @author Gokhan Daglioglu
 */
public class NetworkedClientApplication implements Application {

	/**
	 * The reference to UI instance which allows the user specify the hostname
	 * of the remote server to connect to.
	 */
	private ClientUI clientUI;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		clientUI = new ClientUI(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch() {
		new HotelRoomController(clientUI.getDataService());
	}
}