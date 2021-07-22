/**
 * 
 */
package org.sixel.flight.core.service;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sixel.flight.core.builder.AirportBuilder;
import org.sixel.flight.core.builder.FlyBuilder;
import org.sixel.flight.core.builder.RouteBuilder;
import org.sixel.flight.core.builder.StopOverFlyBuilder;
import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidRouteException;
import org.sixel.flight.core.exception.UnknowAirportException;
import org.sixel.flight.core.persistence.RouteDAO;
import org.sixel.flight.core.persistence.RouteDAOImpl;

/**
 * @author marcos
 *
 */
public final class RouteManagerServiceImpl implements RouteManagerService {
	
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(RouteManagerServiceImpl.class.getName()); 
	
	/**
	 * Singleton instance
	 */
	private static final RouteManagerServiceImpl SINGLE_INSTANCE = new RouteManagerServiceImpl();
	
	/**
	 * Dao component 
	 */
	private static final RouteDAO ROUTE_DAO = RouteDAOImpl.getInstance();

	
	/**
	 * @return
	 */
	public static RouteManagerServiceImpl getInstance() {
		return SINGLE_INSTANCE;
	}
	
	/**
	 * Singleton constructor
	 */
	private RouteManagerServiceImpl () {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Airport> loadFile(String csvFile) throws CoreException {
		return loadFile(new File(csvFile));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Airport> loadFile(final File csvFile) throws CoreException {
		final Set<Airport> airports;
		
		airports = new LinkedHashSet<>();
		for (final Fly fly: ROUTE_DAO.findAll(csvFile)) {
			final Airport departureAirport = fly.getRoute().getFrom();
			final Airport arrivalAirport = fly.getRoute().getTo();
			
			final Optional<Airport> departureAirportOpt;
			final Optional<Airport> arrivalAirportOpt;

			departureAirportOpt=airports.stream().filter(a -> a.equals(departureAirport)).findFirst();
			if (departureAirportOpt.isPresent()) {
				fly.getRoute().setFrom(departureAirportOpt.get());
				departureAirportOpt.get().getDepartures().add(fly);
			} else {
				fly.getRoute().setFrom(departureAirport);
				departureAirport.getDepartures().add(fly);
				airports.add(departureAirport);
			}
			
			arrivalAirportOpt=airports.stream().filter(a -> a.equals(arrivalAirport)).findFirst();
			if (arrivalAirportOpt.isPresent()) {
				fly.getRoute().setTo(arrivalAirportOpt.get());
			} else {
				fly.getRoute().setTo(arrivalAirport);
				airports.add(arrivalAirport);
			}			
		}
		
		LOGGER.info("CVS File load successful");
		return Collections.unmodifiableSet(airports);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fly findBestFly(Set<Airport> airports, Route route) throws CoreException {
		final List<Fly> flies = findFlies(airports, route);
		final Fly firstFly;
		if (flies.isEmpty()) return null;
		
		firstFly = flies.iterator().next();
		return flies.stream().reduce(firstFly, (f1, f2) -> f1.getPrice()<f2.getPrice()?f1:f2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Fly> findFlies(final Set<Airport> airports, final Route route) throws CoreException {
		final List<Fly> foundFlies = new LinkedList<>();
		final Route linkedRoute;
		final Airport airportRootPath;
		
		if (route.getFrom().equals(route.getTo())) {
			throw new InvalidRouteException(route.toString());
		}
		
		linkedRoute = getLinkedRoute(airports, route);
		airportRootPath = findPaths(linkedRoute.getFrom(), linkedRoute.getTo());
		
		if (airportRootPath != null) {
			for (final Fly fly: airportRootPath.getDepartures()) {
				if (fly.isGoingTo(linkedRoute.getTo())) {
					foundFlies.add(
							new FlyBuilder()
								.from(fly.getRoute().getFrom().getCode())
								.to(fly.getRoute().getTo().getCode())
								.atPrice(fly.getPrice())
								.build());
				} else {
					final Queue<Fly> nonStopFliesLifoStack = Collections.asLifoQueue(new LinkedList<Fly>());
					nonStopFliesLifoStack.add(fly);
					extractStopOverFlies(route, fly.getRoute().getTo(), nonStopFliesLifoStack, foundFlies);
					nonStopFliesLifoStack.poll();
				}
			}
		}
		return foundFlies;
	}
	
	/**
	 * @param route
	 * @param airportPtr
	 * @param buffer
	 * @return
	 * @throws CoreException 
	 */
	public List<Fly> extractStopOverFlies(final Route routeArg, final Airport airportPathCopy, final Queue<Fly> nonStopFliesLifoStack, final List<Fly> stopOverFliesFound) throws CoreException {
		if (airportPathCopy.equals(routeArg.getTo())) {
			final List<Fly> nonStopFliesList = new LinkedList<>(nonStopFliesLifoStack);
			Collections.reverse(nonStopFliesList);
			stopOverFliesFound.add(
				new StopOverFlyBuilder()
					.from(routeArg.getFrom())
					.to(routeArg.getTo())
					.composedBy(nonStopFliesList)
					.build());
			LOGGER.log(Level.FINE, "found final airport {0}, building compound StopOverFly", airportPathCopy.getCode());
		} else {
			for (final Fly fly: airportPathCopy.getDepartures()) {
				nonStopFliesLifoStack.add(fly);
				extractStopOverFlies(routeArg, fly.getRoute().getTo(), nonStopFliesLifoStack, stopOverFliesFound);
				nonStopFliesLifoStack.poll();
			}	
		}
		return stopOverFliesFound;

	}
	
	/**
	 * @param airportPtr
	 * @param airportTarget
	 * @return
	 * @throws CoreException 
	 */
	public Airport findPaths(final Airport airportPtr, final Airport airportTarget) throws CoreException {
		final Airport airportPathCopy = new AirportBuilder().identifiedBy(airportPtr.getCode()).build();
		
		if (airportPtr.equals(airportTarget)) {
			LOGGER.log(Level.FINE, "found final path airport {0}", airportPtr.getCode());
			return airportPathCopy;
		}
		
		//TODO have i already walking through this airport (cyclic reference)? (need to implemente reverse linked list - attribute "arrivals" - to check)
		
		for (final Fly fly: airportPtr.getDepartures()) {
			final Airport airportNextClean = findPaths(fly.getRoute().getTo(), airportTarget);
			if (airportNextClean!=null) {
				//valid path
				airportPathCopy.addDeparture(
						new FlyBuilder().from(airportPathCopy).to(airportNextClean).atPrice(fly.getPrice()).build());
			}
		}
		
		if (airportPathCopy.hasDepartures().booleanValue()) {
			return airportPathCopy;
		}
		return null;
	}
	
	/**
	 * @param airports
	 * @param route
	 * @return
	 * @throws CoreException 
	 */
	private Route getLinkedRoute(final Set<Airport> airports, final Route route) throws CoreException {
		final Optional<Airport> departureAirportOpt;
		final Optional<Airport> arrivalAirportOpt;
		
		if (airports==null || airports.isEmpty()) {
			throw new IllegalArgumentException("airports");
		}
		if (route==null || !route.isFulFilled()) {
			throw new IllegalArgumentException("route");
		}
		
		departureAirportOpt=airports.stream().filter(a -> a.equals(route.getFrom())).findFirst();
		arrivalAirportOpt=airports.stream().filter(a -> a.equals(route.getTo())).findFirst();

		if (departureAirportOpt.isEmpty()) {
			throw new UnknowAirportException(route.getFrom().getCode());
		}
		
		if (arrivalAirportOpt.isEmpty()) {
			throw new UnknowAirportException(route.getTo().getCode());
		}
	
		return new RouteBuilder().from(departureAirportOpt.get()).to(arrivalAirportOpt.get()).build();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist(final File csvFile, Fly fly) throws CoreException {
		ROUTE_DAO.persist(csvFile, fly);
		LOGGER.info("Fly persistence successful");	
	}

}
