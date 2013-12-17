/*
 * RecordFieldInfo
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * The Class RecordFieldInfo.
 */
public class RecordFieldInfo {

	/** The bytes in field name. */
	private int bytesInFieldName;

	/** The field name. */
	private String fieldName;

	/** The bytes in field data. */
	private int bytesInFieldData;

	/**
	 * Instantiates a new record field info.
	 *
	 * @param sizeOfFieldName the size of field name
	 * @param fieldName the field name
	 * @param sizeOfFieldData the size of field data
	 */
	public RecordFieldInfo(final int sizeOfFieldName, final String fieldName,
			final int sizeOfFieldData) {
		this.bytesInFieldName = sizeOfFieldName;
		this.fieldName = fieldName;
		this.bytesInFieldData = sizeOfFieldData;
	}

	/**
	 * Instantiates a new record field info.
	 *
	 * @param sizeOfFieldName the size of field name
	 * @param fieldName the field name
	 * @param sizeOfFieldData the size of field data
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public RecordFieldInfo(final int sizeOfFieldName, final byte[] fieldName,
			final int sizeOfFieldData) throws UnsupportedEncodingException {
		this(sizeOfFieldName, new String(fieldName, "US-ASCII"),
				sizeOfFieldData);
	}

	/**
	 * Gets the bytes in field name.
	 *
	 * @return the bytes in field name
	 */
	public int getBytesInFieldName() {
		return this.bytesInFieldName;
	}

	/**
	 * Sets the bytes in field name.
	 *
	 * @param sizeOfFieldName the new bytes in field name
	 */
	public void setBytesInFieldName(final int sizeOfFieldName) {
		this.bytesInFieldName = sizeOfFieldName;
	}

	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * Sets the field name.
	 *
	 * @param fieldName the new field name
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Gets the bytes in field.
	 *
	 * @return the bytes in field
	 */
	public int getBytesInField() {
		return this.bytesInFieldData;
	}

	/**
	 * Sets the bytes in field.
	 *
	 * @param sizeOfFieldData the new bytes in field
	 */
	public void setBytesInField(final int sizeOfFieldData) {
		this.bytesInFieldData = sizeOfFieldData;
	}
}
