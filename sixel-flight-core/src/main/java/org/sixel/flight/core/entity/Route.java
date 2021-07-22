/**
 * 
 */
package org.sixel.flight.core.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author marcos
 *
 */
public final class Route implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Airport from;
	private Airport to;
	
	/**
	 * default 
	 */
	public Route() {}
	
	/**
	 * Copy constructor
	 * 
	 * @param original
	 */
	public Route(final Route original) {
		this.from = new Airport(original.from);
		this.to = new Airport(original.to);
	}
	
	/**
	 * @return the from
	 */
	public Airport getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(Airport from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public Airport getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(Airport to) {
		this.to = to;
	}
	
	/**
	 * Convenience 
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isFulFilled() {
		return (this.from!=null && this.from.isFulFilled() && 
				this.to!=null && this.to.isFulFilled());
	}
	
	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Route other = (Route) obj;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return String.format("Route [from.code=%s, to.code=%s]", from.getCode(), to.getCode());
	}
	
}
