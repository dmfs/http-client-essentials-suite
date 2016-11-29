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


/**
 * An exception that's thrown when a response status code was unexpected. <p> There are three major subclasses: {@link RedirectionException} for
 * <code>3xx</code> response status codes, {@link ClientErrorException} for <code>4xx</code> response status codes and {@link ServerErrorException} for
 * <code>5xx</code> response status codes, some of them having subclasses themselves. Implementations should always pick the most appropriate error class to
 * allow for proper exception handling. </p>
 *
 * @author Marten Gajda
 */
public class UnexpectedStatusException extends ProtocolException
{
    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 0;

    /**
     * The status that was returned by the server.
     */
    private final HttpStatus mStatus;


    /**
     * Create a new {@link UnexpectedStatusException}.
     *
     * @param status
     *         The {@link HttpStatus} returned by the server.
     */
    public UnexpectedStatusException(HttpStatus status)
    {
        this(status, null);
    }


    /**
     * Create a new {@link UnexpectedStatusException} with a message.
     *
     * @param status
     *         The {@link HttpStatus} returned by the server.
     * @param message
     *         An error message.
     */
    public UnexpectedStatusException(HttpStatus status, String message)
    {
        super(message);
        mStatus = status;
    }


    /**
     * Returns the {@link HttpStatus} as returned by the server.
     *
     * @return The {@link HttpStatus}.
     */
    public HttpStatus status()
    {
        return mStatus;
    }
}
