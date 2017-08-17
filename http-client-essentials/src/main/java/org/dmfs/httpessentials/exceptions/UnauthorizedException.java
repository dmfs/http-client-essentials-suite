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
 * This Exception is thrown when an unhandled {@link HttpStatus#UNAUTHORIZED} status code occurs.
 *
 * @author Marten Gajda
 * @see <a href="http://tools.ietf.org/html/rfc7235#section-3.1">RFC 7235, section 3.1</a>
 */
public class UnauthorizedException extends ClientErrorException
{

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link UnauthorizedException}.
     */
    public UnauthorizedException()
    {
        super(HttpStatus.UNAUTHORIZED);
    }


    /**
     * Create a new {@link UnauthorizedException} with a message.
     *
     * @param message
     *         An error message.
     */
    public UnauthorizedException(String message)
    {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
