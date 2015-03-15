package suncertify.app;

import suncertify.app.ui.ClientUI;
import suncertify.ui.HotelRoomController;

/**
 * This class is responsible for establishing a networked client, using RMI in
 * this case.
 * 
 * @author Gokhan Daglioglu
 */
public class NetworkClientApplication implements Application {

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