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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.httpurlconnection.factories.DefaultHttpUrlConnectionFactory;
import org.dmfs.httpessentials.httpurlconnection.factories.decorators.Finite;
import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.SafeToken;
import org.dmfs.httpessentials.types.VersionedProduct;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;


/**
 * An {@link HttpRequestExecutor} that's based on Java's HttpUrlConnection.
 * <p/>
 * Note that this implementation is constrained by the underlying {@link HttpURLConnection} implementation. In
 * particular that means you should not rely on being able to use certain HTTP methods like {@code PROPFIND}. Standard
 * HTTP methods should always work though.
 * <p/>
 * Also note that this, by convention, won't follow redirects.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class HttpUrlConnectionExecutor implements HttpRequestExecutor
{
    private static final String USER_AGENT_SUFFIX;

    static
    {
        String systemHttpAgent = System.getProperty("http.agent");
        Product product = new VersionedProduct(new SafeToken(BuildConfig.NAME), new SafeToken(BuildConfig.VERSION));
        USER_AGENT_SUFFIX = systemHttpAgent == null ? product.toString() : product.toString() + " " + systemHttpAgent;
    }

    private final HttpUrlConnectionFactory mConnectionFactory;


    /**
     * Creates an {@link HttpUrlConnectionExecutor} using a {@link Finite} {@link DefaultHttpUrlConnectionFactory} with
     * default timeouts.
     */
    public HttpUrlConnectionExecutor()
    {
        this(new Finite(new DefaultHttpUrlConnectionFactory()));
    }


    /**
     * Creates an {@link HttpUrlConnectionExecutor} that uses the given {@link HttpUrlConnectionFactory}.
     *
     * @param connectionFactory
     */
    public HttpUrlConnectionExecutor(final HttpUrlConnectionFactory connectionFactory)
    {
        mConnectionFactory = connectionFactory;
    }


    @Override
    public <T> T execute(final URI uri, final HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException,
            UnexpectedStatusException
    {
        HttpResponse r = sendRequest(uri, request);
        return request.responseHandler(r).handleResponse(r);
    }


    /**
     * Sends the request and returns an {@link HttpResponse}.
     *
     * @param uri
     *         The URL to connect to.
     * @param request
     *         The {@link HttpRequest} to send.
     *
     * @return An {@link HttpResponse}.
     *
     * @throws MalformedURLException
     * @throws IOException
     * @throws ProtocolException
     */
    private <T> HttpResponse sendRequest(final URI uri, final HttpRequest<T> request) throws IOException, ProtocolException
    {
        final HttpURLConnection connection;
        try
        {
            connection = mConnectionFactory.httpUrlConnection(uri);
        }
        catch (IllegalArgumentException e)
        {
            throw new ProtocolException("The given URI is not applicable to HTTP requests.", e);
        }
        // ensure the expected redirect behavior
        connection.setInstanceFollowRedirects(false);
        // set the request method
        connection.setDoOutput(request.method().supportsRequestPayload());
        connection.setRequestMethod(request.method().verb());
        // add all headers
        for (Header<?> header : request.headers())
        {
            if ("user-agent".equalsIgnoreCase(header.type().name()))
            {
                connection.setRequestProperty(header.type().name(), header.toString() + " " + USER_AGENT_SUFFIX);
            }
            else
            {
                connection.setRequestProperty(header.type().name(), header.toString());
            }
        }
        // also set the content-type header if we have any content-type
        if (request.requestEntity().contentType() != null)
        {
            connection.setRequestProperty(HttpHeaders.CONTENT_TYPE.name(),
                    HttpHeaders.CONTENT_TYPE.valueString(request.requestEntity().contentType()));
        }
        // send the request entity if applicable
        if (request.method().supportsRequestPayload())
        {
            OutputStream out = connection.getOutputStream();
            try
            {
                request.requestEntity().writeContent(out);
            }
            finally
            {
                out.close();
            }
        }
        // return the response
        return new HttpUrlConnectionResponse(uri, connection);
    }

}
