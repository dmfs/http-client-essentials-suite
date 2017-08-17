/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.httpessentials.mockutils.executors;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;

import java.io.IOException;
import java.net.URI;


/**
 * A simple mock server {@link HttpRequestExecutor} that always throws an exception when executing a request.
 *
 * @author Marten Gajda
 */
public class ExceptionMockServerExecutor implements HttpRequestExecutor
{

    private final Exception mException;


    /**
     * Creates an {@link HttpRequestExecutor} that always throws the given Exception. Note that only {@link IOException}s, {@link ProtocolError}s, {@link
     * ProtocolException}s and their subclasses are supported. Every other {@link Exception} will result in a {@link RuntimeException}.
     *
     * @param exception
     *         The {@link Exception} to throw when executing a request.
     */
    public ExceptionMockServerExecutor(Exception exception)
    {
        mException = exception;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        try
        {
            throw mException;
        }
        catch (IOException | ProtocolError | ProtocolException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Illegal Exception type passed", e);
        }
    }
}
