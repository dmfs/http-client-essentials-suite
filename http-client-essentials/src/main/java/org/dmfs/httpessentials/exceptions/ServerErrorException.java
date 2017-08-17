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
 * This is a special {@link UnexpectedStatusException} for <code>5xx</code> status codes (i.e. server errors).
 *
 * @author Marten Gajda
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6">RFC 7231, section 6.6</a>
 */
public class ServerErrorException extends UnexpectedStatusException
{
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link ServerErrorException}.
     *
     * @param status
     *         The status returned by the server.
     */
    public ServerErrorException(HttpStatus status)
    {
        this(status, null);
    }


    /**
     * Create a new {@link ServerErrorException} with a message.
     *
     * @param status
     *         The status returned by the server.
     * @param message
     *         An error message.
     */
    public ServerErrorException(HttpStatus status, String message)
    {
        super(status, message);
    }
}
