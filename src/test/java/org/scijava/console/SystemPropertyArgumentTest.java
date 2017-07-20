/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, Max Planck
 * Institute of Molecular Cell Biology and Genetics, University of
 * Konstanz, and KNIME GmbH.
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

package org.scijava.console;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Tests {@link SystemPropertyArgument}.
 * 
 * @author Curtis Rueden
 */
public class SystemPropertyArgumentTest {

	@Test
	public void testSystemProperties() {
		assertPropertySet("foo", "bar");
		assertPropertySet("prop.with.dots", "true");
		assertPropertySet("prop-with-dashes", "whiz");
		assertPropertySet("prop_with_underscores", "bang");
		assertPropertySet("empty.value", "");
		assertPropertySet("unspecified.value", null);
		assertPropertySet("_-_", "legal");
		assertPropertySet("-_-", "also legal");
	}

	private void assertPropertySet(final String key, final String value) {
		final SystemPropertyArgument spa = new SystemPropertyArgument();
		final LinkedList<String> args = new LinkedList<>();
		args.add(value == null ? "-D" + key : "-D" + key + "=" + value);
		assertTrue(spa.supports(args));
		assertNull(System.getProperty(key));
		spa.handle(args);
		assertTrue(args.isEmpty());
		assertEquals(value == null ? "" : value, System.getProperty(key));
	}

}
