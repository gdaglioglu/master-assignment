package suncertify.app;

import suncertify.app.ui.ServerUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.ui.HotelRoomController;

public class StandAloneApplication implements Application {

	ServerUI serverUI;

	@Override
	public void launch() {
		this.serverUI = new ServerUI(this);

	}

	@Override
	public void start() {
		this.serverUI.dispose();
		final DataService dataService = new DataServiceImpl();
		new HotelRoomController(dataService);
	}

}
