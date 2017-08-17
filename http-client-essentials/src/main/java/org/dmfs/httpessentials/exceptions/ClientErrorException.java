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

package org.dmfs.httpessentials.exceptions;

import org.dmfs.httpessentials.HttpStatus;


/**
 * This is a special {@link UnexpectedStatusException} for <code>4xx</code> status codes (i.e. client errors).
 *
 * @author Marten Gajda
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5">RFC 7231, section 6.5</a>
 */
public class ClientErrorException extends UnexpectedStatusException
{
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link ClientErrorException}.
     *
     * @param status
     *         The {@link HttpStatus} returned by the server.
     */
    public ClientErrorException(HttpStatus status)
    {
        this(status, null);
    }


    /**
     * Create a new {@link ClientErrorException} with a message.
     *
     * @param status
     *         The {@link HttpStatus} returned by the server.
     * @param message
     *         An error message.
     */
    public ClientErrorException(HttpStatus status, String message)
    {
        super(status, message);
    }
}
