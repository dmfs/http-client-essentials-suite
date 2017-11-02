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
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authschemes.Basic;
import org.dmfs.httpessentials.executors.authenticating.authschemes.Digest;
import org.dmfs.iterables.elementary.Seq;

import java.net.URI;


/**
 * An {@link AuthStrategy} for {@link AuthScheme}s using {@link UserCredentials}.
 *
 * @author Marten Gajda
 */
public final class UserCredentialsAuthStrategy implements AuthStrategy
{
    private final AuthStrategy mDelegate;


    public UserCredentialsAuthStrategy(CredentialsStore<UserCredentials> credentialsStore)
    {
        this(credentialsStore, new Seq<>(new Digest(), new Basic()));
    }


    @SafeVarargs
    public UserCredentialsAuthStrategy(CredentialsStore<UserCredentials> credentialsStore, AuthScheme<UserCredentials>... authSchemes)
    {
        this(credentialsStore, new Seq<>(authSchemes));
    }


    public UserCredentialsAuthStrategy(CredentialsStore<UserCredentials> credentialsStore, Iterable<AuthScheme<UserCredentials>> authSchemes)
    {
        mDelegate = new CredentialsAuthStrategy<>(credentialsStore, authSchemes);
    }


    @Override
    public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
    {
        return mDelegate.authState(method, uri, fallback);
    }
}
