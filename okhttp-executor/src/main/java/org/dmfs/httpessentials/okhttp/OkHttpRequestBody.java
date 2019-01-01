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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.iterators.Function;
import org.dmfs.optional.decorators.Mapped;

import java.io.IOException;


/**
 * {@link RequestBody} adapter for {@link HttpRequestEntity}s.
 *
 * @author Marten Gajda
 */
final class OkHttpRequestBody extends RequestBody
{
    private final HttpRequestEntity mRequestEntity;


    public OkHttpRequestBody(HttpRequestEntity requestEntity)
    {
        mRequestEntity = requestEntity;
    }


    @Override
    public long contentLength()
    {
        return mRequestEntity.contentLength().value(-1L);
    }


    @Override
    public MediaType contentType()
    {
        return new Mapped<>(new MediaTypeConversionFunction(), mRequestEntity.contentType()).value(null);
    }


    @Override
    public void writeTo(final BufferedSink sink) throws IOException
    {
        mRequestEntity.writeContent(sink.outputStream());
    }


    /**
     * A {@link Function} to convert a {@link org.dmfs.httpessentials.types.MediaType} into a {@link MediaType}.
     */
    private final static class MediaTypeConversionFunction implements Function<org.dmfs.httpessentials.types.MediaType, MediaType>
    {
        @Override
        public MediaType apply(org.dmfs.httpessentials.types.MediaType argument)
        {
            return MediaType.parse(argument.toString());
        }
    }
}
