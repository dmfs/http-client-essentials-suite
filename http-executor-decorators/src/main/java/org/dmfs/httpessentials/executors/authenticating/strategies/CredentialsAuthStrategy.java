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

package org.dmfs.httpessentials.executors.authenticating.strategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.AuthStrategy;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.iterables.elementary.Seq;

import java.net.URI;


/**
 * An {@link AuthStrategy} for a specific type of credentials.
 *
 * @param <CredentialsType>
 *         The type of the credentials this supports.
 *
 * @author Marten Gajda
 */
public final class CredentialsAuthStrategy<CredentialsType> implements AuthStrategy
{
    private final CredentialsStore<CredentialsType> mCredentialsStore;
    private final Iterable<AuthScheme<CredentialsType>> mSchemes;


    @SafeVarargs
    public CredentialsAuthStrategy(CredentialsStore<CredentialsType> credentialsStore, AuthScheme<CredentialsType>... authSchemes)
    {
        this(credentialsStore, new Seq<>(authSchemes));
    }


    public CredentialsAuthStrategy(CredentialsStore<CredentialsType> credentialsStore, Iterable<AuthScheme<CredentialsType>> authSchemes)
    {
        mCredentialsStore = credentialsStore;
        mSchemes = authSchemes;
    }


    @Override
    public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
    {
        AuthState result = fallback;
        for (AuthScheme<CredentialsType> authScheme : mSchemes)
        {
            result = authScheme.authState(mCredentialsStore, method, uri, result, fallback);
        }
        return result;
    }
}
