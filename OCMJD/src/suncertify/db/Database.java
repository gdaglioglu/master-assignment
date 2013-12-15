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

	public RoomRecord getRoomRecord(long recNo) {
		return roomRecords.get((int) recNo);
	}

	public String[] getRoomRecordData(long recNo) {
		return getRoomRecord(recNo).getAllFieldsData();
	}

	public void addRoomRecord(RoomRecord roomRecord) {
		roomRecords.add(roomRecord);
	}

	public void addRoomRecord(byte flag, String[] data) {
		this.addRoomRecord(new RoomRecord(flag, data));
	}

	public int getNumberOfRoomRecords() {
		return roomRecords.size();
	}

	public long[] getValidRoomRecordRecNos() {
		List<Long> list = new ArrayList<Long>();
		
		for(int i = 0; i < roomRecords.size(); i++) {
			if(!roomRecords.get(i).isRoomRecordDeleted()){
				list.add((long) i);
			}
		}

		long[] validRoomRecordRecNos = new long[list.size()];

		for (int i = 0; i < list.size(); i++) {
			validRoomRecordRecNos[i] = list.get(i);
		}

		return validRoomRecordRecNos;
	}
	
	public List<RoomRecord> getValidRoomRecords() {
		List<RoomRecord> validRoomRecords = new ArrayList<RoomRecord>();

		long[] validRoomRecordRecNos = getValidRoomRecordRecNos();
		
		for(long validRecNo : validRoomRecordRecNos){
			validRoomRecords.add(roomRecords.get((int) validRecNo));
		}

		return validRoomRecords;
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
