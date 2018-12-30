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
import org.dmfs.optional.Absent;
import org.dmfs.optional.Optional;

import java.io.OutputStream;


/**
 * An empty {@link HttpRequestEntity}. This entity doesn't have any {@link MediaType}. This is typically used for requests that don't send a message body.
 *
 * @author Marten Gajda
 */
public final class EmptyHttpRequestEntity implements HttpRequestEntity
{
    public final static HttpRequestEntity INSTANCE = new EmptyHttpRequestEntity();


    @Override
    public Optional<MediaType> contentType()
    {
        return Absent.absent();
    }


    @Override
    public Optional<Long> contentLength()
    {
        return Absent.absent();
    }


    @Override
    public void writeContent(OutputStream out)
    {
    }

}
