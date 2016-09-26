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

package org.dmfs.httpessentials.executors.logging.formatter;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.executors.logging.BodyLineFormatter;
import org.dmfs.httpessentials.executors.logging.HttpLogFormatter;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;

import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class NoPersonalHeader implements HttpLogFormatter
{
    private final HttpLogFormatter mHttpLogFormatter;


    public NoPersonalHeader(HttpLogFormatter httpLogFormatter)
    {
        mHttpLogFormatter = httpLogFormatter;
    }

    // TODO all methods

    @Override
    public boolean appendRequestMsg(HttpMethod method, URI uri, StringBuilder logMessage)
    {
        return false;
    }


    @Override
    public boolean appendRequestMsg(Header header, StringBuilder logMessage)
    {
        HeaderType authHeader = null; // TODO
        if (header.type().equals(authHeader))
        {
            return false;
        }
        return mHttpLogFormatter.appendRequestMsg(header, logMessage);
    }


    @Override
    public boolean appendRequestMsg(HttpRequestEntity entity, StringBuilder logMessage)
    {
        return false;
    }


    @Override
    public BodyLineFormatter requestBodyFormatter(HttpRequestEntity entity)
    {
        return null;
    }


    @Override
    public boolean responseMsg(HttpStatus status, StringBuilder logMessage)
    {
        return false;
    }


    @Override
    public boolean responseMsg(Header header, HttpStatus status, StringBuilder logMessage)
    {
        return false;
    }


    @Override
    public boolean responseMsg(HttpResponseEntity entity, HttpStatus status, StringBuilder logMessage)
    {
        return false;
    }


    @Override
    public BodyLineFormatter responseBodyFormatter(HttpResponseEntity entity)
    {
        return null;
    }
}
