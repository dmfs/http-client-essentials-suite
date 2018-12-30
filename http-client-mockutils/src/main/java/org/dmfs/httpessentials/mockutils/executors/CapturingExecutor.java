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

import java.net.URI;


/**
 * Stub {@link HttpRequestExecutor} that captures the last {@link URI} and {@link HttpRequest} arguments it receives in {@link #execute(URI, HttpRequest)}.
 * Captured objects can be accessed via public fields.
 *
 * @author Gabor Keszthelyi
 */
public class CapturingExecutor implements HttpRequestExecutor
{

    public HttpRequest mCapturedRequest;
    public URI mCapturedUri;


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request)
    {
        mCapturedUri = uri;
        mCapturedRequest = request;
        return null;
    }
}
