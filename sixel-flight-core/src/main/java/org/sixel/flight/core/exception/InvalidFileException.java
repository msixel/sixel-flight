/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class InvalidFileException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFileException(String message) {
		super(message);
	}

	public InvalidFileException(Throwable cause) {
		super(cause);
	}
	
}
