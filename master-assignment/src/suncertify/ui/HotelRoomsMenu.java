package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class HotelRoomsMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -81759777197357075L;


	public HotelRoomsMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new QuitApplication());
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);
        fileMenu.add(quitMenuItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        this.add(fileMenu);
	}
	
    
    /**
     * Handles all application quit events.
     *
     * @author Gokhan Daglioglu
     */
    private class QuitApplication implements ActionListener {

        /**
         * Quits the application when invoked.
         *
         * @param ae The event triggering the quit operation.
         */
        public void actionPerformed(ActionEvent ae) {
            System.exit(0);
        }
    }
}