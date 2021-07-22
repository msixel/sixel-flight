/**
 * 
 */
package org.sixel.flight.core.mapper;

import org.sixel.flight.core.builder.RouteBuilder;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.InvalidRouteException;
import org.sixel.flight.core.exception.InvalidRouteFormatException;

/**
 * @author marcos
 *
 */
public final class RouteMapper implements Mapper<Route, String> {
	
	/**
	 * Constants
	 */
	private static final Integer ROUTE_LINE_LENGTH = 7;
	private static final char ROUTE_LINE_DELIMITER = '-';
	/**
	 * Singleton instance
	 */
	private static final RouteMapper SINGLE_INSTANCE = new RouteMapper();
	
	/**
	 * @return
	 */
	public static RouteMapper getInstance() {
		return SINGLE_INSTANCE;
	}
	
	/**
	 * Singleton constructor
	 */
	private RouteMapper () {
	}
	
	/**
	 *{@inheritDoc}
	 * @throws InvalidRouteFormatException 
	 * @throws InvalidRouteException 
	 */
	@Override
	public Route from(final String line) throws InvalidRouteFormatException, InvalidRouteException {
		if (line.length()!=ROUTE_LINE_LENGTH || line.charAt(3)!=ROUTE_LINE_DELIMITER ) {
			throw new InvalidRouteFormatException(line);
		}
		return new RouteBuilder().from(line.substring(0,3)).to(line.substring(4)).build();
	}

	/**
	 *{@inheritDoc}
	 * @throws InvalidRouteException 
	 */
	@Override
	public String to(final Route route) throws InvalidRouteException {
		if (route==null) {
			throw new InvalidRouteException("route");
		}
		if (route.getFrom()==null || route.getFrom().getCode()==null) {
			throw new InvalidRouteException("route.from");
		}
		if (route.getTo()==null || route.getTo().getCode()==null) {
			throw new InvalidRouteException("route.to");
		}
		
		return String.format("%s-%s", route.getFrom().getCode(), route.getTo().getCode());
	}

}
