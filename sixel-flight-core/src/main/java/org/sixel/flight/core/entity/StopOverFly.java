/**
 * 
 */
package org.sixel.flight.core.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author marcos
 *
 */
public final class StopOverFly extends Fly {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Fly> nonStopFlies;
	private Long cachedPrice;
	
	/**
	 * 
	 */
	public StopOverFly() {
		this.route = null;
		this.nonStopFlies = new LinkedList<>();
		this.cachedPrice = null;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param original
	 */
	public StopOverFly(final StopOverFly original) {
		this.route = new Route(original.route);
		this.nonStopFlies = new LinkedList<>(original.nonStopFlies);
		this.cachedPrice = null;
	}
	
	/**
	 * @param nonStopFly
	 */
	public void addNonStopFly(final Fly nonStopFly) {
		this.nonStopFlies.add(nonStopFly);
		cachedPrice=null;
	}
	
	/**
	 * @return the nonStopFlies
	 */
	public List<Fly> getNonStopFlies() {
		return nonStopFlies;
	}
	
	/**
	 *
	 */
	@Override
	public void setPrice(Long price) {
		throw new UnsupportedOperationException("the price is compound by the sum of flies' price");
	}
	
	/**
	 *
	 */
	@Override
	public Long getPrice() {
		if (nonStopFlies==null) return NumberUtils.LONG_ZERO;
		if (cachedPrice!=null) return cachedPrice;
		cachedPrice = nonStopFlies.stream().map(Fly::getPrice).reduce(NumberUtils.LONG_ZERO, (a,b) -> a+b);
		return cachedPrice;
	}

	/**
	 *
	 */
	@Override
	@JsonIgnore
	public boolean isFulFilled() {
		return (super.route!=null && super.route.isFulFilled());
	}
	
	/**
	 *
	 */
	@Override
	public int hashCode() {
		return Objects.hash(route);
	}

	/**
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StopOverFly other = (StopOverFly) obj;
		return Objects.equals(super.route, other.route);
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return String.format("StopOverFly [route=%s, nonStopFlies=%s, price=%d]", route, nonStopFlies, getPrice());
	}

}
