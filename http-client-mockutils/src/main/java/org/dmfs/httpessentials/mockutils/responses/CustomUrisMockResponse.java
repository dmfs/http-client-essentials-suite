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
package org.dmfs.httpessentials.mockutils.responses;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * An {@link HttpResponse} that overrides request and response uri of another {@link HttpResponse}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class CustomUrisMockResponse implements HttpResponse
{
    private final URI mRequestUri;
    private final URI mResponseUri;
    private final HttpResponse mDecorated;


    public CustomUrisMockResponse(HttpResponse decorated, URI requestUri)
    {
        this(decorated, requestUri, requestUri);
    }


    public CustomUrisMockResponse(HttpResponse decorated, URI requestUri, URI responseUri)
    {
        mRequestUri = requestUri;
        mResponseUri = responseUri;
        mDecorated = decorated;
    }


    @Override
    public HttpStatus status()
    {
        return mDecorated.status();
    }


    @Override
    public Headers headers()
    {
        return mDecorated.headers();
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return mDecorated.responseEntity();
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