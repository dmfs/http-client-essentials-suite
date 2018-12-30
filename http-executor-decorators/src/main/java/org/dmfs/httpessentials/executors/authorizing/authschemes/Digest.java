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

package org.dmfs.httpessentials.executors.authorizing.authschemes;

import org.dmfs.httpessentials.executors.authorizing.AuthScheme;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.executors.authorizing.CredentialsStore;
import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.executors.authorizing.Tokens;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.authscopes.UriScope;
import org.dmfs.httpessentials.executors.authorizing.authstates.AuthenticatedDigestAuthState;
import org.dmfs.httpessentials.executors.authorizing.utils.ChallengeFilter;
import org.dmfs.httpessentials.executors.authorizing.utils.SimpleParametrized;
import org.dmfs.iterables.Split;
import org.dmfs.iterables.decorators.Fluent;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.optional.First;
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
    public Iterable<Pair<CharSequence, AuthStrategy>> authStrategies(Iterable<Challenge> challenges, final CredentialsStore<UserCredentials> credentialsStore, final URI uri)
    {

        return new org.dmfs.iterables.decorators.Mapped<Pair<Parametrized, UserCredentials>, Pair<CharSequence, AuthStrategy>>(
                new PresentValues<>(new Fluent<>(challenges)
                        // remove any non-Digest challenges
                        .filtered(new ChallengeFilter(Tokens.DIGEST))
                        .mapped((Function<Challenge, Parametrized>) argument -> new SimpleParametrized(argument.challenge()))
                        // remove anything that's not MD5 or SHA-256
                        .filtered(challenge -> {
                            String algorithm = challenge.parameter(Tokens.ALGORITHM).value("MD5").toString();
                            return "MD5".equalsIgnoreCase(algorithm) || "SHA-256".equalsIgnoreCase(algorithm);
                        })
                        // remove any auth type we don't support
                        .filtered(challenge -> new First<>(new Split(challenge.parameter(Tokens.QOP).value("auth"), ','),
                                (Filter<CharSequence>) argument -> argument.toString().equals("auth")).isPresent())
                        // map each challenge to an optional pair of challenge and credentials (optional because we may not have credentials for the realm)
                        // TODO: first sort digestChallenges by protection level (i.e. SHA-256 over MD5 and qop=auth over no qop)
                        .mapped(challenge -> new Mapped<>(
                                userCredentials -> new ValuePair<>(challenge, userCredentials), credentialsStore.credentials(new UriScope(uri))))),
                argument -> new ValuePair<>(argument.left().parameter(Tokens.REALM).value(),
                        (method, uri1, fallback) -> new AuthenticatedDigestAuthState(method, uri1, argument.right(), fallback, argument.left())));
    }
}
