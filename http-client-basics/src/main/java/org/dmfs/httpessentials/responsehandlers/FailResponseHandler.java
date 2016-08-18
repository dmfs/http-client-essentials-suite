/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.*;

import java.io.IOException;


/**
 * An default response handler that doesn't handle the response but throws the most appropriate Exception, based on the
 * response status code. Use {@link #getInstance()} to get an instance.
 *
 * @param <T>
 *         The type of the expected response.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class FailResponseHandler<T> implements HttpResponseHandler<T>
{

    private final static HttpResponseHandler<?> INSTANCE = new FailResponseHandler<Object>();


    @SuppressWarnings("unchecked")
    public static <V> HttpResponseHandler<V> getInstance()
    {
        // this HttpResponseHandler will always throw an Exception, so the actual response type doesn't matter at all.
        return (HttpResponseHandler<V>) INSTANCE;
    }


    @Override
    public T handleResponse(final HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        try
        {
            HttpStatus status = response.status();
            if (status.isClientError())
            {
                if (HttpStatus.NOT_FOUND.equals(status))
                {
                    throw new NotFoundException(response.responseUri(),
                            String.format("Resource at '%s' not found.", response.responseUri().toASCIIString()));
                }

                if (HttpStatus.UNAUTHORIZED.equals(status))
                {
                    throw new UnauthorizedException(
                            String.format("Authentication at '%s' failed.", response.responseUri().toASCIIString()));
                }

                throw new ClientErrorException(status,
                        String.format("'%s' returned a client error: '%d %s'", response.responseUri().toASCIIString(),
                                status.statusCode(), status.reason(), response));
            }

            if (status.isServerError())
            {
                throw new ServerErrorException(status,
                        String.format("'%s' returned a server error: '%d %s'", response.responseUri().toASCIIString(),
                                status.statusCode(), status.reason()));
            }

            throw new UnexpectedStatusException(status,
                    String.format("Unexpected status code '%d %s' returned from '%s'", status.statusCode(),
                            status.reason(), response.responseUri().toASCIIString()));
        }
        finally
        {
            response.responseEntity().contentStream().close();
        }
    }
}
