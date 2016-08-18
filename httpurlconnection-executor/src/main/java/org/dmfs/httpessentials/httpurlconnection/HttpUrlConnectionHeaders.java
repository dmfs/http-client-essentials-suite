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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.headers.*;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * An {@link Headers} adapter for {@link HttpURLConnection}s.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
final class HttpUrlConnectionHeaders implements Headers
{
    private final HttpURLConnection mConnection;


    /**
     * Provides {@link Headers} for the given {@link HttpURLConnection}.
     *
     * @param connection
     *         The {@link HttpURLConnection}.
     */
    public HttpUrlConnectionHeaders(HttpURLConnection connection)
    {
        mConnection = connection;
    }


    @Override
    public boolean contains(HeaderType<?> headerType)
    {
        return mConnection.getHeaderFields().containsKey(headerType.name());
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
        return headerType.entityFromString(mConnection.getHeaderField(headerType.name()));
    }


    @Override
    public <T> Header<List<T>> header(ListHeaderType<T> headerType)
    {
        List<String> headers = mConnection.getHeaderFields().get(headerType.name());

        @SuppressWarnings("unchecked")
        Header<List<T>> result = headerType.entity((List<T>) Collections.emptyList());

        for (String headerString : headers)
        {
            Header<List<T>> header = headerType.entityFromString(headerString);
            result = headerType.merged(result, header);
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
