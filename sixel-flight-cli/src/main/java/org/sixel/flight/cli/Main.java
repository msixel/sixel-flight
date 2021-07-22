/**
 * 
 */
package org.sixel.flight.cli;

import java.util.logging.Logger;

import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.service.RouteManagerService;
import org.sixel.flight.core.service.RouteManagerServiceImpl;

/**
 * @author marcos
 *
 */
public class Main {
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName()); 
	
	/**
	 * Message file entry
	 */
	private static final String MESSAGE_ARGUMENT_CSVFILE_INVALID = "message.argument.csvfile.invalid";
	
	/**
	 * Core service point
	 */
	private static final RouteManagerService ROUTE_MANAGER_SERVICE = RouteManagerServiceImpl.getInstance();
	
	/**
	 * @param args
	 * @throws CoreException 
	 */
	public static void main(String[] args) throws CoreException {
		if (args.length!=1) {
			LOGGER.severe(Messages.getString(MESSAGE_ARGUMENT_CSVFILE_INVALID));
			System.exit(1);
		}
		
		final Cli cli = new Cli(
				ROUTE_MANAGER_SERVICE.loadFile(args[0]));
		cli.run();
	}

}
