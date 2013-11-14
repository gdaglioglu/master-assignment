package suncertify.db;

import java.io.UnsupportedEncodingException;

public class FieldInfo {

	private int bytesInName;
	private String name;
	private int bytesInField;

	public FieldInfo() {
	}

	public FieldInfo(int sizeName, byte[] name, int sizeContents)
			throws UnsupportedEncodingException {
		this(sizeName, new String(name, "US-ASCII"), sizeContents);
	}

	public FieldInfo(int sizeName, String name, int sizeContents) {
		bytesInName = sizeName;
		this.name = name;
		bytesInField = sizeContents;
	}

	public int getBytesInField() {
		return bytesInField;
	}

	public int getBytesInName() {
		return bytesInName;
	}

	public String getName() {
		return name;
	}

	public void setBytesInField(int sizeContents) {
		bytesInField = sizeContents;
	}

	public void setBytesInName(int sizeName) {
		bytesInName = sizeName;
	}

	public void setName(String name) {
		this.name = name;
	}
}
