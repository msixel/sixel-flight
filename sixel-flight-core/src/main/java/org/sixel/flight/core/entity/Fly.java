/**
 * 
 */
package org.sixel.flight.core.entity;

import java.io.Serializable;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author marcos
 *
 */
public class Fly implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Route route;
	private Long price;
	
	/**
	 * default 
	 */
	public Fly() {
		this.route = new Route();
		this.price = NumberUtils.LONG_ZERO;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param original
	 */
	public Fly(final Fly original) {
		this.route = new Route(original.route);
		this.price = original.price;
	}
	
	/**
	 * @return the route
	 */
	public final Route getRoute() {
		return route;
	}
	
	/**
	 * @param route the route to set
	 */
	public final void setRoute(final Route route) {
		this.route = route;
	}

	/**
	 *
	 */
	public final boolean isGoingTo(final Airport airport) {
		if (this.route == null) {
			throw new IllegalStateException("route");
		}
		return airport.equals(this.route.getTo());
	}
	
	/**
	 * @return the price
	 */
	public Long getPrice() {
		return price;
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(final Long price) {
		this.price = price;
	}
	
	/**
	 *
	 */
	@JsonIgnore
	public boolean isFulFilled() {
		return (route!=null && route.isFulFilled() && price!=null);
	}
	
	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
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
		Fly other = (Fly) obj;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return String.format("Fly [route=%s, price=%d]", route, price);
	}
	
}
