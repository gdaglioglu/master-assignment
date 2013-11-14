package suncertify.ui.example;

import javax.swing.SwingUtilities;

import suncertify.ui.example.controllers.ControllerSimple;
import suncertify.ui.example.models.ModelSimple;
import suncertify.ui.example.views.ViewSimple;

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