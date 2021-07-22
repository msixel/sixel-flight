/**
 * 
 */
package org.sixel.flight.core.entity;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author marcos
 *
 */
public final class Airport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private Set<Fly> departures;
		
	/**
	 * default 
	 */
	public Airport() {
		departures = new LinkedHashSet<>();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param original
	 */
	public Airport(final Airport original) {
		this.code = original.code;
		this.departures = new LinkedHashSet<>(original.departures);
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Convenience
	 */
	public Boolean hasDepartures() {
		return !departures.isEmpty();
	}
	
	/**
	 * @return the departures
	 */
	@JsonIgnore		
	public Set<Fly> getDepartures() {
		return departures;
	}

	/**
	 * @param departure the departure to added
	 */
	public void addDeparture(Fly departure) {
		this.departures.add(departure);
	}
	
	/**
	 * Convenience 
	 * 
	 * @return
	 */
	@JsonIgnore	
	public boolean isFulFilled() {
		return (this.code!=null);
	}
	
	/**
	 *
	 */
	@Override
	public int hashCode() {
		return Objects.hash(code);
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
		Airport other = (Airport) obj;
		return Objects.equals(code, other.code);
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return String.format("Airport [code=%s, departures=%s]", code, departures);
	}

}
