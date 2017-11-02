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

/**
 * An authentication strategy which knows how to authenticate using certain {@link Credentials} and {@link AuthScheme}s.
 *
 * @author Marten Gajda
 */
public interface AuthStrategy
{
    /**
     * Returns the initial {@link AuthState}.
     *
     * @param fallback
     *         The {@link AuthState} to return to if now auth scheme was able to authenticate the request.
     *
     * @return An {@link AuthState}.
     */
    AuthState authState(AuthState fallback);
}
