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

import org.apache.http.Header;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;
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
 * @author Marten Gajda
 */
public class EssentialsHttpResponseEntity implements HttpResponseEntity
{
    private final org.apache.http.HttpResponse mResponse;


    public EssentialsHttpResponseEntity(org.apache.http.HttpResponse response)
    {
        mResponse = response;
    }


    @Override
    public Optional<MediaType> contentType()
    {
        return new Mapped<>(new Function<Header, MediaType>()
        {
            @Override
            public MediaType apply(Header argument)
            {
                return new StringMediaType(argument.getValue());
            }
        }, new NullSafe<>(mResponse.getEntity().getContentType()));
    }


    @Override
    public Optional<Long> contentLength()
    {
        long contentLenght = mResponse.getEntity().getContentLength();
        return contentLenght >= 0 ? new Present<>(contentLenght) : Absent.<Long>absent();
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return mResponse.getEntity().getContent();
    }
}
