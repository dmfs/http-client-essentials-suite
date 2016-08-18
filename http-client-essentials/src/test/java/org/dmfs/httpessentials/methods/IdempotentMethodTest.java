package org.dmfs.httpessentials.methods;

import org.junit.Test;

import static org.junit.Assert.*;


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
