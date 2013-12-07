package example.ui;

import java.io.*;
import java.util.Observable;

public class ObserverObservableModel extends Observable {

	public Object readData() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		Object data = null;
		File file = new File("data.ser");
		if (file.exists() && file.isFile() && file.canRead()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					file));
			data = ois.readObject();
			ois.close();
		}
		return data;
	}

	public void writeData(Object data) throws FileNotFoundException,
			IOException {
		if (data != null) {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("data.ser"));
			oos.writeObject(data);
			oos.close();
			setChanged();
			notifyObservers();
		}
	}
}
