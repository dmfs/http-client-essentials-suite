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

package org.dmfs.httpessentials.executors.authenticating;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;


/**
 * A response handler for "401 Unauthorized" responses. When called, it decorates the request with the new {@link AuthState} and tries to execute it again.
 *
 * @author Marten Gajda
 */
final class AuthenticatingResponseHandler<T> implements HttpResponseHandler<T>
{
    private final HttpRequestExecutor mExecutor;
    private final AuthState mAuthState;
    private final URI mUri;
    private final HttpRequest<T> mRequest;


    AuthenticatingResponseHandler(HttpRequestExecutor executor, URI uri, HttpRequest<T> request, AuthState authState)
    {
        mExecutor = executor;
        mAuthState = authState;
        mUri = uri;
        mRequest = request;
    }


    @Override
    public T handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        // start over with the new AuthState
        return mExecutor.execute(
                response.requestUri(),
                new Authenticated<>(mExecutor, mUri, mRequest, mAuthState));
    }
}
