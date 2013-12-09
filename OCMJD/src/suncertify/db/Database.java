package suncertify.db;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Database {

	private long magicCookie;
	private short fieldsPerRoomRecord;
	private List<RecordFieldInfo> dbSchema = new ArrayList<RecordFieldInfo>();
	private List<RoomRecord> roomRecords = new ArrayList<RoomRecord>();

	private Logger log = Logger.getLogger("suncertify.db");

	public Database() {
	}

	public long getMagicCookie() {
		log.entering("suncertify.db.Database", "getMagicCookie()");
		log.exiting("suncertify.db.Database", "getMagicCookie()");

		return magicCookie;
	}

	public void setMagicCookie(long number) {
		log.entering("suncertify.db.Database", "setMagicCookie()", number);

		magicCookie = number;

		log.exiting("suncertify.db.Database", "setMagicCookie()");
	}

	public short getFieldsPerRoomRecord() {
		log.entering("suncertify.db.Database", "getFieldsPerRoomRecord()");
		log.exiting("suncertify.db.Database", "getFieldsPerRoomRecord()");

		return fieldsPerRoomRecord;
	}

	public void setFieldsPerRoomRecord(short number) {
		log.entering("suncertify.db.Database", "setFieldsPerRoomRecord()",
				number);

		fieldsPerRoomRecord = number;

		log.exiting("suncertify.db.Database", "setFieldsPerRoomRecord()");
	}

	public RecordFieldInfo getRecordFieldInfoAtIndex(int index) {
		log.entering("suncertify.db.Database", "getRecordFieldInfoAtIndex()",
				index);
		log.exiting("suncertify.db.Database", "getRecordFieldInfoAtIndex()");

		return dbSchema.get(index);
	}

	public void addRecordFieldInfo(RecordFieldInfo recordFieldInfo) {
		log.entering("suncertify.db.Database", "addRecordFieldInfo()",
				recordFieldInfo);

		dbSchema.add(recordFieldInfo);

		log.exiting("suncertify.db.Database", "addRecordFieldInfo()");
	}

	public String[] getFieldNames() {
		log.entering("suncertify.db.Database", "getFieldNames()");

		String[] fieldNames;
		List<String> tempArray = new ArrayList<String>();

		for (int index = 0; index < dbSchema.size(); index++) {
			tempArray.add(getRecordFieldInfoAtIndex(index).getFieldName());
		}

		fieldNames = new String[tempArray.size()];
		for (int index = 0; index < tempArray.size(); index++) {
			fieldNames[index] = tempArray.get(index);
		}

		log.exiting("suncertify.db.Database", "getFieldNames()");

		return fieldNames;
	}

	public RoomRecord getRoomRecordAtIndex(long index) {
		log.entering("suncertify.db.Database", "getRoomRecordAtIndex()", index);
		log.exiting("suncertify.db.Database", "getRoomRecordAtIndex()");

		return roomRecords.get((int) index);
	}

	public String[] getRoomRecordDataAtIndex(long index) {
		log.entering("suncertify.db.Database", "getRoomRecordDataAtIndex()",
				index);
		log.exiting("suncertify.db.Database", "getRoomRecordDataAtIndex()");

		return getRoomRecordAtIndex(index).getAllFieldsData();
	}

	public RoomRecord getRoomRecord(long recNo) {
		log.entering("suncertify.db.Database", "getRoomRecord()", recNo);
		long index = getIndexOfRecNo(recNo);
		// TODO Expect RecordNotFoundException

		log.exiting("suncertify.db.Database", "getRoomRecord()");

		return getRoomRecordAtIndex(index);
	}

	public String[] getRoomRecordData(long recNo) {
		log.entering("suncertify.db.Database", "getRoomRecordData()", recNo);
		log.exiting("suncertify.db.Database", "getRoomRecordData()");

		// TODO Expect RecordNotFoundException
		return getRoomRecord(recNo).getAllFieldsData();
	}

	public void addRoomRecord(RoomRecord roomRecord) {
		log.entering("suncertify.db.Database", "addRoomRecord()", roomRecord);

		roomRecords.add(roomRecord);

		log.exiting("suncertify.db.Database", "addRoomRecord()");
	}

	public void addRoomRecord(byte flag, String[] data) {
		log.entering("suncertify.db.Database", "addRoomRecord()");

		this.addRoomRecord(new RoomRecord(flag, data));

		log.exiting("suncertify.db.Database", "addRoomRecord()");
	}

	public int getRecNoAtIndex(long index) {
		log.entering("suncertify.db.Database", "getRecNoAtIndex()", index);

		int recNo = Integer.parseInt(getRoomRecordDataAtIndex(index)[0]);

		log.exiting("suncertify.db.Database", "getRecNoAtIndex()", recNo);

		return recNo;
	}

	public long getIndexOfRecNo(long recNo) {
		log.entering("suncertify.db.Database", "getIndexOfRecNo()", recNo);

		for (long index = 0; index < roomRecords.size(); index++) {

			if (getRecNoAtIndex(index) == recNo) {

				log.exiting("suncertify.db.Database", "getIndexOfRecNo()",
						index);

				return index;
			}
		}

		log.exiting("suncertify.db.Database", "getIndexOfRecNo()", -1);

		// TODO Expect RecordNotFoundException
		return -1;
	}

	public int getNumberOfRoomRecords() {
		log.entering("suncertify.db.Database", "getNumberOfRoomRecords()");
		log.exiting("suncertify.db.Database", "getNumberOfRoomRecords()");

		return roomRecords.size();
	}

	public List<RoomRecord> getValidRoomRecords() {
		log.entering("suncertify.db.Database", "getValidRoomRecords()");

		List<RoomRecord> validRoomRecords = new ArrayList<RoomRecord>();

		for (RoomRecord roomRecord : roomRecords) {
			if (!roomRecord.isRoomRecordDeleted()) {
				validRoomRecords.add(roomRecord);
			}
		}

		log.exiting("suncertify.db.Database", "getValidRoomRecords()");

		return validRoomRecords;
	}

	public long[] getValidRecNos() {
		log.entering("suncertify.db.Database", "getValidRecNos()");

		List<RoomRecord> validRoomRecords = getValidRoomRecords();

		List<Long> temp = new ArrayList<Long>();

		for (RoomRecord validRoomRecord : validRoomRecords) {
			temp.add(validRoomRecord.getRecNo());
		}

		long[] result = new long[temp.size()];

		for (int validIndex = 0; validIndex < temp.size(); validIndex++) {
			result[validIndex] = temp.get(validIndex);
		}

		log.exiting("suncertify.db.Database", "getValidRecNos()");

		return result;
	}

	public long[] getValidRoomRecordIndices() {
		log.entering("suncertify.db.Database", "getValidRoomRecordIndices()");

		long[] validRecNos = getValidRecNos();
		long[] validRoomRecordIndices = new long[validRecNos.length];

		for (int i = 0; i < validRecNos.length; i++) {
			validRoomRecordIndices[i] = getIndexOfRecNo(validRecNos[i]);
		}

		log.exiting("suncertify.db.Database", "getValidRoomRecordIndices()");

		return validRoomRecordIndices;
	}

	public boolean isRecordMatch(long recNo, String[] criteria) {
		log.entering("suncertify.db.Database", "isRecordMatch()");

		String[] recordData = getRoomRecordData(recNo);

		for (int i = 0; i < fieldsPerRoomRecord; i++) {
			if (criteria[i] == null) {
				continue;
			}
			if (recordData[i].contains(criteria[i])) {
				log.exiting("suncertify.db.Database", "isRecordMatch()", true);

				return true;
			}
		}
		log.exiting("suncertify.db.Database", "isRecordMatch()", false);

		return false;
	}
}
