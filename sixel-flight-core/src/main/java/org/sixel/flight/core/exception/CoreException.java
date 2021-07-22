/**
 * 
 */
package org.sixel.flight.core.exception;

/**
 * @author marcos
 *
 */
public class CoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public CoreException(String message) {
		super(message);
	}

	public CoreException(Throwable cause) {
		super(cause);
	}
	
}
