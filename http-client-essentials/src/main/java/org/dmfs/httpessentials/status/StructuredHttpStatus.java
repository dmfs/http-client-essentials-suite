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

package org.dmfs.httpessentials.status;

import org.dmfs.httpessentials.HttpStatus;


/**
 * A structured {@link HttpStatus}.
 *
 * @author Marten Gajda
 */
public final class StructuredHttpStatus implements HttpStatus
{
    private final int mStatusCode;
    private final String mReasonPhrase;


    /**
     * Creates a {@link StructuredHttpStatus}.
     *
     * @param statusCode
     *         The status code.
     * @param reasonPhrase
     *         The reason phrase of this status, must be non-{@code null}
     */
    public StructuredHttpStatus(final int statusCode, final String reasonPhrase)
    {
        mStatusCode = statusCode;
        mReasonPhrase = reasonPhrase;
    }


    @Override
    public int statusCode()
    {
        return mStatusCode;
    }


    @Override
    public String reason()
    {
        return mReasonPhrase;
    }


    @Override
    public int hashCode()
    {
        return mStatusCode;
    }


    @Override
    public boolean equals(final Object obj)
    {
        return this == obj || obj instanceof HttpStatus && mStatusCode == obj.hashCode();
    }
}
