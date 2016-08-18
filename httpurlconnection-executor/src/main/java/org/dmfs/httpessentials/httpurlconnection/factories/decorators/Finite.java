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

package org.dmfs.httpessentials.httpurlconnection.factories.decorators;

import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;


/**
 * A {@link HttpUrlConnectionFactory} decorator that sets connection and socket timeouts on the connection (rather than
 * waiting forever).
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Finite implements HttpUrlConnectionFactory
{
    public final static int DEFAULT_SOCKET_TIMEOUT = 120000; // 2 minutes
    public final static int DEFAULT_CONNECTION_TIMEOUT = 10000; // 10 seconds

    private final HttpUrlConnectionFactory mDecorated;
    private final int mConnectionTimeout;
    private final int mSocketTimeout;


    /**
     * An {@link HttpUrlConnectionFactory} decorator that sets default connection timeout and socket (read) timeout on
     * the connections returned by the decorated {@link HttpUrlConnectionFactory}. The default connection timeout is
     * {@value #DEFAULT_CONNECTION_TIMEOUT} milliseconds and the default socket timeout is {@value
     * #DEFAULT_SOCKET_TIMEOUT} milliseconds.
     *
     * @param decorated
     *         Another {@link HttpUrlConnectionFactory}.
     */
    public Finite(HttpUrlConnectionFactory decorated)
    {
        this(decorated, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }


    /**
     * An {@link HttpUrlConnectionFactory} decorator that sets connection timeout and socket (read) timeout on the
     * connections returned by the decorated {@link HttpUrlConnectionFactory}.
     *
     * @param decorated
     *         Another {@link HttpUrlConnectionFactory}.
     * @param connectionTimeout
     *         The connection timeout in milliseconds. A value {@code <0} will preserve the current setting, a value of
     *         {@code 0} will set an infinite timeout.
     * @param socketTimeout
     *         The read timeout in milliseconds. A value {@code <0} will preserve the current setting, a value of {@code
     *         0} will set an infinite timeout.
     */
    public Finite(HttpUrlConnectionFactory decorated, int connectionTimeout, int socketTimeout)
    {
        mDecorated = decorated;
        mConnectionTimeout = connectionTimeout;
        mSocketTimeout = socketTimeout;
    }


    @Override
    public HttpURLConnection httpUrlConnection(URI uri) throws IllegalArgumentException, IOException
    {
        HttpURLConnection connection = mDecorated.httpUrlConnection(uri);
        if (mConnectionTimeout >= 0)
        {
            connection.setConnectTimeout(mConnectionTimeout);
        }
        if (mSocketTimeout >= 0)
        {
            connection.setReadTimeout(mSocketTimeout);
        }
        return connection;
    }

}
