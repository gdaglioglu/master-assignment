package suncertify.db;

import java.io.IOException;

public class TestClass {

	public static void main(String[] args) throws IOException {
		Data test = null;
		
		String user = System.getProperty("user.name");
		
		try {
			test = new Data("C:\\Users\\" + user + "\\git\\OCMJD\\Instructions\\db-1x3.db");
		}
		catch (Exception e) {
			System.err.println("Caught Exception: " + e.getMessage());
		}
		
		test.printDatabase();
	}

}
