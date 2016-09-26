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

import java.io.IOException;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class DefaultHttpLogFormatter implements HttpLogFormatter
{
    @Override
    public boolean appendRequestMsg(HttpMethod method, URI uri, StringBuilder logMessage)
    {
        logMessage.append(method).append(" ").append(uri);
        return true;
    }


    @Override
    public boolean appendRequestMsg(Header header, StringBuilder logMessage)
    {
        logMessage.append(header.type().name()).append(" ").append(header.value());
        return true;
    }


    @Override
    public boolean appendRequestMsg(HttpRequestEntity entity, StringBuilder logMessage)
    {
        logMessage.append(entity.contentType());
        return true;
    }


    @Override
    public BodyLineFormatter requestBodyFormatter(final HttpRequestEntity entity)
    {
        final String charset = entity.contentType().charset("UTF-8");
        return new BodyLineFormatter()
        {
            @Override
            public String charset()
            {
                return charset;
            }


            @Override
            public String bodyLineMsg(String aLineInTheBody)
            {
                return aLineInTheBody;
            }
        };
    }


    @Override
    public boolean responseMsg(HttpStatus status, StringBuilder logMessage)
    {
        logMessage.append(status);
        return true;
    }


    @Override
    public boolean responseMsg(Header header, HttpStatus status, StringBuilder logMessage)
    {
        logMessage.append(header.type().name()).append(" ").append(header.value());
        return true;
    }


    @Override
    public boolean responseMsg(HttpResponseEntity entity, HttpStatus status, StringBuilder logMessage)
    {
        try
        {
            logMessage.append(entity.contentType());
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }


    @Override
    public BodyLineFormatter responseBodyFormatter(HttpResponseEntity entity)
    {
        try
        {
            final String charset = entity.contentType().charset("UTF-8");
            return new BodyLineFormatter()
            {
                @Override
                public String charset()
                {
                    return charset;
                }


                @Override
                public String bodyLineMsg(String aLineInTheBody)
                {
                    return aLineInTheBody;
                }
            };
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
