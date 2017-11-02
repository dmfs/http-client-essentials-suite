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
import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.optional.Optional;

import java.net.URI;


/**
 * An authentication flow for a specific {@link AuthScope}.
 *
 * @author Marten Gajda
 */
public interface AuthState
{
    /**
     * Returns an updated {@link AuthState} considering the given {@link HttpMethod}, {@link URI} and {@link Challenge}s.
     *
     * @param method
     *         The request {@link HttpMethod}.
     * @param resourceUri
     *         The {@link URI} this request goes to.
     * @param challenges
     *         The {@link Challenge}s in the server response.
     *
     * @return A new {@link AuthState}.
     *
     * @throws UnauthorizedException
     *         if the server didn't present a supported {@link Challenge} which could be used to authorize the request.
     */
    AuthState withChallenge(HttpMethod method, URI resourceUri, Iterable<Challenge> challenges) throws UnauthorizedException;

    /**
     * Returns {@link Optional} {@link Credentials} for the current state. In general it's expected that (except for the initial state) each state should be
     * able to produce Credentials.
     *
     * @return {@link Optional} {@link Credentials}.
     */
    Optional<Credentials> credentials();
}
