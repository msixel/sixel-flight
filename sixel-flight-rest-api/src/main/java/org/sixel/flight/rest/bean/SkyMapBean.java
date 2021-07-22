package org.sixel.flight.rest.bean;

import java.io.File;
import java.util.Set;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.rest.service.RouteManagerServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cache in-memory / data struct loaded from CSV
 * 
 * @author marcos
 */
@Component
public class SkyMapBean {

	/**
	 * Bean wrapper of core manager
	 */
	private final RouteManagerServiceBean routeManagerServiceBean;
	
	/**
	 * CSV file received from command line and being used
	 */
	private File csvFile;
	
	/**
	 * Data struct loaded from CVS
	 */
	private Set<Airport> airports;

	/**
	 * @param routeManagerServiceBean
	 */
	@Autowired
	public SkyMapBean (final RouteManagerServiceBean routeManagerServiceBean) {
		this.routeManagerServiceBean = routeManagerServiceBean;
	}
	
	/**
	 * @return
	 */
	public File getCsvFile() {
		return this.csvFile;
	}
	
	/**
	 * @return
	 */
	public Set<Airport> getAirports() {
		return this.airports;
	}
	
	/**
	 * @param csvFile
	 * @throws CoreException
	 */
	public void load(final File csvFile) throws CoreException {
		this.csvFile = csvFile;
		this.airports = routeManagerServiceBean.loadFile(csvFile);
	}
}
