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

package org.dmfs.httpessentials.android.apache;

import org.apache.http.StatusLine;
import org.dmfs.httpessentials.HttpStatus;


/**
 * The {@link HttpStatus} of an Apache {@link StatusLine}.
 *
 * @author Marten Gajda
 */
final class ApacheHttpStatus implements HttpStatus
{
    private final StatusLine mStatusLine;


    public ApacheHttpStatus(StatusLine statusLine)
    {
        mStatusLine = statusLine;
    }


    @Override
    public int statusCode()
    {
        return mStatusLine.getStatusCode();
    }


    @Override
    public String reason()
    {
        return mStatusLine.getReasonPhrase();
    }


    @Override
    public boolean isInformational()
    {
        return statusCode() >= 100 && statusCode() < 200;
    }


    @Override
    public boolean isSuccess()
    {
        return statusCode() >= 200 && statusCode() < 300;
    }


    @Override
    public boolean isRedirect()
    {
        return statusCode() >= 300 && statusCode() < 400;
    }


    @Override
    public boolean isClientError()
    {
        return statusCode() >= 400 && statusCode() < 500;
    }


    @Override
    public boolean isServerError()
    {
        return statusCode() >= 500 && statusCode() < 600;
    }
}
