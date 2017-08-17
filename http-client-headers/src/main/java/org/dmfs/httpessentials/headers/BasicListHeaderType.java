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

package org.dmfs.httpessentials.headers;

import org.dmfs.httpessentials.converters.ListConverter;
import org.dmfs.httpessentials.typedentity.EntityConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * Basic header type class for headers that may contain lists of values.
 *
 * @param <ValueType>
 *         The type of the header value.
 *
 * @author Marten Gajda
 */
public final class BasicListHeaderType<ValueType> implements ListHeaderType<ValueType>
{
    private final String mHeaderName;
    private final EntityConverter<List<ValueType>> mValueConverter;


    /**
     * Creates a {@link HeaderType} for the given header name. Header values are converted using the provided {@link EntityConverter}.
     *
     * @param headerName
     *         The name of the header.
     * @param valueConverter
     *         A {@link EntityConverter} to convert header values from/to string.
     */
    public BasicListHeaderType(String headerName, EntityConverter<ValueType> valueConverter)
    {
        mHeaderName = headerName;
        mValueConverter = new ListConverter<ValueType>(valueConverter);
    }


    @Override
    public String name()
    {
        return mHeaderName;
    }


    @Override
    public Header<List<ValueType>> entityFromString(String headerValueString)
    {
        return new BasicHeader<List<ValueType>>(this, mValueConverter.value(headerValueString));
    }


    @Override
    public Header<List<ValueType>> entity(List<ValueType> value)
    {
        return new BasicHeader<List<ValueType>>(this, value);
    }


    @Override
    public String valueString(List<ValueType> headerValue)
    {
        return mValueConverter.valueString(headerValue);
    }


    @Override
    public List<ValueType> valueFromString(String valueString)
    {
        return mValueConverter.value(valueString);
    }


    @Override
    public int hashCode()
    {
        return mHeaderName.hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof ListHeaderType && mHeaderName.equals(((HeaderType<?>) obj).name()));
    }


    @Override
    public Header<List<ValueType>> merged(Header<List<ValueType>> value1, Header<List<ValueType>> value2)
    {
        List<ValueType> list1 = value1.value();
        List<ValueType> list2 = value2.value();
        if (list1.size() == 0)
        {
            return value2;
        }
        if (list2.size() == 0)
        {
            return value1;
        }

        List<ValueType> merged = new ArrayList<ValueType>(list1.size() + list2.size());
        merged.addAll(list1);
        merged.addAll(list2);
        return entity(merged);
    }

}
