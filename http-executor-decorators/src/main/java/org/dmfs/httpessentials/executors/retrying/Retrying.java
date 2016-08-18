/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.executors.retrying;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.executors.retrying.policies.DefaultRetryPolicy;

import java.io.IOException;
import java.net.URI;


/**
 * A {@link HttpRequestExecutor} decorator that retries failed requests according to the provided {@link RetryPolicy}.
 *
 * @author Gabor Keszthelyi
 */
public final class Retrying implements HttpRequestExecutor
{

    private final HttpRequestExecutor mDecoratedExecutor;
    private final RetryPolicy mPolicy;


    public Retrying(HttpRequestExecutor decoratedExecutor, RetryPolicy policy)
    {
        mDecoratedExecutor = decoratedExecutor;
        mPolicy = policy;
    }


    public Retrying(HttpRequestExecutor decoratedExecutor)
    {
        this(decoratedExecutor, new DefaultRetryPolicy(3));
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return retryingExecute(uri, request, 0);
    }


    private <T> T retryingExecute(URI uri, HttpRequest<T> request, int retryCount) throws IOException, ProtocolError, ProtocolException
    {
        try
        {
            return mDecoratedExecutor.execute(uri, request);
        }
        catch (Exception e)
        {
            if (!mPolicy.shouldRetry(request, e, retryCount + 1))
            {
                throw e;
            }
            return retryingExecute(uri, request, retryCount + 1);
        }
    }

}
