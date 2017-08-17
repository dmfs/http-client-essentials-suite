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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;


/**
 * A {@link HttpResponseHandler} that uses the given request's response handler with only overriding the response's requestUri to the give one.
 *
 * @author Gabor Keszthelyi
 */
final class RequestUriOverridingResponseHandler<T> implements HttpResponseHandler<T>
{

    private final HttpRequest<T> mRequest;
    private final URI mRequestUriToUse;


    public RequestUriOverridingResponseHandler(HttpRequest<T> request, URI requestUriToUse)
    {
        mRequest = request;
        mRequestUriToUse = requestUriToUse;
    }


    @Override
    public T handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        HttpResponse uriOverridingResponse = new RequestUriOverridingResponse(response, mRequestUriToUse);
        return mRequest
                .responseHandler(uriOverridingResponse)
                .handleResponse(uriOverridingResponse);
    }
}
