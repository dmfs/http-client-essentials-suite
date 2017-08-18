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

package org.dmfs.httpessentials.responsehandlers;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.optional.Optional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * A simple {@link HttpResponseHandler} that returns the response body as a {@link String} using the charset given in the content-type if there is any or
 * falling back to a default charset otherwise.
 *
 * @author Marten Gajda
 */
public final class StringResponseHandler implements HttpResponseHandler<String>
{

    public final static String UTF8_CHARSET = "UTF-8";

    private final static int BUFFER_SIZE = 16 * 1024;

    private final String mDefaultCharset;
    private final MediaType mDefaultMediaType;


    /**
     * A {@link StringResponseHandler} that falls back to {@value #UTF8_CHARSET} if there is no charset given in the response.
     */
    public StringResponseHandler()
    {
        this(UTF8_CHARSET);
    }


    /**
     * A {@link StringResponseHandler} that falls back to the given charset if there is no charset given in the response.
     */
    public StringResponseHandler(String defaultCharset)
    {
        this(defaultCharset, new StructuredMediaType("plain", "text", defaultCharset));
    }


    /**
     * A {@link StringResponseHandler} that falls back to the given charset and {@link MediaType} if there is no charset given in the response.
     */
    public StringResponseHandler(String defaultCharset, MediaType defaultMediaType)
    {
        mDefaultCharset = defaultCharset;
        mDefaultMediaType = defaultMediaType;
    }


    @Override
    public String handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        HttpResponseEntity entity = response.responseEntity();

        Optional<MediaType> contentType = entity.contentType();

        StringBuilder builder = new StringBuilder((int) (long) entity.contentLength().value((long) BUFFER_SIZE));

        // note: we already read the content in larger chunks so there should be no need for a BufferedReader
        Reader reader = new InputStreamReader(entity.contentStream(), contentType.value(mDefaultMediaType).charset(mDefaultCharset));
        try
        {
            int read;
            final char[] buffer = new char[BUFFER_SIZE];

            while ((read = reader.read(buffer)) >= 0)
            {
                builder.append(buffer, 0, read);
            }

            return builder.toString();
        }
        finally
        {
            reader.close();
        }
    }
}
