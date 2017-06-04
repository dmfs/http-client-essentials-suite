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

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.status.StatusLineHttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class SimpleResponseFactory implements CacheResponseFactory
{
    @Override
    public HttpResponse response(URI uri, InputStream inputStream) throws IOException
    {
        InputStream responseHeaderStream = new PartialInputStream(inputStream);
        BufferedReader responseHeaderReader = new BufferedReader(new InputStreamReader(responseHeaderStream));

        String responseUri = responseHeaderReader.readLine();
        String statusLine = responseHeaderReader.readLine();

        final Map<String, String> responseHeaders = new HashMap<>(16);
        String headerLine = responseHeaderReader.readLine();
        while (headerLine != null)
        {
            String[] valuePair = headerLine.split(":", 2);
            responseHeaders.put(valuePair[0].toUpperCase(), valuePair[1]);
            headerLine = responseHeaderReader.readLine();
        }

        return new CachedResponse(new StatusLineHttpStatus(statusLine), uri, URI.create(responseUri), new Cached.MapHeaders(responseHeaders), inputStream);
    }
}
