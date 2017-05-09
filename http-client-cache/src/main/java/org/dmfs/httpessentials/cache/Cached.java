/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache;

import org.dmfs.httpessentials.cache.cache.Cache;
import org.dmfs.httpessentials.cache.cache.CacheEntity;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.decoration.ResponseDecorated;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.FilteredHeaders;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.ListHeaderType;
import org.dmfs.httpessentials.headers.SingletonHeaderType;
import org.dmfs.httpessentials.headers.UpdatedHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Initial implementation of a simple caching {@link HttpRequestExecutor}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Cached implements HttpRequestExecutor
{
    private final static String VERSION1 = "1";

    private final HttpRequestExecutor mDelegate;
    private final CachePolicy mCachePolicy;
    private final Cache mCache;


    public Cached(HttpRequestExecutor delegate, CachePolicy cachePolicy, Cache cache)
    {
        mDelegate = delegate;
        mCachePolicy = cachePolicy;
        mCache = cache;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        if (!mCachePolicy.isCacheable(request))
        {
            // not cacheable, just delegate to the decorated executor
            return mDelegate.execute(uri, request);
        }

        final CacheEntity element = mCache.cacheEntity(new RequestCacheKey(uri, request));
        if (element.isEmpty())
        {
            return executeCached(uri, request, element);
        }

        InputStream value = element.inputStream();
        InputStream introStream = new PartialInputStream(value);
        BufferedReader reader = new BufferedReader(new InputStreamReader(introStream));

        String version = reader.readLine();
        if (!VERSION1.equals(version))
        {
            return executeCached(uri, request, element);
        }

        String verb = reader.readLine();
        if (!request.method().verb().equals(verb))
        {
            return executeCached(uri, request, element);
        }

        String uriString = reader.readLine();
        if (!uriString.equals(uri.toASCIIString()))
        {
            return executeCached(uri, request, element);
        }

        final Map<String, String> requestHeaders = new HashMap<>(16);
        String headerLine = reader.readLine();
        while (headerLine != null)
        {
            String[] valuePair = headerLine.split(":", 2);
            requestHeaders.put(valuePair[0], valuePair[1]);
            headerLine = reader.readLine();
        }

        HttpResponse cacheResponse = new SimpleResponseFactory().response(uri, value);
        // return response from cache
        return request.responseHandler(cacheResponse).handleResponse(cacheResponse);

    }


    private <T> T executeCached(URI uri, HttpRequest<T> request, final CacheEntity element) throws IOException, ProtocolError, ProtocolException
    {
        // not cached yet
        final OutputStream out = element.outputStream();
        final Writer writer = new OutputStreamWriter(out);
        // first write the request
        writeIntroVersion1(request, uri, writer);

        HttpRequest<T> decoratedRequest = new ResponseDecorated<T>(request, new Decoration<HttpResponse>()
        {
            @Override
            public HttpResponse decorated(final HttpResponse original)
            {
                try
                {
                    if (!mCachePolicy.isCacheable(original))
                    {
                        // response not cacheable, wipe cache and return original response
                        out.close();
                        element.wipe();
                        return original;
                    }
                    writeResponseVersion1(original, writer);
                    writer.flush();
                    return new DuplicatingResponse(original, out);
                }
                catch (IOException e)
                {
                    throw new RuntimeException();
                }
            }
        });
        return mDelegate.execute(uri, decoratedRequest);
    }


    private void writeIntroVersion1(HttpRequest<?> request, URI requestUri, Writer out) throws IOException
    {
        out.write(VERSION1);
        out.write('\n');
        out.write(request.method().verb());
        out.write('\n');
        out.write(requestUri.toASCIIString());
        out.write('\n');
        writeHeaders(request.headers(), out);
        // add an empty line to separate the response
        out.write('\n');
    }


    private void writeResponseVersion1(HttpResponse response, Writer out) throws IOException
    {
        out.write(response.responseUri().toASCIIString());
        out.write('\n');
        out.write(response.status().httpStatusLine(1, 1));
        out.write('\n');
        writeHeaders(response.headers(), out);
        // add an empty line to separate the payload
        out.write('\n');
    }


    private void writeHeaders(Headers headers, Writer out) throws IOException
    {
        for (Header header : headers)
        {
            out.write(header.type().name());
            out.write(':');
            out.write(header.toString());
            out.write('\n');
        }
    }


    public static class MapHeaders implements Headers
    {
        private final Map<String, String> mHeaders;


        public MapHeaders(Map<String, String> headers)
        {
            mHeaders = headers;
        }


        @Override
        public boolean contains(HeaderType<?> headerType)
        {
            return mHeaders.containsKey(headerType.name().toUpperCase());
        }


        @Override
        public <T> Header<T> header(SingletonHeaderType<T> headerType)
        {
            String header = mHeaders.get(headerType.name().toUpperCase());
            if (header == null)
            {
                throw new NoSuchElementException(String.format("No headers of type %s found", headerType.name()));
            }
            return headerType.entityFromString(header);
        }


        @Override
        public <T> Header<List<T>> header(ListHeaderType<T> headerType)
        {
            String header = mHeaders.get(headerType.name().toUpperCase());
            if (header == null)
            {
                throw new NoSuchElementException(String.format("No headers of type %s found", headerType.name()));
            }
            return headerType.entityFromString(header);
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
            throw new UnsupportedOperationException("Iterating over headers is not supported by cached headers.");
        }
    }
}
