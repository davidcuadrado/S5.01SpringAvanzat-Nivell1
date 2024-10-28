package cat.itacademy.s05.t01.n01.exceptions;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
        super(message);
    }
}
