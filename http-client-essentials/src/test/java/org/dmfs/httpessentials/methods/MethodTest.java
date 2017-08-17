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


public class MethodTest
{

    @Test
    public void testIsSafe()
    {
        assertFalse(new Method("BOGUS", false).isSafe());
        assertFalse(new Method("BOGUS", true).isSafe());
    }


    @Test
    public void testIsIdempotent()
    {
        assertFalse(new Method("BOGUS", false).isIdempotent());
        assertFalse(new Method("BOGUS", true).isIdempotent());
    }


    @Test
    public void testVerb()
    {
        assertEquals("BOGUS", new Method("BOGUS", false).verb());
        assertEquals("BOGUS", new Method("BOGUS", true).verb());
    }


    @Test
    public void testSupportsRequestPayload()
    {
        assertFalse(new Method("BOGUS", false).supportsRequestPayload());
        assertTrue(new Method("BOGUS", true).supportsRequestPayload());
    }


    @Test
    public void testEqualsObject()
    {
        assertTrue(new Method("BOGUS", false).equals(new Method("BOGUS", false)));
        assertTrue(new Method("BOGUS", false).equals(new Method("BOGUS", true)));
        assertFalse(new Method("BOGUS", false).equals(new Method("FAKE", false)));
        assertFalse(new Method("BOGUS", false).equals(new Method("FAKE", true)));
    }

}
