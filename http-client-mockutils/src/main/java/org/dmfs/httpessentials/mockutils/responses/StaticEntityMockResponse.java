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

package org.dmfs.httpessentials.mockutils.responses;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.*;
import org.dmfs.httpessentials.typedentity.EntityConverter;

import java.io.IOException;
import java.net.URI;


/**
 * A mock {@link HttpResponse} that contains a specific {@link HttpResponseEntity}. The response status will always be
 * {@link HttpStatus#OK}. Decorate with {@link CustomStatusMockResponse} to override the status. This will automatically
 * set the {@link HttpHeaders#CONTENT_TYPE} and {@link HttpHeaders#CONTENT_LENGTH} if specified by the entity.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public class StaticEntityMockResponse implements HttpResponse
{

    // TODO: remove this once http-client-headers contains a definition for this
    private final static SingletonHeaderType<Long> CONTENT_LENGTH = new BasicSingletonHeaderType<Long>("content-length",
            new EntityConverter<Long>()
            {
                public Long value(String valueString)
                {
                    return Long.parseLong(valueString);
                }


                ;


                public String valueString(Long value)
                {
                    return String.valueOf(value);
                }


                ;
            });

    private final HttpResponseEntity mEntity;


    public StaticEntityMockResponse(HttpResponseEntity entity)
    {
        mEntity = entity;
    }


    @Override
    public HttpStatus status()
    {
        return HttpStatus.OK;
    }


    @Override
    public Headers headers()
    {
        try
        {
            Headers result = EmptyHeaders.INSTANCE;
            if (mEntity.contentType() != null)
            {
                result = result.withHeader(HttpHeaders.CONTENT_TYPE.entity(mEntity.contentType()));
            }
            if (mEntity.contentLength() >= 0)
            {
                result = result.withHeader(CONTENT_LENGTH.entity(mEntity.contentLength()));
            }
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't retrieve content-type", e);
        }
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return mEntity;
    }


    @Override
    public URI requestUri()
    {
        throw new UnsupportedOperationException("this response doesn't define a request uri, use a decorator for that");
    }


    @Override
    public URI responseUri()
    {
        throw new UnsupportedOperationException(
                "this response doesn't define a response uri, use a decorator for that");
    }

}
