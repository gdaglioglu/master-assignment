package suncertify.db;

public class Record {
	
	private boolean deleted;
	private String[] fields;
	
	public Record() {
	}
	
	public Record(byte[] flag, String[] content){
		
		if (flag[0] == 0x00) {
			deleted = false;
		}
		else if (flag[0] == 0xFF) {
			deleted = true;
		}
		//else throw corrupt data
		
		fields = content;
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
