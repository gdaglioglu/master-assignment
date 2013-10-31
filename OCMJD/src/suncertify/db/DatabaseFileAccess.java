package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseFileAccess {
	
	private Logger log = Logger.getLogger("suncertify.db");
	
	private RandomAccessFile database = null;
	private String dbLocation = null;
	
	private Database db;
	
	public DatabaseFileAccess(String providedDbLocation) throws FileNotFoundException, IOException {
		log.entering("DatabaseFileAccess", "DatabaseFileAccess", providedDbLocation);
		
		if (database == null) {
			database = new RandomAccessFile(providedDbLocation, "rw");
			readDatabase();
			dbLocation = providedDbLocation;
			log.fine("database opened and file location table populated");
		}
		else if (dbLocation != providedDbLocation) {
			log.warning("Only one database location can be specified. "
					+ "Current location: " + dbLocation + " "
					+ "Ignoring provided path: " + providedDbLocation);
		}
		log.exiting("DatabaseFileAccess", "DatabaseFileAccess");
	}

	
	public void readDatabase() throws FileNotFoundException, IOException {
		log.entering("DatabaseFileAccess", "printDatabase");
		try {
			db.setMagicCookie(database.readInt());
			db.setFieldsPerRecord(database.readShort());
	
			for (int i = 0; i < db.getFieldsPerRecord(); i++) {
				int fieldNameLengthTemp = database.readByte();
				byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
				database.read(fieldNameTemp, 0, fieldNameLengthTemp);
				int fieldLength = database.readByte();
				
				FieldInfo fieldInfoTemp = new FieldInfo(fieldNameLengthTemp, fieldNameTemp, fieldLength);
				
				db.addFieldInfo(fieldInfoTemp);
			}
							
			while (database.getFilePointer() < database.length()) {
				List<Object> recordContents = new ArrayList<Object>();
									
				byte[] temp = new byte[1];
				temp[0] = database.readByte();
				
				recordContents.add(new String(temp,0,temp.length, "US-ASCII"));
				
				for (int index = 1; index < db.getFieldsPerRecord(); index++) {
					temp = new byte[db.getFieldInfoAtIndex(index).getSizeContents()];
					database.read(temp, 0, db.getFieldInfoAtIndex(index).getSizeContents());
					recordContents.add(new String(temp,0,temp.length, "US-ASCII"));
				}
				
				db.addRecord(recordContents);
			}
			
		} finally {
		}
	}
	
	public void printDatabase() throws FileNotFoundException, IOException {
		readDatabase();
		
		int index;
		
		System.out.println("Magic Cookie: " + db.getMagicCookie());
		System.out.println("Fields per Record: " + db.getFieldsPerRecord());
		
		for (index = 0; index <= db.getFieldsPerRecord(); index++) {
			System.out.println("Field " + (index + 1) + ":");
			System.out.println("    Length of Field Name: " + db.getFieldInfoAtIndex(index).getSizeName());
			System.out.println("    Field Name: " + db.getFieldInfoAtIndex(index).getName());
			System.out.println("    Length of Field: " + db.getFieldInfoAtIndex(index).getSizeContents());
		}
		
		for (index = 0; index <= db.getFieldsPerRecord(); index++) {
			System.out.format("%-" + (db.getFieldInfoAtIndex(index).getSizeContents() > db.getFieldInfoAtIndex(index).getSizeName() ? db.getFieldInfoAtIndex(index).getSizeContents() : db.getFieldInfoAtIndex(index).getSizeName()) + "s", db.getFieldInfoAtIndex(index).getName());
		}
		
		for (index = 0; index < db.getNumberOfRecords(); index++) {
			System.out.println();
			System.out.format("%-7s", db.getRecordAtIndex(index).getValue(db.getFieldInfoAtIndex(0).getName()));
			for (int j = 1; j < db.getFieldsPerRecord(); j++) {
				System.out.format("%-" + (db.getFieldInfoAtIndex(j-1).getSizeContents() > db.getFieldInfoAtIndex(j-1).getSizeName() ? db.getFieldInfoAtIndex(j-1).getSizeContents() : db.getFieldInfoAtIndex(j-1).getSizeName()) + "s", db.getRecordAtIndex(index).getValue(db.getFieldInfoAtIndex(j).getName()));
			}
		}
	}
	
	public String[] readRecord(long recNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateRecord(long recNo, String[] data, long lockCookie) {
		// TODO Auto-generated method stub
		
	}

	public void deleteRecord(long recNo, long lockCookie) {
		// TODO Auto-generated method stub
		
	}

	public long[] findByCriteria(String[] criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public long createRecord(String[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

}
