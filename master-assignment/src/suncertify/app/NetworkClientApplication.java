package suncertify.app;

import suncertify.app.ui.ClientUI;
import suncertify.ui.HotelRoomController;

public class NetworkClientApplication implements Application {

	private ClientUI clientUI;

	@Override
	public void launch() {
		clientUI = new ClientUI(this);
	}

	@Override
	public void start() {
		new HotelRoomController(clientUI.getDataService());
	}
}