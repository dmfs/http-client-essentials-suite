/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.types;

import java.util.regex.Pattern;


/**
 * A {@link Token} using a {@link String} as the underlying value. See <a href=https://tools.ietf.org/html/rfc7230#section-3.2.6>RFC7230</a>
 * for the allowed characters.
 *
 * @author Gabor Keszthelyi
 */
public final class StringToken extends AbstractStringType implements Token
{

    final static String ALLOWED_CHARS_REGEX_CLASS = "a-zA-Z0-9^_`|~!#$%&'*+.-";

    private final static Pattern PATTERN_TOKEN = Pattern.compile("[" + ALLOWED_CHARS_REGEX_CLASS + "]+");


    public StringToken(String value)
    {
        super(value);
        validate(value);
    }


    private void validate(String value)
    {
        if (!PATTERN_TOKEN.matcher(value).matches())
        {
            throw new IllegalArgumentException(String.format("'%s' is not a valid http header token.", value));
        }

    }

}
