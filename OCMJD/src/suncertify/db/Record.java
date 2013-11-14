package suncertify.db;

public class Record {

	private static int numberOfRecords = 0;

	private int recordID;
	private boolean deleted;
	private String[] fields;

	public Record() {
	}

	public Record(byte flag, String[] content) {

		recordID = ++numberOfRecords;

		if (flag == 0x00) {
			deleted = false;
		} else if (flag == 0xFF) {
			deleted = true;
		}
		// else throw corrupt data

		fields = new String[content.length + 1];
		fields[0] = String.valueOf(recordID);
		for (int i = 0; i < content.length; i++) {
			fields[i + 1] = content[i];
		}
	}

	public String[] getAllFields() {
		return fields;
	}

	public String getField(int number) {
		return fields[number];
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public int getRecordID() {
		return recordID;
	}

	public boolean isDeleted() {
		return deleted;
	}
}
