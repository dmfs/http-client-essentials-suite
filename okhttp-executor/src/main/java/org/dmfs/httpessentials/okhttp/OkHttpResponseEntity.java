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
import okhttp3.ResponseBody;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.iterators.Function;
import org.dmfs.optional.Absent;
import org.dmfs.optional.NullSafe;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;
import org.dmfs.optional.decorators.Mapped;

import java.io.IOException;
import java.io.InputStream;


/**
 * A {@link HttpResponseEntity} based on an OkHttp {@link ResponseBody}.
 *
 * @author Marten Gajda
 */
final class OkHttpResponseEntity implements HttpResponseEntity
{
    private final ResponseBody mResponseBody;


    public OkHttpResponseEntity(ResponseBody responseBody)
    {
        mResponseBody = responseBody;
    }


    @Override
    public Optional<org.dmfs.httpessentials.types.MediaType> contentType()
    {
        return new Mapped<>(
                new Function<MediaType, org.dmfs.httpessentials.types.MediaType>()
                {
                    @Override
                    public org.dmfs.httpessentials.types.MediaType apply(MediaType argument)
                    {
                        return new StringMediaType(argument.toString());
                    }
                },
                new NullSafe<>(mResponseBody.contentType()));
    }


    @Override
    public Optional<Long> contentLength()
    {
        return mResponseBody.contentLength() >= 0 ? new Present<Long>(mResponseBody.contentLength()) : Absent.<Long>absent();
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return mResponseBody.byteStream();
    }
}
