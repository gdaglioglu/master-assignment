package suncertify.db;

public class RecordNotFoundException extends Exception {

	// TODO serialVersionUID
	private static final long serialVersionUID = 9172845648588845215L;

	public RecordNotFoundException() {
		super();
	}

	public RecordNotFoundException(String s) {
		super(s);
	}
}
