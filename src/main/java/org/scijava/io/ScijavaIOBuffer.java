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

/**
 * Self growing buffer over arbitrary bytes.
 * 
 * @author Gabriel Einsdorf
 */
public interface ScijavaIOBuffer {

	/**
	 * @param pos the position to read from
	 * @return the byte at the given position
	 */
	public byte getByte(long pos);

	/**
	 * @param startPos the position in the buffer to start reading from
	 * @param bytes the byte array to read into
	 * @return the number of bytes read
	 */
	default int getBytes(long startPos, byte[] bytes) {
		return getBytes(startPos, bytes, 0, bytes.length);
	}

	/**
	 * @param startPos the position in the buffer to start reading from
	 * @param bytes the byte array to read into
	 * @param offset the offset in the bytes array
	 * @param length the number of elements to read into the bytes array
	 * @return number of bytes read
	 */
	int getBytes(long startPos, byte[] bytes, int offset, int length);

	/**
	 * Sets the bytes starting form the given position to the values form the
	 * provided array.
	 * 
	 * @param startPos the position in the buffer to start writing from
	 * @param bytes the byte array to write
	 * @param offset the offset in the bytes array
	 * @param length the number of bytes to read
	 */
	void setBytes(long startPos, byte[] bytes, int offset, int length);

	/**
	 * Appends the given bytes to the buffer
	 * 
	 * @param bytes the bytes to append to the buffer
	 * @param length the number of elements to append from the bytes array
	 */
	default void appendBytes(byte[] bytes, int length) {
		setBytes(getMaxPos() + 1, bytes, 0, length);
	}

	/**
	 * Clears the buffer
	 */
	public void clear();

	/**
	 * @return the largest bufferd position
	 */
	public long getMaxPos();

	/**
	 * Sets the byte at the given position
	 * 
	 * @param pos the position
	 * @param b the value to set
	 */
	void setByte(long pos, byte b);

	/**
	 * @return the max size of the buffer
	 */
	public int getMaxBufferSize();
}
