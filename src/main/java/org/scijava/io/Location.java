/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.io;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * A <em>location</em> is a data descriptor, such as a file on disk, a remote
 * URL, or a database connection.
 * <p>
 * Analogous to a
 * <a href="https://en.wikipedia.org/wiki/Uniform_resource_identifier">uniform
 * resource identifier</a> ({@link URI}), a location identifies <em>where</em>
 * the data resides, without necessarily specifying <em>how</em> to access that
 * data. The {@link DataHandle} interface defines a plugin that knows how to
 * read and/or write bytes for a particular kind of location.
 * </p>
 * 
 * @author Curtis Rueden
 */
public interface Location {

	/**
	 * Gets the location expressed as a {@link URI}, or null if the location
	 * cannot be expressed as such.
	 */
	default URI getURI() {
		return null;
	}

	/**
	 * @return the name of the object addressed by this location, or an empty
	 *         string if it has no name.
	 */
	default String getName() {
		URI uri = getURI();
		if (uri != null) {
			return uri.toString();
		}
		return "";
	}

	/**
	 * @return whether this location is browsable, meaning the methods
	 *         {@link #getParent()} or {@link #getChildren()} can be called.
	 */
	boolean isBrowsable();

	/**
	 * @return the parent {@link Location} of this {@link Location}, or
	 *         <code>null</code> if this {@link Location} has no parent.
	 * @throws IOException if the parent could not be located.
	 */
	Location getParent() throws IOException;

	/**
	 * @return the children of this {@link Location}, or
	 *         {@link Collections#EMPTY_SET} if this {@link Location} has no
	 *         children.
	 * @throws IOException if the children could not be located.
	 */
	Set<Location> getChildren() throws IOException;
}
