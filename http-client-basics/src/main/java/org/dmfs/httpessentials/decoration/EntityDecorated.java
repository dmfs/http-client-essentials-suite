/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.decoration;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.Headers;

import java.io.IOException;


/**
 * {@link HttpRequest} decorator that only modifies the {@link HttpRequestEntity} using the given {@link Decoration}.
 *
 * @author Gabor Keszthelyi
 */
public final class EntityDecorated<T> implements HttpRequest<T>
{

    private final HttpRequest<T> mOriginalRequest;
    private final Decoration<HttpRequestEntity> mRequestEntityDecoration;


    public EntityDecorated(HttpRequest<T> originalRequest, Decoration<HttpRequestEntity> requestEntityDecoration)
    {
        mOriginalRequest = originalRequest;
        mRequestEntityDecoration = requestEntityDecoration;
    }


    @Override
    public HttpMethod method()
    {
        return mOriginalRequest.method();
    }


    @Override
    public Headers headers()
    {
        return mOriginalRequest.headers();
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return mRequestEntityDecoration.decorated(mOriginalRequest.requestEntity());
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        return mOriginalRequest.responseHandler(response);
    }
}
