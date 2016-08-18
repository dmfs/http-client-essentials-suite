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

package org.dmfs.httpessentials.types;

import org.dmfs.httpessentials.parameters.Parametrized;


/**
 * Interface of media types as defined in <a href="http://tools.ietf.org/html/rfc2045#section-5">RFC 2045, Section 5</a>
 * and <a href="https://tools.ietf.org/html/rfc7231#section-3.1.1.1">RFC 7231, Section 3.1.1.1</a>
 * <p>
 * <p/>
 * By contract {@link MediaType}s are equal if their main and subtype are equal (using case-insensitive comparison). So
 * parameters like <code>charset</code> are not taken into account for comparison. Also {@link #hashCode()} must return
 * a values that's computed as
 * <p>
 * <pre>
 * {@code
 * return type().toLower(Locale.ENGLISH).hashCode()*31 + subType.toLower(Locale.ENGLISH).hashCode();
 * }
 * </pre>
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface MediaType extends Parametrized
{

    /**
     * Returns the content type in the form <code>&lt;maintype>/&lt;subtype></code>.
     *
     * @return The content type.
     */
    public String type();

    /**
     * Returns the main type.
     *
     * @return The main type.
     */
    public String mainType();

    /**
     * Returns the sub-type of this content type.
     *
     * @return The sub-type.
     */
    public String subType();

    /**
     * Returns the value of the charset parameter of this content type.
     *
     * @param defaultCharset
     *         The charset to return if no charset parameter is present.
     *
     * @return The charset of <code>defaultCharset</code>
     */
    public String charset(String defaultCharset);
}
