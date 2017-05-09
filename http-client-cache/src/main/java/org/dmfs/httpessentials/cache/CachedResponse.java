/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.types.MediaType;

import java.io.InputStream;
import java.net.URI;


/**
 * @author marten
 */
public final class CachedResponse implements HttpResponse
{
    private final HttpStatus mStatus;
    private final URI mRequestUri;
    private final URI mResponseUri;
    private final Headers mResponseHeaders;
    private final InputStream mPayload;


    public CachedResponse(HttpStatus mStatus, URI mRequestUri, URI mResponseUri, Headers mResponseHeaders, InputStream mPayload)
    {
        this.mStatus = mStatus;
        this.mRequestUri = mRequestUri;
        this.mResponseUri = mResponseUri;
        this.mResponseHeaders = mResponseHeaders;
        this.mPayload = mPayload;
    }


    @Override
    public HttpStatus status()
    {
        return mStatus;
    }


    @Override
    public Headers headers()
    {
        return mResponseHeaders;
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        MediaType mediaType = mResponseHeaders.contains(HttpHeaders.CONTENT_TYPE) ? mResponseHeaders.header(HttpHeaders.CONTENT_TYPE).value() : null;
        long length = mResponseHeaders.contains(HttpHeaders.CONTENT_LENGTH) ? mResponseHeaders.header(HttpHeaders.CONTENT_LENGTH).value() : -1;
        return new InputStreamResponseEntity(mPayload, mediaType, length);
    }


    @Override
    public URI requestUri()
    {
        return mRequestUri;
    }


    @Override
    public URI responseUri()
    {
        return mResponseUri;
    }

}
