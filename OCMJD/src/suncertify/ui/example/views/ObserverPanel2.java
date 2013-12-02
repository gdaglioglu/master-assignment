package suncertify.ui.example.views;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import suncertify.ui.example.controllers.ObserverController;

public class ObserverPanel2 extends JPanel implements Observer {
	private static final long serialVersionUID = -5101339087937915902L;
	private ObserverController controller = null;

	public ObserverPanel2(ObserverController controller) {
		this.controller = controller;
		this.controller.register(this);
		setPreferredSize(new Dimension(287, 315));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(new JLabel("Panel 2"));
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			Object data = controller.readData();
			if ((data != null) && (data instanceof Integer)) {
				Integer percent = (Integer) data;
				Graphics2D g2d = (Graphics2D) g;
				int height = (200 * (percent.intValue() > 100 ? 100 : percent
						.intValue())) / 100;
				g2d.drawRect(60, 60, 150, 200);
				g2d.fillRect(60, 60, 150, height);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
