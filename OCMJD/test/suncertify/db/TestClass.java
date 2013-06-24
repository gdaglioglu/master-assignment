package suncertify.db;

import java.io.IOException;

public class TestClass {

	public static void main(String[] args) throws IOException {
		Data test = new Data("C:\\Users\\eeoimoo\\git\\OCMJD\\Instructions\\db-1x3.db");
		
		test.printDatabase();
	}

}
