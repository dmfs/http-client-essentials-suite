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

package org.dmfs.httpessentials.mockutils.responses;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;

import java.io.IOException;
import java.net.URI;


/**
 * A static {@link HttpResponse} that always contains the same {@link Headers} and {@link HttpResponseEntity}.
 *
 * @author Marten Gajda
 */
public class StaticMockResponse implements HttpResponse
{
    private final HttpStatus mResponseStatus;
    private final Headers mResponseHeaders;
    private final HttpResponseEntity mResponseEntity;


    public StaticMockResponse(HttpStatus responseStatus, Headers responseHeaders, HttpResponseEntity responseEntity) throws IOException
    {
        mResponseStatus = responseStatus;
        if (responseEntity.contentType() != null)
        {
            // add content-type header if there is any content-type
            mResponseHeaders = responseHeaders.withHeader(
                    HttpHeaders.CONTENT_TYPE.entity(responseEntity.contentType()));
        }
        else
        {
            mResponseHeaders = responseHeaders;
        }
        mResponseEntity = responseEntity;
    }


    @Override
    public HttpStatus status()
    {
        return mResponseStatus;
    }


    @Override
    public Headers headers()
    {
        return mResponseHeaders;
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return mResponseEntity;
    }


    @Override
    public URI requestUri()
    {
        throw new UnsupportedOperationException("this response doesn't define a request uri, use a decorator for that");
    }


    @Override
    public URI responseUri()
    {
        throw new UnsupportedOperationException("this response doesn't define a request uri, use a decorator for that");
    }

}
