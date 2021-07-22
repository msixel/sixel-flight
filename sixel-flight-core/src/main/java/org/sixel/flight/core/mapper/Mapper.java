package org.sixel.flight.core.mapper;

import org.sixel.flight.core.exception.CoreException;

/**
 * @author marcos
 *
 * @param <T>
 * @param <R>
 */
public interface Mapper<T, R> {

	/**
	 * @param raw
	 * @return
	 * @throws CoreException 
	 */
	T from(R raw) throws CoreException;
	
	/**
	 * @param type
	 * @return
	 * @throws CoreException 
	 */
	R to(T type) throws CoreException;
}
