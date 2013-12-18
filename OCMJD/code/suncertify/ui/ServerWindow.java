/*
 * Server
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
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

/**
 * Since the server requires no model, an MVC pattern is not suitable. But this
 * <code>ServerWindow</code> class is similar to a combined View and Controller
 * for the server application. After the user selects values for the config
 * parameters, this class registers the
 * <code>RemoteServerFactory<code> on the RMI registry.
 * 
 * @author Eoin Mooney
 */
public class ServerWindow extends JFrame {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = 8912635373524728136L;

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.ui</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/**
	 * The <code>ConfigDialog</code> used to store users configuration
	 * parameters in the <code>PropertyManager</code>.
	 */
	ConfigDialog dialog;

	/**
	 * Instantiates a new server.
	 */
	public ServerWindow() {
		super("URLyBird 1.0 - Server Mode");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(500, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.setVisible(true);

		// Pop-up the ConfigDialog
		this.dialog = new ConfigDialog(ApplicationMode.SERVER);
		this.dialog.setVisible(true);
	}

	/**
	 * This method is called by the <code>Runner</code> after instantiating a
	 * <code>ServerWindow</code>. It creates a <code>RemoteServerRegistry</code>
	 * which will make a
	 * <code>RemoteServerFactory<code> instance available remotely.
	 */
	public void startServer() {
		try {
			suncertify.remote.RemoteServerRegistry.register();
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
	 * This method creates a new window and populates with the network and
	 * datafile parameters from the properties file. It also provides a button
	 * that stops the server and quits the application
	 * 
	 * @param b
	 *            if true, create the window.
	 */
	public void serverRunning(final boolean b) {
		if (b) {
			final PropertyManager properties = PropertyManager.getInstance();

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
					ServerWindow.this.exitServer();
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
	 * This method is called by the <code>ServerWindow</code> when the
	 * "Exit and Stop Server!" button is pressed
	 */
	private void exitServer() {
		System.exit(0);
	}
}
