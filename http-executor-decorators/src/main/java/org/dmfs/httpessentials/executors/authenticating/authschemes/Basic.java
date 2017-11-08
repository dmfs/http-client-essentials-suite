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
import org.dmfs.httpessentials.executors.authenticating.authstates.AuthenticatedBasicAuthState;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.ChallengeFilter;
import org.dmfs.httpessentials.executors.authenticating.utils.SimpleParametrized;
import org.dmfs.iterables.decorators.Fluent;
import org.dmfs.iterators.Function;
import org.dmfs.jems.function.BiFunction;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.optional.Optional;
import org.dmfs.optional.composite.Zipped;
import org.dmfs.optional.iterable.PresentValues;

import java.net.URI;


/**
 * The Basic authentication scheme.
 *
 * @author Marten Gajda
 */
public final class Basic implements AuthScheme<UserCredentials>
{
    @Override
    public Iterable<Pair<CharSequence, AuthStrategy>> authStrategies(Iterable<Challenge> challenges, final CredentialsStore<UserCredentials> credentialsStore, final URI uri)
    {
        return new PresentValues<>(
                new Fluent<>(challenges)
                        .filtered(new ChallengeFilter(new StringToken("Basic")))
                        .mapped(new Function<Challenge, Parametrized>()
                        {
                            @Override
                            public Parametrized apply(Challenge argument)
                            {
                                return new SimpleParametrized(argument.challenge());
                            }
                        })
                        .mapped(new Function<Parametrized, Optional<Pair<CharSequence, AuthStrategy>>>()
                        {
                            @Override
                            public Optional<Pair<CharSequence, AuthStrategy>> apply(final Parametrized challenge)
                            {
                                return new Zipped<>(
                                        credentialsStore.credentials(new UriScope(uri)),
                                        challenge.parameter(new StringToken("realm")),
                                        new BiFunction<UserCredentials, CharSequence, Pair<CharSequence, AuthStrategy>>()
                                        {
                                            @Override
                                            public Pair<CharSequence, AuthStrategy> value(final UserCredentials userCredentials, CharSequence charSequence)
                                            {
                                                return new ValuePair<CharSequence, AuthStrategy>(charSequence,
                                                        new AuthStrategy()
                                                        {
                                                            @Override
                                                            public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
                                                            {
                                                                return new AuthenticatedBasicAuthState(userCredentials, fallback);
                                                            }
                                                        }
                                                );
                                            }
                                        });
                            }
                        })
        );
    }
}
