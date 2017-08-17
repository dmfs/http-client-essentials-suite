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

package org.dmfs.httpessentials.responsehandlers;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;


/**
 * A simple {@link HttpResponseHandler} that always return the same response. It ensures that the actual response is properly closed.
 *
 * @param <T>
 *         The type of the result.
 *
 * @author Marten Gajda
 */
public final class TrivialResponseHandler<T> implements HttpResponseHandler<T>
{
    private final T mResult;


    /**
     * Creates a {@link TrivialResponseHandler} that returns the given result.
     */
    public TrivialResponseHandler(T result)
    {
        mResult = result;
    }


    @Override
    public T handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        response.responseEntity().contentStream().close();
        return mResult;
    }
}
