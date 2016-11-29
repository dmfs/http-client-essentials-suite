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

package org.dmfs.httpessentials.methods;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SafeMethodTest
{

    @Test
    public void testIsSafe()
    {
        assertTrue(new SafeMethod("BOGUS", false).isSafe());
        assertTrue(new SafeMethod("BOGUS", true).isSafe());
    }


    @Test
    public void testIsIdempotent()
    {
        assertTrue(new SafeMethod("BOGUS", false).isIdempotent());
        assertTrue(new SafeMethod("BOGUS", true).isIdempotent());
    }


    @Test
    public void testVerb()
    {
        assertEquals("BOGUS", new SafeMethod("BOGUS", false).verb());
        assertEquals("BOGUS", new SafeMethod("BOGUS", true).verb());
    }


    @Test
    public void testSupportsRequestPayload()
    {
        assertFalse(new SafeMethod("BOGUS", false).supportsRequestPayload());
        assertTrue(new SafeMethod("BOGUS", true).supportsRequestPayload());
    }


    @Test
    public void testEqualsObject()
    {
        assertTrue(new SafeMethod("BOGUS", true).equals(new SafeMethod("BOGUS", false)));
        assertTrue(new SafeMethod("BOGUS", true).equals(new SafeMethod("BOGUS", true)));
        assertFalse(new SafeMethod("BOGUS", true).equals(new SafeMethod("FAKE", false)));
        assertFalse(new SafeMethod("BOGUS", true).equals(new SafeMethod("FAKE", true)));
    }

}
