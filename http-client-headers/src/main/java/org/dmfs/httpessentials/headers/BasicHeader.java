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

package org.dmfs.httpessentials.headers;

/**
 * A basic implementation of a {@link Header}.
 *
 * @param <ValueType>
 *         The type of the header value.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
final class BasicHeader<ValueType> implements Header<ValueType>
{

    private final HeaderType<ValueType> mHeaderType;
    private final ValueType mValue;


    /**
     * Creates a simple header from {@link HeaderType} and a value.
     *
     * @param headerType
     *         The {@link HeaderType}.
     * @param value
     *         The header value.
     */
    public BasicHeader(HeaderType<ValueType> headerType, ValueType value)
    {
        mHeaderType = headerType;
        mValue = value;
    }


    @Override
    public HeaderType<ValueType> type()
    {
        return mHeaderType;
    }


    @Override
    public ValueType value()
    {
        return mValue;
    }


    @Override
    public String toString()
    {
        return mHeaderType.valueString(mValue);
    }
}
