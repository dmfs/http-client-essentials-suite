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

package org.dmfs.httpessentials.executors.authorizing.authstrategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.httpessentials.executors.authorizing.CredentialsStore;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authorizing.authstates.AuthenticatedBasicAuthState;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.single.combined.Backed;

import java.net.URI;


/**
 * An {@link AuthStrategy} doing premature Basic authentication.
 * <p>
 * Use this with care. It will send the unprotected user credentials with every request.
 * <p>
 * Note, when in use, this {@link AuthStrategy} should have the highest priority, otherwise it's pointless.
 *
 * @author Marten Gajda
 */
public final class PrematureBasicAuthStrategy implements AuthStrategy
{
    private final CredentialsStore<UserCredentials> mCredentialsStore;


    public PrematureBasicAuthStrategy(CredentialsStore<UserCredentials> credentialsStore)
    {
        mCredentialsStore = credentialsStore;
    }


    @Override
    public AuthState authState(HttpMethod method, URI uri, final AuthState fallback)
    {
        return new Backed<>(
                new Mapped<>(
                        argument -> new AuthenticatedBasicAuthState(argument, fallback),
                        mCredentialsStore.credentials(new UriScope(uri))),
                fallback).value();
    }
}
