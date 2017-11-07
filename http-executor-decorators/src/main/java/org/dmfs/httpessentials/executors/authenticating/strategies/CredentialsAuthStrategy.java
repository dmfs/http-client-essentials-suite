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
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthInfo;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.AuthStrategy;
import org.dmfs.httpessentials.executors.authenticating.Authorization;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.iterables.decorators.Filtered;
import org.dmfs.iterables.decorators.Flattened;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterables.decorators.Reverse;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.jems.function.BiFunction;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.elementary.Consumed;
import org.dmfs.optional.Optional;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.dmfs.optional.Absent.absent;


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
    public AuthState authState(final HttpMethod method, final URI uri, final AuthState fallback)
    {
        final Set<String> realms = new HashSet<>();
        return new AuthState()
        {
            @Override
            public AuthState withChallenges(final Iterable<Challenge> challenges) throws UnauthorizedException
            {
                return new Consumed<>(
                        fallback,
                        new BiFunction<Pair<CharSequence, AuthStrategy>, AuthState, AuthState>()
                        {
                            @Override
                            public AuthState value(Pair<CharSequence, AuthStrategy> charSequenceAuthStrategyPair, AuthState authState)
                            {
                                return charSequenceAuthStrategyPair.right().authState(method, uri, authState);
                            }
                        },
                        new Reverse<>(
                                new Filtered<>(
                                        new Flattened<>(
                                                new Mapped<>(
                                                        mSchemes,
                                                        new Function<AuthScheme<CredentialsType>, Iterable<Pair<CharSequence, AuthStrategy>>>()
                                                        {
                                                            @Override
                                                            public Iterable<Pair<CharSequence, AuthStrategy>> apply(AuthScheme<CredentialsType> argument)
                                                            {
                                                                return argument.authStrategies(mCredentialsStore, method, uri, challenges);
                                                            }
                                                        })),
                                        new Filter<Pair<CharSequence, AuthStrategy>>()
                                        {
                                            @Override
                                            public boolean iterate(Pair<CharSequence, AuthStrategy> argument)
                                            {
                                                return realms.add(argument.left().toString());
                                            }
                                        }))
                ).value();
            }


            @Override
            public Optional<Authorization> authorization()
            {
                return absent();
            }


            @Override
            public AuthStrategy prematureAuthStrategy(Optional<AuthInfo> authInfo)
            {
                // not at this point
                return new PassThroughStrategy();
            }
        };

    }
}
