package suncertify.db;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private long magicCookie;
	private short fieldsPerRoomRecord;
	private List<RecordFieldInfo> dbSchema = new ArrayList<RecordFieldInfo>();
	private List<RoomRecord> roomRecords = new ArrayList<RoomRecord>();

	public Database() {
	}

	public long getMagicCookie() {
		return magicCookie;
	}

	public void setMagicCookie(long number) {
		magicCookie = number;
	}

	public short getFieldsPerRoomRecord() {
		return fieldsPerRoomRecord;
	}

	public void setFieldsPerRoomRecord(short number) {
		fieldsPerRoomRecord = number;
	}

	public RecordFieldInfo getRecordFieldInfoAtIndex(int index) {
		return dbSchema.get(index);
	}

	public void addRecordFieldInfo(RecordFieldInfo recordFieldInfo) {
		dbSchema.add(recordFieldInfo);
	}

	public String[] getFieldNames() {
		String[] fieldNames;
		List<String> tempArray = new ArrayList<String>();

		for (int index = 0; index < dbSchema.size(); index++) {
			tempArray.add(getRecordFieldInfoAtIndex(index).getFieldName());
		}

		fieldNames = new String[tempArray.size()];
		for (int index = 0; index < tempArray.size(); index++) {
			fieldNames[index] = tempArray.get(index);
		}

		return fieldNames;
	}

	public RoomRecord getRoomRecordAtIndex(long index) {
		return roomRecords.get((int) index);
	}

	public String[] getRoomRecordDataAtIndex(long index) {
		return getRoomRecordAtIndex(index).getAllFieldsData();
	}

	public RoomRecord getRoomRecord(long recNo) {
		long index = getIndexOfRecNo(recNo);
		// TODO Expect RecordNotFoundException
		return getRoomRecordAtIndex(index);
	}

	public String[] getRoomRecordData(long recNo) {
		// TODO Expect RecordNotFoundException
		return getRoomRecord(recNo).getAllFieldsData();
	}

	public void addRoomRecord(RoomRecord roomRecord) {
		roomRecords.add(roomRecord);
	}

	public void addRoomRecord(byte flag, String[] data) {
		this.addRoomRecord(new RoomRecord(flag, data));
	}

	public int getRecNoAtIndex(long index) {
		int recNo = Integer.parseInt(getRoomRecordDataAtIndex(index)[0]);
		return recNo;
	}

	public long getIndexOfRecNo(long recNo) {
		for (long index = 0; index < roomRecords.size(); index++) {

			if (getRecNoAtIndex(index) == recNo) {
				return index;
			}
		}
		// TODO Expect RecordNotFoundException
		return -1;
	}

	public int getNumberOfRoomRecords() {
		return roomRecords.size();
	}

	public List<RoomRecord> getValidRoomRecords() {
		List<RoomRecord> validRoomRecords = new ArrayList<RoomRecord>();

		for (RoomRecord roomRecord : roomRecords) {
			if (!roomRecord.isRoomRecordDeleted()) {
				validRoomRecords.add(roomRecord);
			}
		}
		return validRoomRecords;
	}

	public long[] getValidRecNos() {

		List<RoomRecord> validRoomRecords = getValidRoomRecords();

		List<Long> temp = new ArrayList<Long>();

		for (RoomRecord validRoomRecord : validRoomRecords) {
			temp.add(validRoomRecord.getRecNo());
		}

		long[] result = new long[temp.size()];

		for (int validIndex = 0; validIndex < temp.size(); validIndex++) {
			result[validIndex] = temp.get(validIndex);
		}
		return result;
	}

	public long[] getValidRoomRecordIndices() {
		long[] validRecNos = getValidRecNos();
		long[] validRoomRecordIndices = new long[validRecNos.length];

		for (int i = 0; i < validRecNos.length; i++) {
			validRoomRecordIndices[i] = getIndexOfRecNo(validRecNos[i]);
		}

		return validRoomRecordIndices;
	}

	public boolean isRecordMatch(long recNo, String[] criteria) {
		String[] recordData = getRoomRecordData(recNo);

		for (int i = 0; i < fieldsPerRoomRecord; i++) {
			if (criteria[i] == null) {
				continue;
			}
			if (recordData[i].contains(criteria[i])) {
				return true;
			}
		}
		return false;
	}
}
