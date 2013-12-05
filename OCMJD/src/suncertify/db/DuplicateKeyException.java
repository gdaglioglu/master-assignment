package suncertify.db;

public class DuplicateKeyException extends Exception {

	public DuplicateKeyException() {
		super();
	}

	public DuplicateKeyException(String s) {
		super(s);
	}
}
