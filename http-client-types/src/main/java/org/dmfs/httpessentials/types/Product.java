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

package org.dmfs.httpessentials.types;

/**
 * Represents a product for the User-Agent header.
 *
 * @author Gabor Keszthelyi
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">RFC7231</a>
 */
public interface Product
{

    /**
     * Appends the {@link Product}s string representation as in the User-Agent header value.
     *
     * @param sb
     *         the StringBuilder to append to
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">RFC7231</a>
     */
    void appendTo(StringBuilder sb);

    /**
     * Returns the {@link Product}s string representation as in the User-Agent header value.
     *
     * @return the string representation
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">RFC7231</a>
     */
    String toString();
}
