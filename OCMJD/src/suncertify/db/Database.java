package suncertify.db;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private long magicCookie;
	private short fieldsPerRecord;
	private List<FieldInfo> fieldsInfo = new ArrayList<FieldInfo>();
	private List<Record> records = new ArrayList<Record>();
	
	public Database() {
		fieldsInfo.add(new FieldInfo(0,"deleted",1));
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
	
	public void addRecord (List<Object> content) {
		List<String> fieldNames = new ArrayList<String>();
		for (FieldInfo fieldInfo : fieldsInfo) {
			fieldNames.add(fieldInfo.getName());
		}
		
		records.add(new Record(fieldNames, content));
	}
	
	public Record getRecordAtIndex (int index) {
		return records.get(index);
	}
	
	public int getNumberOfRecords() {
		return records.size();
	}
}
