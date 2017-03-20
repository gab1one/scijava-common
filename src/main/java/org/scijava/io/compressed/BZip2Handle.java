/*
 * #%L
 * SCIFIO library for reading and converting scientific file formats.
 * %%
 * Copyright (C) 2011 - 2017 Board of Regents of the University of
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

package org.scijava.io.compressed;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.scijava.io.AbstractStreamHandle;
import org.scijava.io.DataHandle;
import org.scijava.io.DataHandleService;
import org.scijava.io.Location;
import org.scijava.io.ResettableStreamHandle;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * StreamHandle implementation for reading from BZip2-compressed files or byte
 * arrays. Instances of BZip2Handle are read-only.
 *
 * @author Melissa Linkert
 * @author Gabriel Einsdorf
 */
@Plugin(type = DataHandle.class)
public class BZip2Handle extends AbstractStreamHandle<BZip2Location> implements
	ResettableStreamHandle<BZip2Location>
{

	@Parameter
	private LogService log;

	@Parameter
	private DataHandleService dataHandleService;

	DataHandle<Location> innerHandle;
	CBZip2InputStream inputStream;

	@Override
	public void resetStream() throws IOException {
		if (inner() instanceof ResettableStreamHandle) {
			// we can just reset the stream
			((ResettableStreamHandle<Location>) inner()).resetStream();
			inputStream.close(false);
			inputStream = new CBZip2InputStream(inner(), log);
		}
		else {
			// invalidate all
			innerHandle = null;
			inputStream.close(true);
			inputStream = null;
		}
		setOffset(0);
	}

	@Override
	public InputStream in() throws IOException {
		if (inputStream == null) {
			// FIXME add buffer arround inner
			DataHandle<Location> wrapped = inner();

			// skip first two bytes, FIXME purpose of this is not clear to me
			// contain "magic bytes"
			int skipped = 0;
			while (skipped < 2) {
				skipped += wrapped.skip(2 - skipped);
			}
			inputStream = new CBZip2InputStream(wrapped, log);
		}
		return inputStream;
	}

	private DataHandle<Location> inner() {
		if (innerHandle == null) {
			innerHandle = dataHandleService.create(get().getInnerLocation());
		}
		return innerHandle;
	}

	@Override
	public OutputStream out() throws IOException {
		return null;
	}

	@Override
	public long length() throws IOException {
		return 0;
	}

	@Override
	public void setLength(long length) throws IOException {
		throw new IOException("BZip2Handle is read-only!");
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
	public Class<BZip2Location> getType() {
		return BZip2Location.class;
	}
}
