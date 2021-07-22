/**
 * 
 */
package org.sixel.flight.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.sixel.flight.core.builder.AirportBuilder;
import org.sixel.flight.core.builder.FlyBuilder;
import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidFileException;
import org.sixel.flight.core.exception.InvalidFileFormatException;
import org.sixel.flight.core.persistence.RouteDAO;
import org.sixel.flight.core.persistence.RouteDAOImpl;

/**
 * @author marcos
 *
 */
public class RouteDAOImplTest {
	
	private final RouteDAO routeDAO;
	
	public RouteDAOImplTest() {
		routeDAO = RouteDAOImpl.getInstance();
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#findAll(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test
	public void testFindAllPositive() throws CoreException {
		final Set<Fly> flies;
		flies = routeDAO.findAll(new File("test.csv"));
		assertEquals(7, flies.size());
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#findAll(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test(expected = InvalidFileException.class)
	public void testFindAllNegativeFileNotFound() throws CoreException {
		routeDAO.findAll(new File("file.not.found.csv"));
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#findAll(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test(expected = InvalidFileFormatException.class)
	public void testFindAllNegativeInvalidFile1() throws CoreException {
		routeDAO.findAll(new File("test.invalid1.csv"));
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#findAll(java.io.File)}.
	 * @throws CoreException 
	 */
	@Test(expected = InvalidFileFormatException.class)
	public void testFindAllNegativeInvalidFile2() throws CoreException {
		routeDAO.findAll(new File("test.invalid2.csv"));
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#persist(java.io.File, org.sixel.flight.core.entity.Fly)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test
	public void testPersistPositive() throws CoreException, IOException {
		final File temp = File.createTempFile("test", ".csv");
		final Airport airportGru = new AirportBuilder().identifiedBy("GRU").build();
		final Airport airportCdg = new AirportBuilder().identifiedBy("CDG").build();
		
		final Fly fly1 = new FlyBuilder().from(airportGru).to(airportCdg).atPrice(150l).build();
		final Fly fly2 = new FlyBuilder().from(airportCdg).to(airportGru).atPrice(140l).build();
		final Fly fly3 = new FlyBuilder().from(airportGru).to(airportCdg).atPrice(180l).build();

		temp.delete();
		assertFalse(temp.exists());

		routeDAO.persist(temp, fly1);
		routeDAO.persist(temp, fly2);
		routeDAO.persist(temp, fly3);
		
		assertTrue(temp.exists());
		
		final Set<Fly> flies = routeDAO.findAll(temp);
		
		assertEquals(3, flies.size());
		
		assertTrue(flies.contains(fly1));
		assertTrue(flies.contains(fly2));
		assertTrue(flies.contains(fly3));
		
		temp.deleteOnExit();
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#persist(java.io.File, org.sixel.flight.core.entity.Fly)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPersistNegativeCsvFileNotNull() throws CoreException, IOException {
		routeDAO.persist((File)null, new Fly());
	}

	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#persist(java.io.File, org.sixel.flight.core.entity.Fly)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPersistNegativeFlyNotNull() throws CoreException, IOException {
		routeDAO.persist(new File("test.csv"), null);
	}
	
	/**
	 * Test method for {@link org.sixel.flight.core.persistence.RouteDAOImpl#persist(java.io.File, org.sixel.flight.core.entity.Fly)}.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPersistNegativeFlyNotFulFilled() throws CoreException, IOException {
		routeDAO.persist(new File("test.csv"), new Fly());
	}
}
