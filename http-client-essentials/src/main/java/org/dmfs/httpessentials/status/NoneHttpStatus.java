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
 * Represents an {@link HttpStatus} for "no status".
 *
 * @author Marten Gajda
 */
public final class NoneHttpStatus implements HttpStatus
{

    /**
     * Creates an {@link HttpStatus} that represents a non existing status.
     */
    public NoneHttpStatus()
    {
    }


    @Override
    public int statusCode()
    {
        throw new UnsupportedOperationException("NoneHttpStatus has no status code");
    }


    @Override
    public String reason()
    {
        throw new UnsupportedOperationException("NoneHttpStatus has no reason");
    }


    @Override
    public String httpStatusLine(final int httpVersionMajor, final int httpVersionMinor)
    {
        throw new UnsupportedOperationException("Can't generate status line for NoneHttpStatus");
    }


    @Override
    public boolean isInformational()
    {
        return false;
    }


    @Override
    public boolean isSuccess()
    {
        return false;
    }


    @Override
    public boolean isRedirect()
    {
        return false;
    }


    @Override
    public boolean isClientError()
    {
        return false;
    }


    @Override
    public boolean isServerError()
    {
        return false;
    }


    @Override
    public int hashCode()
    {
        // return a hash code that won't match the hash code of any valid status
        return -1;
    }


    @Override
    public boolean equals(final Object obj)
    {
        return obj == this || obj instanceof NoneHttpStatus;
    }


    @Override
    public String toString()
    {
        return "NONE HttpStatus";
    }
}
