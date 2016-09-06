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

package org.dmfs.httpessentials.converters;

import org.dmfs.httpessentials.types.StructuredMediaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author marten
 */
public class MediaTypeConverterTest
{
    @Test
    public void value() throws Exception
    {
        assertEquals(new StructuredMediaType("application", "test"),
                new MediaTypeConverter().value("application/test"));
        assertEquals(new StructuredMediaType("application", "test", "latin-1"),
                new MediaTypeConverter().value("application/test; charset=latin-1"));
    }


    @Test
    public void valueString() throws Exception
    {
        assertEquals("application/test",
                new MediaTypeConverter().valueString(new StructuredMediaType("application", "test")));
        assertEquals("application/test;charset=latin-1",
                new MediaTypeConverter().valueString(new StructuredMediaType("application", "test", "latin-1")));

    }

}