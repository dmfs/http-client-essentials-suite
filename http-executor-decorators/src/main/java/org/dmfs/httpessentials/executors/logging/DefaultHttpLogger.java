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
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.executors.logging.io.DuplicatingInputStream;
import org.dmfs.httpessentials.executors.logging.io.InputStreamDecorated;
import org.dmfs.httpessentials.executors.logging.io.LoggingOutputStream;
import org.dmfs.httpessentials.headers.Header;

import java.io.InputStream;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class DefaultHttpLogger implements HttpLogger
{
    private static final String NL = "\n";

    private final HttpLogFormatter mFormatter;
    private final LogFacility mLogFacility;


    public DefaultHttpLogger(HttpLogFormatter logFormatter, LogFacility logFacility)
    {
        mFormatter = logFormatter;
        mLogFacility = logFacility;
    }


    @Override
    public HttpRequest<?> log(URI uri, HttpRequest<?> request)
    {
        String logMessage = composeRequestMessage(uri, request);

        mLogFacility.log(logMessage);

        BodyLineFormatter bodyLineFormatter = mFormatter.requestBodyFormatter(request.requestEntity());
        // TODO if bodyLineFormatter != null, decorate request adding bodyLineFormatter and mLogFacility to the stream handler

        return request;
    }


    private String composeRequestMessage(URI uri, HttpRequest<?> request)
    {
        StringBuilder message = new StringBuilder();

        appendNewLine(mFormatter.appendRequestMsg(request.method(), uri, message), message);
        for (Header<?> header : request.headers())
        {
            appendNewLine(mFormatter.appendRequestMsg(header, message), message);
        }
        appendNewLine(mFormatter.appendRequestMsg(request.requestEntity(), message), message);

        return message.toString();
    }


    @Override
    public HttpResponse log(HttpResponse response)
    {
        String responseLog = composeResponseMessage();

        if (isError(response))
        {
            mLogFacility.logError(responseLog, null);
        }
        else
        {
            mLogFacility.log(responseLog);
        }

        BodyLineFormatter bodyLineFormatter = mFormatter.responseBodyFormatter(response.responseEntity());

        if (bodyLineFormatter != null)
        {
            return new InputStreamDecorated(response, new LoggingStreamDecoration(bodyLineFormatter));
        }

        return response;
    }


    private String composeResponseMessage()
    {
        StringBuilder message = new StringBuilder();
        // TODO similarly as in composeRequestMessage()
        return message.toString();
    }


    private boolean isError(HttpResponse response)
    {
        return response.status().isClientError() || response.status().isServerError();
    }


    private void appendNewLine(boolean appended, StringBuilder message)
    {
        if (appended)
        {
            message.append(NL);
        }
    }


    private class LoggingStreamDecoration implements Decoration<InputStream>
    {
        private final BodyLineFormatter mBodyLineFormatter;

        public LoggingStreamDecoration(BodyLineFormatter bodyLineFormatter)
        {
            mBodyLineFormatter = bodyLineFormatter;
        }


        @Override
        public InputStream decorated(InputStream original)
        {
            return new DuplicatingInputStream(original,
                    new LoggingOutputStream(mLogFacility, mBodyLineFormatter));
        }
    }
}
