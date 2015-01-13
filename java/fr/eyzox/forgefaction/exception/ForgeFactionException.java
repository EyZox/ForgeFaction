package fr.eyzox.forgefaction.exception;

public abstract class ForgeFactionException extends Exception {

	public ForgeFactionException() {
		super();
	}

	public ForgeFactionException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ForgeFactionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ForgeFactionException(String arg0) {
		super(arg0);
	}

	public ForgeFactionException(Throwable arg0) {
		super(arg0);
	}

}
