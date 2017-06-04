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

package org.dmfs.httpessentials.cache;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;


/**
 * An {@link HttpResponse} that duplicates the response payload into a given {@link OutputStream}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class DuplicatingResponse implements HttpResponse
{
    private final HttpResponse mDelegate;
    private final HttpResponseEntity mEntity;


    public DuplicatingResponse(HttpResponse response, OutputStream outputStream) throws IOException
    {
        mDelegate = response;
        mEntity = new DuplicatingResponseEntity(mDelegate.responseEntity(), outputStream);
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
        return mEntity;
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
