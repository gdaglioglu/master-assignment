package example.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimpleController {

	private SimpleModel model;
	private SimpleView view;
	private ActionListener actionListener;

	public SimpleController(SimpleModel model, SimpleView view) {
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