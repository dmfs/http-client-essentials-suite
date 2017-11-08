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

package org.dmfs.httpessentials.executors.authenticating.authschemes;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.AuthStrategy;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.httpessentials.executors.authenticating.Parametrized;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authenticating.authstates.AuthenticatedDigestAuthState;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.ChallengeFilter;
import org.dmfs.httpessentials.executors.authenticating.utils.SimpleParametrized;
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


/**
 * The Digest authentication scheme.
 *
 * @author Marten Gajda
 */
public final class Digest implements AuthScheme<UserCredentials>
{
    @Override
    public Iterable<Pair<CharSequence, AuthStrategy>> authStrategies(Iterable<Challenge> challenges, final CredentialsStore<UserCredentials> credentialsStore, HttpMethod method, final URI uri)
    {

        return new org.dmfs.iterables.decorators.Mapped<Pair<Parametrized, UserCredentials>, Pair<CharSequence, AuthStrategy>>(
                new PresentValues<>(new Fluent<>(challenges)
                        // remove any non-Digest challenges
                        .filtered(
                                new ChallengeFilter(new StringToken("Digest")))
                        .mapped(new Function<Challenge, Parametrized>()
                        {
                            @Override
                            public Parametrized apply(Challenge argument)
                            {
                                return new SimpleParametrized(argument.challenge());
                            }
                        })
                        // remove anything that's not MD5 or SHA-256
                        .filtered(
                                new Filter<Parametrized>()
                                {
                                    @Override
                                    public boolean iterate(Parametrized challenge)
                                    {
                                        String algorithm = challenge.parameter(new StringToken("algorithm")).value("MD5").toString();
                                        return "MD5".equalsIgnoreCase(algorithm) || "SHA-256".equalsIgnoreCase(algorithm);
                                    }
                                })
                        // remove any auth type we don't support
                        .filtered(
                                new Filter<Parametrized>()
                                {
                                    @Override
                                    public boolean iterate(Parametrized challenge)
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
                        // TODO: first sort digestChallenges by protection level (i.e. SHA-256 over MD5 and qop=auth over no qop)
                        .mapped(
                                new Function<Parametrized, Optional<Pair<Parametrized, UserCredentials>>>()
                                {

                                    @Override
                                    public Optional<Pair<Parametrized, UserCredentials>> apply(final Parametrized challenge)
                                    {
                                        return new Mapped<UserCredentials, Pair<Parametrized, UserCredentials>>(
                                                new Function<UserCredentials, Pair<Parametrized, UserCredentials>>()
                                                {
                                                    @Override
                                                    public Pair<Parametrized, UserCredentials> apply(UserCredentials userCredentials)
                                                    {
                                                        return new ValuePair<>(challenge, userCredentials);
                                                    }
                                                }, credentialsStore.credentials(new UriScope(uri)));
                                    }
                                })),
                new Function<Pair<Parametrized, UserCredentials>, Pair<CharSequence, AuthStrategy>>()
                {
                    @Override
                    public Pair<CharSequence, AuthStrategy> apply(final Pair<Parametrized, UserCredentials> argument)
                    {
                        return new ValuePair<CharSequence, AuthStrategy>(argument.left().parameter(new StringToken("realm")).value(), new AuthStrategy()
                        {
                            @Override
                            public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
                            {
                                return new AuthenticatedDigestAuthState(method, uri, argument.right(), fallback, argument.left());
                            }
                        });
                    }
                });
    }
}
