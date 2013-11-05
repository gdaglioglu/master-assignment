package suncertify.db;

import java.io.UnsupportedEncodingException;

public class FieldInfo {
	
	private int bytesInName;
	private String name;
	private int bytesInField;
	
	public FieldInfo() {
	}
	
	public FieldInfo(int sizeName, String name, int sizeContents) {
		this.bytesInName = sizeName;
		this.name = name;
		this.bytesInField = sizeContents;
	}
	
	public FieldInfo(int sizeName, byte[] name, int sizeContents) throws UnsupportedEncodingException {
		this(sizeName, new String(name, "US-ASCII"), sizeContents);
	}
	
	public int getBytesInName(){
		return bytesInName;
	}
	
	public void setBytesInName(int sizeName){
		this.bytesInName = sizeName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getBytesInField(){
		return bytesInField;
	}
	
	public void setBytesInField(int sizeContents){
		this.bytesInField = sizeContents;
	}
}
