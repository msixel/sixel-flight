/**
 * 
 */
package org.sixel.flight.core.builder;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.exception.CoreException;

/**
 * @author marcos
 *
 */
public final class AirportBuilder {
	
	private final Airport draft;

	/**
	 * Constructor
	 */
	public AirportBuilder() {
		draft = new Airport();
	}
	/**
	 * @param code the code to set
	 */
	public AirportBuilder identifiedBy(String code) {
		draft.setCode(code);
		return this;
	}

	/**
	 * @return copied object 
	 * @throws CoreException 
	 */
	public Airport build() {
		return new Airport(draft);
	}
	
}
