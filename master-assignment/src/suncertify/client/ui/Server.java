package suncertify.client.ui;

import static suncertify.client.ui.PropertyManager.DATABASE_LOCATION;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.app.App;
import suncertify.server.DatabaseLocator;

/**
 * This class is responsible for creating, displaying and populating the Server
 * user interface.
 * 
 * @author Gokhan Daglioglu
 */
public abstract class Server extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;
	private JTextField dbFileLocTxt;
	private JButton browseBtn;
	private AbstractButton shutdownBtn;
	private JButton startBtn;

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays
	 * the JFrame.
	 */
	Server() {
		this.setTitle("URlybird Server application");
		this.setSize(600, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().add(initUIElements());
		this.setVisible(true);
	}

	/**
	 * This method is responsible for adding the components of the ServerUI to
	 * the JFrame.
	 */
	private JPanel initUIElements() {

		JPanel jPanel = new JPanel();

		final BoxLayout layout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
		jPanel.setLayout(layout);

		createDBLocationPanel(jPanel);
		createServerButtons(jPanel);

		return jPanel;

	}

	/**
	 * This method will create the ui to display and change the database
	 * location.
	 * 
	 * @param jPanel
	 */
	private void createDBLocationPanel(JPanel jPanel) {
		final JPanel middle = new JPanel();
		final GridBagLayout middleLayout = new GridBagLayout();
		middle.setLayout(middleLayout);

		final JLabel dbFileLocLbl = new JLabel("Database file:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(dbFileLocLbl, c);

		this.dbFileLocTxt = new JTextField(
				PropertyManager.getParameter(DATABASE_LOCATION));
		this.dbFileLocTxt.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = new Insets(5, 0, 5, 0);
		middle.add(this.dbFileLocTxt, c);

		this.browseBtn = new JButton("Locate");
		this.browseBtn.setMnemonic(KeyEvent.VK_L);
		this.browseBtn.addActionListener(new BrowseListener());
		c = new GridBagConstraints();
		c.gridx = 2;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(this.browseBtn, c);
		jPanel.add(middle);
	}

	/**
	 * This method creates a servers buttons, start and stop.
	 */
	private void createServerButtons(JPanel jPanel) {
		final JPanel bottom = new JPanel();
		final FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);

		this.startBtn = new JButton("Start");
		this.startBtn.setMnemonic(KeyEvent.VK_S);
		this.startBtn.addActionListener(new StartListener());
		bottom.add(this.startBtn);

		this.shutdownBtn = new JButton("Shutdown");
		this.shutdownBtn.setMnemonic(KeyEvent.VK_D);
		this.shutdownBtn.addActionListener(new ShutdownListener());
		bottom.add(this.shutdownBtn);
		jPanel.add(bottom);
	}

	/**
	 * This listener handles how to locate a new database file.
	 * 
	 * @author Gokhan Daglioglu
	 */
	private class BrowseListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent arg0) {
			final String location = DatabaseLocator.getLocation();
			if (location != null) {
				Server.this.dbFileLocTxt.setText(location);
			}
		}
	}

	/**
	 * This listener handles how to start the server.
	 * 
	 * @author Gokhan Daglioglu
	 */
	private class StartListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			if (Server.this.dbFileLocTxt.getText().equals("")) {
				App.showError("You must choose a database file before the server can start.");
			} else {
				Server.this.start();

				Server.this.startBtn.setEnabled(false);
				Server.this.browseBtn.setEnabled(false);
			}
		}
	}

	/**
	 * This listener will shutdown the application.
	 * 
	 * @author Gokhan Daglioglu
	 */
	private class ShutdownListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.exit(0);
		}
	}

	abstract void start();
}