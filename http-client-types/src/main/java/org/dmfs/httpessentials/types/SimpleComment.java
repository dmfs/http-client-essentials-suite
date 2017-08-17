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

package org.dmfs.httpessentials.types;

import java.util.regex.Pattern;


/**
 * A {@link Comment} that uses the given string value for the content of the comment. (It adds the surrounding parenthesis).
 * <p>
 * It replaces or escapes characters in the input string that are not valid for comment as per <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">RFC7230</a>
 * (replace with underscore ('_'), escape with backslash ('\')), so it expects a non-escaped input string.
 *
 * @author Gabor Keszthelyi
 */
public final class SimpleComment extends AbstractStringType implements Comment
{
    private final static Pattern PATTERN_CTEXT_CHARS = Pattern.compile(
            "[^\\u0009\\u0020-\\u0029\\u002A-\\u005B\\u005C\\u005D-\\u007E\\u0080-\\u00FF]");
    private final static Pattern PATTERN_ESCAPED_CHARS = Pattern.compile("([\\(\\)\\\\])");


    /**
     * Constructor
     *
     * @param commentContent
     *         the content of the comment (un-escaped string, without the surrounding parenthesis of the comment in the header)
     */
    public SimpleComment(String commentContent)
    {
        super(commentContent == null ? null : makeItStandardCompliant(commentContent));
    }


    private static String makeItStandardCompliant(String value)
    {
        String replaced = PATTERN_ESCAPED_CHARS.matcher(PATTERN_CTEXT_CHARS.matcher(value).replaceAll("_"))
                .replaceAll("\\\\$1");

        return "(" + replaced + ")";
    }
}
