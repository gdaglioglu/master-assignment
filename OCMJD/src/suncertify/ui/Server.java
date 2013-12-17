/*
 * 
 */
package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import suncertify.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public class Server extends JFrame {

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/** The dialog. */
	ConfigDialog dialog;

	/**
	 * Instantiates a new server.
	 */
	public Server() {
		super("URLyBird 1.0 - Server Mode");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(500, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.setVisible(true);

		this.dialog = new ConfigDialog(ApplicationMode.SERVER);
		this.dialog.setVisible(true);
	}

	/**
	 * Start server.
	 */
	public void startServer() {
		try {
			suncertify.remote.ServerRegistry.register();
			this.serverRunning(true);
		} catch (final RemoteException re) {
			this.log.log(Level.SEVERE, re.getMessage(), re);
			System.err
					.println("Remote Exception encountered when starting server "
							+ re.getMessage());
			re.printStackTrace();
		} catch (final MalformedURLException murle) {
			this.log.log(Level.SEVERE, murle.getMessage(), murle);
			System.err.println("Invalid URL used for starting server "
					+ murle.getMessage());
			murle.printStackTrace();
		}
	}

	/**
	 * Server running.
	 *
	 * @param b the b
	 */
	public void serverRunning(final boolean b) {
		if (b) {
			final PropertyFileManager properties = PropertyFileManager
					.getInstance();

			final JLabel dbLabel = new JLabel(
					"Location: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
			final JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			dbPanel.add(dbLabel);

			final JLabel hostLabel = new JLabel(
					"Server Address: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST));
			final JPanel hostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			hostPanel.add(hostLabel);

			final JLabel portLabel = new JLabel(
					"Server Port: "
							+ properties
									.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));
			final JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			portPanel.add(portLabel);

			final JPanel serverPanel = new JPanel(new BorderLayout());
			serverPanel.add(dbPanel, BorderLayout.NORTH);
			serverPanel.add(hostPanel, BorderLayout.WEST);
			serverPanel.add(portPanel);
			this.add(serverPanel, BorderLayout.NORTH);

			final JButton status = new JButton("Server Running ...");
			status.setEnabled(false);
			final JButton action = new JButton("Exit and Stop Server!");
			action.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					Server.this.exitServer();
				}
			});
			final JPanel controlPanel = new JPanel(new FlowLayout(
					FlowLayout.CENTER));
			controlPanel.add(status);
			controlPanel.add(action);
			this.add(controlPanel, BorderLayout.SOUTH);

			this.setVisible(true);
		}
	}

	/**
	 * Exit server.
	 */
	private void exitServer() {
		System.exit(0);
	}
}
