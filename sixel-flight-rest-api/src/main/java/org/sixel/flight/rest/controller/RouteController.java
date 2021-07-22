package org.sixel.flight.rest.controller;

import java.util.List;

import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.rest.bean.SkyMapBean;
import org.sixel.flight.rest.mapper.RouteMapperBean;
import org.sixel.flight.rest.service.RouteManagerServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Rest controllers
 * 
 * @author marcos
 *
 */
@RestController
@RequestMapping(value = "/routes" )
public class RouteController {
	
	/**
	 * Bean wrapper of core manager
	 */
	private final RouteManagerServiceBean routeManagerServiceBean;
	
	/**
	 * Data struct loaded from CSV
	 */
	private final SkyMapBean skyMapData;
	
	/**
	 * Mapper to Route objects 
	 */
	private final RouteMapperBean routeMapperBean;
	
	/**
	 * @param skyMapData
	 * @param routeManagerServiceBean
	 * @param routeMapperBean
	 */
	@Autowired
	public RouteController(final SkyMapBean skyMapData, final RouteManagerServiceBean routeManagerServiceBean, final RouteMapperBean routeMapperBean) {
		this.skyMapData = skyMapData;
		this.routeManagerServiceBean = routeManagerServiceBean;
		this.routeMapperBean = routeMapperBean;
	}
	
	/**
	 * @param fly
	 * @throws CoreException
	 */
	@PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addFly(@RequestBody final Fly fly) throws CoreException {
		routeManagerServiceBean.persist(skyMapData.getCsvFile(), fly);
	}
	
	/**
	 * @throws CoreException
	 */
	@GetMapping(value = "/{routeRaw}", produces = "application/json")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public @ResponseBody List<Fly> findFlies(@PathVariable final String routeRaw) throws CoreException {
		final Route route = routeMapperBean.from(routeRaw);
		return routeManagerServiceBean.findFlies(skyMapData.getAirports(), route);
	}
	
	/**
	 * @throws CoreException
	 */
	@GetMapping(value = "/{routeRaw}/best", produces = "application/json")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public @ResponseBody Fly findBestFly(@PathVariable final String routeRaw) throws CoreException {
		final Route route = routeMapperBean.from(routeRaw);
		return routeManagerServiceBean.findBestFly(skyMapData.getAirports(), route);
	}
	
}
