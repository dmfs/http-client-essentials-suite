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

package org.dmfs.httpessentials.httpurlconnection.factories;

import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;


/**
 * A trivial {@link HttpUrlConnectionFactory}. It merely opens the given connection and performs no other configuration.
 * You may decorate this to do some more advanced configuration.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory
{

    @Override
    public HttpURLConnection httpUrlConnection(URI uri) throws IllegalArgumentException, IOException
    {
        try
        {
            return (HttpURLConnection) uri.toURL().openConnection();
        }
        catch (ClassCastException | MalformedURLException e)
        {
            throw new IllegalArgumentException(
                    String.format("The URI %s does not represent an absolute HTTP URL.", uri.toASCIIString()), e);

        }
    }

}
