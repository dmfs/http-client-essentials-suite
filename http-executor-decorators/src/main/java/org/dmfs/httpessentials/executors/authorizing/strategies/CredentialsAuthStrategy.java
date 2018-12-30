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

package org.dmfs.httpessentials.executors.authorizing.strategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.AuthInfo;
import org.dmfs.httpessentials.executors.authorizing.AuthScheme;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.executors.authorizing.CredentialsStore;
import org.dmfs.iterables.decorators.Filtered;
import org.dmfs.iterables.decorators.Flattened;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.jems.pair.Pair;
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
        return new CredentialsAuthState(method, uri, fallback);

    }


    private final class CredentialsAuthState implements AuthState
    {
        private final HttpMethod mMethod;
        private final URI mUri;
        private final AuthState mFallback;


        public CredentialsAuthState(HttpMethod method, URI uri, AuthState fallback)
        {
            mMethod = method;
            mUri = uri;
            mFallback = fallback;
        }


        @Override
        public AuthState withChallenges(final Iterable<Challenge> challenges)
        {
            final Set<String> realms = new HashSet<>();
            return new Composite(
                    new Mapped<>(
                            new Filtered<>(
                                    new Flattened<>(
                                            new Mapped<>(
                                                    mSchemes,
                                                    argument -> argument.authStrategies(challenges, mCredentialsStore, mUri))),
                                    argument -> realms.add(argument.left().toString())),
                            Pair::right)).authState(mMethod, mUri, mFallback);
        }


        @Override
        public Optional<Authorization> authorization()
        {
            return absent();
        }


        @Override
        public AuthStrategy prematureAuthStrategy(Optional<AuthInfo> authInfo)
        {
            //  no premature authentication available at this point
            return new PassThroughStrategy();
        }
    }
}
