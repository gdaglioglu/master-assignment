/*
 * RecordFieldInfo
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.io.UnsupportedEncodingException;

/**
 * This class holds the schema information for a field in the datafile
 * 
 * @author Eoin Mooney
 */
public class RecordFieldInfo {

	/**
	 * The number of consecutive bytes to be read in to accurately read this
	 * field name.
	 */
	private int bytesInFieldName;

	/** The name of this field. */
	private String fieldName;

	/**
	 * The number of consecutive bytes to be read in to accurately read in data
	 * in this field.
	 */
	private int bytesInFieldData;

	/**
	 * Instantiates a new record field info.
	 * 
	 * @param sizeOfFieldName
	 *            The number of bytes in the field name
	 * @param fieldName
	 *            The name of the field
	 * @param sizeOfFieldData
	 *            The number of bytes in the record for this field
	 */
	public RecordFieldInfo(final int sizeOfFieldName, final String fieldName,
			final int sizeOfFieldData) {
		this.bytesInFieldName = sizeOfFieldName;
		this.fieldName = fieldName;
		this.bytesInFieldData = sizeOfFieldData;
	}

	/**
	 * Converts a <code>byte[]</code> containing fieldName to a
	 * <code>String</code> and instantiates a new <code>RecordFieldInfo</code>.
	 * 
	 * @param sizeOfFieldName
	 *            The number of bytes in the field name
	 * @param fieldName
	 *            The name of the field
	 * @param sizeOfFieldData
	 *            The number of bytes in the record for this field
	 * @throws UnsupportedEncodingException
	 *             Signals that the encoding used is no supported
	 */
	public RecordFieldInfo(final int sizeOfFieldName, final byte[] fieldName,
			final int sizeOfFieldData) throws UnsupportedEncodingException {
		this(sizeOfFieldName, new String(fieldName, "US-ASCII"),
				sizeOfFieldData);
	}

	/**
	 * Gets the size of the field name in bytes.
	 * 
	 * @return the size of the field name in bytes
	 */
	public int getBytesInFieldName() {
		return this.bytesInFieldName;
	}

	/**
	 * Sets the size of the field name in bytes.
	 * 
	 * @param sizeOfFieldName
	 *            the size of the field name in bytes
	 */
	public void setBytesInFieldName(final int sizeOfFieldName) {
		this.bytesInFieldName = sizeOfFieldName;
	}

	/**
	 * Gets the field name.
	 * 
	 * @return the name of the field
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * Sets the field name.
	 * 
	 * @param fieldName
	 *            the name of the field
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Gets the size of the field in bytes.
	 * 
	 * @return the size of the field in bytes
	 */
	public int getBytesInField() {
		return this.bytesInFieldData;
	}

	/**
	 * Sets the size of the field in bytes.
	 * 
	 * @param sizeOfFieldData
	 *            the size of the field in bytes
	 */
	public void setBytesInField(final int sizeOfFieldData) {
		this.bytesInFieldData = sizeOfFieldData;
	}
}
