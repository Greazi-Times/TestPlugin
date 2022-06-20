package com.greazi.plugin.debug;


import lombok.Getter;
import lombok.Setter;

/**
 * Represents our core exception. All exceptions of this
 * kind are logged automatically to the error.log file
 */
public class FoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Should we save thrown exceptions to error.log file automatically when they are thrown?
	 */
	@Getter
	@Setter
	private static boolean errorSavedAutomatically = true;

	/**
	 * Create a new exception and logs it
	 *
	 * @param t
	 */
	public FoException(final Throwable t) {
		super(t);
	}

	/**
	 * Create a new exception and logs it
	 *
	 * @param message
	 */
	public FoException(final String message) {
		super(message);
	}

	/**
	 * Create a new exception and logs it
	 *
	 * @param message
	 * @param t
	 */
	public FoException(final String message, final Throwable t) {
		this(t, message);
	}

	/**
	 * Create a new exception and logs it
	 *
	 * @param message
	 * @param t
	 */
	public FoException(final Throwable t, final String message) {
		super(message, t);
	}

	@Override
	public String getMessage() {
		return "Report: " + super.getMessage();
	}
}