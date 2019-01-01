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
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.optional.decorators.Mapped;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * The Apache {@link HttpEntity} of the {@link HttpRequest} to send.
 *
 * @author Marten Gajda
 */
final class ApacheHttpEntity implements HttpEntity
{
    private final HttpRequest<?> mRequest;


    public ApacheHttpEntity(HttpRequest<?> request)
    {
        mRequest = request;
    }


    @Override
    public boolean isRepeatable()
    {
        return true;
    }


    @Override
    public boolean isChunked()
    {
        return !mRequest.requestEntity().contentLength().isPresent();
    }


    @Override
    public long getContentLength()
    {
        return mRequest.requestEntity().contentLength().value(-1L);
    }


    @Override
    public Header getContentType()
    {
        return new Mapped<>(
                contentType -> new BasicHeader("content-type", contentType.type()),
                mRequest.requestEntity().contentType()).value(null);
    }


    @Override
    public Header getContentEncoding()
    {
        if (!mRequest.headers().contains(HttpHeaders.CONTENT_ENCODING))
        {
            return null;
        }
        org.dmfs.httpessentials.headers.Header<List<String>> contentEncoding = mRequest.headers().header(HttpHeaders.CONTENT_ENCODING);

        return contentEncoding.value().isEmpty()
                ? null
                : new BasicHeader("content-encoding", contentEncoding.toString());
    }


    @Override
    public InputStream getContent() throws IllegalStateException
    {
        throw new IllegalStateException("Request entity doesn't have an InputStream");
    }


    @Override
    public void writeTo(OutputStream outstream) throws IOException
    {
        mRequest.requestEntity().writeContent(outstream);
    }


    @Override
    public boolean isStreaming()
    {
        // not streaming
        return false;
    }


    @Override
    public void consumeContent()
    {
        //  nothing to do
    }
}
