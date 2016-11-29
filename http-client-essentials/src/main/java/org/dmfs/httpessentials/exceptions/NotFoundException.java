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

package org.dmfs.httpessentials.exceptions;

import org.dmfs.httpessentials.HttpStatus;

import java.net.URI;


/**
 * This Exception is thrown when an unhandled {@link HttpStatus#NOT_FOUND} status code occurs.
 *
 * @author Marten Gajda
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">RFC 7231, section 6.5.4</a>
 */
public class NotFoundException extends ClientErrorException
{

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;

    private final URI mUri;


    /**
     * Create a new {@link NotFoundException}.
     */
    public NotFoundException(URI uri)
    {
        this(uri, null);
    }


    /**
     * Create a new {@link NotFoundException} with a message.
     *
     * @param message
     *         An error message.
     */
    public NotFoundException(URI uri, String message)
    {
        super(HttpStatus.NOT_FOUND, message);
        mUri = uri;
    }


    /**
     * Returns the URI that was not found.
     *
     * @return A URI.
     */
    public URI uri()
    {
        return mUri;
    }
}
