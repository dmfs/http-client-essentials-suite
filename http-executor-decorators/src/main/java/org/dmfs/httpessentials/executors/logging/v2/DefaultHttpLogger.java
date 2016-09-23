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

package org.dmfs.httpessentials.executors.logging.v2;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.logging.LoggingFacility;
import org.dmfs.httpessentials.headers.Header;

import java.io.IOException;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class DefaultHttpLogger implements HttpLogger
{
    public static final String NL = "\n";
    private final HttpLogPolicy mPolicy;
    private final LoggingFacility mLoggingFacility;


    public DefaultHttpLogger(HttpLogPolicy policy, LoggingFacility loggingFacility)
    {
        mPolicy = policy;
        mLoggingFacility = loggingFacility;
    }


    @Override
    public HttpRequest<?> log(URI uri, HttpRequest<?> request)
    {
        if (!mPolicy.logRequest(uri, request))
        {
            return request;
        }

        StringBuilder message = new StringBuilder();

        message.append("Request sent:").append(NL);
        message.append("URI: ").append(uri).append(NL);
        message.append("Method: ").append(request.method()).append(NL);

        if (mPolicy.logHeaders())
        {
            message.append("Headers:").append(NL);
            for (Header<?> header : request.headers())
            {
                message.append(" ").append(header.type().name()).append(": ").append(header.value()).append(NL);
            }
        }

        if (mPolicy.logAllBody())
        {
            HttpRequestEntity requestEntity = request.requestEntity();
            message.append("Body:").append(NL);
            message.append(" ").append("ContentType: ").append(requestEntity.contentType()).append(NL);
            message.append(" ").append("ContentLength: ").append(getContentLength(requestEntity)).append(NL);
            // TODO body content
        }

        mLoggingFacility.log(mPolicy.logLevel(), mPolicy.tag(), message.toString());

        return request; // TODO decorate
    }


    private long getContentLength(HttpRequestEntity requestEntity)
    {
        try
        {
            return requestEntity.contentLength();
        }
        catch (IOException e)
        {
            return -1;
        }
    }


    @Override
    public HttpResponse log(HttpResponse response)
    {
        if (!mPolicy.logResponse(response))
        {
            return response;
        }

        String responseLog = null; // compose using response and policy

        mLoggingFacility.log(mPolicy.logLevel(), mPolicy.tag(), responseLog);

        return response; // TODO decorate
    }
}
