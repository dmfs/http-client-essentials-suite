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

import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link HttpResponseEntity} that returns a given {@link InputStream}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class InputStreamResponseEntity implements HttpResponseEntity
{
    private final InputStream mPayload;
    private final MediaType mMediaType;
    private final long mLength;


    public InputStreamResponseEntity(InputStream payload, MediaType mediaType, long length)
    {
        mPayload = payload;
        mMediaType = mediaType;
        mLength = length;
    }


    @Override
    public MediaType contentType() throws IOException
    {
        return mMediaType;
    }


    @Override
    public long contentLength() throws IOException
    {
        return mLength;
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return mPayload;
    }
}
