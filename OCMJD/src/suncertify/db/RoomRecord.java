package suncertify.db;

public class RoomRecord {

	private boolean deleted;
	private String[] fields;

	public RoomRecord(byte flag, String[] content) {
		if (flag == 0x00) {
			deleted = false;
		} else if (flag == 0xFF) {
			deleted = true;
		}
		// TODO else invalid data

		fields = content;
	}

	public boolean isRoomRecordDeleted() {
		return deleted;
	}

	public String getFieldData(int number) {
		return fields[number];
	}

	public String[] getAllFieldsData() {
		return fields;
	}
}
