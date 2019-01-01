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

package org.dmfs.httpessentials.parameters;

import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * Basic {@link ParameterType} implementation.
 *
 * @param <ValueType>
 *         The type of the parameter value.
 *
 * @author Marten Gajda
 */
public final class BasicParameterType<ValueType> implements ParameterType<ValueType>
{
    private final String mName;
    private final EntityConverter<ValueType> mConverter;


    /**
     * Creates a {@link ParameterType} for the given parameter name using the given {@link EntityConverter}.
     *
     * @param name
     *         The name of the parameter.
     * @param converter
     *         A {@link EntityConverter} to convert parameter values from/to string.
     */
    public BasicParameterType(String name, EntityConverter<ValueType> converter)
    {
        mName = name;
        mConverter = converter;
    }


    @Override
    public String name()
    {
        return mName;
    }


    @Override
    public Parameter<ValueType> entityFromString(String valueString)
    {
        return new BasicParameter<>(this, valueFromString(valueString));
    }


    @Override
    public ValueType valueFromString(String valueString)
    {
        return mConverter.value(valueString);
    }


    @Override
    public Parameter<ValueType> entity(ValueType value)
    {
        return new BasicParameter<>(this, value);
    }


    @Override
    public String valueString(ValueType value)
    {
        return mConverter.valueString(value);
    }


    @Override
    public int hashCode()
    {
        return mName.hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        return this == obj || (obj instanceof ParameterType && mName.equalsIgnoreCase(
                (((ParameterType<?>) obj).name())));
    }

}
