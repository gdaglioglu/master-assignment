package example.ui;

import javax.swing.SwingUtilities;


public class SimpleMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SimpleModel model = new SimpleModel(0);
				SimpleView view = new SimpleView("-");
				SimpleController controller = new SimpleController(model, view);
				controller.control();
			}
		});
	}
}