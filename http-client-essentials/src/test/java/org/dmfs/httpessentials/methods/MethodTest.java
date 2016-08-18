package org.dmfs.httpessentials.methods;

import org.junit.Test;

import static org.junit.Assert.*;


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
