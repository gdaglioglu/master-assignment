package suncertify.ui;

import javax.swing.SwingUtilities;

public class Runner {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				Controller controller = new Controller();
				controller.control();
			}
		});
	}
}
