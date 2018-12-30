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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * {@link EntityConverter} for header values that consist of a list of values separated by something that can be expressed as a regular expression (without
 * matching any part of a list element).
 *
 * @author Marten Gajda
 */
public final class RegexpSplitListConverter<T> implements EntityConverter<List<T>>
{
    private final EntityConverter<T> mConverter;
    private final Pattern mDividerPattern;
    private final CharSequence mDivider;


    public RegexpSplitListConverter(EntityConverter<T> converter, Pattern dividerPattern, CharSequence divider)
    {
        mConverter = converter;
        mDividerPattern = dividerPattern;
        mDivider = divider;
    }


    @Override
    public List<T> value(String valueString)
    {
        List<T> result = new ArrayList<>(16);
        for (String value : mDividerPattern.split(valueString))
        {
            result.add(mConverter.value(value));
        }
        return result;
    }


    @Override
    public String valueString(List<T> elements)
    {
        StringBuilder result = new StringBuilder(elements.size() * 30);
        boolean first = true;
        for (T element : elements)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                result.append(mDivider);
            }
            result.append(mConverter.valueString(element));
        }
        return result.toString();
    }
}
