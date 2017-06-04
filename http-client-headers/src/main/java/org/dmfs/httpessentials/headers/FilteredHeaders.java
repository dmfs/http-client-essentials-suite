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

import org.dmfs.iterators.Filter;
import org.dmfs.iterators.decorators.Filtered;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A {@link Headers} decorator that filters certain {@link HeaderType}s from the given {@link Headers}
 *
 * @author Marten Gajda
 */
public final class FilteredHeaders implements Headers
{
    private final Headers mOriginalHeaders;
    private final HeaderType<?>[] mFilteredHeaderTypes;


    /**
     * Creates a {@link Headers} from another {@link Headers} which doesn't contain {@link Header}s of the given {@link HeaderType}s
     *
     * @param originalHeaders
     *         The original {@link Headers}.
     * @param filteredHeaderTypes
     *         The {@link HeaderType}s to remove from the original Headers.
     */
    public FilteredHeaders(Headers originalHeaders, HeaderType<?>... filteredHeaderTypes)
    {
        mOriginalHeaders = originalHeaders;
        mFilteredHeaderTypes = filteredHeaderTypes;
    }


    @Override
    public Iterator<Header<?>> iterator()
    {
        return new Filtered<>(mOriginalHeaders.iterator(), new Filter<Header<?>>()
        {
            @Override
            public boolean iterate(Header<?> element)
            {
                return !isFiltered(element.type());
            }
        });
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return !isFiltered(headerType) && mOriginalHeaders.contains(headerType);
    }


    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        if (isFiltered(headerType))
        {
            throw new NoSuchElementException(String.format("No headers of type %s found", headerType.name()));
        }
        return mOriginalHeaders.header(headerType);
    }


    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        if (isFiltered(headerType))
        {
            throw new NoSuchElementException(String.format("No headers of type %s found", headerType.name()));
        }
        return mOriginalHeaders.header(headerType);
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        if (mFilteredHeaderTypes.length == 1 && mFilteredHeaderTypes[0].equals(header.type()))
        {
            return new UpdatedHeaders(mOriginalHeaders, header);
        }
        else
        {
            return new UpdatedHeaders(this, header);
        }
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        if (isFiltered(headerType))
        {
            return this;
        }
        if (!mOriginalHeaders.contains(headerType))
        {
            return this;
        }
        return new FilteredHeaders(mOriginalHeaders.withoutHeaderType(headerType), mFilteredHeaderTypes);
    }


    /**
     * Returns whether a Header of the given type may be in this set or if it's been removed explicitly.
     *
     * @param headerType
     *
     * @return
     */
    private boolean isFiltered(HeaderType<?> headerType)
    {
        for (HeaderType<?> type : mFilteredHeaderTypes)
        {
            if (type.equals(headerType))
            {
                return true;
            }
        }
        return false;
    }

}
