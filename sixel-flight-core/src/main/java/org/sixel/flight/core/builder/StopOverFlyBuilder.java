/**
 * 
 */
package org.sixel.flight.core.builder;

import java.util.Collection;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.StopOverFly;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidRouteException;

/**
 * @author marcos
 *
 */
public final class StopOverFlyBuilder {
		
	private final StopOverFly draft;
	private final RouteBuilder routeBuilder;

	/**
	 * Constructor
	 */
	public StopOverFlyBuilder() {
		draft = new StopOverFly();
		routeBuilder = new RouteBuilder();
	}

	/**
	 * @param departureAirportCode
	 * @return
	 * @throws CoreException 
	 */
	public StopOverFlyBuilder from(final String departureAirportCode) throws CoreException {
		return from(new AirportBuilder().identifiedBy(departureAirportCode).build());
	}

	/**
	 * @param departureAirport
	 * @return
	 */
	public StopOverFlyBuilder from(final Airport departureAirport) {
		routeBuilder.from(departureAirport);
		draft.setRoute(null);
		return this;
	}
	
	/**
	 * @param arrivelAirportCode the arrivelAirportCode to set
	 * @throws CoreException 
	 */
	public StopOverFlyBuilder to(final String arrivelAirportCode) throws CoreException {
		return to(new AirportBuilder().identifiedBy(arrivelAirportCode).build());
	}
	
	/**
	 * @param arrivelAirport the arrivelAirport to set
	 * @throws CoreException 
	 */
	public StopOverFlyBuilder to(final Airport arrivelAirport) {
		routeBuilder.to(arrivelAirport);
		draft.setRoute(null);
		return this;
	}
	
	/**
	 * @param nonStopFlies
	 * @return
	 * @throws InvalidRouteException 
	 */
	public StopOverFlyBuilder composedBy(final Collection<Fly> nonStopFlies) throws InvalidRouteException {
		for (final Fly nonStopFly: nonStopFlies) {
			draft.addNonStopFly(new FlyBuilder()
					.from(nonStopFly.getRoute().getFrom().getCode())
					.to(nonStopFly.getRoute().getTo().getCode())
					.atPrice(nonStopFly.getPrice())
					.build());
		}
		return this;
	}
	
	/**
	 * @return copied object
	 * @throws InvalidRouteException 
	 */
	public StopOverFly build() throws InvalidRouteException {
		if (draft.getRoute()==null)
			draft.setRoute(routeBuilder.build());
		return new StopOverFly(draft);
	}
	
}
