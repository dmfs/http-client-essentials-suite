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

package org.dmfs.httpessentials.executors.authenticating;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.jems.pair.Pair;

import java.net.URI;


/**
 * An authentication scheme.
 *
 * @author Marten Gajda
 */
public interface AuthScheme<CredentialsType>
{
    /**
     * Returns an initial {@link AuthState} for this {@link AuthScheme}.
     *
     * @param credentialsStore
     *         A {@link CredentialsStore} suitable for this {@link AuthScheme}.
     * @param method
     *         The {@link HttpMethod} of the request to authenticate.
     * @param uri
     *         The target {@link URI} of the request to authenticate.
     * @param challenges
     *
     * @return
     */
    Iterable<Pair<CharSequence, AuthStrategy>> authStrategies(CredentialsStore<CredentialsType> credentialsStore, HttpMethod method, URI uri, Iterable<Challenge> challenges);

}
