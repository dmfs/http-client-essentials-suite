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

package org.dmfs.httpessentials.apache4;

import org.apache.http.HttpResponse;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.ListHeaderType;
import org.dmfs.httpessentials.headers.SingletonHeaderType;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * The {@link Headers} of an Apache {@link HttpResponse}.
 *
 * @author Marten Gajda
 */
final class ApacheHeaders implements Headers
{
    private final HttpResponse mResponse;


    public ApacheHeaders(HttpResponse response)
    {
        mResponse = response;
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return mResponse.containsHeader(headerType.name());
    }


    @Override
    public <T> Header<T> header(SingletonHeaderType<T> headerType)
    {
        return headerType.entityFromString(mResponse.getFirstHeader(headerType.name()).getValue());
    }


    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        Header<List<T>> header = headerType.entity(Collections.<T>emptyList());
        for (org.apache.http.Header headers : mResponse.getHeaders(headerType.name()))
        {
            header = headerType.merged(header, headerType.entityFromString(headers.getValue()));
        }
        return header;
    }


    @Override
    public <T> Headers withHeader(Header<T> header)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    @Override
    public <T> Headers withoutHeaderType(HeaderType<T> headerType)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    @Override
    public Iterator<Header<?>> iterator()
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
