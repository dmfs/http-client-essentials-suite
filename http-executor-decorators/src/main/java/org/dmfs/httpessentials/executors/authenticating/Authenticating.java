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
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.executors.authenticating.authstates.FailedAuthState;

import java.io.IOException;
import java.net.URI;


/**
 * A decorator to {@link HttpRequestExecutor}s which tries to authenticate requests.
 *
 * @author Marten Gajda
 */
public final class Authenticating implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;
    private final AuthStrategy mAuthStrategy;


    public Authenticating(HttpRequestExecutor delegate, AuthStrategy authStrategy)
    {
        mDelegate = delegate;
        mAuthStrategy = authStrategy;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegate.execute(uri, new Authenticated<>(mDelegate, mAuthStrategy.authState(new FailedAuthState()), uri, request));
    }

}
