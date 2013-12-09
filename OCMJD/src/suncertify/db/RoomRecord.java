package suncertify.db;

import java.util.logging.Logger;

public class RoomRecord {

	private static int nextRecNo = 0;

	private boolean deleted;
	private String[] fields;

	private Logger log = Logger.getLogger("suncertify.db");

	public RoomRecord(byte flag, String[] content) {
		log.entering("suncertify.db.RoomRecord", "RoomRecord()");

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

		log.exiting("suncertify.db.RoomRecord", "RoomRecord()");
	}

	public int getNextRecNo() {
		log.entering("suncertify.db.RoomRecord", "getNextRecNo()");
		log.exiting("suncertify.db.RoomRecord", "getNextRecNo()", nextRecNo);

		return nextRecNo;
	}

	public boolean isRoomRecordDeleted() {
		log.entering("suncertify.db.RoomRecord", "isRoomRecordDeleted()");
		log.exiting("suncertify.db.RoomRecord", "isRoomRecordDeleted()",
				deleted);

		return deleted;
	}

	public long getRecNo() {
		log.entering("suncertify.db.RoomRecord", "getRecNo()");
		log.exiting("suncertify.db.RoomRecord", "getRecNo()");

		return Long.parseLong(fields[0]);
	}

	public String getFieldData(int number) {
		log.entering("suncertify.db.RoomRecord", "getFieldData()", number);
		log.exiting("suncertify.db.RoomRecord", "getFieldData()");

		return fields[number];
	}

	public String[] getAllFieldsData() {
		log.entering("suncertify.db.RoomRecord", "getAllFieldsData()");
		log.exiting("suncertify.db.RoomRecord", "getAllFieldsData()");

		return fields;
	}
}
