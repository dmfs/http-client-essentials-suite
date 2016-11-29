/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.types;

import java.util.regex.Pattern;


/**
 * A {@link Token} that uses the given string as the underlying value.
 * <p>
 * Characters in the input string that are not allowed for token according to <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">RFC7230</a> will be
 * replaced with underscore ('_')
 *
 * @author Gabor Keszthelyi
 */
public final class SafeToken extends AbstractStringType implements Token
{

    private final static Pattern PATTERN_NOT_ALLOWED_CHARS =
            Pattern.compile("[^" + StringToken.ALLOWED_CHARS_REGEX_CLASS + "]+?");


    /**
     * Constructor.
     *
     * @param value
     *         the token content, must not be null or empty. Characters not allowed for token will be replaced with underscore.
     */
    public SafeToken(String value)
    {
        super(value == null ? null : makeItStandardCompliant(value));
    }


    private static String makeItStandardCompliant(String value)
    {
        if (value.isEmpty())
        {
            throw new IllegalArgumentException("Token value must not be empty");
        }
        return PATTERN_NOT_ALLOWED_CHARS.matcher(value).replaceAll("_");
    }
}
