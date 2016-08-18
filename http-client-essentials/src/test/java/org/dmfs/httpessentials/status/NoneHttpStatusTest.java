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
