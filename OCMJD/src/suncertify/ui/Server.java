package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.swing.*;

import suncertify.util.*;

public class Server extends JFrame {

	// TODO serialVersionUID
	private static final long serialVersionUID = -7816958458327680485L;

	ConfigDialog dialog;

	public Server() {
		super("URLyBird 1.0 - Server Mode");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(500, 130);
		setResizable(false);
		setLocationRelativeTo(null);

		setVisible(true);

		dialog = new ConfigDialog(ApplicationMode.SERVER);
		dialog.setVisible(true);
	}

	public void startServer() {
		try {
			suncertify.remote.ServerRegistry.register();
			serverRunning(true);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serverRunning(boolean b) {
		if (b) {
			PropertyFileManager properties = PropertyFileManager.getInstance();

			JLabel dbLabel = new JLabel(
					"Location: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
			JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			dbPanel.add(dbLabel);

			JLabel hostLabel = new JLabel(
					"Server Address: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST));
			JPanel hostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			hostPanel.add(hostLabel);

			JLabel portLabel = new JLabel(
					"Server Port: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));
			JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			portPanel.add(portLabel);

			JPanel serverPanel = new JPanel(new BorderLayout());
			serverPanel.add(dbPanel, BorderLayout.NORTH);
			serverPanel.add(hostPanel, BorderLayout.WEST);
			serverPanel.add(portPanel);
			this.add(serverPanel, BorderLayout.NORTH);

			JButton status = new JButton("Server Running ...");
			status.setEnabled(false);
			JButton action = new JButton("Exit and Stop Server!");
			action.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exitServer();
				}
			});
			JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			controlPanel.add(status);
			controlPanel.add(action);
			this.add(controlPanel, BorderLayout.SOUTH);

			setVisible(true);
		}
	}

	private void exitServer() {
		// TODO Clean up before exiting
		System.exit(0);
	}
}
