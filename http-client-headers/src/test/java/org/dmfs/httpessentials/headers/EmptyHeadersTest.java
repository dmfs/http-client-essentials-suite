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

package org.dmfs.httpessentials.headers;

import org.dmfs.httpessentials.types.StringMediaType;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EmptyHeadersTest
{

    @Test
    public void testIterator()
    {
        assertFalse(new EmptyHeaders().iterator().hasNext());
    }


    @Test
    public void testContains()
    {
        assertFalse(new EmptyHeaders().contains(HttpHeaders.CONTENT_TYPE));
    }


    @Test(expected = NoSuchElementException.class)
    public void testHeaderSingletonHeaderTypeOfT()
    {
        new EmptyHeaders().header(HttpHeaders.CONTENT_TYPE);
    }


    @Test(expected = NoSuchElementException.class)
    public void testHeaderListHeaderTypeOfT()
    {
        new EmptyHeaders().header(HttpHeaders.LINK);
    }


    @Test
    public void testWithHeader()
    {
        assertTrue(new EmptyHeaders().withHeader(
                HttpHeaders.CONTENT_TYPE.entity(new StringMediaType("application/binary")))
                .contains(HttpHeaders.CONTENT_TYPE));
        assertEquals(new StringMediaType("application/binary"),
                new EmptyHeaders().withHeader(
                        HttpHeaders.CONTENT_TYPE.entity(new StringMediaType("application/binary")))
                        .header(HttpHeaders.CONTENT_TYPE)
                        .value());
    }


    @Test
    public void testWithoutHeaderType()
    {
        assertFalse(new EmptyHeaders().withoutHeaderType(HttpHeaders.CONTENT_TYPE).contains(HttpHeaders.CONTENT_TYPE));
    }

}
