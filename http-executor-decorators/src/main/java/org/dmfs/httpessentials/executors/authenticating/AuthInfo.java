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
import org.dmfs.optional.Optional;


/**
 * The {@code Authentication-Info} header as per <a href="https://tools.ietf.org/html/rfc7615">RFC 7615</a>
 *
 * @author Marten Gajda
 */
public interface AuthInfo
{

    /**
     * Returns the {@link Optional} value of the {@code Authentication-Info} parameter with the given name.
     *
     * @param name
     *         The parameter name {@link Token} of the parameter value to return.
     *
     * @return the {@link Optional} parameter value {@link CharSequence}. Will be absent if no such parameter exists.
     */
    Optional<CharSequence> parameter(Token name);
}
