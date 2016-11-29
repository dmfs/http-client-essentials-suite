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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.status.SimpleHttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * An {@link HttpResponse} adapter for {@link HttpURLConnection}s.
 *
 * @author Marten Gajda
 */
final class HttpUrlConnectionResponse implements HttpResponse
{
    private final URI mRequestUri;
    private final HttpURLConnection mConnection;


    /**
     * Creates an {@link HttpResponse} for the given {@link HttpURLConnection}.
     *
     * @param resquestUri
     *         The URL the connection was directed to.
     * @param connection
     */
    public HttpUrlConnectionResponse(URI resquestUri, HttpURLConnection connection)
    {
        this.mRequestUri = resquestUri;
        this.mConnection = connection;
    }


    @Override
    public HttpStatus status()
    {
        try
        {
            return new SimpleHttpStatus(mConnection.getResponseCode(), mConnection.getResponseMessage());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't get response code", e);
        }
    }


    @Override
    public Headers headers()
    {
        return new HttpUrlConnectionHeaders(mConnection);
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return new HttpUrlConnectionResponseEntity(mConnection);
    }


    @Override
    public URI requestUri()
    {
        return mRequestUri;
    }


    @Override
    public URI responseUri()
    {
        try
        {
            return mConnection.getURL().toURI();
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException("Can't retrieve response URI", e);
        }
    }
}