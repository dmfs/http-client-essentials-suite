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

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class InputStreamDecorated implements HttpResponse
{
    private final HttpResponse mDelegate;
    private final Decoration<InputStream> mInputStreamDecoration;


    public InputStreamDecorated(HttpResponse delegate, Decoration<InputStream> inputStreamDecoration)
    {
        mDelegate = delegate;
        mInputStreamDecoration = inputStreamDecoration;
    }


    @Override
    public HttpStatus status()
    {
        return mDelegate.status();
    }


    @Override
    public Headers headers()
    {
        return mDelegate.headers();
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return new HttpResponseEntity()
        {
            @Override
            public MediaType contentType() throws IOException
            {
                return mDelegate.responseEntity().contentType();
            }


            @Override
            public long contentLength() throws IOException
            {
                return mDelegate.responseEntity().contentLength();
            }


            @Override
            public InputStream contentStream() throws IOException
            {
                return mInputStreamDecoration.decorated(mDelegate.responseEntity().contentStream());
            }
        };
    }


    @Override
    public URI requestUri()
    {
        return mDelegate.requestUri();
    }


    @Override
    public URI responseUri()
    {
        return mDelegate.responseUri();
    }
}
