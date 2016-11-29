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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Trivial {@link Headers} that don't contain any {@link Header}s.
 *
 * @author Marten Gajda
 */
public final class EmptyHeaders implements Headers
{
    /**
     * An instance of {@link EmptyHeaders}.
     */
    public final static EmptyHeaders INSTANCE = new EmptyHeaders();


    @Override
    public Iterator<Header<?>> iterator()
    {
        return Collections.emptyIterator();
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return false;
    }


    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        throw new NoSuchElementException("EmptyHeaders don't contain any Headers");
    }


    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        throw new NoSuchElementException("EmptyHeaders don't contain any Headers");
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        return new SingletonHeaders(header);
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        return this;
    }

}
