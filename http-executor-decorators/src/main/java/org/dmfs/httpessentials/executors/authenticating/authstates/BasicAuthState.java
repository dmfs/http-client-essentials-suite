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

package org.dmfs.httpessentials.executors.authenticating.authstates;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.Credentials;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.credentials.BasicCredentials;
import org.dmfs.httpessentials.executors.authenticating.utils.ChallengeFilter;
import org.dmfs.jems.function.BiFunction;
import org.dmfs.optional.First;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;
import org.dmfs.optional.composite.Zipped;

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
    private final AuthState mNaDelegate;
    private final AuthState mFailDelegate;


    /**
     * Creates an initial {@link AuthState} for Basic Authentication.
     *
     * @param credentialsStore
     *         The {@link CredentialsStore} to use.
     * @param naDelegate
     *         The follow up {@link AuthState} in case Basic Authentication is not supported by the server.
     * @param failDelegate
     *         The follow up {@link AuthState} in case Basic Authentication failed.
     */
    public BasicAuthState(CredentialsStore<UserCredentials> credentialsStore, AuthState naDelegate, AuthState failDelegate)
    {
        mCredentialsStore = credentialsStore;
        mNaDelegate = naDelegate;
        mFailDelegate = failDelegate;
    }


    @Override
    public AuthState withChallenge(HttpMethod method, URI resourceUri, Iterable<Challenge> challenges)
    {
        // TODO: we should generate one AuthState for each BASIC challenge for which we have credentials
        // Though this is low prio because it's probably quite uncommon to have more than one BASIC challenge
        return new Zipped<>(
                new First<>(challenges, new ChallengeFilter(new StringToken("BASIC"))),
                mCredentialsStore.credentials(new UriScope(resourceUri)),
                new AuthStateFunction(mFailDelegate)).value(mNaDelegate);
    }


    @Override
    public Optional<Credentials> credentials()
    {
        // we don't know how to authenticate in this state. We need to see some challenges first.
        return absent();
    }


    /**
     * A Function to create a Basic {@link AuthState} from the given {@link Challenge} and {@link UserCredentials}.
     */
    private static class AuthStateFunction implements BiFunction<Challenge, UserCredentials, AuthState>
    {
        private final AuthState mDelegate;


        private AuthStateFunction(AuthState delegate)
        {
            mDelegate = delegate;
        }


        @Override
        public AuthState value(Challenge challenge, UserCredentials userCredentials)
        {
            return new AuthenticatedBasicAuthState(userCredentials, mDelegate);
        }
    }


    /**
     * The authenticated Basic AuthState.
     */
    private static final class AuthenticatedBasicAuthState implements AuthState
    {
        private final UserCredentials mCredentials;
        private final AuthState mDelegate;


        AuthenticatedBasicAuthState(UserCredentials credentials, AuthState delegate)
        {
            mCredentials = credentials;
            mDelegate = delegate;
        }


        @Override
        public AuthState withChallenge(HttpMethod method, URI resourceUri, Iterable<Challenge> challenges) throws UnauthorizedException
        {
            // apparently Basic auth didn't work (otherwise we wouldn't see more Challenges), go to the next AuthState
            return mDelegate.withChallenge(method, resourceUri, challenges);
        }


        @Override
        public Optional<Credentials> credentials()
        {
            return new Present<Credentials>(new BasicCredentials(mCredentials));
        }
    }
}
