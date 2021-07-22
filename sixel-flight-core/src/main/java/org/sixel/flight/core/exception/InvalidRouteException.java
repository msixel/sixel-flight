/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class InvalidRouteException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRouteException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRouteException(String message) {
		super(message);
	}

	public InvalidRouteException(Throwable cause) {
		super(cause);
	}
	
}
