/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.types;

/**
 * Represents a product for the User-Agent header. See <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">RFC7231</a>.
 * <p>
 * (Note that opposed to the RFC, comment is part of the product here. Also, the definition is constrained here: only 1
 * comment can belong to a product, and this cannot represent a standalone comment.)
 *
 * @author Gabor Keszthelyi
 */
public interface Product
{

    /**
     * Returns the name of the product.
     *
     * @return the name, never null
     */
    Token name();

    /**
     * Returns the version of the product.
     *
     * @return the version, or null when there is no version
     */
    Token version();

    /**
     * The comment for this product.
     *
     * @return the comment, or null when there is no comment
     */
    Comment comment();

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
