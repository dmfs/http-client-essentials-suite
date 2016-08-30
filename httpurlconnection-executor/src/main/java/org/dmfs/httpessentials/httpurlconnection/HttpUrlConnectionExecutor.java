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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.types.VersionedProduct;

import java.io.IOException;
import java.net.URI;


/**
 * An {@link HttpRequestExecutor} that uses Java's HttpUrlConnection (through {@link PlainHttpUrlConnectionExecutor})
 * and appends its name and version, and the platform's description (<code>http.agent</code> system property) to
 * existing User-Agent request headers.
 *
 * @author Gabor Keszthelyi
 */
public final class HttpUrlConnectionExecutor implements HttpRequestExecutor
{
    private final HttpRequestExecutor mExecutor;


    public HttpUrlConnectionExecutor()
    {
        this(new PlainHttpUrlConnectionExecutor());
    }


    public HttpUrlConnectionExecutor(HttpUrlConnectionFactory connectionFactory)
    {
        this(new PlainHttpUrlConnectionExecutor(connectionFactory));
    }


    private HttpUrlConnectionExecutor(HttpRequestExecutor executor)
    {
        mExecutor = new BottomBranded(
                new BottomBranded(executor, Platform.INSTANCE),
                new VersionedProduct(BuildConfig.NAME, BuildConfig.VERSION));
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mExecutor.execute(uri, request);
    }
}
