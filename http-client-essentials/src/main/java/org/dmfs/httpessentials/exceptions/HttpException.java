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

package org.dmfs.httpessentials.exceptions;

import java.io.IOException;


/**
 * An {@link Exception} that's thrown when an error at HTTP level occurred. Some implementations might choose to throw
 * an {@link IOException} or a subclass of it instead of this one.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public class HttpException extends IOException
{

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new {@link HttpException} with a message.
     *
     * @param message
     *         An error message.
     */
    public HttpException(String message)
    {
        super(message);
    }


    /**
     * Create a new {@link HttpException} with a message and a cause.
     *
     * @param message
     *         An error message.
     * @param cause
     *         The reaons for this error.
     */
    public HttpException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
