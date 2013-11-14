package suncertify.db;

import java.io.*;
import java.util.logging.Logger;

public class DatabaseFileAccess {

	private long startOfDataOffset;

	private Logger log = Logger.getLogger("suncertify.db");

	private RandomAccessFile database = null;
	private String dbLocation = null;

	private Database db = new Database();

	public DatabaseFileAccess(final String providedDbLocation) {
		log.entering("DatabaseFileAccess", "DatabaseFileAccess",
				providedDbLocation);

		if (database == null) {
			try {
				database = new RandomAccessFile(providedDbLocation, "r");
				log.fine("Database opened");

				dbLocation = providedDbLocation;

				database.close();
				log.fine("Database closed");
			} catch (final FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startOfDataOffset = readSchemaFromFile();
			log.fine("Schema read");

			readRecordsFromFile();
			log.fine("Database loaded into memory");
		} else if (dbLocation != providedDbLocation) {
			log.warning("Only one database location can be specified. "
					+ "Current location: " + dbLocation + " "
					+ "Ignoring provided path: " + providedDbLocation);
		}

		log.exiting("DatabaseFileAccess", "DatabaseFileAccess");
	}

	public long readSchemaFromFile() {
		log.entering("DatabaseFileAccess", "readDatabaseSchema");
		long offset = -1;

		try {
			database = new RandomAccessFile(dbLocation, "r");
			log.fine("Database opened");
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			db.setMagicCookie(database.readInt());
			db.setFieldsPerRecord(database.readShort());

			for (int i = 0; i < db.getFieldsPerRecord(); i++) {
				int fieldNameLengthTemp;
				fieldNameLengthTemp = database.readByte();

				final byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
				database.read(fieldNameTemp, 0, fieldNameLengthTemp);

				int fieldLength;
				fieldLength = database.readByte();

				try {
					final FieldInfo fieldInfoTemp = new FieldInfo(
							fieldNameLengthTemp, fieldNameTemp, fieldLength);
					db.addFieldInfo(fieldInfoTemp);
				} catch (final UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			offset = database.getFilePointer();

			database.close();
			log.fine("Database closed");

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.exiting("DatabaseFileAccess", "readDatabaseSchema");

		return offset;
	}

	public void readRecordsFromFile() {
		log.entering("DatabaseFileAccess", "readDatabase");

		try {
			database = new RandomAccessFile(dbLocation, "r");
			log.fine("Database opened");

			database.seek(startOfDataOffset);
			log.fine("Pointer moved to start of Data");
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (database.getFilePointer() < database.length()) {
				final String[] recordContents = new String[db
						.getFieldsPerRecord()];

				byte recordFlag;
				recordFlag = database.readByte();

				for (int index = 0; index < db.getFieldsPerRecord(); index++) {
					final byte[] temp = new byte[db.getFieldInfoAtIndex(index)
							.getBytesInField()];
					database.read(temp, 0, db.getFieldInfoAtIndex(index)
							.getBytesInField());

					try {
						recordContents[index] = new String(temp, 0,
								temp.length, "US-ASCII");
					} catch (final UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				db.addRecord(recordFlag, recordContents);
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			database.close();
			log.fine("Database closed");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.exiting("DatabaseFileAccess", "readDatabaseSchema");
	}

	public void printDatabaseSchema() {
		log.entering("DatabaseFileAccess", "printDatabaseSchema");

		System.out.println("Magic Cookie: " + db.getMagicCookie());
		System.out.println("Fields per Record: " + db.getFieldsPerRecord());

		for (int i = 0; i < db.getFieldsPerRecord(); i++) {
			System.out.println("Field " + (i + 1) + ":");
			System.out.println("    Length of Field Name: "
					+ db.getFieldInfoAtIndex(i).getBytesInName());
			System.out.println("    Field Name: "
					+ db.getFieldInfoAtIndex(i).getName());
			System.out.println("    Length of Field: "
					+ db.getFieldInfoAtIndex(i).getBytesInField());
		}

		log.exiting("DatabaseFileAccess", "printDatabase");
	}

	public void printDatabase() {
		log.entering("DatabaseFileAccess", "printDatabase");
		int i;

		System.out.print("record");
		System.out.print("deleted");
		for (i = 0; i < db.getFieldsPerRecord(); i++) {
			System.out.format("%-"
					+ (db.getFieldInfoAtIndex(i).getBytesInField() > db
							.getFieldInfoAtIndex(i).getBytesInName() ? db
							.getFieldInfoAtIndex(i).getBytesInField() : db
							.getFieldInfoAtIndex(i).getBytesInName()) + "s", db
					.getFieldInfoAtIndex(i).getName());
		}

		for (i = 0; i < db.getNumberOfRecords(); i++) {
			System.out.println();
			System.out.format("%-6s", db.getRecordIDAtIndex(i));
			System.out.format("%-7s", db.isRecordDeleted(i));
			for (int j = 0; j < db.getFieldsPerRecord(); j++) {
				System.out
						.format("%-"
								+ (db.getFieldInfoAtIndex(j).getBytesInField() > db
										.getFieldInfoAtIndex(j)
										.getBytesInName() ? db
										.getFieldInfoAtIndex(j)
										.getBytesInField() : db
										.getFieldInfoAtIndex(j)
										.getBytesInName()) + "s", db
								.getRecordAtIndex(i)[j]);
			}
		}

		log.exiting("DatabaseFileAccess", "printDatabase");
	}

	public String[] readRecord(final long recNo) {
		return db.getRecordAtIndex(recNo);
	}

	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) {
		// TODO Auto-generated method stub
	}

	public void deleteRecord(final long recNo, final long lockCookie) {
		// TODO Auto-generated method stub
	}

	public long[] findByCriteria(final String[] criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public long createRecord(final String[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long lockRecord(long recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return recNo;
	}

	public void unlock(long recNo, long cookie) throws SecurityException {
	}

	public long[] getAllRecordIDs() {
		return db.getValidRecordIDs();
	}
}
