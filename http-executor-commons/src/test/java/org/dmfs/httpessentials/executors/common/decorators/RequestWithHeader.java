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

package org.dmfs.httpessentials.executors.common.decorators;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.Headers;

import java.io.IOException;


/**
 * Stub {@link HttpRequest} that uses the given {@link Header}.
 *
 * @author Gabor Keszthelyi
 */
public class RequestWithHeader<T> implements HttpRequest<T>
{

    private final Header mHeader;


    public RequestWithHeader(Header header)
    {
        mHeader = header;
    }


    @Override
    public HttpMethod method()
    {
        return null;
    }


    @Override
    public Headers headers()
    {
        return EmptyHeaders.INSTANCE.withHeader(mHeader);
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return null;
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        return null;
    }
}
