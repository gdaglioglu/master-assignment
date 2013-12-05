package example.ui;

import javax.swing.SwingUtilities;

import example.ui.controllers.ControllerSimple;
import example.ui.models.ModelSimple;
import example.ui.views.ViewSimple;

public class MainSimple {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ModelSimple model = new ModelSimple(0);
				ViewSimple view = new ViewSimple("-");
				ControllerSimple controller = new ControllerSimple(model, view);
				controller.control();
			}
		});
	}
}