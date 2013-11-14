package suncertify.db;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private long magicCookie;
	private short fieldsPerRecord;
	private List<FieldInfo> schema = new ArrayList<FieldInfo>();
	private List<Record> records = new ArrayList<Record>();

	public Database() {
	}

	public void addFieldInfo(FieldInfo fieldInfo) {
		schema.add(fieldInfo);
	}

	public void addRecord(byte flag, String[] content) {
		this.addRecord(new Record(flag, content));
	}

	public void addRecord(Record record) {
		records.add(record);
	}

	public FieldInfo getFieldInfoAtIndex(int index) {
		return schema.get(index);
	}

	public short getFieldsPerRecord() {
		return fieldsPerRecord;
	}

	public long getMagicCookie() {
		return magicCookie;
	}

	public int getNumberOfRecords() {
		return records.size();
	}

	public long[] getValidRecordIDs() {
		List<Long> temp = new ArrayList<Long>();
		for (long i = 0; i < records.size(); i++) {
			if (!isRecordDeleted(i)) {
				temp.add(i);
			}
		}
		long[] result = new long[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			result[i] = temp.get(i);
		}
		return result;
	}

	public String[] getRecordAtIndex(long index) {
		return records.get((int) index).getAllFields();
	}

	public int getRecordIDAtIndex(long index) {
		return records.get((int) index).getRecordID();
	}

	public boolean isRecordDeleted(long index) {
		return records.get((int) index).isDeleted();
	}

	public void setFieldsPerRecord(short number) {
		fieldsPerRecord = number;
	}

	public void setMagicCookie(long number) {
		magicCookie = number;
	}

}
