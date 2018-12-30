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
import org.dmfs.httpessentials.exceptions.ClientErrorException;
import org.dmfs.httpessentials.exceptions.NotFoundException;
import org.dmfs.httpessentials.exceptions.ServerErrorException;
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.status.SimpleHttpStatus;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * @author marten
 */
public class FailResponseHandlerTest
{
    @Test
    public void getInstance()
    {
        assertNotNull(FailResponseHandler.getInstance());
    }


    @Test
    public void handleResponse()
    {
        final AtomicReference<Boolean> streamClosed = new AtomicReference<>();
        FailResponseHandler<String> testHandler = new FailResponseHandler<>();

        for (int i = 200; i < 600; ++i)
        {
            final HttpStatus status = new SimpleHttpStatus(i, "UNKNOWN");

            try
            {
                testHandler.handleResponse(new MockHttpResponse(status, streamClosed));
                fail("Did not throw");
            }
            catch (UnauthorizedException e)
            {
                assertEquals(HttpStatus.UNAUTHORIZED, status);
            }
            catch (NotFoundException e)
            {
                assertEquals(HttpStatus.NOT_FOUND, status);
            }
            catch (ClientErrorException e)
            {
                assertTrue(status.isClientError());
            }
            catch (ServerErrorException e)
            {
                assertTrue(status.isServerError());
            }
            catch (UnexpectedStatusException e)
            {
                assertTrue(i < 400);
            }
            catch (Exception e)
            {
                fail(String.format("Unexpected Exception thrown: %s", e.getClass().getName()));
            }
        }
    }


    private static class MockHttpResponse implements HttpResponse
    {
        private final HttpStatus status;
        private final AtomicReference<Boolean> streamClosed;


        public MockHttpResponse(HttpStatus status, AtomicReference<Boolean> streamClosed)
        {
            this.status = status;
            this.streamClosed = streamClosed;
        }


        @Override
        public HttpStatus status()
        {
            return status;
        }


        @Override
        public Headers headers()
        {
            fail("headers should not be called");
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
                public InputStream contentStream()
                {
                    return new InputStream()
                    {
                        @Override
                        public int read()
                        {
                            fail("Trivial Response should not read the actual response entity.");
                            return 0;
                        }


                        @Override
                        public void close()
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
            return URI.create("http://example.com/request");
        }


        @Override
        public URI responseUri()
        {
            return URI.create("http://example.com/response");
        }
    }
}