package org.sixel.flight.core.service;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;

/**
 * @author marcos
 *
 */
public interface RouteManagerService {

	/**
	 * Load fly network from CSV file
	 * 
	 * @param csvFile
	 * @return
	 * @throws CoreException
	 */
	public Set<Airport> loadFile(final File csvFile) throws CoreException;

	/**
	 * Load fly network from CSV file - Convenience
	 * 
	 * @param csvFile
	 * @return
	 * @throws CoreException
	 */
	public Set<Airport> loadFile(final String csvFile) throws CoreException;
	
	/**
	 * @param airports
	 * @param route
	 * @return flies for this route
	 * @throws CoreException 
	 */
	public List<Fly> findFlies(final Set<Airport> airports, final Route route) throws CoreException;
	
	/**
	 * @param airports
	 * @param route
	 * @return cheaper fly for this route or null
	 * @throws CoreException 
	 */
	public Fly findBestFly(final Set<Airport> airports, final Route route) throws CoreException;
	
	/**
	 * Store a non stop fly into csv file
	 * 
	 * @param csvFile
	 * @param fly
	 * @throws CoreException 
	 */
	public void persist(final File csvFile, final Fly fly) throws CoreException;
	
}