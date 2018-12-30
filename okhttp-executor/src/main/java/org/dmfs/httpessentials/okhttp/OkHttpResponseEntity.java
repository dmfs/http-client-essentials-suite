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

import okhttp3.ResponseBody;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.adapters.Conditional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.optional.elementary.NullSafe;

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
    public Optional<MediaType> contentType()
    {
        return new Mapped<>(
                mediaType -> new StringMediaType(mediaType.toString()),
                new NullSafe<>(mResponseBody.contentType()));
    }


    @Override
    public Optional<Long> contentLength()
    {
        return new Conditional<>(length -> length >= 0, mResponseBody.contentLength());
    }


    @Override
    public InputStream contentStream()
    {
        return mResponseBody.byteStream();
    }
}
