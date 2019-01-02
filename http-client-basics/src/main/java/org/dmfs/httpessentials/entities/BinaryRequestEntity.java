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

package org.dmfs.httpessentials.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.elementary.Present;
import org.dmfs.jems.single.Single;

import java.io.IOException;
import java.io.OutputStream;


/**
 * A basic {@link HttpRequestEntity} with binary data. This is meant to be used as a delegate for higher level request entities.
 * <p>
 * Note, this is meant to be used for requests with a message body and always expects a valid {@link MediaType}.
 *
 * @author Marten Gajda
 */
public final class BinaryRequestEntity implements HttpRequestEntity
{
    private final MediaType mMediaType;
    private final Single<byte[]> mData;


    public BinaryRequestEntity(MediaType mediaType, Single<byte[]> data)
    {
        mMediaType = mediaType;
        mData = data;
    }


    @Override
    public Optional<MediaType> contentType()
    {
        return new Present<>(mMediaType);
    }


    @Override
    public Optional<Long> contentLength()
    {
        return new Present<>((long) mData.value().length);
    }


    @Override
    public void writeContent(OutputStream out) throws IOException
    {
        out.write(mData.value());
    }
}
