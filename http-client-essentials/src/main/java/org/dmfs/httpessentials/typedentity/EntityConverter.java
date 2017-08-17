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

package org.dmfs.httpessentials.typedentity;

/**
 * Helper to convert {@link Entity} values to instances of their respective <code>ValueType</code> and back to String representations.
 *
 * @param <ValueType>
 *         The type of the entity value.
 *
 * @author Marten Gajda
 */
public interface EntityConverter<ValueType>
{

    /**
     * Parses the given string representation of a value into an instance of type <code>ValueType</code>.
     *
     * @param valueString
     *         The string representation of the value.
     *
     * @return The value as an object.
     */
    public ValueType value(String valueString);

    /**
     * Renders the given entity value into its string representation.
     *
     * @param value
     *         The value.
     *
     * @return The string representation of the value.
     */
    public String valueString(ValueType value);
}
