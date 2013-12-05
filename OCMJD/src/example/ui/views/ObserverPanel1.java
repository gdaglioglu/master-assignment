package example.ui.views;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import example.ui.controllers.ObserverController;

public class ObserverPanel1 extends JPanel implements Observer {
	private static final long serialVersionUID = -545814952082212811L;
	private ObserverController controller = null;

	public ObserverPanel1(ObserverController controller) {
		this.controller = controller;
		this.controller.register(this);
		setPreferredSize(new Dimension(287, 315));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(new JLabel("Panel 1"));
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
				int angle = (360 * (percent.intValue() > 100 ? 100 : percent
						.intValue())) / 100;
				g2d.drawArc(60, 60, 150, 150, 0, 360);
				g2d.fillArc(60, 60, 150, 150, 0, angle);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
