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

	public DataFileAccess(String providedDbLocation) {
		log.entering("suncertify.db.DataFileAccess", "DataFileAccess",
				providedDbLocation);

		if (database == null) {
			try {
				database = new RandomAccessFile(providedDbLocation, "r");
				log.fine("Database opened");

				dbLocation = providedDbLocation;

				database.close();
				log.fine("Database closed");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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

		log.exiting("suncertify.db.DataFileAccess", "DataFileAccess");
	}

	public long readDatabaseSchemaFromFile() {
		log.entering("suncertify.db.DataFileAccess",
				"readDatabaseSchemaFromFile");
		long offset = -1;

		try {
			database = new RandomAccessFile(dbLocation, "r");
			log.fine("Database opened");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			db.setMagicCookie(database.readInt());
			db.setFieldsPerRoomRecord(database.readShort());

			for (int i = 0; i < db.getFieldsPerRoomRecord(); i++) {
				int fieldNameLengthTemp;
				fieldNameLengthTemp = database.readByte();

				byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
				database.read(fieldNameTemp, 0, fieldNameLengthTemp);

				int fieldLength;
				fieldLength = database.readByte();

				try {
					RecordFieldInfo fieldInfoTemp = new RecordFieldInfo(
							fieldNameLengthTemp, fieldNameTemp, fieldLength);
					db.addRecordFieldInfo(fieldInfoTemp);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			offset = database.getFilePointer();

			database.close();
			log.fine("Database closed");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.exiting("suncertify.db.DataFileAccess",
				"readDatabaseSchemaFromFile");

		return offset;
	}

	public void readRecordsDataFromFile() {
		log.entering("suncertify.db.DataFileAccess", "readRecordsDataFromFile");

		try {
			database = new RandomAccessFile(dbLocation, "r");
			log.fine("Database opened");

			database.seek(startOfDataOffset);
			log.fine("Pointer moved to start of Data");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (database.getFilePointer() < database.length()) {
				String[] recordContents = new String[db
						.getFieldsPerRoomRecord()];

				byte recordFlag;
				recordFlag = database.readByte();

				for (int index = 0; index < db.getFieldsPerRoomRecord(); index++) {
					byte[] temp = new byte[db.getRecordFieldInfoAtIndex(index)
							.getBytesInField()];
					database.read(temp, 0, db.getRecordFieldInfoAtIndex(index)
							.getBytesInField());

					try {
						recordContents[index] = new String(temp, 0,
								temp.length, "US-ASCII");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				db.addRoomRecord(recordFlag, recordContents);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			database.close();
			log.fine("Database closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.exiting("suncertify.db.DataFileAccess", "readRecordsDataFromFile");
	}

	public String[] readRecord(long recNo) {
		log.entering("suncertify.db.DataFileAccess", "readRecord", recNo);
		log.exiting("suncertify.db.DataFileAccess", "readRecord");

		return db.getRoomRecordData(recNo);
	}

	public void updateRecord(long recNo, String[] data, long lockCookie) {
		log.entering("suncertify.db.DataFileAccess", "updateRecord");

		// TODO Auto-generated method stub

		log.exiting("suncertify.db.DataFileAccess", "updateRecord");
	}

	public void deleteRecord(long recNo, long lockCookie) {
		log.entering("suncertify.db.DataFileAccess", "deleteRecord");

		// TODO Auto-generated method stub

		log.exiting("suncertify.db.DataFileAccess", "deleteRecord");
	}

	public long[] getValidRecNos() {
		log.entering("suncertify.db.DataFileAccess", "readRecord");
		log.exiting("suncertify.db.DataFileAccess", "readRecord");

		return db.getValidRecNos();
	}

	public long[] findByCriteria(String[] criteria) {
		log.entering("suncertify.db.DataFileAccess", "findByCriteria", criteria);

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

		log.exiting("suncertify.db.DataFileAccess", "findByCriteria");

		return matchedRecNos;
	}

	public long createRecord(String[] data) {
		log.entering("suncertify.db.DataFileAccess", "createRecord", data);
		log.exiting("suncertify.db.DataFileAccess", "createRecord");

		// TODO Auto-generated method stub
		return 0;
	}
}
