package suncertify.ui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchPanel extends JPanel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = -1126709180970637756L;

	/**
	 * The text field that contains the user defined name String.
	 */
	private JTextField nameSearchField = new JTextField(15);

	/**
	 * The text field that contains the user defined location String.
	 */
	private JTextField locationSearchField = new JTextField(15);
	/**
	 * The text field that contains the user defined size String.
	 */
	private JTextField sizeSearchField = new JTextField(15);
	/**
	 * The text field that contains the user defined smoking String.
	 */
	private JTextField smokingSearchField = new JTextField(15);
	/**
	 * The text field that contains the user defined rate String.
	 */
	private JTextField rateSearchField = new JTextField(15);
	/**
	 * The text field that contains the user defined date String.
	 */
	private JTextField dateSearchField = new JTextField(15);
	/**
	 * The text field that contains the user defined owner String.
	 */
	private JTextField ownerSearchField = new JTextField(15);

	JButton searchButton;

	private JCheckBox exactMatch;

	public SearchPanel() {

		this.setBorder(BorderFactory.createTitledBorder("Search"));
		this.setLayout(new GridLayout(2, 8, 10, 5));

		searchButton = new JButton("Search");
		searchButton.setMnemonic(KeyEvent.VK_S);
		// load saved configuration
		final boolean state = Boolean.parseBoolean(PropertyManager
				.getParameter(PropertyManager.EXACT_MATCH));
		this.exactMatch = new JCheckBox("Exact match", state);
		this.exactMatch.setMnemonic(KeyEvent.VK_E);

		final JLabel nameLabel = new JLabel("Hotel name:");
		final JLabel locationLabel = new JLabel("Hotel location:");
		final JLabel sizeLabel = new JLabel("Room size:");
		final JLabel smokingLabel = new JLabel("Smoking state:");
		final JLabel rateLabel = new JLabel("Room rate:");
		final JLabel dateLabel = new JLabel("Date:");
		final JLabel ownerLabel = new JLabel("Owner:");

		this.add(nameLabel);
		this.add(locationLabel);
		this.add(sizeLabel);
		this.add(smokingLabel);
		this.add(rateLabel);
		this.add(dateLabel);
		this.add(ownerLabel);

		this.add(searchButton);

		this.add(nameSearchField);
		this.add(locationSearchField);
		this.add(sizeSearchField);
		this.add(smokingSearchField);
		this.add(rateSearchField);
		this.add(dateSearchField);
		this.add(ownerSearchField);

		this.add(exactMatch);

		nameSearchField.setToolTipText("Enter hotel name to search.");
		locationSearchField.setToolTipText("Enter hotel location to search.");
		sizeSearchField.setToolTipText("Enter room size to search.");
		smokingSearchField.setToolTipText("Enter smoking state to search.");
		rateSearchField.setToolTipText("Enter room rate to search.");
		dateSearchField.setToolTipText("Enter date to search.");
		ownerSearchField.setToolTipText("Enter owner name to search.");
		searchButton.setToolTipText("Submit the Hotel room search.");
		exactMatch
				.setToolTipText("Tick the box to search with exact match option.");

	}

	public String[] getSearchCriteria() {
		String[] criteria = new String[7];
		criteria[0] = nameSearchField.getText().trim();
		criteria[1] = locationSearchField.getText().trim();
		criteria[2] = sizeSearchField.getText().trim();
		criteria[3] = smokingSearchField.getText().trim();
		criteria[4] = rateSearchField.getText().trim();
		criteria[5] = dateSearchField.getText().trim();
		criteria[6] = ownerSearchField.getText().trim();
		return criteria;
	}

	public JButton getSearchButton() {
		return this.searchButton;
	}

	public JCheckBox getExactMatch() {
		return this.exactMatch;
	}

	public String isExactMatchSelected() {
		return String.valueOf(this.exactMatch.isSelected());
	}
}
