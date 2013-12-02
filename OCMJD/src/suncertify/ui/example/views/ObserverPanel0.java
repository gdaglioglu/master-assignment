package suncertify.ui.example.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import suncertify.ui.example.controllers.ObserverController;

public class ObserverPanel0 extends JPanel {
	private static final long serialVersionUID = -545814952082212811L;
	private ObserverController controller = null;
	private JTextField field = null;

	public ObserverPanel0(ObserverController controller) {
		this.controller = controller;
		JLabel label = new JLabel("Input % ");
		add(label);
		field = new JTextField();
		field.setPreferredSize(new Dimension(450, 30));
		try {
			Object data = controller.readData();
			if (data != null) {
				field.setText(data.toString());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		add(field);
		JButton button = new JButton("save");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ObserverPanel0.this.controller.writeData(Integer.parseInt(field
							.getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		add(button);
		setPreferredSize(new Dimension(580, 40));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
}
