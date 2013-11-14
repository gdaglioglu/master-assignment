package suncertify.ui;

import javax.swing.SwingUtilities;

public class Runner {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Model model = new Model();
				View view = new View(model);
				Controller controller = new Controller(model, view);
				controller.control();
			}
		});
	}
}
