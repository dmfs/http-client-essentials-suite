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

package org.dmfs.httpessentials.executors.logging.io;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author Gabor Keszthelyi
 */
public final class OutputStreamDecorated<T> implements HttpRequest<T>
{
    private final HttpRequest<T> mRequest;
    private final Decoration<OutputStream> mOutputStreamDecoration;


    public OutputStreamDecorated(HttpRequest<T> request, Decoration<OutputStream> outputStreamDecoration)
    {
        mRequest = request;
        mOutputStreamDecoration = outputStreamDecoration;
    }


    @Override
    public HttpMethod method()
    {
        return mRequest.method();
    }


    @Override
    public Headers headers()
    {
        return mRequest.headers();
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new HttpRequestEntity()
        {
            @Override
            public MediaType contentType()
            {
                return mRequest.requestEntity().contentType();
            }


            @Override
            public long contentLength() throws IOException
            {
                return mRequest.requestEntity().contentLength();
            }


            @Override
            public void writeContent(final OutputStream out) throws IOException
            {
                mRequest.requestEntity().writeContent(mOutputStreamDecoration.decorated(out));
            }
        };
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        return mRequest.responseHandler(response);
    }
}
