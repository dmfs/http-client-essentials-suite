/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


/**
 * An {@link HttpResponseEntity} adapter for {@link HttpURLConnection}s.
 *
 * @author Marten Gajda
 */
final class HttpUrlConnectionResponseEntity implements HttpResponseEntity
{

    private final HttpURLConnection mConnection;


    public HttpUrlConnectionResponseEntity(HttpURLConnection connection)
    {
        mConnection = connection;
    }


    @Override
    public MediaType contentType() throws IOException
    {
        return new StringMediaType(mConnection.getContentType());
    }


    @Override
    public long contentLength() throws IOException
    {
        return mConnection.getContentLength();
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        if (mConnection.getResponseCode() < 400)
        {
            return mConnection.getInputStream();
        }
        else
        {
            return mConnection.getErrorStream();
        }
    }

}
