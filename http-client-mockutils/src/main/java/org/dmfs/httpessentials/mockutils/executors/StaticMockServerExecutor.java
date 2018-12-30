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
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.mockutils.responses.CustomUrisMockResponse;

import java.io.IOException;
import java.net.URI;


/**
 * A simple {@link HttpRequestExecutor} to test response handling. This class emulates a server that responds to all URLs and with the same response each time.
 *
 * @author Marten Gajda
 */
public class StaticMockServerExecutor implements HttpRequestExecutor
{

    private final HttpResponse mResponse;


    /**
     * Creates a mock server {@link HttpRequestExecutor} that always responds with the same response.
     *
     * @param response
     *         The response to return.
     */
    public StaticMockServerExecutor(HttpResponse response)
    {
        mResponse = response;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException
    {
        HttpResponse response = new CustomUrisMockResponse(mResponse, uri, uri);
        return request.responseHandler(response).handleResponse(response);
    }
}
