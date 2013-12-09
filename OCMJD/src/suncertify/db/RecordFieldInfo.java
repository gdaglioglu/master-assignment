package suncertify.db;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class RecordFieldInfo {

	private int bytesInFieldName;
	private String fieldName;
	private int bytesInFieldData;

	private Logger log = Logger.getLogger("suncertify.db");

	public RecordFieldInfo(int sizeOfFieldName, String fieldName,
			int sizeOfFieldData) {
		log.entering("suncertify.db.RecordFieldInfo", "RecordFieldInfo()");

		bytesInFieldName = sizeOfFieldName;
		this.fieldName = fieldName;
		bytesInFieldData = sizeOfFieldData;

		log.exiting("suncertify.db.RecordFieldInfo", "RecordFieldInfo()");
	}

	public RecordFieldInfo(int sizeOfFieldName, byte[] fieldName,
			int sizeOfFieldData) throws UnsupportedEncodingException {
		this(sizeOfFieldName, new String(fieldName, "US-ASCII"),
				sizeOfFieldData);
	}

	public int getBytesInFieldName() {
		log.entering("suncertify.db.RecordFieldInfo", "getBytesInFieldName()");
		log.exiting("suncertify.db.RecordFieldInfo", "getBytesInFieldName()",
				bytesInFieldName);

		return bytesInFieldName;
	}

	public void setBytesInFieldName(int sizeOfFieldName) {
		log.entering("suncertify.db.RecordFieldInfo", "setBytesInFieldName()",
				sizeOfFieldName);

		bytesInFieldName = sizeOfFieldName;

		log.exiting("suncertify.db.RecordFieldInfo", "setBytesInFieldName()");
	}

	public String getFieldName() {
		log.entering("suncertify.db.RecordFieldInfo", "getFieldName()");
		log.exiting("suncertify.db.RecordFieldInfo", "getFieldName()",
				fieldName);

		return fieldName;
	}

	public void setFieldName(String fieldName) {
		log.entering("suncertify.db.RecordFieldInfo", "setFieldName()",
				fieldName);

		this.fieldName = fieldName;

		log.exiting("suncertify.db.RecordFieldInfo", "setFieldName()");
	}

	public int getBytesInField() {
		log.entering("suncertify.db.RecordFieldInfo", "getBytesInField()");
		log.exiting("suncertify.db.RecordFieldInfo", "getBytesInField()",
				bytesInFieldData);

		return bytesInFieldData;
	}

	public void setBytesInField(int sizeOfFieldData) {
		log.entering("suncertify.db.RecordFieldInfo", "setBytesInField()",
				sizeOfFieldData);

		bytesInFieldData = sizeOfFieldData;

		log.exiting("suncertify.db.RecordFieldInfo", "setBytesInField()");
	}

}
