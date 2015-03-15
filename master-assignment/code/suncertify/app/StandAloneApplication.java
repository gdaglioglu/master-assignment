package suncertify.app;

import suncertify.app.ui.ServerUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.ui.HotelRoomController;

/**
 * This class is responsible for establishing a stand alone client, without
 * using a network connection.
 * 
 * @author Gokhan Daglioglu
 */
public class StandAloneApplication implements Application {

	private ServerUI serverUI;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		serverUI = new ServerUI(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch() {
		serverUI.dispose();
		final DataService dataService = new DataServiceImpl();
		new HotelRoomController(dataService);
	}
}