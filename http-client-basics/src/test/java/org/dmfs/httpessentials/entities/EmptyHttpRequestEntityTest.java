package org.dmfs.httpessentials.entities;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;


public class EmptyHttpRequestEntityTest
{

    @Test
    public void testContentType()
    {
        // the entity should not return a content-type, since there is no content
        assertNull(EmptyHttpRequestEntity.INSTANCE.contentType());
    }


    @Test
    public void testContentLength() throws IOException
    {
        // there is no content, so it can't have any length, not even 0
        assertEquals(-1, EmptyHttpRequestEntity.INSTANCE.contentLength());
    }


    @Test
    public void testWriteContent() throws IOException
    {
        EmptyHttpRequestEntity.INSTANCE.writeContent(new OutputStream()
        {

            @Override
            public void write(int b) throws IOException
            {
                fail("writeContent did write something!");
            }
        });
    }

}
