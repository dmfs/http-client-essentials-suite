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

package org.dmfs.httpessentials.status;

import org.dmfs.httpessentials.HttpStatus;


/**
 * Simple implementation of an {@link HttpStatus}.
 *
 * @author Marten Gajda
 */
public final class SimpleHttpStatus implements HttpStatus
{
    /**
     * The actual status code.
     */
    private final int statusCode;

    /**
     * The reason phrase of this status.
     */
    private final String reasonPhrase;


    /**
     * Initialize an HttpStatus object.
     *
     * @param statusCode
     *         The status code.
     * @param reasonPhrase
     *         The reason phrase of this status.
     */
    public SimpleHttpStatus(final int statusCode, final String reasonPhrase)
    {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }


    @Override
    public int statusCode()
    {
        return statusCode;
    }


    @Override
    public String reason()
    {
        return reasonPhrase;
    }


    @Override
    public String httpStatusLine(final int httpVersionMajor, final int httpVersionMinor)
    {
        return String.format("HTTP/%s.%s %d %s", httpVersionMajor, httpVersionMinor, statusCode, reasonPhrase);
    }


    @Override
    public boolean isInformational()
    {
        return statusCode >= 100 && statusCode < 200;
    }


    @Override
    public boolean isSuccess()
    {
        return statusCode >= 200 && statusCode < 300;
    }


    @Override
    public boolean isRedirect()
    {
        return statusCode >= 300 && statusCode < 400;
    }


    @Override
    public boolean isClientError()
    {
        return statusCode >= 400 && statusCode < 500;
    }


    @Override
    public boolean isServerError()
    {
        return statusCode >= 500 && statusCode < 600;
    }


    @Override
    public int hashCode()
    {
        return statusCode;
    }


    @Override
    public boolean equals(final Object obj)
    {
        return this == obj || obj instanceof HttpStatus && statusCode == obj.hashCode();
    }
}
