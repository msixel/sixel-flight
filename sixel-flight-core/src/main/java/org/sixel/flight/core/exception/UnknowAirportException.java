/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class UnknowAirportException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknowAirportException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknowAirportException(String message) {
		super(message);
	}

	public UnknowAirportException(Throwable cause) {
		super(cause);
	}
	
}
