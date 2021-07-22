package org.sixel.flight.rest;

import java.io.File;

import org.sixel.flight.rest.bean.SkyMapBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Spring App entry point
 * 
 * @author marcos
 */
@Component
public class App {

	private static final String DEFAULT_CSV_FILE = "input-routes.csv";
	
	/**
	 * Bean in-memory data repository
	 */
	private final SkyMapBean skyMapData;
	
	/**
	 * @param context
	 * @param skyMapData
	 */
	@Autowired
	public App (final SkyMapBean skyMapData) {
		this.skyMapData = skyMapData;
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			final File csvFile;
			if (args.length==0) {
				csvFile = new File(DEFAULT_CSV_FILE);
			} else {
				csvFile = new File(args[0]);
			}
			skyMapData.load(csvFile);
		};
	}
}
