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


/**
 * The interface of an authentication challenge. See <a href="https://tools.ietf.org/html/rfc7235#section-2.1">RFC 7235, section 2.1</a>.
 *
 * @author Marten Gajda
 */
public interface Challenge
{
    /**
     * The scheme {@link Token} of the challenge.
     *
     * @return a {@link Token}.
     */
    Token scheme();

    /**
     * Returns the actual challenge {@link CharSequence}. The value will be trimmed, i.e. will not contain any leading or trailing spaces. Though the value may
     * be of length {@code 0}.
     * <p>
     * Note, in most cases this is subject to further parsing.
     *
     * @return A  {@link CharSequence}
     */
    CharSequence challenge();
}
