package suncertify.db;

public class RoomRecord {

	private static int nextRecNo = 0;

	private boolean deleted;
	private String[] fields;

	public RoomRecord() {
	}

	public RoomRecord(byte flag, String[] content) {

		long recNo = nextRecNo++;

		if (flag == 0x00) {
			deleted = false;
		} else if (flag == 0xFF) {
			deleted = true;
		}
		// TODO else invalid data

		fields = new String[content.length + 1];
		fields[0] = String.valueOf(recNo);
		for (int i = 0; i < content.length; i++) {
			fields[i + 1] = content[i];
		}
	}

	public int getNextRecNo() {
		return nextRecNo;
	}

	public boolean isRoomRecordDeleted() {
		return deleted;
	}

	public long getRecNo() {
		return Long.parseLong(fields[0]);
	}

	public String getFieldData(int number) {
		return fields[number];
	}

	public String[] getAllFieldsData() {
		return fields;
	}
}
