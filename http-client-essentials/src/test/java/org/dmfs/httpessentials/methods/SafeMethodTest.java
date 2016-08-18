package org.dmfs.httpessentials.methods;

import org.junit.Test;

import static org.junit.Assert.*;


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
