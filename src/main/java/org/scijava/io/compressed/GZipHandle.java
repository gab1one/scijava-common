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

package org.scijava.io.compressed;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.scijava.io.AbstractStreamHandle;
import org.scijava.io.DataHandle;
import org.scijava.io.DataHandleInputStream;
import org.scijava.io.DataHandleService;
import org.scijava.io.Location;
import org.scijava.io.ResettableStreamHandle;
import org.scijava.io.StreamHandle;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Bytes;

/**
 * StreamHandle implementation for reading from gzip-compressed files or byte
 * arrays. Instances of GZipHandle are read-only.
 *
 * @author Melissa Linkert
 * @author Gabriel Einsdorf
 */
@Plugin(type = DataHandle.class)
public class GZipHandle extends AbstractStreamHandle<GZipLocation> implements
	ResettableStreamHandle<GZipLocation>
{

	@Parameter
	private DataHandleService dataHandleService;

	private GZIPInputStream inputStream;
	private DataHandle<Location> innerHandle;

	// -- Constructor --

	@Override
	public void resetStream() throws IOException {
		// invalidate all
		inputStream.close();
		inputStream = null;
		innerHandle.close();
		setOffset(0);
	}

	@Override
	public InputStream in() throws IOException {
		if (inputStream == null) {
			DataHandle<Location> handle = inner();

			// check if location is valid
			byte[] b = new byte[2];
			handle.read(b);
			if (Bytes.toInt(b, true) != GZIPInputStream.GZIP_MAGIC) {
				throw new IOException("Provided Location is not a valid GZIP file");
			}

			// FIXME make this a Buffered Stream, maybe just use the buffered version
			// of the GZipInputStream?

			if (handle instanceof StreamHandle) {
				inputStream = new GZIPInputStream(((StreamHandle<Location>) handle)
					.in());
			}
			else {
				inputStream = new GZIPInputStream(new DataHandleInputStream<>(handle));
			}
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
		// FIXME: How can we now this?
		// *Not* like this:
//		int length = 0;
//		while (true) {
//			final int skip = in().skipBytes(1024);
//			if (skip <= 0) break;
//			length += skip;
//		}

		return -1;
	}

	@Override
	public void setLength(long length) throws IOException {
		throw new IOException("Can not set length, GZipHandle is read-only!");
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
	public Class<GZipLocation> getType() {
		return GZipLocation.class;
	}

}
