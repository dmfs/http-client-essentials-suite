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

package org.dmfs.httpessentials.executors.authorizing;

import org.dmfs.jems.pair.Pair;

import java.net.URI;


/**
 * An authentication scheme. It knows how to handle challenges with a specific scheme token and can return {@link AuthStrategy}s for each of the supported
 * challenges.
 *
 * @author Marten Gajda
 */
public interface AuthScheme<CredentialsType>
{
    /**
     * Returns a pair of realm and {@link AuthStrategy} for each of the supported {@link Challenge}s.
     * <p>
     * Each of the {@link AuthStrategy}s returned will return an initial {@link AuthState} for one specific {@link Challenge}.
     *
     * @param challenges
     *         The challenges provided by the server.
     * @param credentialsStore
     *         A {@link CredentialsStore} suitable for this {@link AuthScheme}.
     * @param uri
     *         The target {@link URI} of the request to authenticate.
     *
     * @return
     */
    Iterable<Pair<CharSequence, AuthStrategy>> authStrategies(Iterable<Challenge> challenges, CredentialsStore<CredentialsType> credentialsStore, URI uri);
}
