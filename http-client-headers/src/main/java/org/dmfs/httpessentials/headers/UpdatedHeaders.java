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

import org.dmfs.iterators.Filter;
import org.dmfs.iterators.SingletonIterator;
import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.decorators.Serialized;

import java.util.Iterator;
import java.util.List;


/**
 * {@link Headers} that add or replace a {@link Header}.
 *
 * @author Marten Gajda
 */
public final class UpdatedHeaders implements Headers
{
    private final Headers mHeaders;
    private final Header<?> mNewHeader;


    /**
     * Create new {@link Headers} that update a single header of other {@link Headers}.
     *
     * @param headers
     *         The original headers.
     * @param updatedHeader
     *         The updated header.
     */
    public UpdatedHeaders(Headers headers, Header<?> updatedHeader)
    {
        mHeaders = headers;
        mNewHeader = updatedHeader;
    }


    @Override
    public Iterator<Header<?>> iterator()
    {
        return new Serialized<>(
                new Filtered<>(mHeaders.iterator(), new Filter<Header<?>>()
                {
                    @Override
                    public boolean iterate(Header<?> element)
                    {
                        return !element.type().equals(mNewHeader.type());
                    }
                }), new SingletonIterator<Header<?>>(mNewHeader));
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return mNewHeader.type().equals(headerType) || mHeaders.contains(headerType);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        return mNewHeader.type().equals(headerType) ? (Header<T>) mNewHeader : mHeaders.header(headerType);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        return mNewHeader.type().equals(headerType) ? (Header<List<T>>) mNewHeader : mHeaders.header(headerType);
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        if (mNewHeader.type().equals(header.type()))
        {
            return new UpdatedHeaders(mHeaders, header);
        }
        return new UpdatedHeaders(this, header);
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        if (mNewHeader.type().equals(headerType))
        {
            return mHeaders.withoutHeaderType(headerType);
        }
        if (mHeaders.contains(headerType))
        {
            return new UpdatedHeaders(mHeaders.withoutHeaderType(headerType), mNewHeader);
        }
        return this;
    }
}
