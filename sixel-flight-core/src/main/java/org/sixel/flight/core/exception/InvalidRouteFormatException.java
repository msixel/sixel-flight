/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class InvalidRouteFormatException extends InvalidRouteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRouteFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRouteFormatException(String message) {
		super(message);
	}

	public InvalidRouteFormatException(Throwable cause) {
		super(cause);
	}
	
}
