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

package org.dmfs.httpessentials.executors.logging;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class InputStreamDecorated implements HttpResponse
{
    // TODO


    @Override
    public HttpStatus status()
    {
        return null;
    }


    @Override
    public Headers headers()
    {
        return null;
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return null;
    }


    @Override
    public URI requestUri()
    {
        return null;
    }


    @Override
    public URI responseUri()
    {
        return null;
    }
}
