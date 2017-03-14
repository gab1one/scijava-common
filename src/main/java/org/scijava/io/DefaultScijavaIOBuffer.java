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

import org.scijava.util.ByteArray;

/**
 * Default implementation of {@link ScijavaIOBuffer}, uses a {@link ByteArray}
 * as a growing backing buffer.
 * 
 * @author Gabriel Einsdorf
 */
public class DefaultScijavaIOBuffer implements ScijavaIOBuffer {

	private ByteArray buffer;
	private long maxBufferedPos = -1;

	public DefaultScijavaIOBuffer() {
		buffer = new ByteArray();
	}

	@Override
	public int getMaxBufferSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void setBytes(long startpos, byte[] bytes, int offset, int length) {
		// ensure we have space
		checkWritePos(startpos, startpos + length);
		int neededCapacity = (int) (Math.max(maxBufferedPos, 0) + length);
		buffer.ensureCapacity(neededCapacity);

		// copy the data
		System.arraycopy(bytes, offset, buffer.getArray(), (int) startpos, length);
		buffer.setSize(neededCapacity);
		updateMaxPos(startpos + length);
	}

	@Override
	public void setByte(long pos, byte b) {
		checkWritePos(pos, pos);
		buffer.setValue((int) pos, b);
		updateMaxPos(pos);
	}

	private void updateMaxPos(long pos) {
		maxBufferedPos = pos > maxBufferedPos ? pos : maxBufferedPos;
	}

	@Override
	public void clear() {
		buffer.clear();
		maxBufferedPos = 0;
	}

	@Override
	public byte getByte(long pos) {
		checkReadPos(pos, pos);
		// the buffer might contain bytes with negative value
		// we need to flip the sign to positive to satisfy the method contract
		return buffer.getValue((int) pos);
	}

	@Override
	public int getBytes(long startPos, byte[] b, int offset, int length) {
		checkReadPos(startPos, startPos + length);
		System.arraycopy(buffer.getArray(), (int) startPos, b, offset, length);
		return length;
	}

	@Override
	public long getMaxPos() {
		return maxBufferedPos;
	}

	/**
	 * Ensures that the requested range satisfies basic sanity criteria.
	 * 
	 * @param start the start of the range
	 * @param end the end of the range
	 */
	private void basicRangeCheck(long start, long end) {
		if (start > Integer.MAX_VALUE) {
			throw new IndexOutOfBoundsException("Requested postion " + start +
				" is larger than the maximal buffer size: " + Integer.MAX_VALUE);
		}
		if (end > Integer.MAX_VALUE) {
			throw new IndexOutOfBoundsException("Requested postion " + end +
				" is larger than the maximal buffer size: " + Integer.MAX_VALUE);
		}
		if (end < start) {
			throw new IllegalArgumentException(
				"Invalid range, end is smaller than start!");
		}
	}

	/**
	 * Check if we can write to the specified range
	 * 
	 * @param start the start position of the range
	 * @param end the end position of the range
	 */
	private void checkWritePos(long start, long end) {
		basicRangeCheck(start, end);
		if (start > maxBufferedPos + 1) { // we can't have holes in the buffer
			throw new IndexOutOfBoundsException("Requested start position: " + start +
				" would leave a hole in the buffer, largest legal position is: " +
				maxBufferedPos + 1);
		}
	}

	/**
	 * Check if we can read from the specified range
	 * 
	 * @param start the start position of the range
	 * @param end the end position of the range
	 */
	private void checkReadPos(long start, long end) {
		basicRangeCheck(start, end);
		if (start > maxBufferedPos) {
			throw new IndexOutOfBoundsException("Reuested position: " + start +
				" is larger than the maximally buffered postion: " + maxBufferedPos);
		}
		if (end > maxBufferedPos) {
			throw new IndexOutOfBoundsException("reuested position: " + end +
				" is larger than the maximally buffered postion: " + maxBufferedPos);
		}
	}

}
