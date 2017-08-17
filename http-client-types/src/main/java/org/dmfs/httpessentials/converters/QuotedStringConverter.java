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

package org.dmfs.httpessentials.converters;

import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * {@link EntityConverter} for quoted {@link String}s. {@link #value(String)} also accepts unquoted strings, but {@link #valueString(String)} always returns
 * quoted strings.
 *
 * @author Marten Gajda
 * @deprecated To be removed in 1.0. Use {@link OptionallyQuoted} to decorate a {@link PlainStringHeaderConverter} instance instead.
 */
@Deprecated
public final class QuotedStringConverter implements EntityConverter<String>
{
    public final static QuotedStringConverter INSTANCE = new QuotedStringConverter();


    @Override
    public String value(String quotedString)
    {
        String trimmed = quotedString.trim();
        if (trimmed.length() > 1 && trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"')
        {
            return trimmed.substring(1, trimmed.length() - 1).replace("\\\\", "\\").replace("\\\"", "\"");
        }
        return trimmed;
    }


    @Override
    public String valueString(String string)
    {
        return "\"" + string.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

}
