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
 * Represents type of an {@link Entity}. That is, it binds a type name to a value type.
 *
 * @param <ValueType>
 *         The type of the entity value.
 *
 * @author Marten Gajda
 */
public interface EntityType<ValueType>
{

    /**
     * Returns the name of the type.
     *
     * @return The type name, never <code>null</code>.
     */
    String name();

    /**
     * Factory method to create {@link Entity}s of this type from the given {@link String} representation.
     *
     * @param valueString
     *         The String representation of the entity value.
     *
     * @return A new {@link Entity} instance.
     */
    Entity<ValueType> entityFromString(String valueString);

    /**
     * Convert a string to a value of type &lt;ValueType&gt;
     *
     * @param valueString
     *         The String representation of the entity value.
     *
     * @return A new entity value.
     */
    ValueType valueFromString(String valueString);

    /**
     * Factory method to create {@link Entity}s of this {@link EntityType} from a value.
     *
     * @param value
     *         The entity value, must not be <code>null</code>
     *
     * @return A new {@link Entity} instance.
     */
    Entity<ValueType> entity(ValueType value);

    /**
     * Returns the string representation of the given entity value as defined for this {@link EntityType}.
     *
     * @param value
     *         The value to convert.
     *
     * @return A value String representing the given value.
     */
    String valueString(ValueType value);

}
