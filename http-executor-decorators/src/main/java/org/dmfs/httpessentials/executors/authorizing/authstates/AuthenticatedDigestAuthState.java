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

package org.dmfs.httpessentials.executors.authorizing.authstates;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authorizing.AuthInfo;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.authorization.AuthDigestAuthorization;
import org.dmfs.httpessentials.executors.authorizing.authorization.DigestAuthorization;
import org.dmfs.httpessentials.executors.authorizing.charsequences.StringToken;
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
    private final Parametrized mDigestChallenge;
    private final int mNonceCount;


    public AuthenticatedDigestAuthState(HttpMethod httpMethod, URI uri, UserCredentials credentials, AuthState delegate, Parametrized digestChallenge)
    {
        this(httpMethod, uri, credentials, delegate, digestChallenge, 1);
    }


    public AuthenticatedDigestAuthState(HttpMethod httpMethod, URI uri, UserCredentials credentials, AuthState delegate, Parametrized digestChallenge, int nonceCount)
    {
        mHttpMethod = httpMethod;
        mUri = uri;
        mCredentials = credentials;
        mDelegate = delegate;
        mDigestChallenge = digestChallenge;
        mNonceCount = nonceCount;
    }


    @Override
    public AuthState withChallenges(Iterable<Challenge> challenges) throws UnauthorizedException
    {
        return mDelegate.withChallenges(challenges);
    }


    @Override
    public Optional<Authorization> authorization()
    {
        if (!mDigestChallenge.parameter(new StringToken("qop")).isPresent() || !mDigestChallenge.parameter(new StringToken("opaque"))
                .isPresent() || !mDigestChallenge.parameter(new StringToken("realm")).isPresent())
        {
            return new Present<Authorization>(new DigestAuthorization(mHttpMethod, mUri, mDigestChallenge, mCredentials));
        }
        // qop must be auth, we don't support auth-int and wouldn't be here if auth wasn't an option
        return new Present<Authorization>(
                new AuthDigestAuthorization(mHttpMethod, mUri, mDigestChallenge, mCredentials, new Hex(new SecureRandom().generateSeed(16)), mNonceCount));
    }


    @Override
    public AuthStrategy prematureAuthStrategy(Optional<AuthInfo> authInfo)
    {
        // return an AuthStrategy which can authenticate requests to the same realm prematurely
        return new AuthStrategy()
        {
            @Override
            public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
            {
                return new AuthenticatedDigestAuthState(method, uri, mCredentials, fallback, mDigestChallenge, mNonceCount + 1);
            }
        };
    }

}
