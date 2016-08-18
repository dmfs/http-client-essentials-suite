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

package org.dmfs.httpessentials.converters;

import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * An {@link EntityConverter} for header string values. It returns the header string after trimming any leading or
 * trailing white space characters (since in general they do not belong to the value, see <a
 * href="https://tools.ietf.org/html/rfc7230#section-3.2">RFC 7230, Section 3.2</a>).
 * <p/>
 * <a href="https://tools.ietf.org/html/rfc2047">RFC 2047</a> is not supported by this implementation.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class PlainStringHeaderConverter implements EntityConverter<String>
{
    public final static PlainStringHeaderConverter INSTANCE = new PlainStringHeaderConverter();


    @Override
    public String value(String headerValueString)
    {
        return headerValueString.trim();
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The given headerValue must not contain any characters that are not allowed in headers. Basically that means only
     * characters in the ASCII range 0x20-0xff and the tab character (0x09) are allowed.
     * <p>
     * <pre>
     *    field-value    = *( field-content / obs-fold )
     *    field-content  = field-vchar [ 1*( SP / HTAB ) field-vchar ]
     *    field-vchar    = VCHAR / obs-text
     *
     *    obs-fold       = CRLF 1*( SP / HTAB )
     *                     ; obsolete line folding
     *                     ; see Section 3.2.4
     *    VCHAR          = %x21-7E
     *                           ; visible (printing) characters
     *    obs-text       = %x80-FF
     * </pre>
     */
    @Override
    public String valueString(String headerValue)
    {
        // make sure the value is valid
        for (int i = 0, count = headerValue.length(); i < count; ++i)
        {
            char c = headerValue.charAt(i);
            if ((c < 0x20 || c > 0xff) && c != 0x09)
            {
                throw new IllegalArgumentException(String.format(
                        "String '%s' contains non-printable or non-ASCII characters, which is not allowed in headers",
                        headerValue));
            }
        }
        return headerValue;
    }
}
