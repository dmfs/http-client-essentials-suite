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
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.logging.HttpLogger;
import org.dmfs.httpessentials.executors.logging.LoggingFacility;
import org.dmfs.httpessentials.executors.logging.v2.HttpLogPolicy;

import java.net.URI;


/**
 * @author Gabor Keszthelyi
 */
public final class DefaultHttpLogger implements HttpLogger
{
    private final HttpLogPolicy mPolicy;
    private final LoggingFacility mLoggingFacility;


    public DefaultHttpLogger(HttpLogPolicy policy, LoggingFacility loggingFacility)
    {
        mPolicy = policy;
        mLoggingFacility = loggingFacility;
    }


    @Override
    public void log(URI uri, HttpRequest<?> request)
    {
        if (!mPolicy.logRequest(uri, request))
        {
            return;
        }

        String requestLog = null; // compose using uri, request and policy

        mLoggingFacility.log(mPolicy.logLevel(), mPolicy.tag(), requestLog);
    }


    @Override
    public void log(HttpResponse response)
    {
        if (!mPolicy.logResponse(response))
        {
            return;
        }

        String responseLog = null; // compose using response and policy

        mLoggingFacility.log(mPolicy.logLevel(), mPolicy.tag(), responseLog);
    }
}
