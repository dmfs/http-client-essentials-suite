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

package org.dmfs.httpessentials.mockutils.requests;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.OutputStream;


/**
 * An empty {@link HttpRequest} for testing.
 *
 * @author Gabor Keszthelyi
 */
public class EmptyRequest<T> implements HttpRequest<T>
{
    @Override
    public HttpMethod method()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public Headers headers()
    {
        return EmptyHeaders.INSTANCE;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new EmptyHttpRequestEntity();
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        throw new UnsupportedOperationException();
    }


    private final class EmptyHttpRequestEntity implements HttpRequestEntity
    {

        @Override
        public MediaType contentType()
        {
            return null;
        }


        @Override
        public long contentLength() throws IOException
        {
            return -1;
        }


        @Override
        public void writeContent(OutputStream out) throws IOException
        {
        }

    }
}
