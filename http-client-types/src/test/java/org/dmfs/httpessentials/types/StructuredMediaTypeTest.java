/*
 * Copyright 2017 dmfs GmbH
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.httpessentials.types;

import org.dmfs.httpessentials.parameters.Parameter;
import org.dmfs.httpessentials.parameters.Parameters;
import org.junit.Test;

import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Test {@link StructuredMediaType}.
 *
 * @author Marten Gajda
 */
public class StructuredMediaTypeTest
{

    @Test
    public void testHashCode()
    {
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StructuredMediaType("Application", "XML").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StructuredMediaType("Application", "XML", "utf-8").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StructuredMediaType("application", "xml").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StructuredMediaType("application", "xml", "utf-8").hashCode());
    }


    @Test
    public void testFirstParameterParameterTypeOfTT()
    {
        assertEquals("latin-1",
                new StructuredMediaType("Application", "xml").firstParameter(Parameters.CHARSET, "latin-1").value());
        assertEquals("UTF-8",
                new StructuredMediaType("Application", "xml", "UTF-8").firstParameter(Parameters.CHARSET, "latin-1")
                        .value());
        assertEquals("UTF-8",
                new StructuredMediaType("Application", "xml", "UTF-8").firstParameter(Parameters.CHARSET, "UTF-8")
                        .value());
    }


    @Test
    public void testParameters()
    {
        Iterator<Parameter<String>> iterator1 = new StructuredMediaType("Application", "xml").parameters(
                Parameters.TITLE);
        assertFalse(iterator1.hasNext());

        Iterator<Parameter<String>> iterator2 = new StructuredMediaType("Application", "xml", "UTF-8").parameters(
                Parameters.CHARSET);
        assertTrue(iterator2.hasNext());
        assertEquals("UTF-8", iterator2.next().value());
        assertFalse(iterator2.hasNext());

        Iterator<Parameter<String>> iterator3 = new StructuredMediaType("Application", "xml",
                Parameters.CHARSET.entity("UTF-8"),
                Parameters.TITLE.entity("someTitle"), Parameters.CHARSET.entity("latin-1")).parameters(
                Parameters.CHARSET);
        assertTrue(iterator3.hasNext());
        assertEquals("UTF-8", iterator3.next().value());
        assertTrue(iterator3.hasNext());
        assertEquals("latin-1", iterator3.next().value());
        assertFalse(iterator3.hasNext());

    }


    @Test
    public void testHasParameter()
    {
        assertFalse(new StructuredMediaType("Application", "xml").hasParameter(Parameters.TITLE));
        assertTrue(new StructuredMediaType("Application", "xml", "UTF-8").hasParameter(Parameters.CHARSET));
        assertFalse(new StructuredMediaType("Application", "xml", Parameters.CHARSET.entity("abcde")).hasParameter(
                Parameters.TITLE));
        assertTrue(new StructuredMediaType("Application", "xml", Parameters.TITLE.entity("123"),
                Parameters.CHARSET.entity("abcde"),
                Parameters.HREFLANG.entity(Locale.ENGLISH)).hasParameter(Parameters.CHARSET));
    }


    @Test
    public void testType()
    {
        assertTrue("Application/json".equalsIgnoreCase(new StructuredMediaType("Application", "json").type()));
    }


    @Test
    public void testMainType()
    {
        assertTrue("Application".equalsIgnoreCase(new StructuredMediaType("Application", "json").mainType()));
    }


    @Test
    public void testSubType()
    {
        assertTrue("json".equalsIgnoreCase(new StructuredMediaType("Application", "json").subType()));
    }


    @Test
    public void testCharset()
    {
        assertEquals("latin-1", new StructuredMediaType("Application", "xml").charset("latin-1"));
        assertEquals("UTF-8", new StructuredMediaType("Application", "xml", "UTF-8").charset("latin-1"));
        assertEquals("UTF-8", new StructuredMediaType("Application", "xml", "UTF-8").charset("UTF-8"));
    }


    @Test
    public void testToString()
    {
        assertTrue("Application/xml".equalsIgnoreCase(new StructuredMediaType("Application", "xml").toString()));
        assertTrue("Application/xml;charset=\"UTF-8\"".equalsIgnoreCase(
                new StructuredMediaType("Application", "xml", "UTF-8").toString()));
    }


    @Test
    public void testEqualsObject()
    {
        // equals works with other MediaType implementations
        assertTrue(new StructuredMediaType("application", "xml").equals(new StringMediaType("application/xml")));
        assertTrue(new StructuredMediaType("application", "xml").equals(
                new StringMediaType("Application/xml; charset=\"UTF-8\"")));
        assertFalse(new StructuredMediaType("application", "xml").equals(new StringMediaType("Application/json")));

        // parameters are not taken into account
        assertTrue(new StructuredMediaType("application", "xml").equals(
                new StructuredMediaType("application", "xml", "UTF-8")));
        assertTrue(new StructuredMediaType("application", "xml").equals(
                new StructuredMediaType("Application", "xml", "UTF-8")));
        assertFalse(new StructuredMediaType("application", "xml").equals(
                new StructuredMediaType("Application", "json", "UTF-8")));

        // equals is case insensitive
        assertTrue(new StructuredMediaType("application", "xml").equals(new StructuredMediaType("application", "xml")));
        assertTrue(new StructuredMediaType("application", "xml").equals(new StructuredMediaType("Application", "xml")));
        assertFalse(
                new StructuredMediaType("application", "xml").equals(new StructuredMediaType("Application", "json")));

    }
}
