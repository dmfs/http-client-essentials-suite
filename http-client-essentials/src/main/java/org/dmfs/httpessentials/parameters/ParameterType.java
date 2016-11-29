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

package org.dmfs.httpessentials.parameters;

import org.dmfs.httpessentials.typedentity.EntityType;


/**
 * Represents the type of a parameter. That is, it binds a parameter name to a value type.
 * <p>
 * Two {@link ParameterType}s are considered to equal if they have the same header name.
 *
 * @param <ValueType>
 *         The type of the parameter value.
 *
 * @author Marten Gajda
 */
public interface ParameterType<ValueType> extends EntityType<ValueType>
{

    /**
     * Returns the name of the parameter.
     *
     * @return The parameter name, never <code>null</code>.
     */
    @Override
    public String name();

    /**
     * Factory method to create {@link Parameter}s of this type from the given {@link String} representation.
     *
     * @param parameterValueString
     *         The String representation of the Parameter value.
     *
     * @return A new {@link Parameter} instance.
     */
    @Override
    public Parameter<ValueType> entityFromString(String parameterValueString);

    /**
     * Factory method to create {@link Parameter}s of this type from a value.
     *
     * @param value
     *         The value of the parameter, must not be <code>null</code>
     *
     * @return A new {@link Parameter} instance.
     */
    @Override
    public Parameter<ValueType> entity(ValueType value);

    /**
     * Returns the string representation of the given parameter value as defined for this {@link Parameter}.
     *
     * @param headerValue
     *         The value to convert.
     *
     * @return A value String representing the given value.
     */
    @Override
    public String valueString(ValueType headerValue);

}
