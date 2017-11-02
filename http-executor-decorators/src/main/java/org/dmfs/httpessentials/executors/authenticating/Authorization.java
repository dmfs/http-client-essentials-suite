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

import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.pair.Pair;
import org.dmfs.optional.Optional;


/**
 * The authorization sent to the server in order to authenticate. This represents the credentials in a form which goes over the wire as opposed to the real
 * credential as in username &amp; password.
 * <p>
 * Note, an {@link Authorization} can only contain an authentication token or a list of parameters, not both at the same time. If the token {@link Optional} is
 * present, the parameters {@link Iterable} must be empty.
 *
 * @author Marten Gajda
 */
public interface Authorization
{
    /**
     * Returns the authentication scheme token.
     *
     * @return A Token.
     */
    Token scheme();

    /**
     * Returns the authentication token, if there is any.
     *
     * @return An {@link Optional} {@link CharSequence}.
     */
    Optional<CharSequence> token();

    /**
     * Returns the authentication parameters as key-value pairs.
     *
     * @return An {@link Iterable} of {@link Pair}s.
     */
    Iterable<Pair<Token, CharSequence>> parameters();
}