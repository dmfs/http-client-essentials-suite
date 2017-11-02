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
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.Credentials;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.credentials.AuthDigestCredentials;
import org.dmfs.httpessentials.executors.authenticating.credentials.DigestCredentials;
import org.dmfs.jems.charsequence.elementary.Hex;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;

import java.net.URI;
import java.security.SecureRandom;


/**
 * @author Marten Gajda
 */
public final class AuthenticatedDigestAuthState implements AuthState
{
    private final HttpMethod mHttpMethod;
    private final URI mUri;
    private final UserCredentials mCredentials;
    private final AuthState mDelegate;
    private final Challenge mDigestChallenge;


    public AuthenticatedDigestAuthState(HttpMethod httpMethod, URI uri, UserCredentials credentials, AuthState delegate, Challenge digestChallenge)
    {
        mHttpMethod = httpMethod;
        mUri = uri;
        mCredentials = credentials;
        mDelegate = delegate;
        mDigestChallenge = digestChallenge;
    }


    @Override
    public AuthState withChallenge(HttpMethod method, URI resourceUri, Iterable<Challenge> challenges) throws UnauthorizedException
    {
        return mDelegate.withChallenge(method, resourceUri, challenges);
    }


    @Override
    public Optional<Credentials> credentials()
    {
        if (!mDigestChallenge.parameter(new StringToken("qop")).isPresent() || !mDigestChallenge.parameter(new StringToken("opaque"))
                .isPresent() || !mDigestChallenge.parameter(new StringToken("realm")).isPresent())
        {
            return new Present<Credentials>(new DigestCredentials(mHttpMethod, mUri, mDigestChallenge, mCredentials));
        }
        // qop must be auth, we don't support auth-int and wouldn't be here if auth wasn't an option
        return new Present<Credentials>(
                new AuthDigestCredentials(mHttpMethod, mUri, mDigestChallenge, mCredentials, new Hex(new SecureRandom().generateSeed(16))));
    }
}
