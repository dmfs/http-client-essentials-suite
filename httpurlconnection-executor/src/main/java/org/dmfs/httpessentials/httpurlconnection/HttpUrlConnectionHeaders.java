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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.headers.FilteredHeaders;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.ListHeaderType;
import org.dmfs.httpessentials.headers.SingletonHeaderType;
import org.dmfs.httpessentials.headers.UpdatedHeaders;
import org.dmfs.httpessentials.httpurlconnection.utils.iterators.StringEqualsIgnoreCase;
import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.decorators.Flattened;
import org.dmfs.jems.iterator.decorators.Mapped;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;


/**
 * An {@link Headers} adapter for {@link HttpURLConnection}s.
 *
 * @author Marten Gajda
 */
final class HttpUrlConnectionHeaders implements Headers
{
    private final Map<String, List<String>> mHeaders;


    /**
     * Provides {@link Headers} for the given {@link HttpURLConnection}.
     *
     * @param headers
     *         A {@link Map} with all the headers of the response.
     */
    public HttpUrlConnectionHeaders(Map<String, List<String>> headers)
    {
        mHeaders = headers;
    }


    @Override
    public boolean contains(final HeaderType<?> headerType)
    {
        // the Map contains header names as returned by the server - though, we need to compare them in a case-insensitive manner
        return new Filtered<>(mHeaders.keySet().iterator(), new StringEqualsIgnoreCase(headerType.name())).hasNext();
    }


    @Override
    public Iterator<Header<?>> iterator()
    {
        /*
         * Note, technically we can't support this, because we don't know the type of each header. However we could implement something that returns all headers
         * as plain string headers. This implementation is postponed until someone actually needs this method.
         */
        throw new UnsupportedOperationException("Iterating headers is not supported by HttpUrlConnectionHeaders");
    }


    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        // the Map which header names as returned by the server - though, we need to compare them in a case-insensitive manner
        return headerType.entityFromString(mHeaders.get(
                new Filtered<>(mHeaders.keySet().iterator(), new StringEqualsIgnoreCase(headerType.name())).next()).get(0));
    }


    @Override
    public <T> Header<List<T>> header(final ListHeaderType<T> headerType)
    {
        Header<List<T>> result = headerType.entity(emptyList());

        final Iterator<Header<List<T>>> headerIterator = new Mapped<>(
                headerType::entityFromString,
                new Flattened<>(
                        new Mapped<>(
                                mHeaders::get,
                                new Filtered<>(mHeaders.keySet().iterator(),
                                        new StringEqualsIgnoreCase(headerType.name())))));

        // combine all headers of this type into one
        while (headerIterator.hasNext())
        {
            result = headerType.merged(result, headerIterator.next());
        }
        return result;
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        return new UpdatedHeaders(this, header);
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        return new FilteredHeaders(this, headerType);
    }
}
