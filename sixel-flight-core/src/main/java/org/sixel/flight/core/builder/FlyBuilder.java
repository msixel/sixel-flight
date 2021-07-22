/**
 * 
 */
package org.sixel.flight.core.builder;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.exception.InvalidRouteException;

/**
 * @author marcos
 *
 */
public final class FlyBuilder {
		
	private final Fly draft;
	private final RouteBuilder routeBuilder;

	/**
	 * Constructor
	 */
	public FlyBuilder() {
		draft = new Fly();
		routeBuilder = new RouteBuilder();
	}

	/**
	 * @param departureAirportCode
	 * @return
	 */
	public FlyBuilder from(String departureAirportCode) {
		return from(new AirportBuilder().identifiedBy(departureAirportCode).build());
	}

	/**
	 * @param departureAirport
	 * @return
	 */
	public FlyBuilder from(Airport departureAirport) {
		routeBuilder.from(departureAirport);
		draft.setRoute(null);
		return this;
	}
	
	/**
	 * @param arrivelAirportCode the arrivel to set
	 */
	public FlyBuilder to(String arrivelAirportCode) {
		return to(new AirportBuilder().identifiedBy(arrivelAirportCode).build());
	}
	
	/**
	 * @param arrivelAirport the arrivel to set
	 */
	public FlyBuilder to(Airport arrivelAirport) {
		routeBuilder.to(arrivelAirport);
		draft.setRoute(null);
		return this;
	}
	
	/**
	 * @param price the price to set
	 */
	public FlyBuilder atPrice(Long price) {
		draft.setPrice(price);
		return this;
	}
	
	/**
	 * @return copied object
	 * @throws InvalidRouteException 
	 */
	public Fly build() throws InvalidRouteException {
		if (draft.getRoute()==null)
			draft.setRoute(routeBuilder.build());
		return new Fly(draft);
	}
	
}
