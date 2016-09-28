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

package org.dmfs.httpessentials.executors.logging.elements;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.logging.io.AllMediaTypeLogSinkFactory;
import org.dmfs.httpessentials.executors.logging.io.LogSink;
import org.dmfs.httpessentials.executors.logging.io.LogSinkFactory;
import org.dmfs.httpessentials.executors.logging.io.NullLogSink;
import org.dmfs.httpessentials.executors.logging.logfacility.LogFacility;

import java.io.IOException;
import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class AllBody implements LogSinkProvider
{
    private final LogSinkFactory mLogSinkFactory;


    public AllBody(LogFacility logFacility)
    {
        mLogSinkFactory = new AllMediaTypeLogSinkFactory(logFacility);
    }


    @Override
    public LogSink provide(URI uri, HttpRequest<?> request)
    {
        return mLogSinkFactory.logSink(request.requestEntity().contentType());
    }


    @Override
    public LogSink provide(HttpResponse response)
    {
        try
        {
            return mLogSinkFactory.logSink(response.responseEntity().contentType());
        }
        catch (IOException e)
        {
            return NullLogSink.INSTANCE;
        }
    }
}
