package suncertify.db;

import java.io.*;
import java.util.logging.Logger;

public class DatabaseFileAccess {
	
	private Logger log = Logger.getLogger("suncertify.db");
	
	private RandomAccessFile database = null;
	private String dbLocation = null;
	
	private Database db = new Database();
	
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
		log.entering("DatabaseFileAccess", "readDatabase");
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
				String[] recordContents = new String[db.getFieldsPerRecord()];
									
				byte recordFlag = database.readByte();
				
				for (int index = 0; index < db.getFieldsPerRecord(); index++) {
					byte[] temp = new byte[db.getFieldInfoAtIndex(index).getBytesInField()];
					database.read(temp, 0, db.getFieldInfoAtIndex(index).getBytesInField());
					recordContents[index] = new String(temp,0,temp.length, "US-ASCII");
				}
				
				db.addRecord(recordFlag, recordContents);
			}
			
		} finally {
		}
	}
	
	public void printDatabase() throws FileNotFoundException, IOException {
		log.entering("DatabaseFileAccess", "printDatabase");
		int i;
		
		System.out.println("Magic Cookie: " + db.getMagicCookie());
		System.out.println("Fields per Record: " + db.getFieldsPerRecord());
		
		for (i = 0; i < db.getFieldsPerRecord(); i++) {
			System.out.println("Field " + (i + 1) + ":");
			System.out.println("    Length of Field Name: " + db.getFieldInfoAtIndex(i).getBytesInName());
			System.out.println("    Field Name: " + db.getFieldInfoAtIndex(i).getName());
			System.out.println("    Length of Field: " + db.getFieldInfoAtIndex(i).getBytesInField());
		}
		
		System.out.print("record");
		System.out.print("deleted");
		for (i = 0; i < db.getFieldsPerRecord(); i++) {
			System.out.format("%-" + (db.getFieldInfoAtIndex(i).getBytesInField() > db.getFieldInfoAtIndex(i).getBytesInName() ? db.getFieldInfoAtIndex(i).getBytesInField() : db.getFieldInfoAtIndex(i).getBytesInName()) + "s", db.getFieldInfoAtIndex(i).getName());
		}
		
		for (i = 0; i < db.getNumberOfRecords(); i++) {
			System.out.println();
			System.out.format("%-6s", db.getRecordIDAtIndex(i));
			System.out.format("%-7s", db.isRecordDeleted(i));
			for (int j = 0; j < db.getFieldsPerRecord(); j++) {
				System.out.format("%-" + (db.getFieldInfoAtIndex(j).getBytesInField() > db.getFieldInfoAtIndex(j).getBytesInName() ? db.getFieldInfoAtIndex(j).getBytesInField() : db.getFieldInfoAtIndex(j).getBytesInName()) + "s", db.getRecordAtIndex(i)[j]);
			}
		}
	}
	
	public String[] readRecord(long recNo) {
		return db.getRecordAtIndex(recNo);
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
