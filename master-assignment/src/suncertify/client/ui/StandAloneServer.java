package suncertify.client.ui;

import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;

public class StandAloneServer extends Server {

	private static final long serialVersionUID = 5882703936007260570L;

	@Override
	public void start() {
		this.dispose();
		final DataService dataService = new DataServiceImpl();
		new HotelRoomController(dataService);
	}

}
