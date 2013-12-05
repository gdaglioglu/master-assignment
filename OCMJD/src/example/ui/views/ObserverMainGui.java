package example.ui.views;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import example.ui.controllers.ObserverController;

public class ObserverMainGui extends JFrame {
	private static final long serialVersionUID = -6640422987288265832L;

	public ObserverMainGui(ObserverController controller) {
		setTitle("Main GUI");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int sw = screenSize.width, sh = screenSize.height;
		int w = 600, h = 400;
		setSize(new Dimension(w, h));
		setLocation((sw - w) / 2, (sh - h) / 2);
		setResizable(false);

		JPanel panel = new JPanel();
		panel.add(new ObserverPanel0(controller));
		panel.add(new ObserverPanel1(controller));
		panel.add(new ObserverPanel2(controller));
		add(panel);
	}
}
