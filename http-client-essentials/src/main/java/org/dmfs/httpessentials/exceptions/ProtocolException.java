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
 * An {@link Exception} that's thrown when an unrecoverable protocol error at application level occurred while handling a response. <p> An example of a {@link
 * ProtocolException} is if the XML in the response of an XML-based protocol is not well-formed or the server returned a status code that's not allowed for this
 * kind of request. </p> This is different from an {@link ProtocolError} which is thrown if the server returned an error that's valid with respect to the
 * application protocol.
 *
 * @author Marten Gajda
 * @see ProtocolError
 */
public class ProtocolException extends Exception
{
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link ProtocolException} with a message.
     *
     * @param message
     *         An error message.
     */
    public ProtocolException(String message)
    {
        super(message);
    }


    /**
     * Create a new {@link ProtocolException} with a message and a cause.
     *
     * @param message
     *         An error message.
     * @param cause
     *         The reason for this error.
     */
    public ProtocolException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
