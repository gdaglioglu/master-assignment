package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataFileAccess {

	private long startOfDataOffset;

	private Logger log = Logger.getLogger("suncertify.db");

	private RandomAccessFile database = null;
	private String dbLocation = null;

	private Database db = new Database();

	public DataFileAccess(final String providedDbLocation) {
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

			startOfDataOffset = readDatabaseSchemaFromFile();
			log.fine("Schema read");

			readRecordsDataFromFile();
			log.fine("Database loaded into memory");
		} else if (dbLocation != providedDbLocation) {
			log.warning("Only one database location can be specified. "
					+ "Current location: " + dbLocation + " "
					+ "Ignoring provided path: " + providedDbLocation);
		}

		log.exiting("DatabaseFileAccess", "DatabaseFileAccess");
	}

	public Database getDatabase() {
		return db;
	}

	public long readDatabaseSchemaFromFile() {
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
			db.setFieldsPerRoomRecord(database.readShort());

			for (int i = 0; i < db.getFieldsPerRoomRecord(); i++) {
				int fieldNameLengthTemp;
				fieldNameLengthTemp = database.readByte();

				final byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
				database.read(fieldNameTemp, 0, fieldNameLengthTemp);

				int fieldLength;
				fieldLength = database.readByte();

				try {
					final RecordFieldInfo fieldInfoTemp = new RecordFieldInfo(
							fieldNameLengthTemp, fieldNameTemp, fieldLength);
					db.addRecordFieldInfo(fieldInfoTemp);
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

	public void readRecordsDataFromFile() {
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
						.getFieldsPerRoomRecord()];

				byte recordFlag;
				recordFlag = database.readByte();

				for (int index = 0; index < db.getFieldsPerRoomRecord(); index++) {
					final byte[] temp = new byte[db.getRecordFieldInfoAtIndex(
							index).getBytesInField()];
					database.read(temp, 0, db.getRecordFieldInfoAtIndex(index)
							.getBytesInField());

					try {
						recordContents[index] = new String(temp, 0,
								temp.length, "US-ASCII");
					} catch (final UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				db.addRoomRecord(recordFlag, recordContents);
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

	public String[] readRecord(final long recNo) {
		return db.getRoomRecordData(recNo);
	}

	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) {
		// TODO Auto-generated method stub
	}

	public void deleteRecord(final long recNo, final long lockCookie) {
		// TODO Auto-generated method stub
	}

	public long[] getValidRecNos() {
		return db.getValidRecNos();
	}

	public long[] findByCriteria(final String[] criteria) {
		long[] validRecNos = getValidRecNos();
		List<Long> tempArray = new ArrayList<Long>();

		for (long recNo : validRecNos) {
			if (db.isRecordMatch(recNo, criteria)) {
				tempArray.add(recNo);
			}
		}

		long[] matchedRecNos = new long[tempArray.size()];

		for (int i = 0; i < tempArray.size(); i++) {
			matchedRecNos[i] = tempArray.get(i);
		}
		return matchedRecNos;
	}

	public long createRecord(final String[] data) {
		// TODO Auto-generated method stub
		return 0;
	}
}
