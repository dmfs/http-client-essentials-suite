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

package org.dmfs.httpessentials.mockutils.responses;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * A {@link HttpResponse} decorator that overrides the status or an {@link HttpResponse}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public class CustomStatusMockResponse implements HttpResponse
{

    private final HttpResponse mDecorated;
    private final HttpStatus mStatus;


    public CustomStatusMockResponse(HttpResponse decorated, HttpStatus status)
    {
        mDecorated = decorated;
        mStatus = status;
    }


    @Override
    public HttpStatus status()
    {
        return mStatus;
    }


    @Override
    public Headers headers()
    {
        return mDecorated.headers();
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return mDecorated.responseEntity();
    }


    @Override
    public URI requestUri()
    {
        return mDecorated.requestUri();
    }


    @Override
    public URI responseUri()
    {
        return mDecorated.responseUri();
    }
}
