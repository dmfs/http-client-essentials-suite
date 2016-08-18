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

package org.dmfs.httpessentials.status;

import org.dmfs.httpessentials.HttpStatus;


/**
 * An {@link HttpStatus} that's derived from an HTTP status line.
 * <p>
 * TODO: validate the given status line
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class StatusLineHttpStatus implements HttpStatus
{
    /**
     * The status line that contains the status.
     */
    private final String mStatusLine;


    /**
     * Creates an HttpStatus object from a given status line
     *
     * @param statusLine
     *            The status line.
     */
    public StatusLineHttpStatus(final String statusLine)
    {
        mStatusLine = statusLine;
    }


    @Override
    public int statusCode()
    {
        // @formatter:off
		/*
		 * According to RFC 7230, a valid HTTP status line always looks like
		 * 
		 * HTTP-Version SP Status-Code SP Reason-Phrase CRLF
		 * 
		 * Where HTTP-Version always has the format HTTP "/" DIGIT "." DIGIT and Status-Code is always a 3 digit decimal value. That means the status code is
		 * always at the same position:
		 * 
		 * HTTP/x.x xxx xxxxxxxxxxxxxx
		 *          ^^^       Position of status code
		 * 0123456789ab       Hex character index
		 * 
		 * Since the status code is always in ASCII digits and of fixed length, we us the (ugly) fast path to convert it to an int (and avoid one short lived
		 * String object).
		 */
		// @formatter:on
        return ((mStatusLine.charAt(0x09) - '0') * 10 + (mStatusLine.charAt(0x0a) - '0')) * 10 + mStatusLine.charAt(
                0x0b) - '0';
    }


    @Override
    public String reason()
    {
        // @formatter:off
		/*
		 * According to RFC 7230, a valid HTTP status line always looks like
		 * 
		 * HTTP-Version SP Status-Code SP Reason-Phrase CRLF
		 * 
		 * Where HTTP-Version always has the format HTTP "/" DIGIT "." DIGIT and Status-Code is always a 3 digit decimal value. That means the status code is
		 * always at the same position:
		 * 
		 * HTTP/x.x xxx xxxxxxxxxxxxxx
		 *              ^        Start of reason phrase
		 * 0123456789abcd        Hex character index
		 * 
		 * The reason phrase always starts at index 0x0d
		 * 
		 */
		// @formatter:on
        return mStatusLine.substring(0x0d);
    }


    @Override
    public String httpStatusLine(final int httpVersionMajor, final int httpVersionMinor)
    {
        if (httpVersionMajor == mStatusLine.charAt(0x05) - '0' && httpVersionMinor == mStatusLine.charAt(0x07) - '0')
        {
            // version numbers match, so just return the original line
            return mStatusLine;
        }

        return String.format("HTTP/%s.%s %d %s", httpVersionMajor, httpVersionMinor, statusCode(), reason());
    }


    @Override
    public boolean isInformational()
    {
        return mStatusLine.charAt(0x09) == '1';
    }


    @Override
    public boolean isSuccess()
    {
        return mStatusLine.charAt(0x09) == '2';
    }


    @Override
    public boolean isRedirect()
    {
        return mStatusLine.charAt(0x09) == '3';
    }


    @Override
    public boolean isClientError()
    {
        return mStatusLine.charAt(0x09) == '4';
    }


    @Override
    public boolean isServerError()
    {
        return mStatusLine.charAt(0x09) == '5';
    }


    @Override
    public int hashCode()
    {
        return statusCode();
    }


    @Override
    public boolean equals(final Object obj)
    {
        return this == obj || obj instanceof HttpStatus && statusCode() == obj.hashCode();
    }
}
