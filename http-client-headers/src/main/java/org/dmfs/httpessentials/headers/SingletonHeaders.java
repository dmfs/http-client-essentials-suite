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

import org.dmfs.iterators.SingletonIterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Trivial {@link Headers} that contain a single {@link Header} only.
 *
 * @author Marten Gajda
 */
public final class SingletonHeaders implements Headers
{
    private final Header<?> mHeader;


    /**
     * Creates {@link Headers} containing a single {@link Header}.
     *
     * @param header
     *         The {@link Header} of this {@link Headers} object.
     */
    public SingletonHeaders(Header<?> header)
    {
        mHeader = header;
    }


    @Override
    public Iterator<Header<?>> iterator()
    {
        return new SingletonIterator<>(mHeader);
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return mHeader.type().equals(headerType);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        if (!mHeader.type().equals(headerType))
        {
            throw new NoSuchElementException(
                    String.format("Single header in SingletonHeaders is not a %s.", headerType.name()));
        }
        return (Header<T>) mHeader;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        if (!mHeader.type().equals(headerType))
        {
            throw new NoSuchElementException(
                    String.format("Single header in SingletonHeaders is not a %s.", headerType.name()));
        }
        return (Header<List<T>>) mHeader;
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        if (mHeader.type().equals(header.type()))
        {
            // the new header overrides the old one, since it has the same type
            return new SingletonHeaders(header);
        }
        return new UpdatedHeaders(this, header);
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        if (mHeader.type().equals(headerType))
        {
            return EmptyHeaders.INSTANCE;
        }
        return this;
    }

}
