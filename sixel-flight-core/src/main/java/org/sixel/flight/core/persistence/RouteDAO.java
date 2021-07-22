package org.sixel.flight.core.persistence;

import java.io.File;
import java.util.Set;

import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.exception.CoreException;

/**
 * @author marcos
 *
 */
public interface RouteDAO {

	/**
	 * Read non stop flies from CSV file
	 * 
	 * @param csvFile
	 * @return
	 * @throws CoreException
	 */
	Set<Fly> findAll(final File csvFile) throws CoreException;
	
	/**
	 * Save new Fly into CSV file
	 * 
	 * @param csvFile
	 * @param fly
	 * @throws CoreException
	 */
	void persist(final File csvFile, Fly fly) throws CoreException;
		
}