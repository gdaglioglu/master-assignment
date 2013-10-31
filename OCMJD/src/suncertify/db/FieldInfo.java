package suncertify.db;

import java.io.UnsupportedEncodingException;

public class FieldInfo {
	
	private int sizeName;
	private String name;
	private int sizeContents;
	
	public FieldInfo() {
	}
	
	public FieldInfo(int sizeName, String name, int sizeContents) {
		this.sizeName = sizeName;
		this.name = name;
		this.sizeContents = sizeContents;
	}
	
	public FieldInfo(int sizeName, byte[] name, int sizeContents) throws UnsupportedEncodingException {
		this(sizeName, new String(name, "US-ASCII"), sizeContents);
	}
	
	public int getSizeName(){
		return sizeName;
	}
	
	public void setSizeName(int sizeName){
		this.sizeName = sizeName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getSizeContents(){
		return sizeContents;
	}
	
	public void setSizeContents(int sizeContents){
		this.sizeContents = sizeContents;
	}
}
