package suncertify.db.io;

import static suncertify.app.util.App.showErrorAndExit;
import static suncertify.db.io.DBSchema.FIELD_LENGTHS;
import static suncertify.db.io.DBSchema.NUM_BYTES_RECORD_DELETED_FLAG;
import static suncertify.db.io.DBSchema.RECORD_DELETED;
import static suncertify.db.io.DBSchema.RECORD_LENGTH;
import static suncertify.db.io.DBSchema.RECORD_VALID;
import static suncertify.db.io.DBSchema.START_OFFSET;
import static suncertify.db.io.DBSchema.US_ASCII;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is responsible for writing records to the database file.
 * 
 * @author Gokhan Daglioglu
 */
public class DBWriter {

	/**
	 * The <code>RandomAccessFile</code> instance used to read the database
	 * file.
	 */
	private final RandomAccessFile raf;

	/**
	 * The <code>ReentrantLock</code>s used for crud operations towards the
	 * database file.
	 */
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * Create new instance of the database writer.
	 * 
	 * @param raf
	 *            The file which to write to.
	 */
	public DBWriter(final RandomAccessFile raf) {
		this.raf = raf;
	}

	/**
	 * This method writes a record to the database.
	 * 
	 * @param recNo
	 *            The record number of the record to be written.
	 * @param data
	 *            The fields of the record to be written.
	 * @return true if the write succeeded otherwise false.
	 */
	public boolean write(final int recNo, final String[] data) {
		try {
			final int pos = START_OFFSET + RECORD_LENGTH * recNo;
			this.lock.lock();
			this.writeRecord(pos, data);
			this.lock.unlock();
		} catch (final IOException e) {
			showErrorAndExit("Cannot write to database file, changes cannot be persisted.");
		}
		return true;
	}

	/**
	 * Delete a record in the database. This will only mark a record as deleted,
	 * it does not remove the records data from the file.
	 * 
	 * @param recNo
	 *            The record number of the record to be deleted.
	 * @return true if the delete succeeded otherwise false.
	 */
	public boolean delete(final int recNo) {
		final int pos = START_OFFSET + RECORD_LENGTH * recNo;
		try {
			this.lock.lock();
			this.raf.seek(pos);
			this.raf.writeByte(RECORD_DELETED);
			this.lock.unlock();
		} catch (final IOException e) {
			showErrorAndExit("Cannot write to database file, changes cannot be persisted.");
		}
		return true;
	}

	/**
	 * Create a new record in the database. This is reuse a deleted record if it
	 * finds a record marked as deleted.
	 * 
	 * @param data
	 *            The fields of the new record to be created.
	 * @return The record number of the newly created record.
	 */
	public int create(final String[] data) {
		try {
			this.lock.lock();
			while (this.raf.getFilePointer() != this.raf.length()) {
				final long recordPos = this.raf.getFilePointer();
				final int flag = this.raf.readShort();

				if (flag != RECORD_VALID) {
					this.writeRecord(recordPos, data);
					final long recordIndex = (recordPos) / RECORD_LENGTH;
					return (int) recordIndex;
				}

				this.raf.seek((this.raf.getFilePointer() + RECORD_LENGTH)
						- NUM_BYTES_RECORD_DELETED_FLAG);
			}
			this.writeRecord(this.raf.getFilePointer(), data);
			this.lock.unlock();
		} catch (final IOException e) {
			showErrorAndExit("Cannot write to database file.");
		}
		return -1;
	}

	/**
	 * This method does the actually writing of a record to the database file.
	 * 
	 * @param pos
	 *            The position in the file to start the writing.
	 * @param data
	 *            The fields for this record to be written.
	 * @throws IOException
	 *             If the method fails to write to the database file.
	 */
	private void writeRecord(final long pos, final String[] data) throws IOException {
		this.raf.seek(pos);
		// write 1 byte flag to indicate valid record
		this.raf.writeByte(RECORD_VALID);
		for (int i = 0; i < data.length; i++) {
			final byte[] updatedData = Arrays.copyOf(data[i].getBytes(US_ASCII), FIELD_LENGTHS[i]);
			this.raf.write(updatedData);
		}
	}
}