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

package org.dmfs.httpessentials.methods;

import org.dmfs.httpessentials.HttpMethod;


/**
 * A {@link HttpMethod} that's neither safe nor idempotent. Use {@link SafeMethod} or {@link IdempotentMethod} to create one of these.
 *
 * @author Marten Gajda
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.1">RFC 7231, section 4.2.1</a>
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.2">RFC 7231, section 4.2.2</a>
 */
public final class Method extends AbstractMethod
{
    public Method(final String verb, final boolean supportsRequestPayload)
    {
        super(verb, supportsRequestPayload);
    }


    @Override
    public boolean isSafe()
    {
        return false;
    }


    @Override
    public boolean isIdempotent()
    {
        return false;
    }

}
