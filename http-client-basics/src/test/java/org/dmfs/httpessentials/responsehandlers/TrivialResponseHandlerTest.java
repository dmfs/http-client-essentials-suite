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

package org.dmfs.httpessentials.responsehandlers;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * @author marten
 */
public class TrivialResponseHandlerTest
{
    @Test
    public void handleResponse() throws Exception
    {
        final AtomicReference<Boolean> streamClosed = new AtomicReference<>();
        HttpResponse response = new MockHttpResponse(streamClosed);

        assertEquals("TESTVALUE", new TrivialResponseHandler<>("TESTVALUE").handleResponse(response));
        assertTrue(streamClosed.get());
    }


    private static class MockHttpResponse implements HttpResponse
    {
        private final AtomicReference<Boolean> streamClosed;


        public MockHttpResponse(AtomicReference<Boolean> streamClosed)
        {
            this.streamClosed = streamClosed;
        }


        @Override
        public HttpStatus status()
        {
            fail("Trivial Response should not depend on the actual response status");
            return null;
        }


        @Override
        public Headers headers()
        {
            fail("Trivial Response should not depend on the actual response headers");
            return null;
        }


        @Override
        public HttpResponseEntity responseEntity()
        {
            return new HttpResponseEntity()
            {
                @Override
                public Optional<MediaType> contentType()
                {
                    fail("Trivial Response should not depend on the actual response entity");
                    return Absent.absent();
                }


                @Override
                public Optional<Long> contentLength()
                {
                    fail("Trivial Response should not depend on the actual response content length");
                    return Absent.absent();
                }


                @Override
                public InputStream contentStream() throws IOException
                {
                    return new InputStream()
                    {
                        @Override
                        public int read() throws IOException
                        {
                            fail("Trivial Response should not read the actual response entity.");
                            return 0;
                        }


                        @Override
                        public void close() throws IOException
                        {
                            streamClosed.set(true);
                        }
                    };
                }
            };
        }


        @Override
        public URI requestUri()
        {
            fail("Trivial Response should not depend on the actual response request URI");
            return null;
        }


        @Override
        public URI responseUri()
        {
            fail("Trivial Response should not depend on the actual response URI");
            return null;
        }
    }
}