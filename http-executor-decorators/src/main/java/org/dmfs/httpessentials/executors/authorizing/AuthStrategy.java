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

import org.dmfs.httpessentials.HttpMethod;

import java.net.URI;


/**
 * An authentication strategy. It serves as a factory for an initial {@link AuthState} when starting an authenticated request.
 *
 * @author Marten Gajda
 */
public interface AuthStrategy
{
    /**
     * Returns the initial {@link AuthState} of this strategy.
     *
     * @param method
     *         The {@link HttpMethod} of the request to authenticate.
     * @param uri
     *         The target {@link URI} of the request to authenticate.
     * @param fallback
     *         The {@link AuthState} to delegate to if this strategy didn't result in a successful authentication.
     *
     * @return An {@link AuthState}.
     */
    AuthState authState(HttpMethod method, URI uri, AuthState fallback);
}
