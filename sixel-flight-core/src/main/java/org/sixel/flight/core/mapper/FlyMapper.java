/**
 * 
 */
package org.sixel.flight.core.mapper;

import java.util.stream.Collectors;

import org.sixel.flight.core.entity.Airport;
import org.sixel.flight.core.entity.Fly;
import org.sixel.flight.core.entity.Route;
import org.sixel.flight.core.entity.StopOverFly;

/**
 * @author marcos
 *
 */
public final class FlyMapper implements Mapper<Fly, String> {
	
	/**
	 * Singleton instance
	 */
	private static final FlyMapper SINGLE_INSTANCE = new FlyMapper();
	
	/**
	 * @return
	 */
	public static FlyMapper getInstance() {
		return SINGLE_INSTANCE;
	}
	
	/**
	 * Singleton constructor
	 */
	private FlyMapper () {
	}
	
	/**
	 *{@inheritDoc}
	 */
	@Override
	public Fly from(final String line) {
		throw new UnsupportedOperationException();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public String to(final Fly fly) {
		if (fly instanceof StopOverFly) {
			final StopOverFly stopOverFly = (StopOverFly)fly;
			if (stopOverFly.getNonStopFlies().isEmpty())
				return toStrictUpperClass(fly);
			
			final String raw = stopOverFly.getNonStopFlies().stream()
					.map(Fly::getRoute)
					.map(Route::getFrom)
					.map(Airport::getCode)
					.collect(Collectors.joining(" - "));
			return raw + " - " + stopOverFly.getRoute().getTo().getCode() + " > $" + stopOverFly.getPrice();
		}
		return toStrictUpperClass(fly);
	}
	
	/**
	 * @param fly
	 * @return
	 */
	public String toStrictUpperClass(final Fly fly) {
		return String.format("%s - %s > $%d", fly.getRoute().getFrom().getCode(), fly.getRoute().getTo().getCode(), fly.getPrice());
	}

}
