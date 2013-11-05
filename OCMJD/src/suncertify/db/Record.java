package suncertify.db;

public class Record {
	
	private static int numberOfRecords = 0;
	
	private int recordID;
	private boolean deleted;
	private String[] fields;
	
	public Record() {
	}
	
	public Record(byte flag, String[] content){
		
		recordID = numberOfRecords++;
		
		if (flag == 0x00) {
			deleted = false;
		}
		else if (flag == 0xFF) {
			deleted = true;
		}
		//else throw corrupt data
		
		fields = content;
	}
	
	public int getNumberOfRecords() {
		return numberOfRecords;
	}
	
	public int getRecordID() {
		return recordID;
	}
	
	public String getField(int number) {
		return fields[number];
	}
	
	public String[] getAllFields() {
		return fields;
	}
	
	public boolean isDeleted(){
		return deleted;
	}
}
