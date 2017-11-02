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

package org.dmfs.httpessentials.executors.authenticating.authstates.digest;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Authorization;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.ChallengeFilter;
import org.dmfs.iterables.Split;
import org.dmfs.iterables.decorators.Fluent;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.optional.First;
import org.dmfs.optional.Optional;
import org.dmfs.optional.decorators.Mapped;
import org.dmfs.optional.iterable.PresentValues;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;


/**
 * The initial {@link AuthState} of Digest authentication.
 *
 * @author Marten Gajda
 */
public final class DigestAuthState implements AuthState
{
    private final CredentialsStore<UserCredentials> mCredentialsStore;
    private final HttpMethod mMethod;
    private final URI mUri;
    private final AuthState mNaDelegate;
    private final AuthState mFailDelegate;


    public DigestAuthState(CredentialsStore<UserCredentials> credentialsStore, HttpMethod method, URI uri, AuthState naDelegate, AuthState delegate)
    {
        mCredentialsStore = credentialsStore;
        mMethod = method;
        mUri = uri;
        mNaDelegate = naDelegate;
        mFailDelegate = delegate;
    }


    @Override
    public AuthState withChallenges(Iterable<Challenge> challenges) throws UnauthorizedException
    {
        /*
         * We need to match the supported Digest methods and realms with the credentials we have
         *
         * Here is how it works:
         *
         * * Filter the challenges by the DIGEST scheme
         * * Filter DIGEST Challenges by supported algorithm and qop
         * * Pick the most secure supported DIGEST challenge per Realm
         * * Create chain of AuthStates for each challenge which can be authenticated, e.g. which we have credentials for
         */

        Iterable<Pair<Challenge, UserCredentials>> digestChallenges = new PresentValues<>(new Fluent<>(challenges)
                // remove any non-Digest challenges
                .filtered(
                        new ChallengeFilter(new StringToken("Digest")))
                // remove anything that's not MD5 or SHA-256
                .filtered(
                        new Filter<Challenge>()
                        {
                            @Override
                            public boolean iterate(Challenge challenge)
                            {
                                String algorithm = challenge.parameter(new StringToken("algorithm")).value("MD5").toString();
                                return "MD5".equalsIgnoreCase(algorithm) || "SHA-256".equalsIgnoreCase(algorithm);
                            }
                        })
                // remove any auth type we don't support
                .filtered(
                        new Filter<Challenge>()
                        {
                            @Override
                            public boolean iterate(Challenge challenge)
                            {
                                return new First<>(new Split(challenge.parameter(new StringToken("qop")).value("auth"), ','), new Filter<CharSequence>()
                                {
                                    @Override
                                    public boolean iterate(CharSequence argument)
                                    {
                                        return argument.toString().equals("auth");
                                    }
                                }).isPresent();
                            }
                        })
                // map each challenge to an optional pair of challenge and credentials (optional because we may not have credentials for the realm)
                // TODO: first sort digestChallenges by protection level (i.e. SHA-256 over MD5 and qop=auth over no qop) and remove challenges with duplicate realms
                .mapped(
                        new Function<Challenge, Optional<Pair<Challenge, UserCredentials>>>()
                        {

                            @Override
                            public Optional<Pair<Challenge, UserCredentials>> apply(final Challenge challenge)
                            {
                                return new Mapped<>(new Function<UserCredentials, Pair<Challenge, UserCredentials>>()
                                {
                                    @Override
                                    public Pair<Challenge, UserCredentials> apply(UserCredentials userCredentials)
                                    {
                                        return new ValuePair<>(challenge, userCredentials);
                                    }
                                }, mCredentialsStore.credentials(new UriScope(mUri)));
                            }
                        }));

        if (!digestChallenges.iterator().hasNext())
        {
            return mNaDelegate.withChallenges(challenges);
        }

        AuthState result = mFailDelegate;
        for (Pair<Challenge, UserCredentials> challengeUserCredentialsPair : digestChallenges)
        {
            // chain them
            result = new AuthenticatedDigestAuthState(mMethod, mUri, challengeUserCredentialsPair.right(), result, challengeUserCredentialsPair.left());
        }

        return result;
    }


    @Override
    public Optional<Authorization> credentials()
    {
        // can't authenticate in this state, we need challenges first
        return absent();
    }
}
