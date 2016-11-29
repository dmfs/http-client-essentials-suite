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

package org.dmfs.httpessentials.headers;

import org.dmfs.httpessentials.typedentity.EntityType;


/**
 * Represents a header type. That is, it binds a header name to a value type.
 * <p>
 * Two {@link HeaderType}s are considered equal if they have the same header name.
 *
 * @param <ValueType>
 *         The type of the header value.
 *
 * @author Marten Gajda
 */
public interface HeaderType<ValueType> extends EntityType<ValueType>
{

    /**
     * Returns the name of the header.
     *
     * @return The header name, never <code>null</code>.
     */
    @Override
    public String name();

    /**
     * Factory method to create {@link Header}s of this type from the given {@link String} representation.
     *
     * @param headerValueString
     *         The String representation of the Header as transferred over the wire.
     *
     * @return A new {@link Header} instance.
     */
    @Override
    public Header<ValueType> entityFromString(String headerValueString);

    /**
     * Factory method to create {@link Header}s of this type from a value.
     *
     * @param value
     *         The value of the header, must not be <code>null</code>
     *
     * @return A new {@link Header} instance.
     */
    @Override
    public Header<ValueType> entity(ValueType value);

    /**
     * Returns the string representation of the given value as defined for this {@link HeaderType}.
     *
     * @param headerValue
     *         The value to convert.
     *
     * @return A header value String representing the given value.
     */
    @Override
    public String valueString(ValueType headerValue);
}
