/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * A {@link HttpResponse} decorator that overrides the {@link #requestUri()} value with the given one.
 *
 * @author Gabor Keszthelyi
 */
final class RequestUriOverridingResponse implements HttpResponse
{

    private final HttpResponse mResponse;
    private final URI mRequestUriToKeep;


    public RequestUriOverridingResponse(HttpResponse response, URI requestUriToKeep)
    {
        mResponse = response;
        mRequestUriToKeep = requestUriToKeep;
    }


    @Override
    public HttpStatus status()
    {
        return mResponse.status();
    }


    @Override
    public Headers headers()
    {
        return mResponse.headers();
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return mResponse.responseEntity();
    }


    @Override
    public URI requestUri()
    {
        return mRequestUriToKeep;
    }


    @Override
    public URI responseUri()
    {
        return mResponse.responseUri();
    }
}
