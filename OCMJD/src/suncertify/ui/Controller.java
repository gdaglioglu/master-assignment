package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import suncertify.constants.EnvironmentConstants;
import suncertify.db.*;

public class Controller {

	private DBAccess database;

	private Model model;
	private View view;
	private ActionListener searchListener, bookingListener;
	private String lastSearch;

	public Controller() {
		try {
			database = new Data("C:\\Users\\" + EnvironmentConstants.USERNAME
					+ "\\git\\OCMJD\\db-test.db");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model = getRecords("");
		view = new View(model);
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
		return view.getSelectedRowRecNo();
	}

	protected String getCustomerID() {
		return view.getCustomerID();
	}

	private void bookRoom(long recNo, String owner) {
		try {
			long lock = database.lockRecord(recNo);
			String[] record = database.readRecord(recNo);
			record[7] = owner;
			database.updateRecord(recNo, record, lock);
			database.unlock(recNo, lock);
		} catch (RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Model getRecords(String searchText) {
		model = new Model();
		long[] recNoArray = null;

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
				model.addRecord(database.readRecord(recNo));
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return model;
	}
}
