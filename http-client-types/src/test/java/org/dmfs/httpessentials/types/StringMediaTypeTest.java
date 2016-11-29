/*
 * Copyright 2016 dmfs GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Test {@link StringMediaType}.
 *
 * @author Marten Gajda
 */
public class StringMediaTypeTest
{

    @Test
    public void testHashCode()
    {
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StringMediaType("Application/XML").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StringMediaType("Application/XML; charset=\"utf-8\"").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StringMediaType("application/xml").hashCode());
        assertEquals("application".hashCode() * 31 + "xml".hashCode(),
                new StringMediaType("application/xml; charset=\"utf-8\"").hashCode());
    }


    @Test
    public void testFirstParameterParameterTypeOfTT()
    {
        assertEquals("latin-1",
                new StringMediaType("Application/xml").firstParameter(Parameters.CHARSET, "latin-1").value());
        assertEquals("UTF-8",
                new StringMediaType("Application/xml; charset=\"UTF-8\"").firstParameter(Parameters.CHARSET, "latin-1")
                        .value());
        assertEquals("UTF-8",
                new StringMediaType("Application/xml; charset=\"UTF-8\"").firstParameter(Parameters.CHARSET, "UTF-8")
                        .value());
    }


    @Test
    public void testParameters()
    {
        Iterator<Parameter<String>> iterator1 = new StringMediaType("Application/xml").parameters(Parameters.TITLE);
        assertFalse(iterator1.hasNext());

        Iterator<Parameter<String>> iterator2 = new StringMediaType("Application/xml; charset=\"UTF-8\"").parameters(
                Parameters.CHARSET);
        assertTrue(iterator2.hasNext());
        assertEquals("UTF-8", iterator2.next().value());
        assertFalse(iterator2.hasNext());

        Iterator<Parameter<String>> iterator3 = new StringMediaType(
                "Application/xml ; charset=\"UTF-8\" ; title=\"someTitle\";charset=\"latin-1\"")
                .parameters(Parameters.CHARSET);
        assertTrue(iterator3.hasNext());
        assertEquals("UTF-8", iterator3.next().value());
        assertTrue(iterator3.hasNext());
        assertEquals("latin-1", iterator3.next().value());
        assertFalse(iterator3.hasNext());

    }


    @Test
    public void testHasParameter()
    {
        assertFalse(new StringMediaType("Application/xml").hasParameter(Parameters.TITLE));
        assertTrue(new StringMediaType("Application/xml; charset=\"UTF-8\"").hasParameter(Parameters.CHARSET));
        assertFalse(new StringMediaType("Application/xml; x-foo=abcde").hasParameter(Parameters.TITLE));
        assertTrue(new StringMediaType("Application/xml; x-foo=abcde; charset=\"UTF-8\"; x-bar=1234").hasParameter(
                Parameters.CHARSET));
    }


    @Test
    public void testType()
    {
        assertEquals("Application/json", new StringMediaType("Application/json").type());
        assertEquals("Application/json", new StringMediaType("Application/json;").type());
        assertEquals("Application/json", new StringMediaType("Application/json  ").type());
        assertEquals("Application/json", new StringMediaType("Application/json ; ").type());
    }


    @Test
    public void testMainType()
    {
        assertEquals("Application", new StringMediaType("Application/json").mainType());
        assertEquals("Application", new StringMediaType("Application/json;").mainType());
        assertEquals("Application", new StringMediaType("Application/json ").mainType());
        assertEquals("Application", new StringMediaType("Application/json ; ").mainType());
    }


    @Test
    public void testSubType()
    {
        assertEquals("json", new StringMediaType("Application/json").subType());
        assertEquals("json", new StringMediaType("Application/json;").subType());
        assertEquals("json", new StringMediaType("Application/json ").subType());
        assertEquals("json", new StringMediaType("Application/json ; ").subType());
    }


    @Test
    public void testCharset()
    {
        assertEquals("latin-1", new StringMediaType("Application/xml").charset("latin-1"));
        assertEquals("UTF-8", new StringMediaType("Application/xml; charset=\"UTF-8\"").charset("latin-1"));
        assertEquals("UTF-8", new StringMediaType("Application/xml; charset=\"UTF-8\"").charset("UTF-8"));
    }


    @Test
    public void testToString()
    {
        assertEquals("Application/xml", new StringMediaType("Application/xml").toString());
        assertEquals("Application/xml; charset=\"UTF-8\"",
                new StringMediaType("Application/xml; charset=\"UTF-8\"").toString());
    }


    @Test
    public void testEqualsObject()
    {
        // equals is case insensitive
        assertTrue(new StringMediaType("application/xml").equals(new StringMediaType("application/xml")));
        assertTrue(new StringMediaType("application/xml").equals(new StringMediaType("Application/xml")));
        assertFalse(new StringMediaType("application/xml").equals(new StringMediaType("Application/json")));

        // parameters are not taken into account
        assertTrue(new StringMediaType("application/xml").equals(
                new StringMediaType("application/xml; charset=\"UTF-8\"")));
        assertTrue(new StringMediaType("application/xml").equals(
                new StringMediaType("Application/xml; charset=\"UTF-8\"")));
        assertFalse(new StringMediaType("application/xml").equals(
                new StringMediaType("Application/json; charset=\"UTF-8\"")));

        // equals works with other MediaType implementations
        assertTrue(new StringMediaType("application/xml").equals(new StructuredMediaType("application", "xml")));
        assertTrue(new StringMediaType("application/xml").equals(new StructuredMediaType("Application", "xml")));
        assertFalse(new StringMediaType("application/xml").equals(new StructuredMediaType("Application", "json")));

    }
}
