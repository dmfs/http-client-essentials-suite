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


/*
 * TODO: test handling of invalid status lines
 */
public class StatusLineHttpStatusTest
{

    @Test
    public void testHashCode()
    {
        // hashCode is defined to return the status code
        assertEquals(200, new StatusLineHttpStatus("HTTP/1.1 200 Some Reason").hashCode());
    }


    @Test
    public void testStatusCode()
    {
        assertEquals(312, new StatusLineHttpStatus("HTTP/1.1 312 Some Reason").statusCode());
    }


    @Test
    public void testReason()
    {
        assertEquals("Some Reason", new StatusLineHttpStatus("HTTP/1.1 200 Some Reason").reason());
    }


    @Test
    public void testHttpStatusLine()
    {
        assertEquals("HTTP/2.0 355 Some Reason",
                new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").httpStatusLine(2, 0));
        assertEquals("HTTP/2.1 355 Some Reason",
                new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").httpStatusLine(2, 1));
        assertEquals("HTTP/1.1 355 Some Reason",
                new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").httpStatusLine(1, 1));
        assertEquals("HTTP/1.0 355 Some Reason",
                new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").httpStatusLine(1, 0));
    }


    @Test
    public void testIsInformational()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 155 Some Reason").isInformational());
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").isInformational());
    }


    @Test
    public void testIsSuccess()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 255 Some Reason").isSuccess());
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").isSuccess());
    }


    @Test
    public void testIsRedirect()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").isRedirect());
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 455 Some Reason").isRedirect());
    }


    @Test
    public void testIsClientError()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 455 Some Reason").isClientError());
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").isClientError());
    }


    @Test
    public void testIsServerError()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 555 Some Reason").isServerError());
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 355 Some Reason").isServerError());
    }


    @Test
    public void testEqualsObject()
    {
        assertTrue(new StatusLineHttpStatus("HTTP/1.1 100 Some Reason").equals(new SimpleHttpStatus(100, "Reason")));
        assertTrue(
                new StatusLineHttpStatus("HTTP/1.1 100 Some Reason").equals(new SimpleHttpStatus(100, "Some Reason")));
        assertFalse(new StatusLineHttpStatus("HTTP/1.1 100 Some Reason").equals(new SimpleHttpStatus(101, "Reason")));
        assertFalse(
                new StatusLineHttpStatus("HTTP/1.1 100 Some Reason").equals(new SimpleHttpStatus(101, "Some Reason")));
    }

}
