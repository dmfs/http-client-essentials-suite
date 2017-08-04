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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.dmfs.httpessentials.HttpMethod;
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
 * An {@link HttpRequestExecutor} based on OkHttp.
 *
 * @author Marten Gajda
 */
public final class PlainOkHttpExecutor implements HttpRequestExecutor
{

    private final Single<OkHttpClient> mOkHttpClient;


    public PlainOkHttpExecutor(Single<OkHttpClient> okHttpClient)
    {
        mOkHttpClient = okHttpClient;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(uri.toURL());
        HttpMethod method = request.method();

        for (Header<?> header : request.headers())
        {
            requestBuilder.addHeader(header.type().name(), header.toString());
        }

        requestBuilder.method(method.verb(), method.supportsRequestPayload() ? new OkHttpRequestBody(request.requestEntity()) : null);

        HttpResponse httpResponse = new OkHttpResponse(mOkHttpClient.value().newCall(requestBuilder.build()).execute(), uri);
        return request.responseHandler(httpResponse).handleResponse(httpResponse);
    }
}
