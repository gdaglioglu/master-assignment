package example.ui.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observer;

import example.ui.models.ObserverModel;
import example.ui.views.ObserverMainGui;

public class ObserverController {
	private final static ObserverModel model = new ObserverModel();

	public static void main(String... args) {
		ObserverMainGui view = new ObserverMainGui(new ObserverController());
		view.setVisible(true);
	}

	public Object readData() throws FileNotFoundException,
			ClassNotFoundException, IOException {
		return model.readData();
	}

	public void writeData(Object data) throws FileNotFoundException,
			IOException {
		model.writeData(data);
	}

	public void register(Observer observer) {
		if (observer != null) {
			model.addObserver(observer);
		}
	}
}
