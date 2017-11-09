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

package org.dmfs.httpessentials.executors.authorizing;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.executors.authorizing.authcaches.SimpleAuthStrategyCache;
import org.dmfs.httpessentials.executors.authorizing.authstates.FailedAuthState;
import org.dmfs.httpessentials.executors.authorizing.strategies.CachedAuthStrategy;

import java.io.IOException;
import java.net.URI;


/**
 * A decorator to {@link HttpRequestExecutor}s which tries to authorize requests.
 *
 * @author Marten Gajda
 */
public final class Authorizing implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;
    private final AuthStrategy mAuthStrategy;
    private final AuthStrategyCache mAuthStrategyCache;


    public Authorizing(HttpRequestExecutor delegate, AuthStrategy authStrategy)
    {
        this(delegate, authStrategy, new SimpleAuthStrategyCache());
    }


    public Authorizing(HttpRequestExecutor delegate, AuthStrategy authStrategy, AuthStrategyCache authStrategyCache)
    {
        mDelegate = delegate;
        mAuthStrategy = authStrategy;
        mAuthStrategyCache = authStrategyCache;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegate.execute(
                uri,
                new Authorized<>(
                        mDelegate,
                        mAuthStrategyCache, uri,
                        request,
                        new CachedAuthStrategy(mAuthStrategyCache, mAuthStrategy).authState(request.method(), uri, new FailedAuthState())));
    }

}
