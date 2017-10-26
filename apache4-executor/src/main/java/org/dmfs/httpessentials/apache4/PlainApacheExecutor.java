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

package org.dmfs.httpessentials.apache4;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.jems.single.Single;

import java.io.IOException;
import java.net.URI;


/**
 * An {@link HttpRequestExecutor} for the (deprecated) Apache HTTP client version found on Android.
 *
 * @author Marten Gajda
 */
public final class PlainApacheExecutor implements HttpRequestExecutor
{

    private final Single<HttpClient> mClient;


    public PlainApacheExecutor(Single<HttpClient> client)
    {
        mClient = client;
    }


    @Override
    public <T> T execute(final URI uri, final HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        HttpUriRequest apacheRequest = request.method().supportsRequestPayload()
                ? new ApacheEntityRequest<>(uri, request)
                : new ApacheRequest<>(request, uri);

        for (Header header : request.headers())
        {
            apacheRequest.setHeader(header.type().name(), header.toString());
        }

        HttpResponse response = new EssentialsResponse(uri, mClient.value().execute(apacheRequest));
        return request.responseHandler(response).handleResponse(response);
    }

}
