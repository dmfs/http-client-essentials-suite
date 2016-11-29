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

package org.dmfs.httpessentials.status;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class NoneHttpStatusTest
{

    @Test
    public void testHashCode()
    {
        assertTrue(new NoneHttpStatus().hashCode() < 0);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testStatusCode()
    {
        new NoneHttpStatus().statusCode();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testReason()
    {
        new NoneHttpStatus().statusCode();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testHttpStatusLine()
    {
        new NoneHttpStatus().statusCode();
    }


    @Test
    public void testIsInformational()
    {
        assertFalse(new NoneHttpStatus().isInformational());
    }


    @Test
    public void testIsSuccess()
    {
        assertFalse(new NoneHttpStatus().isSuccess());
    }


    @Test
    public void testIsRedirect()
    {
        assertFalse(new NoneHttpStatus().isRedirect());
    }


    @Test
    public void testIsClientError()
    {
        assertFalse(new NoneHttpStatus().isClientError());
    }


    @Test
    public void testIsServerError()
    {
        assertFalse(new NoneHttpStatus().isServerError());
    }


    @Test
    public void testEqualsObject()
    {
        assertFalse(new NoneHttpStatus().equals(new SimpleHttpStatus(200, "Some Reason")));
        assertTrue(new NoneHttpStatus().equals(new NoneHttpStatus()));
    }
}
