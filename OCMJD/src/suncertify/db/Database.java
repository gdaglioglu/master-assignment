package suncertify.db;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private long magicCookie;
	private short fieldsPerRecord;
	private List<FieldInfo> fieldsInfo = new ArrayList<FieldInfo>();
	private List<Record> records = new ArrayList<Record>();
	
	public Database() {
	}
	
	public void setMagicCookie(long number) {
		magicCookie = number;
	}
	
	public long getMagicCookie() {
		return magicCookie;
	}
	
	public void setFieldsPerRecord(short number) {
		fieldsPerRecord = number;
	}
	
	public short getFieldsPerRecord() {
		return fieldsPerRecord;
	}
	
	public void addFieldInfo (FieldInfo fieldInfo) {
		fieldsInfo.add(fieldInfo);
	}
	
	public FieldInfo getFieldInfoAtIndex (int index) {
		return fieldsInfo.get(index);
	}
	
	public void addRecord (Record record) {
		records.add(record);
	}
	
	public void addRecord (byte[] flag, String[] content) {
		this.addRecord(new Record(flag, content));
	}
	
	public String[] getRecordAtIndex (long index) {
		return records.get((int)index).getAllFields();
	}
	
	public boolean isRecordDeleted (long index) {
		return records.get((int)index).isDeleted();
	}
	
	public int getNumberOfRecords() {
		return records.size();
	}
}
