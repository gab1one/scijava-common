/*
 * #%L
 * SCIFIO library for reading and converting scientific file formats.
 * %%
 * Copyright (C) 2011 - 2016 Board of Regents of the University of
 * Wisconsin-Madison
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

package org.scijava.io.handles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.scijava.Priority;
import org.scijava.io.AbstractStreamHandle;
import org.scijava.io.DataHandle;
import org.scijava.io.ResettableStreamHandle;
import org.scijava.plugin.Plugin;

/**
 * Provides random access to URLs using the IRandomAccess interface. Instances
 * of URLHandle are read-only.
 *
 * @deprecated Use {@code HTTPHandle} from {@code scijava-handles-http} instead.
 * @see java.net.URLConnection
 * @author Melissa Linkert
 * @author Gabriel Einsdorf
 */
@Deprecated
@Plugin(type = DataHandle.class, priority = Priority.LOW_PRIORITY)
public class URLHandle extends AbstractStreamHandle<URLLocation> implements
	ResettableStreamHandle<URLLocation>
{

	// -- Constants --

	private static final String[] SUPPORTED_PROTOCOLS = { "http:", "https:" };

	// -- Fields --

	/** URL of open socket */
	private String url;

	InputStream in;

	/** Socket underlying this stream */
	private URLConnection conn;

	// -- Constructors --

	// -- StreamHandle API methods --

	@Override
	public void resetStream() throws IOException {
		conn = (new URL(url)).openConnection();
		in = conn.getInputStream();
		setOffset(0);
	}

	// -- Helper methods --

	@Override
	public InputStream in() throws IOException {
		if (in == null) {
			in = conn().getInputStream();
		}
		return in;
	}

	private URLConnection conn() throws MalformedURLException, IOException {
		if (conn == null) {
			conn = (new URL(url)).openConnection();
		}
		return conn;
	}

	@Override
	public OutputStream out() {
		return null;
	}

	@Override
	public long length() throws IOException {
		return conn().getContentLength();
	}

	@Override
	public void setLength(long length) throws IOException {
		throw new IOException("URLHandle is read-only!");
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public Class<URLLocation> getType() {
		return URLLocation.class;
	}
}
