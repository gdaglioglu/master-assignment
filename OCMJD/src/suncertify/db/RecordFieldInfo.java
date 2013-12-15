package suncertify.db;

import java.io.UnsupportedEncodingException;

public class RecordFieldInfo {

	private int bytesInFieldName;
	private String fieldName;
	private int bytesInFieldData;

	public RecordFieldInfo(int sizeOfFieldName, String fieldName,
			int sizeOfFieldData) {
		bytesInFieldName = sizeOfFieldName;
		this.fieldName = fieldName;
		bytesInFieldData = sizeOfFieldData;
	}

	public RecordFieldInfo(int sizeOfFieldName, byte[] fieldName,
			int sizeOfFieldData) throws UnsupportedEncodingException {
		this(sizeOfFieldName, new String(fieldName, "US-ASCII"),
				sizeOfFieldData);
	}

	public int getBytesInFieldName() {
		return bytesInFieldName;
	}

	public void setBytesInFieldName(int sizeOfFieldName) {
		bytesInFieldName = sizeOfFieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getBytesInField() {
		return bytesInFieldData;
	}

	public void setBytesInField(int sizeOfFieldData) {
		bytesInFieldData = sizeOfFieldData;
	}
}
