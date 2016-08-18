/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
import org.dmfs.iterators.CsvIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * {@link HeaderValueConverter} for header values that consist of a list of values.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class ListConverter<T> implements EntityConverter<List<T>>
{
    private final static char DEFAULT_DIVIDER = ',';
    private final EntityConverter<T> mConverter;
    private final char mDivider;


    public ListConverter(EntityConverter<T> converter)
    {
        this(converter, DEFAULT_DIVIDER);
    }


    public ListConverter(EntityConverter<T> converter, char divider)
    {
        mConverter = converter;
        mDivider = divider;
    }


    @Override
    public List<T> value(String valueString)
    {
        List<T> result = new ArrayList<T>(16);
        Iterator<String> iterator = new CsvIterator(valueString, mDivider);
        while (iterator.hasNext())
        {
            result.add(mConverter.value(iterator.next().trim()));
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
