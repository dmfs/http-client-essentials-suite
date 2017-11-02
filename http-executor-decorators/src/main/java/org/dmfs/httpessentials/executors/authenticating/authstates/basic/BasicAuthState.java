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

package org.dmfs.httpessentials.executors.authenticating.authstates.basic;

import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Authorization;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.ChallengeFilter;
import org.dmfs.iterables.decorators.Fluent;
import org.dmfs.iterators.Function;
import org.dmfs.optional.Optional;
import org.dmfs.optional.iterable.PresentValues;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;


/**
 * The initial {@link AuthState} of the {@code Basic} {@link AuthScheme}.
 *
 * @author Marten Gajda
 */
public final class BasicAuthState implements AuthState
{
    private final CredentialsStore<UserCredentials> mCredentialsStore;
    private final URI mUri;
    private final AuthState mNaDelegate;
    private final AuthState mFailDelegate;


    /**
     * Creates an initial {@link AuthState} for Basic Authentication.
     *
     * @param credentialsStore
     *         The {@link CredentialsStore} to use.
     * @param uri
     * @param naDelegate
     *         The follow up {@link AuthState} in case Basic Authentication is not supported by the server.
     * @param failDelegate
     *         The follow up {@link AuthState} in case Basic Authentication failed.
     */
    public BasicAuthState(CredentialsStore<UserCredentials> credentialsStore, URI uri, AuthState naDelegate, AuthState failDelegate)
    {
        mCredentialsStore = credentialsStore;
        mUri = uri;
        mNaDelegate = naDelegate;
        mFailDelegate = failDelegate;
    }


    @Override
    public AuthState withChallenges(Iterable<Challenge> challenges) throws UnauthorizedException
    {
        Iterable<UserCredentials> credentials = new PresentValues<>(new Fluent<>(challenges)
                .filtered(
                        new ChallengeFilter(new StringToken("Basic")))
                .mapped(
                        new Function<Challenge, Optional<UserCredentials>>()
                        {
                            @Override
                            public Optional<UserCredentials> apply(final Challenge challenge)
                            {
                                return mCredentialsStore.credentials(new UriScope(mUri));
                            }
                        }));
        if (!credentials.iterator().hasNext())
        {
            return mNaDelegate.withChallenges(challenges);
        }

        AuthState result = mFailDelegate;
        for (UserCredentials userCredentials : credentials)
        {
            // chain them
            result = new AuthenticatedBasicAuthState(userCredentials, result);
        }

        return result;
    }


    @Override
    public Optional<Authorization> credentials()
    {
        // we don't know how to authenticate in this state. We need to see some challenges first.
        return absent();
    }

}
