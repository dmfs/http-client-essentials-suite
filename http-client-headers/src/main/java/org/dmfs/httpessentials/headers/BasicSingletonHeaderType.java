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

package org.dmfs.httpessentials.headers;

import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * Basic header type class for headers that don't have list values.
 *
 * @param <ValueType>
 *         The type of the header value.
 *
 * @author Marten Gajda
 */
public final class BasicSingletonHeaderType<ValueType> implements SingletonHeaderType<ValueType>
{
    private final String mHeaderName;
    private final EntityConverter<ValueType> mValueConverter;


    /**
     * Creates a {@link HeaderType} for the given header name. Header values are converted using the provided {@link EntityConverter}.
     *
     * @param headerName
     *         The name of the header.
     * @param valueConverter
     *         A {@link EntityConverter} to convert header values from/to string.
     */
    public BasicSingletonHeaderType(String headerName, EntityConverter<ValueType> valueConverter)
    {
        mHeaderName = headerName;
        mValueConverter = valueConverter;
    }


    @Override
    public String name()
    {
        return mHeaderName;
    }


    @Override
    public Header<ValueType> entityFromString(String headerValueString)
    {
        return new BasicHeader<ValueType>(this, mValueConverter.value(headerValueString));
    }


    @Override
    public Header<ValueType> entity(ValueType value)
    {
        return new BasicHeader<ValueType>(this, value);
    }


    @Override
    public String valueString(ValueType headerValue)
    {
        return mValueConverter.valueString(headerValue);
    }


    @Override
    public ValueType valueFromString(String valueString)
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
        return this == obj || (obj instanceof SingletonHeaderType && mHeaderName.equals(((HeaderType<?>) obj).name()));
    }

}
