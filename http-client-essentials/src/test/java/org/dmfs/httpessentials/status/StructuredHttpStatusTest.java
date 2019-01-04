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

package org.dmfs.httpessentials.status;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StructuredHttpStatusTest
{

    @Test
    public void testHashCode()
    {
        // hashCode is defined to return the status code
        assertEquals(200, new StructuredHttpStatus(200, "someReason").hashCode());
    }


    @Test
    public void testStatusCode()
    {
        assertEquals(312, new StructuredHttpStatus(312, "someReason").statusCode());
    }


    @Test
    public void testReason()
    {
        assertEquals("some Reason", new StructuredHttpStatus(312, "some Reason").reason());
    }


    @Test
    public void testEqualsObject()
    {
        assertTrue(new StructuredHttpStatus(100, "Reason").equals(new StructuredHttpStatus(100, "Reason")));
        assertTrue(new StructuredHttpStatus(100, "Reason").equals(new StructuredHttpStatus(100, "other Reason")));
        assertFalse(new StructuredHttpStatus(100, "Reason").equals(new StructuredHttpStatus(101, "Reason")));
        assertFalse(new StructuredHttpStatus(100, "Reason").equals(new StructuredHttpStatus(101, "other Reason")));
    }

}
