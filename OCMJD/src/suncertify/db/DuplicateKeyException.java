package suncertify.db;

public class DuplicateKeyException extends Exception {

	private static final long serialVersionUID = -6689165809485807888L;

	public DuplicateKeyException() {
		super();
	}

	public DuplicateKeyException(String s) {
		super(s);
	}
}
