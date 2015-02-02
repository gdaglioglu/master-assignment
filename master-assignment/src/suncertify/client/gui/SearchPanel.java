package suncertify.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import suncertify.app.ApplicationRunner;


public class SearchPanel extends JPanel {

	/**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
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


	private GuiController controller;


	private JTable mainTable;

	public SearchPanel(JTable mainTable, GuiController controller) {
		
		this.mainTable = mainTable;
		this.controller = controller;
        // Set up the search pane
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchHotelRoom());
        searchButton.setMnemonic(KeyEvent.VK_S);
        // Search panel
        this.setLayout(new GridLayout(2, 8, 10, 5));
        this.setBorder(BorderFactory.createTitledBorder("Search"));
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
        
        nameSearchField.setToolTipText(
                "Enter hotel name to search.");
        locationSearchField.setToolTipText(
            "Enter hotel location to search.");
        sizeSearchField.setToolTipText(
            "Enter room size to search.");
        smokingSearchField.setToolTipText(
            "Enter smoking state to search.");
        rateSearchField.setToolTipText(
            "Enter room rate to search.");
        dateSearchField.setToolTipText(
            "Enter date to search.");
        ownerSearchField.setToolTipText(
            "Enter owner name to search.");
        searchButton.setToolTipText("Submit the Hotel room search.");
    
	}
	
    
    /**
     * Handles all DVD search events.
     *
     * @author Gokhan Daglioglu
     */
    private class SearchHotelRoom implements ActionListener {

        /**
         * Handles the actionPerformed event for the search button. Uses the
         * String values of the <code>searchField</code> member.
         *
         * @param ae The event initiated by the search button.
         */
        public void actionPerformed(ActionEvent ae) {
        	String[] criteria = new String[7];
            criteria[0] = nameSearchField.getText().trim();
            criteria[1] = locationSearchField.getText().trim();
            criteria[2] = sizeSearchField.getText().trim();
            criteria[3] = smokingSearchField.getText().trim();
            criteria[4] = rateSearchField.getText().trim();
            criteria[5] = dateSearchField.getText().trim();
            criteria[6] = ownerSearchField.getText().trim();
            try {            	
                setupTable(controller.find(criteria));
            } catch (GuiControllerException gce) {
                // Inspect the exception chain
                Throwable rootException = gce.getCause();
                String msg = "Search operation failed.";
                // If a syntax error occurred, get the message
                if (rootException instanceof PatternSyntaxException) {
                    msg += ("\n" + rootException.getMessage());
                }
                ApplicationRunner.handleException(msg);
            }
        }
    }
    
    /**
     * Uses the <code>tableData</code> member to refresh the contents of the
     * <code>mainTable</code>. The method will attempt to preserve all previous
     * selections and contents displayed.
     * @param hotelRoomModel 
     */
    private void setupTable(HotelRoomModel hotelRoomModel) {
        // Preserve the previous selection
        int index = mainTable.getSelectedRow();
        String prevSelected = (index >= 0)
                            ? (String) mainTable.getValueAt(index, 0)
                            : "";

        // Reset the table data
        this.mainTable.setModel(hotelRoomModel);

        // Reselect the previous item if it still exists
        for (int i = 0; i < this.mainTable.getRowCount(); i++) {
            String selected = (String) mainTable.getValueAt(i, 0);
            if (selected.equals(prevSelected)) {
                this.mainTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
	
	

}
