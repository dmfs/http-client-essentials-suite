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

import java.net.URI;


/**
 * An {@link Exception} that's thrown when a redirection loop has been detected.
 *
 * @author Marten Gajda
 */
public final class RedirectionLoopException extends RedirectionException
{
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;


    /**
     * Create a new redirection loop exception for the given status, source and destination.
     *
     * @param statusCode
     *         The {@link HttpStatus} of the redirect.
     * @param redirectingLocation
     *         The source of the redirect.
     * @param newLocation
     *         The destination of the redirect.
     */
    public RedirectionLoopException(final HttpStatus statusCode, final URI redirectingLocation, final URI newLocation)
    {
        super(statusCode, redirectingLocation, newLocation);
    }
}
