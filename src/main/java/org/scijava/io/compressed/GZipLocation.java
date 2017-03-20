
package org.scijava.io.compressed;

import org.scijava.io.AbstractWrappedLocation;
import org.scijava.io.Location;

/**
 * A {@link Location} backed by any other {@link Location}, containing GZIP
 * compressed data.
 * 
 * @author Gabriel Einsdorf
 */
public class GZipLocation extends AbstractWrappedLocation {

	public GZipLocation(Location loc) {
		super(loc);
	}

}
