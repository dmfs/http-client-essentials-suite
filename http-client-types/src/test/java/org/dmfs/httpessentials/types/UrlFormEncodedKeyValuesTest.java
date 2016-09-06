/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.parameters.BasicParameterType;
import org.dmfs.httpessentials.parameters.Parameter;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * @author marten
 */
public class UrlFormEncodedKeyValuesTest
{
    private final static ParameterType<String> PARAM_EMPTY_KEY = new BasicParameterType<>("",
            PlainStringHeaderConverter.INSTANCE);
    private final static ParameterType<String> PARAM_KEY = new BasicParameterType<>("key",
            PlainStringHeaderConverter.INSTANCE);
    private final static ParameterType<String> PARAM_ANOTHER_KEY = new BasicParameterType<>("anotherKey",
            PlainStringHeaderConverter.INSTANCE);
    private final static ParameterType<String> PARAM_ANOTHER_SPECIAL_KEY = new BasicParameterType<>("another&Key",
            PlainStringHeaderConverter.INSTANCE);
    private final static ParameterType<String> PARAM_ANOTHER_SPECIAL_KEY2 = new BasicParameterType<>("another=Key",
            PlainStringHeaderConverter.INSTANCE);


    @Test
    public void testFirstParameter() throws Exception
    {
        assertEquals(PARAM_KEY.entity("defaultValue"),
                new UrlFormEncodedKeyValues("").firstParameter(PARAM_KEY, "defaultValue"));
        assertEquals(PARAM_KEY.entity("defaultValue"),
                new UrlFormEncodedKeyValues("keyX=value&anotherKey=anotherValue").firstParameter(PARAM_KEY,
                        "defaultValue"));
        assertEquals(PARAM_KEY.entity("value"),
                new UrlFormEncodedKeyValues("key=value&anotherKey=anotherValue").firstParameter(PARAM_KEY,
                        "defaultValue"));
        assertEquals(PARAM_KEY.entity("value"),
                new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value").firstParameter(PARAM_KEY,
                        "defaultValue"));
        assertEquals(PARAM_KEY.entity("value1"),
                new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value1&key=value2").firstParameter(PARAM_KEY,
                        "defaultValue"));
        assertEquals(PARAM_ANOTHER_KEY.entity("anotherValue"),
                new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value1&key=value2").firstParameter(
                        PARAM_ANOTHER_KEY, "defaultValue"));
        assertEquals(PARAM_EMPTY_KEY.entity("anotherValue"),
                new UrlFormEncodedKeyValues("=anotherValue&key=value1&key=value2").firstParameter(
                        PARAM_EMPTY_KEY, "defaultValue"));
        assertEquals(PARAM_EMPTY_KEY.entity("anotherValue"),
                new UrlFormEncodedKeyValues("key=value1&key=value2&=anotherValue").firstParameter(
                        PARAM_EMPTY_KEY, "defaultValue"));
        assertEquals(PARAM_ANOTHER_SPECIAL_KEY.entity("anotherValue"),
                new UrlFormEncodedKeyValues("another%26Key=anotherValue&key=value1&key=value2").firstParameter(
                        PARAM_ANOTHER_SPECIAL_KEY, "defaultValue"));
        assertEquals(PARAM_ANOTHER_SPECIAL_KEY2.entity("anotherValue"),
                new UrlFormEncodedKeyValues("another%3DKey=anotherValue&key=value1&key=value2").firstParameter(
                        PARAM_ANOTHER_SPECIAL_KEY2, "defaultValue"));
        assertEquals(PARAM_ANOTHER_KEY.entity("another&Value="),
                new UrlFormEncodedKeyValues("anotherKey=another%26Value%3D&key=value1&key=value2").firstParameter(
                        PARAM_ANOTHER_KEY, "defaultValue"));
    }


    @Test
    public void testParameters() throws Exception
    {
        assertIteratorsEqual(Collections.<Parameter<String>>emptyIterator(),
                new UrlFormEncodedKeyValues("").parameters(PARAM_KEY));
        assertIteratorsEqual(Collections.<Parameter<String>>emptyIterator(),
                new UrlFormEncodedKeyValues("keyX=value&anotherKey=anotherValue").parameters(PARAM_KEY));
        assertIteratorsEqual(Arrays.asList(PARAM_KEY.entity("value")).iterator(),
                new UrlFormEncodedKeyValues("key=value&anotherKey=anotherValue").parameters(PARAM_KEY));
        assertIteratorsEqual(Arrays.asList(PARAM_KEY.entity("value")).iterator(),
                new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value").parameters(PARAM_KEY));
        assertIteratorsEqual(Arrays.asList(PARAM_KEY.entity("value1"), PARAM_KEY.entity("value2")).iterator(),
                new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value1&key=value2").parameters(PARAM_KEY));
    }


    @Test
    public void testHasParameter() throws Exception
    {
        // test empty keys
        assertTrue(new UrlFormEncodedKeyValues("").hasParameter(PARAM_EMPTY_KEY));
        assertTrue(new UrlFormEncodedKeyValues("=").hasParameter(PARAM_EMPTY_KEY));
        assertFalse(new UrlFormEncodedKeyValues("keyX=value&anotherKey=anotherValue").hasParameter(PARAM_EMPTY_KEY));
        assertTrue(new UrlFormEncodedKeyValues("key=value&").hasParameter(PARAM_EMPTY_KEY));
        assertTrue(new UrlFormEncodedKeyValues("=someValue").hasParameter(PARAM_EMPTY_KEY));
        assertFalse(new UrlFormEncodedKeyValues("keyX=value&anotherKey=anotherValue&k=").hasParameter(PARAM_EMPTY_KEY));

        // test non-empty keys
        assertFalse(new UrlFormEncodedKeyValues("").hasParameter(PARAM_KEY));
        assertFalse(new UrlFormEncodedKeyValues("keyX=value&anotherKey=anotherValue").hasParameter(PARAM_KEY));
        assertTrue(new UrlFormEncodedKeyValues("key=value&anotherKey=anotherValue").hasParameter(PARAM_KEY));
        assertTrue(new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value").hasParameter(PARAM_KEY));
        assertTrue(new UrlFormEncodedKeyValues("anotherKey=anotherValue&key=value&key=value").hasParameter(PARAM_KEY));
    }


    @Test
    public void testToString() throws Exception
    {
        assertEquals("", new UrlFormEncodedKeyValues("").toString());
        assertEquals("key=value", new UrlFormEncodedKeyValues("key=value").toString());
        assertEquals("key=value&anotherKey=anotherValue",
                new UrlFormEncodedKeyValues("key=value&anotherKey=anotherValue").toString());
        assertEquals("key=value&key=value2&anotherKey=anotherValue",
                new UrlFormEncodedKeyValues("key=value&key=value2&anotherKey=anotherValue").toString());
        assertEquals(
                "key=value&key=value2&anotherKey=anotherValue&another%26Key=anotherValue&another%3DKey=anotherValue",
                new UrlFormEncodedKeyValues(
                        "key=value&key=value2&anotherKey=anotherValue&another%26Key=anotherValue&another%3DKey=anotherValue")
                        .toString());
    }


    private <T extends Parameter> void assertIteratorsEqual(Iterator<T> expected, Iterator<T> actual)
    {
        assertEquals(expected.hasNext(), actual.hasNext());
        while (actual.hasNext())
        {
            assertEquals(expected.next(), actual.next());
        }
        assertEquals(expected.hasNext(), actual.hasNext());
    }
}