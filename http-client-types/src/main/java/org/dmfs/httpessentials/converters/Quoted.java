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
 * {@link EntityConverter} decorator that quotes/unquotes values. Note: {@link #value(String)} will throw if the passed string is not quoted.
 *
 * @author Marten Gajda
 */
public final class Quoted<E> implements EntityConverter<E>
{
    private final EntityConverter<E> mDelegate;


    public Quoted(EntityConverter<E> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public E value(String quotedString)
    {
        String trimmed = quotedString.trim();
        if (trimmed.length() <= 1 || trimmed.charAt(0) != '"' || trimmed.charAt(trimmed.length() - 1) != '"')
        {
            throw new IllegalArgumentException(String.format("%s is not a properly quoted string.", quotedString));
        }
        return mDelegate.value(trimmed.substring(1, trimmed.length() - 1).replace("\\\\", "\\").replace("\\\"", "\""));
    }


    @Override
    public String valueString(E value)
    {
        return "\"" + mDelegate.valueString(value).replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

}
