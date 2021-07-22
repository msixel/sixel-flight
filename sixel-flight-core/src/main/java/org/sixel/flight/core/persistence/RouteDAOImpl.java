package org.sixel.flight.core.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.math.NumberUtils;
import org.sixel.flight.core.Messages;
import org.sixel.flight.core.builder.FlyBuilder;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.exception.CoreException;
import org.sixel.flight.core.exception.InvalidFileException;
import org.sixel.flight.core.exception.InvalidFileFormatException;

/**
 * @author marcos
 * 
 *
 */
public final class RouteDAOImpl implements RouteDAO {
	
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(RouteDAOImpl.class.getName()); 
	
	/**
	 * Singleton instance
	 */
	private static final RouteDAO SINGLE_INSTANCE = new RouteDAOImpl();

	/**
	 * Constants
	 */
	private static final String FIELD_SEPARATOR = ",";
	private static final int FIELDS_IN_ROW = 3;
	private static final String LOG_BASE_FORMAT = "%s [\"%s\"]";
	
	private final Lock writeLock;
	private final Lock readLock;
	
	/**
	 * @return
	 */
	public static RouteDAO getInstance() {
		return SINGLE_INSTANCE;
	}
	
	/**
	 * Singleton constructor
	 */
	private RouteDAOImpl () {
		final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		writeLock = readWriteLock.writeLock();
		readLock = readWriteLock.readLock();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Fly> findAll(final File csvFile) throws CoreException {
		if (csvFile==null)
			throw new IllegalArgumentException("csvFile");
		
		readLock.lock();
		try {
			final Set<Fly> flies = findAllLocked(csvFile);
			LOGGER.log(Level.INFO, "Flies file loaded successful [records: {0}, path:\"{1}\"]", new Object[] {flies.size(), csvFile.getAbsolutePath()});
			return flies;
		} finally {
			readLock.unlock();
		}
		
	}
	
	/**
	 * @param csvFile
	 * @return
	 * @throws CoreException
	 */
	private Set<Fly> findAllLocked(final File csvFile) throws CoreException {
		
		final Set<Fly> flies = new LinkedHashSet<>();
		
		if (!csvFile.exists()) {
			throw new InvalidFileException(
					String.format(LOG_BASE_FORMAT, Messages.getString("file.not.found"), csvFile.getAbsolutePath()));
		}
		if (!csvFile.isFile() || !csvFile.canRead()) {
			throw new InvalidFileException(
					String.format(LOG_BASE_FORMAT, Messages.getString("file.invalid"), csvFile.getAbsolutePath()));
		}
		
		try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ( (line = bufferedReader.readLine()) != null ) {
				flies.add(parseLine(line));			
			}
			
		} catch (IOException e) {
			throw new CoreException(e.getMessage(), e);
		}
		
		return flies;
	}
	
	/**
	 * @param line
	 * @return
	 * @throws CoreException
	 */
	private Fly parseLine(final String line) throws CoreException {
		final StringTokenizer tokenizer = new StringTokenizer(line, FIELD_SEPARATOR);
		
		if (tokenizer.countTokens() != FIELDS_IN_ROW) {
			throw new InvalidFileFormatException(
					String.format(LOG_BASE_FORMAT, Messages.getString("file.invalid.format"), line));
		}
		final FlyBuilder builder = new FlyBuilder()
				.from(tokenizer.nextToken())
				.to(tokenizer.nextToken());
		final String price = tokenizer.nextToken();
		
		if (!NumberUtils.isCreatable(price)) {
			throw new InvalidFileFormatException(
					String.format(LOG_BASE_FORMAT, Messages.getString("file.invalid.format"), line));
		}
		
		return builder.atPrice(NumberUtils.createLong(price))
				.build();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist(File csvFile, Fly fly) throws CoreException {
		if (csvFile==null || fly==null || !fly.isFulFilled())
			throw new IllegalArgumentException();
		
		writeLock.lock();
		try {
			persistLocked(csvFile, fly);
			LOGGER.log(Level.INFO, "Fly saved successful [fly: {0}, path:\"{1}\"]", new Object[] {fly.toString(), csvFile.getAbsolutePath()});

		} finally {
			writeLock.unlock();
		}
		
	}
	
	/**
	 * @param csvFile
	 * @param flies
	 * @throws CoreException
	 */
	private void persistLocked(File csvFile, Fly fly) throws CoreException {		
		try (final FileWriter fileWriter= new FileWriter(csvFile, true) ) {
			fileWriter.write (
					String.format("%s,%s,%d%n", fly.getRoute().getFrom().getCode(), fly.getRoute().getTo().getCode(), fly.getPrice()));
			
		} catch (IOException e) {
			throw new InvalidFileException(
					String.format(LOG_BASE_FORMAT, Messages.getString("file.invalid"), csvFile.getAbsolutePath()), 
					e);		
		}
	}
}
