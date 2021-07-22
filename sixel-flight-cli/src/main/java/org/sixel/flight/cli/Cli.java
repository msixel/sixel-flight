package org.sixel.flight.cli;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidRouteException;
import org.sixel.flight.core.exception.UnknowAirportException;
import org.sixel.flight.core.mapper.FlyMapper;
import org.sixel.flight.core.mapper.RouteMapper;
import org.sixel.flight.core.service.RouteManagerService;
import org.sixel.flight.core.service.RouteManagerServiceImpl;

/**
 * @author marcos
 *
 */
public class Cli {

	private static final String LOG_BASE_FORMAT = "%s [\"%s\"]";
	
	/**
	 * Message file entries
	 */
	private static final String MESSAGE_ROUTE_INPUT = "message.route.input";
	private static final String MESSAGE_ROUTE_INVALID = "message.route.invalid";
	private static final String MESSAGE_UNKNOWN_AIRPORT = "message.route.unknown.airport";
	private static final String MESSAGE_ROUTE_NOTFOUND = "message.route.notfound";
	private static final String MESSAGE_ROUTE_BEST = "message.route.best";
	
	private static final List<String> EXIT_COMMANDS = Arrays.asList("EXIT", "exit", "QUIT", "quit", "END", "end");

	
	/**
	 * Core service point
	 */
	private static final RouteManagerService ROUTE_MANAGER_SERVICE = RouteManagerServiceImpl.getInstance();
	
	/**
	 * Mapper/Translator
	 */
	private static final RouteMapper ROUTE_MAPPER = RouteMapper.getInstance();
	private static final FlyMapper FLY_MAPPER = FlyMapper.getInstance();
	
	/**
	 * Whole flying map
	 */
	private final Set<Airport> airports;
	
	/**
	 * Utilities
	 */
	private final Scanner scanner;
	private final PrintWriter printWriter;
	
	/**
	 * Constructor 
	 * 
	 * @param airports
	 */
	Cli(final Set<Airport> airports) {
		this.airports = airports;
		this.scanner = new Scanner(System.in);
		this.printWriter = System.console().writer();
	}
	
	
	/**
	 * Read the route from standard input
	 * @return
	 */
	private String readRoute() {
		printWriter.print(Messages.getString(MESSAGE_ROUTE_INPUT));
		printWriter.flush();
		return scanner.nextLine();
	}
	
	/**
	 * @param bestFly
	 */
	private void printFly (final Fly fly){
		if (fly == null) {
			printWriter.println(Messages.getString(MESSAGE_ROUTE_NOTFOUND));
		} else {
			printWriter.print(Messages.getString(MESSAGE_ROUTE_BEST));
			printWriter.println(FLY_MAPPER.to(fly));
		}
		printWriter.flush();
	}
	
	/**
	 * @throws CoreException 
	 */
	public void run() throws CoreException {
		String line;
		
		line = readRoute();
		while (!line.isEmpty() && !EXIT_COMMANDS.contains(line) ) {
			final Fly bestFly;
			final Route route;
			
			try {
				route = ROUTE_MAPPER.from(line);
				bestFly = ROUTE_MANAGER_SERVICE.findBestFly(airports, route);
				printFly(bestFly);
				
			} catch (final InvalidRouteException ire) {
				printWriter.println(
						String.format(LOG_BASE_FORMAT,
								Messages.getString(MESSAGE_ROUTE_INVALID),
								ire.getMessage()));
				
			} catch (final UnknowAirportException uae) {
				printWriter.println(
						String.format(LOG_BASE_FORMAT,
								Messages.getString(MESSAGE_UNKNOWN_AIRPORT),
								uae.getMessage()));
			}
			line = readRoute();
		}
	}
}
