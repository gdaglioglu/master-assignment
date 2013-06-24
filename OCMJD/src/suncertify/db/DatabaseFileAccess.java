package suncertify.db;

import java.io.*;
import java.util.logging.Logger;

public class DatabaseFileAccess {
	
	private Logger log = Logger.getLogger("suncertify.db");
	
	private RandomAccessFile database = null;
	private String dbLocation = null;
	
	public DatabaseFileAccess(String providedDbLocation) throws FileNotFoundException {
		log.entering("DatabaseFileAccess", "DatabaseFileAccess", providedDbLocation);
		
		if (database == null) {
			database = new RandomAccessFile(providedDbLocation, "rw");
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

	public void printDatabase() throws IOException {
		log.entering("DatabaseFileAccess", "printDatabase");
		try {
				int i = 0, j = 0;

				
				long magicCookie = database.readInt();
				short fieldsInRecord = database.readShort();

				int[] fieldNamesLengths = new int[fieldsInRecord];
				String[] fieldNames = new String[fieldsInRecord];
				int[] fieldLengths = new int[fieldsInRecord];

				for (i = 0; i < fieldsInRecord; i++) {
					fieldNamesLengths[i] = database.readByte();
					byte[] temp = new byte[fieldNamesLengths[i]];
					database.read(temp, 0, fieldNamesLengths[i]);
					fieldNames[i] = new String(temp,0,temp.length, "US-ASCII");
					fieldLengths[i] = database.readByte(); 
				}
				

				long location = database.getFilePointer();
				long current = 0;
				i = 0;	
				while (database.getFilePointer() < database.length()) {
					byte[] temp = new byte[1];
					temp[0] = database.readByte();
					for (j = 0; j < fieldsInRecord; j++) {
						temp = new byte[fieldLengths[j]];
						database.read(temp, 0, fieldLengths[j]);
					}
					i++;
				}
				
				database.seek(location);
				String[][] records = new String[i][fieldsInRecord + 1];
				i = 0;
				while (database.getFilePointer() < database.length()) {
					byte[] temp = new byte[1];
					temp[0] = database.readByte();
					records[i][0] = new String(temp,0,temp.length, "US-ASCII");
					for (j = 0; j < fieldsInRecord; j++) {
						temp = new byte[fieldLengths[j]];
						database.read(temp, 0, fieldLengths[j]);
						records[i][j+1] = new String(temp,0,temp.length, "US-ASCII");
					}
					i++;
				}
				
				System.out.println(magicCookie);
				System.out.println(fieldsInRecord);
				for (i = 0; i < fieldsInRecord; i++) {
					System.out.println(fieldNamesLengths[i]);
					System.out.println(fieldNames[i]);
					System.out.println(fieldLengths[i]);
				}
				for (i = 0; i < records.length; i++) {
					for (j = 0; j < records[i].length; j++) {
						System.out.println(records[i][j]);
					}
				}
				
		} finally {
			
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
