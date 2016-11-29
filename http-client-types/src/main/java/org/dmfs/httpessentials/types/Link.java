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

package org.dmfs.httpessentials.types;

import org.dmfs.httpessentials.parameters.Parametrized;

import java.net.URI;
import java.util.Locale;
import java.util.Set;


/**
 * Interface of a link value as specified in <a href="https://tools.ietf.org/html/rfc5988">RFC 5988</a>
 *
 * @author Marten Gajda
 */
public interface Link extends Parametrized
{

    /**
     * Returns the actual target {@link URI} of this Link.
     *
     * @return
     */
    public URI target();

    /**
     * Returns the context of the link (the value of the anchor parameter, if present). If the anchor is a relative URI, it will be resolved relatively to the
     * provided default context {@link URI}.
     *
     * @param defaultContext
     *         The current context to return if no anchor is given.
     *
     * @return The anchor {@link URI} or <code>context</code> if there is no anchor.
     */
    public URI context(URI defaultContext);

    /**
     * Return a {@link Set} of all hreflang values present in the link.
     *
     * @return A {@link Set} of {@link Locale}s, may be empty, never <code>null</code>.
     */
    public Set<Locale> languages();

    /**
     * Returns the value of the title parameter, if there is any or <code>null</code> if there is none.
     *
     * @return The title of the link.
     */
    public String title();

    /**
     * Returns the media-type of the link target.
     *
     * @return A {@link MediaType} or <code>null</code> if there is no media-type parameter.
     */
    public MediaType mediaType();

    /**
     * Returns the set of relation types of this link.
     * <p>
     * Note: According to <a href="https://tools.ietf.org/html/rfc5988#section-5">RFC 5988, Section 5</a> relation types can be Strings or URIs. This
     * implementation returns all relation types as strings, since it's mostly used as an opaque token. The downside of this is that extended relation types may
     * have different string representations due to different encodings, and may be difficult to match.
     *
     * @return
     */
    public Set<String> relationTypes();

    /**
     * Returns the set of reverse relation types of this link.
     * <p>
     * Note: According to <a href="https://tools.ietf.org/html/rfc5988#section-5">RFC 5988, Section 5</a> relation types can be Strings or URIs. This
     * implementation returns all relation types as strings, since it's mostly used as an opaque token. The downside of this is that extended relation types may
     * have different string representations due to different encodings, and may be difficult to match.
     *
     * @return
     */
    public Set<String> reverseRelationTypes();
}
