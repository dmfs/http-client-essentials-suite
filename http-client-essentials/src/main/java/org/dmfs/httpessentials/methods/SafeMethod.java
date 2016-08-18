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

package org.dmfs.httpessentials.methods;

import org.dmfs.httpessentials.HttpMethod;


/**
 * A safe {@link HttpMethod}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.1">RFC 7231, section 4.2.1</a>
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.2">RFC 7231, section 4.2.2</a>
 */
public final class SafeMethod extends AbstractMethod
{
    public SafeMethod(final String verb, final boolean supportsRequestPayload)
    {
        super(verb, supportsRequestPayload);
    }


    @Override
    public boolean isSafe()
    {
        // safe methods are safe
        return true;
    }


    @Override
    public boolean isIdempotent()
    {
        // safe methods are idempotent
        return true;
    }
}
