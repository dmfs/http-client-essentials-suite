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

package org.dmfs.httpessentials.okhttp;

import okhttp3.Response;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.status.SimpleHttpStatus;

import java.net.URI;


/**
 * An {@link HttpResponse} adapter for OkHttp {@link Response}s.
 *
 * @author Marten Gajda
 */
final class OkHttpResponse implements HttpResponse
{
    private final Response mResponse;
    private final URI mRequestUri;


    public OkHttpResponse(Response response, URI requestUri)
    {
        mResponse = response;
        mRequestUri = requestUri;
    }


    @Override
    public HttpStatus status()
    {
        return new SimpleHttpStatus(mResponse.code(), mResponse.message());
    }


    @Override
    public Headers headers()
    {
        return new OkHttpResponseHeaders(mResponse);
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return new OkHttpResponseEntity(mResponse.body());
    }


    @Override
    public URI requestUri()
    {
        return mRequestUri;
    }


    @Override
    public URI responseUri()
    {
        // TODO: how do we get the actual response URI (in case OkHttp followed a redirect)?
        return mRequestUri;
    }
}
