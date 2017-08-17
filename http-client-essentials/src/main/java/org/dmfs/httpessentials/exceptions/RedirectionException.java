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
 * An {@link Exception} that's thrown when the server returns a (unhandled) redirect. <p> There are two subclasses for specific error conditions: {@link
 * TooManyRedirectsException} and {@link RedirectionLoopException}. </p>
 *
 * @author Marten Gajda
 */
public class RedirectionException extends UnexpectedStatusException
{

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 0;

    private final URI mRedirectingLocation;
    private final URI mNewLocation;


    public RedirectionException(HttpStatus status, URI redirectingLocation, URI newLocation)
    {
        super(status);
        mRedirectingLocation = redirectingLocation;
        mNewLocation = newLocation;
    }


    public RedirectionException(HttpStatus status, String message, URI redirectingLocation, URI newLocation)
    {
        super(status, message);
        mRedirectingLocation = redirectingLocation;
        mNewLocation = newLocation;
    }


    /**
     * Returns the {@link URI} of the location that returned the redirect.
     *
     * @return A {@link URI}.
     */
    public URI redirectingLocation()
    {
        return mRedirectingLocation;
    }


    /**
     * Returns the new {@link URI} as returned by the server, may be <code>null</code> for some status codes.
     *
     * @return A {@link URI} or <code>null</code>.
     */
    public URI newLocation()
    {
        return mNewLocation;
    }
}
