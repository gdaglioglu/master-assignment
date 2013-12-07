package example.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observer;


public class ObserverObservableController {
	private final static ObserverObservableModel model = new ObserverObservableModel();

	public static void main(String... args) {
		ObserverObservableMainGui view = new ObserverObservableMainGui(new ObserverObservableController());
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
