/**
 * 
 */
package org.sixel.flight.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.sixel.flight.core.builder.AirportBuilder;
import org.sixel.flight.core.builder.FlyBuilder;
import org.sixel.flight.core.builder.RouteBuilder;
import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.service.RouteManagerServiceImpl;

/**
 * @author marcos
 *
 */
public class RouteManagerServiceImplTest {
	
	private final RouteManagerServiceImpl routeManagerService;
	
	public RouteManagerServiceImplTest() {
		routeManagerService = RouteManagerServiceImpl.getInstance();
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.service.RouteManagerServiceImpl#loadFile(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test
	public void testLoadFilePositive() throws CoreException {
		final Set<Airport> airports;
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportBrc = new AirportBuilder().identifiedBy("BRC").build();
		final Airport airportScl = new AirportBuilder().identifiedBy("SCL").build();
		final Airport airportCdg = new AirportBuilder().identifiedBy("CDG").build();
		final Airport airportOrl = new AirportBuilder().identifiedBy("ORL").build();
		
		airports = routeManagerService.loadFile(new File("test.csv"));
		
		assertEquals(5, airports.size());
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportBrc));
		assertTrue(airports.contains(airportScl));
		assertTrue(airports.contains(airportCdg));
		assertTrue(airports.contains(airportOrl));
		
		assertEquals(4,
				airports.stream().filter(a -> a.equals(airportGru)).findFirst().get().getDepartures().size());
		assertEquals(1,
				airports.stream().filter(a -> a.equals(airportBrc)).findFirst().get().getDepartures().size());
		assertEquals(1,
				airports.stream().filter(a -> a.equals(airportScl)).findFirst().get().getDepartures().size());
		assertEquals(0,
				airports.stream().filter(a -> a.equals(airportCdg)).findFirst().get().getDepartures().size());
		assertEquals(1,
				airports.stream().filter(a -> a.equals(airportOrl)).findFirst().get().getDepartures().size());
	}

	/**
	 * Test method for {@link org.sixel.flight.core.service.RouteManagerServiceImpl#loadFile(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test
	public void testAddFlyPositive() throws CoreException, IOException {
		final File temp = File.createTempFile("test", ".csv");
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportCdg = new AirportBuilder().identifiedBy("CDG").build();
		final Airport airportScl = new AirportBuilder().identifiedBy("SCL").build();
		
		final Fly fly1 = new FlyBuilder().from(airportGru).to(airportCdg).atPrice(150l).build();
		final Fly fly2 = new FlyBuilder().from(airportCdg).to(airportGru).atPrice(140l).build();
		final Fly fly3 = new FlyBuilder().from(airportGru).to(airportCdg).atPrice(180l).build();
		final Fly fly4 = new FlyBuilder().from(airportGru).to(airportScl).atPrice(480l).build();
		final Fly fly5 = new FlyBuilder().from(airportCdg).to(airportScl).atPrice(450l).build();

		temp.delete();
		assertFalse(temp.exists());

		routeManagerService.persist(temp, fly1);
		routeManagerService.persist(temp, fly2);
		routeManagerService.persist(temp, fly3);
		routeManagerService.persist(temp, fly4);
		routeManagerService.persist(temp, fly5);
		
		assertTrue(temp.exists());
		
		final Set<Airport> airports = routeManagerService.loadFile(temp);
		
		assertEquals(3, airports.size());
		
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportCdg));
		assertTrue(airports.contains(airportScl));
		
		assertEquals(5, airports.stream().map(Airport::getDepartures).flatMap(Collection::stream).count());
		
		temp.deleteOnExit();
	}
	
	/**
	 * @throws CoreException
	 */
	@Test
	public void testFindFliesGruCdg() throws CoreException {
		
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportCdg = new AirportBuilder().identifiedBy("CDG").build();
		final Route route = new RouteBuilder().from(airportGru).to(airportCdg).build();		
		final Set<Airport> airports = routeManagerService.loadFile(new File("test.csv"));
		
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportCdg));
		
		final List<Fly> flies = routeManagerService.findFlies(airports, route);
		
		assertEquals(4, flies.size());
		assertEquals(Optional.of(Long.valueOf(221l)), flies.stream().map(Fly::getPrice).reduce((a, b) -> a + b));
	}
	
	/**
	 * @throws CoreException
	 */
	@Test
	public void testFindBestFlyGruCdg() throws CoreException {
		
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportCdg = new AirportBuilder().identifiedBy("CDG").build();
		final Route route = new RouteBuilder().from(airportGru).to(airportCdg).build();		
		final Set<Airport> airports = routeManagerService.loadFile(new File("test.csv"));
		
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportCdg));
		
		final Fly cheaperFly = routeManagerService.findBestFly(airports, route);
		
		assertEquals(Long.valueOf(40l), cheaperFly.getPrice());
	}
	
	/**
	 * @throws CoreException
	 */
	@Test
	public void testFindFliesGruOrl () throws CoreException {
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportOrl = new AirportBuilder().identifiedBy("ORL").build();
		final Route route = new RouteBuilder().from(airportGru).to(airportOrl).build();
		final Set<Airport> airports = routeManagerService.loadFile(new File("test.csv"));
		
		assertEquals(5, airports.size());
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportOrl));
		
		final List<Fly> flies = routeManagerService.findFlies(airports, route);
		
		assertEquals(3, flies.size());
		assertEquals(Optional.of(Long.valueOf(131l)), flies.stream().map(Fly::getPrice).reduce((a, b) -> a + b));
	}
	
	/**
	 * @throws CoreException
	 */
	@Test
	public void testFindBestFlyGruOrl() throws CoreException {
		
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportOrl = new AirportBuilder().identifiedBy("ORL").build();
		final Route route = new RouteBuilder().from(airportGru).to(airportOrl).build();		
		final Set<Airport> airports = routeManagerService.loadFile(new File("test.csv"));
		
		assertTrue(airports.contains(airportGru));
		assertTrue(airports.contains(airportOrl));
		
		final Fly cheaperFly = routeManagerService.findBestFly(airports, route);
		
		assertEquals(Long.valueOf(35l), cheaperFly.getPrice());
	}
}
