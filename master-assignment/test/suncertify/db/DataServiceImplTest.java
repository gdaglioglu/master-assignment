package suncertify.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.app.util.PropertyManager;
import suncertify.domain.HotelRoom;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;

public class DataServiceImplTest {

	private DataService dataService;

	private static final int CREATE_REC_NO = 0;
	private static final int UPDATE_REC_NO = 14;
	private static final int READ_REC_NO = 15;
	private static final int DELETE_REC_NO = 16;

	@BeforeClass
	public static void setupClass() {
		PropertyManager.setParameter(PropertyManager.DATABASE_LOCATION, DataTest.DATABASE_FILE);
	}

	@Before
	public void setup() {
		this.dataService = new DataServiceImpl();
	}

	@Test
	public void testRead() throws RemoteException, RecordNotFoundException {
		final HotelRoom record = this.dataService.read(READ_REC_NO);
		assertThat(record.getName(), is(equalTo("Grandview")));
		assertThat(record.getLocation(), is(equalTo("EmeraldCity")));
		assertThat(record.getSize(), is(equalTo("2")));
		assertThat(record.getSmoking(), is(equalTo("Y")));
		assertThat(record.getRate(), is(equalTo("$160.00")));
		assertThat(record.getDate(), is(equalTo("2003/01/28")));
		assertThat(record.getOwner(), is(equalTo("")));
	}

	@Test
	public void testUpdate() throws RemoteException, RecordNotFoundException {
		final HotelRoom origRecord = this.dataService.read(UPDATE_REC_NO);
		assertThat(origRecord.getName(), is(equalTo("Excelsior")));
		assertThat(origRecord.getLocation(), is(equalTo("EmeraldCity")));
		assertThat(origRecord.getSize(), is(equalTo("6")));
		assertThat(origRecord.getSmoking(), is(equalTo("N")));
		assertThat(origRecord.getRate(), is(equalTo("$160.00")));
		assertThat(origRecord.getDate(), is(equalTo("2005/01/14")));
		assertThat(origRecord.getOwner(), is(equalTo("")));

		final HotelRoom newRecordData = new HotelRoom("Grandview2", "Atlantis2", "7", "Y",
				"$201.00", "2004/12/11", "12345678");
		this.dataService.update(UPDATE_REC_NO, newRecordData);
		final HotelRoom updatedRecord = this.dataService.read(UPDATE_REC_NO);
		for (int i = 0; i < newRecordData.toArray().length; i++) {
			assertThat(updatedRecord.toArray()[i], is(equalTo(newRecordData.toArray()[i])));
		}

		this.dataService.update(UPDATE_REC_NO, origRecord);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDelete() throws RemoteException, RecordNotFoundException, DuplicateKeyException {
		this.dataService.delete(DELETE_REC_NO);
		this.dataService.read(DELETE_REC_NO);
	}

	@Test
	public void testFind() throws RemoteException, RecordNotFoundException {
		String[] findCriteria = new String[] { "Hamner" };
		List<HotelRoom> records = this.dataService.find(findCriteria, true);
		assertThat(records.size(), is(equalTo(0)));

		findCriteria = new String[] { "Dew Drop Inn" };
		records = this.dataService.find(findCriteria, true);
		assertThat(records.size(), is(equalTo(3)));
	}

	@Test
	public void testCreate() throws RemoteException, DuplicateKeyException, RecordNotFoundException {
		final String[] newRecord = new String[] { "Dew Drop Inn", "Smallville", "4", "Y",
				"$210.00", "2005/05/02", "        " };
		this.dataService.delete(CREATE_REC_NO);
		final int newRecordNum = this.dataService.create(newRecord);

		assertThat(newRecordNum, is(equalTo(CREATE_REC_NO)));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreateDup() throws RemoteException, DuplicateKeyException {
		final String[] newRecord = new String[] { "DUPPLICATE", "DUPPLICATE", "DUPPLICATE",
				"DUPPLICATE", "DUPPLICATE", "DUPPLICATE", "DUPPLICATE" };
		this.dataService.create(newRecord);
		this.dataService.create(newRecord);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateEmpty() throws RemoteException, DuplicateKeyException {
		final String[] newRecord = new String[] {};
		this.dataService.create(newRecord);
	}
}