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

import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.adapters.Conditional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.optional.elementary.NullSafe;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Marten Gajda
 */
public final class EssentialsHttpResponseEntity implements HttpResponseEntity
{
    private final org.apache.http.HttpResponse mResponse;


    public EssentialsHttpResponseEntity(org.apache.http.HttpResponse response)
    {
        mResponse = response;
    }


    @Override
    public Optional<MediaType> contentType()
    {
        return new Mapped<>(contentType -> new StringMediaType(contentType.getValue()), new NullSafe<>(mResponse.getEntity().getContentType()));
    }


    @Override
    public Optional<Long> contentLength()
    {
        // return a positive content lengths only
        return new Conditional<>(length -> length >= 0, mResponse.getEntity().getContentLength());
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return mResponse.getEntity().getContent();
    }
}
