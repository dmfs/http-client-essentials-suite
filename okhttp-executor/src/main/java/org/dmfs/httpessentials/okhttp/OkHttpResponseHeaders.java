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

package org.dmfs.httpessentials.okhttp;

import okhttp3.Response;
import org.dmfs.httpessentials.headers.FilteredHeaders;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.ListHeaderType;
import org.dmfs.httpessentials.headers.SingletonHeaderType;
import org.dmfs.httpessentials.headers.UpdatedHeaders;

import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;


/**
 * Response {@link Headers} based on the headers of an OkHttp {@link Response}.
 *
 * @author Marten Gajda
 */
final class OkHttpResponseHeaders implements Headers
{
    private final Response mResponse;


    public OkHttpResponseHeaders(Response response)
    {
        mResponse = response;
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return mResponse.header(headerType.name()) != null;
    }


    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        return headerType.entityFromString(mResponse.header(headerType.name()));
    }


    @Override
    public <T> Header<List<T>> header(final ListHeaderType<T> headerType)
    {
        List<String> headers = mResponse.headers(headerType.name());
        if (headers.isEmpty())
        {
            return headerType.entity(emptyList());
        }

        Iterator<String> iterator = headers.iterator();
        Header<List<T>> result = headerType.entityFromString(iterator.next());

        while (iterator.hasNext())
        {
            headerType.merged(result, headerType.entityFromString(iterator.next()));
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


    @Override
    public Iterator<Header<?>> iterator()
    {
        throw new UnsupportedOperationException("These headers don't support iterating");
    }
}
