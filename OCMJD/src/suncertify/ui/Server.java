package suncertify.ui;

import javax.swing.JFrame;

import suncertify.util.ApplicationMode;

public class Server extends JFrame {

	ConfigDialog dialog;

	public Server() {
		super("URLyBird 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);
		setLocationRelativeTo(null);

		dialog = new ConfigDialog(ApplicationMode.SERVER);
		dialog.setVisible(true);
	}

	public void startServer() {

		dialog.serverRunning(true);
	}
}
