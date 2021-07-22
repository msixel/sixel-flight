/**
 * 
 */
package org.sixel.flight.rest.mapper;

import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.InvalidRouteException;
import org.sixel.flight.core.exception.InvalidRouteFormatException;
import org.sixel.flight.core.mapper.Mapper;
import org.sixel.flight.core.mapper.RouteMapper;
import org.springframework.stereotype.Component;

/**
 * Adapter bean encapsulating core mapper
 * 
 * @author marcos
 *
 */
@Component
public final class RouteMapperBean implements Mapper<Route, String> {
	
	/**
	 * wrapped non-spring component
	 */
	private static final RouteMapper WRAPPED = RouteMapper.getInstance();
	
	/**
	 *{@inheritDoc}
	 * @throws InvalidRouteFormatException 
	 * @throws InvalidRouteException 
	 */
	@Override
	public Route from(final String line) throws InvalidRouteFormatException, InvalidRouteException {
		return WRAPPED.from(line);
	}

	/**
	 *{@inheritDoc}
	 * @throws InvalidRouteException 
	 */
	@Override
	public String to(final Route route) throws InvalidRouteException {
		return WRAPPED.to(route);
	}

}
