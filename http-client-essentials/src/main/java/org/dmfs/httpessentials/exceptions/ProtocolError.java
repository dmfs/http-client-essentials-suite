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

/**
 * An {@link Exception} that's thrown when an error at application level occurred while handling a response. This is to be used for errors that are valid
 * responses within the scope of the application protocol. <p> An example of a ProtocolError is when you try to upload a file but the server refuses to store it
 * for a specific reason. </p> This is different from a {@link ProtocolException} which is thrown when the response does not conform to the application
 * protocol.
 *
 * @author Marten Gajda
 * @see ProtocolException
 */
public class ProtocolError extends Exception
{
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link ProtocolError} with a message.
     *
     * @param message
     *         An error message.
     */
    public ProtocolError(String message)
    {
        super(message);
    }


    /**
     * Create a new {@link ProtocolError} with a message and a cause.
     *
     * @param message
     *         An error message.
     * @param cause
     *         The reason for this error.
     */
    public ProtocolError(String message, Throwable cause)
    {
        super(message, cause);
    }
}
