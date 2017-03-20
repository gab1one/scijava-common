
package org.scijava.io.compressed;

import org.scijava.io.AbstractWrappedLocation;
import org.scijava.io.Location;

/**
 * Location of a BZip2 file
 * 
 * @author Gabriel Einsdorf
 */
public class BZip2Location extends AbstractWrappedLocation {

	public BZip2Location(Location loc) {
		super(loc);
	}
}
