package suncertify.app.ui;

import static suncertify.app.NetworkApplication.RMI_SERVER;
import static suncertify.ui.PropertyManager.SERVER_ADDRESS;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.app.Application;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.ui.PropertyManager;

/**
 * This class is responsible for creating, displaying and populating the
 * networked client user interface. This UI's purpose is to allow the user
 * specify the hostname of the remote server to connect to.
 * 
 * @author Gokhan Daglioglu
 */
public class ClientUI extends JFrame {

	private static final long serialVersionUID = 6636073318499699241L;
	private JTextField textField;
	private DataService dataService;
	private JButton ok;
	private Application application;

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays
	 * the JFrame.
	 */
	public ClientUI(Application application) {
		this.application = application;
		this.setTitle("Server Hostname");
		this.setSize(300, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.initUIElements();
		this.setVisible(true);
	}

	/**
	 * This method is responsible for adding the components of the
	 * NetworkedClientUI to the JFrame.
	 */
	private void initUIElements() {
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = new JPanel(layout);

		final JLabel label = new JLabel("Enter server hostname");
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(2, 2, 2, 2);
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(label, c);

		this.textField = new JTextField(
				PropertyManager.getParameter(SERVER_ADDRESS));
		this.textField.addActionListener(new TextFieldListener());
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.textField, c);

		this.ok = new JButton("OK");
		this.ok.setMnemonic(KeyEvent.VK_O);
		this.ok.addActionListener(new OKListener());
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		panel.add(this.ok, c);

		final JButton cancel = new JButton("Cancel");
		cancel.setMnemonic(KeyEvent.VK_C);
		cancel.addActionListener(new CancelListener());
		c.gridx = 2;
		c.gridy = 2;
		panel.add(cancel, c);

		final JLabel blank = new JLabel("");
		c.gridx = 3;
		c.insets = new Insets(2, 25, 2, 25);
		panel.add(blank, c);

		final JLabel leftBlank = new JLabel("");
		c.gridx = 0;
		panel.add(leftBlank, c);

		this.getContentPane().add(panel);
	}

	/**
	 * Getter for the remote DataService which is created when the user clicks
	 * the OK button.
	 * 
	 * @return The remote DataService.
	 */
	public DataService getDataService() {
		return this.dataService;
	}

	/**
	 * A listener that emulates clicking the OK button when the user presses
	 * enter in the hostname text box.
	 * 
	 * @author Gokhan Daglioglu
	 */
	private class TextFieldListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			ClientUI.this.ok.doClick();
		}
	}

	/**
	 * Listener to validate server hostname and to attempt to connect to the
	 * remote server once the user clicks the OK button.
	 * 
	 * @author Sean Dunne
	 */
	private class OKListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent event) {
			final String hostname = ClientUI.this.textField.getText();

			if (hostname.equals("")) {
				App.showError("You must enter a hostname for the server.");
			} else {
				try {
					final Registry registry = LocateRegistry
							.getRegistry(hostname);
					ClientUI.this.dataService = (DataService) registry
							.lookup(RMI_SERVER);
					PropertyManager.setParameter(SERVER_ADDRESS, hostname);
					ClientUI.this.dispose();
					ClientUI.this.application.start();
				} catch (final RemoteException e) {
					App.showError("Cannot connect to the remote server.\nThe hostname may be incorrect or the server could be down.");
				} catch (final NotBoundException e) {
					App.showError("Server found but cannot connect.\nThe server has not started correctly and should be restarted.");
				}
			}
		}
	}

	/**
	 * Listener to exit the application when the user clicks the Cancel button.
	 * 
	 * @author Gokhan Daglioglu
	 */
	private class CancelListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.exit(0);
		}
	}

}