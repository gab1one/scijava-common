
package org.scijava.io;

import java.net.URI;

/**
 * A Wrapped Location wraps around a different location, to provide type safety.
 * 
 * @author Gabriel Einsdorf
 */
public class AbstractWrappedLocation extends AbstractLocation {

	private final Location innerLocation;

	public AbstractWrappedLocation(Location loc) {
		innerLocation = loc;
	}

	public Location getInnerLocation() {
		return innerLocation;
	}

	@Override
	public URI getURI() {
		return innerLocation.getURI();
	}

}
