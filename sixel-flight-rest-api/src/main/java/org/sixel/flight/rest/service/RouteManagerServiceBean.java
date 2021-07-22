package org.sixel.flight.rest.service;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.service.RouteManagerService;
import org.sixel.flight.core.service.RouteManagerServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Adapter bean encapsulating core route manager
 * 
 * @author marcos
 *
 */
@Service
public class RouteManagerServiceBean implements RouteManagerService {
	
	/**
	 * wrapped non-spring component
	 */
	private static final RouteManagerService WRAPPED = RouteManagerServiceImpl.getInstance();

	/**
	 *
	 */
	@Override
	public Set<Airport> loadFile(File csvFile) throws CoreException {
		return WRAPPED.loadFile(csvFile);
	}

	/**
	 *
	 */
	@Override
	public Set<Airport> loadFile(String csvFile) throws CoreException {
		return WRAPPED.loadFile(csvFile);
	}

	/**
	 *
	 */
	@Override
	public List<Fly> findFlies(Set<Airport> airports, Route route) throws CoreException {
		return WRAPPED.findFlies(airports, route);
	}

	/**
	 *
	 */
	@Override
	public Fly findBestFly(Set<Airport> airports, Route route) throws CoreException {
		return WRAPPED.findBestFly(airports, route);
	}

	/**
	 *
	 */
	@Override
	public void persist(File csvFile, Fly fly) throws CoreException {
		WRAPPED.persist(csvFile, fly);
	}

}
