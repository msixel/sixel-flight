/**
 * 
 */
package org.sixel.flight.core.builder;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidRouteException;

/**
 * @author marcos
 *
 */
public final class RouteBuilder {
		
	private final Route draft;

	/**
	 * Constructor
	 */
	public RouteBuilder() {
		draft = new Route();
	}

	/**
	 * @param departureAirportCode
	 * @return
	 */
	public RouteBuilder from(String departureAirportCode) {
		return from(new AirportBuilder().identifiedBy(departureAirportCode).build());
	}

	/**
	 * @param from the departure airport to set
	 */
	public RouteBuilder from(Airport from) {
		draft.setFrom(from);
		return this;
	}
	
	/**
	 * @param arrivelAirportCode the arrivel to set
	 */
	public RouteBuilder to(String arrivelAirportCode) {
		return to(new AirportBuilder().identifiedBy(arrivelAirportCode).build());
	}
	
	/**
	 * @param to the arrival airport to set
	 */
	public RouteBuilder to(Airport to) {
		draft.setTo(to);
		return this;
	}
	
	/**
	 * @return copied object
	 * @throws CoreException 
	 */
	public Route build() throws InvalidRouteException {
		if (draft.getFrom().equals(draft.getTo())) {
			throw new InvalidRouteException(draft.toString());
		}
		return new Route(draft);
	}
	
}
