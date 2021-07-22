/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class InvalidFileFormatException extends InvalidFileException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFileFormatException(String message) {
		super(message);
	}

	public InvalidFileFormatException(Throwable cause) {
		super(cause);
	}
	
}
