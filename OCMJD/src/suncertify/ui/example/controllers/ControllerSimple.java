package suncertify.ui.example.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import suncertify.ui.example.models.ModelSimple;
import suncertify.ui.example.views.ViewSimple;

public class ControllerSimple {

	private ModelSimple model;
	private ViewSimple view;
	private ActionListener actionListener;

	public ControllerSimple(ModelSimple model, ViewSimple view) {
		this.model = model;
		this.view = view;

	}

	public void control() {
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				linkBtnAndLabel();
			}
		};
		view.getButton().addActionListener(actionListener);
	}

	private void linkBtnAndLabel() {
		model.incX();
		view.setText(Integer.toString(model.getX()));
	}
}