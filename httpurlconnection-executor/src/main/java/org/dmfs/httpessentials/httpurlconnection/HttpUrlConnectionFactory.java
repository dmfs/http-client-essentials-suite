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

package org.dmfs.httpessentials.httpurlconnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;


/**
 * Interface of a factory for HttpUrlConnection instances.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface HttpUrlConnectionFactory
{
    /**
     * Returns an {@link HttpURLConnection} to the given Uri.
     *
     * @param uri
     *         The uri to connect to. This must be an absolute URL.
     *
     * @return An {@link HttpURLConnection}.
     *
     * @throws IllegalArgumentException
     *         if the given URI was not a valid absolute URL.
     * @throws IOException
     */
    public HttpURLConnection httpUrlConnection(URI uri) throws IllegalArgumentException, IOException;
}
