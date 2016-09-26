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
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.decoration.ResponseDecorated;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;

import java.io.IOException;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class Logging implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegateExecutor;
    private final HttpLogger mHttpLogger;


    public Logging(HttpRequestExecutor delegateExecutor, HttpLogger httpLogger)
    {
        mDelegateExecutor = delegateExecutor;
        mHttpLogger = httpLogger;
    }


    public Logging(HttpRequestExecutor delegateExecutor, HttpLogFormatter logFormatter, LogFacility logFacility)
    {
        mDelegateExecutor = delegateExecutor;
        mHttpLogger = new DefaultHttpLogger(logFormatter, logFacility);
        ;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegateExecutor.execute(uri,
                new ResponseDecorated<T>(mHttpLogger.log(uri, request), new LoggingResponseDecoration()));
    }


    private class LoggingResponseDecoration implements Decoration<HttpResponse>
    {
        @Override
        public HttpResponse decorated(HttpResponse original)
        {
            return mHttpLogger.log(original);
        }
    }
}
