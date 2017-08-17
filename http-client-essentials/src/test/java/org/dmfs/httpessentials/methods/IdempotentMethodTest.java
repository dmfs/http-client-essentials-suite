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

package org.dmfs.httpessentials.methods;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class IdempotentMethodTest
{

    @Test
    public void testIsSafe()
    {
        assertFalse(new IdempotentMethod("BOGUS", false).isSafe());
        assertFalse(new IdempotentMethod("BOGUS", true).isSafe());
    }


    @Test
    public void testIsIdempotent()
    {
        assertTrue(new IdempotentMethod("BOGUS", false).isIdempotent());
        assertTrue(new IdempotentMethod("BOGUS", true).isIdempotent());
    }


    @Test
    public void testVerb()
    {
        assertEquals("BOGUS", new IdempotentMethod("BOGUS", false).verb());
        assertEquals("BOGUS", new IdempotentMethod("BOGUS", true).verb());
    }


    @Test
    public void testSupportsRequestPayload()
    {
        assertFalse(new IdempotentMethod("BOGUS", false).supportsRequestPayload());
        assertTrue(new IdempotentMethod("BOGUS", true).supportsRequestPayload());
    }


    @Test
    public void testEqualsObject()
    {
        assertTrue(new IdempotentMethod("BOGUS", false).equals(new IdempotentMethod("BOGUS", false)));
        assertTrue(new IdempotentMethod("BOGUS", false).equals(new IdempotentMethod("BOGUS", true)));
        assertFalse(new IdempotentMethod("BOGUS", false).equals(new IdempotentMethod("FAKE", false)));
        assertFalse(new IdempotentMethod("BOGUS", false).equals(new IdempotentMethod("FAKE", true)));
    }

}
