package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import suncertify.db.*;

public class Controller {

	private DBAccess database;

	private Model model;
	private View view;
	private ActionListener actionListener;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void control() {
		try {
			database = new Data("C:\\Users\\" + "eeoimoo"
					+ "\\git\\OCMJD\\Instructions\\db-1x3.db");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				linkBtnAndTable();
			}
		};
		view.getButton().addActionListener(actionListener);
	}

	private void linkBtnAndTable() {
		view.updateTable(getRecords());
	}

	public Model getRecords() {
		Model test = new Model();
		long[] recordIdArray = null;

		recordIdArray = database.findByCriteria(null);

		for (long recordID : recordIdArray) {

			try {
				test.addRecord(database.readRecord(recordID));
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return test;
	}
}
