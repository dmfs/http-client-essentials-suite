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

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.headers.Header;

import java.io.IOException;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class DefaultHttpLogger implements HttpLogger
{
    private static final String NL = "\n";

    private final HttpLogFormatter mComposer;
    private final LoggingFacility mLoggingFacility;


    public DefaultHttpLogger(HttpLogFormatter logFormatter, LoggingFacility loggingFacility)
    {
        mComposer = logFormatter;
        mLoggingFacility = loggingFacility;
    }


    @Override
    public HttpRequest<?> log(URI uri, HttpRequest<?> request)
    {
        String logMessage = composeRequestMessage(uri, request);

        mLoggingFacility.log(logMessage);

        BodyLineFormatter bodyLineFormatter = mComposer.requestBodyFormatter();
        // TODO if bodyLineFormatter != null, decorate request adding bodyLineFormatter and mLoggingFacility to the stream handler
        return request;
    }


    private String composeRequestMessage(URI uri, HttpRequest<?> request)
    {
        StringBuilder message = new StringBuilder();

        message.append("Request sent:").append(NL);

        String methodAndUri = mComposer.requestMsg(request.method(), uri);
        if (methodAndUri != null)
        {
            message.append(methodAndUri).append(NL);
        }

        for (Header<?> header : request.headers())
        {
            String headerMsg = mComposer.requestMsg(header);
            if (headerMsg != null)
            {
                message.append(headerMsg).append(NL);
            }
        }

        String entityMsg = mComposer.requestMsg(request.requestEntity());
        if (entityMsg != null)
        {
            message.append(entityMsg).append(NL);
        }
        return message.toString();
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
        String responseLog = composerResponseMessage();

        mLoggingFacility.log(responseLog);

        BodyLineFormatter bodyLineFormatter = mComposer.responseBodyFormatter();
        // TODO if bodyLineFormatter != null, decorate response adding bodyLineFormatter and mLoggingFacility to the stream handler
        return response;
    }


    private String composerResponseMessage()
    {
        StringBuilder message = new StringBuilder();
        message.append("Response received:").append(NL);
        // TODO similarly as in composeRequestMessage()
        // ...
        return message.toString();
    }


    private boolean isError(HttpResponse response)
    {
        return response.status().isClientError() || response.status().isServerError();
    }
}
