package suncertify.app.ui;

import static suncertify.app.util.App.showError;
import static suncertify.app.util.PropertyManager.DATABASE_LOCATION;
import static suncertify.app.util.PropertyManager.getParameter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.app.Application;

/**
 * This class is responsible for creating, displaying and populating the
 * networked server user interface.
 * 
 * @author Gokhan Daglioglu
 */
public class ServerUI extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.app.ui</code>.
	 */
	private Logger logger = Logger.getLogger(ServerUI.class.getPackage().getName());

	/**
	 * The <code>JTextField</code> which is used to enter a database file
	 * location.
	 */
	private JTextField dbFileLocationText;

	/**
	 * The <code>JButton</code> that starts <code>JFileChooser</code> for user
	 * to select a .db file.
	 */
	private JButton browseButton;

	/**
	 * The <code>JButton</code> that closes the <code>ServerUI</code>.
	 */
	private JButton shutdownButton;

	/**
	 * The <code>JButton</code> that starts the networked server when pressed.
	 */
	private JButton startButton;

	/**
	 * The reference to a {@link Application} instance.
	 */
	private Application application;

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays
	 * the JFrame.
	 * 
	 * @param application
	 *            The application mode to be started.
	 */
	public ServerUI(Application application) {
		this.application = application;
		this.setTitle("URlybird Server application");
		this.setSize(600, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(initUIElements());
		this.setVisible(true);
		logger.log(Level.FINE, "Initialized Server UI");
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
	 *            The panel that database location panel will be added to.
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
		this.dbFileLocationText = new JTextField(getParameter(DATABASE_LOCATION));
		this.dbFileLocationText.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = new Insets(5, 0, 5, 0);
		middle.add(this.dbFileLocationText, c);
		this.browseButton = new JButton("Locate");
		this.browseButton.setMnemonic(KeyEvent.VK_L);
		this.browseButton.addActionListener(new BrowseListener());
		c = new GridBagConstraints();
		c.gridx = 2;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(this.browseButton, c);
		jPanel.add(middle);
	}

	/**
	 * This method creates a servers buttons, start and stop.
	 * 
	 * @param jPanel
	 *            The panel that the server buttons will be added to.
	 */
	private void createServerButtons(JPanel jPanel) {
		final JPanel bottom = new JPanel();
		final FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);
		this.startButton = new JButton("Start");
		this.startButton.setMnemonic(KeyEvent.VK_S);
		this.startButton.addActionListener(new StartListener());
		bottom.add(this.startButton);
		this.shutdownButton = new JButton("Shutdown");
		this.shutdownButton.setMnemonic(KeyEvent.VK_D);
		this.shutdownButton.addActionListener(new ShutdownListener());
		bottom.add(this.shutdownButton);
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
				ServerUI.this.dbFileLocationText.setText(location);
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
			if (ServerUI.this.dbFileLocationText.getText().equals("")) {
				showError("You must choose a database file before the server can start.");
			} else {
				ServerUI.this.application.launch();

				ServerUI.this.startButton.setEnabled(false);
				ServerUI.this.browseButton.setEnabled(false);
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
}