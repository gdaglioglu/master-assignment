package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import suncertify.db.DBAccess;
import suncertify.db.RecordNotFoundException;
import suncertify.remote.RemoteDBAccess;
import suncertify.util.*;

public class Controller {

	private ApplicationMode applicationMode;

	private DBAccess database;
	private RemoteDBAccess remoteDatabase;

	private Model model;
	private View view;
	private ActionListener searchListener, bookingListener;
	private String lastSearch;

	private ConfigDialog dialog;
	private PropertyFileManager properties = PropertyFileManager.getInstance();

	public Controller(ApplicationMode applicationMode) {
		this.applicationMode = applicationMode;

		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			view = new View("URLyBird 1.0 - Standalone Mode");
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			view = new View("URLyBird 1.0 - Network Mode");
		} else {
			// TODO Massive Problems
		}

		dialog = new ConfigDialog(applicationMode);
		dialog.setVisible(true);

		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			try {
				database = DataConnector
						.getLocal(properties
								.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			try {
				remoteDatabase = DataConnector
						.getRemote(
								properties
										.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST),
								properties
										.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// Massive Problems
		}

		model = getRecords("");
		view.startClientView(model);
	}

	public void control() {
		searchListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				lastSearch = getSearchText();
				view.updateTable(getRecords(lastSearch));
			}
		};
		bookingListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				long selectedRecNo = getReserveRecNo();

				if (selectedRecNo != -1) {
					String owner = getCustomerID();

					if (owner != null) {
						bookRoom(selectedRecNo, owner);
						view.updateTable(getRecords(lastSearch));
					}
				}
			}
		};

		view.getSearchButton().addActionListener(searchListener);
		view.getBookingButton().addActionListener(bookingListener);
	}

	private String getSearchText() {
		return view.getSearchField();
	}

	private long getReserveRecNo() {
		int selectedRow = view.getSelectedRowNo();
		return model.getRecNo(selectedRow - 1);
	}

	protected String getCustomerID() {
		return view.getCustomerID();
	}

	private void bookRoom(long recNo, String owner) {
		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			try {
				long lock = database.lockRecord(recNo);
				String[] record = database.readRecord(recNo);
				record[6] = owner;
				database.updateRecord(recNo, record, lock);
				database.unlock(recNo, lock);
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			try {
				long lock = remoteDatabase.lockRecord(recNo);
				String[] record = remoteDatabase.readRecord(recNo);
				record[6] = owner;
				remoteDatabase.updateRecord(recNo, record, lock);
				remoteDatabase.unlock(recNo, lock);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// TODO Massive Problems
		}
	}

	private Model getRecords(String searchText) {
		model = new Model();
		long[] recNoArray = null;

		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			if ((searchText == null) || searchText.equals("")) {
				recNoArray = database.findByCriteria(null);
			} else {
				String[] search = new String[model.getColumnCount()];
				search[model.findColumn("Name")] = searchText;
				search[model.findColumn("Location")] = searchText;
				recNoArray = database.findByCriteria(search);
			}

			for (long recNo : recNoArray) {
				try {
					model.addRecord(database.readRecord(recNo), recNo);
				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			if ((searchText == null) || searchText.equals("")) {
				try {
					recNoArray = remoteDatabase.findByCriteria(null);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				String[] search = new String[model.getColumnCount()];
				search[model.findColumn("Name")] = searchText;
				search[model.findColumn("Location")] = searchText;
				try {
					recNoArray = remoteDatabase.findByCriteria(search);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (long recNo : recNoArray) {
				try {
					model.addRecord(remoteDatabase.readRecord(recNo), recNo);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			// TODO Massive Problems
		}

		return model;
	}
}
