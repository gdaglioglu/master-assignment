package suncertify.ui;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import suncertify.util.ApplicationMode;

public class Server extends JFrame {

	// TODO serialVersionUID
	private static final long serialVersionUID = -7816958458327680485L;
	
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
		try {
			suncertify.remote.ServerRegistry.register();
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
		
		dialog.serverRunning(true);
	}
}
